package whu.dellinet.rssreader.custom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpStatus;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.util.Log;

import whu.dellinet.rssreader.entity.FeedItem;
import whu.dellinet.rssreader.entity.ItemListEntity;
import whu.dellinet.rssreader.util.HttpUtils;

public class ItemsParaser implements ContentHandler {
	public static final String TAG="ItemsParaser";
	private ItemListEntity itemListEntity;
	private List<FeedItem> feedItems=new ArrayList<FeedItem>();
	private FeedItem item;
	private StringBuffer buffer=new StringBuffer();
	private boolean isDescribed;
	private boolean startItem=false;
	
	public  ItemListEntity paraser(String url) {
		SAXParserFactory saxParserFactory=SAXParserFactory.newInstance();
		SAXParser saxParser=null;
		InputStream inputStream=null;
		InputSource inputSource=null;
		
		
		
		try {
			inputStream=HttpUtils.getInputStreamByUrl(url);
			
			inputSource=new InputSource(inputStream);
			
			
			saxParser=saxParserFactory.newSAXParser();
			XMLReader xmlReader=saxParser.getXMLReader();
			xmlReader.setContentHandler(this);
			xmlReader.parse(inputSource);
			return itemListEntity;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			if (inputStream!=null) {
				try {
					inputStream.close();
					inputStream=null;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		// TODO Auto-generated method stub
		buffer.append(arg0, arg1, arg2);
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		Log.i(TAG, "结束解析");
		itemListEntity.setItemsList(feedItems);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		String content=buffer.toString();
//		Log.i(TAG, "内容为： "+content);
		if (qName.equalsIgnoreCase("title")&&startItem) {
			item.setTitle(content);
			return;
		}else if (qName.equalsIgnoreCase("link")&&startItem) {
			item.setLink(content);
			return;
		}else if (qName.equalsIgnoreCase("pubDate")&&startItem) {
			item.setPubdate(content);
			return;
		}else if (startItem&&(qName.equalsIgnoreCase("description")||qName.equalsIgnoreCase("content:encoded"))) {
//			if (isDescribed) {
//				
//			}
			item.setContent(content);
			List<String> imgSrcsUrls=HtmlFilter.getImgSrcs(content);
			
			item.setImageUrls(imgSrcsUrls);
			if (!imgSrcsUrls.isEmpty()&&imgSrcsUrls.size()>0) {
				item.setFirstImageUrl(imgSrcsUrls.get(0));
			}
			isDescribed=true;
			
			return;
		}else if (startItem&&qName.equalsIgnoreCase("item")) {
			startItem=false;
			return;
		}
	}

	

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		Log.i(TAG, "开始解析");
		itemListEntity=new ItemListEntity();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes atts) throws SAXException {
		// TODO Auto-generated method stub
		buffer.setLength(0);
		if (!startItem&&qName.equalsIgnoreCase("item")) {
			
			
			item=new FeedItem();
			feedItems.add(item);
			startItem=true;
			isDescribed=false;
		}
	}

	@Override
	public void endPrefixMapping(String arg0) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processingInstruction(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDocumentLocator(Locator arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skippedEntity(String arg0) throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startPrefixMapping(String arg0, String arg1)
			throws SAXException {
		// TODO Auto-generated method stub
		
	}


}
