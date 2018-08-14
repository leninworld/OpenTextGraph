package crawler;

public class Correct_dot_and_tag_misplaced {
	
	// correct_dot_and_tag_misplaced
	public static String correct_dot_and_tag_misplaced(String inString){
		String outString="";
		
		try {
			// ex in_IN the_DT region._NN
//			int index=inString.indexOf("._");
//			
//			if(index >=0){
//				  prefix=inString.substring(0,  index);
//				//suffix will be space (as its added in getNLPTrained first line ->strSentence=strSentence.replace(".", ".  "); //otherwise get gets dirty like region."_NN
//				  suffix=inString.substring(index+2, inString.length());
//				
//			}
//			
//			System.out.println("prefix->"+prefix+"<-suffix:"+suffix+"<-");
			
			outString=inString.replace(".__CC","__CC.")
								.replace(".__CD","__CD.")
								.replace(".__DT","__DT.")
								.replace(".__EX","__EX.")
								.replace(".__FW","__FW.")
								.replace(".__IN","__IN.")
								.replace(".__JJR","__JJR.")
								.replace(".__JJS","__JJS.")
								.replace(".__JJ","__JJ.")
								.replace(".__LS","__LS.")
								.replace(".__MD","__MD.")
								.replace(".__NNPS","__NNPS.")
								.replace(".__NNS","__NNS.")
								.replace(".__NNP","__NNP.")
								.replace(".__NN","__NN.")
								.replace(".__PRP$","__PRP$.")
								.replace(".__PDT","__PDT.")
								.replace(".__POS","__POS.")
								.replace(".__PRP","__PRP.")
								.replace(".__RBR","__RBR.")
								.replace(".__RBS","__RBS.")
								.replace(".__RP","__RP.")
								.replace(".__RB","__RB.")
								.replace(".__SYM","__SYM.")
								.replace(".__TO","__TO.")
								.replace(".__UH","__UH.")
								.replace(".__VBZ","__VBZ.")
								.replace(".__VBP","__VBP.")
								.replace(".__VBD","__VBD.")
								.replace(".__VBN","__VBN.")
								.replace(".__VBG","__VBG.")
								.replace(".__VB","__VB.")
								.replace(".__WDT","__WDT.")
								.replace(".__WP$","__WP$.")
								.replace(".__WRB","__WRB.")
								.replace(".__WP","__WP.")
								.replace(".__,","__,.")
								.replace(".__:","__:.")
								.replace(".__(","__(.")
								.replace(".__)","__).");
			

			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		 
		
		return outString;
	}
	
	//main
	public static void main(String[] args) throws Exception {
		//
		System.out.println(
		correct_dot_and_tag_misplaced("china__NNP experimented__VBD past__NN political__JJ systems,__NNS including__VBG multi-party__JJ democracy,__NN work,__NN president__NN xi__NN jinping__NN visit__NN europe,__VB warning__NN copying__VBG foreign__JJ political__JJ development__NN models__NNS catastrophic.__DT This__DT great.__JJR \"__''")
		);
	}
	

}
