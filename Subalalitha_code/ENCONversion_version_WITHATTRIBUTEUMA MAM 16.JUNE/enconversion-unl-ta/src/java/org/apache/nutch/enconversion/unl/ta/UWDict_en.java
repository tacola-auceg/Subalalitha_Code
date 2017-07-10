package org.apache.nutch.enconversion.unl.ta;

import org.apache.nutch.unl.UNL;
import org.apache.nutch.util.NutchConfiguration;
import org.apache.hadoop.conf.Configuration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class UWDict_en {
	
	 public static Configuration conf = NutchConfiguration.create();
	 public static String eng_path = conf.get("eng_unl_resource");

    public BST_en g_bst;
    public BST_en Noun_bst;
    public BST_en Adj_bst;
    public BST_en Adv_bst;
    public BST_en be_bst;
    public BST_en do_bst;
    public BST_en occur_bst;  
    public BSTNode_en bstnode;
    int cnt = 0;
    //  Dictionary dict;
    int hc = 0;
    String lex = "";
    String cl = "";

    public UWDict_en() {
        //  dict  = new Dictionary();
        g_bst = new BST_en();
        Noun_bst = new BST_en();
        Adj_bst = new BST_en();
        Adv_bst = new BST_en();
        be_bst = new BST_en();
        do_bst = new BST_en();
        occur_bst = new BST_en();

        bstnode = new BSTNode_en();
        loadDic();
    }

 /**  	public void loadDic() {
    String conentry;
    try {
    int cnt = 0;
    BufferedReader in = new BufferedReader(
    new InputStreamReader(
    new FileInputStream(
    "/home/English/resource/unl/en/Dictionary/uwdict.txt"),
    "UTF8"));

    while ((conentry = in.readLine()) != null) {
    cnt++;
    String[] tok = conentry.split("/");
    if (tok.length >= 2) {
    String lex = tok[0].toString();
    int hc = lex.hashCode();
    String cl = tok[1].toString();
    bst.insert(hc, lex, cl);
    } else {

    }
    }
    in.close();
    // System.out.println("Total Dict Size:" + cnt);
    } catch (Exception e) {
    e.printStackTrace();
    }
    }   */
   public void loadDic() {

    
        
        noun();
        adjective();
        adverb();
        be_Verb();
        do_Verb();
        occur_Verb();
        load();
    }

    public void load_Dict(String get_Inp) {

        String[] tok = get_Inp.split("/");
        lex = tok[0].toString();
        //   System.out.println("lex:"+lex);
        hc = lex.hashCode();
        cl = tok[1].toString();
        //   System.out.println("cl:"+cl);
    }
    //   class Dictionary {

    public void noun() {
    //    System.out.println("Noun:");
        String get_Inp = "";
        try {
            BufferedReader n_buffer = IOHelper.getBufferedReader(eng_path + "Dictionary/" + "Noun.txt");
            while ((get_Inp = n_buffer.readLine()) != null) {
            //    System.out.println("get_Inp:" + get_Inp);
                load_Dict(get_Inp);
            //    cnt++;
                Noun_bst.insert(hc, lex, cl);

            }
         //  System.out.println("Total Dict Size:" + cnt);
        } catch (Exception e) {
        }
    }

    public void adjective() {
        //    System.out.println("Adjective:");
        String get_Inp = "";
        try {
            BufferedReader adj_buffer = IOHelper.getBufferedReader(eng_path + "Dictionary/" + "Adjective.txt");
            while ((get_Inp = adj_buffer.readLine()) != null) {
             //          System.out.println("get_Inp:" + get_Inp);
                load_Dict(get_Inp);
           //      cnt++;
                Adj_bst.insert(hc, lex, cl);
            }
        //     System.out.println("Total Dict Size:" + cnt);
        } catch (Exception e) {
        }
    }

    public void adverb() {
       //     System.out.println("Adverb:");
        String get_Inp = "";
        try {
            BufferedReader adv_buffer = IOHelper.getBufferedReader(eng_path + "Dictionary/" + "Adverb.txt");
            while ((get_Inp = adv_buffer.readLine()) != null) {
             //        System.out.println("get_Inp:" + get_Inp);
                load_Dict(get_Inp);               
                Adv_bst.insert(hc, lex, cl);
            //     cnt++;
            }
        //    System.out.println("Total Dict Size:" + cnt);
        } catch (Exception e) {
        }
    }

    public void be_Verb() {
    //    System.out.println("be_Verb:");
        String get_Inp = "";
        try {
            BufferedReader be_buffer = IOHelper.getBufferedReader(eng_path + "Dictionary/" + "be_Verb.txt");
            while ((get_Inp = be_buffer.readLine()) != null) {
          //      System.out.println("get_Inp:" + get_Inp);
                load_Dict(get_Inp);
                be_bst.insert(hc, lex, cl);
            //    cnt++;
            }
        //    System.out.println("Total Dict Size:" + cnt);
        } catch (Exception e) {
        }
    }

    public void do_Verb() {
    //    System.out.println("do_Verb:");
        String get_Inp = "";
        try {
            BufferedReader do_buffer = IOHelper.getBufferedReader(eng_path + "Dictionary/" + "do_Verb.txt");
            while ((get_Inp = do_buffer.readLine()) != null) {
           //     System.out.println("get_Inp:" + get_Inp);
                load_Dict(get_Inp);
                do_bst.insert(hc, lex, cl);
             //   cnt++;
            }
       //     System.out.println("Total Dict Size:" + cnt);
        } catch (Exception e) {
        }
    }

    public void occur_Verb() {
    //    System.out.println("occur_Verb:");
        String get_Inp = "";
        try {
            BufferedReader occ_buffer = IOHelper.getBufferedReader(eng_path + "Dictionary/" + "occur_Verb.txt");
            while ((get_Inp = occ_buffer.readLine()) != null) {
            //    System.out.println("get_Inp:" + get_Inp);
                load_Dict(get_Inp);
                occur_bst.insert(hc, lex, cl);
             //   cnt++;
            }
        //    System.out.println("Total Dict Size:" + cnt);
        } catch (Exception e) {
        }
    }

    public void load() {
     //   System.out.println("Noun:");
        String get_Inp = "";
        try {
            BufferedReader buffer = IOHelper.getBufferedReader(eng_path + "Dictionary/" + "uwdict.txt");
            while ((get_Inp = buffer.readLine()) != null) {
            //    System.out.println("get_Inp:" + get_Inp);
                load_Dict(get_Inp);
                g_bst.insert(hc, lex, cl);
            //    cnt++;
            }
        //    System.out.println("Total Dict Size:" + cnt);
        } catch (Exception e) {
        }
    }
//    }*/   

   public BST_en get_bst1() {
        return Noun_bst;
    }

    public BST_en get_bst2() {
        return Adj_bst;
    }

    public BST_en get_bst3() {
        return Adv_bst;
    }

    public BST_en get_bst4() {
        return be_bst;
    }

    public BST_en get_bst5() {
        return do_bst;
    }

    public BST_en get_bst6() {
        return occur_bst;
    }
    public BST_en get_bst7() {
        return g_bst;
    }

    public static void main(String args[]) {
        UWDict_en b = new UWDict_en();
        b.loadDic();
    }
}
