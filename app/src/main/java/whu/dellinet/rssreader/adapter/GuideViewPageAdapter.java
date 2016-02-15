package whu.dellinet.rssreader.adapter;

import java.util.List;

import whu.dellinet.rssreader.R;
import whu.dellinet.rssreader.ui.HomeActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GuideViewPageAdapter extends PagerAdapter {
	private List<View> views;
	private Activity activity;
	public GuideViewPageAdapter(List<View> mViews,Activity mActivity) {
		// TODO Auto-generated constructor stub
		this.views=mViews;
		this.activity=mActivity;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stubs
		if (views!=null) {
			return views.size();
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return (arg0==arg1);
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(views.get(position));
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(views.get(position), 0);
		if (position==(views.size()-1)) {
			ImageView startView=(ImageView) views.get(position).findViewById(R.id.iv_start_rss);
			startView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(activity, HomeActivity.class);
					activity.startActivity(intent);
					activity.finish();
				}
			});
		}
		return views.get(position);
	}
	
	
	

}
