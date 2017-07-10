package org.apache.nutch.enconversion.unl.ta;

import java.io.*;
import java.util.*;

public class learning{

    String tagresult = "";
    String result;

    LinkedList<String> loadnew;
    LinkedList<String> loadprev;

    ArrayList<String> nestlist;
    ArrayList<String> datalist;
    ArrayList<String> resultlist;
    ArrayList<String> loadlist;
    public learning(){

    }

    public void init(){

        loadnew = new LinkedList<String>();
        loadprev = new LinkedList<String>();

        datalist = new ArrayList<String>();
        nestlist = new ArrayList<String>();
        resultlist = new ArrayList<String>();
        loadlist = new ArrayList<String>();

        result = "";
    }
    public String unsupervised(String st){
        init();
    //    System.out.println("ST:"+st);
        StringTokenizer strToken3 = new StringTokenizer(st,"@");
        int total = strToken3.countTokens();
        int initial = 0;
        
        while(initial<total){
            String word1 = strToken3.nextToken();
            loadnew.add(word1);
            initial++;
        }        
        loadTaggedData();
        StringBuffer resultuw = new StringBuffer();
      //  StringBuffer finalresult = new StringBuffer();
        for(int k=0;k<resultlist.size();k++){
            resultuw.append(resultlist.get(k).toString()+" ");
        }
        result +=resultuw;
        result +=".\n";
       // resultlist.clear();
     //   System.out.println("Result:"+result);
        return result;
    }
    public void loadTaggedData(){
        String s = null;
	FileReader fr = null;
	String temp = null;
        String taggedOutput = "";
	String str = "";
	String sent = "", recv = "", token = "", fn = "";
	int temp1 = 0, j = 0,i=0;
       // learning l = new learning();

        try{
            BufferedReader inbr = new BufferedReader(new FileReader("./resource/unl/seinput.txt"));
            while ((s = inbr.readLine()) != null) {
		fn = "./resource/unl/tagDocs/" + s;
                //    tag_file = s;
		//	System.out.println("fn:" + fn);
		StringBuffer docbuff = new StringBuffer();
		fr = new FileReader(fn);

                // System.out.println("fr:"+fr);
                BufferedReader buff = new BufferedReader(new FileReader(fn));
                while( (str=buff.readLine())!=null){
                    docbuff.append(str);
                }
                StringTokenizer sentToken1 = new StringTokenizer(docbuff
						.toString().trim(), ".", false);
                        //l.loadTaggedData();
			j = 0;
                       	while (sentToken1.hasMoreTokens()) {
                            sent = sentToken1.nextToken();
                         //   recv = uploadData(sent);
                            nestlist.add(sent);
                            //taggedOutput+=recv;
                        //    System.out.println("Inside loadtaggeddata");
                         //   i++;
			}
                        checkingProcess();
                      //  System.out.println("nestlist:"+nestlist);
            }
        }catch(Exception e){

        }
    }
    public void checkingProcess(){
        int newsize = 0;
        String tagset = "";
        int nsize = loadnew.size();
        for(int i=0;i<nestlist.size();i++){
          //  System.out.println("i:"+i);
         //   System.out.println("nestlist size:"+nestlist.size());
            String senten = nestlist.get(i).toString();
            ArrayList<String> loaddata = new ArrayList<String>();
            StringTokenizer strToken4 = new StringTokenizer(senten,"@");
            while(strToken4.hasMoreTokens()){
                String word2 = strToken4.nextToken();
                loaddata.add(word2);
            }    
         //   System.out.println("loaddata size:"+loaddata.size());
         //   System.out.println("loadnew size:"+loadnew.size());

            int psize = loaddata.size();
            if(psize>=nsize){
                newsize = psize - nsize;
            }else if (psize<nsize){
                newsize = nsize - psize;
            }
            if( (newsize == 0)){
            //    System.out.println("newsize:"+psize+"-"+nsize+"="+newsize);
               // startTagging();
                for(int j=0;j<psize;j++){
                 //   System.out.println("j:"+j);
                    String getword = loaddata.get(j).toString();
                 //   System.out.println("getword:"+getword);
                    StringTokenizer strTok1 = new StringTokenizer(getword,"+");
                    //prevarr = getword.split("+");
                    String w1 = strTok1.nextToken();
                    String w2 = strTok1.nextToken();
                    String w3 = strTok1.nextToken();
                    String w4 = strTok1.nextToken();
                    String w5 = strTok1.nextToken();
                    String w6 = strTok1.nextToken();
                    
                //    System.out.println("w1:"+w1);
                   // System.out.println("prevarr:"+prevarr[0]+prevarr[1]+prevarr[2]+prevarr[3]+prevarr[4]+prevarr[5]);
                   // for(int k=0;k<nsize;k++){
                        String getnew = loadnew.get(j).toString();
                       // System.out.println("getnew:"+getnew);
                        StringTokenizer strTok2 = new StringTokenizer(getnew,"+");
                    //prevarr = getword.split("+");
                        String w11 = strTok2.nextToken();
                        String w12 = strTok2.nextToken();
                        String w13 = strTok2.nextToken();
                        String w14 = strTok2.nextToken();
                        String w15 = strTok2.nextToken();
                        String w16 = strTok2.nextToken();
                        if( (w13.equals(w3)) && (w14.equals(w4)) && (w15.equals(w5)) ){
                            w16.replace("???", w6);
                        //    System.out.println("w6:"+w6);
                        //    System.out.println("w16:"+w16);
                            tagset = w11+"+"+w12+"+"+w13+"+"+w14+"+"+w15+"+"+w6;
                           // tagresult = tagset+" ";
                            resultlist.add(tagset);
                            //loadnew.remove(j);
                            if(resultlist.size()==psize){
                                loadnew.clear();
                             //   break;
                            }
                        //    System.out.println("tagset:"+tagset);
                        }
                   //     System.out.println("resultlist:"+resultlist);
                    
                //    }
                  //  break;
                }
               // break;
                
            }
            
           /** */
        }
        
        //resultlist.clear();
    }
    public String uploadData(String str){
      
     // LinkedList nestlist = new LinkedList();
      int i=0;
     // while(i<){
      StringTokenizer strToken1 = new StringTokenizer(str,"@");
      int now = strToken1.countTokens();
      int ctnow = 0;
      while(ctnow<now){
         String word = strToken1.nextToken();
         loadlist.add(word);
   //     StringTokenizer strToken2 = new StringTokenizer(word,"+");
         ctnow++;
      }
   //   checkingProcess();
     // nestlist.add(loadlist.toString());
     // i++;
    //}
    //  System.out.println("loadlist:"+loadlist);
      return str;
    }
    public void writeintoFile(String filename,String tag_out){
       try{
        //        System.out.println("Filename:"+filename);
                Writer output = null;
            //    Directory dic = new Directory()"./resource/unl/tagDocs/";
                File fn1 = new File("./resource/unl/tagResult/");
          //      System.out.println("FileDir:"+fn1);
                String[] writeStr = filename.split("/");
                String pathname = writeStr[1];
              /**  String[] splitname = pathname.split(".");
                String prefix = splitname[0];
                String suffix = splitname[1];*/
           //     System.out.println("pathname:"+pathname);
                File dir = new File("./resource/unl/tagResult/"+filename);
                dir.createNewFile();
             //   System.out.println("Directory:"+dir);
              //  dir.createTempFile(prefix, suffix, fn1);
                output =new BufferedWriter(new FileWriter(dir,false));
                output.write(tag_out);
                output.close();
            }catch(Exception e){

            }
        }

    public static void main(String args[]){
        String s = null;
	FileReader fr = null;
	String temp = null;
        String taggedOutput = "";
	String str = "";
	String sent = "", recv = "", token = "", fn = "";
	int temp1 = 0, j = 0;
        learning l = new learning();
        try{
            BufferedReader br = new BufferedReader(new FileReader("./resource/unl/seinput.txt"));
            while ((s = br.readLine()) != null) {
		fn = "./resource/unl/tagDocswoR/" + s;
                //    tag_file = s;
		//	System.out.println("fn:" + fn);
		StringBuffer docbuff = new StringBuffer();
		fr = new FileReader(fn);
                
              //  System.out.println("fr:"+fr);
		   BufferedReader buff = new BufferedReader(new FileReader(fn));
                while( (str=buff.readLine())!=null){
                    docbuff.append(str);
                }
             		//System.out.println("docbuff:"+docbuff);
			StringTokenizer sentToken1 = new StringTokenizer(docbuff
						.toString().trim(), ".", false);
                        //l.loadTaggedData();
		//	j = 0;
                       	while (sentToken1.hasMoreTokens()) {
				sent = sentToken1.nextToken();
				recv = l.unsupervised(sent);
                       //         System.out.println("Inside While");
                                taggedOutput += recv;
	                }
                    //    System.out.println("TGOP:"+taggedOutput);
                     //   l.loadTaggedData();
                        l.writeintoFile(s,taggedOutput);
                        taggedOutput="";
            }

        }catch(Exception e){

        }
    }
}