package whu.dellinet.rssreader.ui;

import java.util.ArrayList;
import java.util.List;

import whu.dellinet.rssreader.R;
import whu.dellinet.rssreader.adapter.CategoryDetailAdapter;
import whu.dellinet.rssreader.db.FeedsDBHelper;
import whu.dellinet.rssreader.entity.Feed;
import whu.dellinet.rssreader.util.ExchangeUtils;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryDetailActivity extends Activity {
	
	public static final String TAG="CategoryDetailActivity";
	private String category;
	private TextView titleView;
	private ListView listView;
	private List<Feed> feeds=new ArrayList<Feed>();
	
	private CategoryDetailAdapter detailAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent=getIntent();
		category=intent.getStringExtra("category");
		Log.i(TAG, "À˘ Ù¿‡±£∫"+category);
		
		initView();
		initFeedData();
	}
	private void initView() {
		setContentView(R.layout.category_detail);
		listView=(ListView) findViewById(R.id.catagory_detail_lv);
		titleView=(TextView) findViewById(R.id.category_detail_tv);
		
	}
	
	private void initFeedData(){
		Intent intent=getIntent();
		String category_en=intent.getStringExtra("category");
		ExchangeUtils exchangeUtils=new ExchangeUtils(CategoryDetailActivity.this);
		category=exchangeUtils.en2zh(category_en);
		titleView.setText(category);
		
		FeedsDBHelper feedsDBHelper=new FeedsDBHelper(CategoryDetailActivity.this, FeedsDBHelper.DB_NAME, null, 1);
		SQLiteDatabase db=feedsDBHelper.getWritableDatabase();
		Cursor cursor=db.query(category_en, null, null, null, null, null, null);
		int titleIndex=cursor.getColumnIndex("title");
		int urlIndex=cursor.getColumnIndex("url");
		int stIndex=cursor.getColumnIndex("select_state");
		Log.i(TAG, "titleIndex :"+titleIndex);
		Log.i(TAG, "urlIndex :"+urlIndex);
		Log.i(TAG, "selectStateIndex :"+stIndex);
		while (cursor.moveToNext()) {
			Feed feed=new Feed();
			String title=cursor.getString(titleIndex);
			String url=cursor.getString(urlIndex);
			int selectState=cursor.getInt(stIndex);
			feed.setTitle(title);
			feed.setUrl(url);
			feed.setSelectState(selectState);
			feeds.add(feed);
		}
		db.close();
		detailAdapter=new CategoryDetailAdapter(CategoryDetailActivity.this,feeds, category_en);
		listView.setAdapter(detailAdapter);
	}
}
