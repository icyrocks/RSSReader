package whu.dellinet.rssreader.adapter;

import whu.dellinet.rssreader.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FeedCategoryAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private String[] categories;
	public FeedCategoryAdapter(Context mContext) {
		// TODO Auto-generated constructor stub
		context=mContext;
		inflater=LayoutInflater.from(context);
		categories=context.getResources().getStringArray(R.array.feedcategory);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return categories.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return categories[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		// TODO Auto-generated method stub
		if (convertView==null) {
			convertView=inflater.inflate(R.layout.category_item, null);
			holder=new ViewHolder();
			holder.textView=(TextView) convertView.findViewById(R.id.category_title);
			convertView.setTag(holder);
		}else {
			holder=(ViewHolder) convertView.getTag();
		}
		holder.textView.setText(categories[position]);
		return convertView;
	}


	private final class ViewHolder{
		private TextView textView;
	}
}
