package whu.dellinet.rssreader.custom;

import java.io.File;

import whu.dellinet.rssreader.util.FileUtils;

public class AppConfig {
	public static final String APP_NAME="RSSReader";
	public static final String APP_ROOT_DIR=FileUtils.getSDRootPath()+File.separator+APP_NAME;
	public static final String APP_CACHE_DIR=APP_ROOT_DIR+File.separator+"cache";
	public static final String APP_SECTION_CACHE_DIR=APP_CACHE_DIR+File.separator+"sections";
	public static final String APP_IMG__CACHE_DIR=APP_CACHE_DIR+File.separator+"imgs";
	public static final String APP_IMG_DIR=APP_ROOT_DIR+File.separator+"imgs";
	
	public static final String PREF_DEPRECATED = "pref_deprecated";
}
