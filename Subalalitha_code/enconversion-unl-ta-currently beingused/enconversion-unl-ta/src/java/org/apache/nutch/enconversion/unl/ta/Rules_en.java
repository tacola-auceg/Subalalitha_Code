package org.apache.nutch.enconversion.unl.ta;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author root
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

//import morphanalyser.*;
public class Rules_en {


//	Porter porter;
	Analyser analyser;
	UWDict_en uwdict;
	BST_en g_dict;
	BST_en n_dict;
	BST_en adj_dict;
	BST_en adv_dict;
	BST_en be_dict;
	BST_en do_dict;
	BST_en occur_dict;
	BSTNode_en g_bstnode;
	BSTNode_en n_bstnode;
	BSTNode_en adj_bstnode;
	BSTNode_en adv_bstnode;
	BSTNode_en be_bstnode;
	BSTNode_en do_bstnode;
	BSTNode_en occur_bstnode;
	// Build_SRS srs;
	ArrayList no;
	ArrayList  root;
	ArrayList  uw;
	ArrayList  rootrel;
	ArrayList  pos;
	ArrayList  rootnew;
	ArrayList  hw;
	ArrayList  VB_List;
	ArrayList  VB_Count;
	ArrayList  word_pos;
	ArrayList  be_VerbList;
	ArrayList  be_VerbCount;

	Stack  stack1;
	Stack  stack2;
	Stack  wrdStack;
	Stack  posStack;
	Stack  phraseStack;
	int cobindex;
	int total, j = 0;

	String verbno2;
	String verbno1;

	String verb1;
	String currword;
	String rel_label;
	String u_word;
	String pos_word;
	String result = "";

	public Rules_en() {
		// srs = new Build_SRS();
	//	porter = new Porter();
		analyser = new Analyser();
		uwdict = new UWDict_en();

		uwdict.g_bst.inorder();
		uwdict.Noun_bst.inorder();
		uwdict.Adj_bst.inorder();
		uwdict.Adv_bst.inorder();
		uwdict.be_bst.inorder();
		uwdict.do_bst.inorder();
		uwdict.occur_bst.inorder();

		g_dict = uwdict.g_bst;
		n_dict = uwdict.Noun_bst;
		adj_dict = uwdict.Adj_bst;
		adv_dict = uwdict.Adv_bst;
		be_dict = uwdict.be_bst;
		do_dict = uwdict.do_bst;
		occur_dict = uwdict.occur_bst;

		// bst = new BST();
		// bstnode = new BSTNode();
	}

	public void init() {

		root = new ArrayList ();
		uw = new ArrayList ();
		no = new ArrayList();
		rootrel = new ArrayList ();
		pos = new ArrayList ();
		rootnew = new ArrayList ();
		hw = new ArrayList ();
		VB_List = new ArrayList ();
		VB_Count = new ArrayList ();
		word_pos = new ArrayList ();
		be_VerbList = new ArrayList ();
		be_VerbCount = new ArrayList ();

		stack1 = new Stack ();
		stack2 = new Stack ();
		wrdStack = new Stack ();
		phraseStack = new Stack ();
		posStack = new Stack ();

		verbno1 = "";
		verbno2 = "";

		verb1 = "";
		u_word = "";
		pos_word = "";
		rel_label = "";
		result = "[s]#";
	}

	public String enconvert(String st) {
		try {
		//	System.out.println("ST:" + st);
			init();
			String getStr = "";
			String getwrd = "";
			String getpos = "";
			String getRoot = "";
			StringTokenizer strToken1 = new StringTokenizer(st);
			StringTokenizer strToken2;
			StringTokenizer strToken3;
			int totwrds = strToken1.countTokens();
			int inlwrds = 0;
			// System.out.println("sen Size:" + totwrds);
			while (inlwrds < totwrds) {
				getStr = strToken1.nextToken();
				// System.out.println("getStr:" + getStr);
				if (getStr.startsWith("(")) {
					stack1.push("(");
					strToken2 = new StringTokenizer(getStr, "(");
					getpos = strToken2.nextToken();
					// System.out.println("getPos:" + getpos);
					posStack.push(getpos);
				} else if (getStr.endsWith(")")) {
					strToken3 = new StringTokenizer(getStr, ")");
					getwrd = strToken3.nextToken();
					// System.out.println("getwrd:" + getwrd);
					if (getwrd.endsWith(".")) {
						getwrd = getwrd.replace(".", "");
						getwrd = getwrd.toLowerCase();
					} else {
						getwrd = getwrd.toLowerCase();
					}
					String anal_word = analyser.analyseWord(getwrd);
					// System.out.println("anal_word:"+anal_word);

					String[] split_Wrd = anal_word.split("&");
					String analysed = split_Wrd[0].toString();
					// System.out.println("analysed:"+analysed);

					StringTokenizer strToken4 = new StringTokenizer(analysed,
							"#");
					getRoot = strToken4.nextToken();
					// String getanal = strToken4.nextToken();
					// System.out.println("getROOT:"+getRoot);
					if (getRoot != null) {
						wrdStack.push(getRoot);
					} else {
						wrdStack.push(getwrd);
					}
					// System.out.println("getRoot:" + getRoot);
					stack1.pop();
					root.add(wrdStack.peek().toString());
					String concatPos = posStack.peek().toString();
					posStack.pop();
					String concatResult = posStack.peek().toString() + " "
							+ concatPos;
					word_pos.add(concatPos);
					pos.add(concatResult);
					wrdStack.pop();
				}
				inlwrds++;
			}
	//		System.out.println("root:" + root + root.size());
	//		System.out.println("word_pos:" + word_pos + word_pos.size());
			findUW();
			findVerb();
			
			if (VB_Count.size() > 1) {
				if (VB_Count.get(0).toString() != null) {
					verbno1 = VB_Count.get(0).toString();
				}
				if (VB_Count.get(1).toString() != null) {
					verbno2 = VB_Count.get(1).toString();
				}
			} else {				
				verbno1 = be_VerbCount.get(0).toString();				
			}			
			findRelnIndex();
			

		} catch (Exception e) {
		}
		return result;
	}

	public void findUW() {
		int count = 1;
		total = root.size();
		j = 0;
		StringBuffer resultuw = new StringBuffer();

		while (j < total) {
			g_bstnode = new BSTNode_en();
			n_bstnode = new BSTNode_en();
			adj_bstnode = new BSTNode_en();
			adv_bstnode = new BSTNode_en();
			be_bstnode = new BSTNode_en();
			do_bstnode = new BSTNode_en();
			occur_bstnode = new BSTNode_en();
			String rword = root.get(j).toString();
			String curr_pos = word_pos.get(j).toString();
			// System.out.println("curr_pos:"+curr_pos);
			int hc = rword.hashCode();
			if ((curr_pos.contains("NN")) || (curr_pos.contains("IN"))
					|| (curr_pos.contains("PRP"))) {
				// System.out.println("Inside curr_pos NN:"+curr_pos);
				if ((n_bstnode = n_dict.search(hc)) != null) {
					// System.out.println("Inside bstnode:");
					rootnew.add(n_bstnode.lexeme);
					uw.add(n_bstnode.constraint);
					no.add(new Integer(count++));
				} else if ((g_bstnode = g_dict.search(hc)) != null) {
					rootnew.add(g_bstnode.lexeme);
					uw.add(g_bstnode.constraint);
					no.add(new Integer(count++));
				} else {
					rootnew.add(rword);
					uw.add("None");
					no.add(new Integer(count++));
				}
			} else if (curr_pos.contains("JJ")) {
				// System.out.println("Inside curr_pos JJ:"+curr_pos);
				if ((adj_bstnode = adj_dict.search(hc)) != null) {
					// System.out.println("Inside bstnode:");
					rootnew.add(adj_bstnode.lexeme);
					uw.add(adj_bstnode.constraint);
					no.add(new Integer(count++));
				} else if ((g_bstnode = g_dict.search(hc)) != null) {
					rootnew.add(g_bstnode.lexeme);
					uw.add(g_bstnode.constraint);
					no.add(new Integer(count++));
				} else {
					rootnew.add(rword);
					uw.add("None");
					no.add(new Integer(count++));
				}
			} else if ((curr_pos.contains("RB")) || (curr_pos.equals("CC"))) {
				// System.out.println("Inside curr_pos RB and CC:"+curr_pos);
				if ((adv_bstnode = adv_dict.search(hc)) != null) {
					// System.out.println("Inside bstnode:");
					rootnew.add(adv_bstnode.lexeme);
					uw.add(adv_bstnode.constraint);
					no.add(new Integer(count++));
				} else if ((g_bstnode = g_dict.search(hc)) != null) {
					rootnew.add(g_bstnode.lexeme);
					uw.add(g_bstnode.constraint);
					no.add(new Integer(count++));
				} else {
					rootnew.add(rword);
					uw.add("None");
					no.add(new Integer(count++));
				}
			} else if (curr_pos.equals("VBZ")) {
				// System.out.println("Inside curr_pos VBZ:"+curr_pos);
				if ((be_bstnode = be_dict.search(hc)) != null) {
					// System.out.println("Inside bstnode:"+be_bstnode.lexeme);
					rootnew.add(be_bstnode.lexeme);
					uw.add(be_bstnode.constraint);
					no.add(new Integer(count++));
				} else if ((g_bstnode = g_dict.search(hc)) != null) {
					rootnew.add(g_bstnode.lexeme);
					uw.add(g_bstnode.constraint);
					no.add(new Integer(count++));
				} else {
					rootnew.add(rword);
					uw.add("None");
					no.add(new Integer(count++));
				}
			} else if ((curr_pos.equals("VBD")) || (curr_pos.equals("VB"))) {
				// System.out.println("Inside curr_pos VBD and VB:"+curr_pos);
				if ((do_bstnode = do_dict.search(hc)) != null) {
					// System.out.println("Inside bstnode:");
					rootnew.add(do_bstnode.lexeme);
					uw.add(do_bstnode.constraint);
					no.add(new Integer(count++));
				} else if ((g_bstnode = g_dict.search(hc)) != null) {
					rootnew.add(g_bstnode.lexeme);
					uw.add(g_bstnode.constraint);
					no.add(new Integer(count++));
				} else {
					rootnew.add(rword);
					uw.add("None");
					no.add(new Integer(count++));
				}
			} else if ((curr_pos.equals("VBP")) || (curr_pos.equals("VBG"))
					|| (curr_pos.equals("VBN"))) {
				// System.out.println("Inside curr_pos VBP & VBG & VBN:"+curr_pos);
				if ((occur_bstnode = occur_dict.search(hc)) != null) {
					// System.out.println("Inside bstnode:");
					rootnew.add(occur_bstnode.lexeme);
					uw.add(occur_bstnode.constraint);
					no.add(new Integer(count++));
				} else if ((g_bstnode = g_dict.search(hc)) != null) {
					rootnew.add(g_bstnode.lexeme);
					uw.add(g_bstnode.constraint);
					no.add(new Integer(count++));
				} else {
					rootnew.add(rword);
					uw.add("None");
					no.add(new Integer(count++));
				}
			} else if ((curr_pos.equals("CC")) || (curr_pos.equals("IN"))) {
				if ((g_bstnode = g_dict.search(hc)) != null) {
					rootnew.add(g_bstnode.lexeme);
					uw.add(g_bstnode.constraint);
					no.add(new Integer(count++));
				} else {
					rootnew.add(rword);
					uw.add("None");
					no.add(new Integer(count++));
				}
			} else if (curr_pos.equals("CD")) {
				rootnew.add(rword);
				uw.add("icl>number");
				no.add(new Integer(count++));
			}/**
			 * else if(curr_pos.equals("DT")){ rootnew.add("None");
			 * uw.add("None"); no.add("None"); }
			 */
			else if ((g_bstnode = g_dict.search(hc)) != null) {
				rootnew.add(g_bstnode.lexeme);
				uw.add(g_bstnode.constraint);
				no.add(new Integer(count++));
			} else {
				rootnew.add("None");
				uw.add("None");
				no.add("None");
			}
			j++;
		}
	//	System.out.println("Rootnew:" + rootnew + "=" + rootnew.size());
	//	System.out.println("POS:" + pos + "=" + pos.size());
	//	System.out.println("UW:" + uw + "=" + uw.size());
	//	System.out.println("NO:" + no + "=" + no.size());
		for (int k = 0; k < uw.size(); k++) {
			if (!(rootnew.get(k).equals("None"))) {
				resultuw.append(rootnew.get(k).toString() + ';');
				resultuw.append(uw.get(k).toString() + ';');
				resultuw.append(pos.get(k).toString() + ';');
				resultuw.append(no.get(k).toString() + '#');
			}
		}
		// System.out.println("Resultuw:" + resultuw);
		result += "[w]#";
		result += resultuw;
		result += "[/w]#";

	}

	public void findVerb() {
		int i = 0;
		int size = root.size();
		while (i < size) {
			if ((pos.get(i).toString().equals("VP VB"))
					|| (pos.get(i).toString().equals("VP VBD"))
					|| (pos.get(i).toString().equals("VP VBG"))
					|| (pos.get(i).toString().equals("VP VBN"))
					|| (pos.get(i).toString().equals("VP VBP"))/**
			 * ||
			 * ((pos.get(i).toString().equals("VP VBZ")) &&
			 * (!root.get(i).toString().equals("be")))
			 */
			) {
		//		System.out.println("Inside SRS IF" + pos.get(i).toString() + i);
				VB_List.add(pos.get(i).toString());
				// int j = no.get(i).toString();
				VB_Count.add(no.get(i).toString());
			} else if (pos.get(i).toString().equals("VP VBZ")) {
				be_VerbList.add(pos.get(i).toString());
				be_VerbCount.add(no.get(i).toString());
			}
			/**
			 * else if(pos.get(i).toString().equals("VP VBZ")){
			 * VB_List.add(pos.get(i).toString()); VB_Count.add(i); }
			 */
			i++;
		}
	//	System.out.println("VBList:VBCount====" + VB_List + VB_Count);
		// cw_fw_List();
	}

	public void rule1() {
		// System.out.println("Inside Rule1");
		// rootrel.set(j, "agt");
		// int l = Integer.parseInt(verbno2);
		// System.out.println("l:"+l);
		int k = j;
		while (k <= root.size()) {
			pos_word = pos.get(k).toString();
			// System.out.println("FOR:" + k + ":" + l + ":" + pos_word);
			if (pos_word.contains("NN")) {
				// System.out.println("pos_word:"+pos.get(k).toString());
				rel_label = "agt";
				findRelation_Category3(k + 1, rel_label);
				break;
			}
			k++;
		}
	}

	public void rule2() {
		rootrel.set(j, "and");
		rel_label = "and";
		findRelation_Category1(j, rel_label);
	}

	public void rule3() {
		// System.out.println("Inside Rule3");
		rootrel.set(j, "aoj");
		rel_label = "aoj";
		findRelation_Category3(j, rel_label);
		// System.out.println("After Rule3");
	}

	public void rule4() {
		rootrel.set(j, "bas");
		rel_label = "bas";
		// findRelation_Category3(j, rel_label);
	}

	public void rule5() {
		rootrel.set(j, "ben");
		u_word = uw.get(j - 1).toString();
		if (u_word.contains("aoj>thing")) {
			rel_label = "ben";
			findRelation_Category5(j - 1, j + 1, rel_label);
		} else {
			rel_label = "ben";
			findRelation_Category4(j + 1, rel_label);
		}

	}

	public void rule6() {
		rootrel.set(j, "cag");
		// pos_word = pos.get(j - 1).toString();
		// if (pos_word.equals("verb")) {
		for (int i = j; i < uw.size(); i++) {
			u_word = uw.get(i).toString();
			if (u_word.contains("person")) {
				rel_label = "cag";
				// findRelation_Category4(j, rel_label);
			} else if (u_word.equals("aoj>thing")) {
				rel_label = "cao";
				// findRelation_Category4(j, rel_label);
			} else if ((u_word.contains("icl>abstract thing"))
					|| (u_word.contains("aoj>thing,obj>thing"))) {
				rel_label = "cob";
			}
			findRelation_Category4(j, rel_label);
		}
		// }
	}

	public void rule7() {
		rootrel.set(j, "cao");
		rel_label = "cao";
		// findRelation_Category2(j, rel_label);
	}

	public void rule8() {
		rootrel.set(j, "cnt");
		rel_label = "cnt";
		// findRelation_Category2(j, rel_label);
	}

	public void rule9() {
		rootrel.set(j, "cob");
		rel_label = "cob";
		// findRelation_Category2(j, rel_label);
	}

	public void rule10() {
		rootrel.set(j, "con");
		rel_label = "con";
		// findRelation_Category2(j, rel_label);
	}

	public void rule11() {
		rootrel.set(j, "coo");
		rel_label = "coo";
		findRelation_Category1(j, rel_label);
	}

	public void rule12() {
		rootrel.set(j, "dur");
		if ((u_word.contains("event")) || (u_word.contains("period"))
				|| (u_word.contains("state"))) {
			rel_label = "dur";
			findRelation_Category4(j, rel_label);
		} else {
			rel_label = "dur";
			findRelation_Category1(j, rel_label);
		}
	}

	public void rule13() {
		rootrel.set(j, "equ");
		rel_label = "equ";
		// findRelation_Category2(j, rel_label);
	}

	public void rule14() {
		rootrel.set(j, "fmt");
		String uw_word = uw.get(j - 1).toString();
		String uw_next = uw.get(j + 1).toString();
		if (uw_word.equals(uw_next)) {
			rel_label = "fmt";
			findRelation_Category1(j, rel_label);
		}

	}

	public void rule15() {
		rootrel.set(j, "icl");
		rel_label = "icl";
		findRelation_Category2(j, rel_label);
	}

	public void rule16() {
		rootrel.set(j, "ins");
		u_word = uw.get(j + 1).toString();
		if ((u_word.contains("instrument")) || (u_word.contains("stationery"))
				|| (u_word.contains("cutley"))) {
			rel_label = "ins";
			findRelation_Category4(j + 1, rel_label);
		}
	}

	public void rule17() {
		rootrel.set(j, "int");
		rel_label = "int";
		// findRelation_Category2(j, rel_label);
	}

	public void rule18() {
		rootrel.set(j, "iof");
		rel_label = "iof";
		// findRelation_Category2(j, rel_label);
	}

	public void rule19() {
		rootrel.set(j, "man");
		rel_label = "man";
		// System.out.println(j + 1 + " " + j + " " + rel_label);
		findRelation_Category2(j, rel_label);
	}

	public void rule20() {
		rootrel.set(j, "met");
		rel_label = "met";
		// findRelation_Category2(j, rel_label);
	}

	public void rule21() {
		rootrel.set(j, "mod");
		int i = j + 1;
		while (i < root.size()) {
			if (pos.get(i).toString().contains("NN")) {
				rel_label = "mod";
				findRelation_Category5(j, i, rel_label);
				break;
			} else if (pos.get(i).toString().contains("JJ")) {
				i++;
			} else {
				break;
			}
		}
	}

	public void rule22() {
		rootrel.set(j, "nam");
		rel_label = "nam";
		// findRelation_Category2(j, rel_label);
	}

	public void rule23() {
		rootrel.set(j, "obj");
		rel_label = "obj";
		// System.out.println(j + 1 + " " + j + " " + rel_label);
		// findRelation_Category2(j, rel_label);
	}

	public void rule24() {
		rootrel.set(j, "or");
		rel_label = "or";
		findRelation_Category1(j, rel_label);
	}

	public void rule25() {
		rootrel.set(j, "per");
		rel_label = "per";
		// findRelation_Category1(j, rel_label);
	}

	public void rule26() {
		rootrel.set(j, "plc");
		u_word = uw.get(j + 1).toString();
		if ((u_word.contains("city")) || (u_word.contains("place"))
				|| (u_word.contains("country")) || (u_word.contains("region"))) {
			rel_label = "plc";
			// findRelation_Category4(j + 1, rel_label);
		} else if (u_word.contains("event")) {
			rel_label = "scn";
			// findRelation_Category4(j + 1, rel_label);
		} else {
			rel_label = "opl";
			// findRelation_Category4(j + 1, rel_label);
		}
		findRelation_Category4(j + 1, rel_label);
	}

	public void rule27() {
		rootrel.set(j, "frm");
		u_word = uw.get(j + 1).toString();
		int k = j + 1;
		if ((u_word.contains("city")) || (u_word.contains("place"))
				|| (u_word.contains("country")) || (u_word.contains("land"))) {
			rel_label = "plf";
		} else if (u_word.contains("day")) {
			rel_label = "frm";
		} else if (u_word.contains("time")) {
			rel_label = "tmf";
		} else {
			rel_label = "src";
		}
		findRelation_Category4(k, rel_label);
	}

	public void rule28() {
		rootrel.set(j, "plt");
		int k = j + 1;
		u_word = uw.get(k).toString();
		// System.out.println("Inside Rule 1"+u_word);
		if ((u_word.contains("city")) || (u_word.contains("place"))
				|| (u_word.contains("country")) || (u_word.contains("land"))) {
			rel_label = "plt";
			// System.out.println("Relation:"+k+":"+rel_label);
		} else if (u_word.contains("time")) {
			rel_label = "tmt";
		} else if (u_word.contains("gol>thing")) {
			rel_label = "gol";
		} else {
			rel_label = "to";
		}
		findRelation_Category4(k, rel_label);
	}

	public void rule29() {
		rootrel.set(j, "pof");
		rel_label = "pof";
		// findRelation_Category4(j + 1, rel_label);
	}

	public void rule30() {
	//	System.out.println("Inside Rule30");
		rootrel.set(j, "pos");
		rel_label = "pos";
		findRelation_Category5(j - 1, j + 1, rel_label);
	//	System.out.println("After Rule30");
	}

	public void rule31() {
		rootrel.set(j, "ptn");
		rel_label = "ptn";
		// findRelation_Category1(j, rel_label);
	}

	public void rule32() {
		rootrel.set(j, "pur");
		rel_label = "pur";
		// findRelation_Category1(j, rel_label);
	}

	public void rule33() {
		rootrel.set(j, "qua");
		rel_label = "qua";
		// System.out.println(j + 1 + " " + j + " " + rel_label);
		findRelation_Category2(j, rel_label);
	}

	public void rule34() {
		rootrel.set(j, "rsn");
		rel_label = "rsn";
		// System.out.println(j + 1 + " " + j + " " + rel_label);
		// findRelation_Category2(j, rel_label);
	}

	public void rule35() {
		rootrel.set(j, "seq");
		rel_label = "seq";
		// System.out.println(j + 1 + " " + j + " " + rel_label);
		// findRelation_Category2(j, rel_label);
	}

	public void rule36() {
		rootrel.set(j, "via");
		u_word = uw.get(j + 1).toString();
		if ((u_word.contains("city")) || (u_word.contains("place"))
				|| (u_word.contains("country"))) {
			rel_label = "via";
			findRelation_Category4(j + 1, rel_label);
		}
	}

	public void rule37() {
		rootrel.set(j, "pos");
		if (pos.get(j + 1).toString().contains("NN")) {
			rel_label = "pos";
			// System.out.println(j + 1 + " " + j + " " + rel_label);
			findRelation_Category5(j, j + 1, rel_label);
		}
	}

	public void rule38() {
	//	System.out.println("Inside Rule38");
		rootrel.set(j, "plc");
		// if(uw.get(j-1).toString().contains("aoj>thing")){
		rel_label = "plc";
	//	System.out.println(j - 1 + " " + j + " " + rel_label + ":"+ uw.get(j - 1).toString() + ":" + uw.get(j + 1).toString());
		findRelation_Category5(j - 1, j + 1, rel_label);
		// }
	}

	public void findRelnIndex() {
		j = 0;
		total = root.size();
		result += "[r]#";
		while (j < total) {
			try {
				rootrel.add("None");
				currword = root.get(j).toString();
				pos_word = pos.get(j).toString();
				// System.out.println("pos_word:" + pos_word);
				u_word = uw.get(j).toString();
				// System.out.println("currword" + currword);
				// System.out.println("J:" + j);
				if (pos_word.contains("CC")) {
					// System.out.println("Inside CC");
					if (currword.contains("and")) {
						// System.out.println("Inside CC");
						rule2();
					} else if (currword.contains("or")) {
						rule24();
					}
					// System.out.println("Rule6:" + j + rel_label);
				}
				if (pos_word.contains("JJ")) {
					rule21();
				}
				if (pos_word.contains("VBZ")) {
					// System.out.println("Inside VBZ");
					rule3();
				}
				if (pos_word.contains("PRP")) {
					rule37();
				}
				if (pos_word.contains("IN")) {
				//	System.out.println("Inside IN");
					if (currword.contains("from")) {
						// System.out.println("Inside FROM");
						rule27();
					}
					if (currword.contains("by")) {
					//	System.out.println("Inside by");
						rule1();
					}
					if ((currword.contains("in")) || (currword.contains("on"))) {
					//	System.out.println("Inside IN ON:" + j);
						rule38();
					}
					if (currword.contains("for")) {
					//	System.out.println("Inside FOR");
						rule5();
						// rule38();
					}
					if (currword.contains("of")) {
					//	System.out.println("Inside of:");
						rule30();
					}
					if (currword.contains("with")) {
						rule6();
						rule16();
					}
				}
				if (pos_word.contains("TO")) {
					if (currword.contains("to")) {
						// System.out.println("Inside TO");
						rule14();
						rule28();
					}
				}
				if (pos_word.contains("CD")) {
					rule33();
				}
				if (currword.contains("while")) {
					rule11();
				}
				if ((currword.contains("during"))) {
					rule12();
				}

			} catch (Exception e) {
			}
			j++;
		}
		j = 0;
		result += "[/r]#[/s]#";
		// System.out.println("RESULT:" + result);
	}

	public void findRelation_Category1(int j, String rl) {
		String concept1 = "";
		String concept2 = "";
		// int size = no.size();
		// if (size != (j + 1)) {
	//	System.out.println("Inside Category1:" + j);
		if (pos.get(j - 1).toString().equals(pos.get(j + 1).toString())) {
		//	System.out.println("Inside Category1:" + pos.get(j - 1).toString()+ ":" + pos.get(j + 1).toString());
			concept1 = no.get(j + 1).toString();
			if ((j - 1) > 0) {
				concept2 = no.get(j - 1).toString();
			} else {
				concept2 = no.get(0).toString();
			}
			result += concept1 + '\t' + rl + '\t' + concept2 + '#';
		}
		// System.out.println("RESULT:" + result);
	}

	public void findRelation_Category2(int j, String rl) {
		String concept1 = "";
		String concept2 = "";
		int tsize = no.size();
		concept2 = no.get(j).toString();
		for (int i = j + 1; i < tsize; i++) {
			if ((pos.get(i).toString().contains("NN"))
					|| (pos.get(i).toString().contains("NNP"))) {
				// System.out.println("Inside Category 2:");
				concept1 = no.get(i).toString();
				break;
			}
		}
		result += concept1 + '\t' + rl + '\t' + concept2 + '#';
		// System.out.println("Inside Category 2:"+result);
	}

	public void findRelation_Category3(int j, String rl) {
		String concept1 = "";
		String concept2 = "";
		// int tsize = no.size();
	//	System.out.println("INSIDE Category 3");
		concept2 = no.get(j - 1).toString();

		if (root.get(j).toString().equals("be")) {
			concept1 = no.get(j + 1).toString();
		} else {
			concept1 = verbno1;
		}
	//	System.out.println("concept1 & concept2:" + concept1 + ":" + concept2);
		if (!concept1.equals(concept2)) {
			result += concept1 + '\t' + rl + '\t' + concept2 + '#';
		}
	}

	public void findRelation_Category4(int j, String rl) {
		String concept1 = "";
		String concept2 = "";
		// int tsize = no.size();
		// System.out.println("INSIDE Category 4");
		concept2 = no.get(j).toString();
		if ((verbno2 != null) && (j >= Integer.parseInt(verbno2))) {
			concept1 = verbno2;
		} else {
			concept1 = verbno1;
		}
		if (!concept1.equals(concept2)) {
			result += concept1 + '\t' + rl + '\t' + concept2 + '#';
		}
	}

	public void findRelation_Category5(int j, int i, String rl) {
		String concept1 = "";
		String concept2 = "";
		concept2 = no.get(j).toString();
		concept1 = no.get(i).toString();
		result += concept1 + '\t' + rl + '\t' + concept2 + '#';
	}}

