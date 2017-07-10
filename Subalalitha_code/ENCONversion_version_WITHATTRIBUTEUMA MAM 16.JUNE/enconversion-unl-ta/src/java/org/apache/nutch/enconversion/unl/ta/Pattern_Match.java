package org.apache.nutch.enconversion.unl.ta;

import org.apache.nutch.analysis.unl.ta.Analyser;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.TreeSet;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author root
 */
public class Pattern_Match {

    Pattern_TagUNL patt = new Pattern_TagUNL();
    TreeSet ts;
    String result;
    String tword;
    String hword;
    String uword;
    String posword;
    String caseEnd;
    String unlReln;
    String getFirst;
    String getNext;
    String getValue;
    String result_Reln;
    String firstReln;
    String nextReln;
    String resultTag;
	String sentid;
	String docid;
    String getFirst1;
    String getNext1;
    String getValue1;
    String get1;
    String get2;
    String get3;
	String tam_word;
	String sen_id;
	String dc_id;

	String get_out;
	String get_res;

    int counter;
   // ArrayList<String> store_tags = new ArrayList<String>();
    Hashtable pattern = new Hashtable();
    Hashtable pattern_out = new Hashtable();

    ArrayList<String> store_tags;// = new ArrayList<String>();
    ArrayList<String> compl_tags;
    ArrayList<String> dst_tags;

    PrintStream pattern_print;
    PrintStream result_print;
    PrintStream Tag_print;
    PrintStream res_print;

    public void init() {
        result = "";

        tword = "";
        hword = "";
        uword = "";
        posword = "";
        caseEnd = "";
        unlReln = "";
	sentid = "";
	docid = "";

        getFirst = "";
        getNext = "";
        getValue = "";
        result_Reln = "";
        firstReln = "";
        nextReln = "";
        resultTag = "";
        get1 = "";
        get2 = "";
        get3 = "";
        getFirst1 = "";
        getNext1 = "";
        getValue1 = "";

	get_out = "";
	get_res = "";

	store_tags = new ArrayList<String>();
        compl_tags = new ArrayList<String>();
	dst_tags = new ArrayList<String>();
    }

    public String process_TagSet(String st) {
        init();
        int i = 0;
        counter = 0;
        //    int count = 1;
        //	String caseEnd = "";
        //      System.out.println("St:"+st);
        StringTokenizer strToken1 = new StringTokenizer(st, "@");
        int cntwrds = strToken1.countTokens();
        while (i < cntwrds) {
            counter++;
            String getExp = strToken1.nextToken();
            //    System.out.println("getExp:"+getExp);
            StringTokenizer strToken2 = new StringTokenizer(getExp, "+");
            tword = strToken2.nextToken();
            hword = strToken2.nextToken();
            uword = strToken2.nextToken();
            posword = strToken2.nextToken();
            caseEnd = strToken2.nextToken();
            unlReln = strToken2.nextToken();

            if (!unlReln.equals("None")) {
                pattern_Pairs();
            }
            i++;
        }
        return result;
    }

    public void writeintoFile() {
        try {
            pattern_print = new PrintStream(new FileOutputStream("./resource/unl/BWA_Prob/pattern.csv"));
            result_print = new PrintStream(new FileOutputStream("./resource/unl/BWA_Prob/TagResult.txt"));
            Tag_print = new PrintStream(new FileOutputStream("./resource/unl/BWA_Prob/Tags.txt"));
	    res_print = new PrintStream(new FileOutputStream("./resource/unl/BWA_Prob/Tags_result.txt"));
            pattern_print.println("Patterns$Total occurences");


        } catch (Exception e) {
        }


    }

    public void pattern_Process() {

        int count1 = 1;
        if (pattern.isEmpty()) {
            pattern.put(getValue, count1);
        } else if (pattern.containsKey(getValue)) {
            Object cnt = pattern.get(getValue);
            int cnt2 = (Integer) cnt;
            cnt2++;
            pattern.put(getValue, cnt2);
        } else {
            count1 = 1;
            pattern.put(getValue, count1);
        }
    }

    public void pattern_Pairs() {
        if (counter == 1) {
            getFirst = caseEnd + ":" + posword + ":" + uword;
            firstReln = unlReln;
            //    System.out.println("FirstReln:"+firstReln);
        } else if (counter > 1) {
            getNext = caseEnd + ":" + posword + ":" + uword;
            nextReln = unlReln;
            //   System.out.println("nextReln:"+nextReln);
            if (firstReln.contains(nextReln)) {
                result_Reln = nextReln;
                //     System.out.println("ResultReln:"+result_Reln);
                if (!getFirst.equals(getNext)) {
                    getValue = result_Reln +"\t"+"(" + getFirst + "," + getNext + ")";
                }
            }
            //      System.out.println("getValue:"+getFirst+"@"+getNext);
            pattern_Process();
        }
    }

    public void writeTags() {
        for (Object key : pattern.keySet()) {
            pattern_print.println(key.toString() + "$" + String.valueOf(pattern.get(key)));
        }
    }

    public StringBuffer fineTuneDocs(StringBuffer docbuff) {
        String getBuff = docbuff.toString();
        StringBuffer sBuff = new StringBuffer();
        if (getBuff.contains("+@")) {
            //       System.out.println("getbuff:"+getBuff);
            getBuff = getBuff.replace("+@", "+None@");
            getBuff = getBuff.replace("<.", "");
            getBuff = getBuff.replace("+src@", "+plc#src@");
            //   System.out.println("gbuff:"+getBuff);
            sBuff.append(getBuff);
        }
        //   System.out.println("sbuff:"+sBuff);
        return sBuff;
    }

    class Pattern_TagUNL {

        public void process_Docs() {
            String s = null;
            FileReader fr = null;
            String temp = null;
            ArrayList al = new ArrayList();
            String output = "";
            String str = "";
            String sent = "", recv = "", token = "", fn = "";
            int temp1 = 0, j = 0;
            //   Pattern_Match P_M = new Pattern_Match();
            try {
                BufferedReader br = new BufferedReader(
                        new FileReader("./resource/unl/tagDocswoR/Output/nonemptyfiles.txt"));
                while ((s = br.readLine()) != null) {
                    fn = "./resource/unl/tagDocswoR/Output/" + s;
			StringTokenizer strTok1 = new StringTokenizer(s,"/");
			String getfold = strTok1.nextToken();
			String getid = strTok1.nextToken(".");
			System.out.println("getid:"+getid);
                    // tag_file = s;
                   System.out.println("fn:" + fn);
                    StringBuffer docbuff = new StringBuffer();
                    StringBuffer docbuff1 = new StringBuffer();
                    fr = new FileReader(fn);

                    
                 //     System.out.println("fr:"+fr);
                    BufferedReader buff = new BufferedReader(new FileReader(fn));
                    while ((str = buff.readLine()) != null) {
                        docbuff.append(str);
                    //      System.out.println("docbuff:"+docbuff);
                        docbuff1 = fineTune(docbuff);
                    }

               //    System.out.println("docbuff1:"+docbuff1);
                    StringTokenizer sentToken1 = new StringTokenizer(docbuff1.toString().trim(), ".", false);
                    // l.loadTaggedData();
                    // j = 0;
                    while (sentToken1.hasMoreTokens()) {
                        sent = sentToken1.nextToken();
                        process_RelationTag(sent);
                    }
                    output = "";
                }
                // P_M.writeintoFile();
                //   P_M.writeTags();
                //     System.out.println("GET_CASE:");
                //     System.out.println("pattern:" + P_M.pattern);
                //  System.out.println("case_pos_Entry:" + cp.case_pos_Entry);
                //  System.out.println("trans_freq_Entry:" + cp.trans_freq_Entry);

            } catch (Exception e) {
            }
        }
    }

    public StringBuffer fineTune(StringBuffer docbuff) {
        String getBuff = docbuff.toString();
        StringBuffer sBuff = new StringBuffer();
      //  if (getBuff.contains("+@")) {
         //   System.out.println("getbuff:" + getBuff);
            getBuff = getBuff.replace("+@", "+???@");
            getBuff = getBuff.replace("<.", "");
            getBuff = getBuff.replace("<தொகு+collect+icl>action+Verb+None+???@", "<");
            getBuff = getBuff.replace("<தாவு+jump+icl>action+Verb+None+???@", "<");
            getBuff = getBuff.replace("தொகு+collect+icl>action+Verb+None+???@", "");
            //   System.out.println("gbuff:"+getBuff);
            sBuff.append(getBuff);
    //    } else {
     //       sBuff.append(getBuff);
      //  }
        //   System.out.println("sbuff:"+sBuff);
        return sBuff;
    }

    public String process_RelationTag(String st) {
        init();
        int i = 0;
        counter = 0;
        //    int count = 1;
        //	String caseEnd = "";
     //   System.out.println("St:" + st);
        if(!st.equals("<")){
        StringTokenizer strToken1 = new StringTokenizer(st, "@");
        int cntwrds = strToken1.countTokens();
        while (i < cntwrds) {
            counter++;
            String getExp = strToken1.nextToken();
            //    System.out.println("getExp:"+getExp);
            StringTokenizer strToken2 = new StringTokenizer(getExp, "+");
            tword = strToken2.nextToken();
            hword = strToken2.nextToken();
            uword = strToken2.nextToken();
            posword = strToken2.nextToken();
            caseEnd = strToken2.nextToken();
	    sentid = strToken2.nextToken();
	    docid = strToken2.nextToken();
            unlReln = strToken2.nextToken();

	    String get_tag = caseEnd + ":" + posword + ":" + uword;
            String com_tag = tword + ":" + hword + ":" + caseEnd + ":" + posword + ":" + uword;
	    String sen_tag = tword + ":"+ posword +":" + sentid + ":" + docid;
            store_tags.add(get_tag);
            compl_tags.add(com_tag);
	    dst_tags.add(sen_tag);

            //  if (!unlReln.equals("None")) {
            get_Pattern_Pairs();
         //   System.out.println("get3:"+get3);
            //  }
            i++;
        }
	if(store_tags.size()>1){
            pattern_Storage();
        }else{
            
        }
        }else{
            
        }
        return result;
    }

    public void get_Pattern_Pairs() {
        if (counter == 1) {
            getFirst1 = tword + ":" + hword + ":" + caseEnd + ":" + posword + ":" + uword;
            get1 = caseEnd + ":" + posword + ":" + uword;
         //   firstReln = unlReln;
      //      System.out.println("FirstReln:"+ getFirst1);
        } else if (counter > 1) {
            getNext1 = tword + ":" + hword + ":" + caseEnd + ":" + posword + ":" + uword;
            get2 = caseEnd + ":" + posword + ":" + uword;
            //   nextReln = unlReln;
        //     System.out.println("nextReln:"+getNext1);
          //  if (!get1.equals(get2)) {
                getValue1 = "(" + getNext1 + "," + getFirst1 + ")";
                get3 = "(" + get2 + "," + get1 + ")";
                Tag_print.println(get3);
                match_Pattern();
         //   }
        }
    }
   public void pattern_Storage() {
        
        for(int i = 0;i < store_tags.size();i++){
            getFirst1 = compl_tags.get(i).toString();
            get1 = store_tags.get(i).toString();
            for(int j = i+1;j<store_tags.size();j++){
                getNext1 = compl_tags.get(j).toString();
                get2 = store_tags.get(j).toString();
                if(!get2.equals(get1)){
                    get3 = "(" + get2 + "," + get1 + ")";
		 //   get_patterns();
                    Tag_print.println(get3);
                    getValue1 = "(" + getNext1 + "," + getFirst1 + ")";
		    
                }
            //   if(!getNext1.equals(getFirst1)){
                            
            //   }
           //     if( (!get3.startsWith("(None:Numbers:icl>number")) ){
                    
                    match_Pattern();
           //     }
            }
        }
      //  }
    }

		public void get_patterns(){
				ts=new TreeSet();
			if(get2.contains("Verb")){
			
			if(pattern_out.isEmpty()){
				pattern_out.put(get2,"1");
				if(ts.add(getFirst1)){
				get_out = getNext1+"--->"+getFirst1;}
			}else if(pattern_out.containsKey(get2)){
				if(ts.add(getFirst1)){
				get_res = get_out+","+getFirst1;
				}					
			}else{
				
				pattern_out.put(get2,"1");
				if(ts.add(getFirst1)){
				get_res = getNext1+"--->"+getFirst1;
				}
			}
			res_print.println(get_res);

			}
		}
	

    public void match_Pattern() {
    //    System.out.println("Inside Match Pattern");
        Enumeration e1 = pattern.keys();
        while (e1.hasMoreElements()) {
            String key1 = (String) e1.nextElement();
       //   System.out.println("key1:"+key1);
            String spt[] = key1.split("\t");
            String reln = spt[0].toString();
            String patn = spt[1].toString();
        //    System.out.println("reln:"+reln + patn);
	//	String patn = key1;
            if (patn.equals(get3)) {
        //     System.out.println("Inside IF");
                resultTag = reln +"\t"+ getValue1+"\t"+sentid+"\t"+docid;
		get_patterns();
		//resultTag = getValue1+ "\t"+sentid;
          //      System.out.println("RESULT:"+resultTag);
                result_print.println(resultTag);
		pattern_out.put(resultTag,"1");
            }/**else{
                resultTag = "???" + getValue1;
                result_print.println(resultTag);
            }*/
        }
    }
    public void load_Tagset(){
        String str = "";
        try{
             BufferedReader br = new BufferedReader(
                    new FileReader("./resource/unl/BWA_Prob/TagSet.txt"));
             while((str = br.readLine())!=null){
	//	System.out.println("The str from file is"+str);
                 pattern.put(str, 1);
             }
        }catch(Exception e){

        }
//	System.out.println("PATTERN"+pattern);
    }
    	

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String s = null;
        FileReader fr = null;
        String temp = null;
        ArrayList al = new ArrayList();
        String output = "";
        String str = "";
        String sent = "", recv = "", token = "", fn = "";
        int temp1 = 0, j = 0;
        Pattern_Match P_M = new Pattern_Match();
      try {
   //         BufferedReader br = new BufferedReader(
    //                new FileReader("./resource/unl/tagDocs/Output/nonemptyfiles.txt"));
        /**      while ((s = br.readLine()) != null) {
                fn = "./resource/unl/tagDocs/Output/" + s;
                // tag_file = s;
                //    System.out.println("fn:" + fn);
                StringBuffer docbuff = new StringBuffer();
                StringBuffer docbuff1 = new StringBuffer();
                fr = new FileReader(fn);

                //	 System.out.println("fr:"+fr);
                BufferedReader buff = new BufferedReader(new FileReader(fn));
                while ((str = buff.readLine()) != null) {
                    docbuff.append(str);
                    docbuff1 = P_M.fineTuneDocs(docbuff);
                }

                // System.out.println("docbuff1:"+docbuff1);
                StringTokenizer sentToken1 = new StringTokenizer(docbuff1.toString().trim(), ".", false);
                // l.loadTaggedData();
                // j = 0;
                while (sentToken1.hasMoreTokens()) {
                    sent = sentToken1.nextToken();
                    P_M.process_TagSet(process_Docssent);
                }
                output = "";
            }   
            P_M.writeintoFile();
            P_M.writeTags();    */
            P_M.load_Tagset();
            P_M.writeintoFile();
            P_M.writeTags(); 
         //   System.out.println("pattern:" + P_M.pattern);
            P_M.patt.process_Docs();
            //     System.out.println("GET_CASE:");
            //     System.out.println("pattern:" + P_M.pattern);
            //  System.out.println("case_pos_Entry:" + cp.case_pos_Entry);
            //  System.out.println("trans_freq_Entry:" + cp.trans_freq_Entry);

        } catch (Exception e) {
        }
    }
}
