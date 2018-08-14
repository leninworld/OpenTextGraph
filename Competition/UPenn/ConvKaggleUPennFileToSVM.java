package UPenn;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import crawler.GetMaxFromArray;

// Kaggle Competition
public class ConvKaggleUPennFileToSVM {
		// Read the files (i.e., output from .R file) and convert to SVM light format 
		public static void convKaggleUPennFileToSVM(String inputFolder, String outputFile, boolean includeFileName){
			try{
				GetMaxFromArray gmaxarr=new GetMaxFromArray();
				BufferedReader reader = null;
				File h = new File(inputFolder);
				String [] files = h.list();
				FileWriter writer = null;
				writer=new FileWriter(outputFile);
				int cnt = 0; String line = "";
				int lineNo = 0;
				String last_position = ""; String last_mmdd = ""; String last_Minute = "";
				String last_Second =""; String last_line = "";
				int cnt2=0;
				System.out.println( "files.length:"+files.length +";inputFolder:"+inputFolder);
				//list sub-folders from main folder.
				while(cnt2 < files.length){
					//
					if(files[cnt2].indexOf("Store")>=0 ) {cnt2++;continue;}
					System.out.println("curr.len:"+inputFolder+files[cnt2]);
					File h2 = new File(inputFolder+files[cnt2]);
					//
					if(!h2.isDirectory()) {cnt2++; continue;}
					System.out.println("1.curr.len:"+files[cnt2]+";len:"+files.length+" cnt2:"+cnt2);
					String [] files2 = h2.list();
					// main folder
					String currSubFolderName=files[cnt2]; String currLabel="";String currLineToWrite="";
				    cnt=0; String lastReadFileName="";
					// if its folder and folder name = 2
					while(cnt < files2.length ){
						lineNo=0;
						if(files2[cnt].indexOf("Store")>=0 ) {cnt++;continue;}
						File h3 = new File(inputFolder+files2[cnt]);
//						System.out.println("input file:"+inputFolder +" files2.length:"+files2.length + "; cnt:"+cnt);
						if(h3.isDirectory()){
							//System.out.println("input file; isDir?"+inputFolder+files2[cnt]);
							cnt++; continue; 
						}
						//System.out.println("f path:"+inputFolder+currSubFolderName+files2[cnt]);
						reader = new BufferedReader(new FileReader(inputFolder+currSubFolderName +"/"+files2[cnt]));
						currLineToWrite="";
						//read current file
						while ((line = reader.readLine()) != null) {
							lineNo++;
							//System.out.println(line);
							//
							if(lineNo>1){
								line=line.substring(line.indexOf(" ")+1, line.length());
								
								String [] s = line.split(" ");
								//System.out.println("max:"+getMaxFromArrary(s,true)+"line:"+line);
								System.out.println("file:"+inputFolder+currSubFolderName +"/"+files2[cnt]);
								currLineToWrite=currLineToWrite+" "+(lineNo-1) + ":"
												+ String.valueOf(gmaxarr.getMaxFromSingleArray(s, false));
								

							}
						} // curr file - each line read end
						
						// Assign label
						if(files2[cnt].indexOf("interictal")==-1 && files2[cnt].indexOf("ictal") >=0)
							currLabel="1";
						else if(files2[cnt].indexOf("interictal")>=0 )
							currLabel="0";
						else if(files2[cnt].indexOf("test")>=0 )
							currLabel="?";
						
						System.out.println(currLabel+currLineToWrite+"#"+files2[cnt]);
						if(includeFileName==true)
							writer.append(currLabel+currLineToWrite+"#"+files2[cnt]+"\n");
						else
							writer.append(currLabel+currLineToWrite+"\n");
						writer.flush();
						cnt++;
					} //while read current folder, to read each file
				
					cnt2++;
				} //while end of sub-folders
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	
	public static void main(String[] args) throws IOException {
		//crawler c = new crawler();
		
		
	}
	
}
