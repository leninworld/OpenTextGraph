package crawler;

public class JavaVersion {
	
	 public static String getJavaVersion(){
		    return System.getProperty("java.runtime.version");
	 }
	
	
	  public static void main(String[] args) {
	    System.out.println(System.getProperty("java.runtime.version"));
	  }
	}