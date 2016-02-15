package whu.dellinet.rssreader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public static final String DB_NAME="reader.db";
	public static final String SECTION_TABLE_NAME="section";
	public static final String FAVORS_TABLE_NAME="favors";
	private static final String CREATE_SECTION_TABLE="create table "+SECTION_TABLE_NAME+
			"(title text,url text, table_name text)";
	private static final String CREATE_FAVORS_TABLE="create table "+FAVORS_TABLE_NAME+
			"(title text,pubdate text,item_detail text,link text,first_img_url text,section_title text)";
	
	public DBHelper(Context context,String name,CursorFactory factory,int version) {
		// TODO Auto-generated constructor stub
		super(context, name, factory, version);
		
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_SECTION_TABLE);
		db.execSQL(CREATE_FAVORS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
