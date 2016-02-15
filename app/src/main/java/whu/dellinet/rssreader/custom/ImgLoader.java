package whu.dellinet.rssreader.custom;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import whu.dellinet.rssreader.util.HttpUtils;
import whu.dellinet.rssreader.util.ImgUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class ImgLoader {
	private static final String TAG="ImgLoader";
	private Bitmap defBitmap;
	private static HashMap<String, SoftReference<Bitmap>> imgCache;
	private static Map<ImageView, String> imageViewMap;
	private static ExecutorService pool;
	
	
	static{
		imgCache=new HashMap<String, SoftReference<Bitmap>>();
		imageViewMap=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
		pool=Executors.newFixedThreadPool(10);
		

	}
	
	public void setDefBitmap(Bitmap bitmap) {
		defBitmap=bitmap;
	}
	public Bitmap getCacheImg(String url) {
		Bitmap btp=null;
		if (imgCache.containsKey(url)) {
			btp=imgCache.get(url).get();
		}
		return btp;
	}
	public void loadImg(String imgUrl,ImageView imageView,int width,int height) {
		
//		Log.i(TAG, "firstImg ��ȣ�"+width);
//		Log.i(TAG, "firstImg �߶ȣ�"+height);
		
		
		
//		imageViewMap.put(imageView, imgUrl);
		Bitmap btp=getCacheImg(imgUrl);
		if (btp!=null) {
			
			Log.i(TAG, "ͼƬ�������У� "+imgUrl);
			
			imageView.setImageBitmap(btp);
		}else {
			File file=AppContext.getImgCache(imgUrl);
			if (file.exists()) {
				btp=BitmapFactory.decodeFile(file.getAbsolutePath());
				Log.i(TAG, "ͼƬ�ļ����У� "+imgUrl);
				imageView.setImageBitmap(btp);
			}else {
				Log.i(TAG, "����δ���У�Ҫ����������أ� "+imgUrl);
//				imageView.setImageBitmap(defBitmap);
				
				imageViewMap.put(imageView, imgUrl);
				
				loadImgByNet(imgUrl, imageView, width, height);
			}
		}
	}
	
	private void loadImgByNet(final String url,final ImageView imageView,final int width,final int height) {
		final Handler handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				String urlTag=imageViewMap.get(imageView);
				if (urlTag!=null&&urlTag.equals(url)) {
					if (msg.obj!=null) {
						Bitmap btp=(Bitmap) msg.obj;
						imageView.setImageBitmap(btp);
					}
				}
			}
			
		};
		pool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				InputStream inputStream=HttpUtils.getInputStreamByUrl(url);
//				Log.i(TAG, "ͼƬ��inputstream �Ƿ�Ϊ�գ� :"+(inputStream==null));
				
				if (inputStream==null) {
//					Log.i(TAG, url+"������ص�ͼƬ Ϊ��!!! :");
					return;
				}

				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true; 
				BufferedInputStream buffer=new BufferedInputStream(inputStream);
				BitmapFactory.decodeStream(buffer,null,options);
				try {
					buffer.reset();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				    // Calculate inSampleSize
				options.inSampleSize = calculateInSampleSize(options, width	,height);

				    // Decode bitmap with inSampleSize set
				options.inJustDecodeBounds = false; 
				Bitmap btp=BitmapFactory.decodeStream(buffer,null,options);
				
				
				if (btp==null) {
					return;
				}
				
				Log.i(TAG, "�Ѿ���õ�ͼƬurl��"+url);
				
//				btp=Bitmap.createScaledBitmap(btp, width, height, true);
				
				Message message=handler.obtainMessage();
				message.obj=btp;
				handler.sendMessage(message);
				//����ͼƬ�����ڴ����Լ����ļ���
				imgCache.put(url, new SoftReference<Bitmap>(btp));
				ImgUtils.saveImgToSDcard(btp, url);
				
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	public static int calculateInSampleSize(BitmapFactory.Options options,  
	        int reqWidth, int reqHeight) {  
	    // ԴͼƬ�ĸ߶ȺͿ��  
	    final int height = options.outHeight;  
	    final int width = options.outWidth;  
	    int inSampleSize = 1;  
	    if (height > reqHeight || width > reqWidth) {  
	        // �����ʵ�ʿ�ߺ�Ŀ���ߵı���  
	        final int heightRatio = Math.round((float) height / (float) reqHeight);  
	        final int widthRatio = Math.round((float) width / (float) reqWidth);  
	        // ѡ���͸�����С�ı�����ΪinSampleSize��ֵ��������Ա�֤����ͼƬ�Ŀ�͸�  
	        // һ��������ڵ���Ŀ��Ŀ�͸ߡ�  
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;  
	    }  
	    return inSampleSize;  
	}  
	
	
}
