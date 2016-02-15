package whu.dellinet.rssreader.ui;

import java.lang.ref.WeakReference;

import whu.dellinet.rssreader.R;
import whu.dellinet.rssreader.custom.AppContext;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HelloActivity extends Activity {
	public static final int GOHOME=10000;
	public static final int GOGUIDE=10001;
	public static final long DELAY_SHOW=3000;
	
	private boolean isFirst;
	private boolean imgLoad;
	public static final String TAG="HelloActivity";
	private MyHandler myHandler=new MyHandler(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hellolayout);
		SharedPreferences sp=AppContext.getSharedPreferences(this);
		imgLoad=sp.getBoolean("imgLoad", false);
		Log.i(TAG, "imgLoad ?"+imgLoad);
		isFirst=sp.getBoolean("isFirst", true);
		Log.i(TAG, "isFrist ?: "+isFirst);
		
		//ÔÝÊ±µÄ
		
//		isFirst=true;
		if (isFirst) {
			Editor editor=sp.edit();
			editor.putBoolean("isFirst", false);
			editor.commit();
			myHandler.sendEmptyMessageDelayed(GOGUIDE, DELAY_SHOW);
			
		}else {
			myHandler.sendEmptyMessageDelayed(GOHOME, DELAY_SHOW);
		}
	}
	private static class MyHandler extends Handler{
		private final WeakReference<Activity> weakActivity;
		public MyHandler(Activity activity) {
			// TODO Auto-generated constructor stub
			weakActivity=new WeakReference<Activity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Activity activity=weakActivity.get();
			if (activity==null) {
				return;
			}
			switch (msg.what) {
			case GOHOME:
				Intent intent=new Intent(activity, HomeActivity.class);
				activity.startActivity(intent);
				activity.finish();
				break;
			case GOGUIDE:
				Intent intent2=new Intent(activity, GuideActivity.class);
				activity.startActivity(intent2);
				activity.finish();
				break;
			default:
				break;
			}
			
			super.handleMessage(msg);
		}
		
	}
}
