package crawler;

import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.TextExtractor;

public class DisplayAllElements {

	public static void main(String[] args) throws Exception {
		String sourceUrlString="http://www.thehindu.com/archive/web/2009/09/01/";
		sourceUrlString="http://www.thehindu.com/archive/print/2010/01/01/";
		if (args.length==0)
		  System.err.println("Using default argument of \""+sourceUrlString+'"');
		else
			sourceUrlString=args[0];
		if (sourceUrlString.indexOf(':')==-1) sourceUrlString="file:"+sourceUrlString;
		Source source=new Source(new URL(sourceUrlString));
		List<Element> linkElements=source.getAllElements(HTMLElementName.A);
		for (Element linkElement : linkElements) {
			String href=linkElement.getAttributeValue("href");
			if (href==null) continue;
			// A element can contain other tags so need to extract the text from it:
			String label=linkElement.getContent().getTextExtractor().toString();
			
			if(href.indexOf(".ece")>=0){
				System.out.println("label:"+label+" href:<"+href+'>');
			}
		}
		
		System.out.println("\nSame again but this time extend the TextExtractor class to also exclude text from P elements and any elements with class=\"control\":\n");
		TextExtractor textExtractor=new TextExtractor(source) {
			public boolean excludeElement(StartTag startTag) {
				return startTag.getName()==HTMLElementName.P || "control".equalsIgnoreCase(startTag.getAttributeValue("class"));
			}
		};
		
  }
	
	
	
}
