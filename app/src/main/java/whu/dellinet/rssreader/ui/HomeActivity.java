package whu.dellinet.rssreader.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.NullCipher;

import whu.dellinet.rssreader.R;
import whu.dellinet.rssreader.adapter.GridAdapter;
import whu.dellinet.rssreader.adapter.GridviewsAdapter;
import whu.dellinet.rssreader.custom.AppConfig;
import whu.dellinet.rssreader.custom.AppContext;
import whu.dellinet.rssreader.custom.ItemsParaser;
import whu.dellinet.rssreader.custom.SectionHelper;
import whu.dellinet.rssreader.custom.SerialHelper;
import whu.dellinet.rssreader.custom.UIHelper;
import whu.dellinet.rssreader.db.DBHelper;
import whu.dellinet.rssreader.db.FeedsDBHelper;
import whu.dellinet.rssreader.entity.ItemListEntity;
import whu.dellinet.rssreader.entity.Section;
import whu.dellinet.rssreader.util.ImgUtils;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	public static final String ACTION_DELETE_SECTION="whu.dellinet.rssreader.delsection";
	public static final String ACTION_ADD_SECTION="whu.dellinet.rssreader.addsection";
	public static final String ACTION_SWITCH_BG="whu.dellinet.rssreader.switchbg";
	
	private Intent listIntent;
	public static final String TAG="HomeActivity in Test :";
	private ViewPager viewPager;
	private TextView pageText;
	private RelativeLayout mainLayout;
	private RelativeLayout loadingLayout;
	
	private List<GridView> gridViews=new ArrayList<GridView>();
	private List<GridAdapter> gridAdapters=new ArrayList<GridAdapter>();
	
	private GridviewsAdapter pAdapter;
	public static final int sizePerPage=8;
	private boolean isEditing;
	private boolean exit;
	
//	private boolean isNight;
	
	private BroadcastReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initView();
		
		initPage();
		
		initBroadcast();
		
//		checkNeedClearCache();
	}
	
	//建立菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(Menu.NONE, Menu.FIRST+1, 1, "添加节目").setIcon(R.drawable.composer_add);
		menu.add(Menu.NONE, Menu.FIRST+2, 2, "我的收藏").setIcon(R.drawable.composer_favorite);
		menu.add(Menu.NONE, Menu.FIRST+3, 3, "切换模式").setIcon(R.drawable.composer_moon);
		menu.add(Menu.NONE, Menu.FIRST+4, 4, "系统设置").setIcon(R.drawable.composer_setting);
		
		
		
		
		return true;
	}
	
	//建立菜单监听
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case Menu.FIRST+1:
			addFeed();
			break;
		case Menu.FIRST+2:
			openFavors();
			break;
		case Menu.FIRST+3:
			switchMode();
			break;
		case Menu.FIRST+4:
			openSettings();
			break;
		
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	
	private void initView() {
		UIHelper.initTheme(this);
		setContentView(R.layout.main);
		mainLayout=(RelativeLayout) findViewById(R.id.home_bg_layout);
		loadingLayout=(RelativeLayout) findViewById(R.id.home_loading_layout);
		viewPager=(ViewPager) findViewById(R.id.home_pager);
		pageText=(TextView) findViewById(R.id.home_page_tv);
//		gridViews=new ArrayList<GridView>();
		pAdapter=new GridviewsAdapter(gridViews);
		viewPager.setAdapter(pAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				pageText.setText((arg0+1)+"");
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	private void initPage() {
		int pageSize=getPageSize();
		Log.i(TAG, "pageSize : "+pageSize);
		
		for (int i = 0; i < pageSize; i++) {
			gridViews.add(getGridView(i));
			pAdapter.notifyDataSetChanged(); 
		}
	}
	
	//这个是重点，生成每个页面视图，并为视图建立监听
	private GridView getGridView(int pageIndex) {
		GridView gridView=new GridView(this);
		LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		gridView.setLayoutParams(params);
		int right=ImgUtils.dip2px(this, 50);
		int left=ImgUtils.dip2px(this, 20);
		int bottom=ImgUtils.dip2px(this, 20);
		int top=ImgUtils.dip2px(this, 20);
		gridView.setPadding(left, top, right, bottom);
		gridView.setNumColumns(2);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int page=viewPager.getCurrentItem();
				GridAdapter gridAdapter=gridAdapters.get(page);
				
//				Log.i(TAG,"ViewPage Index:"+page);
				
				Section section=(Section) gridAdapter.getItem(arg2);
				String url=section.getUrl();
				String title=section.getTitle();
				Log.i(TAG, " url: "+url);
				
			    listIntent=new Intent(HomeActivity.this, ItemListActivity.class);
			    listIntent.putExtra("url", url);
			    listIntent.putExtra("title", title);
				File cache=SectionHelper.getSectionCache(url);
				if (cache.exists()) {
					startActivity(listIntent);
					Log.i(TAG, url+"的"+"cache 已存在，将要进入ItemListActivity");
				}else {
					Log.i(TAG, url+"的"+"cache 不存在，将要进行网络下载");
					if (!AppContext.isNewworkAvailable(HomeActivity.this)) {
						Toast.makeText(HomeActivity.this, "无网络连接！", Toast.LENGTH_LONG).show();
						return;
					}else {
						//启动网络加载器，异步加载数据
						DataLoader dataLoader=new DataLoader();
						dataLoader.execute(url);
					}
				}
			}
		});
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				isEditing=true;
				for (int i = 0; i < gridViews.size(); i++) {
					gridViews.get(i).setEnabled(false);
				}
				for (int j = 0; j < gridAdapters.size(); j++) {
					gridAdapters.get(j).changeDelState(1);
				}
				Toast.makeText(HomeActivity.this, "按返回键退出编辑模式", Toast.LENGTH_LONG).show();
				return true;
			}
		});
		List<Section> pageSections=readSections(pageIndex);
		GridAdapter gridAdapter=new GridAdapter(this, pageSections);
		gridAdapters.add(gridAdapter);
		gridView.setAdapter(gridAdapter);
		
		return gridView;
		
		
		
	}
	
	//从数据库中读取section信息
	private List<Section> readSections(int pageIndex) {
		List<Section> list=new ArrayList<Section>();
		DBHelper dbHelper=new DBHelper(HomeActivity.this, DBHelper.DB_NAME, null, 1);
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor cursor=db.query(DBHelper.SECTION_TABLE_NAME, null, null, null, null, null, null);
		int count=cursor.getCount();
		db.close();
		int start=pageIndex*sizePerPage;
		if (cursor.moveToPosition(start)) {
			int  offset=start+sizePerPage;
			int end=count<offset ? count:offset;
			for (int i = start; i < end; i++) {
				Section section=new Section();
				String title=cursor.getString(cursor.getColumnIndex("title"));
				String url=cursor.getString(cursor.getColumnIndex("url"));
				String tableName=cursor.getString(cursor.getColumnIndex("table_name"));
				section.setTitle(title);
				section.setUrl(url);
				section.setTableName(tableName);
				list.add(section);
				cursor.moveToNext();
				
			}
		}
		return list;
	}
	
	//从数据库中读取共有多少页面
	private int  getPageSize() {
		int pageCount=0;
		DBHelper dbHelper=new DBHelper(this, DBHelper.DB_NAME, null, 1);
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor cursor=db.query(DBHelper.SECTION_TABLE_NAME, null, null, null, null, null, null);
		int sectionCount=cursor.getCount();
		db.close();
		if(sectionCount%sizePerPage==0){
			pageCount=sectionCount/sizePerPage;
		}else {
			pageCount=sectionCount/sizePerPage+1;
		}
		return pageCount;
	}
	//从网络加载数据
	private class DataLoader extends AsyncTask<String, Integer, ItemListEntity>{

		@Override
		//跳转至ItemListActivity中，这时各种信息已加载完毕
		protected void onPostExecute(ItemListEntity result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			loadingLayout.setVisibility(View.GONE);
			if (result!=null&&listIntent!=null&&!result.getItemsList().isEmpty()) {
				startActivity(listIntent);
			}else {
				Toast.makeText(HomeActivity.this, "获取ItemList失败", Toast.LENGTH_LONG).show();
				
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loadingLayout.setVisibility(View.VISIBLE);
		}

		@Override
		//在这里进行了内容的在线加载并保存在Cache中
		protected ItemListEntity doInBackground(String... params) {
			
			Log.i(TAG, "doBackground params[0]: "+params[0]);
			// TODO Auto-generated method stub
			ItemsParaser paraser=new ItemsParaser();
			ItemListEntity itemListEntity=paraser.paraser(params[0]);
			
			if (itemListEntity!=null) {
				
				Log.i(TAG, params[0]+"内容已下载完毕,将要进行cache保存");
				SerialHelper helper=SerialHelper.getInstance();
				File sectionCache=SectionHelper.newSectionCache(params[0]);
				helper.saveObject(itemListEntity, sectionCache);
			}
			return itemListEntity;
		}
		
	}
	//检查缓存，是否需要清除
	private void checkNeedClearCache() {
		String path=getFilesDir()+File.separator+AppConfig.PREF_DEPRECATED;
		File file=new File(path);
		int days=(int)(System.currentTimeMillis()-file.lastModified())/(1000*60*60*24);
		if (days>3) {
			AppContext.clearCache(HomeActivity.this);
			file.setLastModified(System.currentTimeMillis());
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			if (isEditing) {
				isEditing=false;
				for (int i = 0; i < gridViews.size(); i++) {
					gridViews.get(i).setEnabled(true);
				}
				for (int i = 0; i < gridAdapters.size(); i++) {
					gridAdapters.get(i).changeDelState(0);
				}
			}else {
				if (exit) {
					HomeActivity.this.finish();
				}else {
					Toast.makeText(HomeActivity.this, "再按下退出程序", Toast.LENGTH_LONG).show();
					exit=true;
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initBroadcast(){
		receiver=new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action=intent.getAction();
				if (action.equals(ACTION_ADD_SECTION)) {
					//增加一个Section
					
					Log.i(TAG, "收到一个增加section的广播  ");
					GridAdapter gridAdapter=getLastAdapter();
					if (gridAdapter==null||gridAdapter.isFull()) {
						//增加一个页面
						addGridView();
					}else {
						gridAdapter.addItem(getNewSection());
					}
					
				}else if (action.equals(ACTION_DELETE_SECTION)) {
					//删除一个Section
					
					
					GridAdapter gridAdapter=null;
					String url=intent.getStringExtra("url");
					
					Log.i(TAG, "收到一个删除section的广播  section的url为 ："+url);
					
					for (int i = 0; i < gridAdapters.size(); i++) {
						gridAdapter=gridAdapters.get(i);
						if (gridAdapter.removeItem(url)) {
							break;
						}
					}
					GridAdapter lastAdapter=getLastAdapter();
					if (lastAdapter.isEmpty()) {
						if (gridViews.size()<2) {
							return;
						}
						removeLastGridView();
						removeLastAdapter();
						return;
					}
					if (lastAdapter!=gridAdapter) {
						Section section=lastAdapter.getLastItem();
						gridAdapter.addItem(section);
						lastAdapter.removeItem(section.getUrl());
					}
					if (lastAdapter.isEmpty()) {
						if (gridViews.size()<2) {
							return;
						}
						removeLastGridView();
						removeLastAdapter();
					}
					
				}else if (action.equals(ACTION_SWITCH_BG)) {
					int resid=intent.getIntExtra("bgid", R.drawable.home_bg_default);
					mainLayout.setBackgroundResource(resid);
					Editor editor=AppContext.getSharedPreferences(HomeActivity.this).edit();
					editor.putInt("bgid", resid);
					editor.commit();
				}
			}
		};
		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction(ACTION_ADD_SECTION);
		intentFilter.addAction(ACTION_DELETE_SECTION);
		intentFilter.addAction(ACTION_SWITCH_BG);
		registerReceiver(receiver, intentFilter);
	}
	private void removeLastGridView() {
		if (gridViews.isEmpty()) {
			return;
		}
		gridViews.remove(gridViews.size()-1);
		pAdapter.notifyDataSetChanged();
	}
	private void removeLastAdapter() {
		if (gridAdapters.isEmpty()) {
			return;
		}
		gridAdapters.remove(gridAdapters.size()-1);
	}
	
	private Section getNewSection(){
		Section section=null;
		DBHelper helper=new DBHelper(HomeActivity.this, DBHelper.DB_NAME, null, 1);
		SQLiteDatabase db=helper.getReadableDatabase();
		Cursor cursor=db.query(DBHelper.SECTION_TABLE_NAME, null, null, null, null, null, null);
		if (cursor.moveToLast()) {
			String title=cursor.getString(cursor.getColumnIndex("title"));
			String url=cursor.getString(cursor.getColumnIndex("url"));
			String tableName=cursor.getString(cursor.getColumnIndex("table_name"));
			section=new Section();
			section.setTitle(title);
			section.setUrl(url);
			section.setTableName(tableName);
		}
		db.close();
		return section;
	}
	private GridAdapter getLastAdapter() {
		if (gridAdapters.isEmpty()) {
			return null;
		}else {
			return gridAdapters.get(gridAdapters.size()-1);
		}
	}
	private void addGridView() {
		int lastPage=getPageSize()-1;
		GridView gridView=getGridView(lastPage);
		gridViews.add(gridView);
		pAdapter.notifyDataSetChanged();
	}
	
	//菜单项响应函数
	private void addFeed() {
		Intent intent=new Intent(HomeActivity.this, FeedCategoryActivity.class);
		HomeActivity.this.startActivity(intent);
	}
	private void openFavors(){
		Intent intent=new Intent(HomeActivity.this, FavorsActivity.class);
		HomeActivity.this.startActivity(intent);
	}
	private void openSettings() {
		Intent intent=new Intent(HomeActivity.this, SettingActivity.class);
		HomeActivity.this.startActivity(intent);
	}
	private void setBg() {
		Intent intent=new Intent(HomeActivity.this, BGActivity.class);
		HomeActivity.this.startActivity(intent);
	}
	private void switchMode(){
		SharedPreferences sp=AppContext.getSharedPreferences(HomeActivity.this);
		boolean isNight=sp.getBoolean("night_mode", false);
		Editor editor=sp.edit();
		if (isNight) {
			isNight=false;
			mainLayout.setBackgroundResource(R.drawable.homebg_day);
			Toast.makeText(HomeActivity.this, "已切换至白天模式", Toast.LENGTH_LONG).show();
		}else {
			isNight=true;
			mainLayout.setBackgroundResource(R.drawable.homebg_night);
			Toast.makeText(HomeActivity.this, "已切换至夜间模式", Toast.LENGTH_LONG).show();
		}
		editor.putBoolean("night_mode", isNight);
		editor.commit();
	}

	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		super.onDestroy();
	}
}
