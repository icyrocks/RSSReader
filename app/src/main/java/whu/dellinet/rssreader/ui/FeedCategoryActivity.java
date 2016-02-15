package whu.dellinet.rssreader.ui;

import whu.dellinet.rssreader.R;
import whu.dellinet.rssreader.adapter.FeedCategoryAdapter;
import whu.dellinet.rssreader.db.FeedsDBHelper;
import whu.dellinet.rssreader.util.ExchangeUtils;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FeedCategoryActivity extends Activity {
	public static final String TAG="FeedCategoryActivity";
	
	private ListView listView;
	private String[] categories_en;
	private FeedCategoryAdapter feedCategoryAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
	}
	private void initView() {
		setContentView(R.layout.feed_category);
		categories_en=getResources().getStringArray(R.array.feedcategory_en);
		listView=(ListView) findViewById(R.id.feed_category_lsit);
		feedCategoryAdapter=new FeedCategoryAdapter(this);
		listView.setAdapter(feedCategoryAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(FeedCategoryActivity.this, CategoryDetailActivity.class);
				
				intent.putExtra("category", categories_en[arg2]);
				startActivity(intent);
				
			}
		});
		
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(Menu.NONE, Menu.FIRST+1, 1, "添加rss源").setIcon(R.drawable.composer_add);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case Menu.FIRST+1:
			addFeedResource();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void addFeedResource() {
		Builder builder=new Builder(FeedCategoryActivity.this);
		LayoutInflater inflater=LayoutInflater.from(FeedCategoryActivity.this);
		View addfeedView=inflater.inflate(R.layout.addfeedlayout, null);
		builder.setView(addfeedView);
		final EditText titleText=(EditText) addfeedView.findViewById(R.id.addfeedtitle);
		final EditText urlText=(EditText) addfeedView.findViewById(R.id.addfeedurl);
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String feedTitle=titleText.getText().toString().trim();
				String feedUrl=urlText.getText().toString().trim();
				Log.i(TAG, "添加feed 标题："+feedTitle);
				Log.i(TAG, "添加feed 网址："+feedUrl);
				
				FeedsDBHelper feedsDBHelper=new FeedsDBHelper(FeedCategoryActivity.this, FeedsDBHelper.DB_NAME, null, 1);
				feedsDBHelper.addFeedResource(feedTitle, feedUrl);
				Toast.makeText(FeedCategoryActivity.this, "添加rss源成功", Toast.LENGTH_SHORT).show();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		builder.setTitle("添加rss源");
		builder.show();
	}
}
