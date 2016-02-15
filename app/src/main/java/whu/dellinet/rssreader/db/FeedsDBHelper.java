package whu.dellinet.rssreader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedsDBHelper extends SQLiteOpenHelper {
	public static final String DB_NAME="feeds.db";
	public static final String MYFEED="myfeed";
	public FeedsDBHelper(Context context,String name,CursorFactory factory,int version) {
		// TODO Auto-generated constructor stub
		super(context, name, factory, version);
	}
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	public  void updateState(String tableName,String url,int state){
		SQLiteDatabase db=getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put("select_state", state);
		db.update(tableName, cv, "url=?", new String[]{url});
		db.close();
	}
	
	public void addFeedResource(String title,String url) {
		SQLiteDatabase db=getWritableDatabase();
		ContentValues cv=new ContentValues();
		cv.put("title", title);
		cv.put("url", url);
		cv.put("select_state", 0);
		db.insert(MYFEED, null, cv);
		db.close();
	}
	
	
}
