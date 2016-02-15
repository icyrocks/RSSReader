package whu.dellinet.rssreader.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class GridviewsAdapter extends PagerAdapter{
	private List<GridView> views;
	public GridviewsAdapter(List<GridView> list) {
		// TODO Auto-generated constructor stub
		views=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(views.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(views.get(position));
		return views.get(position);
	}
	public void removeAllViews() {
		for (int i = 0; i < views.size(); i++) {
			views.remove(i);
		}
		notifyDataSetChanged();
	}

}
