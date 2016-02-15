package whu.dellinet.rssreader.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import android.util.Log;

public class HttpUtils {
	public static final String TAG="HttpUtils";
//	private static HttpURLConnection connection=null;
	private static final int TIMEOUT=30*1000;
	
	 private static final int REQUEST_TIMEOUT = 30*1000;//设置请求超时10秒钟  
	 private static final int SO_TIMEOUT = 30*1000;  //设置等待数据超时时间10秒钟  
	 
	 public static HttpClient getHttpClient(){  
		    BasicHttpParams httpParams = new BasicHttpParams();  
		    HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);  
		    HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);  
		    HttpClient client = new DefaultHttpClient(httpParams);  
		    return client;  
		}  
	
	public static InputStream getInputStreamByUrl(String url) {
		InputStream inputStream=null;
		
		try {
			HttpGet httpRequest = new HttpGet(url);
			HttpClient httpclient = getHttpClient();
			HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
			inputStream = bufferedHttpEntity.getContent();
		} catch (IOException e) {
			// TODO: handle exception
			Log.i(TAG, "HttpClient 输入流IO异常！");
		}
		
		
//		try {
//			URL httpUrl=new URL(url);
//			connection=(HttpURLConnection) httpUrl.openConnection();
//			connection.setConnectTimeout(TIMEOUT);
//			connection.setReadTimeout(TIMEOUT);
//			if (connection.getResponseCode()==HttpStatus.SC_OK) {
//				inputStream=connection.getInputStream();
//				
//				
//			}
//			
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}catch (SocketTimeoutException e) {
//			// TODO: handle exception
//			Log.i(TAG, "url 加载网络超时！"+url);
//		}
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			Log.i(TAG, "网络加载无法获取数据！"+url);
//			e.printStackTrace();
//		}
		return inputStream;
		
		
		
	}
}
