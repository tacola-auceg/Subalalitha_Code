package org.apache.nutch.index.unl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class EnglishMorphologicalGenerator{
	public Hashtable<String,String> irregularnoun=new Hashtable<String, String>();
	public Hashtable<String,Object> irregularverb=new Hashtable<String, Object>();
	public ArrayList vowel=new ArrayList();
	public ArrayList oesexception=new ArrayList();
	
	public void loadMemory()throws Exception{
		
		loadIrregularNouns();
		loadIrregularVerbs();
		vowel.add('a');
		 vowel.add('e');
		 vowel.add('i');
		 vowel.add('o');
		 vowel.add('u');
		 oesexception.add("photo");
		 oesexception.add("zero");
		 oesexception.add("piano");
		
	}
	public String generateEnglishWord(String term,String attribute){
		
		String generate="";
		try{
			if(attribute.contains("@Noun")){
				
				generate=generateEngNoun(term);
				
			}
			else if(attribute.contains("@Verb")){
				
				generate=generateEngVerb(term,attribute);
				
			}
		}catch(Exception e){};
		return generate;
		
	}
	public String generateEngNoun(String term){
		String generated=null;
		generated=irregularnoun.get(term);
		
		if(generated==null){
			System.out.println("entry into the noun generator:"+term);
			int esindex=term.length()-2;
			
			//System.out.println("the es:"+term.charAt(esindex));			
			//System.out.println("the boolean:"+!term.equals("photo"));
			if(term.endsWith("ouse")){		
				
				generated=term.replace("ouse", "ice").toString();
			}else if(term.endsWith("af")){
				
				String replace=term.substring(term.length()-1, term.length());
				generated=term.replace(replace, "ves").toString();
			}else if(term.endsWith("fe")){
				
				String replace=term.substring(term.length()-2, term.length());
				generated=term.replace(replace,"ves").toString();
			}
			else if(term.charAt(esindex)=='s'||term.endsWith("s")||term.endsWith("ch")||(term.endsWith("o")&& !vowel.contains(term.charAt(esindex))&& !oesexception.contains(term))){// &&!term.equals("photo"))||(term.endsWith("o")&& !vowel.contains(term.charAt(esindex)) &&!term.equals("zero"))||(term.endsWith("o")&& !vowel.contains(term.charAt(esindex)) &&!term.equals("piano"))){//!term.equals("zero")||!term.equals("piano")))){
				
				generated=term+"es";
			}
			else if(term.endsWith("ge")||term.endsWith("ch")){
				
				generated=term+"s";
				
			}else if(term.endsWith("y") &&( !vowel.contains(term.charAt(esindex)))){
				
				int oldindex=term.lastIndexOf("y");
				generated=term.subSequence(0, oldindex).toString();				
				generated=generated+"ies";
			}			
			else{
				
				generated=term+"s";
			}		
			
		System.out.println("the generated word in Noun:"+generated);		
			
		}
		
		////System.out.println("the length of the term:"+term.length());
		
		return generated;
	}
	public void loadIrregularNouns()throws Exception{
		//System.out.println("outside file");
		FileInputStream fis1 = new FileInputStream("./decon_inputs/irregular_nouns.txt");
		InputStreamReader isr1 = new InputStreamReader(fis1);
		BufferedReader in1 = new BufferedReader(isr1);
		String tempVar = in1.readLine();	
		String relation="";
		String cases="";
		String condition="";
		//String position="";
		
		int counter=0;
		try{
			while (null != tempVar) {
				StringTokenizer st=new StringTokenizer(tempVar,"@");
				String key=st.nextToken();
				String value=st.nextToken();
				irregularnoun.put(key, value);					
				tempVar = in1.readLine();
		}
		}catch(Exception e){}
		
	    
isr1.close();
fis1.close();
in1.close();

//System.out.println("Hashtable:"+irregularnoun.toString());
		
		
	}
	public void loadIrregularVerbs()throws Exception{
		//System.out.println("outside file");
		FileInputStream fis1 = new FileInputStream("./decon_inputs/irregular_verbs.txt");
		InputStreamReader isr1 = new InputStreamReader(fis1);
		BufferedReader in1 = new BufferedReader(isr1);
		String tempVar = in1.readLine();	
		String relation="";
		String cases="";
		String condition="";
		//String position="";
		
		int counter=0;
		try{
			while (null != tempVar) {
				StringTokenizer st=new StringTokenizer(tempVar,"@");
				IrregularVerb irv=new IrregularVerb();
				String key=st.nextToken();
				irv.past_simple=st.nextToken();
				irv.past_participle=st.nextToken();
				irv.third_person=st.nextToken();
				irv.present_participle=st.nextToken();
				irregularverb.put(key, irv);					
				tempVar = in1.readLine();
		}
		}catch(Exception e){}
		
	    
isr1.close();
fis1.close();
in1.close();

}
	public String generateIrregularKeyTerm(String term){
		
		String term1=term.substring(0,1);
    	System.out.println("the term1:"+term1);
    	term1=term1.toUpperCase();
    	term=term.subSequence(1, term.length()).toString();
    	System.out.println("the term before:"+term);
    //	term1=term1.toLowerCase();
    	term=term1+term;	    	
    	System.out.println("the term after:"+term);
    	return term;
	}
	
	public void generateIrregularNoun(String term){
		
		String generate="";
		boolean flag=false;
		generate=irregularnoun.get(term);
		if(!generate.equals("")){
			
			flag=true;
		}	
		//System.out.println("the genrated irregular noun:"+generate);
	}
	 public String generateEngVerb(String term,String attribute){
	    	String generate="";
	    	//System.out.println("entry into the verb generator:"+term+"attribute:"+attribute);
		 if(attribute.contains("@present")){
			 generate=generateEngPresentTense(term,attribute);
		 }else if(attribute.contains("@past")){
			 generate=generateEngPastTense(term,attribute);
		 }else if(attribute.contains("@future")){
			 generate=generateEngFutureTense(term,attribute);
		 }    	
	    return generate;
	    }
	
	    public String  generateEngPresentTense(String term,String attribute){
	    	String generate="";
	    	IrregularVerb ir=new IrregularVerb();	
	    //	String term1=term.term.indexOf(term.charAt(0));
	    	
	    	if(attribute.contains("@presentpartciple") ){
	    		ir=(IrregularVerb)irregularverb.get(term);
	    		if(ir!=null)
	    		generate =ir.past_participle;
	    		
	    	/*	ir=(IrregularVerb)irregularverb.get(term);
	    		String simplepast=ir.past_simple;
	    		String pastparticiple=
	    		String thirdperson=ir.third_person;
	    		String presentparticiple=ir.present_participle;	*/	
	    		if(!generate.equals("")){
	    			generate=generateEngPresentParticiple(term);	
	    		}
	    		
	    	}else if (attribute.contains("@passive")){
	    		
	    		generate=generatePastPerfect(term,attribute);
	    		generate=generate.replace("had", "is");
	    	}
	    	else if((attribute.contains("@3") && attribute.contains("@singular")) ){
	    		System.out.println("************************ 3rd person singular************************");
	    		ir=(IrregularVerb)irregularverb.get(term);
	    		if(ir!=null)
	    		generate =ir.third_person;
	    		
		    	/*	ir=(IrregularVerb)irregularverb.get(term);
		    		String simplepast=ir.past_simple;
		    		String pastparticiple=
		    		String thirdperson=ir.third_person;
		    		String presentparticiple=ir.present_participle;	*/	
		    		if(generate==""){
		    			generate=generateEngPresentParticiple(term);	
		    		}
	    		generate=generateThirdPersonSingular(term);
	    		
	    	}
	    	else if(attribute.contains("@progress")){
	    		
	    		generate=generatePresentProgressive(term,attribute);
	    	}else if(attribute.contains("@perfect")){
	    		
	    		generate=generatePresentPerfect(term,attribute);
	    	}else if(attribute.contains("@perfectprogress")){
	    		
	    		generate=generatePresentPerfectProgressive(term,attribute);
	    	}
	    	return generate;
	    }
	    
	    public String generateEngPastTense(String term,String attribute){
	    	term=term.trim();
	    	
	    	String generate="";
	    //	boolean regularflag=false;
	    	term=generateIrregularKeyTerm(term);
	    	try{
	    	IrregularVerb ir=new IrregularVerb();	
	  
	    	if(attribute.contains("@past")){
	    		ir=(IrregularVerb)irregularverb.get(term);
	    		if(ir!=null){
	    		generate =ir.past_simple;
	    		}
	    	//	regularflag=true;
	    		
	    	}
	    	System.out.println("generate"+generate);
	    	 if(generate.equals("")){
	      	System.out.println("have i come inside past tense generate:"+generate);
	    		 if(attribute.contains("@progress")){
	    		generate=generatePastProgressive(term,attribute);
	    		}else if(attribute.contains("@perfect")){
	    		
	    		generate=generatePastPerfect(term,attribute);
	    		}else if(attribute.contains("@perfectprogress")){
	    		
	    		generate=generatePastPerfectProgressive(term,attribute);
	    	    }
	    		else {
	    			System.out.println("have i come inside main past tense generate:"+generate);
    			generate=generateSimplePast(term);
    		}
	    }
	    	}catch(Exception e){e.printStackTrace();};
	    	return generate ;
	    	
	    } public String generateEngFutureTense(String term,String attribute){
	    	
	    	
	    		String generate="";
	    	
	    		try{
		    	generate=generateSimpleFuture(term,attribute);
	    	
    		
	    	if(attribute.contains("@progress")){
	    		generate=generateFutureProgressive(term);
	    	}else if(attribute.contains("@perfect")){
	    		//System.out.println("inside perfect");
	    		generate=generateFuturePerfect(term,attribute);
	    	}else if(attribute.contains("@perfectprogress")){
	    		
	    		generate=generateFuturePerfectProgressive(term);
	    	}
   }catch(Exception e){};
	    	return generate;
	    }
	    public String generateSimpleFuture(String term,String attribute){
	    	
	    	String generate="will "+term;
	    	return generate;
	    	
	    }
	   public String generateSimplePast(String term){
		   System.out.println("the term inside simple past:"+term);
		//   String generate="";
		   term=term.trim();
		   String generate="";
		   try{
	    	
	    //	boolean regularflag=false;
	    	String term2=term;
	    	term=generateIrregularKeyTerm(term);
	    
	    	IrregularVerb ir=new IrregularVerb();	
	  
	    	
	    		ir=(IrregularVerb)irregularverb.get(term);
	    		if(ir!=null){
	    		generate =ir.past_simple;
	    		System.out.println("the term inside if condition");
	    		}
	    		
	    		
	    	term=term2;
		
		   if(!generate.equals("")){
		   if(term!=null){
		         term=term.trim();		   
		         int lc=term.length()-1;
		         int cbl=term.length()-2;	    	
	    	if(term.endsWith("e")){
	    		// System.out.println("have i come here in simple past e ending:"+term);
	    		if(term.contains("@plural")&& !term.contains("@1"))
	    		generate="were"+term+"d";
	    		else if(term.contains("@singular") && !term.contains("@1")){
	    			generate="was"+term+"d";
	    		}else{
	    			generate=term+"d";
	    		}
	    	}
	    	else if (term.endsWith("y")){
	    		
	    		String temp=term.substring(0, term.length()-1);
	    		if(term.contains("@plural") && !term.contains("@1") ){
	    			generate="were "+temp+"ied";
	    		}else if(term.contains("@singular") && !term.contains("@1")){
	    			generate="was "+temp+"ied";
	    		}else{
	    			generate=term+"ied";
	    		}
	    		
	    		
	    	}else if (term.endsWith("c")){
	    		
	    		//String temp=term.substring(0, term.length()-1);
	    		if(term.contains("@plural") && !term.contains("@1")){
	    			generate="were "+term+"ked";
	    		}else if(term.contains("@singular") && !term.contains("@1")){
	    			generate="was "+term+"ked";
	    		}else{
	    			generate=term+"ked";
	    		}
	    		
	    		
	    	}
	    	
	    	else if(!vowel.contains(term.charAt(lc)) && vowel.contains(term.charAt(cbl)) && !term.endsWith("k") && !term.endsWith("r")){
	    		 // System.out.println("have i come here in simple past last else if:"+term);
	    		String temp=term.substring(term.length()-1);
	    		//System.out.println("temp:"+temp);
	    		if(term.contains("@plural") && !term.contains("@1")){
	    		generate=term+temp+"ed";
	    		}else if(term.contains("@singular") && !term.contains("@1")){
	    			generate="was "+term+temp+"ed";
	    		}else{
	    			generate=term+"ed";
	    		}
	    	}
	    	else{
	    		 // System.out.println("have i come here in simple past last else :"+term);
	    		if(term.contains("@plural") && !term.contains("@1")){
	    		generate="were "+term+"ed";
	    		}else if(term.contains("@singular") && !term.contains("@1")){
	    			generate="was "+term+"ed";
	    		}else{
	    			generate=term+"ed";
	    		}
	    		
	    	}
	    	//System.out.println("the generated past tense : "+generate);
		 }
		   }
		   }catch(Exception e ){e.printStackTrace();}
	    	return generate;
		   
		
		   
	   }    
	    public String generatePastPerfectProgressive(String term,String attribute){
	    	String generate="";
	    	//String generate=null;
	    	term=generateIrregularKeyTerm(term);
	    	try{
	    	IrregularVerb ir=new IrregularVerb();	
	    	ir=(IrregularVerb)irregularverb.get(term);
	    	if(ir!=null)
	    	
	    		generate = "had been "+ir.present_participle;
	    	
	    
    		
	    	/*	ir=(IrregularVerb)irregularverb.get(term);
	    		String simplepast=ir.past_simple;
	    		String pastparticiple=
	    		String thirdperson=ir.third_person;
	    		String presentparticiple=ir.present_participle;	*/	
	    	if(!generate.equals("")){
	    		
	    		String ing=generateEngPresentParticiple(term);
	    		generate="had been "+ing;
	    	}
	    	
	    	}catch(Exception e ){};
	    	
	    	//System.out.println("the past perfect progressive:"+generate);
	    	return generate;
	    }public String generatePresentPerfectProgressive(String term,String attribute){
	    	String generate="";
	    	//String generate=null;
	    	term=generateIrregularKeyTerm(term);
	    	try{
	    	IrregularVerb ir=new IrregularVerb();	
	    	
	    		ir=(IrregularVerb)irregularverb.get(term);
	    		if(ir!=null)
	    		generate = "have been "+ir.present_participle;
	    	if(!generate.equals("")){
	    	String ing=generateEngPresentParticiple(term);
	    		
	    		generate="have been "+ing;
	    	}
	    	}catch(Exception e){};
	    	//System.out.println("the present perfect progressive:"+generate);
	    	return generate;
	    }public String generateFuturePerfectProgressive(String term){
	    	String generate="";
	    	term=generateIrregularKeyTerm(term);
	    	//String generate=null;
	    	try{
	    	IrregularVerb ir=new IrregularVerb();	
	    	ir=(IrregularVerb)irregularverb.get(term);
	    	if(ir!=null)
	    	generate = "will have been "+ir.present_participle;
	    	if(!generate.equals("")){
	    	String ing=generateEngPresentParticiple(term);
	    		
	    		generate="will have been  "+ing;
	    	}
	    	}catch(Exception e){};
	    	//System.out.println("the future perfect progressive:"+generate);
	    	return generate;
	    }
	    public String generatePastPerfect(String term,String attribute){
	    	String generate="";
	    	term=generateIrregularKeyTerm(term);
	    	//String generate=null;
	    	try{
	    	IrregularVerb ir=new IrregularVerb();	
	    	ir=(IrregularVerb)irregularverb.get(term);
	    	if(ir!=null)
	    	generate = "had "+ir.past_simple;
	    	if(!generate.equals("")){
	   // 	String past=generateEngPastTense(term,attribute);
	    		String past=generateSimplePast(term);
	    		
	    		generate="had "+past;
	    	}
	    	}catch(Exception e){}
	    	
	    	//System.out.println("the past perfect :"+generate);
	    	return generate;
	    } public String generatePresentPerfect(String term,String attribute){
	    	String generate="";
	    	//String generate=null;
	    	term=generateIrregularKeyTerm(term);
	    	try{
	    	IrregularVerb ir=new IrregularVerb();	
	    	ir=(IrregularVerb)irregularverb.get(term);
	    	if(ir!=null)
	    	generate = "have "+ir.past_simple;
	    	
	    	if(!generate.equals("")){
	    	String past=generateSimplePast(term);
	    	
	    		
	    		generate="have "+past;
	    	}
	    	}catch(Exception e){};
	    	return generate;
	    }
	    public String generateFuturePerfect(String term,String attribute){
	    	String generate="";
	    	//String generate=null;
	    	term=generateIrregularKeyTerm(term);
	    	try{
	    	IrregularVerb ir=new IrregularVerb();	
	    	ir=(IrregularVerb)irregularverb.get(term);
	    	if(ir!=null)
	    	generate = " will have "+ir.past_participle;
	    	//System.out.println("the string generate in future perfect:"+generate);
	    	if(!generate.equals("")){
	    		String past=generateEngPastTense(term,attribute);    	
	    		
	    		generate="will have "+past;
	    	}
	    	
	    	}catch(Exception e){e.printStackTrace();};
	    	//System.out.println("the past perfect :"+generate);
	    	return generate;
	    }
	    
	    public String generatePastProgressive(String term,String attribute){
	    	term=term.trim();
	    	String generate="";
	    	//String generate="";
	    	term=generateIrregularKeyTerm(term);;
	    	
	    	//System.out.println("the past progrssive entry:"+term);
	    	try{
	    	String ing=null;
	    	IrregularVerb ir=new IrregularVerb();
	    	
	    	ir=(IrregularVerb)irregularverb.get(term);
	    	if(ir!=null){
	    	 ing =ir.present_participle;
	    	}
	    	if(ing==null){
	    	 ing=generateEngPresentParticiple(term);
	    	}
	    //	System.out.println("the ing in past progressive:"+ing);
	    	if(attribute.contains("@1")){
	    		
	    		generate="was "+ing;
	    	}else if(attribute.contains("@2") ){
	    		
	    		generate="were "+ing;
	    	}else if(attribute.contains("@3")&& attribute.contains("@singular")){
	    		
	    		generate="was "+ing;
	    	}else if(attribute.contains("@3")&& attribute.contains("@plural")){
	    		
	    		generate="were "+ing;
	    	}
	    //	//System.out.println();
	    	}catch(Exception e){e.printStackTrace();};
	    	return generate;
	    }
	    public String generatePresentProgressive(String term,String attribute){
	    	
	    	System.out.println("****************DECONDECONDECONDECONDECON**********************");  	
	    	String generate="";
	    	//String generate=null;
	    	String ing=null;
	    	term=generateIrregularKeyTerm(term);
	    	try{
	    	IrregularVerb ir=new IrregularVerb();	
	    	ir=(IrregularVerb)irregularverb.get(term);
	    	if(ir!=null)
	    	 ing =ir.present_participle;
	    	if(ing==null){
	    	 ing=generateEngPresentParticiple(term);
	    	}
	    	if(attribute.contains("@1")){
	    		
	    		generate="am "+ing;
	    	}else if(attribute.contains("@2") ){
	    		
	    		generate="are "+ing;
	    	}else if(attribute.contains("@3")&& attribute.contains("@singular")){
	    		
	    		generate="is "+ing;
	    	}else if(attribute.contains("@3")&& attribute.contains("@plural")){
	    		
	    		generate="are "+ing;
	    		System.out.println("GENERATED WORD IS:"+generate);
	    	}
	  //System.out.println("generate");
	    	}catch(Exception e){}
	    	return generate;
	    	
	    	
	    }public String generateFutureProgressive(String term){
	    	
	    	String ing=generateEngPresentParticiple(term);
	    	String generate="will be "+ing;
	    	return generate;
	    }
	    
	    public String generateThirdPersonSingular(String term){
	    	term=term.trim();
	    	String generate="";
	   /*character before last character*/ 	int cbl=term.length()-2;
	    	if(term.endsWith("ss")||term.endsWith("sh")||term.endsWith("ch")||term.endsWith("o")){
	    		generate=term+"es";
	    		
	    	}
	    	else if (term.endsWith("y")&& !vowel.contains(term.charAt(cbl))){
	    		
	    		String temp=term.substring(0, term.length()-1);
	    		generate=temp+"ies";
	    	}else{
	    		
	    		generate=term+"s";
	    	}
	    	//System.out.println("the generated third person verb:"+generate);
	    	return generate;
	    }
	    public   String  generateEngPresentParticiple(String term){
	    	    	
	    	//System.out.println("the term:");
	    	term=term.trim();
	    	boolean flagdouble=vowel.contains(term.charAt(term.length()-2));
	    	String generate="";
	    	if(term.endsWith("ee")){
	    		
	    		generate=term+"ing";
	    	}else if(term.endsWith("ie")){
	    		
	    		generate=term.replace("ie", "ying");
	    	}
	    	else if(term.endsWith("e")){
	    		
	    		term=term.substring(0, term.length()-1);
	    		generate=term+"ing";
	    	}
	    	else if((term.endsWith("t")&& (flagdouble==true))||(term.endsWith("g")&& (flagdouble==true))||(term.endsWith("p")&& (flagdouble==true))||(term.endsWith("n")&& (flagdouble==true))||(term.endsWith("m")&& (flagdouble==true))){//(vowel.contains(term.charAt(term.length()-2)))){
	    	
	    		String append=term.substring(term.length()-1);
	    		//System.out.println("append:"+append);
	    	   generate=term+append+"ing";
	    	}
	    	else{
	    		
	    		generate=term+"ing";
	    	}
	    	
	    	//System.out.println("the generate:"+generate);
	    	return generate;
	    	
	    }
public void generateIrregularVerb(String term){
	String generate="";
		IrregularVerb ir=new IrregularVerb();
		boolean flag=false;
		ir=(IrregularVerb)irregularverb.get(term);
		String simplepast=ir.past_simple;
		String pastparticiple=ir.past_participle;
		String thirdperson=ir.third_person;
		String presentparticiple=ir.present_participle;		
		//System.out.println("simple past:"+simplepast);
		//System.out.println("pastparticiple:"+pastparticiple);
		//System.out.println("thirdperson:"+thirdperson);
		//System.out.println("presentpartciple:"+presentparticiple);	
		if(!generate.equals("")){
			
			flag=true;
		}
	
		////System.out.println("the genrated irregular noun:"+generate);
	}


public void testGenerator()throws Exception{
	
	FileInputStream fis1 = new FileInputStream("./decon_inputs/regularverbs.txt");
	InputStreamReader isr1 = new InputStreamReader(fis1);
	BufferedReader in1 = new BufferedReader(isr1);
	String tempVar = in1.readLine();	
	

	
	try{
		while (null != tempVar) {
				
			EnglishMorphologicalGenerator eng=new EnglishMorphologicalGenerator();
			String input1=tempVar;			
			String attribute1="@Verb@present@perfect";
			String attribute2="@Verb@past@perfect@2";
			String attribute3="@Verb@future@perfect@2";
			String attribute4="@Noun@progress@2";
			String generate1=eng.generateEnglishWord(input1,attribute1);
			String generate2=eng.generateEnglishWord(input1,attribute2);
		String generate3=eng.generateEnglishWord(input1,attribute3);
		//	System.out.println("**********************************");
			System.out.println("the  generated present perfect tense of the verb "+input1.trim()+" is-------> "+generate1);
			System.out.println("the generated past perfect tense of the  verb "+input1.trim()+" is-----------> "+generate2);
			System.out.println("the generated future perfect tense of the verb "+input1.trim()+" is------------> "+generate3);		
			System.out.println("**********************************");
		
		tempVar = in1.readLine();
	}
	}catch(Exception e){}
	

	isr1.close();
	fis1.close();
	in1.close();

	}





	public static void main(String args[])throws Exception{
		EnglishMorphologicalGenerator eng=new EnglishMorphologicalGenerator();
		String input1="come";
		String input2="banana";
		String attribute1="@Verb@present@progress@2";
		String attribute2="@Verb@past@perfect@2";
		String attribute3="@Verb@future@progress@2";
		String attribute4="@Noun@progress@2";
		eng.loadMemory();
	 // 	eng.testGenerator();
	  //eng.generateIrregularNoun(input);
		//eng.generateIrregularVerb(input);
		String generate1=eng.generateEnglishWord(input1,attribute1);
	//	String generate2=eng.generateEnglishWord(input1,attribute2);
		//String generate3=eng.generateEnglishWord(input1,attribute3);
	//	String generate4=eng.generateEnglishWord(input2,attribute4);
		
		System.out.println("the  generated present perfect tense of the verb"+input1+" is-------> "+generate1);
		//System.out.println("the generated past perfect tense of the  verb"+input1+" is-----------> "+generate2);
		//System.out.println("the generated future perfect tense of the verb"+input1+" is------------> "+generate3);
		//System.out.println("the generated plural form of  noun "+input2+" is--------------> "+generate4);
		
		
		
	}
	
}