package whu.dellinet.rssreader.adapter;

import java.util.List;

import whu.dellinet.rssreader.R;
import whu.dellinet.rssreader.custom.AppContext;
import whu.dellinet.rssreader.custom.SectionHelper;
import whu.dellinet.rssreader.db.FeedsDBHelper;
import whu.dellinet.rssreader.entity.Section;
import whu.dellinet.rssreader.ui.HomeActivity;
import whu.dellinet.rssreader.util.FileUtils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {
	
	public static final String TAG="GridAdapter";
	private Context context;
	private List<Section> sections;
	private int isVisible=0;
	private int[] visibleStates={View.GONE,View.VISIBLE};
	
	public GridAdapter(Context mContext,List<Section> mSections) {
		// TODO Auto-generated constructor stub
		context=mContext;
		sections=mSections;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sections.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return sections.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView==null) {
			LayoutInflater inflater=LayoutInflater.from(context);
			convertView=inflater.inflate(R.layout.section_item, null);
			viewHolder=new ViewHolder();
			viewHolder.delView=(ImageView) convertView.findViewById(R.id.item_btn_delete);
			viewHolder.itemTitle=(TextView) convertView.findViewById(R.id.item_text);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		final Section section=sections.get(position);
		viewHolder.itemTitle.setText(section.getTitle());
		viewHolder.delView.setOnClickListener(new OnClickListener() {
			
			@Override
			//删除section的内容和缓存
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String url=section.getUrl();
				final String tableName=section.getTableName();
				final String title=section.getTitle();
				Log.i(TAG, "将要删除的section的url : "+url);
				Log.i(TAG, "将要删除的section的标题 : "+title);
				Log.i(TAG, "将要删除的section的类别 : "+tableName);
				
				Intent intent=new Intent();
				intent.setAction(HomeActivity.ACTION_DELETE_SECTION);
				intent.putExtra("url", url);
				context.sendBroadcast(intent);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						SectionHelper.removeRecord(context, url);
						new FeedsDBHelper(context, FeedsDBHelper.DB_NAME, null, 1).updateState(tableName, url, 0);
						FileUtils.deleteDirectory(AppContext.getSectionCache(url).getAbsolutePath());
					}
				}).start();
			}
		});
		viewHolder.delView.setVisibility(visibleStates[isVisible]);
		return convertView;
	}
	public void addItem(Section section) {
		sections.add(section);
		notifyDataSetChanged();
	}
	public boolean removeItem(String url) {
		for (int i = 0; i <sections.size() ; i++) {
			Section section=sections.get(i);
			if (section.getUrl().equals(url)) {
				
				
				sections.remove(i);
				
				Log.i(TAG, "已删除的section的url为："+url);
				notifyDataSetChanged();
				return true;
			}
		}
		return false;
	}
	public void changeDelState(int state) {
		this.isVisible=state;
		notifyDataSetChanged();
	}
	public boolean isEmpty() {
		return sections.isEmpty();
	}
	public boolean isFull() {
		return sections.size()>(HomeActivity.sizePerPage-1);
	}
	public Section getLastItem() {
		return sections.get(sections.size()-1);
	}
	private static class ViewHolder{
		TextView itemTitle;
		ImageView delView;
	}

}
