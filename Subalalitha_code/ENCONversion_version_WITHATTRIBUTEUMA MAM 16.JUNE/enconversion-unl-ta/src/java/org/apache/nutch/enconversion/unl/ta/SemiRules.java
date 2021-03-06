package org.apache.nutch.enconversion.unl.ta;

import org.apache.nutch.analysis.unl.ta.Analyser;

import java.lang.*;
import java.util.*;
import java.io.*;

public class SemiRules {

	MWDict mwdict;
	UWDict uwdict;

	BSTNode bn;
	BST dict;

	MWBST dict_mw;
	MWBSTNode bn_mw;

	String result;
	String relnWord;
	String verbWord;
        String u_word;
        String h_word;
        String rel_label;
        String tempword;
        String analysed;
        public static String tag_file = null;

	int j, total;

       	int aojindex;
	int plfindex;
	int insindex;
	int cobindex;
	int timindex;
	int cwindex;
	int verbcount;


	ArrayList<String> abbwords = new ArrayList<String>();
	ArrayList<String> expwords = new ArrayList<String>();

	ArrayList<String> root;
	ArrayList<String> rootnew;
	ArrayList<String> pos;
	ArrayList<String> caseEndings;
	ArrayList<String> uw;
	ArrayList<String> hw;
	ArrayList<String> anal;
	ArrayList<String> wordAL;
	ArrayList<String> unlRelation;
        ArrayList<String> rootrel;
	ArrayList<String> sent_store;
	ArrayList sent_id;
	ArrayList<String> doc_store;
	ArrayList no;

	public SemiRules() {
		try {
			dict = new BST();
			dict_mw = new MWBST();

			mwdict = new MWDict();
			mwdict.mwbst.inorder();

			uwdict = new UWDict();
			uwdict.bst.inorder();

			// ws = new WordSense();

			dict = uwdict.bst;
			dict_mw = mwdict.mwbst;

			bn_mw = new MWBSTNode();

			// centering = new Centering();
		} catch (Exception e) {

		}

	}

	public void init() {
		root = new ArrayList<String>();
		rootnew = new ArrayList<String>();
		pos = new ArrayList<String>();
		caseEndings = new ArrayList<String>();
		uw = new ArrayList<String>();
		hw = new ArrayList<String>();
		anal = new ArrayList<String>();
		wordAL = new ArrayList<String>();
		unlRelation = new ArrayList<String>();
                rootrel = new ArrayList<String>();
		sent_store= new ArrayList<String>();
		doc_store=new ArrayList<String>();
		sent_id=new ArrayList();
		no = new ArrayList<String>();
		
		relnWord = null;
		verbWord = null;
                u_word = null;
                rel_label = null;
                result = "";
              //  tempword = null;
              //  analysed = null;

                aojindex = 0;
                insindex = 0;
                plfindex = 0;
                timindex = 0;
                cobindex = 0;

	}

	public String enconvert(String st,int scnt,String doc_id) {
		try {
			init();
			sent_id.add(st);
			
			System.out.println("ST:"+st);
                        if(st.contains("•")){
                          st = st.replaceAll("•","");
                        }
			
			StringTokenizer strToken1 = new StringTokenizer(st, ", ");
			int noofwords = strToken1.countTokens();
			int ctnoofwords = 0;
			while (ctnoofwords < noofwords) {
				// System.out.println("Inside While");

				String word = strToken1.nextToken().trim();
				String pss = "";
				String word2 = "";
				String word1 = "";
				String prev = "", next = "";
				wordAL.add(word);

				String analysed = org.apache.nutch.analysis.unl.ta.Analyser
						.analyseF(word, true);
			//	System.out.println("analysed:" + analysed);
				StringTokenizer strToken2 = new StringTokenizer(analysed,
						":\n<>,=•°*;:?-'\"&", false);

					// System.out.println("Firstindex:"+firstindex);
			int firstindex = analysed.indexOf('<', analysed.indexOf('>'));
				String analmod = analysed.substring(firstindex);
				// System.out.println("analmod:"+analmod);
				anal.add(analmod);
				if ((analysed.indexOf("unknown") != -1)
						|| (analysed.indexOf("count=4") != -1)) {
					word1 = strToken2.nextToken().trim();
					word2 = word1;
				/**	if( (word1.length()>1) || (!(word1.contains("Unknown"))) ){
						word2 = word1;
					}else{
						//word1="";
					}*/
				//	pos.add("None");
				//	root.add(word2);
				//	System.out.println("word1:"+word1);
				} else if ((analysed.indexOf("Adjectival Noun") != -1)
						|| (analysed.indexOf("Noun") != -1)
						|| (analysed.indexOf("Interrogative") != -1)
						|| (analysed.indexOf("Non Tamil Noun") != -1)
						|| (analysed.indexOf("Entity") != -1)
						|| (analysed.indexOf("Adverb") != -1)
						|| (analysed.indexOf("Numbers") != -1)
						|| (analysed.indexOf("charNumbers") != -1)
						|| (analysed.indexOf("DateTime") != -1)
						|| (analysed.indexOf("Postposition") != -1)
						|| ((analysed.indexOf("Verb") != -1) && (analysed
								.indexOf("Finite Verb") == -1))
						|| (analysed.indexOf("Particle") != -1)
                                                || (analysed.indexOf("Demonstrative Adjective") != -1)
						|| (analysed.indexOf("Adjective") != -1)){
					prev = strToken2.nextToken().trim();
					word1 = prev;
					next = strToken2.nextToken().trim();
				}
				while (strToken2.hasMoreTokens()) {
					if ((!(next.equals("Adjectival Noun")))
							&& (!(next.equals("Noun")))
							&& (!(next.equals("Interrogative Adjective")))
							&& (!(next.equals("Interrogative Noun")))
							&& (!(next.equals("Non Tamil Noun")))
							&& (!(next.equals("Entity")))
							&& (!(next.equals("Adverb")))
							&& (!(next.equals("Numbers")))
							&& (!(next.equals("charNumbers")))
							&& (!(next.equals("DateTime")))
							&& (!(next.equals("Postposition")))
							&& (!(next.equals("Verb")))
							&& (!(next.equals("Finite Verb")))
							&& (!(next.equals("Adjective")))
							&& ((!(next.equals("Particle"))))) {
						prev = next;
						next = strToken2.nextToken().trim();
					} else {
						break;
					}
				}
				word2 = prev;
				pss = next;
				root.add(word2);
				pos.add(pss);
				// System.out.println("word2:"+word2+":"+pss);
				ctnoofwords++;
			} // end of while
		//	System.out.println("anal:"+anal.size());
			findUW();
			getCaseEndingsandRelations();			
		//	getRelationswithVerbs();
			getRelationbetweenConcepts();
	//		System.out.println("UNLRELATION:"+unlRelation);
			StringBuffer resultuw = new StringBuffer();
			for(int k = 0;k<unlRelation.size();k++){
				System.out.println(rootnew.get(k).toString()+"+"+hw.get(k).toString()+"+"+uw.get(k).toString()+"+"+pos.get(k).toString()+"+"+caseEndings.get(k).toString()+"+"+unlRelation.get(k).toString());
				if (!(rootnew.get(k).equals("None"))) {
				  	resultuw.append(rootnew.get(k).toString() + '+');
					resultuw.append(hw.get(k).toString() + '+');
					resultuw.append(uw.get(k).toString() + '+');
                    resultuw.append(pos.get(k).toString() + '+');
                    resultuw.append(caseEndings.get(k).toString() + '+');
                    resultuw.append(unlRelation.get(k).toString()+'@');
				}
			}	
			result += "<"; 
			result += resultuw; 
			result += ".\n";
			
		//	System.out.println("RESULT:"+result);
		} catch (Exception e) {

		}
		return result;
	}

	public void findUW() {

		int count = 1;
		int cnt = 0;
		// String pnentry=null;
		String rword = "";
		// ArrayList<String> te = new ArrayList<String>();
		// StringBuffer sb = new StringBuffer();
		// StringBuffer resultuw = new StringBuffer();
		int j = 0, total;
		// System.out.println("SIZE OF ROOT IN FINDUW:"+root.size());
		try {
			total = root.size();
			while (j < total) {

				bn = new BSTNode();

				rword = root.get(j).toString().trim();
			//	System.out.println("RWORD:"+rword+":"+j);
				int hc = rword.hashCode();

				// int cindex = tempcmtw.indexOf(rword);

				if ((bn = dict.search(hc)) != null) {
					hw.add(bn.headword);
					uw.add(bn.constraint);
					no.add(new Integer(count++));
					rootnew.add(rword);
				} else if (pos.get(j).equals("Numbers")) {

					hw.add(rword);
					uw.add("icl>number");
					no.add(new Integer(count++));
					rootnew.add(rword);
				}else {
                                        rootnew.add("None");
					hw.add("None");
					uw.add("None");
					no.add("None");
                                   //     pos.remove(j);
                                   //     pos.add(j, "None");
                                }
				j++;
			} // end of while
		//	System.out.println("root:" + root+":"+root.size());
          //              System.out.println("rootnew:" + rootnew+":"+rootnew.size());
		//	System.out.println("pos:" + pos+":"+pos.size());
         //               System.out.println("uw:" + uw+":"+uw.size());
		} catch (Exception e) {
		}

	}
/**	public void getRelationbetweenConcepts(){
		int j = pos.size();
		String concat = "";
		String relnWord = "";
		String posWord = "";
		String relnexist = "";
		String reln1 = "";
		int i = 0;
		while(i < j){			
			posWord = pos.get(i).toString();
			if(posWord.contains("Verb")){
				String verbReln = rootrel.get(i).toString();
				//		System.out.println("verbReln:"+verbReln);
						if(verbReln.contains("None")){
							rootrel.remove(i);
							rootrel.add(i, concat);
							concat = "";
						}else{
							relnexist = rootrel.get(i).toString();
							rootrel.remove(i);
							rootrel.add(i,"#"+relnexist+concat);
							relnexist ="";
							concat = "";
						}
			}else{
				relnWord = rootrel.get(i).toString();
				if( (relnWord.contains("plc")) || (relnWord.contains("plf")) || (relnWord.contains("ben")) || (relnWord.contains("obj")) || (relnWord.contains("plt")) ){
					concat+="#("+relnWord +")";
					if(relnWord.contains("plf")){
						for(int l = 0;l<rootrel.size();l++){
							String pltFlag = rootrel.get(l).toString();
							if(pltFlag.contains("plt")){
								String tempReln = pltFlag;
								rootrel.remove(l);
								rootrel.add(l, tempReln+"(#"+relnWord+")");
								break;
							}							
						}
					}
				}else if( (relnWord.equals("pos")) || (relnWord.equals("mod")) ){
					int k = i+1;
					reln1 = rootrel.get(k).toString();
					if(reln1.contains("None")){
						rootrel.remove(k);
						rootrel.add(k, relnWord);
					//	relnWord = "";
					}else{						
						rootrel.remove(k);
						rootrel.add(k,reln1+"#"+relnWord);
						reln1 ="";
					//	concat = "";
					}
				}
			}
			i++;
		}
	}   */

	public void getRelationbetweenConcepts(){
		int j = pos.size();
		String concat = "";
		String relnWord = "";
		String posWord = "";
		String relnexist = "";
		String reln1 = "";
		String reln2 = "";
		int i = 0;
		while(i < j){
			posWord = pos.get(i).toString();
			if(posWord.contains("Verb")){
				String verbReln = unlRelation.get(i).toString();
				//		System.out.println("verbReln:"+verbReln);
						if(verbReln.contains("None")){
							unlRelation.remove(i);
							unlRelation.add(i, concat);
							concat = "";
						}else{
							relnexist = unlRelation.get(i).toString();
							unlRelation.remove(i);
							unlRelation.add(i,"#"+relnexist+concat);
							relnexist ="";
							concat = "";
						}
			}else{
				relnWord = unlRelation.get(i).toString();
				if( (relnWord.contains("plc")) || (relnWord.contains("plf")) || (relnWord.contains("ben")) || (relnWord.contains("obj")) || (relnWord.contains("plt")) || (relnWord.contains("aoj")) || (relnWord.contains("cag")) || (relnWord.contains("dur")) || (relnWord.contains("ins")) || (relnWord.contains("man")) || (relnWord.contains("met")) || (relnWord.contains("opl")) || (relnWord.contains("scn")) || (relnWord.contains("src")) || (relnWord.contains("tim")) || (relnWord.contains("tmf")) || (relnWord.contains("tmt")) || (relnWord.contains("to")) || (relnWord.contains("frm")) || (relnWord.contains("fmt")) || (relnWord.contains("via"))  ){
					concat+="#("+relnWord +")";
					if(relnWord.contains("plf")){
						for(int l = 0;l<unlRelation.size();l++){
							String pltFlag = unlRelation.get(l).toString();
							if(pltFlag.contains("plt")){
								String tempReln = pltFlag;
								unlRelation.remove(l);
								unlRelation.add(l, tempReln+"(#"+relnWord+")");
								break;
							}
						}
					}
				}else if( (relnWord.equals("pos")) || (relnWord.equals("mod")) ){
					int k = i+1;
					reln1 = unlRelation.get(k).toString();
					if(reln1.contains("None")){
						unlRelation.remove(k);
						unlRelation.add(k, relnWord);
					//	relnWord = "";
					}else{
						unlRelation.remove(k);
                        unlRelation.add(k,reln1+"#"+relnWord);
						reln1 ="";
					//	concat = "";
					}
				}else if( (relnWord.equals("and")) || (relnWord.contains("bas")) ){
					int k1 = i-1;
					int k2 = i+1;
					reln1 = unlRelation.get(k1).toString();
					reln2 = unlRelation.get(k2).toString();
					if(reln1.contains("None")){
						unlRelation.remove(k1);
						unlRelation.add(k1, relnWord);
					//	relnWord = "";
					}else{
						unlRelation.remove(k1);
                        unlRelation.add(k1,reln1+"#"+relnWord);
						reln1 ="";
					//	concat = "";
					}if(reln2.contains("None")){
						unlRelation.remove(k2);
						unlRelation.add(k2, relnWord);
					//	relnWord = "";
					}else{
						unlRelation.remove(k2);
                        unlRelation.add(k2,reln1+"#"+relnWord);
						reln2 ="";
					//	concat = "";
					}
				}
			}
			i++;
		}
	}


/**	public void getCaseEndingsandRelations() {
		j = 0;

		String analysed;
		String tempword;
		String u_word;
		// System.out.println("Anal inside Findrelnindex:"+anal);

	//	String rel_label;
		String cEndWord = " ";
		String relnWord = " ";

		total = anal.size();
		while (j < total) {
			try {
				// ////////System.out.println("j:"+j);
				// rootrel.add("None");
				analysed = anal.get(j).toString().trim();
				tempword = root.get(j).toString().trim();
				u_word = uw.get(j).toString().trim();
				if (analysed.indexOf("இல்") != -1) {
					if ((analysed.indexOf("இருந்து") != -1)) {
						cEndWord = "இல்#இருந்து";
						relnWord = "plf#frm#tmf#scn";
						caseEndings.add(cEndWord);
						unlRelation.add(relnWord);
					} else if (analysed.indexOf("உள்ள") != -1) {
						cEndWord = "இல்#உள்ள";
						relnWord = "mod";
						caseEndings.add(cEndWord);
						unlRelation.add(relnWord);
					} else if (analysed.indexOf("உம்") != -1) {
						cEndWord = "இல்#உம்";
						relnWord = "and";
						caseEndings.add(cEndWord);
						unlRelation.add(relnWord);
					} else {
						cEndWord = "இல்";
						relnWord = "plc";
						caseEndings.add(cEndWord);
						unlRelation.add(relnWord);
					}
				} else if (analysed.indexOf("உம்") != -1) {
					cEndWord = "உம்";
					relnWord = "and";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((analysed.indexOf("படு") != -1)
						&& (analysed.indexOf("அது") != -1)) {
					cEndWord = "படு#அது";
					relnWord = "agt";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if (analysed.indexOf("ஆல்") != -1) {
					cEndWord = "ஆல்";
					relnWord = "ins#con";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if (analysed.indexOf("உக்கு") != -1) {
					if (analysed.indexOf("ஆக") != -1) {
						cEndWord = "உக்கு#ஆக";
						relnWord = "ben";
						caseEndings.add(cEndWord);
						unlRelation.add(relnWord);
					}
				} else if ((analysed.indexOf("உடன்") != -1)
						|| (analysed.indexOf("ஓடு") != -1)) {
					cEndWord = "உடன்#ஓடு";
					relnWord = "cag#cob#coo";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if (analysed.indexOf("க்கு") != -1) {
					if (analysed.indexOf("உரிய") != -1) {
						cEndWord = "க்கு#உரிய";
						relnWord = "pos";
						caseEndings.add(cEndWord);
						unlRelation.add(relnWord);
					} else if (analysed.indexOf("உட்பட்ட") != -1) {
						cEndWord = "க்கு#உட்பட்ட";
						relnWord = "icl";
						caseEndings.add(cEndWord);
						unlRelation.add(relnWord);
					} else if (analysed.indexOf("உம்") != -1) {
						cEndWord = "க்கு#உம்";
						relnWord = "and";
						caseEndings.add(cEndWord);
						unlRelation.add(relnWord);
					} else {
						cEndWord = "க்கு";
						relnWord = "to#plt#tmt";
						caseEndings.add(cEndWord);
						unlRelation.add(relnWord);
					}
				} else if (analysed.indexOf("ஐ") != -1) {
					cEndWord = "ஐ";
					relnWord = "obj";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if (analysed.indexOf("இன்") != -1) {
					cEndWord = "இன்";
					relnWord = "pos";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((analysed.indexOf("உடைய") != -1)
						|| (analysed.indexOf("அது") != -1)) {
					cEndWord = "உடைய#அது";
					relnWord = "pos";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((analysed.indexOf("ஆக") != -1)
						|| (analysed.indexOf("க்க") != -1)) {
					cEndWord = "ஆக#க்க";
					relnWord = "man****";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((tempword.equals("உள்ளது"))
						|| (tempword.equals("இருக்கும்"))
						|| (tempword.equals("உள்ளன"))
						|| (tempword.equals("நிறைந்தது"))
						|| (tempword.equals("ஆகும்"))
						|| (tempword.equals("உண்டு"))
						|| (tempword.equals("பற்றி"))
						|| (tempword.equals("பற்றிய"))
						|| (analysed.indexOf("உள்ளது") != -1)
						|| (analysed.indexOf("இருக்கும்") != -1)
						|| (analysed.indexOf("உள்ளன") != -1)
						|| (analysed.indexOf("நிறைந்தது") != -1)
						|| (analysed.indexOf("ஆகும்") != -1)
						|| (analysed.indexOf("உண்டு") != -1)) {
					cEndWord = tempword;
					relnWord = "aoj";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((analysed.indexOf("என") != -1)
						|| (tempword.equals("என"))
						|| (tempword.equals("என்று"))) {
					cEndWord = tempword;
					relnWord = "icl";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((tempword.equals("மட்டுமல்லாமல்"))
						|| (tempword.equals("மற்றும்"))
						|| (tempword.equals("மேலும்"))) {
					cEndWord = tempword;
					relnWord = "and";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
					unlRelation.add(j-1,relnWord);
					unlRelation.add(j+1,relnWord);
				} else if (tempword.equals("வழியாக")) {
					cEndWord = tempword;
					relnWord = "via";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((tempword.equals("காட்டிலும்"))
						|| (tempword.equals("விட"))) {
					cEndWord = tempword;
					relnWord = "bas";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
					unlRelation.add(j-1, relnWord);
					unlRelation.add(j+1, relnWord);
				} else if (tempword.equals("என்னும்")) {
					cEndWord = tempword;
					relnWord = "equ";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((tempword.equals("முதல்"))
						|| (tempword.equals("இருந்து"))) {
					cEndWord = tempword;
					relnWord = "frm#plf#tmf";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
					unlRelation.add(j-1, relnWord);
				} else if ((tempword.equals("மூலம்"))
						|| (tempword.equals("வைத்து"))
						|| (tempword.equals("உபயோகி"))
						|| (tempword.equals("கொண்டு"))) {
					cEndWord = tempword;
					relnWord = "ins";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if (tempword.equals("மிக")) {
					cEndWord = tempword;
					relnWord = "man";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((tempword.equals("ஆகிய"))
						|| (tempword.equals("போன்ற"))
						|| (tempword.equals("முதலிய"))) {
					cEndWord = tempword;
					relnWord = "nam";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if (tempword.equals("அதனால்")) {
					cEndWord = tempword;
					relnWord = "rsn";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((tempword.equals("அப்புறம்"))
						|| (tempword.equals("முன்னால்"))
						|| (tempword.equals("பின்னால்"))
						|| (tempword.equals("பின்னர்"))
						|| (tempword.equals("பின்பு"))) {
					cEndWord = tempword;
					relnWord = "seq";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((tempword.equals("ஆம்")) || (tempword.equals("இல்"))
						|| (tempword.equals("ம்")) || (tempword.equals("ல்"))) {
					cEndWord = tempword;
					relnWord = "tim";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((tempword.equals("அல்லாத"))
						|| (tempword.equals("நீங்கலாக"))) {
					cEndWord = tempword;
					relnWord = "neg";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if (tempword.equals("என்பது")) {
					cEndWord = tempword;
					relnWord = "cnt";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((tempword.equals("கொண்டே"))
						|| (tempword.equals("அதேவேளை"))) {
					cEndWord = tempword;
					relnWord = "man****";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((tempword.equals("பொழுது"))
						|| (tempword.equals("போது"))) {
					cEndWord = tempword;
					relnWord = "dur";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if (tempword.equals("வரை")) {
					cEndWord = tempword;
					relnWord = "to#plt#tmt";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
					unlRelation.add(j-1, relnWord);
				} else if (((analysed.indexOf("இங்கு") != -1) || (analysed
						.indexOf("அங்கு") != -1))) {
					cEndWord = "இங்கு";
					relnWord = "plc";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if (tempword.indexOf("பொது") != -1) {
					cEndWord = tempword;
					relnWord = "and****";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if (((analysed.indexOf("இங்கு") == -1))
						&& (analysed.indexOf("Adverb") != -1)) {
					cEndWord = "இங்கு#Adverb";
					relnWord = "plc";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((analysed.indexOf("Adjective") != -1)
						|| (analysed.indexOf("Adjectival Suffix") != -1)
						|| (analysed.indexOf("Adjectival Noun") != -1)) {
					cEndWord = "None";
					relnWord = "mod";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if (tempword.equals("அல்லது")) {
					cEndWord = tempword;
					relnWord = "or";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
					unlRelation.add(j-1,relnWord);
					unlRelation.add(j+1,relnWord);
				} else if (tempword.equals("முறை")) {
					cEndWord = tempword;
					relnWord = "and****";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if (tempword.equals("நிறைய")) {
					cEndWord = tempword;
					relnWord = "qua";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				}else if ((tempword.equals("நாடு")) || (tempword.equals("மாவட்டம்"))
						|| (tempword.equals("நகரம்"))
						|| (tempword.equals("மாநிலம்"))
						|| (tempword.equals("நதி")) || (tempword.equals("ஏரி"))
						|| (tempword.equals("பாலம்"))) {
					cEndWord = tempword;
					relnWord = "iof";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((tempword.equals("உடல்"))
						|| (tempword.equals("மரம்"))) {
					cEndWord = tempword;
					relnWord = "icl****";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((tempword.equals("உள்ளிட்ட"))
						|| (tempword.equals("உள்பட"))
						|| (tempword.equals("உட்பட"))) {
					cEndWord = tempword;
					relnWord = "icl";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if ((tempword.equals("இடையில்"))
						|| (tempword.equals("இடையே"))) {
					cEndWord = tempword;
					relnWord = "int";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if (tempword.equals("போதிலும்")) {
					cEndWord = "போதிலும்";
					relnWord = "and****";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				} else if (tempword.equals("உள்ள")) {
					cEndWord = "உள்ள";
					relnWord = "mod";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				}else if (u_word.contains("person")){
					cEndWord = "None";
					relnWord = "agt";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				}else if( (u_word.contains("place")) || (u_word.contains("country")) || (u_word.contains("region")) || (u_word.contains("continent")) || (u_word.contains("city")) ){
					cEndWord = "None";
					relnWord = "plc";
					caseEndings.add(cEndWord);
					unlRelation.add(relnWord);
				}
				else{
					caseEndings.add("None");
					unlRelation.add("None");
				}
			} catch (Exception e) {
			}
			j++;
		}
		j = 0;
	//	System.out.println("CaseEndings:" + caseEndings +":"+caseEndings.size());
	//	System.out.println("unlRelation:" + unlRelation+":"+unlRelation.size());
	} */


/** Various Rules are defined for UNL Relations.
 * Rule1 is defined for the UNL relations:
 *  frm,
 *  plf,
 *  src,
 *  tmf.
 *  Based on the UNL Constraints the relations will vary.
 *  These relations are obtained by applying the
 *  Rule: <"இல்", "ஆக", "இடம்">-> <"இருந்து">
 */
	public void Rule1()
	{
          //      System.out.println("INSIDE RULE1");

	//	rootrel.set(j, "frm");
	//	rel_label = "plf";
	//	//    findRelation_Category5(j + 1, j, rel_label);
		if ((j + 1) != total) {
			String anal2 = anal.get(j + 1).toString().trim();

			if (anal2.indexOf("Relative Participle") != -1) {
				u_word = uw.get(j).toString();
				if ((u_word.indexOf("place") != -1)
						|| (u_word.indexOf("city") != -1)
						|| (u_word.indexOf("district") != -1)
						|| (u_word.indexOf("organization") != -1)
						|| (u_word.indexOf("temple") != -1)
						|| (u_word.indexOf("facilities") != -1)
						|| (u_word.indexOf("country") != -1)
						|| (u_word.indexOf("lake") != -1)
						|| (u_word.indexOf("vehicle") != -1)
						|| (u_word.indexOf("body") != -1)) {
					rootrel.set(j, "frm");
                    			rel_label = "frm";
                                //        unlRelation.add(j,rel_label);
				}
			} else {
				plfindex = j;
			//	rootrel.set(j, "plf" + "src");
				u_word = uw.get(j).toString();
				if ((u_word.indexOf("place") != -1)
						|| (u_word.indexOf("city") != -1)
						|| (u_word.indexOf("district") != -1)
						|| (u_word.indexOf("organization") != -1)
						|| (u_word.indexOf("temple") != -1)
						|| (u_word.indexOf("facilities") != -1)
						|| (u_word.indexOf("country") != -1)
						|| (u_word.indexOf("lake") != -1)) {
					rel_label = "plf";
                                        rootrel.set(j, "plf");
                                  //      unlRelation.add(j,rel_label);
				} else if (u_word.indexOf("time") != -1) {
					rel_label = "tmf";
                                        rootrel.set(j, "tmf");
                                    //    unlRelation.add(j,rel_label);
				} else if (u_word.indexOf("event") != -1) {
					rel_label = "scn";
                                        rootrel.set(j, "scn");
                                      //  unlRelation.add(j,rel_label);
				} else {
					rel_label = "src";
                                        rootrel.set(j, "scn");
                                    //    unlRelation.add(j,rel_label);
				}
			//	//    findRelation_Category2(j, rel_label);
			}
		}
	}
	/**Rule2 is defined for the UNL Relation:
	 * 'and'.
	 * The 'and' relation is obtained by the following rules
	 * applied: "மட்டுமல்லாமல்", "மற்றும்", "மேலும்", "உம்".
	 *
	 */

	public void Rule2() // mattumallamal
	{
		// flag2=1;
		rootrel.set(j-1, "and");
                rootrel.set(j+1, "and");
		rel_label = "and";
              //  unlRelation.add(j,rel_label);
	//	//    findRelation_Category1(j, rel_label);
	}

	/** Rule3 is defined for the UNL Relation:
	 * ' via'
	 * The 'via'relation is obtained by applying the
	 * Rule: "வழியாக"
	 */

	public void Rule3() // Valiyaga
	{
	//	rootrel.set(j, "via");
	//	rel_label = "via";
	//	//    findRelation_Category4(j, rel_label);
		u_word = uw.get(j - 1).toString();
		if ((u_word.indexOf("place") != -1)
				|| (u_word.indexOf("city") != -1)
				|| (u_word.indexOf("district") != -1)
				|| (u_word.indexOf("organization") != -1)
				|| (u_word.indexOf("temple") != -1)
				|| (u_word.indexOf("facilities") != -1)
				|| (u_word.indexOf("country") != -1)
				|| (u_word.indexOf("lake") != -1)) {
                        rootrel.set(j-1, "via");
			rel_label = "via";
                //        unlRelation.add(j-1,rel_label);
		//	rootrel.set(j, "via");
	//		//    findRelation_Category4(j, rel_label);
		}

	}
	/** Rule4 is defined for the UNL Relation:
	 * 'and'
	 * 'ptn'.
	 * The 'and' and 'ptn' relations are obtained by the following
	 * Rule: "உம்" and the position of the analysed string.
	 */

	public void Rule4() {
		rootrel.set(j, "and");
		String pos1 = pos.get(j).toString();
		if ((j + 1) != total) {
			String pos2 = pos.get(j + 1).toString();
			String anal2 = anal.get(j + 1).toString().trim();
			if ((pos1.equals(pos2)) && (anal2.indexOf("உம்") != -1)) {
				rootrel.set(j, "and");
			//	rootrel.set(j+1, "and");
				rel_label = "ptn#and";
            }
		}else{
			String pos3 = pos.get(j - 1).toString();
			String anal3 = anal.get(j - 1).toString().trim();
			if ((pos1.equals(pos3)) && (anal3.indexOf("உம்") != -1)) {
				rootrel.set(j, "and");
			//	rootrel.set(j+1, "and");
				rel_label = "ptn#and";
            }
		}/**else{
			rootrel.set(j, "and");
		}*/
	}
	/**Rule5 is defined for the UNL Realtion:
	 * 'agt'.
	 * The 'agt' relation is obtained by checking the Instrumental case (i.e. 'ins'relation)
	 * The Rule for 'agt' reltion is "படு","அது".
	 * The "insindex" Rule is "ஆல்".
	 */

	public void Rule5() {

		if (insindex == 1) {
			int i = rootrel.indexOf("ins");
			u_word = uw.get(i).toString();
			if ((u_word.indexOf("person") != -1)
					|| (u_word.indexOf("name") != -1)
					|| (u_word.indexOf("relative") != -1)) {
				rootrel.set(i, "agt");
				rel_label = "agt";
                         //       unlRelation.add(j,rel_label);
			//	//    findRelation_Category2(i, rel_label);
			}
		}
	}
	/** Rule6 is defined for the UNL Relations:
	 * 	'con',
	 *  	'ins',
	 * 	'met'.
	 * 	These relations are obtained by applying the
	 * 	Rule: "ஆல்"
	 * 	The insindex is set only when the concept is instrument, flower, vechicle etc..
	 * 	The 'con' relation is obtained by checking with the verb.
	 * 	And if the concept is a person, name or relative then the 'met' relation is obtained.
	 */

	public void Rule6() {

		if (pos.get(j).toString().equals("Verb")) {
			rootrel.set(j, "con");
			rel_label = "con";
                      //  unlRelation.add(j,rel_label);
		//	//    findRelation_Category1(j, rel_label);
		} else {
			insindex = 1;
                        rootrel.set(j, "ins");
			u_word = uw.get(j).toString();

			if ((u_word.indexOf("instrument") != -1)
					|| (u_word.indexOf("flower") != -1)
					|| (u_word.indexOf("vehicle") != -1)
					|| (u_word.indexOf("stationery") != -1)
					|| (u_word.indexOf("rock") != -1)) {
				rel_label = "ins";
                                rootrel.set(j, "ins");
                        } else if ((u_word.indexOf("person") != -1)
					|| (u_word.indexOf("name") != -1)
					|| (u_word.indexOf("relative") != -1)) {
				rel_label = "met";
                                rootrel.set(j, "met");
                        }
		}
	}
	/**	Rule7 is defined for the UNL Relation:
	 * 	'plt'.
	 * 	The 'plt' Relation is obtained by applying the
	 * 	Rule: "க்கு"
	 */

	public void Rule7() {
		rootrel.set(j, "plt");
		rel_label = "plt";
                unlRelation.add(j,rel_label);
	//	//    findRelation_Category1(j, rel_label);
	}
	/**	Rule8 is defined for the UNL Relation:
	 * 	'bas'.
	 * 	The 'bas' relation is obtained by applying the
	 * 	Rule: <"காட்டிலும்", "விட">
	 */

	public void Rule8() {

		rootrel.set(j, "bas");
		rel_label = "bas";
                unlRelation.add(j,rel_label);
	//	//    findRelation_Category1(j, rel_label);
	}
	/**	Rule9 is defined for the UNL Relation:
	 * 	'ben'.
	 * 	The 'ben' relation is obtained by applying the
	 * 	Rule:"உக்கு", "ஆக"
	 *	By Checking the UW word constraint the relation is set to the corresponding concepts.
	 */

	public void Rule9() {

		rootrel.set(j, "ben");
		u_word = uw.get(j).toString();
		if ((u_word.indexOf("person") != -1) || (u_word.indexOf("place") != -1)
				|| (u_word.indexOf("vehicle") != -1)
				|| (u_word.indexOf("city") != -1)
				|| (u_word.indexOf("district") != -1)
				|| (u_word.indexOf("organization") != -1)
				|| (u_word.indexOf("temple") != -1)
				|| (u_word.indexOf("facilities") != -1)
				|| (u_word.indexOf("country") != -1)
				|| (u_word.indexOf("lake") != -1)
				|| (u_word.indexOf("name") != -1)
				|| (u_word.indexOf("relative") != -1)) {
			rel_label = "ben";
                        unlRelation.add(j,rel_label);
		//	//    findRelation_Category2(j, rel_label);
		}
	}
	/**	Rule10 is defined for the UNL Realtions:
	 * 	'cag',
	 * 	'cao',
	 * 	'cob'.
	 * 	These reltions are obtained by setting the codindex and
	 * 	By applying the Rule: "உடன்", "ஓடு".
	 */

	public void Rule10() {

		// int cobindex;

	//	rootrel.set(j, "cag" + "cao" + "cob");
		int cobindex = 1;
		u_word = uw.get(j).toString();

		if ((u_word.indexOf("person") != -1) || (u_word.indexOf("place") != -1)
				|| (u_word.indexOf("vehicle") != -1)
				|| (u_word.indexOf("city") != -1)
				|| (u_word.indexOf("district") != -1)
				|| (u_word.indexOf("organization") != -1)
				|| (u_word.indexOf("temple") != -1)
				|| (u_word.indexOf("facilities") != -1)
				|| (u_word.indexOf("country") != -1)
				|| (u_word.indexOf("lake") != -1)
				|| (u_word.indexOf("name") != -1)
				|| (u_word.indexOf("relative") != -1)) {
			rootrel.set(j, "cag");
			rel_label = "cag";
                } else {
                        rootrel.set(j, "cao");
			rel_label = "cao";
                }
	}
	/**	Rule11 is defined for the UNL Relation:
	 * 	'cnt'.
	 * 	The 'cnt' Relation  is obtained by applying the
	 * 	Rule: "என்பது".
	 */

	public void Rule11() {

		rootrel.set(j, "cnt");
		rel_label = "cnt";
        }
	/**	Rule12 is defined for the UNL Relation:
	 * 	'coo'.
	 * 	The 'coo' relation is obtained by applying the
	 * 	Rule:<"கொண்டே", "அதேவேளை">
	 */

	public void Rule12() {

		rootrel.set(j, "coo");
		rel_label = "coo";
        }
	/**	Rule13 is defined for the UNL Relation:
	 * 	'dur'.
	 * 	The 'dur' relation is obtained by applying the
	 * 	Rule: <"பொழுது", "போது">
	 */

	public void Rule13() {
		rootrel.set(j, "dur");
		rel_label = "dur";
        }
	/**	Rule14 is defined for the UNL Realtion:
	 * 	'equ'.
	 * 	The 'equ' relation is obtained by applying the
	 * 	Rule:	"என்னும்".
	 */

	public void Rule14() {
                for (int i = 0; i < uw.size(); i++) {
			u_word = uw.get(i).toString();
			if (u_word.indexOf("iof>place") != -1) {
                               	rootrel.set(j, "equ");
				rel_label = "equ";
                	}
                }
	}
	/**	Rule15 is defined for the UNL Relation:
	 * 	'plf',
	 * 	'tmf'.
	 * 	These Relations are obtained by applying the following
	 * 	Rules:	<"முதல்", "இருந்து">
	 */
	public void Rule15() {
		for (int i = j - 1; i >= 0; i--) {
			u_word = uw.get(i).toString();
			if ((u_word.indexOf("place") != -1)
					|| (u_word.indexOf("city") != -1)
					|| (u_word.indexOf("district") != -1)
					|| (u_word.indexOf("organization") != -1)
					|| (u_word.indexOf("temple") != -1)
					|| (u_word.indexOf("facilities") != -1)
					|| (u_word.indexOf("country") != -1)
					|| (u_word.indexOf("lake") != -1)) {
				plfindex = i;
				rel_label = "plf";
                                rootrel.set(i, "plf");
	               		break;
			} else if ((u_word.indexOf("month") != -1)
					|| (u_word.indexOf("time") != -1)) {
				plfindex = i;
                                rootrel.set(i, "plf");
				rel_label = "tmf";
                		break;
			}
		}
		/**
		 * if(plfindex > 0) { rel_label = "fmt";
		 * //    findRelation_Category5(plfindex,i,rel_label); }
		 */
	}
	/**	Rule16 is defined for the UNL Relation:
	 * 	'plt',
	 * 	'gol',
	 * 	'tmt'.
	 * 	These relations are obtained by applying the
	 * 	Rules: "க்கு".
	 */

	public void Rule16() {

	//	rootrel.set(j, "plt" + "gol");
		u_word = uw.get(j).toString();
		if ((u_word.indexOf("place") != -1) || (u_word.indexOf("city") != -1)
				|| (u_word.indexOf("district") != -1)
				|| (u_word.indexOf("organization") != -1)
				|| (u_word.indexOf("temple") != -1)
				|| (u_word.indexOf("facilities") != -1)
				|| (u_word.indexOf("country") != -1)
				|| (u_word.indexOf("lake") != -1)) {
			rootrel.set(j, "plt");
                        rel_label = "plt";
               } else if ((u_word.indexOf("time") != -1)
				|| (u_word.indexOf("month") != -1)) {
			rootrel.set(j, "tmt");
                        rel_label = "tmt";
               } else {
                        rootrel.set(j, "gol");
			rel_label = "gol";
               }
	}
	/**	Rule17 is defined for the UNL Relation:
	 * 	'plt'
	 * 	The 'plt' relation is obtained by applying the
	 * 	Rule: "வரை"
	 */
	public void Rule17() {

               	for (int i = j - 1; i >= 0; i--) {
			u_word = uw.get(i).toString();
			if ((u_word.indexOf("place") != -1)
					|| (u_word.indexOf("city") != -1)
					|| (u_word.indexOf("district") != -1)
					|| (u_word.indexOf("organization") != -1)
					|| (u_word.indexOf("temple") != -1)
					|| (u_word.indexOf("facilities") != -1)
					|| (u_word.indexOf("country") != -1)
					|| (u_word.indexOf("lake") != -1)) {
				rel_label = "plt";
                                rootrel.set(j, "plt");
                		break;
			} else if ((u_word.indexOf("time") != -1)
					|| (u_word.indexOf("month") != -1)) {
				rel_label = "tmt";
                                rootrel.set(j, "tmt");
                		break;
			}
		}
		/**
		 * if(plfindex > 0) { rel_label = "fmt";
		 * //    findRelation_Category5(plfindex,i,rel_label); }
		 */
	}
	/** Rule18 is defined for the UNL Relation:
	 * 	'ins',
	 * 	'obj'.
	 * 	These relations are obtained by applying the
	 * 	Rule:	"ஐ"
	 */

	public void Rule18() {
		//rootrel.set(j, "ins" + "obj");
		u_word = uw.get(j).toString();
		if ((u_word.indexOf("instrument") != -1)
				|| (u_word.indexOf("vehicle") != -1)
				|| (u_word.indexOf("stationery") != -1)) {
			rel_label = "ins";
                        rootrel.set(j, "ins");
                } else {
			rel_label = "obj";
                        rootrel.set(j, "obj");
                }
	}
	/**	Rule19 is defined for the UNL Relation:
	 * 	'plc'
	 * 	The 'plc' relation is obtained by applying the
	 * 	Rule: "இங்கு", "அங்கு" with the Pronoun Checking is true.***
	 */
	public void Rule19() {
		rootrel.set(j, "plc");
		rel_label = "plc";
        }
	/** Rule20 is defined for the UNL Relation:
	 * 	'pos'
	 * 	The 'pos' relation is obtained by applying the
	 * 	Rule:"இன்"
	 */
	public void Rule20() {
		rootrel.set(j, "pos");
		rel_label = "pos";
        	int i = j + 1;
		if (i != total) {
			u_word = uw.get(i).toString();
			if ((u_word.indexOf("place") != -1)
					|| (u_word.indexOf("city") != -1)
					|| (u_word.indexOf("district") != -1)
					|| (u_word.indexOf("organization") != -1)
					|| (u_word.indexOf("temple") != -1)
					|| (u_word.indexOf("facilities") != -1)
					|| (u_word.indexOf("country") != -1)
					|| (u_word.indexOf("lake") != -1) || (u_word.indexOf("region") != -1) ) {
			//	rootrel.set(i, pos);
				rel_label = "pos";
                        //        rootrel.set(i, "pos");
                           //     break;
                        } else {
				// break;
			}
		}
		i++;
	}
	/**	Rule21 is defined for the UNL Relation:
	 * 	'ins',
	 * 	'met'.
	 * 	These Relations are obtained by applying the
	 * 	Rules:<"மூலம்","வைத்து","உபயோகி","கொண்டு">
	 */
	public void Rule21() {
	//	rootrel.set(j, "ins");
		u_word = uw.get(j - 1).toString();
		if ((u_word.indexOf("instrument") != -1)
				|| (u_word.indexOf("flower") != -1)
				|| (u_word.indexOf("vehicle") != -1)
				|| (u_word.indexOf("stationery") != -1)) {
			rel_label = "ins";
                        rootrel.set(j, "ins");
               } else {
			rel_label = "met";
                        rootrel.set(j, "met");
               }
	}
	/**	Rule22 is defined for the UNL Relation:
	 * 	'int'.
	 * 	The 'int' relation is obtained by applying the
	 * 	Rule: "பொது".
	 */
	public void Rule22() {

		rootrel.set(j, "int");
		rel_label = "int";
        }
	/**	Rule23 is defined for the UNL Relation:
	 * 	'man'.
	 * 	The 'man' relation is obtained by applying the
	 * 	Rule:"மிக"
	 */

	public void Rule23() {
		rootrel.set(j, "man");
		rel_label = "man";
        }
	/**	Rule24 is defined for the UNL Relation:
	 * 	'man' by appling the Rule:"இங்கு" with the Adverb.
	 */
	public void Rule24() {
		rootrel.set(j, "man");
		rel_label = "man";
	}
	/**	Rule25 is defined for the UNL Relation:
	 * 	'mod' by applying the Rule: Adjective comes as a concept.
	 */

	public void Rule25() {
		rootrel.set(j, "mod");
		rel_label = "mod";
        }
	/**	Rule26 is defined for the UNL Relation:
	 * 	'nam' by applying the Rules:<"ஆகிய","போன்ற","முதலிய">
	 *  and by checking the next word equals  <"இடம்","நாள்","நாடு">
	 */
	public void Rule26() {
		String w1 = root.get(j + 1).toString().trim();

		if ((w1.equals("இடம்")) || (w1.equals("நாள்")) || (w1.equals("நாடு"))) {
			rootrel.set(j, "nam");
			h_word = hw.get(j + 1).toString();
		//	////////System.out.println("h_word:1" + h_word);
			for (int i = 0; i < uw.size(); i++) {
				u_word = uw.get(i).toString();
		//		////////System.out.println("u_word:1" + u_word);
				if (u_word.indexOf(h_word) != -1) {
					rel_label = "nam";
                                        rootrel.set(i, "nam");
                                }

			}
		}
	}
	/**	Rule27 is defined for the UNL Relation:
	 * 	'or'.
	 * 	The Rules applied for the 'OR' relation is:"அல்லது"
	 */
	public void Rule27() {
		rootrel.set(j, "or");
		rel_label = "or";
        }

	public void Rule28() {
		rootrel.set(j, "per");
		rel_label = "per";
        }

	public void Rule29() {
		if ((pos.get(j).toString().equals("Noun"))
				|| (pos.get(j).toString().equals("Entity"))) {
			rootrel.set(j, "pos");
			rel_label = "pos";
        	} else {
			aojindex = j;
			rel_label = "aoj";
                        rootrel.set(j, "aoj");
        	}
	}

	public void Rule30() {
		if (pos.get(j).toString().equals("Verb")) {
			rootrel.set(j, "pur");
			rel_label = "pur";
        	}
	}

	public void Rule31() {
		rootrel.set(j, "qua");
		rel_label = "qua";
        }

	public void Rule32() {
		rootrel.set(j, "rsn");
		rel_label = "rsn";
        }
	/**	Rule33 is defined for the UNL Relation:
	 * 	'seq'.
	 * 	The 'seq' relation is obtained by applying the
	 * 	Rule:
	 */
	public void Rule33() {
		rootrel.set(j, "seq");
		rel_label = "seq";
        }

	public void Rule34() {
		timindex = 1;
		u_word = uw.get(j - 1).toString();
		if (u_word.indexOf("number") != -1) {
			rel_label = "tim";
			rootrel.set(j - 1, "tim");
        	}
	}

	public void Rule35() {
		rootrel.set(j, "iof");
		h_word = hw.get(j).toString();
		for (int i = 0; i < uw.size(); i++) {
			u_word = uw.get(i).toString();
			if (u_word.indexOf(h_word) != -1) {
				rel_label = "iof";
                                rootrel.set(i, "iof");
        		}
		}
	}

	public void Rule36() {
		rootrel.set(j, "pof");
		h_word = hw.get(j).toString();
		for (int i = 0; i < uw.size(); i++) {
			u_word = uw.get(i).toString();
			if (u_word.indexOf(h_word) != -1) {
				rel_label = "pof";
                       	}
		}
	}
	/**	Rule37 is defined for the UNL Relation:
	 * 	'aoj'.
	 * 	The 'aoj' relation is obtained by applying the
	 * 	Rules: <"உள்ளது","இருக்கும்","உள்ளன","நிறைந்தது","ஆகும்","உண்டு", "பற்றி", "பற்றிய">
	 */
	public void Rule37() {
		// flag3=1;
		aojindex = j;
		rootrel.set(j, "aoj");
		rel_label = "aoj";
        }

	public void Rule38() {

	//	rootrel.set(j, "plc");
		u_word = uw.get(j).toString();
		if (u_word.indexOf("pof<body") != -1) {
			rel_label = "opl";
                        rootrel.set(j, "opl");
        	} else if ((u_word.indexOf("place") != -1)
				|| (u_word.indexOf("city") != -1)
				|| (u_word.indexOf("district") != -1)
				|| (u_word.indexOf("organization") != -1)
				|| (u_word.indexOf("temple") != -1)
				|| (u_word.indexOf("facilities") != -1)
				|| (u_word.indexOf("country") != -1)
				|| (u_word.indexOf("lake") != -1) || (u_word.indexOf("region") != -1) ) {

			rel_label = "plc";
                        rootrel.set(j, "plc");
            	} else if (u_word.indexOf("time") != -1) {
			rel_label = "tim";
                        rootrel.set(j, "tim");
                }else{
                        rootrel.set(j,"src");
                }
	}
	/**	Rule39 is defined for the UNL Relation:
	 * 	'neg'.
	 * 	The 'neg' relation is obtained by applying the
	 * 	Rules:<"அல்லாத","நீங்கலாக">
	 */
	public void Rule39() {
		rootrel.set(j, "neg");
		rel_label = "neg";
    	}
	/**	Rule40
	 *
	 */

	public void Rule40() {

	}
	/**	Rule41 is defined for the UNL Relation:
	 * 	'icl'.
	 * 	The 'icl' relation is obtained by applying the
	 * 	Rule: <"உள்ளிட்ட","உள்பட","உட்பட">
	 */
	public void Rule41() {
		rootrel.set(j, "icl");
		rel_label = "icl";
      }
	/**	Rule42 is defined for the UNL Relation:
	 * 	'obj'.
	 * 	The 'obj' relation is obtained by applying the
	 * 	Rules:<"என","என்று">
	 */

	public void Rule42() {
		rootrel.set(j, "obj");
		rel_label = "obj";
        }
	/**	Rule43 is defined for the UNL Relation:
	 * 	'int'.
	 * 	The 'int' relation is obtained by applying the
	 * 	Rule:<"இடையில்","இடையே>
	 */
	public void Rule43() {
		rootrel.set(j, "int");
		rel_label = "int";
	}
        public void Rule45(){
            rootrel.set(j,"plc");
            rel_label = "plc";
        }
        /**	Rule47 is defined for the UNL Relation:
	 * 	'coo'.
	 * 	The 'coo' relation is obtained by applying the
	 * 	Rule: "போதிலும்".
	 */
	public void Rule47() {
		rootrel.set(j, "coo");
		rel_label = "coo";
	}

	/**	Rule49 is defined for the UNL Relation:
	 * 	'plt'.
	 * 	The 'plt' relation is obtained by checking the uword constraint:
	 * 	"place".
	 * 	Even if the 'plf' will obtained by <"இல்","இருந்து">, its not necessary that when the 'plt'
	 *	relation will comes with <"க்கு">.
	 */
	public void Rule49() {

		u_word = uw.get(j).toString();
		if (u_word.indexOf("place") != -1) {
		//	rootrel.set(j, "plt");
			int i;
			if ((j + 1) != total) {
				if (analysed.indexOf("இல்") != -1) // il
				{
					if ((analysed.indexOf("இருந்து") != -1)) // irundhu
					{
						rootrel.set(j, "plt");
						rel_label = "plt";
                                        //        break;
					}
				}
			}
		}
	}
	/**	Rule50 is defined for the UNL Relation:
	 * 	'pof'.
	 * 	The 'pof' relation is obtained by applying the
	 * 	Rule:"number".
	 * 	If the number street no.,door no.,etc.
	 * 	The condition is checked for the number not comes as a year, date, etc..
	 */
	public void Rule50() {
		if ((j - 1) != total) {
			u_word = uw.get(j - 1).toString();
			if (u_word.indexOf("place") != -1) {
				tempword = root.get(j + 1).toString().trim();

				if (!((tempword.equals("ஆம்")) || (tempword.equals("இல்"))
						|| (tempword.equals("ம்")) || (tempword.equals("ல்")))){
					rootrel.set(j, "pof");
					rel_label = "pof";
                          	}
			}
		}
	}
	/**	Rule53 is defined for the UNL Relation:
	 * 	'pos'.
	 * 	The 'pos' relation is obtained by applying the
	 * 	Rule: <"க்கு","உரிய">,"உள்ள".
	 */

	public void Rule53() {
		rootrel.set(j, "pos");
		rel_label = "pos";
        }
	/** Rule54 is defined for the UNL Relation:
	 * 	'pof'.
	 * 	The 'pof' relation is obtained by applying the
	 * 	Rule:"உட்பட்ட".
	 */
	public void Rule54()
	{
		rootrel.set(j, "pof");
		rel_label = "pof";
        }

	public void Rule55()
	{
	//	rootrel.set(j, "mod");
	//	String uword1 = anal.get(j+1).toString();
	//	if( (uword1.indexOf("person")!=-1) || (uword1.indexOf("god")!=-1) || (uword1.indexOf("goddess")!=-1) ){
			rootrel.set(j, "agt");
			rel_label = "agt";
                       
        //	}
	}

	public void Rule56()
	{
	//	rootrel.set(j, "pos");
		String u_word1 = uw.get(j+1).toString();
 		if(  (u_word1.indexOf("facilities")!=-1) || (u_word1.indexOf("temple")!=-1)  ){
			rel_label = "pos";
                        rootrel.set(j, "pos");
        	}
	}
        public void Rule57(){
                unlRelation.add("None");
        }

	public void getCaseEndingsandRelations() // throws Exception
	{

		j = 0;

		analysed = "";
		tempword = "";
		//System.out.println("Anal inside Findrelnindex:"+anal);

		String rel_label;
		String h_word = " ";
		String u_word = " ";
                String cEndWord = "";

		total = anal.size();
	//	result += "[r]#";
		while (j < total) {
			try {
			//	////////System.out.println("j:"+j);
				rootrel.add("None");
				analysed = anal.get(j).toString().trim();
				tempword = root.get(j).toString().trim();
                                u_word = uw.get(j).toString();
				if ((analysed.indexOf("இல்") != -1)|| (analysed.indexOf("ஆக") != -1)
				|| (analysed.indexOf("இடம்") != -1)) {
					if ((analysed.indexOf("இருந்து") != -1)) {
                                                cEndWord = "இல்#இருந்து";
						caseEndings.add(cEndWord);
                                                Rule1();
					}else if (analysed.indexOf("உள்ள") != -1){
						cEndWord = "இல்#உள்ள";
                                                caseEndings.add(cEndWord);
                                                Rule40();
					}else if (analysed.indexOf("உம்") != -1) {
						cEndWord = "இல்#உம்";
                                                caseEndings.add(cEndWord);
                                                Rule4();
					}else{
						cEndWord = "இல்";
                                                caseEndings.add(cEndWord);
                                                Rule38();
					}
				}else if (analysed.indexOf("உம்") != -1) {
					cEndWord = "உம்";
                                        caseEndings.add(cEndWord);
                                        Rule4();
				}else if ((analysed.indexOf("படு") != -1)
						&& (analysed.indexOf("அது") != -1)) {
					cEndWord =  "படு#அது";
                                        caseEndings.add(cEndWord);
                                        Rule5();
				}else if (analysed.indexOf("ஆல்") != -1) {
					cEndWord = "ஆல்";
                                        caseEndings.add(cEndWord);
                                        Rule6();
				}else if (analysed.indexOf("உக்கு") != -1) {
					if (analysed.indexOf("ஆக") != -1) {
                                            cEndWord = "உக்கு#ஆக";
                                            caseEndings.add(cEndWord);
                                            Rule9();
					}
				}else if ((analysed.indexOf("உடன்") != -1)
						|| (analysed.indexOf("ஓடு") != -1)) {
					cEndWord = "உடன்#ஓடு";
                                        caseEndings.add(cEndWord);
                                        Rule10();
				}else if (analysed.indexOf("க்கு") != -1) {
					if (analysed.indexOf("உரிய") != -1){
						cEndWord = "க்கு#உரிய";
                                                caseEndings.add(cEndWord);
                                                Rule53();
					}else if (analysed.indexOf("உட்பட்ட") != -1) {
						cEndWord = "உட்பட்ட";
                                                caseEndings.add(cEndWord);
                                                Rule54();
					}else if (analysed.indexOf("உம்") != -1){
						cEndWord = "உம்";
                                                caseEndings.add(cEndWord);
                                                Rule4();
					}else{
                                            cEndWord = "க்கு";
                                            caseEndings.add(cEndWord);
                                            Rule16();
                                        }
				}else if (analysed.indexOf("ஐ") != -1) {
					cEndWord = "ஐ";
                                        caseEndings.add(cEndWord);
                                        Rule18();
				}else if (analysed.indexOf("இன்") != -1) {
					cEndWord = "இன்";
                                        caseEndings.add(cEndWord);
                                        Rule20();
				}else if ((analysed.indexOf("உடைய") != -1)
						|| (analysed.indexOf("அது") != -1)) {
					cEndWord = "உடைய#அது";
                                        caseEndings.add(cEndWord);
                                        Rule29();
				}else if ((analysed.indexOf("ஆக") != -1)
						|| (analysed.indexOf("க்க") != -1)) {
					cEndWord = "ஆக#க்க";
                                        caseEndings.add(cEndWord);
                                        Rule30();
				}else if ((tempword.equals("உள்ளது"))
						|| (tempword.equals("இருக்கும்"))
						|| (tempword.equals("உள்ளன"))
						|| (tempword.equals("நிறைந்தது"))
						|| (tempword.equals("ஆகும்"))
						|| (tempword.equals("உண்டு"))
						|| (tempword.equals("பற்றி"))
						|| (tempword.equals("பற்றிய"))
						|| (analysed.indexOf("உள்ளது") != -1)
						|| (analysed.indexOf("இருக்கும்") != -1)
						|| (analysed.indexOf("உள்ளன") != -1)
						|| (analysed.indexOf("நிறைந்தது") != -1)
						|| (analysed.indexOf("ஆகும்") != -1)
						|| (analysed.indexOf("உண்டு") != -1)) {
					cEndWord = "None";
                                        caseEndings.add(cEndWord);
                                        Rule37();
				}else if ((analysed.indexOf("என") != -1) || (tempword.equals("என"))
						|| (tempword.equals("என்று"))) {
					cEndWord = "None";
                                        caseEndings.add(cEndWord);
                                        Rule42();
				}else if ((tempword.equals("மட்டுமல்லாமல்"))||(tempword.equals("மற்றும்")) || (tempword.equals("மேலும்"))) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule2();
				}else if (tempword.equals("வழியாக")) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule3();
				}else if ((tempword.equals("காட்டிலும்")) || (tempword.equals("விட"))) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule8();
				}else if (tempword.equals("என்னும்")) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule14();
				}else if ((tempword.equals("முதல்")) || (tempword.equals("இருந்து"))) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule15();
				}else if ((tempword.equals("மூலம்")) || (tempword.equals("வைத்து"))
						|| (tempword.equals("உபயோகி"))
						|| (tempword.equals("கொண்டு"))) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule21();
				}else if (tempword.equals("மிக")) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule23();
				}else if ((tempword.equals("ஆகிய")) || (tempword.equals("போன்ற"))
						|| (tempword.equals("முதலிய"))) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule26();
				}else if (tempword.equals("அதனால்")) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule32();
				}else if ((tempword.equals("அப்புறம்"))
						|| (tempword.equals("முன்னால்"))||(tempword.equals("பின்னால்"))
						|| (tempword.equals("பின்னர்"))
						|| (tempword.equals("பின்பு")) ) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule33();
				}else if ((tempword.equals("ஆம்")) || (tempword.equals("இல்"))
						|| (tempword.equals("ம்")) || (tempword.equals("ல்"))) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule34();
				}else if ((tempword.equals("அல்லாத"))
						|| (tempword.equals("நீங்கலாக"))) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule39();
				}else if (tempword.equals("என்பது")) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule11();
				}else if ((tempword.equals("கொண்டே")) || (tempword.equals("அதேவேளை"))) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule12();
				}else if ((tempword.equals("பொழுது")) || (tempword.equals("போது"))) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule13();
				}else if(tempword.equals("வரை")) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule17();
				}else if ((analysed.indexOf("இங்கு") != -1) || (analysed.indexOf("அங்கு") != -1)) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule19();
				}else if (tempword.indexOf("பொது") != -1) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule22();
				}else if (((analysed.indexOf("இங்கு") == -1))
						&& (analysed.indexOf("Adverb") != -1)) {
					cEndWord = "இங்கு";
                                        caseEndings.add(cEndWord);
                                        Rule24();
				}else if ((analysed.indexOf("Adjective") != -1)
						|| (analysed.indexOf("Adjectival Suffix") != -1)
						|| (analysed.indexOf("Adjectival Noun") != -1)) {
					cEndWord = "None";
                                        caseEndings.add(cEndWord);
                                        Rule25();
				}else if (tempword.equals("அல்லது")) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule27();
				}else if (tempword.equals("முறை")) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule28();
				}else if (tempword.equals("நிறைய")) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule31();
				}else if ( (tempword.equals("நாடு")) || (tempword.equals("மாவட்டம்")) || (tempword.equals("நகரம்")) || (tempword.equals("மாநிலம்")) || (tempword.equals("நதி")) || (tempword.equals("ஏரி")) || (tempword.equals("பாலம்")) ) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule35();
				}else if ( (tempword.equals("உடல்")) || (tempword.equals("மரம்")) ){
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule36();
				}else if ((tempword.equals("உள்ளிட்ட")) || (tempword.equals("உள்பட"))
						|| (tempword.equals("உட்பட"))) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule41();
				}else if ((tempword.equals("இடையில்")) || (tempword.equals("இடையே"))) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule43();
				}else if (tempword.equals("போதிலும்")) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule47();
				}/**else if (u_word.indexOf("place") != -1) {
					cEndWord = "None";
                                        caseEndings.add(cEndWord);
                                        Rule49();
				}*/else if (u_word.indexOf("number") != -1) {
					cEndWord = "None";
                                        caseEndings.add(cEndWord);
                                        Rule50();
				}else if (tempword.equals("உள்ள")) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule53();
				}else if ((tempword.equals("மட்டுமல்லாமல்"))
						|| (tempword.equals("அல்லாத"))) {
					cEndWord = tempword;
                                        caseEndings.add(cEndWord);
                                        Rule38();
				}else if( (u_word.contains("city")) || (u_word.contains("state")) || (u_word.contains("place")) || (u_word.contains("country")) || (u_word.contains("district")) || (u_word.contains("facilities")) || (u_word.contains("temple")) || (u_word.contains("region")) ){
				//	System.out.println("u_word:"+u_word);
					cEndWord = "None";
                                        caseEndings.add(cEndWord);
                                        Rule45();
				}else if( (u_word.indexOf("person")!=-1) || (u_word.indexOf("god")!=-1) ||  (u_word.indexOf("goddess")!=-1) ){
				//	System.out.println("u_word:"+u_word);
					cEndWord = "None";
                                        caseEndings.add(cEndWord);
                                        Rule55();
				}else{
                                      cEndWord = "None";
                                      caseEndings.add(cEndWord);
                                      Rule57();
                                }
			} catch (Exception e) {
			}
			j++;
		}
                unlRelation.clear();
                unlRelation.addAll(rootrel);
		j = 0;

                System.out.println("CaseEndings:"+caseEndings+":"+caseEndings.size());
                System.out.println("unlRelation:"+unlRelation+":"+unlRelation.size());
                System.out.println("rootrel:"+rootrel+":"+rootrel.size());
}
        public void loadequ() {
		String equentry = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(
					"./resource/unl/equ.txt"));
			while ((equentry = in.readLine()) != null) {
				StringTokenizer tok = new StringTokenizer(equentry, "/");
				abbwords.add(tok.nextToken());
				expwords.add(tok.nextToken());
			}
			in.close();
		} catch (Exception e) {

		}

	}
        public void writeintoFile(String filename,String tag_out){
            try{
        //        System.out.println("Filename:"+filename);
                Writer output = null;
            //    Directory dic = new Directory()"./resource/unl/tagDocs/";
                File fn1 = new File("./resource/unl/tagDocs/");
          //      System.out.println("FileDir:"+fn1);
                String[] writeStr = filename.split("/");
                String pathname = writeStr[1];
              /**  String[] splitname = pathname.split(".");
                String prefix = splitname[0];
                String suffix = splitname[1];*/
           //     System.out.println("pathname:"+pathname);
                File dir = new File("./resource/unl/tagDocs/"+filename);
                dir.createNewFile();
             //   System.out.println("Directory:"+dir);
              //  dir.createTempFile(prefix, suffix, fn1);
                output =new BufferedWriter(new FileWriter(dir,false));
                output.write(tag_out);
                output.close();
            }catch(Exception e){
                
            }
        }
	public static void main(String args[]) {

		SemiRules r = new SemiRules();
		String s = null;
		FileReader fr = null;
		String temp = null;
                String taggedOutput = "";
		String str = "";
		String sent = "", recv = "", token = "", fn = "";
		int temp1 = 0, j = 0;
		int scnt=0;
		int docid=0;
		String docident="";
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"./resource/unl/seinput.txt"));
			docid++;
			docident = "d"+docid;
			while ((s = br.readLine()) != null) {
				fn = "./resource/unl/" + s;
                                tag_file = s;
				System.out.println("fn:" + fn);
				StringBuffer docbuff = new StringBuffer();
				fr = new FileReader(fn);
				// System.out.println("fr:"+fr);
				StreamTokenizer st = new StreamTokenizer(fr);
				
				while (st.nextToken() != st.TT_EOF) {
					temp = "";
					temp1 = 0;
					if (st.ttype == st.TT_WORD)
						temp = st.sval;
					else if (st.ttype == st.TT_NUMBER) {
						temp1 = (int) st.nval;
						temp = Integer.toString(temp1);
					}
					if (temp != null)
						docbuff.append(temp + " ");
				}
			//	System.out.println("docbuff:"+docbuff);
				StringTokenizer sentToken1 = new StringTokenizer(docbuff
						.toString().trim(), ".", false);
				j = 0;
				scnt++;
				while (sentToken1.hasMoreTokens()) {
		
					sent = sentToken1.nextToken();
					//String did="d"+j;
					recv = r.enconvert(sent,scnt,docident);
                                        taggedOutput += recv;
					scnt++;
				}
                             //   taggedOutput = recv;
                             //   System.out.println("Tagged output:"+taggedOutput);
                                r.writeintoFile(tag_file,taggedOutput);
                                taggedOutput="";
			}
                      
		} catch (Exception e) {

		}
	}
}
