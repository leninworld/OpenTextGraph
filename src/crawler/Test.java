package crawler;

import java.io.File;
import java.io.IOException;

public class Test {
	
	public static void t(int n){
//	   System.out.println(this.getClass().getClassLoader().getResource("").getPath());
		
		
		 if ((n & n-1) == 0) {
				System.out.println("power of 2 ");
	        }

		else
			System.out.println("NOT power of 2");
		
	}
    
	//
	public static void main(String[] args) throws IOException {

		
		t(32);
	}

}
