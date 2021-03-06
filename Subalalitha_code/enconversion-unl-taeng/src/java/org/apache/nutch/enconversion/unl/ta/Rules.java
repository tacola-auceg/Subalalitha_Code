package org.apache.nutch.enconversion.unl.ta;

import org.apache.nutch.analysis.unl.ta.Analyser;
import org.apache.nutch.util.NutchConfiguration;
import org.apache.hadoop.conf.Configuration;

import java.lang.*;
import java.util.*;
import java.awt.RenderingHints.Key;
import java.io.*;

/** Rules class consists of a set of Rules to enconvert the tamil documents into UNL Graphs
* There are 53 Rules based on Morphological Endings, POS, Semantics, Co-occurences
* These 53 Rules are Categorized into 5 Categories: Morphological Endings, POS, Semantics, Co-occurences and Connective
* This Class also includes Word Sense Disambiguation consists of 8 Rules
* Finally the Output returned by this class is a set of UNL Graphs for a set of documents.
* UW List and MW list are used to retrieve the  UW concepts of a Tamil word
**/

public class Rules {

	MWDict mwdict;
	UWDict uwdict;

	BSTNode bn;
	BST dict;

	MWBST dict_mw;
	MWBSTNode bn_mw;
	
	Centering centering;
	
	//NestedGraph nestedunl;

	ArrayList<String> anal;
	ArrayList<String> wordAL;
	ArrayList<String> root;
	ArrayList<String> rootnew;
	ArrayList<String> pos;
	ArrayList rootrel;

	ArrayList<String> abbwords = new ArrayList<String>();
	ArrayList<String> expwords = new ArrayList<String>();

	ArrayList<String> tempcmtw;
	ArrayList<String> tempcmew;
	ArrayList<String> tempcmuw;

	ArrayList<String> hw;
	ArrayList<String> uw;
	ArrayList no;
	ArrayList<String> conceptfrm;
	ArrayList<String> relnlabel;
	ArrayList<String> conceptto;
	ArrayList compdid;

	int aojindex;
	int plfindex;
	int insindex;
	int cobindex;
	int timindex;
	int cwindex;
	int verbcount;

	String verb1;
	String verb2;
	String verb3;

	boolean bolverb1;
	boolean bolverb2;
	boolean bolverb3;

	String verbno1;
	String verbno2;
	String verbno3;

	boolean pr_res;
	String pr;
	int pr_index;

	String firstword = "";
	String firstwordpss = "";
	String nestedresult = "";

	int noofcw;

	int j = 0, total;

	int cnt1 = 0, cnt2 = 0, cnt3 = 0, cnt4 = 0, cnt5 = 0;

	String analysed;
	String tempword;

	String rel_label;
	String h_word = " ";
	String u_word = " ";

	String result = "";

	String mwword = "";

	public String rootentry = null;
	public String uwentry = null;
	public String hwentry = null;

	public String prevanal = null;
	public String curranal = null;
	public String nextanal = null;
	public String prevhw = null;
	public String nexthw = null;
	public String prevuw = null;
	public String nextuw = null;
	public String prevconcept = null;
	public String nextconcept = null;

	ArrayList<String> tne = new ArrayList<String>();
	ArrayList<String> uwlist;
	ArrayList<String> hwlist;
	ArrayList<String> rootlist;
	ArrayList<String> fentry;
	ArrayList<String> sense;

	ArrayList<String> splitword;
	ArrayList<String> splitpos;
	ArrayList<String> splitanal;
	ArrayList<String> multisplit;

	ArrayList<String> RefExp;
//	ArrayList<String> RefExpWord = new ArrayList<String>();
	ArrayList<String> personUW;
	ArrayList<String> pnounuwList = new ArrayList<String>();
	
	ArrayList<String> sent_store;
	Hashtable freqTable = new Hashtable();

	// clia.unl.wordsense.WordSense ws;

	public String sword = "";
	public String sente = "";

	Configuration conf = NutchConfiguration.create();
	String path = conf.get("EncInput");

	public Rules() {
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
			
			centering = new Centering();
			
	//		nestedunl = new NestedGraph();
			
		} catch (Exception e) {

		}

	}

	public void init() {
		try {

			anal = new ArrayList<String>();
			wordAL = new ArrayList<String>();
			root = new ArrayList<String>();
			rootnew = new ArrayList<String>();
			pos = new ArrayList<String>();
			
			rootrel = new ArrayList();

			tempcmtw = new ArrayList<String>();
			tempcmew = new ArrayList<String>();
			tempcmuw = new ArrayList<String>();

			hw = new ArrayList<String>();
			uw = new ArrayList<String>();
			no = new ArrayList();

			conceptfrm = new ArrayList<String>();
			relnlabel = new ArrayList<String>();
			conceptto = new ArrayList<String>();
			compdid = new ArrayList();
			sent_store = new ArrayList();
			uwlist = new ArrayList<String>();
			hwlist = new ArrayList<String>();
			rootlist = new ArrayList<String>();
			fentry = new ArrayList<String>();
			sense = new ArrayList<String>();

			splitword = new ArrayList<String>();
			splitpos = new ArrayList<String>();
			splitanal = new ArrayList<String>();
			multisplit = new ArrayList<String>();
			
			RefExp = new ArrayList<String>();
			personUW = new ArrayList<String>();
		//	recencyList = new ArrayList<String>();
			
			aojindex = 0;
			insindex = 0;
			plfindex = 0;
			timindex = 0;
			cobindex = 0;

			cwindex = 0;
			noofcw = 0;

			bolverb1 = false;
			bolverb2 = false;
			bolverb3 = false;

			pr_res = false;
			pr = "";
			pr_index = -1;

			verb1 = "";
			verb2 = "";
			verb3 = "";

			verbno1 = " ";
			result = "[s]#";
			nestedresult = "";

		} catch (Exception e) {

		}
	}

	public String enconvert(String st) {
		try {
			int cnt = 1;
			String templex = "";
			String mwe = "";
			int flag = 0;
			int now = 0;
			String dAdjective = null;
			String adverbWord = null;
			String pronounWord = null;
			String[] demonAdj;
			String[] adverbStr = null;;
			String[] pronounStr;
			// no. of words is initialized to zero
			// To initialize all array list, variables...
			init();
			sent_store.add(st.trim());

			// Identify words delimiter
		//	System.out.println("String:"+st);
			StringTokenizer inp = new StringTokenizer(st, ",; ");
			sente = st;
			int noofwords = inp.countTokens(); // No. of words in the given
												// input
			int ctnoofwords = 0; // Count no. of words is initialized to zero

			// store the words in inp array List and root words in root array
			// List
		//	System.out.println("Start RefExp:"+RefExp);
			while (ctnoofwords < noofwords) // Check the condition
			{

				String word = inp.nextToken().trim(); // Remove the blank
														// spaces in front and
														// back of the sentence
														// using trim().
			//	System.out.println("word:"+word);
				String pss = "";
				String word2 = "";
				String word1 = "";
				String prev, next;

				// to add root word in wordAL arrayList
				wordAL.add(word);

				// analyze the given word

				String analysed = org.apache.nutch.analysis.unl.ta.Analyser
						.analyseF(word, true);
				System.out.println("analysed:"+analysed);
				StringTokenizer strToken2 = new StringTokenizer(analysed,
						":\n<>,=+»←→↓↑•*;:?-.'“\"&", false);

				int firstindex = analysed.indexOf('<', analysed.indexOf('>'));
				String analmod = analysed.substring(firstindex);
				// to store analyzer output
				anal.add(analmod);
				// to get root word and POS from analyzer output
				if ((analysed.indexOf("unknown") != -1)
						|| (analysed.indexOf("count=4") != -1)) {
					word1 = strToken2.nextToken().trim();
					word2 = word1;
					pos.add("None");
					root.add(word2);
					if(RefExp.isEmpty()){
						RefExp.add(word2);
					}else if(RefExp.contains(word2)){
						int k = RefExp.indexOf(word2);							
						RefExp.remove(k);
						if(RefExp.size()!=0){
							RefExp.add(RefExp.size()-1,word2);
						}	
					}else{
						RefExp.add(word2);						
					}
				}else if ((analysed.indexOf("unknown") != -1)
						|| (analysed.indexOf("count=5") != -1)){
					word1 = strToken2.nextToken().trim();
					word2 = word1;
					pos.add("None");
					root.add(word2);
				}else if(analysed.indexOf("Demonstrative Adjective")!=-1){
					prev = strToken2.nextToken().trim();
					word1 = prev;
					next = strToken2.nextToken().trim();
					dAdjective = centering.centeringProcessforDAdjective(next,analysed,pnounuwList);
					if(dAdjective != null){	
						demonAdj = dAdjective.split("/");
				
						root.add(demonAdj[0]);
						pos.add(demonAdj[1]);
					}else{
						root.add(next);
						pos.add("None");
					} 
				}else if ((analysed.indexOf("Adjectival Noun") != -1)
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
						|| (analysed.indexOf("Adjective") != -1)) {
					prev = strToken2.nextToken().trim();
					word1 = prev;
					next = strToken2.nextToken().trim();
					if( (analysed.contains("Entity")) || (analysed.contains("Noun")) ){
						if(RefExp.isEmpty()){
							RefExp.add(next);
						}else if(RefExp.contains(next)){
							int k = RefExp.indexOf(next);							
							RefExp.remove(k);
							if(RefExp.size()!=0){
								RefExp.add(RefExp.size()-1,next);
							}	
						}else{
								RefExp.add(next);
						}						
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
					if (word1.indexOf("இங்கு") != -1) {
						addMorphology(analmod);
					//	System.out.println("Inside Adverb:"+word1);
						adverbWord = centering.centeringProcessforAdverb(pnounuwList,word1);
						if(adverbWord != null){	
							adverbStr = adverbWord.split("/");
							word2 = adverbStr[0];
							pss = adverbStr[1];						
					//		System.out.println("After Processing Adverb:"+word2);
							
							root.add(word2);
							pos.add(pss);
					//		pnounuwList.clear();
						}
					} else {	
						root.add(word2);
						pos.add(pss);
					} 
				} else {
					
					word1 = strToken2.nextToken().trim();
					word2 = strToken2.nextToken().trim();
					System.out.println("word1"+word1+"\t"+word2);
					pss = strToken2.nextToken().trim();
					System.out.println("pss"+pss);

					if ((analysed.contains("Pronoun"))
							|| (word1.indexOf("இதன்") != -1)) // ithan, ithu
					{
					//	System.out.println("Inside Pronoun:"+word1);
						chkIfPerson();
						pronounWord = centering.centeringProcessforPronoun(personUW,pnounuwList,word1,analysed);
						
						if(pronounWord!=null){						
							pronounStr = pronounWord.split("/");
							word2 = pronounStr[0];
							pss = pronounStr[1];						
					//		System.out.println("After Processing Pronoun:"+word2);
							root.add(word2);
							pos.add(pss);
					//		pnounuwList.clear();
						}					
					} else {
						root.add(word2);
						pos.add(pss);					
					}
				} // end of else
				if ((pss.equals("Verb")) || (pss.equals("Finite Verb"))) {
					verbcount++;
					flag = 0;
					if (bolverb1 == false) {
						bolverb1 = true;
						verb1 = word2;
					//	System.out.println("Verb1:"+verb1);
					} else if ((bolverb1 == true) && (bolverb2 == false)) {
						bolverb2 = true;
						verb2 = word2;
					//	System.out.println("Verb2:"+verb2);
					} else if ((bolverb1 == true) && (bolverb2 == true)
							&& (bolverb3 == false)) {
						bolverb3 = true;
						verb3 = word2;
					//	System.out.println("Verb3:"+verb3);
					}
				} // end of if
				else {
					if (flag == 0) {
						int hc = word2.hashCode();
						// Set the unique value for the word using the Hashcode
						// to search the word in the dictionary using the
						// keyvalue
						if ((bn_mw = dict_mw.search(hc)) != null) // Condition
																	// is
																	// checked
						{
							flag = 1;
							cnt++;
							now = bn_mw.no_words;
							templex += word2 + " ";
						}
					} else if ((flag == 1) && (cnt < now)) {
						cnt++;
						templex += word2 + " ";
					} else if ((flag == 1) && (cnt == now)) {
						templex += word2;
						multiwordCheck(templex, bn_mw, cnt, now, pss, analmod);
						templex = "";
						cnt = 1;
						flag = 0;
					} // end of else if
				} // end of else
			
				sword = word2;
				splitword.add(word2);
				splitpos.add(pss);
				ctnoofwords++;				
			} // end of while			
			for (int i = 0; i < pos.size(); i++) {
				String s = pos.get(i).toString();
			}
			// splittingProcess();
			// mwword="";
			findUW();
			if (bolverb1 == true)
				verbno1 = findverbnumber(verb1);
//			firstword = root.get(0).toString(); // to keep track of prev first
												// word for PR
//			firstwordpss = pos.get(0).toString();	
			findrelnindex();
		//	nestedresult = nestedunl.processGraph(result);
		//	pnounuwList.clear();
		//	System.out.println("freqTable:"+freqTable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void clearList(){
		pnounuwList.clear();
	}
	public void loadequ() {
		String equentry = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(path
					+ "/equ.txt"));
			while ((equentry = in.readLine()) != null) {
				StringTokenizer tok = new StringTokenizer(equentry, "/");
				abbwords.add(tok.nextToken());
				expwords.add(tok.nextToken());
			}
			in.close();
		} catch (Exception e) {

		}

	}

	public void findUW() {

		int count = 1;
		int cnt = 0;
		String pnentry=null;
		String rword = "";
		ArrayList<String> te = new ArrayList<String>();
		// StringBuffer sb = new StringBuffer();
		StringBuffer resultuw = new StringBuffer();
		int j = 0, total;
		// System.out.println("SIZE OF ROOT IN FINDUW:"+root.size());
		try {
			total = root.size();
			while (j < total) {

				bn = new BSTNode();

				rword = root.get(j).toString().trim();
			//	System.out.println("RWORD:"+rword);
				int hc = rword.hashCode();

				int cindex = tempcmtw.indexOf(rword);

				if (cindex >= 0) {

					hw.add(tempcmew.get(cindex).toString());
					uw.add(tempcmuw.get(cindex).toString());
					no.add(new Integer(count++));
					rootnew.add(rword);
				} else if ((bn = dict.search(hc)) != null) {
					
					BSTNode bnode = new BSTNode();
					bnode = bn;

					if (bnode.next != null) {
						te = disambiguate(rword, j, sente);

						cnt = te.indexOf(rword);
						if (cnt != -1) {
							hw.add(te.get(cnt + 1).toString());
							uw.add(te.get(cnt + 2).toString());
							no.add(new Integer(count++));
							rootnew.add(rword);
						} else if (cnt == -1) {
							hw.add(bnode.headword);
							uw.add(bnode.constraint);
							no.add(new Integer(count++));
							rootnew.add(rword);
						}					
					} else {
						hw.add(bnode.headword);
						uw.add(bnode.constraint);
						no.add(new Integer(count++));
						rootnew.add(rword);
					}
					// no.add(new Integer(count++));
				} else if (pos.get(j).equals("Numbers")) {

					hw.add(rword);
					uw.add("icl>number");
					no.add(new Integer(count++));
					rootnew.add(rword);
				} else {
					rootnew.add("None");
					hw.add("None");
					uw.add("None");
					no.add("None");
				}
		//		System.out.println("ROOT+POS:"+rootnew.get(j).toString()+"/"+pos.get(j).toString());
				pnentry = rootnew.get(j).toString()+"/"+pos.get(j).toString()+"/"+uw.get(j).toString();
		//		System.out.println("PNENTRY:"+pnentry);
				if(pnounuwList.isEmpty()){
					pnounuwList.add(pnentry);
				}else if(pnounuwList.contains(pnentry ) ){
					int k = pnounuwList.indexOf(pnentry);
				//	System.out.println("Index:"+k);
					pnounuwList.remove(k);
					pnounuwList.add(pnounuwList.size()-1,pnentry);
				}else{
					pnounuwList.add(pnentry);
				}
			//	freqCount(rword);
				// System.out.println("J inside while:"+j);
				j++;
			}// end of while
			
			// to append all concepts

			for (int k = 0; k < hw.size(); k++) {
				if (!(rootnew.get(k).equals("None"))) {

					resultuw.append(rootnew.get(k).toString() + ';');
					resultuw.append(hw.get(k).toString() + ';');
					resultuw.append(uw.get(k).toString() + ';');

					resultuw.append(pos.get(k).toString() + ';');
					resultuw.append(no.get(k).toString() + '#');
					// resultuw.append(fcount.get(k).toString()+';');
				}
			}
			result += "[w]#";
			result += resultuw;
			result += "[/w]#";

		} catch (Exception e) {
		}
	}

	/**
	 * public void splittingProcess(){ for (int i=0;i<multisplit.size();i++){
	 * String getmw = multisplit.get(i).toString(); //
	 * System.out.println("mw:"+mwword); // System.out.println("getmw:"+getmw);
	 * StringTokenizer strTok = new StringTokenizer(getmw," "); int tnow =
	 * strTok.countTokens(); int cnow = 0; while(cnow < tnow){ String splitmw =
	 * strTok.nextToken(); // System.out.println("SplitMW:"+splitmw); int
	 * position = splitword.indexOf(splitmw); //
	 * System.out.println("POSITION:"+position); root.add(splitmw.trim());
	 * pos.add(splitpos.get(position).toString()); cnow++; } } }
	 */

	public ArrayList<String> disambiguate(String sword, int j, String sente) {

		try {
			while (bn != null) {
				// System.out.println("bn1:"+bn.lexeme+"/"+bn.headword+"/"+bn.constraint);
				rootlist.add(bn.lexeme);
				hwlist.add(bn.headword);
				uwlist.add(bn.constraint);

				bn = bn.getNext();
			}
			sense = RulesSet(sword, j);
			// System.out.println("Sense:"+sense);
		} catch (Exception e) {

		}
		return sense;
		// sense.clear();
	}

	public void NoRules(String sword) {
		// int i=0;

		for (int i = 0; i < rootlist.size(); i++) {
			// i = rootlist.indexOf(sword);
			if (sword.equals(rootlist.get(i).toString())) {
				fentry.add(sword);
				fentry.add(hwlist.get(i).toString());
				fentry.add(uwlist.get(i).toString());
				// break;
			}
		}
	}

	public void VerbRule(String sword) {
		// int j = 0;
		for (int i = 0; i < rootlist.size(); i++) {
			// ////////System.out.println("VerbRule");
			rootentry = rootlist.get(i).toString();
			if (rootentry.equals(sword)) {
				int j = rootlist.indexOf(sword);
				uwentry = uwlist.get(j).toString();
				if (uwentry.contains("action")) {
					// ////////System.out.println("Verb Sense:"+uwentry);
					fentry.add(sword);
					fentry.add(hwlist.get(j).toString());
					fentry.add(uwlist.get(j).toString());
				}
			}
		}
	}

	public void NumberSense(String sword) {
		int ind = rootlist.indexOf(sword);
		for (int i = ind; i < uwlist.size(); i++) {
			// ////System.out.println("NumberRule");
			uwentry = uwlist.get(i).toString();
			if (uwentry.contains("number")) {
				// ////System.out.println("Number Sense:"+uwentry);
				fentry.add(sword);
				fentry.add(hwlist.get(i).toString());
				fentry.add(uwentry);
			}
		}// ////System.out.println("NumberRule fentry:"+fentry);
	}

	public void NotNumberSense(String sword) {
		int ind = rootlist.indexOf(sword);
		if (!(uwlist.get(ind).toString().contains("number"))) {
			if (!(uwlist.get(ind + 1).toString().contains("number"))) {
				// ////////System.out.println("NotNumberSense:"+sword);
				fentry.add(sword);
				fentry.add(hwlist.get(ind).toString());
				fentry.add(uwlist.get(ind).toString());
			}
		}
	}

	public void PositionSense(String sword) {
		int ind = rootlist.indexOf(sword);
		for (int i = ind; i < uwlist.size(); i++) {
			uwentry = uwlist.get(i).toString();
			if (uwentry.contains("position")) {
				// ////////System.out.println("Position Sense:"+uwentry);
				fentry.add(sword);
				fentry.add(hwlist.get(i).toString());
				fentry.add(uwentry);
			}
		}
	}

	public void OfferSense(String sword) {
		int ind = rootlist.indexOf(sword);
		for (int i = ind; i < uwlist.size(); i++) {
			uwentry = uwlist.get(i).toString();
			if ((uwentry.contains("offer")) || (uwentry.contains("food"))
					|| (uwentry.contains("fruit"))) {
				// ////////System.out.println("Offer Sense:"+uwentry);
				fentry.add(sword);
				fentry.add(hwlist.get(i).toString());
				fentry.add(uwentry);
			}
		}
	}

	public void PlaceSense(String sword) {
		for (int i = 0; i < uwlist.size(); i++) {
			uwentry = uwlist.get(i).toString();
			hwentry = hwlist.get(i).toString();
			if ((uwentry.contains("money")) || (uwentry.contains("river"))
					|| (uwentry.contains("person"))
					|| (uwentry.contains("weapon"))
					|| (hwentry.contains("place"))) {
				// ////////System.out.println("Offer Sense:"+uwentry);
				fentry.add(sword);
				fentry.add(hwlist.get(i).toString());
				fentry.add(uwlist.get(i).toString());
			}
		}
	}

	public void MonthSense(String sword) {
		for (int i = 0; i < uwlist.size(); i++) {
			uwentry = uwlist.get(i).toString();
			if (uwentry.contains("date")) { // || (uwentry.contains("day")) ){
				// ////////System.out.println("month Sense:"+uwentry);
				fentry.add(sword);
				fentry.add(hwlist.get(i).toString());
				fentry.add(uwentry);
			}
		}
	}

	public void RiverSense(String sword) {
		// System.out.println("INSIDE RIVER");
		int ind = rootlist.indexOf(sword);
		for (int i = ind; i < uwlist.size(); i++) {
			uwentry = uwlist.get(i).toString();
			// System.out.println("INSIDE RIVER uwEntry:"+uwentry);
			if ((uwentry.contains("natural world"))
					|| (uwentry.contains("day"))) {
				// System.out.println("river Sense:"+uwentry);
				fentry.add(sword);
				fentry.add(hwlist.get(i).toString());
				fentry.add(uwentry);
				break;
			}
		}// System.out.println("RIVER fentry:"+fentry);
	}

	public void UnitSense(String sword) {
		int k = rootlist.indexOf(sword);
		// //System.out.println("Unit K:"+k);
		for (int i = k; i < uwlist.size(); i++) {
			uwentry = uwlist.get(i).toString();
			hwentry = hwlist.get(i).toString();
			if ((uwentry.contains("unit")) || (uwentry.contains("number"))
					|| (uwentry.contains("measurement"))
					|| (uwentry.contains("time"))
					|| (uwentry.contains("mountain"))
					|| (hwentry.contains("first"))) {
				// //System.out.println("unit Sense:"+uwentry);
				fentry.add(sword);
				fentry.add(hwlist.get(i).toString());
				fentry.add(uwentry);
			}
		}
	}

	public void StyleSense(String sword) {
		// ////////System.out.println("Sword::uwentry:"+sword);
		int ind = rootlist.indexOf(sword);
		for (int i = ind; i < uwlist.size(); i++) {
			uwentry = uwlist.get(i).toString();
			if ((uwentry.contains("style")) || (uwentry.contains("number"))
					|| (uwentry.contains("book"))
					|| (uwentry.contains("document"))) {
				// ////////System.out.println("style or number Sense:"+uwentry);
				fentry.add(sword);
				fentry.add(hwlist.get(i).toString());
				fentry.add(uwentry);
			}
		}
	}

	public void GodPersonSense(String sword) {
		int ind = rootlist.indexOf(sword);
		for (int i = ind; i < uwlist.size(); i++) {
			uwentry = uwlist.get(i).toString();
			// int ind = rootlist.indexOf(sword);
			if ((uwentry.contains("god")) || (uwentry.contains("goddess"))) {
				// //System.out.println("god Sense:"+uwentry);
				fentry.add(sword);
				fentry.add(hwlist.get(i).toString());
				fentry.add(uwlist.get(i).toString());
			}
		}
	}

	public void PersonSense(String sword) {
		int ind = rootlist.indexOf(sword);
		for (int i = ind; i < uwlist.size(); i++) {
			uwentry = uwlist.get(i).toString();
			// int ind = rootlist.indexOf(sword);
			if (uwentry.contains("person")) {
				// //System.out.println("person Sense:"+uwentry);
				fentry.add(sword);
				fentry.add(hwlist.get(i).toString());
				fentry.add(uwlist.get(i).toString());

			}
		}
	}

	public void ColorSense(String sword) {
		int ind = rootlist.indexOf(sword);
		for (int i = ind; i < uwlist.size(); i++) {
			uwentry = uwlist.get(i).toString();
			if (uwentry.contains("color")) {
				// ////////System.out.println("color Sense:"+uwentry);
				fentry.add(sword);
				fentry.add(hwlist.get(i).toString());
				fentry.add(uwentry);
			}
		}
	}

	public void MetalSense(String sword) {
		int ind = rootlist.indexOf(sword);
		for (int i = ind; i < uwlist.size(); i++) {
			uwentry = uwlist.get(i).toString();
			if (uwentry.contains("metal")) {
				// ////////System.out.println("color Sense:"+uwentry);
				fentry.add(sword);
				fentry.add(hwlist.get(i).toString());
				fentry.add(uwentry);
			}
		}
	}

	public void getFirsthit(String sword) {
		int hc = sword.hashCode();
		// ////////System.out.println("Inside getFirsthhit");
		if ((bn = dict.search(hc)) != null) {
			fentry.add(sword);
			fentry.add(bn.headword);
			fentry.add(bn.constraint);
		}
	}

	public ArrayList<String> RulesSet(String sword, int j) {

		int k = j;

		curranal = anal.get(k).toString();
		if (k > 0) {
			prevconcept = root.get(k - 1).toString();
			prevanal = anal.get(k - 1).toString();

			prevhw = hw.get(k - 1).toString();
			prevuw = uw.get(k - 1).toString();
		}
		if (k < (root.size() - 1)) {

			nextanal = anal.get(k + 1).toString();
			nextconcept = root.get(k + 1).toString();
			getNextUW();
		}
		if (k >= 0) {
			if (curranal.indexOf("Verb") != -1) {
				VerbRule(sword);
			} else if ((prevanal != null) || (prevuw != null)
					|| (prevconcept != null)) {

				if (prevuw.contains("number")) {
					RiverSense(sword);
				} else if ((prevuw.contains("person"))) {
						if(nextuw.contains("person")){
							PersonSense(sword);							
						}else{
							PersonSense(sword);							
						}
				} else if ((prevuw.contains("god"))
						|| (prevuw.contains("goddess"))) {
					if ((nextuw.contains("god"))
							|| (nextuw.contains("goddess"))) {
							GodPersonSense(sword);
					}else{
						GodPersonSense(sword);
					}
				} else if ((prevuw.contains("document"))
						|| (prevuw.contains("book"))
						|| (prevuw.contains("language"))) {
					StyleSense(sword);
				} else if (prevanal.indexOf("ஆவது") != -1) {
					PositionSense(sword);
				} else if ((prevuw.indexOf("food") != -1)
						|| (prevuw.indexOf("fruit") != -1)
						|| (prevhw.indexOf("fruit") != -1)
						|| (prevhw.indexOf("food") != -1)) {
					if ((nextuw.indexOf("food") != -1)
							|| (nextuw.indexOf("fruit") != -1)
							|| (nexthw.indexOf("fruit") != -1)
							|| (nexthw.indexOf("food") != -1)) {
						OfferSense(sword);
					}else{	
						OfferSense(sword);
					}	
				} else if ((prevuw.indexOf("metal") != -1)
						|| (prevhw.indexOf("metal") != -1)) {
					if ((nextuw.indexOf("metal") != -1)
							|| (nexthw.indexOf("metal") != -1)) {
						MetalSense(sword);
					}else{	
						MetalSense(sword);
					}	
				} else if (prevuw.indexOf("date") != -1) {
					MonthSense(sword);
				} else if ((prevanal.indexOf("Numbers") != -1)
						|| (prevanal.contains("charNumbers"))) {
					RiverSense(sword);
					UnitSense(sword);
				} else if ((prevanal.indexOf("Entity") != -1)
						|| (prevuw.contains("number"))
						|| (prevuw.contains("period"))
						|| (prevuw.contains("building"))
						|| (prevhw.contains("place"))) {
					RiverSense(sword);
					PlaceSense(sword);
				} else if ((prevuw.contains("color"))) {
					if ((nextuw.contains("color"))) {
						ColorSense(sword);
					} else{
						ColorSense(sword);
					}	
				} else if (prevconcept.equals("பெரிய")) {
					RiverSense(sword);
				}
			} else if(nextanal != null){
				if (nextanal.contains("Plural Suffix")) {

					NumberSense(sword);

					nextanal = "";
				}
			} else {
				int hc = sword.hashCode();

				if ((bn = dict.search(hc)) != null) {
					fentry.add(sword);
					fentry.add(bn.headword);
					fentry.add(bn.constraint);
				}

			}
		}
		return fentry;
	}
	public void getNextUW(){
		int hc = nextconcept.hashCode();
		if( (bn = dict.search(hc))!=null){
			nexthw = bn.headword;
			nextuw = bn.constraint;
				
		}
	}

	public String findverbnumber(String v) {
		int tempindex;
		int totalrw;

		int totrw = root.size();

		try {
			tempindex = root.indexOf(v.trim());

			if (tempindex != -1)
				return (no.get(tempindex).toString().trim());

		}

		catch (Exception e) {

		}
		return ("0");
	}

	// To check the Multiwords in the given document.

	public void multiwordCheck(String templex, MWBSTNode bn_mw, int cnt,
			int now, String pss, String analmod) {
		String lex = bn_mw.lexeme;

		if (lex.equals(templex)) {
			mwword = lex;
			for (int p = 1; p <= cnt; p++) {
				wordAL.remove(wordAL.size() - 1);
				anal.remove(anal.size() - 1);
				root.remove(root.size() - 1);
				pos.remove(pos.size() - 1);
			}
			wordAL.add(bn_mw.lexeme);
			root.add(bn_mw.lexeme);
			multisplit.add(bn_mw.lexeme);
			pos.add(pss);
			// System.out.println("multisplit1:"+multisplit);
			anal.add(analmod);
			// //System.out.println("anal:"+analmod);
			tempcmtw.add(bn_mw.lexeme);
			tempcmew.add(bn_mw.headword);
			tempcmuw.add(bn_mw.constraint);

			return;
		} else {
			if (bn_mw.next == null) {
				return;
			}
			// if more than one mwe contains same fw
			else {
				int tok_cnt = now;

				while (bn_mw.next != null) {
					MWBSTNode temp = new MWBSTNode();
					String next_lex = " ";
					temp = bn_mw.next;
					bn_mw = bn_mw.next;

					int next_cnt = temp.no_words;
					StringBuffer sb_mw = new StringBuffer();

					if (next_cnt == tok_cnt) {
						next_lex = temp.lexeme;
					} else if (next_cnt < tok_cnt) {
						String temp_lexeme = "";
						next_lex = temp.lexeme;

						while (next_cnt != tok_cnt) {
							int index = templex.lastIndexOf(" ");
							temp_lexeme = templex.substring(0, index).trim();
							sb_mw.insert(0, " "
									+ templex.substring(index + 1, templex
											.length()));

							templex = temp_lexeme;
							tok_cnt = tok_cnt - 1;
						}
					}
					if (next_lex.equals(templex)) {
						mwword = next_lex;
						for (int p = 1; p <= cnt; p++) {
							wordAL.remove(wordAL.size() - 1);
							root.remove(root.size() - 1);
							pos.remove(pos.size() - 1);
							anal.remove(anal.size() - 1);
						}
						wordAL.add(temp.lexeme);
						root.add(temp.lexeme);
						multisplit.add(temp.lexeme);
						anal.add(analmod);
						pos.add(pss);
						// System.out.println("multisplit2:"+multisplit);
						tempcmtw.add(temp.lexeme);
						tempcmew.add(temp.headword);
						tempcmuw.add(temp.constraint);

						// to add remaining words in the framed temporary muli
						// words

						if (sb_mw.length() > 1) {
							StringTokenizer st1_mw = new StringTokenizer(sb_mw
									.toString());
							while (st1_mw.hasMoreTokens()) {
								String temp_tok;
								temp_tok = st1_mw.nextToken();
								root.add(temp_tok);
								wordAL.add(temp_tok);
								anal.add(analmod);
								pos.add(pss);
							}
							sb_mw.delete(0, sb_mw.length());
						}
						return;
					}
				}
			}
			return;
		}
	}	
	public void chkIfPerson(){
		for(int i = 0;i<RefExp.size();i++){
			String chkWord = RefExp.get(i).toString();		
		//	String chkPos = pos.get(i).toString();
			int hc = chkWord.hashCode();
			if( (bn = dict.search(hc))!=null){
				if(bn.constraint.contains("person")){					
					personUW.add(bn.lexeme+"/Entity/"+bn.constraint);
				}
					
			}
		}
	}
	public void addMorphology(String analmod){
		int k = anal.indexOf(analmod);
	//	System.out.println("k:"+k);
		String[] chkanal = analmod.split("400");
	//	System.out.println("chkanal:"+chkanal[0]+":"+chkanal[1]);
		String addPOS = "Entity & 50 >"+" "+"இல்"+" "+"< Locative Case & 504"+chkanal[1];
		anal.remove(k);
		anal.add(k, addPOS);
	//	System.out.println("anal:"+anal);
	}
	public void processPronoun(String word1, int ctnoofwords, String pss,
			String analmod) {
		pr_res = true;
		pr = word1;
		pr_index = ctnoofwords;
		root.add(firstword);
		pos.add(firstwordpss);
		StringTokenizer pr_tok = new StringTokenizer(firstword);
		if (pr_tok.countTokens() > 1) {

			int now = pr_tok.countTokens();
			int hc = (pr_tok.nextToken()).hashCode();

			if ((bn_mw = dict_mw.search(hc)) != null) {
				multiwordCheck(firstword, bn_mw, 1, now, pss, analmod);
			}

		}
	}

	/**
	 * Various Rules are defined for UNL Relations. Rule1 is defined for the UNL
	 * relations: frm, plf, src, tmf. Based on the UNL Constraints the relations
	 * will vary. These relations are obtained by applying the Rule: <"இல்",
	 * "ஆக", "இடம்">-> <"இருந்து">
	 */
	public void rule1() {
		
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
					findRelation_Category5(j + 1, j, rel_label);
				}
			} else {
				plfindex = j;
				rootrel.set(j, "plf" + "src");
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
				} else if (u_word.indexOf("time") != -1) {
					rel_label = "tmf";
				} else if (u_word.indexOf("event") != -1) {
					rel_label = "scn";
				} else {
					// rel_label = "src";
				}
				findRelation_Category2(j, rel_label);
			}
		}else{
			rootrel.set(j, "frm");
			rel_label = "frm";
			findRelation_Category2(j, rel_label);
		}
	}

	/**
	 * Rule2 is defined for the UNL Relation: 'and'. The 'and' relation is
	 * obtained by the following rules applied: "மட்டுமல்லாமல்", "மற்றும்",
	 * "மேலும்", "உம்".
	 * 
	 */

	public void rule2() // mattumallamal
	{
		// flag2=1;
		rootrel.set(j, "and");
		rel_label = "and";
		findRelation_Category1(j, rel_label);
	}

	/**
	 * Rule3 is defined for the UNL Relation: ' via' The 'via'relation is
	 * obtained by applying the Rule: "வழியாக"
	 */

	public void rule3() // Valiyaga
	{
		rootrel.set(j, "via");
		// rel_label = "via";
		// findRelation_Category4(j, rel_label);
		u_word = uw.get(j - 1).toString();
		if ((u_word.indexOf("place") != -1) || (u_word.indexOf("city") != -1)
				|| (u_word.indexOf("district") != -1)
				|| (u_word.indexOf("organization") != -1)
				|| (u_word.indexOf("temple") != -1)
				|| (u_word.indexOf("facilities") != -1)
				|| (u_word.indexOf("country") != -1)
				|| (u_word.indexOf("lake") != -1)) {

			rel_label = "via";
			// rootrel.set(j, "via");
			findRelation_Category4(j, rel_label);
		}

	}

	/**
	 * Rule4 is defined for the UNL Relation: 'and' 'ptn'. The 'and' and 'ptn'
	 * relations are obtained by the following Rule: "உம்" and the position of
	 * the analysed string.
	 */

	public void rule4() {

		String pos1 = pos.get(j).toString();
		if ((j + 1) != total) {
			String pos2 = pos.get(j + 1).toString();
			String anal2 = anal.get(j + 1).toString().trim();
			if ((pos1.equals(pos2)) && (anal2.indexOf("உம்") != -1)) {
				rootrel.set(j, "and" + "ptn");
				rel_label = "ptn";
				findRelation_Category3(j + 1, rel_label);

				rel_label = "and";
				findRelation_Category5(j + 1, j, rel_label);
			}
		}
	}

	/**
	 * Rule5 is defined for the UNL Realtion: 'agt'. The 'agt' relation is
	 * obtained by checking the Instrumental case (i.e. 'ins'relation) The Rule
	 * for 'agt' reltion is "படு","அது". The "insindex" Rule is "ஆல்".
	 */

	public void rule5() {

		if (insindex == 1) {
			int i = rootrel.indexOf("ins");
			u_word = uw.get(i).toString();
			if ((u_word.indexOf("person") != -1)
					|| (u_word.indexOf("name") != -1)
					|| (u_word.indexOf("relative") != -1)) {
				rootrel.set(j, "agt");
				rel_label = "agt";
				findRelation_Category2(i, rel_label);
			}
		}
	}

	/**
	 * Rule6 is defined for the UNL Relations: 'con', 'ins', 'met'. These
	 * relations are obtained by applying the Rule: "ஆல்" The insindex is set
	 * only when the concept is instrument, flower, vechicle etc.. The 'con'
	 * relation is obtained by checking with the verb. And if the concept is a
	 * person, name or relative then the 'met' relation is obtained.
	 */

	public void rule6() {

		if (pos.get(j).toString().equals("Verb")) {
			rootrel.set(j, "con");
			rel_label = "con";
			findRelation_Category1(j, rel_label);
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
				findRelation_Category2(j, rel_label);
			} else if ((u_word.indexOf("person") != -1)
					|| (u_word.indexOf("name") != -1)
					|| (u_word.indexOf("relative") != -1)) {
				rel_label = "met";
				findRelation_Category2(j, rel_label);
			}
		}
	}

	/**
	 * Rule7 is defined for the UNL Relation: 'plt'. The 'plt' Relation is
	 * obtained by applying the Rule: "க்கு"
	 */

	public void rule7() {
		rootrel.set(j, "plt");
		rel_label = "plt";
		findRelation_Category1(j, rel_label);
	}

	/**
	 * Rule8 is defined for the UNL Relation: 'bas'. The 'bas' relation is
	 * obtained by applying the Rule: <"காட்டிலும்", "விட">
	 */

	public void rule8() {

		rootrel.set(j, "bas");
		rel_label = "bas";
		findRelation_Category1(j, rel_label);
	}

	/**
	 * Rule9 is defined for the UNL Relation: 'ben'. The 'ben' relation is
	 * obtained by applying the Rule:"உக்கு", "ஆக" By Checking the UW word
	 * constraint the relation is set to the corresponding concepts.
	 */

	public void rule9() {

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
			findRelation_Category2(j, rel_label);
		}
	}

	/**
	 * Rule10 is defined for the UNL Realtions: 'cag', 'cao', 'cob'. These
	 * reltions are obtained by setting the codindex and By applying the Rule:
	 * "உடன்", "ஓடு".
	 */

	public void rule10() {

		// int cobindex;

		rootrel.set(j, "cag" + "cao" + "cob");
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
			findRelation_Category3(j, rel_label);
		} else {
			rel_label = "cao";
			findRelation_Category5(j + 1, j, rel_label);
		}
	}

	/**
	 * Rule11 is defined for the UNL Relation: 'cnt'. The 'cnt' Relation is
	 * obtained by applying the Rule: "என்பது".
	 */

	public void rule11() {

		rootrel.set(j, "cnt");
		rel_label = "cnt";
		findRelation_Category1(j, rel_label);
	}

	/**
	 * Rule12 is defined for the UNL Relation: 'coo'. The 'coo' relation is
	 * obtained by applying the Rule:<"கொண்டே", "அதேவேளை">
	 */

	public void rule12() {

		rootrel.set(j, "coo");
		rel_label = "coo";
		findRelation_Category1(j, rel_label);
	}

	/**
	 * Rule13 is defined for the UNL Relation: 'dur'. The 'dur' relation is
	 * obtained by applying the Rule: <"பொழுது", "போது">
	 */

	public void rule13() {
		rootrel.set(j, "dur");
		rel_label = "dur";
		findRelation_Category4(j, rel_label);
	}

	/**
	 * Rule14 is defined for the UNL Realtion: 'equ'. The 'equ' relation is
	 * obtained by applying the Rule: "என்னும்".
	 */

	public void rule14() {
		rootrel.set(j, "equ");
		for (int i = 0; i < uw.size(); i++) {
			u_word = uw.get(i).toString();
			if (u_word.indexOf("iof>place") != -1) {
				rel_label = "equ";
				findRelation_Category5(j + 1, i, rel_label);
			}

		}
	}

	/**
	 * Rule15 is defined for the UNL Relation: 'plf', 'tmf'. These Relations are
	 * obtained by applying the following Rules: <"முதல்", "இருந்து">
	 */
	public void rule15() {
		rootrel.set(j, "plf");
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
				findRelation_Category2(i, rel_label);
				break;
			} else if ((u_word.indexOf("month") != -1)
					|| (u_word.indexOf("time") != -1)) {
				plfindex = i;
				rel_label = "tmf";
				findRelation_Category2(i, rel_label);
				break;
			}
		}
		/**
		 * if(plfindex > 0) { rel_label = "fmt";
		 * findRelation_Category5(plfindex,i,rel_label); }
		 */
	}

	/**
	 * Rule16 is defined for the UNL Relation: 'plt', 'gol', 'tmt'. These
	 * relations are obtained by applying the Rules: "க்கு".
	 */

	public void rule16() {

		rootrel.set(j, "plt" + "gol");
		u_word = uw.get(j).toString();
		if ((u_word.indexOf("place") != -1) || (u_word.indexOf("city") != -1)
				|| (u_word.indexOf("district") != -1)
				|| (u_word.indexOf("organization") != -1)
				|| (u_word.indexOf("temple") != -1)
				|| (u_word.indexOf("facilities") != -1)
				|| (u_word.indexOf("country") != -1)
				|| (u_word.indexOf("lake") != -1)) {
			rel_label = "plt";
		} else if ((u_word.indexOf("time") != -1)
				|| (u_word.indexOf("month") != -1)) {
			rel_label = "tmt";
		} else {
			rel_label = "gol";
		}
		findRelation_Category2(j, rel_label);
	}

	/**
	 * Rule17 is defined for the UNL Relation: 'plt' The 'plt' relation is
	 * obtained by applying the Rule: "வரை"
	 */
	public void rule17() {

		rootrel.set(j, "plt");

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
				findRelation_Category2(i, rel_label);
				break;
			} else if ((u_word.indexOf("time") != -1)
					|| (u_word.indexOf("month") != -1)) {
				rel_label = "tmt";
				findRelation_Category2(i, rel_label);
				break;
			}
		}
		/**
		 * if(plfindex > 0) { rel_label = "fmt";
		 * findRelation_Category5(plfindex,i,rel_label); }
		 */
	}

	/**
	 * Rule18 is defined for the UNL Relation: 'ins', 'obj'. These relations are
	 * obtained by applying the Rule: "ஐ"
	 */

	public void rule18() {
		rootrel.set(j, "ins" + "obj");
		u_word = uw.get(j).toString();
		if ((u_word.indexOf("instrument") != -1)
				|| (u_word.indexOf("vehicle") != -1)
				|| (u_word.indexOf("stationery") != -1)) {
			rel_label = "ins";
		} else {
			rel_label = "obj";
		}
		findRelation_Category2(j, rel_label);
	}

	/**
	 * Rule19 is defined for the UNL Relation: 'plc' The 'plc' relation is
	 * obtained by applying the Rule: "இங்கு", "அங்கு" with the Pronoun Checking
	 * is true.***
	 */
	public void rule19() {
		rootrel.set(pr_index, "plc");
		rel_label = "plc";
		findRelation_Category4(j, rel_label);
	}

	/**
	 * Rule20 is defined for the UNL Relation: 'pos' The 'pos' relation is
	 * obtained by applying the Rule:"இன்"
	 */
	public void rule20() {
		rootrel.set(j, "pos");
		rel_label = "pos";
		findRelation_Category5(j + 1, j, rel_label);
		// for(int i=j+1;i<total;i++)
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
					|| (u_word.indexOf("lake") != -1)) {
				rootrel.set(i, pos);
				rel_label = "pos";
				findRelation_Category5(i, j, rel_label);
			} else {
				// break;
			}
		}
		i++;
	}

	/**
	 * Rule21 is defined for the UNL Relation: 'ins', 'met'. These Relations are
	 * obtained by applying the Rules:<"மூலம்","வைத்து","உபயோகி","கொண்டு">
	 */
	public void rule21() {
		rootrel.set(j, "ins");
		u_word = uw.get(j - 1).toString();
		if ((u_word.indexOf("instrument") != -1)
				|| (u_word.indexOf("flower") != -1)
				|| (u_word.indexOf("vehicle") != -1)
				|| (u_word.indexOf("stationery") != -1)) {
			rel_label = "ins";
		} else {
			rel_label = "met";
		}
		findRelation_Category4(j, rel_label);
	}

	/**
	 * Rule22 is defined for the UNL Relation: 'int'. The 'int' relation is
	 * obtained by applying the Rule: "பொது".
	 */
	public void rule22() {

		rootrel.set(j, "int");
		rel_label = "int";
		findRelation_Category5(j - 1, j - 2, rel_label);
	}

	/**
	 * Rule23 is defined for the UNL Relation: 'man'. The 'man' relation is
	 * obtained by applying the Rule:"மிக"
	 */

	public void rule23() {
		rootrel.set(j, "man");
		rel_label = "man";
		findRelation_Category5(j + 1, j, rel_label);
	}

	/**
	 * Rule24 is defined for the UNL Relation: 'man' by appling the Rule:"இங்கு"
	 * with the Adverb.
	 */
	public void rule24() {
		rootrel.set(j, "man");
		rel_label = "man";
		findRelation_Category2(j, rel_label);
	}

	/**
	 * Rule25 is defined for the UNL Relation: 'mod' by applying the Rule:
	 * Adjective comes as a concept.
	 */

	public void rule25() {
		rootrel.set(j, "mod");
		rel_label = "mod";
		findRelation_Category5(j + 1, j, rel_label);
	}

	/**
	 * Rule26 is defined for the UNL Relation: 'nam' by applying the Rules:<"ஆகிய","போன்ற","முதலிய">
	 * and by checking the next word equals <"இடம்","நாள்","நாடு">
	 */
	public void rule26() {
		String w1 = root.get(j + 1).toString().trim();

		if ((w1.equals("இடம்")) || (w1.equals("நாள்")) || (w1.equals("நாடு"))) {
			rootrel.set(j, "nam");
			h_word = hw.get(j + 1).toString();
			// ////////System.out.println("h_word:1" + h_word);
			for (int i = 0; i < uw.size(); i++) {
				u_word = uw.get(i).toString();
				// ////////System.out.println("u_word:1" + u_word);
				if (u_word.indexOf(h_word) != -1) {
					rel_label = "nam";
					findRelation_Category5(j + 1, i, rel_label);
				}

			}
		}
	}

	/**
	 * Rule27 is defined for the UNL Relation: 'or'. The Rules applied for the
	 * 'OR' relation is:"அல்லது"
	 */
	public void rule27() {
		rootrel.set(j, "or");
		rel_label = "or";
		findRelation_Category1(j, rel_label);
	}

	public void rule28() {
		rootrel.set(j, "per");
		rel_label = "per";
		findRelation_Category4(j, rel_label);
	}

	public void rule29() {
		if ((pos.get(j).toString().equals("Noun"))
				|| (pos.get(j).toString().equals("Entity"))) {
			rootrel.set(j, "pos");
			rel_label = "pos";
			findRelation_Category5(j + 1, j, rel_label);
		} else {
			aojindex = j;
			rel_label = "aoj";
		}
	}

	public void rule30() {
		if (pos.get(j).toString().equals("Verb")) {
			rootrel.set(j, "pur");
			rel_label = "pur";
			findRelation_Category2(j, rel_label);
		}
	}

	public void rule31() {
		rootrel.set(j, "qua");
		rel_label = "qua";
		findRelation_Category5(j + 1, j, rel_label);
	}

	public void rule32() {
		rootrel.set(j, "rsn");
		rel_label = "rsn";
		findRelation_Category4(j, rel_label);
	}

	/**
	 * rule33 is defined for the UNL Relation: 'seq'. The 'seq' relation is
	 * obtained by applying the Rule:
	 */
	public void rule33() {
		rootrel.set(j, "seq");
		rel_label = "seq";
		findRelation_Category1(j, rel_label);
	}

	public void rule34() {
		timindex = 1;
		rootrel.set(j - 1, "tim");
		u_word = uw.get(j - 1).toString();
		if (u_word.indexOf("number") != -1) {
			rel_label = "tim";
			findRelation_Category4(j, rel_label);
		}
	}

	public void rule35() {
		rootrel.set(j, "iof");
		h_word = hw.get(j).toString();
		for (int i = 0; i < uw.size(); i++) {
			u_word = uw.get(i).toString();
			if (u_word.indexOf(h_word) != -1) {
				rel_label = "iof";
				findRelation_Category5(j, i, rel_label);
			}else{
				break;
			}
		}
	}

	public void rule36() {
		rootrel.set(j, "pof");
		h_word = hw.get(j).toString();
		for (int i = 0; i < uw.size(); i++) {
			u_word = uw.get(i).toString();
			if (u_word.indexOf(h_word) != -1) {
				rel_label = "pof";
				findRelation_Category5(j, i, rel_label);
			}
		}
	}

	/**
	 * Rule37 is defined for the UNL Relation: 'aoj'. The 'aoj' relation is
	 * obtained by applying the Rules:
	 * <"உள்ளது","இருக்கும்","உள்ளன","நிறைந்தது","ஆகும்","உண்டு", "பற்றி",
	 * "பற்றிய">
	 */
	public void rule37() {
		// flag3=1;
		aojindex = j;
		rootrel.set(j, "aoj");
		rel_label = "aoj";
		findRelation_Category4(j, rel_label);
	}

	public void rule38() {

		rootrel.set(j, "plc");
		u_word = uw.get(j).toString();
		if (u_word.indexOf("pof<body") != -1) {
			rel_label = "opl";
			findRelation_Category2(j, rel_label);
		} else if ((u_word.indexOf("place") != -1)
				|| (u_word.indexOf("city") != -1)
				|| (u_word.indexOf("district") != -1)
				|| (u_word.indexOf("organization") != -1)
				|| (u_word.indexOf("temple") != -1)
				|| (u_word.indexOf("facilities") != -1)
				|| (u_word.indexOf("country") != -1)
				|| (u_word.indexOf("lake") != -1)) {

			rel_label = "plc";
			findRelation_Category2(j, rel_label);
		} else if (u_word.indexOf("time") != -1) {
			rel_label = "tim";
			findRelation_Category2(j, rel_label);
		}
	}

	/**
	 * Rule39 is defined for the UNL Relation: 'neg'. The 'neg' relation is
	 * obtained by applying the Rules:<"அல்லாத","நீங்கலாக">
	 */
	public void rule39() {
		rootrel.set(j, "neg");
		rel_label = "neg";
		findRelation_Category4(j, rel_label);
	}

	/**
	 * Rule40
	 * 
	 */

	public void rule40() {

	}

	/**
	 * Rule41 is defined for the UNL Relation: 'icl'. The 'icl' relation is
	 * obtained by applying the Rule: <"உள்ளிட்ட","உள்பட","உட்பட">
	 */
	public void rule41() {
		rootrel.set(j, "icl");
		rel_label = "icl";
		findRelation_Category4(j, rel_label);
	}

	/**
	 * Rule42 is defined for the UNL Relation: 'obj'. The 'obj' relation is
	 * obtained by applying the Rules:<"என","என்று">
	 */

	public void rule42() {
		rootrel.set(j, "obj");
		rel_label = "obj";
		findRelation_Category4(j, rel_label);
	}

	/**
	 * Rule43 is defined for the UNL Relation: 'int'. The 'int' relation is
	 * obtained by applying the Rule:<"இடையில்","இடையே>
	 */
	public void rule43() {
		rootrel.set(j, "int");
		rel_label = "int";
		findRelation_Category4(j, rel_label);
	}

	/**
	 * Rule44 is defined for the UNL Relation: 'ao if ((u_word.indexOf("place") != -1)
				|| (u_word.indexOf("city") != -1)
				|| (u_word.indexOf("district") != -1)
				|| (u_word.indexOf("organization") != -1)
				|| (u_word.indexOf("temple") != -1)
				|| (u_word.indexOf("facilities") != -1)
				|| (u_word.indexOf("country") != -1)
				|| (u_word.indexOf("lake") != -1)) {j'. The 'aoj' relation is
	 * obtained by applying the Rule:
	 */
	/**
	 * public void Rule44() { rootrel.set(j, "aoj"); rel_label = "aoj";
	 * findRelation_Category4(j, rel_label); }
	 */

	public void rule45() {
		for(int i=j+1;i<root.size();i++){
		//	rootrel.set(i, "plt");
			String pltWord = uw.get(i).toString();
			 if ( (pltWord.contains("place") )
				|| (pltWord.contains("city") )
				|| (pltWord.contains("district") )
				|| (pltWord.contains("organization") )
				|| (pltWord.contains("temple") )
				|| (pltWord.contains("facilities") )
				|| (pltWord.contains("country") )
				|| (pltWord.contains("lake") )
				|| (pltWord.contains("river") )  ) {
			//	System.out.println("pltWord:"+pltWord);
			//	System.out.println("Position:"+i+":"+j);
				rel_label = "plt";
				findRelation_Category5(i,j,rel_label);
			//	break;
			}
		}
	}

	/**
	 * Rule46 is defined for the UNL Relation: 'seq'. The 'seq' relation is
	 * obtained by applying the Rule:
	 */

	/**
	 * public void Rule46() { rootrel.set(j, "seq"); rel_label = "seq";
	 * findRelation_Category1(j, rel_label); }
	 */
	/**
	 * Rule47 is defined for the UNL Relation: 'coo'. The 'coo' relation is
	 * obtained by applying the Rule: "போதிலும்".
	 */
	public void rule47() {
		rootrel.set(j, "coo");
		rel_label = "coo";
		findRelation_Category1(j, rel_label);
	}
	
	/**
	 * Rule49 is defined for the UNL Relation: 'plt'. The 'plt' relation is
	 * obtained by checking the uword constraint: "place". Even if the 'plf'
	 * will obtained by <"இல்","இருந்து">, its not necessary that when the 'plt'
	 * relation will comes with <"க்கு">.
	 */
	public void rule49() {

		u_word = uw.get(j).toString();
		if (u_word.indexOf("place") != -1) {
			rootrel.set(j, "plt");
			int i;
			if ((j + 1) != total) {
				if (analysed.indexOf("இல்") != -1) // il
				{
					if ((analysed.indexOf("இருந்து") != -1)) // irundhu
					{
						rootrel.set(j, "plt");
						rel_label = "plt";
						findRelation_Category4(j, rel_label);
					}
				}
			}
		}
	}

	/**
	 * Rule50 is defined for the UNL Relation: 'pof'. The 'pof' relation is
	 * obtained by applying the Rule:"number". If the number street no.,door
	 * no.,etc. The condition is checked for the number not comes as a year,
	 * date, etc..
	 */
	public void rule50() {
		if ((j - 1) != total) {
			u_word = uw.get(j - 1).toString();
			if (u_word.indexOf("place") != -1) {
				tempword = root.get(j + 1).toString().trim();

				if (!((tempword.equals("ஆம்")) || (tempword.equals("இல்"))
						|| (tempword.equals("ம்")) || (tempword.equals("ல்")))) {
					rootrel.set(j, "pof");
					rel_label = "pof";
					findRelation_Category5(j, j - 1, rel_label);
				}
			}
		}
		/**
		 * if ((j - 2) != total) { tempword = root.get(j - 2).toString(); //
		 * ////////System.out.println("BALA TEMPWORD TELEPHONE"); if
		 * (!((tempword.equals("அறை")) || (tempword .equals("வண்டி")))) { //
		 * ////////System.out.println("JI JI JI"); rootrel.set(j, "pos"); //
		 * rel_label="pos"; // findRelation_Category5(j,j-2,rel_label); } }
		 */
	}

	/**
	 * Rule53 is defined for the UNL Relation: 'pos'. The 'pos' relation is
	 * obtained by applying the Rule: <"க்கு","உரிய">,"உள்ள".
	 */

	public void rule53() {
		rootrel.set(j, "pos");
		rel_label = "pos";
		findRelation_Category5(j + 1, j, rel_label);
	}

	/**
	 * Rule54 is defined for the UNL Relation: 'pof'. The 'pof' relation is
	 * obtained by applying the Rule:"உட்பட்ட".
	 */
	public void rule54() {
		rootrel.set(j, "pof");
		rel_label = "pof";
		findRelation_Category2(j, rel_label);
	}

	public void rule55() {
		rootrel.set(j, "mod");
		String uword1 = anal.get(j + 1).toString();
		if ((uword1.indexOf("person") != -1) || (uword1.indexOf("god") != -1)
				|| (uword1.indexOf("goddess") != -1)) {
			rel_label = "mod";
			findRelation_Category5(j + 1, j, rel_label);
		}
	}

	public void rule56() {
		rootrel.set(j, "pos");
		String u_word1 = uw.get(j + 1).toString();
		if ((u_word1.indexOf("facilities") != -1)
				|| (u_word1.indexOf("temple") != -1)) {
			rel_label = "pos";
			findRelation_Category5(j + 1, j, rel_label);
		}
	}

	public void findCXCfood() {
		String u_word2 = uw.get(j+1).toString();
		if(u_word2.contains("food")){
			rootrel.set(j, "cxc");
			rel_label = "cxc";
			findRelation_Category5(j + 1, j, rel_label);
		}	
	}
	public void findCXCentity() {
		String u_word2 = uw.get(j+1).toString();
		if( (u_word2.contains("place")) || (u_word2.contains("food")) || (u_word2.contains("god")) || (u_word2.contains("goddess")) ){
			rootrel.set(j, "cxc");
			rel_label = "cxc";
			findRelation_Category5(j + 1, j, rel_label);
		}	
	}

	public void findrelnindex() // throws Exception
	{

		j = 0;

		String analysed;
		String tempword;
		// System.out.println("Anal inside Findrelnindex:"+anal);

		String rel_label;
		String h_word = " ";
		String u_word = " ";

		total = anal.size();
		result += "[r]#";
		while (j < total) {
			try {
				// ////////System.out.println("j:"+j);
				rootrel.add("None");
				analysed = anal.get(j).toString().trim();
				tempword = root.get(j).toString().trim();
				u_word = uw.get(j).toString();
				if ((analysed.indexOf("இல்") != -1)
						|| (analysed.indexOf("ஆக") != -1)
						|| (analysed.indexOf("இடம்") != -1)) {
					if ((analysed.indexOf("இருந்து") != -1)) {
					//	System.out.println("Inside il irundhu:");
						rule1();
						rule45();
					}
					if (analysed.indexOf("உள்ள") != -1) {
						rule41();
					}
					if (analysed.indexOf("உம்") != -1) {
						rule4();
					} else {
						rule38();
					}
				}
				if (analysed.indexOf("உம்") != -1) {
					rule4();
				}
				if ((analysed.indexOf("படு") != -1)
						&& (analysed.indexOf("அது") != -1)) {
					rule5();
				}
				if (analysed.indexOf("ஆல்") != -1) {
					rule6();
				}
				if (analysed.indexOf("உக்கு") != -1) {
					if (analysed.indexOf("ஆக") != -1) {
						rule9();
					}
				}
				if ((analysed.indexOf("உடன்") != -1)
						|| (analysed.indexOf("ஓடு") != -1)) {
					rule10();
				}
				if (analysed.indexOf("க்கு") != -1) {
					if (analysed.indexOf("உரிய") != -1) {
						rule53();
					}
					if (analysed.indexOf("உட்பட்ட") != -1) {
						rule54();
					}
					if (analysed.indexOf("உம்") != -1) {
						rule4();
					}
					rule16();
				}
				if (analysed.indexOf("ஐ") != -1) {
					rule18();
				}
				if (analysed.indexOf("இன்") != -1) {
					rule20();
				}
				if ((analysed.indexOf("உடைய") != -1)
						|| (analysed.indexOf("அது") != -1)) {
					rule29();
				}
				if ((analysed.indexOf("ஆக") != -1)
						|| (analysed.indexOf("க்க") != -1)) {
					rule30();
				}
				if ((tempword.equals("உள்ளது"))
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
					rule37();
				}
				if ((analysed.indexOf("என") != -1) || (tempword.equals("என"))
						|| (tempword.equals("என்று"))) {
					rule42();
				}
				if ((tempword.equals("மட்டுமல்லாமல்"))
						|| (tempword.equals("மற்றும்"))
						|| (tempword.equals("மேலும்"))) {
					rule2();
				}
				if (tempword.equals("வழியாக")) {
					rule3();
				}
				if ((tempword.equals("காட்டிலும்")) || (tempword.equals("விட"))) {
					rule8();
				}
				if (tempword.equals("என்னும்")) {
					rule14();
				}
				if ((tempword.equals("முதல்")) || (tempword.equals("இருந்து"))) {
					rule15();
				}
				if ((tempword.equals("மூலம்")) || (tempword.equals("வைத்து"))
						|| (tempword.equals("உபயோகி"))
						|| (tempword.equals("கொண்டு"))) {
					rule21();
				}
				if (tempword.equals("மிக")) {
					rule23();
				}
				if ((tempword.equals("ஆகிய")) || (tempword.equals("போன்ற"))
						|| (tempword.equals("முதலிய"))) {
					rule26();
				}
				if (tempword.equals("அதனால்")) {
					rule32();
				}
				if ((tempword.equals("அப்புறம்"))
						|| (tempword.equals("முன்னால்"))
						|| (tempword.equals("பின்னால்"))
						|| (tempword.equals("பின்னர்"))
						|| (tempword.equals("பின்பு"))) {
					rule33();
				}
				if ((tempword.equals("ஆம்")) || (tempword.equals("இல்"))
						|| (tempword.equals("ம்")) || (tempword.equals("ல்"))) {
					rule34();
				}
				if ((tempword.equals("அல்லாத"))
						|| (tempword.equals("நீங்கலாக"))) {
					rule39();
				}
				if (tempword.equals("என்பது")) {
					rule11();
				}
				if ((tempword.equals("கொண்டே")) || (tempword.equals("அதேவேளை"))) {
					rule12();
				}
				if ((tempword.equals("பொழுது")) || (tempword.equals("போது"))) {
					rule13();
				}
				if (tempword.equals("வரை")) {
					rule17();
				}
				if ((pr_res == true)
						&& ((pr.indexOf("இங்கு") != -1) || (pr.indexOf("அங்கு") != -1))) {
					rule19();
				}
				if (tempword.indexOf("பொது") != -1) {
					rule22();
				}
				if (((pr.indexOf("இங்கு") == -1))
						&& (analysed.indexOf("Adverb") != -1)) {
					rule24();
				}
				if ((analysed.indexOf("Adjective") != -1)
						|| (analysed.indexOf("Adjectival Suffix") != -1)
						|| (analysed.indexOf("Adjectival Noun") != -1)) {
					rule25();
				}
				if (tempword.equals("அல்லது")) {
					rule27();
				}
				if (tempword.equals("முறை")) {
					rule28();
				}
				if (tempword.equals("நிறைய")) {
					rule31();
				}
				if ((tempword.equals("நாடு")) || (tempword.equals("மாவட்டம்"))
						|| (tempword.equals("நகரம்"))
						|| (tempword.equals("மாநிலம்"))
						|| (tempword.equals("நதி")) || (tempword.equals("ஏரி"))
						|| (tempword.equals("பாலம்"))) {
					rule35();
				}
				if ((tempword.equals("உடல்")) || (tempword.equals("மரம்"))) {
					rule36();
				}
				if ((tempword.equals("உள்ளிட்ட")) || (tempword.equals("உள்பட"))
						|| (tempword.equals("உட்பட"))) {
					rule41();
				}
				if ((tempword.equals("இடையில்")) || (tempword.equals("இடையே"))) {
					rule43();
				}				
				if (tempword.equals("போதிலும்")) {
					rule47();
				}			
				if (u_word.indexOf("number") != -1) {
					rule50();
				}
				if (tempword.equals("உள்ள")) {
					rule53();
				}				
				if ((tempword.equals("மட்டுமல்லாமல்"))
						|| (tempword.equals("அல்லாத"))) {
					rule38();
				}
				if ((u_word.indexOf("city") != -1)
						|| (u_word.indexOf("state") != -1)
						|| (u_word.indexOf("place") != -1)
						|| (u_word.indexOf("country") != -1)
						|| (u_word.indexOf("region") != -1)
						|| (u_word.indexOf("facilities") != -1)
						|| (u_word.indexOf("temple") != -1)) {
					rule55();
				}
				if ((u_word.indexOf("person") != -1)
						|| (u_word.indexOf("god") != -1)
						|| (u_word.indexOf("goddess") != -1)) {
					rule56();
				} 
				if(u_word.indexOf("food")!=-1){
					findCXCfood();
				}
				if(u_word.indexOf("place")!=-1){
					rule49();
					findCXCentity();
				}
			} catch (Exception e) {
			}
			j++;
		}
		j = 0;
		// to find aoj,agt
		// lexical end none + aojindex - aoj
		// lexical end none + not aoj index - agt

		for (int i = 0; i < uw.size(); i++) {
			if ((rootrel != null) && (rootrel.size() > 0)
					&& (rootrel.get(i).toString()).equals("None")) {
				u_word = uw.get(i).toString();
				if ((u_word.indexOf("person") != -1)
						|| (u_word.indexOf("food") != -1)
						|| (u_word.indexOf("vehicle") != -1)
						|| (u_word.indexOf("machine") != -1)
						|| (u_word.indexOf("body") != -1)
						|| (u_word.indexOf("place") != -1)) {
					if (aojindex > 0) {
						rel_label = "aoj";
						findRelation_Category5(aojindex, i, rel_label);
						break;
					} else if (uw.indexOf("icl>occupation") != -1) {
						rel_label = "aoj";
						findRelation_Category5(uw.indexOf("icl>occupation"), i,
								rel_label);
						break;
					} else {
						/*
						 * rel_label = "agt";
						 * findRelation_Category3(i,rel_label); break;
						 */
					}
				}
			}
		}
		result += "[/r]#[/s]#";
	}

	public void findRelation_Category1(int j, String rl) {
		String concept1 = " ";
		String concept2 = " ";
		try {
			cnt1++;
			int tot = hw.size();

			if ((rl.equals("cond")) || (rl.equals("rsn"))) {

			} else {
				int size = no.size();
				if (size != (j + 1)) {
					concept1 = no.get(j + 1).toString();					
					if ((j - 1) > 0) {
						concept2 = no.get(j - 1).toString();
					} else {
						concept2 = no.get(0).toString();
					}
					if(!concept1.equals(concept2)){			
						result += concept1 + '\t' + rl + '\t' + concept2 + '#';	
					}				
				}
			}

		} catch (Exception e) {		
			e.printStackTrace();
		}
	}

	public void findRelation_Category2(int j, String rl) {
		try {
			cnt2++; // //////////////System.out.println("Inside Category2");

			String concept1 = " ";
			String concept2 = " ";

			concept1 = verbno1;
			concept2 = no.get(j).toString();
			if(!concept1.equals(concept2)){
				result += concept1 + '\t' + rl + '\t' + concept2 + '#';
			}
			// ////System.out.println("Category2:"+cnt2);
		} catch (Exception e) {
		}
	}

	public void findRelation_Category3(int j, String rl) {
		// //////////////System.out.println("Inside Category3");
		// ArrayList root= new ArrayList();;
		try {
			cnt3++;
			String concept1 = " ";
			String concept2 = " ";

			int temp_i = root.indexOf(verb1);

			// String uword = uw.get(no.indexOf(verbno1)).toString();

			String uword = uw.get(temp_i).toString();

			if ((uword.indexOf("icl>do") != -1)
					|| (uword.indexOf("agt<thing") != -1)) {

				concept1 = verbno1;
				concept2 = no.get(j).toString();
				result += concept1 + '\t' + rl + '\t' + concept2 + '#';
			} else if (cobindex == 1) {

				concept1 = verbno1;
				concept2 = no.get(j).toString();
				if(!concept1.equals(concept2)){
					result += concept1 + '\t' + "cob" + '\t' + concept2 + '#';
				}
			}// ////System.out.println("Category3:"+cnt3);
		} catch (Exception e) {
		}
	}

	public void findRelation_Category4(int j, String rl) {
		try {
			cnt4++;
			String concept1 = " ";
			String concept2 = " ";

			concept1 = verbno1;
			// //////////////System.out.println("Concept Value1"+concept1);
			if ((j - 1) > 0) {
				concept2 = no.get(j - 1).toString();
			} else {
				concept2 = no.get(0).toString();
			}
			// //////////////System.out.println("Concept Value2"+concept2);
			if(!concept1.equals(concept2)){
				result += concept1 + '\t' + rl + '\t' + concept2 + '#';
			}
			// ////System.out.println("Category4:"+cnt4);
		} catch (Exception e) {
		}
	}

	public void findRelation_Category5(int i, int j, String rl) {
		try {
			cnt5++;
			String concept1 = " ";
			String concept2 = " ";
		//	System.out.println("Inside category:"+i+":"+j+":"+rl);
			if (i > 0)
				concept1 = no.get(i).toString();
			else
				concept1 = no.get(0).toString();
			if (j >= 0)
				concept2 = no.get(j).toString();
			else {
				if (i < 0) {
					concept2 = "";
				} else
					concept2 = no.get(0).toString();
			}
			if(!concept1.equals(concept2)){
				result += concept1 + '\t' + rl + '\t' + concept2 + '#';
			}
		//	System.out.println("Category5:"+result);
		} catch (Exception e) {
		}
	}
}
