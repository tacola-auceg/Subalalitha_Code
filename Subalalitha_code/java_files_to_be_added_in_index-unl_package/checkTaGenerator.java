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
import org.apache.nutch.enconversion.unl.ta.ConceptNode;
import org.apache.nutch.enconversion.unl.ta.ConceptToNode;
import org.apache.nutch.enconversion.unl.ta.FinalLLImpl;
//import org.apache.nutch.enconversion.unl.ta.FrameRST;
import org.apache.nutch.enconversion.unl.ta.UWDict;
import org.apache.nutch.enconversion.unl.ta.BST;
import org.apache.nutch.enconversion.unl.ta.HeadNode;
import org.apache.nutch.enconversion.unl.ta.MultiList;
import clia.unl.Source.Word_Gen.Generator.verbinfinitive;
import clia.unl.Source.Word_Gen.Generator.word_noun;
import clia.unl.Source.Word_Gen.Generator.word_verb;

public class checkTaGenerator{
	
	
	public StringBuffer classifyVerb(String anal,String word){
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
		String s1="";
			
            if(anal.contains("அ") && anal.contains("Infinitive Suffix") && !anal.contains("Genitive Case")){
            	verbinfinitive vf=new verbinfinitive();
            	String umout=vf.verbgetinfi(word,"");
				System.out.println("umout  "+umout);
				sb1.append(umout);
            }
            else if((anal.contains("ஆவிடில்")&& anal.contains("Negative Conditional Suffix"))||(anal.contains("ஆது")&& anal.contains("Future Negative  Suffix" ))){
				rule="வினை+எச்சம்+எதிர்மறை";
				// sb= w.verbgeneration("வேண்டு","Past Tense Marker","Third Person Femime Singular Suffix","plural","வினை+இறந்தகாலம்+நிபந்தனை விகுதி");	     
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
			   System.out.println("inside thth case");
			   rule="வினை+எச்சம்+ஈற்றசை";	
			   s1=s1+"thth";
			   System.out.println("analysed :"+anal+"rule:"+rule+"s1"+s1);
			    sb1= w.verbgeneration(word,anal,rule,s1);
			   System.out.println("word"+sb1.toString());
			   
		   }else if(anal.contains("உ") && anal.contains("Verbal Participle Suffix") && anal.contains("ந்த்") && anal.contains("Past Tense Marker")){
			   
			   rule="வினை+எச்சம்+ஈற்றசை";	
			   s1=s1+"innt";
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
	   
	}
	   
	  
	public static void main(String srgs[])throws Exception{
	    checkTaGenerator checkt=new checkTaGenerator();
	    StringBuffer sb= new StringBuffer();
	    word_verb w=new word_verb();
	   //word_noun w=new word_noun();
	    //	  sb=  checkt.classifyVerb("உ"+"Verbal Participle Suffix"+"த்த்"+"Past Tense Marker","படி");
	    //   sb= w.verbgeneration("கல","","வினை+எச்சம்+நிலை+(பால்/இட/எண்)/(தொ.பெ.விகுதி)/(+வே.உ)","வேண்டி");
	 // sb= w.NounCMGen1("அழகு" ,"பெயர்ச்சொல்+உரிச்சொல் ஈற்றசை","");
	//  sb= w.NounCMGen1("அழகு" ,"பெயர்ச்சொல்+உரிச்சொல் ஈற்றசை","இய");
	//  sb= w.NounCMGen1("அழகு" ,"பெயர்ச்சொல்+உரிச்சொல் ஈற்றசை","");
	
	  
	//  sb= w.NounCMGen1("காவிரி" ,"பெயர்ச்சொல்+உரிச்சொல் ஈற்றசை","இன்");
	    //sb= w.NounCMGen1("காவிரி" ,"பெயர்ச்சொல்+பன்மை+வேற்றுமை உருபு","இல்");
	 //   sb= w.NounCMGen1("கல்வியறிவு" ,"பெயர்ச்சொல்+உரிச்சொல் ஈற்றசை","ஐ");
	    // sb= w.verbgeneration("விளையாடு" ,"","வினை+உம்+பெயரெச்ச பின்னொட்டு","படியே");
	    // sb= w.verbgeneration("விளையாடு" ,"","வினை+இறந்தகாலம்+நிபந்தனை விகுதி","");
	    // sb= w.verbgeneration("வா" ,"","வினை+இறந்தகாலம்+நிபந்தனை விகுதி","");
	    String anal="@Verb@Past";
	   // String anal="@Verb@Past";
	 // sb= w.verbgeneration("வா " ,anal,"வினை+எச்சம்+ஈற்றசை","nthulla");
	  sb= w.verbgeneration("அமை" ,anal,"வினை+எச்சம்+ஈற்றசை","nthulla");
	   // sb= w.verbgeneration("அழைக்கப்படு" ,anal,"வினை+காலம்+பால்/இட/எண்","");
	     
	    System.out.println("the generated output"+sb.toString());		
	} 
}
