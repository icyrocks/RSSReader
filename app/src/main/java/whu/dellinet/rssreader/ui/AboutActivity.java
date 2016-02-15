package whu.dellinet.rssreader.ui;

import whu.dellinet.rssreader.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends Activity {
	
	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		webView=(WebView) findViewById(R.id.about_webview);
		webView.loadUrl("file:///android_asset/about.html");
	}
}
