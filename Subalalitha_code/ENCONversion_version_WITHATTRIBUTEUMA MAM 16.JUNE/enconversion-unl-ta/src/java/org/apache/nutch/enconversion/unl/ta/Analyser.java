package org.apache.nutch.enconversion.unl.ta;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package morphanalyzer;

import java.util.Enumeration;

/**
 * 
 * @author root
 */
public class Analyser {

	DictionaryCheck_en dict_check = new DictionaryCheck_en();
	LoadDictionary_en ldict = new LoadDictionary_en();

	String multiout = "";
	String multiEntry = "";

	public String analyseWord(String input) {
		String output = "";
		// dict_check.dict_Tree();
		ldict.loadDict();
		String key2 = "";
		// System.out.println("input:"+input);
		String checkInp = ldict.basetable.get(input);
		if (ldict.multitable.containsValue(input)) {
			Enumeration e2 = ldict.multitable.keys();
			while (e2.hasMoreElements()) {
				key2 = (String) e2.nextElement();
				if (ldict.multitable.get(key2).equals(input)) {
					// System.out.println("Inside IF");
					multiEntry = key2;
				}
			}
			output = checkInp;
			output += "&" + multiEntry;
			return output;
		} else if (checkInp != null) {
			output = checkInp;
			return output;
		} else {
			output = input + "#Unknown";
			return output;
		}
		// output = checkInp;
		// output += "&" + multiEntry;
		// return output;
	}
}
