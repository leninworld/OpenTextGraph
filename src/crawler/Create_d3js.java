package crawler;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

public class Create_d3js {
	
	//create_d3js_for_a_given_xp_number_of_GBAD_file
	public static void create_d3js_for_a_given_xp_number_of_GBAD_file(String baseFolder,
																	  String input_gbad_file,
																	  int    xp_number_interested,
																	  String comment_to_add_2_html,
																	  boolean isSOPprint
																	  ){
		String d3JS_nodeEdgeString="{nodes:[";
		int curr_label_int_only=0;

		
		try {
			//
			TreeMap<Integer,String> map_loaded=
					  ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
					 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(input_gbad_file, 
																							 	 -1, //startline, 
																								 -1, //endline,
																								 "create GBd for give XP", //debug_label
																								 isSOPprint //isPrintSOP
																								 );
			int begin_seq=-1;int end_seq=-1;
			int lineNumber=0;
			// get start and end line number 
			for(int seq:map_loaded.keySet()){

				if(begin_seq>0 && map_loaded.get(seq).indexOf("XP # "+ xp_number_interested )>=0 ){
					end_seq=seq-1;
					break;
				}
				
				// 
				if(map_loaded.get(seq).indexOf("XP # "+ xp_number_interested )>=0 ){
					begin_seq=seq;
				}
				lineNumber=seq;
			}
			System.out.println("create_d3js..() begin:"+begin_seq+";end:"+end_seq); //+"" +map_loaded);
			if(begin_seq>0 && end_seq==-1){// xp # 1 and only 1 XP in this file
				end_seq=map_loaded.size();
			}
			
			System.out.println("create_d3js..() 2 begin:"+begin_seq+";end:"+end_seq);
			TreeMap<String, Integer> map_NodeName_uniqID=new TreeMap<String, Integer>();
			TreeMap<Integer,String> map_uniqID_NodeName=new TreeMap<Integer,String>();
			
			//read only begin and end RANGE 
			for(int seq:map_loaded.keySet()){
				System.out.println("vertex create:"+seq);
				//range match for XP of interest
				if(seq>=begin_seq && seq<=end_seq){
					String eachLine=map_loaded.get(seq);
					if(eachLine.indexOf("v ")>=0){
						// : & = may be special char in html. so replace
						eachLine=eachLine.replace(":", "#").replace("=", "@");
						
						String []s =eachLine.replace("v ", "").split(" ");
						//
						if(s.length==2){
							//
							map_uniqID_NodeName.put( Integer.valueOf(s[0]), s[1]);
							map_NodeName_uniqID.put( s[1] , Integer.valueOf(s[0])); 
						}
						else if(s.length>2){ //IF THERE IS space inside NodeName , it will give >2 tokens, so concatenate it back to original
							
							String nodeName=""; int c=0;
							//
							while(c<s.length ){
								if(c>=1){
									if(nodeName.length()==0)
										nodeName=s[c];
									else
										nodeName=nodeName+" "+s[c];
								}
								c++;
							}
							map_uniqID_NodeName.put( Integer.valueOf(s[0]), nodeName);
							map_NodeName_uniqID.put( nodeName , Integer.valueOf(s[0]));
						}
					} //if(eachLine.indexOf("v ")>=0){
				}
				
			}
			
//			d3JS_nodeEdgeString=d3JS_nodeEdgeString+"{name:\""+ map_uniqID_NodeName.get(id_).replace("'", "").replace("\\", "") 
//					  + " "+tmp_config //configuration element such as $_n and $
//					  +"\""+"},";
			 
			
			//NODES
			for(int id_:map_uniqID_NodeName.keySet()){
				d3JS_nodeEdgeString=d3JS_nodeEdgeString+"{name:\""+ id_ 
						  					+ " "+map_uniqID_NodeName.get(id_).replace("'", "").replace("\\", "").replace("\"", "")  //configuration element such as $_n and $
						  						+"\""+"},";
				
			}
			
			//remove last comma
			d3JS_nodeEdgeString=d3JS_nodeEdgeString.substring(0, d3JS_nodeEdgeString.length()-1);
			d3JS_nodeEdgeString=d3JS_nodeEdgeString+"],\n edges:[";
			System.out.println("ceated d3js BEFORE EDGE CREATION");
			
			//CREATION OF EDGE 
			//read only begin and end RANGE
			for(int seq:map_loaded.keySet()){
				if(isSOPprint)
					System.out.println("edge create:"+seq);
				//range match for XP of interest
				if(seq>=begin_seq && seq<=end_seq){
					if(map_loaded.get(seq).indexOf("d ")>=0 || map_loaded.get(seq).indexOf("u ")>=0){
						String line=map_loaded.get(seq).replace("u ", "").replace("d ", "");
						String []s=line.split(" ");
						if(s.length==2){ 
							d3JS_nodeEdgeString=d3JS_nodeEdgeString+"{source:"+ (Integer.valueOf(map_NodeName_uniqID.get(s[0]))-1)+","+
						    								" target:"+(Integer.valueOf(map_NodeName_uniqID.get(s[1]))-1)+ ","+
						    								" label: "+curr_label_int_only
						    								+ "},";
						}
						else if(s.length>2){ //IF THERE IS space inside nodeName , it will give >2 tokens, so concatenate it back to original nodeName
							
							String nodeName=""; int c=0;
							//
							while(c<s.length ){
								if(c>=1){
									if(nodeName.length()==0)
										nodeName=s[c];
									else
										nodeName=nodeName+" "+s[c];
								}
								c++;
							}
//							d3JS_nodeEdgeString=d3JS_nodeEdgeString+"{source:"+ (Integer.valueOf(map_NodeName_uniqID.get(s[0]))-1)+","+
//																	" target:"+(Integer.valueOf(map_NodeName_uniqID.get(s[1]))-1)+ ","+
//																	" label: "+curr_label_int_only
//																	+ "},";
							
							//System.out.println("d3js edge:1:"+s[0]+" 2:"+s[1]);
							d3JS_nodeEdgeString=d3JS_nodeEdgeString+"{source:"+ (Integer.valueOf( (s[0]))-1)+","+
																	" target:"+(Integer.valueOf((s[1]))-1)+ ","+
																	" label: "+curr_label_int_only
																	+ "},";
						}
					}//END if(map_loaded.get(seq).indexOf("d ")>=0 || map_loaded.get(seq).indexOf("u ")>=0){
				} //END if(seq>=begin_seq && seq<=end_seq){
			} //END for(int seq:map_loaded.keySet()){
			d3JS_nodeEdgeString=d3JS_nodeEdgeString.substring(0, d3JS_nodeEdgeString.length()-1);
			d3JS_nodeEdgeString=d3JS_nodeEdgeString+"]};";
			
			if(isSOPprint)
				System.out.println("d3JS_nodeEdgeString:"+d3JS_nodeEdgeString);
			
			create_d3js_file1(d3JS_nodeEdgeString,baseFolder +"ds3_output.html", comment_to_add_2_html );
			System.out.println("ceated d3js FINAL");
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	//d3js
	public static void create_d3js_file1(String d3jsDataString,
										 String outFile,
										 String comment_to_add_2_html
									     ){
		try{
			System.out.println("Inside create_d3js_file1() creating ..");
			String prefix="<!DOCTYPE html><html lang=\"en\">\n"+
					"<head>\n"+
			"<meta charset=\"utf-8\">\n"+
			"<title>Force Layout with labels on edges</title>\n"+

			"<script src=\"http://d3js.org/d3.v3.min.js\" charset=\"utf-8\"></script>\n"+
			"<style type=\"text/css\">\n"+
			"</style>\n"+
			"</head>\n"+
			"<body>\n"+
			"<script type=\"text/javascript\">\n"+
			 "   var w = 2500;\n"+
			 "   var h = 1400;\n"+
			 "   var linkDistance=100;\n"+
			 "   var colors = d3.scale.category10();\n"+
			 "   var dataset = ";
			
			
			    String suffix=
			 "\n\n   var svg = d3.select(\"body\").append(\"svg\").attr({\"width\":w,\"height\":h});\n"+
			 "   var force = d3.layout.force()\n"+
			 "       .nodes(dataset.nodes)\n"+
			 "       .links(dataset.edges)\n"+
			 "       .size([w,h])\n"+
			 "       .linkDistance([linkDistance])\n"+
			 "       .charge([-500])\n"+
			 "       .theta(0.1)\n"+
			 "       .gravity(0.05)\n"+
			 "       .start();\n"+

			 

			 "   var edges = svg.selectAll(\"line\")\n"+
			 "     .data(dataset.edges)\n"+
			 "     .enter()\n"+
			 "     .append(\"line\")\n"+
			 "     .attr(\"id\",function(d,i) {return 'edge'+i})\n"+
			 "     .attr('marker-end','url(#arrowhead)')\n"+
			 "     .style(\"stroke\",\"#ccc\")\n"+
			 "     .style(\"pointer-events\", \"none\");\n"+   
			 "   var nodes = svg.selectAll(\"circle\")\n"+
			 "     .data(dataset.nodes)\n"+
			 "     .enter()\n"+
			 "     .append(\"circle\")\n"+
			 "     .attr({\"r\":15})\n"+
			 "     .style(\"fill\",function(d,i){return colors(i);})\n"+
			 "     .call(force.drag)\n"+
			 "   var nodelabels = svg.selectAll(\".nodelabel\")\n"+ 
			 "      .data(dataset.nodes)\n"+
			 "      .enter()\n"+
			 "      .append(\"text\")\n"+
			 "      .attr({\"x\":function(d){return d.x;},\n"+
			 "             \"y\":function(d){return d.y;},\n"+
			 "             \"class\":\"nodelabel\",\n"+
			 "             \"stroke\":\"black\"})\n"+
			 "      .text(function(d){return d.name;});\n\n"+
			 "   var edgepaths = svg.selectAll(\".edgepath\")\n"+
			 "       .data(dataset.edges)\n"+
			 "       .enter()\n"+
			 "       .append('path')\n"+
			 "       .attr({'d': function(d) {return 'M '+d.source.x+' '+d.source.y+' L '+ d.target.x +' '+d.target.y},\n"+
			 "             'class':'edgepath',\n"+
			 "              'fill-opacity':0,\n"+
			 "              'stroke-opacity':0,\n"+
			 "              'fill':'blue',\n"+
			 "              'stroke':'red',\n"+
			 "              'id':function(d,i) {return 'edgepath'+i}})\n"+
			 "       .style(\"pointer-events\", \"none\");\n\n"+
			 "   var edgelabels = svg.selectAll(\".edgelabel\")\n"+
			 "       .data(dataset.edges)\n"+
			 "       .enter()\n"+
			 "       .append('text')\n"+
			 "       .style(\"pointer-events\", \"none\")\n"+
			 "       .attr({'class':'edgelabel',\n"+
			 "              'id':function(d,i){return 'edgelabel'+i},\n"+
			 "              'dx':80,\n"+
			 "              'dy':0,\n"+
			 "              'font-size':10,\n"+
			 "              'fill':'#aaa'});\n\n"+
		"\n			    edgelabels.append('textPath')\n"+
		".attr(\"xlink:href\",function(d,i) { return \"#edgepath\" + i;})"+  //these three columns added after example http://jsfiddle.net/bc4um7pc/
		".text(function(d) { "+
		"   return d.label; "+
		"});			 "+   


//"			        .attr('xlink:href',function(d,i) {return '#edgepath'+i})\n"+
//"			        .style(\"pointer-events\", \"none\")\n"+
//"			        .text(function(d,i){return 'label '+i});\n"+




			    "\nsvg.append('defs').append('marker')\n"+
			     "   .attr({'id':'arrowhead',\n"+
			      "         'viewBox':'-0 -5 10 10',\n"+
			      "         'refX':25,\n"+
			      "         'refY':0,\n"+
			      "         //'markerUnits':'strokeWidth',\n"+
			      "         'orient':'auto',\n"+
			      "         'markerWidth':10,\n"+
			       "        'markerHeight':10,\n"+
			        "       'xoverflow':'visible'})\n"+
			       " .append('svg:path')\n"+
			        "    .attr('d', 'M 0,-5 L 10 ,0 L 0,5')\n"+
			        "    .attr('fill', '#ccc')\n"+
			         "   .attr('stroke','#ccc');\n\n"+
			    "force.on(\"tick\", function(){\n"+

			     "   edges.attr({\"x1\": function(d){return d.source.x;},\n"+
			     "               \"y1\": function(d){return d.source.y;},\n"+
			     "               \"x2\": function(d){return d.target.x;},\n"+
			      "              \"y2\": function(d){return d.target.y;}\n"+
			      "  });\n"+

			     "  nodes.attr({\"cx\":function(d){return d.x;},\n"+
			     "               \"cy\":function(d){return d.y;}\n"+
			     "   });\n"+

			      "  nodelabels.attr(\"x\", function(d) { return d.x; })\n"+ 
			       "           .attr(\"y\", function(d) { return d.y; });\n"+

			       " edgepaths.attr('d', function(d) { var path='M '+d.source.x+' '+d.source.y+' L '+ d.target.x +' '+d.target.y;\n"+
			       "                                    //console.log(d)\n"+
			       "                                    return path});    \n"+   

			        "edgelabels.attr('transform',function(d,i){\n"+
			        "    if (d.target.x<d.source.x){\n"+
			        "        bbox = this.getBBox();\n"+
			        "        rx = bbox.x+bbox.width/2;\n"+
			        "        ry = bbox.y+bbox.height/2;\n"+
"			                return 'rotate(180 '+rx+' '+ry+')';\n"+
"			                }\n"+
"			            else {\n"+
"			                return 'rotate(0)';\n"+
"			                }\n"+
"			        });\n"+
"			    });\n"+
"			</script>\n"+
"			</body>\n"+
" <p style=\"font-size:26px\">"+comment_to_add_2_html+"</p>"+
"			</html>";
			
			FileWriter writer=new FileWriter(new File(outFile));
			
			writer.append(prefix+d3jsDataString+suffix);
			writer.flush();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	// main
	public static void main(String[] args) {
		String 	baseFolder="/Users/lenin/Downloads/#problems/p19/10minsplitC1065/";
		String 	input_gbad_file=baseFolder+"C1065_auth_OUTPUT_allcopy.txt";
		int 	xp_number_interested=1;
		boolean isSOPprint=false;
		
		//
		create_d3js_for_a_given_xp_number_of_GBAD_file(
														baseFolder,
														input_gbad_file,
														xp_number_interested,
														"",//comment_to_add_2_html
														isSOPprint
														);
		
	}

}
