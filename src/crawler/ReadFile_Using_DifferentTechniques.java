package crawler;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadFile_Using_DifferentTechniques {
	
	
	//read file for date pattern and move the pointer to specific pattern start
	// targeted_MMM_for_block-> pass a month before. if "feb" is wanted, pass as "jan"
	public static int readFile2( String inputFile, 
								 String stringRegexPatternForDate,
								 String targeted_MMM_for_block,
								 String targeted_YYYY_for_block,
								 FileWriter writerDebugFile){
		targeted_MMM_for_block=targeted_MMM_for_block.toLowerCase().replace(" ", "");
		targeted_YYYY_for_block=targeted_YYYY_for_block.toLowerCase().replace(" ", "");
		final long t0 = System.nanoTime();
		int half_aFile_len=0;
		int totalBlock=0;
		if(stringRegexPatternForDate.length()==0){
			stringRegexPatternForDate="(\\d{2}).([a-zA-Z][a-zA-Z][a-zA-Z]).(\\d{4}).(\\d{2}):(\\d{2}):(\\d{2})";
		}
		String last15Lines="";
		int counterFor_last15lines=0;
		String t=null;
		int Split_by_ratio=2;
		try{
			//FileWriter writerDebug=new FileWriter(new File(debugFile));
			RandomAccessFile aFile = new RandomAccessFile(inputFile, "r");
			
		    half_aFile_len=(int) (aFile.length()/Split_by_ratio);
		    
	       	aFile.seek(half_aFile_len);
	       	
	        FileChannel inChannel = aFile.getChannel();
	        ByteBuffer buffer = ByteBuffer.allocate(1024);
	        int no_blocks_Of_buffer=0;
	        int last_half_aFile_len=0;
	        //
	        while(inChannel.read(buffer) > 0){
	        	no_blocks_Of_buffer++;
	            buffer.flip();
	            for (int i = 0; i < buffer.limit(); i++)
	            {
	            	//buffer.get();
	            	char st= (char) buffer.get();
	                //System.out.print(st);
	                t=t+String.valueOf(st);     
	            }
	            
	            counterFor_last15lines++;
	            if(t.length()>=100)
	              System.out.println("index:"+t.indexOf(stringRegexPatternForDate)+" t:"+t.substring(0, 60));
	            
	            last15Lines=last15Lines+t;
	            //
	            if(counterFor_last15lines>=25){
	            	String text_mmm=	 FindDateByRegex.
			    	    				 findDateByRegexPatternForGivenInputTextandReturnSpecificGroup
			    	    				 		( last15Lines
			    	    						 ,stringRegexPatternForDate,2).get(1);
	            	// OK
	            	if(last15Lines.indexOf(" "+targeted_YYYY_for_block+" ")>=0){
	            		
	            	}
	            	else{
	            		last15Lines="";
	            		counterFor_last15lines=0;
	            		t="";
	            		continue;
	            	}
	            		
	            	
	            	
	            	int mmmTarget_Integer=FindDateByRegex.findMonthIntegerFor_GivenMonthString(text_mmm);
	            	// find date
	            	System.out.println("@@@date mmm:"+ text_mmm+" mmmInteger:"+mmmTarget_Integer);
	            	int int_text_mmm= FindDateByRegex.findMonthIntegerFor_GivenMonthString(text_mmm);
	            	int int_target_mmm=FindDateByRegex.findMonthIntegerFor_GivenMonthString(targeted_MMM_for_block);
	            	writerDebugFile.append("\n------------");
	            	
	            	
	            	// move backward (may > feb_target)
	            	if( int_text_mmm>int_target_mmm){
	            		Split_by_ratio=Split_by_ratio*2;
	            		last_half_aFile_len=half_aFile_len;
	            		half_aFile_len=half_aFile_len-half_aFile_len/2;
	            		//aFile = new RandomAccessFile(inputFile, "r");
	            		aFile.seek(half_aFile_len);
	            		inChannel = aFile.getChannel();
	            		writerDebugFile.append("\n move.backward: "+half_aFile_len+" "+Split_by_ratio
	            				+" 15l:"
	            				+last15Lines.substring( last15Lines.indexOf("201")-25 , last15Lines.indexOf("201")+20)	
	            				+"\npair:"+int_text_mmm+" target:"+int_target_mmm
	            				+"<----\n");
	            		writerDebugFile.flush();
	            		last15Lines="";
	            		counterFor_last15lines=0;t="";
	            		
	            		//continue;
	            	} // move forward
	            	else if(int_text_mmm<int_target_mmm
	            			){ //move forward
	            		last_half_aFile_len=half_aFile_len;
	            		half_aFile_len=half_aFile_len+half_aFile_len/2;
	            		//aFile = new RandomAccessFile(inputFile, "r");
	            		aFile.seek(half_aFile_len);
	            		inChannel = aFile.getChannel();
	            		writerDebugFile.append("\n move.forward: "+" "+half_aFile_len
	            						+Split_by_ratio+" 15l:"
	            						+last15Lines.substring( last15Lines.indexOf("201")-25 , last15Lines.indexOf("201")+20)
	            						+"\npair:"+int_text_mmm+" target:"+int_target_mmm);
	            		writerDebugFile.flush();
	            		last15Lines=""; 
		    	        counterFor_last15lines=0;
	            		t="";
	            		
	            		//continue;
	            	}
	            	else{ //approximately found
	            		writerDebugFile.append("\n move.break found: "+last15Lines
	            						+" f.len:"+aFile.length()+" "+Split_by_ratio
	            						+" 15l:"+last15Lines.substring( last15Lines.indexOf("201")-25 , last15Lines.indexOf("201")+20  )
	            						+"\npair:"+int_text_mmm+" target:"+int_target_mmm
	            						);
	            		writerDebugFile.flush();
	            		//last_half_aFile_len=half_aFile_len;
	            		buffer.clear(); 
	            		break;
	            	}
	            	
	            	
	    
	    	        //break;
	            }
	            
//	            if(t.indexOf(pattern) >=0){
//	            	System.out.println("found");
//	            	buffer.clear(); 
//	            	break;
//	            }
	            
	            buffer.clear(); // do something with the data and clear/compact it.
	            
	        }
	        
	        String lastlast15Lines="";
    		//approximately move backward to find the first appearence of target_pattern 
			int tmp_half_aFile_len=half_aFile_len-1024; //move backward by 1kb
			int smaller=0;
			
//		    if(half_aFile_len<last_half_aFile_len)
//		    	smaller=half_aFile_len;
//		    else
//		    	smaller=last_half_aFile_len;
//		    smaller=smaller-(smaller/2);
//		    //51909274
//		    //18000000
//		    //smaller=51200*1000;
//		    aFile = new RandomAccessFile(inputFile, "r");
//		    //aFile.seek(0);
//			aFile.seek(smaller);
//    		inChannel = aFile.getChannel();
//    		
//			while(inChannel.read(buffer) > 0){
//	        	//no_blocks_Of_buffer++;
//	            buffer.flip();
//	            for (int i = 0; i < buffer.limit(); i++)
//	            {
//	            	//buffer.get();
//	            	char st= (char) buffer.get();
//	                //System.out.print(st);
//	                t=t+String.valueOf(st);     
//	            }
//	            counterFor_last15lines++;
//	            System.out.println("index:"+t.indexOf(stringRegexPatternForDate)
//	            				   +" "+t.toLowerCase().indexOf("01 "+targeted_MMM_for_block+" "));
//	            last15Lines=last15Lines+t;
//	            
//	            if(counterFor_last15lines>=15){
//	            		//
//		            if(last15Lines.toLowerCase().indexOf(" "+ targeted_MMM_for_block+" ")>=0){
//		            	System.out.println("break");
//		            	writerDebugFile.append("\n ----- break-->:\n"+last15Lines+"\n <--------");
//		            	writerDebugFile.flush();
//		            	break;
//		            }
//		            else{
//		            	last15Lines="";
//		            }
//	            
//		            counterFor_last15lines=0;
//	            }
//	       
	            
//	            if(counterFor_last15lines>=25){
//		            	if(  lastlast15Lines.indexOf(targeted_MMM_for_block)>=0
//		            	  && last15Lines.indexOf(targeted_MMM_for_block)==-1){
//		            		counterFor_last15lines=0;
//		            		break;
//		            	}
//	            	
//		            	// still found target_pattern, then move backward 
//		            	if(last15Lines.indexOf(targeted_MMM_for_block)>=0){
//		            		tmp_half_aFile_len=tmp_half_aFile_len-5024; //move backward by 1kb
//		        			aFile.seek(tmp_half_aFile_len);
//		            		inChannel = aFile.getChannel();
//		            	}
//	            		
//			    		counterFor_last15lines=0;
//			    		lastlast15Lines=last15Lines;
//			    		
//			    		writerDebugFile.append("\ntmp_half_aFile_len:"+tmp_half_aFile_len
//			    				+" 15l:"+last15Lines.substring(0, last15Lines.length()/200 )
//			    				+" len:"+last15Lines.length());
//			    		last15Lines="";
//			    		writerDebugFile.flush();
//	            }
//	            
//	            buffer.clear();
//	            
//			}
	        
	        
	        System.out.println("--------------test----------");
	        totalBlock=1024*no_blocks_Of_buffer;
	        
//	        buffer = ByteBuffer.allocate(1024);
//	    	aFile.seek(totalBlock-1024);
//	        while(inChannel.read(buffer) > 0){
//	        	no_blocks_Of_buffer++;
//	            buffer.flip();
//	            for (int i = 0; i < buffer.limit(); i++)
//	            {
//	            	//buffer.get();
//	            	char st= (char) buffer.get();
//	                //System.out.print(st);
//	                t=t+String.valueOf(st);     
//	            }
//	         //   System.out.println("start t:"+t);
//	            break;
//	        }
	    	
	        inChannel.close();
	        aFile.close();
	        
	        if(t.length()>=100)
	        System.out.println("--------------test---2-------"+t.substring(0, 65)+"<--test"+t.length()
	        					);
	        
	        writerDebugFile.append("--------------test---2-------"+"<--test"+t.length());
	        writerDebugFile.flush();
	        
	        System.out.println("readFileInChunksUsingFixedSizeBuffer.Time Taken:" +	NANOSECONDS.toSeconds(System.nanoTime() - t0) +" seconds; "
						    +  (NANOSECONDS.toSeconds(System.nanoTime() - t0))/60 +" minutes"
						    +" totalBlock:"+totalBlock+" last_half_aFile_len:"+last_half_aFile_len
						    +" half_aFile_len:" +half_aFile_len+"<-");
		}
		catch(Exception e){
			 e.printStackTrace();
		 } 
		return half_aFile_len;
	}
	// not test
	public static void readFile(String inputFile,
								String continueParsingFromThisPattern
								){
		
	    final int buffer_size=2024*1;
	    File fileinput = new File(inputFile);
	    char st='\0';
		int countForNumberOFbufferREAD=0;
		int randomSplitToStart=2;
		//Scanner scanner = new Scanner(fileinput.getAbsoluteFile(),"rw");
		
		String concLine2="";
		 
		ByteBuffer buffer = ByteBuffer.allocate(buffer_size);
		
		try {
			RandomAccessFile aFile = new RandomAccessFile(fileinput.getAbsolutePath(), "r");
			FileChannel inChannel = aFile.getChannel();
			
			//search for a pattern so that can skip to it using SEEK
			while(inChannel.read(buffer) > 0){
//				if(countForNumberOFbufferREAD==0)
//				 	aFile.seek(aFile.length()/randomSplitToStart);
				System.out.println("while..");
				//countForNumberOFbufferREAD++;
		 		buffer.flip();
		 	     for (int j = 0; j < buffer.limit(); j++){
		                //System.out.print((char) buffer.get());
		 	    	 	st= (char) buffer.get();
		                concLine2=concLine2+ st;
		                //writerDebug.append("..."+concLine+"\n");
		                 
		         }
		 	    System.out.println("after for.."+concLine2);
		 	     if(concLine2.indexOf(continueParsingFromThisPattern)>=0){
		 	    	 System.out.println("****found.."+
		 	    			 			 concLine2.substring(
		 	    			 					 concLine2.indexOf(continueParsingFromThisPattern),  
		 	    					 			 25)
		 	    					 			 );
		 	    	 break;
		 	     }
		 	     else{
		 	    	System.out.println("@@@@@not found:"+
		 	    			 			concLine2.substring(
						    			 					 1,  
						    					 			 50)+
						    			"#####"+concLine2.indexOf(continueParsingFromThisPattern)
						    			+"<---");
		 	    	concLine2= "";// String.valueOf(st);
		 	    
		 	    	
		 	    	//continue;
		 	     }
		 		buffer.clear();
		 	    //System.out.println(concLine2);
		 	    // read 15 buffers to see if pattern exist
		 	    //move forward
//		 	    if(countForNumberOFbufferREAD==22){
//		 	    	System.out.println("countForNumberOFbufferREAD:"+countForNumberOFbufferREAD
//		 	    			+";concLine2:"+concLine2);
//		 	    	countForNumberOFbufferREAD=0;
//		 	    	inChannel=aFile.getChannel();
//		 	    	randomSplitToStart=randomSplitToStart+1;
//		 	    }
//		 	    else{
//		 	    	inChannel=aFile.getChannel();
//		 	    }
		 	    //move backward
		 	    //buffer.clear();
			} // end of while
			
		} catch (Exception e) {
			// TODO: handle exception
		}	
	}
	// using file size buffer
	public static void readFileWithFileSizeBuffer(String inputFile){
		final long t0 = System.nanoTime();
		try{
			RandomAccessFile aFile = new RandomAccessFile(inputFile,"r");
		    FileChannel inChannel = aFile.getChannel();
		    long fileSize = inChannel.size();
		    ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
		    inChannel.read(buffer);
		    //buffer.rewind();
		    buffer.flip();
		    for (int i = 0; i < fileSize; i++)
		    {
		    	//buffer.get();
		        System.out.print((char) buffer.get());
		        
		    }
		    inChannel.close();
		    aFile.close();
		    System.out.println("readFileWithFileSizeBuffer.Time Taken:" 
		    				+	NANOSECONDS.toSeconds(System.nanoTime() - t0) +" seconds; "
		    				+  (NANOSECONDS.toSeconds(System.nanoTime() - t0))/60 +" minutes");
		}
		 catch(Exception e){
			 e.printStackTrace();
		 }
		 
	    }
	// read file in chunks
	public static void readFileInChunksUsingFixedSizeBuffer(String inputFile){
		final long t0 = System.nanoTime();
		try{
			RandomAccessFile aFile = new RandomAccessFile(inputFile, "r");
			
	        FileChannel inChannel = aFile.getChannel();
	        ByteBuffer buffer = ByteBuffer.allocate(1024);
	        while(inChannel.read(buffer) > 0)
	        {
	            buffer.flip();
	            for (int i = 0; i < buffer.limit(); i++)
	            {
	            	//buffer.get();
	            	char st= (char) buffer.get();
	                System.out.print(st);
	            }
	            buffer.clear(); // do something with the data and clear/compact it.
	        }
	        inChannel.close();
	        aFile.close();
	        System.out.println("readFileInChunksUsingFixedSizeBuffer.Time Taken:" +	NANOSECONDS.toSeconds(System.nanoTime() - t0) +" seconds; "
						    +  (NANOSECONDS.toSeconds(System.nanoTime() - t0))/60 +" minutes");
		}
		catch(Exception e){
			 e.printStackTrace();
		 } 
		
	}
	// read file using mapped byte buffer
	public static void readFileUsingMappedByteBuffer(String inputFile){
		final long t0 = System.nanoTime();
		try{
		    RandomAccessFile aFile = new RandomAccessFile(inputFile, "r");
	        FileChannel inChannel = aFile.getChannel();
	        MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
	        buffer.load(); 
	        for (int i = 0; i < buffer.limit(); i++)
	        {
	        	//buffer.get();
	            System.out.print((char) buffer.get());
	        }
	        buffer.clear(); // do something with the data and clear/compact it.
	        inChannel.close();
	        aFile.close();
	        System.out.println("readFileUsingMappedByteBuffer.Time Taken:" +	NANOSECONDS.toSeconds(System.nanoTime() - t0) +" seconds; "
				    +  (NANOSECONDS.toSeconds(System.nanoTime() - t0))/60 +" minutes");
		}
		catch(Exception e){
			 e.printStackTrace();
		 }
		
	}
	 //main
	 public static void main(String[] args) {
		 String inputFile="/Users/lenin/Downloads/Feeds-5-Jun/EMM Alert India";
		 inputFile="/Users/lenin/Downloads/Feeds-5-Jun/Victoria";
		 inputFile="/Users/lenin/Downloads/Feeds-5-Jun/Up to the Minute";
		 inputFile="/Users/lenin/Downloads/Feeds-5-Jun/Just In";
		 inputFile="/Users/lenin/Downloads/Feeds-5-Jun/EMM Alert India";
		  
		 inputFile="/Users/lenin/Downloads/Feeds-5-Jun.near1/Punjab";
		 inputFile="/Users/lenin/Downloads/Feeds-5-Jun-.dummy1/UptotheMinute";
		 inputFile="/Users/lenin/Downloads/Feeds-5-Jun-.dummy1/WSJcomOpinion";
		 inputFile="/Users/lenin/Downloads/Feeds-5-Jun-.dummy1/EMMAlertIndia";
		 inputFile="/Users/lenin/Downloads/Feeds-5-Jun-.dummy1/Breaking";
		 String debugFile="/Users/lenin/Downloads/Feeds-5-Jun-.dummy1/debug.txt";
		 
		 //Victoria
		 final long t0 = System.nanoTime();
		 try{
			 FileWriter writerDebug=new FileWriter(new File(debugFile));
			 //readFileWithFileSizeBuffer(inputFile); //8s , 34s, 65s, 5 m (318s)
			 
			 //readFileUsingMappedByteBuffer(inputFile); //7s ,32s,62s, 5 m (359s)
			 
			 //readFileInChunksUsingFixedSizeBuffer(inputFile); //8s, 33s, 64s, 4m (325s) best
			 String targeted_YYYY_for_block="2015";
 
			 System.out.println(readFile2(inputFile, "",
					 					  "feb",
					 					  targeted_YYYY_for_block,
					 					  writerDebug ));
			 
			 
			 String dummy=  
							"Date: Mon, 12/01/2015 18:50:36 EST"+
							"Message-Id: <SB12274156626456164535504580385061108174410@localhost.localdomain>"+
							"From: WSJ.com: Opinion"+
							"MIME-Version: 1.0";
			 
			 String pattern = "\\d{4}";
			 pattern = "([0-9])";
			 pattern = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
			 pattern = "2015";
			 pattern = "//";
			 //pattern = "\\d{4}";
			 Pattern p = Pattern.compile(pattern);
			 Matcher m =p.matcher(dummy);
			 Matcher matcher=Pattern.compile(pattern).matcher(dummy);
			 boolean t=m.find();
//			 System.out.println(" s "+t +" "+matcher.matches() +" "+m.group());
			 if(t){
			     //String day = m.group(1);
			     //String month = m.group(2);
				 //System.out.println(" s  :"+day+" "+month); 
			     
			 }
			 
			 System.out.println("Time Taken:" + NANOSECONDS.toSeconds(System.nanoTime() - t0) +" seconds; "
					       		+ (NANOSECONDS.toSeconds(System.nanoTime() - t0))/60 +" minutes"
					       		+ " "+ dummy.matches(pattern)
					       		+ " "+ dummy.matches(pattern) 
					       		+ " "+t
					       		+ " "+ m
					       		//+"\n "+ dummy.substring(0, 17)
					       	   );
 
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
		 
	    }
}