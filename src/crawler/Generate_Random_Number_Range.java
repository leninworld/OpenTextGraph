package crawler;

import java.util.Random;

/** Generate random integers in a certain range. */
public  class Generate_Random_Number_Range {
  //
  public static int generate_Random_Number_Range(int aStart, int aEnd){
	 Random aRandom = new Random();
    if (aStart > aEnd) {
      throw new IllegalArgumentException("Start cannot exceed End.");
    }
    //get the range, casting to long to avoid overflow problems
    long range = (long)aEnd - (long)aStart + 1;
    // compute a fraction of the range, 0 <= frac < range
    long fraction = (long)(range * aRandom.nextDouble());
    int randomNumber =  (int)(fraction + aStart);    
    //log("Generated : " + randomNumber);
    return randomNumber;
  }
  //
  private static void log(String aMessage){
    System.out.println(aMessage);
  }
  //
  public static final void main(String... aArgs){
	    log("Generating random integers in the range 1..10.");
	    
	    int START = 1;
	    int END = 10;
	    //
	    for (int idx = 1; idx <= 10; ++idx){
	    	generate_Random_Number_Range(START, END);
	    }
	    
	    log("Done.");
	  }
} 