
package org.apache.nutch.index.unl;

import java.io.*;
import java.util.*;
public class TamilWordOrder{
	
	ArrayList patterns=new ArrayList();
	Hashtable<String, ArrayList> cooccuringcon=new Hashtable<String, ArrayList>();
	Hashtable<String,Hashtable> cooccuringcon_count=new Hashtable<String, Hashtable>();
	Hashtable<String,ArrayList> cooccuringcon_countordered=new Hashtable<String, ArrayList>();
	
	public void loadPatternstoMemory() throws IOException{
	FileInputStream fis1 = new FileInputStream("./decon_inputs/Tamilwordorderpatterns.txt");
	InputStreamReader isr1 = new InputStreamReader(fis1);
    BufferedReader in1 = new BufferedReader(isr1);
	String tempVar = in1.readLine();
	String tempvar1 = new String();		
	Boolean val = false;
	ArrayList subpatterns=new ArrayList();
	

	while (null != tempVar) {
		System.out.println("tempVar:"+tempVar);
		//counter++;
		if(tempVar.equals("/s")){
			patterns.add(subpatterns);
			subpatterns=new ArrayList();
		}
	subpatterns.add(tempVar);
	
	tempVar = in1.readLine();
	}

	System.out.println("Patterns:"+patterns.toString());
	
}       
	public Hashtable<String,ArrayList> getWordOrder(){
		
		return 	cooccuringcon_countordered;
		
	}
	public void pickPatterns(){
		
	for(int i=0;i<patterns.size();i++){
		ArrayList subpat=(ArrayList)patterns.get(i);
		for(int j=0;j<subpat.size();j++){
			String rel=subpat.get(j).toString();
			findCooccuringPatterns(rel);
			
		}		
	}
	
	System.out.println("Co occuring Patterns:"+cooccuringcon.toString());
	 
    Enumeration keys = cooccuringcon.keys();
    //aHashTable is an instance of java.util.Hashtable



 
    while( keys.hasMoreElements() ) 
    {System.out.println("************************");
        Object aKey = keys.nextElement();
       ArrayList aValue = (ArrayList)cooccuringcon.get(aKey);
        System.out.println("the key:"+aKey.toString()+"the Value:"+aValue.toString());
       findCooccuringconCount(aValue, aKey.toString());
        System.out.println("************************");
    }

	}
	public void  findCooccuringconCount(ArrayList cons,String Key){
		
		Hashtable<String,Integer> h=new Hashtable<String,Integer>();
		for(int i=0;i<cons.size();i++){
			int count =0;
			String s1=cons.get(i).toString();
			for(int j=0;j<cons.size();j++){
				String s2=cons.get(j).toString();
				if(s1.equals(s2)){
					count++;					
				}
			}
			h.put(s1, count);			 
		}
		
		 cooccuringcon_count.put(Key, h);
	}
	
	public void printCooccuringcon_counthashtable(){
		 Enumeration keys = cooccuringcon_count.keys();
		    //aHashTable is an instance of java.util.Hashtable
		 
		    while( keys.hasMoreElements() ) 
		    	{
		    	System.out.println("************************");
		        Object aKey = keys.nextElement();
		         Hashtable<String,Integer>aValue =  (Hashtable<String,Integer>)cooccuringcon_count.get(aKey);
		        System.out.println("the Main  key:"+aKey.toString());
		        System.out.println("the cooccuring cons:"+aValue.toString());
		     /*   Enumeration keys1 = aValue.keys();
		        while(keys1.hasMoreElements()){       	
		        	 Object aKey1 = keys1.nextElement();
		        	 int count=(Integer)aValue.get(aKey1);		        	
		        }*/
		     
		        System.out.println("************************");
		    }
		
		
	}
	
	public void arrangeConsInDescendingOrder(){
		
		 Enumeration keys = cooccuringcon_count.keys();
		    //aHashTable is an instance of java.util.Hashtable
		 
		    while( keys.hasMoreElements() ) 
		    	{
		    	System.out.println("************************");
		        Object aKey = keys.nextElement();
		        Hashtable<String,Integer>h=new Hashtable<String,Integer>();
		        h =  (Hashtable<String,Integer>)cooccuringcon_count.get(aKey);
		        System.out.println("the Main  key:"+aKey.toString());
		        System.out.println("the cooccuring cons:"+h.toString());
		        ArrayList a=new ArrayList();
		        a= passHashtableForArrangement(h);
		     /*   Enumeration keys1 = aValue.keys();
		        while(keys1.hasMoreElements()){       	
		        	 Object aKey1 = keys1.nextElement();
		        	 int count=(Integer)aValue.get(aKey1);		        	
		        }*/
		        cooccuringcon_countordered.put(aKey.toString(), a);
		     
		        System.out.println("************************");
		    }
		
	}
	public ArrayList  passHashtableForArrangement( Hashtable<String,Integer>aValue){		   
		    ArrayList a =new ArrayList();
		    ArrayList sortedArray =new ArrayList();
		    a=new ArrayList(aValue.values());
		    Collections.sort(a);
		    System.out.println("the sorted array:"+a.toString());		    
		    for(int i=0;i<a.size();i++){		    	
		    	int count=(Integer)a.get(i);
		    	String rel=getValueforKey(aValue, count);
		    //	if(!sortedArray.contains(rel)){
		    	//sortedArray.add(rel);
		    	//}else{		    		
		    		String rel1=getValueforKey(aValue, count,sortedArray);
		    		sortedArray.add(rel1);
		    	//}
		    }
		    System.out.println("the actual hashtable  content :"+aValue.toString());		    
		    System.out.println("the sorted array's content :"+sortedArray.toString());	
		    return sortedArray;
	}
	public String getValueforKey(Hashtable<String,Integer>aValue,int count){
		
		 Enumeration keys = aValue.keys();
		 String reqd=new String();
		 while( keys.hasMoreElements() ) 
	    	{
	    	System.out.println("************************");
	        Object aKey = keys.nextElement();
	        reqd=aKey.toString();
	        int count1=(Integer)aValue.get(aKey);
	        if(count==count1){
	        	
	        	break;
	        }
	    	}
	        
		return reqd;
	}
	public String getValueforKey(Hashtable<String,Integer>aValue,int count,ArrayList rel){
		
		 Enumeration keys = aValue.keys();
		 String reqd=new String();
		 while( keys.hasMoreElements() ) 
	    	{
	    	System.out.println("************************");
	        Object aKey = keys.nextElement();
	        reqd=aKey.toString();
	        int count1=(Integer)aValue.get(aKey);	        
	        System.out.println("The passed array:"+rel.toString());
	        System.out.println("The key to be checked:"+reqd);
	        if((count==count1) && (!rel.contains(reqd))){	       
	        	 System.out.println("Inside if ");
	        	break;
	        }
	    	}
	        
		return reqd;
	}
	public void findCooccuringPatterns(String reln){
		try{
		for(int i=0;i<patterns.size();i++){
			ArrayList subpat=(ArrayList)patterns.get(i);
			for(int j=0;j<subpat.size();j++){
				String rel=subpat.get(j).toString();
			//	System.out.println("the problematic j"+j+"problematic size:"+subpat.size());
			      if(rel.equals(reln) && j<=subpat.size()-2){			    			    	 
			    	  String next_rel=subpat.get(j+1).toString();
			    	 // System.out.println("the reln:"+reln);
			    	 // System.out.println("the rel:"+rel);
			    	  ArrayList a=new ArrayList();
			    	  a=  cooccuringcon.get(reln);			    	 
			    	  if(a!=null){
			          System.out.println("size of array a:"+a.size()+"key:"+reln);
			    	  a.add(next_rel);
			    	  }
			    	  if(a==null){
			    		  a=new ArrayList();
			    		  a.add(next_rel);
			    	  }
			    	  cooccuringcon.put(reln, a);
			      }
				
			}		
		}
		}catch(Exception e){e.printStackTrace();}
		
	}
	
public static void main(String args[]) throws IOException{
	TamilWordOrder t=new TamilWordOrder();
	t.loadPatternstoMemory();
	t.pickPatterns();
	t.printCooccuringcon_counthashtable();
	t. arrangeConsInDescendingOrder();
	
}
	
}