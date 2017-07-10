package org.apache.nutch.index.unl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.*;
import org.apache.nutch.enconversion.unl.ta.ConceptNode;
import org.apache.nutch.enconversion.unl.ta.ConceptToNode;
import org.apache.nutch.enconversion.unl.ta.FinalLLImpl;
import org.apache.nutch.enconversion.unl.ta.UWDict;
import org.apache.nutch.enconversion.unl.ta.BST;
import org.apache.nutch.enconversion.unl.ta.HeadNode;
import org.apache.nutch.enconversion.unl.ta.MultiList;
import clia.unl.Source.Word_Gen.Generator.verbinfinitive;
import clia.unl.Source.Word_Gen.Generator.word_noun;
import clia.unl.Source.Word_Gen.Generator.word_verb;

public class FrameworkNew{	
	public ArrayList sen_eng=new ArrayList();
	public ArrayList sen_ta=new ArrayList();
	public ArrayList sen_ma=new ArrayList();
	String attriagt="";
	public ArrayList relation_priority=new ArrayList();
	public Hashtable relations_rules=new Hashtable();
	String inputlanguage_id="";
	String targetlanguage_id="";
	public Hashtable suffix =new Hashtable();
	public Hashtable fullword=new Hashtable();
	public Hashtable additiongen=new Hashtable();
	public ArrayList vowel=new ArrayList();
	public ArrayList oesexception=new ArrayList();
	public ArrayList tamil_priority_window=new ArrayList();
	public ArrayList english_priority_window=new ArrayList();	
	ArrayList MasterDecon=new ArrayList();	
    MultiList m=new MultiList();
    BST_enghashkey bst_en=new BST_enghashkey();// object that contains tamil uw dictionary with head word as hashkey
	/**
	 * plf-இலிருந்து
     * frm -இடமிருந்து
     *---------------- and-மட்டுமல்லாமல் 
     * --------------------via-வழியாக 
     * and-உம்
     *  ptn-உம்
     *  agt-ஆல்
     *  con-ஆல்
     *  ins-ஆல்
     *  met-ஆல்
     *  plt-க்கு
     *  ----------bas-காட்டிலும்
     *  ------------bas-விட
	 * ben-ஆக
	 * cag-ஓடு
	 * cao-ஓடு
	 * cob-ஓடு
	 * -------------cnt-என்பது
	 * ----------coo-கொண்டே
	 * -------------dur-பொழுது
	 * -------------equ-என்னும்
	 * --------------tmf-முதல்
	 * gol-க்கு
	 * tmt-க்கு
	 *------------ plt-வரை
	 * obj-ஐ
	 * plc-இல்  
	 * pos-இன்
	 * ------------ins-மூலம்
	 *----------------- met-மூலம்
	 * ----------------int-பொது
	 * --------------man-மிக
	 *----------------- nam-ஆகிய
	 * ------------------OR-அல்லது
	 * 
	 * 
	 * 	
	 */
	
	 Hashtable Reln_Caseending=new Hashtable();
	 Hashtable Reln_Case=new Hashtable();
	 Hashtable<String,ArrayList> WordOrderHash=new Hashtable<String, ArrayList>();
	 public FrameworkNew()throws IOException{		 
		 TamilWordOrder t=new  TamilWordOrder();
		 t.loadPatternstoMemory();
			t.pickPatterns();
			t.printCooccuringcon_counthashtable();
			t. arrangeConsInDescendingOrder();
		 WordOrderHash=t.getWordOrder();
		 loadEnglish_priority_window();
		 
		 bst_en.loadDic(bst_en);
		 
	 }
	 public void loadMaintable(){
		 
		 Reln_Caseending.put("plf", "இலிருந்து");
		 Reln_Caseending.put("frm", "இலிருந்து");
		 Reln_Caseending.put("and","உம்");
		 Reln_Caseending.put("ptn", "உடன்");		
		 Reln_Caseending.put("agt", "ஆல்");
		 Reln_Caseending.put("con", "ஆல்");
		 Reln_Caseending.put("ins", "ஆல்");
		 Reln_Caseending.put("met", "ஆல்");
		 Reln_Caseending.put("plt","க்கு");		
		 Reln_Caseending.put("ben","உக்கு");
		 Reln_Caseending.put("cag","ஓடு");
		 Reln_Caseending.put("cao", "ஓடு");		
		 Reln_Caseending.put("gol","க்கு");
		 Reln_Caseending.put("tmt","க்கு");
		 Reln_Caseending.put("obj","ஐ");
		 Reln_Caseending.put("plc","இல் ");
		 Reln_Caseending.put("pos","இன்");
		 Reln_Case.put("via","வழியாக ");
		 Reln_Case.put("bas","காட்டிலும்");
		 Reln_Case.put("coo","கொண்டே");
		 Reln_Case.put("dur","பொழுது");
		 Reln_Case.put("equ","என்னும்");
		 Reln_Case.put("tmf","முதல்");
		 Reln_Case.put("ins","மூலம்");
		 Reln_Case.put("int","பொது");
		// Reln_Case.put("man","மிக");
		 Reln_Case.put("nam","ஆகிய");
		 Reln_Case.put("iof","ஆகிய");
		 Reln_Case.put(" OR","அல்லது");
		 	
		 
	 }	
	 public void loadTamil_priority_window(){
		 
		tamil_priority_window.add("plc");
		tamil_priority_window.add("pos");
		tamil_priority_window.add("agt");
		tamil_priority_window.add("qua");
		tamil_priority_window.add("obj");
		tamil_priority_window.add("ins");
		tamil_priority_window.add("con");
		tamil_priority_window.add("cao");
		tamil_priority_window.add("cob");
		tamil_priority_window.add("ptn");
		tamil_priority_window.add("gol");
		tamil_priority_window.add("ben");
		tamil_priority_window.add("to");
		tamil_priority_window.add("plt");
		tamil_priority_window.add("scn");
		tamil_priority_window.add("opl");
		tamil_priority_window.add("iof");
		tamil_priority_window.add("pof");		
		tamil_priority_window.add("dur");
		tamil_priority_window.add("int"); 		 
	 }
	public  ArrayList loadEnglish_priority_window(){
		
		english_priority_window.add("pos");
		english_priority_window.add("plc");
		english_priority_window.add("agt");		
		english_priority_window.add("and");
		english_priority_window.add("man");
		english_priority_window.add("mod");		
		english_priority_window.add("ptn");			
		english_priority_window.add("qua");
		english_priority_window.add("obj");	
		english_priority_window.add("opl");	
		english_priority_window.add("verb");
		english_priority_window.add("plf");
		english_priority_window.add("plt");
		english_priority_window.add("frm");
		english_priority_window.add("gol");
		english_priority_window.add("tim");
		english_priority_window.add("tmf");
		english_priority_window.add("tmt");		
		english_priority_window.add("ben");
		english_priority_window.add("ins");
		
		
		return english_priority_window;
		
	}
		
		public ArrayList doSimpleFileListing(String dirName) throws IOException {
			File dir = new File(dirName);
			String[] children = dir.list();
		ArrayList docs=	printFiles(children, dirName);
		dir = null;
		return docs;
		}

		private ArrayList printFiles(String[] children, String dirName)
				throws IOException {
			ArrayList llist=new ArrayList();
	 		 ArrayList graphs=new ArrayList();
	 	        FinalLLImpl[] ll = null;
			if (children == null) {
			} else {
				for (int i = 0; i <children.length; i++) {
					// Get filename of file or directory
					String filename = children[i];									
					File files = new File(dirName + File.separatorChar + filename);					
					System.out.println("file name:"+files);				
			 		try{			 					  
			         int position=0;    
			         ll = new FinalLLImpl[5000];        
			           FinalLLImpl[] ll_new1 = new FinalLLImpl[5000];
			                  int count1 = 0;
			                try {
			                      FileInputStream fis = new FileInputStream(files);
			                       ObjectInputStream ois = new ObjectInputStream(fis);
			                      	ll_new1=m.list_readfrmfile(ll_new1,ois,i);
			                       	llist.add(ll_new1[i]);
			                      	
			                      	HeadNode h=new HeadNode();
			                      	h=ll_new1[i].head;
			                            	
			                      	fis.close();
			                      ois.close();
			                      files = null;
			                  } catch (Exception e) {
			                      System.out.println(e.getMessage());
			                  }          
			         
			 		}catch(Exception e){e.printStackTrace();}            
					
				}
			}
			return llist;
		}
	
		/*public void processGraphsNew(FinalLLImpl[] ll)throws Exception{
 		int count=0;
 		getRulesfromFile("");
 		try{
 	
 			
	        for (int i = 0; i < ll.length; i++) {
	        	System.out.println("Inside for loop");
	            if (ll[i] != null) {
	                count++;
	            }
	        }
		
	        for (int i = 0, j = 0; j < count; i++, j++) {    
			
	            int flg = 0;
	            HeadNode temp1 = new HeadNode();
	            temp1 = ll[i].head;
	            ConceptNode c1 = new ConceptNode();
	       
		    ConceptToNode tocon = new ConceptToNode();
	            c1 = ll[i].concept;
	            tocon = ll[i].destination;
	            c1 = temp1.colnext;
		    if(c1 != null)
		    {
			System.out.println("The UW"+c1.uwconcept);
		
		    }
		    ArrayList  Deconsentence =new ArrayList();
	            ArrayList  Deconsentence_final =new ArrayList();
	            String sentenceidcheck=null;
	            ArrayList deconsubgraph= new ArrayList();
	            String attribute_main="";
	         
	            if(c1.docid.contains("en")){
	            	inputlanguage_id="eng";
	            }else if(c1.docid.contains("ta")){
	            	inputlanguage_id="ta";
	            }
	            System.out.println("the language id :"+inputlanguage_id);
		  while(c1!= null)
		    {
			System.out.println("The UW"+c1.uwconcept);
	
			if(sentenceidcheck!=c1.sentid){
 				System.out.println("the deconverted sentencetttttttttttttttttt:"+Deconsentence.toString()); 		
 				System.out.println("the deconverted subgraph array size:"+deconsubgraph.size()); 		
 				System.out.println("the deconverted subgraph"+deconsubgraph.toString()+c1.docid); 			
 				Deconsentence =new ArrayList();
 				deconsubgraph=new ArrayList();
 				Deconsentence_final=new ArrayList();
 				if(deconsubgraph.size()==1){
 					System.out.println("inside if 1");
 					break;
 				}
 			}
	           	tocon = c1.rownext;
			if(tocon != null)	
			{
				while (tocon != null) {
				  String Toconcept;
	                        String tamcon;
	                        String param;
	                        String pos;
	                        String index_tamil = c1.gn_word;
	                        String reln = tocon.relnlabel;
	                        String fileName="";
	                        String lang_id2="";
	                    	ConceptNode c2=getConcept(tocon.sentid,tocon.uwtoconcept,ll[i]);
	                    	if(c2!=null){
	                    	String attribute_connected="";	                    	
	                    	if(inputlanguage_id.equals("eng")){
	                    		System.out.println("inputlanguage id inside sub while loop"+inputlanguage_id);
	                    	if(c2.uwconcept.contains("agt")||c2.uwconcept.contains("obj")||c2.uwconcept.contains("action")||c2.uwconcept.contains("do")){
	            				attribute_connected="@Verb";
	            			}else{
	            				attribute_connected="@Noun";
	            			}
	                    	}else{
	                    		attribute_connected=c2.attrb;
	                    	}
	                    	System.out.println("the attribute main:"+attribute_main);
	                    	System.out.println("the attribute connected:"+attribute_connected);
				System.out.println("tocon"+ c1.gn_word+"\t"+tocon.relnlabel+"\t"+tocon.uwtoconcept+c1.sentid);
				String gen=mapRelrule(reln,c1.uwconcept,c2.uwconcept,attribute_main,attribute_connected,lang_id2,c1.gn_word,c2.gn_word,fileName);
				System.out.println("the generated:"+ gen);
 				
 				if(gen!=null){
 					try{
 				StringTokenizer stnew =new StringTokenizer(gen,"@");
 			
 				String gen1=stnew.nextToken();
 				String gen2=stnew.nextToken();
 				
 				if(!Deconsentence.contains(gen1))
 				Deconsentence.add(gen1);
 				if(!Deconsentence.contains(gen2))
 				Deconsentence.add(gen2);
 					}catch(Exception e){};
 				}
 				if(!deconsubgraph.contains(c1.uwconcept+reln+c2.uwconcept)){
 				deconsubgraph.add(c1.uwconcept+reln+c2.uwconcept);
 				}

				tocon = tocon.getRowNext();
				}
			}
			sentenceidcheck=c1.sentid;
		    }
			c1=c1.getColNext();
		    }
		  if(deconsubgraph.size()==1){
			  System.out.println("inside if 2");
				break;
			}
		}
	 		
 		}catch(Exception e){e.printStackTrace();}
 }	 				*/
		
		
		
		/*processGraphNew() gets the UNL graph as input and passes every sentence subgraph for Deconversion
		 * 
		 * 
		 */
		public void processGraphNew(FinalLLImpl ll)throws Exception{
	 		int count=0;
	 		getRulesfromFile("");
	 		loadTamil_priority_window();
	 		try{
	 	           int flg = 0;
		            HeadNode temp1 = new HeadNode();
		            temp1 = ll.head;
		            ConceptNode c1 = new ConceptNode();		       
			        ConceptToNode tocon = new ConceptToNode();
		            c1 = ll.concept;
		            tocon = ll.destination;
		            c1 = temp1.colnext;			    
			    	StringBuffer Deconsentence=new StringBuffer();
			    	ArrayList deconnew=new ArrayList();
		            String sentenceidcheck=new String();
		            ArrayList deconsubgraph= new ArrayList();
		            String attribute_main="";
		         	//inputlanguage_id="ta";
		            System.out.println("the language id :"+inputlanguage_id);		             
		       //     int max_index=tamil_priority_window.size()-1;
		           // int index=0;
		            Hashtable<String,String> conidkeys=new Hashtable<String,String>();
		            Hashtable<String,String> Toconidkeys=new Hashtable<String,String>();
		            ArrayList senidkeys=new ArrayList();
		            ArrayList priorityArraycon=new ArrayList();
		            ArrayList priorityArrayrel=new ArrayList();
		            Hashtable<String,String> relcon=new Hashtable<String,String>();
		         //   Hashtable<String,String> CRC_features=new Hashtable<String,String>();
		            ArrayList deconvertedsentence =new ArrayList();
		            for(int h=0;h<tamil_priority_window.size();h++){
		            	
		            	 deconvertedsentence.add("string");
		            }
		            int senflag=0;
		            while(c1!= null)
		            	{
		            		System.out.println("The UW"+c1.uwconcept+"sentence id:"+c1.sentid);	
		            		if(!sentenceidcheck.equals(c1.sentid)){
		            			System.out.println("conidkeys start loop"+conidkeys.toString());
		            			conidkeys=new Hashtable<String,String>();
		            			Deconsentence.append(".").append("\n");
		            		}			
		            		tocon = c1.rownext;
		            		ConceptNode c2=new ConceptNode();
		            		deconvertedsentence.add("string");
		            		if(tocon != null)	
		            		{	
		            			while (tocon != null) {
		            			String Toconcept;
		                        String tamcon;
		                        String param;
		                        String pos;
		                        String index_tamil = c1.gn_word;
		                        String reln = tocon.relnlabel;
		                        String fileName="";
		                        String lang_id2="";
		                    	c2=getConcept(tocon.sentid,tocon.uwtoconcept,ll);
		                    	deconvertedsentence.add("string");
		                    	if(c2!=null){		                    		
		                    		String attribute_connected="";	                    	
		                    		attribute_main=c1.attrb;
		                    		attribute_connected=c2.attrb;
		                    		System.out.println("the attribute main:"+attribute_main);
		                    		System.out.println("the attribute connected:"+attribute_connected);
		                    		System.out.println("tocon"+ c1.gn_word+"\t"+tocon.relnlabel+"\t"+tocon.uwtoconcept+c1.sentid);
		                    		String gen=mapRelrule(reln,c1.uwconcept,c2.uwconcept,attribute_main,attribute_connected,lang_id2,c1.gn_word,c2.gn_word,fileName);
		                    		System.out.println("the generated:"+ gen+"reln:"+reln);	 
		                    		
		                    		if(gen!=null){
		                    			
		                    			try{
		                    				
		                    				StringTokenizer stnew =new StringTokenizer(gen,"@");	
		                    				String gen1="";
		                    			    String gen2="";
		                    			    try{
		                    			    gen1=stnew.nextToken().toString();
		                    		        gen2=stnew.nextToken().toString();	 
		                    			    }catch(Exception e ){System.out.println("error"+e);}
		                    			    System.out.println("conidkeys:"+conidkeys.toString()+"c1.conceptid:"+c1.conceptid+"c1.uwconcept:"+c1.uwconcept+"c2.conceptid"+c2.conceptid+"c2.uwconcept:"+c2.uwconcept);//+"decon buffer:"+Deconsentence.toString());
		                    			    System.out.println("gen1:"+gen1);
		                    			    System.out.println("gen2:"+gen2);      			
		                    				if(!deconnew.contains(gen1)){
		                    					deconnew.add(gen1);
		                    				}if(!deconnew.contains(gen2)){
		                    					deconnew.add(gen2);
		                    				}		                    			
		                	 			 	priorityArraycon.add(gen1);//second C in CRC
		                	 			 	priorityArrayrel.add(reln);
		                	 			 	System.out.println("the gen2 before relcon:"+gen1);		 					
		                	 			 	relcon.put(reln, gen1);
		                	 			 	System.out.println("the  relcon:"+relcon.toString());		                	 		  		       			
		                    				priorityArraycon.add(gen2);//FIRST C in CRC
		                    				priorityArrayrel.add("None");
		                    				System.out.println("the gen2 before relcon:"+gen2);	 				
		                    				relcon.put("None"+c1.conceptid, gen2);
		                    				System.out.println("the  relcon:"+relcon.toString());	 			 	
		                    		    	
		                    			}catch(Exception e){};
		                    		}
		                    		if(!deconsubgraph.contains(c1.uwconcept+reln+c2.uwconcept)){
		                    			deconsubgraph.add(c1.uwconcept+reln+c2.uwconcept);
		                    		}
		                    	} 
		                    	tocon = tocon.getRowNext();					
		            			}				
		            		}
				
		            		System.out.println("sentence id check:"+sentenceidcheck);				
		            		if(!sentenceidcheck.equals(c1.sentid)){					
		            			System.out.println("the deconverted sentencetttttttttttttttttt:"+Deconsentence.toString()); 	
		            			//rearrangeSentence(priorityArraycon,priorityArrayrel,relcon);//this method makes use of fixed word order predefined for UNL relations seperately for  tamil and english
		            			System.out.println("the deconverted subgraph array size:"+deconsubgraph.size()); 		
		            			System.out.println("the deconverted subgraph"+deconsubgraph.toString());
		            			priorityArraycon =new ArrayList();
		            			priorityArrayrel =new ArrayList(); 			
		            			relcon=new Hashtable<String,String>();
		            			System.out.println("the deconverted sentence:"+deconnew.toString()); //main arraylist that contains output	
		            			deconnew=new ArrayList();
		            		     deconvertedsentence =new ArrayList();
		            			conidkeys=new Hashtable<String,String>();
		            			Toconidkeys=new Hashtable<String,String>();
		            					  
		            		}
				sentenceidcheck=c1.sentid;				
				c1=c1.getColNext();
			    }
			    System.out.println("the  relcon before invoking :"+relcon.toString());
				ArrayList deconresultnew=arrangeGenTerms_wordorder(deconvertedsentence,relcon);//This method makes use of statistical word order technic for tamil to arrange terms correctly after generation
			   	
				System.out.println("the deconverted subgraph array size:"+deconsubgraph.size()); 		
				System.out.println("the deconverted subgraph"+deconsubgraph.toString()); 	 			
			    if(deconsubgraph.size()==1){
				  System.out.println("inside if 2");
				}
		}catch(Exception e){e.printStackTrace();}
	 }	 					
		
public ArrayList arrangeGenTerms_wordorder(ArrayList deconvertedsentence,Hashtable<String,String>relcon){
	
	System.out.println("relcon:"+relcon.toString());
	ArrayList relations=new ArrayList();
	ArrayList index=new ArrayList();
	Hashtable<String,Integer>rel_index=new 	Hashtable<String,Integer>();
	Enumeration keys=relcon.keys();
	String reqd=new String();
	ArrayList rels=new ArrayList();
		while( keys.hasMoreElements() ) 
 		{
			System.out.println("************************");
			Object aKey = keys.nextElement();
			reqd=aKey.toString();
			rels.add(reqd);
			Object val=relcon.get(aKey);
			System.out.println("the key in arrange word order:"+reqd+"the value:"+val.toString());
 		}
		
		 Map m = new HashMap();
		for(int i=0;i<rels.size();i++){
			String s1=rels.get(i).toString();
			int position=0;
			ArrayList val=new ArrayList();
			System.out.println("STRING S1:"+s1);
			
			if( WordOrderHash.containsKey(s1)){				
				System.out.println("the key matched:"+s1);
				val=WordOrderHash.get(s1);
				System.out.println("VAL:"+val);
			}
			for(int j=0;j<rels.size();j++){
				String s2=rels.get(j).toString();	
				System.out.println("STRING S2:"+s2);
					if(!val.contains(s2)||s1.contains("None")){
						position++;
					}				
			}
			index.add(position);
			m.put(s1, position);
		}
		
		System.out.println("index array:"+index.toString());
		System.out.println("The passed HashMap:"+m.toString());
		List sortedArray=sortByValue(m);
		System.out.println("The list returned:"+sortedArray.toString());
		ArrayList finaldeconarray=new ArrayList();
		for(int k=0;k<sortedArray.size();k++){			
			String key=sortedArray.get(k).toString();
			String con=relcon.get(key);
			System.out.println("the length of con:"+con.length()+"con:"+con);
			if(!finaldeconarray.contains(con) && con.length()!=0){
			finaldeconarray.add(con);
			}
			
		}
		
		
		System.out.println("The decon sentence from relcon::"+finaldeconarray.toString());
	return finaldeconarray;
}
public static List sortByValue(final Map m) {
    List keys = new ArrayList();
    keys.addAll(m.keySet());
    Collections.sort(keys, new Comparator() {
        public int compare(Object o1, Object o2) {
            Object v1 = m.get(o1);
            Object v2 = m.get(o2);
            if (v1 == null) {
                return (v2 == null) ? 0 : 1;
            }
            else if (v1 instanceof Comparable) {
                return ((Comparable) v1).compareTo(v2);
            }
            else {
                return 0;
            }
        }
    });
    return keys;
}		
public ArrayList arrangeGenTerms(Hashtable<String,String> hash, ArrayList decon){
	Hashtable<String,String> hashnew=new Hashtable<String,String>();
   Enumeration keys = hash.keys();
   while( keys.hasMoreElements() ) 
    {
        Object aKey = keys.nextElement();
        Object aValue = hash.get(aKey);
        String key=aKey.toString();
        StringTokenizer st=new StringTokenizer(key,"@");       
        String reln=new String();
        String gen1=new String();
        String gen2=new String();
         if(key!=null){
        	 gen1=st.nextToken().toString();
          	 reln=st.nextToken().toString();        
        	if(gen2!=null){
        		 
        		 hashnew.put(gen1, aValue.toString());
        	 }
         }
         System.out.println("size of priority window:"+tamil_priority_window.size());
         System.out.println("size of decon"+decon.size());
         if(tamil_priority_window.contains(reln)){   	 
        	 int index =tamil_priority_window.indexOf(reln);
        	 decon.add(index, gen1);
         }
    }
    
    
    Collections.sort(decon);       
     decon=  insertMainConcepts(decon,hashnew);    
    System.out.println("ArrayList elements after sorting in ascending order : "+decon.toString());    
  
	
	return decon;
}
	public ArrayList insertMainConcepts(ArrayList decon,Hashtable<String,String> hash){
		
	
		   Enumeration keys = hash.keys();
		   Hashtable<String,String> hashnew=new Hashtable<String,String>();
		   int i=0;
		    while( keys.hasMoreElements() ) 
		    {
		        Object aKey = keys.nextElement();
		        Object aValue = hash.get(aKey);
		        String s1=aValue.toString();
		        StringTokenizer st=new StringTokenizer(s1,"@");
				String main_con=st.nextToken();
		        if(s1.contains("@Verb")){					
					
					decon.add(decon.size()-1,main_con);	
				}else{
					
				  decon= fixPosition(decon,main_con,i);
				}
		        i++;
		    }
		return decon;
	}
		
	public ArrayList fixPosition(ArrayList decon,String s1, int i){
		int j=0;
		while(i>j){
			
			if(decon.get(i).toString().equals("string")){
				
				i++;
			}else{
			break;	
			}			
		}
		decon.add(i,s1);
		
		return decon;
		
	}
public void rearrangeSentence(ArrayList con,ArrayList rel,Hashtable relcon){
	

	ArrayList sortedCons=new ArrayList();
	System.out.println("involved relations are :"+rel.toString());
	for(int i=0;i<english_priority_window.size();i++){
		sortedCons.add("emptystr");
	}
		Enumeration keys=relcon.keys();
		ArrayList indices=new ArrayList();
		int index=0;
		while( keys.hasMoreElements() ) 
 		{
			//System.out.println("************************");
			Object aKey = keys.nextElement();
			Object val=relcon.get(aKey);
			
			if(english_priority_window.contains(aKey)){
			index=english_priority_window.indexOf(aKey);
			
			}else{
				System.out.println("the left relation::"+aKey.toString());
			}
			if(!sortedCons.contains(val)){
			sortedCons.add(index,val);
			//sortedCons.r
			}
 		}
		ArrayList newsortedcons=new ArrayList();
		int counter=0;
		for(int j=0;j<sortedCons.size();j++){  //removal of empty str string 
			
			String toAdd=sortedCons.get(j).toString();
			if(!toAdd.equals("emptystr") && !newsortedcons.contains(toAdd)){
				
				newsortedcons.add(toAdd);    
				indices.add(counter);
				counter++;
				
			}
		}
		System.out.println("the sorted con array:"+newsortedcons.toString());	
		
		for(int k=0;k<indices.size();k++){
			if(k<indices.size()-1){
			int index1=(Integer)indices.get(k);
			int index2=(Integer)indices.get(k+1);
			
			boolean consecutive_check=checkConsecutive(index1,index2);
			if(consecutive_check==true){
				index2=index2+1;
				indices.add(k+1,index2);				
			}
			}			
		}
		
		ArrayList sorted1=new ArrayList();
		for(int l=0;l<indices.size()*2;l++){
			
			sorted1.add("emptystring");
		}
		ArrayList sorted2=new ArrayList();
		
		for(int m=0;m<sorted1.size();m++){
				
			int ind1=(Integer)indices.get(m);
		}
		
		
		/*for(int k=0;k<newsortedcons.size();k++){
			
			String word=newsortedcons.get(k).toString();
			Enumeration keys1=relcon.keys();
			
			Object aKey1 = new Object();
			Object val1=new Object();
			while( keys1.hasMoreElements() ) 
	 		{
				//System.out.println("************************");
			aKey1 = keys1.nextElement();
			 val1=relcon.get(aKey1).toString();	
				if(val1.equals(word)){
					break;
				}
				
	 		}
			System.out.println("the con in new sorted con array:"+word);	
			System.out.println("the rel in new sorted con array:"+aKey1.toString());	
		}*/

}

	public boolean checkConsecutive(int index1,int index2){
	
	if(index2==index1+1){
		
		return true;
	}else{
		return false;
	}
	
}
		 public ConceptNode  getConcept(String senid,String conceptid,FinalLLImpl  ll){			 	
		 	 HeadNode temp1 = new HeadNode();
		      temp1 =ll.head;
		      ConceptNode c1 = new ConceptNode();
		      c1=temp1.colnext;
		      String req="";
		      while(c1!=null){
		    	  if((c1.sentid.equals(senid)) && (c1.conceptid.contains(conceptid))){	     
		     	     req=c1.uwconcept;
		     		 break;	     		 
		    	  }  	
		     	 
		     	 c1=c1.colnext;
		     }
		 	
		 	return c1;
		 }

		
	
	public ArrayList loadPriorityArrayList(){
		
		relation_priority.add("and");
		relation_priority.add("agt");
		relation_priority.add("pos");
		relation_priority.add("verb");		
		relation_priority.add("ptn");
		relation_priority.add("mod");	
		relation_priority.add("obj");
		relation_priority.add("tim");
		relation_priority.add("tmf");
		relation_priority.add("tmt");
		relation_priority.add("plf");
		relation_priority.add("plt");
		relation_priority.add("plc");
		relation_priority.add("frm");
		relation_priority.add("gol");
		relation_priority.add("pur");
		relation_priority.add("man");
		relation_priority.add("qua");		
		relation_priority.add("ben");
		relation_priority.add("ins");
		relation_priority.add("via");
		return relation_priority;		
	}
	
	
	
	public void getRulesfromFile(String FileName) throws Exception{
			System.out.println("outside file");
			//FileInputStream fis1 = new FileInputStream("./XMLFiles/rulesta.txt");
			FileInputStream fis1 = new FileInputStream("./decon_inputs/rulesta.txt");
			InputStreamReader isr1 = new InputStreamReader(fis1);
			BufferedReader in1 = new BufferedReader(isr1);
			String tempVar = in1.readLine();	
			String relation="";
			String cases="";
			String condition="";
			int counter=0;
			try{
				while (null != tempVar) {						
						counter++;
				        System.out.println("tempVar:"+tempVar);
						if(counter==1 ){
							StringTokenizer st1=new StringTokenizer(tempVar,":");
							st1.nextToken();							
							inputlanguage_id=st1.nextToken();
							System.out.println("the input language id:"+inputlanguage_id);
							
						}
						else if(counter==2){
							
							StringTokenizer st1=new StringTokenizer(tempVar,":");
							st1.nextToken();
							targetlanguage_id=st1.nextToken();
						    System.out.println("the target language id:"+targetlanguage_id);
						}
						 if(counter>=4){
							StringTokenizer st=new StringTokenizer(tempVar,"@");
							while(st.hasMoreTokens()){
							 relation=st.nextToken();
							 cases=st.nextToken();
							 System.out.println("cases:"+cases);
							 condition=st.nextToken();
							 System.out.println("condition:"+condition);
						
							}		
							  relations_rules.put(relation,cases+"@"+condition);
							if(tempVar.equalsIgnoreCase("relationruleends")){
								break;								
							}
						}					
				
				tempVar = in1.readLine();
			}
			}catch(Exception e){}
			System.out.println("Relation rule afte get rul from file:"+relations_rules.toString());
		    
	isr1.close();
	fis1.close();
	in1.close();
	
	}
	/*mapRelRule() takes the CRC and its components needed for deconversion as input 
	 * Uses respective Rules loaded in relations_rules hashtable that directs usage of correct case endings for each UNL relation
	 * These case endings along with the natural language term is passed on for Generation according to the target language
	 * The input natural language term is identified either from the UNL graph directly or taken from the UW dictionary according to the input-target language compatibility 
	 * 
	 */
	public String mapRelrule(String rel,String con,String tocon,String attri1,String attri2,String lb,String term1, String term2,String fileName){
		System.out.println("in map rel rule");
		String genword="";
 		String genword1="";
 		String cases="";
 		String position="";
		String condition="";
 		String firstword="";
		String case1="";
		String position1="";
		String secondword="";
		String case2="";
		String position2="";
		String req_term1=null;
		String req_term2=null;
		try{
			if(inputlanguage_id.equals("ta") && targetlanguage_id.equals("ta")){
				req_term2=term2;/* The natural language term is obtained from the UNL graph directly*/
				term2=req_term2;/* The natural language term is obtained from the UNL graph directly*/
				System.out.println("the term1:"+term1);
				System.out.println("the term2:"+term2);
			}
			else if(inputlanguage_id.equals("ta")&& targetlanguage_id.equals("eng")){
				StringTokenizer stlng=new StringTokenizer(tocon,"(");
				req_term2=stlng.nextToken();
				term2=req_term2;	/* The natural language term is obtained from the UNL graph directly*/
				System.out.println("the term2:"+term2);
				StringTokenizer stlng1=new StringTokenizer(con,"(");
				req_term1=stlng1.nextToken();
				term1=req_term1;	/* The natural language term is obtained from the UNL graph directly*/
				System.out.println("the term1:"+term1);
			}else if(inputlanguage_id.equals("eng")&& targetlanguage_id.equals("eng")){
				req_term2=term2;/* The natural language term is obtained from the UNL graph directly*/
				term2=req_term2;/* The natural language term is obtained from the UNL graph directly*/
				System.out.println("the term1:"+term1);
				System.out.println("the term2:"+term2);
			}else if(inputlanguage_id.equals("eng")&& targetlanguage_id.equals("ta")){
	           // term1=getTamilword(term1);/* The natural language term is obtained from the UW dictionary loaded in BST tree*/
			//	term2=getTamilword(term2);/* The natural language term is obtained from the UW dictionary loaded in BST tree*/
				int hash1=term1.hashCode();
				int hash2=term2.hashCode();
				 BSTNode_enghashkey bn1=new  BSTNode_enghashkey ();
				 BSTNode_enghashkey bn2=new  BSTNode_enghashkey (); 
				
			bn1=bst_en.search(hash1);
			bn2=bst_en.search(hash2);
			if(bn1!=null){
			 term1=bn1.lexeme;
			}if(bn2!=null){
			 term2=bn2.lexeme;
			}
			    System.out.println("the term1:"+term1+"term2:"+term2);				
			}
				String required=relations_rules.get(rel).toString();/*The hashtable which contains correct case endings */
				System.out.println("the string required:"+required);
				StringTokenizer st=new StringTokenizer(required,"@");
				cases=st.nextToken();
				condition=st.nextToken();		
			    	StringTokenizer st1=new StringTokenizer(cases,"&");
					st1.nextToken();
					case1=st1.nextToken();
					position1=st1.nextToken();
					st1.nextToken();
					case2=st1.nextToken();
					position2=st1.nextToken();
					System.out.println("case1:"+case1);
					System.out.println("position1:"+position1);
					System.out.println("tocon:"+tocon);
					System.out.println("case2:"+case2);
					System.out.println("position2:"+position2);
					System.out.println("term:"+term1);
					genword=generateword2(case1,position1,term2,attri2);/*The case endings along with the natural language term is passed for generation*/
					genword1=generateword1(case2,position2,term1,attri1);/*The case endings along with the natural language term is passed for generation*/
			System.out.println("the gen word1:"+genword1);
			System.out.println("the gen word2:"+genword);
		}catch(Exception e){}
		return genword+"@"+genword1;
	}
public String generateword1(String case2,String position2,String term1,String attri)throws Exception{
	
	System.out.println("the term in generate word1:"+term1+"the atrribute:"+attri+"case2:"+case2);
	String genword=new String();
	//
	if(case2.contains("conditions")){
			if(case2.contains("plural")||case2.contains("singular")){
				if(attri.contains("@plural") && case2.contains("AAG")){
				genword= generate(term1,case2,position2,attri);/*The case endings along with the natural language term is passed for generation where morphological generator is used*/
				System.out.println("gen word:"+genword);/*The case endings along with the natural language term is passed for generation where morphological generator is used*/
				}else{
				genword=term1;/*No generation required for the natural language term*/
				}
			}}else if(attri.contains("@plural") && attri.contains("@Noun")){
				genword= generate(term1,case2,position2,attri);/*The case endings along with the natural language term is passed for generation where morphological generator is used*/
				System.out.println("gen word:"+genword);/*The case endings along with the natural language term is passed for generation where morphological generator is used*/
				}			
		
		else if(case2.equals("NC")){
			genword= term1; /*No generation required for the natural language term*/
			System.out.println("gen word:"+genword);
		 }
		else if((!case2.equals("NC")) && position2.equals("AAG")){
			
			genword= generate(term1,case2,position2,attri); /*The case endings along with the natural language term is passed for generation where morphological generator is used*/
			System.out.println("gen word:"+genword);
		}else if(!(case2.equals("NC")) && position2.equals("AANG")){
			
			genword= term1+case2; /*case ending is just appended with  the natural language term without using morphological generator*/
			System.out.println("gen word:"+genword);
		}	
		else if(case2.equals("NC") && position2.equals("ABNG")){			
		
			genword= case2 +term1; /*case ending is just appended with  the natural language term without using morphological generator*/
		}else if((attri.contains("@Verb")) && position2.equals("AAT")){			
			genword= generate(term1,case2,position2,attri); /*The case endings along with the natural language term is passed for generation where morphological generator is used*/
		}else if( attri.contains("@Verb")){		
			genword= generate(term1,case2,position2,attri); /*The case endings along with the natural language term is passed for generation where morphological generator is used*/
		}			
		else if( attri.contains("@FiniteVerb")){		
			genword= term1; /*The case endings along with the natural language term is passed for generation where morphological generator is used*/
		}
		else{			
			genword=term1;/*No generation required for the natural language term*/
		}
	
    System.out.println("the gen word inside genword1:"+genword);
	return genword;
	
}
public String generateword2(String case1,String position1,String term2,String attri)throws Exception{
	String genword=new String();
	System.out.println("attri inside generate 1"+attri);
	System.out.println("the term in generate word2:"+term2+"the atrribute:"+attri+"case1:"+case1+"position:"+position1);
	if(case1.contains("conditions")){
		if(case1.contains("plural")||case1.contains("singular")){
			if(attri.contains("@plural") && case1.contains("AAG")){
			genword= generate(term2,case1,position1,attri); /*The case endings along with the natural language term is passed for generation where morphological generator is used*/
			System.out.println("gen word:"+genword);
			}else{
			genword=term2;/*No generation required for the natural language term*/
			}
		}		
	}else if(attri.contains("@plural")){
		genword= generate(term2,case1,position1,attri); /*The case endings along with the natural language term is passed for generation where morphological generator is used*/
		System.out.println("gen word:"+genword);
		}
	else if(case1.equals("NC")){
		genword= term2; /*No generation required for the natural language term*/
		System.out.println("gen word:"+genword);
	 }
	else if(!case1.equals("NC") && position1.equals("AAG")){
		if(case1.contains("SW1")){
		StringTokenizer st=new StringTokenizer(case1,"%");
		String s1="";
		ArrayList a=new ArrayList();
		while(st.hasMoreTokens()){
			s1=st.nextToken();
			System.out.println("tokens"+s1);
			a.add(s1);
		}
		
		if(attri.contains("Noun")){
			case1=a.get(1).toString();
		}else{
			case1=a.get(4).toString();
		}
		genword= generate(term2,case1,position1,attri); /*The case endings along with the natural language term is passed for generation where morphological generator is used*/
		}else{
		genword= generate(term2,case1,position1,attri); /*The case endings along with the natural language term is passed for generation where morphological generator is used*/
		}
		System.out.println("gen word:"+genword);
	}else if((!case1.equals("NC")) && position1.equals("AANG")){
		genword= term2+case1;  /*case ending is just appended with  the natural language term without using morphological generator*/
		System.out.println("gen word:"+genword);
	}		
	else if((!case1.equals("NC")) && position1.equals("ABNG")){			
		genword= case1 +term2;  /*case ending is just appended with  the natural language term without using morphological generator*/
	}else if((attri.contains("@Verb")) && position1.equals("AAT")){			
		
			genword= generate(term2,case1,position1,attri); /*The case endings along with the natural language term is passed for generation where morphological generator is used*/
		}else if( attri.contains("@FiniteVerb")){		
			genword= term2; /*The case endings along with the natural language term is passed for generation where morphological generator is used*/
		}

	else{
		
		genword=term2;/*No generation required for the natural language term*/
	}
	System.out.println("the gen word inside genword 2:"+genword);
return genword;
	
}

/**
 * 
 * @param term
 * @param cases
 * @param position
 * @param attri
 * @return
 * @throws Exception
 * invokes the english and tamil morphological generator according to the target language
 */
public String generate(String term,String cases,String position,String attri )throws Exception{
	String genword="";
	System.out.println("have i come inside generation and the term:"+term+targetlanguage_id+inputlanguage_id);
	try{

	if(targetlanguage_id.equals("ta")  && inputlanguage_id.equals("ta") ||targetlanguage_id.equals("ta")  && inputlanguage_id.equals("eng") ){
		System.out.println("cases:"+cases);
		 if(attri.contains("@Verb")){		
			word_verb w=new word_verb();
			StringBuffer sb1=new StringBuffer();
			if(cases.contains("வேண்டி")){
				System.out.println("inside vendi");				
				sb1= w.verbgeneration(term,"","வினை+எச்சம்+நிலை+(பால்/இட/எண்)/(தொ.பெ.விகுதி)/(+வே.உ)","வேண்டி");/*invokes Tamil morphological generator for verb*/
				genword= sb1.toString();
			}else if(attri.contains("passive")){
				
				  sb1= w.verbgeneration(term,attri,"வினை+எச்சம்+நிலை+(பால்/இட/எண்)/(தொ.பெ.விகுதி)/(+வே.உ)","");
				  genword= sb1.toString();
			}else if(attri.contains("FiniteVerb")){
				
				  genword= sb1.toString();
			}else if(attri.equalsIgnoreCase("[@Verb")){
					
				genword=term;
				
			}else if(attri.contains("@present") && attri.contains("@progress")){
					
				 sb1= w.verbgeneration(term,attri,"வினை+உம்+பெயரெச்ச பின்னொட்டு","");
				  genword= sb1.toString();
				
			}else{
				System.out.println("inside PNG");
				String rule="வினை+காலம்+பால்/இட/எண்";			   		
		   		sb1= w.verbgeneration(term,attri,rule,"");/*invokes Tamil morphological generator for verb*/
		   		genword= sb1.toString();
		   		System.out.println("word"+sb1.toString());	
				
			}
	
			
		}else if(attri.contains("@Noun")|| attri.contains(".@of")||attri.contains("@Pronoun")){
		genword= generateTaNoun(term,cases,position,attri); /*invokes Tamil morphological generator for noun*/
		}
	
		}
	else if(targetlanguage_id.equals("eng")){
		EnglishMorphologicalGenerator mg=new EnglishMorphologicalGenerator();
		System.out.println("inside generator");
		mg.loadMemory();
		System.out.println("term in gen:"+term);
		System.out.println("attri in gen:"+attri);
	  StringTokenizer st=new StringTokenizer(term);
	  ArrayList termtokens=new ArrayList();
	  while(st.hasMoreTokens()){
		  termtokens.add(st.nextToken().toString());
		  
	  }
	  if(termtokens.size()>1){/*Checks if the term is a multi word.If the term is  a multi word then its not passed into EngMorphological generator*/ 
		  
		  genword=term;
	  }else{
		genword= mg.generateEnglishWord(term,attri);/*invokes English Morphological generator*/
	  }
	}
	System.out.println("the generated word finally:"+genword);
	}catch(Exception e){e.printStackTrace();}
	return genword;
}
/**
 * invokes Tamil Morphological gen erator for generating noun
 * 
 */
	public String generateTaNoun(String word,String cases,String position,String attri)throws Exception{
		String generated="";
		 StringBuffer sb=new StringBuffer();
	try{
		 word_noun w=new word_noun();
		 String caseending="";		    
		 System.out.println("the word in generator:"+word);     
		if(position.equals("AAG")){
			if(attri.contains("@plural")){
				if(cases.equals("இல்")||cases.equals("இன்")){
				System.out.println("inside plural");
				sb= w.NounCMGen1(word,"பெயர்ச்சொல்+பன்மை+வேற்றுமை உருபு",cases);
				}
				else{
				 sb= w.NounCMGen1(word,"பெயர்ச்சொல்+பன்மை",cases);
				}
				
			}else{
				if(cases.equals("இன்")||cases.equals("ஆன")||cases.equals("இய")||cases.equals("உள்ள")||cases.equals("அற்ற")||cases.equals("ஆக")||cases.equals("இல்")){
					System.out.println("inside in");
					 sb= w.NounCMGen1(word,"பெயர்ச்சொல்+உரிச்சொல் ஈற்றசை",cases.trim());
				}else if (cases.equals("இல்")){
				   sb= w.NounCMGen1(word,"பெயர்ச்சொல்+வேற்றுமை உருபு",cases);
				} else if (cases.equals("Noun")){
					
					sb.append(word);
				}				
					else{
				
				   sb= w.NounCMGen1(word,"பெயர்ச்சொல்+வேற்றுமை உருபு",cases);
				}                                                              
				  
				}
			generated=sb.toString();
		}else if(position.equals("AANG")){
			
			 generated=word+" "+cases;
		}else if(position.equals("NA")){
			
			 generated=word;
		}		 
	}catch(Exception e){}
	System.out.println("generated in genta:"+generated);
		return generated;
		
	}	
	/*public void loadEngFeatures(){
		 vowel.add('a');
		 vowel.add('e');
		 vowel.add('i');
		 vowel.add('o');
		 vowel.add('u');
		 oesexception.add("photo");
		 oesexception.add("zero");
		 oesexception.add("piano");
	}*/
	
	
	/*public StringBuffer classifyVerb(String anal,String word){
		 StringBuffer sb1=new StringBuffer();
		 
		try{		
		word_verb w=new word_verb();	
		String rule="";
		String tense="";
		String person="";
		String number="";
		StringBuffer sb=new StringBuffer();
		System.out.println("TAMIL WORD"+word);
		System.out.println("ANAL:"+anal);	
		StringTokenizer st=new StringTokenizer(anal,"+");
		String s2="";
		while(st.hasMoreTokens()){			
			s2=st.nextToken();		
		}
		System.out.println("s2222222222222"+s2);
		StringTokenizer st1=new StringTokenizer(s2,">");
         String s1="";
         int counter=0;
         s1=st1.nextToken();
		
            if(anal.contains("அ") && anal.contains("Infinitive Suffix") && !anal.contains("Genitive Case")){
            	verbinfinitive vf=new verbinfinitive();
            	String umout=vf.verbgetinfi(word,"");
				System.out.println("umout  "+umout);
				sb1.append(umout);
            }
            else if((anal.contains("ஆவிடில்")&& anal.contains("Negative Conditional Suffix"))||(anal.contains("ஆது")&& anal.contains("Future Negative  Suffix" ))){
				rule="வினை+எச்சம்+எதிர்மறை";
			
				 System.out.println("analysed :"+anal+"rule:"+rule);
			     sb1= w.verbgeneration(word,anal,rule,s1);
			     System.out.println("word"+sb1.toString());
		    }
		   else if((anal.contains("மாறு")&& anal.contains("Post Position"))||(anal.contains("உடன்")&& anal.contains("Associative Case"))||((anal.contains("படி")&& anal.contains("Post Position")) && (anal.contains("ஆல்")&& anal.contains("Instrumental Case")))||(anal.contains("பிறகு  ") && anal.contains("Post Position"))||(anal.contains("பொழுது") && anal.contains("Post Position ") )||(anal.contains("போது ") && anal.contains("Post Position"))||(anal.contains("போதிலும்")&& anal.contains("Post Position") )){ 
			rule="வினை+காலம்+பெ.எச்சம்+பெயரெச்ச பின்னொட்டு";
			System.out.println("analysed :"+anal+"rule:"+rule);
			  sb1= w.verbgeneration(word,anal,rule,s1);
			   System.out.println("word"+sb1.toString());
		   }
	   
		   else if((anal.contains("அ") && anal.contains("Relative Participle Suffix")) && (anal.contains("வ்" ) && anal.contains("Sandhi"))){
			   
			   rule="வினை+காலம்+பெ.எச்சம்+தொ.பெ";
			   System.out.println("analysed :"+anal+"rule:"+rule);
			    sb1= w.verbgeneration(word,anal,rule,s1);
			   System.out.println("word"+sb1.toString());
		   }	   
		   else if((anal.contains("ங்கள்") && anal.contains("Imperative Plural Suffix"))||anal.contains("Imperative Singular Suffix")){
			   
			   rule="ஏ.மு.ஒ.விகுதி(ISM) / ஏ.மு.ப.விகுத¤(IPM)";
			   System.out.println("analysed :"+anal+"rule:"+rule);
			  sb1= w.verbgeneration(word,anal,rule,s1);
			   System.out.println("word"+sb1.toString());
		   }
		   else if((anal.contains("வ்") && anal.contains("Sandhi") && anal.contains("Auxiliary Verb") && anal.contains("அது") && anal.contains("Infinitive Suffix"))){
			   System.out.println("have i come in");
			   rule="வினை+எச்சம்+நிலை+(பால்/இட/எண்)/(தொ.பெ.விகுதி)/(+வே.உ)";
			   System.out.println("analysed :"+anal+"rule:"+rule);
			   sb1= w.verbgeneration(word,anal,rule,s1);
			   System.out.println("word"+sb1.toString());
			   
		   }
		   else if((anal.contains("கூடும்") && anal.contains("Auxilliary Verb"))||((anal.contains("வ்")&& anal.contains("Sandhi")) && (anal.contains("அல்ல")&& anal.contains(" Negative Finite Verb Suffix")))||((anal.contains("வேண்டு") && anal.contains("Verb"))&& (anal.contains("ய்")&& anal.contains("Sandhi")))){
			   
			   rule="வினை+எச்சம்+நிலை+(பால்/இட/எண்)/(தொ.பெ.விகுதி)/(+வே.உ)";
			   System.out.println("analysed :"+anal+"rule:"+rule);
			   sb1= w.verbgeneration(word,anal,rule,s1);
			   System.out.println("word"+sb1.toString());
		   }
		   else if((anal.contains("ட்டும்")&& anal.contains("Permissive Suffix"))||(anal.contains("ஆவிட்டால் ") && anal.contains("Negative Conditional Suffix"))||(anal.contains("ஆமல்") && anal.contains("Negative Conditional Suffix"))||((anal.contains("ஆது")&& anal.contains("Future Negative Suffix"))&& (anal.contains("ஏ") &&anal.contains("Clitic")))||(anal.contains("ஆவிடில்")&& anal.contains("Negative Conditional Suffix") )||(anal.contains("மாட்ட்")&& anal.contains("Future Negative Suffix")) ||anal.contains("Third Future Neuter Singular OR RP") && anal.contains("Sandhi")){
			   
			   rule="வினை+எச்சம்+ஈற்றசை";	
			   System.out.println("analysed :"+anal+"rule:"+rule+"s1"+s1);
			    sb1= w.verbgeneration(word,anal,rule,s1);
			   System.out.println("word"+sb1.toString());
		   }
		   else if((anal.contains("ஆல்") && anal.contains("Instrumental Case"))){
			   
			   rule="வினை+இறந்தகாலம்+நிபந்தனை விகுதி";
			   System.out.println("analysed :"+anal+"rule:"+rule);
		         sb1= w.verbgeneration(word,anal,rule,s1);
			   System.out.println("word"+sb1.toString());
		   }
		   else if(((anal.contains("படி") && anal.contains("Post Position") )||(anal.contains("பொழுது") && anal.contains("Post Position"))||(anal.contains("போதிலும்") && anal.contains("Post Position"))||(anal.contains("போது") && anal.contains("Post Position") ||(anal.contains("வண்ணம்")) && anal.contains("Post Position")) ||anal.contains("Conjunction Suffix"))&& (anal.contains("Third Future Neuter Singular OR RP") && anal.contains("உம்"))){
				   rule="வினை+உம்+பெயரெச்ச பின்னொட்டு";			   
				   sb1= w.verbgeneration(word,anal,rule,s1);
				   System.out.println("word"+sb1.toString());
		   }
		   else if(anal.contains("உ") && anal.contains("Verbal Participle Suffix") && anal.contains("த்த் ") && anal.contains("Past Tense Marker")){
			   rule="வினை+எச்சம்+ஈற்றசை";	
			   s1=s1+"thth";
			   System.out.println("analysed :"+anal+"rule:"+rule+"s1"+s1);
			    sb1= w.verbgeneration(word,anal,rule,s1);
			   System.out.println("word"+sb1.toString());
			   
		   }
		   else{
			   		rule="வினை+காலம்+பால்/இட/எண்";			   		
			   		sb1= w.verbgeneration(word,anal,rule,s1);
			   		System.out.println("word"+sb1.toString());
			   
		   }
		}catch(Exception e){}
		return sb1;
	   
	}*/
	 /*   public void processTamilWords(ArrayList words){
		
		System.out.println(" tamil words"+words.toString());		
	}*/
	        
		
	
	    public String getTamilword(String sword) {
			String tamilword="";
			UWDict b =new UWDict();
			BST bs=new BST();
			bs=b.get_bst();
			tamilword=b.traverse(bs,sword);
		//	tamilword=bs.retrive_tamilword(sword);
			System.out.println("sword"+sword+"\t"+"TamilWord"+tamilword);
			return tamilword;

		}
	   public FinalLLImpl[] readGraphnew(){	    	
	    	  FinalLLImpl[] ll_new = new FinalLLImpl[50];
	    	  MultiList mlist=new MultiList();
	    	
	    	 	File f = new File("./resource/unl/Enconversion/unl-graph/");
	    	        File filename[] = f.listFiles();
	    		FinalLLImpl[] ll = new FinalLLImpl[filename.length];//25000 indicates number of documents to be in memory
	    	        FinalLLImpl[] ll_new1 = new FinalLLImpl[filename.length];
	    		      int count1 = 0;
	    		for (int i = 0; i < filename.length; i++) {//
	    		   try {
	    	                            FileInputStream fis = new FileInputStream(filename[i]);
	    	                            System.out.println("file name is " + filename[i]);
	    	                            ObjectInputStream ois = new ObjectInputStream(fis);
	    	                            ////System.out.println("reading started....");//testing
	    	                            ll_new1 = mlist.list_readfrmfile(ll_new, ois, i);
	    	                            ////System.out.println("reading end...." + ll_new1[i] + "" + ll_new1.length);//testing
	    	                            for (int j = 0; j < ll_new1.length; j++) {
	    	                                if (ll_new1[j] != null) {
	    	                                    ll[count1] = ll_new1[j];
	    	                                   
	    	                                    count1++;
	    	                                }
	    	                            }

	    	                            ois.close();
	    	                        } catch (Exception e) {
	    	                            ////System.out.println(e.getMessage());
	    	                        }
	    	                      
	    		}
	    		  return ll;
	   }	    	        
	    
	  
	   public static void main(String srgs[])throws Exception{
	     FrameworkNew d=new FrameworkNew();	
	     SAXParserNew s=new SAXParserNew();
	     FinalLLImpl ll = new FinalLLImpl();
	   		
	} 
}
