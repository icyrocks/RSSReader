package whu.dellinet.rssreader.entity;

import java.io.Serializable;
import java.util.List;

public class ItemListEntity implements Serializable{
	private List<FeedItem> itemsList;

	public List<FeedItem> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<FeedItem> itemsList) {
		this.itemsList = itemsList;
	}
	
	
}
