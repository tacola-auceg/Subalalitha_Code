package org.apache.nutch.enconversion.unl.ta;

import org.apache.nutch.unl.UNL;

//import org.apache.nutch.util.NutchConfiguration;
//import org.apache.hadoop.conf.Configuration;

import java.util.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class readobject implements Serializable {
	// public static Configuration conf = NutchConfiguration.create();
	// public static String path=conf.get("UNL resource");
	public static FileInputStream fis1;
	public static ObjectInputStream ois1;
	public static MultiList mlist=new MultiList();

	public static void main(String args[]) {
		
		int n = 0;
		File f = new File("./resource/unl/Enconversion/unl-graph");
		// File f=new File("./crawl-unl");
		File filename[] = f.listFiles();
		FinalLLImpl[] ll_new = new FinalLLImpl[filename.length];
		try {

			//System.out.println("length"+filename.length);
			for (int k = 0; k < filename.length; k++) 
			{
				
				
				fis1 = new FileInputStream(filename[k].toString());
				//System.out.println("hello2"+filename[k].toString());
				ois1 = new ObjectInputStream(fis1);
				
				ll_new = mlist.list_readfrmfile(ll_new, ois1, k);

			}
			ois1.close();
			fis1.close();

		} catch (Exception ex) {}

		
	}//main
}
