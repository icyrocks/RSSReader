package whu.dellinet.rssreader.adapter;

import java.util.List;

import whu.dellinet.rssreader.R;
import whu.dellinet.rssreader.custom.SectionHelper;
import whu.dellinet.rssreader.db.FeedsDBHelper;
import whu.dellinet.rssreader.entity.Feed;
import whu.dellinet.rssreader.ui.HomeActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class CategoryDetailAdapter extends BaseAdapter{
	private Context context;
	private List<Feed> feeds;
	private LayoutInflater inflater;
	private String tableName;
	private int[] addIcons={R.drawable.add,R.drawable.added};
	
	public CategoryDetailAdapter(Context context,List<Feed> feeds,String tableName) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.feeds=feeds;
		this.tableName=tableName;
		inflater=LayoutInflater.from(this.context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return feeds.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return feeds.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView==null) {
			convertView=inflater.inflate(R.layout.category_detail_item, null);
			holder=new ViewHolder();
			holder.textView=(TextView) convertView.findViewById(R.id.category_detail_feed_title);
			holder.imageButton=(ImageButton) convertView.findViewById(R.id.category_detail_add);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		
		
		Feed feed=feeds.get(position);
		holder.textView.setText(feed.getTitle());
		holder.imageButton.setImageResource(addIcons[feed.getSelectState()]);
		holder.imageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				Feed feed=feeds.get(position);
				String url=feed.getUrl();
				String title=feed.getTitle();
				int selectState=feed.getSelectState();
				if (selectState==0) {
					selectState=1;
					holder.imageButton.setImageResource(addIcons[selectState]);
					intent.setAction(HomeActivity.ACTION_ADD_SECTION);
					SectionHelper.insertRecord(context, title, url, tableName);
				}else if (selectState==1) {
					selectState=0;
					holder.imageButton.setImageResource(addIcons[selectState]);
					intent.setAction(HomeActivity.ACTION_DELETE_SECTION);
					SectionHelper.removeRecord(context, url);
				}
				FeedsDBHelper feedsDBHelper=new FeedsDBHelper(context, FeedsDBHelper.DB_NAME, null, 1);
				feedsDBHelper.updateState(tableName, url, selectState);
				intent.putExtra("url", url);
				context.sendBroadcast(intent);
				feed.setSelectState(selectState);
			}
		});
		return convertView;
	}
	
	private static final class ViewHolder{
		private TextView textView;
		private ImageButton imageButton;
	}

}
