package whu.dellinet.rssreader.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.dreamteam.custom.ui.PullToRefreshListView;
import com.dreamteam.custom.ui.PullToRefreshListView.OnRefreshListener;

import whu.dellinet.rssreader.R;
import whu.dellinet.rssreader.adapter.ItemListAdapter;
import whu.dellinet.rssreader.custom.AppContext;
import whu.dellinet.rssreader.custom.ItemsParaser;
import whu.dellinet.rssreader.custom.SectionHelper;
import whu.dellinet.rssreader.custom.SerialHelper;
import whu.dellinet.rssreader.custom.UIHelper;
import whu.dellinet.rssreader.entity.FeedItem;
import whu.dellinet.rssreader.entity.ItemListEntity;
import android.R.bool;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ItemListActivity extends Activity{
	public static final String TAG="ItemListActivity";
	public static final String ACTION_LISTITEM_UPDATE="whu.dellinet.rssreader.listitemupdate";
	
	private PullToRefreshListView refreshListView;
	private TextView feedtitleView;
	private String sectionUrl;
	private String sectionTitle;
	private ItemListAdapter itemListAdapter;
	private List<FeedItem> feedItems;
	private BroadcastReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initBroadcast();
	}
	private void initView() {
		UIHelper.initTheme(this);
		setContentView(R.layout.feed_item_list);
		refreshListView=(PullToRefreshListView) findViewById(R.id.fil_refreshlv);
		feedtitleView=(TextView) findViewById(R.id.fil_feed_title);
		refreshListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if (!AppContext.isNewworkAvailable(ItemListActivity.this)) {
					Toast.makeText(ItemListActivity.this, "网络不可用", Toast.LENGTH_LONG).show();
					return;
				}else {
					RefreshTask refreshTask=new RefreshTask();
					refreshTask.execute(sectionUrl);
				}
			}
		});
		refreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.i(TAG, "position: "+arg2);
				//position 要从1算起？
				FeedItem feedItem=feedItems.get(arg2-1);
				final String link=feedItem.getLink();
				if (!feedItem.isReaded()) {
					feedItem.setReaded(true);
					itemListAdapter.notifyDataSetChanged();
					//讲已读状态写入缓存中
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//多线程？read状态可能还没更改？
							ItemListEntity newItemListEntity=new ItemListEntity();
							SerialHelper serialHelper=SerialHelper.getInstance();
							File sectionCache=SectionHelper.getSectionCache(sectionUrl);
							for (FeedItem feedItem : feedItems) {
								if (feedItem.getLink().equals(link)) {
									feedItem.setReaded(true);
								}
							}
							newItemListEntity.setItemsList(feedItems);
							serialHelper.saveObject(newItemListEntity, sectionCache);
						}
					}).start();
				}
				Intent intent=new Intent(ItemListActivity.this, ItemDetailActivity.class);
				String title=feedItem.getTitle();
				String pubDate=feedItem.getPubdate();
				String content=feedItem.getContent();
				boolean isFavor=feedItem.isFavorite();
				String firstImgUrl=feedItem.getFirstImageUrl();
				intent.putExtra("sectionUrl", sectionUrl);
				intent.putExtra("sectionTitle", sectionTitle);
				intent.putExtra("title", title);
				intent.putExtra("link", link);
				intent.putExtra("pubDate", pubDate);
				intent.putExtra("firstImgUrl", firstImgUrl);
				intent.putExtra("content", content);
				intent.putExtra("isFavor", isFavor);
				
				ItemListActivity.this.startActivity(intent);
				
				
				
			}
		});
	}
	private void initData() {
		Intent intent=getIntent();
		sectionUrl=intent.getStringExtra("url");
		Log.i(TAG, "url: "+sectionUrl);
		sectionTitle=intent.getStringExtra("title");
		Log.i(TAG, "title: "+sectionTitle);
		feedtitleView.setText(sectionTitle);
		File sectionCache=SectionHelper.getSectionCache(sectionUrl);
		if (sectionCache.exists()) {
			SerialHelper serialHelper=SerialHelper.getInstance();
			ItemListEntity itemListEntity=(ItemListEntity) serialHelper.readObject(sectionCache);
			feedItems=itemListEntity.getItemsList();
			Log.i(TAG, "条目数量："+feedItems.size());
			if ((feedItems!=null)&&(feedItems.size()>0)) {
				itemListAdapter=new ItemListAdapter(ItemListActivity.this, feedItems);
				refreshListView.setAdapter(itemListAdapter);
			}
		}
		
	}
	
	private void initBroadcast() {
		receiver=new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String link=intent.getStringExtra("link");
				boolean isFavor=intent.getBooleanExtra("isFavor", false);
				for (FeedItem feedItem : feedItems) {
					if (feedItem.getLink().equals(link)) {
						feedItem.setFavorite(isFavor);
						break;
					}
				}
				
			}
		};
		IntentFilter intentFilter=new IntentFilter(ACTION_LISTITEM_UPDATE);
		registerReceiver(receiver, intentFilter);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		super.onDestroy();
		
	}
	
	private class RefreshTask extends AsyncTask<String, Integer, ItemListEntity> {

		@Override
		protected ItemListEntity doInBackground(String... params) {
			// TODO Auto-generated method stub
			ItemsParaser paraser=new ItemsParaser();
			return paraser.paraser(sectionUrl);
		}

		@Override
		protected void onPostExecute(ItemListEntity result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result==null) {
				Toast.makeText(ItemListActivity.this, "网络异常", Toast.LENGTH_LONG).show();
				return;
			}
			List<FeedItem> newItems=new ArrayList<FeedItem>();
			File sectionCache=SectionHelper.getSectionCache(sectionUrl);
			SerialHelper serialHelper=SerialHelper.getInstance();
			List<FeedItem> feedItems=result.getItemsList();
			ItemListEntity oldItemListEntity=(ItemListEntity) serialHelper.readObject(sectionCache);
			String oldFirstDate=oldItemListEntity.getItemsList().get(0).getPubdate();
			int newCount=0;
			for (FeedItem newFeedItem : feedItems) {
				if (newFeedItem.getPubdate().equals(oldFirstDate)) {
					refreshListView.onRefreshComplete();
					Toast.makeText(ItemListActivity.this, "暂无更新", Toast.LENGTH_LONG).show();
					return;
				}
				newCount++;
				newItems.add(newFeedItem);
			}
			//保存并更新Section缓存
			serialHelper.saveObject(result, sectionCache);
			//更新追加新的Item
			itemListAdapter.addItemsToHead(newItems);
			Toast.makeText(ItemListActivity.this, "更新了"+newCount+"条", Toast.LENGTH_LONG).show();
			refreshListView.onRefreshComplete();
			
		}
		
	}
}
