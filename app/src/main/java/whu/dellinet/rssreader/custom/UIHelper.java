package whu.dellinet.rssreader.custom;

import whu.dellinet.rssreader.R;
import android.content.Context;
import android.content.SharedPreferences;

public class UIHelper {
	
public static final String tag = "UIHelper";
	
	
	
	public static void initTheme(Context context) {
		SharedPreferences sp=AppContext.getSharedPreferences(context);
		boolean isNight=sp.getBoolean("night_mode", false);
		if (isNight) {
			context.setTheme(R.style.AppNightTheme);
		}
	}
}
