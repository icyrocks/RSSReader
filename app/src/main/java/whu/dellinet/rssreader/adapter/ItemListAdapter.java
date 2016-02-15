package whu.dellinet.rssreader.adapter;

import java.util.ArrayList;
import java.util.List;

import whu.dellinet.rssreader.R;
import whu.dellinet.rssreader.R.id;
import whu.dellinet.rssreader.custom.AppContext;
import whu.dellinet.rssreader.custom.ImgLoader;
import whu.dellinet.rssreader.entity.FeedItem;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListAdapter extends BaseAdapter{
	public static final String TAG="ItemListAdapter";
	private Context context;
	private List<FeedItem> feedItems;
	private boolean loadImg;
	private boolean isNight;
	private static int[] colors;
	private LayoutInflater inflater;
	private ImgLoader imgLoader=new ImgLoader();
	private String firstImgUrl=null;
	
	Bitmap defBitmap=null;
	
	
	public ItemListAdapter(Context mContext,List<FeedItem> mFeedItems) {
		// TODO Auto-generated constructor stub
		context=mContext;
		feedItems=mFeedItems;
		inflater=LayoutInflater.from(context);
		defBitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.loading_default);
		imgLoader.setDefBitmap(defBitmap);
		Resources resources=context.getResources();
		colors=new int[]{resources.getColor(R.color.black),resources.getColor(R.color.dark_gray)};
		SharedPreferences sp=AppContext.getSharedPreferences(context);
		boolean isNight=sp.getBoolean("night_mode", false);
		if (isNight) {
			colors=new int[]{resources.getColor(R.color.white),resources.getColor(R.color.gray)};
		}
		loadImg=sp.getBoolean("imgLoad", false);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return feedItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return feedItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if (convertView==null) {
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.feed_item, null);
			viewHolder.imageView=(ImageView) convertView.findViewById(R.id.feeditem_image);
			viewHolder.titleView=(TextView) convertView.findViewById(R.id.feeditem_title);
			viewHolder.pubdateView=(TextView) convertView.findViewById(R.id.feeditem_pubdate);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		FeedItem feedItem=feedItems.get(position);
		String title=feedItem.getTitle();
		String pubDate=feedItem.getPubdate();
//		Log.i(TAG, "feeditem title: "+title);
//		Log.i(TAG, "pubDate : "+pubDate);
		
		if (title.length()>30) {
			title=title.substring(0, 30)+"...";
		}
		int colorState=0;
		if (feedItem.isReaded()) {
			colorState=1;
		}
		viewHolder.titleView.setTextColor(colors[colorState]);
		viewHolder.titleView.setText(title);
		viewHolder.pubdateView.setText(feedItem.getPubdate());
		
		firstImgUrl=feedItem.getFirstImageUrl();
		
		//≈–∂œ «∑Òœ‘ æÕº±Í
		if (firstImgUrl==null||!loadImg) {
			viewHolder.imageView.setVisibility(View.GONE);
		}else {
			viewHolder.imageView.setVisibility(View.VISIBLE);
			viewHolder.imageView.setImageBitmap(defBitmap);
			String firstImgUrl=feedItem.getFirstImageUrl();
			
			Log.i(TAG, "firstImgUrl : "+firstImgUrl);
			
			imgLoader.loadImg(firstImgUrl, viewHolder.imageView, 100, 100);
		}
		return convertView;
	}
	
	
	public void addItemsToHead(List<FeedItem> newItems){
		feedItems.addAll(0, newItems);
		notifyDataSetChanged();
	}
	
	private static final class ViewHolder{
		private ImageView imageView;
		private TextView titleView;
		private TextView pubdateView;
	}
}
