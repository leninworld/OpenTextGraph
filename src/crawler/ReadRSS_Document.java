package crawler;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
 

public class ReadRSS_Document {
	
	public static boolean readRSS_Document(String urlString){
		boolean bool_isRSS=false;
		try {
			URL url = new URL(urlString);
			 
			HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
			httpcon.setConnectTimeout(1000);
			httpcon.setReadTimeout(1000);
			System.out.println("after http");
			// Reading the feed
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(httpcon));
			System.out.println("after feed read");
			List entries = feed.getEntries();
			Iterator itEntries = entries.iterator();
			System.out.println("iterate");
			bool_isRSS=true;
			
			
//			while (itEntries.hasNext()) {
//				SyndEntry entry = (SyndEntry) itEntries.next();
//				System.out.println("Title: " + entry.getTitle());
//				System.out.println("Link: " + entry.getLink());
//				System.out.println("Author: " + entry.getAuthor());
//				System.out.println("Publish Date: " + entry.getPublishedDate());
//				System.out.println("Description: " + entry.getDescription().getValue());
//				System.out.println();
//			}
			
			return bool_isRSS;
		} catch (Exception e) {
			// TODO: handle exception
			bool_isRSS=false;
			System.out.println("error");
		}
		return bool_isRSS;
	}
	
	//
	public static void main(String[] args) throws Exception {
		String url="http://feeds.feedburner.com/manishchhabra27";
		//url="http://www.rgagnon.com/javadetails/java-0556.html";
		readRSS_Document(url);
	}

}
