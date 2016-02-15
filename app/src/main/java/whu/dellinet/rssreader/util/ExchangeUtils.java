package whu.dellinet.rssreader.util;

import whu.dellinet.rssreader.R;
import android.content.Context;

public class ExchangeUtils {
	private String[] categories_zh;
	private String[] categories_en;
	
	public ExchangeUtils(Context context) {
		// TODO Auto-generated constructor stub
		categories_zh=context.getResources().getStringArray(R.array.feedcategory);
		categories_en=context.getResources().getStringArray(R.array.feedcategory_en);
	}
	public String zh2en(String cazh) {
		String caen=null;
		for (int i = 0; i < categories_zh.length; i++) {
			if (cazh.equals(categories_zh[i])) {
				caen=categories_en[i];
				break;
			}
		}
		return caen;
	}
	
	public String en2zh(String caen) {
		String cazh=null;
		for (int i = 0; i < categories_en.length; i++) {
			if (caen.equals(categories_en[i])) {
				cazh=categories_zh[i];
				break;
			}
		}
		return cazh;
	}
	
}
