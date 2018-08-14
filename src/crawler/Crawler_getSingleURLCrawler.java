package crawler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

public class Crawler_getSingleURLCrawler {
	
	// get single URL crawled
	public static TreeMap<Integer, String> crawler_getSingleURLCrawler(	  
															String singleURL
															, boolean is_Custom_News_All_Element_Parsing
															, boolean isDebug) {
		String DateTime = "";
		int cnt = 0;
		String bodyText = "";
		TreeMap<Integer, String> out = new TreeMap<Integer, String>();
		boolean isGtDate = false;
		String sourceUrlString = "http://www.kpcnews.com/news/latest/eveningstar/article_47a51423-5249-5347-b2fb-31bcf74a5c2c.html";
		if(singleURL.length()>0)
			sourceUrlString=singleURL;
		// "http://www.ndtv.com/article/india/seven-dead-after-ammonia-gas-leak-in-cold-storage-factory-in-gujarat-415722";
		// "http://somalilandsun.com/index.php/world/somalia/3699-somalia-al-shabaab-claims-responsibility-for-twin-blasts-that-killed-20-in-mogadishu-";
		// "http://www.pressdemocrat.com/article/20130907/articles/130909623?titleUkiah-man-dies,-passenger-hurt-in-crash";
		// "http://www.latimes.com/obituaries/la-me-ab-taylor-20130923,0,6884627.story";
		// http://www.foxnews.com/world/2013/09/15/mine-collapse-kills-27-in-north-afghanistan/";
		// http://www.leadertelegram.com/local_news/briefs/article_441af9a2-2406-11e3-a222-0019bb2963f4.html
		// "http://www.ptinews.com/news/4031916_Girl-alleges-SI-raped-her-in-police-station.html";
		// "http://www.wbtw.com/story/23488330/11-year-old";
		// "http://www.emirates247.com/8-teenage-girls-killed-in-bus-crash-2013-09-02-1.519629";
		// "http://www.kwch.com/kwch-kah-two-killed-in-crash-in-pratt-county-20130906,0,2733055.story";
		// "http://www.mumbaimirror.com/mumbai/crime/Teen-raped-strangled-body-dumped-near-Asangaon-stn/articleshow/22404989.cms";
		// "http://www.chron.com/news/houston-texas/houston/article/Houston-man-held-in-2009-death-of-girl-2-4830634.php?cmpidhtx";
		// http://www.mlive.com/news/muskegon/index.ssf/2013/09/deadly_shootout_at_muskegons_e.html";
		// "http://www.dnaindia.com/india/1882820/report-former-arunachal-minister-dead";
		// http://www.chron.com/news/houston-texas/houston/article/Houston-man-held-in-2009-death-of-girl-2-4830634.php?cmpidhtx
		// "http://articles.timesofindia.indiatimes.com/2013-09-19/bangalore/42216844_1_sambar-vessel-krishnappa-headmaster";
		// "http://www.foxnews.com/world/2013/09/15/mine-collapse-kills-27-in-north-afghanistan/";
		// "http://www.yorkregion.com/news-story/4116596-health-hazards-at-daycare-where-girl-died/";
		// http://www.chron.com/news/houston-texas/houston/article/Houston-man-held-in-2009-death-of-girl-2-4830634.php?cmpidhtx
		InputStream is = null;
		try {
			//approach 1:
			URLConnection openConnection = new URL(sourceUrlString).openConnection();
			//openConnection.addRequestProperty("User-Agent", "Firefox/25.0");
			//conn.setRequestProperty("Cookie", cookies);
			openConnection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			//openConnection.addRequestProperty("User-Agent", "Mozilla");
			openConnection.addRequestProperty("Referer", "google.com");
			openConnection.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			openConnection.setRequestProperty("Accept-Language","en-us,en;q=0.5");
			openConnection.setRequestProperty("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");
			
			openConnection.setRequestProperty("User-Agent","Mozilla/5.0 (X11; U; Linux i686; en-US)");
			  
			is = openConnection.getInputStream();
			Source source = new Source(is);
			
			//approach 2:
			//Source source = new Source(new URL(sourceUrlString) );
			
			String renderedText = source.getRenderer().toString().replace(" ", "#");
			String renderText2=source.getTextExtractor().setIncludeAttributes(true).toString();
			out.put(1, renderedText);
			out.put(2, renderText2);
			bodyText=renderedText; 
			//element read
			if(is_Custom_News_All_Element_Parsing){
			
			List<Element> elementList = source.getAllElements();
			for (Element element : elementList) {
				if ((element.toString().indexOf("2013") >= 0 || element
						.toString().indexOf("2012") >= 0)
						&& element.toString().toLowerCase().indexOf("href") == -1
						&& element.toString().toLowerCase().indexOf("script") == -1
						&& element.toString().toLowerCase().indexOf("copy") == -1
						&& element.toString().toLowerCase().indexOf("reserved") == -1
						&& element.toString().toLowerCase().indexOf("http") == -1
						&& element.toString().toLowerCase().indexOf("meta") == -1
						&& element.toString().toLowerCase().indexOf("press") == -1
						&& element.toString().toLowerCase().indexOf(".gif") == -1
						&& element.toString().toLowerCase().indexOf(".jpg") == -1
						&& element.toString().toLowerCase().indexOf(".png") == -1
						&& cnt <= 3 && isGtDate == false) {
					DateTime = element.toString().toLowerCase();
					if(isDebug)
						System.out.println("year range-> " + element.toString()+ " isGtDate:" + isGtDate);
					//
					if (DateTime.indexOf("am") >= 0
							|| DateTime.indexOf("pm") >= 0
							|| DateTime.indexOf("mon") >= 0
							|| DateTime.indexOf("tue") >= 0
							|| DateTime.indexOf("wed") >= 0
							|| DateTime.indexOf("thu") >= 0
							|| DateTime.indexOf("sat") >= 0
							|| DateTime.indexOf("sun") >= 0
							|| DateTime.indexOf(":") >= 0
							|| DateTime.indexOf("published") >= 0) {
						isGtDate = true;
						cnt = 10; // means we already got the date
					}
				} else if ((element.toString().indexOf("2013") >= 0 || element
						.toString().indexOf("2012") >= 0)
						&& element.toString().toLowerCase().indexOf("href") == -1
						&& element.toString().toLowerCase().indexOf("script") == -1
						&& element.toString().toLowerCase().indexOf("copy") == -1
						&& element.toString().toLowerCase().indexOf("http") == -1
						&& element.toString().toLowerCase().indexOf("meta") == -1
						&& element.toString().toLowerCase().indexOf("reserved") == -1
						&& element.toString().toLowerCase().indexOf("press") == -1
						&& element.toString().toLowerCase().indexOf(".jpg") == -1
						&& element.toString().toLowerCase().indexOf(".png") == -1
						&& element.toString().toLowerCase().indexOf("published") >= 0 
						&& isGtDate == false) {
					DateTime = element.toString().toLowerCase();
					isGtDate = true;
					cnt = 10; //
				}
				if(isDebug)
					System.out.println(" before tag is tht -> isGtDate:" + isGtDate);// +" elem:"+element.toString());

				// System.out.println(element.getDebugInfo());
				if ((element.getStartTag().getName() == HTMLElementName.P)
						&& element.toString().toLowerCase().indexOf("href") == -1
						&& element.toString().toLowerCase().indexOf("nav") == -1
						// && element.toString().toLowerCase().indexOf("style")
						// == -1
						&& element.toString().toLowerCase().indexOf("menu") == -1
						&& element.toString().toLowerCase().indexOf("img") == -1
						&& element.toString().toLowerCase().indexOf("copyright") == -1
						&& element.toString().toLowerCase().indexOf("photo gallery") == -1
						&& element.toString().toLowerCase().indexOf("follow us") == -1
						&& element.toString().toLowerCase().indexOf("h3") == -1
						&& element.toString().toLowerCase().indexOf("@") == -1
						&& element.toString().toLowerCase().indexOf(".") >= 0 // every
																				// sentence
																				// should
																				// have
																				// .
						&& isGtDate == true) {
					
					System.out.println(" p Tag:" + element);
					// did end of page come?
					if ((bodyText.indexOf("comment") >= 0 && 
							bodyText.indexOf("login") >= 0)
							|| bodyText.indexOf("you ") >= 0)
						break;

					bodyText = bodyText
							+ element.toString().replace("<p>", "")
									.replace("</p>", "").replace("<span>", "")
									.replace("</span>", "")
									.replace("<strong>", "")
									.replace("</strong>", "");
					cnt++;
				} // end of P tag
				/*
				 * else if ((element.getStartTag().getName() ==
				 * HTMLElementName.BR )
				 * 
				 * && isGtDate == true ){
				 * 
				 * bodyText= bodyText + element.toString().replace("<p>",
				 * "").replace("</p>", "");
				 * System.out.println(" BR tag -->"+bodyText);
				 * 
				 * 
				 * }
				 */

			} // end of for

			bodyText = bodyText.replace("&nbsp;", " ").replace("&#39;", "'").replace("<br>", " ");
			// get the body using <p> or <br>
			bodyText = removeTagsFromHTML(bodyText, true);
			System.out.println("\n post clean body--->" + bodyText
								+ "<------>isGtDate:" + isGtDate + " DateTime:" + DateTime);

			// get the header <h1> or <h2>
			String header = removeTagsFromHTML(source.getAllElements("h1")
								.toString().replace("[", "").replace("]", ""), true);

			System.out.println("\n location : "
								+ findLocationSpot(renderedText, bodyText));

			if (header.equals("") || header.length() <= 1) {
				header = removeTagsFromHTML(source.getAllElements("h2")
							.toString().replace("[", "").replace("]", ""), true);
				header = SubRemoveTagsFromHTML(header);
			}
			System.out.println("\n header--->" + header + "  isGtDate:"
								+ isGtDate);
			System.out.println("renderedText:"+renderedText);
			}// end is_All_Element_Parsing
			out.put(99, "success");
			out.put(100, bodyText);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("last.bodyText:"+bodyText+"<-");
		return out;
	}


	// displaySegments ( can be (1) includes header of <h1> (2) includes <p
	// class=""> </p> or <br>
	private static String removeTagsFromHTML(String bodyText, boolean isSOPprint) {

		if (isSOPprint == true)
			System.out.println("input:" + bodyText);
		
		int cnt = 0;
		// there can be multiple h1
		if (bodyText.indexOf("<h1") >= 0) {

			String s[] = bodyText.split("</h1>");
			// System.out.println("s.leng:"+s.length+" ;input header:"+bodyText);

			//
			while (cnt < s.length) {

				s[cnt] = s[cnt].replace("<h1>", "").replace("</h1>", "")
						.replace("  ", " ").replace("  ", " ");

				if (isSOPprint == true)
					System.out.println("1. s[cnt] : " + s[cnt] + " :cnt " + cnt
							+ " s[cnt].split().length:"
							+ s[cnt].split(" ").length);

				if (s[cnt].contains("<li") || s[cnt].contains("<ul")
						|| s[cnt].split(" ").length <= 2) {
					cnt++;
					continue; // header can't have list
				}
				//
				if (s[cnt].split(" ").length >= 5) {
					if (isSOPprint == true)
						System.out.println("2. cnt:" + cnt + ":" + "s[cnt]:"
								+ s[cnt]);

					return SubRemoveTagsFromHTML(s[cnt]);
				}
				cnt++;
			}
		} else if (bodyText.indexOf("<h2") >= 0) {
			int startIndex = bodyText.indexOf("<h2");
			int endIndex = bodyText.indexOf("</h2>", startIndex) + 1;

			return SubRemoveTagsFromHTML(bodyText.substring(startIndex,
					endIndex));

		}

		String s[] = null;

		try {
			String start_tag = "";
			String end_tag = "";

			//
			/*
			 * if(bodyText.indexOf("<p") >= 0 && bodyText.indexOf("</p") >= 0 ){
			 * start_tag = "<p"; end_tag = "<"; System.out.println("1.P \n"); }
			 * else if(bodyText.indexOf("<br") >= 0 ){ start_tag = "<br";
			 * end_tag = "<"; System.out.println("2.BR "); } else
			 */
			if (bodyText.indexOf("<") >= 0) {
				start_tag = "<";
				end_tag = ">";
				if(isSOPprint){
					System.out.println("3. < or > ");
				}
			} else
				return SubRemoveTagsFromHTML(bodyText);

			if(isSOPprint){
				System.out.println("\n --->first given bodyText:" + bodyText
						+ "\n start_tag:" + start_tag + " end_tag:" + end_tag);
			}

			int p_startIndex = 0;
			int p_endIndex = 0;
			// replace <> tags with ""
			while (bodyText.indexOf("<") >= 0) {
				// <p class=""> </p>
				int startIndex = bodyText.indexOf(start_tag);
				int endIndex = bodyText.indexOf(end_tag, startIndex) + 1;

				if ((bodyText.indexOf("<") == -1 && bodyText.indexOf(">") == -1)
						|| endIndex <= startIndex) {
					return bodyText;
				}

				if (isSOPprint == true && endIndex >= startIndex){
					System.out.println("to replace:"
							+ bodyText.substring(startIndex, endIndex)
							+ " bodyText:" + bodyText);
				}

				// find all the inner and outer ex: <p></p>
				/*
				 * if( bodyText.indexOf("<p") >=0 ){ System.out.println("11.");
				 * p_startIndex = bodyText.indexOf(">"); p_endIndex =
				 * bodyText.indexOf("<",p_startIndex+1); } else
				 * if(bodyText.indexOf("<br") >=0 ){ p_startIndex =
				 * bodyText.indexOf("<br"); p_endIndex =
				 * bodyText.indexOf("<br",p_startIndex+1);
				 * System.out.println("12. " + " s:"); } else
				 */
				if (bodyText.indexOf("<") >= 0) {

					p_startIndex = bodyText.indexOf("<");
					p_endIndex = bodyText.indexOf(">", p_startIndex + 1);
					if(isSOPprint)
						System.out.println("13. " + " s:");
				}

				if (p_startIndex >= 0 && p_endIndex >= 0
						&& p_endIndex >= p_startIndex) {

					s = bodyText.substring(p_startIndex, p_endIndex).split(" ");
					if(isSOPprint)
						System.out.println("3." + " s:" + s.toString());
				} else {
					if(isSOPprint)
						System.out.println("4.");
					s = bodyText.split(" ");
				}
				
				if(isSOPprint){
					System.out.println("s.length:" + s.length + " to replace:"
							+ bodyText + " startIndex, endIndex:" + startIndex
							+ " " + endIndex + " substring to remove:"
							+ bodyText.substring(startIndex, endIndex));
				}

				if (s.length > 2) {
					if(isSOPprint)
						System.out.println("5.");
					// if number of character lesser than one or two ,then don't
					// write.
					bodyText = bodyText.replace(
							bodyText.substring(startIndex, endIndex), "");
				} else {
					if(isSOPprint){
						System.out.println("6." + " bodyText-->" + bodyText
								+ " bodyText.substring(startIndex, endIndex):"
								+ bodyText.substring(startIndex, endIndex));
					}
					bodyText = bodyText.replace(
							bodyText.substring(startIndex, endIndex), "");

					continue;
				}

			} // while end

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bodyText;
	}

	// find the location (city, state) from the news
	public static String findLocationSpot(String renderedText, String bodyText) {
		String tmp = null;
		int beginIndex = 0;
		int endIndex = 0;
		String first20bodyText = "";
		try {
			System.out.println("1. inside findLocationSpot:\n renderedText:"
					+ renderedText + " \n 0.bodyText:" + bodyText);

			if (bodyText.length() >= 220) {
				first20bodyText = bodyText.substring(0, 220);
				System.out.println("1.2 inside first20bodyText : "
						+ first20bodyText);
			} else if (bodyText.length() >= 120) {
				first20bodyText = bodyText.substring(0, 120);
				System.out.println("1.3 inside first20bodyText : "
						+ first20bodyText);
			} else if (bodyText.length() >= 50) {
				first20bodyText = bodyText.substring(0, 50);
				System.out.println("1.4 inside first20bodyText : "
						+ first20bodyText);
			}

			System.out.println("\n 1.1.first20bodyText:" + first20bodyText);

			//
			while (first20bodyText.indexOf("2013") >= 0
					|| first20bodyText.indexOf("2012") >= 0
					|| first20bodyText.indexOf(".com") >= 0) {

				if (first20bodyText.indexOf("2013") >= 0)
					first20bodyText = first20bodyText.substring(
							first20bodyText.indexOf("2013") + 4,
							first20bodyText.length());
				else if (first20bodyText.indexOf("2012") >= 0)
					first20bodyText = first20bodyText.substring(
							first20bodyText.indexOf("2012") + 4,
							first20bodyText.length());
				else
					first20bodyText = first20bodyText.substring(
							first20bodyText.indexOf(".com") + 4,
							first20bodyText.length());

			}

			System.out.println("1.\n tmp:" + tmp + " first20bodyText:"
					+ first20bodyText);
			// remove junk
			first20bodyText = first20bodyText.toLowerCase().replace("afp/file",
					"");

			first20bodyText = first20bodyText.replace("  ", " ")
					.replace("  ", " ").replace("  ", " ").replace("\n", "")
					.replace("\r", "").replace("\t", "")
					.replaceAll("\\p{Cntrl}", "");

			//
			if ((first20bodyText.indexOf(":") >= 0
					|| first20bodyText.indexOf(",") >= 0
					|| first20bodyText.indexOf("—") >= 0
					|| first20bodyText.indexOf("--") >= 0 || first20bodyText
					.indexOf(")") >= 0)
					|| first20bodyText.indexOf("-") >= 0
					&& first20bodyText.indexOf("year") == -1) {

				System.out.println("\n index: "
						+ first20bodyText.indexOf("—") + " "
						+ first20bodyText.indexOf("-") + " "
						+ first20bodyText.indexOf(":") + " "
						+ first20bodyText.indexOf(")"));

				if (first20bodyText.indexOf("—") >= 0) {

					return first20bodyText.substring(0,
							first20bodyText.indexOf("—"));

				}

				if (first20bodyText.indexOf("--") >= 0) {
					return first20bodyText.substring(0,
							first20bodyText.indexOf("--"));
				}

				if (first20bodyText.indexOf(":") >= 0) {
					return first20bodyText.substring(0,
							first20bodyText.indexOf(":"));
				}

				if (first20bodyText.indexOf(")") >= 0) {
					return first20bodyText.substring(0,
							first20bodyText.indexOf(")") + 1);
				}

				if (first20bodyText.indexOf("-") >= 0) {

					if (first20bodyText.split(" ").length > 3)
						return "dummy";
					else
						return first20bodyText.substring(0,
								first20bodyText.indexOf("-"));
				}

			} else
				return "dummy";

			return "dummy";

			// find the string just before the first word (first20bodyText) of
			// bodyText
			/*
			 * if(renderedText.indexOf(first20bodyText) >=0) tmp =
			 * renderedText.substring(0,renderedText.indexOf(first20bodyText) );
			 * 
			 * 
			 * if(tmp == null) return "tmpnull";
			 * 
			 * return "dummy";
			 */
			/*
			 * if(tmp.length()>70) tmp = tmp.substring( tmp.length() -40 ,
			 * tmp.length());
			 * 
			 * System.out.println("tmp:"+tmp
			 * +" first20bodyText:"+first20bodyText);
			 * 
			 * if(tmp.indexOf(":") >=0 || tmp.indexOf(",") >=0 ||
			 * tmp.indexOf("-") >=0 ){
			 * 
			 * if(tmp.indexOf("-") >=0) return tmp; else if(tmp.indexOf(",")
			 * >=0) return tmp; else if(tmp.indexOf(":") >=0) return tmp;
			 * 
			 * } else return "dummy";
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp;
	}
	
	// remove the tag
		private static String SubRemoveTagsFromHTML(String bodyText) {
			try {
				while (bodyText.indexOf("<") >= 0) {
					// <p class=""> </p>
					int startIndex = bodyText.indexOf("<");
					int endIndex = bodyText.indexOf(">", startIndex) + 1;
					if (endIndex > startIndex)
						bodyText = bodyText.replace(
								bodyText.substring(startIndex, endIndex), "");
					else
						return bodyText;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return bodyText;
		}
		//
		public static void main(String[] args) throws IOException {
			
			String url="http://carnegieendowment.org/experts/?fa=437";
			
			
			url="https://en.wikipedia.org/wiki/International_Yoga_Day";
			url="http://timesofindia.indiatimes.com/india/Pakistan-invites-Hurriyat-but-govt-finds-a-way-to-save-NSA-talks/articleshow/48549986.cms";
	
			//replace by another method
			TreeMap<Integer, String> mapOut=crawler_getSingleURLCrawler(url, false, false);
			
			String text1=mapOut.get(1);
			String text2=mapOut.get(2);
			System.out.println("\n----text2.start:\n-------------------");
			System.out.println(text2);
			System.out.println("text2.end:\n-------------------");
			System.out.println("\n----text1.start:\n-------------------");
			System.out.println(text1);
			System.out.println("text1.end:\n-------------------");
			
			
			
//			TreeMap<Integer, String> mapOut2=
//			find_Start_n_End_Index_Of_GivenString.getMatchedLines
//						(text1, start_tag, end_tag, false);
//
//			  System.out.println(mapOut2.get(1).replace("#", " ") );
			  
	 
		}
		
	
}
