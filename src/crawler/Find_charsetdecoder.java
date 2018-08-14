package crawler;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class Find_charsetdecoder {

  static CharsetEncoder asciiEncoder = 
      Charset.forName("US-ASCII").newEncoder(); // or "ISO-8859-1" for ISO Latin 1

  public static boolean isPureAscii(String v) {
    return asciiEncoder.canEncode(v);
  }

  public static void main (String args[])
    throws Exception {

     String test = "Réal";
     System.out.println(test + " isPureAscii() : " + Find_charsetdecoder.isPureAscii(test));
     test = "Real";
     System.out.println(test + " isPureAscii() : " + Find_charsetdecoder.isPureAscii(test));

     /*
      * output :
      *   Réal isPureAscii() : false
      *   Real isPureAscii() : true
      */
  }
}