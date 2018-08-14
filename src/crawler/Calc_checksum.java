package crawler;
 
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Calc_checksum {

	// 
	public static void calc_checksum_for_file(String inputFP_File){
		try{

	        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
	        FileInputStream fileInput = new FileInputStream(inputFP_File);

	        byte[] dataBytes = new byte[1024];

	        int bytesRead = 0;
			//
	        while ((bytesRead = fileInput.read(dataBytes)) != -1) {
	            messageDigest.update(dataBytes, 0, bytesRead);
	        }

	        byte[] digestBytes = messageDigest.digest();

	        StringBuffer sb = new StringBuffer("");

	        for (int i = 0; i < digestBytes.length; i++) {
	            sb.append(Integer.toString((digestBytes[i] & 0xff) + 0x100, 16).substring(1));

	        }
	        System.out.println("Checksum for the File: " + sb.toString());

	        fileInput.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	//
    public static String calc_checksum_for_string(String input)
            throws NoSuchAlgorithmException, UnsupportedEncodingException
        {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.reset();
            byte[] buffer = input.getBytes("UTF-8");
            md.update(buffer);
            byte[] digest = md.digest();

            String hexStr = "";
            for (int i = 0; i < digest.length; i++) {
                hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
            }
            return hexStr;
        }
	
	//
    public static void main(String args[]) throws Exception {
 
    	String inputFPfile="";

    	calc_checksum_for_file(inputFPfile);
 

    }

}