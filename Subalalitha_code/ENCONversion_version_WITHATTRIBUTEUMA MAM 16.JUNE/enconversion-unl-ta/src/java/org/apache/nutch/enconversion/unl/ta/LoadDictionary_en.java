package org.apache.nutch.enconversion.unl.ta;

import org.apache.nutch.unl.UNL;
import org.apache.nutch.util.NutchConfiguration;
import org.apache.hadoop.conf.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * 
 * @author root
 */
public class LoadDictionary_en {
	String entry = "";
	String getentry = "";
	String nextentry = "";
	String infword = "";
	String rootword = "";
	String postag = "";

	Hashtable<String, String> basetable = new Hashtable<String, String>();
	Hashtable<String, String> multitable = new Hashtable<String, String>();
	
	 public static Configuration conf = NutchConfiguration.create();
	 public static String eng_path = conf.get("eng_unl_resource");

	public void loadDict() {
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(eng_path + "Analyser/" +	"dict.txt"));
			while ((entry = buffer.readLine()) != null) {
				if (entry.contains("#")) {

					StringTokenizer strToken1 = new StringTokenizer(entry, "#");
					getentry = strToken1.nextToken().trim();

					StringTokenizer strToken2 = new StringTokenizer(getentry,
							"\t");
					infword = strToken2.nextToken().trim();
					rootword = strToken2.nextToken().trim();
					postag = strToken2.nextToken().trim();

					basetable.put(infword, rootword + "#" + postag);

					while (strToken1.hasMoreTokens()) {
						nextentry = strToken1.nextToken().trim();
						// System.out.println("nextentry:"+nextentry);
						StringTokenizer strToken3 = new StringTokenizer(
								nextentry, "\t");
						String nextentry = strToken3.nextToken().trim();
						String nextpos = strToken3.nextToken().trim();
						multitable.put(nextentry + "#" + nextpos, infword);
						// System.out.println(getentry);
					}
				} else {
					StringTokenizer strToken4 = new StringTokenizer(entry, "\t");
					infword = strToken4.nextToken().trim();
					rootword = strToken4.nextToken().trim();
					postag = strToken4.nextToken().trim();
					basetable.put(infword, rootword + "#" + postag);
				}

			}
			String out = multitable.toString();
			String out1 = out.replaceAll(",", "\n");
			// System.out.println(out1);
		} catch (Exception e) {

		}
	}

	public static void main(String args[]) {
		LoadDictionary_en ld = new LoadDictionary_en();
		ld.loadDict();
	}
}
