package crawler;

import java.io.FileWriter;
import java.util.TreeMap;
import java.text.SimpleDateFormat;
import java.util.Date;

public class vast2018createTopology {

    String filename = "";
    String delimiter;

    //
    public static String convert(){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E, y-M-d 'at' h:m:s a z");
        System.out.println("Format 1:   " + dateFormatter.format(82799950));

        return "";

    }

    //
    public static void createTopo(
                                String baseFolder,
                                String f1_index,
                                String f2_meeting,
                                String outputFile

    ){

        try{
            FileWriter writerDebug = new FileWriter(baseFolder + "debug.txt");
            FileWriter writerOut = new FileWriter(outputFile);
            //


            TreeMap<Integer,TreeMap<Integer,String>> out1 =
                                        crawler.LoadMultipleValueToMap4.LoadMultipleValueToMap3(
                                                f1_index,
                                                ",",
                                                writerDebug
                                        );


            TreeMap<Integer,TreeMap<Integer,String>> out2 =
                    crawler.LoadMultipleValueToMap4.LoadMultipleValueToMap4(
                            f2_meeting,
                            ",",
                            writerDebug
                    );


            System.out.println(out1.get(1).size());
            System.out.println(out2.get(1).size());
            // index file
            TreeMap<Integer, String> map_lno_fname =out1.get(1);
            TreeMap<Integer, String> map_lno_lname =out1.get(2);
            TreeMap<Integer, String> map_lno_id =out1.get(3);

            TreeMap<Integer, String> map_id_fname = new TreeMap();
            TreeMap<Integer, String> map_id_lname = new TreeMap();
            // Meeting file
            TreeMap<Integer, String> map_lno_sid =out2.get(1);
            TreeMap<Integer, String> map_lno_type =out2.get(2);
            TreeMap<Integer, String> map_lno_did =out2.get(3);
            TreeMap<Integer, String> map_lno_datetime =out2.get(4);

            TreeMap<Integer, String> map_sid_destidCSV = new TreeMap<Integer, String>();
            TreeMap<Integer, String> map_sid_datetimeCSV = new TreeMap<Integer, String>();
            TreeMap<Integer, String> map_sid_destFirstNameCSV = new TreeMap<Integer, String>();
            TreeMap<Integer, String> map_sid_destLastNameCSV = new TreeMap<Integer, String>();

            // index file
            for(int ln:map_lno_id.keySet()){
                try {
                    map_id_fname.put(Integer.valueOf(map_lno_id.get(ln)), map_lno_fname.get(ln));
                    map_id_lname.put(Integer.valueOf(map_lno_id.get(ln)), map_lno_lname.get(ln));
                }
                catch(Exception e){
                    System.out.println("error 1 " + e.getMessage());
                }
            }

            // meeting files -> create output  source_id!!!meeting destination id csv
            for(int ln:map_lno_sid.keySet()){
                try{
                    int sid = Integer.valueOf(map_lno_sid.get(ln));
                    int did = Integer.valueOf(map_lno_did.get(ln));
                    String datetime =  map_lno_datetime.get(ln);
                    String dest_firstName = map_id_fname.get(did);
                    String dest_lastName = map_id_lname.get(did);

                    // sid and destination id csv
                    if(!map_sid_destidCSV.containsKey(sid)){
                        map_sid_destidCSV.put(sid, String.valueOf(did));
                        // destination first name / last name
                        map_sid_destFirstNameCSV.put(sid, dest_firstName);
                        map_sid_destLastNameCSV.put(sid, dest_lastName);
                    }
                    else{
                        String tmp = map_sid_destidCSV.get(sid)  +","+did;
                        map_sid_destidCSV.put(sid, tmp);

                        //                         destination first name / last name
                        tmp = map_sid_destFirstNameCSV.get(sid) + "," + dest_firstName;
                        map_sid_destFirstNameCSV.put(sid, tmp);

                        tmp = map_sid_destLastNameCSV.get(sid) + "," +dest_lastName;
                        map_sid_destLastNameCSV.put(sid, tmp);

                    }
                    // sid and datetime csv
                    if(!map_sid_datetimeCSV.containsKey(sid)){
                        map_sid_datetimeCSV.put(sid, String.valueOf(datetime));
                    }
                    else{
                        String tmp = map_sid_datetimeCSV.get(sid)+","+datetime;
                        map_sid_datetimeCSV.put(sid, tmp);
                    }

                }
                catch(Exception e){
                    System.out.println("error 2:" + e.getMessage());
//                    e.printStackTrace();
                }
            }

            writerOut.append("source id!!!source firstname!!!source lastname!!!destination id csv!!!destination timestamp csv!!!" +
                            "destination firstname csv!!!destination lastname csv"+"\n");
            writerOut.flush();

            for(int id : map_id_fname.keySet()){
                if(map_sid_destidCSV.get(id) == null){
                    continue;
                }

                writerOut.append(id+"!!!"+map_id_fname.get(id)+"!!!"+ map_id_lname.get(id)
                                    +"!!!" + map_sid_destidCSV.get(id) +"!!!"+map_sid_datetimeCSV.get(id)
                                    +"!!!" +map_sid_destFirstNameCSV.get(id) +"!!!" + map_sid_destLastNameCSV.get(id)
                                    +"\n"
                                );
                writerOut.flush();
            }

            System.out.println("map_id_fname:"+map_id_fname.size());
            System.out.println("map_sid_destidCSV:"+map_sid_destidCSV.size());
            System.out.println("map_sid_datetimeCSV:"+map_sid_datetimeCSV.size());

            System.out.println(map_sid_destidCSV.get(1288235)
                            +" time:"+map_sid_datetimeCSV.get(1288235));

        }
        catch(Exception e){
            System.out.println("error 3 " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static void main(String[] args){

        String baseFolder = "/Users/lenin/Dropbox/2018 Mini-Challenge 3/";
        String f_index = baseFolder + "CompanyIndex.csv";
        String f_meetings = baseFolder + "meetings.csv";
        String f_emails = baseFolder + "emails.csv";
        String f_calls = baseFolder +"calls.csv";
        String f_purchase = baseFolder +"purchases.csv";

        System.out.println("meeting..");
        String outputFile = baseFolder + "out_meetings.csv";

        // create topology (meetings)
        createTopo( baseFolder,
                    f_index,
                    f_meetings,
                    outputFile);

        System.out.println("calls..");
        outputFile = baseFolder + "out_calls.csv";

        // create topology (calls)
        createTopo( baseFolder,
                    f_index,
                    f_calls,
                    outputFile);

        System.out.println("emails..");
        outputFile = baseFolder + "out_emails.csv";
        // create topology (emails)
        createTopo( baseFolder,
                    f_index,
                    f_emails,
                    outputFile);

        System.out.println("purchase..");
        outputFile = baseFolder + "out_purchase.csv";
        // create topology (purchase)
        createTopo( baseFolder,
                    f_index,
                    f_purchase,
                    outputFile);


    }
}
