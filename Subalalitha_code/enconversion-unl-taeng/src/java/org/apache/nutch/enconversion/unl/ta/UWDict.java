package org.apache.nutch.enconversion.unl.ta;

import org.apache.nutch.util.NutchConfiguration;
import org.apache.hadoop.conf.Configuration;

import java.io.*;
import java.lang.*;
import java.util.*;

public class UWDict
{
	
	public  BST bst;
	public BSTNode bstnode;

public UWDict() 
{
	
	bst =  new BST();
	bstnode = new BSTNode();
	loadDic();
}
String tamil="";
public String traverse(BST bst1,String sw)
{
	//bst1.inorder();
	tamil=bst1.inorder1(sw);
	return tamil;
        
}

public void loadDic()
{
	Configuration conf = NutchConfiguration.create();
	String path=conf.get("unl_resource_dir");

	String conentry;
	try
	{//System.out.println("entered befor");
		//BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path+"/uwdict.txt"),"UTF8"));
		
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("./resource/unl/uwdict.txt")));

		while((conentry=in.readLine()) != null)
		{
			
			StringTokenizer tok = new StringTokenizer(conentry.trim(),"/");
			String lex = tok.nextToken().trim();
			int hc = lex.hashCode();
			String hw = tok.nextToken().trim();
			String cl = tok.nextToken().trim();
			bst.insert(hc,lex,hw,cl);
					
			
			
			
		}
		//System.out.println("Size of UWDict"+bst.Conceptsize());
		//get_bst1(bst);		
		
		in.close();
	}
	catch(Exception e)
	{
		e.printStackTrace();
		//System.out.println("Exception in loadDic"+e);
	}
	
}
public static BST bstree=new BST();
public  BST get_bst()
{
	return bst;
}
public static BST get_bst1(BST bst1)
{
	
	
	return bst1;
}
public static void main(String args[])
{
	 UWDict b =new UWDict();
	//BST bs=new BST();
	//bs=b.get_bst();
	//b.traverse(bs,"");
}

}
