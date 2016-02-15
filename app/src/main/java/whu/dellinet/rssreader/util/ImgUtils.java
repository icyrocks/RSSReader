package whu.dellinet.rssreader.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import whu.dellinet.rssreader.custom.AppConfig;
import whu.dellinet.rssreader.custom.AppContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

public class ImgUtils {
	public static final CompressFormat C_FORMAT=CompressFormat.JPEG;
	public static final int quality=75;
	
	public static int dip2px(Context context,float dpValue) {
		float scale=context.getResources().getDisplayMetrics().density;
		return (int)(dpValue*scale+0.5f);
	}
	public static int px2dip(Context context,float pxValue) {
		float scale=context.getResources().getDisplayMetrics().density;
		return (int)(pxValue/scale+0.5f);
	}
	public static void saveImgToSDcard(Bitmap bitmap,String url) {
		File file=FileUtils.creatNewFile(AppConfig.APP_IMG__CACHE_DIR+File.separator+MD5Utils.getMD5(url));
		FileOutputStream fos=null;
		try {
			fos=new FileOutputStream(file);
			bitmap.compress(C_FORMAT, quality, fos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
