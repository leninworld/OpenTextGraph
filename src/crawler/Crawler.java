package crawler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.PerformanceMonitor;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import org.bson.Document;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.MongoId;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.RecursiveAction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

//import crawler.convertGBAD2JSON;
//import crawler.convertGBAD2JSON$;
//

//import com.fasterxml.jackson.core.JsonFactory;
//import com.json.parsers.JsonParserFactory;

class Friend {
	// manual
	@MongoId
	private long _id;
	private String url;
}

class Friendurlandbody {
	// manual
	@MongoId
	private long _id;
	private String url;
	private String body;
}

// crawler
public class Crawler extends RecursiveAction {
	//
	public Crawler() {
		System.out.println(" const//");
		compute();
	}

	@Override
	public void compute() {
		System.out.println(" here//");
	}
	//clean_retain_only_alpha_numeric_characters
	public static String clean_retain_only_alpha_numeric_characters(
			String 	inString,
			boolean 	is_Convert_to_lowercase
			){

		//
		if(is_Convert_to_lowercase)
			return inString.replaceAll("[^a-zA-Z0-9]+","").toLowerCase();
		else
			return inString.replaceAll("[^a-zA-Z0-9]+","");
	
	}
	
	// wrapper_getNLPTrained_en_pos_maxent
	// Give: inputSingleURL_OR_input1 (OR) input_ExtractedText_OR_input2
	// REFERENCE: http://opennlp.sourceforge.net/models-1.5/
	public static TreeMap<String, String> wrapper_getNLPTrained_en_pos_maxent(
															String inputSingleURL_AS_input1, // input_1 OR give input_2
															String input_ExtractedText_AS_input2, // input_1 OR give input_2
															String NLPfolder, 
															String Flag, // {"en-pos-maxent.bin"}
															String Flag_2,  // {P,P,O,L,D,T=Person, Percentage,
																			// Organization,Location,Data,Time;}
															boolean isSOPdebug,
															boolean is_overwrite_flag_with_flag_model,//is_overwrite_flag_with_flag_model
															POSModel inflag_model2, // model of "en-pos-maxent.bin" etc
															ParserModel inflag_model2_chunking, //pass as NULL if dont want use, model of "en-parse-chunking.bin" etc
															String debugFile
														) {
		String bodyText = "";
		TreeMap<String, String> maptaggedNLP = new TreeMap<String, String>();
		boolean is_NLP_run = true;
		TreeMap<String, String> mapOut = new TreeMap<String, String>();
		try {
			FileWriter writerdebugOutErrorAndURLFile = new FileWriter(new File(debugFile), true );
			TreeMap<String, String> mapEmailFileName = new TreeMap();

			// NOT null
			if (inputSingleURL_AS_input1.length() >= 4) {

				mapOut = readRenderedTextANDextractFeatures(-1, NLPfolder,
															inputSingleURL_AS_input1, mapEmailFileName, is_NLP_run,
															writerdebugOutErrorAndURLFile, isSOPdebug, debugFile,
															Flag_2, //
															is_overwrite_flag_with_flag_model,
															inflag_model2,
															inflag_model2_chunking 
															);
			}
			// TreeMap<Integer, String> mapOut=
			// crawler_getSingleURLCrawler.crawler_getSingleURLCrawler(inputSingleURL,
			// true, //is_Custom_News_All_Element_Parsing,
			// true //isDebug
			// );

			if (isSOPdebug)
				System.out.println("Printing tagged:" + mapOut.size());
			//
			for (String i : mapOut.keySet()) {
				if (mapOut.get(i) != null) {
					if (mapOut.get(i).length() > 100)
						System.out.println("each: " + i + "------>"
								+ mapOut.get(i).substring(0, 100));
					else
						System.out.println("each: " + i + "--al-->"
								+ mapOut.get(i));
				} else {
					System.out.println("each: (null) " + i);
				}
			}

			// bodyText=mapOut.get(100).replace("#", " ");
			if (isSOPdebug)
				System.out.println("NLPfolder:" + NLPfolder);
			// IF URL is NULL
			if (inputSingleURL_AS_input1.length() <= 4) {
				bodyText = input_ExtractedText_AS_input2;
			}

			if (isSOPdebug) {
				System.out.println("calling ... getNLPTrained");
			}

			//getNLPTrained
			maptaggedNLP = Crawler.getNLPTrained(NLPfolder,
												Flag, // "en-pos-maxent.bin"
												Flag_2, 
												bodyText, 
												null, 
												writerdebugOutErrorAndURLFile,
												is_overwrite_flag_with_flag_model,
												inflag_model2,
												inflag_model2_chunking,
												isSOPdebug);

			if (isSOPdebug)
				System.out.println("\n ---------start--" + Flag
						+ "---------------maptaggedNLP.size:"
						+ maptaggedNLP.size() + " bodyText.len:"
						+ bodyText.length());
			//
			for (String i : maptaggedNLP.keySet()) {
				if (isSOPdebug)
					System.out.println("(tagged) each: " + i + "(" + i.length()
							+ "---->" + maptaggedNLP.get(i));
			}
			if (isSOPdebug)
				System.out.println("\n ---------end----" + Flag
						+ "--------------");

			writerdebugOutErrorAndURLFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maptaggedNLP;
	}

	// read rendered text and extract features, NLP
	public static TreeMap<String, String> readRenderedTextANDextractFeatures(
													int inLineNo, String NLPfolder, String sourceUrlString,
													TreeMap<String, String> mapEmailFileName, boolean is_NLP_run,
													FileWriter writerdebugOutErrorAndURLFile, boolean isSOPprint,
													String debugFile, String flag2,
													boolean is_overwrite_flag_with_flag_model,//is_overwrite_flag_with_flag_model
													POSModel inflag_model2,
													ParserModel inflag_model2_chunking
													) {
		TreeMap<String, String> mapOut = new TreeMap();
		TreeMap<String, String> maptaggedNLP = null;
		// String
		// debugFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/debug.txt";
		// //TBD
		Source source = null;
		try {
			try {
				System.out.println("sourceUrlString:" + sourceUrlString);
				// don't crawl if data already exists in db
				// URL url = new URL(sourceUrlString);
				// URLConnection conn = url.openConnection();
				// conn.setConnectTimeout(1000);
				// conn.setReadTimeout(2000);
				// source=new Source(conn);
				source = new Source(new URL(sourceUrlString));
			} catch (Exception e) {
				System.out.println("e.msg:" + e.getMessage() + " ;exception:"
						+ sourceUrlString);
				writerdebugOutErrorAndURLFile.append("\ne.msg:"
						+ e.getMessage() + " ;exception:" + sourceUrlString
						+ "\n");
				writerdebugOutErrorAndURLFile.flush();
				mapOut.put("status", "failed");
				return mapOut;
			}
			System.out.println("got url:: " + sourceUrlString);
			if (isSOPprint) {// big
				System.out.println("source:" + source);
			}
			String renderedText = source.getRenderer().toString()
					.replace(" ", "#");
			// NOT english
			if (!Find_Language_from_String.find_Language_from_String_2(
					renderedText, debugFile)) {
				mapOut.put("status", "failed");
				return mapOut;
			}

			String DateTime = "";
			int cnt = 0;
			String renderedText1 = source.getTextExtractor().toString();

			String bodyText = "";
			// System.out.println("\nrenderedText1:" + renderedText1);

			// String
			// renderedText2=source.getTextExtractor().setIncludeAttributes(false).toString();
			// System.out.println("\n renderedText2:"+renderedText2);

			System.out.println("\nSimple rendering of the HTML document:\n");
			// System.out.println(renderedText);
			// displaySegments(source.getAllStartTags("<p>"));

			// System.out.println(source.fullSequentialParse());
			System.out.println("head->" + source.getAllElements("h1"));
			// System.out.println("tx1:"+renderedText1);
			// System.out.println("tx2:"+renderedText2);
			boolean isGtDate = false;
			List<Element> elementList = source.getAllElements();

			// System.out.println(elementList);
			System.out
					.println("-----------------------------start--------------------------------------------------");

			for (Element element : elementList) {
				//
				if ((element.toString().indexOf("2014") >= 0
						|| element.toString().indexOf("2013") >= 0 || element
						.toString().indexOf("2012") >= 0)
						&& element.toString().toLowerCase().indexOf("href") == -1
						&& element.toString().toLowerCase().indexOf("script") == -1
						&& element.toString().toLowerCase().indexOf("copy") == -1
						&& element.toString().toLowerCase().indexOf("reserved") == -1
						&& element.toString().toLowerCase().indexOf("http") == -1
						&& element.toString().toLowerCase().indexOf("meta") == -1
						&& element.toString().toLowerCase().indexOf("press") == -1
						&& element.toString().toLowerCase().indexOf(".jpg") == -1
						&& element.toString().toLowerCase().indexOf(".png") == -1
						&& cnt <= 3 && isGtDate == false) {
					DateTime = element.toString().toLowerCase();
					System.out.println("year range-> " + element.toString()
							+ " isGtDate:" + isGtDate);
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
				} else if ((element.toString().indexOf("2014") >= 0
						&& element.toString().indexOf("2013") >= 0 || element
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
						&& element.toString().toLowerCase()
								.indexOf("published") >= 0 && isGtDate == false) {
					DateTime = element.toString().toLowerCase();
					isGtDate = true;
					cnt = 10; //
				}

				// System.out.println(" before tag is tht -> isGtDate:" +
				// isGtDate);// +" elem:"+element.toString());
				// System.out.println(element.getDebugInfo());

				if ((element.getStartTag().getName() == HTMLElementName.P)
						&& element.toString().toLowerCase().indexOf("href") == -1
						&& element.toString().toLowerCase().indexOf("nav") == -1
						// &&
						// element.toString().toLowerCase().indexOf("style")
						// == -1
						&& element.toString().toLowerCase().indexOf("menu") == -1
						&& element.toString().toLowerCase().indexOf("img") == -1
						&& element.toString().toLowerCase()
								.indexOf("copyright") == -1
						&& element.toString().toLowerCase()
								.indexOf("photo gallery") == -1
						&& element.toString().toLowerCase()
								.indexOf("follow us") == -1
						&& element.toString().toLowerCase().indexOf("h3") == -1
						&& element.toString().toLowerCase().indexOf("@") == -1
						&& element.toString().toLowerCase().indexOf(".") >= 0 // every
																				// sentence
																				// should
																				// have
																				// .
						&& isGtDate == true) {
					if (isSOPprint) {
						System.out.println(" p Tag:" + element);
					}

					// did end of page come?
					if ((bodyText.indexOf("comment") >= 0 && bodyText
							.indexOf("login") >= 0)
							|| bodyText.indexOf("you tub") >= 0
							|| bodyText.indexOf("article") >= 0
							|| bodyText.indexOf("all rights") >= 0) {
						writerdebugOutErrorAndURLFile.append("breaking:"
								+ "<->" + bodyText.indexOf("comment") + "<->"
								+ bodyText.indexOf("login") + "<->"
								+ bodyText.indexOf("you tub") + "<->"
								+ bodyText.indexOf("article") + "<->"
								+ bodyText.indexOf("all rights"));
						writerdebugOutErrorAndURLFile.flush();
						break;
					}

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

			System.out
					.println("-------------------------------end------------------------------------------------"
							+ DateTime);
			if (isSOPprint) {
				System.out.println("\n header:" + source.getAllElements("h1"));
				if (bodyText.length() > 101)
					System.out.println("\n body:(1000)"
							+ bodyText.substring(0, 100));
			}

			bodyText = bodyText.replace("&nbsp;", " ").replace("&#39;", "'")
					.replace("<br>", " ");

			bodyText = removeTagsFromHTML(bodyText, isSOPprint);
			if (isSOPprint) { // big
				if (bodyText.length() > 101)
					System.out.println("\n post clean body:"
							+ bodyText.substring(0, 100));
			}

			String location = findLocationSpot(renderedText, bodyText);

			if (location.split(" ").length >= 4)
				location = "dummy";
			if (isSOPprint) {
				System.out.println("\n location : " + location);
			}

			String header = removeTagsFromHTML(source.getAllElements("h1")
					.toString().replace("[", "").replace("]", ""), isSOPprint // isDebug
			);

			if (header.equals("") || header.length() <= 1)
				header = removeTagsFromHTML(source.getAllElements("h2")
						.toString().replace("[", "").replace("]", ""), true);

			System.out.println("\n header:" + header);
			String bodyNLP = "";
			HashMap<Integer, String> map = new HashMap<Integer, String>();
			// if its inFlag=file and outFlag=mongodb, then write to MongoDB
			// if (inFlag.equalsIgnoreCase("file") //input file has only URLs to
			// be crawled
			// && outFlag.equalsIgnoreCase("mongodb")) {

			// getNLPTrained(folder,"en-parser-chunking.bin",header,);
			// writer.append(sourceUrlString+"#"+header
			// +"#"+bodyText+"\n");
			// writer.flush();
			// System.out.println("tx3:"+source.getParseText().toString());

			// bodyText = bodyText.replace("\n", "").replace("\t",
			// "").replace("\r", "").replaceAll("  ", " ");
			bodyText = bodyText.replaceAll("  ", " ").replaceAll("\\n", "")
					.replaceAll("\\t", "").replaceAll("\\r", "");

			// BasicDBObject example
			if (isSOPprint) {
				if (bodyText.length() > 101)
					System.out.println("main_news example(100)..."
							+ bodyText.substring(0, 100) + "\n");
			}
			// }

			mapOut.put("url", sourceUrlString);
			mapOut.put("header", header);
			mapOut.put("body", bodyText);
			mapOut.put("renderedText", renderedText.replace(" ", "#"));
			mapOut.put("location", location);
			mapOut.put("DateTime", DateTime);
			writerdebugOutErrorAndURLFile.append("\n#mapEmailFileName:"
					+ mapEmailFileName + "#");
			writerdebugOutErrorAndURLFile.flush();
			if (inLineNo > 0) {
				if (mapEmailFileName.containsKey(String.valueOf(inLineNo))) {
					mapOut.put("EmailFileName",
							mapEmailFileName.get(String.valueOf(inLineNo)));
				}
			}
			// mapOut.put("EmailFileDT",
			// mapEmailFileName.get(i).substring(0,8));

			if (is_NLP_run) {
				maptaggedNLP = Crawler.getNLPTrained(NLPfolder,
													"en-pos-maxent.bin", flag2, bodyText, null,
													writerdebugOutErrorAndURLFile,
													is_overwrite_flag_with_flag_model,
													inflag_model2,
													inflag_model2_chunking,
													isSOPprint);

				mapOut.put("taggedNLP", maptaggedNLP.get("taggedNLP"));
			}
			writerdebugOutErrorAndURLFile.flush();

			if (maptaggedNLP != null) {
				if (maptaggedNLP.containsKey("taggedNLP")) {
					writerdebugOutErrorAndURLFile.append("\n\nmap2:taggedNLP:"+ maptaggedNLP.get("taggedNLP"));
					writerdebugOutErrorAndURLFile.flush();
				}
			}

			// wrapper not required
			// map=Wrapperget2NLPTrained(NLPfolder, "en-pos-maxent.bin",
			// bodyText);
			// mapOut.put("bodyNLP", map.get(1));
			// call NLP
			// bodyNLP = map.get(1); // if(header.length() > 2){
			// map = Wrapperget2NLPTrained(NLPfolder, "en-pos-maxent.bin",
			// header);
			// String headerNLP = map.get(1);
			// mapOut.put("headerNLP", map.get(1) );

		} catch (Exception e) {
			try {
				writerdebugOutErrorAndURLFile.append("\n\n error:");
				writerdebugOutErrorAndURLFile.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
			}
			e.printStackTrace();
		}
		mapOut.put("status", "success");
		return mapOut;
	}

	// crawl only one url for testing
	public static TreeMap<String, String> getSingleURL_Crawled_Testing(
			String sourceUrlString) {
		TreeMap<String, String> mapOut = new TreeMap();
		Source source = null;

		try {
			System.out.println("sourceUrlString:" + sourceUrlString);
			// don't crawl if data already exists in db
			// URL url = new URL(sourceUrlString);
			// URLConnection conn = url.openConnection();
			// conn.setConnectTimeout(1000);
			// conn.setReadTimeout(2000);
			// source=new Source(conn);
			source = new Source(new URL(sourceUrlString));
			String renderedText = source.getRenderer().toString()
					.replace(" ", "#");

			System.out.println("crawled.test:" + renderedText);
			return mapOut;
		} catch (Exception e) {
			System.out.println("e.msg:" + e.getMessage() + " ;exception:"
					+ sourceUrlString);
			// writerdebugOutErrorAndURLFile.append(
			// "e.msg:"+e.getMessage()+" ;exception:" + sourceUrlString+"\n");
			// writerdebugOutErrorAndURLFile.flush();
		}
		mapOut.put("status", "failed");
		return mapOut;
	}

	// clean crawled files (blank lines in between)
	public static void getCleanedCrawledFiles(int noTokensNeeded,
			String delimiter, String inputFile, String outputFile,
			String debugFile) {
		String line = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
			FileWriter writer = new FileWriter(outputFile);
			FileWriter writerDebug = new FileWriter(debugFile);
			boolean isRightFormat = false;
			String[] s = null;
			String concLine = "";
			String lastLine = "";
			int lineNo = 0;
			int realLine = 0;
			writer.append("filename\turl\ttitle\tbody\n");
			writer.flush();
			//
			while ((line = reader.readLine()) != null) {
				lineNo++;
				line = line + ".";
				// System.out.println(";lineno:"+lineNo+";isRightFormat:"+isRightFormat+";lastLine:"+lastLine);
				//
				if (lineNo == 1) {
					lastLine = line;
				}
				s = lastLine.split(delimiter);
				//
				if (s.length == noTokensNeeded) {
					isRightFormat = true;
				} else
					isRightFormat = false;

				//
				if (s != null) {
					// System.out.println("lineNo:"+lineNo+";s.length:"+s.length
					// +";isRightFormat:"+isRightFormat+";line:"+line);
					// System.out.println(";lastline:"+lastLine);
				}
				writer.flush();
				//
				if (lineNo > 1) {
					if (s.length < noTokensNeeded) {
						lastLine = lastLine + line;
						// writer.append(lastLine+"\n");
						// writer.flush();
						// System.out.println("#### <noTokens:"+noTokensNeeded);
						// continue;
					} else if (s.length == noTokensNeeded) {
						realLine++;
						System.out.println(realLine + ";" + lastLine);
						writer.append(lastLine.toLowerCase().replace("!!!",
								"\t")
								+ "\n"); // so far accumulated
						writer.flush();
						writerDebug.append(realLine + "--"
								+ lastLine.split("!!!").length + "\n");
						writerDebug.flush();
						// System.out.println("==:"+noTokensNeeded);
						lastLine = line; //
					} else
						lastLine = line;
				} else if (lineNo == 1) {
					// System.out.println("@@@@@@@@@@@@lineNo==1:"+line);
					lastLine = line;
				}
				writer.flush();
				System.out.println("lNo:" + lineNo + ";s.len:" + s.length
						+ ";l:" + line + ";#######ll:" + lastLine);
			} // end of while
			System.out.println("in:" + inputFile + ";out:" + outputFile);

			writer.append(lastLine + "\n"); // so far accumulated
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
	// wrapper for get2NLPTrained (input: given String. It splits each para into
	// lines and call "en-parser-chunking.bin" )
	// public static HashMap<Integer, String> Wrapperget2NLPTrained(
	// String foldername, String flag, String Sentence) {
	// HashMap<Integer, String> mapOut = new HashMap();
	// try {
	// System.out.println("------------------Wrapperget2NLPTrained-----------------------------");
	// System.out.println("file:"+foldername + "en-parser-chunking.bin");
	// InputStream is2 = new FileInputStream(foldername +
	// "en-parser-chunking.bin");
	// ParserModel model3 = new ParserModel(is2);
	// Parser parser = ParserFactory.create(model3);
	//
	// String[] outV = null;
	// String[] s2 = null;
	// int cnt = 0;
	//
	// if (Sentence == null) {
	// mapOut.put(1, "blankCrawl");
	// return mapOut;
	// }
	//
	// StringTokenizer stk = new StringTokenizer(Sentence, ".");
	// String s = "";
	// String out = "";
	// int cnt2 = 0;
	// // read each line of given sentence
	// while (stk.hasMoreTokens()) {
	// s = stk.nextToken();
	// cnt2++;
	//
	// if (cnt2 >= 2)
	// out = out.replace(".#", "dot#") + ".";
	//
	// s2 = s.split(" ");
	// cnt = 0;
	// String[] s3 = new String[s2.length];
	//
	// // String str=
	// //
	// "AN 18-year-old girl was shot dead by her brother in the Kahna area on Tuesday.The deceased was identified Jameela, daughter of Abdul Rehman of Bohgal village, Kahna. Police said accused Zahid murdered his daughter on suspicion. The victim’s family claimed that Zahid got infuriated when he was asked to complete his sister’s dowry as her marriage ceremony was due after six days.On the day of the incident, he shot her dead when she was asleep. The accused escaped from the scene. Police have registered a case and removed the body to morgue for autopsy.";
	// // String s[] = Sentence.split("."); int cnt=0;
	// System.out.println("current s:" + s + " flag:" + flag);
	//
	// outV = getNLPTrained(foldername, flag, s, s2);
	// // conOut=conOut+get2NLPTrained( foldername, flag , s, p, outV);
	// System.out.println("incremental outV:" + outV.length + ":"
	// + outV);
	//
	// // step 2:
	// InputStream is = new FileInputStream(foldername + "en-chunker.bin");
	// ChunkerModel cModel = new ChunkerModel(is);
	// ChunkerME chunkerME = new ChunkerME(cModel);
	// String result[] = chunkerME.chunk(s2, outV);
	//
	// System.out.println(" length:" + outV.length + " " + s2.length
	// + " " + result.length +" o:" + outV + "#" + s2 + "#" + result);
	// //
	// while (cnt < outV.length) {
	// // if(outV[cnt] !=null && s2[cnt] !=null && result[cnt] !=
	// // null){
	// //System.out.println(" out word:" + outV[cnt] + " " + s2[cnt] + " cnt:" +
	// cnt);
	//
	// s3[cnt] = outV[cnt] + "#" + s2[cnt] + "#" + result[cnt];
	// out = out + " " + s3[cnt];
	// System.out.println("tag:" + outV[cnt] + " word:" + s2[cnt]
	// + " " + result[cnt] + " cnt:" + cnt+" s3:" + s3[cnt]);
	// // }
	// cnt++;
	// }
	// } // while end of current line of given sentence
	// System.out.println("-->Wrapperget2NLPTrained.out:" + out);
	// mapOut.put(1, out);
	// System.out.println("-->Wrapperget2NLPTrained.out.1:"
	// + mapOut.get(1));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return mapOut;
	// }

	// NLP
	public static String get2NLPTrained(String foldername, String flag,
			String Sentence, Parse p, String outValue) {
		SentenceDetector _sentenceDetector = null;

		List<Parse> nounPhrases = new ArrayList<Parse>();

		InputStream modelIn = null;
		InputStream is2 = null;
		int cnt = 0;
		try {

			// Loading PARSER CHUNKER
			// http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool
			System.out.println("-----------------" + p.toString());
			p.show();
			System.out.println("---end of show--------------");

			System.out.println(" 1.np: getType:" + p.getType() + ";getLabel:"
					+ p.getLabel() + ";getHead:" + p.getHead());
			// +":"+ ";getParent:" + p.getParent() );

			if (!p.getType().equals("TOP")) {
				outValue = outValue + p.getType() + ":" + p.getHead();
			}

			/*
			 * if (p.getType().equals("NP")) { nounPhrases.add(p);
			 * 
			 * System.out.println(" 2.np: type:" + p.getType() +" getNodes:" +
			 * p.getLabel() +":"+ p.getHead() +":"+ " parent:" + p.getParent()
			 * +" child:"+ p.getChildren()); }
			 */
			for (Parse child : p.getChildren()) {
				get2NLPTrained(foldername, flag, Sentence, child, outValue);
			}
			// return p;

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ov:" + outValue);

		return outValue;
	}

	// wrapper for get2NLPTrained (input: given String. It splits each para into
	// lines and call "en-parser-chunking.bin" )
	public static HashMap<Integer, String> Wrapperget2NLPTrained(
																String foldername, String flag, String flag2, String Sentence,
																FileWriter writerdebugOutErrorAndURLFile,
																boolean  is_overwrite_flag_with_flag_model,
																POSModel inflag_model2, // model of "en-pos-maxent.bin" etc
																ParserModel inflag_model2_chunking,
																boolean isSOPdebug) {
		HashMap<Integer, String> mapOut = new HashMap();
		try {
			System.out
					.println("------------------Wrapperget2NLPTrained-----------------------------");
			System.out.println("file:" + foldername + "en-parser-chunking.bin");
			InputStream is2 = new FileInputStream(foldername+ "en-parser-chunking.bin");
			ParserModel model3 = new ParserModel(is2);
			Parser parser = ParserFactory.create(model3);

			String[] outV = null;
			TreeMap<String, String> maptaggedNLP = new TreeMap<String, String>();
			String[] s2 = null;
			int cnt = 0;

			if (Sentence == null) {
				mapOut.put(1, "blankCrawl");
				return mapOut;
			}

			StringTokenizer stk = new StringTokenizer(Sentence, ".");
			String s = "";
			String out = "";
			int cnt2 = 0;
			// read each line of given sentence
			while (stk.hasMoreTokens()) {
				s = stk.nextToken();
				cnt2++;

				if (cnt2 >= 2)
					out = out.replace(".#", "dot#") + ".";

				s2 = s.split(" ");
				cnt = 0;
				String[] s3 = new String[s2.length];

				// String str=
				// "AN 18-year-old girl was shot dead by her brother in the Kahna area on Tuesday.The deceased was identified Jameela, daughter of Abdul Rehman of Bohgal village, Kahna. Police said accused Zahid murdered his daughter on suspicion. The victim’s family claimed that Zahid got infuriated when he was asked to complete his sister’s dowry as her marriage ceremony was due after six days.On the day of the incident, he shot her dead when she was asleep. The accused escaped from the scene. Police have registered a case and removed the body to morgue for autopsy.";
				// String s[] = Sentence.split("."); int cnt=0;
				System.out.println("current s:" + s + " flag:" + flag);

				maptaggedNLP = getNLPTrained(foldername, flag, flag2, s, s2,
												writerdebugOutErrorAndURLFile,
												is_overwrite_flag_with_flag_model,
												inflag_model2,
												inflag_model2_chunking,
												isSOPdebug);
				// conOut=conOut+get2NLPTrained( foldername, flag , s, p, outV);

				if (isSOPdebug) {
					System.out.println("incremental maptaggedNLP:"
							+ maptaggedNLP.size() + ":"
							+ maptaggedNLP.get("taggedNLP"));
				}

				// step 2:
				// InputStream is = new FileInputStream(foldername +
				// "en-chunker.bin");
				// ChunkerModel cModel = new ChunkerModel(is);
				// ChunkerME chunkerME = new ChunkerME(cModel);
				// String result[] = chunkerME.chunk(s2, outV);
				//
				// System.out.println(" length:" + outV.length + " " + s2.length
				// + " " + result.length +" o:" + outV + "#" + s2 + "#" +
				// result);
				// //
				// while (cnt < outV.length) {
				// // if(outV[cnt] !=null && s2[cnt] !=null && result[cnt] !=
				// // null){
				// //System.out.println(" out word:" + outV[cnt] + " " + s2[cnt]
				// + " cnt:" + cnt);
				//
				// s3[cnt] = outV[cnt] + "#" + s2[cnt] + "#" + result[cnt];
				// out = out + " " + s3[cnt];
				// System.out.println("tag:" + outV[cnt] + " word:" + s2[cnt]
				// + " " + result[cnt] + " cnt:" + cnt+" s3:" + s3[cnt]);
				// // }
				// cnt++;
				// }

			} // while end of current line of given sentence
			System.out.println("-->Wrapperget2NLPTrained.out:" + out);
			mapOut.put(1, out);
			System.out.println("-->Wrapperget2NLPTrained.out.1:"
					+ mapOut.get(1));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapOut;
	}

	// inFlag--> "file" -> input approach 1: URL file <email_file_name http_url>
	// output: write to mongodb.
	// input approach 2: input an (single) URL and output (return a map)
	// Map (a) header (b) crawled page
	// input approach 3: input URL file <email_file_name http_url>
	// outFlag-->"outfile"
	// "writeCrawledText2OutFile"<-obsolete maybe
	// input approach 4: input: mongodbURLonly ; output: mongodb
	// input approach 5: input: mongodbURLandBODYonly ; output: mongodb
	// in approach 5 we assume only NLP is left to be worked out, so we read the
	// body and do tagging NLP task
	public static TreeMap<Integer, String> getCrawler(		String folder,
															String NLPfolder, boolean is_NLP_run,
															String inputListOf_URLfileCSV, String inputSingleURL,
															String outputFile, boolean isOutputFileAppendFlag,
															String delimiter, String inFlag, String outFlag, String flag2,
															String outAlreadyCrawledURLsINaFile,
															TreeMap<String, String> mapAlreadyCrawledURL, 
															int fromLine,
															int toLine, 
															String outNewUnCrawledURLFile,
															String outErrorAndURLFile, 
															String parseType, boolean isSOPdebug,
															int URL_present_in_which_column_token, 
															String 	crawlerType,
															boolean is_overwrite_flag_with_flag_model,
															POSModel inflag_model2, // model of "en-pos-maxent.bin" etc
															ParserModel inflag_model2_chunking,
															String debugFileName) {
		String line = "";
		int lineNo = 0;
		BufferedReader reader = null;
		TreeMap<String, String> mapEmailFileName = new TreeMap();
		TreeMap<Integer, String> mapURL = new TreeMap<Integer, String> ();
		TreeMap<String, String> map_originalURL = new TreeMap();
		TreeMap<String, String> mapThird = new TreeMap();
		TreeMap<Integer, String> mapOutput = new TreeMap();
		BasicDBObject query = new BasicDBObject();
		query.put("body", "");
		TreeMap mapID = new TreeMap();
		Source source;
		String currFile = "";
		MongoDatabase database = null;
		try {
			System.out.println("inFlag:" + inFlag);
			Mongo mongo = new Mongo("localhost", 27017);

			// get database from MongoDB,
			// if database doesn't exists, mongoDB will create it automatically
			DB db = new Mongo().getDB("test");
			com.mongodb.MongoClient client = new MongoClient("localhost", 27017);
			// set1 ->
			
			if( inFlag.equalsIgnoreCase("mongodburlandbody"))
					database = (MongoDatabase) client.getDB("test");

			Jongo jongo = new Jongo(db);
			// MongoCollection friendsCollection =
			// jongo.getCollection("main_news");

			// writer
			FileWriter writer = new FileWriter(outputFile,isOutputFileAppendFlag);
			FileWriter writerCrawledURLsFile = new FileWriter(outAlreadyCrawledURLsINaFile, isOutputFileAppendFlag);
			FileWriter writernewNonCrawledURLFile = new FileWriter(outNewUnCrawledURLFile);
			FileWriter writerdebugOutErrorAndURLFile = new FileWriter(outErrorAndURLFile);
			writerdebugOutErrorAndURLFile.append("\ninFlag:" + inFlag);
			writerdebugOutErrorAndURLFile.flush();
			String[] sFile = inputListOf_URLfileCSV.split(delimiter);
			
			int fileL = sFile.length;
			int fileIndex = 0;
			/** Start Approach 1: load URLs from input file */
			// input is a file and output new Uncrawled URL file to output file
			if (inFlag.equalsIgnoreCase("file")) {
				// while read each file
				while (fileIndex < fileL) {
					System.err.println("folder:"+folder +" sFile[fileIndex]:"+sFile[fileIndex]);
					// read current File
					currFile =   sFile[fileIndex];
					
					reader = new BufferedReader(new FileReader(currFile));
					// read each line of given file
					while ((line = reader.readLine()) != null) {
						lineNo++;
						if (lineNo >= fromLine && lineNo <= toLine) {
							String s[] = line.split(delimiter);
							System.out.println("s.length:" + s.length + ";line:"+ line + ";currFile:" + currFile);
							if(URL_present_in_which_column_token>s.length){
								continue;
							}
							//
							String curr_orig_URL=s[URL_present_in_which_column_token - 1].replace("!", "");
							String curr_URL=s[URL_present_in_which_column_token - 1].replace("!", "")
												.replaceAll("http://", "").replaceAll("www.", "").replaceAll("tp://", "");
							
							// check if already exists from previous crawl
							// iteration
							if (!mapURL.containsValue(curr_URL)
									&& !mapAlreadyCrawledURL.containsValue(curr_URL)) {
								// 
								mapEmailFileName.put(Integer.toString(lineNo), s[0]);
								mapURL.put( lineNo, curr_URL);
								map_originalURL.put(Integer.toString(lineNo), curr_orig_URL);
								
								// below not right as it directly puts input
								// from input file
								// int j=mapAlreadyCrawledURL.size()+1;
								// mapAlreadyCrawledURL.put(String.valueOf(j),
								// s[URL_present_in_which_column_token-1]);
								// //already crawled
								// if(s.length==3){
								// mapThird.put(Integer.toString(lineNo), s[2]);
								// }

							} else if (mapAlreadyCrawledURL.containsValue(curr_URL.replaceAll("http://", "")
																				  .replaceAll("www.", "")
																				  .replaceAll("tp://", ""))) {
								writerdebugOutErrorAndURLFile.append("alrdy done:"+ curr_URL+ "\n");
								writerdebugOutErrorAndURLFile.flush();
							}

							if(lineNo<=12)
								System.out.println("Range:{" + fromLine + ";"
													+ toLine + "};lineno:" + +lineNo
													+ ";emailfilename:" + s[0] + ";url:"
													+ curr_URL);
						} // lineNo range
						else {
							System.out.println("Not in Range:{" + fromLine
									+ ";" + toLine + "};lineno:" + +lineNo);
						}
					} // end of while
					fileIndex++;
				}// each file read

				System.out.println("LOADED mapURL.size:"+mapURL.size());
				// write to unCrawled new URL to new file..
				if (inFlag.equalsIgnoreCase("file")
						&& outFlag.toLowerCase().equalsIgnoreCase("outNewUnCrawledURLFile")) {

					// write new non-crawled URL to out file
					for (String lineno2 : mapEmailFileName.keySet()) {
						writernewNonCrawledURLFile.append(mapEmailFileName.get(lineno2)+ "!!!"
														 + mapURL.get(lineno2)+ "\n");
						writernewNonCrawledURLFile.flush();
					}
					System.out.println(" ENDED ");
					return mapOutput;
				}
			}

			if (inFlag.equalsIgnoreCase("map")) { // only output to map (and not
													// write to output file)
				// mapEmailFileName.put("1", );
				mapURL.put(1, inputSingleURL);
			}

			/** End Approach 1: load URLs from input file */
			String queryString = "";
			/** Start Approach 2: load URLs only from mongodb */
			// input is from mongodb ( input is list of URLs without text
			// crawled).
			// Get only URLs of them satisfying flag=""
			if (inFlag.equalsIgnoreCase("mongodbURLonly")) {
				// connect to mongodb and get the URL
				writerdebugOutErrorAndURLFile
						.append("\nInside inFlag:mongodbURLonly");
				writerdebugOutErrorAndURLFile.flush();

				// cursor using MongoCollection
				// queryString="{flag:'urlonly'}";
				// MongoCollection friends = jongo.getCollection("main_news",
				// BasicDBObject.class);
				// MongoCursor mongocursor =
				// friends.find(queryString).as(Friend.class);
				// writerdebugOutErrorAndURLFile.append("\ncursor2.count:"+mongocursor.count());
				// writerdebugOutErrorAndURLFile.flush();
				// //
				// while(mongocursor.hasNext()){
				// System.out.println("mongocursor.");
				// Friend f= (Friend) mongocursor.next();
				//
				// writerdebugOutErrorAndURLFile.append(
				// f.toString());
				//
				// writerdebugOutErrorAndURLFile.flush();
				// }

				// using MongoCollection (working, tested)
				// com.mongodb.MongoClient client = new MongoClient("localhost",
				// 27017 );
				// MongoDatabase database = client.getDatabase("test");
				com.mongodb.client.MongoCollection<Document> collection = database
						.getCollection("main_news");

				// String queryString2="{flag:'urlonly'}";
				com.mongodb.client.MongoCursor<Document> cursor2 = collection.find(eq("flag", "urlonly")).iterator();
				int lNo = 0;
				//
				while (cursor2.hasNext()) {
					lNo++;
					String tf = cursor2.next().toJson();
					System.out.println("json:");
					JSON.parse(tf);
					JSONParser jsonParser = new JSONParser();
					// JSONObject jsonObject = (JSONObject)
					// jsonParser.parse(tf);
					org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
					Object obj = parser.parse(tf);

					JsonParserFactory factory = JsonParserFactory.getInstance();
					JSONParser parser22 = factory.newJsonParser();
					Map jsonData = parser22.parseJson(tf);

					mapURL.put(lNo,
							(String) jsonData.get("url"));
					System.out.println(jsonData.get("_id").toString()
							.replace("{$oid=", "").replace("}", ""));
					mapID.put((String) jsonData.get("url"), jsonData.get("_id")
							.toString().replace("{$oid=", "").replace("}", ""));

					writerdebugOutErrorAndURLFile.append("\nurl22:"
							+ jsonData.get("url"));
					writerdebugOutErrorAndURLFile.append("\njson:" + tf
							+ " mapURL.size:" + mapURL.size() + "\n\n");
					writerdebugOutErrorAndURLFile.flush();
				}
				// using DBCursor (working, tested)
				// DBCollection coll = db.getCollection("main_news");
				// query = new BasicDBObject();
				// query.put("flag", "urlonly");
				// DBCursor cursor = coll.find(query);
				// writerdebugOutErrorAndURLFile.append("\ncursor.count:"+cursor.count());
				// writerdebugOutErrorAndURLFile.flush();
				// lNo=0;
				// // iterate each URL, and crawl the URL, update the mongodb
				// while(cursor.hasNext()){
				// lNo++;
				// DBObject currURL=cursor.next();
				// System.out.println("basicDBobject."+currURL.toString());
				//
				//
				// int first=currURL.toString().indexOf("http:");
				// int second=currURL.toString().indexOf("\"",first+2 );
				// String t=currURL.toString().substring( first,
				// second);
				// writerdebugOutErrorAndURLFile.append("\ncursor next."+currURL.toString()+" t:"+t);
				// writerdebugOutErrorAndURLFile.flush();
				// mapURL.put(String.valueOf(lNo) , t);
				// //crawl and get the crawled text
				// // TreeMap<String, String> updateString=
				// // readRenderedTextANDextractFeatures(NLPfolder,
				// currURL.toString()
				// // , writerdebugOutErrorAndURLFile
				// // , isSOPdebug
				// // );
				//
				// }

				// Get only URLs of them satisfying flag=""
				// db = new Mongo().getDB("test");
				// jongo = new Jongo(db);
				// MongoCollection friends = jongo.getCollection("main_news");
				// friends.find("{flag:'urlonly'}");

			}
			/** End Approach 2: load URLs only from mongodb */

			/** Start Approach 3: load URLs and Crawled TEXT from mongodb */
			/* Do NLP for Text */
			// This means we already have CRAWLED TEXT (renderedtext and
			// body), we do NLP only
			// later left for extract features or both.
			if (inFlag.toLowerCase().equalsIgnoreCase("mongodburlandbody")) {
				writerdebugOutErrorAndURLFile.append("\nhas somethng: ");
				writerdebugOutErrorAndURLFile.flush();
				// db = new Mongo().getDB("test");
				// jongo = new Jongo(db);
				// using MongoCollection (working, tested)
				// com.mongodb.MongoClient client = new MongoClient("localhost",
				// 27017 );
				// MongoDatabase database = client.getDatabase("test");
				MongoCollection friends = jongo.getCollection("main_news");
				com.mongodb.client.MongoCollection<Document> collection2 = database
						.getCollection("main_news");
				com.mongodb.client.MongoCursor<Document> cursor3 = collection2
						.find().iterator();
				// eq("flag", "urlandbody")

				writerdebugOutErrorAndURLFile.append("\nhas somethng:1 ");
				writerdebugOutErrorAndURLFile.flush();

				String currStringForTagging = "";
				// iterate mongocursor
				while (cursor3.hasNext()) {
					String tf = cursor3.next().toJson();
					writerdebugOutErrorAndURLFile.append("\nhas somethng1: "
							+ tf);
					writerdebugOutErrorAndURLFile.flush();
					// JSON.parse(tf);
					JSONParser jsonParser = new JSONParser();
					// JSONObject jsonObject = (JSONObject)
					// jsonParser.parse(tf);
					org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
					Object obj = parser.parse(tf);

					JsonParserFactory factory = JsonParserFactory.getInstance();
					JSONParser parser22 = factory.newJsonParser();
					System.out.println("\nhas somethng1: map: " + tf);

					Gson gson = new Gson();
					// JsonElement jsonElement = (JsonElement)
					// jsonParser.parseJson(tf);
					gson.toJson(tf);
					String sourceUrlString = "";

					String tf2 = gson.toJson(tf);
					System.out.println(":has->" + tf2);
					// Map jsonData= parser22.parseJson(tf2);
					Map jsonData = null;
					JSONObject jsonObject = (JSONObject) parser.parse(tf);
					writerdebugOutErrorAndURLFile
							.append("\nhas somethng1: map: " + jsonData);
					System.out.println("\nhas somethng1: map: " + jsonData
							+ " " + jsonObject.get("body"));
					System.out.println("jsonobject:"
							+ jsonObject.get("database"));
					writerdebugOutErrorAndURLFile.flush();
					currStringForTagging = jsonObject.get("body").toString();
					if (jsonObject.containsKey("url"))
						sourceUrlString = jsonObject.get("url").toString();

					writerdebugOutErrorAndURLFile.append("\nsourceUrlString:"
							+ sourceUrlString);
					writerdebugOutErrorAndURLFile
							.append("\ncurrStringForTagging:"
									+ currStringForTagging);
					writerdebugOutErrorAndURLFile.flush();
					// call NLP
					TreeMap maptaggedNLP = Crawler.getNLPTrained(NLPfolder,
																"en-pos-maxent.bin", flag2, currStringForTagging,
																null, writerdebugOutErrorAndURLFile,
																is_overwrite_flag_with_flag_model,
																inflag_model2,
																inflag_model2_chunking,
																isSOPdebug
																);
					BasicDBObject documentDetail = new BasicDBObject(); //

					writerdebugOutErrorAndURLFile.append("\nbefore.taggedNLP:"
							+ maptaggedNLP.get("taggedNLP"));
					writerdebugOutErrorAndURLFile.flush();
					documentDetail.put("taggedNLP",
							maptaggedNLP.get("taggedNLP")); //
					documentDetail.put("flag", "done"); //
					//set1 : lenin comment
					//friends.update("{url: \"" + sourceUrlString + "\"}").with(documentDetail);

				}

			}
			/** End Approach 3: load URLs and Crawled TEXT from mongodb */

			writerdebugOutErrorAndURLFile.append("mapURL:"+mapURL+"\n");
			writerdebugOutErrorAndURLFile.flush();
			
			/**
			 * Start Approach 4: Process each of URL from input File (OR)
			 * mongodb
			 */
			// input is either list of URL from file or mongodb
			if (inFlag.equalsIgnoreCase("file")
					|| inFlag.equalsIgnoreCase("mongodbURLonly")) {
				// for each loop of mapEmailFileName
				// (1) mapURL has URL to be crawled. mapURL got this from second
				// col of input file
				// (2) crawl the pages from URLs and extract features (datetime,
				// author, text)
				// (3) Do NLP tagging for body text of news.
				// (4) Insert into mongodb
				HashMap<Integer, String> map = null;
				String sourceUrlString = null;
				String orig_sourceUrlString = null;
				String HTTPrenderedText = "";
				TreeMap<String, String> mapGotCrawledTextANDFeatures = new TreeMap<String, String>();
				int manual_counter = 0;
				// each url
				for (int currURL_id_INT : mapURL.keySet()) {
					// 
					String currURL_id= String.valueOf(currURL_id_INT); 
					
					if(sourceUrlString!=null){
						//clean if url has embedded URL in it
						if(sourceUrlString.indexOf("url=")>=0){
							sourceUrlString=Clean_embeddedURLs.clean_get_embeddedURL(sourceUrlString);
						}
					}
					
					manual_counter++;
					sourceUrlString = mapURL.get(currURL_id_INT);
					orig_sourceUrlString=map_originalURL.get(currURL_id);
					// NOT BLANK
					if (sourceUrlString == null
							|| sourceUrlString.length() <= 4) {
						continue;
					}

					writerdebugOutErrorAndURLFile.append("\neach url:"
							+ sourceUrlString);
					writerdebugOutErrorAndURLFile.flush();
					// already crawled
					if (mapAlreadyCrawledURL.containsValue(sourceUrlString)) {
						System.out.println("mapAlreadyCrawledURL.size:"
								+ mapAlreadyCrawledURL.size() + " ;alrdy done:"
								+ sourceUrlString);
						writerdebugOutErrorAndURLFile.append("\nalrdy done:"
								+ sourceUrlString);
						writerdebugOutErrorAndURLFile.flush();
						continue;
					}

					// String
					// sourceUrlString="http://www.thehindu.com/news/national/tamil-nadu/child-dies-after-being-brought-out-of-borewell/article5179926.ece";

					if (crawlerType.equalsIgnoreCase("type1Crawler")
							|| crawlerType.equalsIgnoreCase("all")) {
						System.out.println("currURL_id:" + currURL_id
								+ ";manual_counter:" + manual_counter
								+ ";mapAlreadyCrawledURL.size:"
								+ mapAlreadyCrawledURL.size());
						// Crawl the text and get the features
						mapGotCrawledTextANDFeatures = readRenderedTextANDextractFeatures(
															Integer.valueOf(currURL_id), NLPfolder,
															mapURL.get(currURL_id_INT), mapEmailFileName,
															is_NLP_run, writerdebugOutErrorAndURLFile,
															isSOPdebug, debugFileName,
															"", // flag2
															is_overwrite_flag_with_flag_model,
															inflag_model2,
															inflag_model2_chunking
													);
						if (isSOPdebug) {
							System.out
									.println("back mapGotCrawledTextANDFeatures:"
											+ mapGotCrawledTextANDFeatures
													.get("status")
											+ mapGotCrawledTextANDFeatures
													.size() + "<-");
						}

						//
						if (mapGotCrawledTextANDFeatures.get("status")
								.equalsIgnoreCase("failed"))
							continue;
					} else if (crawlerType.equalsIgnoreCase("type2Crawler")
							|| crawlerType.equalsIgnoreCase("all")) {
						System.out.println("type2Crawler--currURL_id:" + currURL_id
								+ ";manual_counter:" + manual_counter
								+ ";mapAlreadyCrawledURL.size:"
								+ mapAlreadyCrawledURL.size());
						System.out.println("orig_sourceUrlString:" + orig_sourceUrlString);
						// remove dirty concate -> reason.com/archives/1997/02/01/dances-with-mythshttp://reason.com/archives/1997/02/01/happy-warrior
						if(sourceUrlString.indexOf("http:")>=0)	
							sourceUrlString=sourceUrlString.substring(sourceUrlString.indexOf("http:"), sourceUrlString.length());
						// crawler_send_http_request
						HTTPrenderedText = Crawler_send_http_request
													.crawler_send_http_get(orig_sourceUrlString, ""// <proxy.file>
															).get(1);
					}

					// write OUTPUT* to mongodb
					if (outFlag.toLowerCase().equalsIgnoreCase("mongodb")) {

						DBCollection coll = db.getCollection("main_news");
						sourceUrlString = mapURL.get(currURL_id_INT); // URL
						query.put("url", sourceUrlString);

						// mongoCollection used
						// db = new Mongo().getDB("test");
						// jongo = new Jongo(db);
						// DBCursor cursor = coll.find(query);

						com.mongodb.client.MongoCollection<Document> collection = database
								.getCollection("main_news");
						// String queryString2="{flag:'urlonly'}";
						com.mongodb.client.MongoCursor<Document> cursor2 = collection
								.find().iterator();

						MongoCollection friends = jongo
								.getCollection("main_news"); // jongo has db
																// object

						writerdebugOutErrorAndURLFile
								.append("\n mongodb: lineon3:" + currURL_id
										+ " sourceUrlString:" + orig_sourceUrlString
										+ " cursor.count():");
						writerdebugOutErrorAndURLFile.flush();

						// "http://www.dnaindia.com/india/1882820/report-former-arunachal-minister-dead";
						// http://www.dnaindia.com/india/1882820/report-former-arunachal-minister-dead
						// "http://www.chron.com/news/houston-texas/houston/article/Houston-man-held-in-2009-death-of-girl-2-4830634.php?cmpidhtx";
						// "http://www.wbtw.com/story/23488330/11-year-old";
						// "http://articles.timesofindia.indiatimes.com/2013-09-19/bangalore/42216844_1_sambar-vessel-krishnappa-headmaster";
						// "http://www.foxnews.com/world/2013/09/15/mine-collapse-kills-27-in-north-afghanistan/";
						// http://www.yorkregion.com/news-story/4116596-health-hazards-at-daycare-where-girl-died/
						// http://www.chron.com/news/houston-texas/houston/article/Houston-man-held-in-2009-death-of-girl-2-4830634.php?cmpidhtx

						// insert a record into mongo db.
						// ",  DateTime:\"\"  , EmailFileName:\"\",   EmailFileDT:\"\",  location: \"\", header:\"\", body:\"\", headerNLP:\"\", bodyNLP:\"\",rawData:\"\", renderText:\"\"}");
						// db.main_news.insert({ url:"testurl1" , DateTime:"",
						// EmailFileName:"", EmailFileDT:"", location: "",
						// header:"", body:"", headerNLP:"",
						// bodyNLP:"",rawData:"",
						// renderText:""})
						// document.put("main_news", documentDetail);
						// collection.insert(document);
						// collection.save(document);

						// create object to save to mongodb
						BasicDBObject documentDetail = new BasicDBObject(); //
						documentDetail.put("database", "test"); //
						documentDetail.put("table", "main_news");
						documentDetail.put("url", sourceUrlString);
						documentDetail.put("header",
								mapGotCrawledTextANDFeatures.get("header"));
						documentDetail.put("body",
								mapGotCrawledTextANDFeatures.get("body"));
						documentDetail.put("renderedText",
								mapGotCrawledTextANDFeatures
										.get("renderedText").replace(" ", "#"));

						documentDetail
								.put("HTTPrenderedText", HTTPrenderedText);

						documentDetail.put("location",
								mapGotCrawledTextANDFeatures.get("location"));
						documentDetail.put("DateTime",
								mapGotCrawledTextANDFeatures.get("DateTime"));

						if (mapEmailFileName.containsKey(currURL_id)) {
							documentDetail.put("EmailFileName",
									mapEmailFileName.get(currURL_id));
						}

						documentDetail.put("taggedNLP",
								mapGotCrawledTextANDFeatures.get("taggedNLP"));

						if (mapEmailFileName.get(currURL_id) != null) {
							if (mapEmailFileName.get(currURL_id).length() >= 8)
								documentDetail.put("EmailFileDT",
										mapEmailFileName.get(currURL_id)
												.substring(0, 8));
						}
						String bodyNLP = "";
						String headerNLP = "";
						// coll.updateMulti("{url:"+sourceUrlString+"}",
						// documentDetail);

						if (mapID.containsKey(sourceUrlString)) {
							//set1 : lenin comment
							//friends.update("{url: \"" + sourceUrlString + "\"}").with(documentDetail);
							
							// friends.save(documentDetail);
							writerdebugOutErrorAndURLFile
									.append("\nupdate.documentDetail:"
											+ documentDetail + ";url:"
											+ orig_sourceUrlString);
						} else {
							// check if url exists in db.
							queryString = "{url: \"" + sourceUrlString + "\"}";
							long count = jongo.getCollection("main_news")
									.count(queryString);
							friends.find(queryString);
							// already in database.
							if (count > 0) {
								writerdebugOutErrorAndURLFile
										.append("\nupdate2.documentDetail:"
												+ documentDetail + ";url:"
												+ orig_sourceUrlString);
								// if in database.
								//set1 : lenin comment
								//friends.update("{url: \"" + sourceUrlString + "\"}").with(documentDetail);
								
							} else {
								
								//set1 : lenin comment
//								friends.insert(documentDetail);
								
								writerdebugOutErrorAndURLFile
										.append("\ninsert.documentDetail:"
												+ documentDetail + ";url:"
												+orig_sourceUrlString);
								writerdebugOutErrorAndURLFile.flush();
							}
						}
						writerdebugOutErrorAndURLFile.flush();

						// below wrapper not found
						// map =
						// Wrapperget2NLPTrained(folder,"en-pos-maxent.bin",
						// mapGotCrawledTextANDFeatures.get("body"));
						// documentDetail.put("bodyNLP", map.get(1));
						// //call NLP
						// String bodyNLP = map.get(1); // if(header.length() >
						// 2){
						// map =
						// Wrapperget2NLPTrained(folder,"en-pos-maxent.bin",
						// mapGotCrawledTextANDFeatures.get("header"));
						// String headerNLP = map.get(1);
						// documentDetail.put("headerNLP", map.get(1) );

						String insertS = "{ url:'"
								+ sourceUrlString
								+ "',"
								+
								// " DateTime:'"+DateTime.replace("'","").replace("\"","")
								// +
								"',"
								+ " EmailFileName:'"
								+ mapEmailFileName.get(currURL_id)
								// + "',"
								// + " EmailFileDT:'"+
								// mapEmailFileName.get(lineon3).substring(0,8)
								+ "',"
								+ " location:'"
								+ mapGotCrawledTextANDFeatures.get("location")
										.replace("'", "").replace("\"", "")
								+ "',"
								+ " header:'"
								+ mapGotCrawledTextANDFeatures.get("header")
										.replace("'", "").replace("\"", "")
								+ "',"
								+ " body:'"
								+ mapGotCrawledTextANDFeatures.get("body")
										.replace("'", "").replace("\"", "")
								// +
								// "',"+
								// " rawData:'"+mapGotCrawledTextANDFeatures.get("renderedText1").replace("'","").replace("\"","")
								+ "',"
								+ " headerNLP:'"
								+ headerNLP.replace("'", "").replace("\"", "")
								+ "',"
								+ " bodyNLP:'"
								+ bodyNLP.replace("'", "").replace("\"", "")
								+ "',"
								+ " renderText:'"
								+ mapGotCrawledTextANDFeatures
										.get("renderedText").replace(" ", "#")
										.replace("'", "").replace("\"", "")
								+ "'}";

						if (isSOPdebug) {
							System.out.println("\n insert:" + insertS + "\n");
						}

						// friends.insert(insertS); //done
						// writerdebugOutErrorAndURLFile.append("\ninsertS:"+insertS);
						writerdebugOutErrorAndURLFile.flush();

						writerCrawledURLsFile.append("\n" + sourceUrlString);
						writerCrawledURLsFile.flush();
						// System.out.println("\nmain_news inserted...-->"+documentDetail);
						// System.out.println("\nmain_news updated...-->"+document);

					} else if (outFlag.toLowerCase().equalsIgnoreCase("writeCrawledText2OutFile")) {
						// output is write to output file
						String bodyText = "";
						
						if (crawlerType.equalsIgnoreCase("type1Crawler")
								|| crawlerType.equalsIgnoreCase("all")) {

							System.out.println(" ############ writing to an output file; given input is also a file:"
											+ mapGotCrawledTextANDFeatures
													.containsKey("body")
											+ " "
											+ mapGotCrawledTextANDFeatures.size());

							for (String k : mapGotCrawledTextANDFeatures
									.keySet()) {
								if (mapGotCrawledTextANDFeatures.get(k)
										.length() > 100)
									System.out.println("key2:"
											+ k
											+ " "
											+ mapGotCrawledTextANDFeatures.get(
													k).substring(0, 15));
								else {
									System.out.println("key2:"
											+ k
											+ " "
											+ mapGotCrawledTextANDFeatures
													.get(k));
								}
							}

							bodyText = mapGotCrawledTextANDFeatures.get("body")
									.replaceAll("  ", " ")
									.replaceAll("\\n", "")
									.replaceAll("\\t", "")
									.replaceAll("\\r", "");
							
							writer.append(
							// mapEmailFileName.get(i) + "!!!"
							// +
									orig_sourceUrlString+ "!!!"+ mapGotCrawledTextANDFeatures.get("header") + "!!!" + bodyText
									+ "\n");
							writer.flush();
							writerCrawledURLsFile.append("\n" + sourceUrlString);
							writerCrawledURLsFile.flush();
						} else if (crawlerType.equalsIgnoreCase("type2Crawler")
								   || crawlerType.equalsIgnoreCase("all")) {
							
//							if(sourceUrlString.indexOf("http:")>=0)
//								sourceUrlString=sourceUrlString.substring(sourceUrlString.indexOf("http:"), sourceUrlString.length());
//							
//							bodyText = crawler_send_http_request
//									.crawler_send_http_get(sourceUrlString, ""// proxy
//																				// file
//									).get(1);
							
							System.out.println("writing orig_sourceUrlString:"+orig_sourceUrlString );
							String temp="orig_sourceUrlString:"+orig_sourceUrlString + "!!!" + HTTPrenderedText+"<--HTTPrenderedText";
							
							temp=temp.substring( temp.indexOf("orig_sourceUrlString:"), temp.length() ) ;
							
							writer.append( // mapEmailFileName.get(i) + "!!!" +
											temp+ "\n");
							writer.flush();
							writerCrawledURLsFile.append("\n" + sourceUrlString);
							writerCrawledURLsFile.flush();
						}

					} else { // just output as map

						String bodyText = map.get("body").replaceAll("  ", " ")
										.replaceAll("\\n", "").replaceAll("\\t", "")
										.replaceAll("\\r", "");
						mapOutput.put(1, map.get("header"));
						mapOutput.put(2, bodyText);

					}

				} // end of FOR

			} // end of if inFlag.equalsIgnoreCase("file") ||
				// inFlag.equalsIgnoreCase("mongodbURLonly")

			// writer.close();reader.close();
			System.out.println("debug file:" + outErrorAndURLFile + " inFlag:"
					+ inFlag + " outFlag:" + outFlag);
			System.out.println("mapAlreadyCrawledURL.size:"
					+ mapAlreadyCrawledURL.size());
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return mapOutput;
	}

	// findBlankColumnAndUpdate
	public static void findBlankColumnAndUpdate(String folder,
			String NLPfolder, boolean is_NLP_run,
			TreeMap<String, String> mapAlreadyCrawled, String whichDriver,
			boolean isSOPDebug, int URL_present_in_which_column_token,
			String crawlerType, String debugFileName, String flag2,
			boolean is_overwrite_flag_with_inflag_model2,//is_overwrite_flag_with_flag_model
			POSModel inflag_model2, //// model of "en-pos-maxent.bin"
			ParserModel inflag_model2_chunking
			) {
		try {
			// connect to mongoDB, IP and port number
			Mongo mongo = new Mongo("localhost", 27017);

			// get database from MongoDB,
			// if database doesn't exists, mongoDB will create it automatically
			DB db = mongo.getDB("test");

			// get a single collection
			DBCollection coll = db.getCollection("main_news");
			HashMap<Integer, String> mapOut = new HashMap<Integer, String>();
			// using BasicDBObject
			BasicDBObject query = new BasicDBObject();
			query.put("body", "");
			DBCursor cursor = coll.find(query);
			String url = "";

			// read each cursor
			while (cursor.hasNext()) {
				// DBObject currRecord = cursor.next();
				BasicDBObject currRecord = (BasicDBObject) cursor.next();
				url = currRecord.get("url").toString();
				TreeMap<Integer, String> mapOutput = getCrawler(NLPfolder,
													folder, is_NLP_run, folder + "//09//outURL.txt", url,
													folder + "//09//outParsedHTML.txt", true, "!!!",
													"noTfile", "OutFile", flag2, "", mapAlreadyCrawled, 1,
													10000, "d", "outDebugfile.txt", "parse", isSOPDebug,
													URL_present_in_which_column_token, crawlerType,
													is_overwrite_flag_with_inflag_model2,
													inflag_model2,
													inflag_model2_chunking,
													debugFileName);

				System.out.println("currRecord:" + currRecord + " id:"
						+ currRecord.getString("_id") + " \n body:"
						+ mapOut.get(1));
				currRecord.put("body", mapOut.get(1));
				coll.save(currRecord);

				// coll.up
				// coll.update({ _id: currRecord.getString("_id") },
				// { $set: {"detail.body":"test"} });
				// BasicDBObject searchQuery = currRecord.append("_id",
				// "newValue");
				// coll.update( currRecord , searchQuery);

			}

			// cursor =coll.find(query);
			// query.put("detail.body", "newValue");
			// System.out.println("got"+cursor.count()
			// +" cnt:"+coll.find(query).count());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// remove the tag
	public static String SubRemoveTagsFromHTML(String bodyText) {
		try {
			while (bodyText.indexOf("<") >= 0) {
				// <p class=""> </p>
				int startIndex = bodyText.indexOf("<");
				int endIndex = bodyText.indexOf(">", startIndex) + 1;
				if (endIndex > startIndex)
					bodyText = bodyText.replace(
							bodyText.substring(startIndex, endIndex), " ");
				else
					return bodyText;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bodyText;
	}

	// get the /Messages/ folder for uaw (Generic) 27 Aug 2014
	// this method replaced by
	// readFile_get_all_FilesPath_Recursively_From_Given_Folder()
	public static TreeMap<Integer, String> getMessageFoldersFullPathFromGivenTopFolder(
			String inputGivenTopFolder, String outputFile,
			int guessSecondLevelMaxFolder) {
		TreeMap<Integer, String> mapFirstSetFullPathFiles = new TreeMap<Integer, String>();
		try {
			FileWriter writer = new FileWriter(outputFile);

			String decodedPath = URLDecoder.decode(inputGivenTopFolder);

			File h = new File(decodedPath);
			System.out.println("inputGivenTopFolder:" + inputGivenTopFolder);

			URL url = h.toURI().toURL();

			String[] files = h.list();
			int cnt = 0;
			int counter = 0;
			// Read first level folder.
			while (cnt < files.length) { // Read "Data" Folder
				if (files[cnt].indexOf("Store") >= 0
						|| files[cnt].indexOf(".zip") >= 0) {
					cnt++;
					continue;
				}

				File h2 = new File(inputGivenTopFolder + files[cnt]);
				System.out.println("full path of first level:"
						+ h2.getCanonicalPath());

				int cnt3 = 0;
				while (cnt3 <= 9) {

					int cnt2 = 0;
					// read
					while (cnt2 <= guessSecondLevelMaxFolder) {

						// writer.append("::"+h2.getAbsolutePath()
						// +"/"+cnt3+"/"+cnt2 + "/2/1/Messages/" +"\n");
						writer.flush();
						String newFilePath = h2.getAbsolutePath() + "/" + cnt3
								+ "/" + cnt2 + "/2/1/Messages/";

						boolean isFileExists = new File(newFilePath).exists();
						if (isFileExists == false) {
							cnt2++;
							System.out.println("not exist:" + newFilePath);

							continue;
						} else {
							counter++;
							System.out.println("file exists->" + newFilePath);
							mapFirstSetFullPathFiles.put(counter, newFilePath);
						}

						if (!mapFirstSetFullPathFiles.containsValue(h2
								.getAbsolutePath()
								+ "/"
								+ cnt3
								+ "/"
								+ cnt2
								+ "/2/1/Messages/")) {
							mapFirstSetFullPathFiles.put(counter,
									h2.getAbsolutePath() + "/" + cnt3 + "/"
											+ cnt2 + "/2/1/Messages/");
						}
						writer.append(h2.getAbsolutePath() + "/" + cnt3 + "/"
								+ cnt2 + "/2/1/Messages/" + "\n");
						writer.flush();
						cnt2++;

					}
					cnt3++;
				}// cnt3
				cnt++;
			}
			// writer.append("map:" + mapFirstSetFullPathFiles);
			System.out.println("mapFirstSetFullPathFiles.size:"
					+ mapFirstSetFullPathFiles.size());
			writer.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapFirstSetFullPathFiles;
	}

	// removeStandardTagsFromHTML
	private static String removeStandardTagsFromHTML(String bodyText,
			boolean debug) {
		String outText = bodyText;
		try {
			outText = outText.replace("<div>", "").replace("</div", "")
					.replace("<span ", "").replace("</span>", "")
					.replace("position: absolute; top: 82px; left: 0px;", "")
					.replace("<td>", "").replace("</td>", "")
					.replace("style3D", "").replace("<v:rect ", "")
					.replace("</span>", "").replace("height3D", "")
					.replace("<a>", "").replace("</a>", "")
					.replace("style3D", "").replace("< img alt3D", "")
					.replace("<b>", "").replace("</b>", "")
					.replace("<table align3D", "").replace("<table>", "")
					.replace("</table>", "<table>").replace("z-index:1", "")
					.replace("\"padding:", "").replace("padding:", "")
					.replace("cellpadding3D", "").replace("border3D", "")
					.replace("height3D", "").replace(" solid ", "")
					.replace("behavior:", "").replace(" 0px", "")
					.replace(" 8px", "").replace(" 8px", "")
					.replace(" 16px", "").replace("<br>", "")
					.replace("</br>", "").replace("top:0", "")
					.replace("left:0", "").replace("border:0", "")
					.replace("z-index", "").replace(" 82px", "")
					.replace("position:", "").replace("<v:rect", "")
					.replace("width3D", "").replace("width3D", "")
					.replace("padding-right:", "").replace("padding-left:", "")
					.replace(" 18px", "").replace("<a ", "")
					.replace("alt3D", "").replace("Flag as irrelevant", "")
					.replace(":6px", "").replace(":18px", "")
					.replace("<tr>", "").replace("</tr>", "")
					.replace("<td", "").replace("<tr", "")
					.replace("\"Twitter\"", "").replace("\"Facebook\"", "")
					.replace("<table>", "").replace("<img ", "")
					.replace("opacity3D", "").replace("color3D", "")
					.replace("letter-spacing:", "").replace("<div ", "")
					.replace("<", "").replace(">", "").replace("v:textbox", "")
					.replace("mso-column-margin:", "").replace("0pt", "")
					.replace(" 0.8px", "").replace("absolute:", "")
					.replace("height:", "").replace("width:", "")
					.replace("100px", "").replace("100px", "")
					.replace("Google Plus", "").replace("\"6px", "")
					.replace("v:image", "").replace("font ", "")
					.replace("size3D", "").replace("\"table\"", "")
					.replace("\"top\"", "")
					.replace("all stories on this topic &raquo", "")
					.replace("all stories on this topic", "")
					.replace("stroke\"none\"", "").replace("v:fill", "")
					.replace("\"100\"", "").replace("\"right\"", "")
					.replace("display:", "").replace(" none:", "")
					.replace("colspan3D", "").replace("\"middle\"", "")
					.replace("#ffffff", "").replace("cellspacing3D", "")
					.replace("\"16\"", "").replace("\"0\"", "")
					.replace("18px", "").replace("6px", "")
					.replace("align3D", "").replace(";", "").replace(",", "")
					.replace("  ", "").replace("display:inline-block", "")
					.replace("\"50%\"", "").replace("\"100%\"", "")
					.replace("color:", "").replace("\"center\"", "")
					.replace("\"80\"", "").replace("\"", "")
					.replace("text-decoration:none", "").replace("#CCCCCC", "")
					.replace("#1111CC", "").replace("#228822", "");
			// colspan3D"3" v"middle"
			// "16" "16" "16" "  "0" "16" "16"   "16"
			// /"table"100%" v"top""color: #1111CC"
			// Sioux leader Gerald One Feather dies <div
			// 2px"> <div Hall >   <div friend Tom Katus told The Associated Press that One Feather died Thursday at a Rapid City hospital. "
			// +
			// "Katus said One Feather had a massive&nbsp;...> >       "16" "6px"> <img "Google
			// Plus" "0" "16" "16">   "16" "6px"> <img "Facebook" "0" "16" "16">   "16" "6px"> "
			// +
			// "<img "Twitter" "0" "16" "16">   6px     <table> > >   "18px">   "18px">

			//
			// <td width3D"16" "padding-right:6px"> <a <img alt3D"Facebook" "0"
			// "16" width3D"16"> <td width3D"16" "padding-right:6px"> <a <img
			// alt3D"Twitter" "0" "16" width3D"16"> <td 6px <a Flag as
			// irrelevant
			// </tr> <table> > >
			// <td </tr><tr> <td "padding-left:18px"> <td 18px
			//

			// color:#228822" all stories on this topic "padding: 0px 8px 16px
			// 8px;"><table cellpadding3D"0" cellspacing3D"0" border3D"0" valign3D"top"><a "color:
			// #1111CC"
			// Monroe pastor&#39;s daughter <b>dies</b> in swimming accident</a>
			// </span> <div style3D"padding:2px 0px 8px 0px"> <div <a >
			// <div 0px 0px 9:00 PM Sunday (KNOE <b>8</b> News) - Sheriff Rickey
			// Jones tells KNOE 13-year-old Sarah Tellifero was <b>killed</b>
			// Sunday afternoon in a swimming&nbsp;...> >
			// <table> <tr> <td width3D"16" <a <img alt3D"Google Plus"
			// border3D"0" height3D"16" width3D"16"></a> </td> <td width3D"16"
			// <a <img alt3D"Facebook" border3D"0" height3D"16" width3D"16">
			// </a> </td> <td width3D"16" <a <img alt3D"Twitter" border3D"0"
			// height3D"16" width3D"16"></a> </td> <td
			// style3D"padding:0px 0px 6px <a Flag as irrelevant </a> </td> </tr> </table> > > "
			// + "</td> <td </tr><tr> <td <td style3D"padding:18px 0px 12px
			// solid <a <table align3D"right" <tr> <td <td height3D"100"
			// width3D"100" valign3D"bottom"
			// style3D"padding:0px 0px 0px solid #e4e4e4"> <!--[if gte mso 9]>
			// <v:image id3D"theImage" style3D'behavior: url(#default#VML);
			// position:absolute; height: 100px; width: 100px; top:0; left:0;
			// border:0; z-index:1;'
			// <v:rect style3D'behavior: url(#default#VML); position: absolute;
			// top: 82px; left: 0px; width:100px; border:0; z-index:2;'
			// strokecolor3D"none"> <v:fill opacity3D"50%" color3D"#000000"/>
			// <v:textbox mso-column-margin: 0pt; letter-spacing:
			// 0.8px;"                                 <font size3D"-1"       </v:textbox>       </v:rect>       <div style3D"display:
			// none">       <![endif]--> <div <div style3D"padding:3px 0px 4px >
			// <!--[if gte mso 9]>><![endif]--> </td> </tr> </table> </a> <span
			// style3D"padding:0px 6px 0px 0px"> <a

		} catch (Exception e) {
			e.printStackTrace();
		}
		return outText;
	}

	// simple remove start tag and end tag
	public static String removeSimpleTagsFromHTML(String bodyText,
												 boolean debug, boolean isSOPprint) {
		String outbodyText = bodyText;
		String concText = "";
		try {

			outbodyText = outbodyText.replace("<div>", " ").replace("</div>", " ");

			String subString = "";

			String[] s = bodyText.split(" ");
			int cnt = 0;
			//
			while (cnt < s.length) {
				//
				if (s[cnt].indexOf("<") >= 0 && s[cnt].indexOf(">") >= 0
						&& s[cnt].indexOf("<") < s[cnt].indexOf(">")) {
					subString = s[cnt].substring(s[cnt].indexOf("<"),
							s[cnt].indexOf(">"));

					if (cnt == 0)
						concText = s[cnt];
					else
						concText = concText + s[cnt].replace(subString, " ");
				} else {
					if (cnt == 0) {
						concText = s[cnt];
					} else
						concText = concText + s[cnt];
				}

				cnt++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return concText;
	}

	// displaySegments ( can be (1) includes header of <h1> (2) includes <p
	// class=""> </p> or <br>
	public static String removeTagsFromHTML(String bodyText, boolean isSOPprint) {
		if (isSOPprint)
			System.out.println("input:" + bodyText);
		int cnt = 0;
		// there can be multiple h1
		if (bodyText.indexOf("<h1") >= 0) {
			String s[] = bodyText.split("</h1>");
			// System.out.println("s.leng:"+s.length+" ;input header:"+bodyText);
			//
			while (cnt < s.length) {

				s[cnt] = s[cnt].replace("<h1>", " ").replace("</h1>", " ")
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
		} else if (bodyText.indexOf("<h2") >= 0 && bodyText.indexOf("</h2>",  bodyText.indexOf("<h2")  ) >=0 ) {
			int startIndex = bodyText.indexOf("<h2");
			int endIndex = bodyText.indexOf("</h2>", startIndex) + 1;
			
			return SubRemoveTagsFromHTML(bodyText.substring(startIndex,endIndex));
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

			if (isSOPprint == true) {
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

				if (isSOPprint == true && endIndex >= startIndex)
					System.out.println("to replace:"
							+ bodyText.substring(startIndex, endIndex)
							+ " bodyText:" + bodyText);

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

				if (isSOPprint) {
					System.out.println("" + bodyText);
				}

				if (isSOPprint)
					System.out.println("s.length:" + s.length + " to replace:"
							+ " startIndex, endIndex:" + startIndex + " "
							+ endIndex + " substring to remove:"
							+ bodyText.substring(startIndex, endIndex));

				if (s.length > 2) {
					
					if(isSOPprint)
						System.out.println("5.");
					// if number of character lesser than one or two ,then don't
					// write.
					bodyText = bodyText.replace(
							bodyText.substring(startIndex, endIndex), "");
				} else {
					if (isSOPprint) {
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
			// System.out.println("1. inside findLocationSpot:\n renderedText:"
			// + renderedText + " \n 0.bodyText:" + bodyText);

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
			first20bodyText = first20bodyText.toLowerCase().replace("afp/file","");

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

				System.out.println("\n index: " + first20bodyText.indexOf("—")
						+ " " + first20bodyText.indexOf("-") + " "
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

	// read the drill down mapping
	public static String getMapping(String ToCheckString) {

		String line = "";
		String value = "";
		int count = 0;
		String name = "";
		String mappingFile = "/usr/local/apache-tomcat-7.0.37/webapps/ROOT/drilldown.txt";
		// Read the mapping to drill-down
		HashMap<String, String> mapDrillDown = new HashMap();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					mappingFile));
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				StringTokenizer stk = new StringTokenizer(line, " ");
				count = 1;
				if (line.equals(""))
					continue;
				System.out.println(line + ";token size:" + stk.countTokens());
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();
					if (count == 1) {
						name = value;
					} else if (count == 2) {
						mapDrillDown.put(name, value);
					}
					count++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapDrillDown.get(ToCheckString);
	}

	// check numeric
	public static boolean isNumeric(String input) {
		try {
			Double.parseDouble(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// read the drilldown mapping and see if mapping exists
	public static boolean isInMapping(String toCheckString) {
		String line = "";
		String value = "";
		int count = 0;
		String name = "";
		String mappingFile = "/usr/local/apache-tomcat-7.0.37"
				+ "/webapps/ROOT/drilldown.txt";
		// Read the mapping to drilldown
		HashMap<String, String> mapDrillDown = new HashMap();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					mappingFile));
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				StringTokenizer stk = new StringTokenizer(line, " ");
				count = 1;
				if (line.equals(""))
					continue;
				System.out.println(line + ";token size:" + stk.countTokens());
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();
					if (count == 1) {
						name = value;
					} else if (count == 2) {
						mapDrillDown.put(name, value);
					}
					count++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(" mapDrillDown: "
				+ mapDrillDown.containsKey(toCheckString) + " toCheckString:"
				+ toCheckString);
		if (mapDrillDown.containsKey(toCheckString))
			return true;
		else
			return false;

	}

	// Read default Metrics (from File) and search for Anomaly
	public static void FindAnomalyForDefaultMetrics(String configAlert,
			String LogFile, String LLogFile, String AnnotationAlert,
			String flagEpochTime, String delimiter,
			String openTSDBServerIPnPort, String HoursAgo,
			String InterestHosts, String hostListFile, String hTimeZone,
			String DefaultMetricsFile, String BinType, String LocalAnomalyType) {
		String line = "";
		int limitAnomaly = 40;

		System.out.println("FindAnomalyForDefaultMetrics: " + configAlert
				+ " : " + LogFile + " : " + LLogFile + " : " + AnnotationAlert
				+ " : " + flagEpochTime + " : " + delimiter + " : "
				+ openTSDBServerIPnPort + " : " + HoursAgo + " : "
				+ InterestHosts + " : " + hostListFile + " : " + hTimeZone
				+ " : " + DefaultMetricsFile);

		HashMap<Integer, String> mapValue = new HashMap();
		HashMap<String, String> mapTSValue = new HashMap();
		HashMap<String, String> mapTSMetName = new HashMap();
		HashMap<String, String> mapisDistinct = new HashMap();

		String anomaly = "";
		String data = "";
		String value = "";
		int cnt = 0;
		String met = "";
		String TS = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					configAlert));

			FileWriter writer = new FileWriter(LLogFile, true);
			// read each metrics from configAlert
			while ((line = reader.readLine()) != null) {
				mapValue = getOpenTSDBdata(line, "", "", "", "", "", "no",
						InterestHosts, hostListFile, openTSDBServerIPnPort,
						HoursAgo, hTimeZone, 40, "#", 60.0, BinType,
						LocalAnomalyType); // only 40 max

				// check if anomaly exists
				if (mapValue.containsKey(3)) {
					data = mapValue.get(2);
					anomaly = mapValue.get(3);

					// write only alert not exists
					BufferedReader reader2 = new BufferedReader(new FileReader(
							LLogFile));
					while ((line = reader2.readLine()) != null) {
						StringTokenizer stk2 = new StringTokenizer(line, "#");
						cnt = 0;
						// read each token of TS and value ( metrics value)
						while (stk2.hasMoreTokens()) {
							value = stk2.nextToken();
							cnt++;
							if (cnt == 2)
								met = value;
							else if (cnt == 3) {
								System.out.println("existings->" + met + value);
								mapisDistinct.put(met + value, "");
							}

						}
					}

					// data.replaceAll("\\n", "#");
					StringTokenizer stk1 = new StringTokenizer(data, "#");
					// read each token of TS and value ( metrics value)
					while (stk1.hasMoreTokens()) {
						cnt++;
						if (cnt == 1)
							continue; // skip first line
						value = stk1.nextToken();
						// System.out.println("6. split ts,value-> "
						// +value.substring(0, 19)
						// +" : "+ value.substring(21, value.length()));
						mapTSValue.put(value.substring(0, 19),
								value.substring(21, value.length()));

					}
					cnt = 0;
					// read anomaly data
					stk1 = new StringTokenizer(anomaly, "#");
					while (stk1.hasMoreTokens()) {
						cnt++;
						// if(cnt==1) continue; //skip first line
						value = stk1.nextToken();
						// System.out.println(" 6. split ts,metName-> "
						// +value.substring(0, value.indexOf(","))
						// +" : "+ value.substring(value.indexOf(",")+1,
						// value.length()));
						mapTSMetName.put(
								value.substring(0, value.indexOf(",")),
								value.substring(value.indexOf(",") + 1,
										value.length()));
					}
					// write to file
					for (String s : mapTSMetName.keySet()) {
						System.out.println(" 63.Histogram Anomaly" + "#"
								+ mapTSMetName.get(s) + "#" + s + "#"
								+ mapTSValue.get(s) + " isavailable?:"
								+ mapTSMetName.get(s) + s);

						if (!mapisDistinct.containsKey(mapTSMetName.get(s) + s)) {
							System.out.println(" write:" + mapTSMetName.get(s)
									+ s + " mapisDistinct:" + mapisDistinct);
							writer.append("Histogram Anomaly" + "#"
									+ mapTSMetName.get(s) + "#" + s + "#"
									+ mapTSValue.get(s) + "\n");
							writer.flush();

						} else {
							System.out.println(" not write:"
									+ mapTSMetName.get(s) + s
									+ " mapisDistinct:" + mapisDistinct);
						}

					}

				}

				// System.out.println("\n- HBOS map size ->" + mapAnomaly.size()
				// + " concAnomText:"+concAnomText
				// + " limitAnomaly:"+limitAnomaly +" consText:"+consText);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Read config file for alert log
	public static void writeLogForAlert(String configAlert, String LogFile,
			String LLogFile, String AnnotationAlert, String flagEpochTime,
			String delimiter, String openTSDBServerIPnPort, String HoursAgo,
			String InterestHosts, String hostListFile, String hTimeZone,
			String BinType, String LocalAnomalyType) {
		try {

			System.out.println(configAlert + " | " + LogFile + " | " + LLogFile
					+ " | " + AnnotationAlert + " | " + flagEpochTime + " | "
					+ delimiter + " | " + openTSDBServerIPnPort + " | "
					+ HoursAgo + " | " + InterestHosts + " | " + hostListFile
					+ " | " + hTimeZone);

			if (InterestHosts == null)
				InterestHosts = "host=db901.qc.omniture.com";
			// missing host=
			if (InterestHosts.indexOf("host=") == -1)
				InterestHosts = "host=" + InterestHosts;

			BufferedReader reader = new BufferedReader(new FileReader(
					configAlert));
			FileWriter writer = new FileWriter(LogFile, true);
			String TS_noSeconds = "";
			TreeMap<String, Integer> mapTSCheckNGreaterValue = new TreeMap();
			int count2 = 0;
			String line = "";
			String value = "";
			HashMap<Integer, String> mapValue = new HashMap();
			// HashMap<Integer,String> mapValue2 = new HashMap();
			TreeMap<String, String> mapMatchedValue = new TreeMap();
			TreeMap<Long, String> mapMatchedValue2 = new TreeMap();
			TreeMap<Long, String> mapMetrics2 = new TreeMap();
			// get map alias
			HashMap<String, String> mapHostAlias = getHostAlias(hostListFile);
			// HashMap<String,String> mapConfig =
			// config.getConfig("/usr/share/apache-tomcat-7.0.37/webapps/monitor/WebContent/config.txt");
			int count = 0;
			String condition = "";
			int limit = 0;
			String metricName = "";
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				count = 0;
				StringTokenizer stk1 = new StringTokenizer(line, delimiter);
				// read each token
				while (stk1.hasMoreTokens()) {
					count++;
					value = stk1.nextToken();
					System.out.println("  for Token,value-> " + count + " "
							+ value);

					if (count == 1) {
						metricName = value;
						System.out
								.println("  From writeLogForAlert->getOpenTSDB "
										+ count
										+ " "
										+ value
										+ " metricName:"
										+ metricName
										+ " "
										+ "no"
										+ " "
										+ InterestHosts
										+ " "
										+ hostListFile
										+ " "
										+ openTSDBServerIPnPort
										+ " "
										+ HoursAgo + " " + hTimeZone);
						// mapValue = getOpenTSDBdata(value, "","",""
						// ,"","","no",host,openTSDBServerIPnPort,HoursAgo,-1);
						mapValue = getOpenTSDBdata(metricName, "", "", "", "",
								"", "no", InterestHosts, hostListFile,
								openTSDBServerIPnPort, HoursAgo, hTimeZone, 40,
								"", 60.0, BinType, LocalAnomalyType); // only 40
																		// max

						// mapValue2 = getOpenTSDBdata(value, "","",""
						// ,"","","yes");
					} else if (count == 2) {
						condition = value;
					} else if (count == 3) {
						limit = new Integer(value);
					}
				}

				String metricValues = mapValue.get(2);
				System.out.println(" mapValue.size:" + mapValue.size()
						+ " condition:" + condition + " limit:" + limit
						+ " metricValues:" + metricValues);
				StringTokenizer stk2 = new StringTokenizer(metricValues, "\\n");
				value = "";
				count = 0;
				// Pattern pattern = Pattern.compile("-?[0-9]+");
				// System.out.println("metricValues:"+metricValues
				// +";stk2:"+stk2.countTokens() );
				String TS = "";
				SimpleDateFormat sdf = new SimpleDateFormat(
						"dd/MM/yyyy HH:mm:ss");
				long timeInMillisSinceEpoch, timeInMinutesSinceEpoch;

				// condition set
				if (condition.equals("Equals")) {
					// each token
					while (stk2.hasMoreTokens()) {
						value = stk2.nextToken();
						count++;
						if (count == 1 || value.indexOf(",") == -1)
							continue;
						TS = value.substring(0, value.indexOf(",") - 1);
						Date date = sdf.parse(TS);
						timeInMillisSinceEpoch = date.getTime(); // / (60 *
																	// 1000);

						value = value.substring(value.indexOf(",") + 1,
								value.length());
						System.out.println("inside :" + condition + " : "
								+ value + "<<-");
						//
						if (new Integer(value) == limit) {
							writer.append("Rule_Equals_Limit_" + limit + "#"
									+ metricName + "#" + TS + "#" + value
									+ "\n");
							// for limited version log file
							mapMatchedValue.put(TS, value);
							mapMatchedValue2.put(timeInMillisSinceEpoch, value);
							mapMetrics2.put(timeInMillisSinceEpoch, metricName);
						}
					}
				} else if (condition.equals("Greater")) {
					while (stk2.hasMoreTokens()) {
						value = stk2.nextToken();
						count++;

						if (count == 1 || value.indexOf(",") == -1)
							continue;
						TS = value.substring(0, value.indexOf(","));
						Date date = sdf.parse(TS);
						timeInMillisSinceEpoch = date.getTime(); // / (60 *
																	// 1000);
						// System.out.println( "0.value:" +value );
						value = value.substring(value.indexOf(",") + 1,
								value.length());
						// System.out.println( "1.value:" +value );
						//
						if (new Integer(value) > limit) {

							System.out.println("2.value:"
									+ value
									+ " value.indexOf( , ):"
									+ value.indexOf(",")
									+ " value.indexOf(,)+1:"
									+ value.indexOf(",")
									+ 1
									+ "inside :"
									+ condition
									+ " : "
									+ value
									+ " EpochTS:"
									+ timeInMillisSinceEpoch
									+ " TS:"
									+ TS
									+ "<<-mapHostAlias.get:"
									+ mapHostAlias.get(InterestHosts.replace(
											"host=", "")) + " mapHostAlias:"
									+ mapHostAlias);

							writer.append("Rule_Greater_Limit_"
									+ limit
									+ "#"
									+ metricName.trim().replaceAll(" ", "")
									+ mapHostAlias.get(InterestHosts.replace(
											"host=", "")) + "#" + TS // .replace(" ",
																		// "_")
									+ "#" + value + "\n");
							// for limited version log file
							mapMatchedValue.put(
									"Rule_Greater_Limit_"
											+ limit
											+ "#"
											+ metricName.trim().replaceAll(" ",
													"")
											+ mapHostAlias.get(InterestHosts
													.replace("host=", ""))
											+ "#" + TS + "#", value);
							mapMatchedValue2.put(timeInMillisSinceEpoch, value);
							mapMetrics2.put(timeInMillisSinceEpoch, metricName);
						}
					}
				} else if (condition.equals("Lesser")) {
					while (stk2.hasMoreTokens()) {
						value = stk2.nextToken();
						count++;
						if (count == 1 || value.indexOf(",") == -1)
							continue;
						TS = value.substring(0, value.indexOf(",") - 1);
						Date date = sdf.parse(TS);
						timeInMillisSinceEpoch = date.getTime(); // / (60 *
																	// 1000);

						value = value.substring(value.indexOf(",") + 1,
								value.length());
						System.out.println("inside :" + condition + " : "
								+ "<<-");
						//
						if (new Integer(value) < limit) {
							writer.append("Rule_Lesser_Limit_" + limit + "#"
									+ metricName + "#" + TS + "#" + value
									+ "\n");
							// for limited version log file
							mapMatchedValue.put(TS, value);
							mapMatchedValue2.put(timeInMillisSinceEpoch, value);
							mapMetrics2.put(timeInMillisSinceEpoch, metricName);
						}
					}
				} else if (condition.equals("20Greater")) {
					while (stk2.hasMoreTokens()) {
						value = stk2.nextToken();
						count++;
						int diff_value = 0;
						if (count == 1 || value.indexOf(",") == -1)
							continue;
						TS = value.substring(0, value.indexOf(","));
						TS_noSeconds = TS.substring(0, TS.length() - 3);
						String TS_noMinutes = TS_noSeconds.substring(0,
								TS_noSeconds.length() - 3);
						int minute = new Integer(TS_noSeconds.substring(
								TS_noSeconds.length() - 2,
								TS_noSeconds.length()));
						String MinLess5 = "";
						// reduce 5 minutes
						if (minute > 5) {
							MinLess5 = TS_noMinutes + ":"
									+ Integer.toString(minute - 5);
						} else
							continue;

						Date date = sdf.parse(TS);
						timeInMillisSinceEpoch = date.getTime(); // / (60 *
																	// 1000);
						value = value.substring(value.indexOf(",") + 1,
								value.length());
						mapTSCheckNGreaterValue.put(TS_noSeconds, new Integer(
								value));

						// String oneMinLessDT = new
						// java.text.SimpleDateFormat("MM/dd/yyyy HH:mm").
						// format(new java.util.Date (timeInMillisSinceEpoch -
						// 300));

						// Check if data point 5 minute before exists
						if (mapTSCheckNGreaterValue.containsKey(MinLess5)) {
							System.out.println("inside contains 5 mins data:"
									+ count2 + ":" + timeInMillisSinceEpoch
									+ "<-60>:" + (timeInMillisSinceEpoch - 60));
							diff_value = (new Integer(value) - mapTSCheckNGreaterValue
									.get(MinLess5)) / new Integer(value);
						} else {
							System.out.println("No contains 5 mins data:"
									+ count2 + ":" + timeInMillisSinceEpoch
									+ " <-60>:" + (timeInMillisSinceEpoch - 60)
									+ " oneMinLessDT:" + MinLess5);
						}

						System.out.println("->inside :"
								+ condition
								+ " : "
								+ value
								+ " EpochTS:"
								+ timeInMillisSinceEpoch
								+ " TS:"
								+ TS
								+ " diff_value:"
								+ diff_value
								+ " TS_noSeconds:"
								+ TS_noSeconds
								+ " TS_noSeconds-60:"
								+ (timeInMillisSinceEpoch - 60)
								+ " Minute:"
								+ TS_noSeconds.substring(
										TS_noSeconds.length() - 2,
										TS_noSeconds.length()) + " MinLess5:"
								+ MinLess5 + " TS_noMinutes:" + TS_noMinutes
								+ "<<-");

						// raise as 20% in 5 minutes time window
						if (diff_value > 10) {
							System.out.println("inside 20% increase!! ");
							writer.append("Rule_20Greater_Limit_" + "20%" + "#"
									+ metricName + "#" + TS // .replace(" ",
															// "_")
									+ "#" + value + "\n");
							// for limited version log file
							mapMatchedValue.put("Rule_20Greater_Limit_" + "20%"
									+ "#" + metricName + "#" + TS + "#", value); // human
																					// read
																					// date
																					// format
							// mapMetrics2.put(timeInMillisSinceEpoch,
							// metricName);
						}
					}
				} else if (condition.equals("10Greater")) {

				} else if (condition.equals("5Greater")) {

				}
				writer.flush();
			} // end of outer while loop
			boolean flag = true;
			count = mapMatchedValue.size();
			// FileWriter writer2 = null;

			// why we hve this
			/*
			 * writer2 = new FileWriter(LLogFile,false); //write to limited log
			 * alert file for(String s:mapMatchedValue.keySet() ){ count--;
			 * if(count <= 5 ){//write only latest 10 log files
			 * //System.out.println( " lol..inside "); writer2.append(s +
			 * mapMatchedValue.get(s) + "\n"); writer2.flush(); //flag = false;
			 * } } writer2.close();
			 */
			flag = true;
			count = mapMatchedValue2.size();

			FileWriter writer3 = new FileWriter(AnnotationAlert, false); // note
																			// false
																			// ,
																			// only
																			// few
																			// show
																			// in
																			// UI
			// write epoch TS
			for (Long s : mapMatchedValue2.keySet()) {
				count--;
				if (count <= 5) {// write only latest 10 log files
					// System.out.println( " lol..inside 2 : " +s +":"+
					// mapMatchedValue2.get(s));
					writer3.append(condition + "#" + mapMetrics2.get(s) + "#"
							+ s + "#" + mapMatchedValue2.get(s) + "\n");
					writer3.flush();
					// flag = false;
				}
			}
			writer3.close();
		} catch (Exception e) {
			System.out.println(" writeLogForAlert error 1: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// get the map for alias of Host
	public static HashMap<String, String> getHostAlias(String FilePath) {
		HashMap<String, String> mapHostAlias = new HashMap();
		String line = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(FilePath));

			// read each line of given file
			while ((line = reader.readLine()) != null) {
				mapHostAlias.put(line.substring(0, line.indexOf("(")),
						line.substring(line.indexOf("("), line.length()));
				System.out.println(line.substring(0, line.indexOf("(")) + " : "
						+ line.substring(line.indexOf("("), line.length()));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapHostAlias;
	}

	// construct the subpart of URL string to send to OpenTSDB
	public static String getConstructHostMetricsString(String hostList,
			String MetricList) {
		System.out.println(" getConstructHostMetricsString:" + hostList + " : "
				+ MetricList);
		String value = "", value2 = "", concPartURL = "";
		if (MetricList.indexOf("#") >= 0) {
			MetricList = MetricList.substring(0, MetricList.indexOf("#"));
			System.out.println(" 1.0revised getConstructHostMetricsString:"
					+ hostList + " : " + MetricList);
		}

		try {
			// >1 host name present
			if (hostList.indexOf(";") >= 0 || MetricList.indexOf(";") >= 0) {

				StringTokenizer stk1 = new StringTokenizer(hostList, ";");
				// renderedText of whole HTTP GET split
				while (stk1.hasMoreTokens()) {
					value = stk1.nextToken().trim();
					StringTokenizer stk2 = new StringTokenizer(MetricList, ";");
					while (stk2.hasMoreTokens()) {
						value2 = stk2.nextToken().trim();
						concPartURL = concPartURL + "&m=avg:" + value2
								+ "{host=" + value + "}";

					}
				}

			} else {
				concPartURL = concPartURL + "&m=avg:" + MetricList + "{host="
						+ hostList + "}" + "&ascii";
			}

			System.out.println(" out.concPartURL:" + concPartURL);

			// http://10.162.181.174:4243/q?start=24h-ago&m=avg:mysql.queries{host=3}&m=avg:mysql.queries{host=db901.qc.omniture.com}&ascii

		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
		return concPartURL;
	}

	// find n th index
	public static int getNindex(String value, String subString, int N) {
		String conctStr = "";
		int i = 0;
		int ind = -1;
		int cnt = 0;
		try {
			while (cnt <= N) {
				cnt++;
				if (cnt == 1)
					ind = value.indexOf(subString);
				else
					ind = value.indexOf(subString, ind + 1);
				if (cnt == N)
					return ind;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	// get getRecursiveStringN
	public static String getRecursiveStringN(int cnt) {
		String conctStr = "";
		try {
			int i = 1;
			// while loop
			while (i <= cnt) {
				conctStr = conctStr + ",";
				System.out.println("0.cnt:" + cnt + " conctStr:" + conctStr);
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("cnt:" + cnt + " conctStr:" + conctStr);
		return conctStr;
	}

	// wrapper to call getOpenTSDB
	public static HashMap<Integer, String> wrappergetOpenTSDBdata(
			String metricName, String host, String FromDate, String ToDate) {
		HashMap<Integer, String> outMap = new HashMap<Integer, String>();
		try {

			// hard-code
			outMap = getOpenTSDBdata(
					metricName,
					FromDate,
					ToDate,
					"",
					"",
					"",
					"no",
					"host=mirror.qe1.ut1.omniture.com",
					"/usr/share/apache-tomcat-7.0.37/webapps/monitor/hostList.txt",
					"10.162.181.174:4243", "0", "CST", -1, "", 0, "", "");

			System.out.println(" wrapper out:" + outMap.get(3) + " <--> "
					+ outMap.get(2) + " <--> " + outMap.get(1) + " metricName:"
					+ metricName + " FromDate:" + FromDate + " ToDate:"
					+ ToDate);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return outMap;
	}

	// input: 2013/06/06-19:09:10 output:
	public static String getOneMinuteLess(String date) {
		String newDate = "";
		String Snew_value = "";
		try {
			int f_idx = date.indexOf(":");
			int s_idx = date.indexOf(":", f_idx + 1);
			int new_value = 0;

			String Sold_value = date.substring(f_idx + 1, s_idx);
			int old_value = new Integer(date.substring(f_idx + 1, s_idx));

			if (old_value > 0) {
				new_value = old_value - 1;
				Snew_value = String.valueOf(new_value);

				if (Snew_value.length() == 1)
					Snew_value = "0" + Snew_value;
				if (Sold_value.length() == 1)
					Sold_value = "0" + Sold_value;

				newDate = date.replace(":" + Sold_value + ":", ":" + Snew_value
						+ ":");
			} else
				newDate = date;

			System.out.println("two indexes " + f_idx + " : " + s_idx
					+ " substring ->" + date.substring(f_idx + 1, s_idx)
					+ " Sold_value:" + Sold_value + " Snew_value:" + Snew_value
					+ " old_value:" + old_value + " newDate:" + newDate);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return newDate;

	}

	// getOpenTSDBdata
	public static HashMap<Integer, String> getOpenTSDBdata_dummy(
			String new_metric, String FromDate, String ToDate,
			String FromRange, String ToRange, String Checkbox,
			String epochFlag, String host, String HostAliasFile,
			String TSDBipPort, String HoursAgo, String iTimeZone,
			int limitAnomaly, String newDelim, double PercChange,
			String BinType, String LocalAnomalyType) {
		System.out.println("dummy tools4  input params: " + new_metric + " | "
				+ FromDate + " | " + ToDate + " | " + FromRange + " | "
				+ ToRange + " | " + Checkbox + " | " + epochFlag + " | " + host
				+ " | " + HostAliasFile + " | " + TSDBipPort + " | " + HoursAgo
				+ " | " + iTimeZone + " | " + limitAnomaly + " |newDelim "
				+ newDelim + " | " + PercChange + " | " + BinType + " | "
				+ LocalAnomalyType);
		HashMap<Integer, String> outMap = new HashMap();
		try {

			outMap.put(1, "url");
			outMap.put(2, "time,value\\n2008-05-07,20;75;100");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return outMap;
	}

	// get the data from openTSDB
	public static HashMap<Integer, String> getOpenTSDBdata(String new_metric,
			String FromDate, String ToDate, String FromRange, String ToRange,
			String Checkbox, String epochFlag, String host,
			String HostAliasFile, String TSDBipPort, String HoursAgo,
			String iTimeZone, int limitAnomaly, String newDelim,
			double PercChange, String BinType, String LocalAnomalyType) {
		System.out.println("tools4  input params: " + new_metric + " | "
				+ FromDate + " | " + ToDate + " | " + FromRange + " | "
				+ ToRange + " | " + Checkbox + " | " + epochFlag + " | " + host
				+ " | " + HostAliasFile + " | " + TSDBipPort + " | " + HoursAgo
				+ " | " + iTimeZone + " | " + limitAnomaly + " |newDelim "
				+ newDelim + " | " + PercChange + " | " + BinType + " | "
				+ LocalAnomalyType);

		String delimiter = " ";
		int file_flag = 0;
		int ivalue = 0;
		String line = "";
		String value = "";
		String value2 = "";
		boolean flag = false;
		String RecursiveStringN = "";
		int count = 0;
		String ts = "";
		String consText = "";
		HashMap<Integer, String> outMap = new HashMap();
		int iFromRange = 0;
		int iToRange = 0;

		// FromDate
		if (FromDate != null) {
			if (FromDate.length() > 5 && FromRange != null) { // caution on this
																// condition
				if (!FromRange.equalsIgnoreCase("")) {
					iFromRange = new Integer(FromRange);
					iToRange = new Integer(ToRange);
				}
			}
		} else {
			FromDate = "";
			ToDate = ""; // replace null with ""
		}

		// get map alias
		HashMap<String, String> mapHostAlias = getHostAlias(HostAliasFile);

		String concPartURL = "";
		String urlString = "";
		try {

			FileWriter writer = new FileWriter(
					"/usr/share/apache-tomcat-7.0.37/webapps/dmonitor/debug.txt");

			concPartURL = getConstructHostMetricsString(host, new_metric); // list
																			// of
																			// items
			// > 1 host chosen
			if (host.indexOf(";") >= 0 || new_metric.indexOf(";") >= 0) {

				// if date range given
				if (FromDate.length() > 1 && ToDate.length() > 1) {

					// both date should not be same, so reduce the ToDate to 60
					// seconds lesser
					if (FromDate.equalsIgnoreCase(ToDate)) {
						System.out.println("fromdate=todate ; -> " + ToDate
								+ " " + FromDate);
						getOneMinuteLess(FromDate);
						// ToDate = String.valueOf( (Integer.valueOf(ToDate) -60
						// ) );

					}

					urlString = "http://" + TSDBipPort + "/q?start="
							+ FromDate.replace(" ", "-") + "&end="
							+ ToDate.replace(" ", "-") + concPartURL + "&ascii";
				} else {
					// construct the proper URL part of host and metrics
					urlString = "http://" + TSDBipPort + "/q?start=" + HoursAgo
							+ "h-ago" + concPartURL + "&ascii";
				}
				System.out.println(">1 host; return concPartURL :"
						+ concPartURL + " : " + "http://" + TSDBipPort
						+ "/q?start=" + HoursAgo + "h-ago" + concPartURL
						+ "&ascii");
			} else { // only 1 host chosen
				host = "{host=" + host + "}";
				// extract
				/*
				 * if(new_metric.indexOf("#") >=0){ new_metric =
				 * new_metric.substring(0,new_metric.indexOf("#"));
				 * 
				 * }
				 */

				// check if data range given
				if (FromDate.length() > 1 && ToDate.length() > 1) {

					// both date should not be same, so reduce the ToDate to 60
					// seconds lesser
					if (FromDate.equalsIgnoreCase(ToDate)) {
						System.out.println(" one host ->fromdate=todate ; -> "
								+ ToDate + " " + FromDate);
						// get the minute part and reduce one minute for
						// "FromDate"
						FromDate = getOneMinuteLess(FromDate);
						// ToDate = String.valueOf( (Integer.valueOf(ToDate) -60
						// ) );

					}

					urlString = "http://" + TSDBipPort + "/q?start="
							+ FromDate.replace(" ", "-") + "&end="
							+ ToDate.replace(" ", "-") + concPartURL;
				} else {
					urlString = "http://" + TSDBipPort + "/q?start=" + HoursAgo
							+ "h-ago" + concPartURL;
				}

			}
			urlString = urlString.replace("host=host=", "host=").replace(
					"m=avg:m=avg:", "m=avg:");

			System.out.println("1.0 final fix urlString: " + urlString);

			// String urlString =
			// "http://127.0.0.1:4243/q?start=30s-ago&m=avg:"+new_metric
			// +"&ascii";
			// if data range given
			/*
			 * if(FromDate != null){ if(FromDate.length() >1 && ToDate.length()
			 * >1){ // first host for ; metrics second host for last metrics
			 * urlString = "http://"+TSDBipPort+"/q?start="+
			 * FromDate.replace(" ", "-") +"&end="+ ToDate.replace(" ", "-")
			 * +"&m=avg:"+new_metric.replace(";", host+";") + host+"&ascii";
			 * 
			 * //urlString = "http://127.0.0.1:4243/q?start="+
			 * FromDate.replace(" ", "-") +"&end="+ ToDate.replace(" ", "-") //
			 * +"&m=avg:"+new_metric+"&ascii"; } }
			 */
			urlString = urlString.replace(" ", "");
			// out.println(urlString);
			outMap.put(1, urlString);

			TreeMap<String, String> NameValue = new TreeMap();
			int cnt = 0;
			URL url = new URL(urlString);
			// HttpURLConnection conn = (HttpURLConnection)
			// url.openConnection();
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(1000);
			conn.setReadTimeout(2000);
			Source source = new Source(conn);
			String renderedText = source.getRenderer().toString();

			// System.out.println(
			// "1.urlString: "+urlString+" start of render->"+renderedText
			// +"<-end of render");
			String date = null;
			DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			format.setTimeZone(TimeZone.getTimeZone(iTimeZone));
			StringTokenizer stk1 = new StringTokenizer(renderedText, " ");
			// out.println("rendered Text : "+renderedText +"\n");
			count = 1;
			Pattern pattern = Pattern.compile("-?[0-9]+");
			String metric = "";
			TreeMap<Integer, String> mapMetrics0 = new TreeMap();
			TreeMap<Integer, String> mapMetrics = new TreeMap();
			TreeMap<Integer, String> mapDistinctMetrics = new TreeMap();
			TreeMap<String, String> mapDistMetrics = new TreeMap();
			TreeMap<Integer, String> mapHostName = new TreeMap();
			TreeMap<Integer, String> maplnHostAlias = new TreeMap();
			TreeMap<Integer, String> mapTS = new TreeMap();
			TreeMap<Integer, String> mapEpochTS = new TreeMap();
			TreeMap<Integer, String> mapValue = new TreeMap();
			TreeMap<String, String> mapAnomaly = new TreeMap();
			TreeMap<String, String> mapLocalAnomaly = new TreeMap();
			TreeMap<String, String> newMapMetrics = new TreeMap();
			String concAnomText = "";
			int lineNumber = 1;
			// renderedText of whole HTTP GET split
			while (stk1.hasMoreTokens()) {
				value = stk1.nextToken().trim();
				Matcher matcher = pattern.matcher(value);
				Matcher matcher2 = pattern.matcher(value);

				// out.println( lineNumber+":"+value //+ matcher.find()
				// +":"+value.length()+":"+value.indexOf("mysql.")+"\n");
				// metrics value
				if (matcher.find() && (count == 3)) {
					// System.out.println(" tools2 Ranges:"+iFromRange+" "+iToRange
					// + " Checkbox "+Checkbox);
					// if range value not given
					if (iFromRange == 0 && iToRange == 0)
						mapValue.put(lineNumber, value);
					else { // if range value provided
						ivalue = Integer.valueOf(value);
						System.out.println(" tools2 Ranges:" + value + " "
								+ iFromRange + " " + iToRange + " Checkbox "
								+ Checkbox);
						// if inclusive = yes
						if (Checkbox.equals("yes")) {
							System.out.println(" tools2 inside yes:"
									+ iFromRange + " " + iToRange
									+ " Checkbox " + Checkbox);
							if (ivalue >= iFromRange && ivalue <= iToRange) {
								mapValue.put(lineNumber, value);
							}
						} else { // if inclusive = no
							if (ivalue > iFromRange && ivalue < iToRange)
								mapValue.put(lineNumber, value);
						}
					}
				} else if (value.indexOf("host=") >= 0) { // host machine name
					mapHostName.put(lineNumber, value);
					maplnHostAlias.put(lineNumber, mapHostAlias.get(mapHostName
							.get(lineNumber).replace("host=", "")));
					// System.out.println(lineNumber +
					// " : "+mapHostAlias.get(mapHostName.get(lineNumber).replace("host=",
					// "") )+" : "+
					// mapHostName.get(lineNumber));
					lineNumber++; // continue;
					// out.println("inside 3");
				} else if (value.indexOf(".") >= 0) { // metrics name
					mapMetrics0.put(lineNumber, value);
					/*
					 * if(!mapDistinctMetrics.containsValue(value)){
					 * mapDistinctMetrics.put(mapDistinctMetrics.size()+1,
					 * value); }
					 */
					// out.println("inside 2");
				} else if (matcher2.find() && value.length() == 10) { // timeStamp
					mapEpochTS.put(lineNumber, value); // has epoch time stamp
					// out.println("inside 4");
					date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
							.format(new java.util.Date(
									Long.valueOf(value) * 1000));

					mapTS.put(lineNumber, date);

				}
				// System.out.println("count,value->"+count +","+valueParsed
				// <TimeStamp,Value>->" +mapTS.get(lineNumber) +":"+
				// mapValue.get(lineNumber));
				count++;
				//
				if (count > 4) {
					count = 1;
				}

				// timestamp length
				// if(ts.length()>5)
				// NameValue.put( date ,value2); //ts to change here
			}// end of outer while loop

			// writer.append("name,value\n"); //,value2\n");
			String header = "Time";
			cnt = 0;
			// rename metrics name to suffix with Alias of Server
			for (int i : mapMetrics0.keySet()) {
				// System.out.println( " rename alias:" + mapMetrics0.get(i)
				// +" : "+ mapHostAlias.get(mapHostName.get(i)) +
				// " : "+host +" : "+mapHostName.get(i));
				mapMetrics.put(i, mapMetrics0.get(i) + maplnHostAlias.get(i));
				// count distinct metrics
				if (!mapDistinctMetrics.containsValue(mapMetrics0.get(i)
						+ maplnHostAlias.get(i))) {
					cnt++;
					mapDistinctMetrics.put(cnt, mapMetrics0.get(i)
							+ maplnHostAlias.get(i));
				}
			}
			cnt = 1;
			// get the header
			while (cnt <= mapDistinctMetrics.size()) {
				header = header + "," + mapDistinctMetrics.get(cnt);
				cnt++;
			}
			header = header + "\\n";
			System.out.println(" tools2 " + urlString + " header: " + header
					+ " mapDistinctMetrics->" + mapDistinctMetrics);
			System.out.println(" tools2 -->mapMetrics:" + mapMetrics.size()
					+ " mapTS: " + mapTS.size() + " : =mapValue:"
					+ mapValue.size() + " distinct3:"
					+ mapDistinctMetrics.size() + "<-- header:" + header
					+ " iToRange:" + iToRange + " iToRange:" + iToRange
					+ " limitAnomaly:" + limitAnomaly);

			/*
			 * if(mapDistinctMetrics.size() > 1){ RecursiveStringN =
			 * getRecursiveStringN(mapDistinctMetrics.size()); for(int
			 * t:mapTS.keySet()){ newMapMetrics.put(mapTS.get(t) ,
			 * RecursiveStringN); //System.out.println("mapts:"+mapTS.get(t) +
			 * RecursiveStringN); } }
			 */
			int index = 0;

			// write unique key-value to file - single metrics
			if (mapDistinctMetrics.size() == 1) {
				// no range provided
				if (iFromRange == 0 && iToRange == 0) {

					if (limitAnomaly > 0) {
						// Type 1: Anomaly Detection (HBOS)
						mapAnomaly = HBOS(mapMetrics, mapTS, mapValue,
								limitAnomaly);
						count = 0;
						// concAnomText
						for (String s : mapAnomaly.keySet()) {
							count++;
							// more than 5 anomaly just break
							if (count > 5)
								break;
							concAnomText = concAnomText + s + ","
									+ mapAnomaly.get(s) + ",HBOS#";
						}

						System.out.println("only histogram concAnomText:"
								+ concAnomText);
						/**
						 * Anomaly a = new Anomaly(); //Type 2: Localized
						 * anomaly mapLocalAnomaly=a.localAnomaly(mapMetrics,
						 * mapTS, mapValue,PercChange,BinType,LocalAnomalyType);
						 * // more than N % raise or fall count=0; //iterate
						 * each got points for(String
						 * sl:mapLocalAnomaly.keySet()){ count++;
						 * System.out.println("only mapLocalAnomaly ");
						 * if(sl.equalsIgnoreCase("999")) continue;
						 * 
						 * // if more than 100 if(count>30) break; concAnomText
						 * = concAnomText + sl + ","+
						 * mapLocalAnomaly.get(sl).substring
						 * (0,mapLocalAnomaly.get(sl).indexOf(",") ) +"," +
						 * mapLocalAnomaly
						 * .get(sl).substring(mapLocalAnomaly.get(
						 * sl).indexOf(",")+1, mapLocalAnomaly.get(sl).length()
						 * ) +"#"; }
						 * 
						 * if(mapAnomaly.size() > 0 ){
						 * outMap.put(3,concAnomText); }
						 */
						// local anomaly
						if (mapLocalAnomaly.size() > 0) {
							consText = mapLocalAnomaly.get("999");
							System.out.println("else mapLocalAnomaly>0");
							mapLocalAnomaly.remove("999");
						} else {
							System.out
									.println("else mapLocalAnomaly:epochFlag:"
											+ epochFlag);
							// concatenate the TS and value to output
							for (int t : mapTS.keySet()) {
								if (epochFlag.equals("yes"))
									consText = consText + mapEpochTS.get(t)
											+ "," + mapValue.get(t) + "\\n";
								else {

									if (newDelim.equals("#"))
										consText = consText + mapTS.get(t)
												+ "," + mapValue.get(t) + "#";
									else
										consText = consText + mapTS.get(t)
												+ "," + mapValue.get(t) + "\\n";
								}

								// System.out.println(
								// " tools2  inside size=one: consText "+header
								// + consText );
								// writer.append(t+","+ NameValue.get(t) +"\n");
								// writer.flush();
							}

						}

						System.out.println("local added concAnomText:"
								+ concAnomText + "\nHBOS.mapAnomaly.size:"
								+ mapAnomaly.size() + " mapLocalAnomaly.size:"
								+ mapLocalAnomaly.size());
						// System.out.println("\n- HBOS map size ->" +
						// mapAnomaly.size() + " concAnomText:"+concAnomText
						// + " limitAnomaly:"+limitAnomaly
						// +" consText:"+consText);
					} // end of if for limitAnomaly
					else { // no anomaly limit given, so skip anomaly, but
							// iterate for output

						// concatenate the TS and value to output
						for (int t : mapTS.keySet()) {
							if (epochFlag.equals("yes"))
								consText = consText + mapEpochTS.get(t) + ","
										+ mapValue.get(t) + "\\n";
							else {

								if (newDelim.equals("#"))
									consText = consText + mapTS.get(t) + ","
											+ mapValue.get(t) + "#";
								else
									consText = consText + mapTS.get(t) + ","
											+ mapValue.get(t) + "\\n";
							}
							System.out.println("no anomaly limit given");
							// System.out.println(
							// " tools2  inside size=one: consText "+header +
							// consText );
							// writer.append(t+","+ NameValue.get(t) +"\n");
							// writer.flush();
						}

					}

				} else { // range option provided

					System.out.println("Range option IN");

					for (int ln : mapValue.keySet()) {
						if (epochFlag.equals("yes"))
							consText = consText + mapEpochTS.get(ln) + ","
									+ mapValue.get(ln) + "\\n";
						else
							consText = consText + mapTS.get(ln) + ","
									+ mapValue.get(ln) + "\\n";
					}
					System.out.println("Range option OUT");
				}
				outMap.put(2, header + consText);
				outMap.put(4, Integer.toString(mapLocalAnomaly.size())); // insert
																			// the
																			// size
																			// of
																			// local
																			// anomaly

				// debug place
				writer.append("------------before return from getOpenTSDB-----------:mapValue.size "
						+ mapValue.size());
				// write to debug file
				for (int f : mapValue.keySet()) {
					writer.append(f + " " + mapEpochTS.get(f) + " "
							+ mapTS.get(f) + " " + mapValue.get(f) + "\n");
					writer.flush();
				}
				writer.close();

				return outMap;
			} else { // more than one metrics
				cnt = 0;
				RecursiveStringN = getRecursiveStringN(mapDistinctMetrics
						.size());
				// precreate the newMapMetrics
				for (int i : mapTS.keySet()) {
					System.out.println(" 0.more than 1 metrics " + mapTS.get(i)
							+ RecursiveStringN);
					newMapMetrics.put(mapTS.get(i), RecursiveStringN);
				}
				// each metrics , find new Metrics concatenation (from row to
				// column)
				for (int t : mapMetrics.keySet()) {
					if (t == 1) {
						newMapMetrics.put(mapTS.get(t), mapValue.get(t));
						continue;
					}

					mapDistMetrics.put(mapMetrics.get(t), "");

					// System.out.println(
					// " current,prev value:"+mapMetrics.get(t)
					// +" : "+mapMetrics.get(t-1) );
					// if there is change in metrics line
					if (!mapMetrics.get(t).equalsIgnoreCase(
							mapMetrics.get(t - 1))
							|| flag == true) {

						System.out.println("\n 1.more than 1 metrics:"
								+ mapMetrics.get(t) + " : "
								+ mapMetrics.get(t - 1)
								+ " ;mapDistMetrics.size():"
								+ mapDistMetrics.size() + " ;epochFlag:"
								+ epochFlag);

						// if already key exists
						// epoch Flag
						if (epochFlag.equals("yes")) {
							if (newMapMetrics.containsKey(mapEpochTS.get(t))) {// if
																				// the
																				// TS
																				// already
																				// exists
								String newValue = newMapMetrics.get(mapEpochTS
										.get(t)) + "," + mapValue.get(t);
								newMapMetrics.put(mapEpochTS.get(t), newValue);
							} else {
								newMapMetrics.put(mapEpochTS.get(t),
										mapValue.get(t));
							}
						} else { // not epoch
									// System.out.println(" 0.not same metrics:"+
									// mapTS.get(t) + " : "+mapValue.get(t));
							value2 = newMapMetrics.get(mapTS.get(t));
							// get already prepared newMapMetrics and find the
							// index where to place this value
							index = getNindex(value2, ",",
									mapDistMetrics.size());

							/*
							 * if(newMapMetrics.containsKey(mapTS.get(t)) ){//
							 * if the TS already exists String newValue =
							 * newMapMetrics.get(mapTS.get(t)) +","
							 * +mapValue.get(t);
							 * //System.out.println(" 1.not same metrics:"
							 * +mapTS.get(t)+RecursiveStringN+mapValue.get(t));
							 * newMapMetrics.put(mapTS.get(t), newValue); }else{
							 */
							/*
							 * String nval = newMapMetrics.get(t).substring( 0,
							 * getNindex
							 * (newMapMetrics.get(t),",",mapDistMetrics.size())
							 * + 1) + mapValue.get(t) +
							 * newMapMetrics.get(t).substring(
							 * getNindex(newMapMetrics
							 * .get(t),",",mapDistMetrics.size()) + 1,
							 * newMapMetrics.get(t).length());
							 */

							// System.out.println(" new string: "+nval);
							// this no exists mean ,generate the blank , for
							// non-available metrics
							// RecursiveStringN=getRecursiveStringN(mapDistMetrics.size());
							// System.out.println(" 2.not same metrics:"+mapTS.get(t)+RecursiveStringN+mapValue.get(t));
							newMapMetrics.put(
									mapTS.get(t),
									value2.substring(0, index + 1)
											+ mapValue.get(t)
											+ value2.substring(index + 1,
													value2.length()));
							// newMapMetrics.put(mapTS.get(t), nval);
							// }

						}
						flag = true;
					} else { // same metrics
						flag = false;

						value2 = newMapMetrics.get(mapTS.get(t));
						// get already prepared newMapMetrics and find the index
						// where to place this value
						index = getNindex(value2, ",", mapDistMetrics.size());

						System.out.println("\n same metrics:" + mapTS.get(t)
								+ " : " + mapValue.get(t)
								+ " mapDistMetrics.size():"
								+ mapDistMetrics.size() + " epochFlag:"
								+ epochFlag + " value2:" + value2 + "index:"
								+ index + " newvalue2:"
								+ value2.substring(0, index + 1)
								+ mapValue.get(t)
								+ value2.substring(index + 1, value2.length()));

						if (epochFlag.equals("yes")) {
							newMapMetrics.put(mapEpochTS.get(t),
									mapValue.get(t)); // TS , value
						} else {
							// newMapMetrics.put(mapTS.get(t), mapValue.get(t)
							// ); //TS , value
							newMapMetrics.put(
									mapTS.get(t),
									value2.substring(0, index + 1)
											+ mapValue.get(t)
											+ value2.substring(index + 1,
													value2.length())); // TS ,
																		// value
						}
					} // end of >1 metrics else
				} // end of for

				/*
				 * if(mapDistMetrics.size() == 2){ for(String
				 * ts1:newMapMetrics.keySet()){ int cnt2 =
				 * count(newMapMetrics.get(ts1) , ",") ;
				 * //System.out.println("new:cnt->" + newMapMetrics.get(ts1) +
				 * cnt2 ); if( cnt2 == mapDistMetrics.size()){
				 * 
				 * } else{ if(cnt2 == 1){ newMapMetrics.put(ts1,
				 * newMapMetrics.get(ts1).replace(",", ",,") ); }
				 * 
				 * 
				 * } } }
				 */

				// No range value passed
				if (iFromRange == 0 && iToRange == 0) {
					System.out.println(" tools2 No Range Value Passed");
					// concatenate
					for (String t : newMapMetrics.keySet()) {

						if (newDelim.equals("#"))
							consText = consText + t + newMapMetrics.get(t)
									+ newDelim;
						else
							consText = consText + t + newMapMetrics.get(t)
									+ "\\n";

					}
				} // Range selected:if any single metric match the range, all
					// other metrics of the TS
					// will also be picked
				else {
					System.out
							.println(" tools2 inside range select concatenation newMapMetrics:"
									+ newMapMetrics.size());
					for (int ln : mapValue.keySet()) {
						// If epoch
						if (epochFlag.equals("yes")) {
							consText = consText + mapEpochTS.get(ln) + ","
									+ newMapMetrics.get(mapEpochTS.get(ln))
									+ "\\n";
						} else {

							if (newDelim.equals("#"))
								consText = consText + mapTS.get(ln) + ","
										+ newMapMetrics.get(mapTS.get(ln))
										+ newDelim;
							else
								consText = consText + mapTS.get(ln) + ","
										+ newMapMetrics.get(mapTS.get(ln))
										+ "\\n";
						}
					}
					consText = consText.replace("null", "");
				}
				// add header to the data.
				consText = header + consText;
				System.out.println("\n final tools2 ;mapAnomaly.size():"
						+ mapAnomaly.size() + " ;header:" + header
						+ " ;consText:" + consText);

				/*
				 * consText = "1361622244,,200\\n" + "1361622260,150,201\\n" +
				 * "1361622290,1,21\\n"+ "1361622305,2,26\\n"+
				 * "1361622321,17,\\n"+ "1361622336,18,77\\n";
				 */
				outMap.put(2, consText);
			} // end of if condition of > or = 1 metrics

		} catch (Exception e) {
			System.out.println(" tools2 - error:" + e.getMessage());
			e.printStackTrace();
		}
		return outMap;
	}

	// method to HBOS
	public static TreeMap<String, String> HBOS(
			TreeMap<Integer, String> mapMetrics,
			TreeMap<Integer, String> mapTS, TreeMap<Integer, String> mapValue,
			int limitAnomaly) {
		int N = mapValue.size(); // no data points
		int k = (int) Math.sqrt(N);
		int noElementEachBin;
		int count = 1;
		int noNonZeroBuckets = 0;
		double minDS, maxDS;
		TreeMap<Integer, Integer> mapDP2Bucket = new TreeMap();
		TreeMap<String, Double> mapBucketMinMax = new TreeMap();
		TreeMap<Integer, Double> mapBucketMinMaxDiff = new TreeMap();
		TreeMap<Integer, Integer> mapBucketNoDS = new TreeMap();
		TreeMap<String, String> mapADTSValue = new TreeMap();
		// TreeMap<String,String> mapADTSValue = new TreeMap();

		try {
			// Total number of bins
			TreeMap<Integer, Double> mapMinBucketRange = new TreeMap();
			TreeMap<Integer, Double> mapMaxBucketRange = new TreeMap();

			minDS = getMinValueFromTM(mapValue);
			maxDS = getMaxValueFromTM(mapValue);
			System.out.println(" minDS:" + minDS + " maxDS:" + maxDS);
			// min and max bucket range
			double minBRange = 0.0, maxBRange = 0.0;
			noElementEachBin = (int) (maxDS - minDS) / k;
			System.out.println("N: " + N + " k:" + k + " noElementEachBin:"
					+ noElementEachBin);

			// each bin , check the range
			while (count <= k) { // k -> number of bins
				if (count == 1) {
					minBRange = minDS;
				} else {
					minBRange = maxBRange + 1;
				}
				// for last bin, adjust the height
				if (count == k) {
					maxBRange = maxDS;
				} else
					maxBRange = minBRange + noElementEachBin;

				maxBRange = minBRange + noElementEachBin;
				mapMinBucketRange.put(count, minBRange);
				mapMaxBucketRange.put(count, maxBRange);

				count++;
			} // end of while loop to fill the bucket range
				// debug print the bucket range
			/*
			 * for(int i:mapMinBucketRange.keySet()){
			 * System.out.println("bucket: "+ i +" min Range:" +
			 * mapMinBucketRange.get(i) +" max Range:"+mapMaxBucketRange.get(i)
			 * +" elements: " + ( mapMaxBucketRange.get(i)
			 * -mapMinBucketRange.get(i)) );
			 * 
			 * }
			 */
			count = 1;
			// for each bucket , initialize min max bucket value
			while (count <= k) {
				mapBucketNoDS.put(count, 0);
				// mapBucketMinMax.put(count+"min", 0);
				// mapBucketMinMax.put(count+"max", mapMaxBucketRange.get(count)
				// /2);
				count++;
			}
			// value -> bucket
			// value -> bucket value range
			// now find each data points belong to which bucket
			for (int i : mapValue.keySet()) {
				count = 1;
				// check each bucket range
				while (count <= k) {
					// debug print
					// System.out.println("count:"+count+" dp value: "+mapValue.get(i)
					// +" bucket:"+mapMinBucketRange.get(count)
					// +" bucket max:"+ mapMaxBucketRange.get(count));
					if (new Double(mapValue.get(i)) >= mapMinBucketRange
							.get(count)
							&& new Double(mapValue.get(i)) <= mapMaxBucketRange
									.get(count)) {
						mapDP2Bucket.put(i, count); // line no of value, bucket
						// store each bucket, no of DS
						mapBucketNoDS.put(count, mapBucketNoDS.get(count) + 1);

						// System.out.println(" bucket wise min,max:"+
						// mapBucketMinMax.get(count+"min") +" : "+ new
						// Integer(mapValue.get(i)));
						// initialize
						if (!mapBucketMinMax.containsKey(count + "min")) {
							mapBucketMinMax.put(count + "min", 1000000000000.0);
							mapBucketMinMax.put(count + "max", 0.0);
						}

						if (new Double(mapValue.get(i)) > mapBucketMinMax
								.get(count + "max")) {
							mapBucketMinMax.put(count + "max", new Double(
									mapValue.get(i)));
						}
						// if the min value already exists
						if (new Double(mapValue.get(i)) < mapBucketMinMax
								.get(count + "min")) {
							mapBucketMinMax.put(count + "min", new Double(
									mapValue.get(i)));
						}
						break;
					}
					count++;
				}
			}

			count = 1;
			// some bucket may have no values
			/*
			 * while(count <= k){ if(!mapBucketMinMax.containsKey(count+"min"))
			 * mapBucketMinMax.put(count+"min", 10000);
			 * if(!mapBucketMinMax.containsKey(count+"max"))
			 * mapBucketMinMax.put(count+"max", 0); count++; }
			 */

			// debug print DP and bucket
			/*
			 * for(int i:mapDP2Bucket.keySet()){ System.out.println("dp line: "+
			 * i+" dp value:"+mapValue.get(i) +" bucket:"+mapDP2Bucket.get(i));
			 * }
			 */
			count = 1;
			// debug print some bucket may have no values
			/*
			 * while(count <= k){ System.out.println( "bucket:" + count +
			 * " mapBucketNoDS:" + mapBucketNoDS.get(count)); count++; }
			 */
			count = 1;
			int SumOfBucketRange = 0;
			int maxminDiff = 0;
			// print bucket-wise range
			while (count <= k) {
				if (mapBucketNoDS.get(count) > 0) { // non zero bucket
					noNonZeroBuckets++;
					maxminDiff = (int) (mapBucketMinMax.get(count + "max") - mapBucketMinMax
							.get(count + "min"));
					// mapBucketNoDS.get(count);
					if (maxminDiff == 0)
						maxminDiff = 1;

					mapBucketMinMaxDiff.put(count, new Double(maxminDiff));
					SumOfBucketRange = SumOfBucketRange + maxminDiff; // max min
																		// diff
																		// of
																		// current
																		// bucket

					/*
					 * System.out.println("bucket:"+count+" bucket-wise min:"+
					 * mapBucketMinMax.get(count+"min") +
					 * " bucket-wise max:"+mapBucketMinMax.get(count+"max") +
					 * " mapBucketNoDS.get(count):"+mapBucketNoDS.get(count) +
					 * " SumOfBucketRange:" +SumOfBucketRange+
					 * " bucket range (density esti): "+
					 * mapBucketMinMaxDiff.get(count)+ " normalize density:"
					 * +(mapBucketMinMaxDiff.get(count) /
					 * (float)SumOfBucketRange) );
					 */
				}
				count++;
			}
			// only one bucket , so no anomaly
			if (noNonZeroBuckets == 1)
				return mapADTSValue;

			// normalize the bucket-wise min max density estimation
			count = 1;
			float bucketDensityScore = 0.0f;
			float minBucketScore = 400.0f;
			int minBucket = 0;
			while (count <= k) {
				if (mapBucketNoDS.get(count) > 0) {
					// bucketDensityScore = (float)
					// Math.log(1/(mapBucketMinMaxDiff.get(count) /
					// (float)SumOfBucketRange));
					// bucketDensityScore = (mapBucketMinMaxDiff.get(count) /
					// (float)SumOfBucketRange);
					bucketDensityScore = mapBucketNoDS.get(count)
							/ (float) SumOfBucketRange;

					if (bucketDensityScore < minBucketScore) { // min density
						minBucket = count;
						minBucketScore = bucketDensityScore;
					}
					System.out.println("bucket:" + count
							+ " mapBucketMinMaxDiff.get(count):"
							+ mapBucketMinMaxDiff.get(count)
							+ " SumOfBucketRange:" + SumOfBucketRange
							+ " bucketDensityScore:" + bucketDensityScore);

				}

				count++;
			} // while loop end
			count = 1;
			// pull data points from maxBucket
			for (int i : mapDP2Bucket.keySet()) { // line no of value, bucket
				if (mapDP2Bucket.get(i) == minBucket) {
					mapADTSValue.put(mapTS.get(i), mapMetrics.get(i));
				}
				count++;
				// if cross limit break
				if (limitAnomaly == mapADTSValue.size())
					break;
			}
			System.out.println(" minbucket: " + minBucket
					+ " mapADTSValue.size:" + mapADTSValue.size());

			// print the output
			for (String i : mapADTSValue.keySet()) {
				System.out.println("Ano detect TS:" + i + " value:"
						+ mapADTSValue.get(i) + " mapADTSValue.size:"
						+ mapADTSValue.size());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapADTSValue;
	}

	// get the min value from Tree map
	public static double getMinValueFromTM(TreeMap<Integer, String> mapToProcess) {
		double min;
		min = 10000000000000.0;
		try {
			for (int i : mapToProcess.keySet()) {
				if (new Double(mapToProcess.get(i)) < min)
					min = new Double(mapToProcess.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return min;
	}

	// get max value form Tree Map
	public static double getMaxValueFromTM(TreeMap<Integer, String> mapToProcess) {
		double max;
		max = -99.0;
		try {
			for (int i : mapToProcess.keySet()) {
				if (new Double(mapToProcess.get(i)) > max)
					max = new Double(mapToProcess.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return max;
	}

	// parse and write to a file
	public static void writeConfig(String input, String tokenizer,
			String writeFile) {
		try {
			FileWriter writer = new FileWriter(writeFile);
			// StringTokenizer stk = new StringTokenizer(input, tokenizer);
			writer.append(input.trim().replaceAll(" ", ""));
			writer.flush();

			/*
			 * while (stk.hasMoreTokens()) { }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}

	}
   // edit distance (edit_distance)
	public static int editDis(String str1, String str2, boolean isClean_input_strings) {
		// System.out.println("Inside editDis() ");
		
		if(isClean_input_strings){
			str1 = cleanName(str1, "|||").toLowerCase();
			str2 = cleanName(str2, "|||").toLowerCase();
		}
		
		str1 = "*" + str1;
		str2 = "*" + str2;
		int len1 = str1.length(), len2 = str2.length();
		int[][] d = new int[len1][len2];
		for (int i = 0; i < len1; i++) {
			d[i][0] = i;
		}
		for (int j = 0; j < len2; j++) {
			d[0][j] = j;
		}
		for (int i = 1; i < len1; i++) {
			for (int j = 1; j < len2; j++) {
				d[i][j] = Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1);
				int delta = (str1.charAt(i) == str2.charAt(j)) ? 0 : 1;
				d[i][j] = Math.min(d[i][j], d[i - 1][j - 1] + delta);
			}
		}
		return d[len1 - 1][len2 - 1];
	}

	// read a file and get the editDistance for given gid,aid pair
	// input file format-> gid|||aid|||Gname||Aname
	public static void getEditDistance(String inputFile, String outputFile, String delimiter) {
		String line = "";
		String gid = "", aid = "", Gname = "", Aname = "";
		Map<String, String> mapNames = new HashMap();
		String value = "";
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(inputFile));
			FileWriter writer = new FileWriter(outputFile);
			while ((line = reader.readLine()) != null) {
				StringTokenizer stk = new StringTokenizer(line, delimiter);
				count = 1;
				if (line.equals(""))
					continue;
				System.out.println(line + ";token size:" + stk.countTokens());
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();
					if (count == 1) {
						gid = value;
					} else if (count == 2 && !(value.equalsIgnoreCase("nana"))) {
						aid = value;
					} else if (count == 3 && !(value.equalsIgnoreCase("nana"))) {
						Gname = value;
					} else if (count == 4 && !(value.equalsIgnoreCase("nana"))) {
						Aname = getLongest(value, ",");
					}
					count++;
				} // while loop for string tokenization
					// writerDebug.flush();
					// writer.append("key->"+key+";first string:"+mapNames.get("1")+"; 2nd string:"+mapNames.get("2")+"\n");
				writer.append(gid + "|||" + aid + "|||" + Gname + "|||" + Aname
						+ "|||" + editDis(Gname, Aname ,true) + "\n");
				writer.flush();
				mapNames.clear();
			} // while loop for line read of given file
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// clean the name method.
	public static String cleanName(String name, String delimiter) {
		name = name.replace("-", ",").replace("prof.", "").replace("dr.", "")
				.replace(".", " ").replace(" ph.d", "").replace(" phd", "")
				.replace("dr ", "").replace("Dr. ", "").replace(" aka", "")
				.replace("M.Sc", "").replace("B.Sc", "").replace(" ms,", "")
				.replace("(", "").replace(")", "").replace("Associate", "")
				.replace("professor", "").replace("prof.", "")
				.replace("prof ", "").replace("Ph. D", "").replace(" md ", "")
				.replace("ph.d", "").replace(",phd", "").replace(" mba ", "")
				.replace(" md,", "").replace(" mhs", "").replace("@", "")
				.replace("SMIEEE", "").replace(" or", "")
				.replace(" Research", "").replace("(", "").replace(")", "")
				.replace("-", " ").replace(";", ",").replace("\"", " ")
				.replace(",", " ").replace(" ", delimiter)
				.replace(delimiter + delimiter, delimiter)
				.replace(delimiter + delimiter, delimiter);

		String value = "";
		String tname = "";
		int cnt = 1;
		// remove single character
		StringTokenizer stk = new StringTokenizer(name, delimiter);
		while (stk.hasMoreTokens()) {
			value = stk.nextToken();
			if (value.length() > 1 && cnt == 1)
				tname = value;
			else if (value.length() > 1 && cnt > 1)
				tname = tname + delimiter + value;
			cnt++;
		}
		// return the name
		return tname.toLowerCase();
	}

	// Clean special characters etc in the title
	public static String cleanTitle(String name) {
		name = name.replace("-", "").replace(".", "").replace("(", "")
				.replace(")", "").replace(":", "").replace("@", "")
				.replace("(", "").replace(")", "").replace("-", "")
				.replace(";", "").replace("\"", "").replace(",", "")
				.replace(" ", "").replace("_", "").replaceAll("&#8208", "")
				.replace("&#146", "").replaceAll("&#43", "")
				.replace("&#45", "").replaceAll("&#2817", "")
				.replace("&#039", "").replaceAll("&#8211", "")
				.replaceAll("&#8217", "").replaceAll("&#xd", "")
				.replaceAll("&#x2014", "").replace("&#150", "")
				.replace("&#8212", "").replaceAll("&#x2013", "")
				.replace("&#39", "").replace("&#146", "").replace("&#146", "")
				.replace("&#x208", "").replace("+", "").toLowerCase();

		return name;
	}

	// Clean the name method , retain single length subnames
	public String cleanNameRetain(String name, String delimiter) {
		name = name.replace("-", ",").replace(".", " ").replace(" ph.d", "")
				.replace(" phd", "").replace("dr ", "").replace("Dr. ", "")
				.replace(" aka", "").replace("M.Sc", "").replace("B.Sc", "")
				.replace(" ms,", "").replace("(", "").replace(")", "")
				.replace("Associate", "").replace("professor", "")
				.replace("prof.", "").replace("prof ", "").replace("Ph. D", "")
				.replace(" md ", "").replace("ph.d", "").replace(",phd", "")
				.replace(" mba ", "").replace(" md,", "").replace(" mhs", "")
				.replace("@", "").replace("SMIEEE", "").replace(" or", "")
				.replace(" Research", "").replace("(", "").replace(")", "")
				.replace("-", " ").replace(";", ",").replace("\"", " ")
				.replace(",", " ").replace(" ", delimiter)
				.replace(delimiter + delimiter, delimiter)
				.replace(delimiter + delimiter, delimiter);

		String value = "";
		String tname = "";
		int cnt = 1;
		// remove single character
		StringTokenizer stk = new StringTokenizer(name, delimiter);
		while (stk.hasMoreTokens()) {
			value = stk.nextToken();
			if (cnt == 1)
				tname = value;
			else if (cnt > 1)
				tname = tname + delimiter + value;
			cnt++;
		}
		// return the name
		return tname.toLowerCase();
	}

	// method to get the longest length from givne CSV
	// Michael Chau,Michael Chiu-Lung Chau -> Michael Chiu-Lung Chau
	public static String getLongest(String str, String delimiter) {
		str = str.replace(".", "").replace("-", "");
		System.out.println(str);
		StringTokenizer stk = new StringTokenizer(str, delimiter);
		String value = "";
		int pre_len = 0;
		String longest_str = "";
		// System.out.println( "  before while loop->"+ str);
		while (stk.hasMoreTokens()) {
			value = stk.nextToken();
			// System.out.println( " next token "+ value);
			if (value.length() >= pre_len) {
				longest_str = value;
			}
			// System.out.println( " prelength");
			pre_len = value.length();
		}
		// System.out.println( " return from getLongest->" +longest_str+"\n");
		return longest_str;
	}

	// read a file and get the longest 2 subNames from given name and write to
	// an output file
	public static void getLongest2Names(String inputFile, String outputFile,
			String delimiter) {
		String line = "";
		String key = "";
		Map<String, String> mapNames = new HashMap();
		String value = "";
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(inputFile));
			FileWriter writer = new FileWriter(outputFile);
			while ((line = reader.readLine()) != null) {
				StringTokenizer stk = new StringTokenizer(line, delimiter);
				count = 1;
				if (line.equals(""))
					continue;
				System.out.println(line + ";token size:" + stk.countTokens());
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();
					if (count == 1) {
						key = value;
					} else if (count == 2 && !(value.equalsIgnoreCase("nana"))) {
						mapNames = get2Longest(value, " ");
					}
					count++;
				} // while loop for string tokenization
					// writerDebug.flush();
					// writer.append("key->"+key+";first string:"+mapNames.get("1")+"; 2nd string:"+mapNames.get("2")+"\n");
				writer.append(key + "|||" + mapNames.get("1") + "|||"
						+ mapNames.get("2") + "\n");
				writer.flush();
				mapNames.clear();
			} // while loop for line read of given file
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// For Aminer: For given name, get the longest 2 names from it
	public static Map<String, String> get2Longest(String str, String delimiter) {
		str = str.replace(".", " ").replace("-", " ").replace("\"", " ")
				.replace(",", " ").replace("  ", " ").toLowerCase();
		System.out.println(str);
		StringTokenizer stk = new StringTokenizer(str, delimiter);
		String value = "";
		Map<String, String> names = new HashMap();
		Map<String, String> LongestNames = new HashMap();
		int pre_len = 0;
		String longest_str = "";
		// System.out.println( "  before while loop->"+ str);
		while (stk.hasMoreTokens()) {
			value = stk.nextToken();
			// System.out.println( " next token "+ value);
			if (!names.containsKey(value) && value.length() > 1)
				names.put(value, "");
		}
		// get the first longest length
		for (String name : names.keySet()) {
			if (name.length() >= pre_len && name.length() > 1) {
				longest_str = name;
			}
			pre_len = value.length();
		}
		// System.out.println( " size:"+ names.size() +"\n");
		// remove the first longest length and search for 2nd longest space
		LongestNames.put("1", longest_str);
		names.remove(longest_str);
		System.out.println("First getLongest->" + longest_str + "; size:"
				+ names.size() + "\n");
		pre_len = 0;
		longest_str = "";
		// get the second longest length
		for (String name : names.keySet()) {
			// System.out.println( "\n proces->" + pre_len + ";"+ name.length()
			// +";" +longest_str);
			if (name.length() >= pre_len && name.length() > 1) {
				longest_str = name;
			}
			pre_len = value.length();
		}
		if (longest_str.length() > 0)
			LongestNames.put("2", longest_str);
		// System.out.println( " prelength");
		System.out.println("Second getLongest->" + longest_str + "\n");
		return LongestNames;
	}

	// find the occurence of given substring for a given string
	public static int count(final String string, final String substring) {
		int count = 0;
		int idx = 0;
		int offset = 0;

		while ((idx = string.indexOf(substring, idx + offset)) != -1) {
			idx++;
			count++;
			offset = 2;
		}

		return count;
	}

	// get noise words from given file
	public static Map getNoiseWords(String NoiseWordsFile) {
		Map<String, String> NoiseWordMap = new HashMap();
		try {
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(NoiseWordsFile)));
			// read the file and put into map
			while ((line = reader.readLine()) != null) {
				NoiseWordMap.put(line, "");
			}
			System.out.println(" noise word size->" + NoiseWordMap.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NoiseWordMap;
	}

	// Clean the conference
	public static String CleanConference(Map<String, String> NoiseWords,
			String conferenceFileName, String OutputFile, FileWriter writerDebug) {

		String value = "";
		String conference_total = "", line = "";
		String aid = "", conf = "";
		try {
			// writerDebug.append("given conference:" + "" +";Noise Word size->"
			// +NoiseWords.size()+"\n");
			BufferedReader reader = new BufferedReader(new FileReader(
					conferenceFileName));
			FileWriter writer = new FileWriter(OutputFile);
			int count = 0;

			while ((line = reader.readLine()) != null) {
				line = line.toLowerCase().replace(";", " ").replace(".", " ")
						.replace(":", " ").replace(",", " ");
				count = 1;
				StringTokenizer stk1 = new StringTokenizer(line, "|||");
				while (stk1.hasMoreTokens()) { // token has gid and conf string
					value = stk1.nextToken();
					if (count == 1)
						aid = value;
					if (count == 2)
						conf = value;
					count++;
				} // while end of tokenization
				conference_total = "";
				StringTokenizer stk = new StringTokenizer(conf, " ");
				// for each of the noise words
				while (stk.hasMoreTokens()) { // for each word of given
												// conference
					value = stk.nextToken();
					if (NoiseWords.containsKey(value)) {
					} else
						conference_total = conference_total + " " + value;
				}// while end of tokenization
				writer.append(aid + "|||" + conference_total + "\n");
			} // outer while loop end
				// writerDebug.append("return conference:"+conference_total
				// +"\n");
			writer.flush();
			// writerDebug.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conference_total;
	}

	// method to read from file ( it uses thread) and returns as Map
	public static Map readFromFile(final String f, Thread thread,
			final Map nameValueMap) {
		Runnable readRun = new Runnable() {
			public void run() {
				FileInputStream in = null;
				String text = null;
				try {
					Thread.sleep(0);
					File inputFile = new File(f);
					in = new FileInputStream(inputFile);
					byte bt[] = new byte[(int) inputFile.length()];
					in.read(bt);
					text = new String(bt);
					System.out.println(text);
					String line = "";
					String id = "";
					String names = "";
					BufferedReader reader = new BufferedReader(
							new FileReader(f));
					while ((line = reader.readLine()) != null) {
						id = line.substring(0, line.indexOf("|||") + 1)
								.replace("|", "");
						line = line.replace(id, "");
						names = line.substring(0, line.length())
								.replace(";", ",").replace("|", "");
						System.out.println("name->" + id + ";value->" + names);
						nameValueMap.put(id, names.toLowerCase());
					}
				} catch (Exception ex) {
				}
			}
		};
		thread = new Thread(readRun);
		thread.start();
		return nameValueMap;
	}

	// method to find strings with special characters from given line ( : ? " "
	// - )
	public static String getSpecialWords(Map<String, String> Title) {
		String value = "", SpecialWords = "";
		Map<String, String> specialWordMap = new HashMap();
		try {
			for (String line : Title.keySet()) {
				line = line.replace("  ", " ").replaceAll("\\p{Cntrl}", "");
				StringTokenizer stk = new StringTokenizer(line, " ");
				while (stk.hasMoreTokens()) { // for each word of given
												// conference
					value = stk.nextToken();
					if (value.indexOf("?") > 0 || value.indexOf("?") > 0
							|| value.indexOf(":") > 0
							|| value.indexOf("\"") > 0
							|| value.indexOf("-") > 0 || value.indexOf(",") > 0
							|| value.indexOf("+") > 0 || value.indexOf("/") > 0
							|| value.indexOf(",") > 0) {
						if (SpecialWords.length() == 0)
							SpecialWords = value;
						else
							SpecialWords = SpecialWords + "|||" + value;

					}
				}// while loop inner
			}// while loop outer
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SpecialWords;
	}

	// method to find strings with ING in the ending from given line (Example:
	// linking, processing)
	public static String getINGwords(Map<String, String> Title) {
		String value = "";
		Map<String, String> specialWordMap = new HashMap();
		String SpecialWords = "";
		try {
			for (String line : Title.keySet()) {
				line = line.replace("  ", " ").replaceAll("\\p{Cntrl}", "");
				StringTokenizer stk = new StringTokenizer(line, " ");
				while (stk.hasMoreTokens()) { // for each word of given
												// conference
					value = stk.nextToken().toLowerCase();
					if (value.indexOf("ing") > 0
							&& !value.equalsIgnoreCase("using")
							&& !value.equalsIgnoreCase("doing")
							&& !value.equalsIgnoreCase("single")
							&& !value.equalsIgnoreCase("multiple")) {

						if (SpecialWords.length() == 0)
							SpecialWords = value;
						else
							SpecialWords = SpecialWords + "|||" + value;
					}
				}// while loop inner
			}// for loop outer
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SpecialWords.replace(".", "").replace(":", "").replace(",", "");
	}

	// method to find non-dictionary words - Personalized Words from given title
	public static String getPersonalizedWords(Map<String, String> Title,
			Map<String, String> DictionaryMap) {
		String value = "";
		Map<String, String> specialWordMap = new HashMap();
		String SpecialWords = "";
		Pattern pattern = Pattern.compile("[0-9]");
		Matcher matcher;

		try {
			for (String line : Title.keySet()) {
				line = line.replace("?", " ").replace("-", " ")
						.replace(":", " ").replace(",", " ").replace("-", " ")
						.replace("+", "").replace("/", "").replace(",", "")
						.replace("!", "").replace(".", "");
				line = line.replace("  ", " ").replaceAll("\\p{Cntrl}", "");
				StringTokenizer stk = new StringTokenizer(line, " ");

				while (stk.hasMoreTokens()) { // for each word of given
												// conference
					value = stk.nextToken().toLowerCase();
					matcher = pattern.matcher(value);

					if (!DictionaryMap.containsKey(value)
							&& !matcher.find()
							&& // not contains number
							value.indexOf("the") == -1
							&& value.indexOf("and") == -1
							&& value.indexOf("tha") == -1
							&& value.indexOf("ent") == -1
							&& value.indexOf("ing") == -1
							&& value.indexOf("ion") == -1
							&& value.indexOf("tio") == -1
							&& value.indexOf("for") == -1
							&& value.indexOf("nde") == -1
							&& value.indexOf("has") == -1
							&& value.indexOf("nce") == -1
							&& value.indexOf("edt") == -1
							&& value.indexOf("tis") == -1
							&& value.indexOf("oft") == -1
							&& value.indexOf("sth") == -1
							&& value.indexOf("men") == -1) {

						if (SpecialWords.length() == 0)
							SpecialWords = value;
						else
							SpecialWords = SpecialWords + "|||" + value;
					}
				}// while loop inner
			}// for loop outer
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SpecialWords;
	}

	// method to find strings with numbers characters from given line ( 2d or 3d
	// or three or two )
	public static String getNumbers(Map<String, String> Title) {
		String value = "";
		Pattern pattern = Pattern.compile("[0-9]");
		Matcher matcher;
		String SpecialWords = "";
		Map<String, String> specialWordMap = new HashMap();
		try {
			for (String line : Title.keySet()) {
				line = line.replace("  ", " ").replaceAll("\\p{Cntrl}", "");
				StringTokenizer stk = new StringTokenizer(line, " ");
				while (stk.hasMoreTokens()) { // for each word of given
												// conference
					value = stk.nextToken();
					matcher = pattern.matcher(value);
					if ((matcher.find() || value.equalsIgnoreCase("two") || value
							.equalsIgnoreCase("three")
							&& (value.length() > 0 && value
									.equalsIgnoreCase(" ")))) {
						if (SpecialWords.length() == 0)
							SpecialWords = value;
						else
							SpecialWords = SpecialWords + "|||" + value;
					}
				}// while loop inner
			}// while loop outer
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SpecialWords.replace(" ", "");
	}

	// Load Nth token from given file list and load to a map (and return).
	// Additionally write to output file.
	public static TreeMap<String, String> readNthTokenFromWithORWithoutGivenPatternForGivenFileandRemoveDuplicationWrite2File(
			String inputFileList, int NofInterestedToken, String delimiter,
			String Pattern1, String Pattern2, String replace1, String replace2,
			String outputFile, String outputDebug) {
		TreeMap<String, String> mapOut = new TreeMap();
		String line = "";
		String key = "";
		String value = "";
		int count = 0;
		int lineNo = 0;
		try {
			FileWriter writer = new FileWriter(outputFile);
			FileWriter writerDebug = new FileWriter(outputDebug);
			String[] s = inputFileList.split(delimiter);
			int fileL = s.length;
			int fileIndex = 0;
			String currFile = "";
			// each file
			while (fileIndex < s.length) {
				currFile = s[fileIndex];
				BufferedReader reader = new BufferedReader(new FileReader(
						currFile));
				// read current file
				while ((line = reader.readLine()) != null) {
					lineNo++;
					line = line.replaceAll("\\p{Cntrl}", "");
					StringTokenizer stk = new StringTokenizer(line, delimiter);
					count = 1;
					while (stk.hasMoreTokens()) {
						value = stk.nextToken();
						System.out.println("lineno:" + lineNo + " cnt:" + count
								+ " - " + NofInterestedToken);
						if (count == NofInterestedToken) {
							// pattern
							if (Pattern1.length() > 0 || Pattern2.length() > 0) {
								System.out.println("if..");
								//
								if (Pattern1.length() > 0
										&& Pattern2.length() > 0) {
									if (value.indexOf(Pattern1) >= 0
											&& value.indexOf(Pattern2) >= 0) {
										value = value.replaceAll(replace1, "")
												.replaceAll(replace2, "");
										// remove duplicate
										if (!mapOut.containsValue(value)) {
											writer.append(line + "\n");
											mapOut.put(
													Integer.toString(lineNo),
													value);
										} else if (mapOut.containsValue(value)) {
											writerDebug.append("duplicate:"
													+ line + "\n");
											writerDebug.flush();
										}
									}
								} else if (Pattern1.length() > 0) {
									if (value.indexOf(Pattern1) >= 0) {
										value = value.replaceAll(replace1, "")
												.replaceAll(replace2, "");
										// remove duplicate
										if (!mapOut.containsValue(value)) {
											writer.append(line + "\n");
											mapOut.put(
													Integer.toString(lineNo),
													value);
										} else if (mapOut.containsValue(value)) {
											writerDebug.append("duplicate:"
													+ line + "\n");
											writerDebug.flush();
										}
									}
								} else if (Pattern2.length() > 0) {
									if (value.indexOf(Pattern2) >= 0) {
										value = value.replaceAll(replace1, "")
												.replaceAll(replace2, "");
										// remove duplicate
										if (!mapOut.containsValue(value)) {
											writer.append(line + "\n");
											mapOut.put(
													Integer.toString(lineNo),
													value);
										} else if (mapOut.containsValue(value)) {
											writerDebug.append("duplicate:"
													+ line + "\n");
											writerDebug.flush();
										}
									}
								}
							} else {
								System.out.println("else..");
								// writerDebug.append(" hp map insertion "+
								// key+";"+value+"\n");
								value = value.replaceAll(replace1, "")
										.replaceAll(replace2, "");
								// remove duplicate
								if (!mapOut.containsValue(value)) {
									writer.append(line + "\n");
									mapOut.put(key, value);
								} else if (mapOut.containsValue(value)) {
									writerDebug.append("duplicate:" + line
											+ "\n");
									writerDebug.flush();
								}
							}
							System.out.println("flushing.. ");
							writer.flush();
							writerDebug.flush();
						}
						count++;
					} // while loop for string tokenization
				} // while loop for line read of given file
				fileIndex++;
				// write to output File
				// for(String k:mapOut.keySet()){
				// writer.append( mapOut.get(key) +"\n");
				// writer.flush();
				// }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapOut;
	}

	// Load Nth token from given file list and load to a map (and return)
	public static TreeMap<String, String> LoadNthTokenFromWithORWithoutGivenPatternFromGivenFileListandLoadToMap(
										String 	inputFileList, 
										int 	NofInterestedToken, 
										String 	delimiter,
										String 	Pattern1, 
										String 	Pattern2, 
										String  replace1, 
										String  replace2,
										boolean isSOPprint
										) {
		TreeMap<String, String> mapOut = new TreeMap();
		String line = "";
		String key = "";
		String value = "";
		int count = 0;
		int lineNo = 0;
		try {
			System.out.println("LoadNthTokenFromWithORWithoutGivenPatternFromGivenFileListandLoadToMap:");
			System.out.println("---------" + inputFileList + "------");
			String[] s = inputFileList.split(delimiter);
			int fileL = s.length;
			int fileIndex = 0;
			String currFile = "";
			//
			while (fileIndex < s.length) {
				currFile = s[fileIndex];
				System.out.println("\n currFile:" + currFile);
				BufferedReader reader = new BufferedReader(new FileReader(currFile));
				//
				while ((line = reader.readLine()) != null) {
					lineNo++;
					System.out.println("loading line:" + line);
					line = line.replaceAll("\\p{Cntrl}", "");
					StringTokenizer stk = new StringTokenizer(line, delimiter);
					count = 1;
					while (stk.hasMoreTokens()) {
						value = stk.nextToken();
						if(isSOPprint)
							System.out.println("count:" + count+ " NofInterestedToken:" + NofInterestedToken);
						if (count == NofInterestedToken) {
							// pattern
							if (Pattern1.length() > 0 || Pattern2.length() > 0) {
								if(isSOPprint)
									System.out.println("pattern given Pattern1 || Pattern2");
								//
								if (Pattern1.length() > 0
										&& Pattern2.length() > 0) {
									if(isSOPprint)
										System.out.println("out.pattern found.Pattern1:"
														+ Pattern1
														+ ";Pattern2:"
														+ Pattern2);
									if (value.indexOf(Pattern1) >= 0
											|| value.indexOf(Pattern2) >= 0) {
										value = value.replaceAll(replace1, "")
												.replaceAll(replace2, "");
										if(isSOPprint)
										System.out.println("pattern found.Pattern1:"
														+ Pattern1
														+ ";Pattern2:"
														+ Pattern2);
										mapOut.put(Integer.toString(lineNo),
												value);
									}
								} else if (Pattern1.length() > 0) {
									if(isSOPprint)
									System.out.println("out.pattern found.Pattern1:"
													+ Pattern1);
									if (value.indexOf(Pattern1) >= 0) {
										value = value.replaceAll(replace1, "")
												.replaceAll(replace2, "");
										if(isSOPprint)
										System.out.println("pattern found.Pattern1:"
														+ Pattern1);
										mapOut.put(Integer.toString(lineNo),
												value);
									}
								} else if (Pattern2.length() > 0) {
									if(isSOPprint)
										System.out.println("out.pattern found.Pattern2:"+ Pattern2);
									if (value.indexOf(Pattern2) >= 0) {
										value = value.replaceAll(replace1, "")
												.replaceAll(replace2, "");
										if(isSOPprint)
												System.out
														.println("pattern found.Pattern2:"
																+ Pattern2);
										mapOut.put(Integer.toString(lineNo),
												value);
									}
								}
							} else {
								// writerDebug.append(" hp map insertion "+
								// key+";"+value+"\n");
								if(isSOPprint)
									System.out.println("NOT match pattern found");
								value = value.replaceAll(replace1, "")
										.replaceAll(replace2, "");
								mapOut.put(key, value);
							}
						}
						count++;
					} // while loop for string tokenization
				} // while loop for line read of given file
				fileIndex++;
			}
			System.out.println("loaded inputfile:" + inputFileList + " size:"
					+ mapOut.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapOut;
	}

	// load multiple values to maps
	public static void LoadMultipleValueToMap4(final Map mapName,
			final Map mapHomepage, final Map mapEmail, final Map mapPosition,
			String filename, String delimiter, FileWriter writerDebug) {
		String line = "";
		String key = "";
		String value = "";
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null) {
				line = line.replaceAll("\\p{Cntrl}", "");
				StringTokenizer stk = new StringTokenizer(line, delimiter);
				count = 1;
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();
					if (count == 1) {
						key = value;
					} else if (count == 2 && !(value.equalsIgnoreCase("na"))) {
						// writerDebug.append(" hp map insertion "+
						// key+";"+value+"\n");
						mapName.put(key, value);
					} else if (count == 3 && !(value.equalsIgnoreCase("na"))) {
						// writerDebug.append(" hp map insertion "+
						// key+";"+value+"\n");
						mapHomepage.put(key, value);
					} else if (count == 4 && !(value.equalsIgnoreCase("na"))) {
						// writerDebug.append(" email map insertion "+
						// key+";"+value+"\n");
						mapEmail.put(key, value);
					} else if (count == 5 && !(value.equalsIgnoreCase("na"))) {
						// writerDebug.append("  Postion map insertion "+
						// key+";"+value+"\n");
						mapPosition.put(key, value);
					}
					count++;
				} // while loop for string tokenization
				writerDebug.flush();
			} // while loop for line read of given file
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// method to find score two given strings delimited with " " -- score based on common words between two vectors...
	public static final Map<String, String> plainScore(String word1, String word2, String delimiter, FileWriter writerDebug) {
		word1 = word1.replace(delimiter, " ").toLowerCase();
		word2 = word2.replace(delimiter, " ").toLowerCase();
		delimiter = " ";
		word1.replace(delimiter + delimiter, delimiter);
		String[] awords = word1.split(delimiter);
		String common_words = "";
		String key = "";
		Map<String, String> scoreMap = new HashMap();
		int min_w_length = 0;
		int value = 0;
		int sum_freq = 0;
		int t_common_words = 0;
		Map gwordsmap = new TreeMap();
		Integer frequency = 0;
		Map awordsmap = new TreeMap();
		try {
			// writerDebug.append("\nInside plainScore:\nword1-->"+word1+"\nword2-->"+word2+"\narrayWord->"+awords.length);
			// For each word
			for (int i2 = 0, n = awords.length; i2 < n; i2++) {
				if (awords[i2].length() > 0) {
					frequency = (Integer) awordsmap.get(awords[i2]); // get key
																		// from
																		// value
					if (frequency == null) {
						frequency = 1;
					} else {
						value = frequency.intValue();
						frequency = new Integer(value + 1);
					}
				}
				awordsmap.put(awords[i2], frequency);
			}
			String gwords[] = word2.split(delimiter);
			System.out.println("g length ->" + gwords.length);
			// For each word
			for (int i2 = 0, n = gwords.length; i2 < n; i2++) {
				if (gwords[i2].length() > 0) {
					frequency = (Integer) gwordsmap.get(gwords[i2]); // get key
																		// from
																		// value
					if (frequency == null) {
						frequency = 1;
					} else {
						value = frequency.intValue();
						frequency = new Integer(value + 1);
					}
				}
				gwordsmap.put(gwords[i2], frequency);
			}
			Set set2 = awordsmap.entrySet();
			// Get an iterator
			Iterator it = set2.iterator();
			gwordsmap.remove(" ");
			awordsmap.remove(" ");
			// check for word matching
			while (it.hasNext()) {
				Map.Entry me = (Map.Entry) it.next();
				key = (String) me.getKey().toString().replace(" ", "");
				value = (Integer) me.getValue();
				// writerDebug.append("\n  key value ->"+ key+"\n"+value);
				// System.out.println( " aword  ->"+ key + "; freq->" +value);
				if (gwordsmap.containsKey(key) && !(key.equalsIgnoreCase(" "))
						&& key.length() > 0) {
					t_common_words = t_common_words + 1;
					sum_freq = sum_freq + value + (Integer) gwordsmap.get(key);
					// writerDebug.append("\n bfr common word->"+common_words);
					common_words = common_words + delimiter + key;
					// writerDebug.append("\n afr common word->"+common_words);
				}
			}
			common_words = common_words.replace(" ", "");
			// find min word length
			if (gwordsmap.size() <= awordsmap.size())
				min_w_length = gwordsmap.size();
			else
				min_w_length = awordsmap.size();

			// writerDebug.append("\nsum_freq ->" + sum_freq / 2 +
			// "; min length->"
			// + (min_w_length ) + ";common words->" +
			// t_common_words+";score got->"+String.valueOf( t_common_words /
			// (float) (min_w_length )));

			scoreMap.put("1",
					String.valueOf(t_common_words / (float) (min_w_length)));
			scoreMap.put("2", common_words);
			// writerDebug.append("\n returned from map->"+scoreMap.get("1")+";"+scoreMap.get("2"));

			// writerDebug.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return scoreMap;
	}
	
	

	// load multiple values to maps - 3 values for conference
	// key$$$correct_abbreviation$$$correct_conference name
	// map1 contains key -> conference name ; map2 contains correct_abbr ->
	// conference name
	public static void LoadMultipleValueToMap3(
			final Map<String, String> keyFNameConfMap,
			final Map<String, String> cAbbrFNameConfMap, String filename,
			String delimiter, FileWriter writerDebug) {
		String line = "";
		String key = "", correctAbbr = "";
		String value = "";
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null) {
				StringTokenizer stk = new StringTokenizer(line, delimiter);
				count = 1;
				System.out.println("token size:" + stk.countTokens());
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();
					if (count == 1) {
						key = value;
					} else if (count == 2 && !(value.equalsIgnoreCase("nana"))) {
						// writerDebug.append(" hp map insertion "+
						// key+";"+value+"\n");
						correctAbbr = value;
					} else if (count == 3 && !(value.equalsIgnoreCase("nana"))) {
						System.out.println("\n loading ->" + key + ";" + value
								+ ";" + correctAbbr);
						keyFNameConfMap.put(key, value); // key and full
															// conference name
						cAbbrFNameConfMap.put(correctAbbr, value);// correct
																	// abbreviation
																	// and full
																	// conference
																	// name
					}// if ends
					count++;
				} // while loop for string tokenization
					// writerDebug.flush();
			} // while loop for line read of given file
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// method will take <key|||value1|||value2> output-> map<key,value1> ,
	// map<key,value2)
	public static TreeMap<Integer, TreeMap<String, String>> LoadMultipleValueTo_Key_Value_Map3(
			String filename, String delimiter, String DebugFile) {
		String line = "";
		String key = "", url = "";
		String value = "";
		int count = 0;
		TreeMap<String, String> urlMap = new TreeMap();
		;
		TreeMap<String, String> truthMap = new TreeMap();
		TreeMap<Integer, TreeMap<String, String>> mapOut = new TreeMap();

		try {
			FileWriter writerDebug = new FileWriter(DebugFile);
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while ((line = reader.readLine()) != null) {
				// writerDebug.append("new line , delimiter->"+line +"<->"+
				// delimiter);
				// writerDebug.flush();
				StringTokenizer stk = new StringTokenizer(line, delimiter);
				count = 1;
				// System.out.println("token size:" + stk.countTokens());
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();
					if (count == 1) {
						key = value.replace(" ", "");
					} else if (count == 2 && !(value.equalsIgnoreCase("nana"))) {
						// writerDebug.append(" hp map insertion "+
						// key+";"+value+"\n");
						url = value;
					} else if (count == 3 && !(value.equalsIgnoreCase("nana"))) {
						writerDebug.append("key:" + key + "<->value:" + value
								+ "<->url:" + url + "\n\n");
						truthMap.put(key, value); // truth map (third value
						urlMap.put(key, url); // url map (second value
						writerDebug.flush();
					}// if ends
					count++;
				} // while loop for string tokenization
			} // while loop for line read of given file

			mapOut.put(1, urlMap);
			mapOut.put(2, truthMap);
			// writerDebug.append(urlMap.toString());
			writerDebug.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapOut;
	}

	// clean title from input file and write to output file , input format
	// <gid|||Title>
	public static void cleanTitle(String inputFile, String outputFile,
			String delimiter) {
		String line = "";
		String key = "", title = "";
		String value = "";
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(inputFile));
			FileWriter writer = new FileWriter(outputFile);
			while ((line = reader.readLine()) != null) {
				StringTokenizer stk = new StringTokenizer(line, delimiter);
				count = 1;
				if (line.equals(""))
					continue;
				System.out.println(line + ";token size:" + stk.countTokens());
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();
					if (count == 1) {
						key = value;
					} else if (count == 2 && !(value.equalsIgnoreCase("nana"))) {
						title = cleanTitle(value);
					}
					count++;
				} // while loop for string tokenization
					// writerDebug.flush();
				writer.append(key + "|||" + title + "\n");
				writer.flush();
			} // while loop for line read of given file
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// getTxtFormatToJSon
	public static void getTxtFormatToJSon(String metricName,
			String destFilePath, String destFile, String delimiter,
			String outputFile) {

		String line = "";
		int lineNo = 0;
		String value = "";
		int counter = 0;
		HashMap<Integer, String> mapCSVEntities = new HashMap();
		HashMap<String, String> mapCheckUniqueServer = new HashMap();
		double currValue = 0.0D;
		String currEntity = "";
		String curr = "";
		String consolidate = "";
		int currCluster = 0;
		try {

			BufferedReader reader = new BufferedReader(new FileReader(
					destFilePath + destFile));
			FileWriter writer = new FileWriter(destFilePath + outputFile);
			// bufferedreader reader = new bufferedreader(new filereader((new
			// StringBuilder(string.valueOf(destFilePath))).append(destFile).toString()));
			// filewriter writer = new filewriter((new
			// StringBuilder(string.valueOf(destFilePath))).append(outputFile).toString());
			// system.out.println((double)"1".indexOf(".") + math.ceil((new
			// double("2.7")).doubleValue())); while((line = reader.readLine())
			// != null) { if(lineNo > 75) break;

			while ((line = reader.readLine()) != null) {
				// if(lineNo > 75) break;
				StringTokenizer stk = new StringTokenizer(line, delimiter);
				while (stk.hasMoreTokens()) {
					value = stk.nextToken();
					counter++;
					if (counter == 1)
						currValue = Math.round(Double.valueOf(value));
					else if (counter == 2)
						currCluster = new Integer(value);
					else if (counter == 3) {
						counter = 0;
						curr = "{\"name\":\"" + value + "\"" + ",\"size\":"
								+ currValue + "}";

					}

					// System.out.println(" counter:"+counter +
					// " currC:"+currCluster);

					if (!mapCheckUniqueServer.containsKey(value)
							&& counter == 0) { // counter==0 means one line
												// complete

						if (mapCSVEntities.containsKey(Integer
								.valueOf(currCluster))) {
							mapCSVEntities.put(Integer.valueOf(currCluster),
									mapCSVEntities.get(currCluster) + ","
											+ curr);
						} else {
							mapCSVEntities.put(Integer.valueOf(currCluster),
									curr);
						}
						// add the unique server so next time not added to json
						// output
						mapCheckUniqueServer.put(value, "sample");
					} else {
						continue;
					}

				} // end of inner while
				lineNo++;
			} // end of outer while

			curr = "";
			counter = 0;
			System.out.println(" final mapCSVEntities.size:"
					+ mapCSVEntities.size());
			for (int f : mapCSVEntities.keySet()) {
				System.out.println(counter + " : " + curr);
				if (counter == 0)
					curr = curr + "{\"name\": " + "\"" + f + "\","
							+ "\"children\": [" + "{\"name\":" + "\"" + f
							+ "\"," + "\"children\": [" + mapCSVEntities.get(f)
							+ "]}]}";
				else
					curr = curr + "," + "{\"name\": " + "\"" + "\","
							+ "\"children\": [" + "{\"name\":" + "\"" + f
							+ "\"," + "\"children\": [" + mapCSVEntities.get(f)
							+ "]}]}";
				counter++;
			} // end of for

			curr = "{\"name\":\"" + metricName + "\",\"children\":[" + curr
					+ "]}";
			curr = curr.replace("[,{", "[{");
			System.out.println(" final curr:" + curr);
			writer.append(curr);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// getRclustersInJson
	public static boolean getRclustersInJson(String metricName,
			String startDate, String endDate, String destFilePath,
			String destFile, String delimiter, String outputFile) {
		String temp = "";
		try {
			temp = "Rscript /home/mookiah/R/productionMySQLData.R "
					+ metricName + " " + startDate + " " + endDate + " "
					+ destFilePath + destFile;
			System.out.println(temp);
			Process p = Runtime.getRuntime().exec(temp);
			// if(p.exitValue() == 0) {

			boolean f = new File(destFilePath + destFile).exists();
			// already existing file delete
			if (f == true) {
				File fi = new File(destFilePath + destFile);
				File f2 = new File(destFilePath + outputFile);
				fi.delete();
				f2.delete();
			}

			System.out.println(" first check: " + f + " file:" + destFilePath
					+ destFile);
			int counter = 0;
			// sleep for sometime
			while (counter < 200) {
				f = new File(destFilePath + destFile).exists();
				System.out.println("counter:" + counter + " " + f + " file:"
						+ destFilePath + destFile);
				if (f == false) {
					Thread.sleep(1000);
				} else if (f == true) {
					break;
				}
				counter++;
			}
			getTxtFormatToJSon(metricName, destFilePath, destFile, delimiter,
					outputFile);

			// }
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	// get for given date formate
	public static String getCurrDateFormat(String dateFormat) {
		String out = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date date = new Date();
			out = new String(sdf.format(date));
			// System.out.println(sdf.format(date));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return out;
	}

	// convert Raw HIT data to unique value one
	public static void cleanHitsData(String inputFile, String inputHeaderFile,
								     String outputFile, String dictFile, String delimiter) {

		try {
			BufferedReader reader1 = new BufferedReader(new FileReader(
					inputHeaderFile));
			BufferedReader reader2 = new BufferedReader(new FileReader(
					inputFile));
			FileWriter writer = new FileWriter(outputFile);
			FileWriter writer2 = new FileWriter(dictFile);
			// bufferedreader reader = new bufferedreader(new filereader((new
			// StringBuilder(string.valueOf(destFilePath))).append(destFile).toString()));
			// filewriter writer = new filewriter((new
			// StringBuilder(string.valueOf(destFilePath))).append(outputFile).toString());
			// system.out.println((double)"1".indexOf(".") + math.ceil((new
			// double("2.7")).doubleValue())); while((line = reader.readLine())
			// != null) { if(lineNo > 75) break;
			String line = "";
			int lineNo = 0;
			HashMap<Integer, String> mapHeader = new HashMap();
			HashMap<Integer, Boolean> mapBoolean = new HashMap();
			String[] stk2 = null;
			// read the header
			while ((line = reader1.readLine()) != null) {
				lineNo++;
				// System.out.println("reader1:"+line);
				mapHeader.put(lineNo, line);
			}
			lineNo = 0;
			String currToken = "";
			int TokenNumber = 0;
			StringTokenizer stk;
			int i = 0;
			// first read few lines to get if its numeric value or not
			while ((line = reader2.readLine()) != null) {
				lineNo++;
				TokenNumber = 0; // reset
				// stk = new StringTokenizer(line, delimiter );
				stk2 = line.split("#");
				System.out.println("reader2:line:" + line + ";Token:"
						+ stk2.length);
				// string token
				while (i < stk2.length) {
					// if(i>282 ) break;
					currToken = stk2[i];
					TokenNumber++;
					// System.out.println(TokenNumber +" : "+ currToken +" : "+
					// isNumeric(currToken));
					//
					mapBoolean.put(TokenNumber, isNumeric(currToken));
					i++;
				}
				if (lineNo > 4)
					break;
			}
			i = 0;
			// print boolean
			/*
			 * while(i <= stk2.length){ System.out.println("Boolean2:"+i
			 * +" : "+mapBoolean.get(i)); }
			 */

			// <tokenNumber , <uniqueValue, id> >
			HashMap<Integer, HashMap<String, Integer>> headerUniqueList = new HashMap();
			HashMap<String, Integer> UniqueList = new HashMap();
			String concLine = "";
			int temp = 0;
			reader2 = new BufferedReader(new FileReader(inputFile));

			// UniqueList.put("dummy", 1);
			// headerUniqueList.put(999, UniqueList);
			Integer tmp = null;
			// read
			while ((line = reader2.readLine()) != null) {
				lineNo++;
				if (lineNo > 500000)
					break;
				TokenNumber = 0; // reset
				// stk = new StringTokenizer(line, delimiter );
				stk2 = line.split("#");
				System.out.println("lineNo:" + lineNo + ":reader2:" + line
						+ " : " + stk2.length);
				i = 0;
				concLine = "";
				// has more token
				while (i < stk2.length) {
					TokenNumber++;
					if (i > 282)
						break;
					currToken = stk2[i];
					// blank data let be blank
					if (currToken.equals("") || currToken.equals(" ")
							|| currToken.equals(null)) {
						concLine = concLine + "," + "";
						i++;
						continue;
					}

					// if not numeric, then
					if (mapBoolean.get(TokenNumber) == false && // it is not a
																// number
							mapHeader.get(TokenNumber).toLowerCase()
									.indexOf("time") == -1) { // not a time/date

						if (headerUniqueList.containsKey(TokenNumber)) {
							UniqueList = headerUniqueList.get(TokenNumber);
							//
							if (UniqueList.containsKey(currToken)) { // factor /
																		// variable
																		// already
																		// exists
								System.out.println("lineNo:" + lineNo
										+ " there token:" + currToken + " : ");
								concLine = concLine + ","
										+ UniqueList.get(currToken);
								i++;
								continue;
							} else { // new factor (variable)
								temp = UniqueList.size() + 1;
								UniqueList.put(currToken, temp);
								headerUniqueList.put(TokenNumber, UniqueList);
							}
						} else {
							UniqueList.put(currToken, 1);
							headerUniqueList.put(TokenNumber, UniqueList);
						}
						System.out.println("TokenNumber:" + TokenNumber
								+ " : currToken:" + currToken + ": UniqueList:"
								+ UniqueList);
						//

						UniqueList = headerUniqueList.get(TokenNumber);
						concLine = concLine + "," + UniqueList.get(currToken); // get
																				// the
																				// id
																				// of
																				// uniqueValue

					} else { // this is a numeric value (so directly print out)
						concLine = concLine + "," + currToken;
					}
					i++;
				} // inner while

				System.out.println(concLine);
				writer.append(concLine + "\n");
				writer.flush();
			} // outerspriong while

			i = 0;
			// write the dictionary to a file
			for (Integer f : headerUniqueList.keySet()) {
				i++;
				UniqueList = headerUniqueList.get(f);
				//
				for (String k : UniqueList.keySet()) {
					writer2.append("start->" + f + " : " + k + " : "
							+ UniqueList.get(k) + "<-end \n");
					writer2.flush();
				}
				if (i > 3)
					break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// split a string into array and remove words >= of specified length.
	public static String SplitText2WordAndGetOnlyRequiredLengthWordString(
			String inputText, int OffSetlength) {
		String outputString = "";
		try {
			String[] s = inputText.split(" ");

			int cnt = 0;
			while (cnt < s.length) {

				if (s[cnt].length() <= OffSetlength) {
					outputString = outputString + " " + s[cnt];
				}

				cnt++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputString;
	}

	// read the file (ascii equivalent-> {%253F,?} ; {%3D=,=} ,{%2=53D,=} ,
	// {%253F , ?} {%253D ,\ } , {%252B,+}
	public static void readFolderEmailFilesUAW(
			TreeMap<String, String> all_Files_From_Given_Email_Feed_Folder,
			String debugfile, String outputFile, String output_SuccessFile,
			String YearOfEmailFile, boolean isAppend_OutFile,
			TreeMap<Integer, TreeMap<String, String>> mapContainsKeyValueTreeMap) {
		TreeMap<String, String> mapKeyTextBody2L = null;
		String part_curr_file_Name = "";
		try {
			FileWriter writerDebug = new FileWriter(debugfile, true);
			FileWriter writer = new FileWriter(outputFile, isAppend_OutFile);
			FileWriter writerSuccess = new FileWriter(output_SuccessFile,
					isAppend_OutFile);
			// File h = new File(curr_FileName);
			// String[] files = h.list();
			String line = "";

			TreeMap<String, String> mapKeyURL = new TreeMap();
			TreeMap<String, String> mapKeyURL_Original = mapContainsKeyValueTreeMap
					.get(1);
			if (mapContainsKeyValueTreeMap.containsKey(2)) {
				mapKeyTextBody2L = mapContainsKeyValueTreeMap.get(2);// NOT used
			}
			// Partial File Path
			for (String s : mapKeyURL_Original.keySet()) {
				if (s.indexOf("IMAP") >= 0) {
					mapKeyURL.put(s.substring(s.indexOf("IMAP"), s.length()),
							mapKeyURL_Original.get(s));

					// writerDebug.append("\n load:"+s.substring(s.indexOf("IMAP"),
					// s.length())
					// +"!!!"+mapKeyURL_Original.get(s));
					// writerDebug.flush();

				} else {
					mapKeyURL.put(s, mapKeyURL_Original.get(s));
					// writerDebug.append("\n load2:"+s+"!!!"+mapKeyURL_Original.get(s));
					// writerDebug.flush();
				}
			}

			// int hh=0;
			// while(hh<10){
			// for(String s:mapKeyURL.keySet()){
			// System.out.println("hh:"+s+"!!!"+mapKeyURL.get(s));
			// }
			// System.out.println("----------------");
			// }
			//

			String concLine = "";
			String origConcLine = "";
			int fileNo = 0;
			// read each file from that given email feed folder
			for (String curr_file_Name : all_Files_From_Given_Email_Feed_Folder
					.keySet()) {
				System.out.println("input file to read:" + "curr_FileName"
						+ curr_file_Name);
				part_curr_file_Name = curr_file_Name
						.substring(curr_file_Name.indexOf("IMAP"),
								curr_file_Name.length());
				int hh = 0;

				// while(hh<1000){
				// System.out.println("input file to read.skip:"
				// +"curr_FileName"+ part_curr_file_Name+"!!!"
				// + mapKeyURL.containsKey(part_curr_file_Name));
				// }

				// skip already read file
				if (mapKeyURL.containsKey(part_curr_file_Name)) {
					System.out.println("Already exists..."
							+ part_curr_file_Name + "!!!"
							+ mapKeyURL.get(part_curr_file_Name));
					writerDebug.append("\nAlready exists..."
							+ part_curr_file_Name + "!!!"
							+ mapKeyURL.containsKey(part_curr_file_Name));
					writer.flush();
					fileNo++;
					continue;
				} else {
					writerDebug.append("\n not exists..." + part_curr_file_Name
							+ ":" + mapKeyURL.containsKey(part_curr_file_Name));
					writer.flush();
				}

				BufferedReader reader11 = new BufferedReader(new FileReader(
						curr_file_Name));
				concLine = "";
				// read each line of current file
				while ((line = reader11.readLine()) != null) {
					// System.out.println("reading new line:"+line);
					concLine = concLine + line;

				}
				origConcLine = concLine.replace("=", "");
				// System.out.println("reading concLine:"+concLine);
				concLine = concLine.replace("=", "");
				boolean isYear2014 = false;
				if (concLine.indexOf(" 2014") >= 0
						|| concLine.toLowerCase().indexOf("Daily Update ") >= 0)
					isYear2014 = true;

				writer.flush();

				// which year it is
				if (isYear2014 == true) {
					System.out.println(" 2014 ----->" + origConcLine + "<--");
					String startPattern = "url?q3D", EndPattern = "amp";
					TreeMap<Integer, String> mapURL = new TreeMap<Integer, String>();
					int cnt = 0;
					String nextURL = "";
					// extract each url of current email
					while (concLine.indexOf(startPattern) >= 0
							&& concLine.indexOf(EndPattern) >= 0) {

						int beginIndex = concLine.indexOf(startPattern) + 9;
						int endIndex = concLine.indexOf(EndPattern, beginIndex) - 1;
						System.out.println("beginIndex-->" + beginIndex + " "
								+ endIndex + " " + concLine);
						// System.out.println(files[fileNo] +" "+
						// concLine.substring(beginIndex, endIndex) );
						String url = concLine
								.substring(beginIndex, endIndex)
								.replace(
										"http://news.google.com/news/story%3Fncl%3D",
										"").replace("%", "");
						if (url.indexOf("3Fop") >= 0) {
							url = url.substring(0, url.indexOf("3Fop"));
						}
						if (url.indexOf("&amp") >= 0) {
							url = url.substring(0, url.indexOf("&amp"));
						}
						// url cant be too short, its junk then
						if (url.replace("http://www", "").length() >= 7) {
							cnt++;
							mapURL.put(cnt, url);

						}

						// writer.append(files[fileNo] +"#"+
						// concLine.substring(beginIndex, endIndex) +"\n");
						// writer.flush();
						concLine = concLine
								.substring(endIndex, concLine.length())
								.replace("=", "").replace("=", "")
								.replace("%253F", "?").replace("%253D", "")
								.replace("%253D", "\\").replace("%252B", "+");

					} // extract each url of current email file
						//
					for (int i : mapURL.keySet()) {
						EndPattern = "228822";
						int beginIndex = origConcLine.indexOf(mapURL.get(i));
						//
						if (i < mapURL.size()) {
							nextURL = mapURL.get(i + 1);
						}

						EndPattern = nextURL;
						//
						if (i == mapURL.size()) // for last one change
							EndPattern = "Edit this";

						System.out.println("EndPattern:" + EndPattern
								+ ";curr.mapURL:" + mapURL.get(i) + ";nextURL:"
								+ nextURL + "beginI:" + beginIndex
								+ ";Endindex:"
								+ origConcLine.indexOf(EndPattern));
						if (origConcLine.indexOf(EndPattern) == -1
								|| (origConcLine.indexOf(EndPattern) <= beginIndex)) {
							System.out.println("orig->" + origConcLine);// .substring(beginIndex,
																		// origConcLine.length()));
							System.out.println("***cant find end Pattern:"
									+ mapURL);
							continue;
						}
						int endIndex = origConcLine.indexOf(EndPattern,
								beginIndex) - 1;

						String TitleAndBodyStartText = "";

						if (beginIndex >= 0 && endIndex > 0) {
							TitleAndBodyStartText = origConcLine.substring(
									beginIndex, endIndex); // error
						}

						String Subject = origConcLine.substring(origConcLine
								.indexOf("Subject:"), origConcLine.indexOf(
								" Google Alerts",
								origConcLine.indexOf("Subject:")));
						String Date = origConcLine.substring(
								origConcLine.indexOf("Date:"),
								origConcLine.indexOf("Subject: ",
										origConcLine.indexOf("Date:")));

						if (TitleAndBodyStartText.indexOf("Full Coverage") == -1)
							writer.append(curr_file_Name
									+ "!!!"
									+ mapURL.get(i)
									+ "!!!"
									+ SplitText2WordAndGetOnlyRequiredLengthWordString(
											removeStandardTagsFromHTML(
													TitleAndBodyStartText,
													false), 20).replace("   ",
											" ").replace("  ", " ") + "!!!"
									+ Subject + "!!!" + Date + "!!!" + "\n");

						writer.flush();
						System.out.println("flushing..2014:" + outputFile);

					}

				} else { // NOT 2014
					origConcLine = origConcLine.replace("=", "");
					System.out
							.println("not 2014 ----->" + origConcLine + "<--");
					TreeMap<Integer, String> mapURL = new TreeMap<Integer, String>();
					int cnt = 0;
					String startPattern = "3Fncl%3Dhttp", endPattern = "%26hl%3D";
					startPattern = "3Dhttp://";
					endPattern = "&amp";
					int OffSet = 2;
					writerDebug.append("\nstart,endPattern:"
							+ concLine.indexOf(startPattern) + ","
							+ concLine.indexOf(endPattern) + ","
							+ curr_file_Name);
					writerDebug.flush();

					// extract each url of current email
					while (concLine.indexOf(startPattern) >= 0
							&& concLine.indexOf(endPattern) >= 0) {

						int beginIndex = concLine.indexOf(startPattern)
								+ OffSet;
						int endIndex = concLine.indexOf(endPattern, beginIndex);
						System.out.println("beginIndex-->" + beginIndex + " "
								+ endIndex + " " + concLine);
						System.out.println(curr_file_Name + " "
								+ concLine.substring(beginIndex, endIndex));

						String url = concLine
								.substring(beginIndex, endIndex)
								.replace(
										"http://news.google.com/news/story%3Fncl%3D",
										"");
						if (url.indexOf("%") >= 0) {
							url = url.substring(0, url.indexOf("%"));
						}
						if (url.length() >= 0 && !mapURL.containsValue(url)) {
							cnt++;
							mapURL.put(cnt, url);
						}

						concLine = concLine
								.substring(endIndex, concLine.length())
								.replace("=", "").replace("=", "")
								.replace("%253F", "?").replace("%253D", "")
								.replace("%253D", "\\").replace("%252B", "+");

					} // which end extract each url of current email
					String nextURL = "";
					//
					for (int i : mapURL.keySet()) {
						String EndPattern = "228822";
						int beginIndex = origConcLine.indexOf(mapURL.get(i));
						//
						if (i < mapURL.size()) {
							nextURL = mapURL.get(i + 1);
						}
						EndPattern = nextURL;
						if (i == mapURL.size()) // for last one change
							EndPattern = "This once a day Google Alert";
						System.out.println("1.EndPattern:" + EndPattern
								+ ";curr.mapURL:" + mapURL.get(i) + ";nextURL:"
								+ nextURL + "beginI:" + beginIndex
								+ ";Endindex:"
								+ origConcLine.indexOf(EndPattern));
						if (origConcLine.indexOf(EndPattern) == -1
								|| (origConcLine.indexOf(EndPattern) <= beginIndex)) {
							System.out.println("orig->" + origConcLine);// .substring(beginIndex,
																		// origConcLine.length()));
							System.out.println("***cant find end Pattern:"
									+ mapURL);
							continue;
						}
						int endIndex = origConcLine.indexOf(EndPattern,
								beginIndex) - 1;

						if (endIndex == -1 || beginIndex == -1)
							continue;

						String TitleAndBodyStartText = origConcLine.substring(
								beginIndex, endIndex);
						// Extract Subject and Date
						// Date: Wed, 18 Mar 2015 03:01:51 +0000
						// Subject: Google Alert - kent AND general AND hospital
						// AND dover AND delaware
						// From: Google Alerts <googlealerts-noreply@google.com>
						String Subject = origConcLine.substring(origConcLine
								.indexOf("Subject:"), origConcLine.indexOf(
								" Google Alerts",
								origConcLine.indexOf("Subject:")));
						String Date = origConcLine.substring(
								origConcLine.indexOf("Date:"),
								origConcLine.indexOf("Subject: ",
										origConcLine.indexOf("Date:")));

						writer.append(curr_file_Name
								+ "!!!"
								+ mapURL.get(i)
								+ "!!!"
								+ SplitText2WordAndGetOnlyRequiredLengthWordString(
										removeStandardTagsFromHTML(
												TitleAndBodyStartText, false),
										20).replace("   ", " ").replace("  ",
										" ") + "!!!" + Subject + "!!!" + Date
								+ "!!!" + "\n");
						writer.flush();
						System.out.println("flushing...NOT 2014:" + outputFile);

					}

				}
				writer.flush();
				// System.out.println(files[fileNo]+" "+concLine);
				fileNo++;
				writerSuccess.append("\n" + curr_file_Name);

				writerSuccess.flush();
			} // end read each file

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// // NLP
	// public static String get2NLPTrained(String foldername, String flag,
	// String Sentence, Parse p, String outValue) {
	// SentenceDetector _sentenceDetector = null;
	//
	// List<Parse> nounPhrases = new ArrayList<Parse>();
	//
	// InputStream modelIn = null;
	// InputStream is2 = null;
	// int cnt = 0;
	// try {
	//
	// // Loading PARSER CHUNKER
	// //
	// http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool
	// System.out.println("-----------------" + p.toString());
	// p.show();
	// System.out.println("---end of show--------------");
	//
	// System.out.println(" 1.np: getType:" + p.getType() + ";getLabel:"
	// + p.getLabel() + ";getHead:" + p.getHead());
	// // +":"+ ";getParent:" + p.getParent() );
	//
	// if (!p.getType().equals("TOP")) {
	// outValue = outValue + p.getType() + ":" + p.getHead();
	// }
	//
	// /*
	// * if (p.getType().equals("NP")) { nounPhrases.add(p);
	// *
	// * System.out.println(" 2.np: type:" + p.getType() +" getNodes:" +
	// * p.getLabel() +":"+ p.getHead() +":"+ " parent:" + p.getParent()
	// * +" child:"+ p.getChildren()); }
	// */
	// for (Parse child : p.getChildren()) {
	// get2NLPTrained(foldername, flag, Sentence, child, outValue);
	// }
	// // return p;
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// System.out.println("ov:" + outValue);
	//
	// return outValue;
	// }

	// token name finder model
	public static TokenNameFinderModel getNameFinderModel(URL name) {
		try {
			return new TokenNameFinderModel(new DataInputStream(
					name.openStream()));
		} catch (IOException E) {
			E.printStackTrace();
			throw new RuntimeException(
					"OpenNLP Tokenizer can not be initialized!", E);
		}
	}

	// getNLPTrained (below link has information on training etc)
	// https://opennlp.apache.org/documentation/manual/opennlp.html
	// http://opennlp.sourceforge.net/models-1.5/
	public static TreeMap<String, String> getNLPTrained(String foldername,
														String flag, //// "en-pos-maxent.bin"
														String flag_2, // {}
														String strSentence, // give strSentence or arraySentence
														String[] arraySentence, FileWriter writerdebugOutErrorAndURLFile,
														boolean is_overwrite_flag_with_inflag_model2,//is_overwrite_flag_with_flag_model
														POSModel inflag_model2_maxent, //// model of "en-pos-maxent.bin"
														ParserModel inflag_model2_chunking, //can be null(if dont want use)
														boolean isSOPdebug) {

		//important . 
		strSentence=strSentence.replace("\\.,", ",") .replace("\\.", "\\.  "); //otherwise get gets dirty like region."_NN
		
		SentenceDetector _sentenceDetector = null;
		String sent[] = null;
		List<Parse> nounPhrases = new ArrayList<Parse>();
		TreeMap<String, String> mapOut = new TreeMap<String, String>();
		InputStream modelIn = null;
		InputStream is2 = null; POSModel model2 = null;ParserModel model2_chunker = null;
		int cnt = 0;
		try {

			// Loading sentence detection model
			String paragraph = "Hi. How are you? This is Mike.";

			if (flag.equalsIgnoreCase("en-sent.bin")
					|| flag.equalsIgnoreCase("all")) {
				// always start with a model, a model is learned from training
				// data
				System.out
						.println("------------------------en-sent.bin--------------");
				is2 = new FileInputStream(foldername + "en-sent.bin");
				
				SentenceModel model5 = new SentenceModel(is2);
				is2.close();
				SentenceDetectorME sdetector = new SentenceDetectorME(model5);

				String sentences[] = sdetector.sentDetect(paragraph);

				System.out.println("sentence:" + sentences[0]);
				System.out.println("sentence:" + sentences[1]);
				is2.close();

				// Loading TOKEN model
				InputStream is10 = new FileInputStream(foldername+ "en-token.bin");
				TokenizerModel model10 = new TokenizerModel(is10);
				Tokenizer tokenizer = new TokenizerME(model10);
				// String tokens[] = tokenizer
				// .tokenize("Hi. How are you? This is Mike.");
				String tokens[] = tokenizer
						.tokenize("Hi. How are you? This is Mike.");

				for (String a : tokens)
					System.out.println("token:" + a);

				is10.close();
			} // end of en-sent.bin

			System.out.println("\n -------------------------");
			
			// output < NNP NN VP > etc = EN-POS-MAXENT
			if (flag.equalsIgnoreCase("en-pos-maxent.bin")
					|| flag.equalsIgnoreCase("all")
					|| flag_2.equalsIgnoreCase("P,P,O,L,D,T")) {
				if (isSOPdebug)
					System.out
							.println("---IF condition---------------start--------en-ner-maxent.bin--------------");
				writerdebugOutErrorAndURLFile
						.append("------------------------en-ner-maxent.bin--------------");
				String tags[] = null;
				// example 1
				/*
				 * 
				 * // Loading POS TAGGER model (Output: NNP VBD PRP NN)
				 * InputStream modelIn3 = new FileInputStream(foldername
				 * +"en-pos-maxent.bin"); //+ "en-pos-maxent.bin"); POSModel
				 * model = new POSModel(modelIn3);
				 * 
				 * POSTaggerME tagger = new POSTaggerME(model);
				 * 
				 * /* String sent[] = new String[]{ "Rockwell", "International",
				 * "Corp.", "'s", "Tulsa", "unit", "said", "it", "signed", "a",
				 * "tentative", "agreement", "extending", "its", "contract",
				 * "with", "Boeing", "Co.", "to", "provide", "structural",
				 * "parts", "for", "Boeing", "'s", "747", "jetliners", "." };
				 */
				/*
				 * if (arraySentence == null) sent = arraySentence; else sent =
				 * Sentence.split(" ");
				 * 
				 * tags = tagger.tag(sent); System.out.println("array:" +
				 * arraySentence.length + " "+ sent.length); cnt = 0;
				 */
				String[] out = null;
				//
				/*
				 * while(cnt < tags.length && tags[cnt] !=null ){
				 * System.out.println("out1->"+sent[cnt] + " "+tags[cnt]);
				 * out[cnt]= sent[cnt] + "_"+tags[cnt];
				 * System.out.println("out->"+out[cnt]); cnt++; //
				 */
				// example 2
				if (isSOPdebug)
					System.out.println("model path:" + foldername+ "en-pos-maxent.bin");
				
				// use input (less time)
				if(is_overwrite_flag_with_inflag_model2){
					model2 =inflag_model2_maxent;
					model2_chunker=inflag_model2_chunking;
				}
				// load (takes time)
				if(!is_overwrite_flag_with_inflag_model2){
					model2 = new POSModelLoader().load(new File(foldername+ "en-pos-maxent.bin"));
					InputStream in_model2_chunker = new FileInputStream(foldername+"en-parser-chunking.bin");
					model2_chunker = new ParserModel(in_model2_chunker);
				}
				 
				PerformanceMonitor perfMon = new PerformanceMonitor(System.err,"sent");
				POSTaggerME tagger2 = new POSTaggerME(model2); 

				String input = "Hi. How are you? This is Mike.";
				// input="AN 18-year-old girl was shot dead by her brother in the Kahna area on Tuesday. "
				// +
				// "The deceased was identified Jameela, daughter of Abdul Rehman of Bohgal village, Kahna."
				// +
				// "Police said accused Zahid murdered his daughter on suspicion. The victim’s family claimed that Zahid "
				// +
				// "got infuriated when he was asked to complete his sister’s dowry as her marriage ceremony was due after six days. "
				// +
				// "On the day of the incident, he shot her dead when she was asleep. "
				// +
				// "The accused escaped from the scene. Police have registered a case and removed the body to morgue for autopsy."
				// + "People are very sad.";

				input = strSentence; // RESET INPUT
				String[] arr_each_sentence =input.split("\\.");
				int len_arr_each_sentence=arr_each_sentence.length;
				int cnt1=0;

				if (isSOPdebug) {
					System.out.println("^^^^^^ strSentence:"
							+ strSentence.length());

					writerdebugOutErrorAndURLFile.append("\n Sentence:"
							+ strSentence);
					writerdebugOutErrorAndURLFile.flush();

				}

				ObjectStream<String> lineStream = new PlainTextByLineStream(new StringReader(input));

				perfMon.start();
				POSSample sampleTaggedText = null;
				String line;
				while ((line = lineStream.read()) != null) {

					String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE.tokenize(line);
					String[] tags2 = tagger2.tag(whitespaceTokenizerLine);

					sampleTaggedText = new POSSample(whitespaceTokenizerLine,tags2);
					
					 		
					if (isSOPdebug) {
						System.out.println("\n $$$$ pos2:"
								+ sampleTaggedText.toString());
					}

					perfMon.incrementCounter();
				}
				//perfMon.stopAndPrintFinalResult();
				
				if(model2_chunker!=null){
					 
				
						// chunker to create dependency tree of sentence(input)
						Parser parser = ParserFactory.create(model2_chunker);
						Parse topParses[] = ParserTool.parseLine(input, parser, 1);
						// parse dependency tree to String 
					    for (opennlp.tools.parser.Parse p : topParses){
					        StringBuffer  sb2 = new StringBuffer(input.length() * 4);
					      
					        p.show(sb2);
					        //sb now contains all the tags
					        if(isSOPdebug)
					        	System.out.println("stringbuilder:" +sb2);
		 
						//
						if (isSOPdebug)
							System.out.println("----IF condition--------end------------en-ner-maxent.bin--------------"+ sampleTaggedText);
						
						writerdebugOutErrorAndURLFile.append("\n sample:"+ sampleTaggedText + " body:" + input);
						writerdebugOutErrorAndURLFile.append("\n--en-ner-maxent.bin------tags:" + tags);
						writerdebugOutErrorAndURLFile.flush();
						
						if (sampleTaggedText != null) {
							
							//System.out.println("sb2.toString()----->"+sb2.toString());
							// adding dependency tree.
							mapOut.put("taggedDependencyTree", sb2.toString());	
						}
						// if(flag_2.equalsIgnoreCase("") &&
						// !flag.equalsIgnoreCase("all") )
						// return mapOut;
					} // END for (opennlp.tools.parser.Parse p : topParses){
			    
			} //END if(model2_chunker!=null){
				
				mapOut.put("taggedNLP", Correct_dot_and_tag_misplaced.correct_dot_and_tag_misplaced(sampleTaggedText.toString()));
			    
			    
			} // END if (flag.equalsIgnoreCase("en-pos-maxent.bin")
			    
			// dont want run both methods as it wound take more time
			String flag_type_1_OR_2 = "2";// {1=METHOD 1, 2= METHOD 2, 3=BOTH
											// METHOD }
			InputStream modelIn2 = null;
			TokenNameFinderModel model20=null; 
			// Loading PERSON model
			if (flag.equalsIgnoreCase("en-ner-person.bin")
					|| flag.equalsIgnoreCase("all")) {
				// WE IGNORE THIS, AS WE CHOOSE METHOD 2
				if (flag_type_1_OR_2.equalsIgnoreCase("1")
						|| flag_type_1_OR_2.equalsIgnoreCase("3")// 3 is fOR
																	// BOTH
																	// METHOD TO
																	// USE
				) {

					if (isSOPdebug)
						System.out
								.println("------------------------en-ner-person.bin--------------");

					  modelIn2 = new FileInputStream(foldername+ "en-ner-person.bin");

					if (isSOPdebug)
						System.out.println("model" + foldername
								+ "en-ner-person.bin");

					model20 = new TokenNameFinderModel(modelIn2);

					modelIn2.close();
					NameFinderME NameFinder = new NameFinderME(model20);

					// sent = new String[]{"lenin","is","a","good","person" };

					sent = strSentence.split(" ");
					Span nameSpans[] = NameFinder.find(sent);

					NameFinder.clearAdaptiveData();

					System.out.println("PERSON nameSpans.length;"
							+ nameSpans.length);

					String conc = "";
					for (int i = 0; i < nameSpans.length; i++) {

						if (nameSpans[i].getStart() < nameSpans.length) {
							conc = sent[nameSpans[i].getStart()];
							System.out.println("Person Span: "
									+ nameSpans[i].toString());
						}
						if (nameSpans[i].getStart() + 1 < nameSpans.length) {
							conc = conc + "#####"
									+ sent[nameSpans[i].getStart() + 1];
						}

					}
					String new_string = mapOut.get("taggedNLP") + "$#$#person:"
							+ conc + "#####";
					mapOut.put("taggedNLP", new_string);

					// while (cnt < nameSpans.length) {
					// System.out.println("@@@@@ person<tag>:nameSpans:" +
					// nameSpans[cnt] + "<-");
					//
					//
					// System.out.println(" namespace string:"+nameSpans[cnt].toString());
					// String tmp=nameSpans[cnt].toString();
					// // String h[]=nameSpans[cnt].toString().replace("..",
					// ".").replace("[", "").replace(")", "")
					// // .split(".");
					//
					// String
					// numbers=tmp.substring(tmp.indexOf("[")+1,tmp.indexOf(")")).trim().replace("..",
					// "!!!");
					// String [] arr_number=numbers.split("!!!");
					// System.out.println("numbers: "+numbers +" size:"
					// +arr_number.length);
					// if(arr_number.length==2){
					// System.out.println(" SIZE 2" );
					//
					// int orig_start_index=Integer.valueOf(arr_number[0]);
					// int orig_end_index=Integer.valueOf(arr_number[1]);
					// int start_index=orig_start_index;
					// System.out.println(
					// "orig_start_index:"+orig_start_index+" orig_end_index:"+orig_end_index
					// );
					// while(start_index<=orig_end_index){
					// System.out.println(" *caught="+sent[start_index] );
					// start_index++;
					// mapOut.put(sent[start_index], "");
					// }
					//
					// }
					//
					// int c_h=0;
					// // while(c_h<h.length){
					// // System.out.println("person found:"+sent[c_h]);
					// // c_h++;
					// // }
					// cnt++;
					// }
				}

				// METHOD 2
				if (flag_type_1_OR_2.equalsIgnoreCase("2")
						|| flag_type_1_OR_2.equalsIgnoreCase("3")
						|| flag_2.equalsIgnoreCase("P,P,O,L,D,T")) {

					// example 2
					InputStream is = new FileInputStream(foldername
							+ "en-ner-person.bin");

					TokenNameFinderModel model100 = new TokenNameFinderModel(is);
					is.close();

					NameFinderME nameFinder = new NameFinderME(model100);

					// String []sentence = new String[]{
					// "Mike",
					// "Smith",
					// "is",
					// "a",
					// "good",
					// "person"
					// };
					sent = strSentence.split(" ");
					Span nameSpans[] = nameFinder.find(sent);

					//
					String conc = "";
					for (int i = 0; i < nameSpans.length; i++) {

						if (nameSpans[i].getStart() < nameSpans.length) {
							conc = sent[nameSpans[i].getStart()];
							System.out.println("Person Span: "
									+ nameSpans[i].toString());
						}
						if (nameSpans[i].getStart() + 1 < nameSpans.length) {
							conc = conc + "#####"
									+ sent[nameSpans[i].getStart() + 1];
						}

					}
					String new_string = mapOut.get("taggedNLP") + "$#$#person:"
							+ conc + "#####";
					mapOut.put("taggedNLP", new_string);

					// for(Span s: nameSpans2){
					// System.out.println("@@@@ person<tag>:"+s+"<--->"+s.toString());

					// String tmp=s.toString();
					// // String h[]=nameSpans[cnt].toString().replace("..",
					// ".").replace("[", "").replace(")", "")
					// // .split(".");
					//
					// String
					// numbers=tmp.substring(tmp.indexOf("[")+1,tmp.indexOf(")")).trim().replace("..",
					// "!!!");
					// String [] arr_number=numbers.split("!!!");
					// System.out.println(" 2method numbers: "+numbers +" size:"
					// +arr_number.length);
					// if(arr_number.length==2){
					// System.out.println("arr 1:"+arr_number[0]+" arr 2:"+arr_number[1]);
					//
					// if(sent==null)
					// System.out.println("sent=NULL ->"+strSentence);
					// else
					// System.out.println("sent=NOT NULL"+sent.length);
					//
					// System.out.println(" 2method  caught="+sent[Integer.valueOf(arr_number[0])]
					// );
					//
					// int orig_start_index=Integer.valueOf(arr_number[0]);
					// int orig_end_index=Integer.valueOf(arr_number[1]);
					// int start_index=orig_start_index;
					// System.out.println(
					// " 2method orig_start_index:"+orig_start_index+" orig_end_index:"+orig_end_index
					// );
					// while(start_index<=orig_end_index){
					// System.out.println(" 2method  *caught="+sent[start_index]
					// );
					// mapOut.put(sent[start_index], "");
					// start_index++;
					//
					// }
					// }
					// } //end FOR span

				}

			}// end of en-ner-person.bin"

			// MONEY Finding
			if (flag.equalsIgnoreCase("en-ner-money.bin")
					|| flag.equalsIgnoreCase("all")
					|| flag_2.equalsIgnoreCase("P,P,O,L,D,T")) {
				try {
					InputStream is = new FileInputStream(foldername
							+ "en-ner-money.bin");

					TokenNameFinderModel model = new TokenNameFinderModel(is);
					is.close();

					NameFinderME nameFinder = new NameFinderME(model);

					sent = strSentence.split(" ");

					Span nameSpans[] = nameFinder.find(sent);
					System.out.println("MONEY nameSpans.length:"
							+ nameSpans.length);
					// for(Span s: nameSpans)
					// System.out.println(" caught TIME:"+s.toString());
					String conc = "";
					for (int i = 0; i < nameSpans.length; i++) {

						if (nameSpans[i].getStart() < nameSpans.length) {
							conc = sent[nameSpans[i].getStart()];
							System.out.println("Money Span: "
									+ nameSpans[i].toString());
						}
						if (nameSpans[i].getStart() + 1 < nameSpans.length) {
							conc = conc + "#####"
									+ sent[nameSpans[i].getStart() + 1];
						}

					}
					String new_string = mapOut.get("taggedNLP") + "$#$#money:"
							+ conc + "#####";
					mapOut.put("taggedNLP", new_string);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Time Finding
			if (flag.equalsIgnoreCase("en-ner-time.bin")
					|| flag.equalsIgnoreCase("all")
					|| flag_2.equalsIgnoreCase("P,P,O,L,D,T")) {
				try {
					InputStream is = new FileInputStream(foldername
							+ "en-ner-time.bin");

					TokenNameFinderModel model = new TokenNameFinderModel(is);
					is.close();

					NameFinderME nameFinder = new NameFinderME(model);

					sent = strSentence.split(" ");

					Span nameSpans[] = nameFinder.find(sent);

					// for(Span s: nameSpans)
					// System.out.println(" caught TIME:"+s.toString());
					String conc = "";
					for (int i = 0; i < nameSpans.length; i++) {

						if (nameSpans[i].getStart() < nameSpans.length) {
							conc = sent[nameSpans[i].getStart()];
							System.out.println("Time Span: "
									+ nameSpans[i].toString());
						}
						if (nameSpans[i].getStart() + 1 < nameSpans.length) {
							conc = conc + "#####"
									+ sent[nameSpans[i].getStart() + 1];
						}

					}
					String new_string = mapOut.get("taggedNLP") + "$#$#time:"
							+ conc + "#####";
					mapOut.put("taggedNLP", new_string);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// PERCENTAGE en-ner-percentage.bin
			if (flag.equalsIgnoreCase("en-ner-percentage.bin")
					|| flag.equalsIgnoreCase("all")
					|| flag_2.equalsIgnoreCase("P,P,O,L,D,T")) {
				try {
					InputStream is = new FileInputStream(foldername
							+ "en-ner-percentage.bin");

					TokenNameFinderModel model = new TokenNameFinderModel(is);
					is.close();

					NameFinderME nameFinder = new NameFinderME(model);

					sent = strSentence.split(" ");

					Span nameSpans[] = nameFinder.find(sent);

					// for(Span s: nameSpans)
					// System.out.println(" caught TIME:"+s.toString());
					String conc = "";
					for (int i = 0; i < nameSpans.length; i++) {

						if (nameSpans[i].getStart() < nameSpans.length) {
							conc = sent[nameSpans[i].getStart()];
							System.out.println("Percentage Span: "
									+ nameSpans[i].toString());
						}
						if (nameSpans[i].getStart() + 1 < nameSpans.length) {
							conc = conc + "#####"
									+ sent[nameSpans[i].getStart() + 1];
						}

					}
					String new_string = mapOut.get("taggedNLP")
							+ "$#$#percentage:" + conc + "#####";
					mapOut.put("taggedNLP", new_string);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// Date Finding
			if (flag.equalsIgnoreCase("en-ner-date.bin")
					|| flag.equalsIgnoreCase("all")
					|| flag_2.equalsIgnoreCase("P,P,O,L,D,T")) {
				try {
					InputStream is = new FileInputStream(foldername
							+ "en-ner-date.bin");

					TokenNameFinderModel model = new TokenNameFinderModel(is);
					is.close();

					NameFinderME nameFinder = new NameFinderME(model);

					sent = strSentence.split(" ");

					Span nameSpans[] = nameFinder.find(sent);

					// for(Span s: nameSpans){
					// System.out.println(" caught DATE:"+s.toString());
					// }
					String conc = "";
					for (int i = 0; i < nameSpans.length; i++) {

						if (nameSpans[i].getStart() < nameSpans.length) {
							conc = sent[nameSpans[i].getStart()];
							System.out.println("Date Span: "
									+ nameSpans[i].toString());
						}
						if (nameSpans[i].getStart() + 1 < nameSpans.length) {
							conc = conc + "#####"
									+ sent[nameSpans[i].getStart() + 1];
						}
					}
					String new_string = mapOut.get("taggedNLP") + "$#$#date:"
							+ conc + "#####";
					mapOut.put("taggedNLP", new_string);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			FileInputStream modelIn200=null;
			NameFinderME nameFinder=null;
			// ORGANIZATION
			if (flag.equalsIgnoreCase("en-ner-organization.bin")
					|| flag.equalsIgnoreCase("all")
					|| flag_2.equalsIgnoreCase("P,P,O,L,D,T")) {
				try {
					sent = strSentence.split(" ");

					// InputStream is = new FileInputStream(foldername+
					// "en-token.bin");
					// TokenizerModel model = new TokenizerModel(is);
					// Tokenizer tokenizer = new TokenizerME(model);
					// String tokens[] =
					// tokenizer.tokenize("Hi. How are you? This is Mike.");

					modelIn200 = new FileInputStream(foldername+ "en-ner-organization.bin");
					nameFinder = new NameFinderME(new TokenNameFinderModel(modelIn200));

					// Tokenizer tokenizer = new TokenizerME(tokenModel);

					String tokens[] = strSentence.split(" ");
					Span nameSpans[] = nameFinder.find(sent);
					String conc = "";
					for (int i = 0; i < nameSpans.length; i++) {

						if (nameSpans[i].getStart() < nameSpans.length) {
							conc = sent[nameSpans[i].getStart()];
							System.out.println("Organization Span: "
									+ nameSpans[i].toString());
						}
						if (nameSpans[i].getStart() + 1 < nameSpans.length) {
							conc = conc + "#####"
									+ sent[nameSpans[i].getStart() + 1];
						}

					}
					String new_string = mapOut.get("taggedNLP")
							+ "$#$#organization:" + conc + "#####";
					mapOut.put("taggedNLP", new_string);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			NameFinderME nameFinder100 = null;
			// LOCATION
			if (flag.equalsIgnoreCase("en-ner-location.bin")
					|| flag.equalsIgnoreCase("all")
					|| flag_2.equalsIgnoreCase("P,P,O,L,D,T")) {
				try {

					// CharSequence
					// str="This is Indian which is in Buffalo New York.";
					// String documentStr=str.toString();
					modelIn = new FileInputStream(foldername + "en-token.bin");
					TokenizerModel tokenModel = new TokenizerModel(modelIn);
					modelIn.close();
					Tokenizer tokenizer = new TokenizerME(tokenModel);

					nameFinder100 = new NameFinderME(
							new TokenNameFinderModel(new FileInputStream(
									foldername + "en-ner-location.bin")));

					String tokens[] = tokenizer.tokenize(strSentence);
					Span nameSpans[] = nameFinder100.find(tokens);

					String conc = "";
					for (int i = 0; i < nameSpans.length; i++) {

						if (nameSpans[i].getStart() < nameSpans.length) {
							conc = sent[nameSpans[i].getStart()];
							System.out.println("Location Span: "
									+ nameSpans[i].toString());
						}
						if (nameSpans[i].getStart() + 1 < nameSpans.length) {
							conc = conc + "#####"
									+ sent[nameSpans[i].getStart() + 1];
						}

					}
					String new_string = mapOut.get("taggedNLP")
							+ "$#$#location:" + conc + "#####";
					mapOut.put("taggedNLP", new_string);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}

			//
			if (flag.equalsIgnoreCase("en-chunker.bin")
					|| flag.equalsIgnoreCase("all")) {
				System.out
						.println("------------------------en-chunker.bin--------------");
				// Loading CHUNKER
				InputStream is = new FileInputStream(foldername
						+ "en-chunker.bin");
				ChunkerModel cModel = new ChunkerModel(is);

				String whitespaceTokenizerLine[] = new String[] { "Rockwell",
						"International", "Corp.", "'s", "Tulsa", "unit",
						"said", "it", "signed", "a", "tentative", "agreement",
						"extending", "its", "contract", "with", "Boeing",
						"Co.", "to", "provide", "structural", "parts", "for",
						"Boeing", "'s", "747", "jetliners", "." };

				String pos[] = new String[] { "NNP", "NNP", "NNP", "POS",
						"NNP", "NN", "VBD", "PRP", "VBD", "DT", "JJ", "NN",
						"VBG", "PRP$", "NN", "IN", "NNP", "NNP", "TO", "VB",
						"JJ", "NNS", "IN", "NNP", "POS", "CD", "NNS", "." };

				ChunkerME chunkerME = new ChunkerME(cModel);
				String result[] = chunkerME.chunk(whitespaceTokenizerLine, pos);
				// OUTPUT -> B-NP (or) I-NP
				for (String s : result)
					System.out.println("chunkerME:" + s);

				// OUTPUT -> [0..3) NP
				Span[] span = chunkerME.chunkAsSpans(whitespaceTokenizerLine,
						pos);
				for (Span s : span) {
					System.out.println("chunkerME.chunkAsSpans: "
							+ s.toString());
				}

				double probs[] = chunkerME.probs();
				System.out.println("chunkerME.probs: " + probs.length);
				cnt = 0;
				while (cnt < probs.length) {
					System.out.println("chunkerME.chunkAsSpans: " + probs[cnt]);
					cnt++;
				}

			} // end of "en-chunker.bin"

			//
			if (flag.equalsIgnoreCase("en-parser-chunking.bin")
					|| flag.equalsIgnoreCase("all")) {
				Parse p1 = null;
				System.out
						.println("------------------------en-parser-chunking.bin--------------");
				
				// Loading PARSER CHUNKER
				// http://sourceforge.net/apps/mediawiki/opennlp/index.php?title=Parser#Training_Tool
				is2 = new FileInputStream(foldername + "en-parser-chunking.bin");
				ParserModel model3 = new ParserModel(is2);
				
				Parser parser = ParserFactory.create(model3);
				String[] SentenceA = strSentence.split(" ");

				// sentence = "Programcreek is a very huge and useful website.";
				Parse topParses1[] = ParserTool.parseLine(strSentence, parser, 1);// what
															// is
															// 1
				System.out.println(" ---topParses.len---" + topParses1.length+ ":" + topParses1[0]);
				String s = "";
				for (Parse p : topParses1) {
					System.out.println(" --1-----");
					p.show();
					System.out.println(" ------3-");
					p.getType();
					System.out.println(" --2-----" + p.getType() + "<--");
					p.toString();

					if (p.getType().equals("NP")) {
						System.out.println(" -->np---> " + p.getChildren()
								+ " : " + p.getParent());
					}

					// return p.show();
					// nounPhrases.add(p.show());
					System.out.println(" ---cc--- \n" + topParses1);
				}
				is2.close();
				// return p;

			}// end of en-parser-chunking.bin

			//
			if (flag_2.equalsIgnoreCase("P,P,O,L,D,T")) {
				String tm = mapOut.get("taggedNLP") + "$#$#";
				mapOut.put("taggedNLP", tm);
			}

			// return p1.show();
			
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (final IOException e) {
				} // oh well!
			}
		}
		return mapOut;
	}

	// to be copied:
	public static void wrapperGetReport(String rootFolderPath,
			String FilePathDelimiter, String crimKeyWord, String MonthYear,
			String CollectionName, String outReportFile,
			String outFileforDBdump, String outFileforStateDBdump, String Flag,
			String getPlaceFlag, String statePatternToSearch,
			String ReportType, String CountryCode) {
		try {

			File h = new File(rootFolderPath);
			String[] files = h.list();

			int cnt = 0;

			// if its folder and folder name = 2
			while (cnt < files.length) {

				File h2 = new File(rootFolderPath + files[cnt]);

				System.out.println("\n 1:" + rootFolderPath + files[cnt]
						+ FilePathDelimiter + files[cnt] + ".txt" + "\n 2:"
						+ files[cnt] + FilePathDelimiter + outReportFile
						+ "\n 3:" + rootFolderPath + files[cnt]
						+ FilePathDelimiter + outFileforDBdump + "\n 4:"
						+ rootFolderPath + files[cnt] + FilePathDelimiter
						+ outFileforStateDBdump + "\n files[cnt].length():"
						+ files[cnt].length() + " files.length:" + files.length
						+ "\n cnt:" + cnt);

				// directory
				if (h2.isDirectory() && files[cnt].length() != 2) {
					cnt++;
					continue;
				} else if (h2.isDirectory() && files[cnt].length() == 2) {
					getReport(crimKeyWord, MonthYear, CollectionName,
							rootFolderPath + files[cnt] + FilePathDelimiter
									+ files[cnt] + ".txt", rootFolderPath
									+ files[cnt] + FilePathDelimiter
									+ outReportFile, rootFolderPath
									+ files[cnt] + FilePathDelimiter
									+ outFileforDBdump, rootFolderPath
									+ files[cnt] + FilePathDelimiter
									+ outFileforStateDBdump, Flag,
							getPlaceFlag, statePatternToSearch, ReportType,
							CountryCode // "" if to be read from physical file (
										// and not from mongodb)
							, false, 1000);

				}
				cnt++;
			}

		} catch (Exception e) {

		}
	}

	// get state/city info from database ; Flag : {"state","city"}
	public static HashMap<Integer, HashMap<String, String>> getCityNameFromMongoDB(
			String Flag, String CountryCode, String CollectionName) {
		//
		HashMap<Integer, HashMap<String, String>> mapOut = new HashMap();
		try {

			String latlong = "";
			DBCursor cursor = null;
			HashMap<String, String> mapState = new HashMap();
			Mongo mongoClient = new Mongo("10.162.181.174");
			DB db1 = mongoClient.getDB("test");
			DBCollection coll = db1.getCollection(CollectionName); // may be
																	// country
			System.out.println(" inside getCityNameFromMongoDB: db:" + db1
					+ " CountryCode:" + CountryCode + " CollectionName:"
					+ CollectionName);
			// Jongo jongo = new Jongo(db1);
			// MongoCollection friends = jongo.getCollection(CollectionName);
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			obj.add(new BasicDBObject("code", java.util.regex.Pattern
					.compile(CountryCode))); // country code
			// obj.add(new BasicDBObject("ebody",
			// java.util.regex.Pattern.compile( " "+mapStateName.get(i) ) ));
			BasicDBObject andQuery = new BasicDBObject();
			andQuery.put("$and", obj);
			// JsonParserFactory factory=JsonParserFactory.getInstance();
			// org.codehaus.jackson.JsonFactory jfactory = new
			// org.codehaus.jackson.JsonFactory();
			// JSONParser parser= factory.newJsonParser();

			int cnt = 0;
			//
			if (Flag.equalsIgnoreCase("state-wise")) {
				cursor = coll.find(andQuery);
				System.out.println(" cursor.count " + cursor.count());
				// find record
				if (cursor.count() > 0) {
					//
					JSONParser parser = new JSONParser();
					ObjectMapper m = new ObjectMapper();
					while (cursor.hasNext()) {
						Map a = cursor.next().toMap();
						System.out.println("curr state:" + " a:" + a);
						//
						for (Object i : a.keySet()) {

							// if(cnt == 1) continue;

							if (i.toString().equalsIgnoreCase("states")) {
								cnt = 0;
								// parser.parseJson( (String) i );
								String j = a.get(i).toString(); // .replaceAll("\"",
																// "");
								System.out.println(" key.j:" + j);
								JsonNode rootNode = m.readTree(j);
								String jsonData = a.get(i).toString();

								while (cnt < rootNode.size()) {

									System.out.println(rootNode.get(cnt).get(
											"lat")
											+ " :: "
											+ rootNode.get(cnt).get("lon")
											+ " :: "
											+ rootNode.get(cnt).get("name"));
									mapState.put(
											rootNode.get(cnt).get("name")
													.toString()
													.replace("\"", ""),
											rootNode.get(cnt).get("name")
													.toString()
													.replace("\"", "")
													+ "#"
													+ rootNode.get(cnt)
															.get("lat")
															.toString()
															.replace("\"", "")
													+ "#"
													+ rootNode.get(cnt)
															.get("lon")
															.toString()
															.replace("\"", ""));

									cnt++;
								}

							}
						} // for loop
					} // while end cursor
					mapOut.put(5, mapState);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapOut;
	}


	// to be copied: ReportType_ {state-wise, city-wise , zone-wise }
	public static void getReport(String crimKeyWord, String MonthYear,
			String CollectionName, String CityFile, String outReportFile,
			String outFileforDBdump, String outFileforStateDBdump, String Flag,
			String getPlaceFlag, String statePatternToSearch,
			String ReportType, String CountryCode, boolean isSleep,
			int sleepSeconds) {

		System.out.println(" getReport ");
		String currCityName = "";
		String currStateName = "";
		try {
			FileWriter writer = new FileWriter(outReportFile);
			Mongo mongoClient = new Mongo("10.162.181.174");

			DB db1 = mongoClient.getDB("test");
			System.out.println("getReport1 " + db1 + " CollectionName:"
					+ CollectionName + "");
			// Jongo jongo = new Jongo(db1);
			// MongoCollection friends = jongo.getCollection(CollectionName);
			// friends = jongo.getCollection(CollectionName);
			// String outFile =
			// "C:\\Users\\mookiah\\GoogleDrive\\Vooz\\IN\\OutCityState.txt";
			HashMap<String, String> mapIsProcessedState = new HashMap();
			HashMap<String, String> mapCityName = new HashMap();
			HashMap<String, String> mapStateName = new HashMap();
			HashMap<String, String> mapLatitude = new HashMap();
			HashMap<String, String> mapLongitude = new HashMap();
			HashMap<String, String> mapDistinctStateName = new HashMap();
			/*
			 * 
			 * // city name HashMap<String,String> mapCityName = mapOut.get(1);
			 * HashMap<String,String> mapStateName = mapOut.get(2);
			 * HashMap<String,String> mapLatitude = mapOut.get(3);
			 * HashMap<String,String> mapLongitude = mapOut.get(4);
			 * HashMap<String,String> mapDistinctStateName = mapOut.get(5);
			 */

			// read from db
			if (CountryCode.length() > 1) {
				HashMap<Integer, HashMap<String, String>> mapOut = getCityNameFromMongoDB(
						ReportType, CountryCode, "country");
				mapDistinctStateName = mapOut.get(5);
				System.out
						.println("......read from database state names->mapout:"
								+ mapOut.size()
								+ " outReportFile:"
								+ outReportFile
								+ " mapDistinctStateName:"
								+ mapDistinctStateName);

			} else {
				System.out.println("......read from file state names");
				HashMap<Integer, HashMap<String, String>> mapOut = getCityName(
						CityFile, outFileforDBdump, outFileforStateDBdump,
						statePatternToSearch, getPlaceFlag, ",");
			}

			// System.out.println("mapout:"+mapOut.size() +
			// " mapDistinctStateName.size:"+mapDistinctStateName.size()
			// +" mapDistinctStateName:"+mapDistinctStateName);
			/*
			 * mapDistinctStateName.put("manipur","manipur#25#94");
			 * mapDistinctStateName.put("goa","goa#15.33333#74.08333");
			 * mapDistinctStateName.put("rajasthan","rajasthan#26#74");
			 * mapDistinctStateName.put("kerala","kerala#10#76.5");
			 * mapDistinctStateName.put("tripura","tripura#24#92");
			 * mapDistinctStateName.put("nagaland","nagaland#26#94.25");
			 * mapDistinctStateName.put("chhattisgarh","chhattisgarh#21.5#82");
			 * mapDistinctStateName
			 * .put("madhya pradesh","madhya pradesh#23.5#78.5");
			 * mapDistinctStateName.put("maharashtra","maharashtra#19.5#75");
			 * mapDistinctStateName
			 * .put("chandigarh","union territory of chandigarh#30.75#76.8");
			 * mapDistinctStateName.put("sikkim","sikkim#27.75#88.5");
			 * mapDistinctStateName.put("tamil nadu","tamil nadu#11#78");
			 * mapDistinctStateName.put("jharkhand","jharkhand#23.75#85.5");
			 * mapDistinctStateName.put("west bengal","west bengal#24#88");
			 * mapDistinctStateName
			 * .put("himachal pradesh","himachal pradesh#31.91667#77.25");
			 * mapDistinctStateName.put("gujarat","gujarat#23#72");
			 * mapDistinctStateName.put("andaman and nicobar",
			 * "union territory of andaman and nicobar islands#11.70065#92.67517"
			 * ); mapDistinctStateName.put("jammu and kashmir",
			 * "jammu and kashmir#33.91667#76.66667");
			 * mapDistinctStateName.put("arunachal pradesh"
			 * ,"arunachal pradesh#28#94.5");
			 * mapDistinctStateName.put("uttar pradesh"
			 * ,"uttar pradesh#27.25#80.75");
			 * mapDistinctStateName.put("mizoram","mizoram#23#93");
			 * mapDistinctStateName.put("dadra and nagar haveli",
			 * "union territory of dadra and nagar haveli#20.16667#73.03333");
			 * mapDistinctStateName.put("karnataka","karnataka#13.5#76");
			 * mapDistinctStateName.put("odisha","odisha#20.5#84.41667");
			 * mapDistinctStateName
			 * .put("puducherry","union territory of puducherry#11.93333#79.81667"
			 * ); mapDistinctStateName.put("haryana","haryana#29#76");
			 * mapDistinctStateName.put("assam","assam#26#93");
			 * mapDistinctStateName
			 * .put("lakshadweep","union territory of lakshadweep#10.9385#72.28043"
			 * ); mapDistinctStateName.put("punjab","punjab#30.91667#75.41667");
			 * mapDistinctStateName.put("bihar","bihar#25.75#85.75");
			 * mapDistinctStateName.put("meghalaya","meghalaya#25.5#91.33333");
			 * mapDistinctStateName
			 * .put("uttarakhand","uttarakhand#30.25#79.25");
			 * mapDistinctStateName
			 * .put("andhra pradesh","andhra pradesh#16#79");
			 */
			int cnt = 0;
			DBCollection coll = db1.getCollection(CollectionName); // may be
																	// main_news
			BasicDBObject query = new BasicDBObject(); // .append( "ebody",
														// java.util.regex.Pattern.compile(
														// crimKeyWord ) );
			// DBCursor cursor = null;
			// System.out.println("cityname.size:"+mapCityName.size() +
			// " state.size:"+mapStateName.size());
			String concOut = "";

			String[] skey = crimKeyWord.split(" ");
			List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
			int c = 0;
			while (c < skey.length) {
				obj.add(new BasicDBObject("ebody", java.util.regex.Pattern
						.compile(skey[c])));
				c++;
			}

			// read each line (get state-wise report)
			if (ReportType.equalsIgnoreCase("state-wise")) {
				System.out.println("processing..;mapDistinctStateName::"
						+ mapDistinctStateName);
				//
				for (String i : mapDistinctStateName.keySet()) {

					if (i.substring(0, 1).equalsIgnoreCase(" "))
						i = i.substring(1, i.length());

					// crimKeyWord = " "+crimKeyWord;
					currStateName = i.toLowerCase().replace("'", ""); // tailoring
																		// space
																		// consider
																		// later
					System.out.println("processing..;crimKeyWord:"
							+ crimKeyWord + ";currStateName:" + currStateName
							+ ";CollectionName:" + CollectionName + "-------");

					if (currStateName.equals("") || currStateName.equals(" ")
							|| currStateName == null
							|| mapIsProcessedState.containsKey(currStateName))
						continue;

					if (currStateName.length() <= 5)
						currStateName = " " + currStateName + " ";

					mapIsProcessedState.put(currStateName, "dummy");
					System.out.println("processing..City i:" + currStateName
							+ ";crimKeyWord:" + crimKeyWord + ";i:" + i + ";");
					obj.add(new BasicDBObject("ebody", java.util.regex.Pattern
							.compile(currStateName))); // city or location name
					// obj.add(new BasicDBObject("ebody",
					// java.util.regex.Pattern.compile( " "+mapStateName.get(i)
					// ) ));

					BasicDBObject andQuery = new BasicDBObject();
					andQuery.put("$and", obj);
					String latlong = "";
					DBCursor cursor = coll.find(andQuery);

					if (cursor.count() > 0) {

						cnt++;
						latlong = mapDistinctStateName.get(i).substring(
								mapDistinctStateName.get(i).indexOf("#"),
								mapDistinctStateName.get(i).length());
						latlong = latlong.substring(1, latlong.length());

						String[] s = latlong.split("#");
						System.out.println("Found > 0 City i :" + i
								+ ";currStateName:" + currStateName + ";"
								+ "cursor:" + cursor.count()
								+ " mapDistinctStateName.get(i) :"
								+ mapDistinctStateName.get(i) + " sub:"
								+ latlong + " s[0]:" + s[0] + " s[1]:" + s[1]);
						// {"name": "Manipur", "code": "Manipur", "city":
						// "Atlanta", "state": "GA", "lat": 25, "lon": 94,
						// "keyword": 44414121 }
						if (cnt == 1)
							concOut = "{\"name\": \"" + currStateName
									+ "\", \"code\": \"" + currStateName
									+ "\", \"city\": \"" + currStateName
									+ "\", \"state\": " + "\"" + currStateName
									+ "\", \"lat\":" + s[0] + ", \"lon\":"
									+ s[1] + ", \"count\":" + cursor.count()
									+ ", \"keyword\": \"" + crimKeyWord
									+ "\" }";
						else
							concOut = concOut + "," + "{\"name\": \""
									+ currStateName + "\", \"code\": \""
									+ currStateName + "\", \"city\": \""
									+ currStateName + "\", \"state\": " + "\""
									+ currStateName + "\", \"lat\":" + s[0]
									+ ", \"lon\":" + s[1] + ", \"count\":"
									+ cursor.count() + ", \"keyword\": \""
									+ crimKeyWord + "\" }";
						// crimKeyWord+"\t"+ currStateName + "\t"+
						// mapDistinctStateName.get(i) +"\t"+ cursor.count()+
						// "\n";

					}
					System.out.println(" cursor.count() = " + cursor.count()
							+ " obj:" + obj.size() + " 0:" + obj.get(0)
							+ " skey.length:" + skey.length); // +" 1:"+obj.get(1)
																// +" 2:"+obj.get(2)
					obj.remove(skey.length); // remove state name
				} // end of
				concOut = "{\"main\":{\"state\":[" + concOut + "]}}";
				System.out.println(" concOut:" + concOut + "\noutReportFile:"
						+ outReportFile);
				writer.append(concOut);
				// "\t"+ mapLatitude.get(i) + "\t" + mapLongitude.get(i) +
				// "\n");
				writer.flush();

				// isSleep
				if (isSleep == true) {
					System.out.println(" sleeping: " + sleepSeconds);
					Thread.sleep(sleepSeconds);
					System.out.println(" sleeping ends: " + sleepSeconds);
				}

			}

			// find-and-update-state (find a "state" in an element and update
			// its "state" column
			if (ReportType.equalsIgnoreCase("find-and-update-state")) {
				String updateS = "";

				for (String i : mapDistinctStateName.keySet()) {

					if (i.equals("") || i.equals(" ") || i == null)
						continue;

					if (i.substring(0, 1).equalsIgnoreCase(" "))
						i = i.substring(1, i.length());

					updateS = "{$set : { state:'" + i.replace("'", "")
							+ "'}  }";

					obj = new ArrayList<BasicDBObject>();
					obj.add(new BasicDBObject("ebody", java.util.regex.Pattern
							.compile(i)));
					obj.add(new BasicDBObject("eurl", java.util.regex.Pattern
							.compile(i))); // city or location name
					// obj.add(new BasicDBObject("ebody",
					// java.util.regex.Pattern.compile( " "+mapStateName.get(i)
					// ) ));

					BasicDBObject andQuery = new BasicDBObject();

					andQuery.put("$or", obj);

					DBCursor cursor = coll.find(andQuery);
					// update while

					if (cursor.hasNext()) {
						String url = cursor.next().get("eurl").toString();
						System.out.println("eurl:" + url + " updated city:" + i
								+ " ebody:" + "{ebody:'" + i + "'}"
								+ "; updateS:" + updateS);
						// friends.update("{eurl:'" + url + "'}").with(updateS);
					}

					System.out.println(" updated city:" + i + " ebody:"
							+ "{ebody:'" + i + "'}" + "; updateS:" + updateS);
				}

			}

			if (ReportType.equalsIgnoreCase("zone-wise")) {
				// read each line (zone-wise report)
				for (String i : mapCityName.keySet()) {

					crimKeyWord = " " + crimKeyWord;
					currCityName = " " + mapCityName.get(i).toLowerCase() + " ";

					System.out.println("3. crimKeyWord:" + crimKeyWord
							+ " ;City:" + currCityName + " ;State :"
							+ mapStateName.get(i) + " 	" + mapLatitude.get(i)
							+ " " + mapLongitude.get(i));

					/*
					 * //query.put("ebody", "$regex: /delhi/i " );
					 * query.put("ebody",
					 * java.util.regex.Pattern.compile(" "+mapCityName
					 * .get(i).toLowerCase()) );
					 * 
					 * cursor = coll.find(query);
					 * 
					 * 
					 * if(cursor.count()>0) System.out.println("2.City i :"+ i +
					 * " city:"+ mapCityName.get(i) + " "+" {ebody :  /"+
					 * mapCityName.get(i).toLowerCase() + "/" +" }"+
					 * "  "+" cnt: "+cnt + " cursor:"+cursor.count());
					 */
					// set the keywords and city names

					obj = new ArrayList<BasicDBObject>();
					obj.add(new BasicDBObject("ebody", java.util.regex.Pattern
							.compile(crimKeyWord)));
					// obj.add(new BasicDBObject("ebody",
					// java.util.regex.Pattern.compile( " "+mapStateName.get(i)
					// ) ));
					obj.add(new BasicDBObject("ebody", java.util.regex.Pattern
							.compile(currCityName))); // city or locaiton name
					BasicDBObject andQuery = new BasicDBObject();

					andQuery.put("$and", obj);

					DBCursor cursor = coll.find(andQuery);

					if (cursor.count() > 0) {
						System.out.println("3.City i :" + i + " city:"
								+ mapCityName.get(i) + " " + " {ebody :  /"
								+ mapCityName.get(i).toLowerCase() + "/" + " }"
								+ "  " + " cnt: " + cnt + " cursor:"
								+ cursor.count());

						writer.append(crimKeyWord + "\t" + mapCityName.get(i)
								+ "\t" + mapStateName.get(i) + "\t"
								+ cursor.count() + "\t" + mapLatitude.get(i)
								+ "\t" + mapLongitude.get(i) + "\n");
						writer.flush();
					}
					// coll.find( new
					// BasicDBObject(" {ebody : { $regex: 'delhi',$options:'i' }} ");

				}

			}// end if of zone-wise

			System.out.println("\n out file:" + outReportFile);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// SOURCE: http://dev.maxmind.com/geoip/legacy/geolite/
	// to be copied: Flag {'OnlyStateName' , 'OnlyCity' , 'OnlyZone' , 'All',
	// 'US' }
	// not sure about 'OnlyStateName' , 'OnlyCity' , 'OnlyZone' behaviour after
	// recent source data format used.
	public static HashMap<Integer, HashMap<String, String>> getCityName(
			String CityNamesFile, String outputCityFile,
			String outputDistinctState, String statePatternToSearch,
			String Flag, String delimiter) {
		String line = "";
		int count = 0;
		int lineNo = 0;
		HashMap<Integer, HashMap<String, String>> mapOut = new HashMap<Integer, HashMap<String, String>>();
		System.out.println(" getCityName : statePatternToSearch:"
				+ statePatternToSearch);
		String currState = "";
		HashMap<String, String> mapIdCityName = new HashMap();
		HashMap<String, String> mapIdStateName = new HashMap();
		HashMap<String, String> mapIdLatitude = new HashMap();
		HashMap<String, String> mapIdLongitutude = new HashMap();

		HashMap<String, String> mapDistinctStateName = new HashMap();
		HashMap<String, String> mapDistinctStateName2 = new HashMap();
		HashMap<String, Integer> mapIsPreNameStateName = new HashMap();
		HashMap<String, Integer> mapIsPreNameStateName2 = new HashMap();

		if (statePatternToSearch.equals(""))
			statePatternToSearch = "state of ";

		try {
			FileWriter writer = new FileWriter(outputCityFile);
			FileWriter writer2 = new FileWriter(outputDistinctState);
			BufferedReader reader = new BufferedReader(new FileReader(
					CityNamesFile));
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				line = line.toLowerCase().replace("state of ", "")
						.replace(" province", "").replace("province", "");
				// StringTokenizer stk = new StringTokenizer(line, "\t");
				lineNo++;
				String[] stk = line.split(delimiter);

				count = 0;
				if (line.equals(""))
					continue;
				System.out.println(line + ";token size:" + stk.length);
				// read each token
				// while ( count < stk.length) {
				// count++;
				int cint = 0;
				if (Flag.equalsIgnoreCase("onlystatename")
						|| Flag.equalsIgnoreCase("all")) {
					System.out.println("line.indexOf(statePatternToSearch):"
							+ line.indexOf(statePatternToSearch)
							+ " ;statePatternToSearch:" + statePatternToSearch);

					// find "state of" -> state pattern
					if (line.indexOf(statePatternToSearch) >= 0
							&& line.indexOf("embassy") == -1) {
						cint++;
						// int beginIndex = line.indexOf("state of ");
						// int endIndex = line.indexOf(",", beginIndex+3);

						currState = stk[2].replace(statePatternToSearch, "");
						System.out.println("currState:" + currState
								+ ";stk[2]:" + stk[2] + ";line:" + line);

						mapDistinctStateName.put(currState, stk[4] + "#"
								+ stk[5]);
						continue;
					}
				}

				System.out.println(" found from file state.size:"
						+ mapDistinctStateName.size());
				// if(count == 2){
				if (Flag.equalsIgnoreCase("all")) {
					System.out.println(stk.length + "#" + line);
					if (stk.length < 6)
						continue;
					System.out.println(" city:" + stk[3] + " currState:"
							+ currState);

					mapIdCityName.put(Integer.toString(lineNo), stk[3]);
					mapIdStateName.put(Integer.toString(lineNo), stk[2]);
					mapIdLatitude.put(Integer.toString(lineNo), stk[5]);
					mapIdLongitutude.put(Integer.toString(lineNo), stk[6]);

					writer.append(stk[1] + "\t" + stk[2] + "\t" + stk[3] + "\t"
							+ stk[5] + "\t" + stk[6] + "\n");
					writer.flush();
				} else {
					System.out.println(stk.length + "#" + line);
					if (stk.length < 6
							|| !stk[1].replace("\"", "").toLowerCase()
									.equalsIgnoreCase(Flag))
						continue;
					System.out.println(" city:" + stk[3] + " currState:"
							+ currState);

					mapIdCityName.put(Integer.toString(lineNo), stk[3]);
					mapIdStateName.put(Integer.toString(lineNo), stk[2]);
					mapIdLatitude.put(Integer.toString(lineNo), stk[5]);
					mapIdLongitutude.put(Integer.toString(lineNo), stk[6]);

					writer.append(stk[1] + "\t" + stk[2] + "\t" + stk[3] + "\t"
							+ stk[5] + "\t" + stk[6] + "\n");
					writer.flush();
				}

				// }
				// else
				// continue;

				// }

			} // end of while loop
			String preStateName2 = "";
			String preStateName = "";
			int icnt = 0;
			int beginIndex = 0;
			int beginIndex2 = 0;
			//
			for (String k : mapDistinctStateName.keySet()) {
				icnt++;
				beginIndex = k.indexOf(" ");

				if (beginIndex >= 0)
					preStateName = k.substring(0, beginIndex + 1);
				else {

					/*
					 * if(!mapIsPreNameStateName.containsKey(k) )
					 * mapIsPreNameStateName.put( k , 1 ); else{ int c =
					 * mapIsPreNameStateName.get(k); mapIsPreNameStateName.put(
					 * k , c++ ); }
					 */

					continue;
				}
				//
				beginIndex2 = k.indexOf(" ", beginIndex + 1);
				if (beginIndex2 >= 0) {
					preStateName2 = k.substring(0, beginIndex2 + 1);
				}

				// if(beginIndex2 >=0)

				if (mapIsPreNameStateName.containsKey(preStateName)) {
					int c = mapIsPreNameStateName.get(preStateName) + 1;
					mapIsPreNameStateName.put(preStateName, c);
					System.out.println("if curr preStateName:" + preStateName
							+ " c:" + c);
					if (beginIndex2 >= 0
							&& !mapIsPreNameStateName2
									.containsKey(preStateName2)) {
						mapIsPreNameStateName2.put(preStateName2, 1);
					} else if (beginIndex2 >= 0
							&& mapIsPreNameStateName2
									.containsKey(preStateName2)) {
						int c2 = mapIsPreNameStateName2.get(preStateName2) + 1;
						mapIsPreNameStateName2.put(preStateName2, c2);
					}

				} else {
					mapIsPreNameStateName.put(preStateName, 1);
					System.out.println("else curr preStateName:" + preStateName
							+ " 1:" + 1);
					if (beginIndex2 >= 0) {
						mapIsPreNameStateName2.put(preStateName2, 1);
					}
				}

			}
			System.out.println(" mapIsPreNameStateName:"
					+ mapIsPreNameStateName.size() + " mapIsPreNameStateName:"
					+ mapIsPreNameStateName + " mapIsPreNameStateName.size:"
					+ mapIsPreNameStateName2.size()
					+ " mapIsPreNameStateName2:" + mapIsPreNameStateName2
					+ " mapDistinctStateName:" + mapDistinctStateName.size()
					+ "  " + mapDistinctStateName);
			// same value
			boolean isFoundRepeatPreStateName = false, isFoundRepeatPreStateName2 = false;
			beginIndex = 0;
			boolean useFirstPre = false;
			boolean useSecondPre = false;
			//
			if (mapIsPreNameStateName.size() > mapIsPreNameStateName2.size())
				useFirstPre = true;
			else
				useSecondPre = true;

			HashMap<String, String> mapDistinctStateNameFinal = new HashMap<String, String>();
			int cnt = 0;
			// each mapDistinctStateName
			for (String k : mapDistinctStateName.keySet()) {
				cnt = 0;
				if (useFirstPre == true) {
					for (String ff : mapIsPreNameStateName.keySet()) {
						cnt++;
						System.out.println("k:" + k + " ff:" + ff
								+ " k.indexOf(ff):" + k.indexOf(ff)
								+ "  mapIsPreNameStateName.get(ff):"
								+ mapIsPreNameStateName.get(ff)
								+ " k.replace(ff, ):" + k.replace(ff, ""));
						if (k.indexOf(ff) >= 0
								&& mapIsPreNameStateName.get(ff) > 1) {
							System.out.println("adding k.replace(ff, ):"
									+ k.replace(ff, ""));
							mapDistinctStateNameFinal.put(k.replace(ff, ""), k
									+ "#" + mapDistinctStateName.get(k));
							break;
						}

						if (cnt == mapIsPreNameStateName.size())
							mapDistinctStateNameFinal.put(k, k + "#"
									+ mapDistinctStateName.get(k));
					}
				} // end of if<>

				if (useSecondPre == true) {
					for (String ff : mapIsPreNameStateName2.keySet()) {
						cnt++;
						System.out.println("k:" + k + " ff:" + ff
								+ " k.indexOf(ff):" + k.indexOf(ff)
								+ "  mapIsPreNameStateName2.get(ff):"
								+ mapIsPreNameStateName.get(ff)
								+ " k.replace(ff, ):" + k.replace(ff, ""));
						if (k.indexOf(ff) >= 0
								&& mapIsPreNameStateName2.get(ff) > 1) {
							System.out.println("adding k.replace(ff, ):"
									+ k.replace(ff, ""));
							mapDistinctStateNameFinal.put(k.replace(ff, ""), k
									+ "#" + mapDistinctStateName.get(k));
							break;
						}

						if (cnt == mapIsPreNameStateName2.size())
							mapDistinctStateNameFinal.put(k, k + "#"
									+ mapDistinctStateName.get(k));

					}
				} // end of if<>

			}

			System.out.println("isFoundRepeatPreStateName2:"
					+ isFoundRepeatPreStateName2
					+ " isFoundRepeatPreStateName:" + isFoundRepeatPreStateName
					+ "\n mapDistinctStateName:" + mapDistinctStateName
					+ "\n mapIsPreNameStateName.size:"
					+ mapIsPreNameStateName.size()
					+ "\n mapIsPreNameStateName:" + mapIsPreNameStateName
					+ "\n mapIsPreNameStateName2.size:"
					+ mapIsPreNameStateName2.size()
					+ "\n mapIsPreNameStateName2:" + mapIsPreNameStateName2
					+ "\n mapDistinctStateName2:" + mapDistinctStateName2
					+ "\n mapDistinctStateNameFinal:"
					+ mapDistinctStateNameFinal);
			if ((useSecondPre == true || useFirstPre == true)
					&& mapDistinctStateNameFinal.size() > 1) {
				System.out.println("1.mapDistinctStateNameFinal:"
						+ mapDistinctStateNameFinal);
				mapOut.put(5, mapDistinctStateNameFinal);
				// write to file - province name
				for (String hk : mapDistinctStateNameFinal.keySet()) {
					writer2.append(hk + "#" + mapDistinctStateNameFinal.get(hk)
							+ "\n");
					writer2.flush();
				}

			} else {
				System.out.println("3.mapDistinctStateName:"
						+ mapDistinctStateName);
				mapOut.put(5, mapDistinctStateName);
				// write to file - province name
				for (String hk : mapDistinctStateName.keySet()) {
					writer2.append(hk + "#" + hk + "\n");
					writer2.flush();
				}
			}

			mapOut.put(1, mapIdCityName);
			mapOut.put(2, mapIdStateName);
			mapOut.put(3, mapIdLatitude);
			mapOut.put(4, mapIdLongitutude);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapOut;
	}

	//
	public static void test() {
		try {
			System.out.println("test...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// read configuration file
	public static TreeMap<String, String> getConfig(String inConfigFileName) {
		TreeMap<String, String> mapConfig = new TreeMap();
		String line = "";
		System.out.println("getConfig():inConfigFileName=");
		System.out.println(inConfigFileName);
		System.out.println("-----------------------");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					inConfigFileName));
			System.out.println("\nReading config file...\n");
			// read each line of given file
			while ((line = reader.readLine()) != null) {
				String[] s = line.split("=");
				System.out.println("line:" + line);
				try {
					if (s.length < 2)
						continue;
					mapConfig.put(s[0], s[1]);
				} catch (Exception e) {
					System.out
							.println("EACH LINE OF CONFIG FILE SHOULD HAVE TWO TOKENS");
					e.printStackTrace();
				}
			}
			System.out.println("getConfig:" + mapConfig);
			System.out.println("\n-----------getConfig end----------");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapConfig;
	}

	// read_input_from_command_line_config_file_name
	public static String read_input_from_command_line_to_continue_Yes_No(
																		BufferedReader br) {
		String option = "";
		System.out.print("Enter Y (or) N to continue..");
		System.out.print("\n");

		// read the username from the command-line; need to use try/catch with
		// the
		// readLine() method
		try {
			option = br.readLine();
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your option!");
			System.exit(1);
		}

		return option;
	}

	// read a config file
	private static String read_input_from_command_line_config_file_name(
			BufferedReader br, TreeMap<String, String> mapConfig) {
		String option = null;
		String confFile = "";
		try {

			System.out.println("Reading Given Configuration...:");
			if (mapConfig != null) {
				for (String i : mapConfig.keySet()) {
					System.out.println(i + " " + mapConfig.get(i));
				}
			} else {
				System.out.println("\n ! empty...:");
			}

			try {
				System.out.print("\n Want to enter new config file? (y/n)");
				option = br.readLine();
			} catch (IOException ioe) {
				System.out.println("IO error trying to read your option!");
				System.exit(1);
			}

			if (option.equalsIgnoreCase("y")) {
				try {
					System.out
							.print("\nEnter configuration file (if wish to overwrite above one):");
					confFile = br.readLine();
				} catch (IOException ioe) {
					System.out.println("IO error trying to read your option!");
					System.exit(1);
				}
			}

		} // try
		catch (Exception e) {
			e.printStackTrace();
		}
		return confFile;
	}

	//
	// read command
	private static TreeMap<Integer, String> read_input_from_Command_Line(
			String confFile, BufferedReader br, String inFolder,
			TreeMap<String, String> mapConfig) {

		System.out.println("Given Configuration:");
		for (String i : mapConfig.keySet()) {
			System.out.println(i + " " + mapConfig.get(i));
		}

		// TODO Auto-generated method stub
		TreeMap<Integer, String> mapOut = new TreeMap<Integer, String>();
		System.out.print("\nBelow configuration file:");
		System.out.print("\n" + confFile);
		String option = "";
		System.out
				.print("\nAfter checking command line argument, below configuration file:");
		System.out.print("\n" + confFile);
		System.out.print("\nGiven input Folder below:" + "\n" + inFolder);
		System.out
				.print("\nWish to overwrite above configuration file (y/n)?:");

		try {
			option = br.readLine();
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your option!");
			System.exit(1);
		}

		if (option.equalsIgnoreCase("y")) {
			//

			try {
				System.out
						.print("\nEnter configuration file (if wish to overwrite above one):");
				confFile = br.readLine();
			} catch (IOException ioe) {
				System.out.println("IO error trying to read your option!");
				System.exit(1);
			}

			System.out.println("\nGiven input Folder:" + inFolder);
			// output folder
			try {
				System.out.print("\nWish to overwrite input Folder (y/n)?:");
				try {
					option = br.readLine();
				} catch (IOException ioe) {
					System.out.println("IO error trying to read your option!");
					System.exit(1);
				}
				// get out folder
				if (option.equalsIgnoreCase("y")) {
					System.out.print("\nEnter input Folder:");
					inFolder = br.readLine();
				}

			} catch (IOException ioe) {
				System.out
						.println("IO error trying to read your option outFolder!");
				System.exit(1);
			}

			mapOut.put(1, confFile);
			mapOut.put(3, inFolder);

			System.out.print("\nConfig File:" + mapOut.get(1));
			System.out.print("\nInput Folder:" + mapOut.get(3));

		} else {
			try {
				System.out.print("\nFinally, OK to continue..(y/n)");
				option = br.readLine();
			} catch (IOException ioe) {
				System.out.println("IO error trying to read your option!");
				System.exit(1);
			}
			mapOut.put(2, option);
		}

		return mapOut;
	}

    //
    public static TreeMap<Integer,TreeMap<String, TreeMap<Integer, String>>> create_intermediate_file(
    																	int    lineNumber_fromSourceEdgeDestMappings,
    																	String inputFile,
						                                                String outFile_intermediate,
						                                                TreeMap<Integer,String> mapSourceVertex,
						                                                TreeMap<Integer,String> mapDestVertex,
						                                                TreeMap<Integer,String> mapEdgeLabel,
						                                                TreeMap<Integer,String> edgeLabel_4_ChainNode,
						                                                TreeMap<Integer,String> map_dest2_4_ChainNode,
						                                                int    ID_column,
						                                                String START_edgeLabel,
						                                                String END_edgeLabel,
						                                                String OTHER_edgeLabel,
						                                                String delimiter_4_inputDataFileToCreateGBADGraph,
						                                                boolean is_header_present,
						                                                boolean is_SOPprint
    		){
        TreeMap<String,  String> map_ID_N_Destination2StringCSV=new TreeMap<String,String>();
        TreeMap<String,  String> map_ID_N_Destination2StringCSV_lineNoCSV=new TreeMap<String,String>();
        TreeMap<String,  String> map_ID_N_Destination2EdgeLabels_CSV=new TreeMap<String,String>();

        TreeMap<Integer,TreeMap<String, TreeMap<Integer, String>>> mapOUTPUT_ID_N_Source_EdgeLabel_Destination=
        													new TreeMap<Integer, TreeMap<String, TreeMap<Integer, String>>>();
        
        TreeMap<String, TreeMap<Integer, String>> mapOut_ID_N_Source_EdgeLabel_Destination_MAIN=new TreeMap<String, TreeMap<Integer, String>>();
        TreeMap<String, TreeMap<Integer, String>> mapOut_ID_N_Source_EdgeLabel_Destination_ONE=new TreeMap<String, TreeMap<Integer, String>>();
        TreeMap<String, TreeMap<Integer, String>> mapOut_ID_N_Source_EdgeLabel_Destination_ZERO=new TreeMap<String, TreeMap<Integer, String>>();
        try{
            boolean is_START_edgeLabel_integer=IsInteger.isInteger(START_edgeLabel);
            boolean is_END_edgeLabel_integer=IsInteger.isInteger(END_edgeLabel);
            boolean is_OTHER_edgeLabel_integer=IsInteger.isInteger(OTHER_edgeLabel);
            TreeMap<String, Integer> map_ID_lineNo=new TreeMap<String, Integer>();
            TreeMap<Integer, String> map_lineNo_Dest2FromlineNumber_fromSourceEdgeDestMappings=new TreeMap<Integer, String>();
            TreeMap<String, Integer> map_lineNo_ID=new TreeMap<String, Integer>();
            String [] arr_edgeLabel_4_ChainNode=new String[2];
            String [] arr_map_dest2_4_ChainNode=new String[2];

            //THIS METHOD (1) replace , with " " (2) then, replace " " with " AND " *****" AND " is the delimiter
            Map<Integer, String> mapLines =  convert.Convert_For_GBAD.convert_readCSVFileforConvertForGBADFn(
                                                                        inputFile,
                                                                        -1,
                                                                        -1,
                                                                         delimiter_4_inputDataFileToCreateGBADGraph,
                                                                         is_header_present
                                                                        );


   /*         ---------------------------------
            Given graphTopology: s1.graphtopology=1->(3)2,3->(2)4{[ID.1]#[START.edgeLabel]#[OTHER.edgeLabel]#[has].1**[has].2#[END.edgeLabel]}
            1:<lineNo,Source>:{1=1, 2=3}
            2:<lineNo,Destination>:{1=2, 2=4}
            4:(lineNo,edge):{1=(3), 2=(2)}
            3:(distinct New_VertexID-Old_VertexID):{1=1, 2=2, 3=3, 4=4}
            5:<lineNo,edgeLabel_4_ChainNode>:{2=[has]**[has]}
            6:<lineNo,dest2_4_ChainNode>:{2=1**2}
            7:<lineNo,mapID(KEY(lineNumber), ID as VALUE)>:{2=2}
            8:<lineNo,mapSTART(KEY(lineNumber), START as VALUE)>:{2=START}
            9:<lineNo,mapEND(KEY(lineNumber), END as VALUE)>:{2=END}
            10:<lineNo,mapOTHER(KEY(lineNumber), OTHER as VALUE)>:{2=OTHER}
            ---------------------------------*/

            String tree_sourceNode_column=""; boolean is_tree_sourceNode_column_INTEGER=false;
            String tree_destination_Node_column_toBe_Chained_from_sourceNode="";
            
            TreeMap<Integer, String> map_lineNo_edgeLabelZERO_4_ChainNode=new TreeMap<Integer, String>();
            TreeMap<Integer, String> map_lineNo_map_dest2ZERO_4_ChainNode=new TreeMap<Integer, String>();
            TreeMap<Integer, String> map_lineNo_edgeLabelONE_4_ChainNode=new TreeMap<Integer, String>();
            TreeMap<Integer, String> map_lineNo_map_dest2ONE_4_ChainNode=new TreeMap<Integer, String>();
            
            int lineNo_having_edgesANDnodes_toConnectToChainOfDestination2Nodes = -1;
            try{
	            // get set of <source,edge,destination> for each of dest2_4_ChainNode and edgeLabel_4_ChainNode
	            // get this by iterating mapSource, get source , destination ( neglect edge as it is not useful to get from here for chain of nodes)
	            for(int lineNo:mapSourceVertex.keySet()){
	            	 
	                //does exist in dest2_4_ChainNode and edgeLabel_4_ChainNode
	                if(map_dest2_4_ChainNode.containsKey(lineNo) &&  edgeLabel_4_ChainNode.containsKey(lineNo)){
	                    tree_sourceNode_column=mapSourceVertex.get(lineNo).replace("(","").replace(")", "");
	                    tree_destination_Node_column_toBe_Chained_from_sourceNode=mapDestVertex.get(lineNo);
	                    lineNo_having_edgesANDnodes_toConnectToChainOfDestination2Nodes=lineNo;
	                    
	                    // get the inner edge-destNode2 pair which needs to be attached to each of the destination chain nodes 
		                arr_edgeLabel_4_ChainNode=edgeLabel_4_ChainNode.get(lineNo).split("\\*\\*");
		                arr_map_dest2_4_ChainNode=map_dest2_4_ChainNode.get(lineNo).split("\\*\\*");
	                    
	                    System.out.println("-------> Found pair:"+tree_sourceNode_column+","+tree_destination_Node_column_toBe_Chained_from_sourceNode
	                    					+" arr_1:"+arr_edgeLabel_4_ChainNode[0]+" arr_2:"+arr_map_dest2_4_ChainNode[0]);
	                }
	            }
            }
            catch(Exception e){
            	e.printStackTrace();
            }
            
            int arr_edgeLabel_4_ChainNode_ZERO=-1;int arr_map_dest2_4_ChainNode_ZERO=-1;
            int arr_edgeLabel_4_ChainNode_ONE=-1;int arr_map_dest2_4_ChainNode_ONE=-1;
            boolean arr_edgeLabel_4_ChainNode_ZERO_isINTEGER=false;
            boolean arr_map_dest2_4_ChainNode_ZERO_isINTEGER=false;
            boolean arr_edgeLabel_4_ChainNode_ONE_isINTEGER=false;
            boolean arr_map_dest2_4_ChainNode_ONE_isINTEGER=false;
            
            if(arr_edgeLabel_4_ChainNode.length>=1){
            	if(arr_edgeLabel_4_ChainNode[0]!=null)
            		arr_edgeLabel_4_ChainNode[0]=arr_edgeLabel_4_ChainNode[0].replace("[", "").replace("]", "").replace("(", "").replace(")", "");
            }
            if(arr_edgeLabel_4_ChainNode.length>=2){
            	if(arr_edgeLabel_4_ChainNode[1]!=null)
            		arr_edgeLabel_4_ChainNode[1]=arr_edgeLabel_4_ChainNode[1].replace("[", "").replace("]", "").replace("(", "").replace(")", "");
            }
            if(arr_edgeLabel_4_ChainNode.length>=1){
            	if(arr_edgeLabel_4_ChainNode[0]!=null)
            		arr_map_dest2_4_ChainNode[0]=arr_map_dest2_4_ChainNode[0].replace("[", "").replace("]", "").replace("(", "").replace(")", "");
            }
            if(arr_edgeLabel_4_ChainNode.length>=2){
            	if(arr_edgeLabel_4_ChainNode[1]!=null)
            		arr_map_dest2_4_ChainNode[1]=arr_map_dest2_4_ChainNode[1].replace("[", "").replace("]", "").replace("(", "").replace(")", "");
            }
            
            ///are they integers??? meaning columns in the file and NOT hard-coded
            if(arr_edgeLabel_4_ChainNode.length>=1 && arr_edgeLabel_4_ChainNode[0]!=null){
	            if(IsInteger.isInteger(arr_edgeLabel_4_ChainNode[0])){
	            	arr_edgeLabel_4_ChainNode_ZERO=Integer.valueOf(arr_edgeLabel_4_ChainNode[0]);
	            	arr_edgeLabel_4_ChainNode_ZERO_isINTEGER=true;
	            }
            }
            if(arr_edgeLabel_4_ChainNode.length>=2){
            	System.out.println(" arr_edgeLabel_4_ChainNode.len:"+arr_edgeLabel_4_ChainNode.length+" "+arr_edgeLabel_4_ChainNode[0]);
            	if(arr_edgeLabel_4_ChainNode[0]!=null){
		            if(IsInteger.isInteger(arr_edgeLabel_4_ChainNode[1])  ){
		            	arr_edgeLabel_4_ChainNode_ONE=Integer.valueOf(arr_edgeLabel_4_ChainNode[1]);
		            	arr_edgeLabel_4_ChainNode_ONE_isINTEGER=true;
		            }
            	}
            }
            if(arr_edgeLabel_4_ChainNode.length>=1 && arr_edgeLabel_4_ChainNode[0]!=null){
	            if(IsInteger.isInteger(arr_map_dest2_4_ChainNode[0])){
	            	arr_map_dest2_4_ChainNode_ZERO=Integer.valueOf(arr_map_dest2_4_ChainNode[0]);
	            	arr_map_dest2_4_ChainNode_ZERO_isINTEGER=true;
	            }
            }
            if(arr_edgeLabel_4_ChainNode.length>=2){
            	if(arr_edgeLabel_4_ChainNode[0]!=null){
		            if(IsInteger.isInteger(arr_map_dest2_4_ChainNode[1]) ){
		            	arr_map_dest2_4_ChainNode_ONE=Integer.valueOf(arr_map_dest2_4_ChainNode[1]);
		            	arr_map_dest2_4_ChainNode_ONE_isINTEGER=true;
		            }
            	}
            }
            if(is_SOPprint){
	            System.out.println("***convert_INT:arr_edgeLabel_4_ChainNode_ZERO:"+arr_edgeLabel_4_ChainNode_ZERO+" arr_edgeLabel_4_ChainNode_ONE:"+arr_edgeLabel_4_ChainNode_ONE
	            				 +" arr_map_dest2_4_ChainNode_ZERO:"+arr_map_dest2_4_ChainNode_ZERO+" arr_map_dest2_4_ChainNode_ONE:"+arr_map_dest2_4_ChainNode_ONE
	            					);
	            System.out.println("***is_INT:"+arr_edgeLabel_4_ChainNode_ZERO_isINTEGER+" "+arr_edgeLabel_4_ChainNode_ONE_isINTEGER
	            						+" "+arr_map_dest2_4_ChainNode_ZERO_isINTEGER+" "+arr_map_dest2_4_ChainNode_ONE_isINTEGER);
            }
            //
            if(IsInteger.isInteger(tree_sourceNode_column))
            	is_tree_sourceNode_column_INTEGER=true; //given source node is actually an ID
            // This given source "tree_sourceNode_column" is actually an column and not a fixed hard-coded value?
            // there can be multiple (distinct) value in the input file per ID (primary key), so just pick the first line hit..	
            
            TreeMap<String,String> map_ID_sourceNode=new TreeMap<String, String>();
            String [] arr_node_edge_pairs_toConnectToNodesInChain=new String[1];
            //
            int tree_destination_Node_column_toBe_Chained_from_sourceNode_INTEGER=
                                                                Integer.valueOf(tree_destination_Node_column_toBe_Chained_from_sourceNode);
            String currID="";
            
            int dest2_EdgeLabelColumnNumber=Integer.valueOf(mapDestVertex.get(lineNumber_fromSourceEdgeDestMappings));
            System.out.println("dest2_EdgeLabelColumnNumber:"+dest2_EdgeLabelColumnNumber
            						+" lineNumber_fromSourceEdgeDestMappings:"+lineNumber_fromSourceEdgeDestMappings+" mapDestVertex:"+mapDestVertex);
            
            // get CSV of nodes of destination2 ()
            for(int lineNo:mapLines.keySet()){
                System.out.println("");
                String [] token = mapLines.get(lineNo).replace(","," AND ").split(" AND ");
                currID=token[ID_column-1];
                map_ID_lineNo.put(currID, lineNo);
                
                // when DUMMY is mentioned in the chain for START,END,OTHER edgelabel, then only use this,
                map_lineNo_Dest2FromlineNumber_fromSourceEdgeDestMappings.put(lineNo, token[dest2_EdgeLabelColumnNumber-1] );
                
                int counter=0;
                String temp_CSV = "";
                String temp_lineNoCSV = "";
                String temp_destination2CSV = "";
                
                if(is_SOPprint)
                	System.out.println("mapLines.get(lineNo):"+mapLines.get(lineNo));
                while(counter<token.length) {
                	String new_token= token[tree_destination_Node_column_toBe_Chained_from_sourceNode_INTEGER - 1];
                	String new_token_lineNo= String.valueOf(lineNo);
                	// for each ID(primaryKEY), get the first hit of source node (given source node is INTEGER and NOT hard-coded node)
                	if(is_tree_sourceNode_column_INTEGER){// given source NODE is integer (a column in the input file)
		                	if(Integer.valueOf(tree_sourceNode_column)-1 == counter){
		                		map_ID_sourceNode.put(currID, token[Integer.valueOf(tree_sourceNode_column)-1]);
		                	}
                	}
                	else{ // given source NODE is HARD-CODED (not a column value in the input file)
                		map_ID_sourceNode.put(currID, tree_sourceNode_column);
                	}
                	if(!map_ID_N_Destination2EdgeLabels_CSV.containsKey(currID))
                		map_ID_N_Destination2EdgeLabels_CSV.put(currID, String.valueOf(token[dest2_EdgeLabelColumnNumber-1]) );
                	else{
                		String t=map_ID_N_Destination2EdgeLabels_CSV.get(currID);
                		map_ID_N_Destination2EdgeLabels_CSV.put(currID, String.valueOf(token[dest2_EdgeLabelColumnNumber-1]) +"#"+t  );
                	}
                    
                    //
                    if (	!map_ID_N_Destination2StringCSV.containsKey(currID) && 
                    		tree_destination_Node_column_toBe_Chained_from_sourceNode_INTEGER-1 == counter) {
                            map_ID_N_Destination2StringCSV.put(currID, new_token);
                            map_ID_N_Destination2StringCSV_lineNoCSV.put(currID, String.valueOf(lineNo) );
                             
                            
//                            System.out.println("1.map_ID_N_Destination2StringCSV:"+map_ID_N_Destination2StringCSV);
                        } else if ( map_ID_N_Destination2StringCSV.containsKey(currID) &&
                        			tree_destination_Node_column_toBe_Chained_from_sourceNode_INTEGER-1 == counter )  {
                        	
                            if (!map_ID_N_Destination2StringCSV.containsKey(currID) ){
                            	temp_CSV = new_token;
                            	temp_lineNoCSV = String.valueOf(lineNo);
                            	if(is_SOPprint)
                            	System.out.println("1.entry:"+temp_CSV);
                            	}
                            else if(map_ID_N_Destination2StringCSV.containsKey(currID)){
                            	temp_CSV = map_ID_N_Destination2StringCSV.get(currID)+"#"+new_token ;
                            	temp_lineNoCSV = map_ID_N_Destination2StringCSV_lineNoCSV.get(currID)+"#"+new_token_lineNo ;
                            	if(is_SOPprint)
                            		System.out.println("2.entry:"+temp_CSV);
                                }
                            map_ID_N_Destination2StringCSV.put(currID, temp_CSV);
                            map_ID_N_Destination2StringCSV_lineNoCSV.put(currID, temp_lineNoCSV);
//                            System.out.println("2.map_ID_N_Destination2StringCSV:"+map_ID_N_Destination2StringCSV);
                        }
                    
                    //**** get the inner tree edge-dest2Node to be added ...save them to set of maps..
                    if(arr_edgeLabel_4_ChainNode_ZERO_isINTEGER){ ///<--- edgeLabel (inner to be connected to chain dest nodes)
                    	map_lineNo_edgeLabelZERO_4_ChainNode.put(lineNo, token[arr_edgeLabel_4_ChainNode_ZERO-1]);
                    }
                    else{
                    	if(arr_edgeLabel_4_ChainNode.length>=1)
                    		map_lineNo_edgeLabelZERO_4_ChainNode.put(lineNo, "["+arr_edgeLabel_4_ChainNode[0]+"]");
                    } ////////////////////////////////////
                    if(arr_edgeLabel_4_ChainNode_ONE_isINTEGER){
                    	map_lineNo_edgeLabelONE_4_ChainNode.put(lineNo, token[arr_edgeLabel_4_ChainNode_ONE-1]);
                    }
                    else{
                    	if(arr_edgeLabel_4_ChainNode.length>=2)
                    		map_lineNo_edgeLabelONE_4_ChainNode.put(lineNo, "["+arr_edgeLabel_4_ChainNode[1]+"]");
                    }////////////////////////////////////
                    if(arr_map_dest2_4_ChainNode_ZERO_isINTEGER){///<--- dest2 (inner to be connected to chain dest nodes)
                    	map_lineNo_map_dest2ZERO_4_ChainNode.put(lineNo, token[arr_map_dest2_4_ChainNode_ZERO-1]);
                    }
                    else{
                    	if(arr_edgeLabel_4_ChainNode.length>=1)
                    		map_lineNo_map_dest2ZERO_4_ChainNode.put(lineNo, "["+arr_map_dest2_4_ChainNode[0]+"]");
                    }////////////////////////////////////
                    if(arr_map_dest2_4_ChainNode_ONE_isINTEGER){
                    	map_lineNo_map_dest2ONE_4_ChainNode.put(lineNo, token[arr_map_dest2_4_ChainNode_ONE-1]);
                    }
                    else{
                    	if(arr_edgeLabel_4_ChainNode.length>=2)
                    		map_lineNo_map_dest2ONE_4_ChainNode.put(lineNo, "["+arr_map_dest2_4_ChainNode[1]+"]");
                    }
                    counter++;
                }
            }

//             System.out.println("tree_destination_Node_column_toBe_Chained_from_sourceNode_INTEGER:"+tree_destination_Node_column_toBe_Chained_from_sourceNode_INTEGER
//            					+" ID_column:"+ID_column);
//            System.out.println("<----------------->map_ID_N_Destination2StringCSV:"+map_ID_N_Destination2StringCSV);
//            System.out.println("<----------------->map_ID_N_Destination2StringCSV_lineNoCSV:"+map_ID_N_Destination2StringCSV_lineNoCSV);
//            System.out.println("<------->map_ID_sourceNode:"+map_ID_sourceNode);
//            System.out.println("<------->map_lineNo_map_dest2ZERO_4_ChainNode:"+map_lineNo_map_dest2ZERO_4_ChainNode);
//            System.out.println("<------->map_lineNo_map_dest2ONE_4_ChainNode:"+map_lineNo_map_dest2ONE_4_ChainNode);
//            System.out.println("<------->map_lineNo_edgeLabelZERO_4_ChainNode:"+map_lineNo_edgeLabelZERO_4_ChainNode);
//            System.out.println("<------->map_lineNo_edgeLabelONE_4_ChainNode:"+map_lineNo_edgeLabelONE_4_ChainNode);
            System.out.println("<------map_lineNo_Dest2FromlineNumber_fromSourceEdgeDestMappings:"+map_lineNo_Dest2FromlineNumber_fromSourceEdgeDestMappings);
            System.out.println("------->map_ID_N_Destination2EdgeLabels_CSV:"+map_ID_N_Destination2EdgeLabels_CSV);
            //System.out.println(" mapLines:"+mapLines);

            // ----------------> write to a file
            //////////////////////////// sample to be output ////////////////////////// 
            ////// BELOW SET 1 is about source, edge, and destination and lineNo_destination
            // <ID_1,<source!!!START!!!Destination2_1!!!LineNo_of_Destination2_1>>
            // <ID_1,<Destination2_1!!!OTHER!!!Destination2_2!!!LineNo_of_Destination2_2>>
            // <ID_1,<Destination2_2!!!END!!!Destination2_3!!!LineNo_of_Destination2_3>>
            ////// BELOW SET 2 is about further drill-down in each of node in destination chain, where in each node we add 1/2 edge-dest2Node pairs.
            //<ID_1,<Destination2_1!!![hardCodeEdgeLabel]!!!<NODE1_from_mCOLUMNinFile>>
            //<ID_1,<Destination2_1!!![hardCodeEdgeLabel]!!!<NODE2_from_nCOLUMNinFile>>
            //<ID_1,<Destination2_2!!![hardCodeEdgeLabel]!!!<NODE1_from_mCOLUMNinFile>>
            //<ID_1,<Destination2_2!!![hardCodeEdgeLabel]!!!<NODE2_from_nCOLUMNinFile>>
            //<ID_1,<Destination2_3!!![hardCodeEdgeLabel]!!!<NODE1_from_mCOLUMNinFile>>
            //<ID_1,<Destination2_3!!![hardCodeEdgeLabel]!!!<NODE2_from_nCOLUMNinFile>>
            
            int counter=0;
            TreeMap<String, TreeMap<String,String>> map_ID_N_Set1asKEY=new TreeMap<String, TreeMap<String,String>>();
            ////////// BELOW SET 1 is about source, edge, and destination
             // iterate each of ID (primary key)
            for(String currID2:map_ID_N_Destination2StringCSV.keySet()){
            	TreeMap<String,String> map_temp_currID_SET1=new TreeMap<String, String>();
            	String mainSource=map_ID_sourceNode.get(currID2);
            	 
            	String [] main_Destination_Chain=map_ID_N_Destination2StringCSV.get(currID2).split("#");
            	String [] main_Destination_Chain_lineNo=map_ID_N_Destination2StringCSV_lineNoCSV.get(currID2).split("#");
            	String [] main_Destination2EdgeLabels_CSV=map_ID_N_Destination2EdgeLabels_CSV.get(currID2).split("#");
            	int counter3=0;
            	String next_sourceNode="";
//            		System.out.println("counter3+1:"+(counter3+1)+" main_Destination_Chain.length:"+main_Destination_Chain.length);
            		//
            		if(main_Destination_Chain.length>=3){
            			
            			while(counter3<main_Destination_Chain.length){
		            		if(counter3+1==1){
		            			String t="";
		            			
		            			if(!START_edgeLabel.equalsIgnoreCase("DUMMY"))
		            				t=mainSource+"!!!"+START_edgeLabel+"!!!"+main_Destination_Chain[counter3]+"^^^"+counter3+"^^^"+"!!!"+main_Destination_Chain_lineNo[counter3];
		            			else
		            				t=mainSource+"!!!"+main_Destination2EdgeLabels_CSV[counter3]+"!!!"+main_Destination_Chain[counter3]+"^^^"+counter3+"^^^"+"!!!"+main_Destination_Chain_lineNo[counter3];
		            				
		            			 
		            			if(t.indexOf("null")==-1){
			            			if(is_SOPprint)
			            				System.out.println(t);
			            			map_temp_currID_SET1.put(t, "");
			            			next_sourceNode=main_Destination_Chain[counter3]+"^^^"+counter3+"^^^"; //^^^ added so that it is not mixed with nodes having same name
		            			}
		            		}
		            		else if( (counter3+1)>1 && (counter3+1) < main_Destination_Chain.length){
		            			String t="";
		            			t=next_sourceNode+"!!!"+OTHER_edgeLabel+"!!!"+main_Destination_Chain[counter3]+"^^^"+counter3+"^^^"+"!!!"+main_Destination_Chain_lineNo[counter3];
		            			
		            			if(t.indexOf("null")==-1){
			            			if(is_SOPprint)
			            				System.out.println(t);
			            			map_temp_currID_SET1.put(t,"");
			            			next_sourceNode=main_Destination_Chain[counter3]+"^^^"+counter3+"^^^";
		            			}
		            		}
		            		else if(counter3+1==main_Destination_Chain.length){
		            			String t=""; 
		            			t=next_sourceNode+"!!!"+END_edgeLabel+"!!!"+main_Destination_Chain[counter3]+"^^^"+counter3+"^^^"+"!!!"+main_Destination_Chain_lineNo[counter3];
		            			if(t.indexOf("null")==-1){
		            				if(is_SOPprint)
		            					System.out.println(t);
		            				map_temp_currID_SET1.put(t,"");
		            			}
		            		}
		            		counter3++;
            			}
            			
            		}
            		else if (main_Destination_Chain.length==2){
            			
            			while(counter3<main_Destination_Chain.length){
		            		if(counter3+1==1){
		            			String t=""; 
		            			t=mainSource+"!!!"+START_edgeLabel+"!!!"+main_Destination_Chain[counter3]+"^^^"+counter3+"^^^"+"!!!"+main_Destination_Chain_lineNo[counter3];
		            			if(t.indexOf("null")==-1){
			            			if(is_SOPprint)
			            				System.out.println(t);
			            			map_temp_currID_SET1.put(t,"");
			            			next_sourceNode=main_Destination_Chain[counter3]+"^^^"+counter3+"^^^";
		            			}
		            		}
		            		else if( (counter3+1)>1 && (counter3+1) < main_Destination_Chain.length){
		            			String t=""; 
		            			t=next_sourceNode+"!!!"+OTHER_edgeLabel+"!!!"+main_Destination_Chain[counter3]+"^^^"+counter3+"^^^"+"!!!"+main_Destination_Chain_lineNo[counter3];
		            			if(t.indexOf("null")==-1){
			            			if(is_SOPprint)
			            				System.out.println(t);
			            			map_temp_currID_SET1.put(t,"");
			            			next_sourceNode=main_Destination_Chain[counter3]+"^^^"+counter3+"^^^";
		            			}
		            		}
		            		else if(counter3+1==main_Destination_Chain.length){
		            			String t=""; 
		            			t=next_sourceNode+"!!!"+END_edgeLabel+"!!!"+main_Destination_Chain[counter3]+"^^^"+counter3+"^^^"+"!!!"+main_Destination_Chain_lineNo[counter3];
		            			if(t.indexOf("null")==-1){
			            			if(is_SOPprint)
			            				System.out.println(t);
			            			map_temp_currID_SET1.put(t,"");
		            			}
		            		}
		            		counter3++;
            			}
            			
            		}
            		else if (main_Destination_Chain.length==1){
            			
            			while(counter3<main_Destination_Chain.length){
		            		if(counter3+1==1){
		            			String t=""; 
		            			t=mainSource+"!!!"+OTHER_edgeLabel+"!!!"+main_Destination_Chain[counter3]+"^^^"+counter3+"^^^"+"!!!"+main_Destination_Chain_lineNo[counter3];
			            		if(t.indexOf("null")==-1){
			            			System.out.println(t);
			            			map_temp_currID_SET1.put(t,"");
		            			}
		            		}
		            		counter3++;
            			}
            		}
            		//persist all SET_1 stuff
            		map_ID_N_Set1asKEY.put(currID2, map_temp_currID_SET1);
            	}
            	
	            ////// BELOW SET 2 is about further drill-down in each of node in destination chain, where in each node we add 1/2 edge-dest2Node pairs.
	            //<ID_1,<Destination2_1!!![hardCodeEdgeLabel]!!!<NODE1_from_mCOLUMNinFile>>
	            //<ID_1,<Destination2_1!!![hardCodeEdgeLabel]!!!<NODE2_from_nCOLUMNinFile>>
	            //<ID_1,<Destination2_2!!![hardCodeEdgeLabel]!!!<NODE1_from_mCOLUMNinFile>>
	            //<ID_1,<Destination2_2!!![hardCodeEdgeLabel]!!!<NODE2_from_nCOLUMNinFile>>
	            //<ID_1,<Destination2_3!!![hardCodeEdgeLabel]!!!<NODE1_from_mCOLUMNinFile>>
	            //<ID_1,<Destination2_3!!![hardCodeEdgeLabel]!!!<NODE2_from_nCOLUMNinFile>>
            
            	//***** SET_2 - is about further drill-down in each of node in destination chain, where in each node we add 1 or 2 edge-dest2Node pairs.
            	// GET INFO from "map_ID_N_Set1asKEY" and get only DESTINATION node in this structure, and add 1 or 2 edge-dest2Node pairs.
            	for(String currID_:map_ID_N_Set1asKEY.keySet()){
            		TreeMap<String,String> map_Set1asKEY=map_ID_N_Set1asKEY.get(currID_);
            		int counter10=0;
            		TreeMap<Integer,String> map_temp_counter10_N_Set1asKEY_MAIN=new TreeMap<Integer, String>(); // <--- SET 1
            		TreeMap<Integer,String> map_temp_counter10_N_Set1asKEY_ZERO=new TreeMap<Integer, String>();// <--- SET 2
            		TreeMap<Integer,String> map_temp_counter10_N_Set1asKEY_ONE=new TreeMap<Integer, String>();// <--- SET 2
 
            		// 
            		for(String source_edgeLabel_dest:map_Set1asKEY.keySet()){
            			counter10++;
            			String[] arr_source_edgeLabel_dest=source_edgeLabel_dest.split("!!!");
            			int lineNo_of_dest_fromChain=Integer.valueOf(arr_source_edgeLabel_dest[3]); //get only lineNo of destination nodes (chain destination node)
            			String dest_fromChain=arr_source_edgeLabel_dest[2];
            			
//            			System.out.println(dest_fromChain+"!!!"+arr_edgeLabel_4_ChainNode[0]+"!!!"+arr_map_dest2_4_ChainNode[0]);
            			// ZERO set
            			String t_1=dest_fromChain+"!!!"+map_lineNo_edgeLabelZERO_4_ChainNode.get(lineNo_of_dest_fromChain)
						 					+"!!!"+map_lineNo_map_dest2ZERO_4_ChainNode.get(lineNo_of_dest_fromChain)+"^^^"+counter10+"^^^";
            			
            			 
            			if(is_SOPprint && t_1.indexOf("null")==-1)
            				System.out.println(t_1);
            			// ONE set
            			String t_2=dest_fromChain+"!!!"+map_lineNo_edgeLabelONE_4_ChainNode.get(lineNo_of_dest_fromChain)
						   						+"!!!"+map_lineNo_map_dest2ONE_4_ChainNode.get(lineNo_of_dest_fromChain)+"^^^"+counter10+"^^^";
            			if(is_SOPprint && t_2.indexOf("null")==-1)
            				System.out.println(t_2);
            			
            			if(source_edgeLabel_dest.indexOf("null")==-1)
            				map_temp_counter10_N_Set1asKEY_MAIN.put(counter10, source_edgeLabel_dest);// <--- SET 1
            			//// --- SET 2
            			if(t_1.indexOf("null")==-1)
            				map_temp_counter10_N_Set1asKEY_ZERO.put(counter10, t_1); // <--- SET 2
            			if(t_2.indexOf("null")==-1)
            				map_temp_counter10_N_Set1asKEY_ONE.put(counter10, t_2);// <--- SET 2
            		}
            		// to be output staging
            		mapOut_ID_N_Source_EdgeLabel_Destination_MAIN.put(currID_, map_temp_counter10_N_Set1asKEY_MAIN);// <--- SET 1
            		mapOut_ID_N_Source_EdgeLabel_Destination_ZERO.put(currID_, map_temp_counter10_N_Set1asKEY_ZERO); // <--- SET 2
            		mapOut_ID_N_Source_EdgeLabel_Destination_ONE.put(currID_, map_temp_counter10_N_Set1asKEY_ONE);// <--- SET 2
            		
            	}
            	/// FINAL OUTPUT
            	mapOUTPUT_ID_N_Source_EdgeLabel_Destination.put(1, mapOut_ID_N_Source_EdgeLabel_Destination_MAIN);
            	mapOUTPUT_ID_N_Source_EdgeLabel_Destination.put(2, mapOut_ID_N_Source_EdgeLabel_Destination_ZERO);
            	mapOUTPUT_ID_N_Source_EdgeLabel_Destination.put(3, mapOut_ID_N_Source_EdgeLabel_Destination_ONE);

             
            if(is_SOPprint)
            		System.out.println("()()()(oooo->"+map_ID_N_Set1asKEY);
        }
        catch(Exception e){
        		e.printStackTrace();
        }
        return mapOUTPUT_ID_N_Source_EdgeLabel_Destination;
    }

    // main method
	public static void main(String[] args) throws IOException {
		int c = 0;
		System.out.println(" Usage from Command Line: java -jar crawler.jar getcrawler \n"
						+ " \"conf.txt\" \n" + " \n");
		System.out.println("Number of input params:" + args.length);
		// List out the input arguments
		while (c < args.length) {
			System.out.println("input args[" + c + "]=" + args[c]);
			c++;
		}

		String prefix = "D:\\share\\lenin\\";
		String inputFile = prefix + "GetEditDistance1.txt";
		String outputFile = prefix + "GetEditDistance1_out.txt";
		// cleanTitle(inputFile,outputFile,"|||");
		String debugFile = "//Users//guest2//Downloads//outDebug.txt";
		String folder = "//Users//guest2//Downloads//uaw.test.input//";
		String confFile = "";
		String macUserName = "lenin";
		folder = "//Users//" + macUserName + "//Downloads//uaw.test.input//";

		String outAlreadyCrawledURLsINaFile = "//Users//"
												+ macUserName
												+ "//Downloads//uaw.test.input//outDebugAlreadyCrawledURLsINaFile.txt";
		String run_type="prob"; // {"main","prob"}
//		 	   run_type="main";
		TreeMap<String, String> mapSS_interested_SectionNames=new TreeMap<String, String>();

		//////////////////////////////////////////////////MAIN START//////////////////////////////////////////////////
		
		// System.out.println("mapCrawled->" + mapAlreadyCrawledURL);
		// Run from command prompt
		if (args.length >= 1) {
			run_type = args[0].toLowerCase(); // first argument from command line
			confFile = args[1]; // first argument from command line
		}
		// dummy no input parameters..
		else if (args.length == 0) {
			// pre-defined config file

		}

		TreeMap<String, String> mapConfig = null;

		String option = null;
		String method_to_run = "";
		System.out.print("\n Choose option/method to call:");
		//main
		if(run_type.toLowerCase().equals("main")){
				System.out.print("\n run_type=main"); 
				System.out.print("\n 1.  crawler");
				System.out.print("\n 2.  readparsexmlfeeds (next readFile_readEachLine_For_a_Set_of_Start_String_End_String_WriteToAnotherFile() method)");
				System.out.print("\n 3.  readFolderEmailFilesUAW");
				System.out.print("\n 4.  MergeFiles (Horizontal)");
				System.out.print("\n 5.  readFile_get_Set_Of_N_positioned_Tokens");
				System.out.print("\n 6.  readFile_verify_and_Correct_No_Of_Tokens_In_EachLine");
				System.out.print("\n 7.  readFile_only_verify_No_Of_Tokens_In_EachLine");
				System.out.print("\n 8.  getSingleURL_Crawled_Testing");
				System.out.print("\n 9.  getWikiPageLinks.getLinks");
				System.out.print("\n 10. getWikiPageLinks.http_wrapper_for_http_getLinks");
				System.out.print("\n 11. getWikiPageLinks.http_recursive_call_for_wrapper_for_http_getLinks");
				System.out.print("\n 12. readFile_verify_is_RSS.readFile_verify_is_RSS");
				System.out.print("\n 13. readFile_Split_One_into_Many_by_Size.split_by_line_Count");
				System.out.print("\n 14. readFile_EachLine_from_First_File_exists_in_Second_File_as_String");
				System.out.print("\n 15. find_wrapper_for_find_links_from_Google_Search_Engine_for_keywords_from_file");
				System.out.print("\n 16. readFile_wrapper_for_find_String_replaceWith_String_Write_to_Another_File");
				System.out.print("\n 17. URLOFthehindu_print (practice:since 2009 crawled)");
				System.out.print("\n 18. ~URLOFthehinduprint06ToTill (obsolete)");
				System.out.print("\n 19. URLOFthehinduprint_any_year_range");
				System.out.print("\n 20. ~URLOFthehinduprint2000To2005detailedNational (obsolete)");
				System.out.print("\n 21. URLOFtimesofindia");
				System.out.print("\n 22. getNLPTrained (NLP tagging)");
				System.out.print("\n 23. ");
				System.out.print("\n 24. ");
		}
		//prob
		if(run_type.toLowerCase().equals("prob")){
				System.out.print("\n run_type=prob");
				System.out.print("\n 1.  collect_NYT_news_using_API.main_to_run_nyt_date_range_and_custom_url");
				System.out.print("\n 2.  prepare_Input_feature_file_from_raw_data_p6.extract_features_from_Each_doc");
				System.out.print("\n 3.  convert_givenString_to_Json.readFile_eachLine_convert_a_Token_to_JSON_FOR_NYT");
				System.out.print("\n 4.  wrapper_for_mainType_createGBADgraphFromTextUsingNLP");  ///******
				System.out.print("\n 5.  createCSVtoGBADgraph");
		}
		
		System.out.print("\n");

		// open up standard input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// prompt the user to enter their OPTION
		try {
			//method_to_run = br.readLine();
		} catch (Exception ioe) {
			System.out.println("IO error trying to read your option!");
			System.exit(1);
		}
        method_to_run = "6"; //debug hard-coded
		// System.out.println("Given Option->" + option);
		// option="y";
		//

		inputFile = "//Users//" + macUserName
				+ "//GoogleDrive//uaw.test.input//CrawledOutHTML1.txt";

		// inputFile="/Users/lenin/GoogleDrive/uaw.test.input/CrawledOutHTML.txt";
		// outputFile="/Users/lenin/GoogleDrive/uaw.test.input/CrawledOutHTMLCleaned.txt";
		// debugFile="/Users/lenin/GoogleDrive/uaw.test.input/CrawledOutHTMLDebug.txt";
		// clean
		// getCleanedCrawledFiles( 4, "!!!", inputFile, outputFile, debugFile);

		long t0 = System.nanoTime();
		// call the method
		if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("1")) {

			// Run from command prompt
			if (args.length >= 1) {
				confFile = args[1]; // second argument from command line
				System.out.println("\n Got config File from command prompt:"+ confFile);
			} else if (args.length == 0) {
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/1.crawler/config.txt";
			}
			mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);

			// getCrawler()
			// crawler t2 = new crawler();
			// t2.mapConfig = mapConfig;
			folder = mapConfig.get("folder"); // base folder
			String  NLPfolder = mapConfig.get("NLPfolder"); // base folder
			boolean is_NLP_run = Boolean.valueOf(mapConfig.get("is_NLP_run"));
			String  inputSingleURL = mapConfig.get("inputSingleURL");
					outputFile     = folder+ mapConfig.get("outputFile");
			boolean isAppend = Boolean.valueOf(mapConfig.get("isAppend"));
			String  delimiter = mapConfig.get("delimiter"); // "!!!"
			String  inputType = mapConfig.get("inputType"); // {"file",""} input
															// is a file type
															// (or) single url
			String outputType = mapConfig.get("outputType");// {"writeCrawledText2OutFile","mongodb","outNewUnCrawledURLFile"}
				   outAlreadyCrawledURLsINaFile =folder+ mapConfig.get("outAlreadyCrawledURLsINaFile"); // "outAlreadyCrawledURLsINaFile.txt";
			String inputListOf_URLfileCSV =folder+ mapConfig.get("inputListOf_URLfileCSV");
			String debugFileName = folder + mapConfig.get("debugFileName");

			int fromLine = Integer.valueOf(mapConfig.get("fromLine"));
			int toLine = Integer.valueOf(mapConfig.get("toLine"));
			String outdebugErrorAndURLfile = folder+ mapConfig.get("outdebugErrorAndURLfile");
			String outNewUnCrawledURLFile = folder+mapConfig.get("outNewUnCrawledURLFile");
			String parseType = mapConfig.get("parseType");
			String method = mapConfig.get("method");
			String flag2 = mapConfig.get("flag2");
			boolean isSOPdebug = Boolean.valueOf(mapConfig.get("isSOPdebug"));
			System.out.println("url present:"+ mapConfig.get("URL_present_in_which_column_token"));
			int URL_present_in_which_column_token = Integer.valueOf(mapConfig.get("URL_present_in_which_column_token"));
			String crawlerType = mapConfig.get("crawlerType");
			int in_Already_Crawled_File_URL_present_in_which_column_token = Integer.valueOf(mapConfig.get("in_Already_Crawled_File_URL_present_in_which_column_token"));
			isSOPdebug = Boolean.valueOf(mapConfig.get("isSOPdebug"));
			boolean is_overwrite_flag_with_flag_model=Boolean.valueOf(mapConfig.get("is_overwrite_flag_with_flag_model"));
			//model for tagging
			POSModel inflag_model2=new POSModelLoader().load(new File(NLPfolder+ "en-pos-maxent.bin"));
			//model for chunker
			InputStream is = new FileInputStream(NLPfolder+"en-parser-chunking.bin");
			ParserModel inflag_model2_chunking = new ParserModel(is);
			
			System.out.println("\nConfiguration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + "->" + mapConfig.get(i));
			}
			// option="n";
			// // continue
			// option=read_input_from_command_line_to_continue_Yes_No(br);
			//
			// if(!option.equalsIgnoreCase("y"))
			// System.exit(1);
			//
			// t0 = System.nanoTime();
			//
			// if(outAlreadyCrawledURLsINaFile.equals("")){
			// outAlreadyCrawledURLsINaFile=inputFile;
			// }
			
			// not EXISTS , create empty
			if(!new File(outAlreadyCrawledURLsINaFile).exists())
				new File(outAlreadyCrawledURLsINaFile).createNewFile();

			// N th token is assumed to have already crawled URL
			// This much be the output file (format:
			// <email.file.name!!!URL!!!crawled text>) <- N=2 here
			// mapAlreadyCrawledURL <--> <lineNo,URL>
			TreeMap<String, String> mapAlreadyCrawledURL = LoadNthTokenFromWithORWithoutGivenPatternFromGivenFileListandLoadToMap(
															outAlreadyCrawledURLsINaFile,
															in_Already_Crawled_File_URL_present_in_which_column_token,
															"!!!", "http", // pattern1
															"www", // pattern2
															"http://", // replace
															"www.", //
															false //isSOPprint
															);

			System.out.println("folder:" + folder + "\nNLPfolder:" + NLPfolder
					+ "\nis_NLP_run:" + is_NLP_run
					+ "\ninputListOf_URLfileCSV:" + inputListOf_URLfileCSV
					+ "\ninputSingleURL:" + inputSingleURL + "\noutputFile"
					+ outputFile + "\nisAppend" + isAppend + "\ndelimiter:"
					+ delimiter + "\ninputType:" + inputType + "\noutputType:"
					+ outputType + "\noutAlreadyCrawledURLsINaFile:"
					+ outAlreadyCrawledURLsINaFile + "\ninputURLfileListCSV:"
					+ inputListOf_URLfileCSV + "\nfromLine:" + fromLine
					+ "\ntoLine:" + toLine + "\noutdebugErrorAndURLfile:"
					+ outdebugErrorAndURLfile + "\noutNewUnCrawledURLFile:"
					+ outNewUnCrawledURLFile + "\nparseType:" + parseType
					+ "\nmethod:" + method);

			System.out.println("mapAlreadyCrawledURL.size:"
					+ mapAlreadyCrawledURL.size());
			System.out.print("\n Enter Y (or) N to continue..");

			// read the username from the command-line; need to use try/catch
			// with the
			// readLine() method
			try {
				option = br.readLine();
			} catch (IOException ioe) {
				System.out.println("IO error trying to read your option!");
				System.exit(1);
			}
			if (!option.equalsIgnoreCase("y"))
				System.exit(1);
			t0 = System.nanoTime();
			// run the main crawler function
			getCrawler(
					folder,
					NLPfolder,
					is_NLP_run,
					inputListOf_URLfileCSV, // input file
											// <email.name!!!URL!!!header text
											// from email> <- at least two
											// column
					inputSingleURL,
					outputFile,
					isAppend,
					delimiter,
					inputType, // {"file","mongodbURLonly","mongodburlandbody}
								// input is a file type (or) single url
					outputType, // {"outfile","mongodb",""}
					flag2, //
					outAlreadyCrawledURLsINaFile, 
					mapAlreadyCrawledURL,
					fromLine,
					toLine,
					outNewUnCrawledURLFile, // for
											// {outputType="outNewUnCrawledURLFile"}
					outdebugErrorAndURLfile, parseType, isSOPdebug,
					URL_present_in_which_column_token, crawlerType,
					is_overwrite_flag_with_flag_model,
					inflag_model2,
					inflag_model2_chunking,
					debugFileName);
			
			System.out.println("end:mapAlreadyCrawledURL:"
					+ mapAlreadyCrawledURL.size());
		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("2")) {
			{ // readParseXMLFeeds
				TreeMap<Integer, String> mapConfinput = new TreeMap<Integer, String>();
				String past_already_ran_success_file_for_skipping = "";
				String inFile = "/Users/lenin/Downloads/Feeds (5-Jun)/Minneapolis";
				inFile = "/Users/lenin/Downloads/Feeds (5-Jun)/BBC News - Scotland";
				String inFolder = "/Users/lenin/Downloads/Feeds-5-Jun";
				inFolder = "/Users/lenin/Downloads/Feeds-5-Jun-.dummy1/";
				// inFolder="/Users/lenin/Downloads/Feeds-29-June-2015/";
				debugFile = "";
				boolean is_consider_past_already_ran_success_file_for_skipping = true;
				boolean is_Streaming = true;
				boolean is_outputFileName_Dynamic = true;
				boolean is_outputFileName_Static = true;
				boolean is_run_only_static = false;
				boolean is_run_only_dynamic = false;
				boolean is_run_only_eachLineNEWSstring=false;
				String SuccessfullyProcessedFiles_for_current_stat_out = "";
				String SuccessfullStaticFiles_for_current_stat_out = "";
				String outFile_Static_eachLineNEWSstring = "";
				String SuccessfullDynamicFiles_for_current_stat_out = "";
				String alreadyFailureProcessedFiles = "";
				String continueParsingFromThisPatternMMM = "";
				String continueParsingFromThisPatternYYYY = "";
				String outFileDynamic = "";
				boolean Go_AFTER_found_ContinueParsingFromThisPattern = false;
				boolean isSOPprint = false;
				boolean is_convert_to_lowercase=true;
				boolean is_debug_WriteMore=false;
				String outFile_Static = "/Users/lenin/Downloads/output/dum.uptominute.output1.txt";
				outFile_Static = "/Users/lenin/Downloads/output/dum.uptominute.output1.txt";
				continueParsingFromThisPatternMMM = ""; // not working
				continueParsingFromThisPatternYYYY = ""; // not working

				// read and parse XML feeds
				// readParseXMLFeeds(inFolder, Filenames , outFile,"/");
				confFile = "";

				// Run from command prompt
				if (args.length >= 1) {
					confFile = args[1]; // second argument from command line
					System.out
							.println("\n Got config File from command prompt:"
									+ confFile);
				} else if (args.length == 0) {
					confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/2.readparsexmlfeeds/xmlFeedconfig.txt";
					confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/2.readparsexmlfeeds/1.xmlFeedconfig.txt";
				}
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
				//Files in below folder should have " " at each line. Use AWK to achieve that (awk '{print $0" "}' file))
				inFolder = mapConfig.get("s1.inputFolderpath"); // input folder -> awk '{print $0" "}' file)
				outFile_Static = mapConfig.get("s1.outputFile"); // OUT FILE
				outFile_Static_eachLineNEWSstring=outFile_Static+"_eachLineNEWSstring.txt";
				is_outputFileName_Static = Boolean.valueOf(mapConfig.get("s1.is_Append_outputFile_Static"));

				outFileDynamic = mapConfig.get("s1.outputFileName_Dynamic");
				is_outputFileName_Dynamic = Boolean.valueOf(mapConfig
						.get("s1.is_Append_outputFileName_Dynamic"));

				debugFile = mapConfig.get("debugFile");
				SuccessfullStaticFiles_for_current_stat_out = mapConfig
						.get("SuccessfullStaticFiles_for_current_stat_out");
				SuccessfullDynamicFiles_for_current_stat_out = mapConfig
						.get("SuccessfullDynamicFiles_for_current_stat_out");

				SuccessfullyProcessedFiles_for_current_stat_out = mapConfig
						.get("SuccessfullyProcessedFiles_for_current_stat_out");
				alreadyFailureProcessedFiles = mapConfig
						.get("alreadyFailureProcessedFiles");
				past_already_ran_success_file_for_skipping = mapConfig
						.get("past_already_ran_success_file_for_skipping");

				is_run_only_static = Boolean.valueOf(mapConfig.get("is_run_only_static"));
				is_run_only_dynamic = Boolean.valueOf(mapConfig.get("is_run_only_dynamic"));
				is_convert_to_lowercase=Boolean.valueOf( mapConfig.get("is_convert_to_lowercase"));
				is_Streaming = Boolean.valueOf(mapConfig.get("is_streaming"));
				isSOPprint = Boolean.valueOf(mapConfig.get("isSOPprint"));
				is_debug_WriteMore= Boolean.valueOf(mapConfig.get("is_debug_WriteMore"));
				is_run_only_eachLineNEWSstring= Boolean.valueOf(mapConfig.get("is_run_only_eachLineNEWSstring"));
				is_consider_past_already_ran_success_file_for_skipping = Boolean
						.valueOf(mapConfig
								.get("is_consider_past_already_ran_success_file_for_skipping"));
				System.out.println("input folder:"+inFolder);
				File file = new File(inFolder);
				File[] Filenames = file.listFiles();
				if(file!=null)
					System.out.println("input folder files:"+file.listFiles().length);
				String new_inFolder=inFolder.substring(0, inFolder.length()-1)+".space";
				//create new folder <inFolder>+".space"
				//create awkscript that can add " "to each line of each file and copy to new folder <inFolder>+".space"
				new File(new_inFolder).mkdir(); //create new folder
				Runtime rt = Runtime.getRuntime();				
				String awkScript=new_inFolder+"/"+"AWKcopyFilesADDSPACE.sh";
				FileWriter writerAWK=new FileWriter(new File(awkScript));
				//create AWK script
				for (int i=0; i < Filenames.length; i++)
				{ 	if(Filenames[i].toString().indexOf(".DS_Store")>=0) continue;
				
					String new_FULL_filename_curr=Filenames[i].getAbsolutePath();
					new_FULL_filename_curr=new_FULL_filename_curr.replace("'", "\'").replace(" ", "\\ ")
											.replace("'", "\\'").replace("(", "\\(").replace(")", "\\)")
											.replace("&", "\\&")
											;
					String new_PART_filename_curr=Filenames[i].getName();
					new_PART_filename_curr=new_PART_filename_curr.replace("'", "\'").replace(" ", "\\ ")
											.replace("'", "\\'").replace("(", "\\(").replace(")", "\\)")
											.replace("&", "\\&")
											;
				
					String curr_awkcommand_for_currFile="awk '{print $0\" \"}'"+" "+new_FULL_filename_curr+" >> "+new_inFolder+"/"+new_PART_filename_curr;
					   
					writerAWK.append(curr_awkcommand_for_currFile+"\n");
					writerAWK.flush();
				   
					  //absolutePathOfInputFile+ slashtype +
					System.out.println(curr_awkcommand_for_currFile);
//					try {
//						int retVal = pr.waitFor();
//						System.out.println("process:"+retVal);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					
				 }
				Process pr = rt.exec("chmod 775 "+awkScript);
				//Runtime.getRuntime().exec("/path/to/your/awk_script");

				if (new File(debugFile).exists())
					new File(debugFile).delete();
				// if(new File(alreadySuccessfullyProcessedFiles).exists())
				// new File(alreadySuccessfullyProcessedFiles).delete();
				// if(new File(alreadyFailureProcessedFiles).exists())
				// new File(alreadyFailureProcessedFiles).delete();

				System.out.println("\nConfiguration:");
				for (String i : mapConfig.keySet()) {
					System.out.println(i + "->" + mapConfig.get(i));
				}

				System.out.println("debugFile:" + debugFile);
				System.out
						.println("SuccessfullyProcessedFiles_for_current_stat_out:"
								+ SuccessfullyProcessedFiles_for_current_stat_out);
				System.out
						.println("SuccessfullStaticFiles_for_current_stat_out:"
								+ SuccessfullStaticFiles_for_current_stat_out);
				System.out
						.println("SuccessfullDynamicFiles_for_current_stat_out:"
								+ SuccessfullDynamicFiles_for_current_stat_out);
				System.out.println("alreadyFailureProcessedFiles:"
						+ alreadyFailureProcessedFiles);
				System.out.println("inFolder" + inFolder);

				FileWriter writerDebug = new FileWriter(new File(debugFile),
						true);
				FileWriter writerSuccess = new FileWriter(new File(
						SuccessfullyProcessedFiles_for_current_stat_out), true);
				FileWriter writerSuccess_only_static = new FileWriter(new File(
																SuccessfullStaticFiles_for_current_stat_out), true);
				
				
				FileWriter writerSuccess_only_dynamic = new FileWriter(
																new File(SuccessfullDynamicFiles_for_current_stat_out),
																		true);
				FileWriter writerFailure = new FileWriter(new File(
						alreadyFailureProcessedFiles), true);
				
				Go_AFTER_found_ContinueParsingFromThisPattern = false;
				System.out.println("***new inFolder="+new_inFolder+"/");
				inFolder=new_inFolder+"/";
				
				System.out.println("-----------instruction----------");
				System.out.println("IMP!!: Run AWK script before continue:\n"+awkScript);
				System.out.println("-------------------------------");
				file = new File(inFolder);
				Filenames = file.listFiles();
				// input command line
				// mapConfinput=read_input_from_Command_Line(confFile,
				// br,
				// inFolder,
				// mapConfig);
				//
				// // read input from Command Line //not test
				// if(mapConfinput.containsKey(1)){
				// confFile = mapConfinput.get(1); //
				// }
				//
				// if(! mapConfinput.get(2).equalsIgnoreCase("y"))
				// System.exit(1);

				// continue
				option = read_input_from_command_line_to_continue_Yes_No(br);

				if (!option.equalsIgnoreCase("y"))
					System.exit(1);

				t0 = System.nanoTime();

				//did same thing above (twice for confirm)
				file = new File(inFolder);
				Filenames = file.listFiles();
				writerDebug
						.append("\nBefore exec: number of files:Filenames.len:"
								+ Filenames+"\n inFolder:"+inFolder
								+"\n files.size:"+Filenames.length);
				writerDebug.flush();

				// read parse xml feeds
				ReadParseXMLFeeds.readParseXMLFeeds(
														"", // inputFolderName(empty if "Filenames" given)
														Filenames, // array of input Files
														outFile_Static, 
														is_outputFileName_Static,
														outFile_Static_eachLineNEWSstring,
														outFileDynamic, 
														is_outputFileName_Dynamic, "/",
														is_run_only_static, 
														is_run_only_dynamic,
														is_run_only_eachLineNEWSstring,
														continueParsingFromThisPatternMMM,
														continueParsingFromThisPatternYYYY, confFile,
														past_already_ran_success_file_for_skipping,
														is_consider_past_already_ran_success_file_for_skipping,
														writerSuccess, 
														writerSuccess_only_static,
														writerSuccess_only_dynamic, 
														writerFailure, 
														writerDebug,
														mapConfig,
														Go_AFTER_found_ContinueParsingFromThisPattern,
														is_Streaming, // isStreamingInputFile
														is_convert_to_lowercase, //is_convert_to_lowercase
														isSOPprint,
														is_debug_WriteMore
														);

				// test case: use files-> News-4 ; Breaking; NYT Liberia..msf;
				// RT - Daily news
				System.out.println("Time Taken (FINAL ENDED):"
						+ NANOSECONDS.toSeconds(System.nanoTime() - t0)
						+ " seconds; "
						+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
						+ " minutes");
				//
			}
		}// *method="readParseXMLFeeds.readParseXMLFeeds"
		else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("3")) {
			// read email alert feed files
			folder = "//Users//lenin//Library//Mail//V2//IMAP-uglyartworld@imap.gmail.com//[Gmail].mbox//All Mail.mbox//47E0C302-248D-474E-94D3-453E762B3BC1//";

			String outputFile_For_FullPath_generated = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/fullpath_generated.txt";
			String pastRun_alreadyRead_FileName = "";
			String outputSuccessFile = "";
			System.out.println("\n config File:" + confFile);
			boolean is_pastRun_alreadyRead_FileName_A_Success_File = false;
			// Run from command prompt
			if (args.length >= 1) {
				confFile = args[1]; // second argument from command line
				System.out.println("\n Got config File from command prompt:"
						+ confFile);
			} else {
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/3.emailAlert/EmailAlertconfig.ugly.txt"; // auto
																																			// config
			}

			// read Config
			mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);

			folder = mapConfig.get("inputFolder");
			String YYYY = mapConfig.get("YYYY"); // dummy NOT really used
			debugFile = mapConfig.get("debugFile");
			outputFile = mapConfig.get("outputFile");
			outputFile_For_FullPath_generated = mapConfig
					.get("outputFileForFullPathGenerated");
			pastRun_alreadyRead_FileName = mapConfig
					.get("pastRunAlreadyReadFilesCSV");
			is_pastRun_alreadyRead_FileName_A_Success_File = Boolean
					.valueOf(mapConfig
							.get("is_pastRun_alreadyRead_FileName_A_Success_File"));

			outputSuccessFile = mapConfig.get("outputSuccessFile");

			System.out.println("\nConfiguration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + "->" + mapConfig.get(i));
			}
			System.out.println("\nmapConfig:" + mapConfig);
			confFile = read_input_from_command_line_config_file_name(br,mapConfig);

			// TreeMap<Integer,String> mapFoldersList =
			// getMessageFoldersFullPathFromGivenTopFolder(folder,
			// outputFile_For_FullPath_generated,
			// 10);

			TreeMap<String, String> mapAllFiles_FromInputFolder = ReadFile_get_all_FilesPath_Recursively_From_Given_Folder
					.readFile_get_all_FilesPath_Recursively_From_Given_Folder(
							folder, outputFile_For_FullPath_generated);
			TreeMap<String, String> mapTempLoad = new TreeMap<String, String>();
			TreeMap<Integer, TreeMap<String, String>> mapReadMapFromFile = new TreeMap<Integer, TreeMap<String, String>>();

			if (!is_pastRun_alreadyRead_FileName_A_Success_File) {
				// Previous output
				// (<emlx_File_Name_only/emlx_Full_Path>!!!<URL>!!!<Title_AND_body_Text>)
				// from readFolderEmailFilesUAW() is given (so that no need to
				// re-run for same file)
				mapReadMapFromFile = LoadMultipleValueTo_Key_Value_Map3(
						pastRun_alreadyRead_FileName, "!!!", debugFile);
			} else {
				// Another way: Previous output (<emlx_File_Name_Full_Path>)
				mapTempLoad = ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
							 .readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
								pastRun_alreadyRead_FileName, -1, -1, "",
								false, false, " already load"// debug_label
								, -1 //token_to_be_used_as_primarykey
								, false  // isSOPprint
								 );
				
				mapReadMapFromFile.put(1, mapTempLoad);

				for (String k : mapTempLoad.keySet()) {
					System.out.println("k:" + k + "!!!" + mapTempLoad.get(k));
				}

			}

			// map is out of getMessageFoldersFullPathFromGivenTopFolder
			// folder list
			// for(String g:mapFoldersList.keySet()){
			// folder = mapFoldersList.get(g);

			System.out.println("curr folder:" + folder);

			t0 = System.nanoTime(); //
			boolean isAppend_Output_File = true; // true for append
			// read email (feed) folder
			readFolderEmailFilesUAW(mapAllFiles_FromInputFolder, debugFile,
					outputFile,// <File_Name>!!!<URL>!!!<Title_AND_body_Text>
					outputSuccessFile,// <File_Name>
					YYYY, isAppend_Output_File, mapReadMapFromFile);
			// }

		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("4")) {
			// merge files horizontally
			String inputFolderName = "/Users/lenin/Dropbox/#Data/group-by-age-related-out/";
			String outputFileName = "/Users/lenin/Dropbox/#Data/group-by-age-related-out/output.csv";
			if (args.length >= 1) {
				confFile = args[1]; // second argument from command line
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/mergefileConfig.txt"; // auto
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}

			System.out.println("\n input File:" + inputFile);
			System.out.println("\n config File:" + confFile);
			// read Config
			if (mapConfig.containsKey("inputFolderName"))
				inputFolderName = mapConfig.get("inputFolderName");
			if (mapConfig.containsKey("outputFileName"))
				outputFileName = mapConfig.get("outputFileName");

			System.out.println("\nConfiguration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + "->" + mapConfig.get(i));
			}
			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();
			// merge
			ReadFiles_in_a_Folder_mergeFiles_Horizontal
					.readFiles_in_a_Folder_mergeFiles_Horizontal(
							inputFolderName,
							"", //only_inputFiles_match_on_pattern
							outputFileName,
							false, //is_append_primary_FileName_at_end
							null, // inputFiles_input_2,
							false, //  isSOPprint
							"!!!" //delimiter
							);
		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("5")) {
			// readFile_get_Set_Of_N_positioned_Tokens
			String inputFileName = "/Users/lenin/Dropbox/#Data/group-by-age-related-out/";
			String outputFileName = "/Users/lenin/Dropbox/#Data/group-by-age-related-out/output.csv";
			boolean is_remove_duplicate = true;
			String interested_token_in_csv = "1";
			if (args.length >= 1) {
				confFile = args[1]; // second argument from command line
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/5.getSetof~N~tokenedPositions/NtokenConfig.txt"; // auto
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}

			System.out.println("\n input File:" + inputFile);
			System.out.println("\n config File:" + confFile);

			// read Config
			if (mapConfig.containsKey("inputFileName"))
				inputFileName = mapConfig.get("inputFileName");
			if (mapConfig.containsKey("outputFileName"))
				outputFileName = mapConfig.get("outputFileName");
			if (mapConfig.containsKey("interested_token_in_csv"))
				interested_token_in_csv = mapConfig
						.get("interested_token_in_csv");
			if (mapConfig.containsKey("is_remove_duplicate"))
				is_remove_duplicate = Boolean.valueOf(mapConfig
						.get("is_remove_duplicate"));
			
			System.out.println("\nConfiguration:");
			for (String s : mapConfig.keySet()) {
				System.out.println(s + "->" + mapConfig.get(s));
			}

			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();
			// pull tokens interested to output file
			ReadFile_get_Set_Of_N_positioned_Tokens.readFile_get_Set_Of_N_positioned_Tokens(
																			inputFileName,
																			outputFileName, "!!!", interested_token_in_csv,
																			is_remove_duplicate,
																			false //isSOPprint
																			);

			System.out.println("Terminated...");
		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("6")) {
			// readFile_verify_and_Correct_No_Of_Tokens_In_EachLine
			String inputFileName = "/Users/lenin/Dropbox/#Data/group-by-age-related-out/";
			String outputFileName = "/Users/lenin/Dropbox/#Data/group-by-age-related-out/output.csv";
			int expectedNumberOftokens = 0;
			if (args.length >= 1) {
				confFile = args[1]; // second argument from command line
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/mergefileConfig.txt"; // auto
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}

			// read Config
			if (mapConfig.containsKey("inputFileName"))
				inputFileName = mapConfig.get("inputFileName");
			if (mapConfig.containsKey("outputFileName"))
				outputFileName = mapConfig.get("outputFileName");
			if (mapConfig.containsKey("debugFile"))
				debugFile = mapConfig.get("debugFile");
			if (mapConfig.containsKey("expectedNumberOftokens"))
				expectedNumberOftokens = Integer.valueOf(mapConfig
						.get("expectedNumberOftokens"));

			System.out.println("\nConfiguration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + "->" + mapConfig.get(i));
			}
			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();

			// Correct
			ReadFile_verify_and_Correct_No_Of_Tokens_In_EachLine
					.readFile_verify_and_Correct_No_Of_Tokens_In_EachLine(
							expectedNumberOftokens, "!!!", inputFileName,
							outputFileName, debugFile);
		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("7")) {
			// readFile_only_verify_No_Of_Tokens_In_EachLine
			String inputFileName = "/Users/lenin/Dropbox/#Data/group-by-age-related-out/";
			String outputFileName = "/Users/lenin/Dropbox/#Data/group-by-age-related-out/output.csv";
			int expectedNumberOftokens = 0;
			//
			if (args.length >= 1) {
				confFile = args[1]; // second argument from command line
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/7.Verify~Number~OF~tokens/verifyConfig.txt"; // auto
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}

			// read Config
			if (mapConfig.containsKey("inputFileName"))
				inputFileName = mapConfig.get("inputFileName");
			if (mapConfig.containsKey("outputFileName"))
				outputFileName = mapConfig.get("outputFileName");
			if (mapConfig.containsKey("debugFile"))
				debugFile = mapConfig.get("debugFile");
			if (mapConfig.containsKey("expectedNumberOftokens"))
				expectedNumberOftokens = Integer.valueOf(mapConfig
						.get("expectedNumberOftokens"));

			System.out.println("\nConfiguration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + "->" + mapConfig.get(i));
			}

			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();

			// verify only
			ReadFile_verify_and_Correct_No_Of_Tokens_In_EachLine
					.readFile_only_verify_No_Of_Tokens_In_EachLine(
							expectedNumberOftokens, 
							"!!!", 
							inputFileName,
							outputFileName+"_not_matched.txt",
							outputFileName, 
							debugFile,
							false, //boolean is_flag_strictly_lesserORequal_noTokensNeeded,
							false, //is_add_lineNo_at_end
							false //isSOPprint
							);
			
			  
		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("8")) {
			// getSingleURL_Crawled_Testing
			String url = "http://www.brookings.edu/experts";
			url = "http://www.brookings.edu/rss#topics/";
			url = "http://guides.library.upenn.edu/content.php?pid=323076&sid=2644804";
			url = "http://www.hrw.org/publications?keyword=&date[value]&&&page=2";
			url = "http://guides.library.upenn.edu/content.php?pid=323076&sid=2644796";
			url = "http://www.wilsoncenter.org/experts-list/27?title_op=contains&title=&field_profile_term_start_value_op=%3E%3D&term_start[value]=&term_start[min]=&term_start[max]=&field_profile_term_end_value_op=%3C%3D&term_end[value]=&term_end[min]=&term_end[max]=&page=2";
			// getSingleURL_Crawled_Testing
			getSingleURL_Crawled_Testing(url);
		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("9")) {
			// http_getWikiPageLinks.http_getLinks
			folder = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/";

			String url = "http://guides.library.upenn.edu/content.php?pid=323076&sid=2644795";
			url = "http://www.hrw.org/";
			url = "http://carnegieendowment.org/rss/";
			url = "http://www.cfr.org/";
			url = "http://brookings.edu";
			url = "http://www.thedialogue.org/";
			url = "http://belfercenter.ksg.harvard.edu/experts/";
			url = "http://csis.org/experts/";
			// url="http://belfercenter.ksg.harvard.edu/experts/index.html?filter=S&groupby=1&type=";
			String start_tag = "href";
			String end_tag = ">";
			String FilterURLOnMatchedString = "";
			String FilterURL_Omit_OnMatchedString = "";
			String Stop_Word_for_ExtractedText = "";
			String input_list_of_URL_in_a_file = "";
			String outFileNameSuffix = "";
			String html_input_file = "";
			boolean skip_is_english = false;
			int curr_level = 1;
			int max_level = 1;
			String output_Already_Crawled_File = "";
			if (args.length > 0) {
				confFile = args[1]; // second argument from command line
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/11./recursiveCrawlerConfig.txt";
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}
			// read Config
			if (mapConfig.containsKey("input_list_of_URL_in_a_file"))
				input_list_of_URL_in_a_file = mapConfig
						.get("input_list_of_URL_in_a_file");
			if (mapConfig.containsKey("start_tag"))
				start_tag = mapConfig.get("start_tag");
			if (mapConfig.containsKey("end_tag"))
				end_tag = mapConfig.get("end_tag");
			if (mapConfig.containsKey("FilterURLOnMatchedString"))
				FilterURLOnMatchedString = mapConfig
						.get("FilterURLOnMatchedString");
			if (mapConfig.containsKey("FilterURL_Omit_OnMatchedString"))
				FilterURL_Omit_OnMatchedString = mapConfig
						.get("FilterURL_Omit_OnMatchedString");
			if (mapConfig.containsKey("Stop_Word_for_ExtractedText"))
				Stop_Word_for_ExtractedText = mapConfig
						.get("Stop_Word_for_ExtractedText");
			if (mapConfig.containsKey("curr_level"))
				curr_level = Integer.valueOf(mapConfig.get("curr_level"));
			if (mapConfig.containsKey("max_level"))
				max_level = Integer.valueOf(mapConfig.get("max_level"));
			if (mapConfig.containsKey("folder"))
				folder = mapConfig.get("folder");
			if (mapConfig.containsKey("outFileNameSuffix"))
				outFileNameSuffix = mapConfig.get("outFileNameSuffix");
			if (mapConfig.containsKey("skip_is_english"))
				skip_is_english = Boolean.valueOf(mapConfig
						.get("skip_is_english"));
			if (mapConfig.containsKey("output_Already_Crawled_File"))
				output_Already_Crawled_File = mapConfig
						.get("output_Already_Crawled_File");

			FilterURLOnMatchedString = FilterURLOnMatchedString.replace(
					"dummy", "");
			FilterURL_Omit_OnMatchedString = FilterURL_Omit_OnMatchedString
					.replace("dummy", "");
			Stop_Word_for_ExtractedText = Stop_Word_for_ExtractedText.replace(
					"dummy", "");

			System.out.println("\nConfiguration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + "->" + mapConfig.get(i));
			}

			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();

			// get link ((later uncomment this, temporarily added this to get it running)
			TreeMap<Integer, TreeMap<Integer, String>> mapLinks = Http_getWikiPageLinks
					.http_getLinks(curr_level, url, // url
							html_input_file, // html_input_file
							output_Already_Crawled_File, folder
									+ "1.outfoundURLfromWikiPage.txt", folder
									+ "2./outfoundURLfromWikiPage.txt", false, // isOutFile_1_Append
							false, // isOutFile_2_Append
							"/wiki/", // ToFind
							"http://en.wikipedia.org/wiki/", // ToReplaceWith
							"dummy." + url, // prefix counter
							FilterURLOnMatchedString, // FilterURLOnMatchedString
							FilterURL_Omit_OnMatchedString, // FilterURL_Omit_OnMatchedString
							Stop_Word_for_ExtractedText, start_tag, // http or
																	// <start
																	// tag>
							end_tag, // > or " or <end tag>
							folder + "debug.txt", skip_is_english, // skip_is_english
							"", // type
							true // write debug
					);

			System.out.println("size:" + mapLinks.size());
		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("10")) {
			// wrapper for http_getWikiPageLinks.http_getLink
			String input_list_of_URL_in_a_file = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/file.us.50.thinktank.txt";
			folder = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/urls_from_url_of_file.us.50.thinktank.txt";
			String start_tag = "href";
			boolean skip_is_english = false;
			String end_tag = ">";
			String FilterURLOnMatchedString = "";
			int curr_iteration_level = 1;
			int max_iteration_level = 2;
			String FilterURL_Omit_OnMatchedString = "";
			String Stop_Word_for_ExtractedText = "";
			String output_to_store_list_of_URLs_already_crawled_file = "";
			boolean is_consider_only_uniqueURLdomain=true;
			// **THIS MAP IS EMPTY NOW..
			TreeMap<Integer, String> mapAlreadyCrawledURL = new TreeMap<Integer, String>();

			Http_getWikiPageLinks.http_wrapper_for_http_getLinks(
					curr_iteration_level, input_list_of_URL_in_a_file,
					mapAlreadyCrawledURL,
					output_to_store_list_of_URLs_already_crawled_file, folder
							+ "1.outfoundURLfromWikiPage.txt", folder
							+ "2.outfoundURLfromWikiPage.txt", true, // isOutFile_1_Append
					true, // isOutFile_2_Append
					"/wiki/", // ToFind
					"http://en.wikipedia.org/wiki/", // ToReplaceWith
					"2014.DEC", // (dummy) prefix counter <- dummy in this place
					FilterURLOnMatchedString, // FilterURLOnMatchedString
					FilterURL_Omit_OnMatchedString, // FilterURL_Omit_OnMatchedString
					Stop_Word_for_ExtractedText, start_tag, // http or <start
															// tag>
					end_tag, // > or " or <end tag>
					folder + "debug.txt", skip_is_english, false,
					is_consider_only_uniqueURLdomain,
					-1 // top_N_lines_to_skip_from_input_URL_file
					);

		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("11")) {
			// http_recursive_call_for_wrapper_for_http_getLinks
			String input_list_of_URL_in_a_file = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/file.us.50.thinktank.dummy.txt";
			input_list_of_URL_in_a_file = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/";
			input_list_of_URL_in_a_file = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/file.us.50.thinktank.txt";
			String start_tag = "href";
			String end_tag = ">";
			int max_level = 0;
			int curr_level = 0;
			String outFileNameSuffix = "outfoundURLfromWikiPage";
			String FilterURLOnMatchedString = "";
			String FilterURL_Omit_OnMatchedString = "";
			String Stop_Word_for_ExtractedText = "";
			String output_to_store_list_of_URLs_already_crawled_file = "";
			String output_Already_Crawled_File = "";
			int top_N_lines_to_skip_from_input_URL_file = -1;
			curr_level = 1;
			max_level = 1;
			boolean skip_is_english = false;
			boolean is_consider_only_uniqueURLdomain=false;
			mapConfig = new TreeMap<String, String>();
			if (args.length > 0) {
				confFile = args[1]; // second argument from command line
				// confFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/11.http_recursive_call_for_wrapper_for_http_getLinks/recursiveCrawlerConfig_rss.txt";
				System.out.println("From command line : confFile:" + confFile);
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
				System.out.println("From command line : confFile.size:"
						+ mapConfig.size());
			} else {
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/11.http_recursive_call_for_wrapper_for_http_getLinks/recursiveCrawlerConfig_rss.txt";
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
				System.out.println("confFile.size:" + mapConfig.size());
			}
			// read Config
			if (mapConfig.containsKey("input_list_of_URL_in_a_file"))
				input_list_of_URL_in_a_file = mapConfig
						.get("input_list_of_URL_in_a_file");

			if (mapConfig.containsKey("output_Already_Crawled_File"))
				output_Already_Crawled_File = mapConfig
						.get("output_Already_Crawled_File");

			if (mapConfig.containsKey("start_tag"))
				start_tag = mapConfig.get("start_tag");
			if (mapConfig.containsKey("end_tag"))
				end_tag = mapConfig.get("end_tag");
			if (mapConfig.containsKey("FilterURLOnMatchedString"))
				FilterURLOnMatchedString = mapConfig
						.get("FilterURLOnMatchedString");
			if (mapConfig.containsKey("FilterURL_Omit_OnMatchedString"))
				FilterURL_Omit_OnMatchedString = mapConfig
						.get("FilterURL_Omit_OnMatchedString");
			if (mapConfig.containsKey("Stop_Word_for_ExtractedText"))
				Stop_Word_for_ExtractedText = mapConfig
						.get("Stop_Word_for_ExtractedText");
			if (mapConfig.containsKey("curr_level"))
				curr_level = Integer.valueOf(mapConfig.get("curr_level"));
			if (mapConfig.containsKey("max_level"))
				max_level = Integer.valueOf(mapConfig.get("max_level"));
			if (mapConfig.containsKey("folder"))
				folder = mapConfig.get("folder");
			if (mapConfig.containsKey("outFileNameSuffix"))
				outFileNameSuffix = mapConfig.get("outFileNameSuffix");
			if (mapConfig.containsKey("skip_is_english"))
				skip_is_english = Boolean.valueOf(mapConfig
						.containsKey("skip_is_english"));
			if (mapConfig.containsKey("is_consider_only_uniqueURLdomain"))
				is_consider_only_uniqueURLdomain=
							Boolean.valueOf(mapConfig.containsKey("is_consider_only_uniqueURLdomain"));
			if (mapConfig.containsKey("top_N_lines_to_skip_from_input_URL_file"))
				top_N_lines_to_skip_from_input_URL_file = Integer
						.valueOf(mapConfig
								.get("top_N_lines_to_skip_from_input_URL_file"));

			FilterURLOnMatchedString = FilterURLOnMatchedString.replace("dummy", "");
			FilterURL_Omit_OnMatchedString = FilterURL_Omit_OnMatchedString.replace("dummy", "");
			Stop_Word_for_ExtractedText = Stop_Word_for_ExtractedText.replace("dummy", "");

			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();

			// recursive call
			Http_getWikiPageLinks
					.http_recursive_call_for_wrapper_for_http_getLinks(
							max_level, curr_level, input_list_of_URL_in_a_file,
							output_Already_Crawled_File, // already crawled
							folder + "1." + outFileNameSuffix + ".txt", folder
									+ "2." + outFileNameSuffix + ".txt", true, // isOutFile_1_Append
							true, // isOutFile_2_Append
							"/wiki/", // ToFind
							"http://en.wikipedia.org/wiki/", // ToReplaceWith
							"", // prefixCounter/(dummy) prefix counter <-takes
								// URL automatically
							FilterURLOnMatchedString, // FilterURLOnMatchedString
							FilterURL_Omit_OnMatchedString, // FilterURL_Omit_OnMatchedString
							Stop_Word_for_ExtractedText, start_tag, // http or
																	// <start
																	// tag>
							end_tag, // > or " or <end tag>
							folder + "debug.txt", skip_is_english, // false
																	// means all
																	// URL given
																	// considered
																	// as
																	// ENGLISH
							false, 
							is_consider_only_uniqueURLdomain, // 
							top_N_lines_to_skip_from_input_URL_file // top_N_lines_to_skip_from_input_URL_file
							);
		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("12")) {
			// readFile_verify_is_RSS.readFile_verify_is_RSS
			String inFile = "";
			String outFile = "";
			int start_line = 20000;
			int end_line = 60000;
			boolean is_Append_outFile = true;
			boolean is_Append_pastRunResultsFile = true;
			boolean isSOPprint = true;
			inFile = "/Users/lenin/Dropbox/dataDONTDELETE/rss.us.tt.merged.nodup.txt";
			outFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/rss.us.tt.merged.nodup_isRSS.txt";
			String outFile_pastRunResultsFile = "";
			if (args.length > 0) {
				confFile = args[1]; // second argument from command line
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/12.readFile_verify_is_RSS/Config.txt";
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}

			// read Config
			if (mapConfig.containsKey("outFile"))
				outFile = mapConfig.get("outFile");
			if (mapConfig.containsKey("outFile_pastRunResultsFile"))
				outFile_pastRunResultsFile = mapConfig
						.get("outFile_pastRunResultsFile");
			if (mapConfig.containsKey("inFile"))
				inFile = mapConfig.get("inFile");
			if (mapConfig.containsKey("start_line"))
				start_line = Integer.valueOf(mapConfig.get("start_line"));
			if (mapConfig.containsKey("end_line"))
				end_line = Integer.valueOf(mapConfig.get("end_line"));
			if (mapConfig.containsKey("is_Append_outFile"))
				is_Append_outFile = Boolean.valueOf(mapConfig
						.get("is_Append_outFile"));
			if (mapConfig.containsKey("is_Append_pastRunResultsFile"))
				is_Append_pastRunResultsFile = Boolean.valueOf(mapConfig.get("is_Append_pastRunResultsFile"));
			if (mapConfig.containsKey("isSOPprint"))
				isSOPprint=Boolean.valueOf(mapConfig.get("isSOPprint"));
			
			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();
			// list of url (from file) verify RSS
			ReadFile_verify_is_RSS.readFile_verify_is_RSS(inFile,
														  start_line,
														  end_line, 
														  outFile, 
														  is_Append_outFile,
														  outFile_pastRunResultsFile, 
														  is_Append_pastRunResultsFile,
														  isSOPprint //false 
														  );

		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("13")) {
			// readFile_Split_One_into_Many_by_Size.split_by_line_Count
			String inFile = "";
			int number_of_output_File = 1;

			if (args.length > 0) {
				confFile = args[1]; // second argument from command line
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/13.split_by_line_Count/Config.txt";
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}

			// read Config
			if (mapConfig.containsKey("inFile"))
				inFile = mapConfig.get("inFile");
			if (mapConfig.containsKey("number_of_output_File"))
				number_of_output_File = Integer.valueOf(mapConfig
						.get("number_of_output_File"));

			System.out.println("Given Configuration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + " " + mapConfig.get(i));
			}
			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();
			//
			ReadFile_Split_One_into_Many_by_Size.split_by_line_Count(inFile, number_of_output_File);

		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("14")) {
			// readFile_EachLine_from_First_File_exists_in_Second_File_as_String
			String first_File = "";
			String delimiter_to_be_added_to_first_File = "";
			String second_File = "";
			String out_File_exists = "";
			boolean is_Append_outFile_exists = true;
			String out_File_not_exists = "";
			boolean is_Append_outFile_not_exists = true;
			String outFile_File1_addedComment="";
			if (args.length > 0) {
				confFile = args[1]; // second argument from command line
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/14.readFile_EachLine_from_First_File_exists_in_Second_File_as_String/Config.txt";
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}

			// read Config
			if (mapConfig.containsKey("first_File"))
				first_File = mapConfig.get("first_File");
			if (mapConfig.containsKey("delimiter_to_be_added_to_first_File"))
				delimiter_to_be_added_to_first_File = mapConfig
						.get("delimiter_to_be_added_to_first_File");
			if (mapConfig.containsKey("second_File"))
				second_File = mapConfig.get("second_File");
			if (mapConfig.containsKey("out_File_exists"))
				out_File_exists = mapConfig.get("out_File_exists");
			if (mapConfig.containsKey("out_File_not_exists"))
				out_File_not_exists = mapConfig.get("out_File_not_exists");
			if (mapConfig.containsKey("is_Append_outFile_exists"))
				is_Append_outFile_exists = Boolean.valueOf(mapConfig
						.get("is_Append_outFile_exists"));
			if (mapConfig.containsKey("is_Append_outFile_not_exists"))
				is_Append_outFile_not_exists = Boolean.valueOf(mapConfig
						.get("is_Append_outFile_not_exists"));
			outFile_File1_addedComment=first_File+"_addComments.txt";
			
			
			delimiter_to_be_added_to_first_File = delimiter_to_be_added_to_first_File
					.replace("dummy", "");

			System.out.println("Given Configuration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + " " + mapConfig.get(i));
			}
			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();

			
			//
			ReadFile_2_EachLineORtoken_from_First_File_exists_in_Second_File_as_String
			.readFile_EachLineORtoken_from_First_File_exists_in_Second_File_as_String(
											first_File, 
											delimiter_to_be_added_to_first_File,
											-1, //token_interested_in_first_File, 
											second_File,
											"!!!", //delimiter_for_splitting
											out_File_exists,
											is_Append_outFile_exists, 
											out_File_not_exists,
											is_Append_outFile_not_exists,
											outFile_File1_addedComment,
											false //isSOPprint
											);
			

		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("15")) {
			// find_wrapper_for_find_links_from_Google_Search_Engine_for_keywords_from_file
			String input_proxy_file = "";
			String input_Keyword_File = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data/15.find_links_from_Google_Search_Engine/keywords.txt";
			String output_File_1 = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/out_google_search_1.txt";
			boolean is_Append_output_File_1 = true;
			String output_File_2 = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/out_google_search_2.txt";
			boolean is_Append_output_File_2 = true;
			int num_results_required = 20;
			String add_Pattern_to_each_line_of_input_file = " rss";
			String output_already_searched_keywords_File = "";
			if (args.length > 0) {
				confFile = args[1]; // second argument from command line
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/15.find_links_from_Google_Search_Engine/config.txt";
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}

			if (mapConfig.containsKey("input_Keyword_File"))
				input_Keyword_File = mapConfig.get("input_Keyword_File");
			if (mapConfig.containsKey("output_File_1"))
				output_File_1 = mapConfig.get("output_File_1");
			if (mapConfig.containsKey("output_File_2"))
				output_File_2 = mapConfig.get("output_File_2");
			if (mapConfig.containsKey("is_Append_output_File_1"))
				is_Append_output_File_1 = Boolean.valueOf(mapConfig
						.get("is_Append_output_File_1"));
			if (mapConfig.containsKey("is_Append_output_File_2"))
				is_Append_output_File_2 = Boolean.valueOf(mapConfig
						.get("is_Append_output_File_2"));
			if (mapConfig.containsKey("input_proxy_file"))
				input_proxy_file = mapConfig.get("input_proxy_file");
			if (mapConfig.containsKey("output_already_searched_keywords_File"))
				output_already_searched_keywords_File = mapConfig
						.get("output_already_searched_keywords_File");

			System.out.println("Given Configuration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + " " + mapConfig.get(i));
			}
			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();

			// approach 2
			Find_links_from_Google_Search_Engine
					.find_wrapper_for_find_links_from_Google_Search_Engine_for_keywords_from_file(
							input_Keyword_File, output_File_1,
							output_already_searched_keywords_File,
							is_Append_output_File_1, output_File_2,
							is_Append_output_File_2, num_results_required,
							add_Pattern_to_each_line_of_input_file,
							input_proxy_file);

		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("16")) {
			// readFile_wrapper_for_find_String_replaceWith_String_Write_to_Another_File
			String inFolder = "/Users/lenin/GoogleDrive/uaw.test.input/email.feed.all/";
			String outFolder = "/Users/lenin/GoogleDrive/uaw.test.input/email.feed.all.tsv.out/";
			int start_line = -1;
			int end_line = -1;
			String find_string = "!!!";
			String replace_with_string = "\t";

			if (args.length > 0) {
				confFile = args[1]; // second argument from command line
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/16.readFile_wrapper_for_find_String_replaceWith_String_Write_to_Another_File/config.txt";
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}

			//
			if (mapConfig.containsKey("inFolder"))
				inFolder = mapConfig.get("inFolder");
			if (mapConfig.containsKey("outFolder"))
				outFolder = mapConfig.get("outFolder");
			if (mapConfig.containsKey("start_line"))
				start_line = Integer.valueOf(mapConfig.get("start_line"));
			if (mapConfig.containsKey("end_line"))
				end_line = Integer.valueOf(mapConfig.get("end_line"));
			if (mapConfig.containsKey("find_string"))
				find_string = mapConfig.get("find_string");
			if (mapConfig.containsKey("replace_with_string"))
				replace_with_string = mapConfig.get("replace_with_string");

			// find_string=Boolean.valueOf(mapConfig.get("is_Append_output_File_2"));

			System.out.println("Given Configuration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + " " + mapConfig.get(i));
			}
			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();

			// wrapper
			ReadFile_find_String_replaceWith_String_Write_to_Another_File
					.readFile_wrapper_for_find_String_replaceWith_String_Write_to_Another_File(
							inFolder,// inFile,
							find_string, // find String
							replace_with_string, // replace string
							start_line, end_line, outFolder,// outFile,
							true, // is_Append_outFile,
							true // is_Write_To_OutputFile
					);
		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("17")) {
			// URLOFthehinduweb

			String Folder = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/";
			TreeMap<Integer, String> mapOut = null;
			String failed_File = "";
			boolean is_Append_Failed_File = true;
			String success_File = "";
			boolean is_Append_Success_File = true;
			boolean is_Append_outFile = true;
			boolean skip_is_english = true;
			boolean isDebug = true;
			int min_year = 2009;
			int max_year = 2015;
			String stop_pattern_for_url = "2015/07";
			boolean is_ignore_past_ran_success_file_for_skipping_url = true;
			String past_ran_success_file_for_skipping_url = "";
			boolean is_kick_start_pattern_url_true = false;

			if (args.length > 0) {
				mapConfig = new TreeMap<String, String>();
				confFile = args[1]; // second argument from command line
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/17.URLOFthehinduweb/config.txt";
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}
			String kick_start_pattern_url = "";
			String kick_start_pattern_url_forFileName="";
			String stop_pattern_for_url_forFileName="";
			//
			if (mapConfig.containsKey("Folder"))
				Folder = mapConfig.get("Folder");
			if (mapConfig.containsKey("is_Append_Failed_File"))
				is_Append_Failed_File = Boolean.valueOf(mapConfig
						.get("is_Append_Failed_File"));
			if (mapConfig.containsKey("is_Append_Success_File"))
				is_Append_Success_File = Boolean.valueOf(mapConfig
						.get("is_Append_Success_File"));
			if (mapConfig.containsKey("is_Append_outFile"))
				is_Append_outFile = Boolean.valueOf(mapConfig
						.get("is_Append_outFile"));
			if (mapConfig.containsKey("skip_is_english"))
				skip_is_english = Boolean.valueOf(mapConfig
						.get("skip_is_english"));
			if (mapConfig.containsKey("stop_pattern_for_url"))
				stop_pattern_for_url = mapConfig.get("stop_pattern_for_url");
			
			if (mapConfig.containsKey("stop_pattern_for_url_forFileName"))
				stop_pattern_for_url_forFileName = mapConfig.get("stop_pattern_for_url_forFileName");
			
			if (mapConfig.containsKey("min_year"))
				min_year = Integer.valueOf(mapConfig.get("min_year"));
			if (mapConfig.containsKey("max_year"))
				max_year = Integer.valueOf(mapConfig.get("max_year"));
			if (mapConfig.containsKey("isDebug"))
				isDebug = Boolean.valueOf(mapConfig.get("isDebug"));
			if (mapConfig.containsKey("kick_start_pattern_url"))
				kick_start_pattern_url = mapConfig
						.get("kick_start_pattern_url");
			
			if (mapConfig.containsKey("kick_start_pattern_url_forFileName"))
				kick_start_pattern_url_forFileName = mapConfig.get("kick_start_pattern_url_forFileName");
			
			
			if (mapConfig.containsKey("is_kick_start_pattern_url_true"))
				is_kick_start_pattern_url_true = Boolean.valueOf(mapConfig
						.get("is_kick_start_pattern_url_true"));

			if (mapConfig
					.containsKey("is_ignore_past_ran_success_file_for_skipping_url"))
				is_ignore_past_ran_success_file_for_skipping_url = Boolean
						.valueOf(mapConfig
								.get("is_ignore_past_ran_success_file_for_skipping_url"));
			if (mapConfig.containsKey("past_ran_success_file_for_skipping_url")) {
				past_ran_success_file_for_skipping_url = mapConfig
						.get("past_ran_success_file_for_skipping_url");
				past_ran_success_file_for_skipping_url = Folder
						+ past_ran_success_file_for_skipping_url;
			}

			// find_string=Boolean.valueOf(mapConfig.get("is_Append_output_File_2"));

			System.out.println("Given Configuration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + " " + mapConfig.get(i));
			}
			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();

			String pattern="-"+kick_start_pattern_url_forFileName+"-"+stop_pattern_for_url_forFileName;
			pattern=pattern.replace("/", "-");
			
			//THIS Option "17" is faster version of WEB instead of PRINT version,Both should have same news articles, check once
			// pull thehinduweb
			Get_NewsPaper_ArchiveURL.URLOFthehindu_print(
										Folder + "thehindu-web-main"+pattern+".txt",
										is_Append_outFile,
										Folder,
										Folder + "thehindu.allnewsArticles_thehindu_web_URL"+pattern+".txt",
										"thehindu.failed.thehindu_web"+pattern+".txt", // failed_file
										is_Append_Failed_File, // is_Append_Failed_File
										"thehindu.success.thehindu_web"+pattern+".txt", // success_file
										is_Append_Success_File, // is_Append_Success_File
										skip_is_english, // skip_is_english
										stop_pattern_for_url,// stop_pattern_for_url
										min_year, max_year,
										past_ran_success_file_for_skipping_url,
										is_ignore_past_ran_success_file_for_skipping_url, //
										kick_start_pattern_url, is_kick_start_pattern_url_true,
										isDebug // is_Debug
										);
			// rm -rf thehindu-web.txt
			// thehindu.allnewsArticles_thehindu_web_URL.txt
			// thehindu.failed.thehindu_web.txt
			// thehindu.success.thehindu_web.txt

		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("18")) {
			// URLOFthehinduprint06ToTill (*obsolete)
			String Folder = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/";
			TreeMap<Integer, String> mapOut = null;
			String failed_File = "";
			boolean is_Append_Failed_File = true;
			String success_File = "";
			boolean is_Append_Success_File = true;
			boolean is_Append_outFile = true;
			boolean skip_is_english = true;

			if (args.length > 0) {
				confFile = args[1]; // second argument from command line
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/18.URLOFthehinduprint06ToTill/config.txt";
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}

			if (mapConfig.containsKey("Folder"))
				Folder = mapConfig.get("Folder");
			if (mapConfig.containsKey("is_Append_Failed_File"))
				is_Append_Failed_File = Boolean.valueOf(mapConfig
						.get("is_Append_Failed_File"));
			if (mapConfig.containsKey("is_Append_Success_File"))
				is_Append_Success_File = Boolean.valueOf(mapConfig
						.get("is_Append_Success_File"));
			if (mapConfig.containsKey("is_Append_outFile"))
				is_Append_outFile = Boolean.valueOf(mapConfig
						.get("is_Append_outFile"));
			if (mapConfig.containsKey("skip_is_english"))
				skip_is_english = Boolean.valueOf(mapConfig
						.get("skip_is_english"));
			// find_string=Boolean.valueOf(mapConfig.get("is_Append_output_File_2"));

			System.out.println("Given Configuration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + " " + mapConfig.get(i));
			}
			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();

			// may be NO print version from 06-till date
			Get_NewsPaper_ArchiveURL
					.URLOFthehinduprint06ToTill(
							Folder + "thehindu-06-to-till.txt",
							is_Append_outFile, // is_Append_outFile
							Folder,
							Folder
									+ "thehindu.allnewsArticles-thehindu-URL-Print-06-to-till.txt",
							"thehindu.failed.thehindu-print-06-to-till.txt", // failed_file
							is_Append_Failed_File, // is_Append_Failed_File
							"thehindu.success.thehindu-print-06-to-till.txt", // success_file
							is_Append_Success_File, // is_Append_Success_File
							skip_is_english, // skip_is_english
							true // is_Debug
					);
			// rm -rf thehindu-06-to-till.txt
			// thehindu.allnewsArticles-thehindu-URL-Print-06-to-till.txt
			// thehindu.failed.thehindu-print-06-to-till.txt
			// thehindu.success.thehindu-print-06-to-till.txt

		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("19")) {
			// URLOFthehinduprint_any_year_range
			String Folder = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/";
			TreeMap<Integer, String> mapOut = null;
			String failed_File = "";
			boolean is_Append_Failed_File = true;
			String success_File = "";
			boolean is_Append_Success_File = true;
			boolean is_Append_outFile = true;
			boolean skip_is_english = true;
			boolean isDebug = true;
			String past_ran_success_file_for_skipping_url = "";
			boolean is_ignore_past_ran_success_file_for_skipping_url = false;
			String which_set_of_url = "option1";
			String stop_pattern_for_url = "";
			String kick_start_pattern="";
			int start_year_range = 2000;
			int end_year_range = 2005;
			String which_crawler = ""; // {}

			if (args.length > 0) {
				confFile = args[1]; // second argument from command line
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/19.URLOFthehinduprint2000To2005/config.txt";
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}

			if (mapConfig.containsKey("Folder"))
				Folder = mapConfig.get("Folder");
			if (mapConfig.containsKey("is_Append_Failed_File"))
				is_Append_Failed_File = Boolean.valueOf(mapConfig
						.get("is_Append_Failed_File"));
			if (mapConfig.containsKey("is_Append_Success_File"))
				is_Append_Success_File = Boolean.valueOf(mapConfig
						.get("is_Append_Success_File"));
			if (mapConfig.containsKey("is_Append_outFile"))
				is_Append_outFile = Boolean.valueOf(mapConfig
						.get("is_Append_outFile"));
			if (mapConfig.containsKey("skip_is_english"))
				skip_is_english = Boolean.valueOf(mapConfig
						.get("skip_is_english"));
			if (mapConfig.containsKey("isDebug"))
				isDebug = Boolean.valueOf(mapConfig.get("isDebug"));
			if (mapConfig.containsKey("start_year_range"))
				start_year_range = Integer.valueOf(mapConfig
						.get("start_year_range"));
			if (mapConfig.containsKey("end_year_range"))
				end_year_range = Integer.valueOf(mapConfig
						.get("end_year_range"));
			if (mapConfig
					.containsKey("is_ignore_past_ran_success_file_for_skipping_url"))
				is_ignore_past_ran_success_file_for_skipping_url = Boolean
						.valueOf(mapConfig
								.get("is_ignore_past_ran_success_file_for_skipping_url"));
			if (mapConfig.containsKey("past_ran_success_file_for_skipping_url")) {
				past_ran_success_file_for_skipping_url = mapConfig
						.get("past_ran_success_file_for_skipping_url");
				past_ran_success_file_for_skipping_url = Folder
						+ past_ran_success_file_for_skipping_url;
			}
			if (mapConfig.containsKey("which_set_of_url"))
				which_set_of_url = mapConfig.get("which_set_of_url");
			if (mapConfig.containsKey("stop_pattern_for_url")) {
				stop_pattern_for_url = mapConfig.get("stop_pattern_for_url");

			}
			if (mapConfig.containsKey("which_crawler")) {
				which_crawler = mapConfig.get("which_crawler");
				which_crawler = which_crawler.replace("dummy", "");
			}
			if (mapConfig.containsKey("kick_start_pattern")) {
				kick_start_pattern=mapConfig.get("kick_start_pattern");
			}
			
			String year_range = "-" + start_year_range + "-" + end_year_range+ "-";
			// find_string=Boolean.valueOf(mapConfig.get("is_Append_output_File_2"));

			System.out.println("Given Configuration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + " " + mapConfig.get(i));
			}
			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();

			// NOTE:: (DONE) url doesn't have title, hence has to extract title
			// from seed page.
			// http://www.thehindu.com/thehindu/2000/01/01/ <front page>
			// http://www.thehindu.com/thehindu/2000/01/01/02hdline.htm
			// http://www.thehindu.com/thehindu/2000/01/01/03hdline.htm
			// http://www.thehindu.com/thehindu/2000/01/01/04hdline.htm

		//			lenin: temporarily commen this call to Get_NewsPaper_ArchiveURL
			//(Option "17" is faster version of WEB instead of PRINT version,Both should have same news articles, check once)
			// Output of this method is given as input to 
			// readFile_readEachLine_For_a_repeated_pattern_IFmatched_WriteToAnotherFile_thehindu
//			Get_NewsPaper_ArchiveURL
//					.URLOFthehinduprint_any_year_range(
//							Folder + "thehindu.main.monthly" + year_range
//									+ "-opt-" + which_set_of_url + ".txt",
//							Folder,
//							Folder
//									+ "thehindu.allnewsArticles-thehindu-URL-print"
//									+ year_range + "1-opt-" + which_set_of_url
//									+ ".txt",
//							Folder
//									+ "thehindu.allnewsArticles-thehindu-URL-print"
//									+ year_range + "2-opt-" + which_set_of_url
//									+ ".txt",
//							"thehindu.failed.thehindu-URL-print" + year_range
//									+ "-opt-" + which_set_of_url + ".txt", // failed_file
//							is_Append_Failed_File, // is_Append_Failed_File
//							"thehindu.success.thehindu-URL-print" + year_range
//									+ "-opt-" + which_set_of_url + ".txt", // success_file
//							is_Append_Success_File, // is_Append_Success_File
//							is_Append_outFile, // isAppendoutFileForSetOFnewsURLextractedFromEachMonthly
//							skip_is_english, // skip_is_english
//							past_ran_success_file_for_skipping_url,
//							is_ignore_past_ran_success_file_for_skipping_url,
//							which_set_of_url, // {option1,option2}
//							start_year_range, // since 2000
//							end_year_range, // current year
//							kick_start_pattern, //kick_start_pattern
//							stop_pattern_for_url,// stop_pattern_for_url
//							which_crawler, // which_crawler={"dummy","crawler_send_http_request"}
//							isDebug // isDebug
//					);
//			// rm -rf thehindu.main.monthly-2000-2005.txt
			// thehindu.allnewsArticles-thehindu-URL-print-2000-2005.txt
			// thehindu.failed.thehindu-URL-print-2000-2005.txt
			// thehindu.success.thehindu-URL-print-2000-2005.txt
			// thehindu.allnewsArticles-thehindu-URL-print-2000-2005_1.txt
			// thehindu.allnewsArticles-thehindu-URL-print-2000-2005_2.txt

		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("20")) {
			// URLOFthehinduprint2000To2005detailedNational (*obsolete)
			String Folder = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/";
			TreeMap<Integer, String> mapOut = null;
			String failed_File = "";
			boolean is_Append_Failed_File = true;
			String success_File = "";
			boolean is_Append_Success_File = true;
			boolean is_Append_outFile = true;
			boolean skip_is_english = true;
			boolean isDebug = true;
			String past_ran_success_file_for_skipping_url = "";
			boolean is_ignore_past_ran_success_file_for_skipping_url = false;

			if (args.length > 0) {
				confFile = args[1]; // second argument from command line
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/20.URLOFthehinduprint2000To2005detailedNational/config.txt";
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}

			if (mapConfig.containsKey("Folder"))
				Folder = mapConfig.get("Folder");
			if (mapConfig.containsKey("is_Append_Failed_File"))
				is_Append_Failed_File = Boolean.valueOf(mapConfig
						.get("is_Append_Failed_File"));
			if (mapConfig.containsKey("is_Append_Success_File"))
				is_Append_Success_File = Boolean.valueOf(mapConfig
						.get("is_Append_Success_File"));
			if (mapConfig.containsKey("is_Append_outFile"))
				is_Append_outFile = Boolean.valueOf(mapConfig
						.get("is_Append_outFile"));
			if (mapConfig.containsKey("skip_is_english"))
				skip_is_english = Boolean.valueOf(mapConfig
						.get("skip_is_english"));
			if (mapConfig.containsKey("isDebug"))
				isDebug = Boolean.valueOf(mapConfig.get("isDebug"));
			if (mapConfig
					.containsKey("is_ignore_past_ran_success_file_for_skipping_url"))
				is_ignore_past_ran_success_file_for_skipping_url = Boolean
						.valueOf(mapConfig
								.get("is_ignore_past_ran_success_file_for_skipping_url"));
			if (mapConfig.containsKey("past_ran_success_file_for_skipping_url")) {
				past_ran_success_file_for_skipping_url = mapConfig
						.get("past_ran_success_file_for_skipping_url");
				past_ran_success_file_for_skipping_url = Folder
						+ past_ran_success_file_for_skipping_url;
			}

			// find_string=Boolean.valueOf(mapConfig.get("is_Append_output_File_2"));

			System.out.println("Given Configuration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + " " + mapConfig.get(i));
			}
			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();

			// from 23hdline to start
			Get_NewsPaper_ArchiveURL
					.URLOFthehinduprint2000To2005detailedNational(
							Folder
									+ "thehindu.main-wise-00-05-detailed-national.txt",
							Folder,
							Folder
									+ "thehindu.all.print.state-wise-00-05-detailed-national.txt",
							is_Append_outFile, // is_Append_outFileForSetOFnewsURLextractedFromEachMonthly
							"thehindu.failed.thehindu00-05-detailed-national.txt", // failed_file
							is_Append_Failed_File, // is_Append_Failed_File
							"thehindu.success.thehindu00-05-detailed.txt", // success_file
							is_Append_Success_File, // is_Append_Success_File
							skip_is_english, // skip_is_english
							past_ran_success_file_for_skipping_url,
							is_ignore_past_ran_success_file_for_skipping_url,
							isDebug);
			// rm -rf thehindu.main-wise-00-05-detailed-national.txt
			// thehindu.all.print.state-wise-00-05-detailed-national.txt
			// thehindu.failed.thehindu00-05-detailed-national.txt
			// thehindu.success.thehindu00-05-detailed.txt

		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("21")) {
			// URLOFtimesofindia

			String Folder = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/";
			TreeMap<Integer, String> mapOut = null;
			String failed_File = "";
			boolean is_Append_Failed_File = true;
			String success_File = "";
			boolean is_Append_Success_File = true;
			boolean skip_is_english = true;
			// TreeMap<Integer, String> mapOut = thehindu("");
			// TreeMap<Integer, String> mapOut = ndtv("");
			// TreeMap<Integer, String> mapOut =
			// timesofindia(Folder+"timesofIndia.txt");
			String stop_pattern_for_url = "";
			String past_ran_success_file_for_skipping_url = Folder
					+ "timesofindia.success.time_of_india.txt";
			boolean is_ignore_past_ran_success_file_for_skipping_url = false;
			boolean is_Append_outFile = true;

			if (args.length > 0) {
				confFile = args[1]; // second argument from command line
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/21.URLOFtimesofindia/config.txt";
				mapConfig = getConfig(confFile); mapConfig.put("run_type",run_type);
			}

			if (mapConfig.containsKey("Folder"))
				Folder = mapConfig.get("Folder");
			if (mapConfig.containsKey("stop_pattern_for_url")) {
				stop_pattern_for_url = mapConfig.get("stop_pattern_for_url");
				stop_pattern_for_url = stop_pattern_for_url
						.replace("dummy", "");
			}
			if (mapConfig.containsKey("is_Append_Failed_File"))
				is_Append_Failed_File = Boolean.valueOf(mapConfig
						.get("is_Append_Failed_File"));
			if (mapConfig.containsKey("is_Append_Success_File"))
				is_Append_Success_File = Boolean.valueOf(mapConfig
						.get("is_Append_Success_File"));
			if (mapConfig.containsKey("is_Append_outFile"))
				is_Append_outFile = Boolean.valueOf(mapConfig
						.get("is_Append_outFile"));
			if (mapConfig.containsKey("skip_is_english"))
				skip_is_english = Boolean.valueOf(mapConfig
						.get("skip_is_english"));
			if (mapConfig
					.containsKey("is_ignore_past_ran_success_file_for_skipping_url"))
				is_ignore_past_ran_success_file_for_skipping_url = Boolean
						.valueOf(mapConfig
								.get("is_ignore_past_ran_success_file_for_skipping_url"));
			if (mapConfig.containsKey("past_ran_success_file_for_skipping_url")) {
				past_ran_success_file_for_skipping_url = mapConfig
						.get("past_ran_success_file_for_skipping_url");
				past_ran_success_file_for_skipping_url = Folder
						+ past_ran_success_file_for_skipping_url;
			}

			// find_string=Boolean.valueOf(mapConfig.get("is_Append_output_File_2"));

			System.out.println("Given Configuration:");
			for (String i : mapConfig.keySet()) {
				System.out.println(i + " " + mapConfig.get(i));
			}
			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();

			// pull all the required news articles from base archives
			Get_NewsPaper_ArchiveURL
					.URLOFtimesofindia(
							Folder + "timesofindia-main.txt",
							true, // is_Append_outFile
							Folder,
							Folder
									+ "timesofindia.allnewsArticles-times-of-india-URL.txt",
							"timesofindia.failed.time_of_india.txt", // failed_file
							true, // is_Append_Failed_File
							"timesofindia.success.time_of_india.txt", // success_file
							true, // is_Append_Success_File
							true, // skip_is_english
							stop_pattern_for_url,
							past_ran_success_file_for_skipping_url,
							is_ignore_past_ran_success_file_for_skipping_url,
							false // isDebug
					);

		} else if (run_type.equalsIgnoreCase("main") && method_to_run.equalsIgnoreCase("22")) {
			if (args.length > 0) {
				confFile = args[1]; // second argument from command line
			} else {
				mapConfig = new TreeMap<String, String>();
				confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/22.wrapper_getNLPTrained_en_pos_maxent/config.txt";
				mapConfig = getConfig(confFile); mapConfig.put("run_type", run_type);
			}
			TreeMap<String, String> maptaggedNLP = new TreeMap<String, String>();
			String input_ExtractedText_OR_input2 = "";
			String Folder = mapConfig.get("folder");
			String NLPfolder = mapConfig.get("NLPfolder");
			boolean isSOPdebug = Boolean.valueOf(mapConfig.get("isSOPdebug"));
			String debugFileName = Folder + mapConfig.get("debugFileName");
			String inputSingleURL_OR_input1 = mapConfig.get("inputSingleURL_OR_input1");
			String GBAD_output_file = mapConfig.get("GBAD_output_file");
			option = "n";
			System.out.println("Given Configuration:");
			//
			for (String i : mapConfig.keySet()) {
				System.out.println(i + " " + mapConfig.get(i));
			}
			System.out.println(NLPfolder + "--" + Folder + "--" + debugFileName
					+ "--" + inputSingleURL_OR_input1);
			// continue
			option = read_input_from_command_line_to_continue_Yes_No(br);

			if (!option.equalsIgnoreCase("y"))
				System.exit(1);

			t0 = System.nanoTime();

			input_ExtractedText_OR_input2 = "Making classical music easily accessible to a large smartphone user base, Saragama India Ltd., an RPG group enterprise, has introduced an exclusive app that opens up a treasure trove of more than 8000 recordings of about 400 stalwarts of Hindustani classical, Carnatic, and fusion music for music lovers. A galaxy of musicians including Pandit Hariprasad Chaurasia, Pandit Jasraj, Dr Balamuralikrishna, Ustad Amjad Ali Khan, Pandit Ajay Pohankar and poet-lyricist-film-maker Gulzar were present at the introduction of the app “Saregama Classical” at a suburban Mumbai hotel on Tuesday. ";

			input_ExtractedText_OR_input2 = "At least 37 workers died and more than 70 were injured when a series of powerful explosions ripped through a large unit in Sivakasi, the capital of the fireworks industry on Wednesday. Officials quoted doctors here as saying that 35 bodies had been recovered from the site of the blasts at Om Shakthi Fireworks Ltd. Four workers died in hospitals in Madurai, 85km away. Many died in the blaze that spread after the blasts. Several of the survivors were in a critical condition.The explosions and the fire were so devastating that they reduced 40 of 48 sheds in the unit to rubble. Eyewitnesses told police that many victims perished w they rushed to the aid of workers in the block, where the first blasts went off at 12.15pm.";

			input_ExtractedText_OR_input2 = "The remains of four victims have been found in a wooded area behind a strip mall here in central Connecticut that the authorities described on Monday as the dumping ground for a person suspected as a serial killer in seven deaths. It is the same area where the partial skeletons of three women were found in 2007, the police said.“This is certainly the burial site,” Chief James Wardwell of the New Britain Police Department said. "
					+ "Infosys corporation made huge profit. IT is Tuesday. It is 3'o clock. 4:00 pm is good for exercise.I lost 200 USD. I won 10 $.";

			inputSingleURL_OR_input1 = "";
			boolean is_overwrite_flag_with_flag_model=Boolean.valueOf(mapConfig.get("is_overwrite_flag_with_flag_model")); 
			POSModel inflag_model2 = new POSModelLoader().load(new File(NLPfolder+ "en-pos-maxent.bin"));
			InputStream is = new FileInputStream(NLPfolder+"en-parser-chunking.bin");
			ParserModel inflag_model2_chunking = new ParserModel(is);
 
			// wrapper_getNLPTrained_en_pos_maxent
			maptaggedNLP = wrapper_getNLPTrained_en_pos_maxent(
													inputSingleURL_OR_input1, // give input_1 or input_2
													input_ExtractedText_OR_input2, 
													NLPfolder,
													"en-ner-money.bin", // Flag
																		// ={en-pos-maxent.bin,en-ner-person.bin,en-ner-organization.bin,en-ner-location.bin,en-ner-time.bin,en-ner-money.bin}
													"", // flag2
													isSOPdebug,
													is_overwrite_flag_with_flag_model,
													inflag_model2,
													inflag_model2_chunking,
													debugFileName);

			System.out.println("maptaggedNLP:" + maptaggedNLP);
			String taggedNLP = maptaggedNLP.get("taggedNLP");

			// convert_NLP_tagged_string_array_to_GBAD_graph
			// convert_NLP_tagged_string_array_to_GBAD_graph
			// .convert_NLP_tagged_string_array_to_GBAD_graph(taggedNLP,
			// "";
			// );
		} 
		
		//////////////////////////////////////////////////MAIN END////////////////////// ////////////////////////////
		
		//////////////////////////////////////////////////// PROB //////////////////////START////////////////////////////
		//run_type="prob"
		if (run_type.equalsIgnoreCase("prob")) {
			
			if (run_type.equalsIgnoreCase("prob") && method_to_run.equalsIgnoreCase("1")) {
				int startYear = 2014;
				int endYear = 2015;
				String baseFolder = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data/NYT-2014-09-TO-2014-12/";
				String outFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data/NYT-2014-09-TO-2014-12/outRange.txt";
				TreeMap<String, String> map_Customized_List_Of_url_of_NYT = new TreeMap<String, String>();

				String outFile_Already_Crawled = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data/NYT-2014-09-TO-2014-12/success.txt";
				String outFile_writing_crawled_URL_list = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/data/NYT-2014-09-TO-2014-12/NYT_out_crawled.txt";
				String startPattern = "201409"; // to appear
				String api_key_CSV = "23c38a9337878b2ca1c10fca0afda77b:15:72969328,"
						+ "532b9a6915ec648bb56636be3809b77b:15:73018277,"
						+ "ee46096862bd43a1c8d0fa3c20167758:4:73018177";
				// 23c38a9337878b2ca1c10fca0afda77b:15:72969328
				// 532b9a6915ec648bb56636be3809b77b:15:73018277
				// ee46096862bd43a1c8d0fa3c20167758:4:73018177
				TreeMap<String, String> map_api_key = new TreeMap<String, String>();
				String[] api_array = api_key_CSV.split(",");
				int cnt = 0;
				//
				while (cnt < api_array.length) {
					map_api_key.put(api_array[0], api_array[0]);
					cnt++;
				}
	  //add config later
//				String Folder = mapConfig.get("folder"); 
//				String NLPfolder = mapConfig.get("NLPfolder");
//				boolean isSOPdebug = Boolean.valueOf(mapConfig.get("isSOPdebug"));
//				String debugFileName = Folder + mapConfig.get("debugFileName");
//				String inputSingleURL_OR_input1 = mapConfig
//						.get("inputSingleURL_OR_input1");
//				String GBAD_output_file = mapConfig.get("GBAD_output_file");
//				option = "n";
//				System.out.println("Given Configuration:");
				//
				for (String i : mapConfig.keySet()) {
					System.out.println(i + " " + mapConfig.get(i));
				}
			 
				// continue
				option = read_input_from_command_line_to_continue_Yes_No(br);

				if (!option.equalsIgnoreCase("y"))
					System.exit(1);

				t0 = System.nanoTime();

				// collect NYT
				Collect_NYT_news_using_API.main_to_run_nyt_date_range_and_custom_url(outFile,
																			startPattern, startYear, endYear,
																			outFile_Already_Crawled,
																			outFile_writing_crawled_URL_list, baseFolder,
																			map_api_key);
			} else if (run_type.equalsIgnoreCase("prob") && method_to_run.equalsIgnoreCase("2")) {

				TreeMap<String, String> mapout = new TreeMap<String, String>();
				String inFile = "/Users/lenin/Downloads/#problems/p6/merged/merged-trad-29-Jun-policy-26Oct.txt";
				String outFile = "/Users/lenin/Downloads/p6/merged/merged-trad-29-Jun-policy-26Oct_out.txt";
				boolean is_Append_outFile = false;
				mapConfig = new TreeMap<String, String>();
				//
				if (args.length > 0) {
					confFile = args[1]; // second argument from command line
				} else {
					mapConfig = new TreeMap<String, String>();
					// confFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/24.extract_features_from_Each_doc/policy.config.txt";
					confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/p.2.extract_features_from_Each_doc/trad.config.txt";
					// mapConfig=getConfig(confFile);
				}

				mapConfig = Crawler.getConfig(confFile);

				TreeMap<String, String> maptaggedNLP = new TreeMap<String, String>();
				String input_ExtractedText_OR_input2 = "";
				String baseFolder = mapConfig.get("baseFolder");
				// set file path
				inFile = baseFolder + mapConfig.get("inFile");
				outFile = baseFolder + mapConfig.get("outFile");

				String NLPfolder = mapConfig.get("NLPfolder");
				boolean isSOPdebug = Boolean.valueOf(mapConfig.get("isSOPdebug"));
				String debugFileName = baseFolder + mapConfig.get("debugFileName");
				int token_containing_body_text = Integer.valueOf(mapConfig.get("token_containing_body_text"));
				int token_containing_title = Integer.valueOf(mapConfig.get("token_containing_title"));
			    int token_containing_auth_Name=Integer.valueOf(mapConfig.get("token_containing_auth_Name"));
				int token_containing_sourceURL= Integer.valueOf(mapConfig.get("token_containing_sourceURL"));
				int token_containing_keywords= Integer.valueOf(mapConfig.get("token_containing_keywords"));
				int token_containing_date= Integer.valueOf(mapConfig.get("token_containing_date"));
				int Flag_3_feature_parser_types=Integer.valueOf(mapConfig.get("Flag_3_feature_parser_types"));
				
				String start_pattern_containing_author_name = mapConfig.get("start_pattern_containing_author_name"); // from:
				is_Append_outFile = Boolean.valueOf(mapConfig
						.get("is_Append_outFile"));
				int skip_top_N_lines = Integer.valueOf(mapConfig
						.get("skip_top_N_lines"));
				String Flag_2 = mapConfig.get("Flag_2");

				System.out.println("Given Configuration:");
				//
				for (String i : mapConfig.keySet()) {
					System.out.println(i + " " + mapConfig.get(i));
				}

				// continue
				option = read_input_from_command_line_to_continue_Yes_No(br);

				if (!option.equalsIgnoreCase("y"))
					System.exit(1);

				t0 = System.nanoTime();
				// Prepare INPUT Feature
				
				
				p6_new.P6_prepare_Input_feature_file_from_raw_data_p6
				.extract_features_from_Each_doc(
														inFile,//inFile_containing_Authors_AND_Documents
														outFile, 
														is_Append_outFile, // is_Append_outFile
														start_pattern_containing_author_name,
														token_containing_body_text, // body_text
														token_containing_title, // title
														token_containing_auth_Name, // auth Name
														token_containing_sourceURL, // source URL
														token_containing_keywords, //keywords
														token_containing_date, //date
														NLPfolder, // NLP model folder
														debugFileName, 
														isSOPdebug, // is_SOP_print
														skip_top_N_lines, 
														Flag_2,
														Flag_3_feature_parser_types
														);

			} else if (run_type.equalsIgnoreCase("prob") && method_to_run.equalsIgnoreCase("3")) {
				String inputBigFile_Having_JSONToken = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/All.Merged/Merged-All.txt";
				String baseFolder="";
				System.out.println("inputBigFile_Having_JSONToken:"+ inputBigFile_Having_JSONToken);
				// choose interested section names
				mapSS_interested_SectionNames=new TreeMap<String, String>();
				//NOTE: LOWER CASE GIVE
				mapSS_interested_SectionNames.put("n.y. / region", "");
				mapSS_interested_SectionNames.put("u.s.", "");
				mapSS_interested_SectionNames.put("world", "");
				
				// convert_givenString_to_JSON(temp);
				Convert_givenString_to_Json.readFile_eachLine_convert_a_Token_to_JSON_FOR_NYT_N_write2CSVfile(
															baseFolder,	
															inputBigFile_Having_JSONToken, 
															2, // token_index_that_contains_JSON_String
															"",// YES_filter_for_eachLine_of_inputFile,
															"",// NO_filter_for_eachLine_of_inputFile,
															mapSS_interested_SectionNames   //TreeMap<String, String> mapSS_interested_SectionNames
															);
				
		  }
		  else if (run_type.equalsIgnoreCase("prob") && method_to_run.equalsIgnoreCase("4")) {
			    // Run from command prompt 
				if (args.length >= 1) {
					confFile = args[1]; // second argument from command line
					System.out.println("\n Got config File from command prompt:"+ confFile);
				} else if (args.length == 0) {
					confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/p.4/config.txt";
				}
				//CONFIGURATION
				mapConfig = getConfig(confFile);
				String  baseFolder=mapConfig.get("baseFolder");
				int     create_graph_with_only_top_N_lines_of_inputFile=Integer.valueOf(mapConfig.get("create_graph_with_only_top_N_lines_of_inputFile"));
				int		skip_Top_N_lines_while_taggingNLP_of_inputFile=Integer.valueOf(mapConfig.get("skip_Top_N_lines_while_taggingNLP_of_inputFile"));
				//int isskip_Top_N_lines_while_taggingNLP_of_inputFile=Integer.valueOf(mapConfig.get("isskip_Top_N_lines_while_taggingNLP_of_inputFile"));
				boolean	isskip_Tagging_NLP_all_together=Boolean.valueOf(mapConfig.get("isskip_Tagging_NLP_all_together"));
				int		token_index_containingText_tobeTAGGED_in_inputFile=Integer.valueOf(mapConfig.get("token_index_containingText_tobeTAGGED_in_inputFile"));
				boolean	isSOPprint=Boolean.valueOf(mapConfig.get("isSOPprint"));
				boolean	is_debug_File_Write_more=Boolean.valueOf(mapConfig.get("is_debug_File_Write_more"));
				boolean	isskip_d3js_creation=Boolean.valueOf(mapConfig.get("isskip_d3js_creation"));
				boolean	is_overwrite_flag_with_inflag_model2=Boolean.valueOf(mapConfig.get("is_overwrite_flag_with_inflag_model2"));
				boolean	is_remove_stop_words=Boolean.valueOf(mapConfig.get("is_remove_stop_words"));
				boolean	is_do_stemming_global=Boolean.valueOf(mapConfig.get("is_do_stemming_global"));
				boolean	is_remove_CSV_delimiter_in_final_Element_in_graph_created=Boolean.valueOf(mapConfig.get("is_remove_CSV_delimiter_in_final_Element_in_graph_created"));
				boolean	is_remove_NLPtag_in_graph_output=Boolean.valueOf(mapConfig.get("is_remove_NLPtag_in_graph_output"));
				boolean	is_create_each_document_as_an_XP_in_graph=Boolean.valueOf(mapConfig.get("is_create_each_document_as_an_XP_in_graph"));
				boolean	is_write_to_repository_if_token_semantic_Pair=Boolean.valueOf(mapConfig.get("is_write_to_repository_if_token_semantic_Pair"));
				boolean	is_split_on_blank_space_among_tokens=Boolean.valueOf(mapConfig.get("is_split_on_blank_space_among_tokens"));
				boolean	is_RUN_only_create_gbad_xp=Boolean.valueOf(mapConfig.get("is_RUN_only_create_gbad_xp"));
				String	prefix_for_repository=mapConfig.get("prefix_for_repository");
				int		seq=Integer.valueOf(mapConfig.get("seq"));
				String	NLPfolder=mapConfig.get("NLPfolder");
				String	serializedClassifier=mapConfig.get("serializedClassifier");
						debugFile=mapConfig.get("debugFile");
				String	graph_topology_orig=mapConfig.get("graph_topology_orig");
				String	inputFolder_OR_File=mapConfig.get("inputFolder_OR_File");
				// 
				if(inputFolder_OR_File.length()==0)
					inputFolder_OR_File=baseFolder;
				else
					inputFolder_OR_File=baseFolder+inputFolder_OR_File;
				
				// Run from command prompt
				if (args.length >= 1) {
					confFile = args[1]; // second argument from command line
					System.out.println("\n Got config File from command prompt:"+ confFile);
				} else if (args.length == 0) {
					confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/p.4/config.txt";
				}
				//CONFIGURATION
				mapConfig =  Crawler.getConfig(confFile);
				t0 = System.nanoTime();
				TreeMap<String, Integer> map_SourceFeature_idx_mapping=new TreeMap<String, Integer>();
				///addtemp folder
				mapConfig.put("tempFolder", baseFolder+"_temp3/");
				
				System.out.println("debg: "+mapConfig.get("tempFolder"));
				
				//  createGBADgraphFromTextUsingNLP
					
					//call wrapper
					// example <_v,1> ->meaning verb available in map counter <<lineNo>:1>
//					map_SourceFeature_idx_mapping.put("$_n", 100);//noun available in ID=<<lineNo>:100>
//					map_SourceFeature_idx_mapping.put("$_v", 101);//noun available in <<lineNo>:101>
//					map_SourceFeature_idx_mapping.put("fn.sentiment", 102); //example:"fn.sentiment", 102. input to function of sentiment available in <<lineNo>:102>
//					map_SourceFeature_idx_mapping.put("$_n2", 103); //noun2
//					map_SourceFeature_idx_mapping.put("$_number", 104);
//					map_SourceFeature_idx_mapping.put("$_person", 105);
//					map_SourceFeature_idx_mapping.put("$_organization", 106);
//					map_SourceFeature_idx_mapping.put("$_location", 107);
//					map_SourceFeature_idx_mapping.put("$body",  2);
//					map_SourceFeature_idx_mapping.put("$line",  3);
//					map_SourceFeature_idx_mapping.put("$yyyy",  4);
					
					System.out.println("Below feature related configuration added ");
					//iterate all mapConfig and add only that has $ and fn. 
					for(String config_feature_name:mapConfig.keySet()){
						//
						if(( config_feature_name.indexOf("$")>=0 ||  config_feature_name.indexOf("fn.")>=0 ) 
							  &&  config_feature_name.indexOf(".remark" )==-1  ){
							System.out.println( config_feature_name+" "+mapConfig.get(config_feature_name));
							map_SourceFeature_idx_mapping.put( config_feature_name , Integer.valueOf( mapConfig.get(config_feature_name) ) );
						}
					}
					
					// below header of file should match this
//					map_SourceFeature_idx_mapping.put("$snip",  2); //second column in file has document "bodyText"
//					map_SourceFeature_idx_mapping.put("$lpara", 3);
//					map_SourceFeature_idx_mapping.put("$sect",  4); //primary key (first column in the file) <-header of file should match this 
//					map_SourceFeature_idx_mapping.put("$keyw",  6);
//					map_SourceFeature_idx_mapping.put("$organiz", 7);
//					map_SourceFeature_idx_mapping.put("$original", 8);
					
					System.out.println("debg: "+mapConfig.get("tempFolder"));
					// run complete crawler
					if(!is_RUN_only_create_gbad_xp){ //FALSE
						System.out.println("is_RUN_only_create_gbad_xp is FALSE****");
						// wrapper to create GBAD file
						convert.Convert_For_GBAD.wrapper_for_mainType_createGBADgraphFromTextUsingNLP
																						(	
																						baseFolder,
																						mapConfig, 
																						map_SourceFeature_idx_mapping
																						);
						
					}
					/// RUN FROM PAST FILES
					else if (is_RUN_only_create_gbad_xp==true){
						String  newTempFolder=mapConfig.get("tempFolder");
						System.out.println("is_RUN_only_create_gbad_xp is TRUE**");
						BufferedReader reader_map_docidSEQ_mapnodePairNedgeForCreateGrap = new BufferedReader(new FileReader(newTempFolder+"map_input_create_graph_cnt_NodePairEdgeString.txt"));
						BufferedReader reader_map_onebigMap_for_oneGBADfile = new BufferedReader(new FileReader(newTempFolder+"map_onebigMap_for_oneGBADfile.txt"));
						String line="";
						TreeMap<Integer,String> map_input_create_graph_cnt_NodePairEdgeString=new TreeMap<Integer, String>();
						TreeMap<Integer,String> map_onebigMap_for_oneGBADfile=new TreeMap<Integer, String>();
							//
							while ((line = reader_map_onebigMap_for_oneGBADfile.readLine()) != null) {
								String []s=line.split("@@@@@");
								map_onebigMap_for_oneGBADfile.put(Integer.valueOf(s[0]), s[1]);
							}
							//
							while ((line = reader_map_docidSEQ_mapnodePairNedgeForCreateGrap.readLine()) != null) {
								String []s=line.split("@@@@@");
								map_input_create_graph_cnt_NodePairEdgeString.put(Integer.valueOf(s[0]), s[1]);
							}
						// 
						boolean is_append_out_gbad_File=Boolean.valueOf(mapConfig.get("is_append_out_gbad_File"));
						String  outFile_for_gbad_graph=newTempFolder+mapConfig.get("outFile_for_gbad_graph");
						// NOTE: NOT THIS has been tested only for only big XP, not for each document create an XP case
                        convert.Convert_For_GBAD.create_gbad_xp(
														newTempFolder,
								   					    map_onebigMap_for_oneGBADfile,
								   					    map_input_create_graph_cnt_NodePairEdgeString, //map_idSeq_nodePairNedge
													    outFile_for_gbad_graph, //output
													    true, //is_append_out_gbad_File
													    isskip_d3js_creation, //isskip_d3js_creation
													    1, // xp_number,
													    debugFile,
													    is_remove_NLPtag_in_graph_output,
					//								    is_split_on_blank_space_among_tokens, // split on noun , verb
													    is_debug_File_Write_more, // is_debug_File_Write_more
													    isSOPprint,
													    is_RUN_only_create_gbad_xp
													   );
					}
		  }
		  else if (run_type.equalsIgnoreCase("prob") && method_to_run.equalsIgnoreCase("5")) {
				int startLineNo=1;
				int endLineNo=100;
				String delimiter_4_inputDataFileToCreateGBADGraph="";
				boolean isSOPprint=true; boolean isDebugMore=false;
				boolean is_create_each_inFile_as_an_XP_in_graph=false;
				boolean is_header_present=false;
				boolean is_skip_d3js_fileCreation=false;
			    // Run from command prompt 
					if (args.length >= 1) {
						confFile = args[1]; // second argument from command line
						System.out.println("\n Got config File from command prompt:"+ confFile);
					} else if (args.length == 0) {
						confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/p.5/config.txt";
					}
					//CONFIGURATION
					mapConfig = getConfig(confFile);
			   
				// graphtopology="[person]->(with)[ID],[ID]->(is)1.substr(1_to_6)AND2.substr(0_to_2),[person]->(xy)[XY],[XY]->(3)3.substr(1_to_3)AND3.substr(1_to_4)
				String graphtopology=mapConfig.get("s1.graphtopology");
				//String attributefunction=mapConfig.get("attributefunction");
				String WSbaseFolder=mapConfig.get("WSbaseFolder");
				String inputDataFileToCreateGBADGraph_OR_Folder=WSbaseFolder+mapConfig.get("inputDataFileToCreateGBADGraph_OR_Folder");
				String outFile=WSbaseFolder+mapConfig.get("outFile");
				delimiter_4_inputDataFileToCreateGBADGraph=mapConfig.get("delimiter_4_inputDataFileToCreateGBADGraph");
				startLineNo=Integer.valueOf(mapConfig.get("startLineNo"));
				endLineNo=Integer.valueOf(mapConfig.get("endLineNo"));
				is_create_each_inFile_as_an_XP_in_graph=Boolean.valueOf(mapConfig.get("is_create_each_inFile_as_an_XP_in_graph"));
				isSOPprint=Boolean.valueOf(mapConfig.get("isSOPprint"));
				isDebugMore=Boolean.valueOf(mapConfig.get("isDebugMore"));
				is_header_present=Boolean.valueOf(mapConfig.get("is_header_present"));
				is_skip_d3js_fileCreation=Boolean.valueOf(mapConfig.get("is_skip_d3js_fileCreation"));

				TreeMap<Integer,String> mapEdgeLabel_4_ChainNode=new TreeMap<Integer,String>();
				TreeMap<Integer,String> map_dest2_4_ChainNode=new TreeMap<Integer,String>();
                TreeMap<Integer,String> mapID=new TreeMap<Integer,String>();
                TreeMap<Integer,String> mapSTART=new TreeMap<Integer,String>();
                TreeMap<Integer,String> mapEND=new TreeMap<Integer,String>();
                TreeMap<Integer,String> mapOTHER=new TreeMap<Integer,String>();
				// pattern for topology 
				TreeMap<Integer,TreeMap<Integer,String>> graphTopologyMaps= convert.
								                                            ConvertPatternStringToMap.convertStringToMap(graphtopology,",","->");
				System.out.println("Given graphtopology=>"+graphtopology);
				System.out.println("Given graphTopologyMaps=>"+graphTopologyMaps.size() +" - "+graphTopologyMaps);
				TreeMap<Integer,String> mapSourceVertex=graphTopologyMaps.get(1);
				TreeMap<Integer,String> mapDestVertex=graphTopologyMaps.get(2);
				TreeMap<Integer,String> mapDistinct_NewVertexID_OldVertexID=graphTopologyMaps.get(3);
				TreeMap<Integer,String> mapEdgeLabel=graphTopologyMaps.get(4);
				
				if(graphTopologyMaps.containsKey(7))
					mapID=graphTopologyMaps.get(7);
				if(graphTopologyMaps.containsKey(8))
					mapSTART=graphTopologyMaps.get(8);
				if(graphTopologyMaps.containsKey(9))
					mapEND=graphTopologyMaps.get(9);
				if(graphTopologyMaps.containsKey(10))
					mapOTHER=graphTopologyMaps.get(10);
				// below two are tightly linked..
                if(graphTopologyMaps.containsKey(5)){
				    mapEdgeLabel_4_ChainNode = graphTopologyMaps.get(5);
                }
                if(graphTopologyMaps.containsKey(6))
				    map_dest2_4_ChainNode = graphTopologyMaps.get(6);
				
				System.out.println("size:"+mapSourceVertex 
									+"\n#mapSourceVertex:"+mapSourceVertex 
									+"\n#mapDestVertex:"+mapDestVertex 
									+"\n#mapDistinct_NewVertexID_OldVertexID:"+mapDistinct_NewVertexID_OldVertexID
									+"\n#mapEdgeLabel:"+mapEdgeLabel
									+"\n#mapEdgeLabel_4_ChainNode:"+mapEdgeLabel_4_ChainNode
									+"\n#map_dest2_4_ChainNode:"+map_dest2_4_ChainNode
                                    +"\n#mapID:"+mapID
                                    +"\n#mapSTART:"+mapSTART
                                    +"\n#mapEND:"+mapEND
                                    +"\n#mapOTHER:"+mapOTHER
                                  );
				int ID__=-1;String START_edgeLabel_=""; String END_edgeLabel_="";String OTHER_edgeLabel_="";
				TreeMap<Integer,TreeMap<String, TreeMap<Integer, String>>> mapOUTPUT_ID_N_Source_EdgeLabel_Destination=new TreeMap<Integer, TreeMap<String,TreeMap<Integer,String>>>();
				
				TreeMap<Integer, TreeMap<Integer,TreeMap<String, TreeMap<Integer, String>>>> mapOUTPUT_lineNumber_ID_N_Source_EdgeLabel_Destination
																		=new TreeMap<Integer,TreeMap<Integer, TreeMap<String,TreeMap<Integer,String>>>>();
				
				// NOTE : THIS can process >=1  set of trees, if wanted more than 1, then MULTIPLE iteration on this method needed 
				for(int lineNumber:mapID.keySet()){
						//
						ID__=Integer.valueOf(mapID.get(lineNumber));
						START_edgeLabel_=mapSTART.get(lineNumber);
						END_edgeLabel_=mapEND.get(lineNumber);
						OTHER_edgeLabel_=mapOTHER.get(lineNumber);
						int lineNumber_fromSourceEdgeDestMappings=lineNumber;
					
			        // ***CREATE intermediate*** when ID is given processing is required to create an intermediate file
					mapOUTPUT_ID_N_Source_EdgeLabel_Destination   =					create_intermediate_file(
																											lineNumber_fromSourceEdgeDestMappings,
														                                                    inputDataFileToCreateGBADGraph_OR_Folder,
														                                                    WSbaseFolder+"intermediate.txt",
														                                                    mapSourceVertex,
														                                                    mapDestVertex,
														                                                    mapEdgeLabel,
														                                                    mapEdgeLabel_4_ChainNode,
														                                                    map_dest2_4_ChainNode,
														                                                    ID__, //primary key
														                                                    START_edgeLabel_,
														                                                    END_edgeLabel_,
														                                                    OTHER_edgeLabel_,
														                                                    delimiter_4_inputDataFileToCreateGBADGraph,
														                                                    is_header_present,
														                                                    isSOPprint
														                                            	    );
					
					// accumulate results of each of tree (i.e., chain of nodes w.r.t ID)
					mapOUTPUT_lineNumber_ID_N_Source_EdgeLabel_Destination.put(lineNumber, mapOUTPUT_ID_N_Source_EdgeLabel_Destination);
				
				}
				
				if(isSOPprint)
					System.out.println("@@@@@@@ finaly:"+mapOUTPUT_lineNumber_ID_N_Source_EdgeLabel_Destination);
				// SET 1
		        TreeMap<String, TreeMap<Integer, String>> mapOut_ID_N_Source_EdgeLabel_Destination_MAIN=mapOUTPUT_ID_N_Source_EdgeLabel_Destination.get(1);
				// SET 2 (PART 1)
		        TreeMap<String, TreeMap<Integer, String>> mapOut_ID_N_Source_EdgeLabel_Destination_ZERO=mapOUTPUT_ID_N_Source_EdgeLabel_Destination.get(2);
				// SET 2 (PART 2)
		        TreeMap<String, TreeMap<Integer, String>> mapOut_ID_N_Source_EdgeLabel_Destination_ONE=mapOUTPUT_ID_N_Source_EdgeLabel_Destination.get(3);
		        if(isSOPprint)
		        	System.out.println(" gotch gotch..."+mapOUTPUT_ID_N_Source_EdgeLabel_Destination); 
				String comment="";
				for(int i:mapOUTPUT_ID_N_Source_EdgeLabel_Destination.keySet()){
					TreeMap<String, TreeMap<Integer, String>> t10=mapOUTPUT_ID_N_Source_EdgeLabel_Destination.get(i);
					System.out.println("-----------------------------------------");
					if(i==1) comment="SET 1"; else if(i==2) comment="SET 2 (PART 1)";else if(i==3) comment="SET 2 (PART 2)";
					if(isSOPprint){
						System.out.println(" i:"+i+" "+t10);
						System.out.println("-----------------------------------------"+comment);
					}
					for(String h:t10.keySet()){
						if(isSOPprint)
							System.out.println("h:"+h+" "+t10.get(h));
					}
				}

				try{
					FileWriter outWriter=new FileWriter(outFile);
					String outFile_of_outWriter=outFile; //just copy
					String graphtopology_STRING_for_DEBUG=graphtopology; //just copy
					// create GBAD input graph file
                    convert.
					Convert_For_GBAD.createCSVtoGBADgraph(   WSbaseFolder,
															 mapSourceVertex,
															 mapDestVertex,
															 mapEdgeLabel,
															 mapDistinct_NewVertexID_OldVertexID,
                                                             mapEdgeLabel_4_ChainNode,
                                                             map_dest2_4_ChainNode,
                                                             mapID,
															 mapSTART,
															 mapEND,
															 mapOTHER,
															 mapOUTPUT_lineNumber_ID_N_Source_EdgeLabel_Destination,
															 graphtopology_STRING_for_DEBUG, //for debug
															 inputDataFileToCreateGBADGraph_OR_Folder,
															 delimiter_4_inputDataFileToCreateGBADGraph,
															 outWriter,
															 outFile_of_outWriter,
															 startLineNo,
															 endLineNo,
															 is_create_each_inFile_as_an_XP_in_graph,
															 is_header_present, //NOTE:HEADER always assumed to be true..
															 isSOPprint,
															 is_skip_d3js_fileCreation,
															 isDebugMore
														 );
					
				}
				catch(Exception e){
					e.printStackTrace();
				}

		  }
		  else if (run_type.equalsIgnoreCase("prob") && method_to_run.equalsIgnoreCase("6")){
//                convertGBAD2JSON.hallo(); // class
//                convertGBAD2JSON.hallo();  //  object [2] (static)
//                convertGBAD2JSON$.MODULE$.hello();  // object [3] (hidden static)


			}


		} //end if (run_type.equalsIgnoreCase("prob")) {
		
		//////////////////////////////////////////////////// PROB //////////////////////END////////////////////////////
		
		
		System.out.println("Time Taken:"
				+ NANOSECONDS.toSeconds(System.nanoTime() - t0) + " seconds; "
				+ (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
				+ " minutes");

		// read Nth Token
		// readNthTokenFromWithORWithoutGivenPatternForGivenFileandRemoveDuplicationWrite2File(
		// "//Users//lenin//Downloads//uaw.test.input//outURL.prev.txt", 2,
		// "!!!",
		// "http", "www", "http://",
		// "www.","//Users//lenin//Downloads//uaw.test.input//outURL.prev.NoDup.txt",
		// "//Users//lenin//Downloads//uaw.test.input//outURLDebug.prev.DupliateDebug.txt");

		outAlreadyCrawledURLsINaFile = "//Users//lenin//GoogleDrive//uaw.test.input//outDebugAlreadyCrawledURLsINaFile.txt";
		// getCrawler(folder, folder+"//09//outURL.txt","",
		// folder+"//09//outParsedHTML.txt" ,"file");

		// getSingleURLCrawler();

		// findBlankColumnAndUpdate(folder);

		// .
		/*
		 * String str=
		 * "AN 18-year-old girl was shot dead by her brother in the Kahna area on Tuesday. The deceased was identified Jameela, daughter of Abdul Rehman of Bohgal village, Kahna. Police said accused Zahid murdered his daughter on suspicion. The victim’s family claimed that Zahid got infuriated when he was asked to complete his sister’s dowry as her marriage ceremony was due after six days.On the day of the incident, he shot her dead when she was asleep. The accused escaped from the scene. Police have registered a case and removed the body to morgue for autopsy."
		 * ; HashMap<Integer,String> map =
		 * Wrapperget2NLPTrained(folder,"en-pos-maxent.bin", str);
		 * System.out.println("tagged output: " +map.get(1));
		 */

		// "Programcreek is a very huge and useful website."
		String[] s = null;
		// getNLPTrained(folder,"en-ner-person.bin","Programcreek is a very huge and useful website.",s);

		folder = "//Users//lenin//Library//Mail//V2//IMAP-uglyartworld@imap.gmail.com//INBOX.mbox//607D4241-C58A-40CF-A186-08075A3BEA63//Data//0//1//2//1//Messages//";

		// folder="//Users//lenin//Library//Mail//V2//IMAP-uglyartworld@imap.gmail.com//[Gmail].mbox//All Mail.mbox//607D4241-C58A-40CF-A186-08075A3BEA63//";
		// TreeMap<Integer,String> mapFoldersList =
		// getMessageFoldersFullPathFromGivenTopFolder(folder,
		// "//Users//lenin//Downloads//uaw.test.input//debugoutput.txt",
		// 10);
		// //load the output file of "readFolderEmailFilesUAW"
		// inputFile="//Users//lenin//Downloads//uaw.test.input//outURL.prev.txt";
		// TreeMap<Integer, TreeMap<String, String>> mapReadMapFromFile =
		// LoadMultipleValueToKeyMap3(inputFile, "!!!", debugFie);
		//
		// System.out.println("input:"+mapReadMapFromFile.get(1));
		//
		// // folder list
		// for(int g:mapFoldersList.keySet()){ //map is out of
		// getMessageFoldersFullPathFromGivenTopFolder
		// folder = mapFoldersList.get(g);
		// System.out.println("curr folder:"+folder);
		// boolean overwriteOutputFile = true; //true for append
		// readFolderEmailFilesUAW (folder,
		// "//Users//lenin//Downloads//uaw.test.input//uaw.read.debug.txt",
		// "//Users//lenin//Downloads//uaw.test.input//outURL.txt",
		// "2014",
		// overwriteOutputFile,
		// mapReadMapFromFile);
		// }

		folder = "//Users//guest2//Downloads//uaw.test.input//";
		// readFolderEmailFilesUAW (folder ,
		// "//Users//lenin//Downloads//uaw.test.input//uaw.read.debug.txt",
		// "//Users//lenin//Downloads//outURL.txt",
		// "2014");

	}

}