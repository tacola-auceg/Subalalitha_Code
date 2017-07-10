package org.apache.nutch.enconversion.unl.ta;
public class MWBSTNode {
	
	int el;
	public String lexeme;
	public String headword;
	public String constraint;
	public int no_words;
	public String firstword;
	
	MWBSTNode next;
    
    MWBSTNode left, right;
    
    public MWBSTNode() 
    {
        next = left = right = null;
    }
    public MWBSTNode(int el,String lex,String hw,String CL,int nw,String fw) 
    {
   		this.el = el;
        lexeme = lex;
        headword = hw;
        constraint = CL;
        no_words = nw;
        firstword = fw;
     }
     
     public MWBSTNode getNext()
	 {
			return next;
	 }
	
	public void setNext(MWBSTNode nxt)
	{
			next = nxt;
		}
  }
