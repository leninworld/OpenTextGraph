package crawler;

import net.htmlparser.jericho.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class Find_encoding_url {
		// HTTP GET request
		private static void send_http_get(String url2) throws Exception {
			String USER_AGENT = "Mozilla/5.0";
			String url = url2;
	 
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
	 
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			System.out.println(response.toString());
	 
		}
	 
	
	public static void DisplayAllElements4(String url2){
		try{
			
			Source source = new Source("<TABLE  cellspacing=0>\n" +
				    "  <tr><td>HELLO</td>  \n" +
				    "  <td>How are you</td></tr>\n" +
				    "</TABLE>");

				Element table = source.getFirstElement();
				String tableContent = table.getContent().toString();

				System.out.println(tableContent);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//display all elements
	public static void DisplayAllElements3(String url2){
		try{ 

			if (url2.indexOf(':')==-1) url2="file:"+url2;
			MicrosoftConditionalCommentTagTypes.register();
			PHPTagTypes.register();
			MasonTagTypes.register();
			Source source=new Source(new URL(url2));
	
			new SourceCompactor(source).writeTo(new OutputStreamWriter(System.out));
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	
	//display all elements
	public static void DisplayAllElements2(String url2){
		try{
		Source source=new Source(new URL(url2));

		TextExtractor textExtractor=new TextExtractor(source);
		System.out.println(textExtractor.toString());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	// display all elements
	public static void DisplayAllElements(String url2){
		try{
			String sourceUrlString=url2;
			
			if (sourceUrlString.indexOf(':')==-1) {
				sourceUrlString="file:"+sourceUrlString;
			}
			MicrosoftConditionalCommentTagTypes.register();
			PHPTagTypes.register();
			PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags for this example otherwise they override processing instructions
			MasonTagTypes.register();
			Source source=new Source(new URL(sourceUrlString));
			List<Element> elementList=source.getAllElements();
			//
			for (Element element : elementList) {
				System.out.println("-------------------------------------------------------------------------------");
				System.out.println(element.getDebugInfo());
				if (element.getAttributes()!=null) System.out.println("XHTML StartTag:\n"+element.getStartTag().tidy(true));
				System.out.println("Source text with content:\n"+element);
				System.out.println("all:"+element.getAllElements());
			}
			System.out.println(source.getCacheDebugInfo());
		}
		catch(Exception e){
			
		}
	}
	
	public static void find_encoding_url(String url2){
		
		String sourceUrlString=url2;
		 
		try{
			if (sourceUrlString.indexOf(':')==-1) 
				sourceUrlString="file:"+sourceUrlString;
			
			System.out.println("\nSource URL:");
			System.out.println(sourceUrlString);
			URL url=new URL(sourceUrlString);
			Source source=new Source(url);
			System.out.println("\nDocument Title:");
			Element titleElement=source.getFirstElement(HTMLElementName.TITLE);
			System.out.println(titleElement!=null ? titleElement.getContent().toString() : "(none)");
			System.out.println("\nSource.getEncoding():");
			System.out.println(source.getEncoding());
			System.out.println("\nSource.getEncodingSpecificationInfo():");
			System.out.println(source.getEncodingSpecificationInfo());
			System.out.println("\nSource.getPreliminaryEncodingInfo():");
			System.out.println(source.getPreliminaryEncodingInfo());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//
	public static void main(String[] args) throws Exception {
		String url="http://www.thehindu.com/thehindu/2005/12/30/23hdline.htm";
		url="http://www.thehindu.com/thehindu/2005/12/31/14hdline.htm";
		//find_encoding_url.find_encoding_url(url);
		//find_encoding_url.DisplayAllElements4(url);
		Find_encoding_url.send_http_get(url);
	}
}

