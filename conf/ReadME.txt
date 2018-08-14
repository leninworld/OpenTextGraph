----------------------------------------------------------------------------------------------------
getCrawler() method
----------------------------------------------------------------------------------------------------

outFlag: {"outNewUnCrawledURLFile", "writeCrawledText2OutFile", "mongodb"}
inFlag : {"file","mongodbURLonly","dummy"}

Below are four different types:
##Type: Input: file ; output to mongodb
input approach 1: URL file <email_file_name http_url>
				  output: write to mongodb.

## Test Case: single input URL and output is just map
input approach 2: input an (single) URL and 
				  output (return a map) Map  (a) header (b) crawled page

##Typical: to read input file and output file {outFlag: "writeCrawledText2OutFile"}
input approach 3: 
-Input URL file <email_file_name http_url>
-Option to choose N token (token which has URL) is given 
				  
##Typical: To get new un-Crawled URL file  
Approach 4: input URL file <email_file_name http_url> 
Input: crawledOutFile(s); {Param: inputListOf_URLfileCSV }
Output: Get URL list which are not crawled {Param: outFlag:"outNewUnCrawledURLFile"	}

Approach 5: input: URL from mongodb {inFlag="mongodbURLonly"}
				  output: write to mongodb.

It parses "body" and parse. Also taggedNLP. write to mongodb.
				  
Approach 6: input:    {inFlag: "mongodburlandbody"}
			output: write taggedNLP to mongodb 
			
This means we already have CRAWLED TEXT (rendederedtext, body, header etc.), we do NLP only 
later left for extract features or both.
				  

----------------------------------------------------------------------------------------------------
prepAlertStringsForCountryRStatesRCity.generateAlertStringListForEachCityRStateRCountry
----------------------------------------------------------------------------------------------------
generateAlertStringListForEachCityRStateRCountry(
									  folder+"testInputCityList.txt",
									  folder+"testlistcrime.txt",
									  folder+"outList.txt"
									  );
testInputCityList.txt
---------------------
cookeville tennessee
sacramento california
toronto canada

testlistcrime.txt
-----------------
drug
violence
thief

outList.txt
-----------
cookeville tennessee drug
sacramento california drug
toronto canada drug
cookeville tennessee violence
sacramento california violence
toronto canada violence
cookeville tennessee thief
sacramento california thief
toronto canada thief

----------------------------------------------------------------------------------------------------
Tested:
-------
Case 1:
inputType=file
outputType=mongodb
Insert and Update
Status: Succeed

Case 2:
inputType=mongodbURLonly
outputType=mongodb

Case 3:
inputType=mongodbURLonly
outputType=outfile

Case 4:
inputType=mongodburlandbody
outputType=mongodb
----------------------------------------------------------------------------------------------------

Testing:
------
1) Check if  "CSV of authors" from XML feed coming without missing anyone.
2) 

Pending:
-------
ForkJoin

----------------------------------------------------------------------------------------------------
NLP
----
crawler.getNLPTrained
----------------------------------------------------------------------------------------------------
XML Feed parsing
-----------------
readParseXMLFeeds()

Case 1: SUCCESS
Input: A folder with two feeds. one feed for static pattern(config.txt) and another dynamic
Output: extracts features from feed
 
Case 2: FAILED
Input: A folder with two feeds. one feed for static pattern(config.txt) and another dynamic
	   and set value for filters-> continueParsingFromThisPatternMMM and 
	   continueParsingFromThisPatternYYYY.
Output: extracts features from feed

