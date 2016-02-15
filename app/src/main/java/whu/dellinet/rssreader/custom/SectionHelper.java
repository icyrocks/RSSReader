package whu.dellinet.rssreader.custom;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import whu.dellinet.rssreader.db.DBHelper;
import whu.dellinet.rssreader.util.FileUtils;
import whu.dellinet.rssreader.util.MD5Utils;

public class SectionHelper {
	
	
	public static void removeRecord(Context context,String url) {
		DBHelper dbHelper=new DBHelper(context, DBHelper.DB_NAME, null, 1);
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		db.delete(DBHelper.SECTION_TABLE_NAME, "url=?", new String[]{url});
		db.close();
	}
	public static void insertRecord(Context context,String title,String url,String table_name) {
		DBHelper dbHelper=new DBHelper(context, DBHelper.DB_NAME, null, 1);
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		ContentValues contentValues=new ContentValues();
		contentValues.put("title", title);
		contentValues.put("url", url);
		contentValues.put("table_name", table_name);
		db.insert(DBHelper.SECTION_TABLE_NAME, null, contentValues);
		db.close();
		
	}
	
	public static File getSectionCache(String url) {
		return new File(AppConfig.APP_SECTION_CACHE_DIR+File.separator+MD5Utils.getMD5(url));
	}
	public static File newSectionCache(String url) {
		String fileName=AppConfig.APP_SECTION_CACHE_DIR+File.separator+MD5Utils.getMD5(url);
		return FileUtils.creatNewFile(fileName);
	}
}
