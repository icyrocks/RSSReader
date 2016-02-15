package whu.dellinet.rssreader.ui;

import java.io.File;
import java.util.List;

import whu.dellinet.rssreader.R;
import whu.dellinet.rssreader.custom.AppContext;
import whu.dellinet.rssreader.custom.FavorsHelper;
import whu.dellinet.rssreader.custom.HtmlFilter;
import whu.dellinet.rssreader.custom.SerialHelper;
import whu.dellinet.rssreader.custom.UIHelper;
import whu.dellinet.rssreader.entity.FeedItem;
import whu.dellinet.rssreader.entity.ItemListEntity;
import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Toast;

public class ItemDetailActivity extends FragmentActivity {
	
	public static final String TAG="ItemDetailActivity";
	private static WebView webView;
	private boolean isNight;
	
	private int[] favorIcons={R.drawable.btn_favorite_empty,R.drawable.btn_favorite_full};
	private ImageButton favorButton;
	
	private String sectionTitle;
	private String sectionUrl;
	private String title;
	private String link;
	private String pubDate;
	private String firstImgUrl;
	private String itemDetail;
	private boolean isFavor;
	
	private boolean isFromFavor=false;
	
	private StringBuffer buffer = new StringBuffer();
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initView();
		initData();
	}
	private void initView() {
		SharedPreferences sp=AppContext.getSharedPreferences(this);
		isNight=sp.getBoolean("night_mode", false);
		if (isNight) {
			this.setTheme(R.style.AppNightTheme);
			favorIcons=new int[]{R.drawable.btn_favorite_empty_night,R.drawable.btn_favorite_full_night};
		}
		isFavor=getIntent().getBooleanExtra("isFavor", false);
		setContentView(R.layout.feed_item_detail);
		favorButton=(ImageButton) findViewById(R.id.fid_btn_favor);
		if (isFavor) {
			favorButton.setImageResource(favorIcons[1]);
		}else {
			favorButton.setImageResource(favorIcons[0]);
		}
		favorButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isFavor) {
					isFavor=false;
					Toast.makeText(ItemDetailActivity.this, "您取消了收藏", Toast.LENGTH_LONG).show();
//					favorButton.setBackgroundResource(favorIcons[0]);
					favorButton.setImageResource(favorIcons[0]);
					
					FavorsHelper.removeFavor(ItemDetailActivity.this, link);
				}else {
					isFavor=true;
					Toast.makeText(ItemDetailActivity.this, "收藏成功", Toast.LENGTH_LONG).show();
//					favorButton.setBackgroundResource(favorIcons[1]);
					favorButton.setImageResource(favorIcons[1]);
					FavorsHelper.insertFavor(ItemDetailActivity.this, title, link, pubDate, itemDetail, firstImgUrl, sectionTitle);
				}
				
				if (isFromFavor) {
					return;
				}
				
				Intent intent=new Intent();
				intent.putExtra("link", link);
				intent.putExtra("isFavor", isFavor);
				intent.setAction(ItemListActivity.ACTION_LISTITEM_UPDATE);
				sendBroadcast(intent);
				//保存收藏状态至文件中
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						SerialHelper serialHelper=SerialHelper.getInstance();
						File file=AppContext.getSectionCache(sectionUrl);
						Log.i(TAG, "section的文件缓存是否为空？"+file.exists());
						ItemListEntity listEntity=(ItemListEntity) serialHelper.readObject(file);
						Log.i(TAG, "通过反序列化得到的ItemListEntity是否为空？ "+(listEntity==null));
						
						List<FeedItem> feedItems=listEntity.getItemsList();
						for (FeedItem feedItem : feedItems) {
							if (feedItem.getLink().equals(link)) {
								feedItem.setFavorite(isFavor);
								break;
							}
						}
						serialHelper.saveObject(listEntity, file);
						
					}
				}).start();
			}
		});
		webView=(WebView) findViewById(R.id.my_web_view);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.getSettings().setJavaScriptEnabled(true);
	}
	private void initData() {
		Intent intent=getIntent();
		
		isFromFavor=intent.getBooleanExtra("isFromFavor", false);
		
		sectionTitle=intent.getStringExtra("sectionTitle");
		sectionUrl=intent.getStringExtra("sectionUrl");
		title=intent.getStringExtra("title");
		link=intent.getStringExtra("link");
		firstImgUrl=intent.getStringExtra("firstImgUrl");
		pubDate=intent.getStringExtra("pubDate");
		itemDetail=intent.getStringExtra("content");
		isFavor=intent.getBooleanExtra("isFavor", false);
		
		Log.i(TAG, "过滤前content内容为："+itemDetail);
		Log.i(TAG, "过滤前STYLEREGEX正则式： "+HtmlFilter.STYLEREGEX);
		//对内容进行过滤
		itemDetail=itemDetail.replaceAll(HtmlFilter.STYLEREGEX, "");
		itemDetail=itemDetail.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
		itemDetail=itemDetail.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
		
		SharedPreferences sp=AppContext.getSharedPreferences(ItemDetailActivity.this);
		boolean loadImg=sp.getBoolean("imgLoad", false);
		if (!loadImg) {
			itemDetail=itemDetail.replaceAll(HtmlFilter.IMGREGEX, "");
		}
		buffer.append("<h1>"+title+"</h1>");
		buffer.append("<body>"+itemDetail+"</body>");
		
		boolean isNight=AppContext.getSharedPreferences(ItemDetailActivity.this).getBoolean("night_mode", false);
		if (isNight) {
//			webView.setBackgroundColor(Color.GRAY);
//			webView.setBackgroundColor(color.darker_gray);
			webView.setBackgroundColor(Color.DKGRAY);
		}
		webView.loadDataWithBaseURL(null, buffer.toString(), "text/html", "UTF-8", null);
		
	}
}
