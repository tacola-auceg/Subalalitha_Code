package org.apache.nutch.enconversion.unl.ta;
import java.io.*;
import java.lang.*;
import java.util.*;

public class MWBST
 {
   	MWBSTNode root = null;
    PrintStream concept,temp;
    int ConceptCount;
    
    public MWBST()
     {
    	try
    	{
    	    	
    	    	ConceptCount = 0;
    	  }
    	  catch(Exception e)
    	  {
    	  	
    	  }
    }
    
        
    //traversing MWBST
     
    public void inorder() 
    {
        inorder(root);
    }
            
    protected void visit(MWBSTNode p) 
    {
       // concept.print(p.el + " ");
        //concept.print(p.lexeme + " ");
       // concept.print(p.headword+ " ");
      //  concept.print(p.constraint + " ");
      //  concept.println(p.no_words + " ");
        if(p.getNext() != null)
        	{
        		visit(p.getNext());
        	}
        
    }
    
    protected void inorder(MWBSTNode p) 
    {
        if (p != null) {
             inorder(p.left);
             visit(p);
             inorder(p.right);
        }
    }
    
    
    
    //to search for a string
    
    public MWBSTNode search(int el) 
    {
        return search(root,el);
    }
    
    protected MWBSTNode search(MWBSTNode p, int el) 
    {
        //temp.println("inside search");
         while (p != null)
        	    
            if (el == p.el)
                 return p;
            else if (el < (p.el))
                 p = p.left;
            else p = p.right;
        return null;
    }
    
    
    //to insert new string
    
     public void insert(int el,String lex, String hw, String CL, int nw, String fw) 
     {
        MWBSTNode p = root, prev = null;
        //temp.println("Inside Insert");
        while (p != null)
        { 
          // find a place for inserting new node;
            prev = p;
           if (el < (p.el))
            {
            	 p = p.left;
            }
                
            else if (el > (p.el))
            {
                 p = p.right;
                       	
             }
           else
           	{
           		break;
           	}    	
         	                
        }
        
        if (root == null)    // tree is empty;
             root = new MWBSTNode(el,lex,hw,CL,nw,fw);
        else if (el < (prev.el))
             prev.left = new MWBSTNode(el,lex,hw,CL,nw,fw);
        else  if (el > (prev.el))
             prev.right  = new MWBSTNode(el,lex,hw,CL,nw,fw);
         else
         {
         		//temp.println("Inside OFB");
         		//temp.println("Given lexeme"+lex);
         		//temp.println(prev.lexeme);
         		//temp.println(prev.next);
         		while(prev.next  != null)
         		{
         			//temp.println("Inside While");
         			if (prev.next == null) 
         				break;
       				else if (prev.next.no_words >= nw)
       				{
       					//temp.println("Insertion at end");
       					//temp.println(prev.next.no_words);
       					//temp.println("Given lexeme now"+nw);
       					prev = prev.getNext();
       				}
       				else
       					break;
         		 }
    			
    			if (prev.next == null) 
         			prev.setNext(new MWBSTNode(el,lex,hw,CL,nw,fw));
         		else if(prev.no_words < nw)
         		{
         			MWBSTNode new_node = new MWBSTNode(el,prev.lexeme,prev.headword,prev.constraint,prev.no_words,fw);
         			prev.lexeme = lex;
         			prev.headword = hw;
         			prev.constraint = CL;
         			prev.no_words = nw;
         			new_node.setNext(prev.getNext());
         			prev.setNext(new_node);
         		}
    			else
         		{
         				MWBSTNode new_node = new MWBSTNode(el,lex,hw,CL,nw,fw);
         				new_node.setNext(prev.getNext());
         				prev.setNext(new_node);
         		} 
         }
        	 
        ConceptCount++;
    }
    public  String find_tamil_multiword(int el,String tamil,String UNL)
    {
    	MWBSTNode mn=new 	MWBSTNode();
    	////System.out.println("passed string "+tamil);
    	////System.out.println("passedunl "+UNL);
    	////System.out.println("passedint "+el);
    	mn=search(el);
    	////System.out.println("the retrieved first word"+mn.firstword);
    	
    	while(mn!=null)
    	{
    		
    		////System.out.println("the nodes"+mn.firstword+mn.lexeme);
    		if((mn.firstword).equals(tamil) && (mn.headword+"("+mn.constraint+")").equals(UNL))
    		{
    			//System.out.println("came inside");
    			break;
    		}
    		////System.out.println("the head words"+mn.headword);
    		mn=mn.getNext();
    	}    	
    	return mn.lexeme;
    	
    }
    
    public int Conceptsize()
	{
		return ConceptCount;
	}
	
public static void main(String args[])
{
 MWBST mwbst = new MWBST();
}

    }
