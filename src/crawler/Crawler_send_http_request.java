package crawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Properties;
import java.util.TreeMap;

public   class Crawler_send_http_request {
	//google search API
	public static TreeMap<Integer, String> crawler_send_http_get(
													 String 	url2,
													 String 	input_proxy_ip_address_file
												 	){
		Proxy proxy=null;
		int responseCode=-1;
		TreeMap<Integer, String> mapProxyList= new TreeMap<Integer, String>();
		StringBuffer response = new StringBuffer();
		TreeMap<Integer, String> mapOut=new TreeMap<Integer, String>();
		try{
		// input proxy file not empty
		if(!input_proxy_ip_address_file.equalsIgnoreCase("") 
			|| input_proxy_ip_address_file.length()>4){
			mapProxyList=ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
				  readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line
					 (input_proxy_ip_address_file
							 ,-1
							 ,-1
							 , "" //debug_label
							 , true // isPrintSOP
							 );
		}
		int size_of_proxy_list=mapProxyList.size();
		int random_number=1; String proxy_ip_and_port="";
		 
		 if(size_of_proxy_list>1){
			 Generate_Random_Number_Range.generate_Random_Number_Range(1, size_of_proxy_list);
		 	 proxy_ip_and_port=mapProxyList.get(random_number);
		 }
		Properties systemProperties = System.getProperties();
		String [] s=proxy_ip_and_port.split("!!!");
		System.out.println("proxy_ip_and_port:"+proxy_ip_and_port);
		HttpURLConnection con=null;
		//
		if(proxy_ip_and_port.length()>=4){
			InetSocketAddress local=new InetSocketAddress(s[0],Integer.valueOf(s[1]));
			proxy = new Proxy(	Proxy.Type.HTTP, 
								local
							  );
			
			systemProperties.setProperty("http.proxyHost",s[0]);
			systemProperties.setProperty("http.proxyPort",s[1]);	 
		}
		String USER_AGENT = "Mozilla/5.0";
		String url = url2;

		System.out.println("crawler_send_http_get.currurl:-->URL:"+url);
		//NULL
		if(url.length()<=4){ 
			mapOut.put(2, "-9" ); //failed
			return mapOut;
		}
		URL obj = new URL(url);
		//proxy given
		if(proxy_ip_and_port.length()>=4){//NOT null
			con = (HttpURLConnection) obj.openConnection(proxy);
		}
		else{
			con = (HttpURLConnection) obj.openConnection();
		}
 
		// optional default is GET
		con.setRequestMethod("GET");
 
		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		try{
			con.setConnectTimeout(22);
		responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()) );
		
		String inputLine;
		
		//
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		}
		catch(Exception e){
			response.append("failed");
		}
		}
		catch(Exception e){
			System.out.println(" error: "+e.getMessage());
		}
		//print result
		
		mapOut.put(1, response.toString());
		mapOut.put(2, String.valueOf(responseCode) );
		
		//System.out.println(response.toString());
		return mapOut;
	}
	// main
	public static void main(String[] args) throws Exception {
		String url="http://www.thehindu.com/thehindu/2005/12/31/14hdline.htm";
		url="https://www.google.com/search?q=Adam+Smith+Institute+United+Kingdom+rss+&num=20&ie=utf-8&oe=utf-8";
		String proxy_file="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/proxy-ip-address-usa.txt";
		System.out.println(crawler_send_http_get(url,proxy_file)
							);
	}

}
