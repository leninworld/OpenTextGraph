package p21;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import crawler.ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile;

// 
public class P21_split_list_of_filenamesMonthly_from_unix_ls_ltr {
	
	
 
	// main
	public static void main(String[] args) throws IOException {
		 
		String inputFolder="/Users/lenin/Downloads/reu-cdean/RawData/dailytry4/2014Only-numerictry2/categorical/ApartmentGBADInjected/";
		
		
		String [] arr_files= (new File(inputFolder)).list();
		String month="";
			int c=0;
		    //delete if already exists
			while(c<arr_files.length){
				String currFile=arr_files[c];
			 	
				if(currFile.toLowerCase().indexOf("jan")>=0)
					month="jan";
				else if(currFile.toLowerCase().indexOf("feb")>=0)
					month="feb";
				else if(currFile.toLowerCase().indexOf("mar")>=0)
					month="mar";
				else if(currFile.toLowerCase().indexOf("apr")>=0)
					month="apr";
				else if(currFile.toLowerCase().indexOf("may")>=0)
					month="may";
				else if(currFile.toLowerCase().indexOf("jun")>=0)
					month="jun";
				else if(currFile.toLowerCase().indexOf("jul")>=0)
					month="jul";
				else if(currFile.toLowerCase().indexOf("aug")>=0)
					month="aug";
				else if(currFile.toLowerCase().indexOf("sep")>=0)
					month="sep";
				else if(currFile.toLowerCase().indexOf("oct")>=0)
					month="oct";
				else if(currFile.toLowerCase().indexOf("nov")>=0)
					month="nov";
				else if(currFile.toLowerCase().indexOf("dec")>=0)
					month="dec";
	 
				
				if(new File(inputFolder+month+".txt").exists())
					new File(inputFolder+month+".txt").delete();
			
				c++;
			}
		
			c=0;
			// while(c<arr_files.length){
			while(c<arr_files.length){
				String currFile=arr_files[c];
				if(currFile.indexOf(".g")==-1
						|| currFile.indexOf(".anom1")>=0
						) {
					c++;
					continue;
				}
				if(currFile.toLowerCase().indexOf("jan")>=0)
					month="jan";
				else if(currFile.toLowerCase().indexOf("feb")>=0)
					month="feb";
				else if(currFile.toLowerCase().indexOf("mar")>=0)
					month="mar";
				else if(currFile.toLowerCase().indexOf("apr")>=0)
					month="apr";
				else if(currFile.toLowerCase().indexOf("may")>=0)
					month="may";
				else if(currFile.toLowerCase().indexOf("jun")>=0)
					month="jun";
				else if(currFile.toLowerCase().indexOf("jul")>=0)
					month="jul";
				else if(currFile.toLowerCase().indexOf("aug")>=0)
					month="aug";
				else if(currFile.toLowerCase().indexOf("sep")>=0)
					month="sep";
				else if(currFile.toLowerCase().indexOf("oct")>=0)
					month="oct";
				else if(currFile.toLowerCase().indexOf("nov")>=0)
					month="nov";
				else if(currFile.toLowerCase().indexOf("dec")>=0)
					month="dec";
	 
				
				FileWriter writer=new FileWriter(new File(inputFolder+month+".txt"), true);
				
				//run gbad
				String temp
				="/Users/lenin/Downloads/gbad/bin/gbad -prob 2 -prune -maxsize 7 -limit 50 "+inputFolder+currFile 
					+" > "+inputFolder+"gbadoutput/"+"output_"+currFile.replace(".g", ".txt") +" &";
				
				writer.append(temp+"\n");
				writer.flush();
				
				c++;
			}
 			
	}
	
}