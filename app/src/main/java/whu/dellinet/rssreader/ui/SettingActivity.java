package whu.dellinet.rssreader.ui;

import java.io.File;

import whu.dellinet.rssreader.R;
import whu.dellinet.rssreader.custom.AppConfig;
import whu.dellinet.rssreader.custom.AppContext;
import whu.dellinet.rssreader.util.FileUtils;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ListView;

public class SettingActivity extends PreferenceActivity {
	
	private static final String TAG="SettingActivity";
	private CheckBoxPreference imgLoadCBP;
	private Preference clearCachePreference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initPreferences();
	}
	private void initView() {
		addPreferencesFromResource(R.xml.preferences);
		ListView listView=getListView();
		listView.setBackgroundColor(0);
		listView.setCacheColorHint(0);
		((ViewGroup)(listView.getParent())).removeView(listView);
		ViewGroup viewGroup=(ViewGroup) getLayoutInflater().inflate(R.layout.setting, null);
		((ViewGroup)(viewGroup.findViewById(R.id.setting_content))).addView(listView);
		setContentView(viewGroup);
		
		boolean imgLoad=AppContext.getSharedPreferences(SettingActivity.this).getBoolean("imgLoad", false);
		Log.i(TAG, "是否加载图片？"+imgLoad);
		
	}
	private void initPreferences() {
		
		imgLoadCBP=(CheckBoxPreference) findPreference("imgLoad");
		imgLoadCBP.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
//				boolean imgLoad=AppContext.getSharedPreferences(SettingActivity.this).getBoolean("imgLoad", false);
//				Log.i(TAG, "是否加载图片？"+imgLoad);
				Log.i(TAG, "加载偏好点击");
				
				return false;
			}
		});
		
		findPreference("about").setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SettingActivity.this, AboutActivity.class);
				SettingActivity.this.startActivity(intent);
				
				return false;
			}
		});
		
		clearCachePreference=findPreference("clearCache");
		long fileSize=0;
		String preClearCache="暂无缓存";
		File cacheDir=getCacheDir();
		Log.i(TAG, "Cache path: "+cacheDir.getAbsolutePath());
		File sectionCache = new File(AppConfig.APP_SECTION_CACHE_DIR);
		File imgCache = new File(AppConfig.APP_IMG__CACHE_DIR);
		fileSize+=FileUtils.getDirSize(cacheDir);
		fileSize+=FileUtils.getDirSize(sectionCache);
		fileSize+=FileUtils.getDirSize(imgCache);
		preClearCache=FileUtils.formatFileSize(fileSize);
		
		clearCachePreference.setSummary(preClearCache);
		clearCachePreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				boolean imgLoad=AppContext.getSharedPreferences(SettingActivity.this).getBoolean("imgLoad", false);
				Log.i(TAG, "改变前 imgLoad from sharedpreferences :"+imgLoad);
				//清理缓存
				ClearCacheTask task=new ClearCacheTask();
				task.execute(0);
				return false;
			}
		});
		
		
		
	}
	
	private class ClearCacheTask extends AsyncTask<Integer, Integer	, Integer>{

		@Override
		protected Integer doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			AppContext.clearCache(SettingActivity.this);
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			clearCachePreference.setSummary("暂无缓存");
			super.onPostExecute(result);
		}
		
	}
}
