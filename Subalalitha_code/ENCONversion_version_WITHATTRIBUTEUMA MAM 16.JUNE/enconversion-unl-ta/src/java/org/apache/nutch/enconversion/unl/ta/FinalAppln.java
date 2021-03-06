package org.apache.nutch.enconversion.unl.ta;

import org.apache.nutch.unl.UNL;
import org.apache.nutch.util.NutchConfiguration;
import org.apache.hadoop.conf.Configuration;

import java.lang.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.logging.FileHandler;
//import jomp.runtime.*;

public class FinalAppln implements Serializable, UNL {
	public static int docid = 0;
	public static int dociditer = 0;
	public static int initialdocid = 0;
	public static int finaldociditer = 0;
	int sentid = 0;
	int no_doc = 0;
	public static String did = "";
	public static String docid1 = "";
	public static HashMap unlgraphs = new HashMap();
	public static MultiList mlist=new MultiList();
	int ctrs = 0;
	String encon_out = "";
	public static int filecount;
	public Hashtable get_count = new Hashtable();
	 
	org.apache.nutch.enconversion.unl.ta.Rules r = new Rules();

	org.apache.nutch.enconversion.unl.ta.Rules_en r_en = new Rules_en();
	
	
	public static FinalLLImpl[] ll_new = new FinalLLImpl[50000];
	
	public static Hashtable fileList = new Hashtable();
	
	public static Hashtable new_freq = new Hashtable();
	public static Hashtable al = new Hashtable();
	
	public static ArrayList<String> rlist = new ArrayList<String>();
	public static ArrayList<String> flist = new ArrayList<String>();
	
	public static Hashtable getcount = new Hashtable();
	public static FileHandler hand; 
	public static Logger log;
	public static PrintWriter logwriter;
	public static ArrayList list = new ArrayList();

	public static boolean flag = true;
	public static int stfrm;

	public static Configuration conf = NutchConfiguration.create();
	public static String input_path = conf.get("SentenceExtraction");
	public static String path = conf.get("unl_resource_dir");
	public static String graph_path = conf.get("unl-Graph");


	public FinalAppln() {

	}

	public void start(FinalLLImpl[] ll_new) throws IOException,
			ClassNotFoundException {
		encon_out = "";			
		String s = "";
		String s1 = "";	
		String docid1 = "";
		ArrayList<String> alnew = new ArrayList<String>();		
		
		BufferedReader in = new BufferedReader(new FileReader(input_path+ "Output/" + "enconinput.txt"));
		
		r.loadequ();
		readFilelist();	
		readDocid();
		
		String str = "";
		String sent = "", recv = "", token = "", fn = "", temp = "";
		int temp1 = 0, j = 0;
		FileReader fr = null;
		docid = no_doc;
		initialdocid = no_doc;
		int incrementalIter = 0;
		
		
		while ((s = in.readLine()) != null) {
			
			fn = input_path + "Output/" + s;
			
			File empFile = new File(fn);
			
			fileList.put(no_doc + 1, fn);
			no_doc++;
			incrementalIter++;
			
	
				
				StringBuffer docbuff = new StringBuffer();
				encon_out += "[d]#";
				
				String splitid[] = s.split("/");
				
				docid1 = splitid[1].replace(".txt", "").toString();

			
				alnew.add(docid1);
			// read sentences from file
	
			fr = new FileReader(fn); 
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
StringTokenizer sentToken1 = new StringTokenizer(docbuff.toString()
					.trim(), ".", false);
			j = 0;
			while (sentToken1.hasMoreTokens()) {

				sent = sentToken1.nextToken();
				recv = r.enconvert(sent);
				encon_out += recv;
			}
			encon_out += "[/d]#";
			r.clearList();
			
			fr.close();
			
			
		
		}
		in.close();		
	
		freq_count(alnew);		
	System.out.println("encon_out_ta"+encon_out );
		
		graphconstruct(ll_new, al,"ta");
//		unlgraphs=putunlgraph(new HashMap(),docid1,ll_new);
		writeFilelist();
		alnew.clear();
		al.clear();
		
	
	}
	 public void start_en(FinalLLImpl[] ll_new) throws IOException,
            ClassNotFoundException {
        encon_out = "";
        String s = "";
        String s1 = "";
        String docid_en1 = "";
        ArrayList<String> alnew = new ArrayList<String>();

        BufferedReader in = new BufferedReader(new FileReader(input_path
				+ "Output/" + "enconinput.txt"));

      //  r.loadequ();

        readFilelist();
        readDocid();

        String str = "";
        String sent = "", recv = "", token = "", fn = "", temp = "";
        int temp1 = 0, j = 0;
        FileReader fr = null;
        docid = no_doc;
        initialdocid = no_doc;
        int incrementalIter = 0;


        while ((s = in.readLine()) != null) {
            // {
            fn = input_path + "Output/" + s;

            File empFile = new File(fn);

            fileList.put(no_doc + 1, fn);
            no_doc++;
            incrementalIter++;

            //	if(empFile.length()>25){

            StringBuffer docbuff = new StringBuffer();
            encon_out += "[d]#";

            String splitid[] = s.split("/");
            docid_en1 = splitid[1].replace(".txt", "").toString();

            	
            alnew.add(docid_en1);
            // read sentences from file      
            FileReader file = new FileReader(fn);
            Scanner scanner = new Scanner(file).useDelimiter("\\Z");
            String contents = scanner.next();
          
            StringTokenizer strToken = new StringTokenizer(contents.toString(), "#", false);
            while (strToken.hasMoreTokens()) {
               sent = strToken.nextToken();               
                recv = r_en.enconvert(sent);
                encon_out += recv;  
       
            }
        
            encon_out += "[/d]#";
        
     
          
        }
        in.close();
   
        freq_count(alnew);
       

        graphconstruct(ll_new, al,"en");
//	unlgraphs=putunlgraph(new HashMap(),docid_en1,ll_new);
       
        writeFilelist();
	alnew.clear();
        al.clear();
	System.out.println("encon_out_en"+encon_out );

    }

	public void freq_count(ArrayList<String> alnew){
		String word = "";
		String g_word = "";
		String conentry = "";
		String relnentry = "";
		String docid1="";
		Hashtable freq_table = null;
		int l=0;
		try {
			StringTokenizer strToken = new StringTokenizer(encon_out, "#");
		    while (strToken.hasMoreTokens()) {
				try {
					word = strToken.nextToken().trim();
					if (word.equals("[d]")) {	
							freq_table = new Hashtable();
							docid1 = alnew.get(l).toString();
					
							l++;
					} else if (word.equals("[s]")) {
						
					} else if (word.equals("[/d]")) {
						al.put(docid1, freq_table);
						
					} else if (word.equals("[w]")) {

						conentry = strToken.nextToken().trim();
					
						while (!(conentry.equals("[/w]"))) {
							StringTokenizer strToken1 = new StringTokenizer(
									conentry, ";");
							if (strToken1.hasMoreElements()) {
								g_word = strToken1.nextToken().trim();
						
								int count = 1;
								String frequency="";
								if(freq_table.isEmpty()){
									frequency = Integer.toString(count).toString();
									freq_table.put(g_word, frequency);
								}else if(freq_table.containsKey(g_word)){
									String freq_count = freq_table.get(g_word).toString();
									int f_cnt = Integer.parseInt(freq_count);
									f_cnt++;
									frequency = Integer.toString(f_cnt).toString();
									freq_table.put(g_word, frequency);
								}else{
									frequency = Integer.toString(count).toString();
									freq_table.put(g_word, frequency);
								}
							}
							if (strToken1.hasMoreElements()) {
								
							}
							if (strToken1.hasMoreElements()) {
								
							}							
							if (strToken1.hasMoreElements()) {
								
							}
							conentry = strToken.nextToken().trim();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();					
				}
			}
		
		}catch (Exception e) {
			e.getStackTrace();
		}
	
	}
	String store_doc_sid="";
	public void graphconstruct(FinalLLImpl[] ll_new, Hashtable al,String tag) {
		String g_word = "", concept = "", conceptto = "", sid = "", did = "", relnid = "", frmscpid = "",toscpid = "", conceptfrm = "", ConceptToNode = "";
		String verbword = "";
		String conentry = "";
		String relnentry = "";
		String frmscopeid = "";
		String toscopeid = "";
		String word = "";
		String attribute= "";
		int totcon_sent = 0;
		int totrel_sent = 0;
		
		String fd = "";
		String fd1 = "";
		String fldoc = "";

		try {

			StringTokenizer strToken = new StringTokenizer(encon_out, "#");
			String conceptid = " ";
			
			int iter = 1;
			while (strToken.hasMoreTokens()) {
				try {
					word = strToken.nextToken().trim();
					if (word.equals("[d]")) {

						ll_new[docid] = new FinalLLImpl();
						fldoc = list.get(docid).toString();
						StringTokenizer strTok = new StringTokenizer(fldoc, "/");
						fd1 = strTok.nextToken();
						fd = strTok.nextToken();
						fd = fd.replace(".txt", "");
						String[] d=fd.split("-");
						store_doc_sid = d[0].trim();
						
						
						docid++;
						get_count = (Hashtable) al.get(d[0].trim());
				
						
					} else if (word.equals("[s]")) {
						totcon_sent = 0;
						totrel_sent = 0;
						
						sentid++;

					} else if (word.equals("[/d]")) {
						
						sentid = 0;
			
						iter++;
					} else if (word.equals("[w]")) {

						conentry = strToken.nextToken().trim();
						while (!(conentry.equals("[/w]"))) {
							StringTokenizer strToken1 = new StringTokenizer(
									conentry, ";");
							if (strToken1.hasMoreElements()) {
								g_word = strToken1.nextToken().trim();
							}
							if (strToken1.hasMoreElements()) {
	
								if(tag.equals("ta")){
								concept = strToken1.nextToken().trim() + '('
										+ strToken1.nextToken().trim() + ')';
								}
								else
								{
									concept = g_word + '('+ strToken1.nextToken().trim() + ')';
								}
							}
							if (strToken1.hasMoreElements()) {
								verbword = strToken1.nextToken().trim();
							}
							totcon_sent++;
							if (strToken1.hasMoreElements()) {
								attribute = strToken1.nextToken().trim();
							}
							if (strToken1.hasMoreElements()) {
								conceptid = strToken1.nextToken().trim();
							}

							sid = ("s" + sentid);

							did = fd.trim();

							Object str = get_count.get(g_word);
					
							if (str != null) {
								ll_new[docid - 1].addConcept(g_word, concept,attribute,conceptid, did, sid, verbword, str
												.toString(), "", "");
							} else {
								ll_new[docid - 1].addConcept(g_word, concept,attribute,conceptid, did, sid, verbword, "", "", "");
							}

							conentry = strToken.nextToken().trim();

						}

					}

					else if (word.equals("[r]")) {
						relnentry = strToken.nextToken().trim();
					
						while (!(relnentry.equals("[/r]"))) {

							StringTokenizer strToken2 = new StringTokenizer(
									relnentry);
						
							if (strToken2.hasMoreElements()) {

								conceptfrm = strToken2.nextToken().trim();
							//	System.out.println("frmconceptid:"+conceptfrm);
							}
							if (strToken2.hasMoreElements()) {

								relnid = strToken2.nextToken().trim();
							//	System.out.println("relnid:"+relnid);
							}
							if (strToken2.hasMoreElements()) {
								conceptto = strToken2.nextToken().trim();
							//	System.out.println("toconceptid:"+conceptto);
							}
						/**	if (strToken2.hasMoreElements()) {

								toscopeid = strToken2.nextToken().trim();
							//	System.out.println("toscpid:"+toscopeid);
								if(!(toscopeid.equals("scp2"))){
									toscpid = toscopeid;
								}else{
									toscpid = "None";
								}
							}	*/

							sid = ("s" + sentid);
							// did=("d"+(FinalAppln.initialdocid+iter));
							did = fd.trim();

							totrel_sent++;

							if ((!(conceptfrm.equals("None")))
									&& (!(conceptto.equals("None")))) {

								ll_new[docid - 1].addRelation(relnid);
								ConceptToNode cn = ll_new[docid - 1]
										.addCT_Concept(conceptfrm, conceptto,
												relnid, did, sid);
								ll_new[docid - 1].addCT_Relation(cn);
							}

							if (conceptto.equals("None")) {

							}

							relnentry = strToken.nextToken().trim();

						}

					}

					else {

					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Document is Empty");
					no_doc++;
				}
			}

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public void readFilelist(){
		try {
			File f = new File(graph_path + "filelist.txt");
			/** to check whether the file exist or not **/

			boolean fileexist = f.exists();
			if (fileexist) {
				FileInputStream fis = new FileInputStream(graph_path
						+ "filelist.txt");
				ObjectInputStream ois = new ObjectInputStream(fis);
				fileList = (Hashtable) ois.readObject();
				ois.close();
				fis.close();
			} else {
				f.createNewFile();
			}
		} catch (Exception e) {
			fileList = new Hashtable();

		}
	}
	public void writeFilelist(){
		try{
			FileOutputStream fos = new FileOutputStream(graph_path + "filelist.txt");
			ObjectOutput oos = new ObjectOutputStream(fos);
			oos.writeObject(fileList);
			oos.flush();
			oos.close();
		}catch(Exception e){
			
		}
	}	
	public void readDocid(){
		try {
			BufferedReader in2 = new BufferedReader(new FileReader(path
					+ "Docid.txt"));
			String str1 = in2.readLine();
			if (str1.trim() != null)
				no_doc = Integer.parseInt(str1) + fileList.size();
			else
				no_doc = 0;
			in2.close();
			// graphconstruct(ll_new,al);
		} catch (Exception e) {
			no_doc = 0;
		}
	}
	
	public static void main(String args[]) throws Exception {
		// String str = args[0];
		int sen = 0;
		// FinalAppln ap=new FinalAppln();
		int docid = stfrm;
		String did = "";
		String fn1 = null;
		// Index in=new Index();
	//	ArrayList<String> docnew = new ArrayList<String>();
		BufferedWriter buf = null;
		BufferedWriter out = null;
		BufferedWriter buffer = null;
		BufferedWriter inbuf = null;
		BufferedWriter emptybuf = null;
		
		BufferedReader inbr = null;
		BufferedReader in = null;
		BufferedReader reademptybuf = null;
		//int filecount=4;
		int total_pos = 0;
		int counter = 0;
		String sendoc = null;
		String strsen1 = null;
		String strSent = null;
		String getfName = null;

		try {

			BufferedReader bufred = new BufferedReader(new FileReader(
					input_path + "Output/" + "nonemptyfiles.txt"));
			inbr = new BufferedReader(new FileReader(path + "lastfile.txt"));
			in = new BufferedReader(new FileReader(path + "lastcount.txt"));
			reademptybuf = new BufferedReader(new FileReader(path + "emptyfiles.txt"));
			out = new BufferedWriter(new FileWriter(input_path + "Output/"
					+ "enconinput.txt"));
			String sentStr = "";
			String prefix = "";
			String suffix = "";
			String st = "";
			String stg = "";
			int incrementalIter = 0;
			
			FinalAppln docp = new FinalAppln();
			int count = 0;
			
			while ((sentStr = bufred.readLine()) != null) {
			//	System.out.println ("bufred.readLine()"+sentStr);
				String func = input_path + "Output/"+ sentStr;
				System.out.println("FUNC:"+func);
				File newFile = new File(func);
				if(newFile.length() > 50){
					list.add(sentStr);
					System.out.println(sentStr);
				}else{
					System.out.println("EmptyFiles:"+sentStr);
				}
			}
			System.out.println("LIST:"+list+":"+list.size());
			
			int totalsize = list.size();
			if ((st = inbr.readLine()) != null) {
				int sz = list.indexOf(st);
				stfrm = sz + 1;
			} else {
				stfrm = 0;
			}
		//	String getfName = list.get(list.size() - 1).toString();
			if ((stg = in.readLine()) != null) {
				int fcnt = Integer.parseInt(stg);
				filecount = fcnt;
			} else {
				filecount = 0;
			}	
			for (int i = stfrm; i < list.size(); i++) {
				try {						
						sentStr = (String) list.get(i);				
					} catch (Exception e) {
					e.printStackTrace();
				}
				out.write(sentStr + "\n");
				incrementalIter++;
				if (incrementalIter % 1== 0) {			
					getfName = list.get(i).toString();
				System.out.println("getfName"+getfName);
					out.close();
				//	System.out.println("ALNEW:"+alnew);
					try {
						
						if(getfName.endsWith("_ta.txt")){
						//System.out.println("getfName"+getfName);
						docp.start(ll_new);
						writeinObject("unlgraph_ta" + filecount + ".txt");

						if (incrementalIter % 1 == 0) {
							
							filecount++;							
							ll_new = null;
							ll_new = new FinalLLImpl[list.size()];							
						}		
						}else{
							docp.start_en(ll_new);
						writeinObject("unlgraph_en" + filecount + ".txt");

						//System.out.println("getfName"+getfName);
						if (incrementalIter % 1 == 0) {
							
							filecount++;							
							ll_new = null;
							ll_new = new FinalLLImpl[list.size()];							
						}
						}				
						System.gc();
					} catch (Throwable ex) {
					}
					System.gc();
					out = new BufferedWriter(new FileWriter(input_path
							+ "Output/" + "enconinput.txt"));
					buffer = new BufferedWriter(new FileWriter(path + "lastfile.txt"));
					inbuf = new BufferedWriter(new FileWriter(path + "lastcount.txt"));				
				}	
			//	buffer.close();
				
			}
			//buffer.write(getfName);
			buffer.close();
			//inbuf.write(Integer.toString(filecount));
			inbuf.close(); 
			emptybuf.close();		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}// main

	public static void writeinObject(String filename) {
		
		try {
			System.out.println("Entered in to writing block");
			FileOutputStream fostream = new FileOutputStream(graph_path
					+ filename);
			ObjectOutputStream oostream = new ObjectOutputStream(fostream);
			System.out.println("write_object"+ll_new);
			//oostream.writeObject(ll_new);
				mlist.list_writeintofile(ll_new,oostream);
			System.out.println("Writing Completed");
			oostream.close();
			fostream.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.log(Level.SEVERE, "Uncaught exception", e);
		}
	
	}
	/*public static HashMap putunlgraph(HashMap hasputs, String key, FinalLLImpl[] value) {
        if (!hasputs.containsKey(key)) {
            hasputs.put(key, value);
        }
        return hasputs;
    	}*/
	public ArrayList process(String queryString,String langcheck) {
		ArrayList arr = null;
		return arr;
	}

}
