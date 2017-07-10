package org.apache.nutch.enconversion.unl.ta;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.*;
import java.io.*;
import java.util.*;

import org.apache.nutch.unl.UNL;

public class MultiList implements Serializable {
	// public static FinalLLImpl[] llq = new FinalLLImpl[35000];
	public static PrintStream ugt;
	public static int i = 0;
//	public static FinalLLImpl[] ll_new = new FinalLLImpl[50];
	public static void list_writeintofile(FinalLLImpl[] llq,
			ObjectOutputStream output) {
		// FinalLLImpl[] llq = new FinalLLImpl[50000];
		// llq = ll_new;
		/**
		 * int count = 0; for (int i = 0; i < llq.length; i++) { if (llq[i] !=
		 * null) { count++; } }
		 */

		for (int i = 0; i < llq.length; i++) {
			try {
				HeadNode head = new HeadNode();
				ConceptNode con = new ConceptNode();
				ConceptToNode conto = new ConceptToNode();

				head = llq[i].head;
				if (head != null) {
					output.writeObject("HEADNODE EXISTS");
				//	output.writeObject(head);
					//System.out.println("Head" + head);
					con = head.colnext;
					//System.out.println("concept" + con);
					while (con != null) {
						output.writeObject("CONCEPTNODE EXISTS");
						output.writeObject(con.gn_word);
						output.writeObject(con.uwconcept);
						output.writeObject(con.conceptid);
						output.writeObject(con.attrb);
						//output.writeObject(con.scopeid);
						output.writeObject(con.docid);
						output.writeObject(con.sentid);
						output.writeObject(con.poscheck);
						output.writeObject(con.freq_count);
						output.writeObject(con.queryTag);
						output.writeObject(con.MWtag_Qw);
						//System.out.println("Inside while concept" + con.gn_word);
						conto = con.getRowNext();
						while (conto != null) {
							output.writeObject("CONCEPTTONODE EXISTS");
							output.writeObject(conto.uwfrmconcept);
							output.writeObject(conto.uwtoconcept);
							output.writeObject(conto.relnlabel);
							//output.writeObject(conto.frmscopeid);
							//output.writeObject(conto.toscopeid);
							output.writeObject(conto.docid);
							output.writeObject(conto.sentid);
							conto = conto.getRowNext();
						}
						con = con.getColNext();
						output.writeObject("NO CONCEPTTONODE");
					}
					/* else{ */
					output.writeObject("NO CONCEPTNODE");
					// } */
				}
			} catch (Exception e) {

			}
		}// for ends

	}

/**	public static void list_readfrmfile(ObjectInputStream input, int i)
			throws IOException, ClassNotFoundException {
		// index must be 1 or higher
		
		String str = "";
		HeadNode head = new HeadNode();
		ConceptNode current = new ConceptNode();
		ConceptToNode ctNode;
		try {
			ugt = new PrintStream(new FileOutputStream(new File(
					"./graphtrav.txt"), true));

			str = (String) input.readObject();
			// //System.out.println("Str1"+str);
			if (str.equalsIgnoreCase("HEADNODE EXISTS")) {
				// head=new HeadNode();
				// current = head.colnext;
				// //System.out.println("Str1 inside if"+str);
				head = (HeadNode) input.readObject();
				if (head != null) {
					ll_new[i].head = head;
				}
				str = (String) input.readObject();
				if (str.equalsIgnoreCase("CONCEPTNODE EXISTS")) {
					current = head.colnext;

					current.gn_word = (String) input.readObject();
					current.uwconcept = (String) input.readObject();
					current.conceptid = (String) input.readObject();
					current.scopeid = (String) input.readObject();
					current.docid = (String) input.readObject();
					current.sentid = (String) input.readObject();
					current.poscheck = (String) input.readObject();
					current.freq_count = (String) input.readObject();
					current.queryTag = (String) input.readObject();
					current.MWtag_Qw = (String) input.readObject();

					ugt.println();
					ugt.print(current.gn_word + '\t');
					ugt.print(current.uwconcept + '\t');
					ugt.print(current.conceptid + '\t');
					ugt.print(current.freq_count + '\t');
					ugt.print(current.poscheck + '\t');
					ugt.print(current.queryTag + '\t');
					ugt.print(current.MWtag_Qw + '\t');

					current = new ConceptNode();
					str = (String) input.readObject();
					// //System.out.println("Str1 enter into while"+str);
					while (str.equalsIgnoreCase("CONCEPTNODE EXISTS")) {
						current = new ConceptNode();

						current.gn_word = (String) input.readObject();
						current.uwconcept = (String) input.readObject();
						current.conceptid = (String) input.readObject();
						current.scopeid = (String) input.readObject();
						current.docid = (String) input.readObject();
						current.sentid = (String) input.readObject();
						current.poscheck = (String) input.readObject();
						current.freq_count = (String) input.readObject();
						current.queryTag = (String) input.readObject();
						current.MWtag_Qw = (String) input.readObject();

						ugt.println();
						ugt.print(current.gn_word + '\t');
						ugt.print(current.uwconcept + '\t');
						ugt.print(current.conceptid + '\t');
						ugt.print(current.freq_count + '\t');
						ugt.print(current.poscheck + '\t');
						ugt.print(current.queryTag + '\t');
						ugt.print(current.MWtag_Qw + '\t');

						str = (String) input.readObject();
						if (str.equalsIgnoreCase("CONCEPTTONODE EXISTS")) {
							ctNode = new ConceptToNode();
							// //System.out.println("Str1 tonode if"+str);
							// ctNode = current.getRowNext();

							ctNode.uwfrmconcept = (String) input.readObject();
							ctNode.uwtoconcept = (String) input.readObject();
							ctNode.relnlabel = (String) input.readObject();
							ctNode.frmscopeid = (String) input.readObject();
							ctNode.toscopeid = (String) input.readObject();
							ctNode.docid = (String) input.readObject();
							ctNode.sentid = (String) input.readObject();

							ugt.print(ctNode.uwfrmconcept + '\t');
							ugt.print(ctNode.uwtoconcept + '\t');
							ugt.print(ctNode.relnlabel + '\t');
							ugt.print(ctNode.docid + '\t');
							ugt.println(ctNode.sentid + '\t');
							str = (String) input.readObject();
							// //System.out.println("The str is"+str);
							while (str.equalsIgnoreCase("CONCEPTTONODE EXISTS")) {
								// //System.out.println("Str1 tonode while"+str);
								ctNode = new ConceptToNode();
								// ctNode = ctNode.getRowNext();

								ctNode.uwfrmconcept = (String) input
										.readObject();
								ctNode.uwtoconcept = (String) input
										.readObject();
								ctNode.relnlabel = (String) input.readObject();
								ctNode.frmscopeid = (String) input.readObject();
								ctNode.toscopeid = (String) input.readObject();
								ctNode.docid = (String) input.readObject();
								ctNode.sentid = (String) input.readObject();

								ugt.print(ctNode.uwfrmconcept + '\t');
								ugt.print(ctNode.uwtoconcept + '\t');
								ugt.print(ctNode.relnlabel + '\t');
								ugt.print(ctNode.docid + '\t');
								ugt.println(ctNode.sentid + '\t');

								str = (String) input.readObject();
							}

						}
						current = current.getColNext();
						str = (String) input.readObject();
					}
					str = (String) input.readObject();
				}

			}// end of if
			// //ugt.close();
		} catch (Exception e) {
		}
		// return current;
	}	*/
	public static FinalLLImpl[] list_readfrmfile(FinalLLImpl[] ll_new, ObjectInputStream input, int j)
	throws IOException, ClassNotFoundException {
// index must be 1 or higher

String str = "";
HeadNode head = new HeadNode();
ConceptNode current = new ConceptNode();
ConceptToNode ctNode;

String gn_word = "",uwconcept = "", conceptid = "", scopeid = "",sentid = "", docid = "",poscheck = "", freq_count = "", queryTag = "", MWtag_Qw = "";
String uwfrmconcept = "", uwtoconcept = "", relnlabel = "", frmscopeid = "", toscopeid = "";
String attr="";
try {
	ugt = new PrintStream(new FileOutputStream(new File("./graphtrav.txt"), true));
	
	str = (String) input.readObject();
	
	if (str.equalsIgnoreCase("HEADNODE EXISTS")) {
		
		ll_new[i] = new FinalLLImpl();
		i++;
		str = (String) input.readObject();		
		
			while (str.equalsIgnoreCase("CONCEPTNODE EXISTS")) {
				
				current = new ConceptNode();
				 
				gn_word = (String) input.readObject();
				uwconcept = (String) input.readObject();
				conceptid = (String) input.readObject();
				attr = (String) input.readObject();
				docid = (String) input.readObject();
				sentid = (String) input.readObject();
				poscheck = (String) input.readObject();
				freq_count = (String) input.readObject();
				queryTag = (String) input.readObject();
				MWtag_Qw = (String) input.readObject();

				ll_new[i-1].addConcept(gn_word, uwconcept,attr,conceptid,docid, sentid, poscheck, freq_count, queryTag, MWtag_Qw);
								
				ugt.println();
				ugt.print(gn_word + '\t');
				ugt.print(uwconcept + '\t');
				ugt.print(attr + '\t');
				ugt.print(conceptid + '\t');
				ugt.print(freq_count + '\t');
				ugt.print(poscheck + '\t');
				ugt.print(queryTag + '\t');
				ugt.print(MWtag_Qw + '\t');

				str = (String) input.readObject();
				if (str.equalsIgnoreCase("CONCEPTTONODE EXISTS")) {
					
					ctNode = new ConceptToNode();
					

					uwfrmconcept = (String) input.readObject();
					uwtoconcept = (String) input.readObject();
					relnlabel = (String) input.readObject();
					
					docid = (String) input.readObject();
					sentid = (String) input.readObject();
					
					if ((!(uwfrmconcept.equals("None")))
							&& (!(uwtoconcept.equals("None")))) {

						ll_new[i - 1].addRelation(relnlabel);
						ConceptToNode cn = ll_new[i - 1].addCT_Concept(uwfrmconcept, uwtoconcept,relnlabel, docid, sentid);
						ll_new[i - 1].addCT_Relation(cn);
					}

					ugt.print(uwfrmconcept + '\t');
					ugt.print(uwtoconcept + '\t');
					ugt.print(relnlabel + '\t');
					ugt.print(docid + '\t');
					ugt.println(sentid + '\t');
					str = (String) input.readObject();
					
					while (str.equalsIgnoreCase("CONCEPTTONODE EXISTS")) {
						
						ctNode = new ConceptToNode();
						

						uwfrmconcept = (String) input.readObject();
						uwtoconcept = (String) input.readObject();
						relnlabel = (String) input.readObject();
					
						docid = (String) input.readObject();
						sentid = (String) input.readObject();
						
						if ((!(uwfrmconcept.equals("None")))
								&& (!(uwtoconcept.equals("None")))) {

							ll_new[i - 1].addRelation(relnlabel);
							ConceptToNode cn = ll_new[i - 1].addCT_Concept(uwfrmconcept, uwtoconcept,relnlabel, docid, sentid);
							ll_new[i - 1].addCT_Relation(cn);
						}

						ugt.print(uwfrmconcept + '\t');
						ugt.print(uwtoconcept + '\t');
						ugt.print(relnlabel + '\t');
						ugt.print(docid + '\t');
						ugt.println(sentid + '\t');

						str = (String) input.readObject();
					}

				}
				current = current.getColNext();
				str = (String) input.readObject();
			}
			str = (String) input.readObject();
		//}

	}// end of if
	// //ugt.close();
} catch (Exception e) {
}
 return ll_new;
}	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
