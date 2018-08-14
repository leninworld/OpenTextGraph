package crawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.*;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

// find is RSS
public class Find_is_RSS {
	// find is RSS	
	public static boolean find_is_RSS(String URL){
		DocumentBuilder builder;
		int timeout_second=10; // milli second
		boolean boolRSS=false;
		URL url=null;
		URLConnection con=null;
		Document doc =null;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			System.out.println("before parse:"+URL);
//			Document doc = builder.parse(URL) ; // url is your url for testing
			
			   url = new URL(URL);
	           con = url.openConnection();
	           con.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
				//openConnection.addRequestProperty("User-Agent", "Mozilla");
	           con.addRequestProperty("Referer", "google.com");
	           con.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
	           con.setRequestProperty("Accept-Language","en-us,en;q=0.5");
	           con.setRequestProperty("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");
				
	           con.setRequestProperty("User-Agent","Mozilla/5.0 (X11; U; Linux i686; en-US)");
	           
	           
	           System.out.println("before parse 1:: "+timeout_second);
	           con.setConnectTimeout(timeout_second);//The timeout in mills
	             
	           con.setReadTimeout( 3000 );
	           System.out.println("before parse 2");
	           //con.setAllowUserInteraction(true);
	           InputStream iStream = con.getInputStream (); 
	           doc = builder.parse(iStream);
			
			//doc.getDocumentElement().getNodeName().equalsIgnoreCase("rss");
			//System.out.println("atom"+doc.getDocumentElement().getNodeName().equalsIgnoreCase("atom"));
			System.out.println("after parse");
			boolRSS=doc.getDocumentElement().getNodeName().equalsIgnoreCase("rss");
			System.out.println("boolRSS:"+boolRSS+"; getNodeName():"+doc.getDocumentElement().getNodeName());
			return boolRSS;
		} catch ( Exception e) {
			System.out.println("boolRSS:"+boolRSS);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		return boolRSS;
	}
	
	public static boolean isAtom(String URL) throws ParserConfigurationException, SAXException, IOException{
	    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
	    f.setNamespaceAware(true);
	    DocumentBuilder builder = f.newInstance().newDocumentBuilder();
	    Document doc = builder.parse(URL);
	    Element e = doc.getDocumentElement(); 
	    System.out.println(e);	
	    
	    return e.getLocalName().equals("feed") && 
	            e.getNamespaceURI().equals("http://www.w3.org/2005/Atom");
	}
	// main
	public static void main(String[] args) throws Exception{
		String url="http://mf.feeds.reuters.com/reuters/UKSportsNews";
		url="http://www.thehindu.com/news/cities/chennai/?service=rss";
		url="http://mf.feeds.reuters.com/reuters/UKMotorSportsNews";
		url="http://mf.feeds.reuters.com/reuters/UKSportsNews";
		url="http://docs.oracle.com/javase/7/docs/api/javax/xml/parsers/DocumentBuilder.html";
		url="http://timesofindia.feedsportal.com/c/33039/f/533916/index.rss";
		url="http://www.opensocietyfoundations.org/topics/black-male-achievement/feed";
		//isAtom(url);
		
		find_is_RSS(url);
		
		
	}
	

}
