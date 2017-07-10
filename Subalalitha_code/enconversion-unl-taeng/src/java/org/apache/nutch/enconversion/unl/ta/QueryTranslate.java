package org.apache.nutch.enconversion.unl.ta;
import java.lang.*;
import java.io.*;
import java.util.*;


public class QueryTranslate
{

    PrintStream allout;

    UWDict uwdict;

    BSTNode bn;
    BST dict;

    ArrayList anal;
    ArrayList root;
    ArrayList rootnew;
    ArrayList pos;
    ArrayList rootrel;

    ArrayList hw;
    ArrayList uw;
    ArrayList no;

    int aojindex;
    int plfindex;
    int cobindex;
   	int insindex;
    int timindex;

    
    int verbcount;

    String verb1;
    String verb2;
    String verb3;

    boolean bolverb1;
    boolean bolverb2;
    boolean bolverb3;

    String verbno1;
    String verbno2;
    String verbno3;

	String result="";

    public QueryTranslate()  {
    	try
    	{
    	   	dict = new BST();
		//allout = new PrintStream(new FileOutputStream(new File("/usr/output/finalout.txt"),true));


    	   	uwdict = new UWDict();
    	   	uwdict.bst.inorder();
    	   	dict = uwdict.bst;

    	 }
    	 catch(Exception e)
    	 {
    	 	//System.out.println("Exception in creating output file"+e);
    	 }
    }

    public String translate(String st)
    {
    try
    {
    	String r_word = "";
    	String lex_end ="";
    	
    	//System.out.println("Inside Enconversion");

      //To initialize all array list, variables...

	//System.out.println(st);
        init();


    //    //System.out.println("Given Sentence"+st);

      //Identify words delimiter

        StringTokenizer inp = new StringTokenizer(st,";");
        
      //  //System.out.println("No of Tokens in a given string"+inp.countTokens());
        
        int noofpairs = inp.countTokens();
        int ctnoofpairs=0;

     //store the words in inp array List and root words in root array List

        while(ctnoofpairs < noofpairs)
	{

        	String word = inp.nextToken().trim();
		
        	StringTokenizer inp1 = new StringTokenizer(word);
        	
        	//llout.println("No of Tokens in a given pair"+inp1.countTokens());
			
			if(inp1.countTokens() == 2)
			{
				r_word = inp1.nextToken().trim();
				lex_end = inp1.nextToken().trim();
			}
			else
			{
				r_word = inp1.nextToken().trim();
				lex_end = "None";
			}
			
			
          //to add root word arrayList
          
            root.add(r_word);
            anal.add(lex_end);

			ctnoofpairs++;

		
		}
		
		verb1 = root.get(root.size()-1).toString().trim();
        
         //to write words,pos,root in file

        //System.out.println("Root Word"+root);
       //System.out.println("Lex End"+anal);
      //System.out.println("Verb"+verb1);
            
         findUW();
     //System.out.println("Find UW "+result);	
         verbno1 = findverbnumber(verb1);
       //System.out.println("Verb"+verb1);
       //System.out.println("Verb Number"+verbno1);

         findrelnindex();
//System.out.println("Find Rel "+result);	
     
      } // end of try blcok
	
      catch(Exception e){
            //System.out.println("Exception inside main pgm"+e);
      }
    return result;
  }

public void init()
{
	try
    {

    	//System.out.println("Inside Init");

     	anal = new ArrayList();
    	root= new ArrayList();
    	rootnew = new ArrayList();
    	pos = new ArrayList();

    	rootrel = new ArrayList();

    	 hw=new ArrayList();
    	uw = new ArrayList();
    	no = new ArrayList();

       	aojindex = 0;
   		insindex = 0;
    	plfindex = 0;
		timindex = 0;
		cobindex = 0;

    	
        bolverb1=false;
    	bolverb2=false;
    	bolverb3=false;

    	
    	verb1 = "";
    	verb2 = "";
    	verb3 = "";

    	String verbno1=" ";
    	

   }
   catch(Exception e)
   {
   	//System.out.println("Exception inside Init");
   }
}



//to find universal word for root words

public void findUW()
{
//to find Universal Word if available in dictionary

	//System.out.println("Find UW");
	int count =1;
    String uwentry;
    String rword="";
    StringBuffer sb=new StringBuffer();
    StringBuffer resultuw =new StringBuffer();

    int j=0,total;

    

    try
   	{
    	total=root.size();
    	while(j<total)
{
        	
        	bn = new BSTNode();

          //  //System.out.println("Value of j is"+j);

        	rword=root.get(j).toString().trim();
        	int hc = rword.hashCode();
			
		//System.out.println("hash value of root"+hc);

        	 if((bn = dict.search(hc)) != null)
        	{
            	//System.out.println("Inside UW dict");
                hw.add(bn.headword);
            	uw.add(bn.constraint);
            	no.add(new Integer(count++));

            	rootnew.add(rword) ;
            }

	       	else
        	{

               	rootnew.add("None");
         	   	hw.add("None");
            	uw.add("None");
            	no.add("None");
        	}


    	j++;
      }// end of while


  //to append all concepts

     for(int k=0;k<hw.size();k++)
{
        if(!(rootnew.get(k).equals("None")))
        {
          	resultuw.append(rootnew.get(k).toString()+';');
           	resultuw.append(hw.get(k).toString()+';');
           	resultuw.append(uw.get(k).toString()+';');
           	resultuw.append(no.get(k).toString()+'#');
        }

     }

     result += "[w]#";
     result += resultuw;
     result += "[/w]#";


  }
  catch(Exception e)
  {
   	 //System.out.println("Exception inside find UW");
  }
     //System.out.println("Concept id" +hw);
    // //System.out.println("Concept id" +uw);
     //System.out.println("Concept id" +no);
}


public void findrelnindex() throws Exception
{
    int j=0,total;

    String analysed;
    String tempword;


    String rel_label;
    String h_word=" ";
    String u_word = " ";

    total=anal.size();

    result += "[r]#";

    while(j<total)
{
        rootrel.add("None");
        j++;
    }
    //System.out.println("Size of root reln"+rootrel.size());
    j=0;
    while(j<total)
{
        //System.out.println("Inside Find Relation Index \t"+j);
        try
        {


        analysed =anal.get(j).toString().trim();
        tempword = root.get(j).toString().trim();

        //to set index for various relations

        if( (analysed.indexOf("படு") !=-1 ) && (analysed.indexOf("அது") !=-1 ))
        {
        	//System.out.println("inside pattathu");
        	if(insindex ==1)
        	{
        		int i = rootrel.indexOf("ins");
        		u_word = uw.get(i).toString();
        		if(u_word.indexOf("person")!=-1)
        		{
        			//System.out.println("agt success");
        			rootrel.set(j,"agt");
        			rel_label = "agt";
        			findRelation_category2(i,rel_label);
        		}
        	}
        }

        else if(analysed.indexOf("ஆல்") !=-1 ) // aal
        {
            //System.out.println("Inside aal");
            if(pos.get(j).toString().equals("Verb"))
            {
            	/*//System.out.println("condn success");
            	rootrel.set(j,"cond");
            	rel_label = "cond";
            	findRelation_category1(j,rel_label);*/
            }
            else
            {

            	insindex = 1;
               	rootrel.set(j,"ins");
               	u_word = uw.get(j).toString();

             	if( (u_word.indexOf("instrument")!=-1) || (u_word.indexOf("flower")!=-1) || (u_word.indexOf("vehicle")!=-1) || (u_word.indexOf("stationary")!=-1) )
             	 {
             	   		//System.out.println("instrument success");
             	   		rel_label = "ins";
             			findRelation_category2(j,rel_label);
             	 }

             	else if (u_word.indexOf("person") ==-1)
             	{
             			//System.out.println("Method success");
             			rel_label = "met";
             		   	findRelation_category2(j,rel_label);
             	}

            }
        }

    	else if((tempword.equals("மற்றும்")) ||  (tempword.equals("மேலும்"))) //mattrum
        {
       //System.out.println("Inside mattrum- and success");
            rootrel.set(j,"and");
            rel_label = "and";
            findRelation_category1(j,rel_label);

        }

      else if(analysed.indexOf("உம்") !=-1 ) // Um
      {
      		//System.out.println("Inside Um");
      		String pos1 = pos.get(j).toString();
      		if((j+1) !=total)
      		{
      		  		String pos2 = pos.get(j + 1).toString();
      				String anal2 = anal.get(j + 1).toString().trim();
      				if( (pos1.equals(pos2)) && (anal2.indexOf("உம்") !=-1 ) )
      				{
      					//System.out.println("Inside ptn and AND success");
      					rootrel.set(j,"and"+"ptn");
      					rel_label = "ptn";
      					findRelation_category3(j+1,rel_label);

      					rel_label = "and";
      					findRelation_category5(j+1,j,rel_label);
      				}
      		}
       }

    	else if((tempword.equals("உள்ளது")) || (tempword.equals("இருக்கும்")) || (tempword.equals("உள்ளன")) || (tempword.equals("நிறைந்தது")) || (tempword.equals("ஆகும்")) || (tempword.equals("உண்டு")) || (analysed.indexOf("உள்ளது") !=-1 ) || (analysed.indexOf("இருக்கும்") !=-1) || (analysed.indexOf("உள்ளன") !=-1) || (analysed.indexOf("நிறைந்தது") !=-1)|| (analysed.indexOf("ஆகும்") !=-1)|| (analysed.indexOf("உண்டு") !=-1)) // ullathu or irukum
        {
            //System.out.println("Inside ullathu/irukum/ullana");
            aojindex = j;
            rootrel.set(j,"aoj");

        }

        else if(analysed.indexOf("உக்கு") !=-1 ) // Ukkhaha
        {
            if(analysed.indexOf("ஆக") !=-1 )
            {
              //System.out.println("Inside Ukkhaha ");
                rootrel.set(j,"ben");
             }
             else
             {
             	//System.out.println("Inside Ukku");
                rootrel.set(j,"ben");
             }

             u_word = uw.get(j).toString();

             if( (u_word.indexOf("person")!=-1) || (u_word.indexOf("place")!=-1) || (u_word.indexOf("vehicle")!=-1) || (u_word.indexOf("city")!=-1) || (u_word.indexOf("district")!=-1) || (u_word.indexOf("organization")!=-1) || (u_word.indexOf("temple")!=-1) || (u_word.indexOf("facility")!=-1) || (u_word.indexOf("country")!=-1) || (u_word.indexOf("lake")!=-1))
          	{
          		//System.out.println("ben success");
          		rel_label = "ben";
          		findRelation_category2(j, rel_label);
          	}
        }

        else if((analysed.indexOf("உடன்") !=-1 ) || (analysed.indexOf("ஓடு") !=-1 )) // utan or oodu
        {
        //System.out.println("Inside utan or oodu");
            cobindex = 1;
            rootrel.set(j,"cag" + "cao" + "cob");

            u_word = uw.get(j).toString();

            if( (u_word.indexOf("person")!=-1) || (u_word.indexOf("place")!=-1) || (u_word.indexOf("vehicle")!=-1) || (u_word.indexOf("city")!=-1) || (u_word.indexOf("district")!=-1) || (u_word.indexOf("organization")!=-1) || (u_word.indexOf("temple")!=-1) || (u_word.indexOf("facility")!=-1) || (u_word.indexOf("country")!=-1) || (u_word.indexOf("lake")!=-1))
          	{
          		//System.out.println("inside cag");
          		rel_label = "cag";
          		findRelation_category3(j, rel_label);
          	}
            else
            {
            	//System.out.println("cao success");
            	rel_label = "cao";
          		findRelation_category5(j+1, j,rel_label);
            }
        }


       else if((analysed.indexOf("இல்")!=-1))
       {
       	    if((analysed.indexOf("இருந்து") !=-1))
       	    {
       	    	//System.out.println("Inside il and lirunthu");
       	    	if((j+1) !=total)
       	    	{
       	    		String anal2 = anal.get(j + 1).toString().trim();
       	    	if(anal2.indexOf("Relative Participle") != -1)
       	    		{


       	    		}
            		else
            		{
            			plfindex = j;

            			rootrel.set(j,"plf" + "src");
            			u_word = uw.get(j).toString();

            			if( (u_word.indexOf("place")!=-1) || (u_word.indexOf("city")!=-1) || (u_word.indexOf("district")!=-1) || (u_word.indexOf("organization")!=-1) || (u_word.indexOf("temple")!=-1) || (u_word.indexOf("facility")!=-1) || (u_word.indexOf("country")!=-1) || (u_word.indexOf("lake")!=-1))
             			{
             				//System.out.println("plf success");
             				rel_label = "plf";
             			}
             			else if(u_word.indexOf("time")!=-1)
             			{
             				//System.out.println("tmf success");
             				rel_label = "tmf";
             			}
             			else if(u_word.indexOf("event")!=-1)
             			{
             				//System.out.println("scn success");
             				rel_label = "scn";
             			}
             			else
             			{
             				//System.out.println("src success");
             				rel_label = "src";
             			}


            			findRelation_category2(j,rel_label);
            		}
              	}
       	    }


       	    else
       	    {
       	    	//System.out.println("Inside il");

            	rootrel.set(j,"plc");

            	u_word = uw.get(j).toString().trim();
            	if(u_word.indexOf("pof<body")!=-1)
		{
            		//System.out.println("opl success");
            		rel_label = "opl";
            		findRelation_category2(j,rel_label);
            	}
            	else if( (u_word.indexOf("place")!=-1) || (u_word.indexOf("city")!=-1) || (u_word.indexOf("district")!=-1) || (u_word.indexOf("organization")!=-1) || (u_word.indexOf("temple")!=-1) || (u_word.indexOf("facility")!=-1) || (u_word.indexOf("country")!=-1) || (u_word.indexOf("lake")!=-1))
            	{
            		//System.out.println("plc success");
            		rel_label = "plc";
            		findRelation_category2(j,rel_label);
            	}

            	else if(u_word.indexOf("time")!=-1)
            	{
            		//System.out.println("tim success");
            		rel_label = "tim";
            		findRelation_category2(j,rel_label);
            	}

            }


      }



       else if(analysed.indexOf("க்கு") !=-1 )
       {
            if(bolverb1 == true)
            {
            	rootrel.set(j,"plt" + "gol");

            	u_word = uw.get(j).toString();

             	if( (u_word.indexOf("place")!=-1) || (u_word.indexOf("city")!=-1) || (u_word.indexOf("district")!=-1) || (u_word.indexOf("organization")!=-1) || (u_word.indexOf("temple")!=-1) || (u_word.indexOf("facility")!=-1) || (u_word.indexOf("country")!=-1) || (u_word.indexOf("lake")!=-1))
             	{
             		rel_label = "plt";
             		//System.out.println("plt success");
             	}
             	else if(u_word.indexOf("time")!=-1)
             	{
             		//System.out.println("tmt success");
             		rel_label = "tmt";
             	}
             	else
             	{
             		//System.out.println("gol success");
             		rel_label = "gol";
             	}

            	findRelation_category2(j,rel_label);
            }


        }


        else if(analysed.indexOf("ஐ") !=-1 ) // ai
        {
            //System.out.println("ai");
           
            rootrel.set(j,"ins"+"obj");

            u_word = uw.get(j).toString();

            if( (u_word.indexOf("instrument")!=-1) || (u_word.indexOf("vehicle")!=-1) || (u_word.indexOf("stationary")!=-1) )
             {
             		//System.out.println("ins success");
             		rel_label = "ins";
             }

            else
            {
            	//System.out.println("obj success");
            	rel_label = "obj";
            }

            findRelation_category2(j,rel_label);
        }

       else if(analysed.indexOf("இன்")!=-1) //in
        {
   	//System.out.println("Inside in");

            	rootrel.set(j,"mod" + "poss");

            	rel_label = "mod";
          //System.out.println("mod success");
            findRelation_category5(j+1,j,rel_label);

        }


         else if (((tempword.indexOf("இங்கு") == -1)) && (analysed.indexOf("Adverb")!=-1) ) // miha or munbu
        {
            //System.out.println("Inside adverb - man success");
            
            rootrel.set(j,"man");
            rel_label = "man";
            findRelation_category2(j,rel_label);
        }

        else if((analysed.indexOf("Adjective")!=-1)|| (analysed.indexOf("Adjectival Suffix")!=-1) || (analysed.indexOf("Adjectival Noun")!=-1))
        {
            //System.out.println("Inside Adjective - mod success");
            
            rootrel.set(j,"mod");
            rel_label = "mod";
            findRelation_category5(j+1,j,rel_label);
        }



        else if(tempword.equals("அல்லது")) //allathu
        {
            //System.out.println("Inside allathu - or success");
            
            rootrel.set(j,"or");
            rel_label = "or";
            findRelation_category1(j,rel_label);
        }


        else if((analysed.indexOf("உடைய") !=-1 ) ||  (analysed.indexOf("அது") !=-1 )) // Uddaya
        {
            //System.out.println("Inside Uddaya");
            if( (pos.get(j).toString().equals("Noun")) ||  (pos.get(j).toString().equals("Entity")))
            {
            	//System.out.println("poss success");
            	
            	rootrel.set(j,"poss");

            	rel_label = "pos";
            	findRelation_category5(j+1,j,rel_label);
            }
            else
            {
            	aojindex = j;
            	rel_label = "aoj";
            }

        }


       else if ( (analysed.indexOf("ஆக") !=-1 ) || (analysed.indexOf("க்க") !=-1 ) ) // aaha
        {
            //System.out.println("Inside aaha");
            if(pos.get(j).toString().equals("Verb"))
            {
            	//System.out.println("pur success");
            	
            	rootrel.set(j,"pur");

            	rel_label = "pur";
            	findRelation_category2(j,rel_label);

            }
       }

        else
        {
        	//System.out.println("inside else");
        }

        //System.out.println("TW"+tempword);
        if((tempword.equals("நகரம்"))  ||  (tempword.equals("மாவட்டம்")) ||  (tempword.equals("நாடு")) || (tempword.equals("மாநிலம்")) || (tempword.equals("நதி")) || (tempword.equals("ஏரி")) || (tempword.equals("பாலம்")) )//naharam
        {
            //System.out.println("Inside Naharam");
            
            rootrel.set(j,"iof");

            h_word = hw.get(j).toString();

            for(int i= 0 ; i < uw.size() ;i++)
	{
                u_word = uw.get(i).toString();

            	if(u_word.indexOf(h_word)!=-1)
            	{
            		//System.out.println("iof success");
        			rel_label = "iof";
         			findRelation_category5(j,i,rel_label);
            	}
            }
        }

        else if(tempword.equals("உடல்") || tempword.equals("மரம்஢") ) //uttal maram
        {
            //System.out.println("Inside vuudal - pof");
            
            rootrel.set(j,"pof");

            h_word = hw.get(j).toString();

            for(int i= 0 ; i < uw.size() ;i++)
   {
                u_word = uw.get(i).toString();

            	if(u_word.indexOf(h_word)!=-1)
            	{
            		//System.out.println("pof success");
        			rel_label = "pof";
         			findRelation_category5(j,i,rel_label);
            	}
            }
        }



         h_word = hw.get(j).toString();
         u_word = uw.get(j).toString();

         if(u_word.indexOf("aoj>thing")!=-1)
         {
           	//System.out.println("aoj success");
           	rel_label = "aoj";
           	findRelation_category5(j,j+1,rel_label);
          }
         if(u_word.indexOf("icl>how")!=-1)
         {
           	//System.out.println("man success");
           	rel_label = "man";
           	findRelation_category5(j+1,j,rel_label);
         }
         else if(u_word.indexOf("obj>thing")!=-1)
         {
           	//System.out.println("obj success");
           	rel_label = "obj";
           	findRelation_category5(j,j-1,rel_label);
         }
         else if(u_word.indexOf("date")!=-1)
         {
           	//System.out.println("tim success");
           	rel_label = "tim";
           	findRelation_category2(j,rel_label);
         }

        }
         catch(Exception e)
         {
         	//System.out.println("Exception inside findreln"+e);
         }

        j++;

    }          //while


     //to find aoj,agt
     //lexical end none + aojindex - aoj
     //lexical end none + not aoj index - agt

     for(int i = 0 ; i<uw.size() ; i++)	{
          	if((rootrel.get(i).toString()).equals("None"))
            {

     		     u_word = uw.get(i).toString();

            	if( (u_word.indexOf("person")!=-1) || (u_word.indexOf("food")!=-1) || (u_word.indexOf("vehicle")!=-1) || (u_word.indexOf("machine")!=-1) || (u_word.indexOf("body")!=-1) || (u_word.indexOf("place")!=-1))
            	{
             		if(aojindex > 0)
             		{
             				//System.out.println("aoj success");
             				rel_label = "aoj";
             				findRelation_category5(aojindex,i,rel_label);
             				break;
             		}

             		else
             		{
             				//System.out.println("inside agt ");
             				rel_label = "agt";
             				findRelation_category3(i,rel_label);
             				break;
             		}
            	}
            }
        }

//to write reln index value in a file

                //System.out.println("Possibility of relation"+rootrel);

                result  += "[/r]#";
                StringTokenizer strToken_res = new StringTokenizer(result.toString(),"#");

        		while (strToken_res.hasMoreTokens())
        		{
            		String word = strToken_res.nextToken();
            		//System.out.println(word);
            	}
                //System.out.println("End of findreln");
}


public void findRelation_category1(int j,String rl)
{
	try
	{
		String concept1=" ";
    	String concept2=" ";
    	int tot = hw.size();

		//System.out.println("Inside Category1"+j);
		if( (rl.equals("cond")) || (rl.equals("rsn")))
		{

		}
		else
		{
			concept1 = no.get(j+1).toString();
        	concept2 = no.get(j -1).toString();
        	result += concept1 + '\t'+ rl + '\t' + concept2 + '#';
		}

	}
	catch(Exception e)
	{
		//System.out.println("Exception inside category1" +e);

	}

}

public void findRelation_category2(int j,String rl)
{
	try
	{
		String concept1=" ";
    	String concept2=" ";

    	//System.out.println("Inside Category2"+j);
		concept1 = verbno1;
        concept2 = no.get(j ).toString();
        result += concept1 + '\t'+ rl + '\t' + concept2+ '#';
    }
    catch(Exception e)
	{
		//System.out.println("Exception inside category2" +e);

	}

}


public void findRelation_category3(int j,String rl)
{
	try
	{
		String concept1=" ";
    	String concept2=" ";

    	//System.out.println("Inside Category3"+j);
    	//System.out.println("Verb number"+verbno1);
    	int temp_i = root.indexOf(verb1);
    	//System.out.println("Temp i value is"+temp_i);
    	//String uword = uw.get(no.indexOf(verbno1)).toString();

    	String uword = uw.get(temp_i).toString();

    	if((uword.indexOf("icl>do") != -1) || (uword.indexOf("agt<thing") != -1))
    	{
    			//System.out.println("agt /cag success");
    			concept1 = verbno1;
            	concept2 = no.get(j ).toString();
            	result += concept1 + '\t'+ rl + '\t' + concept2+ '#';
    	}
    	else if(cobindex == 1)
    	{
    			//System.out.println("cob success");
    			concept1 = verbno1;
            	concept2 = no.get(j ).toString();
            	result += concept1 + '\t'+ "cob" + '\t' + concept2+ '#';
    	}
 	}

 	catch(Exception e)
	{
		//System.out.println("Exception inside category3" +e);

	}

}


public void findRelation_category4(int j,String rl)
{
	try
	{
		String concept1=" ";
    	String concept2=" ";

    	//System.out.println("Inside Category4"+j);
		concept1 = verbno1;
        concept2 = no.get(j-1 ).toString();
        result += concept1 + '\t'+ rl + '\t' + concept2+ '#';
     }

    catch(Exception e)
	{
		//System.out.println("Exception inside category4" +e);
	}
}


public void findRelation_category5(int i,int j,String rl)
{
	try
	{
		String concept1=" ";
    	String concept2=" ";

  	//System.out.println("Inside Category5"+j);
		concept1 = no.get(i ).toString();
        concept2 = no.get(j ).toString();
        result += concept1 + '\t'+ rl + '\t' + concept2+ '#';
     }
     catch(Exception e)
	{
		//System.out.println("Exception inside category5" +e);
	}
}

public String findverbnumber(String v)
{
    int tempindex;
    int totalrw;

    int totrw=root.size();


	try
	{
	    //System.out.println("\n inside Find verb Number");


        tempindex=root.indexOf(v.trim());
       // //System.out.println("-1 not  exist"+tempindex);
        if(tempindex != -1)
            return(no.get(tempindex).toString().trim());

    }

     catch(Exception e)
    {
    	//System.out.println("Exception inside find verb number");
    }
    return("0");
}
}
