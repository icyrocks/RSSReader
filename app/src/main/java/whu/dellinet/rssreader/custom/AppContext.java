package whu.dellinet.rssreader.custom;

import java.io.File;

import whu.dellinet.rssreader.util.FileUtils;
import whu.dellinet.rssreader.util.MD5Utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.CacheManager;

public class AppContext {
	public static void clearCache(Context context) {
//		clearAppCache(context);
		clearSDCache();
		clearWebViewCache(context);
	}
	public static void clearAppCache(Context context) {
		File appCache=context.getCacheDir();
		String appCachePath=appCache.getAbsolutePath();
		Log.i("AppContext", "AppCacheDir: "+appCachePath);
		FileUtils.deleteDirectory(appCachePath);
	}
	
	public static void clearSDCache() {
		FileUtils.deleteDirectory(AppConfig.APP_SECTION_CACHE_DIR);
		FileUtils.deleteDirectory(AppConfig.APP_IMG__CACHE_DIR);
	}
	public static void clearWebViewCache(Context context) {
		File file=CacheManager.getCacheFileBaseDir();
		if (file!=null&&file.exists()&&file.isDirectory()) {
			File[] files=file.listFiles();
			for (File file2 : files) {
				file2.delete();
			}
			file.delete();
		}
		context.deleteDatabase("webview.db");
		context.deleteDatabase("webview.db-shm");
		context.deleteDatabase("webview.db-wal");
		context.deleteDatabase("webviewCache.db");
		context.deleteDatabase("webviewCache.db-shm");
		context.deleteDatabase("webviewCache.db-wal");
		
	}
	public static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	public static File getSectionCache(String url) {
		return new File(AppConfig.APP_SECTION_CACHE_DIR+File.separator+MD5Utils.getMD5(url));
	}
	public static File getImgCache(String url) {
		return new File(AppConfig.APP_IMG__CACHE_DIR+File.separator+MD5Utils.getMD5(url));
	}
	public static boolean isNewworkAvailable(Context context) {
		ConnectivityManager conManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo=conManager.getActiveNetworkInfo();
		if (networkInfo==null||!networkInfo.isConnected()) {
			return false;
		}
		return true;
	}
}
