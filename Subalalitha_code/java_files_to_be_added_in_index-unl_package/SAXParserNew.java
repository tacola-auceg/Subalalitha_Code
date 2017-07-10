/**
  * version history**
  *version number:1.0 
  @author subalalitha
 */

 
/* SAXParserNew java file is used to read Data from XML files and upload the data into Multilist as UNL 
 *  
 *   
 */

package org.apache.nutch.index.unl;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.nutch.enconversion.unl.ta.ConceptNode;
import org.apache.nutch.enconversion.unl.ta.ConceptToNode;
import org.apache.nutch.enconversion.unl.ta.FinalLLImpl;
import org.apache.nutch.enconversion.unl.ta.UWDict;
import org.apache.nutch.enconversion.unl.ta.BST;
import org.apache.nutch.enconversion.unl.ta.HeadNode;
import org.apache.nutch.enconversion.unl.ta.MultiList;

public class SAXParserNew extends DefaultHandler {

	List myEmpls;
	int maincounter=0;
	private String tempVal;
	public ArrayList sen_ta=new ArrayList();
	public ArrayList sen_ma=new ArrayList();
	public FinalLLImpl[] ll_new = new FinalLLImpl[5000];
	public GraphNode tempEmp;	
	FinalLLImpl ll;
	
	public SAXParserNew()throws Exception {
		myEmpls = new ArrayList();
		 ll=new FinalLLImpl();
	}
	/*runExample method invokes parseDocument()method and printData() method to load the UNL graph into multilist
	 *           
	 */
	public FinalLLImpl runExample(String dir)throws Exception {
	
		parseDocument(dir);
		ll=printData();
		return ll;
	}                                                                                                                                                        
/*Activates SAXParser to parse the XML document
 * 
 */
	private void parseDocument(String dir)throws Exception {
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {		
			SAXParser sp = spf.newSAXParser();
			System.out.println("dir in parse:"+dir.trim());
			sp.parse(dir.trim(),this);			
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			
			ie.printStackTrace();
		}
	}
	/* doSimpleFileListing() method gets the directory name as input and reads the XML files present in the directory and loads as UNL graphs in the variable ll
	 * by invoking the method printFiles()
	 */
	public 	FinalLLImpl doSimpleFileListing(String dirName) throws IOException {	
		File dir = new File("./decon_inputs/inputeng");
		String[] children = dir.list();
		System.out.println("children:"+children.toString());
		System.out.println("dir:"+dirName);
		ll=printFiles(children, dirName);
	    dir = null;
	    return ll;
	}

/* printFiles method gets the directory name and file names in the directory  as input and reads the XML files present in the directory and loads as UNL graphs in the variable ll
 * returns the UNL graph loaded in the multi list by invoking the method runExample()
 * 
 * 
 */
	private FinalLLImpl  printFiles(String[] children, String dirName)throws IOException {
		
		if (children == null) {
		} else {
			for (int i = 0; i <children.length; i++) {
				String filename = children[i];									
				File files = new File(dirName + File.separatorChar + filename);					
				System.out.println("file name:"+files);				
		 		try{			 		
		
		 		dirName=("./decon_inputs/inputeng");
		 		
		                try {
		                     ll=runExample(dirName.trim()+files);
		                  } catch (Exception e) {
		                      System.out.println(e.getMessage());
		                  }          
		         
		 		}catch(Exception e){e.printStackTrace();}            
				dirName="";
			}
		}
		return ll;
	}
	/**
	 * Iterate through the list and print
	 * the contents from the XML files and loads the UNL graphs in multi lists ll_new[]
	 * the printData() method is called iteratively for all XML files in the directory 
	 */
	public FinalLLImpl  printData()throws Exception{		
		FrameworkNew fd=new FrameworkNew();
		Boolean plural_flag=false;
		ArrayList<String> priorityarray=new ArrayList<String>();
		Iterator it = myEmpls.iterator();
		while(it.hasNext()) {
			System.out.println(it.next().toString());
		}
		ArrayList sen=new ArrayList();
		ArrayList docids=new ArrayList();
		ArrayList fromids=new ArrayList();
		ArrayList toids=new ArrayList();
		ArrayList relns=new ArrayList();
		 int docid=1;
		  ll_new[docid-1] = new FinalLLImpl();
		  ArrayList sensub=new ArrayList();
		  ArrayList docidsub=new ArrayList();
		  ArrayList fromidsub=new ArrayList();
		 ArrayList toidsub=new ArrayList();
		 ArrayList relnsub=new ArrayList();
		 System.out.println("myEMpls size :"+myEmpls.size());
		 try{
		for(int i=0;i<myEmpls.size();i++){
			 sensub=new ArrayList();
			 docidsub=new ArrayList();
			 fromidsub=new ArrayList();
			toidsub=new ArrayList();
			relnsub=new ArrayList();
			String s=myEmpls.get(i).toString();			
			StringTokenizer st=new StringTokenizer(s,"#");
			String con="";
			String rel="";
			String attri="";
			String connect="";
			String fromid="";
			String toid="";
			String term="";
			String tocon="";
			String pos="";
			String sid="";
			String did="";
			System.out.println("the string s :"+s);
				attri=st.nextToken();
				StringTokenizer st1=new StringTokenizer(attri,":");
				st1.nextToken();
				attri=st1.nextToken();
				
				rel=st.nextToken();
				System.out.println("the string s :"+s);
				StringTokenizer st2=new StringTokenizer(rel,":");
				st2.nextToken();
			//	while(st2.hasMoreTokens()){
					String snew=st2.nextToken();
					StringTokenizer st2new=new StringTokenizer(snew,"@");
					while(st2new.hasMoreTokens()){
						String snew1=st2new.nextToken();
					System.out.println("st2 tokens:"+snew1);
					relnsub.add(snew1);
				}
				
				System.out.println("rels in tokenizer:"+relnsub.toString());
				fromid=st.nextToken();
				StringTokenizer st3=new StringTokenizer(fromid,":");
			    st3.nextToken();
				fromid=st3.nextToken();
				System.out.println("from id in tokenizer:"+fromid);
				
				toid=st.nextToken();
				StringTokenizer st4=new StringTokenizer(toid,":");
				st4.nextToken();
			     String snew2=st4.nextToken();
				StringTokenizer st4new=new StringTokenizer(snew2," [,]");
				 //st4new.nextToken();
				while(st4new.hasMoreTokens()){
					String snew3=st4new.nextToken();
					toidsub.add(snew3);
					
				}
				System.out.println("toids in tokenizer:"+toidsub.toString());
				con=st.nextToken();
				StringTokenizer st5=new StringTokenizer(con,":");
				st5.nextToken();
				con=st5.nextToken();
				System.out.println("Concept in Tokenizer:"+con);
				
				term=st.nextToken();
				StringTokenizer st6=new StringTokenizer(term,":");
				st6.nextToken();
				term=st6.nextToken();
				System.out.println("term in tokenizer:"+term);
				
				pos=st.nextToken();
				StringTokenizer st7=new StringTokenizer(pos,":");
				st7.nextToken();
				pos=st7.nextToken();
				System.out.println("pos in tokenizer:"+pos);
				sid=st.nextToken();
				StringTokenizer st8=new StringTokenizer(sid,":");
				st8.nextToken();				
				sid=st8.nextToken();
				System.out.println("s id in tokenizer:"+sid);
				did=st.nextToken();
				StringTokenizer st9=new StringTokenizer(did,":");
                st9.nextToken();
				did=st9.nextToken();
			    System.out.println("did in tokenizer:"+did);
				System.out.println("the concept:"+con);
		        priorityarray=fd.loadPriorityArrayList();
		        fromid="c"+fromid;
		        did="d"+did;
		        sid="s"+sid;
		        System.out.println("did:"+did);		
		        System.out.println("fromid:"+fromid);	
		        System.out.println("attri:"+attri);
		        System.out.println("con:"+con);
				ll_new[docid - 1].addConcept(term, con,attri,fromid,did,sid,pos, "", "", "");
				System.out.println("the address after add concept:"+ll_new[docid-1]);
				sensub.add(sid);
				docidsub.add(did);
				fromidsub.add(fromid);
				sen.add(sensub);
				docids.add(docidsub);
				fromids.add(fromidsub);
				relns.add(relnsub);
				toids.add(toidsub);
			}
		 }catch(Exception e){e.printStackTrace();}
		System.out.println("size of fromid:"+fromids.size());
		System.out.println("size of fromidsub:"+fromidsub.size());
		
		ConnectToConcepts(sen, docids,fromids,toids,relns,docid);
		printUNLGraph();
	    maincounter++;
		return ll_new[docid-1];
	}
	
	/*ConnectToConcepts() populates the connected concepts present in the UNL graphs and loads in the multi lists ll_new[]
	 * 
	 */
	public void ConnectToConcepts(ArrayList sen,ArrayList docids ,ArrayList fromids,ArrayList toids,ArrayList relns,int docid){
		try{		      
		     
			for(int j=0;j<fromids.size();j++){
				ArrayList rel=(ArrayList)relns.get(j);
				ArrayList toid=(ArrayList)toids.get(j);
				ArrayList fromid=(ArrayList)fromids.get(j);
				ArrayList senid=(ArrayList)sen.get(j);
				ArrayList doid=(ArrayList)docids.get(j);
				System.out.println("size of rel in connect:"+rel.size());
				System.out.println("****************for loop starts**********************");
				for(int i=0;i<rel.size();i++){
					 String reln=rel.get(i).toString();
			    	   String td=toid.get(i).toString();
			    	   String  did=doid.get(0).toString();
			    	   String sid=senid.get(0).toString();	
			    	   String frmid=fromid.get(0).toString();
			    	   td="c"+td;
			    	   System.out.println("from id in addct:"+frmid);
			    	   System.out.println("to id in add ct:"+td);
			    	   System.out.println("reln in add ct:"+reln);
			    	   if(!reln.contains("None")){
			    	   ll_new[docid - 1].addRelation(reln);
					   ConceptToNode cn = ll_new[docid - 1].addCT_Concept(frmid,td,reln, did, sid);
					   System.out.println("the address after add ct concept:"+ll_new[docid-1]);
					   ll_new[docid - 1].addCT_Relation(cn);	
			    	   }
					   System.out.println("the address after add ct relationt:"+ll_new[docid-1]);					   
					 
				}
				System.out.println("****************for loop ends**********************");
			}
			
		}catch( Exception e){e.printStackTrace();}
				
	}
	public void printUNLGraph(){
		
		int count=0;
		 for (int i = 0; i < ll_new.length; i++) {
	        	if (ll_new[i] != null) {
	                count++;
	            }
	        }
		
	        for (int i = 0, j = 0; j < count; i++, j++) {    
			
	            int flg = 0;
	            int docid=1;
	            HeadNode temp1 = new HeadNode();
	            System.out.println("ll_new in print:"+ll_new[docid-1]);
	            temp1 = ll_new[docid-1].head;
	            ConceptNode c1 = new ConceptNode();
	       
		    ConceptToNode tocon = new ConceptToNode();
	            c1 = ll_new[docid-1].concept;
	            c1 = temp1.colnext;
	            
	            while(c1!=null){
	            	tocon=c1.rownext;
	            	System.out.println("UW CONCEPT:"+c1.uwconcept+"senid:"+c1.sentid+"docid:"+c1.docid+"conceptid:"+c1.conceptid);
	            	System.out.println("UW TO CONCEPT:"+tocon);
	            	while(tocon!=null){
	            		System.out.println("UW TO CONCEPT:"+tocon.uwtoconcept+"sentid:"+tocon.sentid);
	            		tocon=tocon.rownext;
	            	}
	            	c1=c1.colnext;
	            } 	
	            	
	            	
	            }
		  
	        }
	
		public ArrayList analysePosition(ArrayList sen,ArrayList priorityarray){
			
			boolean verb_type=false;
			
	    	System.out.println("entering loop"+sen.toString());
			String sen_eng[]=new String[20];
		    int tail_count=20;
			for(int i=0;i<sen.size();i++){
				tail_count--;
				String s=sen.get(i).toString();
				if(s.contains("*")){
				sen_eng=getPosition(s,priorityarray,sen_eng);
				}else{
					sen_eng[tail_count-10]=s;
				}
		
			}
		ArrayList sentence=new ArrayList();
		for(int j=0;j<sen_eng.length;j++){
			
			String sen1=sen_eng[j];
			if(!sentence.contains(sen1) && sen1!=null){
			sentence.add(sen1);
			}
		}
		
	
		return sentence;
}
		
	public void constructUNLGraphFromXML(String con,String toid,String fromid,String rel,String attri,String  did, String sid,String term,int docid )throws Exception{
			
		ArrayList rels=new ArrayList();
		ArrayList toids=new ArrayList();
		StringTokenizer st1=new StringTokenizer(rel,"@");
		while(st1.hasMoreTokens()){
			
			rels.add(st1.nextToken());
			System.out.println("rels size:"+rels.size());
		}
	
			
		       StringTokenizer st=new StringTokenizer(toid,",");
		       int relcount=0;
		       while(st.hasMoreTokens()){
		    	   toids.add(st.nextToken()); 	 
		    	   
		       }
		       
		       System.out.println("the size of toids:"+toids.size());
			  System.out.println("****************for loop starts**********************");
		       for(int i=0;i<rels.size();i++){
		    	   String reln=rels.get(i).toString();
		    	   String td=toids.get(i).toString();
		    	   System.out.println("from id:"+fromid);
		    	   System.out.println("To id:"+td);
		    	   ll_new[docid - 1].addRelation(reln);
				   ConceptToNode cn = ll_new[docid - 1].addCT_Concept(fromid,td,reln, did, sid);
				   ll_new[docid - 1].addCT_Relation(cn);
		    	   
		       }
		       System.out.println("****************for loop starts**********************");
		}
		public String[] getPosition(String sinput, ArrayList priorityArray,String[] sen_eng){
			try{
			System.out.println("the sinput:"+sinput);
			StringTokenizer st=new StringTokenizer(sinput,"*");
			String s=st.nextToken();
			int pos=priorityArray.indexOf(st.nextToken().toString());
			sen_eng[pos]=s;
			}catch(Exception e){e.printStackTrace();}
			return sen_eng;
			
		}
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {		//reset
		tempVal = "";
		if(qName.equalsIgnoreCase("Node")) {
			tempEmp = new GraphNode();
		}
	}
	

	public void characters(char[] ch, int start, int length) throws SAXException {		
		tempVal = new String(ch,start,length);
		 
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 * consists of member variables of a node present in a UNL graph
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		if(qName.equalsIgnoreCase("Node")) {
			myEmpls.add(tempEmp);			
		}else if (qName.equalsIgnoreCase("Concept")) {			
			tempEmp.setConcept(tempVal);
		}else if (qName.equalsIgnoreCase("Fromid")) {
			tempEmp.setFromId((tempVal));
		}else if (qName.equalsIgnoreCase("Relation")){
			tempEmp.setRelation((tempVal));
		}
		else if (qName.equalsIgnoreCase("Toid")) {
			tempEmp.setToId((tempVal));
		}
		else if (qName.equalsIgnoreCase("Attributes")) {
			tempEmp.setAttributes((tempVal));
		
		}else if (qName.equalsIgnoreCase("Term")) {
			tempEmp.setTerm((tempVal));
		
		}else if (qName.equalsIgnoreCase("POS")) {
			tempEmp.setPOS((tempVal));
		
		}else if (qName.equalsIgnoreCase("Sid")) {
			tempEmp.setSid((tempVal));
		
		}else if (qName.equalsIgnoreCase("Did")) {
			tempEmp.setDid((tempVal));
		
		}
		
	}
	
	public static void main(String[] args)throws Exception{
		SAXParserNew spe = new SAXParserNew();
		FinalLLImpl ll=new FinalLLImpl();
		String dir="";
		ll=spe.doSimpleFileListing("");
		FrameworkNew f=new FrameworkNew();
		f.processGraphNew(ll);
		System.out.println("the ll:"+ll);
		HeadNode h=new HeadNode();
		ConceptNode c=new ConceptNode();
		h=ll.head;
		c=h.colnext;
		while(c!=null){			
			System.out.println("Concept in sax main:"+c.uwconcept+"senid"+c.sentid);
			c=c.colnext;
		}
	}
	
}




