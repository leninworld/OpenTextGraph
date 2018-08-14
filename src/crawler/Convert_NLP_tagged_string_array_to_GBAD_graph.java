package crawler;

import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;
 

public class Convert_NLP_tagged_string_array_to_GBAD_graph {
	
     //  convert_NLP_tagged_string_array_to_GBAD_graph (convert (1) to GBAD (2) d3 graph)
	// it writes nodes to separate file for d3
	// it writes edges to separate file for d3
	// http://bl.ocks.org/jhb/5955887 <- d3 graph edge labels
  public static String  convert_NLP_tagged_string_array_to_GBAD_graph(String 	input_taggedNLP,
		  															  String 	outputFile_1,
		  															  boolean   is_append_outputFile_1,
		  															  boolean 	is_allow_write_to_outputFile_1,
		  															  String 	outputFile_d3_visualization,
		  															  boolean   is_append_outputFile_d3_visualization,
		  															  String	outputFile_d3_nodes_only,
		  															  String 	outputFile_d3_edges_only
		  															  ) {
	  
	  input_taggedNLP=input_taggedNLP.replace("   ", " ").replace("  ", " ");
	  String [] taggedNLP=input_taggedNLP.split(" ");
	  int vertex_ID=0;
	  
      int len=taggedNLP.length;
      int cnt_TokenID_position_in_line=0;
      TreeMap<Integer, String> mapCurrVertexID_Word=new TreeMap<Integer, String>();
      TreeMap<Integer, String> mapCurrVertexID_Tag=new TreeMap<Integer, String>();
      TreeMap<String, String> mapAlreadyWroteEdgePair=new TreeMap<String, String>();
      TreeMap<String, String> map_for_writer2_d3_graph=new TreeMap<String, String >();
      
      String first_Noun_From_curr_line=""; int first_Noun_From_curr_line_VertexID=0;
      String curr_word="";
      String first_Verb_From_curr_line="";int XP_count=0;
      int LastIteration_first_Noun_From_curr_line_VertexID=-1;
      int first_Verb_From_curr_line_VertexID=0;
	  try{
		  FileWriter writer=new FileWriter(new File(outputFile_1));
		  FileWriter writer2_d3_graph=new FileWriter(new File(outputFile_d3_visualization));
//		  FileWriter writer2_d3_graph_nodes_only=new FileWriter(new File(outputFile_d3_nodes_only));
//		  FileWriter writer2_d3_graph_edges_only=new FileWriter(new File(outputFile_d3_edges_only));

		  XP_count++;
		  writer.append("\nXP # "+XP_count);
		  // each token
		  while(cnt_TokenID_position_in_line<len){
			  
			  int _dash=taggedNLP[cnt_TokenID_position_in_line].indexOf("_");
			  curr_word=taggedNLP[cnt_TokenID_position_in_line].substring
					  											(0,
					  											 _dash 
																 );
			  String curr_tag=taggedNLP[cnt_TokenID_position_in_line]
					  					.substring(_dash+1, 
					  								taggedNLP[cnt_TokenID_position_in_line].length());
			  //
			  if( curr_tag.indexOf("NN")>=0 ||
				  curr_tag.indexOf("JJ")>=0 ||
				  curr_tag.indexOf("VB")>=0 
					  ){
				  //increase vertex ID
				  vertex_ID++;
				  mapCurrVertexID_Word.put(vertex_ID, curr_word);
				  mapCurrVertexID_Tag.put(vertex_ID, curr_tag);
			  }
			  else{
				  cnt_TokenID_position_in_line++;
				  continue;
			  }
			  
			  //blank
			  if(first_Noun_From_curr_line.length()==0 && 
				 curr_tag.indexOf("NN")>=0){
				  first_Noun_From_curr_line=curr_word;
				  first_Noun_From_curr_line_VertexID=vertex_ID;
			  }
			  if(first_Verb_From_curr_line.length()==0 && 
					  curr_tag.indexOf("VB")>=0){
				 first_Verb_From_curr_line=curr_word;
				 first_Verb_From_curr_line_VertexID=vertex_ID;
			  }
			  
			  System.out.println(taggedNLP[cnt_TokenID_position_in_line]
						  	 	 +"!!!"+_dash
						  		 +"!!!"+curr_tag
						  		 +"!!!"+ vertex_ID
						  		);
			   
			  //
			  if(taggedNLP[cnt_TokenID_position_in_line].indexOf("._")>=0){
				  System.out.println("first_Noun_From_curr_line:"+first_Noun_From_curr_line
						  			+"\nfirst_Verb_From_curr_line:"+first_Verb_From_curr_line);
				  System.out.println("mapCurrVertex:"+mapCurrVertexID_Word);
				  System.out.println("first_Noun_From_curr_line_VertexID:"+first_Noun_From_curr_line_VertexID+
						  			 "\nfirst_Verb_From_curr_line_VertexID:"+first_Verb_From_curr_line_VertexID);
				  int first_vertex_id_in_this_set=-1;
				  int last_vertex_id_in_this_set=-1;
				  //write to output file
				  //each vertex
				  for(int i:mapCurrVertexID_Word.keySet()){
					  //
					  if(first_vertex_id_in_this_set<0){
						  first_vertex_id_in_this_set=i;
					  }
					  
					  writer.append("\nv "+i+" "+"\""+mapCurrVertexID_Word.get(i)
							  		+"-"+mapCurrVertexID_Tag.get(i)+"\"");
					  writer.flush();
					  
//					  writer2_d3_graph.append("\n{name: \""+mapCurrVertexID_Word.get(i)
//							  								+"-"+mapCurrVertexID_Tag.get(i)
//							  								+"\"},");
//					  writer2_d3_graph.flush();
					  map_for_writer2_d3_graph.put("\n{name: \""+mapCurrVertexID_Word.get(i)
													+"-"+mapCurrVertexID_Tag.get(i)
													+"\"},", "dummy");
					  
					  last_vertex_id_in_this_set=i;
				  }
				  
				  // write edge 
				  if(!mapAlreadyWroteEdgePair.containsKey("d "+first_Noun_From_curr_line_VertexID+" "
							 +first_Verb_From_curr_line_VertexID)
					 && first_Verb_From_curr_line_VertexID>0
					 && first_Noun_From_curr_line_VertexID>0
						  ){
						//link first noun and first verb
						writer.append("\nd "+first_Noun_From_curr_line_VertexID+" "
									        +first_Verb_From_curr_line_VertexID+" "
									        +"\""+"e2"+"\"");
						writer.flush();
//						writer2_d3_graph.append("\n{source:"+(first_Noun_From_curr_line_VertexID-1)
//								  +", target:"+(first_Verb_From_curr_line_VertexID-1)+ "},");
//						writer2_d3_graph.flush();
						
						map_for_writer2_d3_graph.put( "\n{source:"+(first_Noun_From_curr_line_VertexID-1)
								  					  +", target:"+(first_Verb_From_curr_line_VertexID-1)+ "},"
														, "dummy");
						
					}

					mapAlreadyWroteEdgePair.put( "d "+first_Noun_From_curr_line_VertexID+" "
							  			   +first_Verb_From_curr_line_VertexID, "dummy");
					mapAlreadyWroteEdgePair.put( "d "+first_Verb_From_curr_line_VertexID +" "
				  			   					+first_Noun_From_curr_line_VertexID, "dummy");
										
					System.out.println("debug:LastIteration_first_Noun_From_curr_line_VertexID:"+LastIteration_first_Noun_From_curr_line_VertexID
							+"\nfirst_Noun_From_curr_line_VertexID:"+first_Noun_From_curr_line_VertexID);
					
					if(LastIteration_first_Noun_From_curr_line_VertexID>0
							&& first_Noun_From_curr_line_VertexID!=LastIteration_first_Noun_From_curr_line_VertexID
							&& !mapAlreadyWroteEdgePair.containsKey("d "
													+LastIteration_first_Noun_From_curr_line_VertexID+" "
														+first_Noun_From_curr_line_VertexID)
							
							){
								writer.append("\nd "+LastIteration_first_Noun_From_curr_line_VertexID+" "
								 			  +first_Noun_From_curr_line_VertexID
								 			 +" "+"\""+"e1"+"\"");
								writer.flush();
								
//								writer2_d3_graph.append("\n{source:"+(LastIteration_first_Noun_From_curr_line_VertexID-1)
//														  +", target:"+(first_Noun_From_curr_line_VertexID-1)+ "},");
//								writer2_d3_graph.flush();
								//{source: 0, target: 1},
								 
								map_for_writer2_d3_graph.put("\n{source:"+(LastIteration_first_Noun_From_curr_line_VertexID-1)
										  					 +", target:"+(first_Noun_From_curr_line_VertexID-1)+ "},",
															 "dummy");	
								
								mapAlreadyWroteEdgePair.put( "d "+LastIteration_first_Noun_From_curr_line_VertexID+" "
								 			  			+first_Noun_From_curr_line_VertexID, "dummy");
								
								mapAlreadyWroteEdgePair.put( "d "+first_Noun_From_curr_line_VertexID +" "
				 			  								+LastIteration_first_Noun_From_curr_line_VertexID, "dummy");
								
								
							}
				  
				  //reset
				  first_Noun_From_curr_line="";
				  mapCurrVertexID_Word=new TreeMap<Integer, String>();
				  first_Verb_From_curr_line="";
				  
				  if(LastIteration_first_Noun_From_curr_line_VertexID<0
						 &&first_Noun_From_curr_line_VertexID>0){
					  LastIteration_first_Noun_From_curr_line_VertexID=first_Noun_From_curr_line_VertexID;
				  }
				  System.out.println("\n-------------end----------------");
					System.out.println("debug:LastIteration_first_Noun_From_curr_line_VertexID:"+LastIteration_first_Noun_From_curr_line_VertexID
							+"\nfirst_Noun_From_curr_line_VertexID:"+first_Noun_From_curr_line_VertexID
							+"\n"+mapAlreadyWroteEdgePair.containsKey("d "
									+LastIteration_first_Noun_From_curr_line_VertexID+" "
									+first_Noun_From_curr_line_VertexID));
					
					System.out.println("last_vertex_id_in_this_set:"+last_vertex_id_in_this_set+
									"first_vertex_id_in_this_set"+first_vertex_id_in_this_set);
					int cnt2=0;
					//iterate between  first_vertex_id_in_this_set && last_vertex_id_in_this_set
					while(first_vertex_id_in_this_set<=last_vertex_id_in_this_set){
						String t="d "+first_Verb_From_curr_line_VertexID+" " 
									 +first_vertex_id_in_this_set;
						String t_for_d3="{source:"+(first_Verb_From_curr_line_VertexID-1)+", "+ 
										 "target:"+(first_vertex_id_in_this_set-1)+"},";
						String rev_t="d "+first_Verb_From_curr_line_VertexID+" " 
								 		 +first_vertex_id_in_this_set;
						//same number pair
						if(first_Verb_From_curr_line_VertexID==first_vertex_id_in_this_set){
							first_vertex_id_in_this_set++;
							continue;
						}
						
						//
						if(!mapAlreadyWroteEdgePair.containsKey(t)
							&& !mapAlreadyWroteEdgePair.containsKey(rev_t)){
							
							writer.append("\n"+t+" \""+"e3"+"\"");
							writer.flush();
							
//							writer2_d3_graph.append("\n"+t_for_d3);
//							writer2_d3_graph.flush();
							map_for_writer2_d3_graph.put("\n"+t_for_d3, 
														"");
							
							
							mapAlreadyWroteEdgePair.put(t, "dummy");
							mapAlreadyWroteEdgePair.put(rev_t, "dummy");
						}
						
						first_vertex_id_in_this_set++;
					}
					
					
//					if(LastIteration_first_Noun_From_curr_line_VertexID>0
//							&& first_Noun_From_curr_line_VertexID>0
//							&& first_Noun_From_curr_line_VertexID!=LastIteration_first_Noun_From_curr_line_VertexID
//							&& !mapAlreadyWroteEdgePair.containsKey("d "
//													+LastIteration_first_Noun_From_curr_line_VertexID+" "
//														+first_Noun_From_curr_line_VertexID)
//							
//							){
//							System.out.println("**last curr edge writing");
//								writer.append("\nd "+LastIteration_first_Noun_From_curr_line_VertexID+" "
//								 			  +first_Noun_From_curr_line_VertexID);
//								mapAlreadyWroteEdgePair.put( "d "+LastIteration_first_Noun_From_curr_line_VertexID+" "
//								 			  			+first_Noun_From_curr_line_VertexID, "dummy");
//								writer.flush();
//							}
				  
				  first_Noun_From_curr_line_VertexID=-1;
				  first_Verb_From_curr_line_VertexID=-1;
				  
				  System.out.println("\n-------------eol----------------");
			  }
		
			  //write the output to output file
			  
			  cnt_TokenID_position_in_line++;
		  }
		  
		  //only nodes
		  ReadFile_readEachLine_For_a_pattern_IFmatched_WriteToAnotherFile
		  .readFile_readEachLine_For_a_pattern_IF_matched_WriteToAnotherFile(
				  												  outputFile_d3_visualization
				  												, outputFile_d3_nodes_only
				  												, "name"
				  												, false //is_Append_out_File
				  												);
		  
		  //only edges
		  ReadFile_readEachLine_For_a_pattern_IFmatched_WriteToAnotherFile
		  .readFile_readEachLine_For_a_pattern_IF_matched_WriteToAnotherFile(
				  												  outputFile_d3_visualization
				  												, outputFile_d3_edges_only
				  												, "source"
				  												, false //is_Append_out_File
				  												);
		  
		  
		  
	  }
	  catch(Exception e){
		  e.printStackTrace();
	  }
	  return "";
  }

  public static void main (String args[]){
	  try{

		  String input_ExtractedText_OR_input2="Making classical music easily accessible to a large smartphone user base, Saragama India Ltd., an RPG group enterprise, has introduced an exclusive app that opens up a treasure trove of more than 8000 recordings of about 400 stalwarts of Hindustani classical, Carnatic, and fusion music for music lovers. A galaxy of musicians including Pandit Hariprasad Chaurasia, Pandit Jasraj, Dr Balamuralikrishna, Ustad Amjad Ali Khan, Pandit Ajay Pohankar and poet-lyricist-film-maker Gulzar were present at the introduction of the app “Saregama Classical” at a suburban Mumbai hotel on Tuesday. ";
		  input_ExtractedText_OR_input2="Making_VBG classical_JJ music_NN easily_RB accessible_JJ to_TO a_DT large_JJ smartphone_JJ user_NN base,_NN Saragama_NNP India_NNP Ltd.,_NNP an_DT RPG_NNP group_NN enterprise,_NN has_VBZ introduced_VBN an_DT exclusive_JJ app_NN that_WDT opens_VBZ up_RP a_DT treasure_NN trove_NN of_IN more_JJR than_IN 8000_CD recordings_NNS of_IN about_IN 400_CD stalwarts_NNS of_IN Hindustani_NNP classical,_, Carnatic,_NNP and_CC fusion_NN music_NN for_IN music_NN lovers._VBD A_DT galaxy_NN of_IN musicians_NNS including_VBG Pandit_NN Hariprasad_NNP Chaurasia,_NNP Pandit_NNP Jasraj,_NNP Dr_NNP Balamuralikrishna,_NNP Ustad_NNP Amjad_NNP Ali_NNP Khan,_NNP Pandit_NNP Ajay_NNP Pohankar_NNP and_CC poet-lyricist-film-maker_NN Gulzar_NNP were_VBD present_JJ at_IN the_DT introduction_NN of_IN the_DT app_NN “Saregama_NNP Classical”_NNP at_IN a_DT suburban_JJ Mumbai_NNP hotel_NN on_IN Tuesday._NNP Lenin_NN is_TO Nice._JJ";
		  input_ExtractedText_OR_input2="At_IN least_JJS 37_CD workers_NNS died_VBD and_CC more_JJR than_IN 70_CD were_VBD injured_VBN when_WRB a_DT series_NN of_IN powerful_JJ explosions_NNS ripped_VBD through_IN a_DT large_JJ unit_NN in_IN Sivakasi,_NNP the_DT capital_NN of_IN the_DT fireworks_NNS industry_NN on_IN Wednesday._NNP Officials_NNP quoted_VBD doctors_NNS here_RB as_IN saying_VBG that_IN 35_CD bodies_NNS had_VBD been_VBN recovered_VBN from_IN the_DT site_NN of_IN the_DT blasts_NNS at_IN Om_NNP Shakthi_NNP Fireworks_NNPS Ltd._NNP Four_CD workers_NNS died_VBD in_IN hospitals_NNS in_IN Madurai,_NNP 85km_IN away._RB Many_JJ died_VBN in_IN the_DT blaze_NN that_WDT spread_VBD after_IN the_DT blasts._NN Several_JJ of_IN the_DT survivors_NNS were_VBD in_IN a_DT critical_JJ condition.The_NN explosions_NNS and_CC the_DT fire_NN were_VBD so_RB devastating_JJ that_IN they_PRP reduced_VBD 40_CD of_IN 48_CD sheds_NNS in_IN the_DT unit_NN to_TO rubble._NNP Eyewitnesses_NNS told_VBD police_NNS that_IN many_JJ victims_NNS perished_VBD w_WRB they_PRP rushed_VBD to_TO the_DT aid_NN of_IN workers_NNS in_IN the_DT block,_NN where_WRB the_DT first_JJ blasts_NNS went_VBD off_RP at_IN 12.15pm._CD";
		  input_ExtractedText_OR_input2="The_DT remains_VBZ of_IN four_CD victims_NNS have_VBP been_VBN found_VBN in_IN a_DT wooded_JJ area_NN behind_IN a_DT strip_NN mall_NN here_RB in_IN central_JJ Connecticut_NNP that_IN the_DT authorities_NNS described_VBD on_IN Monday_NNP as_IN the_DT dumping_VBG ground_NN for_IN a_DT person_NN suspected_VBN as_IN a_DT serial_JJ killer_NN in_IN seven_CD deaths._NN It_PRP is_VBZ the_DT same_JJ area_NN where_WRB the_DT partial_JJ skeletons_NNS of_IN three_CD women_NNS were_VBD found_VBN in_IN 2007,_CD the_DT police_NN said.“This_. is_VBZ certainly_RB the_DT burial_NN site,”_NN Chief_NNP James_NNP Wardwell_NNP of_IN the_DT New_NNP Britain_NNP Police_NNP Department_NNP said._VBD";
		  
		  String outputFile="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/gbad_output.txt";
		  
		  String outputFile_d3_visualization=
				  	"/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/output_2_d3_visualization.txt";
		  String outputFile_d3_nodes_only="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/output_2_d3_visualization_nodes_only.txt";
		  String outputFile_d3_edges_only="/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/output/output_2_d3_visualization_edges_only.txt";
		  //
		  convert_NLP_tagged_string_array_to_GBAD_graph(input_ExtractedText_OR_input2,
				  										outputFile,
				  										false, //is_append
				  										true,
				  										outputFile_d3_visualization,
				  										false, // is_append
				  										outputFile_d3_nodes_only,
				  										outputFile_d3_edges_only
				  										);
		  
	  }
	  catch(Exception e){
		  
	  }
  
  }
}