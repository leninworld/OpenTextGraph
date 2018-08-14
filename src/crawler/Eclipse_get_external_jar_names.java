package crawler;

import java.io.File;
import java.io.FileWriter;

import jflex.sym;

public class Eclipse_get_external_jar_names {
	
	// 
	public static void eclipse_get_external_jar_names_N_pom_xml_file( ){
		
		String baseFolder="/Users/lenin/OneDrive/jar/";
		String f1=baseFolder+"common";
		String f2=baseFolder+"additional";
		String f3=baseFolder+"google-api-java-client/libs/";
		String f4=baseFolder+"springframework";
		String f5=baseFolder;
		FileWriter writer=null;
		
		try {
		
		
		writer=new FileWriter(new File(baseFolder+"new_manualPOM.txt"));
		
		String [] folders=new String[5];
		String [] folders_parent_dir=new String[5];
		
		folders[0]= f1; folders[1]= f2;
		folders[2]= f3; folders[3]= f4; folders[4]= f5;
		
		folders_parent_dir[0]="common";
		folders_parent_dir[1]="additional";
		folders_parent_dir[2]="google-api-java-client/libs";
		folders_parent_dir[3]="springframework";
		folders_parent_dir[4]="";
		
		int max=folders.length; int cnt=0; int cnt3=0;
		System.out.println("folders:"+max);
			// 
			while (cnt<max) {
				
				String [] files = new File( folders[cnt] ).list();
				
				int max_files=files.length;
				System.out.println("max_files:"+max_files+" for "+ folders[cnt] );
				int cnt2=0;
				
				//each file
				while (cnt2 < max_files) {
					cnt3++;
					File currFile= new File(files[cnt2]);
					
					
					if(files[cnt2].indexOf("context")>=0)
						System.out.println("found");
					//skip if folder
					if(currFile.isDirectory()){
						System.out.println("direcory:skip..." + files[cnt2]);
						cnt2++; continue;
					}
					
					String version= files[cnt2].substring( files[cnt2].indexOf("-")+1 , files[cnt2].length())
										.replace(".jar", "");
					//System.out.println("1.cnt2:"+cnt2+" max_files:"+max_files);
					//System.out.println( folders_parent_dir[cnt]);
					//
					//only jar
					if(currFile.getName().indexOf(".jar")>=0){
						writer.append("\n");
						writer.append("<dependency>"+"\n");
						writer.append("<groupId>"+currFile.getName().replace(".jar", "")+"</groupId>"+"\n");
						writer.append("<artifactId>LIB_NAME_"+cnt+"_"+cnt3+"</artifactId>"+"\n");
						writer.append("<version>"+version+"</version>"+"\n");
						writer.append("<scope>system</scope>"+"\n");
						String tmp="";
						if(folders_parent_dir[cnt].length()>0)
							  tmp="<systemPath>${project.basedir}/lib/"
									+ folders_parent_dir[cnt] + "/"
									+currFile.getName() +"</systemPath>";
						else
							  tmp="<systemPath>${project.basedir}/lib/"
									+ folders_parent_dir[cnt] 
									+currFile.getName() +"</systemPath>";
						 
						
						
						writer.append(tmp+"\n");
						writer.append("</dependency> "+"\n");
						writer.flush();
					
					}
					
					cnt2++;
				}
				
				cnt++;
			}
		
		
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		
	}

	

	// main
	public static void main(String[] args) throws Exception{

		eclipse_get_external_jar_names_N_pom_xml_file( );
	}
}
