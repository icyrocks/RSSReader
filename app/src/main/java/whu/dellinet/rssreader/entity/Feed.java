package whu.dellinet.rssreader.entity;

public class Feed {
	private String title;
	private String url;
	private int selectState;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSelectState() {
		return selectState;
	}
	public void setSelectState(int selectState) {
		this.selectState = selectState;
	}
	
}
