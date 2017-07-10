package org.apache.nutch.enconversion.unl.ta;

import java.io.*;
import java.util.*;

public class eventlearning{

	public static MultiList mlist=new MultiList();
	
	public static void main(String args[]){
eventlearning elearn=new eventlearning();
	 File f = new File("./resource/unl/Enconversion/unl-graph/");
         File filename[] = f.listFiles();
	ArrayList event_reln=new ArrayList();
		event_reln.add("aoj");
		event_reln.add("agt");
		event_reln.add("cag");
		event_reln.add("plc");
		event_reln.add("plf");
		event_reln.add("plt");
		event_reln.add("pur");
		event_reln.add("gol");
		event_reln.add("tim");
		event_reln.add("mod");
String[] event_constrain={"icl>event","icl>action","iof>person","icl>phenomenon"};
	
	 for (int i = 0; i < filename.length; i++) {//loop for integrating the unl graphs binary files

			System.out.println("filenamesize"+filename.length);
                        FinalLLImpl[] ll = new FinalLLImpl[filename.length];//25000 indicates number of documents to be in memory
                        FinalLLImpl[] ll_new1 = new FinalLLImpl[filename.length];
                        int count1 = 0;
                       
                        try {

                            FileInputStream fis = new FileInputStream(filename[i]);                       
                            ObjectInputStream ois = new ObjectInputStream(fis);                       
                            ll_new1 = mlist.list_readfrmfile(ll, ois, i);                    
                            for (int j = 0; j < ll_new1.length; j++) {


                                if (ll_new1[j] != null) {
                                    HeadNode temp1 = new HeadNode();
                			temp1 = ll_new1[j].head;
			                ConceptNode c1 = new ConceptNode();
			                ConceptToNode current1 = new ConceptToNode();
			                c1 = temp1.colnext;

			                while (c1 != null) {
					 current1 = c1.rownext;
					while(current1 != null)
					{
						if(Integer.parseInt(c1.freq_count)>2)
						{
								
						
								String c2=ll_new1[j].getconcept_vs_conceptid_query(current1.uwtoconcept.trim());
								//System.out.println("Tocon"+c2);
								String tamil_c2=ll_new1[j].gettamilword(c2).trim();
								System.out.println("Words are"+c1.uwconcept+c1.gn_word+"relations"+current1.relnlabel);


						}
						current1=current1.getRowNext();
					}
					c1 = c1.getColNext();

					
					}//concept node not null
                                }
                            }

                            ois.close();
                        } catch (Exception e) {
                            ////System.out.println(e.getMessage());
                        }

		}//for
	}//main
	public static String getNextLinkConcept(String docid,String c2,FinalLLImpl ll_new1)
	{
		String a="";
		
	 if (ll_new1 != null) 
	{
	                                HeadNode temp1 = new HeadNode();
                			temp1 = ll_new1.head;
			                ConceptNode c1 = new ConceptNode();
			                ConceptToNode current1 = new ConceptToNode();
			                c1 = temp1.colnext;

			                while (c1 != null) {
					 current1 = c1.rownext;
					while(current1 != null)
					{
						if((c1.uwconcept).equals(c2)&& c1.docid.equals(docid))
						{
					//	System.out.println("The conexc"+c1.uwconcept);
						
						a=c1.uwconcept+c1.gn_word;
						
					
						}
						current1=current1.getRowNext();
					}
					c1 = c1.getColNext();

					
					}//concept node not null
        }//if	
	return a;
	}//get

}//class
