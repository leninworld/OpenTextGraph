package crawler;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
// printMap
public class PrintMap {
	//print integer and string
	public static void printIntStringTreeMap(TreeMap<Integer, String> map){
		//
		for(int i:map.keySet()){
			System.out.println(i+" "+map.get(i));
		}
		
	}
	//print string and integer
	public static void printStringIntTreeMap(TreeMap<String, Integer> map){
		//
		for(String i:map.keySet()){
			System.out.println(i+" "+map.get(i));
		}
		
	}
	//print string and string
	public static void printStringStringTreeMap(TreeMap<String, String> map){
		//
		for(String i:map.keySet()){
			System.out.println(i+" "+map.get(i));
		}
		
	}
	//print integer and integer
	public static void printIntegerIntegerTreeMap(TreeMap<Integer, Integer> map){
		//
		for(Integer i:map.keySet()){
			System.out.println(i+" "+map.get(i));
		}
		
	}
	//hashmap
	//print integer and string
	public static void printIntStringHashMap(HashMap<Integer, String> map){
		//
		for(int i:map.keySet()){
			System.out.println(i+" "+map.get(i));
		}
		
	}
	//print string and integer
	public static void printStringIntHashMap(HashMap<String, Integer> map){
		//
		for(String i:map.keySet()){
			System.out.println(i+" "+map.get(i));
		}
		
	}
	//print string and string
	public static void printStringStringHashMap(HashMap<String, String> map){
		//
		for(String i:map.keySet()){
			System.out.println(i+" "+map.get(i));
		}
		
	}
	//print integer and integer
	public static void printIntegerIntegerHashMap(HashMap<Integer, Integer> map){
		//
		for(Integer i:map.keySet()){
			System.out.println(i+" "+map.get(i));
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		
		
	}
	
}
