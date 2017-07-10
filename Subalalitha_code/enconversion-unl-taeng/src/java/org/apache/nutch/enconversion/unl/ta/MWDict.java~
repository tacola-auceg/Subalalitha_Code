package org.apache.nutch.enconversion.unl.ta;

import org.apache.nutch.util.NutchConfiguration;
import org.apache.hadoop.conf.Configuration;

import java.io.*;
import java.lang.*;
import java.util.*;

public class MWDict
{
	
	 public MWBST mwbst; 
	
	 public MWBSTNode bstnode; 
	
public static void main(String args[])
{
	MWDict mw = new MWDict();
}
public MWDict() 
{
	
	mwbst= new MWBST();
	bstnode =  new MWBSTNode();
	loadDic();
	//traverse();
}

public void loadDic()
{
	Configuration conf = NutchConfiguration.create();
	String path=conf.get("unl_resource_dir");
	String conentry;
//	//System.out.println("Inside Load Multi Words Dictionary");
	try
	{
	BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path+"/multiwords.txt"),"UTF8"));
		while((conentry=in.readLine()) != null)
		{
//			//System.out.println("Inside Concept While");
			
			StringTokenizer tok = new StringTokenizer(conentry,"/");
			String lex = tok.nextToken();
			String hw = tok.nextToken();
			String cl = tok.nextToken();
			int nw = Integer.parseInt(tok.nextToken());
			String fw = tok.nextToken();
			int hc = fw.hashCode();
			mwbst.insert(hc,lex,hw,cl,nw,fw);
		}
		in.close();
	}
	catch(Exception e)
	{
		e.printStackTrace();
		//System.out.println("Exception in loadDic"+e);
	}
	
}
public MWBST get_mwbst()
{
	return mwbst;
}
public void traverse()
{
	mwbst.inorder();
	//System.out.println(mwbst.Conceptsize());
}
}
