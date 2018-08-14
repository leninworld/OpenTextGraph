package convert;

import cern.colt.Arrays;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import crawler.*;
import crawler.IsInteger;
import crawler.RemoveBackSlash;
import crawler.RemoveUnicodeChar;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.TreeMap;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

// convert for GBAD
public class Convert_For_GBAD {

    //remove brackets
    public static String remove_brackets(String edge_feature) {
        return edge_feature.replace("(", "").replace(")", "");
    }

    //convert_curr_nodepair_edge_to_config_mapping
    public static String convert_curr_nodepair_edge_to_config_mapping(String curr_nodepair_edge,
                                                                      String debug_File,
                                                                      boolean is_write_to_debugFile
    ) throws IOException {
        String out = "";
        FileWriter writer_debug = null;
        String s[] = curr_nodepair_edge.split("!#!#");
        try {

            if (s.length < 3) {
                return curr_nodepair_edge;
            }

            String curr_nodepair_edge_orig = curr_nodepair_edge;

            curr_nodepair_edge = curr_nodepair_edge.replace("__", "_");

            writer_debug = new FileWriter(new File(debug_File), true);


            String s0_config = "";
            String s1_config = "";
            String s2_config = "";

            int idx_s0 = s[0].indexOf("_");
            int idx_s1 = s[1].indexOf("_");
            int idx_s2 = s[2].indexOf("_");

            //hard-code
            if (s[0].indexOf("[") >= 0) {
                s0_config = s[0];
            } else if (idx_s0 >= 0) {
                if (s[0].length() >= idx_s0 + 3)
                    s0_config = s[0].substring(idx_s0, idx_s0 + 3);
                else if (s[0].length() >= idx_s0 + 2)
                    s0_config = s[0].substring(idx_s0, idx_s0 + 2);
                else if (s[0].length() >= idx_s0 + 1)
                    s0_config = s[0].substring(idx_s0, idx_s0 + 1);
            } else {
                s0_config = s[0];
            }

            //DESTINAT hard-code
            if (s[1].indexOf("[") >= 0) {
                s1_config = s[1];
            } else if (idx_s1 >= 0) {
                if (s[1].length() >= idx_s1 + 3)
                    s1_config = s[1].substring(idx_s1, idx_s1 + 3);
                else if (s[1].length() >= idx_s1 + 2)
                    s1_config = s[1].substring(idx_s1, idx_s1 + 2);
                else if (s[1].length() >= idx_s1 + 1)
                    s1_config = s[1].substring(idx_s1, idx_s1 + 1);
            } else {
                s1_config = s[1];
            }
            // edge
            if (s[2].indexOf("[") >= 0) {
                s2_config = s[2];
            } else if (idx_s2 >= 0) {
                if (s[2].length() >= idx_s2 + 3)
                    s2_config = s[2].substring(idx_s2, idx_s2 + 3);
                else if (s[2].length() >= idx_s2 + 2)
                    s2_config = s[2].substring(idx_s2, idx_s2 + 2);
                else if (s[2].length() >= idx_s2 + 1)
                    s2_config = s[2].substring(idx_s2, idx_s2 + 1);

            } else {
                s2_config = s[2];
            }


            //
            if (is_write_to_debugFile) {
                writer_debug.append("\n ");
                writer_debug.flush();

            }
            out = s[0] + "!#!#" + s[1] + "!#!#" + s[2] + "!#!#remark:" + "!#!#" + s0_config + " " + s1_config + " " + s2_config;
            out = out.replace("__", "_");

        } catch (Exception e) {
            try {
                writer_debug.append("\n ERROR: t_new: " + e.getMessage() + "-- " + e.getStackTrace().toString() + "--" + e.toString() + "<->" + s.length + "<->" + curr_nodepair_edge);
                writer_debug.flush();
                //e.printStackTrace();
            } catch (Exception e2) {
                System.out.println("err:" + e2.getMessage());
            }
            //e.printStackTrace();
            // TODO: handle exception
        } finally {
            if (writer_debug != null)
                writer_debug.close();
        }
        return out;
    }

    // nodePair_edge_do_stemming
    public static String nodePair_edge_do_stemming(String currNodePair_edge,
                                                   Stemmer stemmer,
                                                   String debug_File,
                                                   boolean is_Write_to_debugFile
    ) {
        String token = "", tag = "";
        String nodePair_Edge_new = "";
        FileWriter writer_debug = null;
        try {
            writer_debug = new FileWriter(new File(debug_File), true);

            String[] s = currNodePair_edge.split("!#!#");

            String concLine_s0 = "";
            //source
            if (s[0].indexOf("_") >= 0) {
                //only one word in this token
                if (s[0].indexOf("_") >= 0 && s[0].indexOf(" ") == -1) {
                    token = s[0].substring(0, s[0].indexOf("_"));
                    tag = s[0].substring(s[0].indexOf("_"), s[0].length());
                    if (concLine_s0.length() == 0)
                        concLine_s0 = stemmer.stem(token) + tag;
                    else
                        concLine_s0 = concLine_s0 + " " + stemmer.stem(token) + tag;

                }
                // more than one word in this token
                else if (s[0].indexOf("_") >= 0 && s[0].indexOf(" ") >= 0) {
                    String[] s2 = s[0].split(" ");
                    int c = 0;
                    while (c < s2.length) {
                        token = s2[c].substring(0, s2[c].indexOf("_"));
                        tag = s2[c].substring(s2[c].indexOf("_"), s2[c].length());

                        if (concLine_s0.length() == 0)
                            concLine_s0 = stemmer.stem(token) + tag;
                        else
                            concLine_s0 = concLine_s0 + " " + stemmer.stem(token) + tag;
                        c++;
                    }

                }
                // tag with _ present. but token has only one word (not multiple word in a token)
                else {

                    token = stemmer.stem(s[0]);
                    tag = "";
                    if (concLine_s0.length() == 0)
                        concLine_s0 = stemmer.stem(token) + tag;
                    else
                        concLine_s0 = concLine_s0 + " " + stemmer.stem(token) + tag;

                }

            }
            //no tagging with  "_" present
            else {
                //
                if (s[0].indexOf(" ") >= 0) {

                    String[] s2 = s[0].split(" ");
                    int c = 0;
                    while (c < s2.length) {
                        token = s2[c];

                        if (concLine_s0.length() == 0)
                            concLine_s0 = stemmer.stem(token);
                        else
                            concLine_s0 = concLine_s0 + " " + stemmer.stem(token);
                        c++;
                    }

                } else {
                    if (concLine_s0.length() == 0)
                        concLine_s0 = s[0];
                    else
                        concLine_s0 = concLine_s0 + " " + s[0];
                }
            }

            String concLine_s1 = "";
            //destination
            if (s[1].indexOf("_") >= 0) {
                //only one word in this token
                if (s[1].indexOf("_") >= 0 && s[1].indexOf(" ") == -1) {
                    token = s[1].substring(0, s[1].indexOf("_"));
                    tag = s[1].substring(s[1].indexOf("_"), s[1].length());
                    if (concLine_s1.length() == 0)
                        concLine_s1 = stemmer.stem(token) + tag;
                    else
                        concLine_s1 = concLine_s1 + " " + stemmer.stem(token) + tag;

                }
                // more than one word in this token
                else if (s[1].indexOf("_") >= 0 && s[1].indexOf(" ") >= 0) {
                    String[] s2 = s[1].split(" ");
                    int c = 0;
                    while (c < s2.length) {
                        token = s2[c].substring(0, s2[c].indexOf("_"));
                        tag = s2[c].substring(s2[c].indexOf("_"), s2[c].length());

                        if (concLine_s1.length() == 0)
                            concLine_s1 = stemmer.stem(token) + tag;
                        else
                            concLine_s1 = concLine_s1 + " " + stemmer.stem(token) + tag;
                        c++;
                    }

                }
                // tag with _ present. but token has only one word (not multiple word in a token)
                else {

                    token = stemmer.stem(s[1]);
                    tag = "";
                    if (concLine_s1.length() == 0)
                        concLine_s1 = stemmer.stem(token) + tag;
                    else
                        concLine_s1 = concLine_s1 + " " + stemmer.stem(token) + tag;

                }

            }
            //no tagging with  "_" present
            else {
                //
                if (s[1].indexOf(" ") >= 0) {

                    String[] s2 = s[1].split(" ");
                    int c = 0;
                    while (c < s2.length) {
                        token = s2[c];

                        if (concLine_s1.length() == 0)
                            concLine_s1 = stemmer.stem(token);
                        else
                            concLine_s1 = concLine_s1 + " " + stemmer.stem(token);
                        c++;
                    }

                } else {
                    if (concLine_s1.length() == 0)
                        concLine_s1 = s[1];
                    else
                        concLine_s1 = concLine_s1 + " " + s[1];
                }
            }


            String concLine_s2 = "";
            //edge
            if (s[2].indexOf("_") >= 0) {
                //only one word in this token
                if (s[2].indexOf("_") >= 0 && s[2].indexOf(" ") == -1) {
                    token = s[2].substring(0, s[2].indexOf("_"));
                    tag = s[2].substring(s[2].indexOf("_"), s[2].length());
                    if (concLine_s2.length() == 0)
                        concLine_s2 = stemmer.stem(token) + tag;
                    else
                        concLine_s2 = concLine_s2 + " " + stemmer.stem(token) + tag;

                }
                // more than one word in this token
                else if (s[2].indexOf("_") >= 0 && s[2].indexOf(" ") >= 0) {
                    String[] s2 = s[2].split(" ");
                    int c = 0;
                    while (c < s2.length) {
                        token = s2[c].substring(0, s2[c].indexOf("_"));
                        tag = s2[c].substring(s2[c].indexOf("_"), s2[c].length());

                        if (concLine_s2.length() == 0)
                            concLine_s2 = stemmer.stem(token) + tag;
                        else
                            concLine_s2 = concLine_s2 + " " + stemmer.stem(token) + tag;
                        c++;
                    }

                }
                // tag with _ present. but token has only one word (not multiple word in a token)
                else {

                    token = stemmer.stem(s[2]);
                    tag = "";
                    if (concLine_s2.length() == 0)
                        concLine_s2 = stemmer.stem(token) + tag;
                    else
                        concLine_s2 = concLine_s2 + " " + stemmer.stem(token) + tag;

                }

            }
            //no tagging with  "_" preseent
            else {
                //
                if (s[2].indexOf(" ") >= 0) {

                    String[] s2 = s[2].split(" ");
                    int c = 0;
                    while (c < s2.length) {
                        token = s2[c];

                        if (concLine_s2.length() == 0)
                            concLine_s2 = stemmer.stem(token);
                        else
                            concLine_s2 = concLine_s2 + " " + stemmer.stem(token);
                        c++;
                    }

                } else {
                    if (concLine_s2.length() == 0)
                        concLine_s2 = s[2];
                    else
                        concLine_s2 = concLine_s2 + " " + s[2];
                }
            }

            nodePair_Edge_new = concLine_s0 + "!#!#" + concLine_s1 + "!#!#" + concLine_s2;

            boolean is_mapping_exist = false;
            if (s.length >= 4) {
                nodePair_Edge_new = nodePair_Edge_new + "!#!#" + s[3];
            }
            if (s.length >= 5) {
                nodePair_Edge_new = nodePair_Edge_new + "!#!#" + s[4];
                is_mapping_exist = true;
            }
            if (s.length >= 6) {
                nodePair_Edge_new = nodePair_Edge_new + "!#!#" + s[5];
            }

            if (is_mapping_exist == false) {

                if (nodePair_Edge_new.split("!#!#").length >= 3) {

                    nodePair_Edge_new = convert_curr_nodepair_edge_to_config_mapping(nodePair_Edge_new, debug_File,
                            true //is_write_to_debugFile
                    );
                } else {
                    writer_debug.append("\n temp.get(seq_id)/currNodePair_edge: LESSER TOKEN ->" + nodePair_Edge_new);
                    writer_debug.flush();
                }
            }


            // debug
            if (is_Write_to_debugFile) {
                if (s.length <= 3) {

                    writer_debug.append("\n temp.get(seq_id)/currNodePair_edge: " + currNodePair_edge + " token:" + token + " tag:" + tag
                            + " concLine_s0:" + concLine_s0
                            + " concLine_s1:" + concLine_s1
                            + " concLine_s2:" + concLine_s2
                            + " stemmer.stem(token):" + stemmer.stem(token)
                            + " s.len:" + s.length
                            + " nodePair_Edge_new:" + nodePair_Edge_new
                    );
                    writer_debug.flush();
                }
            }


        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (writer_debug != null)
                try {
                    writer_debug.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return nodePair_Edge_new;
    }


    //get_semantics_for_a_word  (inToken can be NLP tagged or untagged word (TOKEN))
    public static TreeMap<Integer, String> get_semantics_for_a_word_FOR_SynsetType(
            String inToken,
            String[] in_arr_input_orig_mapping,
            int index_for_in_arr_input_orig_mapping,
            boolean isSOPprint,
            FileWriter writer_outFile_repository_to_store_token_semantic_Pair,
            boolean is_write_to_repository_if_token_semantic_Pair,
            String inSynsetType
    ) {
        TreeMap<Integer, String> mapOut = new TreeMap<Integer, String>();

        try {
            Stemmer stemmer = new Stemmer();
            String token = "", tag = "";
            // token
            if (inToken.indexOf("_") >= 0) {
                token = inToken.substring(0, inToken.indexOf("_"));
            } else {
                token = inToken;
            }
            // tag
            if (inToken.indexOf("_") >= 0) {
                tag = inToken.substring(inToken.indexOf("_"), inToken.length());
            } else {
                tag = "";
            }

            String[] s2_new = null;
            String token_semantics = "";
            int c22 = 0;
            TreeMap<String, String> map_already_passed_token_mapOut = new TreeMap<String, String>();
            // .semantic()
            if (in_arr_input_orig_mapping[index_for_in_arr_input_orig_mapping].indexOf(".semantic()") >= 0) {
                // expand semantics
                token_semantics = Wordnet.wordnet_extension(token,
                        0, //print
                        true, //is_remove_stop_words
                        "VERB"  // inSynsetType
                );


                String semantic[] = token_semantics.split(" ");
                s2_new = new String[semantic.length];
                // semantic
                while (c22 < semantic.length) {
                    if (semantic[c22] != null)
                        if (semantic[c22].length() > 0)
                            s2_new[c22] = semantic[c22];
                    c22++;
                }

                int c22_1 = 0;
                // iterate just abv found semantics
                while (c22_1 < s2_new.length) {
                    if (s2_new[c22_1] != null) {
                        if (s2_new[c22_1].length() > 0) {

                            if (isSOPprint)
                                System.out.println(">>>>>>.semantic s2_new[c22_1]=:" + s2_new[c22_1]);

                            if (!map_already_passed_token_mapOut.containsKey(s2_new[c22_1]))
                                mapOut.put(c22_1, stemmer.stem(s2_new[c22_1]));

                            map_already_passed_token_mapOut.put(s2_new[c22_1], "");
                        }
                    }
                    c22_1++;
                }

                //put the original token
                mapOut.put(c22_1, stemmer.stem(token));

            } //END if(in_arr_input_orig_mapping[index_for_in_arr_input_orig_mapping].indexOf(".semantic()")>=0){

            //.stemming()
            if (in_arr_input_orig_mapping[index_for_in_arr_input_orig_mapping].indexOf(".stemming()") >= 0) {
                int c23 = 0;
                // ITERATE expanded semantics, and do stemmping
                while (c23 < s2_new.length) {
                    if (s2_new[c23] != null) {
                        if (s2_new[c23].length() > 0) {

                            if (isSOPprint)
                                System.out.println(">>>>>>. stem(stem s2_new[c23]:" + stemmer.stem(s2_new[c23]));

                            if (!map_already_passed_token_mapOut.containsKey(s2_new[c23]))
                                mapOut.put(c23, stemmer.stem(s2_new[c23]));

                            map_already_passed_token_mapOut.put(s2_new[c23], "");
                        }
                    }
                    c23++;
                }
                //put the original token
                mapOut.put(c23, stemmer.stem(token));
            } //END if(in_arr_input_orig_mapping[index_for_in_arr_input_orig_mapping].indexOf(".stemming()")>=0){


            // repository writing
            if (is_write_to_repository_if_token_semantic_Pair) {
                for (int seq : mapOut.keySet()) {
                    writer_outFile_repository_to_store_token_semantic_Pair.append(inToken + "!!!" + mapOut.get(seq) + "\n");
                    writer_outFile_repository_to_store_token_semantic_Pair.flush();
                }
            }//

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapOut;
    }

    // remove_all_NLP_tags
    public static String remove_all_NLP_tags(String inString

    ) {
        String outString = "";
        try {

            //System.out.println( "inString:"+ inString);

            outString = inString.replace("__CC", "")
                    .replace("__CD", "")
                    .replace("__DT", "")
                    .replace("__EX", "")
                    .replace("__FW", "")
                    .replace("__IN", "")
                    .replace("__JJR", "")
                    .replace("__JJS", "")
                    .replace("__JJ", "")
                    .replace("__LS", "")
                    .replace("__MD", "")
                    .replace("__NNPS", "")
                    .replace("__NNS", "")
                    .replace("__NNP", "")
                    .replace("__NN", "")
                    .replace("__PRP$", "")
                    .replace("__PDT", "")
                    .replace("__POS", "")
                    .replace("__PRP", "")
                    .replace("__RBR", "")
                    .replace("__RBS", "")
                    .replace("__RP", "")
                    .replace("__RB", "")
                    .replace("__SYM", "")
                    .replace("__TO", "")
                    .replace("__UH", "")
                    .replace("__VBZ", "")
                    .replace("__VBP", "")
                    .replace("__VBD", "")
                    .replace("__VBN", "")
                    .replace("__VBG", "")
                    .replace("__VB", "")
                    .replace("__WDT", "")
                    .replace("__WP$", "")
                    .replace("__WRB", "")
                    .replace("__WP", "")
                    .replace("__.", "")
                    .replace("__,", "")
                    .replace("__:", "")
                    .replace("__(", "")
                    .replace("__)", "");

            // lowercase - cmd+shift+y
            outString = outString.replace("__cc", "")
                    .replace("__cd", "")
                    .replace("__dt", "")
                    .replace("__ex", "")
                    .replace("__fw", "")
                    .replace("__in", "")
                    .replace("__jjs", "")
                    .replace("__jjr", "")
                    .replace("__jj", "")
                    .replace("__ls", "")
                    .replace("__md", "")
                    .replace("__nnps", "")
                    .replace("__nns", "")
                    .replace("__nnp", "")
                    .replace("__nn", "")
                    .replace("__prp$", "")
                    .replace("__pdt", "")
                    .replace("__pos", "")
                    .replace("__prp", "")
                    .replace("__rbr", "")
                    .replace("__rbs", "")
                    .replace("__rp", "")
                    .replace("__rb", "")
                    .replace("__sym", "")
                    .replace("__to", "")
                    .replace("__uh", "")
                    .replace("__vbz", "")
                    .replace("__vbp", "")
                    .replace("__vbd", "")
                    .replace("__vbn", "")
                    .replace("__vbg", "")
                    .replace("__vb", "")
                    .replace("__wdt", "")
                    .replace("__wp$", "")
                    .replace("__wrb", "")
                    .replace("__wp", "")
                    .replace("__.", "")
                    .replace("__,", "")
                    .replace("__:", "")
                    .replace("__(", "")
                    .replace("__)", "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return outString;
    }

    // create GBAD graph
    private static TreeMap<Integer, String> create_gbad_graph_preprocess_and_create(
            String baseFolder,
            String outFile_for_gbad_graph,
            TreeMap<Integer, String> map_input_create_graph_cnt_NodePairEdgeString,
            TreeMap<String, Integer> map_distinct_SourceDestEdge_as_KEY_PARAM,
            TreeMap<Integer, String> map_distinct_SourceDestEdge_ORIG_as_KEY_REVERSE_PARAM,
            String debugFile,
            boolean isSOPprint,
            boolean is_debug_File_Write_more,
            boolean is_remove_NLPtag_in_graph_output,
            boolean is_remove_CSV_delimiter_in_final_Element_in_graph_created,
            boolean is_create_each_document_as_an_XP_in_graph,
            boolean isskip_d3js_creation,
            int xp_number,
            TreeMap<String, TreeMap<Integer, String>> map_token_semantic_equiv,
            FileWriter writer_outFile_repository_to_store_token_semantic_Pair,
            boolean is_split_on_blank_space_among_tokens, // split on noun , verb
            boolean is_write_to_repository_if_token_semantic_Pair
    ) {
        String source = "";
        String dest = "";
        String edge = "";
        String SourceDestEdge_from_input_mapping = "";
        String SourceDestEdge_ORIGINAL_from_input_mapping = "";
        TreeMap<Integer, String> map_input_gbad_graph_creation = new TreeMap<Integer, String>();
        TreeMap<Integer, TreeMap<Integer, String>> map_input_gbad_graph_creation_final = new TreeMap<Integer, TreeMap<Integer, String>>();
        TreeMap<Integer, TreeMap<Integer, String>> map_docidSEQ_mapnodePairNedgeForCreateGraph = new TreeMap<Integer, TreeMap<Integer, String>>();
        int cnt_map_input_gbad_graph_creation = 0;
        int stat_hit_on_repo_semantic = 0;
        Stemmer stemmer = new Stemmer();
        String nodePair_Edge_new = "";
        TreeMap<String, Integer> map_distinct_SourceDestEdge_as_KEY = new TreeMap<String, Integer>();
        TreeMap<Integer, String> map_distinct_SourceDestEdge_ORIG_as_KEY_REVERSE = new TreeMap<Integer, String>();
        FileWriter writerdebug = null;
        // TODO Auto-generated method stub
        try {
            //
            for (String i : map_distinct_SourceDestEdge_as_KEY_PARAM.keySet()) {
                String t = remove_brackets(i);
                map_distinct_SourceDestEdge_as_KEY.put(t, map_distinct_SourceDestEdge_as_KEY_PARAM.get(i));
                map_distinct_SourceDestEdge_ORIG_as_KEY_REVERSE.put(map_distinct_SourceDestEdge_as_KEY_PARAM.get(i), t);
            }

            writerdebug = new FileWriter(new File(debugFile), true); //append
            writerdebug.append("---------------------------------------------\n");
            // DEBUG - iterate nodepair_edge
            if (isSOPprint) {
                for (int id_seq : map_input_create_graph_cnt_NodePairEdgeString.keySet()) {
                    System.out.println("debug: " + id_seq + "<->" + map_input_create_graph_cnt_NodePairEdgeString.get(id_seq));
                }
            }

            writerdebug.append("\n----start-----map_input_create_graph_cnt_NodePairEdgeString\n");
            writerdebug.flush();

            for (int id_seq : map_input_create_graph_cnt_NodePairEdgeString.keySet()) {
                writerdebug.append("\n " + id_seq + "<->" + map_input_create_graph_cnt_NodePairEdgeString.get(id_seq));
            }
            if (is_debug_File_Write_more) {
                writerdebug.append("\n-----end-----map_input_create_graph_cnt_NodePairEdgeString\n");
                writerdebug.flush();
            }

            TreeMap<Integer, String> map_atom_as_VALUE = new TreeMap<Integer, String>();

            if (is_debug_File_Write_more) {
                writerdebug.append("\n------------final input to gbad input graph creation----START-----\n");
                writerdebug.flush();
            }
            map_input_gbad_graph_creation_final = new TreeMap<Integer, TreeMap<Integer, String>>();
            int size_ = map_input_create_graph_cnt_NodePairEdgeString.size();
            int counter = 0;
            // create   Graph --- BELOW processing each document
            for (int doc_id_seq : map_input_create_graph_cnt_NodePairEdgeString.keySet()) {
                counter++;
                System.out.println("CREATE graph doc_id_seq->" + doc_id_seq + " counter:" + counter + " out of total=" + size_);

                if (is_debug_File_Write_more) {
                    writerdebug.append("\n-----------Iterate map_input_create_graph_cnt_NodePairEdgeString:---"
                            + " doc_id_seq:" + doc_id_seq
                            + " size:" + map_input_create_graph_cnt_NodePairEdgeString.size() + "\n");
                    writerdebug.flush();
                }

                //reset
                map_input_gbad_graph_creation = new TreeMap<Integer, String>();


                String curr_nodepair_edge = map_input_create_graph_cnt_NodePairEdgeString.get(doc_id_seq);

                if (curr_nodepair_edge.length() < 1) {
                    System.out.println("curr_nodepair_edge is empty..");
                    continue;
                }
                String[] s = curr_nodepair_edge.split("!#!#");
                //System.out.println("no-pair,edg->"+curr_nodepair_edge);
                source = s[0];
                dest = s[1];
                edge = s[2]; //source. dest . edge
                if (isSOPprint)
                    System.out.println("doc_id_seq:" + doc_id_seq + " curr:" + curr_nodepair_edge);
                //
                //System.out.println("SourceDestEdge_from_input_mapping:"+SourceDestEdge_from_input_mapping+" curr_nodepair_edge:"+curr_nodepair_edge);
                //
                try {
                    SourceDestEdge_from_input_mapping = s[4];
                } catch (Exception e) {
                    if (SourceDestEdge_from_input_mapping == null) {
                        writerdebug.append("\n NULL .. is that ok? " + SourceDestEdge_from_input_mapping);
                        writerdebug.flush();
                        System.out.println("NULL .. is that ok? " + SourceDestEdge_from_input_mapping);
                        continue;

                    }
                }

                if (isSOPprint && map_distinct_SourceDestEdge_as_KEY != null)
                    System.out.println("NOT NULL## map_distinct_SourceDestEdge_as_KEY:" + map_distinct_SourceDestEdge_as_KEY);
                if (map_distinct_SourceDestEdge_as_KEY == null)
                    System.out.println("NULL** map_distinct_SourceDestEdge_as_KEY:" + map_distinct_SourceDestEdge_as_KEY);

                int id_matched_nodepairEdge = -1;
                if (isSOPprint && !map_distinct_SourceDestEdge_as_KEY.containsKey(SourceDestEdge_from_input_mapping)) {
                    System.out.println("warning: not found?=" + SourceDestEdge_from_input_mapping + " inside=" + map_distinct_SourceDestEdge_as_KEY);
                    continue;
                }
                // NULL
                if (!map_distinct_SourceDestEdge_as_KEY.containsKey(SourceDestEdge_from_input_mapping)) {

                    writerdebug.append("\n ERROR CANT FIND SourceDestEdge_from_input_mapping->" + SourceDestEdge_from_input_mapping
                            + "<--> INSIDE-->" + map_distinct_SourceDestEdge_as_KEY
                            + "<--->curr_nodepair_edge:" + curr_nodepair_edge);


                    writerdebug.flush();
                    continue;
                }
                //
                id_matched_nodepairEdge = map_distinct_SourceDestEdge_as_KEY.get(SourceDestEdge_from_input_mapping);

                if (isSOPprint) {
                    System.out.println("not null->" + map_distinct_SourceDestEdge_as_KEY.get(SourceDestEdge_from_input_mapping)
                            + " " + SourceDestEdge_from_input_mapping + " " + map_distinct_SourceDestEdge_ORIG_as_KEY_REVERSE.containsKey(id_matched_nodepairEdge)
                            + " " + id_matched_nodepairEdge);
                }
                //ORIGINAL
                SourceDestEdge_ORIGINAL_from_input_mapping = map_distinct_SourceDestEdge_ORIG_as_KEY_REVERSE.get(id_matched_nodepairEdge);
                if (isSOPprint)
                    System.out.println("orig mapping:" + SourceDestEdge_ORIGINAL_from_input_mapping);

                // atom
                if (SourceDestEdge_ORIGINAL_from_input_mapping.indexOf(".atom()") >= 0) {

                    if (is_debug_File_Write_more) {
                        writerdebug.append("*****************************atom found " + SourceDestEdge_from_input_mapping + " original:" + SourceDestEdge_ORIGINAL_from_input_mapping + "\n");
                        writerdebug.flush();
                    }

                    //split to individual (atom)
                    map_atom_as_VALUE = split_to_atom(curr_nodepair_edge,
                            SourceDestEdge_from_input_mapping,
                            SourceDestEdge_ORIGINAL_from_input_mapping,
                            false, //isSOPprint
                            map_token_semantic_equiv,
                            writer_outFile_repository_to_store_token_semantic_Pair,
                            is_write_to_repository_if_token_semantic_Pair,
                            debugFile
                    );
                    //
                    stat_hit_on_repo_semantic = stat_hit_on_repo_semantic + Integer.valueOf(map_atom_as_VALUE.get(-9));

                    //
                    for (int id_seq2 : map_atom_as_VALUE.keySet()) {

                        if (id_seq2 > 0) {
                            if (isSOPprint)
                                System.out.println("received atom:" + map_atom_as_VALUE.get(id_seq2));

                            //out map
                            cnt_map_input_gbad_graph_creation++;

                            if (is_debug_File_Write_more) {
                                //debug
                                writerdebug.append("\n received atom cnt:" + cnt_map_input_gbad_graph_creation
                                        + " received atom:(redund print)" + map_atom_as_VALUE.get(id_seq2));
                                writerdebug.flush();
                            }

                            map_input_gbad_graph_creation.put(cnt_map_input_gbad_graph_creation, map_atom_as_VALUE.get(id_seq2));
                        }
                    }

                } else { //not atom (do only stemming as semantic() (group of related words in some order) will not make sense.
                    if (isSOPprint)
                        System.out.println("not atom:" + source + " " + dest + " " + edge);

                    // CSV STEMMING
                    // example: convert "delayed__vbn!!!arriving__vbg!!!departing__vbg" to "delai__vbn!!!arriv__vbg!!!depart__vbg"
                    if (SourceDestEdge_ORIGINAL_from_input_mapping.indexOf(".atom()") == -1
                            && SourceDestEdge_ORIGINAL_from_input_mapping.indexOf(".semantic()") == -1
                            && SourceDestEdge_ORIGINAL_from_input_mapping.indexOf(".stemming()") >= 0 // ONLY STEMMING
                            ) {
                        if (is_debug_File_Write_more) {
                            writerdebug.append("\n ****** ONLY STEMMING ***** \n");
                            writerdebug.flush();
                        }
                        // do stemming of CSV string of verb-like tagged words/untagged words
                        String curr_stemmed_csv = do_stemming_verbtypeCSVstring_of_applicable_curr_nodepair_edge(curr_nodepair_edge,
                                SourceDestEdge_from_input_mapping,
                                SourceDestEdge_ORIGINAL_from_input_mapping,
                                false //isSOPprint
                        );
                        if (is_debug_File_Write_more) {
                            writerdebug.append("not atom(stem):(redund print)" + curr_stemmed_csv + "\n");
                            writerdebug.flush();
                        }
                        //out map
                        cnt_map_input_gbad_graph_creation++;
                        map_input_gbad_graph_creation.put(cnt_map_input_gbad_graph_creation, curr_stemmed_csv);
                    } else { //   NO CSV STEMMING (+ NO SEMANTIC <-wont make sense)

                        if (is_debug_File_Write_more) {
                            writerdebug.append("\n ****** NO CSV STEMMING & NO SEMANTIC ***** \n");
                            writerdebug.flush();
                        }

                        String csv_out = source + "!#!#" + dest + "!#!#" + edge;

                        if (is_debug_File_Write_more) {
                            writerdebug.append("not atom(no stem):(redund print)" + csv_out + "\n");
                            writerdebug.flush();
                        }

                        //out map
                        cnt_map_input_gbad_graph_creation++;
                        map_input_gbad_graph_creation.put(cnt_map_input_gbad_graph_creation, csv_out);
                    }
                } ////not atom (do only stemming as semantic() (group of related words in some order) will not make sense.

                if (is_debug_File_Write_more) {
                    writerdebug.append("\n map_input_gbad_graph_creation.size():" + map_input_gbad_graph_creation.size()
                            + "\n map_input_gbad_graph_creation:" + map_input_gbad_graph_creation + " for doc_id_seq:" + doc_id_seq);
                    writerdebug.flush();
                }

                // map_input_gbad_graph_creation_final (may be statistic related negative value wont come here
                if (doc_id_seq < 0) {
                    System.out.println("negative value for other purpose");
                    continue;
                }

                map_input_gbad_graph_creation_final.put(doc_id_seq, map_input_gbad_graph_creation);

                //map_docidSEQ_mapnodePairNedgeForCreateGraph.put( doc_id_seq, map_input_gbad_graph_creation_final );

                if (is_debug_File_Write_more) {
                    if (map_input_gbad_graph_creation_final != null)
                        writerdebug.append("\n size as addsup :doc_id_seq:" + doc_id_seq + " size:" + map_input_gbad_graph_creation_final.get(doc_id_seq).size() + "\n"
                                + "curr map_input_gbad_graph_creation_final->" + map_input_gbad_graph_creation_final + "\n");
                    writerdebug.flush();
                }

            } //for(int doc_id_seq:map_input_create_graph_cnt_NodePairEdgeString.keySet()){

            if (is_debug_File_Write_more) {
                writerdebug.append("\n map_input_gbad_graph_creation_final.size:" + map_input_gbad_graph_creation_final.size() + "\n");
                writerdebug.append("\n map_input_gbad_graph_creation_final:" + map_input_gbad_graph_creation_final + "\n");
                writerdebug.flush();
            }

            //current document remove nlp tag from each node or edge in graph output
            if (is_remove_NLPtag_in_graph_output
                    && is_remove_CSV_delimiter_in_final_Element_in_graph_created) {

                writerdebug.append("\n 1. is_remove_NLPtag_in_graph_output:" + is_remove_NLPtag_in_graph_output
                        + "  is_remove_CSV_delimiter_in_final_Element_in_graph_created:" + is_remove_CSV_delimiter_in_final_Element_in_graph_created);
                writerdebug.flush();
                //
                for (int docID : map_input_gbad_graph_creation_final.keySet()) {
                    System.out.println("1. remove NLP TAG: docID->" + docID);

                    if (docID < 0) {
                        System.out.println("negative value for other purpose");
                        continue;
                    }

                    TreeMap<Integer, String> temp = map_input_gbad_graph_creation_final.get(docID);
                    TreeMap<Integer, String> temp_new = new TreeMap<Integer, String>();
                    writerdebug.append("\n 1. remove NLP TAG: docID->" + docID + " map:" + temp);
                    writerdebug.flush();

                    // removing CSV_delimiter_in_final_Element_in_graph_created
                    //always YES here (see else if)
                    if (is_remove_CSV_delimiter_in_final_Element_in_graph_created) {

                        for (int seq_id : temp.keySet()) {

                            //if(temp.get(seq_id).indexOf("#")==-1)
                            temp_new.put(seq_id,
                                    stemmer.stem(remove_all_NLP_tags(temp.get(seq_id).replace("!!!", " ")))
                            );
//							else{
//								String t=remove_all_NLP_tags(temp.get(seq_id).replace("!!!"," "));
//								temp_new.put(seq_id,
//											 t.substring(0 , t.indexOf(str))
//										 );
//							}

                            writerdebug.append("\n temp.get(seq_id) :" + temp.get(seq_id));
                            writerdebug.flush();

                        }
                    }

                    map_docidSEQ_mapnodePairNedgeForCreateGraph.put(docID, temp_new);

                    if (is_debug_File_Write_more) {
                        //System.out.println("remove tag:"+ tmp);
                        writerdebug.append("final:1:" + map_docidSEQ_mapnodePairNedgeForCreateGraph.size() + "\n");
                        writerdebug.flush();
                    }
                    //create new for modified
                }

            } //END if(is_remove_NLPtag_in_graph_output){
            else if (is_remove_NLPtag_in_graph_output == true && is_remove_CSV_delimiter_in_final_Element_in_graph_created == false) {

                writerdebug.append("\n 2. is_remove_NLPtag_in_graph_output:" + is_remove_NLPtag_in_graph_output
                        + "  is_remove_CSV_delimiter_in_final_Element_in_graph_created:" + is_remove_CSV_delimiter_in_final_Element_in_graph_created);
                writerdebug.flush();

                //
                for (int docID : map_input_gbad_graph_creation_final.keySet()) {
                    System.out.println("2. remove NLP TAG: docID->" + docID);

                    TreeMap<Integer, String> temp = map_input_gbad_graph_creation_final.get(docID);
                    TreeMap<Integer, String> temp_new = new TreeMap<Integer, String>();

                    // removing CSV_delimiter_in_final_Element_in_graph_created
                    //always YES here (see else if)
                    if (is_remove_CSV_delimiter_in_final_Element_in_graph_created) {

                        for (int seq_id : temp.keySet()) {
                            temp_new.put(seq_id, remove_all_NLP_tags(temp.get(seq_id)));
                        }
                    }

                    map_docidSEQ_mapnodePairNedgeForCreateGraph.put(docID, temp_new);

                    if (is_debug_File_Write_more) {
                        //System.out.println("remove tag:"+ tmp);
                        writerdebug.append("final:4:" + map_docidSEQ_mapnodePairNedgeForCreateGraph.size() + "\n");
                        writerdebug.flush();
                    }
                    //create new for modified
                }


            }
            // ****** TYPICAL CASE
            //  is_remove_NLPtag_in_graph_output-->FALSE && is_remove_CSV_delimiter_in_final_Element_in_graph_created-->TRUE
            else if (is_remove_NLPtag_in_graph_output == false && is_remove_CSV_delimiter_in_final_Element_in_graph_created == true) {

                writerdebug.append("\n 3. is_remove_NLPtag_in_graph_output:" + is_remove_NLPtag_in_graph_output
                        + "  is_remove_CSV_delimiter_in_final_Element_in_graph_created:" + is_remove_CSV_delimiter_in_final_Element_in_graph_created);
                writerdebug.flush();

                for (int docID : map_input_gbad_graph_creation_final.keySet()) {
                    System.out.println("3. remove NLP TAG: docID->" + docID);

                    TreeMap<Integer, String> temp = map_input_gbad_graph_creation_final.get(docID);
                    TreeMap<Integer, String> temp_new = new TreeMap<Integer, String>();
                    String tag = "";
                    String token = "";
                    // removing CSV_delimiter_in_final_Element_in_graph_created
                    //always YES here (see else if)
                    if (is_remove_CSV_delimiter_in_final_Element_in_graph_created) {
                        //
                        for (int seq_id : temp.keySet()) {

                            String currNodePair_edge = temp.get(seq_id);

                            // nodePair_edge_do_stemming --- do stemming
                            nodePair_Edge_new = nodePair_edge_do_stemming(currNodePair_edge,
                                    stemmer,
                                    debugFile,
                                    false // is_Write_to_debugFile
                            );
                            nodePair_Edge_new = nodePair_Edge_new.replace("!!!", " ");
                            //
                            writerdebug.append("\n after nodePair_Edge_new():" + nodePair_Edge_new);
                            writerdebug.flush();

                            if (nodePair_Edge_new.split("!#!#").length >= 3) {
                                writerdebug.append("\n calling convert: ");
                                writerdebug.flush();
                                //
                                nodePair_Edge_new = convert_curr_nodepair_edge_to_config_mapping(nodePair_Edge_new, debugFile,
                                        true //is_write_to_debugFile
                                );
                            }

                            //temp_new.put(seq_id, temp.get(seq_id) .replace("!!!"," "));
                            temp_new.put(seq_id, nodePair_Edge_new);

                        }
                    }

                    map_docidSEQ_mapnodePairNedgeForCreateGraph.put(docID, temp_new);

                    if (is_debug_File_Write_more) {
                        //System.out.println("remove tag:"+ tmp);
                        writerdebug.append("final:2:" + map_docidSEQ_mapnodePairNedgeForCreateGraph.size() + "\n");
                        writerdebug.flush();
                    }
                    //create new for modified
                }
            }
            // BOTH false
            else if (is_remove_NLPtag_in_graph_output == false && is_remove_CSV_delimiter_in_final_Element_in_graph_created == false) {

                writerdebug.append("\n 4. is_remove_NLPtag_in_graph_output:" + is_remove_NLPtag_in_graph_output
                        + "  is_remove_CSV_delimiter_in_final_Element_in_graph_created:" + is_remove_CSV_delimiter_in_final_Element_in_graph_created);
                writerdebug.flush();

                for (int docID : map_input_gbad_graph_creation_final.keySet()) {
                    System.out.println("4. remove NLP TAG: docID->" + docID);
                    TreeMap<Integer, String> temp = map_input_gbad_graph_creation_final.get(docID);
                    TreeMap<Integer, String> temp_new = new TreeMap<Integer, String>();

                    // removing CSV_delimiter_in_final_Element_in_graph_created
                    //always YES here (see else if)
                    if (is_remove_CSV_delimiter_in_final_Element_in_graph_created) {

                        for (int seq_id : temp.keySet()) {
                            temp_new.put(seq_id, temp.get(seq_id));

                        }
                    }

                    map_docidSEQ_mapnodePairNedgeForCreateGraph.put(docID, temp_new);

                    if (is_debug_File_Write_more) {
                        //System.out.println("remove tag:"+ tmp);
                        writerdebug.append("final:3:" + map_docidSEQ_mapnodePairNedgeForCreateGraph.size() + "\n");
                        writerdebug.flush();
                    }
                    //create new for modified
                }
            }

            if (is_debug_File_Write_more) {
                //debug
                for (int docID : map_docidSEQ_mapnodePairNedgeForCreateGraph.keySet()) {
                    writerdebug.append("\n docID:" + docID + " map:" + map_docidSEQ_mapnodePairNedgeForCreateGraph.get(docID));
                    writerdebug.flush();
                }
            }

            writerdebug.append("\n------------final input to gbad input graph creation---END------\n");
            writerdebug.flush();
            //int xp_number=0;
            boolean is_append_out_gbad_File = false;
            TreeMap<Integer, String> map_gbad_XP_out = new TreeMap<Integer, String>();
            //each document create as a graph XP in GBAD
            if (is_create_each_document_as_an_XP_in_graph == true) {
                int total = map_docidSEQ_mapnodePairNedgeForCreateGraph.size();
                counter = 0;
                for (int doc_id_seq : map_docidSEQ_mapnodePairNedgeForCreateGraph.keySet()) {
                    counter++;
                    System.out.println("1. create XP in GBAD ->counter:" + counter + " out of total=" + total);

                    if (map_docidSEQ_mapnodePairNedgeForCreateGraph.size() > 0) {
                        xp_number++;

                        if (xp_number > 1) is_append_out_gbad_File = true;

                        if (is_debug_File_Write_more) {
                            writerdebug.append("\n o.map_docidSEQ_mapnodePairNedgeForCreateGraph:" + map_docidSEQ_mapnodePairNedgeForCreateGraph);
                            writerdebug.append("\n o.map_input_create_graph_cnt_NodePairEdgeString:" + map_input_create_graph_cnt_NodePairEdgeString);
                            writerdebug.flush();
                        }


                        System.out.println("(1) PROCESSING creation of GBAD file using create_gbad_xp():");
                        // create GBAD XP
                        map_gbad_XP_out = create_gbad_xp(baseFolder,
                                map_docidSEQ_mapnodePairNedgeForCreateGraph.get(doc_id_seq), //map_idSeq_nodePairNedge
                                map_input_create_graph_cnt_NodePairEdgeString,
                                outFile_for_gbad_graph, //output
                                is_append_out_gbad_File, //is_append_out_gbad_File
                                isskip_d3js_creation, //isskip_d3js_creation
                                xp_number,
                                debugFile,
                                is_remove_NLPtag_in_graph_output,
//													   is_split_on_blank_space_among_tokens, // split on noun , verb
                                is_debug_File_Write_more, // is_debug_File_Write_more
                                isSOPprint,
                                false // is_RUN_only_create_gbad_xp
                        );
                    } //END if(map_docidSEQ_mapnodePairNedgeForCreateGraph.size()>0){

                }

            } else { // create one big graph (1 XP) GBAD

                TreeMap<Integer, String> map_temp = new TreeMap<Integer, String>();
                TreeMap<Integer, String> map_onebigMap_for_oneGBADfile = new TreeMap<Integer, String>();
                int cnt = 0;
                int total = map_docidSEQ_mapnodePairNedgeForCreateGraph.size();
                counter = 0;
                //create as one map
                for (int doc_id_seq : map_docidSEQ_mapnodePairNedgeForCreateGraph.keySet()) {
                    counter++;
                    //System.out.println("2. create XP (one big) in GBAD ->counter:"+counter +" out of total="+total);
                    map_temp = map_docidSEQ_mapnodePairNedgeForCreateGraph.get(doc_id_seq);

                    if (is_debug_File_Write_more) {
                        writerdebug.append("\n f string:" + doc_id_seq + "<->" + map_temp + "\n");
                        writerdebug.flush();
                    }
                    //
                    for (int key : map_temp.keySet()) {
                        cnt++;//new seq, as sequence across different document is same/similar from 1...N
                        TreeMap<Integer, String> map_new_temp = new TreeMap<Integer, String>();
                        // NOT split on blank space .
                        if (is_split_on_blank_space_among_tokens == false) {
                            writerdebug.append("\n split on blank space FALSE");
                            writerdebug.flush();

//							//apply stemming here.
//							map_new_temp=new TreeMap<Integer, String>();
//
//
//							for(int seq: map_temp.keySet()){
//								map_new_temp.put( seq, stemmer.stem(map_temp.get(seq)) ) ;
//							}

                            //stemming
                            map_onebigMap_for_oneGBADfile.put(cnt, stemmer.stem(map_temp.get(key)));

                        }
                        // split on blank space .
                        else {

//							writerdebug.append("\n split on blank space TRUE");
//							writerdebug.flush();
                            String[] s = map_temp.get(key).split("!#!#");

                            int c = 0;
                            //
                            //while(c<s.length){

                            //writerdebug.append("\n entry-> s[0]:"+s[0] +"<->s[1]:"+s[1]+"<->s[2]:"+s[2] );
                            //writerdebug.append("\n entry idx-> s[0]:"+s[0].indexOf(" ") +"<->s[1]:"+s[1].indexOf(" ")+"<->s[2]:"+s[2].indexOf(" ") );
                            //writerdebug.flush();

                            //System.out.println("s:"+s.length);
                            int c22 = 0;
                            if (s.length < 3) {
                                continue;
                            }
                            //while(c22<s.length){System.out.println("s["+c22+"]->"+  s[c22]);  c22++;}

                            //no space
                            if (s[0].indexOf(" ") == -1 && s[1].indexOf(" ") == -1 && s[2].indexOf(" ") == -1) {

                                map_onebigMap_for_oneGBADfile.put(cnt, map_temp.get(key));
                                //writerdebug.append("\n *******IF--> map_new_temp(no space):"+map_temp.get(key));
                                //writerdebug.flush();
                            }
                            //split on space
                            else {
                                int c2 = 0;

                                if (s.length <= 3) {
                                    writerdebug.append("\n *******ELSE(len<3)--> s.len->" + s.length + "---for-->map_temp.get(key)->" + map_temp.get(key) + "<-->"
                                            //+map_input_create_graph_cnt_NodePairEdgeString.get(map_temp.get(key))
                                            + map_distinct_SourceDestEdge_as_KEY);
                                    writerdebug.flush();
                                    //continue;
                                }
                                String[] s2_0 = s[0].split(" ");
                                String[] s2_1 = s[1].split(" ");
                                String[] s2_2 = s[2].split(" ");

//									 writerdebug.append("\n *******ELSE-->(space) s->"+s[0]+"<->"+s[1]+"<->"+s[2] +"-->map_temp.get(key)->"+map_temp.get(key));
//									 writerdebug.append("\n *******ELSE-->(space) s.split.size->"+s2_0.length+"<-->"+s2_1.length+"<-->"+s2_2.length);
//									 writerdebug.flush();

                                int i = 0, j = 0, k = 0;
                                map_new_temp = new TreeMap<Integer, String>();
                                int cnt22 = 0;

                                i = 0;
                                j = 0;
                                k = 0;
                                //approach 2
                                while (i < s2_0.length) {
                                    j = 0;
                                    while (j < s2_1.length) {
                                        k = 0;
                                        while (k < s2_2.length) {
                                            cnt22++;
                                            try {
//													 writerdebug.append("\n i,j,k->"+i+","+j+","+k);
//													 writerdebug.append("\n cnt22:"+cnt22+"<----->"+s2_0[i]+"!!!"+s2_1[j]+"!!!"+s2_2[k]);
//													 writerdebug.flush();

                                                String t = s2_0[i] + "!#!#" + s2_1[j] + "!#!#" + s2_2[k];

                                                if (s.length >= 4)
                                                    t = t + "!#!#" + s[3];
                                                if (s.length >= 5)
                                                    t = t + "!#!#" + s[4];
                                                if (s.length >= 6)
                                                    t = t + "!#!#" + s[5];


                                                //
                                                map_new_temp.put(cnt22, t); // label to debug : approach 2

                                            } catch (Exception e) {
                                                writerdebug.append(" ERROR on split on space:");
                                                writerdebug.flush();
                                            }
                                            k++;
                                        }
                                        j++;
                                    }
                                    i++;
                                }

//									 writerdebug.append("\n ***************");
                                //
                                for (int ij : map_new_temp.keySet()) {
                                    int sz = map_onebigMap_for_oneGBADfile.size() + 1;
                                    cnt++;
                                    map_onebigMap_for_oneGBADfile.put(cnt, map_new_temp.get(ij));
                                    //
//										  writerdebug.append("\n ***ELSE map_onebigMap_for_oneGBADfile:cnt:"+cnt
//												  			+"\n (sz):"+sz
//												  			+"\n<-->map_onebigMap_for_oneGBADfile.get(cnt):"+map_onebigMap_for_oneGBADfile.get(cnt)
//												  			+"\n<-->(curr)map_new_temp.get(ij):"+map_new_temp.get(ij)
//												  			+"\n<-->(all)map_new_temp:"+map_new_temp);
//										  writerdebug.flush();

                                }
//									 writerdebug.append("\n ***************");

                            }
                            //	c++;
                            //}


                            //writerdebug.append("\n ELSE map_new_temp(map):"+map_new_temp);
                            writerdebug.flush();

                        } //END if(is_split_on_blank_space_among_tokens==false){
                    }

                }
                FileWriter writer_map_onebigMap_for_oneGBADfile = null;
                FileWriter writer_map_input_create_graph_cnt_NodePairEdgeString = null;


                writer_map_onebigMap_for_oneGBADfile = new FileWriter(new File(baseFolder + "map_onebigMap_for_oneGBADfile.txt"));
                writer_map_input_create_graph_cnt_NodePairEdgeString = new FileWriter(new File(baseFolder + "map_input_create_graph_cnt_NodePairEdgeString.txt"));

                //write it to output file (next time load from external file)
                for (int i : map_onebigMap_for_oneGBADfile.keySet()) {
                    writer_map_onebigMap_for_oneGBADfile.append(i + "@@@@@" + map_onebigMap_for_oneGBADfile.get(i) + "\n");
                    writer_map_onebigMap_for_oneGBADfile.flush();
                }

                for (int i : map_input_create_graph_cnt_NodePairEdgeString.keySet()) {
                    writer_map_input_create_graph_cnt_NodePairEdgeString.append(i + "@@@@@" + map_input_create_graph_cnt_NodePairEdgeString.get(i) + "\n");
                    writer_map_input_create_graph_cnt_NodePairEdgeString.flush();
                }

                // (1) re-load above two files (2) sync up
                //fine-grained
                TreeMap<Integer, String> map_onebigMap_for_oneGBADfile_reload =
                        ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
                                .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
                                        baseFolder + "map_onebigMap_for_oneGBADfile.txt",
                                        -1, //startline,
                                        -1, //endline,
                                        " reloading..", //debug_label
                                        false //isPrintSOP
                                );
                //coarse-grained
                TreeMap<Integer, String> map_input_create_graph_cnt_NodePairEdgeString_reload =
                        ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
                                .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
                                        baseFolder + "map_input_create_graph_cnt_NodePairEdgeString.txt",
                                        -1, //startline,
                                        -1, //endline,
                                        "thehindu", //debug_label
                                        false //isPrintSOP
                                );

                FileWriter writer_map_onebigMap_for_oneGBADfile_NEW = new FileWriter(new File(baseFolder + "map_onebigMap_for_oneGBADfile_NEW.txt"));
                FileWriter writer_map_input_create_graph_cnt_NodePairEdgeString_NEW = new FileWriter(new File(baseFolder + "map_input_create_graph_cnt_NodePairEdgeString_NEW.txt"));

                //
                //map_input_create_graph_cnt_NodePairEdgeString -> coarse-grained
                //map_idSeq_nodePairNedge -> fine-grained  (sync up
                for (int seq : map_input_create_graph_cnt_NodePairEdgeString.keySet()) {
                    String t = map_input_create_graph_cnt_NodePairEdgeString.get(seq).replace("!!!!!!", "!!!").replace("!!!!!!", "!!!")
                            .replace("!!!", " ").replace("  ", " ");
                    //writer_map_onebigMap_for_oneGBADfile_NEW.append("orig:"+t+"\n");
                    writer_map_onebigMap_for_oneGBADfile_NEW.flush();
                    String[] s = t.split("!#!#");
                    //System.out.println("-->t:"+t +" len:"+s.length);
                    String config_mapping = "";
                    String c_target = "";
                    String c_source = "";
                    //
                    if (s.length >= 6) {
                        c_source = s[0];
                        c_target = s[1];
                        if (s[2].indexOf("remark") == -1)
                            edge = s[2];
                        else if (s[3].indexOf("remark") == -1)
                            edge = s[3];


                        config_mapping = s[4]; //mapping

                        //reconstruct with only 5 tokens
                        t = s[0] + "!#!#" + s[1] + "!#!#" + s[2] + "!#!#" + s[3] + "!#!#" + s[4];
                        s = t.split("!#!#");

                    } else if (s.length == 4) {
                        c_source = s[0];
                        c_target = s[1];
                        edge = s[2];
                        config_mapping = s[3]; //mapping

                    } else {
                        writer_map_onebigMap_for_oneGBADfile_NEW.write(map_input_create_graph_cnt_NodePairEdgeString.get(seq) + "\n");
                        writer_map_onebigMap_for_oneGBADfile_NEW.flush();
                    }

                    if (c_source.equalsIgnoreCase("") || c_target.equalsIgnoreCase(""))
                        continue;
                    String[] arr_source = c_source.split(" ");
                    String[] arr_target = c_target.split(" ");

//						System.out.println("source:"+c_source+" len:"+arr_source.length);
//						System.out.println("target:"+c_target+" len:"+arr_target.length);

                    //writer_map_onebigMap_for_oneGBADfile_NEW.append("source:"+c_source+" len:"+arr_source.length+"\n");
                    //writer_map_onebigMap_for_oneGBADfile_NEW.append("target:"+c_target+" len:"+arr_target.length+"\n");
                    writer_map_onebigMap_for_oneGBADfile_NEW.flush();

                    int c1 = 0;
                    int c2 = 0;
                    //
                    while (c1 < arr_source.length) {
                        c2 = 0;
                        while (c2 < arr_target.length) {
                            int sz = map_onebigMap_for_oneGBADfile_reload.size() + 1;
                            writer_map_onebigMap_for_oneGBADfile_NEW.write(arr_source[c1] + "!#!#" + arr_target[c2] + "!#!#" + edge + "!#!#remark!#!#" + config_mapping + "\n");
                            writer_map_onebigMap_for_oneGBADfile_NEW.flush();
                            c2++;
                        }
                        c1++;
                    }
                    //BELOW LINE HELPS TO FIX
                    //map_idSeq_nodePairNedge.put( , map_idSeq_nodePairNedge.get( ));
                }
                //LOAD ABOVE JUST CREATED FILE

                // dont load, read line by line
//					TreeMap<Integer,String> map_onebigMap_for_oneGBADfile_NEW=
//							  readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
//							 .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
//									 							 baseFolder+"map_onebigMap_for_oneGBADfile_NEW.txt",
//															 	 -1, //startline,
//																 -1, //endline,
//																 "thehindu", //debug_label
//																 false //isPrintSOP
//																 );
                int cnt22 = 0;
                String line = "";
                BufferedReader reader = new BufferedReader(new FileReader(baseFolder + "map_onebigMap_for_oneGBADfile_NEW.txt"));
                // read each line of given file
                while ((line = reader.readLine()) != null) {
                    cnt22 = map_onebigMap_for_oneGBADfile.size() + 1;

                    if (map_onebigMap_for_oneGBADfile.containsKey(cnt22))
                        writerdebug.append(" already exists : cnt:" + cnt);
                    writerdebug.flush();

                    map_onebigMap_for_oneGBADfile.put(cnt22, line);

                }

                //
                if (map_onebigMap_for_oneGBADfile.size() > 0) {
                    xp_number++; // ONE

                    if (xp_number > 1) {
                        is_append_out_gbad_File = true;
                    }

                    if (is_debug_File_Write_more) {
                        writerdebug.append("\n map_docidSEQ_mapnodePairNedgeForCreateGraphNsize:" + map_docidSEQ_mapnodePairNedgeForCreateGraph.size() + "<->"
                                + " map=>" + map_docidSEQ_mapnodePairNedgeForCreateGraph);
                        writerdebug.append("\n map_input_create_graph_cnt_NodePairEdgeStringNsize:" + map_input_create_graph_cnt_NodePairEdgeString.size() + "<->"
                                + " map=>" + map_input_create_graph_cnt_NodePairEdgeString);
                        writerdebug.flush();
                    }

                    //create the memory START
                    map_atom_as_VALUE.clear();
                    map_token_semantic_equiv.clear();
                    map_gbad_XP_out.clear();
                    map_input_gbad_graph_creation_final.clear();
                    map_distinct_SourceDestEdge_as_KEY.clear();
                    map_docidSEQ_mapnodePairNedgeForCreateGraph.clear();
                    //map_onebigMap_for_oneGBADfile_NEW.clear();
                    //create the memory END

                    /// example composite nouns with space "Ukraines government" will be split into "Ukraines" and "government"
                    System.out.println("(2) PROCESSING creation of GBAD file using create_gbad_xp() : " + map_onebigMap_for_oneGBADfile.size());

                    //CREATE one big graph
                    map_gbad_XP_out = create_gbad_xp(baseFolder,
                            map_onebigMap_for_oneGBADfile, /////fine-grained
                            map_input_create_graph_cnt_NodePairEdgeString, // coarse-grained
                            outFile_for_gbad_graph, //output
                            is_append_out_gbad_File, //is_append_out_gbad_File, //is_append_out_gbad_File
                            isskip_d3js_creation,
                            xp_number,
                            debugFile,
                            is_remove_NLPtag_in_graph_output,
//												    is_split_on_blank_space_among_tokens, // split on noun , verb
                            is_debug_File_Write_more,
                            isSOPprint,
                            false //is_RUN_only_create_gbad_xp
                    );

                }

            } // END if(is_create_each_document_as_an_XP_in_graph==true){


            //status print STATUS
            if (map_gbad_XP_out.containsKey(-9))
                System.out.println(" **STATUS: GBAD XP creation has WARNING: " + map_gbad_XP_out.get(-9));
            else
                System.out.println(" **STATUS: NO issues in GBAD XP creation.. ");

            //
            if (String.valueOf(xp_number) == null) {
                writerdebug.append("\n ERROR :  xp_number is null, so reset to zero ");
                writerdebug.flush();
                xp_number = 0;
            }

            //return the last used xp_number
            map_input_gbad_graph_creation.put(-99, String.valueOf(xp_number));
            map_input_gbad_graph_creation.put(-9, String.valueOf(stat_hit_on_repo_semantic));

            writerdebug.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writerdebug != null)
                try {
                    writerdebug.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return map_input_gbad_graph_creation;
    }

    // create XP
    public static TreeMap<Integer, String> create_gbad_xp(String baseFolder,
                                                          TreeMap<Integer, String> map_idSeq_nodePairNedge, ///fine-grained
                                                          TreeMap<Integer, String> map_input_create_graph_cnt_NodePairEdgeString, //coarse-grained
                                                          String out_gbad_File,
                                                          boolean is_append_out_gbad_File,
                                                          boolean isskip_d3js_creation,
                                                          int xp_number,
                                                          String debugFile,
                                                          boolean is_remove_NLPtag_in_graph_output,
                                                          //boolean 	is_split_on_blank_space_among_tokens, // split on noun , verb
                                                          boolean is_debug_File_Write_more,
                                                          boolean isSOPprint,
                                                          boolean is_RUN_only_create_gbad_xp //true means running only this method from past saved files
    ) {
        Stemmer stemmer = new Stemmer();
        TreeMap<Integer, String> mapOut = new TreeMap<Integer, String>();
        // TODO Auto-generated method stub
        TreeMap<Integer, String> map_uniqID_NodeName = new TreeMap<Integer, String>();
        //Node name such as "Ukraine government" will be split to "Ukraine" and "Government"
        TreeMap<Integer, TreeMap<Integer, String>> map_uniqID_NodeName_N_SplitNodeName =
                new TreeMap<Integer, TreeMap<Integer, String>>();

        TreeMap<String, Integer> map_NodeName_uniqID = new TreeMap<String, Integer>();

        TreeMap<String, String> map_uniqID_ItsConfigMapping = new TreeMap<String, String>();
        boolean is_convert_NodePair_edge_to_lowercase = true;
        int maxValueInMap_map_last_RAN_seq_ID = -1;
        String d3JS_nodeEdgeString = "{nodes:[";
        TreeMap<String, String> map_isAlready_wrote_edge = new TreeMap<String, String>();
        TreeMap<String, String> map_TokenWithOutTag_TokenWithTag = new TreeMap<String, String>();
        TreeMap<String, String> map_TokenWithTag_TokenWithOutTag = new TreeMap<String, String>();
        TreeMap<Integer, String> map_past_partially_ouput_GBAD = new TreeMap<Integer, String>();
        TreeMap<Integer, String> map_last_RAN_seq_ID = new TreeMap<Integer, String>();
        FileWriter writer_gbad = null;
        FileWriter writer_debug = null;
        FileWriter writer_last_RAN_seq_ID = null;
        try {
            //load last RAN seq ID
            if (is_RUN_only_create_gbad_xp)
                writer_last_RAN_seq_ID = new FileWriter(new File(baseFolder + "last_ran_seq_ID.txt"), true);
            else
                writer_last_RAN_seq_ID = new FileWriter(new File(baseFolder + "last_ran_seq_ID.txt"), false);

            System.out.println("Convert_For_GBAD.create_gbad_xp() out_gbad_File->" + out_gbad_File + " is_append_out_gbad_File:" + is_append_out_gbad_File);
            if (!new File(out_gbad_File).exists()) {
                System.out.println("creating new file ********");
                new File(out_gbad_File).createNewFile();
            }

            if (writer_gbad != null) {
                System.out.println("creating new file CLOSING ********");
                writer_gbad.close();
            }

            try {
                writer_gbad = new FileWriter(new File(out_gbad_File), is_append_out_gbad_File);
                // load past partially created GBAD file
                if (is_RUN_only_create_gbad_xp) {
                    map_past_partially_ouput_GBAD = ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
                            .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(out_gbad_File,
                                    -1, //startline,
                                    -1, //endline,
                                    "thehindu", //debug_label
                                    true //isPrintSOP
                            );

                    map_last_RAN_seq_ID = ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
                            .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(baseFolder + "last_ran_seq_ID.txt",
                                    -1, //startline,
                                    -1, //endline,
                                    "thehindu", //debug_label
                                    true //isPrintSOP
                            );
                    //**** Below MAX not working!@!@@!@
                    maxValueInMap_map_last_RAN_seq_ID = Integer.valueOf((Collections.max(map_last_RAN_seq_ID.values())));

                    maxValueInMap_map_last_RAN_seq_ID = Integer.valueOf(map_last_RAN_seq_ID.get(1));

                    writer_debug.append("\nGOT maxValueInMap_map_last_RAN_seq_ID:" + maxValueInMap_map_last_RAN_seq_ID);
                    writer_debug.flush();


                }

            } catch (Exception e) {
                System.out.println("ERROR ***** GBAD OPEN " + e.getMessage());
            }
            try {
                writer_debug = new FileWriter(new File(debugFile), true);
            } catch (Exception e) {
                System.out.println("ERROR ***** DEBUG OPEN " + e.getMessage());
            }

            writer_debug.append("\nVERIFY:two size should be equal:" + map_input_create_graph_cnt_NodePairEdgeString.size() + "<-->" + map_idSeq_nodePairNedge.size());
            writer_debug.flush();


//			writer_debug.append("\n------------inside ---start -----");
//			for(int s:map_idSeq_nodePairNedge.keySet()){
//				writer_debug.append( "\n"+map_idSeq_nodePairNedge.get(s));
//				writer_debug.flush();
//			}
//			writer_debug.append("\n------------inside - end-------");

//			for(int t2:map_input_create_graph_cnt_NodePairEdgeString.keySet()){
//				writer_debug.append("\n map1:"+t2+"<-->"+map_input_create_graph_cnt_NodePairEdgeString.get(t2));
//				writer_debug.flush();
//			}


//			if(map_idSeq_nodePairNedge.size() >map_input_create_graph_cnt_NodePairEdgeString.size() ){
//				for(int t11:map_idSeq_nodePairNedge.keySet()){
//					//BELOW LINE HELPS TO FIX
//					map_input_create_graph_cnt_NodePairEdgeString.put(t11, map_idSeq_nodePairNedge.get(t11));
//					writer_debug.append("\n map2:"+t11+"<-->"+map_input_create_graph_cnt_NodePairEdgeString.get(t11));
//					writer_debug.flush();
//				}
//			}
//			else {


//			}


            //

            writer_debug.append("\nVERIFY:two size should be equal (after):" + map_input_create_graph_cnt_NodePairEdgeString.size() + "<-->" + map_idSeq_nodePairNedge.size());

            String curr_source_config_mapping = "";
            String curr_dest_config_mapping = "";
            String curr_edge_config_mapping = "";
            int counter = 0;
            int total = map_idSeq_nodePairNedge.size();
            //	prepare pair of token (without tag , and token with tag)
            for (int id_seq : map_idSeq_nodePairNedge.keySet()) {   //map_idSeq_nodePairNedge.keySet()){
                counter++;
                System.out.println("create gbad(1):" + id_seq + " counter:" + counter + " out of total=" + total);
                String t = map_idSeq_nodePairNedge.get(id_seq).replace("\"", "");//map_idSeq_nodePairNedge.get(id_seq).replace("\"", "");
                if (is_convert_NodePair_edge_to_lowercase)
                    t = t.toLowerCase();
                // cleaning (copied to all place uniform in this method)
                t = t.replace(",", " ").replace(":", " ").replace("  ", " ");
                //
                String[] s = t.split("!#!#");

                if (s.length < 3) {
                    continue;
                }

                String curr_token = "";
                //source
                if (s[0].indexOf("_") >= 0) {
                    ///word /token
                    curr_token = stemmer.stem(s[0].substring(0, s[0].indexOf("_")));

                    //NOT split on blank space of token
// 						if(is_split_on_blank_space_among_tokens==false){
                    //TreeMap<Integer,String> temp=map_TokenWithOutTag_TokenWithTag.get(curr_token);
                    //int sz =temp.size()+1;
                    //temp.put(sz , value );
                    map_TokenWithOutTag_TokenWithTag.put(curr_token, s[0]);
                    map_TokenWithTag_TokenWithOutTag.put(s[0], curr_token);
// 						}
// 						else{// split on blank space of token
//
// 						}

                } else {
                    //NOT split on blank space of token
// 						if(is_split_on_blank_space_among_tokens==false){
                    map_TokenWithOutTag_TokenWithTag.put(s[0], s[0]);
                    map_TokenWithTag_TokenWithOutTag.put(s[0], stemmer.stem(s[0]));
//						}
//						else{// split on blank space of token
//						}
                }
                //destination
                if (s[1].indexOf("_") >= 0) {
                    //NOT split on blank space of token
// 						if(is_split_on_blank_space_among_tokens==false){
                    ///word /token
                    curr_token = stemmer.stem(s[1].substring(0, s[1].indexOf("_")));
                    map_TokenWithOutTag_TokenWithTag.put(curr_token, s[1]);
                    map_TokenWithTag_TokenWithOutTag.put(s[1], curr_token);
// 						}
// 						else{ // split on blank space of token
//
// 						}

                } else {
                    //NOT split on blank space of token
// 						if(is_split_on_blank_space_among_tokens==false){
                    map_TokenWithOutTag_TokenWithTag.put(s[1], s[1]);
                    map_TokenWithTag_TokenWithOutTag.put(s[1], s[1]);
// 						}
// 						else{ // split on blank space of token
//
// 						}
                }

                //edges
                if (s[2].indexOf("_") >= 0) {
                    ///word /token
                    curr_token = stemmer.stem(s[2].substring(0, s[2].indexOf("_")));
                    map_TokenWithOutTag_TokenWithTag.put(curr_token, s[2]);
                    map_TokenWithTag_TokenWithOutTag.put(s[2], curr_token);
                } else {
                    map_TokenWithOutTag_TokenWithTag.put(s[2], s[2]);
                    map_TokenWithTag_TokenWithOutTag.put(s[2], s[2]);
                }
            } //END for(int id_seq:map_idSeq_nodePairNedge.keySet()){
            total = map_idSeq_nodePairNedge.size();
            counter = 0;
            // iterate each of node pair and its edge.
            for (int id_seq : map_idSeq_nodePairNedge.keySet()) {
                counter++;
                System.out.println("create gbad(2):" + id_seq + " counter:" + counter + " out of total=" + total);
                String t = map_idSeq_nodePairNedge.get(id_seq).replace("\"", "");     //map_idSeq_nodePairNedge.get(id_seq).replace("\"", "");
                if (is_convert_NodePair_edge_to_lowercase)
                    t = t.toLowerCase();

                // cleaning (copied to all place uniform in this method)
                t = t.replace(",", " ").replace(":", " ").replace("  ", " ");
                String[] s = t.split("!#!#");
                String curr_token = "", curr_tag = "";
                // this is same as t above, but t is lowercase
                String curr_config_nodePair_edge = map_idSeq_nodePairNedge.get(id_seq);

                if (s.length <= 3) {
                    //
                    String t_new = convert_curr_nodepair_edge_to_config_mapping(curr_config_nodePair_edge,
                            debugFile,
                            true //is_write_to_debugFile
                    );
                    writer_debug.append("\n error:lesser than <=3 tokens : " + t + " t_new:" + t_new);
                    t = t_new;
                    writer_debug.flush();
                    curr_config_nodePair_edge = t_new;
                    //continue;
                }

                //
                if (is_debug_File_Write_more) {
                    writer_debug.append("\nis_remove_NLPtag_in_graph_output:" + is_remove_NLPtag_in_graph_output +
                            "\n t:" + t + " curr_config_nodePair:" + curr_config_nodePair_edge);
                    //writer_debug.append("\n t:.id:"+map_idSeq_nodePairNedge.get(id_seq));
                    writer_debug.flush();

                }
                if (curr_config_nodePair_edge == null) {
                    if (is_debug_File_Write_more) {
                        writer_debug.append("\n ERROR NULL on t.1:" + t + " AND curr_config_nodePair_edge IS NULL:" + curr_config_nodePair_edge);
                        writer_debug.flush();
                    }
                    continue;
                }

                if (isSOPprint)
                    System.out.println("curr_config_nodePair_edge:" + curr_config_nodePair_edge);

                if (curr_config_nodePair_edge.length() < 1) {
                    System.out.println("curr_config_nodePair_edge continue...");
                    continue;
                }

                String[] s_curr_nodePair_Edge_config_maping = null;

                s_curr_nodePair_Edge_config_maping = curr_config_nodePair_edge.split("!#!#")[4].split(" ");

                writer_debug.append("\n ---curr_config_nodePair_edge:" + curr_config_nodePair_edge);
                writer_debug.flush();

                //add the tag
                if (is_remove_NLPtag_in_graph_output == false) {
                    try {
                        //System.out.println("curr_config_nodePair_edge:"+curr_config_nodePair_edge);
                        if (s_curr_nodePair_Edge_config_maping[0].indexOf("[") == -1)
                            curr_source_config_mapping = s_curr_nodePair_Edge_config_maping[0];
                        if (s_curr_nodePair_Edge_config_maping[1].indexOf("[") == -1)
                            curr_dest_config_mapping = s_curr_nodePair_Edge_config_maping[1];
                        if (s_curr_nodePair_Edge_config_maping[2].indexOf("[") == -1)
                            curr_edge_config_mapping = s_curr_nodePair_Edge_config_maping[2];
                        //
                        if (is_debug_File_Write_more) {
                            writer_debug.append("\ngot curr_source_config_mapping->" + curr_source_config_mapping
                                    + "<->" + curr_dest_config_mapping + "<->" + curr_edge_config_mapping);
                            writer_debug.flush();
                        }
                    } catch (Exception e) {
                        curr_source_config_mapping = "";
                        curr_dest_config_mapping = "";
                        curr_edge_config_mapping = "";
                    }


                } else {
                    curr_source_config_mapping = "";
                    curr_dest_config_mapping = "";
                    curr_edge_config_mapping = "";
                }


                // NODES - apply stemming here if only single token.(intuition - most verbs are singular in our case)
                if (s[0] != null) {
                    if (s[0].indexOf(" ") == -1) {
                        //NLP tag present
                        if (s[0].indexOf("_") >= 0) {

//							  ///word /token
//							curr_token=stemmer.stem(s[0].substring(0,s[0].indexOf("_")));
//
//							//	nlp tag
//							if(s[0].indexOf("_")>=0)
//								curr_tag=s[0].substring(s[0].indexOf("_"), s[0].length());
//							else
//								curr_tag="";
                            //reassign
                            //s[0]=curr_token+curr_tag;
                            // NOT split on blank space
//							if(is_split_on_blank_space_among_tokens==false){
                            if (is_remove_NLPtag_in_graph_output) {
                                s[0] = stemmer.stem(map_TokenWithTag_TokenWithOutTag.get(s[0]));
                            } else {
                                if (map_TokenWithOutTag_TokenWithTag.containsKey(s[0]))
                                    s[0] = stemmer.stem(map_TokenWithOutTag_TokenWithTag.get(s[0]));
                            }
//							}
//							else{//split on blank space of token
//
//								if(is_remove_NLPtag_in_graph_output){
//									s[0]=stemmer.stem(map_TokenWithTag_TokenWithOutTag.get(s[0]));
//								}
//								else{
//									if(map_TokenWithOutTag_TokenWithTag.containsKey(s[0]))
//										s[0]=stemmer.stem(map_TokenWithOutTag_TokenWithTag.get(s[0]));
//								}
//
//							}


                            //System.out.println(" entry 7. "+s[0]);
                        } else {
                            // NOT split on blank space
                            //if(is_split_on_blank_space_among_tokens==false){
                            // no NLP tag
                            s[0] = stemmer.stem(s[0]);
                            //System.out.println(" entry 8. "+s[0]);
//							}//split on blank space of token
//							else{
//
//							}
                        }

                    }

                }
                //destination
                if (s[1] != null) {
                    if (s[1].indexOf(" ") == -1) {
                        //s[1]=stemmer.stem(s[1]);
                        //NLP tag present
                        if (s[1].indexOf("_") >= 0) {

                            ///word /token
//							curr_token=stemmer.stem(s[1].substring(0,s[1].indexOf("_")));
//
//							//	nlp tag
//							if(s[1].indexOf("_")>=0)
//								curr_tag=s[1].substring(s[1].indexOf("_"), s[1].length());
//							else
//								curr_tag="";
//							//reassign
//							s[1]=curr_token+curr_tag;

                            // NOT split on blank space
//							if(is_split_on_blank_space_among_tokens==false){
                            if (is_remove_NLPtag_in_graph_output) {
                                s[1] = stemmer.stem(map_TokenWithTag_TokenWithOutTag.get(s[1]));
                            } else {
                                if (map_TokenWithOutTag_TokenWithTag.containsKey(s[1])) {
                                    s[1] = stemmer.stem(map_TokenWithOutTag_TokenWithTag.get(s[1]));
                                }
                            }
//							}
//							else{//split on blank space of token
//
//							}

                            //System.out.println(" entry 5. "+s[1]);
                        } else {
                            // NOT split on blank space
//							if(is_split_on_blank_space_among_tokens==false){
                            // no NLP tag
                            s[1] = stemmer.stem(s[1]);
                            //System.out.println(" entry 6. "+s[1]);
//							}
//							else{//split on blank space of token
//
//							}
                        }

                    }
                }
                boolean _is_valid = false;
                //source
                if (!map_NodeName_uniqID.containsKey(s[0]) && !s[0].equalsIgnoreCase("null") && s[0].length() > 0) {
                    writer_debug.append("\n source d3js:" + s[0] + "<--->" + s[1]);
                    writer_debug.flush();
                    // NOT split on blank space
//					if(is_split_on_blank_space_among_tokens==false){
                    _is_valid = false; ////FIRST WORD CANT BE A TAG
                    if (s[0].replace(":", "").replace("\\", "").indexOf("$_n") == 0 ||
                            s[0].replace(":", "").replace("\\", "").indexOf("$_v") == 0) {
                        _is_valid = true;
                    }
                    //
                    if (!map_NodeName_uniqID.containsKey(s[0]) // && _is_valid==false
                            ) {
                        int new_id = map_NodeName_uniqID.size() + 1;
                        map_NodeName_uniqID.put(s[0], new_id);
                        map_uniqID_NodeName.put(new_id, s[0]);
                        map_uniqID_ItsConfigMapping.put(s[0], curr_source_config_mapping);
                    }

//					}
//					else{//split on blank space of token
//						String [] split_token=s[0].split(" ");
//						int c=0;
//						//
//						while(c<split_token.length){
//							int new_id=map_NodeName_uniqID.size()+1;
//							map_NodeName_uniqID.put(split_token[c] , new_id);
//							map_uniqID_NodeName.put(new_id, split_token[c]);
//							map_uniqID_ItsConfigMapping.put(split_token[c], curr_source_config_mapping);
//							c++;
//						}
//					}
                }

                //destination
                _is_valid = false; //FIRST WORD CANT BE A TAG
                if (s[1].replace(":", "").replace("\\", "").indexOf("$_n") == 0 ||
                        s[1].replace(":", "").replace("\\", "").indexOf("$_v") == 0) {
                    _is_valid = true;
                }
                //destination
                if (!map_NodeName_uniqID.containsKey(s[1]) && !s[1].equalsIgnoreCase("null") && s[1].length() > 0 // && _is_valid==false
                        ) {
                    // NOT split on blank space
//					if(is_split_on_blank_space_among_tokens==false){
                    if (!map_NodeName_uniqID.containsKey(s[1])) {
                        int new_id = map_NodeName_uniqID.size() + 1;
                        map_NodeName_uniqID.put(s[1], new_id);
                        map_uniqID_NodeName.put(new_id, s[1]);
                        map_uniqID_ItsConfigMapping.put(s[1], curr_dest_config_mapping);
                    }
//					}
//					else{//split on blank space of token
//						String [] split_token=s[1].split(" ");
//						int c=0;
//						//
//						while(c<split_token.length){
//							int new_id=map_NodeName_uniqID.size()+1;
//							map_NodeName_uniqID.put(split_token[c] , new_id);
//							map_uniqID_NodeName.put(new_id, split_token[c]);
//							map_uniqID_ItsConfigMapping.put(split_token[c], curr_dest_config_mapping);
//							c++;
//						}
//					}
                } //END if(!map_NodeName_uniqID.containsKey(s[1]) && !s[1].equalsIgnoreCase("null")  && s[1].length()>0 && _is_valid==true){

                curr_source_config_mapping = "";
                curr_dest_config_mapping = "";
                curr_edge_config_mapping = "";
            }

            writer_gbad.append("XP # " + xp_number + "\n");
            writer_gbad.flush();
            String tmp_config = "";

            if (is_debug_File_Write_more) {
                writer_debug.append("\n map_uniqID_ItsConfigMapping:" + map_uniqID_ItsConfigMapping);
                writer_debug.append("\n d3js.map_uniqID_NodeName:" + map_uniqID_NodeName);
                writer_debug.flush();
            }
            total = map_uniqID_NodeName.size();


            counter = 0;
            //print all node
            for (int id_ : map_uniqID_NodeName.keySet()) {
                counter++;
                System.out.println("create gbad(3):" + id_ + " counter:" + counter + " out of total=" + total);
                //
                if (map_uniqID_ItsConfigMapping.containsKey(map_uniqID_NodeName.get(id_)))
                    tmp_config = map_uniqID_ItsConfigMapping.get(map_uniqID_NodeName.get(id_));

                //error node names, cant start directly with tag names.
//				if(map_uniqID_NodeName.get(id_).replace(":", "").replace("\\", "").indexOf("$_n") ==0 ||
//				  map_uniqID_NodeName.get(id_).replace(":", "").replace("\\", "").indexOf("$_v") ==0){
//					continue;
//				}

                //
                writer_gbad.append("v " + id_ + " \"" + map_uniqID_NodeName.get(id_).replace(":", "").replace("\\", "")
                        + " " + tmp_config
                        + "\"\n");
                writer_gbad.flush();

                if (isskip_d3js_creation == false) {
                    //
                    d3JS_nodeEdgeString = d3JS_nodeEdgeString + "{name:\"" + map_uniqID_NodeName.get(id_).replace("'", "").replace("\\", "")
                            + " " + tmp_config //configuration element such as $_n and $
                            + "\"" + "},";
                }

                //writer_debug.append("\n source d3js d3JS_nodeEdgeString:"+d3JS_nodeEdgeString);
                writer_debug.flush();

            }
            curr_source_config_mapping = "";
            curr_dest_config_mapping = "";
            curr_edge_config_mapping = "";

            if (isskip_d3js_creation == false) {
                //remove last comma
                d3JS_nodeEdgeString = d3JS_nodeEdgeString.substring(0, d3JS_nodeEdgeString.length() - 1);
                d3JS_nodeEdgeString = d3JS_nodeEdgeString + "],\n edges:[";
            }

            total = map_idSeq_nodePairNedge.size();
            counter = 0;
            String conc_Id_Seq = "";
            // iterate each of node pair and its edge. Step gbad(4)
            for (int id_seq : map_idSeq_nodePairNedge.keySet()) { // map_idSeq_nodePairNedge.keySet()){
                //long t10 = System.nanoTime();
                counter++;

                if (is_RUN_only_create_gbad_xp == true && id_seq <= maxValueInMap_map_last_RAN_seq_ID) {
                    System.out.println("gbad(4) Past ran (CURR) id_seq->" + id_seq + " maxValueInMap_map_last_RAN_seq_ID:" + maxValueInMap_map_last_RAN_seq_ID);
                    continue;
                }

                String t = map_idSeq_nodePairNedge.get(id_seq).replace("\"", ""); //map_idSeq_nodePairNedge.get(id_seq).replace("\"", "");
                System.out.println("create gbad(4):" + id_seq + " counter:" + counter + " out of total=" + total);//+" t->"+t);

                if (is_convert_NodePair_edge_to_lowercase)
                    t = t.toLowerCase();

                // cleaning (copied to all place uniform in this method)
                t = t.replace(",", " ").replace(":", " ").replace("  ", " ");
                String[] s = t.split("!#!#");


//				System.out.println("1 Time Taken (FINAL ENDED):"+ NANOSECONDS.toSeconds(System.nanoTime() - t10)+ " seconds; "
//									+ (NANOSECONDS.toSeconds(System.nanoTime() - t10)) / 60+ " minutes");
//				t10 = System.nanoTime();

                //add the tag
                if (is_remove_NLPtag_in_graph_output == false) {
                    // config mapping
                    String[] s_curr_nodePair_Edge_config_maping = null;
                    String curr_config_nodePair_edge = map_input_create_graph_cnt_NodePairEdgeString.get(id_seq);

                    if (curr_config_nodePair_edge == null) {
                        writer_debug.append("\n ERROR: NULL on t.2:" + t + "<-->AND curr_config_nodePair_edge:" + curr_config_nodePair_edge);

                        if (is_debug_File_Write_more)
                            writer_debug.append("\n ERROR id_seq:" + id_seq + "<-->" + map_input_create_graph_cnt_NodePairEdgeString);

                        writer_debug.flush();
                        //on analysis there is no IMPACT on this on UI .. all nodes having right tag such as _vb or _nn

                        curr_config_nodePair_edge = "dum!#!#dum!#!#dum!#!#dum!#!#[dum] [dum] dum!#!#dum!#!#";

                        //continue;
                    }
//					else{writer_debug.append("\n NO .ERROR: NULL on t.2:"+ t+ "AND curr_config_nodePair_edge:"+curr_config_nodePair_edge);
//						writer_debug.flush();}

                    if (curr_config_nodePair_edge.length() > 1)
                        s_curr_nodePair_Edge_config_maping = curr_config_nodePair_edge.split("!#!#")[4].split(" ");

                    if (is_debug_File_Write_more) {
                        writer_debug.append("\nis_remove_NLPtag_in_graph_output:" + is_remove_NLPtag_in_graph_output +
                                "\n t:" + t + " curr_config_nodePair:" + curr_config_nodePair_edge);
                        //writer_debug.append("\n t:.id:"+map_idSeq_nodePairNedge.get(id_seq));
                        writer_debug.flush();
                    }

                    //System.out.println("split:"+s_curr_nodePair_Edge_config_maping[0]+"<->"+s_curr_nodePair_Edge_config_maping[1]+"<->"+s_curr_nodePair_Edge_config_maping[2]);

                    try {
                        if (s_curr_nodePair_Edge_config_maping[0].indexOf("[") == -1)
                            curr_source_config_mapping = s_curr_nodePair_Edge_config_maping[0];
                        if (s_curr_nodePair_Edge_config_maping[1].indexOf("[") == -1)
                            curr_dest_config_mapping = s_curr_nodePair_Edge_config_maping[1];
                        if (s_curr_nodePair_Edge_config_maping[2].indexOf("[") == -1)
                            curr_edge_config_mapping = s_curr_nodePair_Edge_config_maping[2];

                        if (is_debug_File_Write_more) {
                            writer_debug.append("\ngot curr_source_config_mapping->" + curr_source_config_mapping
                                    + "<->" + curr_dest_config_mapping + "<->" + curr_edge_config_mapping);
                            writer_debug.flush();
                        }
                    } catch (Exception e) {
                        curr_source_config_mapping = "";
                        curr_dest_config_mapping = "";
                        curr_edge_config_mapping = "";
                    }
                } else {
                    curr_source_config_mapping = "";
                    curr_dest_config_mapping = "";
                    curr_edge_config_mapping = "";
                }

                String curr_token = "", curr_tag = "";
                //skipping NULL
                if (s[0].equalsIgnoreCase("null") || s[0].length() == 0 || s.length < 2) continue;
                if (s[1].equalsIgnoreCase("null") || s[1].length() == 0) continue;
                //trim
                //s[0]=s[0].trim();s[1]=s[1].trim();s[2]=s[2].trim();
//				System.out.println("2 Time Taken (FINAL ENDED):"+ NANOSECONDS.toSeconds(System.nanoTime() - t10)+ " seconds; "
//						+ (NANOSECONDS.toSeconds(System.nanoTime() - t10)) / 60+ " minutes");
//				t10 = System.nanoTime();

                // NODES - apply stemming here if only single token.(intuition - most verbs are singular in our case)
                // NODES - apply stemming here if only single token.(intuition - most verbs are singular in our case)
                if (s[0] != null) {
                    if (s[0].indexOf(" ") == -1) {
                        //NLP tag present
                        if (s[0].indexOf("_") >= 0) {

                            ///word /token
//							curr_token=stemmer.stem(s[0].substring(0,s[0].indexOf("_")));
//
//							//	nlp tag
//							if(s[0].indexOf("_")>=0)
//								curr_tag=s[0].substring(s[0].indexOf("_"), s[0].length());
//							else
//								curr_tag="";
//							//reassign
//							s[0]=curr_token+curr_tag;

                            //
                            if (is_remove_NLPtag_in_graph_output) {
                                // NOT split on blank space
//								if(is_split_on_blank_space_among_tokens==false){
                                ///semming is a

                                s[0] = stemmer.stem(map_TokenWithTag_TokenWithOutTag.get(s[0]));

                                //s[0]= map_TokenWithTag_TokenWithOutTag.get(s[0]);
//								}
//								else{//split on blank space of token
//									s[0]=stemmer.stem(map_TokenWithTag_TokenWithOutTag.get(s[0]));
//
//								}
                            } else {
                                // NOT split on blank space
//								if(is_split_on_blank_space_among_tokens==false){
                                if (map_TokenWithOutTag_TokenWithTag.containsKey(s[0])) {
                                    s[0] = stemmer.stem(map_TokenWithOutTag_TokenWithTag.get(s[0]));
                                }
//								}
//								else{//split on blank space of token
//									s[0]=stemmer.stem(map_TokenWithOutTag_TokenWithTag.get(s[0]));
//
//								}
                            }

                            //System.out.println(" entry 1. "+s[0]);
                        } else {
                            // NOT split on blank space of token
//							if(is_split_on_blank_space_among_tokens==false){
                            // no NLP tag
                            s[0] = stemmer.stem(s[0]);
                            //System.out.println(" entry 2. "+s[0]);
//							}
//							else{//split on blank space of token
//
//							}
                        }

                    }

                }

//				System.out.println("3 Time Taken (FINAL ENDED):"+ NANOSECONDS.toSeconds(System.nanoTime() - t10)+ " seconds; "
//									+ (NANOSECONDS.toSeconds(System.nanoTime() - t10)) / 60+ " minutes");
//				t10 = System.nanoTime();
                //destination
                if (s[1] != null) {
                    if (s[1].indexOf(" ") == -1) {
                        //s[1]=stemmer.stem(s[1]);
                        //NLP tag present
                        if (s[1].indexOf("_") >= 0) {

//							  ///word /token
//							curr_token=stemmer.stem(s[1].substring(0,s[1].indexOf("_")));
//
//							//	nlp tag
//							if(s[1].indexOf("_")>=0)
//								curr_tag=s[1].substring(s[1].indexOf("_"), s[1].length());
//							else
//								curr_tag="";
//							//reassign
//							s[1]=curr_token+curr_tag;

                            //s[1]=stemmer.stem(map_TokenWithTag_TokenWithOutTag.get(s[1]));

                            if (is_remove_NLPtag_in_graph_output) {
                                // NOT split on blank space of token
//								if(is_split_on_blank_space_among_tokens==false){
                                s[1] = stemmer.stem(map_TokenWithTag_TokenWithOutTag.get(s[1]));
//								}
//								else{//split on blank space of token
//
//								}
                            } else {
                                // NOT split on blank space of token
//								if(is_split_on_blank_space_among_tokens==false){
                                if (map_TokenWithOutTag_TokenWithTag.containsKey(s[1]))
                                    s[1] = stemmer.stem(map_TokenWithOutTag_TokenWithTag.get(s[1]));
//								}
//								else{//split on blank space of token
//
//								}
                            }

                            //System.out.println(" entry 3. "+s[1]);
                        } else {
                            // NOT split on blank space of token
//							if(is_split_on_blank_space_among_tokens==false){
                            // no NLP tag
                            s[1] = stemmer.stem(s[1]);
                            //System.out.println(" entry 4. "+s[1]);
//							}
//							else{
//								s[1]=stemmer.stem(s[1]);
//								String [] s1=s[1].split(" ");
//								int c=0;
//								//
//								while(c<s1.length){
//									 //s1[c];
//
//									c++;
//								}
//
//							}
                        }
                        //reassign
                        //s[1]=curr_token + curr_tag;
                    }
                }

//				System.out.println("4 Time Taken (FINAL ENDED):"+ NANOSECONDS.toSeconds(System.nanoTime() - t10)+ " seconds; "
//									+ (NANOSECONDS.toSeconds(System.nanoTime() - t10)) / 60+ " minutes");
//				t10 = System.nanoTime();

                if (s.length >= 3) {
                    String edge = map_NodeName_uniqID.get(s[0]) + " " + map_NodeName_uniqID.get(s[1]) + " \"" + s[2] + "\"";

                    if (map_NodeName_uniqID.get(s[0]) == null && map_NodeName_uniqID.get(stemmer.stem(s[0])) == null) {
                        int new_uniqID = map_NodeName_uniqID.size() + 1;
                        map_NodeName_uniqID.put(stemmer.stem(s[0]), new_uniqID);
                        //writer_debug.append("\n new s[0]:"+stemmer.stem( s[0])+" "+ new_uniqID);
                    }
                    if (map_NodeName_uniqID.get(s[1]) == null && map_NodeName_uniqID.get(stemmer.stem(s[1])) == null) {
                        int new_uniqID = map_NodeName_uniqID.size() + 1;
                        map_NodeName_uniqID.put(stemmer.stem(s[1]), new_uniqID);
                        //writer_debug.append("\n new s[1]:"+stemmer.stem( s[1])+" "+ new_uniqID);
                    }
                    if (map_NodeName_uniqID.get(s[2]) == null && map_NodeName_uniqID.get(stemmer.stem(s[2])) == null) {
                        int new_uniqID = map_NodeName_uniqID.size() + 1;
                        map_NodeName_uniqID.put(stemmer.stem(s[2]), new_uniqID);
                        //writer_debug.append("\n new s[2]:"+stemmer.stem( s[2])+" "+ new_uniqID);
                    }
                    writer_debug.flush();

                    String edge2 = map_NodeName_uniqID.get(stemmer.stem(s[0])) + " " + map_NodeName_uniqID.get(stemmer.stem(s[1])) + " \"" + stemmer.stem(s[2]) + "\"";
                    // checksum
                    String chk = Calc_checksum.calc_checksum_for_string(edge);
                    String chk2 = Calc_checksum.calc_checksum_for_string(edge2);
                    //map_isAlready_wrote_edge.put(chk, "");

//					System.out.println("5 Time Taken (FINAL ENDED):"+ NANOSECONDS.toSeconds(System.nanoTime() - t10)+ " seconds; "
//										+ (NANOSECONDS.toSeconds(System.nanoTime() - t10)) / 60+ " minutes");
//					t10 = System.nanoTime();

                    // not wrote before edge
                    if (!map_isAlready_wrote_edge.containsKey(chk) && !map_isAlready_wrote_edge.containsKey(chk2) &&
                            (edge.indexOf("null") == -1 || edge2.indexOf("null") == -1)) {
                        //
                        if (edge.indexOf("null") == -1) {
                            writer_gbad.append("d " + edge + "\n");
                            map_isAlready_wrote_edge.put(chk, "");
                        } else if (edge2.indexOf("null") == -1) {
                            chk = Calc_checksum.calc_checksum_for_string(edge2);
                            writer_gbad.append("d " + edge2 + "\n");
                            map_isAlready_wrote_edge.put(chk, "");
                        } else {

                            writer_debug.append("\n ERROR: EDGE IS NULL.." + "<-->source:" + s[0] + "<-->dest:" + s[1] + "<-->edge:" + s[2]
                                    + " " + map_NodeName_uniqID.get(stemmer.stem(s[0])) + " " + map_NodeName_uniqID.get(stemmer.stem(s[1]))
                                    + " " + map_NodeName_uniqID.get(stemmer.stem(s[2]))
                                    + " -->edge -->" + edge
                                    + " -->edge2-->" + edge2
                            );
                            writer_debug.flush();
                        }

                        writer_gbad.flush();

                        int curr_label_int_only = 0;
//					System.out.println("6 Time Taken (FINAL ENDED):"+ NANOSECONDS.toSeconds(System.nanoTime() - t10)+ " seconds; "
//									+ (NANOSECONDS.toSeconds(System.nanoTime() - t10)) / 60+ " minutes");
//					t10 = System.nanoTime();
                        //
                        if (IsNumeric.isNumeric(s[2])) {
                            if (s[2].indexOf(".") > 0)
                                curr_label_int_only = Integer.valueOf(s[2].substring(0, s[2].indexOf("."))); // remove .0 in the float number
                            else
                                curr_label_int_only = Integer.valueOf(s[2]);
                        } else {
                            //System.out.println("NOT numeric : "+s[2]);

                            curr_label_int_only = 0;
                        }

//					System.out.println("size = "+s.length+" "+s[0]+" "+s[1]+" "+s[2]
//								+ map_NodeName_uniqID.containsKey(s[0])+" "+ map_NodeName_uniqID.containsKey(s[1]));

                        // is_split_on_blank_space_among_tokens == false
//					if(is_split_on_blank_space_among_tokens==false){
                        //
                        if (map_NodeName_uniqID.containsKey(s[0]) && map_NodeName_uniqID.containsKey(s[1])) {
                            //d3js
                            d3JS_nodeEdgeString = d3JS_nodeEdgeString + "{source:" + (Integer.valueOf(map_NodeName_uniqID.get(s[0])) - 1) + "," +
                                    " target:" + (Integer.valueOf(map_NodeName_uniqID.get(s[1])) - 1) + "," +
                                    " label: " + curr_label_int_only
                                    + "},";
                        } else {
                            //						if(map_NodeName_uniqID.containsKey(s[0]))
                            //								System.out.println("WARNING : not found s[0]:"+s[0]);
                            //						if(map_NodeName_uniqID.containsKey(s[1]))
                            //							System.out.println("WARNING : not found s[1]:"+s[1]);

                            //continue;
                        }

//						System.out.println("7 Time Taken (FINAL ENDED):"+ NANOSECONDS.toSeconds(System.nanoTime() - t10)+ " seconds; "
//								+ (NANOSECONDS.toSeconds(System.nanoTime() - t10)) / 60+ " minutes");

//					}
//					//is_split_on_blank_space_among_tokens == true
//					else{
//						String []s1=s[0].split(" ");
//						String []s2=s[1].split(" ");
//						int c1=0; int c2=0;
//						//
//						while(c1<s1.length){
//							while(c2<s2.length){
//								//d3js
//								d3JS_nodeEdgeString=d3JS_nodeEdgeString+"{source:"+ (Integer.valueOf(map_NodeName_uniqID.get(s[c1]))-1)+","+
//																	    " target:"+(Integer.valueOf(map_NodeName_uniqID.get(s[c2]))-1)+ ","+
//																	    " label: "+curr_label_int_only
//																	    + "},";
//
//								c2++;
//							}
//							c1++;
//						}
//					}

                        if (String.valueOf(curr_label_int_only) == null) {
                            mapOut.put(-9, "\n  WARNING: (1) Edge label is NULL");
                        }

                    }
                } else {
                    writer_debug.append("ERROR (min.tokens=3)->" + t + "\n");
                    writer_debug.flush();
                }

                curr_source_config_mapping = "";
                curr_dest_config_mapping = "";
                curr_edge_config_mapping = "";


//				System.out.println("10 Time Taken (FINAL ENDED):"+ NANOSECONDS.toSeconds(System.nanoTime() - t10)+ " seconds; "
//										+ (NANOSECONDS.toSeconds(System.nanoTime() - t10)) / 60+ " minutes");


//				if(conc_Id_Seq.length()==0) conc_Id_Seq= id_seq;
//				else conc_Id_Seq= id_seq+"\n"+conc_Id_Seq;

                writer_last_RAN_seq_ID.append(id_seq + "\n");
                writer_last_RAN_seq_ID.flush();

            } //END for(int id_seq:map_idSeq_nodePairNedge.keySet()){
            //remove last commas
            d3JS_nodeEdgeString = d3JS_nodeEdgeString.substring(0, d3JS_nodeEdgeString.length() - 1);
            d3JS_nodeEdgeString = d3JS_nodeEdgeString + "]};";

            if (isskip_d3js_creation == false) {
                //d3js create
                if (xp_number == 1) {
                    //
                    Create_d3js.create_d3js_file1(d3JS_nodeEdgeString,
                            out_gbad_File + "d3js.html",
                            "comment" //comment_to_add_2_html
                    );
                }
            }


            //DEBUG
            if (is_append_out_gbad_File) {
                writer_debug.append("\n map_NodeName_uniqID:" + map_NodeName_uniqID);
                writer_debug.flush();
            }
            writer_gbad.flush();
            writer_gbad.close();
            writer_debug.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("~~~~ If NOT NULL, close writer_gbad:" + writer_gbad);
            System.out.println("~~~~ If NOT NULL, close writer_debug:" + writer_debug);
            if (writer_gbad != null) {
                try {
                    writer_gbad.close();
                } catch (Exception e) {
                    System.out.println("ERROR clsing writer_gbad ");
                }
            }
            if (writer_debug != null) {
                try {
                    writer_debug.close();
                } catch (Exception e) {
                    System.out.println("ERROR clsing writer_debug ");
                }
            }


        }
        return mapOut;
    }

    // CSV stemming of verb-line tagged CSV string
    private static String do_stemming_verbtypeCSVstring_of_applicable_curr_nodepair_edge(String curr_nodepair_edge,
                                                                                         String sourceDestEdge_from_input_mapping,
                                                                                         String sourceDestEdge_ORIGINAL_from_input_mapping,
                                                                                         boolean isSOPprint
    ) {
        String outString = "";
        Stemmer stemmer = new Stemmer();
        try {
            // TODO Auto-generated method stub
            String[] s = curr_nodepair_edge.split("!#!#");


            TreeMap<Integer, String> map_curr_token_semantic = new TreeMap<Integer, String>();
            String[] s1 = null;
            String[] s2 = null;
            String[] s3 = null;
            String[] arr_input_orig_mapping = sourceDestEdge_ORIGINAL_from_input_mapping.split(" ");
            String s1_concLine = "", s2_concLine = "", s3_concLine = "";
            String curr_token = "", curr_tag = "";
            //SOURCE
            if (arr_input_orig_mapping[0].indexOf(".stemming()") >= 0) {
                if (isSOPprint) {
                    System.out.println("%%%% SOURCE given string length:nodepair,edge-> " + s[0] + " " + s[1] + " " + s[2]);
                }
                s1 = s[0].split("!!!"); //source
                int c21 = 0;
                // split token and NLP tag
                if (s1[c21].indexOf("_") >= 0) {
                    // token (word)
                    if (s1[c21].indexOf("_") >= 0)
                        curr_token = stemmer.stem(s1[c21].substring(0, s1[c21].indexOf("_")));
                    else // not tagged token or word given
                        curr_token = stemmer.stem(s1[c21]);


                    //	nlp tag
                    if (s1[c21].indexOf("_") >= 0)
                        curr_tag = s1[c21].substring(s1[c21].indexOf("_"), s1[c21].length());
                    else
                        curr_tag = "";
                }

                //
                while (c21 < s1.length) {

                    if (s1_concLine.length() == 0) {
                        s1_concLine = stemmer.stem(curr_token) + curr_tag;
                    } else {
                        s1_concLine = s1_concLine + "!!!" + stemmer.stem(curr_token) + curr_tag;
                    }
                    c21++;
                }
            } else {
                // no splitting required
                s1_concLine = s[0];
            }


            String[] s2_new = null;

            //DESTINATION
            if (arr_input_orig_mapping[1].indexOf(".stemming()") >= 0) {
                if (isSOPprint) {
                    System.out.println("%%%% DESTINATION given string length:nodepair,edge-> " + s[0] + " " + s[1] + " " + s[2]);
                }
                s2 = s[1].split("!!!"); //dest
                int c22 = 0;

                //
                while (c22 < s2.length) {

                    // split token and NLP tag
                    if (s2[c22].indexOf("_") >= 0) {
                        // token (word)
                        if (s2[c22].indexOf("_") >= 0)
                            curr_token = stemmer.stem(s2[c22].substring(0, s2[c22].indexOf("_")));
                        else // not tagged token or word given
                            curr_token = stemmer.stem(s2[c22]);


                        //	nlp tag
                        if (s2[c22].indexOf("_") >= 0)
                            curr_tag = s2[c22].substring(s2[c22].indexOf("_"), s2[c22].length());
                        else
                            curr_tag = "";
                    }

                    //
                    if (s2_concLine.length() == 0) {
                        s2_concLine = stemmer.stem(curr_token) + curr_tag;
                    } else {
                        s2_concLine = s2_concLine + "!!!" + stemmer.stem(curr_token) + curr_tag;
                    }

                    c22++;
                }
            } else {
                s2_concLine = s[1];
            }

            // EDGE
            if (arr_input_orig_mapping[2].indexOf(".stemming()") >= 0) {
                if (isSOPprint) {
                    System.out.println("%%%% EDGE given string length:nodepair,edge-> " + s[0] + " " + s[1] + " " + s[2]);
                }

                s3 = s[2].split("!!!"); //edge
                int c23 = 0;

                //each tagged token
                while (c23 < s3.length) {
                    // token (word)
                    if (s3[c23].indexOf("_") >= 0)
                        curr_token = s3[c23].substring(0, s3[c23].indexOf("_"));
                    else // not tagged token or word given
                        curr_token = s3[c23];
                    //	nlp tag
                    if (s3[c23].indexOf("_") >= 0)
                        curr_tag = s3[c23].substring(s3[c23].indexOf("_"), s3[c23].length());
                    else
                        curr_tag = "";

                    if (isSOPprint)
                        System.out.println("token, tag->" + curr_token + "<-->" + curr_tag + "\n from s3[c23]:" + s3[c23]);

                    if (s3_concLine.length() == 0) {
                        s3_concLine = stemmer.stem(curr_token) + curr_tag;
                    } else {
                        s3_concLine = s3_concLine + "!!!" + stemmer.stem(curr_token) + curr_tag;
                    }
                    c23++;
                }
            } else {
                s3_concLine = s[2];
            }

            outString = s1_concLine + "!#!#" + s2_concLine + "!#!#" + s3_concLine;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outString;
    }

    // split_to_atom
    private static TreeMap<Integer, String> split_to_atom(String curr_nodepair_edge,
                                                          String sourceDestEdge_from_input_mapping,
                                                          String sourceDestEdge_ORIGINAL_from_input_mapping,
                                                          boolean isSOPprint,
                                                          TreeMap<String, TreeMap<Integer, String>> map_token_semantic_equiv,
                                                          FileWriter writer_outFile_repository_to_store_token_semantic_Pair,
                                                          boolean is_write_to_repository_if_token_semantic_Pair,
                                                          String debug_File
    ) {
        TreeMap<Integer, String> mapOut_as_VALUE = new TreeMap<Integer, String>();
        Stemmer stemmer = new Stemmer();
        TreeMap<String, TreeMap<Integer, String>> map_giveToken_corresponding_Semantics = new TreeMap<String, TreeMap<Integer, String>>();
        FileWriter writer_debug = null;
        int stat_hit_on_repo_semantic = 0;
        // TODO Auto-generated method stub
        try {
            writer_debug = new FileWriter(new File(debug_File), true);
            String[] s = curr_nodepair_edge.split("!#!#");
            if (isSOPprint) {
                System.out.println("given string length:nodepair,edge-> " + s[0] + " " + s[1] + " " + s[2]);
            }
            TreeMap<Integer, String> map_curr_token_semantic = new TreeMap<Integer, String>();
            String[] s1 = null;
            String[] s2 = null;
            String[] s3 = null;
            String[] arr_input_orig_mapping = sourceDestEdge_ORIGINAL_from_input_mapping.split(" ");
            String token_semantics = "";
            //SOURCE
            if (arr_input_orig_mapping[0].indexOf(".atom()") >= 0) {
                //System.out.println("SOURCE ATOM");

                s1 = s[0].split("!!!"); //source
                int c21 = 0;
                //
                while (c21 < s1.length) {
                    //
                    if (map_token_semantic_equiv.containsKey(s1[c21])) {
                        map_curr_token_semantic = map_token_semantic_equiv.get(s1[c21]);
                        stat_hit_on_repo_semantic++;

                    } else {
                        //get semantic expansion (related words) of NLP tagged or untagged token (word)
                        map_curr_token_semantic = get_semantics_for_a_word_FOR_SynsetType(
                                s1[c21], //NLP tagged or untagged token (word)
                                arr_input_orig_mapping,
                                0, //index_for_in_arr_input_orig_mapping
                                isSOPprint,
                                writer_outFile_repository_to_store_token_semantic_Pair,
                                is_write_to_repository_if_token_semantic_Pair,
                                "VERB"
                        );
                    }
                    //
                    for (int id_seq : map_curr_token_semantic.keySet()) {
                        if (isSOPprint)
                            System.out.println("c21: " + map_curr_token_semantic.get(id_seq));

                        map_giveToken_corresponding_Semantics.put(s1[c21], map_curr_token_semantic);
                    }

                    c21++;
                }
            } else {
                s1 = s[0].split("!!!^^^^^@"); //dummy splitter
            }

            String[] s2_new = null;
            map_curr_token_semantic = new TreeMap<Integer, String>();
            map_giveToken_corresponding_Semantics = new TreeMap<String, TreeMap<Integer, String>>();
            //DESTINATION
            if (arr_input_orig_mapping[1].indexOf(".atom()") >= 0) {
                if (isSOPprint)
                    System.out.println("DESTINATION ATOM");

                s2 = s[1].split("!!!"); //dest
                int c22 = 0;
                //
                while (c22 < s2.length) {
                    //
                    //System.out.println("s2[c22]:"+s2[c22]+"<->"+map_token_semantic_equiv.size());

                    if (map_token_semantic_equiv.containsKey(s2[c22])) {
                        map_curr_token_semantic = map_token_semantic_equiv.get(s2[c22]);
                        //System.out.println("contains s2[c22]:"+s2[c22]);

//						writer_debug.append("\n DEST curr token, loaded semantic equiv map_curr_token_semantic.size:"+map_curr_token_semantic.size()
//											+" for "+s2[c22]+" MAP:"+map_curr_token_semantic );
                        writer_debug.flush();
                        stat_hit_on_repo_semantic++;
                    } else {
                        //get semantic expansion (related words) of NLP tagged or untagged token (word)
                        map_curr_token_semantic = get_semantics_for_a_word_FOR_SynsetType(
                                s2[c22],
                                arr_input_orig_mapping,
                                1, //index_for_in_arr_input_orig_mapping
                                isSOPprint,
                                writer_outFile_repository_to_store_token_semantic_Pair,
                                is_write_to_repository_if_token_semantic_Pair,
                                "VERB"
                        );
                    }

                    if (isSOPprint) {
                        for (int id_seq : map_curr_token_semantic.keySet()) {
                            System.out.println("c22: " + map_curr_token_semantic.get(id_seq));
                        }
                    }
                    //
                    map_giveToken_corresponding_Semantics.put(s2[c22], map_curr_token_semantic);
                    c22++;
                }
            } else {
                s2 = s[1].split("!!!^^^^^@"); //dummy splitter
            }

            map_curr_token_semantic = new TreeMap<Integer, String>();
            map_giveToken_corresponding_Semantics = new TreeMap<String, TreeMap<Integer, String>>();
            // EDGE
            if (arr_input_orig_mapping[2].indexOf(".atom()") >= 0) {
                if (isSOPprint)
                    System.out.println("EDGE ATOM");

                s3 = s[2].split("!!!"); //edge
                int c23 = 0;
                //each tagged token
                while (c23 < s3.length) {
                    //
                    if (map_token_semantic_equiv.containsKey(s3[c23])) {
                        map_curr_token_semantic = map_token_semantic_equiv.get(s3[c23]);
                        stat_hit_on_repo_semantic++;
                    } else {
                        //get semantic expansion (related words) of NLP tagged or untagged token (word)
                        map_curr_token_semantic = get_semantics_for_a_word_FOR_SynsetType
                                (s3[c23], arr_input_orig_mapping,
                                        2, //index_for_in_arr_input_orig_mapping
                                        isSOPprint,
                                        writer_outFile_repository_to_store_token_semantic_Pair,
                                        is_write_to_repository_if_token_semantic_Pair,
                                        "VERB"
                                );

                    }
                    map_giveToken_corresponding_Semantics.put(s3[c23], map_curr_token_semantic);
                    c23++;
                }

            } else {
                s3 = s[2].split("!!!^^^^^@"); //edge
            }

            int cnt1 = 0, cnt2 = 0, cnt3 = 0;
            int cnt1_new = 1000, cnt2_new = 2000, cnt3_new = 3000;

            //do reverse from s3 to s1 (edge to first node)
            for (int i = 0; i < s3.length; i++) {
                if (s3[i].length() == 0) continue;
                for (int j = 0; j < s2.length; j++) {
                    if (s2[j].length() == 0) continue;
                    for (int j2 = 0; j2 < s1.length; j2++) {
                        if (s1[j2].length() == 0) continue;


                        if (isSOPprint)
                            System.out.println("atom: " + s1[j2] + "!#!#" + s2[j] + "!#!#" + s3[i]
                                    + "!#!#" + map_giveToken_corresponding_Semantics.containsKey(s1[j2])
                                    + "!#!#" + map_giveToken_corresponding_Semantics.containsKey(s2[j])
                                    + "!#!#" + map_giveToken_corresponding_Semantics.containsKey(s3[i])
                            );

                        mapOut_as_VALUE.put(cnt1, s1[j2] + "!#!#" + s2[j] + "!#!#" + s3[i]);

                        //SEMANTIC: does any of s1[] or s2[] or s3[] has its corresponding semantic expansions
                        if (map_giveToken_corresponding_Semantics.containsKey(s1[j2])) {
                            TreeMap<Integer, String> map_semantic_equiv = map_giveToken_corresponding_Semantics.get(s1[j2]);

                            for (int id_seq : map_semantic_equiv.keySet()) {
                                cnt1_new++;
                                mapOut_as_VALUE.put(cnt1_new, map_semantic_equiv.get(id_seq) + "!#!#" + s2[j] + "!#!#" + s3[i]);
                            }
                        }
                        //
                        if (map_giveToken_corresponding_Semantics.containsKey(s2[j])) {
                            if (isSOPprint)
                                System.out.println("found token: " + s2[j] + " inside=>" + map_giveToken_corresponding_Semantics);

                            TreeMap<Integer, String> map_semantic_equiv = map_giveToken_corresponding_Semantics.get(s2[j]);
                            writer_debug.append("\n split_atom() map_semantic_equiv.size:" + map_semantic_equiv.size());
                            writer_debug.flush();
                            for (int id_seq : map_semantic_equiv.keySet()) {
                                cnt2_new++;

                                if (isSOPprint)
                                    System.out.println("n-p,ege->" + s1[j2] + "!#!#" + map_semantic_equiv.get(id_seq) + "!#!#" + s3[i]);

                                mapOut_as_VALUE.put(cnt2_new, s1[j2] + "!#!#" + map_semantic_equiv.get(id_seq) + "!#!#" + s3[i]);
                            }
                        }

                        if (map_giveToken_corresponding_Semantics.containsKey(s3[i])) {
                            TreeMap<Integer, String> map_semantic_equiv = map_giveToken_corresponding_Semantics.get(s3[i]);
                            for (int id_seq : map_semantic_equiv.keySet()) {
                                cnt3_new++;
                                mapOut_as_VALUE.put(cnt3_new, s1[j2] + "!#!#" + s2[j] + "!#!#" + map_semantic_equiv.get(id_seq));
                            }
                        }

                        cnt1++;
                    }
                    cnt2++;
                }
                cnt3++;
            }

            writer_debug.close();

            mapOut_as_VALUE.put(-9, String.valueOf(stat_hit_on_repo_semantic));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer_debug != null)
                try {
                    writer_debug.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (writer_outFile_repository_to_store_token_semantic_Pair != null)
                try {
                    writer_outFile_repository_to_store_token_semantic_Pair.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return mapOut_as_VALUE;
    }

    // construct (has to debug a method**)
    public static TreeMap<Integer, String> convert_readCSVFileforConvertForGBADFn(
																		            String inFile,
																		            int startLineNo,
																		            int endLineNo,
																		            String delimiter,
																		            boolean is_header_present
    																			) {
        TreeMap<Integer, String> mapLines = new TreeMap<Integer, String>();
        try {

            mapLines = LoadGoogleAlert_FileToMap.loadEachGoogleAlertLineToMap(	inFile,
                    															delimiter,
                    															is_header_present
            																	);
        } catch (Exception e) {
            System.out.println("convert_readCSVFileforConvertForGBADFn():error 102");
            e.printStackTrace();
        }
        return mapLines;
    }

    //sentiment
    //writer_global_repository_to_save_sentenceWise_sentiments<- this file stores the all processed "sentence_checksum!!!score"
    public static float sentiment(String curr_sentence_untaggedTEXT,
                                  FileWriter writer_global_repository_to_save_sentenceWise_sentiments,
                                  boolean is_write_to_repository) {

        float score = 0.f;
        try {
            //return find_sentiments_of_a_sentence.find_sentiments_classify1(curr_sentence_untaggedTEXT); // openNLP
            score = Find_sentiment_stanford_OpenNLP.find_sentiment_stanford_OpenNLP_returnScore(curr_sentence_untaggedTEXT);

            if (is_write_to_repository) {
                writer_global_repository_to_save_sentenceWise_sentiments.append("\n" +
                        Calc_checksum.calc_checksum_for_string(curr_sentence_untaggedTEXT) + "!!!" + score + "!!!"
                        + curr_sentence_untaggedTEXT + "!!!"
                        + Clean_retain_only_alpha_numeric_characters.
                        clean_retain_only_alpha_numeric_characters(curr_sentence_untaggedTEXT, true)//convert to lowercase
                );
                writer_global_repository_to_save_sentenceWise_sentiments.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return score;
    }

    // Parse hetero input stream
    public static TreeMap<Integer, String> parseHeteroInputStream(String inFile, String config) {
        TreeMap<Integer, String> mapOut = new TreeMap<Integer, String>();
        TreeMap<String, String> mapConfig = ReadConfig.readConfig(config, "=");
        //Sources=S1->XML,S2->XML,S3->CSV,S4->HTML,S5->TEXT
        try {

        } catch (Exception e) {
            System.out.println("parseHeteroInputStream():error 101");
            e.printStackTrace();
        }
        return mapOut;
    }

    //example:
    //	XP # 1
    //	v 1 "n omirou st  "
    //	v 2 "n carretera st  "
    //	v 3 "n finiatur st  "
    //	d 1 2 "times"
    //create GBAD input graph File
    @SuppressWarnings("null")
    public static void createCSVtoGBADgraph(String baseFolder,
                                            TreeMap<Integer, String> mapSource,
                                            TreeMap<Integer, String> mapDest,
                                            TreeMap<Integer, String> mapEdgeLabel,
                                            TreeMap<Integer, String> mapDistinct_NewVertexID_OldVertexID,
                                            TreeMap<Integer, String> mapEdgeLabel_4_ChainNode,
                                            TreeMap<Integer, String> map_dest2_4_ChainNode,
                                            TreeMap<Integer, String> mapID,
                                            TreeMap<Integer, String> mapSTART,
                                            TreeMap<Integer, String> mapEND,
                                            TreeMap<Integer, String> mapOTHER,
                                            TreeMap<Integer,TreeMap<Integer,TreeMap<String, TreeMap<Integer, String>>>> mapOUTPUT_lineNumber_ID_N_Source_EdgeLabel_Destination,
                                            String graphtopology_STRING_for_DEBUG,
                                            String inputDataFileToCreateGBADGraph_OR_a_folder,
                                            String delimiter_4_inputDataFileToCreateGBADGraph,
                                            FileWriter outWriter,
                                            String outFile_of_outWriter,
                                            int startLineNo,
                                            int endLineNo,
                                            boolean is_create_each_inFile_as_an_XP_in_graph,
                                            boolean is_header_present,
                                            boolean isSOPprint,
                                            boolean is_skip_d3js_fileCreation,
                                            boolean isDebugMore
    ) {

//    	TreeMap<String, TreeMap<Integer, String>> mapOut_ID_N_Source_EdgeLabel_Destination_MAIN=mapOUTPUT_ID_N_Source_EdgeLabel_Destination.get(1); //set_1 (chain of nodes)
//        TreeMap<String, TreeMap<Integer, String>> mapOut_ID_N_Source_EdgeLabel_Destination_ZERO=mapOUTPUT_ID_N_Source_EdgeLabel_Destination.get(2);//set_2 (inner branch on top of chain of nodes)
//        TreeMap<String, TreeMap<Integer, String>> mapOut_ID_N_Source_EdgeLabel_Destination_ONE=mapOUTPUT_ID_N_Source_EdgeLabel_Destination.get(3);//set_2 (inner branch on top of chain of nodes)
        
        FileWriter debugwriter = null;
        String outFile_of_outWriter_CLEAN = outFile_of_outWriter + "_CLEAN.g";
        String prefix_for_NODEname = "o.conf.idx:";
        boolean is_skip_edge_having_missingVALUE_as_na = true;
        TreeMap<Integer, String> mapDistinct_NewVertexID_OldVertexID_ORIGINAL=mapDistinct_NewVertexID_OldVertexID;
        // 
        try {
            debugwriter = new FileWriter(new File(baseFolder + "debug.txt"));
            System.out.println("createGBADinputGraphFile():");
            TreeMap<Integer, TreeMap<Integer, String>> attributefunctionMaps= new TreeMap<Integer, TreeMap<Integer, String>>();
            //
            debugwriter.append("mapSource:" + mapSource + "\n");
            debugwriter.append("mapDest:" + mapDest + "\n");
            debugwriter.append("mapEdgeLabel:" + mapEdgeLabel + "\n");
            debugwriter.append("mapDistinct_NewVertexID_OldVertexID:" + mapDistinct_NewVertexID_OldVertexID + "\n");
            debugwriter.flush();

            // when there is chain of nodes to be created as per one primary key (ID), then only
            // preprocess and get appropriate data..

//			for(int id:mapSource.keySet()){
//
//			}
            /********* start sample  ****/
//			System.out.println("XP #");
//			//vertex creation (sample from config)
//			for(int i:mapDistinct_NewVertexID_OldVertexID.keySet()){
//				System.out.println("v "+  i
//									+" \"o.conf.idx:"+mapDistinct_NewVertexID_OldVertexID.get(i)+"\"");
//			}
//			// edge creation  (sample from config)
//			for(int i:mapSource.keySet()){
//				String newSourceVertexID=
//							getKeyByValueInTreeMap.getKeyByValueInTreeMap(mapSource.get(i),
//																		  mapDistinct_NewVertexID_OldVertexID,
//																		  false);
//				String newDistVertexID=
//						 	getKeyByValueInTreeMap.getKeyByValueInTreeMap(mapDest.get(i),
//																		  mapDistinct_NewVertexID_OldVertexID,
//																		  false);
//				System.out.println("d "+newSourceVertexID+" "+newDistVertexID
//									+" \""+  mapEdgeLabel.get(i)   + "\"");
//			}
            /********* end sample  ****/
            TreeMap<Integer, String> mapLines = new TreeMap<Integer, String>();
            // this is a folder or a file
            File inFile = new File(inputDataFileToCreateGBADGraph_OR_a_folder);
            boolean is_inputFile_a_folder = false;

            if (inFile.isDirectory())
                is_inputFile_a_folder = true;

            String[] arr_list_of_files = new String[1];

            // given is a FILE
            if (is_inputFile_a_folder == false) {
                arr_list_of_files[0] = inputDataFileToCreateGBADGraph_OR_a_folder;
                System.out.println("Given File is file = " + arr_list_of_files[0]);
            } else {
                //
                arr_list_of_files = inFile.list();
                System.out.println("GIVEN FILE IS A FOLDER:" + inputDataFileToCreateGBADGraph_OR_a_folder);
            }
            System.out.println("GIVEN is_create_each_inFile_as_an_XP_in_graph=" + is_create_each_inFile_as_an_XP_in_graph);
            //
            debugwriter.append("\n is_inputFile_a_folder:" + is_inputFile_a_folder + "\n arr_list_of_files.size:" + arr_list_of_files.length);
            debugwriter.append("\n LOADED total lines:mapLines.size:" + mapLines.size() + "\n");
            debugwriter.flush();

            String line = "";
            int lineNo = 0;
            int total_lineNo = mapLines.size();
            String[] tokens = new String[1];
            String[] currOldVertexIDarrayOfDotSplit = new String[1];
            String header = "";
            int siz_of_files_in_folder = Array.getLength(arr_list_of_files);
            int cnt200 = 0;
            String curr_file = "";
            int xp_cnt = 0;
            String arrOutFiles[] = new String[1];
            int countOutFile = 0;

            // 1 file = 1 XP
            if (is_create_each_inFile_as_an_XP_in_graph == true) {

                // iterate each file
                while (cnt200 < siz_of_files_in_folder) {
                    if (cnt200 % 100 == 0)
                        System.out.println("ENTERING ..." + cnt200);
                    //
                    String file_name_construct = String.format("%010d", cnt200) + ".txt";

                    //curr_file=arr_list_of_files[cnt200];
                    curr_file = inputDataFileToCreateGBADGraph_OR_a_folder + file_name_construct;
                    ///
                    if ((new File(curr_file).exists())) {
                        //
                        debugwriter.append("exists: processing file : " + inputDataFileToCreateGBADGraph_OR_a_folder + file_name_construct + "\n");
                        debugwriter.flush();
                    } else {
                        debugwriter.append("NOT_exists: processing file : " + inputDataFileToCreateGBADGraph_OR_a_folder + file_name_construct + "\n");
                        debugwriter.flush();
                        if (cnt200 % 200 == 0)
                            System.out.println("CONTINUE..2");
                        cnt200++;
                        continue;
                    }

                    System.out.println("....Processing:curr_file->" + curr_file
                            + " has lines->" + mapLines.size());

                    // read each line, each line becomes a "XP # <number>"
                    //THIS METHOD (1) replace , with " " (2) then, replace " " with " AND " *****" AND " is the delimiter
                    mapLines = convert_readCSVFileforConvertForGBADFn(
										                            curr_file,
										                            startLineNo,
										                            endLineNo,
										                            delimiter_4_inputDataFileToCreateGBADGraph,
										                            is_header_present
                    												);
                    TreeMap<Integer, String> mapVERTEXUnique = new TreeMap();
                    TreeMap<String, String> map_already_EDGEpairNlabel_AS_KEY = new TreeMap();
                    String concOldVertexIDtokens = "";
                    int int_NewVertexIndex = 0;
                    int NewVertexIndex = 0;
                    int oldNewVertexIndex = 0;

                    xp_cnt++;
                    outWriter.append("XP # " + xp_cnt + "\n");
                    outWriter.flush();
                    // each Line
                    for (int i : mapLines.keySet()) {
                        if (lineNo == 0) {
                            header = mapLines.get(i);
                            lineNo++;
                            System.out.println("CONTINUE...");
                            continue;
                        } else {
                            lineNo++;
                        }

                        System.out.println("lineNo:" + lineNo + " out of total_lineNo=" + total_lineNo);
                        //
                        line = mapLines.get(i);
                        tokens = line.split(" AND "); //*****" AND " is the delimiter
                        if (isSOPprint)
                            System.out.println("tokens.len=" + tokens.length + " line:" + line);
                        String currArrayOldVertexID = "";
                        concOldVertexIDtokens = "";
                        //mapUnique= new TreeMap();
                        //each token of new vertexID
                        for (int h : mapDistinct_NewVertexID_OldVertexID.keySet()) {
                            concOldVertexIDtokens = "";
                            int cnt = 0;
                            if (mapDistinct_NewVertexID_OldVertexID.get(h).indexOf("[") >= 0) {

                                if (isDebugMore)
                                    debugwriter.append("\n found [ ");
                                debugwriter.flush();
                                //
                                String NewVertexIndex2 = GetKeyByValueInTreeMap.getKeyByValueInTreeMap(mapDistinct_NewVertexID_OldVertexID.get(h),
                                        mapDistinct_NewVertexID_OldVertexID,
                                        false);

                                //
                                String t = "";
                                String value = "";
                                if (mapDistinct_NewVertexID_OldVertexID.get(h).indexOf("[") == -1) {
                                    t = ":val=" + tokens[cnt];
                                } else {
                                    t = "";
                                }

                                value = " \"o.conf.idx:" + mapDistinct_NewVertexID_OldVertexID.get(h);

                                String nodeName = value + t + "\"";
                                //add missing " at end
//							if(value.indexOf("]\"")==-1 && value.indexOf("]")>=0)
//								value=value+"\"";
//							value=value.replace("\"\"", "\"");

                                //
                                if (!mapVERTEXUnique.containsValue(nodeName)) {

                                    int_NewVertexIndex++;
                                    //CONVERT 2 String
                                    NewVertexIndex2 = String.valueOf(int_NewVertexIndex);
                                    // manipulate missing " at end
                                    //value=value+"\"";value=value.replace("\"\"", "\"");

                                    mapVERTEXUnique.put(h, nodeName);
                                    //						System.out.println("v "+  NewVertexIndex
                                    //											   +" \"o.conf.idx:"+mapDistinct_NewVertexID_OldVertexID.get(h)
                                    //											   +t +""
                                    //											   +"\"");

                                    //if(t.equalsIgnoreCase("")){  t=t+"\"";} //add "
                                    outWriter.append("v " + int_NewVertexIndex
                                            + nodeName
                                            + "\n");
                                }
                                outWriter.flush();
                                continue;
                            } //end if(mapDistinct_NewVertexID_OldVertexID.get(h).indexOf("[")>=0){
                            int oldVertexID = 0;
                            String[] arrayoldVertexID = null;
                            String[] arrayListOldVertexID = new String[1];
                            String t = null;
                            //
                            if (mapDistinct_NewVertexID_OldVertexID.get(h).indexOf(",") >= 0) {
                                arrayListOldVertexID = mapDistinct_NewVertexID_OldVertexID.get(h)
                                        .replace(",", ",").split(",");
                                if (isSOPprint)
                                    System.out.println(". AND:" + arrayListOldVertexID.length
                                            + " mapDistinct_NewVertexID_OldVertexID.get(h):" + mapDistinct_NewVertexID_OldVertexID.get(h));
                            } else {
                                t = mapDistinct_NewVertexID_OldVertexID.get(h);
                                arrayListOldVertexID[0] = t;
                                if (isSOPprint)
                                    System.out.println("1.NO AND:" + mapDistinct_NewVertexID_OldVertexID.get(h));
                            }
                            //					System.out.println("arraylistoldVertexID.len:"+arraylistoldVertexID.length
                            //											+"\n# mapDistinct_NewVertexID_OldVertexID:"+mapDistinct_NewVertexID_OldVertexID
                            //											+"\n# arraylistoldVertexID[0]:"+t
                            //											+"\n# h:"+h);
                            //String arrayoldVertexID2=arraylistoldVertexID[0];
                            try {
                                // list of old Vertex ID given with AND
                                //arrayoldVertexID=mapDistinct_NewVertexID_OldVertexID.get(h).split("AND");
                            } catch (Exception e) {
                                System.out.println("h:" + h);
                                e.printStackTrace();
                            }

                            if (isDebugMore)
                                debugwriter.append("\narrayListOldVertexID:" + Arrays.toString(arrayListOldVertexID));
                            debugwriter.flush();

                            int cnt3 = 0;
                            int cnt2 = 0;
                            //int NewVertexIndex=0; int oldNewVertexIndex=0;
                            String oldconcOldVertexIDtokens = "";
                            //read each of arrayListOldVertexID after split on AND
                            while (cnt2 < arrayListOldVertexID.length) {
                                currArrayOldVertexID = arrayListOldVertexID[cnt2];
                                if (isSOPprint)
                                    System.out.println("currArrayOldVertexID->" + currArrayOldVertexID + "<-");

                                //NewVertexIndex=h;
                                // split based on . for currArrayOldVertexID
                                String[] currDotSplitArray = currArrayOldVertexID.replace(".", ",").split(",");

                                if (isDebugMore)
                                    debugwriter.append("\n 1.currArrayOldVertexID:" + currArrayOldVertexID + " NewVertexIndex:" + NewVertexIndex
                                            + " currDotSplitArray:" + Arrays.toString(currDotSplitArray));

                                // NO "." (DOT) PRESENT
                                if (currDotSplitArray.length == 0) {
                                    currDotSplitArray[0] = currArrayOldVertexID;
                                }

                                cnt3 = 0;
                                String finalconcOldVertexIDtokens = "";
                                // currArrayOldVertexID has split of AND
                                while (cnt3 < currDotSplitArray.length) {
                                    if (currArrayOldVertexID.indexOf(".") >= 0) {
                                        currOldVertexIDarrayOfDotSplit =
                                                currArrayOldVertexID.replace(".", ",").split(",");
                                    } else {
                                        currOldVertexIDarrayOfDotSplit = new String[1];
                                        currOldVertexIDarrayOfDotSplit[0] = currArrayOldVertexID;
                                    }
                                    //											System.out.println("currOldVertexIDarrayOfDotSplit.length:"+
                                    //																currOldVertexIDarrayOfDotSplit.length+":"
                                    //																+currOldVertexIDarrayOfDotSplit[0]
                                    //																);
                                    int cnt4 = 0;
                                    if (isDebugMore)
                                        debugwriter.append("\n 2.currOldVertexIDarrayOfDotSplit:" + Arrays.toString(currOldVertexIDarrayOfDotSplit));

                                    // For each of Dot of currArrayOldVertexID
                                    while (cnt4 < currOldVertexIDarrayOfDotSplit.length) {
                                        String currcurrOldVertexIDarrayOfDotSplit = currOldVertexIDarrayOfDotSplit[cnt4];
                                        if (isDebugMore)
                                            debugwriter.append("\n 3.currcurrOldVertexIDarrayOfDotSplit:" + (currcurrOldVertexIDarrayOfDotSplit));
                                        if (IsInteger.isInteger(currcurrOldVertexIDarrayOfDotSplit)) {
                                            oldVertexID = new Integer(currcurrOldVertexIDarrayOfDotSplit);
                                            if (isDebugMore)
                                                debugwriter.append("\n 4.tokens:" + (tokens.length) + " oldVertexID:" + oldVertexID);
                                            if (oldVertexID + 1 <= tokens.length) {
                                                if (isSOPprint)
                                                    System.out.println("2.len=0 :lineNo:" + lineNo + ";oldVertexID:"
                                                            + oldVertexID + ";NewVertexIndex:" + NewVertexIndex
                                                            + ",tokens.len:" + tokens.length);

                                                concOldVertexIDtokens = tokens[oldVertexID - 1];
                                                if (isDebugMore)
                                                    debugwriter.append("\n 4.concOldVertexIDtokens:" + concOldVertexIDtokens + " oldVertexID:" + oldVertexID);
                                            }

                                        } else {
                                            //												System.out.println("not integer:"+currOldVertexIDarrayOfDotSplit[cnt4]
                                            //														+"\n"+
                                            //												convertPatternStringToMap.convertPatternSubStringString2StartEndIndexMap(currOldVertexIDarrayOfDotSplit[cnt4])
                                            //												);
                                            //currcurrOldVertexIDarrayOfDotSplit=currOldVertexIDarrayOfDotSplit[cnt4];
                                            TreeMap<Integer, Integer> mapSubString =
                                                    ConvertPatternStringToMap.convertPatternSubStringString2StartEndIndexMap(
                                                            currcurrOldVertexIDarrayOfDotSplit);
                                            //
                                            if (currcurrOldVertexIDarrayOfDotSplit.indexOf("substr") >= 0) {
                                                concOldVertexIDtokens = concOldVertexIDtokens.substring(
                                                        mapSubString.get(1), mapSubString.get(2));
                                            } else {
                                                concOldVertexIDtokens = concOldVertexIDtokens;
                                                //System.out.println("cnt4:"+cnt4);
                                            }
                                            //												System.out.println("concOldVertexIDtokens:"+concOldVertexIDtokens
                                            //														+" oldVertexID:"+oldVertexID
                                            //														+" currcurrOldVertexIDarrayOfDotSplit:"+currcurrOldVertexIDarrayOfDotSplit
                                            //														+" substr(a,b):"+"("+mapSubString.get(1)+","+ mapSubString.get(2)+")"
                                            //														+" cnt4:"+cnt4
                                            //														+" currOldVertexIDarrayOfDotSplit.length:"+currOldVertexIDarrayOfDotSplit.length
                                            //														+" currDotSplitArray.length:"+currDotSplitArray.length
                                            //														+" currArrayOldVertexID:"+currArrayOldVertexID);
                                        }
                                        cnt4++;
                                    }
                                    debugwriter.flush();
                                    //final concat
                                    if (finalconcOldVertexIDtokens.length() == 0) {
                                        finalconcOldVertexIDtokens = concOldVertexIDtokens;
                                    } else {
                                        finalconcOldVertexIDtokens = finalconcOldVertexIDtokens + "," + concOldVertexIDtokens;
                                    }
                                    cnt3++;
                                }
                                //									System.out.println("v "+  NewVertexIndex
                                //													+" \"o.conf.idx:"+oldVertexID +" ("+mapDistinct_NewVertexID_OldVertexID +")"
                                //													+":val="+concOldVertexIDtokens+"; f:"+finalconcOldVertexIDtokens
                                //													+"\"");

                                String nodeName = " \"o.conf.idx:" + oldVertexID + ":val=" + concOldVertexIDtokens + "\"";

                                // missing "
//										if(tmp.indexOf("]\"")==-1 && tmp.indexOf("]")>=0)
//											tmp=tmp+"\"";
//										tmp=tmp.replace("\"\"", "\"");

                                if (!mapVERTEXUnique.containsValue(nodeName)) {
                                    int_NewVertexIndex++;
                                    NewVertexIndex = int_NewVertexIndex; //CONVERT 2 String

                                    outWriter.append("v " + int_NewVertexIndex + nodeName + "\n");
                                    outWriter.flush();

                                    if (!mapVERTEXUnique.containsKey(NewVertexIndex)) {
                                        mapVERTEXUnique.put(NewVertexIndex,
                                                nodeName);
                                    } else {
                                        String lstoken = mapVERTEXUnique.get(NewVertexIndex);
                                        mapVERTEXUnique.put(NewVertexIndex,
                                                            lstoken + "," + nodeName);
                                    }
                                }


                                cnt2++;
                                oldNewVertexIndex = NewVertexIndex;
                                oldconcOldVertexIDtokens = concOldVertexIDtokens;
                            } // while(cnt<arrayListOldVertexID.length){
                        } //END for(int h:mapDistinct_NewVertexID_OldVertexID.keySet()){

                        String t = "";
                        String oldSourceVertexID = "";
                        String oldDistVertexID = "";

                        for (int i2 : mapSource.keySet()) {
                            if (isDebugMore)
                                debugwriter.append("\n---------------------------------------------------");
                            //	*********SOURCE BEGIN ***************** /
                            String source_Name = mapSource.get(i2);
                            //
                            oldSourceVertexID =
                                    GetKeyByValueInTreeMap.getKeyByValueInTreeMap(
                                                                                    source_Name,
                                                                                    mapDistinct_NewVertexID_OldVertexID, //mapDistinct_NewVertexID_OldVertexID,
                                                                           false);
                            int int_oldSourceVertexID = Integer.valueOf(oldSourceVertexID);
                            String newSourceVertexID = "";
                            //INTEGER (INDEX)
                            if (source_Name.indexOf("[") == -1) {
                                if (isSOPprint)
                                    System.out.println("tokens.len:" + tokens.length + " int_oldSourceVertexID:" + int_oldSourceVertexID + " source_Name:" + source_Name
                                            			+ " i2:" + i2);
                                //
                                if (IsInteger.isInteger(source_Name)) {
                                    int_oldSourceVertexID = Integer.valueOf(source_Name);
                                    t = " \"o.conf.idx:" + int_oldSourceVertexID + ":val=" + tokens[int_oldSourceVertexID - 1] + "\"";
                                    //IS ALREADY PRESENT
                                    newSourceVertexID = GetKeyByValueInTreeMap.getKeyByValueInTreeMap(
                                                                                                t,
                                                                                                mapVERTEXUnique, //mapDistinct_NewVertexID_OldVertexID,
                                                                                        false);
                                    if (isDebugMore)
                                        debugwriter.append("\n source entry 1 $$$$$$$ t:" + t);
                                }
                            } else if (source_Name.indexOf("[") >= 0) {
                                t = " \"o.conf.idx:" + source_Name + "\"";
                                //IS ALREADY PRESENT
                                newSourceVertexID = GetKeyByValueInTreeMap.getKeyByValueInTreeMap(
													                                        t,
													                                        mapVERTEXUnique, //mapDistinct_NewVertexID_OldVertexID,
													                                        false);
                                if (isDebugMore)
                                    debugwriter.append("\n source entry 2 $$$$$$$ t:" + t);
                            }


                            //	*********SOURCE END ***************** /

                            //	*********DESTINATION BEGIN ***************** /

                            String destination_Name = mapDest.get(i2);
                            String oldDestinationVertexID = "";
                            int int_oldDestinationVertexID = -1;

                            //if its INTEGER, IT is the token number of the destination token
                            if (IsInteger.isInteger(destination_Name)) {
                                oldDestinationVertexID = destination_Name;
                                int_oldDestinationVertexID = Integer.valueOf(oldDestinationVertexID);
                            } else {
                                //
                                oldDestinationVertexID = GetKeyByValueInTreeMap.getKeyByValueInTreeMap(
                                                                                                        destination_Name,
                                                                                                        mapVERTEXUnique, //mapDistinct_NewVertexID_OldVertexID,
                                                                                                        false);

                                if (isSOPprint)
                                    System.out.println("oldDestinationVertexID:" + oldDestinationVertexID + " destination_Name=" + destination_Name + " source_Name:" + source_Name);
                            }

                            String newDestinationVertexID = "";
                            //INTEGER (INDEX)
                            if (destination_Name.indexOf("[") == -1) {
                                if (isSOPprint)
                                    System.out.println("tokens.len:" + tokens.length + " int_oldDestinationVertexID:" + int_oldDestinationVertexID + " destination_Name:" + destination_Name
                                            + " i2:" + i2);
                                if (IsInteger.isInteger(destination_Name)) {
                                    int_oldDestinationVertexID = Integer.valueOf(destination_Name);
                                    t = " \"o.conf.idx:" + int_oldDestinationVertexID + ":val=" + tokens[int_oldDestinationVertexID - 1] + "\"";
                                    newDestinationVertexID = GetKeyByValueInTreeMap.getKeyByValueInTreeMap(
                                                                                                    t,
                                                                                                    mapVERTEXUnique, //mapDistinct_NewVertexID_OldVertexID,
                                                                                                    false);
                                    if (isDebugMore)
                                        debugwriter.append("\n destination entry 1 $$$$$$$ t:" + t);
                                }
                            } else if (destination_Name.indexOf("[") >= 0) {
                                t = " \"o.conf.idx:" + destination_Name + "\"";
                                //IS ALREADY PRESENT
                                newDestinationVertexID = GetKeyByValueInTreeMap.getKeyByValueInTreeMap(
                                                                                                    t,
                                                                                                    mapVERTEXUnique, //mapDistinct_NewVertexID_OldVertexID,
                                                                                                    false);

                                if (isDebugMore)
                                    debugwriter.append("\n destination entry 2 $$$$$$$ t:" + t);
                            }

                            //	*********DESTINATION END ***************** /


//						String newDistVertexID=
//								 	getKeyByValueInTreeMap.getKeyByValueInTreeMap(  mapDest.get(i2),
//								 												    mapDistinct_NewVertexID_OldVertexID,//mapDistinct_NewVertexID_OldVertexID,
//								 													false);


                            if (isSOPprint)
                                System.out.println("mapVERTEXUnique:" + mapVERTEXUnique + " \n source:" + mapSource.get(i2) + " dest:" + mapDest.get(i2)
			                                        + "\n mapDistinct_NewVertexID_OldVertexID:" + mapDistinct_NewVertexID_OldVertexID
			                                        + " source_:" + source_Name + " newSourceVertexID:" + oldSourceVertexID
			                                        + " newSourceVertexID:" + newSourceVertexID
			                                        + " t:" + t
			                                        + " 1:" + mapSource
			                                        + " 2:" + mapDest);
//						String newSourceVertexID=mapVERTEXUnique.get(mapSource.get(i2));
//						String newDistVertexID=mapVERTEXUnique.get(mapDest.get(i2));

                            //	*********EDGE BEGIN ***************** /

                            if (mapSource.get(i2).indexOf("[") >= 0 && mapDest.get(i2).indexOf("[") >= 0) {


                                String s_h = " \"" + "o.conf.idx:" + mapSource.get(i2) + "\"";
                                newSourceVertexID = GetKeyByValueInTreeMap.getKeyByValueInTreeMap(
														                                        s_h,
														                                        mapVERTEXUnique, //mapDistinct_NewVertexID_OldVertexID,
														                                        false);

                                String h = " \"" + "o.conf.idx:" + mapDest.get(i2) + "\"";
                                newDestinationVertexID = GetKeyByValueInTreeMap.getKeyByValueInTreeMap(
																                                        h,
																                                        mapVERTEXUnique, //mapDistinct_NewVertexID_OldVertexID,
																                                        false);

                                if (isDebugMore) {
                                    debugwriter.append("\n newSourceVertexID:" + newSourceVertexID + " newDestinationVertexID:" + newDestinationVertexID);
                                    debugwriter.append("\n s_h:" + s_h + " h:" + h + " mapVERTEXUnique:" + mapVERTEXUnique);
                                }
                                t = "d " + newSourceVertexID + " " + newDestinationVertexID + " \"" + mapEdgeLabel.get(i2) + "\"";

                                if (isDebugMore) {
                                    debugwriter.append(t + "\n");
                                }

                                //EDGE PAIR + LABEL NOT ALREADY WROTE
                                if (!map_already_EDGEpairNlabel_AS_KEY.containsKey(t)) {

                                    if (isSOPprint)
                                        System.out.println("1.edgeString:" + t);

                                    outWriter.append(t + "\n");
                                    map_already_EDGEpairNlabel_AS_KEY.put(t, "");
                                }
                                if (isDebugMore)
                                    debugwriter.append("\n edge entry 1 $$$$$$$ t:" + t);
                            } else if (!newSourceVertexID.equalsIgnoreCase(newDestinationVertexID)
                                //&& newSourceVertexID.length()>0 && newDestinationVertexID.length()>0
                                    ) {
                                t = "d " + newSourceVertexID + " " + newDestinationVertexID + " \"" + mapEdgeLabel.get(i2) + "\"";
                                //EDGE PAIR + LABEL - NOT ALREADY WROTE
                                if (!map_already_EDGEpairNlabel_AS_KEY.containsKey(t)) {
                                    if (isSOPprint)
                                        System.out.println("1.edgeString:" + t);
                                    outWriter.append(t + "\n");
                                    map_already_EDGEpairNlabel_AS_KEY.put(t, "");
                                }
                                if (isDebugMore)
                                    debugwriter.append("\n edge entry 2 $$$$$$$ t:" + t);
                            }
                            //
                            if (isDebugMore) {
                                debugwriter.append("\nsource:" + mapSource.get(i2) + " dest:" + mapDest.get(i2) +
                                        " sVertexid:" + newSourceVertexID + " dVertexid:" + newDestinationVertexID);
                                debugwriter.flush();
                            }

                            // ********EDGE creation  (sample from config)*****end


                            //	*********EDGE END ***************** /

//						boolean toCheckForInteger =isInteger.isInteger(mapEdgeLabel.get(i2).replace("(", "").replace(")", ""));
//						//
//						if(toCheckForInteger ){
//								t="u "+newSourceVertexID+" "+oldDistVertexID
//						   			+" \""+   tokens[2]    + "\"" ;
//
//							int i10=Integer.valueOf(mapEdgeLabel.get(i2).replace("(", "").replace(")", ""));
//							//System.out.println("token:"+ tokens[2] );
//							System.out.println(t);
//							outWriter.append(t +"\n");
//						}
//						else{
//								t="u "+newSourceVertexID+" "+oldDistVertexID
//									   +" \""+  mapEdgeLabel.get(i2)   + "\"";
//							System.out.println(t);
//							outWriter.append(t+"\n");
//						}
                            outWriter.flush();

                        }


                    }//END for(int i:mapLines.keySet() ){

                    System.out.println("cnt200 -- siz_of_files_in_folder:" + cnt200 + "--" + siz_of_files_in_folder);

                    cnt200++;
                } //END while(cnt200 < siz_of_files_in_folder){


                if (is_skip_d3js_fileCreation == false) {
                    // create d3js file for "XP # 1"
                    Create_d3js.
                            create_d3js_for_a_given_xp_number_of_GBAD_file(baseFolder,
										                                    outFile_of_outWriter,//GBAD OUTPUT FILE
										                                    1,
										                                    "comment:" + graphtopology_STRING_for_DEBUG + "(**header=" + header + ")", //comment_to_add_2_html
										                                    false
										                            		);
                }

                outWriter.close();
            } else {   // each line is created as an XP
                if (is_inputFile_a_folder == true)
                    arr_list_of_files = inFile.list(); // get ALL files from FOLDER

                System.out.println("ELSE...out file:" + outFile_of_outWriter + " inFile:" + inFile);
                debugwriter.append("\n ELSE...out file:" + outFile_of_outWriter + "\n inFile:" + inFile);
                debugwriter.flush();
                siz_of_files_in_folder = arr_list_of_files.length;
                cnt200 = 0; //
                // iterate each file
                while (cnt200 < siz_of_files_in_folder) {
                    System.out.println("ENTERING 2.2..." + cnt200);
//					  String file_name_construct=String.format("%010d", cnt200)+".txt";

                    if (is_inputFile_a_folder == true)
                        curr_file = inFile + "/" + arr_list_of_files[cnt200];
                    else
                        curr_file = inputDataFileToCreateGBADGraph_OR_a_folder;
//					    curr_file=inputDataFileToCreateGBADGraph_OR_a_folder+file_name_construct;

                    if (curr_file == null || curr_file.indexOf("Store") >= 0) {
                        System.out.println("curr_file IS NULL:" + curr_file);
                        debugwriter.append("\n --------Processing file(NULL):" + curr_file);
                        cnt200++;
                        continue;
                    }
                    System.out.println("------curr_file:" + curr_file + " siz_of_files_in_folder:" + siz_of_files_in_folder);
                    ///
                    if ((new File(curr_file).exists())) {
                        System.out.println("CONTINUE..2.1 -- EXISTS..");
//						debugwriter.append("exists: processing file : "+inputDataFileToCreateGBADGraph_OR_a_folder+file_name_construct+"\n");
                        debugwriter.flush();
                    } else {
//						  debugwriter.append("NOT_exists: processing file : "+inputDataFileToCreateGBADGraph_OR_a_folder+file_name_construct
//								  						+" file:"+curr_file+"\n");
                        debugwriter.flush();
                        System.out.println("CONTINUE..2.2 -- FILE NOT EXISTS->" + " file:" + curr_file);
                        debugwriter.append("\n --------Processing file(NOT EXIST):" + curr_file);
                        cnt200++;
                        continue;
                    }

                    System.out.println("....Processing:curr_file->" + curr_file + " has lines->" + mapLines.size());

                    debugwriter.append("\n....1.Processing:curr_file->" + curr_file);
                    debugwriter.flush();

                    String temp_outputFile_dot_g = curr_file.replace(".txt", "").replace(".csv", "") + ".g";
                    //overwrite for each out file (GBAD .g output file)
                    outWriter = new FileWriter(new File(temp_outputFile_dot_g));

                    arrOutFiles[countOutFile] = temp_outputFile_dot_g;

                    countOutFile++;

                    // read each line, each line becomes a "XP # <number>"
                    //THIS METHOD (1) replace , with " " (2) then, replace " " with " AND " *****" AND " is the delimiter
                    mapLines = convert_readCSVFileforConvertForGBADFn(
                                                                        curr_file,
                                                                        startLineNo,
                                                                        endLineNo,
                                                                        delimiter_4_inputDataFileToCreateGBADGraph,
                                                                        is_header_present
                                                                    );

                    debugwriter.append("\n --------Processing file:" + curr_file + " loaded lines:" + mapLines.size());
                    debugwriter.append("\n    output file:" + temp_outputFile_dot_g);
                    debugwriter.flush();
                    System.out.println("---ENTER each line is an XP mapLines.size:" + mapLines.size() + " from file=" + curr_file);
                    debugwriter.append("\n---ENTER each line is an XP mapLines.size:" + mapLines.size() + " from file=" + curr_file);
                    debugwriter.flush();
                    int ID_column=-1; TreeMap<Integer, Integer> map_UniqueLineNumber_as_KEY=new TreeMap<Integer, Integer>();
                    // if ID (PRIMARY KEY) exists, then there is a chain of node exists w.r.T THIS ID (primary key)
                    // the targeted chain of node is suppose assumed to be in order with respect to either ID or some other DATETIME column
                    //get ID value 
                    for(int lineNumber:mapID.keySet()){
                    	ID_column=Integer.valueOf(mapID.get(lineNumber));
                    	map_UniqueLineNumber_as_KEY.put(lineNumber, -1);
                    }

                    lineNo = 0; //reset
                    String curr_ID=""; String last_ID="";
                    // each Line = 1 XP (a "XP # <number>")
                    for (int i : mapLines.keySet()) {
                        
                        line = mapLines.get(i);
                        tokens = line.split(" AND "); //*****" AND " is the delimiter
                        //RETRY: if token length=1...wrong delimiter. .try with !!!
                        if (tokens.length == 1)
                            tokens = line.split("!!!"); //***** "!!!" is the delimiter
                        //RETRY: if token length=1...wrong delimiter. .try with ,
                        if (tokens.length == 1)
                            tokens = line.split(","); //*****"," is the delimiter
                        
                        mapDistinct_NewVertexID_OldVertexID=new TreeMap<Integer, String>();
                        for(int k:mapDistinct_NewVertexID_OldVertexID_ORIGINAL.keySet())
                        	mapDistinct_NewVertexID_OldVertexID.put(k, mapDistinct_NewVertexID_OldVertexID_ORIGINAL.get(k));
                        //
                        if(ID_column>=0){
                        	curr_ID=tokens[ID_column-1]; // get primary key
                        	if(last_ID.length()>0 && curr_ID.equalsIgnoreCase(last_ID)) //each ID will have only one line processed, rest ignore
                        		continue;
                        	// for the only line per ID, get it inserted into "mapDistinct_NewVertexID_OldVertexID"
                        	// now whatever I am putting into "mapDistinct_NewVertexID_OldVertexID" is specific to ID, so I have to reset "mapDistinct_NewVertexID_OldVertexID"
                        	// for each line
                        	 
                        	// there can be multiple sub-graph (source and chain of destinations) == multiple lineNumbers
                        	for(int currLineNumber:mapOUTPUT_lineNumber_ID_N_Source_EdgeLabel_Destination.keySet()){
                        		TreeMap<Integer, TreeMap<String, TreeMap<Integer, String>>> map_tmp= 
                        															mapOUTPUT_lineNumber_ID_N_Source_EdgeLabel_Destination.get(currLineNumber);
                        			// SET 1
                        			TreeMap<String, TreeMap<Integer, String>> map_set_1=map_tmp.get(1);
                        			// SET 2 (PART I)
                        			TreeMap<String, TreeMap<Integer, String>> map_set_2_part1=map_tmp.get(2);
                        			// SET 2 (PART II)
                        			TreeMap<String, TreeMap<Integer, String>> map_set_2_part2=map_tmp.get(3);
                        			// 
                        			TreeMap<Integer, String> map_seq_N_set1_for_currID = map_set_1.get(curr_ID);
                        			TreeMap<Integer, String> map_seq_N_set2_part1_for_currID = map_set_2_part1.get(curr_ID);
                        			TreeMap<Integer, String> map_seq_N_set2_part2_for_currID = map_set_2_part2.get(curr_ID);
                        			// set 1 - create nodes (newVertexID)
                        			for(int seq_set1:map_seq_N_set1_for_currID.keySet()){
                        				String [] arr_source_edgeLabel_dest=map_seq_N_set1_for_currID.get(seq_set1).split("!!!");
                        				String source=arr_source_edgeLabel_dest[0];String dest=arr_source_edgeLabel_dest[2];
//                        				System.out.println("newly.mapDistinct_NewVertexID_OldVertexID:"+mapDistinct_NewVertexID_OldVertexID);
                        				// source
	        							if(!mapDistinct_NewVertexID_OldVertexID.containsValue(source)){
	        								if(source!=null){
		        								int sz=mapDistinct_NewVertexID_OldVertexID.size()+1;
		        								mapDistinct_NewVertexID_OldVertexID.put(sz,source);
	        								}
	        							}
	        							// dest
	        							if(!mapDistinct_NewVertexID_OldVertexID.containsValue(dest)){
	        								if(dest!=null){
		        								int sz=mapDistinct_NewVertexID_OldVertexID.size()+1;
		        								mapDistinct_NewVertexID_OldVertexID.put(sz, dest);
	        								}
	        							}
                        			}
                        			// set 2 (part 1) - create nodes (newVertexID)
                        			for(int seq_set2_part1:map_seq_N_set2_part1_for_currID.keySet()){
                        				String [] arr_source_edgeLabel_dest=map_seq_N_set2_part1_for_currID.get(seq_set2_part1).split("!!!");
                        				String source=arr_source_edgeLabel_dest[0];String dest=arr_source_edgeLabel_dest[2];
//                        				System.out.println("newly.mapDistinct_NewVertexID_OldVertexID:"+mapDistinct_NewVertexID_OldVertexID);
                        				// source
	        							if(!mapDistinct_NewVertexID_OldVertexID.containsValue(source)){
	        								if(source!=null){
		        								int sz=mapDistinct_NewVertexID_OldVertexID.size()+1;
		        								mapDistinct_NewVertexID_OldVertexID.put(sz,source);
	        								}
	        							}
	        							// dest
	        							if(!mapDistinct_NewVertexID_OldVertexID.containsValue(dest)){
	        								if(dest!=null){
	        									int sz=mapDistinct_NewVertexID_OldVertexID.size()+1;
	        									mapDistinct_NewVertexID_OldVertexID.put(sz, dest);
	        								}
	        							}
                        			}
                        			// set 2 (part 2) - create nodes (newVertexID)
                        			for(int seq_set2_part2:map_seq_N_set2_part2_for_currID.keySet()){
                        				String [] arr_source_edgeLabel_dest=map_seq_N_set2_part2_for_currID.get(seq_set2_part2).split("!!!");
                        				String source=arr_source_edgeLabel_dest[0];String dest=arr_source_edgeLabel_dest[2];
//                        				System.out.println("newly.mapDistinct_NewVertexID_OldVertexID:"+mapDistinct_NewVertexID_OldVertexID);
                        				// source
	        							if(!mapDistinct_NewVertexID_OldVertexID.containsValue(source)){
	        								if(source!=null){
		        								int sz=mapDistinct_NewVertexID_OldVertexID.size()+1;
		        								mapDistinct_NewVertexID_OldVertexID.put(sz,source);
	        								}
	        							}
	        							// dest
	        							if(!mapDistinct_NewVertexID_OldVertexID.containsValue(dest)){
	        								if(dest!=null){
		        								int sz=mapDistinct_NewVertexID_OldVertexID.size()+1;
		        								mapDistinct_NewVertexID_OldVertexID.put(sz, dest);
	        								}
	        							}
                        			}
                        		}
                        }
                        
                        //
                        if (is_header_present && i == 1) {
                            continue;
                        } else {
                            lineNo++;
                        }
                        if(isSOPprint)
                        	System.out.println("----------------ID_column:"+ID_column+" last_ID:"+last_ID+" curr_ID:"+curr_ID);                        	
                        System.out.println("XP # " + (lineNo));
                        outWriter.append("XP # " + (lineNo) + "\n"); //i replaced by lineNo 9/6
                        outWriter.flush();
 
                        if (lineNo < 100)
                            System.out.println("token:" + tokens.length + " tokens[2]:" + tokens[2] + " line:" + line);
                        String currArrayOldVertexID = "";
                        String concOldVertexIDtokens = "";
                        TreeMap<Integer, String> mapUnique = new TreeMap();
                        int old_vertexID=-1;
                         
                        // each token of new vertexID
                        for (int h : mapDistinct_NewVertexID_OldVertexID.keySet()) {
                            concOldVertexIDtokens = "";
                            int cnt = 0;
                            
                            boolean toCheckForInteger = IsInteger.isInteger(mapDistinct_NewVertexID_OldVertexID.get(h));
                            if(toCheckForInteger)
                                old_vertexID=Integer.valueOf(mapDistinct_NewVertexID_OldVertexID.get(h));
                            // *************** hard-coded vertex ... example: [START] **********************
                            if (mapDistinct_NewVertexID_OldVertexID.get(h).indexOf("[") >= 0 || toCheckForInteger==false) {
                                //
                                String NewVertexIndex = GetKeyByValueInTreeMap.getKeyByValueInTreeMap
                                                                                            (mapDistinct_NewVertexID_OldVertexID.get(h),
                                                                                             mapDistinct_NewVertexID_OldVertexID,
                                                                                            false);
                                String t = "";
                                if ( toCheckForInteger==false) {
                                	t = ":val="+ mapDistinct_NewVertexID_OldVertexID.get(h);
                                } else {
                                	t = ":val=" + tokens[old_vertexID-1]; // tokens[cnt];
                                }
                                String nodeName = "\"o.conf.idx:" + mapDistinct_NewVertexID_OldVertexID.get(h) + t;
                                 
                                //tail " missing
                                if (nodeName.indexOf("[") >= 0 && nodeName.indexOf("]\"") == -1)
                                    nodeName = nodeName + "\"";
                                if(!nodeName.substring(nodeName.length(), nodeName.length()).equalsIgnoreCase("\"")) nodeName = nodeName  + "\"";
                                nodeName=nodeName.replace("\"\"", "\"");

                                mapUnique.put(h, " \"o.conf.idx:" + mapDistinct_NewVertexID_OldVertexID.get(h));

                                //						System.out.println("v "+  NewVertexIndex
                                //											   +" \"o.conf.idx:"+mapDistinct_NewVertexID_OldVertexID.get(h)
                                //											   +t +""
                                //											   +"\"");
                                outWriter.append("v " + NewVertexIndex
                                                + " " + nodeName
                                                + "\n");
                                outWriter.flush();
                                continue;
                            }
                            int oldVertexID = 0;
                            String[] arrayoldVertexID = null;
                            String[] arrayListOldVertexID = new String[1];
                            String t = null;
                            //************** Single vertex ID (OR) list of vertex ID with "," as delimiter **************
                            if (mapDistinct_NewVertexID_OldVertexID.get(h).indexOf(",") >= 0) {
                                arrayListOldVertexID = mapDistinct_NewVertexID_OldVertexID.get(h)
                                                                                          .replace(",", ",").split(",");
                                if (isSOPprint)
                                    System.out.println(". AND:" + arrayListOldVertexID.length
                                                        + " mapDistinct_NewVertexID_OldVertexID.get(h):" + mapDistinct_NewVertexID_OldVertexID.get(h));
                            } else {
                                t = mapDistinct_NewVertexID_OldVertexID.get(h);
                                arrayListOldVertexID[0] = t;
                                if (isSOPprint)
                                    System.out.println("2.NO AND:" + mapDistinct_NewVertexID_OldVertexID.get(h) + "<-> t:" + t);
                            }
                            //					System.out.println("arraylistoldVertexID.len:"+arraylistoldVertexID.length
                            //											+"\n# mapDistinct_NewVertexID_OldVertexID:"+mapDistinct_NewVertexID_OldVertexID
                            //											+"\n# arraylistoldVertexID[0]:"+t
                            //											+"\n# h:"+h);
                            //String arrayoldVertexID2=arraylistoldVertexID[0];
                            try {
                                // list of old Vertex ID given with AND
                                //arrayoldVertexID=mapDistinct_NewVertexID_OldVertexID.get(h).split("AND");
                            } catch (Exception e) {
                                System.out.println("h:" + h);
                                e.printStackTrace();
                            }
                            int cnt3 = 0;
                            int cnt2 = 0;
                            int NewVertexIndex = 0;
                            int oldNewVertexIndex = 0;
                            String oldconcOldVertexIDtokens = "";
                            //******** read each of arrayListOldVertexID after split on AND ********
                            while (cnt2 < arrayListOldVertexID.length) {
                                currArrayOldVertexID = arrayListOldVertexID[cnt2];
                                if (isSOPprint)
                                    System.out.println("currArrayOldVertexID->" + currArrayOldVertexID + "<-" + concOldVertexIDtokens);
                                NewVertexIndex = h;
                                // split based on . for currArrayOldVertexID
                                String[] currDotSplitArray = currArrayOldVertexID.replace(".", ",").split(",");

                                cnt3 = 0;
                                String finalconcOldVertexIDtokens = "";
                                // currArrayOldVertexID has split of AND
                                while (cnt3 < currDotSplitArray.length) {
                                    if (currArrayOldVertexID.indexOf(".") >= 0) {
                                        currOldVertexIDarrayOfDotSplit =
                                                currArrayOldVertexID.replace(".", ",").split(",");
                                    } else {
                                        currOldVertexIDarrayOfDotSplit = new String[1];
                                        currOldVertexIDarrayOfDotSplit[0] = currArrayOldVertexID;
                                    }
                                    //											System.out.println("currOldVertexIDarrayOfDotSplit.length:"+
                                    //																currOldVertexIDarrayOfDotSplit.length+":"
                                    //																+currOldVertexIDarrayOfDotSplit[0]
                                    //																);
                                    int cnt4 = 0;
                                    // For each of Dot of currArrayOldVertexID
                                    while (cnt4 < currOldVertexIDarrayOfDotSplit.length) {
                                        String currcurrOldVertexIDarrayOfDotSplit = currOldVertexIDarrayOfDotSplit[cnt4];
                                        if (IsInteger.isInteger(currcurrOldVertexIDarrayOfDotSplit)) {
                                            oldVertexID = new Integer(currcurrOldVertexIDarrayOfDotSplit);


                                            if (oldVertexID <= tokens.length) {
                                                if (isSOPprint)
                                                    System.out.println("1.len=0 :lineNo:" + lineNo + ";oldVertexID:"
			                                                            + oldVertexID + ";NewVertexIndex:" + NewVertexIndex
			                                                            + ",tokens.len:" + tokens.length + " oldVertexID-1:" + (oldVertexID - 1)
			                                                            + " cnt4:" + cnt4
			                                                            + " array.len:" + currOldVertexIDarrayOfDotSplit.length);

                                                concOldVertexIDtokens = tokens[oldVertexID - 1];

                                            }

                                        } else {
                                            //												System.out.println("not integer:"+currOldVertexIDarrayOfDotSplit[cnt4]
                                            //														+"\n"+
                                            //												convertPatternStringToMap.convertPatternSubStringString2StartEndIndexMap(currOldVertexIDarrayOfDotSplit[cnt4])
                                            //												);
                                            //currcurrOldVertexIDarrayOfDotSplit=currOldVertexIDarrayOfDotSplit[cnt4];
                                            TreeMap<Integer, Integer> mapSubString = new TreeMap<Integer,Integer>();

                                            if (currcurrOldVertexIDarrayOfDotSplit.indexOf("substr") >= 0) {
                                                //
                                                mapSubString = ConvertPatternStringToMap.convertPatternSubStringString2StartEndIndexMap
                                                        											(currcurrOldVertexIDarrayOfDotSplit);
                                                concOldVertexIDtokens = concOldVertexIDtokens.substring(
                                                        												mapSubString.get(1), mapSubString.get(2));
                                                System.out.println("cnt2: concOldVertexIDtokens:" + concOldVertexIDtokens);
                                            } else {
                                                concOldVertexIDtokens = concOldVertexIDtokens;
                                                System.out.println("cnt4:" + cnt4 + " ;concOldVertexIDtokens:" + concOldVertexIDtokens);
                                            }

                                            if (isSOPprint)
                                                System.out.println("concOldVertexIDtokens:" + concOldVertexIDtokens
			                                                        + " oldVertexID:" + oldVertexID
			                                                        + " currcurrOldVertexIDarrayOfDotSplit:" + currcurrOldVertexIDarrayOfDotSplit
			                                                        + " substr(a,b):" + "(" + mapSubString.get(1) + "," + mapSubString.get(2) + ")"
			                                                        + " cnt4:" + cnt4
			                                                        + " currOldVertexIDarrayOfDotSplit.length:" + currOldVertexIDarrayOfDotSplit.length
			                                                        + " currDotSplitArray.length:" + currDotSplitArray.length
			                                                        + " currArrayOldVertexID:" + currArrayOldVertexID);
                                        }
                                        cnt4++;
                                    }
                                    //final concat
                                    if (finalconcOldVertexIDtokens.length() == 0) {
                                        finalconcOldVertexIDtokens = concOldVertexIDtokens;
                                    } else {
                                        finalconcOldVertexIDtokens = finalconcOldVertexIDtokens + "," + concOldVertexIDtokens;
                                    }
                                    cnt3++;
                                }
                                if(isSOPprint)
	                                System.out.println("v " + NewVertexIndex
	                                                    + " \"o.conf.idx:" + oldVertexID + " (" + mapDistinct_NewVertexID_OldVertexID + ")"
	                                                    + ":val=" + concOldVertexIDtokens + "; f:" + finalconcOldVertexIDtokens +"(lineNo(file)="+i
	                                                    + "\"");
                                outWriter.append("v " + NewVertexIndex
                                                      + " \"o.conf.idx:" + oldVertexID
                                                      + ":val=" + concOldVertexIDtokens
                                                      + "\"" + "\n");
                                outWriter.flush();

                                if (!mapUnique.containsKey(NewVertexIndex)) {
                                    mapUnique.put(  NewVertexIndex,
                                                    concOldVertexIDtokens);
                                } else {
                                    String lstoken = mapUnique.get(NewVertexIndex);
                                    mapUnique.put(  NewVertexIndex,
                                                    lstoken + "," + concOldVertexIDtokens);
                                }

                                cnt2++;
                                oldNewVertexIndex = NewVertexIndex;
                                oldconcOldVertexIDtokens = concOldVertexIDtokens;
                            } // while(cnt<arrayListOldVertexID.length){
                        } // end of each of mapDistinct_NewVertexID_OldVertexID
                        int cnt10 = 1;
                        int max = mapUnique.size();
                        //System.out.println("cnt10:"+cnt10 +" max:"+max +" "+mapUnique);
                        // -------> DEBUG
                        for (int s : mapUnique.keySet()) { // <--------- DEBUG
                            //while(cnt10<=mapUnique.size()){
                            // FULL VALUE of token -> tokens[cnt]  (baseSOP)
                            if (isSOPprint)
                                System.out.println("v " + s
                                                    + " \"o.conf.idx: " + ":val=" + mapUnique.get(s)
                                                    + " ->"
                                                    + "\"");
                            cnt10++;
                        }
                        //---------------- EDGE creation  (sample from config)  ----------------
                        for (int i2 : mapSource.keySet()) {
                        	// skip - this is already built as set1 and set2 (part 1 and part 2) using "create_intermediate_file"
                        	if(map_UniqueLineNumber_as_KEY.containsKey(i2)){
                        		TreeMap<String, TreeMap<Integer, String>> map_set_2_part2=new TreeMap<String, TreeMap<Integer,String>>();
                        		TreeMap<String, TreeMap<Integer, String>> map_set_2_part1=new TreeMap<String, TreeMap<Integer,String>>();
                        		TreeMap<String, TreeMap<Integer, String>> map_set_1=new TreeMap<String, TreeMap<Integer,String>>();
                        		TreeMap<Integer, String> map_seq_N_set1_for_currID = new TreeMap<Integer, String>();
                        		TreeMap<Integer, String> map_seq_N_set2_part1_for_currID = new TreeMap<Integer, String>();
                        		TreeMap<Integer, String> map_seq_N_set2_part2_for_currID = new TreeMap<Integer, String>();
	                        		// build 
	                            	// there can be multiple sub-graph (source and chain of destinations) == multiple lineNumbers
	                            	for(int currLineNumber:mapOUTPUT_lineNumber_ID_N_Source_EdgeLabel_Destination.keySet()){
		                            		TreeMap<Integer, TreeMap<String, TreeMap<Integer, String>>> map_tmp= 
		                            															mapOUTPUT_lineNumber_ID_N_Source_EdgeLabel_Destination.get(currLineNumber);
		                            			// SET 1
		                            			if(map_tmp.containsKey(1))
		                            				map_set_1=map_tmp.get(1);
		                            			// SET 2 (PART I)
		                            			if(map_tmp.containsKey(2))
		                            				map_set_2_part1=map_tmp.get(2);
		                            			// SET 2 (PART II)
		                            			if(map_tmp.containsKey(3))
		                            					map_set_2_part2=map_tmp.get(3);
		                            			// 
		                            			System.out.println(" edge for dest2 create: curr_ID:"+curr_ID);
		                            			if(map_set_1.containsKey(curr_ID))
		                            				map_seq_N_set1_for_currID = map_set_1.get(curr_ID);
		                            			if(map_set_2_part1.containsKey(curr_ID))
		                            				map_seq_N_set2_part1_for_currID = map_set_2_part1.get(curr_ID);
		                            			if(map_set_2_part2.containsKey(curr_ID))
		                            				map_seq_N_set2_part2_for_currID = map_set_2_part2.get(curr_ID);
		                            			if(isSOPprint){
			                            			System.out.println("map_set_1:"+map_seq_N_set1_for_currID);System.out.println("map_set_2_part1:"+map_seq_N_set2_part1_for_currID);
			                            			System.out.println("map_set_2_part2:"+map_seq_N_set2_part1_for_currID);
		                            			}
		                            			// set 1 - create edges (newVertexID)
		                            			for(int seq_set1:map_seq_N_set1_for_currID.keySet()){
		                            				String [] arr_source_edgeLabel_dest=map_seq_N_set1_for_currID.get(seq_set1).split("!!!");
		                            				String source=arr_source_edgeLabel_dest[0];String dest=arr_source_edgeLabel_dest[2];String edgeLabel=arr_source_edgeLabel_dest[1];
		                            				if(isSOPprint){
			                            				System.out.println("newly.mapDistinct_NewVertexID_OldVertexID:"+mapDistinct_NewVertexID_OldVertexID);
			                            				System.out.println("mapDistinct_NewVertexID_OldVertexID_ORIGINAL:"+mapDistinct_NewVertexID_OldVertexID_ORIGINAL);
		                            				}
		                            				 
		                                            String newSourceVertexID =
		                                                    GetKeyByValueInTreeMap.getKeyByValueInTreeMap(
		                                                    											source,
		                                                                                                mapDistinct_NewVertexID_OldVertexID,
		                                                                                                false);
		                                            String newDistVertexID =
		                                                    GetKeyByValueInTreeMap.getKeyByValueInTreeMap(dest,
		                                                                                                  mapDistinct_NewVertexID_OldVertexID,
		                                                                                                  false);
		                                            outWriter.append("d " + newSourceVertexID + " " + newDistVertexID
	                                                          			+ " \"" + edgeLabel + "\"" + "\n");
		                            			}
		                            			// set 2 (part 1) - create edges (newVertexID)
		                            			if(map_seq_N_set2_part1_for_currID!=null){
		                            				if(map_seq_N_set2_part1_for_currID.size()>0){
				                            			for(int seq_set2_part1:map_seq_N_set2_part1_for_currID.keySet()){
				                            				String [] arr_source_edgeLabel_dest=map_seq_N_set2_part1_for_currID.get(seq_set2_part1).split("!!!");
				                            				String source=arr_source_edgeLabel_dest[0];String dest=arr_source_edgeLabel_dest[2];String edgeLabel=arr_source_edgeLabel_dest[1];
				                            				if(isSOPprint){
				                            					System.out.println("newly.mapDistinct_NewVertexID_OldVertexID:"+mapDistinct_NewVertexID_OldVertexID);
				                            				}
				                                            String newSourceVertexID =
				                                                    GetKeyByValueInTreeMap.getKeyByValueInTreeMap(
				                                                    											source,
				                                                                                                mapDistinct_NewVertexID_OldVertexID,
				                                                                                                false);
				                                            String newDistVertexID =
				                                                    GetKeyByValueInTreeMap.getKeyByValueInTreeMap(dest,
				                                                                                                  mapDistinct_NewVertexID_OldVertexID,
				                                                                                                  false);
				                                            outWriter.append("d " + newSourceVertexID + " " + newDistVertexID
			                                                          			+ " \"" + edgeLabel + "\"" + "\n");
				                            			}
		                            				}
		                            			}
		                            			// set 2 (part 2) - create edges (newVertexID)
		                            			if(map_seq_N_set2_part2_for_currID!=null){
		                            				if(map_seq_N_set2_part2_for_currID.size()>0){
			                            			for(int seq_set2_part2:map_seq_N_set2_part2_for_currID.keySet()){
			                            				String [] arr_source_edgeLabel_dest=map_seq_N_set2_part2_for_currID.get(seq_set2_part2).split("!!!");
			                            				String source=arr_source_edgeLabel_dest[0];String dest=arr_source_edgeLabel_dest[2];String edgeLabel=arr_source_edgeLabel_dest[1];
			                            				if(isSOPprint){
			                            					System.out.println("newly.mapDistinct_NewVertexID_OldVertexID:"+mapDistinct_NewVertexID_OldVertexID);
			                            				}
			                                            String newSourceVertexID =
			                                                    GetKeyByValueInTreeMap.getKeyByValueInTreeMap(
			                                                    											source,
			                                                                                                mapDistinct_NewVertexID_OldVertexID,
			                                                                                                false);
			                                            String newDistVertexID =
			                                                    GetKeyByValueInTreeMap.getKeyByValueInTreeMap(dest,
			                                                                                                  mapDistinct_NewVertexID_OldVertexID,
			                                                                                                  false);
			                                            outWriter.append("d " + newSourceVertexID + " " + newDistVertexID
		                                                          			+ " \"" + edgeLabel+ ""+ "\"" + "\n");
			                            			}
		                            				}
		                            			}
		                            		}
	                            	continue;    	 
                        	} ///if(map_UniqueLineNumber_as_KEY.containsKey(i2)){
                        	
                            String newSourceVertexID =
                                    GetKeyByValueInTreeMap.getKeyByValueInTreeMap(
                                                                                mapSource.get(i2),
                                                                                mapDistinct_NewVertexID_OldVertexID,
                                                                                false);

                            String newDistVertexID =
                                    GetKeyByValueInTreeMap.getKeyByValueInTreeMap(mapDest.get(i2),
                                                                                  mapDistinct_NewVertexID_OldVertexID,
                                                                                  false);
                            if(isSOPprint)
	                            System.out.println("new.mapDistinct_NewVertexID_OldVertexID:"+mapDistinct_NewVertexID_OldVertexID
	                            					+"\n mapDistinct_NewVertexID_OldVertexID_ORIGINAL"+mapDistinct_NewVertexID_OldVertexID_ORIGINAL
	                            					+"\n mapSource.get(i2):"+mapSource.get(i2)+" mapDest.get(i2):"+mapDest.get(i2)
	        										+" got_newSourceVertexID:"+newSourceVertexID+" newDistVertexID:"+newDistVertexID);
                            
                            // is Edge Label an integer (column index) or not?
                            boolean toCheckForInteger = IsInteger.isInteger(mapEdgeLabel.get(i2)
                                                                            .replace("(", "").replace(")", ""));

                            if (toCheckForInteger) {
                                int cnt_for_token = Integer.valueOf(mapEdgeLabel.get(i2).replace("(", "").replace(")", ""));
                                int i10 = Integer.valueOf(mapEdgeLabel.get(i2).replace("(", "").replace(")", ""));
                                //System.out.println("token:"+ tokens[2] );
							if(isSOPprint) //baseSOP
	                                System.out.println("1.d~~~>" + newSourceVertexID + "<==>" + newDistVertexID
	                                                    + "<==>\"" + tokens[cnt_for_token - 1] + "\"  given)cnt_for_token:"+cnt_for_token);
                                // missing value for a feature and mentioned as "na"
                                if (is_skip_edge_having_missingVALUE_as_na == true && tokens[cnt_for_token - 1].equalsIgnoreCase("na")) {
                                } else
                                    outWriter.append("d " + newSourceVertexID + " " + newDistVertexID
                                                          + " \"" + tokens[cnt_for_token - 1] + "\"" + "\n");
                            } else {

							if(isSOPprint) //baseSOP
                                System.out.println("2.d~~~>" + newSourceVertexID + " " + newDistVertexID
                                                          + " \"" + mapEdgeLabel.get(i2) + "\"");
                                outWriter.append("d " + newSourceVertexID + " " + newDistVertexID
                                                      + " \"" + mapEdgeLabel.get(i2) + "\"" + "\n");
                            }
                            outWriter.flush();

                        }//edge creation

                        outWriter.flush();
                        if(ID_column>=0) last_ID=tokens[ID_column-1]; // last ID (primary key)
                    }//end of each line
                    cnt200++;
                } // end while(cnt200 < siz_of_files_in_folder){

                //CLEAN GBAD*** - remove unwanted in NODE NAMES
                Clean_gbad.clean_GBAD_file(baseFolder, outFile_of_outWriter, outFile_of_outWriter_CLEAN);

                //UNCLEAN- create d3js file for "XP # 1"
                Create_d3js.create_d3js_for_a_given_xp_number_of_GBAD_file( baseFolder,
											                                outFile_of_outWriter,//GBAD OUTPUT FILE
											                                1,
											                                "comment:" + graphtopology_STRING_for_DEBUG + "(**header=" + header + ")", //comment_to_add_2_html
											                                false
											                        );

                //CLEAN- create d3js file for "XP # 1"
                Create_d3js.
                        create_d3js_for_a_given_xp_number_of_GBAD_file( baseFolder,
										                                outFile_of_outWriter_CLEAN,//GBAD OUTPUT FILE
										                                1,
										                                "comment:" + graphtopology_STRING_for_DEBUG + "(**header=" + header + ")", //comment_to_add_2_html
										                                false
										                        	 );

            } //ELSE END OF if(is_create_each_document_as_an_XP_in_graph==true){

            System.out.println("INPUT FILE--->" + inputDataFileToCreateGBADGraph_OR_a_folder);
            System.out.println("OUTPUT file--->" + outFile_of_outWriter+" warning: this is NOT always right output file");
            System.out.println("OUTPUT file--->" + Arrays.toString(arrOutFiles));
        } catch (Exception e) {
            System.out.println("error 10.2");
            e.printStackTrace();
        }
    }

    //read Crawled Files and Tag Sentences Using NLP
    //input Flag -> {"file", "mongodb"}  / output Flag -> {"mongodb"}
    private static void readCrawledFilesANDTagSentencesUsingNLP(
            String baseWorkFolder,
            String inCrawledFile,
            String outCrawledFile,
            String inFlag,
            String outFlag,
            String mongoDBname,
            String confFile,
            boolean isDebug) {

        inCrawledFile = baseWorkFolder + inCrawledFile;
        outCrawledFile = baseWorkFolder + outCrawledFile;
        // TODO Auto-generated method stub
        String folder = "/Users/lenin/Dropbox/NLP/models/";
        String str = "AN 18-year-old girl was shot dead by her brother in the Kahna area on Tuesday . "
                + "The deceased was identified Jameela, daughter of Abdul Rehman of Bohgal village Kahna ."
                + "Police said accused Zahid murdered his daughter on suspicion. The victim’s family claimed that Zahid "
                + "got infuriated when he was asked to complete his sister’s dowry as her marriage ceremony was due after six days . "
                + "On the day of the incident, he shot her dead when she was asleep. "
                + "The accused escaped from the scene. Police have registered a case and removed the body to morgue for autopsy. "
                + "People are very sad .";

//		str ="A student from Hyderabad has been shot dead by unidentified gunman in the United States, according to his family ."+
//			 "Sai Kiran Goud was killed by a group of unidentified men in Miami, Florida, on Sunday, it said ."+
//			 "The 21-year-old was apparently waylaid by a group of suspected robbers . When he refused to handover his iPhone, a gunman opened fire, killing him instantaneously ."+
//			 "Sai had gone to the US last month and was pursuing his masters in a university in Miami ."+
//			 "The news shocked Sai’s parents and other family members . A pall of gloom descended on the house of the youth in Kushaiguda area ."+
//			 "Sai’s family has appealed to the Telangana and the central governments to help bring the body home .";
//		str="According to news agency ANI , Sai Kiran , 23 , was killed in Florida after he refused to give his mobile phone ."
//			+"While speaking to the news agency, Sai Kiran's parents who are based in Hyderabad , said , Sai Kiran was shot dead by locals over a petty issue ."
//			+"Meanwhile, Kiran's uncle Shravan Kumar said, Four rounds were fired at Sai Kiran at around 12.15 pm today .";

        String[] s = new String[]{"lenin", "is", "a", "good", "person"};
        //String[] map =new String[1];
        int cnt = 0;
        folder = "/Users/lenin/Dropbox/NLP/models/";
        // new param added to getNLPTrained
        //TreeMap<String, String> mapOutOfNLP = Crawler.getNLPTrained(folder,"en-pos-maxent.bin",str, null);

        BufferedReader reader = null;
        TreeMap<String, String> mapLineNoNURL = new TreeMap<String, String>();
        try {
            // input Flag -> {"file", "mongodb"}
            if (inFlag.equalsIgnoreCase("file")
                    && outFlag.equalsIgnoreCase("mongodb")
                    ) {
//				getBufferedReader.getBufferedReader(inCrawledFile);
//				//
//				TreeMap<String, String> mapLineNoNURL=
//					Crawler.readNthTokenFromWithORWithoutGivenPatternForGivenFileandRemoveDuplicationWrite2File(
//																	inCrawledFile, 2, "!!!",
//																	"http", "www",
//																	"http://", "www.",
//																	outCrawledFile,
//																	baseWorkFolder+"debug.txt");
//				//
//				for(String ss:mapLineNoNURL.keySet()){ System.out.println(ss+" "+ mapLineNoNURL.get(ss)); }

                TreeMap<String, String> mapConfig = Crawler.getConfig(confFile);
                System.out.println("readNthTokenFromWithORWithoutGivenPatternForGivenFileandRemoveDuplicationWrite2File:"
                        + "	read URL map size. mapLineNoNURL :" + mapLineNoNURL.size());
                String inputFile = "/Users/lenin/Google Drive/uaw.test.input/CrawledOutHTML.txt";
                String outputFile = "/Users/lenin/Google Drive/uaw.test.input/CrawledOutHTMLCleaned.txt";
                String debugFile = "/Users/lenin/Google Drive/uaw.test.input/CrawledOutHTMLDebug.txt";
                String inputSingleURL = mapConfig.get("inputSingleURL");
                outputFile = mapConfig.get("outputFile");
                boolean isAppend = Boolean.valueOf(mapConfig.get("isAppend"));
                String delimiter = mapConfig.get("delimiter"); // "!!!"
                String inputType = mapConfig.get("inputType");  //{"file",""}  input is a file type (or) single url
                String outputType = mapConfig.get("outputType");// {"writeCrawledText2OutFile","mongodb","outNewUnCrawledURLFile"}
                String outAlreadyCrawledURLsINaFile = "//Users//" + "lenin" + "//Downloads//uaw.test.input//outDebugAlreadyCrawledURLsINaFile.txt";
                outAlreadyCrawledURLsINaFile = mapConfig.get("outAlreadyCrawledURLsINaFile"); //  "outAlreadyCrawledURLsINaFile.txt";
                String inputListOf_URLfileCSV = mapConfig.get("inputListOf_URLfileCSV");
                TreeMap<String, String> mapAlreadyCrawledURL = new TreeMap<String, String>();
                int fromLine = Integer.valueOf(mapConfig.get("fromLine"));
                int toLine = Integer.valueOf(mapConfig.get("toLine"));
                String outdebugErrorAndURLfile = mapConfig.get("outdebugErrorAndURLfile");
                String outNewUnCrawledURLFile = mapConfig.get("outNewUnCrawledURLFile");
                String parseType = mapConfig.get("parseType");

                //
//				crawler.getCrawler( folder,
//									inputListOf_URLfileCSV, // input file <email.name!!!URL!!!header text from email>   <- at least two column
//									inputSingleURL,
//									outputFile, isAppend, delimiter,
//									inputType, //{"file",""}  input is a file type (or) single url
//									outputType, // {"outfile","mongodb","outNewUnCrawledURLFile"}
//									outAlreadyCrawledURLsINaFile,
//									mapAlreadyCrawledURL,
//									fromLine,
//									toLine,
//									outNewUnCrawledURLFile, //for {outputType="outNewUnCrawledURLFile"}
//									outdebugErrorAndURLfile,
//									parseType);
            }

            // output Flag -> {"file", "mongodb"}
            if (
                    inFlag.equalsIgnoreCase("mongodb") &&
                            outFlag.equalsIgnoreCase("mongodb")) { //imp


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //convert given string to JSON
    public static JsonObject convert_givenString_to_JSON(String json_String) {
        //JSONParser parser = new JSONParser();
        System.out.println(json_String);
        //json = "{\"Success\":true,\"Message\":\"Invalid access token.\"}";
        JsonParser jsonParser = new JsonParser();
        JsonObject jo = null;
        try {
            jo = (JsonObject) jsonParser.parse(json_String);
        } catch (Exception e) {

        }
        return jo;
    }

    //convert NYT news article to GBAD graph
    private static void convertNYTnewsARTICLEtoGBADgraph(String inputFolderOfNYT,
                                                         String inputFileName_for_NYT,
                                                         String inputFolder_for_thehindu,
                                                         String inputFileName_for_thehindu,
                                                         String inputFolder_for_timesOfIndia_source3,
                                                         String inputFileNamefor_timesOfIndia_source3,
                                                         String inputFolder_for_NDTV_source4,
                                                         String inputFileNamefor_NDTV_source4,
                                                         int token_having_title_for_keywords_inputFileName_for_thehindu,
                                                         int token_having_title_for_keywords_inputFileName_for_timesOFindia,
                                                         int token_having_title_for_keywords_inputFileName_for_NDTV,
                                                         String outputFolderOfNYT,
                                                         TreeMap<String, String> mapSS_uniqueKeywords_from_other_sources,
                                                         TreeMap<String, TreeMap<String, String>> mapSplitMap_By_FirstLetter,
                                                         int startline,
                                                         int endline,
                                                         TreeMap<String, TreeMap<String, String>> ip_mapISS_MapOut,
                                                         TreeMap<Integer, Integer> ip_mapISS_Orig_NewsID_For_MapOut,
                                                         boolean is_use_input_mapISS_MapOut,
                                                         boolean is_generate_GBAD_output
    ) {

        TreeMap<Integer, String> mapEachLine = new TreeMap();
        TreeMap<Integer, String> mapInter2 = new TreeMap();
        TreeMap<Integer, String> mapInter3 = new TreeMap();
        TreeMap<Integer, String> mapInter4 = new TreeMap();
        TreeMap<String, String> mapIS_CurrNewsID_KeywordMainString = new TreeMap();
        TreeMap<String, String> mapIS_Counter_ByLine = new TreeMap();
        TreeMap<String, String> mapSS_Curr_KeywordNameValue = new TreeMap();
        int new_keywords_got_using_inputFile_unique_keywords_from_other_sources = 0;
        String curr_snippet = "";
        TreeMap<String, TreeMap<String, String>> mapISS_MapOut = new TreeMap();
        TreeMap<String, String> mapISS_irregular_NewsID_only_second_source = new TreeMap();

        TreeMap<Long, Long> mapISS_Orig_NewsID_For_MapOut = new TreeMap();


        System.out.println(" Inside convertNYTnewsARTICLEtoGBADgraph...outputFolderOfNYT:" + outputFolderOfNYT);
        int counterWord = 0;
        String intermediateFile = "";
        String intermediateFile_2 = "";
        String intermediateFile_3 = "";
        if (inputFileName_for_NYT.length() > 0) {
            intermediateFile = outputFolderOfNYT + inputFileName_for_NYT + ".1.txt";
            intermediateFile_2 = outputFolderOfNYT + inputFileName_for_NYT + ".2.txt";
            intermediateFile_3 = outputFolderOfNYT.replace(".txt", "") + inputFileName_for_NYT.replace(".txt", "") + ".g";
        } else {
            intermediateFile = outputFolderOfNYT + inputFileName_for_thehindu + ".1.txt";
            intermediateFile_2 = outputFolderOfNYT + inputFileName_for_thehindu + ".2.txt";
            intermediateFile_3 = outputFolderOfNYT.replace(".txt", "") + inputFileName_for_thehindu.replace(".txt", "") + ".g";
        }

        intermediateFile_3 = intermediateFile_3.replaceAll(".txt", "");
        String intermediateFile_source2 = outputFolderOfNYT + inputFileName_for_thehindu + ".1.txt";
        String intermediateFile_source3 = outputFolderOfNYT + inputFileNamefor_timesOfIndia_source3 + ".1.txt";
        String intermediateFile_source4 = outputFolderOfNYT + inputFileNamefor_NDTV_source4 + ".1.txt";

        FileWriter writer2 = null;
        FileWriter writer3 = null;

        try {
            System.out.println("intermediateFile_2:" + intermediateFile_2);
            System.out.println("intermediateFile_3:" + intermediateFile_3);
            writer2 = new FileWriter(intermediateFile_2);
            writer3 = new FileWriter(intermediateFile_3);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        long counterIndividualNewsID = 0;
        // TODO Auto-generated method stub
        try {
//			mapEachLine=
//				readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
//				readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line( inputFileOfNYT
//																						   , -1 //startline
//																						   , -1
//																						  );


            int c2 = 0;

            String patternString = "{\"web_url,word_count";
            TreeMap<Integer, String> mapInter = new TreeMap();

            String Yes_Filter_Pattern_for_each_line = "\"section_name\":\"world\"";  //example: "\"section_name\":\"world\""
            Yes_Filter_Pattern_for_each_line = "\"world\"";
            writer2.append("\ninput file for nytimes:" + inputFolderOfNYT + inputFileName_for_NYT);
            writer2.flush();

            //
            if (inputFileName_for_NYT.length() > 0) {
                mapInter = ReadFile_readEachLine_For_a_Set_of_Start_String_End_String_WriteToAnotherFile
                        .readFile_readEachLine_For_a_Set_of_Start_String_End_String_WriteToAnotherFile_Recursive(
                                patternString,
                                inputFolderOfNYT + inputFileName_for_NYT,
                                intermediateFile,
                                Yes_Filter_Pattern_for_each_line, //Yes_Filter_Pattern_for_each_line  //example: "name\":\"world\""
                                true, //is_Append_out_File
                                "nyt rec pattern"    //debug_label
                        );
            }


            //writer2.append("\nmapInter.size:"+mapInter.size()+"!!!");
            if (mapInter.containsKey(0))
                System.out.println("first line.split tokens:" + mapInter.get(0).split("!!!").length);

            // THEHINDU
            if (inputFileName_for_thehindu.length() > 0) {
                System.out.println("loading thehindu...");
                //writer3.append("\ninputFileName_for_thehindu.len:"+inputFileName_for_thehindu.length());
                writer3.flush();
                try {
                    mapInter2 = ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
                            .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(inputFolder_for_thehindu + inputFileName_for_thehindu,
                                    startline,
                                    endline,
                                    "thehindu", //debug_label
                                    true //isPrintSOP
                            );


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            // TIMES OF INDIA
            if (inputFileNamefor_timesOfIndia_source3.length() > 0) {
                System.out.println("loading times of india...");
                //writer3.append("\ninputFileName_for_thehindu.len:"+inputFileName_for_thehindu.length());
                writer3.flush();
                try {
                    mapInter3 = ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
                            .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(inputFolder_for_timesOfIndia_source3 + inputFileNamefor_timesOfIndia_source3,
                                    startline,
                                    endline,
                                    "timesOfIndia", //debug_label
                                    false //isPrintSOP
                            );
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            // NDTV
            if (inputFileNamefor_NDTV_source4.length() > 0) {
                System.out.println("loading NDTV ...");
                //writer3.append("\ninputFileName_for_thehindu.len:"+inputFileName_for_thehindu.length());
                writer3.flush();
                try {
                    mapInter4 = ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
                            .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(inputFolder_for_NDTV_source4 + inputFileNamefor_NDTV_source4,
                                    startline,
                                    endline,
                                    "NDTV", //debug_label
                                    false //isPrintSOP
                            );
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            //keyword example for a news"
//					"keywords":[{"rank":"1","is_major":"y","value":"tests and examinations","name":"subject"},{"rank":"3",
//					"is_major":"y","value":"farina, carmen","name":"persons"},{"rank":"4","is_major":"y","value":"new york city",
//					"name":"glocations"},{"rank":"2","is_major":"y","value":"education (k-12)","name":"subject"}]
//					"byline":{"contributor":"","person":[{"organization":"","role":"reported","firstname":"jennifer","rank":1,"lastname":"medina"}],"original":"by jennifer medina"}
//				    "original":"by nelson d. schwartz"}
            int lineNo = 0;
            Gson gson = new Gson();
            JsonObject jo = null;
            TreeMap<Long, Integer> mapII_EachArticle_MaxC2 = new TreeMap();
            TreeMap<String, String> mapSSAlreadyWroteToGBADfile = new TreeMap();

            TreeMap<String, String> mapStopWords = new TreeMap();
            mapStopWords = Stopwords.stopwords();

            writer2.append("\n nytimes.mapInter:" + mapInter.size());
            writer2.flush();

            // Source 1
            if (inputFileName_for_NYT.length() > 0) {
                //READ EACH LINE (Each line = a news feed having 10 articles)
                for (Integer s : mapInter.keySet()) {
                    lineNo++;
                    System.out.println("@@@@s:" + s + " " + mapInter.get(s).split("!!!").length);
                    //each of element after split is an article
                    String[] ss = mapInter.get(s).split("!!!");
                    //						String JSON_String=ss[1];
                    //						writer2.append("\njson:"+JSON_String+"<-");//this not JSON file
                    //						writer2.flush();
                    //						try{
                    //						//Response response = gson.fromJson(json, Response.class);
                    //						jo=convert_givenString_to_JSON(JSON_String);
                    //						JsonObject  jobject = jo.getAsJsonObject();
                    //					    jobject = jobject.getAsJsonObject("response");
                    //
                    //					    JsonArray jarray = jobject.getAsJsonArray("docs");
                    ////					    jobject = jarray.get(0).getAsJsonObject();
                    ////					    String result = jobject.get("web_url").toString();
                    ////					    System.out.println(result +" "+jarray.size());
                    //					    int cnt=0;
                    //					    System.out.println("-----------------"+cnt);
                    //
                    //					    	  //jarray = jobject.getAsJsonArray("docs");
                    //						jobject = jarray.get(lineNo-1).getAsJsonObject();
                    //						String weburl = jobject.get("web_url").toString();
                    //						String keywords = jobject.get("keywords").toString();
                    //						String news_desk= jobject.get("news_desk").toString();
                    //						String snippet= jobject.get("snippet").toString();
                    //						String section_name = jobject.get("section_name").toString();
                    //						    //JsonArray BYLINE = jobject.getAsJsonArray("byline");
                    //						String BYLINE = jobject.get("byline").toString();
                    //
                    //						System.out.println("JSON PARSER:"+news_desk);
                    //						writer2.append("\n JSON parser: "+news_desk+";section_name:"+section_name
                    //										+"");
                    //						writer2.flush();
                    //						}
                    //						catch(Exception e){
                    //							writer2.append("\n ERR on JSON parser: "
                    //									+"");
                    //							writer2.flush();
                    //							System.out.println("error on JSON");
                    //						}

                    int cnt2 = 0;


                    //
                    while (cnt2 < ss.length) {
                        //							if(cnt2<=0){
                        //								cnt2++; //json_string is in second token
                        //								continue;
                        //							}

                        counterIndividualNewsID++;
                        mapSS_Curr_KeywordNameValue = new TreeMap();
                        mapIS_CurrNewsID_KeywordMainString = new TreeMap();
                        mapIS_Counter_ByLine = new TreeMap();

                        //writer2.append("----------------------------\n");
                        writer2.flush();
                        try {
                            //							int section_index_begin=ss[cnt2].indexOf("section_name");
                            //							int section_index_end=ss[cnt2].indexOf("byline",
                            //																	section_index_begin);
                            //							String section=ss[cnt2].substring(section_index_begin, section_index_end);
                            //							int hh=ss[cnt2].indexOf(Yes_Filter_Pattern_for_each_line);
                            //							int hh2=section.indexOf(Yes_Filter_Pattern_for_each_line);
                            //
                            //							if(Yes_Filter_Pattern_for_each_line.length()>0
                            //								&& hh2==-1){
                            //
                            //								writer2.append("\n @@@@@@@@@ REMOVED as PER Filter_Pattern_for_each_line:"
                            //													+Yes_Filter_Pattern_for_each_line
                            //													+"!!!"+ss[cnt2].indexOf("section_name")
                            //													+"!!!"+section.replace("\"", "")
                            //													+"!!!hh2:"+hh2
                            //													);
                            //								writer2.append("\n filtered section line:"+ss[cnt2]);
                            //								writer2.flush();
                            //								cnt2++;
                            //								continue;
                            //							}

                        } catch (Exception e) {

                        }

                        String t = ss[cnt2];

                        System.out.println("token 10: " + ss[cnt2]);

                        //
                        if (Yes_Filter_Pattern_for_each_line.length() > 0 &&
                                ss[cnt2].indexOf(Yes_Filter_Pattern_for_each_line) == -1) {
                            cnt2++;
                            continue;
                        }
                        //
                        if (ss[cnt2].indexOf("snippet\":") >= 0) {
                            int i = ss[cnt2].indexOf("snippet\":");
                            int j = ss[cnt2].indexOf("abstract", i);
                            if (j > i) {
                                curr_snippet = ss[cnt2].substring(i, j).toLowerCase()
                                        .replace("u201c", " ")
                                        .replace("u201d", "");

                                curr_snippet = RemoveUnicodeChar.removeUnicodeChar(curr_snippet);
                                curr_snippet = curr_snippet.replace("\"", "")
                                        .replace(":", " ").replace("snippet:", " ")
                                        .replace("'", " ").replace("//", "")
                                        .replace(".", " ").replace(",", " ").replace("  ", " ")
                                        .replace("  ", " ")
                                        .replace("\\", "");

                            }
                            //writer2.append("\ngot.snippet:"+curr_snippet);
                            writer2.flush();
                        } else {
                            curr_snippet = "";
                            //writer2.append("\n Null.snippet:"+ss[cnt2]+"<-----cnt2:"+cnt2);
                            writer2.flush();
                        }

                        if (ss[cnt2].indexOf("keywords\":[") >= 0) {

                            int i = ss[cnt2].indexOf("keywords\":[");
                            int j = ss[cnt2].indexOf("]", i);
                            int c2_global = 0;
                            if (i > 0 && j > 0) {
                                String tt = ss[cnt2].substring(i, j + 1);
                                System.out.println("1.keywords:" + tt + ";i:" + i + ";j:" + j);
                                //writer2.append(s+";keywords:"+tt+"\n");
                                writer2.flush();
                                mapIS_CurrNewsID_KeywordMainString.put("keyword", tt);
                                System.out.println("2.keyword.tt:" + tt + ";index:" + tt.indexOf("},{"));
                                if (tt.indexOf("},{") >= 0) {
                                    String[] sss = tt.split("}");
                                    c2 = 0;
                                    //
                                    while (c2 < sss.length) {
                                        sss[c2] = sss[c2].replace("keywords\":", "");
                                        String[] sss2 = sss[c2].split("\",\"");
                                        //mapKeywordNameValue.put(sss2[0], sss2[1]);
                                        int sss2_Size = sss2.length;
                                        int c3 = 0;
                                        String concat = "";
                                        while (c3 < sss2_Size) {
                                            //concat=concat+sss2[c3];

                                            String[] h = sss2[c3].split(":");
                                            if (h.length >= 2) {
                                                h[0] = h[0].replace("\"", "").replace("{", "").replace(",", "");
                                                h[1].replace("\"", "").replace("{", "");
                                                //writer2.append(s+";keywords(individual):N:"+h[0]
                                                //				  +";V:"+h[1]+"##\n");
                                                writer2.flush();
                                                mapSS_Curr_KeywordNameValue.put(h[0] + ":" + c2 //counter c3
                                                        , h[1].replace("\"", ""));
                                            }
                                            c3++;
                                        }
                                        c2++;
                                    } //while
                                }
                                c2 = 0;
                                // only single; NOT MULTIPLE
                                if (tt.indexOf("},{") == -1) {
                                    tt = tt.replace("keywords\":", "");
                                    String[] sss2 = tt.split("\",\"");
                                    //mapKeywordNameValue.put(sss2[0], sss2[1]);
                                    int sss2_Size = sss2.length;
                                    int c3 = 0;
                                    String concat = "";
                                    while (c3 < sss2_Size) {
                                        //concat=concat+sss2[c3];

                                        String[] h = sss2[c3].split(":");
                                        if (h.length >= 2) {
                                            h[0] = h[0].replace("\"", "").replace("{", "").replace(",", "");
                                            h[1].replace("\"", "").replace("{", "");
                                            //writer2.append(s+";keywords(individual):N:"+h[0]
                                            //				  +";V:"+h[1]+"##\n");
                                            writer2.flush();
                                            h[0] = h[0].replace("rank,", "").replace(" name", "");
                                            if (h[0].indexOf("name") >= 0) {
                                                h[0] = h[0].substring(h[0].indexOf("name"),
                                                        h[0].length()).replace("}", "").replace("]", "")
                                                        .replace("{", "").replace("[", "");
                                            }
                                            mapSS_Curr_KeywordNameValue.put(h[0] + ":" + c2 //counter c3
                                                    , h[1].replace("\"", "").replace("}", "").replace("]", "")
                                                            .replace("{", "").replace("[", ""));
                                        }
                                        c3++;
                                    }
                                    c2++;
                                }
                                c2_global = c2;
                            }
                            //add this to current NEWSID
                            int size_before = mapSS_Curr_KeywordNameValue.size();
                            c2 = c2_global;

                            //from external unique keywords
                            if (curr_snippet.length() > 2) {
                                String[] arr_curr_snippet = curr_snippet.split(" ");
                                int arr_curr_snippet_LEN = arr_curr_snippet.length;
                                int cnt = 0;
                                String tmp = "", tmp2 = "", tmp3 = "";

                                // APPROACH 3 : BEGIN BELOW DIFFERENT TYPE OF APPROCH
                                if (mapSS_uniqueKeywords_from_other_sources.size() > 0) {

                                    while (cnt < arr_curr_snippet_LEN) {

                                        System.out.println("len:" + arr_curr_snippet_LEN + " cnt:" + cnt);
                                        String firstLetter = "";

                                        try {
                                            tmp = arr_curr_snippet[cnt]; //iraq
                                            firstLetter = tmp.substring(0, 1);
                                        } catch (Exception e) {
                                            tmp = "";
                                        }
                                        if (tmp != null) {
                                            // stop words
                                            if (tmp.length() > 0 &&
                                                    mapStopWords.containsKey(tmp)) {
                                                System.out.println("&&&& stopwords:" + tmp);
                                                cnt++;
                                                continue;
                                            }
                                        }

                                        try {
                                            tmp2 = arr_curr_snippet[cnt] + " " +
                                                    arr_curr_snippet[cnt + 1]; //iraq
                                        } catch (Exception e) {
                                            tmp2 = "";
                                        }
                                        try {
                                            tmp3 = arr_curr_snippet[cnt] + " " +
                                                    arr_curr_snippet[cnt + 1]
                                                    + " " + arr_curr_snippet[cnt + 2]; //iraq
                                        } catch (Exception e) {
                                            tmp3 = "";
                                        }

                                        //
                                        TreeMap<String, String> curr_Keywords_consider =
                                                mapSplitMap_By_FirstLetter.get(firstLetter);

                                        if (curr_Keywords_consider == null) {
                                            cnt++;
                                            continue;
                                        }

                                        System.out.println(
                                                "token from snipp:" + tmp
                                                        + ";curr_Keywords_consider:" + curr_Keywords_consider
                                                        + " tmp:" + tmp);
                                        //System.out.println("mapStopWords:"+mapStopWords);

                                        if (tmp != null) {
                                            //
                                            if (tmp.length() > 0
                                                    && curr_Keywords_consider.containsKey(tmp)) {
                                                mapSS_Curr_KeywordNameValue.put("name:" + c2, tmp);
                                                c2++;
                                                new_keywords_got_using_inputFile_unique_keywords_from_other_sources++;
                                            }
                                        }
                                        //
                                        if (tmp2 != null) {
                                            if (tmp2.length() > 0
                                                    && curr_Keywords_consider.containsKey(tmp2)) {
                                                mapSS_Curr_KeywordNameValue.put("name:" + c2, tmp2);
                                                c2++;
                                                new_keywords_got_using_inputFile_unique_keywords_from_other_sources++;
                                            }
                                        }
                                        //
                                        if (tmp3 != null) {
                                            if (tmp3.length() > 0
                                                    && curr_Keywords_consider.containsKey(tmp3)) {
                                                mapSS_Curr_KeywordNameValue.put("name:" + c2, tmp3);
                                                c2++;
                                                new_keywords_got_using_inputFile_unique_keywords_from_other_sources++;
                                            }
                                        }
                                        cnt++;

                                    }
                                }
                                // APPROACH 3 : END BELOW DIFFERENT TYPE OF APPROCH

                                //									APPROACH 2 :END BELOW DIFFERENT TYPE OF APPROCH
                                //									while(cnt<arr_curr_snippet_LEN){
                                //										System.out.println("len:"+arr_curr_snippet_LEN+" cnt:"+cnt);
                                //										if(cnt<=arr_curr_snippet_LEN-2){
                                //											tmp=arr_curr_snippet[cnt]+arr_curr_snippet[cnt+1];
                                //										}
                                //										else
                                //											tmp=arr_curr_snippet[cnt];
                                //										cnt++;
                                //										c2++;
                                //									}
                                //									//
                                //									if(mapSS_uniqueKeywords_from_other_sources.containsKey(tmp)){
                                //										mapSS_Curr_KeywordNameValue.put("name:"+c2, tmp);
                                //									}
                                //									APPROACH 2 :END BELOW DIFFERENT TYPE OF APPROCH

                                //APPROACH 1 :BEING BELOW DIFFERENT TYPE OF APPROCH - IT TAKES TIME
                                // current keyword
                                //									for(String i2:mapSS_uniqueKeywords_from_other_sources.keySet()){
                                //										String curr_keyword=i2.replace("\"", "");
                                //										if(curr_keyword.length()<=2) //blank value
                                //											continue;
                                //										System.out.println("lineNo:"+lineNo+" ext keyword:"+curr_keyword+" bef:"+size_before
                                //															+" now:"+mapSS_Curr_KeywordNameValue.size());
                                //
                                ////										writer2.append("\n2.single.ext curr.keyword:"+curr_keyword
                                ////														+" curr_snippet:"+curr_snippet
                                ////														+" ss[cnt2].len:"+ss[cnt2].length()
                                ////														+" index:"+curr_snippet.indexOf(curr_keyword)
                                ////														+"<-------"
                                ////														);
                                //										writer2.flush();
                                //										if(curr_snippet.indexOf(curr_keyword)>=0){
                                //											mapSS_Curr_KeywordNameValue.put("name:"+c2, curr_keyword);
                                //											//writer2.append("\n 2.external keywords:"+curr_keyword);
                                //											writer2.flush();
                                //											c2++;
                                //										}
                                ////										int c3=0;
                                ////										while(c3<arr_curr_snippet_LEN){
                                ////											ar
                                ////											c3++;
                                ////										}
                                //
                                //									}

                            }

                            //mapSS_Curr_KeywordNameValue
                        } else { //NO KEYWORDS
                            c2 = 0;
                            String[] arr_curr_snippet = curr_snippet.split(" ");
                            int arr_curr_snippet_LEN = arr_curr_snippet.length;
                            //writer2.append("\n EXT.keyword.size:"+mapSS_uniqueKeywords_from_other_sources.size());
                            writer2.flush();
                            //not NULL
                            if (curr_snippet.length() > 2) {

                                int cnt = 0;
                                String tmp = "", tmp2 = "", tmp3 = "";
                                // APPROACH 3 : BEGIN BELOW DIFFERENT TYPE OF APPROCH
                                if (mapSS_uniqueKeywords_from_other_sources.size() > 0) {

                                    while (cnt < arr_curr_snippet_LEN) {
                                        System.out.println("len:" + arr_curr_snippet_LEN + " cnt:" + cnt);
                                        String firstLetter = "";

                                        try {
                                            tmp = arr_curr_snippet[cnt]; //iraq
                                            firstLetter = tmp.substring(0, 1);
                                        } catch (Exception e) {
                                            tmp = "";
                                        }
                                        if (tmp != null) {
                                            // stop words
                                            if (tmp.length() > 0 &&
                                                    mapStopWords.containsKey(tmp)) {
                                                System.out.println("&&&& stopwords:" + tmp);
                                                cnt++;
                                                continue;
                                            }
                                        }

                                        try {
                                            tmp2 = arr_curr_snippet[cnt] + " " +
                                                    arr_curr_snippet[cnt + 1]; //iraq
                                        } catch (Exception e) {
                                            tmp2 = "";
                                        }
                                        try {
                                            tmp3 = arr_curr_snippet[cnt] + " " +
                                                    arr_curr_snippet[cnt + 1]
                                                    + " " + arr_curr_snippet[cnt + 2]; //iraq
                                        } catch (Exception e) {
                                            tmp3 = "";
                                        }

                                        //
                                        TreeMap<String, String> curr_Keywords_consider =
                                                mapSplitMap_By_FirstLetter.get(firstLetter);

                                        if (curr_Keywords_consider == null) {
                                            cnt++;
                                            continue;
                                        }

                                        System.out.println(
                                                "token from snipp:" + tmp
                                                        + ";curr_Keywords_consider:" + curr_Keywords_consider
                                                        + " tmp:" + tmp);
                                        //System.out.println("mapStopWords:"+mapStopWords);

                                        if (tmp != null) {
                                            //
                                            if (tmp.length() > 0
                                                    && curr_Keywords_consider.containsKey(tmp)) {
                                                mapSS_Curr_KeywordNameValue.put("name:" + c2, tmp);
                                                c2++;
                                                new_keywords_got_using_inputFile_unique_keywords_from_other_sources++;
                                            }
                                        }
                                        //
                                        if (tmp2 != null) {
                                            if (tmp2.length() > 0
                                                    && curr_Keywords_consider.containsKey(tmp2)) {
                                                mapSS_Curr_KeywordNameValue.put("name:" + c2, tmp2);
                                                c2++;
                                                new_keywords_got_using_inputFile_unique_keywords_from_other_sources++;
                                            }
                                        }
                                        //
                                        if (tmp3 != null) {
                                            if (tmp3.length() > 0
                                                    && curr_Keywords_consider.containsKey(tmp3)) {
                                                mapSS_Curr_KeywordNameValue.put("name:" + c2, tmp3);
                                                c2++;
                                                new_keywords_got_using_inputFile_unique_keywords_from_other_sources++;
                                            }
                                        }
                                        cnt++;
                                    }
                                }
                                // APPROACH 3 : END BELOW DIFFERENT TYPE OF APPROCH


                                //									APPROACH 2 : BEGIN BELOW DIFFERENT TYPE OF APPROCH
                                //									while(cnt<arr_curr_snippet_LEN){
                                //										if(cnt<=arr_curr_snippet_LEN-1)
                                //											tmp=arr_curr_snippet[cnt]+arr_curr_snippet[cnt+1];
                                //										else
                                //											tmp=arr_curr_snippet[cnt];
                                //										cnt++;
                                //										c2++;
                                //									}
                                //									//
                                //									if(mapSS_uniqueKeywords_from_other_sources.containsKey(tmp)){
                                //										mapSS_Curr_KeywordNameValue.put("name:"+c2, tmp);
                                //									}
                                //									APPROACH 2 :END BELOW DIFFERENT TYPE OF APPROCH

                                //APPROACH 1 : BEING BELOW DIFFERENT TYPE OF APPROCH - IT TAKES TIME
                                // current keyword
                                //								for(String i:mapSS_uniqueKeywords_from_other_sources.keySet()){
                                //									String curr_keyword=i.replace("\"", "");
                                //									if(curr_keyword.length()<=2) //blank value
                                //										continue;
                                //
                                //									System.out.println("ext keyword:"+curr_keyword);
                                //
                                ////									writer2.append("\nsingle.ext curr.keyword:"+curr_keyword
                                ////													+" curr_snippet:"+curr_snippet
                                ////													+" ss[cnt2].len:"+ss[cnt2].length()
                                ////													+" index:"+curr_snippet.indexOf(curr_keyword)
                                ////													+"<-------"
                                ////													);
                                //									writer2.flush();
                                //
                                //									if(curr_snippet.indexOf(curr_keyword)>=0){
                                //										mapSS_Curr_KeywordNameValue.put("name:"+c2, curr_keyword);
                                //										//writer2.append("\n .external keywords:"+curr_keyword);
                                //										writer2.flush();
                                //										c2++;
                                //									}
                                ////									int c3=0;
                                ////									while(c3<arr_curr_snippet_LEN){
                                ////										ar
                                ////										c3++;
                                ////									}
                                //
                                //								}
                                // APPROACH 1 : END  BELOW DIFFERENT TYPE OF APPROCH - IT TAKES TIME

                            } else {
                                //writer2.append("\nsnippet BLANK"+"\n");
                                writer2.flush();
                            }
                            //writer2.append(s+";keywords:"+"NOT available"+"\n");
                            writer2.flush();
                        }

                        if (mapIS_CurrNewsID_KeywordMainString.size() > 0) {
                            mapISS_MapOut.put(String.valueOf(counterIndividualNewsID) + ":1:",
                                    mapIS_CurrNewsID_KeywordMainString);
                        }
                        if (mapIS_CurrNewsID_KeywordMainString.size() > 0) {
                            mapISS_MapOut.put(String.valueOf(counterIndividualNewsID) + ":3:",
                                    mapSS_Curr_KeywordNameValue);
                        }
                        //							mapIS_CurrNewsID_KeywordMainString=new TreeMap();
                        //							mapSS_Curr_KeywordNameValue=new TreeMap();
                        //writer2.append("-------end of keywords-----\n");
                        writer2.flush();
                        String[] personInfoString = {""};
                        //example of byline having multiple authors
                        //,"byline":{"person":[{"organization":"","role":"reported","firstname":"annie","rank":1,"lastname":"correal"},
                        //{"organization":"","role":"reported","firstname":"andy","rank":2,"lastname":"newman"}]
                        // below example of 2 people in byline
                        //byline:byline":{"contributor":"choe sang-hun reported from jindo, and david e. sanger from seoul, south korea. steve kenny contributed reporting from washington.","person":[{"organization":"","role":"reported","firstname":"choe","rank":1,"lastname":"sang-hun"},{"firstname":"david","middlename":"e.","lastname":"sanger","rank":2,"role":"reported","organization":""
                        //personInfoString[cnt20]:byline:{contributor : choe sang-hun reported from jindo, and david e. sanger from seoul, south korea. steve kenny contributed reporting from washington.,person:[{organization:","role":"reported","firstname":"choe","rank":1,"lastname":"sang-hun"},"
                        //{"firstname":"david","middlename":"e.","lastname":"sanger","rank":2,"role":"reported","organization: ,


                        //writer2.append("\n2.ss[cnt2]:"+ss[cnt2]+"\n");
                        writer2.append("\n ~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
                        writer2.flush();
                        if (ss[cnt2].indexOf("byline\":{") >= 0) {
                            int i = ss[cnt2].indexOf("byline\":{");
                            int j = ss[cnt2].indexOf("}]", i + 3);

                            if (i > 0 && j > 0) {
                                String tt = ss[cnt2].substring(i, j);
                                System.out.println("1. found byline:" + tt);
                                //writer2.append("2.found byline"+s+";byline:"+tt+"\n");
                                writer2.flush();

                                tt = tt + ",";
                                //writer2.append("11.split on },{ ->"+tt.split("\\},\\{").length +"\n");
                                writer2.flush();
                                if (tt.split("\\},\\{").length > 1) {
                                    personInfoString = tt.split("\\},\\{");
                                    //writer2.append("10.split on \\},\\{ ->"+personInfoString.length+"\n");
                                    writer2.flush();
                                } else {
                                    personInfoString[0] = tt;
                                }
                            }
                            int cnt20 = 0;
                            //
                            while (cnt20 < personInfoString.length) {
                                //writer2.append("4.personInfoString(array:)"+personInfoString[cnt20]+"\n");
                                writer2.flush();
                                cnt20++;
                            }

                            cnt20 = 0;
                            if (personInfoString != null) {
                                //
                                while (cnt20 < personInfoString.length) {
                                    //personInfoString[cnt20]=personInfoString[cnt20]+",";
                                    String organization = "", firstname = "", lastname = "";
                                    if (personInfoString[cnt20].length() < 2) { //null
                                        cnt20++;
                                        continue;
                                    }

                                    i = personInfoString[cnt20].indexOf("organizat");
                                    int k = personInfoString[cnt20].indexOf(":", i + 2) + 2;

                                    j = personInfoString[cnt20].indexOf("\"", k);
                                    System.out.println("org:" + personInfoString[cnt20]);
                                    //writer2.append("\n 21. organization index:"+i+" "+k+" "+j+"\n");
                                    writer2.flush();
                                    if (i >= 0 && j >= 0 && j > i) {
                                        organization = personInfoString[cnt20].substring(i, j);
                                        int oo = organization.indexOf(",");

                                        if (oo >= 0) {
                                            organization = organization.substring(0, oo - 1);
                                        }
                                        organization = organization
                                                .replace("\"", "").replace("organization:", "")
                                                .replace("\\", "").replace("/", "");

                                        ////writer2.appendppend("\n 22.found organization:"+organization+"\n");
                                        writer2.flush();
                                    }

                                    i = personInfoString[cnt20].indexOf("firstname");
                                    j = personInfoString[cnt20].indexOf("\",", i);

                                    if (i >= 0 && j >= 0 && j > i) {
                                        firstname = personInfoString[cnt20].substring(i, j);
                                    }

                                    i = personInfoString[cnt20].indexOf("lastname\":");
                                    j = personInfoString[cnt20].indexOf("\"", i + "lastname\":".length() + 1);
                                    if (i >= 0 && j >= 0 && j > i) {
                                        lastname = personInfoString[cnt20].substring(i, j);
                                    }
                                    organization = organization.replace("organization", "").replace(":", "")
                                            .replace("{", "").replace("”", "").replace("", "").replace("\"", "");
                                    firstname = firstname.replace("firstname\":", "").replace("\"", "");
                                    lastname = lastname.replace("lastname\":", "").replace("\"", "");
                                    //writer2.append("!!2.personInfoString[cnt20]:"+personInfoString[cnt20]+
                                    //				";personInfoString.len:"+personInfoString.length+"\n");
                                    //writer2.append("###o,f,l->"+organization+"!!!"+firstname+"!!!"+lastname+"###\n");
                                    writer2.flush();
                                    mapIS_Counter_ByLine.put(String.valueOf(cnt20),
                                            organization + "!!!" + firstname + "!!!" + lastname);
                                    cnt20++;
                                }
                            } //not null

                            if (mapIS_Counter_ByLine.size() > 0) {
                                mapISS_MapOut.put(String.valueOf(counterIndividualNewsID) + ":2:",
                                        mapIS_Counter_ByLine);

                            }
                        } else {
                            //writer2.append(s+";byline:"+"NOT available"+"\n");
                            writer2.flush();
                        }

                        //writer2.append("-------end of byline-----\n");
                        writer2.flush();
                        //Obsolete , organization contains same value as BYLINE .so holding it
                        if (ss[cnt2].indexOf("organization\":") >= 0) {
                            int i = ss[cnt2].indexOf("organization\":");
                            int j = ss[cnt2].indexOf("}]", i + 2);

                            if (i > 0 && j > 0) {
                                String tt = ss[cnt2].substring(i, j);
                                System.out.println("organization:" + tt);
                                //writer2.append(s+";organization:"+tt+"\n");
                                writer2.flush();
                                //mapOrganization.put(counterIndividualNewsID, tt);
                            }
                        } else {
                            //writer2.append(s+";organization:"+"NOT available"+"\n");
                            writer2.flush();
                        }
                        //writer2.append("-------end of organization-----\n");
                        writer2.flush();

                        cnt2++;

                        if (mapIS_Counter_ByLine == null && mapSS_Curr_KeywordNameValue == null) {
                            continue;
                        }

                        if (mapIS_Counter_ByLine.size() == 0 && mapSS_Curr_KeywordNameValue.size() == 0) {
                            continue;
                        }
                        //counterIndividualNewsID++;
                        mapISS_Orig_NewsID_For_MapOut.put(counterIndividualNewsID, counterIndividualNewsID);

                        //							if(mapSS_Curr_KeywordNameValue.size()>0){
                        //								mapISS_MapOut.put(String.valueOf(counterIndividualNewsID+":3:"),
                        //								  		  		  				mapSS_Curr_KeywordNameValue);
                        //							}


                        //							writer3.append("debug: newsid->("+counterIndividualNewsID+")Final written:\n"+
                        //											"1:"+mapISS_MapOut.get(String.valueOf(counterIndividualNewsID+":1:"))+"\n"+
                        //											"2:"+mapISS_MapOut.get(String.valueOf(counterIndividualNewsID+":2:"))+"\n"+
                        //											"3:"+mapISS_MapOut.get(String.valueOf(counterIndividualNewsID+":3:"))+
                        //											"\n");
                        writer3.flush();

                        // NO BYLINE and NO KeywordNameValue from this article
                        //							if( !mapISS_MapOut.containsKey(String.valueOf(counterIndividualNewsID+":2:"))
                        //								&& !mapISS_MapOut.containsKey(String.valueOf(counterIndividualNewsID+":3:"))
                        //							   ){
                        //								counterIndividualNewsID--;
                        //							}


                        //							writer3.append("--------------------------------\n");
                        //							writer3.append("mapISS_Orig_NewsID_For_MapOut:"+ mapISS_Orig_NewsID_For_MapOut+"\n");
                        //							writer3.append("mapIS_CurrNewsID_KeywordMainString:"+ mapIS_CurrNewsID_KeywordMainString+"\n");
                        //							writer3.append("mapIS_Counter_ByLine:"+ mapIS_Counter_ByLine+"\n");
                        //							writer3.append("mapSS_Curr_KeywordNameValue:"+ mapSS_Curr_KeywordNameValue+"\n");
                        //							writer3.append("mapISS_MapOut:"+ mapISS_MapOut+"\n");
                        writer3.flush();


                        mapSS_Curr_KeywordNameValue = new TreeMap();
                        mapIS_CurrNewsID_KeywordMainString = new TreeMap();
                        mapIS_Counter_ByLine = new TreeMap();

                        mapII_EachArticle_MaxC2.put(counterIndividualNewsID,
                                c2);

                    } // while cnt2 - each token in line


                } //each line

            }

            //writer3.append("mapISS_MapOut:"+mapISS_MapOut+"\n");
            //writer3.flush();


            writer2.append("\n nytimes: " + mapISS_MapOut + "\n -###---");
            writer2.flush();

            long news_id_for_second_source = 1500000;
            //int before_size_mapISS_MapOut=mapISS_MapOut.size();
            // Source 2
            if (inputFileName_for_thehindu.length() > 0) {

                for (int currLine : mapInter2.keySet()) {
                    mapSS_Curr_KeywordNameValue = new TreeMap();
                    news_id_for_second_source++;
                    String line = mapInter2.get(currLine).replace("title:", "");
                    //title used for keywrods
                    String title = line.split("!!!")[token_having_title_for_keywords_inputFileName_for_thehindu - 1];
                    String[] curr_tokens = title.split(" ");
                    int c200 = 0;
                    //
                    while (c200 < curr_tokens.length) {
                        String curr_token_string = curr_tokens[c200];

                        if (!Stopwords.is_stopword(curr_token_string)) {
                            //if()
                            mapSS_Curr_KeywordNameValue.put("name:" + c200, "name");
                            mapSS_Curr_KeywordNameValue.put("value:" + c200, curr_token_string);
                        }
                        System.out.println(" thehindu:" + curr_token_string);
                        c200++;
                    }
                    mapISS_MapOut.put(String.valueOf(news_id_for_second_source) + ":3:",
                            mapSS_Curr_KeywordNameValue);
                    mapISS_irregular_NewsID_only_second_source.put(
                            String.valueOf(news_id_for_second_source),
                            String.valueOf(news_id_for_second_source)
                    );

                    mapII_EachArticle_MaxC2.put(Long.valueOf(news_id_for_second_source),
                            15);
                    System.out.println("Before:" + ";mapISS_MapOut:" + mapISS_MapOut);
                    System.out.println("(thehindu)mapISS_irregular_NewsID_only_second_source:" + mapISS_irregular_NewsID_only_second_source);
                }
            } //END if(inputFileName_for_thehindu.length() >0){

            long last_regular_newsid = 0;
            // get last regular NEWSID
            for (long i : mapISS_Orig_NewsID_For_MapOut.keySet()) {
                last_regular_newsid = mapISS_Orig_NewsID_For_MapOut.get(i);
            }

//					writer3.append("\nmapISS_irregular_NewsID_only_second_source:"+mapISS_irregular_NewsID_only_second_source
//									+" "+mapISS_irregular_NewsID_only_second_source.size());
            writer3.flush();

            // thehindu - give unique newsid across different sources
            for (String irregularNewsID : mapISS_irregular_NewsID_only_second_source.keySet()) {
                last_regular_newsid++;
                mapISS_Orig_NewsID_For_MapOut.put(Long.valueOf(irregularNewsID),
                        last_regular_newsid);
            }
            System.out.println("\nmapISS_irregular_NewsID_only_second_source:" + mapISS_irregular_NewsID_only_second_source
                    + " " + mapISS_irregular_NewsID_only_second_source.size());

            // BEGIN TIMES OF INDIA
            mapISS_irregular_NewsID_only_second_source = new TreeMap();
            news_id_for_second_source = 2500000;
            //int before_size_mapISS_MapOut=mapISS_MapOut.size();
            // Source 3
            if (inputFileNamefor_timesOfIndia_source3.length() > 0) {

                for (int currLine : mapInter3.keySet()) {
                    mapSS_Curr_KeywordNameValue = new TreeMap();
                    news_id_for_second_source++;
                    String line = mapInter3.get(currLine).replace("title:", "");
                    //title used for keywrods
                    String title = line.split("!!!")[token_having_title_for_keywords_inputFileName_for_timesOFindia - 1];
                    String[] curr_tokens = title.split("-");
                    int c200 = 0;
                    //
                    while (c200 < curr_tokens.length) {
                        String curr_token_string = curr_tokens[c200];

                        if (!Stopwords.is_stopword(curr_token_string)) {
                            //if()
                            mapSS_Curr_KeywordNameValue.put("name:" + c200, "name");
                            mapSS_Curr_KeywordNameValue.put("value:" + c200, curr_token_string);
                        }
                        System.out.println(" timesOfIndia:" + curr_token_string);
                        c200++;
                    }
                    mapISS_MapOut.put(String.valueOf(news_id_for_second_source) + ":3:",
                            mapSS_Curr_KeywordNameValue);
                    mapISS_irregular_NewsID_only_second_source.put(
                            String.valueOf(news_id_for_second_source),
                            String.valueOf(news_id_for_second_source)
                    );

                    mapII_EachArticle_MaxC2.put(Long.valueOf(news_id_for_second_source),
                            15);
                    System.out.println("Before:" + ";mapISS_MapOut:" + mapISS_MapOut);
                    System.out.println("(toI)mapISS_irregular_NewsID_only_second_source:" + mapISS_irregular_NewsID_only_second_source);
                }
            } //END

            last_regular_newsid = 0;
            // get last regular NEWSID
            for (long i : mapISS_Orig_NewsID_For_MapOut.keySet()) {
                last_regular_newsid = mapISS_Orig_NewsID_For_MapOut.get(i);
            }

//					writer3.append("\nmapISS_irregular_NewsID_only_second_source:"+mapISS_irregular_NewsID_only_second_source
//									+" "+mapISS_irregular_NewsID_only_second_source.size());
            writer3.flush();

            // times of India - give unique newsid across different sources
            for (String irregularNewsID : mapISS_irregular_NewsID_only_second_source.keySet()) {
                last_regular_newsid++;
                mapISS_Orig_NewsID_For_MapOut.put(Long.valueOf(irregularNewsID),
                        last_regular_newsid);
            }
            System.out.println("\nmapISS_irregular_NewsID_only_second_source:" + mapISS_irregular_NewsID_only_second_source
                    + " " + mapISS_irregular_NewsID_only_second_source.size());

            // END 	 TIMES OF INDIA

            // BEGIN NDTV
            //reuse variable
            news_id_for_second_source = 3500000;
            mapISS_irregular_NewsID_only_second_source = new TreeMap();
            //int before_size_mapISS_MapOut=mapISS_MapOut.size();
            // Source 3
            if (inputFileNamefor_NDTV_source4.length() > 0) {

                for (int currLine : mapInter4.keySet()) {
                    mapSS_Curr_KeywordNameValue = new TreeMap();
                    news_id_for_second_source++;
                    String line = mapInter4.get(currLine).replace("title:", "");
                    //title used for keywrods
                    String title = line.split("!!!")[token_having_title_for_keywords_inputFileName_for_NDTV - 1];
                    String[] curr_tokens = title.split("-");
                    int c200 = 0;
                    //
                    while (c200 < curr_tokens.length) {
                        String curr_token_string = curr_tokens[c200];

                        if (!Stopwords.is_stopword(curr_token_string)) {
                            //if()
                            mapSS_Curr_KeywordNameValue.put("name:" + c200, "name");
                            mapSS_Curr_KeywordNameValue.put("value:" + c200, curr_token_string);
                        }
                        //System.out.println(" NDTV:"+curr_token_string);
                        c200++;
                    }
                    mapISS_MapOut.put(String.valueOf(news_id_for_second_source) + ":3:",
                            mapSS_Curr_KeywordNameValue);
                    mapISS_irregular_NewsID_only_second_source.put(
                            String.valueOf(news_id_for_second_source),
                            String.valueOf(news_id_for_second_source)
                    );

                    mapII_EachArticle_MaxC2.put(Long.valueOf(news_id_for_second_source),
                            10);
                    System.out.println("Before:" + ";mapISS_MapOut:" + mapISS_MapOut);
                    System.out.println("(ndtv)mapISS_irregular_NewsID_only_second_source:" + mapISS_irregular_NewsID_only_second_source);
                }
            } //END if(inputFileName_for_thehindu.length() >0){

            last_regular_newsid = 0;
            // get last regular NEWSID
            for (long i : mapISS_Orig_NewsID_For_MapOut.keySet()) {
                last_regular_newsid = mapISS_Orig_NewsID_For_MapOut.get(i);
            }

//					writer3.append("\nmapISS_irregular_NewsID_only_second_source:"+mapISS_irregular_NewsID_only_second_source
//									+" "+mapISS_irregular_NewsID_only_second_source.size());
            writer3.flush();

            // times of India - give unique newsid across different sources
            for (String irregularNewsID : mapISS_irregular_NewsID_only_second_source.keySet()) {
                last_regular_newsid++;
                mapISS_Orig_NewsID_For_MapOut.put(Long.valueOf(irregularNewsID),
                        last_regular_newsid);
            }
            System.out.println("\nmapISS_irregular_NewsID_only_second_source:" + mapISS_irregular_NewsID_only_second_source
                    + " " + mapISS_irregular_NewsID_only_second_source.size());


            // END 	 NDTV

//					writer3.append("\nmapISS_irregular_NewsID_only_second_source:"+mapISS_irregular_NewsID_only_second_source
//									+" "+mapISS_irregular_NewsID_only_second_source.size());
//					writer3.flush();

            TreeMap<String, String> mapIS_NEW_CurrNewsID_KeywordMainString = new TreeMap();
            TreeMap<String, String> mapIS_NEW_Counter_ByLine = new TreeMap();
            TreeMap<String, String> mapSS_NEW_Curr_KeywordNameValue = new TreeMap();
            //
            for (long o : mapISS_Orig_NewsID_For_MapOut.keySet()) {

                if (mapISS_MapOut.containsKey(String.valueOf(o) + ":1:"))
                    mapIS_NEW_CurrNewsID_KeywordMainString = mapISS_MapOut.get(String.valueOf(o) + ":1:");
                else
                    mapIS_NEW_CurrNewsID_KeywordMainString = new TreeMap();

                if (mapISS_MapOut.containsKey(String.valueOf(o) + ":2:"))
                    mapIS_NEW_Counter_ByLine = mapISS_MapOut.get(String.valueOf(o) + ":2:");
                else
                    mapIS_NEW_Counter_ByLine = new TreeMap();

                if (mapISS_MapOut.containsKey(String.valueOf(o) + ":3:"))
                    mapSS_NEW_Curr_KeywordNameValue = mapISS_MapOut.get(String.valueOf(o) + ":3:");
                else
                    mapSS_NEW_Curr_KeywordNameValue = new TreeMap();

                // if both BYLINE missing AND keywords missing, ignore that news article.
                if (mapIS_NEW_Counter_ByLine.size() == 0 && mapSS_NEW_Curr_KeywordNameValue.size() == 0) {
                    continue;
                }

                //writer3.append("############ main keyword ############first key:"+String.valueOf(o) +":1:"+"#\n");
                writer3.flush();
                //
                for (String s11 : mapIS_NEW_CurrNewsID_KeywordMainString.keySet()) {
                    //writer3.append(o+"!!!"+s11+"!!!"+mapIS_NEW_CurrNewsID_KeywordMainString.get(s11)+"\n");
                    writer3.flush();
                }
                //writer3.append("----------ByLine-----------\n");
                writer3.flush();
                for (String s12 : mapIS_NEW_Counter_ByLine.keySet()) {
                    //writer3.append(o+"!!!"+s12+"!!!"+mapIS_NEW_Counter_ByLine.get(s12)+"\n");
                    writer3.flush();
                }
                //writer3.append("----------Keyword Name Value-----------\n");
                writer3.flush();
                for (String s13 : mapSS_NEW_Curr_KeywordNameValue.keySet()) {
                    //writer3.append(o+"!!!"+s13+"!!!"+mapSS_NEW_Curr_KeywordNameValue.get(s13)  +"\n");
                    writer3.flush();
                }

            } //end for

            System.out.println("mapISS_Orig_NewsID_For_MapOut:" + mapISS_Orig_NewsID_For_MapOut);
            System.out.println("mapISS_Orig_NewsID_For_MapOut_source_1:");

            TreeMap<String, String> map_Keyword_NameValue_Already_Existed_For_GBAD_OUT
                    = new TreeMap();
            // iterate over keywords and find overlapping keywords

            TreeMap<Integer, String> mapGlobalID_KeyValuePair = new TreeMap();
            //writer3.append("----------GBAD-----------\n");
            writer3.flush();

            //

            // generate gbad
            if (is_generate_GBAD_output == true) {

                writer3.append("XP # 1\n");
                writer3.flush();
                int news_node_vertex_id = 0;
                int new_vertex_id = 0;
                TreeMap<Long, Integer> mapNewsSequenceID_NewsVertexID = new TreeMap();

                //writer3.append("\nmain:"+mapISS_Orig_NewsID_For_MapOut);
                writer3.flush();

                //iterate over each news article
                for (long o : mapISS_Orig_NewsID_For_MapOut.keySet()) {

                    news_node_vertex_id++;
//						writer3.append("\n-----------");
//						writer3.append("\ndebug.main:old_newID:"+o+")1:"+mapISS_MapOut.get(String.valueOf(o) +":1:")); //keywords
//						writer3.append("\ndebug.main:old_newID:"+o+")2:"+mapISS_MapOut.get(String.valueOf(o) +":2:")); //byline
//						writer3.append("\ndebug.main:old_newID:"+o+")3:"+mapISS_MapOut.get(String.valueOf(o) +":3:"+"\n\n"));
//						writer3.append("\n");
                    writer3.flush();
                    writer2.append("\n -------- -------- -------- ---------");
                    //Article DONT have both BYLINE and KEYWORDS IGNORE
                    if (!mapISS_MapOut.containsKey(String.valueOf(o) + ":2:") && //keywords
                            !mapISS_MapOut.containsKey(String.valueOf(o) + ":3:")  // byline
                            ) {
                        writer2.append("\n EMPTY:key:" + o + "\n");

                        if (!mapISS_MapOut.containsKey(String.valueOf(o) + ":1:")) {
                            writer2.append("\n value: :1: " + mapISS_MapOut.get(String.valueOf(o) + ":1:"));
                        }

                        writer2.flush();
                        continue;
                    } else {
                        writer2.append("\n NOT missing:key:" + o + "\n");

                        if (!mapISS_MapOut.containsKey(String.valueOf(o) + ":1:")) {
                            writer2.append("\n value: :1: " + mapISS_MapOut.get(String.valueOf(o) + ":1:"));
                        }
                        if (!mapISS_MapOut.containsKey(String.valueOf(o) + ":2:")) {
                            writer2.append("\n value: :2: " + mapISS_MapOut.get(String.valueOf(o) + ":2:"));
                            writer2.flush();
                        } else {
                            writer2.append("\n for key=" + String.valueOf(o) + ":2:" + " value not found..");
                            writer2.flush();
                        }
                        if (!mapISS_MapOut.containsKey(String.valueOf(o) + ":3:")) {
                            writer2.append("\n value: :3: " + mapISS_MapOut.get(String.valueOf(o) + ":3:"));
                            writer2.flush();
                        } else {
                            writer2.append("\n for key=" + String.valueOf(o) + ":3:" + " value not found..");
                            writer2.flush();
                        }

                        writer2.flush();
                    }
                    writer2.append("\n o, news_node_vertex_id -> " + o + " " + news_node_vertex_id);
                    writer2.flush();
                    mapNewsSequenceID_NewsVertexID.put(o, news_node_vertex_id);
                    //new_vertex_id=o;
                } //end for for(int o:mapISS_Orig_NewsID_For_MapOut.keySet()){
                writer2.append("\nmapISS_MapOut:" + mapISS_MapOut);
                writer2.flush();
                writer2.append("\n\n");
                //printed for easy read
                for (String s : mapISS_MapOut.keySet()) {
                    writer2.append("\n key=" + s + " value=" + mapISS_MapOut.get(s));
                    writer2.flush();
                }

                //writer3.append("\n debug. END \n");
                writer3.flush();

                TreeMap<Integer, String> mapGlobalID_ByLine_Authors = new TreeMap();
                // get global id for each vertex of byline first name+middlename+lastname
                for (long o : mapISS_Orig_NewsID_For_MapOut.keySet()) {
                    if (new_vertex_id == 0) { //first node
                        news_node_vertex_id = 1;
                    } else {
                        new_vertex_id++;
                        news_node_vertex_id = new_vertex_id;
                    }
                    //new_vertex_id=news_node_vertex_id;
                    if (mapISS_MapOut.containsKey(String.valueOf(o) + ":2:")
                            || mapISS_MapOut.containsKey(String.valueOf(o) + ":3:")
                            ) {
                        int h = 0, m = 0;
                        if (mapISS_MapOut.get(String.valueOf(o) + ":2:") != null) {
                            h = mapISS_MapOut.get(String.valueOf(o) + ":2:").size();
                        }
                        if (mapISS_MapOut.get(String.valueOf(o) + ":3:") != null) {
                            m = mapISS_MapOut.get(String.valueOf(o) + ":3:").size();
                        }

                        //
                        if (h > 0
                                || m > 0
                                ) {

                            //writer3.append("\ndebug: ---------------------------------------\n");
                            writer3.flush();

                            // adding "news" node for each news
                            //System.out.print("v "+o+" \"newsID" +"\n");
                            String t = "v " + news_node_vertex_id + " \"NEWS\""//+"\" old_id:"+o
                                    + "\n";
                            //writer3.append("v "+o+" \"newsID"+o+"\"" +"\n"); // **DONT ADD ID
                            if (!mapSSAlreadyWroteToGBADfile.containsKey(t)) {
                                writer3.append(t);
                                writer3.flush();
                            }

                            mapSSAlreadyWroteToGBADfile.put(t, "");

                        }

                    }

                    //fetch its BYLINE nodes
                    if (mapISS_MapOut.containsKey(String.valueOf(o) + ":2:")) {
                        mapIS_NEW_Counter_ByLine = mapISS_MapOut.get(String.valueOf(o) + ":2:");
                        if (new_vertex_id == 0)
                            new_vertex_id = news_node_vertex_id + 1;
                        else
                            new_vertex_id++;

                        int BYLINE_vertex_id = new_vertex_id;
                        String t = "v " + new_vertex_id + " \"" + "BYLINE"//+"("+si+")"
                                + "\"" + "\n";

                        if (!mapSSAlreadyWroteToGBADfile.containsKey(t)) {
                            writer3.append(t);
                            writer3.flush();
                        }
                        mapSSAlreadyWroteToGBADfile.put(t, "");

                        //connect BYLINE node to NEWS node
                        writer3.append("d " + news_node_vertex_id + " " + BYLINE_vertex_id + " \"has_a_byline\" \n");
                        writer3.flush();
                        String[] Organization_FName_LName = new String[3];
                        Organization_FName_LName[0] = "";
                        Organization_FName_LName[1] = "";
                        Organization_FName_LName[2] = "";
                        for (String si : mapIS_NEW_Counter_ByLine.keySet()) {

                            Organization_FName_LName = mapIS_NEW_Counter_ByLine.get(si).split("!!!");


                            mapGlobalID_ByLine_Authors.put(new_vertex_id, mapIS_NEW_Counter_ByLine.get(si));
                            int l2 = 0;
                            int l1 = 0;
                            try {
                                if (Organization_FName_LName.length > 0) {
                                    l1 = Organization_FName_LName[1].length();
                                    l2 = Organization_FName_LName[2].length();
                                }

                            } catch (Exception e) {

                            }

                            //first name and last name
                            if (l1 > 0 ||
                                    l2 > 0
                                    ) {
                                if (new_vertex_id == 0)
                                    new_vertex_id = news_node_vertex_id + 1;
                                else
                                    new_vertex_id++;
//									writer3.append("\ndebug.1:o,f,l:"+Organization_FName_LName[0]+"-"+Organization_FName_LName[1]+"-"
//												   +Organization_FName_LName[2]+"\n");
                                writer3.flush();

                                //adding author name
                                //System.out.println("v "+new_vertex_id +mapIS_NEW_Counter_ByLine.get(si)+"\n");


                                System.out.println("size of Organization_FName_LName:" + Organization_FName_LName.length);
                                if (Organization_FName_LName.length == 2) {
                                    System.out.println("size of Organization_FName_LName:" + Organization_FName_LName[0]
                                            + "!!!" + Organization_FName_LName[1]);
                                }
                                if (Organization_FName_LName.length == 3) {
                                    Organization_FName_LName[1] = Organization_FName_LName[1].replace("\\", "").replace("/", "");
                                    Organization_FName_LName[2] = Organization_FName_LName[2].replace("\\", "").replace("/", "");
                                    t = "v " + new_vertex_id + " \"" +
                                            RemoveBackSlash.removeBackSlash(
                                                    RemoveUnicodeChar.removeUnicodeChar(
                                                            Organization_FName_LName[1] + " " + Organization_FName_LName[2] //+"("+si+")"
                                                    ))
                                            + "\"" + "\n";
                                    writer3.append(t);
                                    if (!mapSSAlreadyWroteToGBADfile.containsKey(t))
                                        mapSSAlreadyWroteToGBADfile.put(t, "");

                                    writer3.append("d " + BYLINE_vertex_id + " " + new_vertex_id + " \"" + "person" + "\"" + "\n");
                                    writer3.flush();
                                } else if ((Organization_FName_LName.length == 2)) {
                                    t = "v " + new_vertex_id + " \"" +
                                            RemoveBackSlash.removeBackSlash(
                                                    RemoveUnicodeChar.removeUnicodeChar(
                                                            Organization_FName_LName[1] //+"("+si+")"
                                                    ))
                                            + "\"" + "\n";
                                    writer3.append(t);
                                    writer3.append("d " + BYLINE_vertex_id + " " + new_vertex_id + " \"" + "person" + "\"" + "\n");
                                    writer3.flush();
                                }


                                writer3.flush();
                            }

                            System.out.println("map:" + mapIS_NEW_Counter_ByLine);
                            if (Organization_FName_LName != null) {
                                System.out.println("\n orig.f.l:" + Organization_FName_LName.length);
                                //ad organization
                                if (Organization_FName_LName != null && Organization_FName_LName.length > 0
                                        && !Organization_FName_LName[0].equalsIgnoreCase("")) {

                                    if (new_vertex_id == 0)
                                        new_vertex_id = news_node_vertex_id + 1;
                                    else
                                        new_vertex_id++;
                                    int organization_vertex_id = new_vertex_id;
                                    Organization_FName_LName[0] = Organization_FName_LName[0].replace("\\", "").replace("/", "");
                                    t = "v " + organization_vertex_id + " \"" +
                                            RemoveBackSlash.removeBackSlash(
                                                    RemoveUnicodeChar.removeUnicodeChar(
                                                            Organization_FName_LName[0]
                                                    ))
                                            + "\"" + "\n";
                                    writer3.append(t);
                                    if (!mapSSAlreadyWroteToGBADfile.containsKey(t))
                                        mapSSAlreadyWroteToGBADfile.put(t, "");

                                    writer3.append("d " + BYLINE_vertex_id + " " + organization_vertex_id + " \"" + "organization" + "\"" + "\n");
                                    writer3.flush();

                                }
                            }

                        }

                    }


                    // BEGIN KEYWORDS
                    if (!mapISS_MapOut.containsKey(String.valueOf(o) + ":3:")) {
//							writer3.append("debug:mapSS_NEW_Curr_KeywordNameValue:NOT AVAILABLE?:(OLD_NEWS_ID):"
//											+o+" map:"
//											+mapISS_MapOut.containsKey(String.valueOf(o) +":3:")+"\n");

                        writer2.append("\n :3: not available for key=" + String.valueOf(o) + ":3:");
                        writer2.flush();
                        writer3.flush();
                    }

                    if (mapISS_MapOut.containsKey(String.valueOf(o) + ":3:")) {
                        writer2.append("\n :3: AVAILABLE for key=" + String.valueOf(o) + ":3:");
                        writer2.flush();

                        mapSS_NEW_Curr_KeywordNameValue = mapISS_MapOut.get(String.valueOf(o) + ":3:");

                        //System.out.println("mapSS_NEW_Curr_KeywordNameValue:"+mapSS_NEW_Curr_KeywordNameValue);
                        //writer3.append("debug:mapSS_NEW_Curr_KeywordNameValue:"+mapSS_NEW_Curr_KeywordNameValue+"\n");
                        writer3.flush();
                        int c10 = 0;
                        int miss_c10 = 0;

                        int max_c2 = mapII_EachArticle_MaxC2.get(o);
                        //
                        while (c10 < max_c2 + 4) { // i assume max 25 keywords in a article
                            //atomic <name,value> of keyword stored in mapSS_NEW_Curr_KeywordNameValue

                            if (mapSS_NEW_Curr_KeywordNameValue.containsKey("name:" + c10)
                                    && mapSS_NEW_Curr_KeywordNameValue.containsKey("value:" + c10)) {
                                //writer3.append(" key--->"+"name:"+c10+"\n");
                                writer3.flush();
                                //
                                String alreadyExistingKeywordNode = mapSS_NEW_Curr_KeywordNameValue.get("name:" + c10) +
                                        mapSS_NEW_Curr_KeywordNameValue.get("value:" + c10);

//								   writer3.append("debug:map_Keyword_NameValue_Already_Existed_For_GBAD_OUT:"+map_Keyword_NameValue_Already_Existed_For_GBAD_OUT
//										   		 +"\n"+"debug::"+alreadyExistingKeywordNode+"\n");
                                writer3.flush();
                                if (!map_Keyword_NameValue_Already_Existed_For_GBAD_OUT.containsKey(alreadyExistingKeywordNode)) {
                                    //add NODE "keyword"

                                    if (new_vertex_id == 0)
                                        new_vertex_id = news_node_vertex_id + 1;
                                    else
                                        new_vertex_id++;

                                    int keyword_NODE_vertex_id = new_vertex_id;
                                    //System.out.println("v "+new_vertex_id+ " "+"\""+"keyword"+"\""+"\n");
                                    String t = "v " + new_vertex_id + " " + "\"" + "keyword" + "\"" + "\n";
                                    if (!mapSSAlreadyWroteToGBADfile.containsKey(t)) {
                                        writer3.append(t);
                                        writer3.flush();
                                    }
                                    mapSSAlreadyWroteToGBADfile.put(t, "");
                                    //add NODE vertex for VALUE of NAME
                                    new_vertex_id++;
                                    //System.out.println("v "+new_vertex_id+ " "+"\""+mapSS_NEW_Curr_KeywordNameValue.get("name:"+c10)+"\""+"\n");
                                    t = "v " + new_vertex_id + " " + "\"" +
                                            RemoveBackSlash.removeBackSlash(
                                                    RemoveUnicodeChar.removeUnicodeChar(
                                                            mapSS_NEW_Curr_KeywordNameValue.get("name:" + c10))
                                            ) + "\"" + "\n";
                                    //ading name vertex
                                    writer3.append(t);
                                    writer3.flush();

                                    new_vertex_id++;
                                    //add NODE vertex for VALUE of VALUE
//								   System.out.println("v "+new_vertex_id+ " "+"\""
//										   +
//										   removeBackSlash.removeBackSlash(
//												   	removeUnicodeChar.removeUnicodeChar(
//												   			mapSS_NEW_Curr_KeywordNameValue.get("value:"+c10)
//												   				))
//												   				+"\""+"\n") ;

                                    t = "v " + new_vertex_id + " " + "\""
                                            + mapSS_NEW_Curr_KeywordNameValue.get("value:" + c10).replace("}", "").replace("]", "")
                                            + "\"" + "\n";
                                    if (!mapSSAlreadyWroteToGBADfile.containsKey(t)) {
                                        writer3.append(t);
                                        writer3.flush();
                                    }
                                    mapSSAlreadyWroteToGBADfile.put(t, "");

                                    //System.out.println("d "+(new_vertex_id-2) +" "+(new_vertex_id-1) +" "+"\""+"name"+"\""+"\n");

                                    writer3.append("d " + (new_vertex_id - 2) + " " + (new_vertex_id - 1) + " " + "\"" + "name" + "\"" + "\n");
                                    writer3.flush();
                                    //System.out.println("d "+(new_vertex_id-2)+" "+new_vertex_id +" "+"\""+"value"+"\""+"\n");
                                    writer3.append("d " + (new_vertex_id - 2) + " " + new_vertex_id + " " + "\"" + "value" + "\"" + "\n");
                                    writer3.flush();
                                    //System.out.println("d "+news_node_vertex_id +" "+(new_vertex_id-2)  +" "+"\"has_a_keyword"+"\""+"\n");
                                    writer3.append("d " + news_node_vertex_id + " "
                                            + keyword_NODE_vertex_id + " " + "\"has_a_keyword" + "\"" + "\n");

                                    writer3.flush();

                                    //put already exists
                                    map_Keyword_NameValue_Already_Existed_For_GBAD_OUT.put(
                                            alreadyExistingKeywordNode,
                                            String.valueOf(keyword_NODE_vertex_id)  //keyword node
                                    );

                                } else {
                                    //add EDGE between already existing node "keyword" and NEWS node.
                                    //new_vertex_id++;
//									   System.out.println("d. "+o+" "+
//											   map_Keyword_NameValue_Already_Existed_For_GBAD_OUT.get(alreadyExistingKeywordNode)//keyword node
//											   +" "+"\"has_a_keyword"+"\""+"\n");
                                    writer3.append("d " + news_node_vertex_id + " " +
                                            map_Keyword_NameValue_Already_Existed_For_GBAD_OUT.get(alreadyExistingKeywordNode)//keyword node
                                            + " " + "\"has_a_keyword" + "\"" + "\n");
                                    writer3.flush();
                                }

                                // Add name and value to the above added "keyword" node
//								   System.out.println("ind: N:"+"name:"+c10
//								   			 + " V:"+mapSS_NEW_Curr_KeywordNameValue.get("name:"+c10) );

                            } else {
                                miss_c10++;

                            }
                            if (miss_c10 > 15) {
                                //break;
                            }
                            c10++;
                        }
                    }
//							for(String si:mapSS_NEW_Curr_KeywordNameValue.keySet()){
//								if(si.indexOf("name") ==-1 && si.indexOf("value")==-1)
//									continue;
//
//								if(!mapGlobalID_KeyValuePair.containsValue(si+"!!!"+mapSS_NEW_Curr_KeywordNameValue.get(si))){
//									new_vertex_id++;
//									mapGlobalID_KeyValuePair.put(new_vertex_id,
//																si+"!!!"+mapSS_NEW_Curr_KeywordNameValue.get(si));
//									System.out.println("v "+new_vertex_id +" name-val-pair="
//																	+si+"!!!"+mapSS_NEW_Curr_KeywordNameValue.get(si));
//									//System.out.println("map:"+mapSS_NEW_Curr_KeywordNameValue);
//								}
//
//							}
                    // END KEYWORDS

                } //end for for(int o:mapISS_Orig_NewsID_For_MapOut.keySet()){


            } // is_generate_GBAD_output=true
            //

            System.out.println("\nnew_keywords_got_using_inputFile_unique_keywords_from_other_sources:" +
                    new_keywords_got_using_inputFile_unique_keywords_from_other_sources);


            System.out.println("\nmapSS_Curr_KeywordNameValue:"
            );
            System.out.println("given inputFileName_for_thehindu:" + inputFileName_for_thehindu);
            System.out.println("mapInter2:" + mapInter2.size());
            System.out.println("\nmapII_EachArticle_MaxC2:" +
                    mapII_EachArticle_MaxC2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // get only 30
    public static TreeMap<Integer, String> for_loop_starting_from_a_index_and_get_30_elements(
            int startPoint,
            TreeMap<Integer, String> mapRangeDateTimeFilter,
            int size_of_elements_in_each_set
    ) {
        TreeMap<Integer, String> mapIndependentRangeOfDates = new TreeMap<Integer, String>();
        int outerCount = 0;
        try {

            for (int s : mapRangeDateTimeFilter.keySet()) {
                //
                if (s >= startPoint && mapIndependentRangeOfDates.size() < size_of_elements_in_each_set) {
                    mapIndependentRangeOfDates.put(s, mapRangeDateTimeFilter.get(s));
                } else if (s >= startPoint) {
                    System.out.println("breakin...from loop:" + mapIndependentRangeOfDates.size()
                            + ";startPoint:" + startPoint + ";s:" + s);
//					 outerCount++;
//					 mapIndependentRangeOfDates.put(outerCount, mapIndependentRangeOfDates);
//					 mapIndependentRangeOfDates=new TreeMap();
                    break;
                }
                //System.out.println(s+"\n");
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return mapIndependentRangeOfDates;
    }

    //get_First_And_Last_Key_in_MAP
    private static TreeMap<Integer, String> get_First_And_Last_Key_in_MAP(TreeMap<Integer, String> mapIn) {
        // TODO Auto-generated method stub
        TreeMap<Integer, String> mapOut = new TreeMap();
        String first = "";
        String last = "";
        try {
            int c = 0;
            for (int i : mapIn.keySet()) {
                c++;
                if (c == 1) {
                    first = mapIn.get(i);
                }
                last = mapIn.get(i);
            }

            mapOut.put(1, first);
            mapOut.put(2, last);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapOut;
    }

    //splitting date range to mutiple set. each set has 30 days.
    // here we filter out only set of months we want (ex: only april and may)
    public static TreeMap<Integer, TreeMap<Integer, String>> SplitGivenDateRange_To_multiple_set_each_set_has_30_days
    (
            String StartDate,
            String EndDate,
            int Number_of_elements_in_each_set,
            FileWriter writerdebugFile
    ) {
        TreeMap<Integer, TreeMap<Integer, String>> mapOut = new TreeMap();
        try {
            TreeMap<Integer, String> mapRangeDateTime =
                    Get_NewsPaper_ArchiveURL.thehindu_print("/Users/lenin/Downloads/dummy.daterange.txt",//outFile,
                            "", //baseURL
                            Integer.valueOf(StartDate),
                            Integer.valueOf(EndDate));
            TreeMap<Integer, String> mapRangeDateTimeFilter = new TreeMap();

            writerdebugFile.append("\n !!!###Number_of_elements_in_each_set#### " + Number_of_elements_in_each_set + "\n");
            writerdebugFile.flush();

            int counter = 0;
            for (int i : mapRangeDateTime.keySet()) {
                if (mapRangeDateTime.get(i).indexOf("2014/04") >= 0
//				   || mapRangeDateTime.get(i).indexOf("2014/05")>=0
//				   || mapRangeDateTime.get(i).indexOf("2014/06")>=0
//				   || mapRangeDateTime.get(i).indexOf("2014/07")>=0
//				   || mapRangeDateTime.get(i).indexOf("2014/08")>=0
//				   || mapRangeDateTime.get(i).indexOf("2014/09")>=0
//				   || mapRangeDateTime.get(i).indexOf("2014/10")>=0
//				   || mapRangeDateTime.get(i).indexOf("2014/11")>=0
//				   || mapRangeDateTime.get(i).indexOf("2014/12")>=0
                        )
                    if (Find_isGivenString_ValidDate.isValidDate(mapRangeDateTime.get(i), "yyyy/MM/dd")) {
                        counter++;
                        mapRangeDateTimeFilter.put(counter, mapRangeDateTime.get(i).replace("/", "-"));
                    }

            }
            System.out.println("mapRangeDateTime:" + mapRangeDateTime);
            System.out.println("mapRangeDateTimeFilter.size:" + mapRangeDateTimeFilter.values());
            System.out.println("mapRangeDateTimeFilter.size:" + mapRangeDateTimeFilter.size());
            System.out.println("--------------");
            int outerCount = 1;
            TreeMap<Integer, String> mapIndependentRangeOfDates = new TreeMap();
            int startPoint = 1;
            boolean isTrue = true;
            while (isTrue) {
                System.out.println("-------startPoint-------" + startPoint);
                mapIndependentRangeOfDates = for_loop_starting_from_a_index_and_get_30_elements(startPoint,
                        mapRangeDateTimeFilter,
                        Number_of_elements_in_each_set);

                if (mapIndependentRangeOfDates.size() < Number_of_elements_in_each_set) {
                    System.out.println("mapIndependentRangeOfDates.size:(break):" + mapIndependentRangeOfDates.size());
                    isTrue = false;
                    //break;
                } else {
                    System.out.println("mapIndependentRangeOfDates.size:" + mapIndependentRangeOfDates.size());
                    mapOut.put(outerCount, mapIndependentRangeOfDates);
                    outerCount++;
                    startPoint++;
                }

            }

            //check if the string is date

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapOut;
    }

    // Splitting
    public static TreeMap<Integer, TreeMap<Integer, String>> splitting_Convert_TimeSlice_OneBigFile_To_30DaysEachFile(
            String inputBig_Complete_File_With_TimeStamp_EachLine,
            String start_date,
            String end_date,
            int number_of_elements_in_each_set,
            FileWriter debugFile
    ) {
        TreeMap<Integer, TreeMap<Integer, String>> mapOut = new TreeMap();
        try {

            mapOut =
                    SplitGivenDateRange_To_multiple_set_each_set_has_30_days(start_date,
                            end_date,
                            number_of_elements_in_each_set,
                            debugFile);
            System.out.println("splitting_Convert_TimeSlice_OneBigFile_To_30DaysEachFile.mapOut:" + mapOut.size());

        } catch (Exception e) {

        }
        return mapOut;
    }

    //SplitGivenSingleFile_to_multiple_files_Each_file_corresponds_to_one_YYYYMMDD
    private static void SplitGivenSingleFile_to_multiple_files_Each_file_corresponds_to_one_YYYYMMDD(
            String inputBigFileForSplitting, String outputStagingFolder, TreeMap<String, String> MAPuniqueYYYYMMDD) {
        // TODO Auto-generated method stub
        try {
            System.out.println("inputBigFileForSplitting:" + inputBigFileForSplitting);
            BufferedReader reader = new BufferedReader(new FileReader(inputBigFileForSplitting));
            String line = "";
            for (String yyyymmdd : MAPuniqueYYYYMMDD.keySet()) {
                FileWriter writer = new FileWriter(outputStagingFolder + yyyymmdd + ".txt");
                reader = new BufferedReader(new FileReader(inputBigFileForSplitting));
                System.out.println("curr out file -> " + outputStagingFolder + yyyymmdd + ".txt");
                //
                while ((line = reader.readLine()) != null) {
                    String orig_line = line;
                    if (line.indexOf("key=") >= 0) {
                        line = line.substring(0, line.indexOf("key="));
                    }


                    if (line.indexOf(yyyymmdd) >= 0) {
                        writer.append(orig_line + "\n");
                        writer.flush();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // generate NODES and EDGES string
    private static TreeMap<Integer, TreeMap<Integer, String>> generateNodesANDedges_currDocument_allSentences(
            int lineNo,
            TreeMap<String, TreeMap<String, String>> mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence,
            TreeMap<Integer, TreeMap<Integer, String>> map_graph_Topology,
            TreeMap<String, String> map_graph_Topology_sourceNdest_TYPE,
            TreeMap<String, Integer> map_SourceFeature_Idx_mapping,
            TreeMap<Integer, TreeMap<Integer, String>> map_idx_interested_lineNo_Tokens_from_inputFile,
            TreeMap<String, String> map_checksum_score,
            TreeMap<String, String> map_CompressedSentence_score,
            boolean isSOPprint,
            FileWriter writer_debug,
            FileWriter writer_global_repository_to_save_sentenceWise_sentiments,
            boolean is_debug_more
    ) {
        String last_key = "";
        Stemmer stemmer = new Stemmer();
        String outString = "";
        TreeMap<Integer, TreeMap<Integer, String>> mapOut_final = new TreeMap<Integer, TreeMap<Integer, String>>();
        TreeMap<Integer, String> mapOut = new TreeMap<Integer, String>();
        TreeMap<String, String> map_isAlreadyProcessed = new TreeMap<String, String>();
        Float curr_senten_sentime_score = 0.f;
        int stat_hit_on_repo_semantic = 0;
        int stat_hit_on_repo_sentimen = 0;
        int stat_hit_on_repo_NLPtagging = 0;
        boolean is_one_time_ran = false;

        // TODO Auto-generated method stub
        try {
            //
            if (isSOPprint)
                System.out.println("----------------------method genNodeANDedges_currDoc lineNo:" + lineNo);
            TreeMap<Integer, String> mapGraphElementID_Value = new TreeMap<Integer, String>();
            int max_map_graph_Topology = map_graph_Topology.get(1).size();
            int cnt = 1;

            if (isSOPprint)
                System.out.println("max_map_graph_Topology:" + max_map_graph_Topology);

            // *** REFERENCE - take size of noun
            int max_size_No_sentences = mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + map_SourceFeature_Idx_mapping.get("$_n")).size();

            int id_increment_for_mapOut = 0;
            TreeMap<Integer, String> map_outString_nodepairedge = new TreeMap<Integer, String>();
            String curr_nodepair_edge = "";

            // each
            while (cnt <= max_map_graph_Topology) {
                //
                //int curr_feat_graph_element_mapping_idx=mapSourceFeatures.get(curr_feature_for_graph_element);
//						TreeMap<String, String> map_curr_feat_value=
//															mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo+":"+curr_feat_graph_element_mapping_idx);

//						System.out.println("mapsource.value:"+mapSourceFeatures.get(curr_feature_for_graph_element)
//											+" curr_feature_for_graph_element:"+curr_feature_for_graph_element
//											+" map_value:"+map_curr_feat_value);

                // each sentence -> noun, verb and sentence itself
                //
                if (isSOPprint) {
                    System.out.println("#-----PROCESSING.... mapping for node pair and edge:" +
                            map_graph_Topology.get(1).get(cnt) + "!!!" + map_graph_Topology.get(2).get(cnt) + "!!!" + map_graph_Topology.get(4).get(cnt));
                    System.out.println("lineNo:" + lineNo + " " + " cnt:" + cnt + " "
                            + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + map_SourceFeature_Idx_mapping.get("$_n"))
                            + " map_SourceFeature_Idx_mapping:" + map_SourceFeature_Idx_mapping
                            + " map_graph_Topology.get(4).get(cnt):" + map_graph_Topology.get(4).get(cnt)
                    );
                }
                //
                curr_nodepair_edge = map_graph_Topology.get(1).get(cnt) + " " + map_graph_Topology.get(2).get(cnt) + " (" + map_graph_Topology.get(4).get(cnt) + ")";
                curr_nodepair_edge = curr_nodepair_edge.replace("((", "(").replace("))", ")");
                String edge_feature = map_graph_Topology.get(4).get(cnt).replace("(", "").replace(")", "");

                if (isSOPprint)
                    System.out.println("curr_nodepair_edge:" + curr_nodepair_edge);

                String edge_feature_given = ""; //debug purpose
                String remark = "edge_feature->" + edge_feature;
                int cnt2 = 1;
                String source_dest_debug = map_graph_Topology.get(1).get(cnt) + ":" + map_graph_Topology.get(2).get(cnt);
                int index_source_node = -1, index_dest_node = -1;
                // source AND dest cant be same.
                if (map_graph_Topology.get(1).get(cnt).equals(map_graph_Topology.get(2).get(cnt))) {
                    System.out.println("source AND dest cant be same.");
                    cnt++;
                    continue;
                }

                //get TYPE
                String curr_sourceNdest_TYPE = map_graph_Topology_sourceNdest_TYPE.get(map_graph_Topology.get(1).get(cnt) + ":" + map_graph_Topology.get(2).get(cnt));

//						writer_debug.append("\n TYPE found (processing): "+curr_sourceNdest_TYPE
//										  +" for source destination->"+(map_graph_Topology.get(1).get(cnt) +":"+map_graph_Topology.get(2).get(cnt) )
//										  +" <--- TYPE.1 ,2,3 & TYPE 5 will only process -->");
                writer_debug.flush();

                // both source and dest from tags such as _nn and _vbz  -->TYPE.1
                // one hard-coded and another from tags such as _vbz -->TYPE.2
                // $_n->()$keyw where $keyw is a *token from inputFile(Each line) -->TYPE.3
                // source N destination both from *tokens from inputFile(Each line)  -->TYPE.4
                // Both are FIXED. example [A]->()[B] -->TYPE.5
                // source (or dest) is fixed [fixed] and other one is from a token (example: $byline) -->TYPE.6

                //Only TYPE.3 and TYPE.4 allowed in this method
                if (!curr_sourceNdest_TYPE.equalsIgnoreCase("TYPE.1")
                        && !curr_sourceNdest_TYPE.equalsIgnoreCase("TYPE.2")
                        && !curr_sourceNdest_TYPE.equalsIgnoreCase("TYPE.5") // type.5 both source n dest hard-coded
                        && !curr_sourceNdest_TYPE.equalsIgnoreCase("TYPE.3")
                        ) {
                    //&& !curr_sourceNdest_TYPE.equalsIgnoreCase("TYPE.6")){
                    if (isSOPprint)
                        System.out.println("%%%%%%%%%% NOT TYPE.1 or TYPE.2 or TYPE.5 ----> GOT=" + curr_sourceNdest_TYPE);

//							writer_debug.append("\n %%%%%%%%%% CONTINUE.. NOT PROCESSING TYPE.1 or TYPE.2 or TYPE.5 GOT="+curr_sourceNdest_TYPE
//												+"<--->source,dest->"+source_dest_debug);
//							writer_debug.flush();
                    cnt++;
                    continue; // iterate next node pair
                } else {
//							writer_debug.append("\n ########## SUCCESS.1 ... PROCESSING ...curr_sourceNdest_TYPE:"+curr_sourceNdest_TYPE
//															+"<--->source,dest->"+source_dest_debug);
                    writer_debug.flush();
                }
                String concPartOutString = "";
                boolean is_source_node_verb = false;
                boolean is_dest_node_verb = false;
                // BOTH source and destination has $_ OR at least one of SOURCE/DESTINATION has $_
                if (map_graph_Topology.get(1).get(cnt).indexOf("$_") >= 0 || //source
                        map_graph_Topology.get(2).get(cnt).indexOf("$_") >= 0) { //destination

                    //writer_debug.append("\n ########## SUCCESS.2 ... PROCESSING ...curr_sourceNdest_TYPE:");
                    writer_debug.flush();

                    // which one source or destination is verb (in order to apply stemming)
                    if (map_graph_Topology.get(1).get(cnt).indexOf("$_v") >= 0)
                        is_source_node_verb = true;
                    if (map_graph_Topology.get(2).get(cnt).indexOf("$_v") >= 0)
                        is_dest_node_verb = true;

                    //get index of source and destination
                    if (map_SourceFeature_Idx_mapping.containsKey(map_graph_Topology.get(1).get(cnt))) {
                        index_source_node = Integer.valueOf(map_SourceFeature_Idx_mapping.get(map_graph_Topology.get(1).get(cnt)));
                    }
                    if (map_SourceFeature_Idx_mapping.containsKey(map_graph_Topology.get(2).get(cnt))) {
                        index_dest_node = Integer.valueOf(map_SourceFeature_Idx_mapping.get(map_graph_Topology.get(2).get(cnt)));
                    }
                    // both source and destination are hard-coded, then no need to run for each sentence
                    if (isSOPprint)
                        System.out.println("@@debug.case.1 source,dest->" + index_source_node + "," + index_dest_node + " cnt=" + cnt);

                    //writer_debug.append("\n@@debug.case.1 source,dest->"+index_source_node+","+index_dest_node+" cnt="+cnt);
                    writer_debug.flush();

                    int index_edge_feature = -1;//
                    concPartOutString = "";
                    //iterate each sentence of curr line
                    while (cnt2 <= max_size_No_sentences) {

                        if (isSOPprint) {
                            try {
                                if (isSOPprint)
                                    System.out.println("(out)node pair,edge->cnt2" + cnt2 + "<--max_size_No_sentences:" + max_size_No_sentences
                                            //+mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo+":"+index_source_node).get(lineNo+":"+cnt2)+"!!!"+
                                            //mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo+":"+index_dest_node).get(lineNo+":"+cnt2)+"!!!"
                                            //mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo+":"+index_edge_feature).get(lineNo+":"+cnt2)
                                    );
                            } catch (Exception e) {
                                System.out.println("ERROR ON SOP");
                            }
                        }
                        //
                        if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_dest_node) != null) {
                            //DEBUG
                            if (is_debug_more) {
                                writer_debug.append("\n " + "generateNodesANDedges_currDocument_allSentences()-> (lineNo=" + lineNo + ")"
                                        + "--mapping:source<->dest=>" + map_graph_Topology.get(1).get(cnt) + "<->" + map_graph_Topology.get(2).get(cnt)
                                        + "--index_source_node:" + index_source_node
                                        + "--index_dest_node:" + index_dest_node
                                );
                                //
                                if (is_one_time_ran == false) {
                                    writer_debug.append("\n given line_no=" + lineNo + "--source=>"
                                            + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_source_node)
                                            + "--dest=>" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_dest_node));
                                    is_one_time_ran = true;
                                }

                                writer_debug.append("\n mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence:"
                                        + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence);
                                writer_debug.flush();
                            }
                        }

                        writer_debug.flush();

                        //single char source or destination value dont write.
                        concPartOutString = "";
                        //source
                        try {
                            //writer_debug.append("\n 11. "); writer_debug.flush();
                            //SOURCE is a VERB
                            if (is_source_node_verb) {
                                //writer_debug.append("\n 22. "); writer_debug.flush();
                                if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_source_node).get(lineNo + ":" + cnt2).length() <= 1
                                        || !mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_source_node).containsKey(lineNo + ":" + cnt2)
                                        ) {
                                    cnt2++;
                                    //writer_debug.append("\n 33. "); writer_debug.flush();
                                    writer_debug.append("\n continue.22->");
                                    writer_debug.flush();
                                    continue;
                                }
                                //writer_debug.append("\n 44. "); writer_debug.flush();

                                concPartOutString =
                                        stemmer.stem(mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_source_node).get(lineNo + ":" + cnt2));

                            } else if (map_graph_Topology.get(1).get(cnt).indexOf("$_n") >= 0) { //can be noun or noun2???
                                // OR it can be noun (NO STEMMING)
                                writer_debug.append("\n 222. cnt2:" + cnt2 + "--lineNo:" + lineNo + "--map:" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_source_node));
                                writer_debug.flush();

                                if (!mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_source_node).containsKey(lineNo + ":" + cnt2)) {
                                    cnt2++;
                                    continue;
                                }

                                concPartOutString = mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_source_node).get(lineNo + ":" + cnt2);

                            } else {

                                // only
//										if(mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey(lineNo+":"+index_source_node)){
//											if(mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo+":"+index_source_node).get(lineNo+":"+cnt2).length()<=1){
//												cnt2++;
//												writer_debug.append("\n 6. "); writer_debug.flush();
//												continue;
//											}
//										}

                                //writer_debug.append("\n 77. "); writer_debug.flush();

                                // it must be token ($) or tag ($_n or $_v
                                if (map_idx_interested_lineNo_Tokens_from_inputFile.containsKey(index_source_node)) {
                                    //writer_debug.append("\n 77.1 index_source_node:"+index_source_node+" lineNo:"+lineNo+"\n"+map_idx_interested_lineNo_Tokens_from_inputFile.get(index_source_node).get(lineNo));
                                    writer_debug.flush();
                                    concPartOutString = map_idx_interested_lineNo_Tokens_from_inputFile.get(index_source_node).get(lineNo);
                                } else if (map_graph_Topology.get(1).get(cnt).indexOf("[") >= 0) { //hard-coded
                                    //writer_debug.append("\n 77.2 "); writer_debug.flush();
                                    concPartOutString = map_graph_Topology.get(1).get(cnt);
                                } else {
                                    //writer_debug.append("\n continue.1->"+index_source_node+"--"+map_SourceFeature_Idx_mapping.get(map_graph_Topology.get(1).get(cnt)));
                                    writer_debug.flush();
                                    cnt2++;
                                    continue;
                                }


                            }
                        } catch (Exception e) {
                            concPartOutString = map_graph_Topology.get(1).get(cnt) + "[Error]-" + e.getMessage();
                        }
                        //destination
                        try {
                            if (is_dest_node_verb) {
                                if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_dest_node).get(lineNo + ":" + cnt2).length() <= 1
                                        || !mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_dest_node).containsKey(lineNo + ":" + cnt2)

                                        ) {
                                    cnt2++;
                                    continue;
                                }
                                //writer_debug.append("\n 88.1 "); writer_debug.flush();
                                concPartOutString = concPartOutString + "!#!#"
                                        + stemmer.stem(mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_dest_node).get(lineNo + ":" + cnt2));

                            } else if (map_graph_Topology.get(1).get(cnt).indexOf("$_n") >= 0) { //can be noun or noun2???
                                // OR it can be noun (NO STEMMING)

                                if (!mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_dest_node).containsKey(lineNo + ":" + cnt2)) {
                                    cnt2++;
                                    continue;
                                }
                                //writer_debug.append("\n 88.2 "); writer_debug.flush();
                                concPartOutString = concPartOutString + "!#!#" +
                                        mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_dest_node).get(lineNo + ":" + cnt2);

                            } else {
                                int idx = map_graph_Topology.get(2).get(cnt).indexOf("[");
                                //destination is a VERB
                                if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey(lineNo + ":" + index_dest_node)) {
                                    if (!mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_dest_node).containsKey(lineNo + ":" + cnt2)) {
                                        cnt2++;
                                        continue;
                                    }
                                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_dest_node).get(lineNo + ":" + cnt2).length() <= 1) {
                                        cnt2++;
                                        if (is_debug_more) {
                                            writer_debug.append("\n continue...");
                                            writer_debug.flush();
                                        }
                                        writer_debug.append("\n continue.2->");
                                        writer_debug.flush();
                                        continue;
                                    }
                                    //writer_debug.append("\n 88.3 concPartOutString:"+concPartOutString); writer_debug.flush();
                                    concPartOutString = concPartOutString + "!#!#"
                                            + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_dest_node).get(lineNo + ":" + cnt2);
                                }
                                // it must be token ($) or tag ($_n or $_v
                                else if (map_idx_interested_lineNo_Tokens_from_inputFile.containsKey(index_source_node)) {
                                    //writer_debug.append("\n 88.4 "); writer_debug.flush();
                                    concPartOutString = concPartOutString + "!#!#" + map_idx_interested_lineNo_Tokens_from_inputFile.get(index_dest_node).get(lineNo);
                                    writer_debug.flush();
                                } else if (idx >= 0) { //hard-coded destination
                                    //writer_debug.append("\n 88.5 "); writer_debug.flush();
                                    concPartOutString = concPartOutString + "!#!#" + map_graph_Topology.get(2).get(cnt);
                                } else {
                                    cnt2++;
                                    writer_debug.append("\n continue.111->" + index_dest_node + "--" + map_SourceFeature_Idx_mapping.get(map_graph_Topology.get(2).get(cnt))
                                            + "--dest:" + map_graph_Topology.get(2).get(cnt));
                                    writer_debug.flush();
                                    //writer_debug.append("\n 8. continue"); writer_debug.flush();
                                    continue;
                                }
                            }
                        } catch (Exception e) {
                            writer_debug.append("\nERROR CAUGT " + e.getMessage());
                            writer_debug.flush();
                            //ERROR
                            concPartOutString = concPartOutString + "!#!#" + map_graph_Topology.get(2).get(cnt) + "!#!#[Error]:" + e.getMessage();
                            concPartOutString = "";
                        }

                        //apply edge feature (function) and get value.
                        if (edge_feature.indexOf("fn.") >= 0) {
                            if (edge_feature.equalsIgnoreCase("fn.sentiment")) {
                                index_edge_feature = Integer.valueOf(map_SourceFeature_Idx_mapping.get(edge_feature));//4
                                String curr_sentenc_checksum = "";

                                if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_edge_feature).containsKey(lineNo + ":" + cnt2))
                                    curr_sentenc_checksum = Calc_checksum.calc_checksum_for_string(
                                            mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_edge_feature).get(lineNo + ":" + cnt2)
                                    );

                                String curr_sentenc_compressed = "";
                                // compress the current sentence.
                                if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_edge_feature).containsKey(lineNo + ":" + cnt2))
                                    curr_sentenc_compressed =
                                            Clean_retain_only_alpha_numeric_characters
                                                    .clean_retain_only_alpha_numeric_characters(
                                                            mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_edge_feature).get(lineNo + ":" + cnt2)
                                                            , true);

                                //
                                if (map_checksum_score.containsKey(curr_sentenc_checksum)) {
                                    curr_senten_sentime_score = Float.valueOf(map_checksum_score.get(curr_sentenc_checksum));
                                    stat_hit_on_repo_sentimen++;
                                } else if (map_CompressedSentence_score.containsKey(curr_sentenc_compressed)) {
                                    //
                                    curr_senten_sentime_score = Float.valueOf(map_CompressedSentence_score.get(curr_sentenc_compressed));
                                    stat_hit_on_repo_sentimen++;
                                } else {
                                    //
                                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_edge_feature).containsKey(lineNo + ":" + cnt2)) {
                                        //
                                        curr_senten_sentime_score = sentiment(mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_edge_feature).get(lineNo + ":" + cnt2)
                                                , writer_global_repository_to_save_sentenceWise_sentiments
                                                , true //is_write_to_repository
                                        );

                                        map_checksum_score.put(curr_sentenc_checksum, String.valueOf(curr_senten_sentime_score));
                                    }

                                }

                                if (isSOPprint)
                                    System.out.println("fn curr_nodepair_edge**** ->" + curr_nodepair_edge);

                                //
                                outString = concPartOutString + "!#!#"
                                        + curr_senten_sentime_score
                                        //+sentiment(mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo+":"+index_edge_feature).get(lineNo+":"+cnt2))
                                        //+mapEachDOClineNoCnt_Sentiment.get(lineNo+":"+cnt2)
                                        + "!#!#remark:edgeFunction(1):" + edge_feature
                                        + "!#!#" + map_graph_Topology.get(1).get(cnt) + " " + map_graph_Topology.get(2).get(cnt) + " " + remove_brackets(edge_feature)
                                        + "!#!#"
                                ;

                                outString = outString.replace("!!!!!!", "!!!");

                                if (!mapOut.containsValue(outString)) {
                                    id_increment_for_mapOut++;
                                    if (isSOPprint)
                                        System.out.println("outString.1:" + outString + " for *key cnt2=" + cnt + " id_increment_for_mapOut:" + id_increment_for_mapOut);
                                    writer_debug.append("\noutString.1:" + outString + "!#!#" + "\n");
                                    writer_debug.flush();
                                    mapOut.put(id_increment_for_mapOut, outString);
                                    map_outString_nodepairedge.put(id_increment_for_mapOut, curr_nodepair_edge);
                                }
                            }
                            concPartOutString = "";
                            //outString="";
                        } else if (edge_feature.indexOf("$") == -1 && edge_feature.indexOf("$_") == -1) { //  == hard-coded value -> not a token from input file
                            outString = concPartOutString + "!#!#"
                                    + edge_feature
                                    + "!#!#remark:edgeFunction(2):" + edge_feature
                                    + "!#!#" + map_graph_Topology.get(1).get(cnt) + " " + map_graph_Topology.get(2).get(cnt) + " " + remove_brackets(edge_feature)
                                    + "!#!#";
                            outString = outString.replace("!!!!!!", "!!!");

                            if (!mapOut.containsValue(outString)) {
                                id_increment_for_mapOut++;
                                if (isSOPprint)
                                    System.out.println("outString.1:" + outString + " for *key cnt2=" + cnt + " id_increment_for_mapOut:" + id_increment_for_mapOut);
                                writer_debug.append("\noutString.1:" + outString + "!#!#" + "\n");
                                writer_debug.flush();

                                mapOut.put(id_increment_for_mapOut, outString);
                                map_outString_nodepairedge.put(id_increment_for_mapOut, curr_nodepair_edge);
                                //outString="";
                            }
                            concPartOutString = "";
                        } else if (edge_feature.indexOf("$_") >= 0) { //edge feature is a tag such as _n or _v
                            index_edge_feature = Integer.valueOf(map_SourceFeature_Idx_mapping.get(edge_feature));

                            String curr_sentenc_checksum = Calc_checksum.calc_checksum_for_string(
                                    mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_edge_feature).get(lineNo + ":" + cnt2)
                            );

                            // compress the current sentence.
                            String curr_sentenc_compressed =
                                    Clean_retain_only_alpha_numeric_characters.clean_retain_only_alpha_numeric_characters(
                                            mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_edge_feature).get(lineNo + ":" + cnt2)
                                            , true);

                            if (map_checksum_score.containsKey(curr_sentenc_checksum)) {
                                curr_senten_sentime_score = Float.valueOf(map_checksum_score.get(curr_sentenc_checksum));
                                stat_hit_on_repo_sentimen++;
                            } else if (map_CompressedSentence_score.containsKey(curr_sentenc_compressed)) {
                                //
                                curr_senten_sentime_score = Float.valueOf(map_CompressedSentence_score.get(curr_sentenc_compressed));
                                stat_hit_on_repo_sentimen++;
                            } else {
                                curr_senten_sentime_score = sentiment(mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_edge_feature).get(lineNo + ":" + cnt2)
                                        , writer_global_repository_to_save_sentenceWise_sentiments
                                        , true //is_write_to_repository
                                );
                                map_checksum_score.put(curr_sentenc_checksum, String.valueOf(curr_senten_sentime_score));
                            }

                            outString = concPartOutString + "!#!#"
                                    + curr_senten_sentime_score
//											    +find_sentiment_stanford_OpenNLP.find_sentiment_stanford_OpenNLP_returnScore(
//											    			mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo+":"+index_edge_feature).get(lineNo+":"+cnt2))
                                    //+sentiment(mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo+":"+index_edge_feature).get(lineNo+":"+cnt2))
                                    //+mapEachDOClineNoCnt_Sentiment.get(lineNo+":"+cnt2) <- bad as
                                    + "!#!#remark:edgeFunction(3):" + edge_feature
                                    + "!#!#" + map_graph_Topology.get(1).get(cnt) + " " + map_graph_Topology.get(2).get(cnt) + " " + remove_brackets(edge_feature)
                                    + "!#!#";
                            outString = outString.replace("!!!!!!", "!!!");

                            if (!mapOut.containsValue(outString)) {
                                id_increment_for_mapOut++;
                                if (isSOPprint)
                                    System.out.println("outString.1:" + outString + " for *key cnt2=" + cnt + " id_increment_for_mapOut:" + id_increment_for_mapOut);
                                writer_debug.append("\noutString.1:" + outString + "!#!#");
                                writer_debug.flush();
                                mapOut.put(id_increment_for_mapOut, outString);
                                map_outString_nodepairedge.put(id_increment_for_mapOut, curr_nodepair_edge);
                            }
                            concPartOutString = "";
                        }
                        cnt2++;
                        concPartOutString = "";
                        //outString="";
                    } //END while(cnt2<=max_size_No_sentences){
                }
                //BOTH SOURCE AND DESTINATION ARE HARD-CODED
                else if (map_graph_Topology.get(1).get(cnt).indexOf("[") >= 0 &&
                        map_graph_Topology.get(2).get(cnt).indexOf("[") >= 0) { //
                    int index_edge_feature = -1;
                    String t = "";

                    if (isSOPprint)
                        System.out.println("@@debug.case.2 source,dest->" + index_source_node + "," + index_dest_node + " cnt=" + cnt);

                    if (is_debug_more) {
                        writer_debug.append("\n@@debug.case.2 source,dest->" + index_source_node + "," + index_dest_node + " cnt=" + cnt);
                        writer_debug.flush();
                    }

                    //apply edge feature (function) and get value.
                    if (edge_feature.indexOf("fn.") >= 0) {
                        if (edge_feature.equalsIgnoreCase("fn.sentiment")) {

                            String curr_sentenc_checksum = Calc_checksum.calc_checksum_for_string(
                                    mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_edge_feature).get(lineNo + ":" + cnt2)
                            );

                            // compress the current sentence.
                            String curr_sentenc_compressed =
                                    Clean_retain_only_alpha_numeric_characters.clean_retain_only_alpha_numeric_characters(
                                            mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_edge_feature).get(lineNo + ":" + cnt2)
                                            , true);


                            if (map_checksum_score.containsKey(curr_sentenc_checksum)) {
                                curr_senten_sentime_score = Float.valueOf(map_checksum_score.get(curr_sentenc_checksum));
                                stat_hit_on_repo_sentimen++;
                            } else if (map_CompressedSentence_score.containsKey(curr_sentenc_compressed)) {
                                //
                                curr_senten_sentime_score = Float.valueOf(map_CompressedSentence_score.get(curr_sentenc_compressed));
                                stat_hit_on_repo_sentimen++;
                            } else {
                                curr_senten_sentime_score = sentiment(mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + index_edge_feature).get(lineNo + ":" + cnt2)
                                        , writer_global_repository_to_save_sentenceWise_sentiments
                                        , true //is_write_to_repository
                                );
                                map_checksum_score.put(curr_sentenc_checksum, String.valueOf(curr_senten_sentime_score));
                            }
                            index_edge_feature = Integer.valueOf(map_SourceFeature_Idx_mapping.get(edge_feature));//4

                            outString = map_graph_Topology.get(1).get(cnt) + "!#!#" +
                                    map_graph_Topology.get(2).get(cnt) + "!#!#" +
                                    curr_senten_sentime_score
//										      +find_sentiment_stanford_OpenNLP.find_sentiment_stanford_OpenNLP_returnScore(
//												  	mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo+":"+index_edge_feature).get(lineNo+":"+cnt2))
                                    //+sentiment(mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo+":"+index_edge_feature).get(lineNo+":"+cnt2))
                                    //+mapEachDOClineNoCnt_Sentiment.get(lineNo+":"+cnt2)
                                    + "!#!#remark:edgeFunction(4):" + edge_feature
                                    + "!#!#" + map_graph_Topology.get(1).get(cnt) + " " + map_graph_Topology.get(2).get(cnt) + " " + remove_brackets(edge_feature)
                                    + "!#!#";

                            if (!mapOut.containsValue(outString)) {
                                id_increment_for_mapOut++;
                                if (isSOPprint)
                                    System.out.println("outString.2:" + outString + " for *key cnt2=" + cnt + " id_increment_for_mapOut:" + id_increment_for_mapOut);
                                writer_debug.append("\noutString.2:" + outString + "!#!#" + "\n");
                                writer_debug.flush();

                                mapOut.put(id_increment_for_mapOut, outString);
                                map_outString_nodepairedge.put(id_increment_for_mapOut, curr_nodepair_edge);
                                //outString="";
                            }
                        }
                    } else if (edge_feature.indexOf("$") == -1) { // == hard-coded value--> not a token from input file
                        outString = map_graph_Topology.get(1).get(cnt) + "!#!#" +
                                map_graph_Topology.get(2).get(cnt) + "!#!#"
                                + edge_feature + "!#!#remark:edgeFunction(5):" + edge_feature
                                + "!#!#" + map_graph_Topology.get(1).get(cnt) + " " + map_graph_Topology.get(2).get(cnt) + " " + remove_brackets(edge_feature)
                                + "!#!#";

                        if (!mapOut.containsValue(outString)) {
                            id_increment_for_mapOut++;
                            if (isSOPprint)
                                System.out.println("\noutString.2:" + outString + " for *key cnt2=" + cnt + " id_increment_for_mapOut:" + id_increment_for_mapOut);
                            writer_debug.append("\noutString.2:" + outString + "!#!#" + "\n");
                            writer_debug.flush();
                            mapOut.put(id_increment_for_mapOut, outString);
                            map_outString_nodepairedge.put(id_increment_for_mapOut, curr_nodepair_edge);
                            //outString="";
                        }
                    }
                }
                //already
                map_isAlreadyProcessed.put(lineNo + ":" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + map_SourceFeature_Idx_mapping.get("$_n")), "");
                map_outString_nodepairedge.put(id_increment_for_mapOut, curr_nodepair_edge);
                outString = "";
                concPartOutString = "";
                cnt++;
            }
            if (isSOPprint) {
                System.out.println("^^^^^mapOut.size:" + mapOut.size());
            }

            if (isSOPprint) {
                for (int h : map_outString_nodepairedge.keySet()) {
                    System.out.println("nodepairedge,outString," + curr_nodepair_edge + "<-->" + mapOut.get(h) + "<-->" + h);
                }
            }

            TreeMap<Integer, String> tmp = new TreeMap<Integer, String>();
            tmp.put(-9, String.valueOf(stat_hit_on_repo_sentimen));

            mapOut_final.put(1, mapOut);
            mapOut_final.put(2, map_outString_nodepairedge);

            writer_debug.append("\n2.VERIFY: size must be same: " + mapOut.size() + "<-->" + map_outString_nodepairedge.size());
            writer_debug.flush();

            mapOut_final.put(-9, tmp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapOut_final;
    }

    // wrapper wrapper_for_mainType_createGBADgraphFromTextUsingNLP
    public static int wrapper_for_mainType_createGBADgraphFromTextUsingNLP(
            String baseFolder,
            TreeMap<String, String> mapConfig,
            TreeMap<String, Integer> map_SourceFeature_idx_mapping
    ) {
        try {
            System.out.println("INSIDE wrapper_for_mainType_createGBADgraphFromTextUsingNLP()..... ");
            FileWriter writer_out = null;
            TreeMap<Integer, String> map_inFile_cnt_article = new TreeMap<Integer, String>();
            TreeMap<Integer, String> map_inFile_cnt_primaryKEY = new TreeMap<Integer, String>();
            TreeMap<Integer, String> map_inFile_cnt_taggedNLP_doc = new TreeMap<Integer, String>();
            //String baseFolder="/Users/lenin/Downloads/#Data2/NYT-Apr_May-WorkCopy/StagingSplitFilesEachDay.dummy/";
//						baseFolder="/Users/lenin/Downloads/#problems/p8/work-FullArticle/samples/sample.on.keywords_ds1/final/";
            // inputFile format-><url(primarykey)!!!document!!!keyword!!!author> created from <URL(primarykey)!!!JSONobject>
            // using "convert_givenString_to_Json.readFile_eachLine_convert_a_Token_to_JSON_FOR_NYT_N_write2CSVfile"
            // ************************these values are reset below******
            // ***first row should have header
            String inputFolder_OR_File = baseFolder; //+"20140401.txt_CSV.txt";
            String newTempFolder = mapConfig.get("tempFolder");
            //REPOSITORY
            String prefix_for_repository = mapConfig.get("prefix_for_repository");
            //RESET NEW FOLDER
            if (newTempFolder == null) {
                newTempFolder = baseFolder + "_temp3/";
                System.out.println("reseting null TempFolder -- newTempFolder=" + newTempFolder);
            }

            //not much use - obsolete
            String outputFile = newTempFolder + "20140401.txt_GRAPH.txt";
            int create_graph_with_only_top_N_lines_of_inputFile = 1000; //****
            /// SKIPPING
            int skip_Top_N_lines_while_taggingNLP_of_inputFile = -1;
            // int 	isskip_Top_N_lines_while_taggingNLP_of_inputFile=-1;
            //NOT yet implemented - this means past ran has the NLP tagging done and stored in a file.
            boolean isskip_Tagging_NLP_all_together = true;
            //*******************IMPORTANT*******************
            int token_index_containingText_tobeTAGGED_in_inputFile = 2; //get bigger text
            //cleaning obsolete
            boolean is_remove_html_tags_N_control_char_of_token_index_containingText_tobeTAGGED_in_inputFile = false;
            boolean isSOPprint = false;
            boolean isskip_d3js_creation = false;
            boolean is_run_only_NLP_tagging = false;
            boolean is_debug_File_Write_more = false; //<---debug
            boolean is_overwrite_flag_with_inflag_model2 = true;
            boolean is_remove_stop_words = true;
            //NOT REQUIRED. NOTE: global, does stemming at file creation (tagged file) level. Locally stemming is done at graph creation level using stemming() feature
            boolean is_do_stemming_global = false;
            boolean isAppend_outFile_OF_taggedNLP = false;
            boolean is_remove_CSV_delimiter_in_final_Element_in_graph_created = true;
            //*** false->(*SLOW) so-so working(OK OK:stemming problem reoccur if .atom() not used in $_v and $_n (in that pick only token having __, rest ignore)
            boolean is_remove_NLPtag_in_graph_output = false;
            //boolean is_remove_NLPtag_in_graph_output_AND_do_stemming_together_global=true;
            boolean is_create_each_document_as_an_XP_in_graph = false; //************
            boolean is_write_to_repository_if_token_semantic_Pair = true;
            //***if a tag (such as noun or verb) has more than two tokens, it splits on " ". example:"ukraines government" split into "ukraines" and "goverment"
            boolean is_split_on_blank_space_among_tokens = true; //***
            String outFile_for_gbad_graph = newTempFolder + "output_gbad_.g";

            int seq = 1;
            String inFolder_repositoryFolder_for_past_sentiment_saved = "", outFile_repositor_to_save_curren_ran_sentiment = "", inFolder_repository_to_store_token_semantic_Pair = "";
            String outFile_repository_to_store_token_semantic_Pair = "", inFolder_repository_to_store_compSenten_NLPtagged = "", outFile_repository_to_store_compSenten_NLPtagged = "";
            String repository_baseFolder = "/Users/lenin/Downloads/#repository/";
            System.out.println("newTempFolder:" + newTempFolder + " mapConfig:" + mapConfig);
            //create temp folder.
            if (!new File(newTempFolder).exists() || !new File(newTempFolder).isDirectory()) {
                new File(newTempFolder).mkdir();
            }
            if (!new File(outputFile).exists()) {
                try {
                    new File(outputFile).createNewFile();
                } catch (Exception e) {
                    System.out.println("!!ERROR ON new file create" + e.getMessage());
                }
            }
            try {
                inFolder_repositoryFolder_for_past_sentiment_saved = repository_baseFolder + prefix_for_repository + ".sentiment/";
                if (!new File(inFolder_repositoryFolder_for_past_sentiment_saved).exists()) {
                    System.out.println(" create MKDIR.1->" + inFolder_repositoryFolder_for_past_sentiment_saved);
                    new File(inFolder_repositoryFolder_for_past_sentiment_saved).mkdir();
                }
                outFile_repositor_to_save_curren_ran_sentiment = inFolder_repositoryFolder_for_past_sentiment_saved + prefix_for_repository + ".senti.repo." + seq + ".txt";
                inFolder_repository_to_store_token_semantic_Pair = repository_baseFolder + prefix_for_repository + ".semantic/";
                if (!new File(inFolder_repository_to_store_token_semantic_Pair).exists()) {
                    System.out.println(" create MKDIR->" + inFolder_repository_to_store_token_semantic_Pair);
                    new File(inFolder_repository_to_store_token_semantic_Pair).mkdir();
                }
                outFile_repository_to_store_token_semantic_Pair = inFolder_repository_to_store_token_semantic_Pair + prefix_for_repository + ".semantic.repo." + seq + ".txt";
                inFolder_repository_to_store_compSenten_NLPtagged = repository_baseFolder + prefix_for_repository + ".NLPtagged/";
                if (!new File(inFolder_repository_to_store_compSenten_NLPtagged).exists()) {
                    System.out.println(" create MKDIR->" + inFolder_repository_to_store_compSenten_NLPtagged);
                    new File(inFolder_repository_to_store_compSenten_NLPtagged).mkdir();
                }
                outFile_repository_to_store_compSenten_NLPtagged = inFolder_repository_to_store_compSenten_NLPtagged + prefix_for_repository + ".NLPtagged.repo." + seq + ".txt";
            } catch (Exception e) {
                System.out.println("ERROR on file repository:" + e.getMessage());
            }

            FileWriter writer_global_repository_to_save_sentenceWise_sentiments = null;
            FileWriter writer_global_repository_to_store_token_semantic_Pair = null;
            FileWriter writer_global_repository_to_store_compSenten_NLPtagged = null;

            String NLPfolder = "/Users/lenin/OneDrive/jar/NLP/models/";
            String serializedClassifier = "/Users/lenin/OneDrive/jar/stanford-ner-2014-01-04/classifiers/english.conll.4class.distsim.crf.ser.gz";

            String debugFile = newTempFolder + "debug.txt";
            FileWriter writer_debug = null;
            POSModel inflag_model2_maxent = new POSModelLoader().load(new File(NLPfolder + "en-pos-maxent.bin"));
            CRFClassifier<CoreLabel> NER_classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);

            //model for chunker
            InputStream is = null;
            ParserModel inflag_model2_chunking = null;
            String curr_file_processing = "";
            try {
                is = new FileInputStream(NLPfolder + "en-parser-chunking.bin");
                inflag_model2_chunking = new ParserModel(is);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                System.out.println("debugFile:" + debugFile);
                writer_out = new FileWriter(new File(outputFile));
                writer_debug = new FileWriter(new File(debugFile));
                //append
                writer_global_repository_to_save_sentenceWise_sentiments = new FileWriter(new File(outFile_repositor_to_save_curren_ran_sentiment), true);
                writer_global_repository_to_store_token_semantic_Pair = new FileWriter(new File(outFile_repository_to_store_token_semantic_Pair), true);
                writer_global_repository_to_store_compSenten_NLPtagged = new FileWriter(new File(outFile_repository_to_store_compSenten_NLPtagged), true);
            } catch (Exception e) {
                System.out.println("ERROR: cant open REPOSITORY file ");
                e.printStackTrace();
            }
            // BEGIN********BELOW EXAMPLES 4 GRAPH TOPOLOGY********
            //"[person]->(with)[ID],[ID]->(is)1.substr(1to6)AND2.substr(0to2),[person]->(xy)[XY],[XY]->(3)3.substr(1to3)AND3.substr(1to4)" <-EXAMPLE 1
            //"[article]->(13)$yyyy,[article]->(14)$mm,[article]->(11)$line,$line->(12)$_n.atom(),$_n.atom()->(fn.sentiment)$_v.atom().semantic().stemming(),$_v.atom()->(fn.sentiment)$_n2.atom(),$line->(9)$_number,$line->(8)$_person,$line->(7)$_organization,$line->(6)$_location" <-EXAMPLE 2
            //"[article]->(having)[organiz],[article]->(fn.sentiment)$_n.atom(),$_n.atom()->(fn.sentiment)$_v.atom().semantic().stemming(),$_v.atom()->(10)$_n2.atom(),[article]->(9)$_number,[article]->(8)$_person,[article]->(7)$_organization,[article]->(6)$_location"<-EXAMPLE 3
            //"[article]->(having)[keyword],[keyword]->(is)$keyw,[article]->(having)[organiz],[organiz]->(is)$organiz,[article]->(having)[section],[section]->(is)$sect,[article]->(fn.sentiment)$_n.atom(),$_n.atom()->(fn.sentiment)$_v.atom().semantic().stemming(),$_v.atom()->(-9)$_n2.atom(),[article]->(9)$_number,[article]->(8)$_person,[article]->(7)$_organization,[article]->(6)$_location" <- NY TIMES
            // END********BELOW EXAMPLES 4 GRAPH TOPOLOGY**********

            //For this graph topology, ASSUME input will be XML feed, URL, .
            // (1) "$_"  <--> indicates it is a token extracted from a sentence. 7 such keywords are $_n, $_n2, $_v, $_number, $_person, $_organization, and $_location.
            // $_n is the noun just before verb, and $_n2 just after verb.
            // Some sample configuration for "$_" below:
            //  map_SourceFeature_idx_mapping.put("$_n", 100); //noun available in ID=<<lineNo>:100  <-consider starting with 100 ()
            //IMPORTANT:3 type of pairs involving $ ==> #TYPE.1=> $_n->()$_v #TYPE.2=> [noun]->()$_n #TYPE.3=> $_n->()$keyw where $keyw is a token from inputFile(Each line)
            // #TYPE.4=> $keyw->()$byline where both $keyw & $byline are tokens from inputFile(each Line).
            //keywords: (1) .atom() helps to split csv to individual. Example usage: $_v.atom() in $_n->(fn.sentiment)$_v.atom()
            //	.only one of noun,verb or edge can mention atom().
            // (2) fn. helps to call function. example:$_n->(fn.sentiment)$_v.atom() is calling function sentiment for edge value.
            // (3) Special case: $url->($_v)$bline. Here $url and $bline (byline or author) are document-level. $_v is sentence-level. each sentence may have one/more verb.
            //  Between each token, there can be a single token from a tag (ex: did_vbz) or multiple token from a tag (ex: is_vb worried_vbz).
            //  First case(single token --> Code with comment "//(DEFAULT: if edge_feature=$_v then edge is set of verbs from all sentences in CSV)" handles
            //  Second case(multiple)-->Handled with atom. Example: $url->(_v.atom())$bline.
            // (4) Use WORDNET to expand semantic using semantic().  Example: $url->(_v.atom().semantic())$bline.
            // (5) Use STEMMING concept using stemming(). (Case 1:) NOTE: Goal here is to create new realted nodepair and edge .
            //	   So if multiple words(verbs together) stemming() should appear at end, where first is atom().
            //     Example 1=>$url->(_v.atom().semantic().stemming())$bline. Example 2=>$_n->(fn.sentiment)$_v.atom().semantic().stemming()
            //     (Case 2:) only need stemming of one/more verb-like tokens. **NO new related node-pair and edge created.
            //     Example: Convert "delayed__vbn!!!arriving__vbg!!!departing__vbg" to "delai__vbn!!!arriv__vbg!!!depart__vbg"
            //     For this case, use $_v.stemming(), where note atom() is NOT used, also NOT semantic() as it wont make sense.
            //
            //		//		//		//		//		//
            //			***** TIPS *****
            //   (1) Try using $_n, $_n2 and $_v with atom(). example: $_n.atom() or $_n2.atom() or $_v.atom()
            //		//		//		//		//		//
            //Tested Cases: Each line below represent tested cases
            //$_n->(fn.sentiment)$_v|$url---($keyw)$bline---$_n->(hello)$url---$_n->($bline)$url---$_n->($_v)$url---$url->($_v)$bline
            //$_n->(fn.sentiment)$_v.atom().semantic().stemming(),[noun]->(test)$_n,[house]->(yeshh)[hut],$url->($_v)$bline,$_n->($_v.stemming())$url
            // WIP status: (1) Study this case.6-> [article]->($_v)$_n later. (2) (3) fn. as node to check in future

            //below (example) used for CSV, not here.
            // graph_topology_orig="[person]->(with)[ID],[ID]->(is)1.substr(1_to_6)AND2.substr(0_to_2),[person]->(xy)[XY],[XY]->(3)3.substr(1_to_3)AND3.substr(1_to_4)";

            // Tried graph_topology: (1) $_n->(fn.sentiment)$_v,[noun]->(test)$_n,[house]->(yeshh)[hut],$url->($_v)$bline,$_n->($_v)$url

            String graph_topology_orig = "";
            //this one used for NYtimes
            graph_topology_orig = "[article]->(having)[keyword],[keyword]->(is)$keyw,[article]->(having)[organiz],[organiz]->(is)$organiz,[article]->(having)[section]," +
                    "[section]->(is)$sect,[article]->(fn.sentiment)$_n.atom(),$_n.atom()->(fn.sentiment)$_v.atom().semantic().stemming(),$_v.atom()->(-9)$_n2.atom()" +
                    "[article]->(9)$_number,[article]->(8)$_person,[article]->(7)$_organization,[article]->(6)$_location";
            graph_topology_orig = "[article]->(having)[organiz]," +
                    "[article]->(fn.sentiment)$_n.atom(),$_n.atom()->(fn.sentiment)$_v.atom().semantic().stemming(),$_v.atom()->(10)$_n2.atom()," +
                    "[article]->(9)$_number,[article]->(8)$_person,[article]->(7)$_organization,[article]->(6)$_location";
            graph_topology_orig = "";

            TreeMap<String, Integer> map_distinct_SourceDestEdge_as_KEY = new TreeMap();
            TreeMap<String, Integer> map_distinct_SourceDestEdge_ORIG_as_KEY = new TreeMap();
            TreeMap<Integer, String> map_distinct_SourceDestEdge_ORIG_as_KEY_REVERSE = new TreeMap();

            /*************START CONFIGURATION ****************/
            String confFile = "";

            System.out.println("mapConfig:" + mapConfig);
            baseFolder = mapConfig.get("baseFolder");
            System.out.println(mapConfig.get("create_graph_with_only_top_N_lines_of_inputFile"));
            create_graph_with_only_top_N_lines_of_inputFile = Integer.valueOf(mapConfig.get("create_graph_with_only_top_N_lines_of_inputFile"));
            skip_Top_N_lines_while_taggingNLP_of_inputFile = Integer.valueOf(mapConfig.get("skip_Top_N_lines_while_taggingNLP_of_inputFile"));
            //isskip_Top_N_lines_while_taggingNLP_of_inputFile=Integer.valueOf(mapConfig.get("isskip_Top_N_lines_while_taggingNLP_of_inputFile"));
            isskip_Tagging_NLP_all_together = Boolean.valueOf(mapConfig.get("isskip_Tagging_NLP_all_together"));
            token_index_containingText_tobeTAGGED_in_inputFile = Integer.valueOf(mapConfig.get("token_index_containingText_tobeTAGGED_in_inputFile"));
            isSOPprint = Boolean.valueOf(mapConfig.get("isSOPprint"));
            is_debug_File_Write_more = Boolean.valueOf(mapConfig.get("is_debug_File_Write_more"));
            isskip_d3js_creation = Boolean.valueOf(mapConfig.get("isskip_d3js_creation"));
            is_overwrite_flag_with_inflag_model2 = Boolean.valueOf(mapConfig.get("is_overwrite_flag_with_inflag_model2"));
            is_remove_stop_words = Boolean.valueOf(mapConfig.get("is_remove_stop_words"));
            is_do_stemming_global = Boolean.valueOf(mapConfig.get("is_do_stemming_global"));
            is_remove_CSV_delimiter_in_final_Element_in_graph_created = Boolean.valueOf(mapConfig.get("is_remove_CSV_delimiter_in_final_Element_in_graph_created"));
            is_remove_NLPtag_in_graph_output = Boolean.valueOf(mapConfig.get("is_remove_NLPtag_in_graph_output"));
            is_run_only_NLP_tagging = Boolean.valueOf(mapConfig.get("is_run_only_NLP_tagging"));
            is_create_each_document_as_an_XP_in_graph = Boolean.valueOf(mapConfig.get("is_create_each_document_as_an_XP_in_graph"));
            is_write_to_repository_if_token_semantic_Pair = Boolean.valueOf(mapConfig.get("is_write_to_repository_if_token_semantic_Pair"));
            is_split_on_blank_space_among_tokens = Boolean.valueOf(mapConfig.get("is_split_on_blank_space_among_tokens"));
            //file already ran and created
            boolean is_RUN_only_create_gbad_xp = Boolean.valueOf(mapConfig.get("is_RUN_only_create_gbad_xp"));

            prefix_for_repository = mapConfig.get("prefix_for_repository");
            seq = Integer.valueOf(mapConfig.get("seq"));
            NLPfolder = mapConfig.get("NLPfolder");
            serializedClassifier = mapConfig.get("serializedClassifier");
            debugFile = mapConfig.get("debugFile");
            graph_topology_orig = mapConfig.get("graph_topology_orig");
            inputFolder_OR_File = mapConfig.get("inputFolder_OR_File");
            // if dummy, set to basefolder
            if (inputFolder_OR_File.toLowerCase().indexOf("dummy") >= 0)
                inputFolder_OR_File = baseFolder;
            else
                inputFolder_OR_File = baseFolder + inputFolder_OR_File;

            // convertStringToMap
            TreeMap<Integer, TreeMap<Integer, String>> map_graph_Topology_orig = ConvertPatternStringToMap.convertStringToMap(graph_topology_orig, ",", "->");
            System.out.println("map_graph_Topology_orig:" + map_graph_Topology_orig + "\n" + map_graph_Topology_orig.get(1));
            TreeMap<String, String> map_already_proc = new TreeMap<String, String>();
            String[] arr_inputFolder_OR_File = new String[1];

            /*************END CONFIGURATION ****************/
            // IS INPUT file or FOLDER?
            File inputFile = new File(inputFolder_OR_File);
            boolean is_input_a_folder = inputFile.isDirectory();
            int total_no_input_files = -1;
            String prefix_newTempFolder_inputFileName_Path = "";
            System.out.println("give is a folder or a file --->" + is_input_a_folder);
            //input is folder
            if (is_input_a_folder) {
                arr_inputFolder_OR_File = inputFile.list();
                total_no_input_files = arr_inputFolder_OR_File.length;
            } else { // input is file
                arr_inputFolder_OR_File[0] = inputFolder_OR_File;

                //check if path is absolute
                boolean is_absolute_path =
                        Check_if_a_file_path_is_absolute_or_not.check_if_a_file_path_is_absolute_or_not(arr_inputFolder_OR_File[0]);

                System.out.println("is_absolute_path:" + is_absolute_path);

                // absolute path
                if (is_absolute_path) {
                    prefix_newTempFolder_inputFileName_Path = arr_inputFolder_OR_File[0];
                    System.out.println("1.prefix_newTempFolder_inputFileName_Path:" + prefix_newTempFolder_inputFileName_Path);
                } else {
                    System.out.println("newTempFolder:" + newTempFolder + "arr_inputFolder_OR_File[0]:" + arr_inputFolder_OR_File[0]);
                    prefix_newTempFolder_inputFileName_Path = newTempFolder + arr_inputFolder_OR_File[0];
                }

                total_no_input_files = 1;
            }

            Stemmer stemmer_ = new Stemmer();
            //map_graph_Topology_orig
            for (int _id : map_graph_Topology_orig.keySet()) {
                int max_ = map_graph_Topology_orig.get(1).size();
                int cnt = 0;
                //
                while (cnt <= max_) {
                    String t = map_graph_Topology_orig.get(1).get(cnt) + " " + map_graph_Topology_orig.get(2).get(cnt)
                            + " " + map_graph_Topology_orig.get(4).get(cnt);
                    //
                    if (map_graph_Topology_orig.get(1).containsKey(cnt) && !map_already_proc.containsKey(t)) {
                        System.out.println(cnt + " n1,n2,edge:" + t);
                        map_already_proc.put(t, "");
                        map_distinct_SourceDestEdge_ORIG_as_KEY.put(t, cnt);
                        map_distinct_SourceDestEdge_ORIG_as_KEY_REVERSE.put(cnt, t);
                    }
                    cnt++;
                }
            }

            String graph_topology = graph_topology_orig.replace(".atom()", "").replace(".semantic()", "").replace(".stemming()", "");

            //,[keyword]->(yeskey)$keyw
            TreeMap<Integer, TreeMap<Integer, String>> map_graph_Topology = ConvertPatternStringToMap.convertStringToMap(graph_topology, ",", "->");
            TreeMap<String, String> map_graph_Topology_sourceNdest_TYPE = new TreeMap<String, String>();

            // splitting -> individual/union-> splitting node to many(individual) or one(union)
            // + -> concatenate both/ _n ->noun ; _v ->verb ; _jj-> adjective ( "_" indicates its a tagged )
            //Edge->() represents what has to be added for "fn.<abc>"-> function "abc" will be called and its return value will be edge; <abc> means edge value is "abc"
            //TreeMap<String, Integer> map_SourceFeature_idx_mapping=new TreeMap();
            TreeMap<Integer, String> map_idx_SourceFeature_mapping = new TreeMap();
            TreeMap<Integer, String> map_interested_idx_SourceFeature_mapping = new TreeMap();
            TreeMap<Integer, TreeMap<String, String>> map_Out_Sentim_repo = new TreeMap<Integer, TreeMap<String, String>>();
            TreeMap<Integer, TreeMap<String, String>> map_Out_taggedNLP_repo = new TreeMap<Integer, TreeMap<String, String>>();
            TreeMap<String, String> map_repo_checksum_score = new TreeMap<String, String>();
            TreeMap<String, String> map_repo_CompressedSentence_score = new TreeMap<String, String>();
            TreeMap<String, String> map_repo_checksum_compressedSentence = new TreeMap<String, String>();

            TreeMap<String, String> map_repo_checksum_taggedNLPdocument = new TreeMap<String, String>();
            TreeMap<String, String> map_repo_CompressedSentencesOfDoc_taggedNLPdocumented = new TreeMap<String, String>();
            TreeMap<String, String> map_repo_HALF_of_CompressedSentencesOfDoc_taggedNLPdocumented = new TreeMap<String, String>();

            TreeMap<String, TreeMap<Integer, String>> map_repo_token_semantic_equiv = new TreeMap<String, TreeMap<Integer, String>>();

            //
            int idx_of_$_n = -1;
            int idx_of_$_v = -1;
            int idx_of_$_n2 = -1;
            int idx_of_$_number = -1;
            int idx_of_fndotsentiment = -1;
            int idx_of_$_person = -1;
            int idx_of_$_organization = -1;
            int idx_of_$_location = -1;

            if (map_SourceFeature_idx_mapping.containsKey("$_n"))
                idx_of_$_n = map_SourceFeature_idx_mapping.get("$_n");
            if (map_SourceFeature_idx_mapping.containsKey("$_v"))
                idx_of_$_v = map_SourceFeature_idx_mapping.get("$_v");
            if (map_SourceFeature_idx_mapping.containsKey("$_n2"))
                idx_of_$_n2 = map_SourceFeature_idx_mapping.get("$_n2");
            if (map_SourceFeature_idx_mapping.containsKey("$_number"))
                idx_of_$_number = map_SourceFeature_idx_mapping.get("$_number");
            if (map_SourceFeature_idx_mapping.containsKey("fn.sentiment"))
                idx_of_fndotsentiment = map_SourceFeature_idx_mapping.get("fn.sentiment");
            if (map_SourceFeature_idx_mapping.containsKey("$_person"))
                idx_of_$_person = map_SourceFeature_idx_mapping.get("$_person");
            if (map_SourceFeature_idx_mapping.containsKey("$_organization"))
                idx_of_$_organization = map_SourceFeature_idx_mapping.get("$_organization");
            if (map_SourceFeature_idx_mapping.containsKey("$_location"))
                idx_of_$_location = map_SourceFeature_idx_mapping.get("$_location");

            int max_map_graph_Topology = map_graph_Topology.get(1).size();
            int cnt = 1;

            try {
                if (writer_debug == null)
                    System.out.println("debug object is null " + writer_debug);

                writer_debug.append("graph_topology:" + graph_topology + "\n");
                writer_debug.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
            cnt = 1; //reset
            int stat_hit_on_repo_semantic = 0;
            int stat_hit_on_repo_sentimen = 0;
            int stat_hit_on_repo_NLPtagging = 0;

            //createGBADgraphFromTextUsingNLP
            //if(mainType.equalsIgnoreCase("createGBADgraphFromTextUsingNLP")){
            try {

                map_repo_token_semantic_equiv = new TreeMap<String, TreeMap<Integer, String>>();
                map_repo_checksum_score = new TreeMap<String, String>();

                writer_debug.append("\n entering createGBADgraphFromTextUsingNLP");
                writer_debug.flush();

                // LOAD REPOSITORIES (sentiment)
                map_Out_Sentim_repo = ReadFolder_load_checksum_N_sentiment_from_eachFile
                        .readFolder_load_checksum_N_sentiment_from_eachFile(inFolder_repositoryFolder_for_past_sentiment_saved,
                                debugFile,
                                false //isSOPprint
                        );
                map_repo_checksum_score = map_Out_Sentim_repo.get(1);
                map_repo_checksum_compressedSentence = map_Out_Sentim_repo.get(2);
                // LOAD REPOSITORIES (semantic)
                map_repo_token_semantic_equiv = ReadFolder_load_token_semantic_equiv_from_eachFile
                        .readFolder_load_token_semantic_equiv_from_eachFile(inFolder_repository_to_store_token_semantic_Pair,
                                debugFile,
                                false //isSOPprint
                        );
                // LOAD REPOSITORIES (taggedNLP)
                map_Out_taggedNLP_repo = ReadFolder_load_checksum_N_taggedNLPdocLevel_from_eachFile.readFolder_load_checksum_N_taggedNLPdocLevel_from_eachFile(
                        inFolder_repository_to_store_compSenten_NLPtagged
                        , debugFile
                        , isSOPprint);
                //
                map_repo_checksum_taggedNLPdocument = map_Out_taggedNLP_repo.get(1);
                map_repo_CompressedSentencesOfDoc_taggedNLPdocumented = map_Out_taggedNLP_repo.get(2);
                // half of compressed sentence
                if (map_Out_taggedNLP_repo.containsKey(3))
                    map_repo_HALF_of_CompressedSentencesOfDoc_taggedNLPdocumented = map_Out_taggedNLP_repo.get(2);

                //
                if (map_repo_checksum_compressedSentence != null)
                    writer_debug.append("\n Loaded repositories(score): map_checksum_compressedSentence:" + map_repo_checksum_compressedSentence.size());
                else
                    writer_debug.append("\n Loaded repositories(score): NULL map_checksum_compressedSentence:");

                if (map_repo_HALF_of_CompressedSentencesOfDoc_taggedNLPdocumented != null)
                    writer_debug.append("\n Loaded repositories(score): map_repo_HALF_of_CompressedSentencesOfDoc_taggedNLPdocumented:"
                            + map_repo_HALF_of_CompressedSentencesOfDoc_taggedNLPdocumented.size());
                else
                    writer_debug.append("\n Loaded repositories(score): NULL map_repo_HALF_of_CompressedSentencesOfDoc_taggedNLPdocumented:");

                if (map_repo_token_semantic_equiv != null)
                    writer_debug.append("\n Loaded repositories(score): map_repo_token_semantic_equiv:" + map_repo_token_semantic_equiv.size());
                else
                    writer_debug.append("\n Loaded repositories(score): NULL map_repo_token_semantic_equiv:");

                if (map_repo_checksum_score != null)
                    writer_debug.append("\n Loaded repositories(sentiment): map_repo_checksum_score->" + map_repo_checksum_score.size());
                else
                    writer_debug.append("\n Loaded repositories(sentiment): map_repo_checksum_score->" + map_repo_checksum_score.size());

                if (map_repo_checksum_taggedNLPdocument != null)
                    writer_debug.append("\n Loaded repositories(NLPtagged):" + map_repo_checksum_taggedNLPdocument.size());
                else
                    writer_debug.append("\n Loaded repositories(NLPtagged) NULL map_repo_checksum_taggedNLPdocument");

                if (map_repo_CompressedSentencesOfDoc_taggedNLPdocumented != null)
                    writer_debug.append("\n Loaded repositories(NLPtagged):map_repo_CompressedSentencesOfDoc_taggedNLPdocumented:"
                            + map_repo_CompressedSentencesOfDoc_taggedNLPdocumented.size());
                else
                    writer_debug.append("\n Loaded repositories(NLPtagged) NULL map_repo_CompressedSentencesOfDoc_taggedNLPdocumented");


                writer_debug.flush();
                int deb_cnt = 0;
                //DEBUG FIRST FEW 10000 Lines
                for (String i : map_repo_token_semantic_equiv.keySet()) {
                    if (deb_cnt == 0) {
                        writer_debug.append("\n Below are samples:");
                    }
                    writer_debug.append("\n token:" + i + "<->Equiv_semantic.size:" + map_repo_token_semantic_equiv.get(i).size()
                            + "<->Equiv_semantic:" + map_repo_token_semantic_equiv.get(i));
                    writer_debug.flush();
                    deb_cnt++;
                    if (deb_cnt > 50) break;
                }

                System.out.println("\n Loaded repositories:map_checksum_score:" + map_repo_checksum_score.size() + " map_token_semantic_equiv:" + map_repo_token_semantic_equiv.size());

            } catch (IOException e1) {
                try {
                    writer_debug.append("\n error on writing");
                    writer_debug.flush();
                } catch (Exception e) {
                }
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            //IMPORTANT:3 type of pairs involving $ ==> #TYPE.1=> $_n->()$_v #TYPE.2=> [noun]->()$_n #TYPE.3=> $_n->()$keyw where $keyw is a token from inputFile(Each line)
            // #TYPE.4=> $keyw->()$byline where both $keyw & $byline are tokens from inputFile(each Line).
            map_already_proc = new TreeMap<String, String>();
            // iter to find TYPE
            while (cnt <= max_map_graph_Topology) {
                String curr_TYPE = "";
                String source = map_graph_Topology.get(1).get(cnt);
                String dest = map_graph_Topology.get(2).get(cnt);
                String edge = map_graph_Topology.get(4).get(cnt);
                //if(!map_already_proc.containsKey(source+":"+dest+":"+edge)){cnt++; continue;}<- not working. wrong my undersand logic

                //both source and dest from tags such as _nn and _vbz
                if (source.indexOf("$_") >= 0 && dest.indexOf("$_") >= 0) {
                    curr_TYPE = "TYPE.1";
                }
                // one hard-coded and another from tags such as _vbz
                else if ((source.indexOf("[") >= 0 && dest.indexOf("$_") >= 0) || (source.indexOf("$_") >= 0 && dest.indexOf("[") >= 0)) {
                    curr_TYPE = "TYPE.2";
                }
                //$_n->()$keyw where $keyw is a *token from inputFile(Each line)
                else if ((source.indexOf("$") >= 0 && dest.indexOf("$_") >= 0) || (source.indexOf("$_") >= 0 && dest.indexOf("$") >= 0)) {
                    curr_TYPE = "TYPE.3";
                }
                //source N destination both from *tokens from inputFile(Each line)
                else if ((source.indexOf("$") >= 0 && source.indexOf("$_") == -1) && (dest.indexOf("$") >= 0 && dest.indexOf("$_") == -1)) {
                    curr_TYPE = "TYPE.4";
                }
                //Both are FIXED. example [A]->()[B]
                else if ((source.indexOf("[") >= 0) && (dest.indexOf("[") >= 0)) {
                    curr_TYPE = "TYPE.5";
                }
                // source (or dest) is fixed [fixed] and other one is from a token (example: $byline)
                else if ((source.indexOf("$") >= 0 && source.indexOf("$_") == -1 && dest.indexOf("[") >= 0)
                        || (dest.indexOf("$") >= 0 && dest.indexOf("$_") == -1 && source.indexOf("[") >= 0)
                        ) {
                    curr_TYPE = "TYPE.6";
                }

                System.out.println("setting TYPE:" + source + "<->" + dest + "<->" + curr_TYPE + "<->cnt:" + cnt + "<->" + map_graph_Topology);
                map_graph_Topology_sourceNdest_TYPE.put(source + ":" + dest, curr_TYPE);
                map_already_proc.put(source + ":" + dest + ":" + edge, "");
                map_distinct_SourceDestEdge_as_KEY.put(source + " " + dest + " " + edge, cnt);
                cnt++;
            }

            //iterate & get interested tokens only
            for (String key : map_SourceFeature_idx_mapping.keySet()) {
                map_idx_SourceFeature_mapping.put(map_SourceFeature_idx_mapping.get(key), key);
                //get interested tokens only
                if (key.indexOf("$") >= 0 || //token from input file
                        key.indexOf("$_") == -1) //_nn or _vbn
                    map_interested_idx_SourceFeature_mapping.put(map_SourceFeature_idx_mapping.get(key), key);
            }
            int cnt_file_procss = 0;
            int xp_number = 0;
            try {
                int last_xp_number = -1;
                //iterate each file of current folder (if single file given, iterate the file)
                while (cnt_file_procss < total_no_input_files) {

                    //check if path is absolute
                    boolean is_absolute_path =
                            Check_if_a_file_path_is_absolute_or_not.check_if_a_file_path_is_absolute_or_not(arr_inputFolder_OR_File[cnt_file_procss]);

                    if (is_absolute_path)
                        curr_file_processing = arr_inputFolder_OR_File[cnt_file_procss];
                    else
                        curr_file_processing = baseFolder + arr_inputFolder_OR_File[cnt_file_procss];

                    //SKIP if its a sub-folder
                    if (new File(curr_file_processing).isDirectory() || curr_file_processing.toLowerCase().indexOf("debug") >= 0
                            || curr_file_processing.toLowerCase().indexOf("Store") >= 0) {
                        System.out.println("skip subfolder");
                        writer_debug.append("\n SKIPPING curr_file_processing: " + curr_file_processing);
                        writer_debug.flush();
                        cnt_file_procss++;
                        continue;
                    }

                    //check if path is absolute
                    is_absolute_path =
                            Check_if_a_file_path_is_absolute_or_not.check_if_a_file_path_is_absolute_or_not(arr_inputFolder_OR_File[cnt_file_procss]);

                    // prefix prefix_newTempFolder inputFileName_Path
                    if (is_absolute_path)
                        prefix_newTempFolder_inputFileName_Path = arr_inputFolder_OR_File[cnt_file_procss];
                    else
                        prefix_newTempFolder_inputFileName_Path = newTempFolder + arr_inputFolder_OR_File[cnt_file_procss];

                    writer_debug.append("\n curr_file_processing: " + curr_file_processing);
                    writer_debug.flush();

                    //get interested token number values from file
                    TreeMap<Integer, String> map_inFile_eachLine =
                            ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
                                    .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
                                            curr_file_processing,
                                            -1,
                                            -1,
                                            " createGBADgraphFromTextUsingNLP ", //debug_label
                                            false //isPrintSOP
                                    );
                    //header
                    String header = map_inFile_eachLine.get(1);
                    //verify  header=config
                    String[] arr_header = header.split("!!!");
                    //verify  header=config
                    boolean bool_verified_header = Verify_input_createGBADfromTextUsing.
                            verify_input_createGBADfromTextUsing(map_interested_idx_SourceFeature_mapping,
                                    arr_header);
                    if (isSOPprint)
                        System.out.println("verify_input_createGBADfromTextUsing out:" + bool_verified_header);

                    //below map only takes in interested
                    TreeMap<Integer, TreeMap<Integer, String>> map_idx_interested_lineNo_Tokens_from_inputFile =
                            new TreeMap<Integer, TreeMap<Integer, String>>();
                    TreeMap<Integer, TreeMap<Integer, String>> map_idx_interested_lineNo_Tokens_from_inputFile_past =
                            new TreeMap<Integer, TreeMap<Integer, String>>();

                    //initialize
                    for (int idx : map_interested_idx_SourceFeature_mapping.keySet()) {
                        TreeMap<Integer, String> tmp = new TreeMap<Integer, String>();
                        //each interested token idx will have a TreeMap
                        if (!map_idx_interested_lineNo_Tokens_from_inputFile_past.containsKey(idx))
                            map_idx_interested_lineNo_Tokens_from_inputFile_past.put(idx, tmp);
                    }

                    writer_debug.append("\n map_inFile_cnt_article:" + map_inFile_cnt_article);
                    writer_debug.append("\n map_inFile_eachLine.size():" + map_inFile_eachLine.size());
                    writer_debug.flush();

                    String nodePair_Edge_new = "";

                    // get token (which will be tagged NLP)
                    for (int linNo : map_inFile_eachLine.keySet()) {
                        //if(linNo==1) continue; //header

                        String currLine = map_inFile_eachLine.get(linNo);
                        String[] s = currLine.split("!!!");

                        // dont load empty
                        if (s.length < token_index_containingText_tobeTAGGED_in_inputFile) {
                            continue;
                        }

                        try {
                            // remove html tag
                            if (is_remove_html_tags_N_control_char_of_token_index_containingText_tobeTAGGED_in_inputFile) {
                                //
                                map_inFile_cnt_article.put(linNo,
                                        RemoveUnicodeChar.removeUnicodeChar(Crawler.removeTagsFromHTML(
                                                s[token_index_containingText_tobeTAGGED_in_inputFile - 1],
                                                false
                                                )
                                        )
                                );
                            } else {
                                map_inFile_cnt_article.put(linNo, s[token_index_containingText_tobeTAGGED_in_inputFile - 1]);
                            }
                        } catch (Exception e) {
                            writer_debug.append("\n ERROR on reading for token index: token_index_containingText_tobeTAGGED_in_inputFile:"
                                    + token_index_containingText_tobeTAGGED_in_inputFile + " on linNo=" + linNo + " e:" + e.getMessage());
                            writer_debug.flush();
                        }

                        map_inFile_cnt_primaryKEY.put(linNo, s[0]); //PRIMARY KEY
                        //System.out.println("map_idx_interested_lineNo_Tokens_from_inputFile_past:"+map_idx_interested_lineNo_Tokens_from_inputFile_past);

                        // DEBUG: PRINT only 10 LINES
                        if (linNo <= 10) {
                            writer_debug.append("\nmap_idx_interested_lineNo_Tokens_from_inputFile:" + map_idx_interested_lineNo_Tokens_from_inputFile);
                            writer_debug.append("\nmap_idx_interested_lineNo_Tokens_from_inputFile_past:" + map_idx_interested_lineNo_Tokens_from_inputFile_past);
                            writer_debug.flush();
                        }

                        //iterate index of each interested token and add to *map_idx_interested_lineNo_Tokens_from_inputFile.
                        for (int curr_intr_idx : map_idx_interested_lineNo_Tokens_from_inputFile_past.keySet()) {
                            try {
                                if (curr_intr_idx < 100) {
                                    //	each token take existing, and add new one
                                    if (map_idx_interested_lineNo_Tokens_from_inputFile.containsKey(curr_intr_idx)) {
                                        TreeMap<Integer, String> tmp = map_idx_interested_lineNo_Tokens_from_inputFile.get(curr_intr_idx);
                                        tmp.put(linNo, s[curr_intr_idx - 1]);
                                        map_idx_interested_lineNo_Tokens_from_inputFile.put(curr_intr_idx, tmp);
                                    } else {
                                        TreeMap<Integer, String> t = new TreeMap<Integer, String>();

                                        writer_debug.flush();
                                        t.put(linNo, s[curr_intr_idx - 1]);
                                        map_idx_interested_lineNo_Tokens_from_inputFile.put(curr_intr_idx, t);
                                    }
                                }
                            } catch (Exception e) {
                                writer_debug.append("\n ERROR: ON LOADING s.len=" + s.length + " curr_intr_idx=" + curr_intr_idx + " line=" + currLine);
                                writer_debug.flush();
                            }

                        } //END for(int curr_intr_idx:map_idx_interested_lineNo_Tokens_from_inputFile_past.keySet() ){


                    } //END for(int linNo:map_inFile_eachLine.keySet()){

                    //DEBUG
                    if (map_idx_interested_lineNo_Tokens_from_inputFile.size() > 0) {
                        for (int curr_intr_idx : map_idx_interested_lineNo_Tokens_from_inputFile.keySet()) {
                            if (curr_intr_idx < 100) {
                                if (map_idx_interested_lineNo_Tokens_from_inputFile.get(curr_intr_idx) != null)
                                    writer_debug.append("\nVERIFY: LOADED curr_intr_idx=" + curr_intr_idx + " "
                                            + map_idx_interested_lineNo_Tokens_from_inputFile.get(curr_intr_idx).size());
                                else
                                    writer_debug.append("\nVERIFY: LOADED curr_intr_idx=" + curr_intr_idx + " (GOT NULL)->"
                                            + map_idx_interested_lineNo_Tokens_from_inputFile.get(curr_intr_idx));
                                writer_debug.flush();

                            }
                        }
                    } else {
                        writer_debug.append("\n **** ERROR(RED): DID NOT LOAD ANYTHING !!!");
                        writer_debug.flush();
                    }

                    //
                    for (int curr_intr_idx : map_idx_interested_lineNo_Tokens_from_inputFile.keySet()) {
                        writer_debug.append("\n Interested index->map_idx_interested_lineNo_Tokens_from_inputFile.get(+" + curr_intr_idx + ").size() -> "
                                + map_idx_interested_lineNo_Tokens_from_inputFile.get(curr_intr_idx).size());
                        writer_debug.flush();
                    }
                    //
                    writer_debug.append("\n map_idx_interested_lineNo_Tokens_from_inputFile -> " + map_idx_interested_lineNo_Tokens_from_inputFile.size());
                    writer_debug.flush();

                    TreeMap<String, String> maptaggedNLP = new TreeMap();
                    //if repository folder did not exist create.
                    if (!new File(inFolder_repositoryFolder_for_past_sentiment_saved).exists())
                        new File(inFolder_repositoryFolder_for_past_sentiment_saved).mkdir();
                    if (!new File(inFolder_repository_to_store_token_semantic_Pair).exists())
                        new File(inFolder_repository_to_store_token_semantic_Pair).mkdir();

                    FileWriter writer_url_taggedline_untaggedline = new FileWriter(new File(prefix_newTempFolder_inputFileName_Path + "_url_tagline_untagline.dummy.txt")
                            , isAppend_outFile_OF_taggedNLP);
                    FileWriter writer_url_taggedline_untaggedline_all = new FileWriter(new File(prefix_newTempFolder_inputFileName_Path + "_url_tagline_untagline.dummy.all.txt")
                            , isAppend_outFile_OF_taggedNLP);

                    writer_debug.append("\n LOADED map_inFile_cnt_article:" + map_inFile_cnt_article.size());
                    writer_debug.flush();

                    // SKIPPING TAGGING ALL TOGETHER
                    if (isskip_Tagging_NLP_all_together == false) {

                        /*****/
                        //NLP TAGGING -> each line = each document (article)
                        for (int linNo : map_inFile_cnt_article.keySet()) {
                            System.out.println("Tagging NLP for Document lineNo=" + linNo);
                            //skip
                            if (skip_Top_N_lines_while_taggingNLP_of_inputFile > 0 && linNo <= skip_Top_N_lines_while_taggingNLP_of_inputFile) {
                                System.out.println("skipping top N lines- > " + linNo);
                                continue;
                            }
                            // create graph with only top n lines of inputFile
                            if (create_graph_with_only_top_N_lines_of_inputFile > 0 && linNo > create_graph_with_only_top_N_lines_of_inputFile) {
                                System.out.println("break as create_graph_with_only_top_N_lines_of_inputFile exceeds");
                                break;
                            }

                            String curr_doc_curr_sent_tagged = "";
                            String curr_doc_curr_sent_dependency_tree = "";
                            String curr_doc = map_inFile_cnt_article.get(linNo);
                            //checksum for current document (not sentence-level)
                            String checksum_of_curr_document = Calc_checksum.calc_checksum_for_string(curr_doc);
                            //compress all sentences of given document.
                            String compressedSentences_of_curr_doc = Clean_retain_only_alpha_numeric_characters.clean_retain_only_alpha_numeric_characters(curr_doc, true);
                            int half_size = compressedSentences_of_curr_doc.length() / 2;
                            String half_of_compressedSentences_of_curr_doc = compressedSentences_of_curr_doc.substring(compressedSentences_of_curr_doc.length() - half_size,
                                    compressedSentences_of_curr_doc.length());

                            // NOT already in repository
                            if (!map_repo_checksum_taggedNLPdocument.containsKey(checksum_of_curr_document) &&
                                    !map_repo_CompressedSentencesOfDoc_taggedNLPdocumented.containsKey(compressedSentences_of_curr_doc)
                                    ) {
                                //
                                if (curr_doc.length() <= 2) {
                                    writer_debug.append("\n ERROR.2: curr_doc IS NULL for linNo=" + linNo);
                                    writer_debug.flush();
                                    continue;
                                }

                                if (is_debug_File_Write_more) {
                                    writer_debug.append("\n IF input for NLPtagging:" + curr_doc);
                                    writer_debug.flush();
                                }
                                //each document - nlp tagging
                                maptaggedNLP = Crawler.wrapper_getNLPTrained_en_pos_maxent(
                                        "", //inputSingleURL_OR_input1, // give input_1 or input_2
                                        curr_doc,
                                        NLPfolder,
                                        "en-pos-maxent.bin", // Flag
                                        // ={en-pos-maxent.bin,en-ner-person.bin,en-ner-organization.bin,en-ner-location.bin,en-ner-time.bin,en-ner-money.bin}
                                        "", // flag2
                                        false, //isSOPdebug,
                                        is_overwrite_flag_with_inflag_model2,//overwrite with inflag_model2
                                        inflag_model2_maxent, //model with POSModelLoader().load()
                                        null,// inflag_model2_chunking,//pass as NULl
                                        debugFile  //debugFileName
                                );
                                //
                                curr_doc_curr_sent_tagged = maptaggedNLP.get("taggedNLP");
                                curr_doc_curr_sent_dependency_tree = maptaggedNLP.get("taggedDependencyTree");

                                //System.out.println("curr_doc_curr_sent_tagged: "+curr_doc_curr_sent_tagged);
                                writer_global_repository_to_store_compSenten_NLPtagged.append(checksum_of_curr_document + "!!!" + curr_doc_curr_sent_tagged +
                                        "!!!" + compressedSentences_of_curr_doc +
                                        "!!!" + curr_doc_curr_sent_dependency_tree + "\n");
                                writer_global_repository_to_store_compSenten_NLPtagged.flush();

                            } else {// exists in *repo
                                stat_hit_on_repo_NLPtagging++;
                                if (is_debug_File_Write_more) {
                                    writer_debug.append("\n ELSE input for NLPtagging:" + curr_doc);
                                    writer_debug.flush();
                                }

                                if (map_repo_checksum_taggedNLPdocument.containsKey(checksum_of_curr_document)) {
                                    curr_doc_curr_sent_tagged = map_repo_checksum_taggedNLPdocument.get(checksum_of_curr_document);
                                } else if (map_repo_CompressedSentencesOfDoc_taggedNLPdocumented.containsKey(compressedSentences_of_curr_doc)) {
                                    curr_doc_curr_sent_tagged = map_repo_checksum_taggedNLPdocument.get(compressedSentences_of_curr_doc);
                                } else {
                                    writer_debug.append("\n ERROR: suppose to be found in repository..");
                                    writer_debug.flush();
                                }
                                curr_doc_curr_sent_dependency_tree = ""; //THIS ONE not yet stored in repository. later work
                            }

                            String curr_doc_curr_sent_tagged_remove_stopwords = "";
                            //write to repository document-level tagging (and not sentence level).

                            //System.out.println("debug : "+curr_doc_curr_sent_tagged+ "\n"+curr_doc);

                            String conc_line = "";
                            //remove stop words  || do stemming ( all 3 cases done here)
                            if (is_remove_stop_words || is_do_stemming_global) {
                                //System.out.println("curr_doc_curr_sent_tagged:"+curr_doc_curr_sent_tagged);
                                if (curr_doc_curr_sent_tagged == null) continue;

                                String[] s = curr_doc_curr_sent_tagged.split(" ");
                                int c50 = 0;
                                //
                                while (c50 < s.length) {
                                    //if(s[c50].indexOf("_v")>=0){
                                    String token = s[c50].substring(0, s[c50].indexOf("_"));
                                    String tag = s[c50].substring(s[c50].indexOf("_"), s[c50].length());
                                    // BOTH remove stop words && stemming SET
                                    if (is_remove_stop_words && is_do_stemming_global) {
                                        if (!Stopwords.is_stopword(token)) {
                                            token = stemmer_.stem(token); //.replace("\"", "") );
                                            //
                                            if (conc_line.length() == 0) {

                                                conc_line = token + "_" + tag;
                                            } else {
                                                conc_line = conc_line + " " + token + "_" + tag;
                                            }
                                        }
                                    }
                                    // ONLY remove stop word
                                    if (is_remove_stop_words && is_do_stemming_global == false) {
                                        if (!Stopwords.is_stopword(token)) {
                                            //
                                            if (conc_line.length() == 0) {
                                                conc_line = token + "_" + tag;
                                            } else {
                                                conc_line = conc_line + " " + token + "_" + tag;
                                            }
                                        }
                                    }

                                    // ONLY do stemming (less used)
                                    if (is_remove_stop_words == false && is_do_stemming_global) {
                                        token = stemmer_.stem(token); //.replace("\"", "") );
                                        //
                                        if (conc_line.length() == 0) {

                                            conc_line = token + "_" + tag;
                                        } else {
                                            conc_line = conc_line + " " + token + "_" + tag;
                                        }
                                    }

                                    //}
                                    c50++;
                                }
                                //REPLACING with appropriate
                                curr_doc_curr_sent_tagged = conc_line;
                            } //if(is_remove_stop_words && is_do_stemming ){

                            // write to output file (***curr_doc_curr_sent_tagged replaced with conc_line as per configuration)
                            writer_url_taggedline_untaggedline.append(map_inFile_cnt_primaryKEY.get(linNo) + "!#!#" + curr_doc_curr_sent_tagged +
                                    "!#!#" + curr_doc + "!#!#" + curr_doc_curr_sent_dependency_tree + "\n");
                            writer_url_taggedline_untaggedline.flush();
                            //write all different varieties.
                            writer_url_taggedline_untaggedline_all.append(map_inFile_cnt_primaryKEY.get(linNo) + "!#!#" + curr_doc_curr_sent_tagged +
                                    "!#!#" + curr_doc + "!#!#" + conc_line + "!#!#" + curr_doc_curr_sent_dependency_tree + "!#!#" + "\n");
                            writer_url_taggedline_untaggedline_all.flush();

                        } //END for(int linNo:map_inFile_cnt_article.keySet())
//						/***/

                    } else {
                        writer_debug.append("\n *********** isskip_Tagging_NLP_all_together IS TRUE**** ");
                        writer_debug.flush();
                    }

                    //stop here if only NLP tagging to be done
                    if (is_run_only_NLP_tagging) {
                        return 0; //success
                    }

                    // TAKE THE TAGGED FILE
                    TreeMap<Integer, String> map_inFile_eachLine_tagged =
                            ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile
                                    .readFile_load_Each_Line_of_Generic_File_To_Map_Integer_Counter_String_Line(
                                            prefix_newTempFolder_inputFileName_Path + "_url_tagline_untagline.dummy.txt",
                                            -1,
                                            -1,
                                            " createGBADgraphFromTextUsingNLP ", //debug_label
                                            false //isPrintSOP
                                    );

                    // get token taggedNLP
//						for(int linNo2:map_inFile_eachLine_tagged.keySet()){
//							String []s=map_inFile_eachLine_tagged.get(linNo2).split("!!!");
//							map_inFile_cnt_taggedNLP_doc.put( linNo2, s[1]); //second column has taggedNLP
//						}
                    boolean entering = true;
                    String corresp_config_mapping_of_NodePair_Edge = "";
                    TreeMap<Integer, TreeMap<Integer, String>> mapOut =
                            new TreeMap<Integer, TreeMap<Integer, String>>();

                    TreeMap<String, TreeMap<String, String>> mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence =
                            new TreeMap<String, TreeMap<String, String>>();

                    TreeMap<String, String> mapEachDOClineNoCnt_Sentiment = new TreeMap<String, String>();
                    int first_lineno = -1;
                    String curr_doc_curr_sent_dependency_tree = "";
                    String curr_doc_taggedNLP = "";
                    String curr_doc_untaggedTEXT = "";
                    //TreeMap<String, String> mapcurr_Senti=new TreeMap<String, String>();
                    // for each document (taggedNLP), each sentence, get verb and noun
                    // this file format "url!!!taggedline!!!untaggedline"
                    for (int lineNo : map_inFile_eachLine_tagged.keySet()) {

                        // create graph with only top n lines of inputFile
                        if (create_graph_with_only_top_N_lines_of_inputFile > 0 && lineNo > create_graph_with_only_top_N_lines_of_inputFile) {
                            System.out.println("break as create_graph_with_only_top_N_lines_of_inputFile exceeds");
                            break;
                        }

                        String curr_doc = map_inFile_eachLine_tagged.get(lineNo);
                        if (entering) {
                            first_lineno = lineNo;
                            entering = false;
                        }

                        if (curr_doc == null) {
                            if (isSOPprint)
                                System.out.println("!!!" + "NULL.." + curr_doc + "(lineno=" + lineNo + ") ; firstlineno:" + first_lineno);
                            continue;
                        }
                        //split file containing tagged NLP
                        String[] arr_curr_doc = curr_doc.split("!#!#");

                        if (arr_curr_doc.length < 2) {
                            System.out.println("!!!" + arr_curr_doc.length + " " + lineNo + " ERROR : curr_doc:" + curr_doc);
                            continue;
                        }
                        //if(lineNo>10) break;
                        curr_doc_taggedNLP = arr_curr_doc[1];
                        curr_doc_untaggedTEXT = arr_curr_doc[2];
                        try {
                            // all sentences together dependency tree. MAY not be feasible for sentence-wise dependency tree usage.
                            if (arr_curr_doc.length >= 4)
                                curr_doc_curr_sent_dependency_tree = arr_curr_doc[3];
                        } catch (Exception e) {
                            writer_debug.append("\n error: in token 4 for curr_doc: " + curr_doc);
                            writer_debug.flush();
                        }

                        String[] arr_curr_doc_taggedNLP = curr_doc_taggedNLP.split("\\.");
                        String[] arr_curr_doc_untaggedTEXT = curr_doc_untaggedTEXT.split("\\.");

                        int len_sentenc_tag = arr_curr_doc_taggedNLP.length;
                        int len_sentenc_untag = arr_curr_doc_untaggedTEXT.length;
                        cnt = 0;
                        if (isSOPprint)
                            System.out.println("tagging lineNo:" + lineNo + " len_sentenc_untag:" + len_sentenc_untag
                                    + " curr_doc_taggedNLP:" + curr_doc_taggedNLP + " arr_curr_doc_untaggedTEXT:" + curr_doc_untaggedTEXT);
                        TreeMap<Integer, String> map_Noun_r_Verb_allSentence_currDoc = new TreeMap<Integer, String>();
                        int counter_sentence_with_N_V = 1;
                        // each sentence , extract noun-verb (verb and noun just occurs before verb)
                        while (cnt < len_sentenc_untag) {

                            try {

                                String curr_sentence_taggedNLP = arr_curr_doc_taggedNLP[cnt];
                                String curr_sentence_untaggedTEXT = arr_curr_doc_untaggedTEXT[cnt];

                                // skip if no noun-verb pair found in this sentence.
                                if (curr_sentence_taggedNLP.toLowerCase().indexOf("_") == -1 ||
                                        curr_sentence_taggedNLP.toLowerCase().indexOf("_") == -1) {
                                    cnt++;
                                    continue;
                                }
//								FileWriter  writer_debug2=new FileWriter(new File(debugFile));
                                //mapOut.get(1) -> curr sentence noun / mapOut.get(2)-> curr sentence noun-verb pair. Picks Verb and Noun Just before that.
                                //mapOut.get(3).get(lineNumber) -> curr sentence dependency tree
                                //mapOut.put(4, Map<lineNumber,<noun_2_CSV> );  <- contains map_unique_noun_2
                                //mapOut.get(5, Map<lineNumber,<Numbers>>)  <- contains Numbers from current sentence
                                //mapOut.put(6, Map<1111,<person_CSV> );    <- contains person in CSV ; 1111 is the lineNumber by default.
                                //mapOut.put(7, Map<2222,<organization_CSV> );  <- contains organization in CSV ; 2222 is the lineNumber by default.
                                //mapOut.put(8, Map<3333,<location_CSV );  <- contains organization in CSV ; 3333 is the lineNumber by default.
                                // stemming default applied here
                                mapOut = Find_Verb_AND_Noun_JustBfr_from_TaggedString_inFile.
                                        find_VERB_AND_Noun_JustBfr_from_TaggedString_function(
                                                baseFolder,
                                                curr_sentence_taggedNLP,// inText_TaggedNLP,
                                                curr_sentence_untaggedTEXT, //curr_sentence_untaggedTEXT
                                                -1, // token_for_bodytext_taggedNLP_inFile
                                                false, // is_inText_TaggedNLP_CSV,
                                                false, //isSOPprint
                                                lineNo, //lineNumber
                                                null, // inflag_model2_chunking(=null DEPENDENCY TREE not called)
                                                NER_classifier,
                                                writer_debug,
                                                debugFile
                                        );


                                // If i do sentiment here, it would most likely be "document-level". So did not take this approach as required is sentence-level
                                //get sentiment of current sentence. ( import com.aliasi.classify.LMClassifier )
                                //String curr_senti=find_sentiments_of_a_sentence.find_sentiments_classify1(curr_sentence_untaggedTEXT);
                                // from STANFORD NLP
                                //String curr_senti= String.valueOf(find_sentiment_stanford_OpenNLP.find_sentiment_stanford_OpenNLP_returnScore(curr_sentence_untaggedTEXT));

                                String h = mapOut.get(1).toString().replace("{", "").replace("}", "");

                                System.out.println("N-V lineNo:" + lineNo
//							 				+" "+cnt+" \n"+mapOut.get(3).get(3)+"\n<---->"+curr_sentence_untaggedTEXT
//							 						+"<---->"
//							 						  + find_sentiment_stanford_OpenNLP.find_sentiment_inputDependencyTreeSentenceString(mapOut.get(3).get(3))// <--this giving -1 why
                                );

                                // is NOUN string exists?
                                //if(!h.equals("") && curr_sentence_untaggedTEXT !=null ){
                                if (!h.equals("") && curr_sentence_untaggedTEXT != null) {

                                    TreeMap<Integer, String> tmp_1 = new TreeMap<Integer, String>();
                                    TreeMap<Integer, String> tmp_2 = new TreeMap<Integer, String>();
                                    TreeMap<Integer, String> tmp_3 = new TreeMap<Integer, String>();
                                    TreeMap<Integer, String> tmp_4 = new TreeMap<Integer, String>();
                                    TreeMap<Integer, String> tmp_5 = new TreeMap<Integer, String>();
                                    TreeMap<Integer, String> tmp_6 = new TreeMap<Integer, String>();
                                    TreeMap<Integer, String> tmp_7 = new TreeMap<Integer, String>();
                                    TreeMap<Integer, String> tmp_8 = new TreeMap<Integer, String>();

                                    //System.out.println("start counter_sentence_with_N_V:"+counter_sentence_with_N_V+" of lineNo:"+lineNo);

                                    //noun (curr)
                                    if (mapOut.containsKey(1))
                                        tmp_1 = mapOut.get(1);
                                    //verb (curr)
                                    if (mapOut.containsKey(2))
                                        tmp_2 = mapOut.get(2);
                                    //noun_2 (curr)
                                    if (mapOut.containsKey(4))
                                        tmp_4 = mapOut.get(4);
                                    //number (curr)  mapOut.get(5, Map<lineNumber,<Numbers>>)  <- contains Numbers from current sentence
                                    if (mapOut.containsKey(5))
                                        tmp_5 = mapOut.get(5);
                                    //person (curr) mapOut.put(6, Map<1111,<person_CSV> );    <- contains person in CSV ; 1111 is the lineNumber by default.
                                    if (mapOut.containsKey(6))
                                        tmp_6 = mapOut.get(6);
                                    //organization (curr)  mapOut.put(7, Map<2222,<organization_CSV> );  <- contains organization in CSV ; 2222 is the lineNumber by default.
                                    if (mapOut.containsKey(7))
                                        tmp_7 = mapOut.get(7);
                                    // location mapOut.put(8, Map<3333,<location_CSV );  <- contains location in CSV ; 3333 is the lineNumber by default.
                                    tmp_8 = mapOut.get(8);


                                    //below two methods obselete, test and remove later???????????
                                    TreeMap<String, String> mapOut_1_new = new TreeMap();
                                    TreeMap<String, String> mapOut_2_new = new TreeMap();
                                    // below are all tags found from current sentences
                                    TreeMap<String, String> mapTEMP_1 = new TreeMap<String, String>(); //noun(s) of each sentence
                                    TreeMap<String, String> mapTEMP_2 = new TreeMap<String, String>(); //verb(s) of each sentence
                                    TreeMap<String, String> mapTEMP_3 = new TreeMap<String, String>(); //each (curr) sentence
                                    TreeMap<String, String> mapTEMP_4 = new TreeMap<String, String>(); //noun_2 of each sentence
                                    TreeMap<String, String> mapTEMP_5 = new TreeMap<String, String>(); //numbers (tagged) of each sentence
                                    TreeMap<String, String> mapTEMP_6 = new TreeMap<String, String>(); //persons (tagged) of each sentence
                                    TreeMap<String, String> mapTEMP_7 = new TreeMap<String, String>(); //organization (tagged) of each sentence
                                    TreeMap<String, String> mapTEMP_8 = new TreeMap<String, String>(); //location (tagged) of each sentence

                                    // numbers map add

                                    // person map add -  mapOut.put(6, Map<1111,<person_CSV> );  <- contains person in CSV ; 1111 is the lineNumber by default.

                                    // organization map add - mapOut.put(7, Map<2222,<organization_CSV> );  <- contains organization in CSV ; 2222 is the lineNumber by default.

                                    // location map add

                                    // Take existing one (we iterate each sentence of curr document, so we take existing sentence)
                                    // noun - Take existing one
                                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey(lineNo + ":" + idx_of_$_n))
                                        mapTEMP_1 = mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + idx_of_$_n);
                                    // verb -Take existing one
                                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey(lineNo + ":" + idx_of_$_v))
                                        mapTEMP_2 = mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + idx_of_$_v);
                                    // sentence - Take existing one
                                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey(lineNo + ":" + idx_of_fndotsentiment))
                                        mapTEMP_3 = mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + idx_of_fndotsentiment);
                                    // noun_2 -> $_n2
                                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey(lineNo + ":" + idx_of_$_n2))
                                        mapTEMP_4 = mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + idx_of_$_n2);
                                    // number
                                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey(lineNo + ":" + idx_of_$_number))
                                        mapTEMP_5 = mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + idx_of_$_number);
                                    // person_CSV
                                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey(lineNo + ":" + idx_of_$_person))
                                        mapTEMP_6 = mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + idx_of_$_person);
                                    // organization_CSV
                                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey(lineNo + ":" + idx_of_$_organization))
                                        mapTEMP_7 = mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + idx_of_$_organization);
                                    // location_CSV
                                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey(lineNo + ":" + idx_of_$_location))
                                        mapTEMP_8 = mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + idx_of_$_location);

                                    // SENTIMENT using  untagged text obsolete
                                    //mapcurr_Senti.put( lineNo +":"+counter_sentence_with_N_V, curr_sentence_untaggedTEXT );

                                    if (isSOPprint)
                                        System.out.println("sentiment :" + lineNo + ":" + counter_sentence_with_N_V + "<-->" + curr_sentence_untaggedTEXT);

                                    // NOUN
                                    for (int i : tmp_1.keySet()) {
                                        if (tmp_1.get(i).length() > 1) {// "#" used instead of blank so that no issue in delimiter
                                            mapOut_1_new.put(i + ":" + counter_sentence_with_N_V, tmp_1.get(i));//OBSELETE
                                            mapTEMP_1.put(i + ":" + counter_sentence_with_N_V, tmp_1.get(i));
                                        }
                                    }
                                    // VERB
                                    for (int i : tmp_2.keySet()) {
                                        if (tmp_2.get(i).length() > 1) {
                                            mapOut_2_new.put(i + ":" + counter_sentence_with_N_V, tmp_2.get(i)); ////OBSELETE
                                            mapTEMP_2.put(i + ":" + counter_sentence_with_N_V, tmp_2.get(i));
                                        }
                                        //SENTENCE UNTAGGED
                                        mapTEMP_3.put(i + ":" + counter_sentence_with_N_V, curr_sentence_untaggedTEXT); //sentence
                                    }
                                    // NOUN_2
                                    for (int i : tmp_4.keySet()) {
                                        if (tmp_4.get(i).replace(" ", "").length() > 0)
                                            mapTEMP_4.put(i + ":" + counter_sentence_with_N_V, tmp_4.get(i));
                                    }
                                    // NUMBERS
                                    for (int i : tmp_5.keySet()) {
                                        if (tmp_5.get(i).replace(" ", "").length() > 0)
                                            mapTEMP_5.put(i + ":" + counter_sentence_with_N_V, tmp_5.get(i));
                                    }
                                    if (mapOut.containsKey(6) && tmp_6 != null) {
                                        // person
                                        for (int i : tmp_6.keySet()) {
                                            if (tmp_6.get(i).replace(" ", "").length() > 0)
                                                mapTEMP_6.put(i + ":" + counter_sentence_with_N_V, tmp_6.get(i));
                                        }
                                    }

                                    if (mapOut.containsKey(7) && tmp_7 != null) {
                                        // organization
                                        for (int i : tmp_7.keySet()) {
                                            if (tmp_7.get(i).replace(" ", "").length() > 0)
                                                mapTEMP_7.put(i + ":" + counter_sentence_with_N_V, tmp_7.get(i));
                                        }
                                    }
                                    if (mapOut.containsKey(8) && tmp_8 != null) {
                                        // location
                                        for (int i : tmp_8.keySet()) {
                                            if (tmp_8.get(i).replace(" ", "").length() > 0)
                                                mapTEMP_8.put(i + ":" + counter_sentence_with_N_V, tmp_8.get(i));
                                        }
                                    }

                                    if (isSOPprint) {
                                        System.out.println("**" + mapOut.get(1) + "!!!" + mapOut.get(2) + "!!!" + curr_doc_untaggedTEXT);
                                    }
                                    // noun map add
                                    if (mapTEMP_1.size() > 0)
                                        mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.put(lineNo + ":" + idx_of_$_n,
                                                mapTEMP_1); //noun

                                    //verb map add
                                    if (mapTEMP_2.size() > 0)
                                        mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.put(lineNo + ":" + idx_of_$_v,
                                                mapTEMP_2); //verb

                                    //input to the fn.sentiment is added here (each sentence)
                                    if (mapTEMP_3.size() > 0)
                                        mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.put(lineNo + ":" + idx_of_fndotsentiment,
                                                mapTEMP_3);

                                    //sentiment (we need sentence-level sentiment, NOT document-level. we avoid doing that here.
                                    //mapEachDOClineNoCnt_Sentiment.put(lineNo+":102", curr_senti);

                                    // noun_2 map add
                                    if (mapTEMP_4.size() > 0)
                                        mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.put(lineNo + ":" + idx_of_$_n2,
                                                mapTEMP_4);
                                    // numbers map add
                                    if (mapTEMP_5.size() > 0)
                                        mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.put(lineNo + ":" + idx_of_$_number,
                                                mapTEMP_5);

                                    // person map add -  mapOut.put(6, Map<1111,<person_CSV> );  <- contains person in CSV ; 1111 is the lineNumber by default.
                                    if (mapTEMP_6.size() > 0)
                                        mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.put(lineNo + ":" + idx_of_$_person,
                                                mapTEMP_6);

                                    // organization map add - mapOut.put(7, Map<2222,<organization_CSV> );  <- contains organization in CSV ; 2222 is the lineNumber by default.
                                    if (mapTEMP_7.size() > 0)
                                        mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.put(lineNo + ":" + idx_of_$_organization,
                                                mapTEMP_7);

                                    // location map add - mapOut.put(8, Map<3333,<location_CSV );  <- contains organization in CSV ; 3333 is the lineNumber by default.
                                    if (mapTEMP_8.size() > 0)
                                        mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.put(lineNo + ":" + idx_of_$_location,
                                                mapTEMP_8);

                                    counter_sentence_with_N_V++;

                                    if (isSOPprint)
                                        System.out.println("end counter_sentence_with_N_V:" + counter_sentence_with_N_V);
                                } //END if(!h.equals("") && curr_sentence_untaggedTEXT !=null ){
                                else {
                                    writer_debug.append("\n curr_sentence_untaggedTEXT:" + curr_sentence_untaggedTEXT + " h:" + h);
                                    writer_debug.flush();
                                }

                            } catch (Exception e) {
                                writer_debug.append("\n Error: " + e.getMessage() + " curr_sentence_taggedNLP:" + curr_doc_taggedNLP
                                        + " curr_doc_untaggedTEXT:" + curr_doc_untaggedTEXT);
                                writer_debug.flush();

                            }

                            cnt++;

                            //mapcurr_Senti=new TreeMap<String, String>(); //reset
                        } //END while(cnt<len_sentenc_untag){
                    } //END for(int lineNo:map_inFile_eachLine_tagged.keySet()){

                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey("1:" + idx_of_$_n))
                        writer_debug.append("\n first line (sample).noun :" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get("1:" + idx_of_$_n));
                    else
                        writer_debug.append("\n first line (sample).noun is null");
                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey("1:" + idx_of_$_v))
                        writer_debug.append("\n first line (sample).verb :" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get("1:" + idx_of_$_v));
                    else
                        writer_debug.append("\n first line (sample).verb is null");
                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey("1:" + idx_of_fndotsentiment))
                        writer_debug.append("\n first line (sample).fn :" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get("1:" + idx_of_fndotsentiment) + "\n");
                    else
                        writer_debug.append("\n first line (sample).fn is null \n");
                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey("1:" + idx_of_$_n2))
                        writer_debug.append("\n first line (sample).noun2 :" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get("1:" + idx_of_$_n2) + "\n");
                    else
                        writer_debug.append("\n first line (sample).noun2 is null \n");
                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey("1:" + idx_of_$_number))
                        writer_debug.append("\n first line (sample).number :" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get("1:" + idx_of_$_number) + "\n");
                    else
                        writer_debug.append("\n first line (sample).number is null \n");
                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey("1:" + idx_of_$_person))
                        writer_debug.append("\n first line (sample).person :" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get("1:" + idx_of_$_person) + "\n");
                    else
                        writer_debug.append("\n first line (sample).person is null \n");
                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey("1:" + idx_of_$_organization))
                        writer_debug.append("\n first line (sample).organization :" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get("1:" + idx_of_$_organization) + "\n");
                    else
                        writer_debug.append("\n first line (sample).organization is null \n");
                    if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey("1:" + idx_of_$_location))
                        writer_debug.append("\n first line (sample).location :" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get("1:" + idx_of_$_location) + "\n");
                    else
                        writer_debug.append("\n first line (sample).location is null \n");

                    writer_debug.flush();

                    TreeMap<Integer, String> map_generateNodesANDedges_currDoc_allSentence = new TreeMap<Integer, String>();
                    TreeMap<Integer, String> map_input_create_graph_cnt_NodePairEdgeString = new TreeMap<Integer, String>();
                    TreeMap<Integer, TreeMap<Integer, String>> map_eachDoc_map_generateNodesANDedges_currDoc_allSentence = new TreeMap<Integer, TreeMap<Integer, String>>();

                    if (isSOPprint) {
                        System.out.println("mapEachDOClineNo_mapNoun_mapVerb:" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence);
                        System.out.println("mapEachDOClineNoCnt_Sentiment:" + mapEachDOClineNoCnt_Sentiment);
                    }
                    //

                    //Iterate each doc (each line), and build nodePair_and_edge String for one graph (NOTE: each input file = one graph)
                    for (int lineNo : map_inFile_eachLine_tagged.keySet()) { //<-- IMPORTANT for TAGGED TEXT

                        //header skip
                        //if(lineNo==1) {continue;}

                        System.out.println("! Tagging lineNo:" + lineNo);
                        //if(mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey(lineNo+":100")){
                        // just take <$_n> as DoclinNo across different maps inside it.
                        // NOTE: reference is $_n
                        if (mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey(lineNo + ":" + map_SourceFeature_idx_mapping.get("$_n"))) {

                            if (isSOPprint) {
                                System.out.println("each line out:" + lineNo
                                        + "!!!noun:" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + idx_of_$_n)
                                        + "!!!verb:" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + idx_of_$_v)
                                        + "!!!sentence:" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + idx_of_fndotsentiment)
                                );
                            }

                            writer_debug.append("\neach line out:" + lineNo
                                    + "!!!noun:" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + idx_of_$_n)
                                    + "!!!verb:" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + idx_of_$_v)
                                    + "!!!sentence:" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo + ":" + idx_of_fndotsentiment)
                            );
                            writer_debug.flush();


                            // both source and dest from tags such as _nn and _vbz  -->TYPE.1
                            // one hard-coded and another from tags such as _vbz -->TYPE.2
                            // $_n->()$keyw where $keyw is a *token from inputFile(Each line) -->TYPE.3
                            // source N destination both from *tokens from inputFile(Each line)  -->TYPE.4
                            // Both are FIXED. example [A]->()[B] -->TYPE.5
                            // source (or dest) is fixed [fixed] and other one is from a token (example: $byline) -->TYPE.6
                            /////////// works for TYPE.3 and TYPE.4
                            //generate NODES and EDGES (handles node pairs of types = NOT TYPE.1 or TYPE.2 or TYPE.5 (both source & dest hard-coded) )
                            TreeMap<Integer, TreeMap<Integer, String>> map_final = generateNodesANDedges_currDocument_allSentences(
                                    lineNo,
                                    mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence,
                                    map_graph_Topology,
                                    map_graph_Topology_sourceNdest_TYPE,
                                    map_SourceFeature_idx_mapping,
                                    map_idx_interested_lineNo_Tokens_from_inputFile,
                                    map_repo_checksum_score,
                                    map_repo_CompressedSentence_score,
                                    false, //isSOPprint
                                    writer_debug,
                                    writer_global_repository_to_save_sentenceWise_sentiments,
                                    is_debug_File_Write_more
                            );

                            //System.out.println("! Tagging lineNo 2:"+lineNo);
                            // generate NODES and edges (curr document all sentences) (handles node pairs of types = TYPE.1 or TYPE.2 or TYPE.5  )
                            map_generateNodesANDedges_currDoc_allSentence = map_final.get(1);
                            //get nodepairedge of input mapping
                            TreeMap<Integer, String> map_nodepair_edge_from_input_mapping = map_final.get(2);

                            if (map_generateNodesANDedges_currDoc_allSentence != null && map_nodepair_edge_from_input_mapping != null) {
                                //
                                if (map_nodepair_edge_from_input_mapping.size() != map_generateNodesANDedges_currDoc_allSentence.size())
                                    writer_debug.append("\n 1.VERIFY:map_nodepair_edge_from_input_mapping:" + map_nodepair_edge_from_input_mapping.size()
                                            + ",map_generateNodesANDedges_currDoc_allSentence:" + map_generateNodesANDedges_currDoc_allSentence.size());
                                else {
                                    writer_debug.append("\n 1.VERIFY: same size");
                                }
                            } else {

                                if (map_nodepair_edge_from_input_mapping == null)
                                    writer_debug.append("\n NULL ONE for " + lineNo + " map_nodepair_edge_from_input_mapping=null ");
                                if (map_generateNodesANDedges_currDoc_allSentence == null)
                                    writer_debug.append("\n NULL ONE for " + lineNo + " map_generateNodesANDedges_currDoc_allSentence=null ");


                                continue;
                            }


                            writer_debug.flush();

                            // statisti
                            stat_hit_on_repo_sentimen = stat_hit_on_repo_sentimen + Integer.valueOf(map_final.get(-9).get(-9));

                            if (isSOPprint) {
                                System.out.println("*!*!gen.node.edge(cur.doc)->" + map_generateNodesANDedges_currDoc_allSentence);
                            }

                            //writer_debug.append("outString.1:"+map_generateNodesANDedges_currDoc_allSentence);

                            writer_debug.append("\n appending map_generateNodesANDedges_currDoc_allSentence.put(lineNo=" + lineNo + ")-->"
                                    + map_generateNodesANDedges_currDoc_allSentence);
                            writer_debug.flush();

                            //add to global map
                            map_eachDoc_map_generateNodesANDedges_currDoc_allSentence.put(lineNo, map_generateNodesANDedges_currDoc_allSentence);

                            //System.out.println("! Tagging lineNo 3:"+lineNo);
                            //
                            for (int h : map_generateNodesANDedges_currDoc_allSentence.keySet()) {
                                if (isSOPprint) {
                                    System.out.println("atom->" + h + "<->" + map_generateNodesANDedges_currDoc_allSentence.get(h) + map_nodepair_edge_from_input_mapping.get(h));
                                }
                                //
                                int c41 = map_input_create_graph_cnt_NodePairEdgeString.size();

                                //DEBUG
                                if (map_generateNodesANDedges_currDoc_allSentence.get(h).split("!#!#").length <= 3) {
                                    writer_debug.append("\n <=3 adding config:mapping->" + map_nodepair_edge_from_input_mapping.get(h)
                                            + "\n sentence:" + map_generateNodesANDedges_currDoc_allSentence.get(h));
                                    writer_debug.flush();
                                }

                                //(1) do splitting and stemming for nodePair_edge
                                nodePair_Edge_new = nodePair_edge_do_stemming(map_generateNodesANDedges_currDoc_allSentence.get(h),
                                        stemmer_,
                                        debugFile,
                                        false // is_Write_to_debugFile
                                );

                                //System.out.println("nodePair_Edge_new:"+nodePair_Edge_new);
                                // (2)
                                map_input_create_graph_cnt_NodePairEdgeString.put(c41 + 1,
                                        map_generateNodesANDedges_currDoc_allSentence.get(h)
                                                + nodePair_Edge_new //.replace("!#!#", " ")+"!#!#"
                                );
                                //+map_nodepair_edge_from_input_mapping.get(h)+"!#!#");
                            }// END for(int h:map_generateNodesANDedges_currDoc_allSentence.keySet()){

                            //System.out.println("! Tagging lineNo 4:"+lineNo);
                        }// END if(mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey(lineNo+":"+map_SourceFeature_idx_mapping.get("$_n"))){
                    } // END iterate each doc (each line), and build one graph (NOTE: each input file = one graph)

                    //debugging
//						for(int lineNo:map_inFile_eachLine_tagged.keySet()){
//							//
//							if(mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.containsKey(lineNo+":100")){
//								TreeMap<String,String> map_currNoun=mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo+":100");
//
//								for(String curr_lineNo_id:map_currNoun.keySet()){
//									System.out.println("atom: lineNo:"+lineNo
//											+"!!!curr_lineNo_id:"+curr_lineNo_id
//											+"\n!!!noun:"+mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo+":100").get(curr_lineNo_id)
//											+"!!!verb:"+mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo+":101").get(curr_lineNo_id)
//											+"!!!sentence:"+mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.get(lineNo+":102").get(curr_lineNo_id)
//										   );
//								}
//							}//
//						}// end FOR

                    //System.out.println("maptaggedNLP:"+maptaggedNLP.size());
                    System.out.println("map_inFile_eachLine_tagged:" + map_inFile_eachLine_tagged.size());
                    System.out.println("map_inFile_eachLine_tagged:" + map_inFile_eachLine_tagged.get(2));
                    System.out.println("map_graph_Topology:" + map_graph_Topology);
                    System.out.println("*Verify header=config!" + bool_verified_header);
                    System.out.println("create_graph_with_only_top_N_lines_of_inputFile:" + create_graph_with_only_top_N_lines_of_inputFile);

                    if (isSOPprint) {
                        //
                        for (int idx_intr_token : map_idx_interested_lineNo_Tokens_from_inputFile.keySet()) {
                            System.out.println("idx:" + idx_intr_token + " size:" +
                                    map_idx_interested_lineNo_Tokens_from_inputFile.get(idx_intr_token).size());
                        }
                        //
                        for (int s : map_graph_Topology.keySet()) {
                            System.out.println(s + "->" + map_graph_Topology.get(s));
                        }
                    }

//						// is_remove_stop_words
//						if(is_remove_stop_words){
//							System.out.println("size:"+map_eachDoc_map_generateNodesANDedges_currDoc_allSentence.size());
//							//iterate each document, each sentence, remove stop word
////							for(int lineNo:map_generateNodesANDedges_currDoc_allSentence.keySet()){
////								TreeMap<Integer, String> map_= map_eachDoc_map_generateNodesANDedges_currDoc_allSentence.get(lineNo);
////								if(map_==null){System.out.println("null..continue"); continue;}
////								//
////								for(Integer s:map_.keySet()){
////									System.out.println("dummy:"+ s+" "+map_.get(s));
////								}
////
////							}
//						}
//						//


                    //(In below handles node pairs of types = TYPE.3 or TYPE.4 )
                    //map_currDoc_map_generateNodesANDedges_currDoc_allSentence
                    //"TYPE.4" process (both nodes are document-level)<-> example $byline and $keyw  ---- orig
                    cnt = 1; //reset
                    System.out.println("reset cnt:" + cnt + "<->" + max_map_graph_Topology);
                    //graph topology iterate
                    while (cnt <= max_map_graph_Topology) {
                        String curr_source_name = map_graph_Topology.get(1).get(cnt);
                        String curr_dest_name = map_graph_Topology.get(2).get(cnt);
                        String curr_edge_feature = map_graph_Topology.get(4).get(cnt).replace("(", "").replace(")", "");
                        String curr_edge_feature_value = "";
                        TreeMap curr_fn_map = new TreeMap();
                        int edge_feature_idx = -1;
                        TreeMap<Integer, String> curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_LOADED = new TreeMap<Integer, String>();
                        TreeMap<Integer, String> curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED = new TreeMap<Integer, String>();
                        TreeMap<Integer, String> curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED = new TreeMap<Integer, String>();

                        String curr_nodepair_edge = curr_source_name + " " + curr_dest_name + " (" + curr_edge_feature + ")";
                        curr_nodepair_edge = curr_nodepair_edge.replace("((", "(").replace("))", ")");

                        writer_debug.append("\n BEFORE ENTRY FOR TYPE.4 source:destination->" + curr_source_name + ":" + curr_dest_name
                                + " GOT TYPE:" + map_graph_Topology_sourceNdest_TYPE.get(curr_source_name + ":" + curr_dest_name)
                                + " for curr_nodepair_edge=" + curr_nodepair_edge);
                        writer_debug.flush();

                        //TYPE.4 ( This will be document-level and not sentence-level )
                        if (map_graph_Topology_sourceNdest_TYPE.get(curr_source_name + ":" + curr_dest_name).equalsIgnoreCase("TYPE.4")) {
                            if (isSOPprint) {
                                System.out.println("names:" + curr_source_name + "<->" + curr_dest_name + "<->" + curr_edge_feature);
                                System.out.println("map_SourceFeature_idx_mapping:" + map_SourceFeature_idx_mapping);
                            }
                            //get index
                            int source_feature_idx = map_SourceFeature_idx_mapping.get(curr_source_name);
                            int dest_feature_idx = map_SourceFeature_idx_mapping.get(curr_dest_name);

                            //loading
                            curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED =
                                    map_idx_interested_lineNo_Tokens_from_inputFile.get(source_feature_idx);
                            curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED =
                                    map_idx_interested_lineNo_Tokens_from_inputFile.get(dest_feature_idx);
                            //edge idx
                            if (map_SourceFeature_idx_mapping.containsKey(curr_edge_feature))
                                edge_feature_idx = map_SourceFeature_idx_mapping.get(curr_edge_feature);

                            // type 2 -  TOKEN -- loading
                            if (curr_edge_feature.indexOf("$") >= 0 && curr_edge_feature.indexOf("$_") == -1) {
                                curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_LOADED =
                                        map_idx_interested_lineNo_Tokens_from_inputFile.get(edge_feature_idx);
                            }

                            writer_debug.append("\n TYPE.4 LOADED FOR curr_nodepair_edge-->" + curr_nodepair_edge);
                            if (curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED != null) {
                                writer_debug.append("\n TYPE.4 loaded curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED.size->" +
                                        curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED.size()
                                        + " for curr_source_name=" + curr_source_name);
                            } else {
                                writer_debug.append("\n TYPE.4 loaded curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED.size->NULL**"
                                        + " for curr_source_name=" + curr_source_name);

                            }
                            if (curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED != null) {
                                writer_debug.append("\n TYPE.4 loaded curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED.size->" +
                                        curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED.size()
                                        + " curr_dest_name=" + curr_dest_name);
                            } else {
                                writer_debug.append("\n TYPE.4 loaded curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED.size->NULL"
                                        + " curr_dest_name=" + curr_dest_name);
                            }

                            if (curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_LOADED != null) {
                                writer_debug.append("\n TYPE.4 loaded curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_LOADED.size->" +
                                        curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_LOADED.size()
                                        + " curr_edge_feature=" + curr_edge_feature);
                            } else {
                                writer_debug.append("\n TYPE.4 loaded curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_LOADED.size->NULL"
                                        + " curr_edge_feature=" + curr_edge_feature);
                            }
                            writer_debug.flush();

                            // type 3 -  tag  -- loading ( not possible case, think later )
                            if (curr_edge_feature.indexOf("$_") >= 0) {
                                //map_idx_interested_lineNo_Tokens_from_inputFile.get( edge_feature_idx);
                            }
                            // load function related ( think later )
                            if (curr_edge_feature.indexOf("fn.") >= 0) {
                                if (isSOPprint)
                                    System.out.println(".....load curr_interested_lineNo_Tokens_from_inputFile..");
                            }
                            System.out.println("idx:Source:" + source_feature_idx + "<->idx:Dest:" + dest_feature_idx + "<->idx:Edge:" + edge_feature_idx);
                            TreeMap<Integer, String> map_lineNo_source_value = map_idx_interested_lineNo_Tokens_from_inputFile.get(source_feature_idx);
                            TreeMap<Integer, String> map_lineNo_dest_value = map_idx_interested_lineNo_Tokens_from_inputFile.get(dest_feature_idx);
                            // apply for each line (doc)
                            for (int lineNo : map_eachDoc_map_generateNodesANDedges_currDoc_allSentence.keySet()) {

                                //System.out.println("sent:"+map_eachDoc_map_generateNodesANDedges_currDoc_allSentence.get(lineNo));

                                String _source_value = map_lineNo_source_value.get(lineNo);
                                String _dest_value = map_lineNo_dest_value.get(lineNo);

                                //System.out.println("outString.3:" +_source +"!!!"+_dest);

                                // 4 type edges->#1 fixed. ex:(has) #2 token #3 a tag (_n or _vbz) #4 function call
                                // type 1 - Hard-coded (no function, no token, not a tag (_n or _vbz)
                                if (curr_edge_feature.indexOf("fn.") == -1 && curr_edge_feature.indexOf("$") == -1 && curr_edge_feature.indexOf("$_") == -1) {
                                    String t = curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED.get(lineNo)
                                            + "!#!#" + curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED.get(lineNo)
                                            + "!#!#" + curr_edge_feature + "!#!#" + "remark:"
                                            + "!#!#" + map_graph_Topology.get(1).get(cnt) + " " + map_graph_Topology.get(2).get(cnt) + " " + remove_brackets(curr_edge_feature)
                                            + "!#!#";
                                    if (isSOPprint)
                                        System.out.println("outString.3:" + t);

                                    writer_debug.append("\n a.outString.3:" + t + "\n");
                                    writer_debug.flush();

                                    int cnt41 = map_input_create_graph_cnt_NodePairEdgeString.size();
                                    map_input_create_graph_cnt_NodePairEdgeString.put(cnt41 + 1, t);

                                }
                                // type 2 -  TOKEN
                                if (curr_edge_feature.indexOf("$") >= 0 && curr_edge_feature.indexOf("$_") == -1) {
                                    String t = curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED.get(lineNo)
                                            + "!#!#" + curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED.get(lineNo)
                                            + "!#!#" + curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_LOADED.get(lineNo)
                                            + "!#!#" + "remark:"
                                            + "!#!#" + map_graph_Topology.get(1).get(cnt) + " " + map_graph_Topology.get(2).get(cnt) + " " + remove_brackets(curr_edge_feature)
                                            + "!#!#";

                                    writer_debug.append("\n b.outString.3:" + t + "\n");
                                    writer_debug.flush();

                                    int cnt41 = map_input_create_graph_cnt_NodePairEdgeString.size();
                                    map_input_create_graph_cnt_NodePairEdgeString.put(cnt41 + 1, t);

                                }
                                // type 3 -  tag
                                //(DEFAULT: if edge_feature=$_v then edge is set of verbs from all sentences in CSV)
                                // (later think if only from top most first sentence)
                                if (curr_edge_feature.indexOf("$_") >= 0) {
                                    TreeMap<Integer, String> map_sentences = map_eachDoc_map_generateNodesANDedges_currDoc_allSentence.get(lineNo);

                                    //each senten
                                    for (int senten_seq : map_sentences.keySet()) {
                                        if (isSOPprint)
                                            System.out.println(" %% entered tag as edge feature.search curr_edge_feature=" + curr_edge_feature + " on "
                                                    + map_sentences.get(senten_seq));
                                        // match <tag> on curr sentence
                                        if (map_sentences.get(senten_seq).indexOf(curr_edge_feature.replace("$", "")) >= 0) {
                                            String matched_sentence = map_sentences.get(senten_seq);

                                            String[] arr_token_of_edge_or_nodes = matched_sentence.split("!#!#");
                                            int c40 = 0;
                                            int max_arr_token_of_edge_or_nodes = arr_token_of_edge_or_nodes.length;
                                            //
                                            while (c40 < max_arr_token_of_edge_or_nodes) {
                                                //if token of tagged curr sentence = edge_feature (tag)
                                                if (arr_token_of_edge_or_nodes[c40].indexOf(curr_edge_feature.replace("$", "")) >= 0) {
                                                    String t = curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED.get(lineNo)
                                                            + "!#!#" + curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED.get(lineNo)
                                                            + "!#!#" + arr_token_of_edge_or_nodes[c40]
                                                            + "!#!#" + "remark:"
                                                            + "!#!#" + map_graph_Topology.get(1).get(cnt) + " " + map_graph_Topology.get(2).get(cnt) + " " + remove_brackets(curr_edge_feature)
                                                            + "!#!#";

                                                    if (isSOPprint)
                                                        System.out.println("outString.3(tag):" + t);

                                                    writer_debug.append("\n c.outString.3(tag):" + t + "\n");
                                                    writer_debug.flush();

                                                    int cnt41 = map_input_create_graph_cnt_NodePairEdgeString.size();
                                                    map_input_create_graph_cnt_NodePairEdgeString.put(cnt41 + 1, t);
                                                    break; //only first matters, so break it
                                                }

                                                c40++;
                                            }
                                        }
                                    }
                                }
                                // type 4 - function call (NOT TESTED) ( think later )
                                else if (curr_edge_feature.indexOf("fn.") >= 0) {
                                    // which fn?
                                    if (curr_edge_feature.equalsIgnoreCase("fn.sentiment")) {
                                        //
                                        //if(curr_interested_lineNo_Tokens_from_inputFile==null)
                                        System.out.println("curr_interested_lineNo_Tokens_from_inputFile is null..");
                                        //
//											curr_edge_feature_value= find_sentiments_of_a_sentence
//																	.find_sentiments_classify1(  .get(lineNo));
                                    }
                                    String t = map_SourceFeature_idx_mapping.get(source_feature_idx)
                                            + "!#!#" + map_SourceFeature_idx_mapping.get(dest_feature_idx)
                                            + "!#!#" + curr_edge_feature_value
                                            + "!#!#" + "remark:"
                                            + "!#!#" + map_graph_Topology.get(1).get(cnt) + " " + map_graph_Topology.get(2).get(cnt) + " " + remove_brackets(curr_edge_feature)
                                            + "!#!#";
                                    if (isSOPprint)
                                        System.out.println("outString.3:" + t);

                                    writer_debug.append("\n d.outString.3:" + t + "\n");
                                    writer_debug.flush();

                                    int cnt41 = map_input_create_graph_cnt_NodePairEdgeString.size();
                                    map_input_create_graph_cnt_NodePairEdgeString.put(cnt41 + 1, t);
                                }
                            } // apply for each line(doc)
                        } // END = TYPE.4 ( This will be document-level and not sentence-level )
                        cnt++;
                    }


                    //map_currDoc_map_generateNodesANDedges_currDoc_allSentence
                    //"TYPE.6" process hard-code [] and token ($keyw )
                    cnt = 1; //reset
                    System.out.println("reset cnt:" + cnt + "<->" + max_map_graph_Topology);
                    //graph topology iterate
                    while (cnt <= max_map_graph_Topology) {
                        String curr_source_name = map_graph_Topology.get(1).get(cnt);
                        String curr_dest_name = map_graph_Topology.get(2).get(cnt);
                        String curr_edge_feature = map_graph_Topology.get(4).get(cnt).replace("(", "").replace(")", "");
                        String curr_edge_feature_value = "";
                        TreeMap curr_fn_map = new TreeMap();
                        int edge_feature_idx = -1;
                        TreeMap<Integer, String> curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_LOADED = new TreeMap<Integer, String>();
                        TreeMap<Integer, String> curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED = new TreeMap<Integer, String>();
                        TreeMap<Integer, String> curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED = new TreeMap<Integer, String>();

                        String curr_nodepair_edge = curr_source_name + " " + curr_dest_name + " (" + curr_edge_feature + ")";
                        writer_debug.append("\n TYPE.6 map_graph_Topology_sourceNdest_TYPE.get(curr_source:curr_dest):"
                                + map_graph_Topology_sourceNdest_TYPE.get(curr_source_name + ":" + curr_dest_name) + " for curr_nodepair_edge=" + curr_nodepair_edge);
                        writer_debug.flush();

                        curr_nodepair_edge = curr_nodepair_edge.replace("((", "(").replace("))", ")");
                        //TYPE.6 ( This will be document-level and not sentence-level )
                        if (map_graph_Topology_sourceNdest_TYPE.get(curr_source_name + ":" + curr_dest_name).equalsIgnoreCase("TYPE.6")) {
                            if (isSOPprint) {
                                System.out.println("names:" + curr_source_name + "<->" + curr_dest_name + "<->" + curr_edge_feature);
                                System.out.println("map_SourceFeature_idx_mapping:" + map_SourceFeature_idx_mapping);
                            }
                            //get index
                            int source_feature_idx = -1;
                            int dest_feature_idx = -1;
                            if (map_SourceFeature_idx_mapping.containsKey(curr_source_name))
                                source_feature_idx = map_SourceFeature_idx_mapping.get(curr_source_name);
                            if (map_SourceFeature_idx_mapping.containsKey(curr_dest_name)) {
                                dest_feature_idx = map_SourceFeature_idx_mapping.get(curr_dest_name);
                            }

                            //loading (Which one is token and which one is hard-coded)
                            curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED =
                                    map_idx_interested_lineNo_Tokens_from_inputFile.get(source_feature_idx);

                            curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED =
                                    map_idx_interested_lineNo_Tokens_from_inputFile.get(dest_feature_idx);

                            //type.6 ONE OF source or dest is fixed []

                            //edge idx
                            if (map_SourceFeature_idx_mapping.containsKey(curr_edge_feature))
                                edge_feature_idx = map_SourceFeature_idx_mapping.get(curr_edge_feature);

                            // type 2 -  TOKEN -- loading
                            if (curr_edge_feature.indexOf("$") >= 0 && curr_edge_feature.indexOf("$_") == -1) {
                                curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_LOADED =
                                        map_idx_interested_lineNo_Tokens_from_inputFile.get(edge_feature_idx);
                            }

                            writer_debug.append("\n LOADED FOR curr_nodepair_edge-->" + curr_nodepair_edge);
                            if (curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED != null)
                                writer_debug.append("\n TYPE.6 loaded curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED.size->" +
                                        curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED.size());
                            else {
                                writer_debug.append("\n TYPE.6 loaded curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED IS NULL**->"
                                        + "source_feature_idx:" + source_feature_idx + " curr_source_name:" + curr_source_name);
                            }
                            if (curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED != null) {
                                writer_debug.append("\n TYPE.6 loaded curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED.size->" +
                                        curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED.size()
                                        + " curr_dest_name:" + curr_dest_name);
                            } else {
                                writer_debug.append("\n TYPE.6 loaded curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED.size is NULL** ->"
                                        + "dest_feature_idx:" + dest_feature_idx + " curr_dest_name:" + curr_dest_name);
                            }
                            if (curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_LOADED != null)
                                writer_debug.append("\n TYPE.6 loaded curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_LOADED.size->" +
                                        curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_LOADED.size());
                            else
                                writer_debug.append("\n TYPE.6 loaded curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_LOADED is NULL**->");
                            writer_debug.flush();

                            // type 3 -  tag  -- loading ( not possible case, think later )
                            if (curr_edge_feature.indexOf("$_") >= 0) {
                                //map_idx_interested_lineNo_Tokens_from_inputFile.get( edge_feature_idx);
                            }
                            // load function related ( think later )
                            if (curr_edge_feature.indexOf("fn.") >= 0) {
                                if (isSOPprint)
                                    System.out.println(".....load curr_interested_lineNo_Tokens_from_inputFile..");
                            }
                            System.out.println("idx:" + source_feature_idx + "<->" + dest_feature_idx + "<->" + edge_feature_idx);
                            TreeMap<Integer, String> map_lineNo_source_value = map_idx_interested_lineNo_Tokens_from_inputFile.get(source_feature_idx);
                            TreeMap<Integer, String> map_lineNo_dest_value = map_idx_interested_lineNo_Tokens_from_inputFile.get(dest_feature_idx);
                            // apply for each line (doc)
                            for (int lineNo : map_eachDoc_map_generateNodesANDedges_currDoc_allSentence.keySet()) {

                                //System.out.println("sent:"+map_eachDoc_map_generateNodesANDedges_currDoc_allSentence.get(lineNo));
                                String _source_value = "";
                                String _dest_value = "";
                                //source
                                if (map_lineNo_source_value != null) {
                                    if (map_lineNo_source_value.containsKey(lineNo))
                                        _source_value = map_lineNo_source_value.get(lineNo);
                                }
                                //dest
                                if (map_lineNo_dest_value != null) {
                                    if (map_lineNo_dest_value.containsKey(lineNo))
                                        _dest_value = map_lineNo_dest_value.get(lineNo);
                                }

                                //System.out.println("outString.3:" +_source +"!!!"+_dest);
                                String t = "";
                                // 4 type edges->#1 fixed. ex:(has) #2 token #3 a tag (_n or _vbz) #4 function call
                                // type 1 - Hard-coded (no function, no token, not a tag (_n or _vbz)
                                if (curr_edge_feature.indexOf("fn.") == -1 && curr_edge_feature.indexOf("$") == -1 && curr_edge_feature.indexOf("$_") == -1) {

                                    //if source is fixed/hard-coded []
                                    if (source_feature_idx == -1 && dest_feature_idx > 0) {

                                        System.out.println("**contains: " + curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED.size() + "--");

                                        t = curr_source_name
                                                + "!#!#" + curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED.get(lineNo)
                                                + "!#!#" + curr_edge_feature + "!#!#" + "remark:"
                                                + "!#!#" + curr_source_name + " " + curr_dest_name + " " + remove_brackets(curr_edge_feature)
                                                + "!#!#";
                                    }
                                    //if dest is fixed/hard-coded []
                                    else if (source_feature_idx > 0 && dest_feature_idx == -1) {
                                        t = curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED.get(lineNo)
                                                + "!#!#" + curr_dest_name
                                                + "!#!#" + curr_edge_feature + "!#!#"
                                                + "remark:"
                                                + "!#!#" + curr_source_name + " " + curr_dest_name + " " + remove_brackets(curr_edge_feature)
                                                + "!#!#";
                                    }

                                    if (isSOPprint)
                                        System.out.println("outString.3:" + t);

                                    writer_debug.append("\n e.outString.3:" + t + "\n");
                                    writer_debug.flush();

                                    int cnt41 = map_input_create_graph_cnt_NodePairEdgeString.size();
                                    map_input_create_graph_cnt_NodePairEdgeString.put(cnt41 + 1, t);

                                }
                                // type 2 -  TOKEN
                                if (curr_edge_feature.indexOf("$") >= 0 && curr_edge_feature.indexOf("$_") == -1) {


                                    t = curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED.get(lineNo)
                                            + "!#!#" + curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED.get(lineNo)
                                            + "!#!#" + curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_LOADED.get(lineNo)
                                            + "!#!#" + "remark:"
                                            + "!#!#" + curr_source_name + " " + curr_dest_name + " " + remove_brackets(curr_edge_feature)
                                            + "!#!#";


                                    writer_debug.append("\n f.outString.3:" + t + "\n");
                                    writer_debug.flush();

                                    int cnt41 = map_input_create_graph_cnt_NodePairEdgeString.size();
                                    map_input_create_graph_cnt_NodePairEdgeString.put(cnt41 + 1, t);

                                }
                                // type 3 -  tag
                                //(DEFAULT: if edge_feature=$_v then edge is set of verbs from all sentences in CSV)
                                // (later think if only from top most first sentence)
                                if (curr_edge_feature.indexOf("$_") >= 0) {
                                    TreeMap<Integer, String> map_sentences = map_eachDoc_map_generateNodesANDedges_currDoc_allSentence.get(lineNo);

                                    //each senten
                                    for (int senten_seq : map_sentences.keySet()) {
                                        if (isSOPprint)
                                            System.out.println(" %% entered tag as edge feature.search curr_edge_feature=" + curr_edge_feature + " on "
                                                    + map_sentences.get(senten_seq));
                                        // match <tag> on curr sentence
                                        if (map_sentences.get(senten_seq).indexOf(curr_edge_feature.replace("$", "")) >= 0) {
                                            String matched_sentence = map_sentences.get(senten_seq);

                                            String[] arr_token_of_edge_or_nodes = matched_sentence.split("!#!#");
                                            int c40 = 0;
                                            int max_arr_token_of_edge_or_nodes = arr_token_of_edge_or_nodes.length;
                                            //
                                            while (c40 < max_arr_token_of_edge_or_nodes) {
                                                //if token of tagged curr sentence = edge_feature (tag)
                                                if (arr_token_of_edge_or_nodes[c40].indexOf(curr_edge_feature.replace("$", "")) >= 0) {
                                                    t = curr_interested_lineNo_Tokens_from_inputFile_for_source_feature_LOADED.get(lineNo)
                                                            + "!#!#" + curr_interested_lineNo_Tokens_from_inputFile_for_dest_feature_LOADED.get(lineNo)
                                                            + "!#!#" + arr_token_of_edge_or_nodes[c40] + "!#!#" + "remark:"
                                                            + "!#!#" + curr_source_name + " " + curr_dest_name + " " + remove_brackets(curr_edge_feature)
                                                            + "!#!#";

                                                    if (isSOPprint)
                                                        System.out.println("outString.3(tag):" + t);

                                                    writer_debug.append("\n g.outString.3(tag):" + t + "\n");
                                                    writer_debug.flush();

                                                    int cnt41 = map_input_create_graph_cnt_NodePairEdgeString.size();
                                                    map_input_create_graph_cnt_NodePairEdgeString.put(cnt41 + 1, t);
                                                    break; //only first matters, so break it
                                                }

                                                c40++;
                                            }
                                        }
                                    }
                                }
                                // type 4 - function call (NOT TESTED) ( think later )
                                else if (curr_edge_feature.indexOf("fn.") >= 0) {
                                    // which fn?
                                    if (curr_edge_feature.equalsIgnoreCase("fn.sentiment")) {
                                        //
                                        //if(curr_interested_lineNo_Tokens_from_inputFile==null)
                                        System.out.println("curr_interested_lineNo_Tokens_from_inputFile is null..");
                                        //
//											curr_edge_feature_value= find_sentiments_of_a_sentence
//																	.find_sentiments_classify1(  .get(lineNo));
                                    }
                                    t = map_SourceFeature_idx_mapping.get(source_feature_idx)
                                            + "!#!#" + map_SourceFeature_idx_mapping.get(dest_feature_idx)
                                            + "!#!#" + curr_edge_feature_value + "!#!#" + "remark:"
                                            + "!#!#" + map_graph_Topology.get(1).get(cnt) + " " + map_graph_Topology.get(2).get(cnt) + " " + remove_brackets(curr_edge_feature)
                                            + "!#!#";
                                    if (isSOPprint)
                                        System.out.println("outString.3:" + t);

                                    writer_debug.append("\n h.outString.3:" + t + "\n");
                                    writer_debug.flush();

                                    int cnt41 = map_input_create_graph_cnt_NodePairEdgeString.size();
                                    map_input_create_graph_cnt_NodePairEdgeString.put(cnt41 + 1, t);
                                }
                            } // apply for each line(doc)
                        } // END = TYPE.6 ( This will be document-level and not sentence-level )
                        cnt++;
                    }
                    // so we pick each interested tag (from each tagged sentence) and connect it to the document-level token.
                    TreeMap<Integer, String> curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED = new TreeMap<Integer, String>();
                    TreeMap<Integer, String> curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_token_LOADED = new TreeMap<Integer, String>();
                    cnt = 1;
                    // "TYPE.3" process <-> example: $_n (tag) and $keyw (token) ( relationship between sentence-level (tags) and document-level (token))
                    // graph topology iterate
                    while (cnt <= max_map_graph_Topology) {
                        int edge_feature_idx = -1;
                        int source_feature_idx = -1;
                        int dest_feature_idx = -1;
                        String curr_source_name = map_graph_Topology.get(1).get(cnt);
                        String curr_dest_name = map_graph_Topology.get(2).get(cnt);
                        String curr_edge_feature = map_graph_Topology.get(4).get(cnt).replace("(", "").replace(")", "");
                        String curr_edge_feature_value = "";
                        curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED = new TreeMap<Integer, String>();
                        curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_token_LOADED = new TreeMap<Integer, String>();
                        writer_debug.append("\n BEFORE ENTRY FOR TYPE.3 source:destination->" + curr_source_name + ":" + curr_dest_name
                                + " GOT TYPE:" + map_graph_Topology_sourceNdest_TYPE.get(curr_source_name + ":" + curr_dest_name));
                        writer_debug.flush();

                        // TYPE.3
                        if (map_graph_Topology_sourceNdest_TYPE.get(curr_source_name + ":" + curr_dest_name).equalsIgnoreCase("TYPE.3")) {
                            //
                            writer_debug.append("\n ENTERING type.3: " + curr_source_name + ":" + curr_dest_name);
                            writer_debug.flush();
                            String given_tag = "", given_token = "";
                            int given_token_idx = -1;
                            //given_tag to match (one of below)
                            if (curr_source_name.indexOf("$_") >= 0)
                                given_tag = curr_source_name.replace("$", "");
                            if (curr_dest_name.indexOf("$_") >= 0)
                                given_tag = curr_dest_name.replace("$", "");

                            //given_token to match (one of below)
                            if (curr_source_name.indexOf("$") >= 0 && curr_source_name.indexOf("$_") == -1)
                                given_token = curr_source_name;
                            if (curr_dest_name.indexOf("$") >= 0 && curr_dest_name.indexOf("$_") == -1)
                                given_token = curr_dest_name;
                            //
                            writer_debug.append("\n^^^given tag:" + curr_source_name + "<->" + curr_dest_name + "<->" + given_tag + "<->" + given_token
                                    + " curr_edge_feature:" + curr_edge_feature + "<--map-->" + map_SourceFeature_idx_mapping);
                            writer_debug.flush();

                            if (isSOPprint)
                                System.out.println("^^^given tag:" + curr_source_name + "<->" + curr_dest_name + "<->" + given_tag + "<->" + given_token
                                        + " curr_edge_feature:" + curr_edge_feature);

                            //get index source, destination, and edge feature
                            if (map_SourceFeature_idx_mapping.containsKey(curr_source_name))
                                source_feature_idx = map_SourceFeature_idx_mapping.get(curr_source_name);
                            if (map_SourceFeature_idx_mapping.containsKey(curr_dest_name))
                                dest_feature_idx = map_SourceFeature_idx_mapping.get(curr_dest_name);
                            if (map_SourceFeature_idx_mapping.containsKey(curr_edge_feature))
                                edge_feature_idx = map_SourceFeature_idx_mapping.get(curr_edge_feature);
                            //edge feature is a token
                            if (curr_edge_feature.indexOf("$") >= 0 && curr_edge_feature.indexOf("$_") == -1 && edge_feature_idx > 0) {
                                curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_token_LOADED
                                        = map_idx_interested_lineNo_Tokens_from_inputFile.get(edge_feature_idx);
                                //error
                                if (edge_feature_idx < 0) {
                                    System.out.println("error: edge_feature_idx should be negative ");
                                }
                            }
                            // idx of token (source (or) destination) - only
                            if (source_feature_idx < 100 && source_feature_idx > 0) {
                                given_token_idx = source_feature_idx;
                            } else if (dest_feature_idx < 100 && dest_feature_idx > 0) {
                                given_token_idx = dest_feature_idx;
                            }
                            //
                            String curr_nodepair_edge = curr_source_name + " " + curr_dest_name + " (" + curr_edge_feature + ")";
                            curr_nodepair_edge = curr_nodepair_edge.replace("((", "(").replace("))", ")");
                            //load the token map (which is either a source or a destination)
                            curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED = map_idx_interested_lineNo_Tokens_from_inputFile.get(given_token_idx);

                            if (curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED != null)
                                writer_debug.append("\n TYPE.3 loaded given_token_idx:" + given_token_idx + " curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED:"
                                        + curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED.size());
                            else
                                writer_debug.append("\n TYPE.3 loaded given_token_idx:" + given_token_idx + " curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED:"
                                        + curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED);

                            writer_debug.flush();
                            //
                            if (isSOPprint)
                                System.out.println("^^^idx source,dest:" + source_feature_idx + " " + dest_feature_idx + " given_token_idx:" + given_token_idx
                                        + " loaded.token.map.size:" + curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED.size());

                            // apply for each line (doc) (ALL nodePair_and_edge generated must be here)
                            for (int lineNo : map_eachDoc_map_generateNodesANDedges_currDoc_allSentence.keySet()) {
                                //header skip

                                System.out.println("Processing all sentenctes of document:" + lineNo);
                                TreeMap<Integer, String> map_tagged_set_of_sentence = map_eachDoc_map_generateNodesANDedges_currDoc_allSentence.get(lineNo);

                                writer_debug.append("\n TYPE.3 map_eachDoc_map_generateNodesANDedges_currDoc_allSentence.get(lineNo=" + lineNo + "):"
                                        + map_eachDoc_map_generateNodesANDedges_currDoc_allSentence.get(lineNo));
                                writer_debug.flush();

                                //
                                if (isSOPprint)
                                    System.out.println(lineNo + " map_tagged_set_of_sentence:" + map_tagged_set_of_sentence);
                                // curr_sentence_seq_id
                                for (int curr_sentence_seq_id : map_tagged_set_of_sentence.keySet()) {
                                    String curr_line_curr_sentence = map_tagged_set_of_sentence.get(curr_sentence_seq_id);
                                    if (isSOPprint) {
                                        System.out.println("\n--------------------------iterate on mapping--------" + curr_source_name + ":" + curr_dest_name + " curr_edge:" + curr_edge_feature);
                                        System.out.println("sentence:" + curr_line_curr_sentence + " given tag:" + given_tag);
                                    }

                                    //does contain given tag?
                                    if (curr_line_curr_sentence.indexOf(given_tag) == -1) {
                                        System.out.println("continue.");
                                        writer_debug.append("\n TYPE.3 ***continue.given.tag->" + given_tag + "----on string---->" + curr_line_curr_sentence);
                                        writer_debug.flush();
                                        continue;
                                    }
                                    String[] s = curr_line_curr_sentence.split("!#!#");
                                    int c10 = 0;
                                    String curr_line_curr_senten = "";
                                    String curr_line_curr_senten_curr_tag_String = "";
                                    // iterate each token of curr_line_curr_sentence
                                    while (c10 < s.length) {
                                        curr_line_curr_senten = s[c10];
                                        if (isSOPprint)
                                            System.out.println("each token:" + curr_line_curr_senten);
                                        String[] s2 = curr_line_curr_senten.split("!!!");
                                        int c20 = 0;
                                        //split token of sentence-wise node-pair and edge string. <node_1!!!node_2!!!>
                                        while (c20 < s2.length) {
                                            if (isSOPprint)
                                                System.out.println("check:" + "-------token:" + s[c10]);
                                            // success match of token containing given tag
                                            if (curr_line_curr_senten.indexOf(given_tag) >= 0) {
                                                if (isSOPprint) {
                                                    System.out.println("###matched token:" + s2[c20] + " (on match=" + given_tag + ")"
                                                            + "<---->lineNo:" + lineNo + " token:"
                                                            + " LOADED:" + curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED.get(lineNo)
                                                            + "\n curr_edge_feature:" + curr_edge_feature
                                                    );
                                                }

                                                writer_debug.append("\n###matched token:" + s2[c20] + " (on match=" + given_tag + ")"
                                                        + "<---->lineNo:" + lineNo + " token:"
                                                        + "\n curr_edge_feature:" + curr_edge_feature
                                                );
                                                //DEBUG
                                                if (curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED != null) {
                                                    if (curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED.containsKey(lineNo)) {
                                                        writer_debug.append("\n## here:" + curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED.get(lineNo));
                                                    } else
                                                        writer_debug.append("\n## ERROR: IS NULL");
                                                } else
                                                    writer_debug.append("\n## ERROR: LOADED curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED IS NULL"
                                                            + "	for given_tag:" + given_tag + " given_token:" + given_token);

                                                writer_debug.flush();
                                                if (curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED.get(lineNo) == null) {
                                                    writer_debug.append("\n lineNo:" + lineNo + " curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED.get(lineNo):"
                                                            + curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED
                                                    );
                                                    writer_debug.flush();
                                                }


                                                // 4 type edges->#1 fixed. ex:(has) #2 token #3 a tag (_n or _vbz) #4 function call
                                                // type 1 - Hard-coded (no function, no token, not a tag (_n or _vbz)
                                                if (curr_edge_feature.indexOf("fn.") == -1 && curr_edge_feature.indexOf("$") == -1 && curr_edge_feature.indexOf("$_") == -1) {
                                                    String t = s2[c20]
                                                            + "!#!#" + curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED.get(lineNo)
                                                            + "!#!#" + curr_edge_feature
                                                            + "!#!#" + "remark:2"
                                                            + "!#!#" + map_graph_Topology.get(1).get(cnt) + " " + map_graph_Topology.get(2).get(cnt) + " " + remove_brackets(curr_edge_feature)
                                                            + "!#!#";
                                                    if (isSOPprint)
                                                        System.out.println(" a.outString.4:" + t);

                                                    //start cant be tag
                                                    if (t.indexOf("$_") >= 0) {
                                                        c20++;
                                                        continue;
                                                    }
                                                    writer_debug.append("\n a.outString.4:" + t + "!#!#" + "\n");
                                                    writer_debug.flush();


                                                    int cnt41 = map_input_create_graph_cnt_NodePairEdgeString.size();

                                                    map_input_create_graph_cnt_NodePairEdgeString.put(cnt41 + 1, t);


                                                }
                                                // type 2 -  TOKEN
                                                if (curr_edge_feature.indexOf("$") >= 0 && curr_edge_feature.indexOf("$_") == -1) {
                                                    String t = s2[c20]
                                                            + "!#!#" + curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED.get(lineNo)
                                                            + "!#!#" + curr_interested_lineNo_Tokens_from_inputFile_for_edge_feature_token_LOADED.get(lineNo)
                                                            + "!#!#" + "remark:3"
                                                            + "!#!#" + map_graph_Topology.get(1).get(cnt) + " " + map_graph_Topology.get(2).get(cnt) + " " + remove_brackets(curr_edge_feature)
                                                            + "!#!#";
                                                    if (isSOPprint)
                                                        System.out.println("outString.4:" + t);
                                                    //start cant be tag
                                                    if (t.indexOf("$_") >= 0) {
                                                        c20++;
                                                        continue;
                                                    }

                                                    writer_debug.append("\n b.outString.4:" + t + "!#!#" + curr_nodepair_edge + "\n");
                                                    writer_debug.flush();


                                                    int cnt41 = map_input_create_graph_cnt_NodePairEdgeString.size();
                                                    map_input_create_graph_cnt_NodePairEdgeString.put(cnt41 + 1, t);
                                                }
                                                // type 3 -  tag (tested)
                                                if (curr_edge_feature.indexOf("$_") >= 0) {
                                                    int c30 = 0;
                                                    //iterate each token
                                                    while (c30 < s.length) {
//																System.out.println( "tag as node N tag as edge: search curr_edge_feature:"+curr_edge_feature+" on =>"+s[c30]
//																			+ " s.len:"+s.length+ "<->"+s[c30]);


                                                        //
                                                        if (s[c30].indexOf(curr_edge_feature.replace("$", "")) >= 0) {
                                                            String t = s2[c20]
                                                                    + "!#!#" + curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED.get(lineNo)
                                                                    + "!#!#" + s[c30]
                                                                    + "!#!#" + "remark:1"
                                                                    + "!#!#" + map_graph_Topology.get(1).get(cnt) + " " + map_graph_Topology.get(2).get(cnt) + " " + remove_brackets(curr_edge_feature)
                                                                    + "!#!#";

                                                            if (isSOPprint)
                                                                System.out.println("outString.4:" + t);
                                                            //start cant be tag
                                                            if (t.indexOf("$_") >= 0) {
                                                                c20++;
                                                                continue;
                                                            }

                                                            writer_debug.append("\n c.outString.4:" + t + "!#!#" + "\n");
                                                            writer_debug.flush();

                                                            int cnt41 = map_input_create_graph_cnt_NodePairEdgeString.size();
                                                            map_input_create_graph_cnt_NodePairEdgeString.put(cnt41 + 1, t);
                                                        }
                                                        c30++;
                                                    }
                                                }
                                                // type 4 - function call (NOT TESTED) ( think later )
                                                else if (curr_edge_feature.indexOf("fn.") >= 0) {

                                                    // which fn?
                                                    if (curr_edge_feature.equalsIgnoreCase("fn.sentiment")) {
                                                        //
                                                        //if(curr_interested_lineNo_Tokens_from_inputFile==null)
                                                        if (isSOPprint)
                                                            System.out.println("curr_interested_lineNo_Tokens_from_inputFile is null..");
                                                        //
//															curr_edge_feature_value= find_sentiments_of_a_sentence
//																					.find_sentiments_classify1(  .get(lineNo));
                                                    }
                                                }
                                            }
                                            c20++;
                                        }
                                        c10++;
                                    }
                                }// END for(int curr_sentence_seq_id: map_tagged_set_of_sentence.keySet()){

                            } //end for(int lineNo:map_eachDoc_map_generateNodesANDedges_currDoc_allSentence.keySet()){

                        } //END if(map_graph_Topology_sourceNdest_TYPE.get(curr_source_name+":"+curr_dest_name).equalsIgnoreCase("TYPE.3")){
                        cnt++;
                    }

                    if (isSOPprint) {
                        System.out.println("curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED:" + curr_interested_lineNo_Tokens_from_inputFile_for_token_feature_LOADED.size());
                        System.out.println("graph_topology:" + graph_topology);
                    }

                    cnt = 0;

//						String curr_doc_curr_sent=curr_doc_sentences[cnt];
//						if(cnt<len_sentenc){
//						cnt++;
//						}
                    writer_debug.append("---------------------------------------\n");
                    //debug: input to create graph
                    for (int i : map_input_create_graph_cnt_NodePairEdgeString.keySet()) {
                        if (isSOPprint)
                            System.out.println("iter:" + i + "<->" + map_input_create_graph_cnt_NodePairEdgeString.get(i));
                        writer_debug.append("nodepairEdgeString<->" + i + "<->" + map_input_create_graph_cnt_NodePairEdgeString.get(i) + "\n");
                        writer_debug.flush();
                    }
                    //
                    if (last_xp_number > 0) {
                        xp_number = last_xp_number;
                    }

                    //create gbadgraph: PREPROCESS and get the map of lines (node_pair and edge).
                    TreeMap<Integer, String> map_nodePair_N_edge_for_graph_creation_gbad = create_gbad_graph_preprocess_and_create(
                            newTempFolder, //output to _temp folder created
                            outFile_for_gbad_graph,
                            map_input_create_graph_cnt_NodePairEdgeString,
                            map_distinct_SourceDestEdge_as_KEY,
                            map_distinct_SourceDestEdge_ORIG_as_KEY_REVERSE,
                            debugFile,
                            isSOPprint,
                            is_debug_File_Write_more,
                            is_remove_NLPtag_in_graph_output,
                            is_remove_CSV_delimiter_in_final_Element_in_graph_created,
                            is_create_each_document_as_an_XP_in_graph,
                            isskip_d3js_creation,
                            xp_number,
                            map_repo_token_semantic_equiv,
                            writer_global_repository_to_store_token_semantic_Pair,
                            is_split_on_blank_space_among_tokens,
                            is_write_to_repository_if_token_semantic_Pair
                    );
                    //
                    stat_hit_on_repo_semantic = stat_hit_on_repo_semantic + Integer.valueOf(map_nodePair_N_edge_for_graph_creation_gbad.get(-9));

                    System.out.println("map_distinct_SourceDestEdge_as_KEY:" + map_distinct_SourceDestEdge_as_KEY);
                    System.out.println("map_distinct_SourceDestEdge_ORIG_as_KEY:" + map_distinct_SourceDestEdge_ORIG_as_KEY);
                    System.out.println("map_distinct_SourceDestEdge_ORIG_as_KEY_REVERSE:" + map_distinct_SourceDestEdge_ORIG_as_KEY_REVERSE);
                    ///System.out.println("maptaggedNLP:"+maptaggedNLP.size());
                    System.out.println("map_inFile_eachLine_tagged:" + map_inFile_eachLine_tagged.size());
                    System.out.println("map_graph_Topology:" + map_graph_Topology);
                    System.out.println("*****Verify header=config!" + bool_verified_header);
                    System.out.println("mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.size:" + mapEachDOClineNoANDcnt_mapNoun_mapVerb_mapSentence.size());
                    System.out.println("stat_hit_on_repo_sentimen:" + stat_hit_on_repo_sentimen);
                    System.out.println("stat_hit_on_repo_semantic:" + stat_hit_on_repo_semantic);
                    System.out.println("stat_hit_on_repo_NLPtagging:" + stat_hit_on_repo_NLPtagging);
                    //
                    if (map_repo_checksum_score.size() == 0 || map_repo_token_semantic_equiv.size() == 0) {
                        System.out.println("*Loaded Repository Zero!-->" + map_repo_checksum_score.size() + " " + map_repo_token_semantic_equiv.size());
                    }
                    writer_debug.append("\nLast XP_number used:" + map_nodePair_N_edge_for_graph_creation_gbad.get(-99));
                    writer_debug.flush();

                    last_xp_number = Integer.valueOf(map_nodePair_N_edge_for_graph_creation_gbad.get(-99));

                    cnt_file_procss++;

                } // END while(cnt_file_procss<total_no_input_files){
                writer_out.close();
                writer_debug.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writer_debug != null)
                    try {
                        writer_debug.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                if (writer_out != null)
                    try {
                        writer_out.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            }

            //} //END if(mainType.equalsIgnoreCase("createGBADgraphFromTextUsingNLP"))


        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; //success
    }

    // main
    public static void main(String[] args) {
        System.out.println("graph12");
        long t0 = System.nanoTime();
        /////#######//////////////#######//////////////#######//////////////#######//////////////#######///////
        String WSbaseFolder = "/Users/lenin/Downloads/#NOW/VAST2017/";
        String baseFolder = "/Users/lenin/Downloads/Data/vast2015mc1/MC12015Data/";
        // Location of config file..
        String tmpFolder = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/p.5/";
        //String fileName="nf-chunk1.csv";
        String configFile = "config.txt";
        String outFile = "out-Fri-MC1-VAST2015.txt";
        boolean isSOPprint = false;
        // ---------------- README.txt ---------------- ---------------- ---------------- ---------------- ----------------
        //(1) mainType-> {createCSVtoGBADgraph, createHTMLtoGBADgraph, createXMLtoGBADgraph, createGraphFromTextUsingNLP
        //   			  createGBADgraphFromTextUsingNLP, convertNYTnewsARTICLEtoGBADgraph}
        //(2) When mainType="convertNYTnewsARTICLEtoGBADgraph" then "Flag" variable has to be set. Otherwise doesnt matter
        //  	*Flag="SplitFilesForSlidingWindow" -> split into 30 days.
        // 		*Flag="CreateGBADFile" -> Created GBAD file
        //(3) Update values inside "configFile" file
        // ---------------- ---------------- ---------------- ---------------- ---------------- ---------------- -----------
        String mainType = "createCSVtoGBADgraph";
        String Flag = "SplitFilesForSlidingWindow";// {"SplitFilesForSlidingWindow","CreateGBADFile"}
        Flag = "CreateGBADFile";

        boolean isDebug = false;
        String graphtopology = "";
        // graphtopology="[person]->(with)[ID],[ID]->(is)1.substr(1_to_6)AND2.substr(0_to_2),[person]->(xy)[XY],[XY]->(3)3.substr(1_to_3)AND3.substr(1_to_4)
        // createCSVtoGBADgraph
        if (mainType.equalsIgnoreCase("createCSVtoGBADgraph")) {

            System.out.println("createCSVtoGBADgraph");
            TreeMap<Integer, String> mapEdgeLabel_4_ChainNode = new TreeMap<Integer, String>();
            TreeMap<Integer, String> map_dest2_4_ChainNode = new TreeMap<Integer, String>();
            //read config
            TreeMap<String, String> mapConfig = ReadConfig.readConfig(tmpFolder + configFile, "=");

            graphtopology = mapConfig.get("s1.graphtopology");
            //String attributefunction=mapConfig.get("attributefunction");
            //
            TreeMap<Integer, TreeMap<Integer, String>> graphTopologyMaps =
                    ConvertPatternStringToMap.convertStringToMap(graphtopology, ",", "->");
            System.out.println("Given graphtopology=>" + graphtopology);
            System.out.println("Given graphTopologyMaps=>" + graphTopologyMaps.size()
                    + " - " + graphTopologyMaps);
            TreeMap<Integer, String> mapSourceVertex = graphTopologyMaps.get(1);
            TreeMap<Integer, String> mapDestVertex = graphTopologyMaps.get(2);
            TreeMap<Integer, String> mapDistinct_NewVertexID_OldVertexID = graphTopologyMaps.get(3);
            TreeMap<Integer, String> mapEdgeLabel = graphTopologyMaps.get(4);
            TreeMap<Integer, String> mapID = graphTopologyMaps.get(7);
            TreeMap<Integer, String> mapSTART = graphTopologyMaps.get(8);
            TreeMap<Integer, String> mapEND = graphTopologyMaps.get(9);
            TreeMap<Integer, String> mapOTHER = graphTopologyMaps.get(10);
            isSOPprint=Boolean.valueOf(mapConfig.get("isSOPprint"));

            // below two are tightly linked..
            if (graphTopologyMaps.containsKey(5))
                mapEdgeLabel_4_ChainNode = graphTopologyMaps.get(5);
            if (graphTopologyMaps.containsKey(6))
                map_dest2_4_ChainNode = graphTopologyMaps.get(6);

            int startLineNo = 1;
            int endLineNo = 100;
            baseFolder = "";
            // change input file here
            String inputDataFileToCreateGBADGraph = WSbaseFolder + "LekagulSensorData.csv";

            System.out.println("size:" + mapSourceVertex
                    + "\n#mapSourceVertex:" + mapSourceVertex
                    + "\n#mapDestVertex:" + mapDestVertex
                    + "\n#mapDistinct_NewVertexID_OldVertexID:" + mapDistinct_NewVertexID_OldVertexID
                    + "\n#mapEdgeLabel:" + mapEdgeLabel);
 
            try {
            	 
                FileWriter outWriter = new FileWriter(WSbaseFolder + outFile);
                String inputDataFileToCreateGBADGraph_OR_Folder=WSbaseFolder+mapConfig.get("inputDataFileToCreateGBADGraph_OR_Folder");
                String delimiter_4_inputDataFileToCreateGBADGraph=mapConfig.get("delimiter_4_inputDataFileToCreateGBADGraph");
                boolean is_header_present=Boolean.valueOf(mapConfig.get("is_header_present"));
                
				int ID__=-1;String START_edgeLabel_=""; String END_edgeLabel_="";String OTHER_edgeLabel_="";
				TreeMap<Integer,TreeMap<String, TreeMap<Integer, String>>> mapOUTPUT_ID_N_Source_EdgeLabel_Destination=new TreeMap<Integer, TreeMap<String,TreeMap<Integer,String>>>();
				
				TreeMap<Integer, TreeMap<Integer,TreeMap<String, TreeMap<Integer, String>>>> mapOUTPUT_lineNumber_ID_N_Source_EdgeLabel_Destination
																						=new TreeMap<Integer,TreeMap<Integer, TreeMap<String,TreeMap<Integer,String>>>>();
				
				// NOTE : THIS can process >=1  set of trees, if wanted more than 1, then MULTIPLE iteration on this method needed 
				for(int lineNumber:mapID.keySet()){
						//
						ID__=Integer.valueOf(mapID.get(lineNumber));
						START_edgeLabel_=mapSTART.get(lineNumber);
						END_edgeLabel_=mapEND.get(lineNumber);
						OTHER_edgeLabel_=mapOTHER.get(lineNumber);
						int lineNumber_fromSourceEdgeDestMappings=lineNumber;
					
			        // ***CREATE intermediate*** when ID is given processing is required to create an intermediate file
					mapOUTPUT_ID_N_Source_EdgeLabel_Destination   =					Crawler.create_intermediate_file(
																											lineNumber_fromSourceEdgeDestMappings,
														                                                    inputDataFileToCreateGBADGraph_OR_Folder,
														                                                    WSbaseFolder+"intermediate.txt",
														                                                    mapSourceVertex,
														                                                    mapDestVertex,
														                                                    mapEdgeLabel,
														                                                    mapEdgeLabel_4_ChainNode,
														                                                    map_dest2_4_ChainNode,
														                                                    ID__, //primary key
														                                                    START_edgeLabel_,
														                                                    END_edgeLabel_,
														                                                    OTHER_edgeLabel_,
														                                                    delimiter_4_inputDataFileToCreateGBADGraph,
														                                                    is_header_present,
														                                                    isSOPprint
														                                            	    );
					
					// accumulate results of each of tree (i.e., chain of nodes w.r.t ID)
					mapOUTPUT_lineNumber_ID_N_Source_EdgeLabel_Destination.put(lineNumber, mapOUTPUT_ID_N_Source_EdgeLabel_Destination);
				
				}
                
                // create GBAD input graph file
                createCSVtoGBADgraph(baseFolder,
                        mapSourceVertex,
                        mapDestVertex,
                        mapEdgeLabel,
                        mapDistinct_NewVertexID_OldVertexID,
                        mapEdgeLabel_4_ChainNode,
                        map_dest2_4_ChainNode,
                        mapID,
                        mapSTART,
                        mapEND,
                        mapOTHER,
                        mapOUTPUT_lineNumber_ID_N_Source_EdgeLabel_Destination,
                        "", //graphtopology_STRING_for_DEBUG
                        inputDataFileToCreateGBADGraph,
                        "!!!", //delimiter_4_inputDataFileToCreateGBADGraph
                        outWriter,
                        WSbaseFolder + outFile, //OUTFILE
                        startLineNo,
                        endLineNo,
                        true, //is_create_each_document_as_an_XP_in_graph
                        true,// is_header_present,
                        false, // isSOPprint
                        true, //is_skip_d3js_fileCreation
                        false //isDebugMore
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }//end of mainType==creategbadgraph
        /////#######//////////////#######//////////////#######//////////////#######//////////////#######///////

        /////#######//////////////#######//////////////#######//////////////#######//////////////#######///////


        // createHTMLtoGBADgraph //
        // 2 steps:: Step 1: extract news info from html  ;
        //			 Step 2: extract title, author names, datetime etc.
        //			 Step 3: OpenNLP to convert to graph
        //Goal: Iterate each doc (each line) of input file, and build one graph (NOTE: each input file= one graph)
        if (mainType.equalsIgnoreCase("createHTMLtoGBADgraph")) {
            // call
            int c = 0;
            System.out.println(" Usage from Command Line: java -jar crawler.jar getcrawler \n" +
                    " \"conf.txt//input base folder\" \n" +
                    " \n");
            System.out.println("Number of input params:" + args.length);
            // List out the input arguments
            while (c < args.length) {
                System.out.println("input args[" + c + "]=" + args[c]);
                c++;
            }
            String prefix = "D:\\share\\lenin\\";
            String inputFile = prefix + "GetEditDistance1.txt";
            String outputFile = prefix + "GetEditDistance1_out.txt";
            // cleanTitle(inputFile,outputFile,"|||");
            String debugFile = "//Users//guest2//Downloads//outDebug.txt";
            String folder = "//Users//guest2//Downloads//uaw.test.input//";
            String confFile = "";
            String macUserName = "lenin";
            folder = "//Users//" + macUserName + "//Downloads//uaw.test.input//";

            String outAlreadyCrawledURLsINaFile = "//Users//" + macUserName + "//Downloads//uaw.test.input//outDebugAlreadyCrawledURLsINaFile.txt";

            // dummy no input parameters..
            if (args.length == 0) {
                // pre-defined config file
                confFile = "/Users/" + "lenin" + "/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/config.txt";
            }
            //System.out.println("mapCrawled->" + mapAlreadyCrawledURL);
            // Run from command prompt
            if (args.length >= 2) {
                confFile = args[1]; //second argument from command line
            }
            System.out.println("Given Config File: " + confFile);
            TreeMap<String, String> mapConfig = Crawler.getConfig(confFile);
            //crawler t2 = new crawler();
            //t2.mapConfig = mapConfig;
            folder = mapConfig.get("folder"); // base folder
            String inputSingleURL = mapConfig.get("inputSingleURL");
            outputFile = mapConfig.get("outputFile");
            boolean isAppend = Boolean.valueOf(mapConfig.get("isAppend"));
            String delimiter = mapConfig.get("delimiter"); // "!!!"
            String inputType = mapConfig.get("inputType");  //{"file",""}  input is a file type (or) single url
            String outputType = mapConfig.get("outputType");// {"writeCrawledText2OutFile","mongodb","outNewUnCrawledURLFile"}

            outAlreadyCrawledURLsINaFile = mapConfig.get("outAlreadyCrawledURLsINaFile"); //  "outAlreadyCrawledURLsINaFile.txt";
            String inputListOf_URLfileCSV = mapConfig.get("inputListOf_URLfileCSV");

            int fromLine = Integer.valueOf(mapConfig.get("fromLine"));
            int toLine = Integer.valueOf(mapConfig.get("toLine"));
            String outdebugErrorAndURLfile = mapConfig.get("outdebugErrorAndURLfile");
            String outNewUnCrawledURLFile = mapConfig.get("outNewUnCrawledURLFile");
            String parseType = mapConfig.get("parseType");
            System.out.println("folder:" + folder + "\ninputListOf_URLfileCSV:" + inputListOf_URLfileCSV +
                    "\ninputSingleURL:" + inputSingleURL + "\noutputFile" + outputFile + "\nisAppend" + isAppend +
                    "\ndelimiter:" + delimiter + "\ninputType:" + inputType + "\noutputType:" + outputType +
                    "\noutAlreadyCrawledURLsINaFile:" + outAlreadyCrawledURLsINaFile +
                    "\ninputURLfileListCSV:" + inputListOf_URLfileCSV + "\nfromLine:" + fromLine +
                    "\ntoLine:" + toLine + "\noutdebugErrorAndURLfile:" + outdebugErrorAndURLfile +
                    "\noutNewUnCrawledURLFile:" + outNewUnCrawledURLFile +
                    "\nparseType:" + parseType);
            //  prompt the user to enter their name
            System.out.print("Enter Y (or) N to continue..");
            //  open up standard input
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String option = null;

            //  read the username from the command-line; need to use try/catch with the
            //  readLine() method
            try {
                option = br.readLine();
            } catch (IOException ioe) {
                System.out.println("IO error trying to read your name!");
                System.exit(1);
            }
            System.out.println("Given Option->" + option);
            //
            if (option.toLowerCase().equalsIgnoreCase("y")) {
                inputFile = "//Users//" + macUserName + "//Google Drive//uaw.test.input//CrawledOutHTML1.txt";

                // This much be the output file (format: <email.file.name!!!URL!!!crawled text>)
                TreeMap<String, String> mapAlreadyCrawledURL =
                        Crawler.LoadNthTokenFromWithORWithoutGivenPatternFromGivenFileListandLoadToMap(
                                inputFile, 2, "!!!", "http", "www", "http://", "www.", false //isSOPprint
                        );

                inputFile = "/Users/lenin/Google Drive/uaw.test.input/CrawledOutHTML.txt";
                outputFile = "/Users/lenin/Google Drive/uaw.test.input/CrawledOutHTMLCleaned.txt";
                debugFile = "/Users/lenin/Google Drive/uaw.test.input/CrawledOutHTMLDebug.txt";
                //clean
                Crawler.getCleanedCrawledFiles(4, "!!!",
                        inputFile, outputFile, debugFile);

                // run the main crawler function
                //getCrawler( folder,
                //	inputListOf_URLfileCSV, // input file <email.name!!!URL!!!header text from email>   <- at least two column
                //	inputSingleURL,
                //	outputFile, isAppend, delimiter,
                //	inputType, //{"file",""}  input is a file type (or) single url
                //	outputType, // {"outfile","mongodb",""}
                //	outAlreadyCrawledURLsINaFile,
                //	mapAlreadyCrawledURL,
                //	fromLine,
                //	toLine,
                //	outNewUnCrawledURLFile, //for {outputType="outNewUnCrawledURLFile"}
                //	outdebugErrorAndURLfile,
                //	parseType);

            } else {
                System.out.println("Terminated...");
            }

            // read Nth Token
            //readNthTokenFromWithORWithoutGivenPatternForGivenFileandRemoveDuplicationWrite2File(
            //"//Users//lenin//Downloads//uaw.test.input//outURL.prev.txt", 2, "!!!",
            //"http", "www", "http://", "www.","//Users//lenin//Downloads//uaw.test.input//outURL.prev.NoDup.txt",
            //"//Users//lenin//Downloads//uaw.test.input//outURLDebug.prev.DupliateDebug.txt");

            outAlreadyCrawledURLsINaFile = "//Users//lenin//Google Drive//uaw.test.input//outDebugAlreadyCrawledURLsINaFile.txt";
            // getCrawler(folder, folder+"//09//outURL.txt","",
            // folder+"//09//outParsedHTML.txt" ,"file");

        } //END if(mainType.equalsIgnoreCase("createHTMLtoGBADgraph"))

        /////#######//////////////#######//////////////#######//////////////#######//////////////#######///////

        ///////#######//////////////#######//////////////#######//////////////#######//////////////#######///////
        // createXMLtoGBADgraph
        if (mainType.equalsIgnoreCase("createXMLtoGBADgraph")) {

        } //END if(mainType.equalsIgnoreCase("createXMLtoGBADgraph"))
        /////////#######//////////////#######//////////////#######//////////////#######//////////////#######///////


        ///////#######//////////////#######//////////////#######//////////////#######//////////////#######///////

        System.out.print("mainType:" + mainType);
        System.out.println("Time Taken (FINAL ENDED):"
                + NANOSECONDS.toSeconds(System.nanoTime() - t0)
                + " seconds; "
                + (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
                + " minutes");

        /////#######//////////////#######//////////////#######//////////////#######//////////////#######///////

        /////#######//////////////#######//////////////#######//////////////#######//////////////#######///////
        t0 = System.nanoTime();
        //createGraphFromTextUsingNLP - build graph using NLP
        if (mainType.equalsIgnoreCase("createGraphFromTextUsingNLP")) {
            System.out.println("createGraphFromTextUsingNLP");
            String config = "[N]->[V]";
            String baseWorkFolder = "/Users/lenin/Google Drive/uaw.test.input/";
            String inCrawledFile = "CrawledOutHTML1.txt";
            String outCrawledFile = "dummy.txt";
            String inFlag = "file";
            String outFlag = "";
            //(NOT TESTED, WIP)
            // read crawled File and Tag Sentences using NLP
            readCrawledFilesANDTagSentencesUsingNLP(
                    baseWorkFolder,
                    inCrawledFile,
                    outCrawledFile,
                    inFlag,
                    outFlag,
                    "mongodb.societycare",
                    "",
                    true);


        } // end of createGraphFromTextUsingNLP

        System.out.println("Time Taken (FINAL ENDED):"
                + NANOSECONDS.toSeconds(System.nanoTime() - t0)
                + " seconds; "
                + (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
                + " minutes");

        /////#######//////////////#######//////////////#######//////////////#######//////////////#######///////
        String confFile = "";

        // Run from command prompt
        if (args.length >= 1) {
            confFile = args[0]; // second argument from command line
            System.out.println("\n Got config File from command prompt:" + confFile);
        } else if (args.length == 0) {
            confFile = "/Users/lenin/Dropbox/workspace-sts-3.4.0.RELEASE.macmini/crawler/conf/p.18/config.txt";
        }
        //CONFIGURATION
        TreeMap<String, String> mapConfig = new TreeMap<String, String>();
        t0 = System.nanoTime();
        TreeMap<String, Integer> map_SourceFeature_idx_mapping = new TreeMap<String, Integer>();
        //  createGBADgraphFromTextUsingNLP
        if (mainType.equalsIgnoreCase("createGBADgraphFromTextUsingNLP")) {
            mapConfig = Crawler.getConfig(confFile);

            // ***********BELOW SETTING has been moved to config file ************/			// call wrapper
            // example <_v,1> ->meaning verb available in map counter <<lineNo>:1>
//			map_SourceFeature_idx_mapping.put("$_n", 100);//noun available in ID=<<lineNo>:100>
//			map_SourceFeature_idx_mapping.put("$_v", 101);//noun available in <<lineNo>:101>
//			map_SourceFeature_idx_mapping.put("fn.sentiment", 102); //example:"fn.sentiment", 102. input to function of sentiment available in <<lineNo>:102>
//			map_SourceFeature_idx_mapping.put("$_n2", 103); //noun2
//			map_SourceFeature_idx_mapping.put("$_number", 104);
//			map_SourceFeature_idx_mapping.put("$_person", 105);
//			map_SourceFeature_idx_mapping.put("$_organization", 106);
//			map_SourceFeature_idx_mapping.put("$_location", 107);
//			map_SourceFeature_idx_mapping.put("$body",  2); //second column in file has document "bodyText"
//			map_SourceFeature_idx_mapping.put("$yyyy",  4);

            // below header of file should match this
//			map_SourceFeature_idx_mapping.put("$snip",  2); //second column in file has document "bodyText"
//			map_SourceFeature_idx_mapping.put("$lpara", 3);
//			map_SourceFeature_idx_mapping.put("$sect",  4); //primary key (first column in the file) <-header of file should match this
//			map_SourceFeature_idx_mapping.put("$keyw",  6);
//			map_SourceFeature_idx_mapping.put("$organiz", 7);
//			map_SourceFeature_idx_mapping.put("$original", 8);

            for (String config_feature_name : mapConfig.keySet()) {
                //
                if ((config_feature_name.indexOf("$") >= 0 || config_feature_name.indexOf("fn.") >= 0)
                        && config_feature_name.indexOf(".remark") == -1) {
                    System.out.println(config_feature_name + " " + mapConfig.get(config_feature_name));
                    map_SourceFeature_idx_mapping.put(config_feature_name, Integer.valueOf(mapConfig.get(config_feature_name)));
                }
            }
            baseFolder = mapConfig.get("baseFolder");
            //	if (is_R)
            // 	Wrapper to create GBAD file
            wrapper_for_mainType_createGBADgraphFromTextUsingNLP(baseFolder,
                    mapConfig,
                    map_SourceFeature_idx_mapping
            );

            System.out.println("Time Taken (FINAL ENDED):"
                    + NANOSECONDS.toSeconds(System.nanoTime() - t0)
                    + " seconds; "
                    + (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
                    + " minutes");

        }


        /////#######//////////////#######//////////////#######//////////////#######//////////////#######///////

        //either give folder (or) a file.
        String inputFileOfNYT = "/Users/lenin/OneDrive/Data.temp/NYT-Apr-May/NYT_out_crawled.SET.2.OnlyMay.txt";
        String inputFolder_for_FilesOfNYT = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/";
        inputFolder_for_FilesOfNYT = "/Users/lenin/OneDrive/Data.temp/dummy/";
        String outputFolder = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/output/";
        String outputFolder_for_Staging_split_files = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/StagingSplitFiles/";
        String pastAlreadyProcessedFile = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/dummyAlready.txt";
        inputFolder_for_FilesOfNYT = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/All.Merged/";
        inputFolder_for_FilesOfNYT = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/output.range.file/";
        inputFolder_for_FilesOfNYT = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/dummy.nytimes/";
        String outputFolder_for_FilesOfNYT = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/StagingSplitFiles_output/";
        outputFolder_for_FilesOfNYT = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/StagingSplitFiles_gbad_output.range.file/";
        outputFolder_for_FilesOfNYT = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/output/";

        String debugFile = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/debug.txt";
        boolean boolean_isrunningSplitFiles = false;

        //int startline=0; int endLine=1000000000;
        t0 = System.nanoTime();

        File f = new File(inputFolder_for_FilesOfNYT);
        String[] filesOfgivenFolder = f.list();
        System.out.println("given files array:" + filesOfgivenFolder);
        int cnt = 0;
        FileWriter writerAlreadyProcessedFile = null;

        // load past already run file
        TreeMap<String, String> mapPastAlreadyProcessedFile = new TreeMap();

        try {

            // convertNYTnewsARTICLEtoGBADgraph -> NYT news article to GBAD graph
            // Flag="SplitFilesForSlidingWindow" -> split into 30 days.
            // Flag="CreateGBADFile" -> Created GBAD file
            if (mainType.equalsIgnoreCase("convertNYTnewsARTICLEtoGBADgraph")) {
                if (!new File(pastAlreadyProcessedFile).exists()) {
                    try {
                        new File(pastAlreadyProcessedFile).createNewFile();
                        writerAlreadyProcessedFile = new FileWriter(new File(pastAlreadyProcessedFile));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    try {
                        writerAlreadyProcessedFile = new FileWriter(new File(pastAlreadyProcessedFile), true); //append
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                //
                mapPastAlreadyProcessedFile = ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
                        readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
                                pastAlreadyProcessedFile,
                                -1, //startline,
                                -1, //endline,
                                "",//outFile,
                                false, //is_Append_outFile,
                                false, //is_Write_To_OutputFile
                                " already load",// debug_label
                                -1,  //  token_to_be_used_as_primarykey
                                false // isSOPprint
                        );


                FileWriter writerDebug200 = new FileWriter(new File(debugFile), true);

                TreeMap<String, String> mapSS_uniqueKeywords_from_other_sources = new TreeMap();
                TreeMap<String, TreeMap<String, String>> mapSplitMap_By_FirstLetter = new TreeMap();
                TreeMap<Integer, TreeMap<Integer, String>> mapOut = new TreeMap();
                //
                mapOut = splitting_Convert_TimeSlice_OneBigFile_To_30DaysEachFile("",
                        "2014"
                        , "2014"
                        , 30 //number_of_elements_in_each_set
                        , writerDebug200
                );
                TreeMap<String, String> MAPuniqueYYYYMMDD = new TreeMap();
                //
                for (int i : mapOut.keySet()) {
                    System.out.println("mapOut.size:" + mapOut.size() + ";i:" + i + ";mapOut:" + mapOut);
                    writerDebug200.append(i + "<->" + mapOut.get(i) + "\n");
                    writerDebug200.flush();
                    for (Integer j : mapOut.get(i).keySet()) {
                        MAPuniqueYYYYMMDD.put(mapOut.get(i).get(j).replace("-", ""),
                                mapOut.get(i).get(j).replace("-", "")
                        );
                    }
                }

                if (boolean_isrunningSplitFiles) {
                    //SplitGivenSingleFile_to_multiple_files_Each_file_corresponds_to_one_YYYYMMDD
                    SplitGivenSingleFile_to_multiple_files_Each_file_corresponds_to_one_YYYYMMDD(
                            inputFolder_for_FilesOfNYT + "Merged-All.txt",
                            outputFolder_for_Staging_split_files,
                            MAPuniqueYYYYMMDD
                    );
                }
                //
                while (cnt < filesOfgivenFolder.length) { // while start
                    if (filesOfgivenFolder[cnt].indexOf("Store") >= 0) {
                        cnt++;
                        continue;
                    }
                    System.out.println("##$$$$$$$$$$$$$$$$$$$#############");
                    //SplitFilesForSlidingWindow
                    if (Flag.equalsIgnoreCase("SplitFilesForSlidingWindow")) {

                        //each set of keys inside value of mapOut
                        for (int h : mapOut.keySet()) {//mapOut from splitting_Convert_TimeSlice_OneBigFile_To_30DaysEachFile
                            //System.out.println("set of permuted keys:"+h+" -> "+mapOut.get(h) +"\n");
                            writerDebug200.append("\n*** start of for loop:" + h + "~~~" + mapOut.get(h) + "\n");
                            writerDebug200.flush();
                            TreeMap<Integer, String> mapFirstLastKey = get_First_And_Last_Key_in_MAP(mapOut.get(h));
                            String temp_output_file_name = "nytimes_world_" + mapFirstLastKey.get(1) + "-" + mapFirstLastKey.get(2) + ".txt";
                            String temp_output_debug_file_name = "Debug-only" + temp_output_file_name;
                            //already process
                            if (!mapPastAlreadyProcessedFile.containsKey(temp_output_file_name)) {
                                System.out.println(" already processed : " + temp_output_file_name + ";mapOut.SIZE:" + mapOut.size());
                                cnt++;
                                //continue; //debug

                                System.out.println(" already processed : 2");
                                writerDebug200.append("BEFORE calling readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found\n");
                                writerDebug200.flush();
                                System.out.println(" already processed : 3");
                                // Flag="SplitFilesForSlidingWindow"
                                ReadFile_CompleteFile_to_a_Single_String.
                                        readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found_2
                                                ("", //inputFolder_for_FilesOfNYT+filesOfgivenFolder[cnt], //ONE BIG FILE IS NOT PASSED, PASSED FROM SPLIT STAGED FILE
                                                        outputFolder + temp_output_file_name,
                                                        outputFolder_for_Staging_split_files,
                                                        outputFolder + temp_output_debug_file_name, //debug file
                                                        true, //is_Append_outFile
                                                        mapOut.get(h),
                                                        true, //is_it_NYC_File_where_YYYYMMDD_is_the_key
                                                        mapPastAlreadyProcessedFile,
                                                        writerDebug200
                                                );
                                System.out.println(" already processed : 4");
                                writerDebug200.append("AFTER calling readFile_eachLine_Find_A_Pattern_given_as_keys_in_a_Map_write_to_another_file_if_atleast_oneKey_Found\n");
                                writerDebug200.flush();
                                //FileWriter writeDebug2=new FileWriter(outputFolder+"Debug-only"+temp_output_file_name, true);
                                writerDebug200.append("\n#############end of one loop . h of mapOut##############" + h
                                        + " of " + mapOut.get(h) + "\n");
                                writerDebug200.flush();
                                //writeDebug.close();

                                writerAlreadyProcessedFile.append(temp_output_file_name + "\n");
                                writerAlreadyProcessedFile.flush();

                                mapPastAlreadyProcessedFile.put(temp_output_file_name, "");


                            }// NOT PROCESSED BEFORE mapPastAlreadyProcessedFile

                        } // each element h of map
                    } //end of SplitFilesForSlidingWindow
                    //CreateGBADFile
                    if (Flag.equalsIgnoreCase("CreateGBADFile")) {
                        String inputFile_unique_keywords_from_other_sources
                                = "/Users/lenin/Downloads/outputUniqNames_only_keywords.txt";
                        String inputFolder_for_thehindu_source2 = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/dummy.thehindu/";
                        String inputFilefor_thehindu_source2 = "thehindu-040114-040214.txt";

                        String inputFolder_for_timesOfIndia_source3 = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/dummy.timesOfIndia/";
                        String inputFilefor_timesOfIndia_source3 = "timesofIndia-040114-040214.txt";

                        String inputFolder_for_NDTV_source4 = "/Users/lenin/OneDrive/Data.temp/NYT-Apr_May-WorkCopy/dummy.NDTV/";
                        String inputFilefor_NDTV_source4 = "ndtv-040114-040214.txt";

                        int token_having_title_for_keywords_inputFileName_for_thehindu = 2;
                        int token_having_title_for_keywords_inputFileName_for_timesOFindia = 1;
                        int token_having_title_for_keywords_inputFileName_for_NDTV = 1;

                        TreeMap<String, TreeMap<String, String>> ip_mapISS_MapOut = new TreeMap();

                        boolean is_use_input_mapISS_MapOut = false;
                        TreeMap<Integer, Integer> ip_mapISS_Orig_NewsID_For_MapOut = new TreeMap();
                        boolean is_generate_GBAD_output = false;
                        //NOT NULL
                        if (inputFile_unique_keywords_from_other_sources.length() > 1 &&
                                mapSS_uniqueKeywords_from_other_sources.size() == 0) {

                            mapSS_uniqueKeywords_from_other_sources =
                                    ReadFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile.
                                            readFile_load_Each_Line_of_Generic_File_To_Map_String_String_remove_dup_write_to_outputFile(
                                                    inputFile_unique_keywords_from_other_sources
                                                    , -1
                                                    , -1
                                                    , "" //outFile
                                                    , false //is_Append_outFile
                                                    , false //is_Write_To_OutputFile
                                                    , " key from other source" //debug_label
                                                    , -1  //  token_to_be_used_as_primarykey
                                                    , false // isSOPprint
                                            );
                        }
                        if (mapSplitMap_By_FirstLetter.size() == 0) {
                            // split by f letter
                            mapSplitMap_By_FirstLetter =
                                    Splitting_given_1_uniqueKeys_SplitMap_to_multiple_each_having_keys_Start_with_first_letter
                                            .splitting_given_1_uniqueKeys_SplitMap_to_multiple_each_having_keys_Start_with_first_letter
                                                    (
                                                            mapSS_uniqueKeywords_from_other_sources,
                                                            0 //flag
                                                    );
                        }

                        //
//					 extract_keywords_from_thehindu_url.
//					 extract_keywords_from_thehindu_url();

                        // convert NYT to GBAD graph
                        convertNYTnewsARTICLEtoGBADgraph(
                                inputFolder_for_FilesOfNYT
                                , "20140401-20140402.txt" // filesOfgivenFolder[cnt] // inputFileName_for_NYT
                                , inputFolder_for_thehindu_source2
                                , inputFilefor_thehindu_source2
                                , inputFolder_for_timesOfIndia_source3
                                , inputFilefor_timesOfIndia_source3
                                , inputFolder_for_NDTV_source4
                                , inputFilefor_NDTV_source4
                                , token_having_title_for_keywords_inputFileName_for_thehindu
                                , token_having_title_for_keywords_inputFileName_for_timesOFindia
                                , token_having_title_for_keywords_inputFileName_for_NDTV
                                , outputFolder_for_FilesOfNYT
                                , mapSS_uniqueKeywords_from_other_sources
                                , mapSplitMap_By_FirstLetter
                                , -1 //startline
                                , -1 //endLine
                                , ip_mapISS_MapOut
                                , ip_mapISS_Orig_NewsID_For_MapOut
                                , is_use_input_mapISS_MapOut
                                , true //is_generate_GBAD_output
                        );
                    }

                    System.out.println("last file:" + filesOfgivenFolder[cnt]);
                    cnt++;
                } //WHILE END (each file of given input folder)

                System.out.println("filesOfgivenFolder.len:" + filesOfgivenFolder.length);
            } //end if(mainType.equalsIgnoreCase("convertNYTnewsARTICLEtoGBADgraph")){

            System.out.println("Time Taken (FINAL ENDED):"
                    + NANOSECONDS.toSeconds(System.nanoTime() - t0)
                    + " seconds; "
                    + (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60
                    + " minutes");

            /////#######//////////////#######//////////////#######//////////////#######//////////////#######///////


        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("------------------------------------------------");
        System.out.println("mainType:" + mainType);
        System.out.println("graphtopology:" + graphtopology);
        System.out.println("Time Taken:" + NANOSECONDS.toSeconds(System.nanoTime() - t0) + " seconds; "
                + (NANOSECONDS.toSeconds(System.nanoTime() - t0)) / 60 + " minutes");


    } //end of main


}//END OF CLASS
 