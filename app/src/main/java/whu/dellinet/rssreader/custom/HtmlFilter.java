package whu.dellinet.rssreader.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlFilter {
	public static final String HTMLREGEX="<([^>]*)>";
	public static final String IMGREGEX="(<|;)\\s*(IMG|img)\\s+([^;>]*)\\s*(;|>)";
	public static final String IMGURLREGEX="http://([^\"]+)\"";
	public static final String STYLEREGEX="\\s*style=\"([^\"]*)\"";
	public static final String ENCODINGREGEX="\\s*encoding=\"([^\"]*)\"";
	
	
	public static List<String> getImgSrcs(String content) {
		List<String> imgSrcs=new ArrayList<String>();
		Pattern imgPattern=Pattern.compile(IMGREGEX);
		Matcher imgMatcher=imgPattern.matcher(content);
		Pattern urlPattern=Pattern.compile(IMGURLREGEX);
		while (imgMatcher.find()) {
			Matcher urlMatcher=urlPattern.matcher(imgMatcher.group());
			while (urlMatcher.find()) {
				String srcUrl=urlMatcher.group().replace("\"", "");
				imgSrcs.add(srcUrl);
			}
		}
		return imgSrcs;
	}
}
