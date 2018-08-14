/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.VerbSynset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.impl.file.synset.VerbReferenceSynset;
import edu.sussex.nlp.jws.JWS;
import net.didion.jwnl.JWNLException;
import crawler.Stopwords;
        

/**
 *
 * @author lenin
 */
public class Wordnet {
 
   

    
	//wordnet extension
	public static String wordnet_extension(  
											 String inString,
											 int print_top_N_definition,
											 boolean is_remove_stop_words,
											 String inSynsetType
											 ){
		String outString="";Synset[] synsets = new Synset[1];
        NounSynset[] hyponyms = null; 
        VerbSynset[] vhyponyms= null;
        try{
            //System.setProperty("wordnet.database.dir", "C:\WordNet-3.0\dict\");
            //System.setProperty("wordnet.database.dir","//Users//lenin//OneDrive//jar//WordNet-2.0//dict//");
        	System.setProperty("wordnet.database.dir","//Users//lenin//OneDrive//jar//WordNet-3.0//dict//");
            NounSynset nounSynset = null; 
            VerbReferenceSynset verbSynset=null;

            
            //WordNetDatabase database = WordNetDatabase.getFileInstance();
            WordNetDatabase database = WordNetDatabase.getFileInstance();
            
            // WHAT TYPE parser
            if(inSynsetType.equalsIgnoreCase("VERB"))
            		synsets = database.getSynsets(inString, SynsetType.VERB);
            else if(inSynsetType.equalsIgnoreCase("NOUN"))
            		synsets = database.getSynsets(inString, SynsetType.NOUN );
            
            //SynsetType.VERB
            
            String concLine="";
            for (int i = 0; i < synsets.length; i++) {
            	
            	
            	if(inSynsetType.equalsIgnoreCase("NOUN")){
	                nounSynset = (NounSynset)(synsets[i]); 
	                hyponyms = nounSynset.getHyponyms();
	                
	                if(!nounSynset.getWordForms()[0].equalsIgnoreCase(inString)){
	                	//System.out.println("NOT EQU:"+nounSynset.getWordForms()[0]+"<-->"+inString);
	                	outString=nounSynset.getWordForms()[0]+" "+nounSynset.getDefinition();
	                }
	                else
	                	outString=nounSynset.getDefinition();
	                
	                ///print 
//	                if(i<=print_top_N_definition){
//		                System.err.println(nounSynset.getWordForms()[0] + 
//		                        ": " + nounSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms");
//	                }
	                
            	}
            	if(inSynsetType.equalsIgnoreCase("VERB")){
            		verbSynset = (VerbReferenceSynset) synsets[i]; 
            		vhyponyms = verbSynset.getHypernyms();
            		
	                if(!verbSynset.getWordForms()[0].equalsIgnoreCase(inString)){
	                	//System.out.println("NOT EQU:"+nounSynset.getWordForms()[0]+"<-->"+inString);
	                	outString=verbSynset.getWordForms()[0]+" "+verbSynset.getDefinition();
	                }
	                else
	                	outString=verbSynset.getDefinition();
            		
            	}
                
                
                
                
                if(is_remove_stop_words){
                	String []s=outString.split(" ");
                	int cnt=0;
                	while(cnt<s.length){
                		//not stop words
                		if( !Stopwords.is_stopword(s[cnt]) && !s[cnt].equalsIgnoreCase(inString) ){
                			if(concLine.length()==0)
                				concLine=s[cnt];
                			else
                				concLine=concLine+" "+s[cnt];
                		}
                			
                		cnt++;
                	}
                	outString=concLine;
                }
                else{
                	String []s=outString.split(" ");
                	int cnt=0;
                	
                	while(cnt<s.length){
                		 
                			if(concLine.length()==0)
                				concLine=s[cnt];
                			else
                				concLine=concLine+" "+s[cnt];
                		
                			
                		cnt++;
                	}
                	outString=concLine;
                }
                
                 
                if(i>0) break;
                
            }
            
            //remove what is inside ()
            if(outString.indexOf("(")>=0 && outString.indexOf(")")>=0){
            	String prefix=outString.substring(0, outString.indexOf("("));
            	String suffix=outString.substring( outString.indexOf(")")+1 , outString.length());
            	outString=prefix+" "+suffix;
            }
            outString=outString.replace(";", " ").replace("  "," ").replace("  "," ");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return outString;
    }
	
    //
    public static void wordnet(String inString){
        try{
            //System.setProperty("wordnet.database.dir", "C:\WordNet-3.0\dict\");
            //System.setProperty("wordnet.database.dir","//Users//lenin//OneDrive//jar//WordNet-2.0//dict//");
        	System.setProperty("wordnet.database.dir","//Users//lenin//OneDrive//jar//WordNet-3.0//dict//");
            NounSynset nounSynset; 
            NounSynset[] hyponyms; 
            //WordNetDatabase database = WordNetDatabase.getFileInstance();
            WordNetDatabase database = WordNetDatabase.getFileInstance(); 
            Synset[] synsets = database.getSynsets(inString, SynsetType.NOUN); 
            for (int i = 0; i < synsets.length; i++) {
                nounSynset = (NounSynset)(synsets[i]); 
                hyponyms = nounSynset.getHyponyms(); 
                System.err.println(nounSynset.getWordForms()[0] + 
                        ": " + nounSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms"); 
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    // -> NOT TESTED - needed edu.mit.jwi_2.1.4.jar and edu.sussex.nlp.jws.beta.11.jar
    public static String stem_wordnet(String inString){
    	
    	// http://www.programcreek.com/java-api-examples/index.php?api=edu.smu.tspell.wordnet.SynsetType
    	
    	String stem_word="";
    	try{
    		//System.setProperty("wordnet.database.dir","//Users//lenin//OneDrive//jar//WordNet-3.0//dict//");
    		JWS ws = new JWS("//Users//lenin//OneDrive//jar//WordNet-3.0","");
    		WordnetStemmer stem =  new WordnetStemmer(ws.getDictionary());
    		System.out.println("test" + stem.findStems("reading", POS.VERB) );
    		stem_word=stem.findStems("reading" , POS.VERB).toString();
    	}
    	catch(Exception e){
    		
    	}
    	return stem_word;
    }
    
    	public static void main(String[] args) {
            //wordnet("talk");
            System.out.println("got:"+wordnet_extension("bangladesh", 8, true,"verb"));
          
        }
    
    
}
