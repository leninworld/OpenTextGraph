package crawler;
//package com.simplecode.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

class Xls2csv 
{
static void xls(File inputFile, File outputFile) 
{
        // For storing data into CSV files
        StringBuffer data = new StringBuffer();
        try 
        {
        FileOutputStream fos = new FileOutputStream(outputFile);

        // Get the workbook object for XLS file
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(inputFile));
        // Get first sheet from the workbook
        HSSFSheet sheet = workbook.getSheetAt(0);
        Cell cell;
        Row row;

        // Iterate through each rows from first sheet
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) 
        {
                row = rowIterator.next();
                // For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) 
                {
                        cell = cellIterator.next();
                        
                        switch (cell.getCellType()) 
                        {
                        case Cell.CELL_TYPE_BOOLEAN:
                    		//if(cell.getStringCellValue().length()>1)
                                data.append(cell.getBooleanCellValue() + ",");
                                break;
                                
                        case Cell.CELL_TYPE_NUMERIC:
                    		//if(cell.getStringCellValue().length()>1)
                                data.append(cell.getNumericCellValue() + ",");
                                break;
                                
                        case Cell.CELL_TYPE_STRING:
                        		if(cell.getStringCellValue().toString().length()>1)
                                data.append(cell.getStringCellValue() + ",");
                                break;

                        case Cell.CELL_TYPE_BLANK:
                             //   data.append("" + ",");
                                break;
                        
                        default:
                                //data.append(cell + ",");
                        	data.append(cell );
                        }
                        
                        System.out.println("d:"+data);
                }
                data.append('\n'); 
        }

        String a = (String)data.toString().replace(" ,", ",").replace(",,", "");
        //System.out.println(a+":"+a.length());
        
        //fos.write(data.toString().getBytes());
        fos.write(data.toString().replace(" ,", ",").replace(",,", ",").replace(",", ",").replace(",", ",")
        		.getBytes());
        
        fos.close();
        }
        catch (FileNotFoundException e) 
        {
                e.printStackTrace();
        }
        catch (IOException e) 
        {
                e.printStackTrace();
        }
        }

	//wrapper
	public static void wrapperxls(String inputFolderName, String outputFolder1,String slashtype){
		File file = new File(inputFolderName);
		File file2 = new File(outputFolder1);
		
		if(file.isDirectory() == false)
		{
			System.out.println(" Given file is not a directory");
		//	return;
		}
		
        String absolutePathOfInputFile = file.getAbsolutePath();
        String absolutePathOfOutputFile = file2.getAbsolutePath();
        String Parent =  file.getParent(); 
        
        String[] Filenames = file.list();
        String outputFileName1="";
        System.out.println("absolutePathOfInputFile:"+absolutePathOfInputFile);
        System.out.println("Parent:"+Parent);
        System.out.println("inputFolderName:"+inputFolderName);
		try{
		     
				//While loop to read each file and write into.
				for (int i=0; i < Filenames.length; i++)
				{
					outputFileName1 =Filenames[i];
					
				  try{
					  File fileinput = new File(absolutePathOfInputFile+ slashtype + Filenames[i]);
					  File fileoutput = new File(absolutePathOfOutputFile+ slashtype +  Filenames[i]+".csv");
					  System.out.println ("File input:"+absolutePathOfInputFile+ slashtype + Filenames[i]);
					  System.out.println ("File output:"+absolutePathOfOutputFile+ slashtype +  Filenames[i]+".csv");
					  System.out.println ("-----------------------");
					  //
					  xls( fileinput,  fileoutput  );
			
				  }
				  catch(Exception e)
				  {
					  System.out.println("An exception occurred while calling Write method ..!!");	
					  e.printStackTrace();
					  
				  }
				  
				}
				 
		  	   } //end try
				 catch(Exception e){
					 System.out.println("error.");
				 }
			
		}
		 
		// main
        public static void main(String[] args) 
        {
                File inputFile = new File("/Users/lenin/Dropbox/#Data/group-by-age-related/2010shrtbl02.xls");
                File outputFile = new File("/Users/lenin/Dropbox/#Data/group-by-age-related/2010shrtbl02.csv");
                //xls(inputFile, outputFile);
                
                String inputFolder = "/Users/lenin/Dropbox/#Data/group-by-age-related-in";
                String outputFolder="/Users/lenin/Dropbox/#Data/group-by-age-related-out";
                wrapperxls(inputFolder, outputFolder, "/");
        }
}