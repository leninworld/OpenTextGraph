package crawler;
import org.nnh.bean.Alert;
import org.nnh.bean.DeliveryTo;
import org.nnh.bean.HowMany;
import org.nnh.bean.HowOften;
import org.nnh.service.GAService;

import java.io.FileWriter;
import java.util.List;
import java.util.TreeMap;

//class
public class GoggleAlert {
	//add new alert
	//if offSetLinesToSkip =0 , no skips of any lines
	//flag={"writeAlertsToGoogleAlert","getAlertsFromGoogleAlert"}
	//"writeAlertsToGoogleAlert" -> alert API call to insert new alerts 
	public static void addGoogleAlert(String inFile, String outFile, 
									  String flag, int offSetLinesToSkip,
									  TreeMap<String, String> mapUsers, String oFile){
		
		LoadGoogleAlert_FileToMap lf=new LoadGoogleAlert_FileToMap();
		
		String guname="";
		GAService service=null; String password = "";
		TreeMap<Integer,String> mapAlertLines =lf.loadEachGoogleAlertLineToMap(inFile, "!!!", true);
		System.out.println("loaded alerts:"+mapAlertLines.size());
		int maxSize = mapAlertLines.size(); int counter=1;
		TreeMap<String, String> mapGotAlerts = new TreeMap();
		// Get alert by id. -> Alert alert = service.getAlertById(alertId);
		//
		try{
			FileWriter writer = new FileWriter(oFile, true);
			
			Alert alert = new Alert();
			alert.setHowMany(HowMany.ONLY_THE_BEST_RESULTS);
			alert.setHowOften(HowOften.ONCE_A_DAY);
			//alert.setResultType(ResultType.EVERYTHING);
			//alert.setSearchQuery("~fingerprint");
			
			if(flag.indexOf("getAlertsFromGoogleAlert")>=0){
				for(String uname: mapUsers.keySet()){
					
					
					  password=mapUsers.get(uname);
					  service = new GAService(uname, password);
					
					//);
					service.doLogin();
					List<Alert> lList = service.getAlerts();
					 for(Alert alert2 : lList)
					 {
						 System.out.println(alert2.getSearchQuery());
						 if(!mapGotAlerts.containsKey(alert2.getSearchQuery()))
						 mapGotAlerts.put(alert2.getSearchQuery(), "");
						 
					 }
					 for(String a:mapGotAlerts.keySet()){
						 writer.append(a+"\n");
						 writer.flush();	 
					 }
					 
					
					System.out.println(service.getAlerts().size());
				}
				writer.close();
			}
			else if(flag.indexOf("writeAlertsToGoogleAlert")>=0){
				//get pwd
				for(String uname: mapUsers.keySet()){
								guname=uname;
								password=mapUsers.get(guname);
				}
				System.out.println("name:"+guname+";pwd:"+password);
				
				service = new GAService(guname, password);
				service.doLogin();
			//
			while(counter<= maxSize){
				
				if(counter<=offSetLinesToSkip){
					System.out.println("skipping line->"+counter);
					System.out.println("name:"+guname+";pwd:"+password);
					counter++; continue;
				}
				String alert2 = (String) mapAlertLines.get(counter);
				List<Alert> lstAlert = service.getAlerts();
				int tAlertCnt=lstAlert.size();
				try{
					lstAlert = service.getAlerts(alert2);
					
					if(lstAlert.size()>0){ //skip existing (already added)
						System.out.println("Already existing:"+alert2);
						counter++;
						continue;
					}
					
					if(lstAlert.size()>=1000){
						break;
					}
					
					alert.setSearchQuery(alert2);
					alert.setDeliveryTo(DeliveryTo.EMAIL);
 					String id = service.createAlert(alert);
					System.out.println("Done:"+alert2+";Already found?"+lstAlert.size()+";Total Alert:"+tAlertCnt+";counter:"+counter
											+ " (Out of "+maxSize+")");
					Thread.sleep(20000); //sleep
				}
				catch(Exception e){
					System.out.println("error");
					e.printStackTrace();
				}
				
				counter++;
			}//end of while
			}// else end
		  } //try end
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	// main
	public static void main(String[] args) {
		
		TreeMap<String, String> mapUsers = new TreeMap<String, String>();

		
		String folder= "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/";
		//out.listofbeach.all.country.txt
		// add alert 
		addGoogleAlert(folder +"out.listofhospitals.euro.all.txt",
				folder +"whileWritingToGoogleAlert.listofhospitals.euro.all.txt",
				"writeAlertsToGoogleAlert", //{"writeAlertsToGoogleAlert","getAlertsFromGoogleAlert"}
				2284, // number of lines to be skipped ( from starting line 1)
				mapUsers,
				folder+"outAllAlerts.txt");
	 
	}		 

}