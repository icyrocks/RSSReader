package whu.dellinet.rssreader.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import whu.dellinet.rssreader.R;
import whu.dellinet.rssreader.adapter.GuideViewPageAdapter;
import whu.dellinet.rssreader.custom.AppConfig;
import whu.dellinet.rssreader.custom.AppContext;
import whu.dellinet.rssreader.db.FeedsDBHelper;
import android.R.integer;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends Activity implements OnPageChangeListener {
	private ViewPager viewPager;
	private GuideViewPageAdapter guideAdapter;
	private List<View> views;
	private ImageView[] bottomdots;
	
	private int currentIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guidelayout);
		initView();
		initDots();
		initFeedsDB();
		writeSaveDays();
		AppContext.clearSDCache();
	}
	private void initView(){
		LayoutInflater inflater=LayoutInflater.from(GuideActivity.this);
		views=new ArrayList<View>();
		views.add(inflater.inflate(R.layout.what_new_one, null));
		views.add(inflater.inflate(R.layout.what_new_two, null));
		views.add(inflater.inflate(R.layout.what_new_three, null));
		views.add(inflater.inflate(R.layout.what_new_four, null));
		guideAdapter=new GuideViewPageAdapter(views, GuideActivity.this);
		
		viewPager=(ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(guideAdapter);
		viewPager.setOnPageChangeListener(this);
	}
	private void initDots(){
		LinearLayout layout=(LinearLayout) findViewById(R.id.ll);
		bottomdots=new ImageView[views.size()];
		for (int i = 0; i < bottomdots.length; i++) {
			bottomdots[i]=(ImageView) layout.getChildAt(i);
			bottomdots[i].setEnabled(true);
		}
		currentIndex=0;
		bottomdots[currentIndex].setEnabled(false);
	}
	
	private void initFeedsDB(){
		InputStream inputStream=null;
		try {
			inputStream=getAssets().open("feeds.db");
			FeedsDBHelper helper=new FeedsDBHelper(this, FeedsDBHelper.DB_NAME, null, 1);
			SQLiteDatabase feedsdb=helper.getWritableDatabase();
			File dbFile=new File(feedsdb.getPath());
			if (dbFile.exists()) {
				dbFile.delete();
			}
			FileOutputStream outputStream=null;
			try {
				
				outputStream=new FileOutputStream(dbFile);
				byte[] buffers=new byte[1024*4];
				while (inputStream.read(buffers)!=-1) {
					outputStream.write(buffers);
				}
			} catch (FileNotFoundException e) {
				// TODO: handle exception
				e.printStackTrace();
			}catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}finally{
				try {
					if (inputStream!=null) {
						inputStream.close();
						inputStream=null;
					}
					if (outputStream!=null) {
						outputStream.close();
						outputStream=null;
					}
				} catch (IOException e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
				feedsdb.close();
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void writeSaveDays() {
		String fileName=getFilesDir().getAbsolutePath()+File.separatorChar+AppConfig.PREF_DEPRECATED;
		File file=new File(fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setCurrentDot(arg0);
	}
	
	private void setCurrentDot(int position) {
		if (position<0||position>(views.size()-1)||position==currentIndex) {
			return;
		}
		bottomdots[position].setEnabled(false);
		bottomdots[currentIndex].setEnabled(true);
		currentIndex=position;
	}
}
