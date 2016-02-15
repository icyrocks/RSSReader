package whu.dellinet.rssreader.ui;

import java.util.ArrayList;
import java.util.List;

import whu.dellinet.rssreader.R;
import whu.dellinet.rssreader.adapter.ItemListAdapter;
import whu.dellinet.rssreader.db.DBHelper;
import whu.dellinet.rssreader.entity.FeedItem;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FavorsActivity extends Activity {
	private ListView favorListView;
	private ItemListAdapter favorListAdapter;
	private List<FeedItem> favorItems=new ArrayList<FeedItem>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}
	
	private void initView() {
		setContentView(R.layout.favorite_list);
		favorListView=(ListView) findViewById(R.id.favorite_list);
		favorListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=new Intent(FavorsActivity.this, ItemDetailActivity.class);
				// TODO Auto-generated method stub
				FeedItem feedItem=favorItems.get(arg2);
				String title=feedItem.getTitle();
				String link=feedItem.getLink();
				String pubDate=feedItem.getPubdate();
				String content=feedItem.getContent();
				if (content!=null&&content.length()>0) {
					intent.putExtra("content", content);
				}
				intent.putExtra("title", title);
				intent.putExtra("link", link);
				
				intent.putExtra("pubDate", pubDate);
				//从收藏列表进入ItemDetailActivity
				intent.putExtra("isFavor", true);
				
				intent.putExtra("isFromFavor", true);
				
				FavorsActivity.this.startActivity(intent);
			}
		});
	}
	
	private void initData() {
		DBHelper dbHelper=new DBHelper(FavorsActivity.this, DBHelper.DB_NAME, null, 1);
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor cursor=db.query(DBHelper.FAVORS_TABLE_NAME, null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			FeedItem feedItem=new FeedItem();
			String title=cursor.getString(cursor.getColumnIndex("title"));
			String link=cursor.getString(cursor.getColumnIndex("link"));
			String pubDate=cursor.getString(cursor.getColumnIndex("pubdate"));
			String content=cursor.getString(cursor.getColumnIndex("item_detail"));
			String firstImgUrl=cursor.getString(cursor.getColumnIndex("first_img_url"));
			feedItem.setContent(content);
			feedItem.setLink(link);
			feedItem.setPubdate(pubDate);
			feedItem.setTitle(title);
//			feedItem.setFirstImageUrl(firstImgUrl);
			
			favorItems.add(feedItem);
		}
		db.close();
		
		favorListAdapter=new ItemListAdapter(FavorsActivity.this, favorItems);
		favorListView.setAdapter(favorListAdapter);
		
	}
}
