package crawler;

import java.util.TreeMap;


public class Stopwords {
	
	 
	//is_stopword
	public static boolean is_stopword(String word_To_check){
		try{
			TreeMap<String,String> stopwords=new TreeMap();
			stopwords = stopwords();
			if(stopwords.containsKey(word_To_check)){
				return true;
			}
			else{
				return false;
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	public static TreeMap<String, String> get_FocusedWords(){
		TreeMap<String, String> mapOut=new TreeMap<String, String>();
		TreeMap<String, String> mapOut_new=new TreeMap<String, String>();
		try{
			
			mapOut.put("worst","");
			mapOut.put("health organization","");
			mapOut.put("warning system","");
			mapOut.put("warned","");
			mapOut.put("wanted","");
			mapOut.put("users","");
			mapOut.put("university","");
			mapOut.put("unit","");
			mapOut.put("undertaken","");
			mapOut.put("understand","");
			mapOut.put("undergoing","");
			mapOut.put("trend","");
			mapOut.put("trained","");
			mapOut.put("tool","");
			mapOut.put("tests","");
			mapOut.put("team","");
			mapOut.put("taken up","");
			mapOut.put("take steps","");
			mapOut.put("system<>launch","");
			mapOut.put("system","");
			mapOut.put("supervisor","");
			mapOut.put("stringent","");
			mapOut.put("stepped","");
			mapOut.put("statistic","");
			mapOut.put("specialist","");
			mapOut.put("special session","");
			mapOut.put("special<>group","");
			mapOut.put("source","");
			mapOut.put("since","");
			mapOut.put("setting up","");
			mapOut.put("secretary<>state","");
			mapOut.put("scientist","");
			mapOut.put("science","");
			mapOut.put("safety norm","");
			mapOut.put("safety<>not","");
			mapOut.put("role<>help","");
			mapOut.put("resolut","");
			mapOut.put("reform","");
			mapOut.put("rally","");
			mapOut.put("punish","");
			mapOut.put("protect ","");
			mapOut.put("progress","");
			mapOut.put("professor","");
			mapOut.put("preventive","");
			mapOut.put("precaution","");
			mapOut.put("power","");
			mapOut.put("possible help","");
			mapOut.put("pledge","");
			mapOut.put("phenomeno","");
			mapOut.put("percent","");
			mapOut.put("per cent","");
			mapOut.put("opinion","");
			mapOut.put("ombudsmen","");
			mapOut.put("official","");
			mapOut.put("officer","");
			mapOut.put("number","");
			mapOut.put("news conference","");
			mapOut.put("new application","");
			mapOut.put("negotiate","");
			mapOut.put("national institute","");
			mapOut.put("municipal corporation","");
			mapOut.put("municipal","");
			mapOut.put("movement","");
			mapOut.put("model","");
			mapOut.put("ministry","");
			mapOut.put("minimi","");
			mapOut.put("million","");
			mapOut.put("method<>help","");
			mapOut.put("medical work","");
			mapOut.put("machine<>help","");
			mapOut.put("litigation ","");
			mapOut.put("limited","");
			mapOut.put("limit","");
			mapOut.put("legislati<>pass","");
			mapOut.put("Lawye","");
			mapOut.put("laboratory","");
			mapOut.put("killed more","");
			mapOut.put("Investigat","");
			mapOut.put("introduction","");
			mapOut.put("introduc","");
			mapOut.put("interim compensation","");
			mapOut.put("institute","");
			mapOut.put("inspection","");
			mapOut.put("independent regulators","");
			mapOut.put("include","");
			mapOut.put("implement","");
			mapOut.put("implemen","");
			mapOut.put("human resources","");
			mapOut.put("helps","");
			mapOut.put("helpline","");
			mapOut.put("help the government","");
			mapOut.put("help senior","");
			mapOut.put("help reduce","");
			mapOut.put("help minimiz","");
			mapOut.put("help government","");
			mapOut.put("help fight","");
			mapOut.put("help avoid","");
			mapOut.put("help<>stop","");
			mapOut.put("help<>speed","");
			mapOut.put("help<>rescue","");
			mapOut.put("help<>office","");
			mapOut.put("help<>method","");
			mapOut.put("help<>lack","");
			mapOut.put("help<>monitor","");
			mapOut.put("held","");
			mapOut.put("guideline","");
			mapOut.put("government","");
			mapOut.put("general secretary","");
			mapOut.put("fund","");
			mapOut.put("forc","");
			mapOut.put("flagged","");
			mapOut.put("finding","");
			mapOut.put("federatio","");
			mapOut.put("extend<>help","");
			mapOut.put("expert","");
			mapOut.put("expert","");
			mapOut.put("executive","");
			mapOut.put("evidence","");
			mapOut.put("escala","");
			mapOut.put("enact","");
			mapOut.put("effort<>improve","");
			mapOut.put("effect","");
			mapOut.put("divisional<>officer","");
			mapOut.put("division regional   ","");
			mapOut.put("division<>deputy","");
			mapOut.put("disaster<>management","");
			mapOut.put("Directorate","");
			mapOut.put("directive","");
			mapOut.put("development","");
			mapOut.put("deputy speaker","");
			mapOut.put("deput","");
			mapOut.put("department","");
			mapOut.put("decision","");
			mapOut.put("death<>rise","");
			mapOut.put("crisis","");
			mapOut.put("crack<>down","");
			mapOut.put("corrective","");
			mapOut.put("correction","");
			mapOut.put("control","");
			mapOut.put("contact the ","");
			mapOut.put("contact","");
			mapOut.put("conservator","");
			mapOut.put("consensus","");
			mapOut.put("conference","");
			mapOut.put("committee","");
			mapOut.put("commissioner","");
			mapOut.put("civil body","");
			mapOut.put("civic bodies","");
			mapOut.put("civic ","");
			mapOut.put("chargesheet","");
			mapOut.put("charges ","");
			mapOut.put("census","");
			mapOut.put("cause","");
			mapOut.put("called","");
			mapOut.put("bill<>pass","");
			mapOut.put("bill<>introduced","");
			mapOut.put("benefit","");
			mapOut.put("being taken","");
			mapOut.put("authoritie","");
			mapOut.put("at least","");
			mapOut.put("association","");
			mapOut.put("application","");
			mapOut.put("anti","");
			mapOut.put("announc","");
			mapOut.put("analysis","");
			mapOut.put("amend","");
			mapOut.put("aid<>organizat","");
			mapOut.put("agency","");
			mapOut.put("agency","");
			mapOut.put("affairs","");
			mapOut.put("added","");
			mapOut.put("activist","");
			mapOut.put("acceler","");
			mapOut.put("academy","");
			mapOut.put("ability","");
			mapOut.put("a large","");
			mapOut.put("a few","");
			mapOut.put("step","");
			mapOut.put("response to","");
			mapOut.put("india limited","");
			mapOut.put("in respons","");
			mapOut.put("#youth","");
			mapOut.put("#year","");
			mapOut.put("#violat","");
			mapOut.put("#testing","");
			mapOut.put("#test","");
			mapOut.put("#study","");
			mapOut.put("#review","");
			mapOut.put("#record","");
			mapOut.put("#protest","");
			mapOut.put("#promise","");
			mapOut.put("#probe","");
			mapOut.put("#post","");
			mapOut.put("#possibil","");
			mapOut.put("#learn","");
			mapOut.put("#inquiry","");
			mapOut.put("#hit","");
			mapOut.put("#heighten","");
			mapOut.put("#found","");
			mapOut.put("#fact","");
			mapOut.put("#effort","");
			mapOut.put("#decision","");
			mapOut.put("#cost","");
			mapOut.put("#coordin","");
			mapOut.put("#construct","");
			mapOut.put("#conduct","");
			mapOut.put("#compound","");
			mapOut.put("#caus","");
			mapOut.put("#call","");
			mapOut.put("#beat","");
			mapOut.put("#base","");
			mapOut.put("#ban","");
			mapOut.put("#assembl","");
			mapOut.put("#add","");
			mapOut.put("#activi","");
			mapOut.put("#action<>case","");
			mapOut.put("#act","");
			mapOut.put(" block","");
			mapOut.put(" explor","");
			//N values
			mapOut.put("person","");
			mapOut.put("people","");
			mapOut.put("destroy","");
			 
			
			for(String term:mapOut.keySet()){
				String [] s=term.replace(" ","<>").split("<>");
				// 
				if(s.length==1)
					mapOut_new.put(s[0].replace("#", " "), "");
				if(s.length==2)
					mapOut_new.put(s[0].replace("#", " "), s[1].replace("#", " "));
			}
			
			
		}
		catch(Exception e){
			
		}
		return mapOut_new;
		
	}
	
	

	public static TreeMap<String, String > stopwords(){
		
		 TreeMap<String, String > mapStopwords=new TreeMap();
		
		try{
			
		    //Stopwords list from Rainbow
		    mapStopwords.put("a","");
		    mapStopwords.put("able","");
		    mapStopwords.put("about","");
		    mapStopwords.put("above","");
		    mapStopwords.put("according","");
		    mapStopwords.put("accordingly","");
		    mapStopwords.put("across","");
		    mapStopwords.put("actually","");
		    mapStopwords.put("after","");
		    mapStopwords.put("afterwards","");
		    mapStopwords.put("again","");
		    mapStopwords.put("against","");
		    mapStopwords.put("all","");
		    mapStopwords.put("allow","");
		    mapStopwords.put("allows","");
		    mapStopwords.put("almost","");
		    mapStopwords.put("alone","");
		    mapStopwords.put("along","");
		    mapStopwords.put("already","");
		    mapStopwords.put("also","");
		    mapStopwords.put("although","");
		    mapStopwords.put("always","");
		    mapStopwords.put("am","");
		    mapStopwords.put("among","");
		    mapStopwords.put("amongst","");
		    mapStopwords.put("an","");
		    mapStopwords.put("and","");
		    mapStopwords.put("another","");
		    mapStopwords.put("any","");
		    mapStopwords.put("anybody","");
		    mapStopwords.put("anyhow","");
		    mapStopwords.put("anyone","");
		    mapStopwords.put("anything","");
		    mapStopwords.put("anyway","");
		    mapStopwords.put("anyways","");
		    mapStopwords.put("anywhere","");
		    mapStopwords.put("apart","");
		    mapStopwords.put("appear","");
		    mapStopwords.put("appreciate","");
		    mapStopwords.put("appropriate","");
		    mapStopwords.put("are","");
		    mapStopwords.put("around","");
		    mapStopwords.put("as","");
		    mapStopwords.put("aside","");
		    mapStopwords.put("ask","");
		    mapStopwords.put("asking","");
		    mapStopwords.put("associated","");
		    mapStopwords.put("at","");
		    mapStopwords.put("available","");
		    mapStopwords.put("away","");
		    mapStopwords.put("awfully","");
		    mapStopwords.put("b","");
		    mapStopwords.put("be","");
		    mapStopwords.put("became","");
		    mapStopwords.put("because","");
		    mapStopwords.put("become","");
		    mapStopwords.put("becomes","");
		    mapStopwords.put("becoming","");
		    mapStopwords.put("been","");
		    mapStopwords.put("before","");
		    mapStopwords.put("beforehand","");
		    mapStopwords.put("behind","");
		    mapStopwords.put("being","");
		    mapStopwords.put("believe","");
		    mapStopwords.put("below","");
		    mapStopwords.put("beside","");
		    mapStopwords.put("besides","");
		    mapStopwords.put("best","");
		    mapStopwords.put("better","");
		    mapStopwords.put("between","");
		    mapStopwords.put("beyond","");
		    mapStopwords.put("both","");
		    mapStopwords.put("but","");    
		    mapStopwords.put("brief","");
		    mapStopwords.put("by","");
		    mapStopwords.put("c","");
		    mapStopwords.put("came","");
		    mapStopwords.put("can","");
		    mapStopwords.put("certain","");
		    mapStopwords.put("certainly","");
		    mapStopwords.put("clearly","");
		    mapStopwords.put("co","");
		    mapStopwords.put("com","");
		    mapStopwords.put("come","");
		    mapStopwords.put("comes","");
		    mapStopwords.put("contain","");
		    mapStopwords.put("containing","");
		    mapStopwords.put("contains","");
		    mapStopwords.put("corresponding","");
		    mapStopwords.put("could","");
		    mapStopwords.put("course","");
		    mapStopwords.put("currently","");
		    mapStopwords.put("d","");
		    mapStopwords.put("definitely","");
		    mapStopwords.put("described","");
		    mapStopwords.put("despite","");
		    mapStopwords.put("did","");
		    mapStopwords.put("due","");
		    mapStopwords.put("different","");
		    mapStopwords.put("do","");
		    mapStopwords.put("does","");
		    mapStopwords.put("doing","");
		    mapStopwords.put("done","");
		    mapStopwords.put("down","");
		    mapStopwords.put("downwards","");
		    mapStopwords.put("during","");
		    mapStopwords.put("e","");
		    mapStopwords.put("each","");
		    mapStopwords.put("edu","");
		    mapStopwords.put("eg","");

		    mapStopwords.put("either","");
		    mapStopwords.put("else","");
		    mapStopwords.put("elsewhere","");
		    mapStopwords.put("enough","");
		    mapStopwords.put("entirely","");
		    mapStopwords.put("especially","");
		    mapStopwords.put("et","");
		    mapStopwords.put("etc","");
		    mapStopwords.put("even","");
		    mapStopwords.put("ever","");
		    mapStopwords.put("every","");
		    mapStopwords.put("everybody","");
		    mapStopwords.put("everyone","");
		    mapStopwords.put("everything","");
		    mapStopwords.put("everywhere","");
		    mapStopwords.put("ex","");
		    mapStopwords.put("exactly","");
		    mapStopwords.put("example","");
		    mapStopwords.put("except","");
		    mapStopwords.put("f","");
		    mapStopwords.put("far","");
		    mapStopwords.put("few","");
		    mapStopwords.put("fifth","");
		    mapStopwords.put("first","");

		    mapStopwords.put("followed","");
		    mapStopwords.put("following","");
		    mapStopwords.put("follows","");
		    mapStopwords.put("for","");
		    mapStopwords.put("former","");
		    mapStopwords.put("formerly","");
		    mapStopwords.put("forth","");

		    mapStopwords.put("from","");
		    mapStopwords.put("further","");
		    mapStopwords.put("furthermore","");
		    mapStopwords.put("g","");
		    mapStopwords.put("get","");
		    mapStopwords.put("gets","");
		    mapStopwords.put("getting","");
		    mapStopwords.put("given","");
		    mapStopwords.put("gives","");
		    mapStopwords.put("go","");
		    mapStopwords.put("goes","");
		    mapStopwords.put("going","");
		    mapStopwords.put("gone","");
		    mapStopwords.put("got","");
		    mapStopwords.put("gotten","");
		    mapStopwords.put("greetings","");
		    mapStopwords.put("h","");
		    mapStopwords.put("had","");
		    mapStopwords.put("happens","");
		    mapStopwords.put("hardly","");
		    mapStopwords.put("has","");
		    mapStopwords.put("have","");
		    mapStopwords.put("having","");
		    mapStopwords.put("he","");
		    mapStopwords.put("hello","");
		    //mapStopwords.put("help","");
		    mapStopwords.put("hence","");
		    mapStopwords.put("her","");
		    mapStopwords.put("here","");
		    mapStopwords.put("hereafter","");
		    mapStopwords.put("hereby","");
		    mapStopwords.put("herein","");
		    mapStopwords.put("hereupon","");
		    mapStopwords.put("hers","");
		    mapStopwords.put("herself","");
		    mapStopwords.put("hi","");
		    mapStopwords.put("him","");
		    mapStopwords.put("himself","");
		    mapStopwords.put("his","");
		    mapStopwords.put("hither","");
		    mapStopwords.put("hopefully","");
		    mapStopwords.put("how","");
		    mapStopwords.put("howbeit","");
		    mapStopwords.put("however","");
		    mapStopwords.put("i","");
		    mapStopwords.put("ie","");
		    mapStopwords.put("if","");
		    mapStopwords.put("ignored","");
		    mapStopwords.put("immediate","");
		    mapStopwords.put("in","");
		    mapStopwords.put("inasmuch","");
		    mapStopwords.put("inc","");
		    mapStopwords.put("indeed","");
		    mapStopwords.put("indicate","");
		    mapStopwords.put("indicated","");
		    mapStopwords.put("indicates","");
		    mapStopwords.put("inner","");
		    mapStopwords.put("insofar","");
		    mapStopwords.put("instead","");
		    mapStopwords.put("into","");
		    mapStopwords.put("inward","");
		    mapStopwords.put("is","");
		    mapStopwords.put("it","");
		    mapStopwords.put("its","");
		    mapStopwords.put("itself","");
		    mapStopwords.put("j","");
		    mapStopwords.put("just","");
		    mapStopwords.put("k","");
		    mapStopwords.put("keep","");
		    mapStopwords.put("keeps","");
		    mapStopwords.put("kept","");
		    mapStopwords.put("know","");
		    mapStopwords.put("knows","");
		    mapStopwords.put("known","");
		    mapStopwords.put("l","");
		    mapStopwords.put("last","");
		    mapStopwords.put("lately","");
		    mapStopwords.put("later","");
		    mapStopwords.put("latter","");
		    mapStopwords.put("latterly","");
		    mapStopwords.put("least","");
		    mapStopwords.put("less","");
		    mapStopwords.put("lest","");
		    mapStopwords.put("let","");
		    mapStopwords.put("like","");
		    mapStopwords.put("liked","");
		    mapStopwords.put("likely","");
		    mapStopwords.put("little","");
		    mapStopwords.put("ll",""); 
		    mapStopwords.put("look","");
		    mapStopwords.put("looking","");
		    mapStopwords.put("looks","");
		    mapStopwords.put("ltd","");
		    mapStopwords.put("m","");
		    mapStopwords.put("mainly","");
		    mapStopwords.put("many","");
		    mapStopwords.put("may","");
		    mapStopwords.put("maybe","");
		    mapStopwords.put("me","");
		    mapStopwords.put("mean","");
		    mapStopwords.put("meanwhile","");
		    mapStopwords.put("merely","");
		    mapStopwords.put("might","");
		    mapStopwords.put("more","");
		    mapStopwords.put("moreover","");
		    mapStopwords.put("most","");
		    mapStopwords.put("mostly","");
		    mapStopwords.put("much","");
		    mapStopwords.put("must","");
		    mapStopwords.put("my","");
		    mapStopwords.put("myself","");
		    mapStopwords.put("n","");
		    mapStopwords.put("n't","");
		    mapStopwords.put("name","");
		    mapStopwords.put("namely","");
		    mapStopwords.put("nd","");
		    mapStopwords.put("near","");
		    mapStopwords.put("nearly","");
		    mapStopwords.put("necessary","");
		    mapStopwords.put("need","");
		    mapStopwords.put("needs","");
		    mapStopwords.put("neither","");
		    mapStopwords.put("never","");
		    mapStopwords.put("nevertheless","");
		    mapStopwords.put("new","");
		    mapStopwords.put("next","");

		    mapStopwords.put("normally","");
		    mapStopwords.put("novel","");
		    mapStopwords.put("no","");
		    mapStopwords.put("nobody","");
		    mapStopwords.put("non","");
		    mapStopwords.put("none","");
		    mapStopwords.put("noone","");
		    mapStopwords.put("nor","");
		    mapStopwords.put("normally","");
		    mapStopwords.put("not","");
		    mapStopwords.put("n't","");
		    mapStopwords.put("nothing","");
		    mapStopwords.put("novel","");
		    mapStopwords.put("now","");
		    mapStopwords.put("nowhere","");
		    mapStopwords.put("now","");
		    mapStopwords.put("nowhere","");
		    mapStopwords.put("o","");
		    mapStopwords.put("obviously","");
		    mapStopwords.put("of","");
		    mapStopwords.put("off","");
		    mapStopwords.put("often","");
		    mapStopwords.put("oh","");
		    mapStopwords.put("ok","");
		    mapStopwords.put("okay","");
		    mapStopwords.put("old","");
		    mapStopwords.put("on","");
		    mapStopwords.put("once","");

		    mapStopwords.put("ones","");
		    mapStopwords.put("only","");
		    mapStopwords.put("onto","");
		    mapStopwords.put("or","");
		    mapStopwords.put("other","");
		    mapStopwords.put("others","");
		    mapStopwords.put("otherwise","");
		    mapStopwords.put("ought","");
		    mapStopwords.put("our","");
		    mapStopwords.put("ours","");
		    mapStopwords.put("ourselves","");
		    mapStopwords.put("out","");
		    mapStopwords.put("outside","");
		    mapStopwords.put("over","");
		    mapStopwords.put("overall","");
		    mapStopwords.put("own","");
		    mapStopwords.put("p","");
		    mapStopwords.put("particular","");
		    mapStopwords.put("particularly","");
		    mapStopwords.put("per","");
		    mapStopwords.put("perhaps","");
		    mapStopwords.put("placed","");
		    mapStopwords.put("please","");
		    mapStopwords.put("plus","");
		    mapStopwords.put("possible","");
		    mapStopwords.put("presumably","");
		    mapStopwords.put("probably","");
		    
		    mapStopwords.put("q","");
		    mapStopwords.put("que","");
		    mapStopwords.put("quite","");
		    mapStopwords.put("qv","");
		    mapStopwords.put("r","");
		    mapStopwords.put("rather","");
		    mapStopwords.put("rd","");
		    mapStopwords.put("re","");
		    mapStopwords.put("really","");
		    mapStopwords.put("reasonably","");
		    mapStopwords.put("regarding","");
		    mapStopwords.put("regardless","");
		    mapStopwords.put("regards","");
		    mapStopwords.put("relatively","");
		    mapStopwords.put("respectively","");
		    mapStopwords.put("right","");
		    mapStopwords.put("s","");
		    mapStopwords.put("said","");
		    mapStopwords.put("same","");
		    mapStopwords.put("saw","");
		    mapStopwords.put("say","");
		    mapStopwords.put("saying","");
		    mapStopwords.put("says","");
		    mapStopwords.put("second","");
		    mapStopwords.put("secondly","");
		    mapStopwords.put("see","");
		    mapStopwords.put("seeing","");
		    mapStopwords.put("seem","");
		    mapStopwords.put("seemed","");
		    mapStopwords.put("seeming","");
		    mapStopwords.put("seems","");
		    mapStopwords.put("seen","");
		    mapStopwords.put("self","");
		    mapStopwords.put("selves","");
		    mapStopwords.put("sensible","");
		    mapStopwords.put("sent","");
		    mapStopwords.put("serious","");
		    mapStopwords.put("seriously","");

		    mapStopwords.put("several","");
		    mapStopwords.put("shall","");
		    mapStopwords.put("she","");
		    mapStopwords.put("should","");
		    mapStopwords.put("since","");


		    mapStopwords.put("so","");
		    mapStopwords.put("some","");
		    mapStopwords.put("somebody","");
		    mapStopwords.put("somehow","");
		    mapStopwords.put("someone","");
		    mapStopwords.put("something","");
		    mapStopwords.put("sometime","");
		    mapStopwords.put("sometimes","");
		    mapStopwords.put("somewhat","");
		    mapStopwords.put("somewhere","");
		    mapStopwords.put("soon","");
		    mapStopwords.put("sorry","");
		    mapStopwords.put("specified","");
		    mapStopwords.put("specify","");
		    mapStopwords.put("specifying","");
		    mapStopwords.put("still","");
		    mapStopwords.put("sub","");
		    mapStopwords.put("such","");
		    mapStopwords.put("sup","");
		    mapStopwords.put("sure","");
		    mapStopwords.put("t","");
		    mapStopwords.put("take","");
		    mapStopwords.put("taken","");
		    mapStopwords.put("tell","");
		    mapStopwords.put("tends","");
		    mapStopwords.put("th","");
		    mapStopwords.put("than","");
		    mapStopwords.put("thank","");
		    mapStopwords.put("thanks","");
		    mapStopwords.put("thanx","");
		    mapStopwords.put("that","");
		    mapStopwords.put("thats","");
		    mapStopwords.put("the","");
		    mapStopwords.put("the..","");mapStopwords.put("the...","");
		    mapStopwords.put("the.","");
		    mapStopwords.put("their","");
		    mapStopwords.put("theirs","");
		    mapStopwords.put("them","");
		    mapStopwords.put("themselves","");
		    mapStopwords.put("then","");
		    mapStopwords.put("thence","");
		    mapStopwords.put("there","");
		    mapStopwords.put("thereafter","");
		    mapStopwords.put("thereby","");
		    mapStopwords.put("therefore","");
		    mapStopwords.put("therein","");
		    mapStopwords.put("theres","");
		    mapStopwords.put("thereupon","");
		    mapStopwords.put("these","");
		    mapStopwords.put("they","");
		    mapStopwords.put("think","");
		    mapStopwords.put("third","");
		    mapStopwords.put("this","");
		    mapStopwords.put("thorough","");
		    mapStopwords.put("thoroughly","");
		    mapStopwords.put("those","");
		    mapStopwords.put("though","");
		    mapStopwords.put("through","");
		    mapStopwords.put("throughout","");
		    mapStopwords.put("thru","");
		    mapStopwords.put("thus","");
		    mapStopwords.put("to","");
		    mapStopwords.put("together","");
		    mapStopwords.put("too","");
		    mapStopwords.put("took","");
		    mapStopwords.put("toward","");
		    mapStopwords.put("towards","");
		    mapStopwords.put("tried","");
		    mapStopwords.put("tries","");
		    mapStopwords.put("truly","");
		    mapStopwords.put("try","");
		    mapStopwords.put("trying","");
		    mapStopwords.put("twice","");
		    mapStopwords.put("u","");
		    mapStopwords.put("un","");
		    mapStopwords.put("under","");
		    mapStopwords.put("unfortunately","");
		    mapStopwords.put("unless","");
		    mapStopwords.put("unlikely","");
		    mapStopwords.put("until","");
		    mapStopwords.put("unto","");
		    mapStopwords.put("up","");
		    mapStopwords.put("upon","");
		    mapStopwords.put("us","");
		    mapStopwords.put("use","");
		    mapStopwords.put("used","");
		    mapStopwords.put("useful","");
		    mapStopwords.put("uses","");
		    mapStopwords.put("using","");
		    mapStopwords.put("usually","");
		    mapStopwords.put("uucp","");
		    mapStopwords.put("v","");
		    mapStopwords.put("value","");
		    mapStopwords.put("various","");
		    mapStopwords.put("ve",""); 
		    mapStopwords.put("very","");
		    mapStopwords.put("via","");
		    mapStopwords.put("viz","");
		    mapStopwords.put("vs","");
		    mapStopwords.put("w","");
		    mapStopwords.put("want","");
		    mapStopwords.put("wants","");
		    mapStopwords.put("was","");
		    mapStopwords.put("way","");
		    mapStopwords.put("we","");
		    mapStopwords.put("welcome","");
		    mapStopwords.put("well","");
		    mapStopwords.put("went","");
		    mapStopwords.put("were","");
		    mapStopwords.put("what","");
		    mapStopwords.put("whatever","");
		    mapStopwords.put("when","");
		    mapStopwords.put("whence","");
		    mapStopwords.put("whenever","");
		    mapStopwords.put("where","");
		    mapStopwords.put("whereafter","");
		    mapStopwords.put("whereas","");
		    mapStopwords.put("whereby","");
		    mapStopwords.put("wherein","");
		    mapStopwords.put("whereupon","");
		    mapStopwords.put("wherever","");
		    mapStopwords.put("whether","");
		    mapStopwords.put("which","");
		    mapStopwords.put("while","");
		    mapStopwords.put("whither","");
		    mapStopwords.put("who","");
		    mapStopwords.put("whoever","");
		    mapStopwords.put("whole","");
		    mapStopwords.put("whom","");
		    mapStopwords.put("whose","");
		    mapStopwords.put("why","");
		    mapStopwords.put("will","");
		    mapStopwords.put("willing","");
		    mapStopwords.put("wish","");
		    mapStopwords.put("with","");
		    mapStopwords.put("within","");
		    mapStopwords.put("without","");
		    mapStopwords.put("wonder","");
		    mapStopwords.put("would","");
		    mapStopwords.put("would","");
		    mapStopwords.put("x","");
		    mapStopwords.put("y","");
		    mapStopwords.put("yes","");
		    mapStopwords.put("yet","");
		    mapStopwords.put("you","");
		    mapStopwords.put("your","");
		    mapStopwords.put("yours","");
		    mapStopwords.put("yourself","");
		    mapStopwords.put("yourselves","");
		    mapStopwords.put("z","");
		    mapStopwords.put("zero","");
		    
		    mapStopwords.put("à","");
		    mapStopwords.put("’s","");
		    mapStopwords.put("|","");
		    mapStopwords.put("©","");
		    mapStopwords.put("&amp;","");
		    mapStopwords.put("&amp","");
		    mapStopwords.put("&gt;&lt;div&gt;","");
		    mapStopwords.put("&", "");
		    mapStopwords.put("&gt;","");
		    
		  
		    mapStopwords.put("i'm","");
		    mapStopwords.put("he's","");
		    mapStopwords.put("she's","");
		    mapStopwords.put("you're","");
		    mapStopwords.put("i'll","");
		    mapStopwords.put("you'll","");
		    mapStopwords.put("she'll","");
		    mapStopwords.put("he'll","");
		    mapStopwords.put("it's","");
		    mapStopwords.put("don't","");
		    mapStopwords.put("can't","");
		    mapStopwords.put("didn't","");
		    mapStopwords.put("i've","");
		    mapStopwords.put("that's","");
		    mapStopwords.put("there's","");
		    mapStopwords.put("isn't","");
		    mapStopwords.put("what's","");
		    mapStopwords.put("rt","");
		    mapStopwords.put("doesn't","");
		    mapStopwords.put("w/","");
		    mapStopwords.put("w/o","");
		    mapStopwords.put(":","");
		    mapStopwords.put("told","");
		    mapStopwords.put("tell","");
		    
		    mapStopwords.put("two","");
		    mapStopwords.put("three","");
		    mapStopwords.put("six","");
		    mapStopwords.put("seven","");
		    mapStopwords.put("one","");
		    mapStopwords.put("nine","");
		    mapStopwords.put("four","");
		    mapStopwords.put("five","");
		    mapStopwords.put("eight","");
		    
		    //nlp
		    mapStopwords.put("-lrb-","");
		    mapStopwords.put("-rrb-","");
		    mapStopwords.put("span","");
		    mapStopwords.put("id=","");
		    
		    
		    
		    //my stopwords
		    mapStopwords.put("world","");
		    mapStopwords.put("monday","");
		    mapStopwords.put("tuesday","");
		    mapStopwords.put("wednesday","");
		    mapStopwords.put("thursday","");
		    mapStopwords.put("friday","");
		    mapStopwords.put("saturday","");
		    mapStopwords.put("sunday","");
		    
		    
		    
		    mapStopwords.put("show","");
		    mapStopwords.put("shows","");
		    mapStopwords.put("coming","");
		    mapStopwords.put("day","");
		    mapStopwords.put("days","");
		    mapStopwords.put("incoming","");
		    mapStopwords.put("africa","");
		    mapStopwords.put("india","");
		    mapStopwords.put("delhi","");
		    mapStopwords.put("britain","");
		    mapStopwords.put("ive","");
		    mapStopwords.put("argentine","");
		    mapStopwords.put("toldthe","");
		    mapStopwords.put("ravi","");
		    mapStopwords.put("zair","");
		    mapStopwords.put("senegal","");
		    mapStopwords.put("southern","");
		    mapStopwords.put("south","");
		    
		    //mapStopwords.put("provides","");
		    
			
		}
		catch(Exception e){
			
		}
		return mapStopwords;
	}

	

	// main
	public static void main(String[] args) {
		
		
		String a="Sadanandan,32,construction";
				  a="100,00,00";
		String []arr_word=new String[1];
		
		// input : "Sadanandan,32,construction";
		arr_word=a.split(",");
		
		
       String [] arr_="subjected are connecting collaborating keywords: martin triggered are united allowing learn sharing have been joining are are drawing united are use learn control palestinians, respectively. has manifested organizing groups. united protesting be heard invoking shut san protest held — engaged aimed reclaiming jr. asserted link have given resist crackdowns, shared sent took proclaiming took palestine. aimed learn build forge movements. — matter, united concluded is united is rooted illuminate operate united are use learn control palestinians, police built created exists education, feared were reigned inflicting brutalized played became were deemed has been were created fight ended, did were lynched united keep killed killed named sparked were protested cointelpro suppressed exploiting assassinated fred hampton, shooting outlived told have face is is devise recognizes appearing became outlived told have face is is devise recognizes appearing has is are be incarcerated use".split(" ");
       String concVerb_nostopword=""; TreeMap<String, String> map_Uniquewords_asKEY=new TreeMap<String, String>();
       
       int c2=0;
       while(c2<arr_.length){
    	   
    	   if(!is_stopword(arr_[c2])){
    		   concVerb_nostopword+=" "+arr_[c2];
    		   
    		   map_Uniquewords_asKEY.put(arr_[c2], "");
    	   }
    	   
    	   c2++;
       }
       System.out.println("concVerb_nostopword:"+concVerb_nostopword);
       System.out.println("map_Uniquewords_asKEY:"+map_Uniquewords_asKEY.toString().replace("=,", ","));
       
		 
		 int cn= 1;
//				 find_occurance_Count_Substring.find_occurance_Count_Substring(",", a);
		 
		 if(cn==1){
			 
		 }
		 else{

			 int cnt=0;
			 String concWord="";
			 boolean last_token_last_Character_numeric=false;
			 //
			 while(cnt<arr_word.length){
				 
			 
				 String []  h=arr_word[cnt].split(",");
					// last token
					 //System.out.println( h[h.length-1]);
					 
					 //last token after , split -> its first character
					 String c=  h[h.length-1].substring(0, 1);
					 
					 if(! IsNumeric.isNumeric( c)){
						 System.out.println("false");
						 String new2=arr_word[cnt].substring( arr_word[cnt].lastIndexOf(",")+1 ,arr_word[cnt].length());
						 arr_word[cnt]=new2;
						 
						 
						 concWord=concWord+""+arr_word[cnt];
						 System.out.println("SKIP proper noun new..(continue) "+concWord);
					 }
					 else{ // NUMERIC .. 1,00,00  <- this should not skip 
						 //System.out.println("true");
						 
						 if(concWord.length()==0)
							 concWord=arr_word[cnt];
						 else{
							 
							 // get last 
							 if(cnt>0 && last_token_last_Character_numeric){ //NUMERIC 
								 concWord=concWord+","+arr_word[cnt];	 
							 }
							 else if(cnt>0 && last_token_last_Character_numeric==false){ // NOT NUMERIC 
								 concWord=concWord+""+arr_word[cnt];
							 }
							 else { //NUMERIC
								 concWord=concWord+","+arr_word[cnt];
							 }
							 
						 }
						 

						 
						 System.out.println("SKIP proper noun new..(continue) "+concWord);
					 }
					 
					 
					 //last token , last character is NUMERIC
					 try{
						 
						 String lastToken_lastChar=arr_word[cnt].substring(arr_word[cnt].length()-1, arr_word[cnt].length());
						 //
						 if(IsNumeric.isNumeric(lastToken_lastChar)){
							 last_token_last_Character_numeric=true;
						 }
						 
						 
					 }
					 catch(Exception e){
						 
					 }
					 
				cnt++;
			 } //while(cnt<arr_word.length){
			 
		 }
		 
		 
		 
		
	}
	
	
}
