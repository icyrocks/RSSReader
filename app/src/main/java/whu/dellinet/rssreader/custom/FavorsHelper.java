package whu.dellinet.rssreader.custom;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import whu.dellinet.rssreader.db.DBHelper;

public class FavorsHelper {
	public static void insertFavor(Context context,String title,String link,String pubDate,String itemDetail,String firstImgUrl,String sectionTitle) {
		DBHelper dbHelper=new DBHelper(context, DBHelper.DB_NAME, null, 1);
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		ContentValues cv=new ContentValues();
		cv.put("title", title);
		cv.put("pubdate", pubDate);
		cv.put("link", link);
		cv.put("item_detail", itemDetail);
		cv.put("section_title", sectionTitle);
		cv.put("first_img_url", firstImgUrl);
		db.insert(DBHelper.FAVORS_TABLE_NAME, null, cv);
	}
	
	public static void removeFavor(Context context,String link) {
		DBHelper dbHelper=new DBHelper(context, DBHelper.DB_NAME, null, 1);
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		db.delete(DBHelper.FAVORS_TABLE_NAME, "link=?", new String[]{link});
		db.close();
	}
}
