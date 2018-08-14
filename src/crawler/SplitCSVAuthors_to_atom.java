/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

/**
 *
 * @author lenin
 */
public class SplitCSVAuthors_to_atom {
    
    
    public static TreeMap<String, String> splitCSVAuthors_to_atom(
                                                            String curr_author_names_CSV,
                                                            String inFile_debug,
                                                            boolean isSOPprint
                                                            ){
        
        String []s_token=null;String []s_token_2=null;
        TreeMap<String, String> mapAuthorNames = new TreeMap();
        int c10=0; boolean isWriteDebug=true;FileWriter writer_debugFile=null;
        try{
            try{
                writer_debugFile=new FileWriter(new File(inFile_debug));
            }
            catch(Exception e){
                isWriteDebug=false;
            }
             
            
                   if( ( curr_author_names_CSV.indexOf(" and ")>=0 || curr_author_names_CSV.indexOf(" & ")>=0  )&& 
                         curr_author_names_CSV.indexOf(",")>=0){
                       
                       if(isWriteDebug){
                         writer_debugFile.append(("\n AND , both:"+curr_author_names_CSV));
                         writer_debugFile.flush();
                       }
                         
                        curr_author_names_CSV=curr_author_names_CSV.replace("from:from:", "from:")
                        					.replace("auth:from::from:", "auth:from:");
                        
                        //// 
                        if(curr_author_names_CSV.indexOf(" and ")>=0 )
                        	s_token=curr_author_names_CSV.split(" and ");
                        else if( curr_author_names_CSV.indexOf(" & ")>=0)
                        	s_token=curr_author_names_CSV.split(" & ");                        
                        
                        if(isSOPprint)
                        	System.out.println("\nAND , token:");
                        
                        if(isWriteDebug){
                            writer_debugFile.append("\ns_token[1],s_token[0]:"+s_token[1]+"<->"+s_token[0]);
                            writer_debugFile.flush();
                        }
                        
                        if(s_token[1].length()>1 && !IsInteger.isInteger(s_token[1]) ){
                        	
                    		//( not there, but ) there, so remove it 
                    		if(s_token[1].indexOf("(")==-1 && s_token[1].indexOf(")")>=0)
                    			s_token[1]=s_token[1].replace(")", "");
                        	
                        	mapAuthorNames.put(s_token[1], "");
                        }
                        //
                        if(s_token[0].indexOf(",")>=0){
                            s_token_2=s_token[0].split(",");
                            c10=0;
                            //
                            while(c10<s_token_2.length){
                            	if(s_token_2[c10].length()>1 && !IsInteger.isInteger(s_token_2[c10])){
                            		
                            		//( not there, but ) there, so remove it 
                            		if(s_token[c10].indexOf("(")==-1 && s_token[c10].indexOf(")")>=0)
                            			s_token[c10]=s_token[c10].replace(")", "");
                            		
                            		mapAuthorNames.put(s_token_2[c10], "");
                            	}
                                c10++;
                            }
                        }
                        else{
                        	if(s_token[0].length()>1 && !IsInteger.isInteger(s_token[0])){
                        		
                        		//( not there, but ) there, so remove it 
                        		if(s_token[0].indexOf("(")==-1 && s_token[0].indexOf(")")>=0)
                        			s_token[0]=s_token[0].replace(")", "");
                        					
                        		mapAuthorNames.put(s_token[0], "");
                        	}
                        }
                     }
                     else if(curr_author_names_CSV.indexOf(",")>=0){
                        s_token=curr_author_names_CSV.split(",");
                        
                            c10=0;
                            //
                            while(c10<s_token.length){
                            	
                        		//( not there, but ) there, so remove it 
                        		if(s_token[c10].indexOf("(")==-1 && s_token[c10].indexOf(")")>=0)
                        			s_token[c10]=s_token[c10].replace(")", "");
                            	
                            	if(s_token[c10].length()>1 && !IsInteger.isInteger(s_token[c10]))
                            		mapAuthorNames.put(s_token[c10], "");
                                c10++;
                            }
                        if(isSOPprint)
                        	System.out.println("\nCSV token:");
                        if(isWriteDebug){
                            writer_debugFile.append("\nCSV token:"+curr_author_names_CSV+"<-");
                            writer_debugFile.flush();
                        }
                     }
                     else if(curr_author_names_CSV.indexOf(" and ")>=0  || curr_author_names_CSV.indexOf(" & ")>=0  ){
                    	 
                    	 // 
                    	 if(curr_author_names_CSV.indexOf(" and ")>=0){
                    		 s_token=curr_author_names_CSV.split(" and ");
                    	 }
                    	 else if (curr_author_names_CSV.indexOf(" & ")>=0){
                    		 s_token=curr_author_names_CSV.split(" & ");
                    	 }
                         
                         c10=0;
                            //
                            while(c10<s_token.length){
                            	if(s_token[c10].length()>1 && !IsInteger.isInteger(s_token[c10])){
                            		
                            		//( not there, but ) there, so remove it 
                            		if(s_token[c10].indexOf("(")==-1 && s_token[c10].indexOf(")")>=0)
                            			s_token[c10]=s_token[c10].replace(")", "");
                            		
                            		mapAuthorNames.put(s_token[c10], "");
                            		
                            	}
                                c10++;
                            }
                         
                         if(isSOPprint)
                        	 System.out.println("\nAND CSV token:");
                         
                         if(isWriteDebug){
                            writer_debugFile.append("\nAND CSV token:"+curr_author_names_CSV+"<-");
                            writer_debugFile.flush();
                         }
                     }
                     else{ // DEFAULT (using CSV)
                         s_token=curr_author_names_CSV.split(",");
                         
                         c10=0;
                            //
                            while(c10<s_token.length){
                            	if(s_token[c10].length()>1 && !IsInteger.isInteger(s_token[c10])){
                            		
                            		//( not there, but ) there, so remove it 
                            		if(s_token[c10].indexOf("(")==-1 && s_token[c10].indexOf(")")>=0)
                            			s_token[c10]=s_token[c10].replace(")", "");
                            		
                            		mapAuthorNames.put(s_token[c10], "");
                            	}
                                c10++;
                            }
                            
                         if(isSOPprint)
                        	 System.out.println("\nNO CSV token:");
                         
                         if(isWriteDebug){
                            writer_debugFile.append("\nNO CSV token:"+curr_author_names_CSV+"<-");
                            writer_debugFile.flush();
                         }
                     }
                
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        return mapAuthorNames;
    }
    	//main
	public static void main(String[] args){
		TreeMap<String,String> mapout= new TreeMap<String, String>();
                String outFolder="/Users/lenin/Downloads/debug.txt";
                
                System.out.println("out:"+splitCSVAuthors_to_atom(  "lenin, hisako ueno and makiko inoue",
                                                                    "",
                                                                    false   //isSOPprint
                													));
                
        }
    
}
