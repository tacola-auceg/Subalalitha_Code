package org.apache.nutch.enconversion.unl.ta;

import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author root
 */
public class WordSenseDisambiguate {

    Rules r;
    NounRules wsdNoun;
    VerbRules wsdVerb;

    ArrayList<String> entryList;
    ArrayList<String> uwList;
    ArrayList<String> hwList;
    ArrayList<String> rwList;
    ArrayList<String> aList;

    String ambiguous;
    String analValue;
    String result;
    String curranal;
    String prevanal;
    String nextanal;
    String nextuw;
    String prevuw;
    String prevhw;
    String nexthw;


    int index;

    boolean verbFlag;
    boolean nounFlag;
    boolean prevFlag;
    boolean nextFlag;

    public WordSenseDisambiguate(){
        r = new Rules();
        wsdNoun = new NounRules();
        wsdVerb = new VerbRules();
    }    
    public void initialize(){
        entryList = new ArrayList<String>();
        uwList = new ArrayList<String>();
        hwList = new ArrayList<String>();
        rwList = new ArrayList<String>();
        aList = new ArrayList<String>();

        ambiguous = "";
        analValue = "";
        result = "";
        curranal = "";
        prevanal = "";
        nextanal = "";
        nextuw = "";
        prevuw = "";
        prevhw = "";
        nexthw = "";

        index = 0;

        verbFlag = false;
        nounFlag = false;
        prevFlag = false;
        nextFlag = false;
    }
    public String processWSD(ArrayList<String> sList,ArrayList<String> analList,ArrayList<String> posList,String aword, int pos){
        entryList.addAll(sList);
        aList.addAll(analList);
        ambiguous = aword;
        index = pos;
        getSenses();
        disambiguatePOS(posList,pos);
        //analValue = analList.get(pos).toString();
        getMorph();
        return result;
    }
    public void getMorph(){
     //   NounRules wsdNoun = new NounRules();
     //   VerbRules wsdVerb = new VerbRules();
        String ps = "Plural Suffix";
        curranal = aList.get(index).toString();
        prevanal = aList.get(index-1).toString();
        nextanal = aList.get(index+1).toString();
        if(curranal.contains(ps)){
            wsdNoun.river_army_Sense();
        }else if( (nextanal.contains(ps)) || (aList.get(index+2).toString().contains(ps)) ){
            wsdNoun.number_Sense();
        }else if( (prevanal.contains("Dative Case")) || (prevanal.contains("Locative Case")) || (prevanal.contains("Instrumental Case")) || (prevanal.contains("Genitive Case")) || (prevanal.contains("Accusative Case")) ){
            if(verbFlag == true){
                verbDisambiguation();
                verbFlag = false;
            }else if(nounFlag == true){
                wsdNoun.river_army_Sense();
                nounFlag = false;
            }
        }else  if (prevanal.indexOf("ஆவது") != -1) {
            wsdNoun.position_Sense();
	}else if( (prevanal.contains("Numbers")) || (prevanal.contains("charNumbers")) ){
            wsdNoun.river_army_Sense();
        }
    }
    public void getSenses(){
        try{
            for(int i = 0;i<entryList.size();i++){
                String getEntry = entryList.get(i).toString();
                String[] splitEntry = getEntry.split("/");
                rwList.add(splitEntry[0]);
                hwList.add(splitEntry[1]);
                uwList.add(splitEntry[2]);
            }
        }catch(Exception e){

        }
    }
    public void disambiguatePOS(ArrayList<String> pList,int j){
        String getPos = pList.get(j).toString();
        if(getPos.contains("Verb")){
            verbFlag = true;
        }else if(getPos.contains("Noun")){
            nounFlag = true;
        }
    }
    public void verbDisambiguation(){
    //    VerbRules wsdVerb = new VerbRules();
        for (int i = index-1;i>0;i--){
            prevanal = aList.get(i).toString();
            if (prevanal.contains("Dative Case")){
                wsdVerb.offer_Sense();
            //    break;
            }else if(prevanal.contains("Accusative Case")){
                wsdVerb.create_Sense();
           //     break;
            }
        }
    }

    public void nounDisambiguation(){
       

    }
    public void semantics_Senses(){
        int i=0, total;
        total = uwList.size();
        String u_word;
        String h_word;
        String r_word;
        while(i<total){
            r_word = rwList.get(i).toString();
            h_word = hwList.get(i).toString();
            u_word = uwList.get(i).toString();
            if( (h_word.contains("river")) || (h_word.contains("army")) ){

            }else if(h_word.contains("number")){

            }else if(u_word.contains("position")){

            }else if( (u_word.contains("money")) || (u_word.contains("river")) || (u_word.contains("person")) || (u_word.contains("weapon")) || (h_word.contains("place"))) {

            }else if(u_word.contains("date")) {

            }else if(u_word.contains("natural world")){

            }else if ((u_word.contains("unit")) || (u_word.contains("number"))
					|| (u_word.contains("measurement"))
					|| (u_word.contains("time"))
					|| (u_word.contains("mountain"))
					|| (h_word.contains("first"))) {

            }else if( (u_word.contains("style")) || (u_word.contains("number"))
					|| (u_word.contains("book"))
					|| (u_word.contains("document")) ) {

            }else if( (u_word.contains("god")) || (u_word.contains("goddess")) ) {

            }else if(u_word.contains("person")) {

            }else if (u_word.contains("color")) {

            }else if (u_word.contains("metal")) {

            }

            i++;
        }
    }
    class NounRules{
        int getIn = rwList.indexOf(ambiguous);
        public void river_army_Sense(){
           
        }
        public void number_Sense(){
            if(hwList.contains("number")){
                int ind = hwList.indexOf("number");
                result = rwList.get(ind).toString()+"/"+hwList.get(ind).toString()+"/"+uwList.get(ind).toString();
            }
        }
        public void position_Sense(){
            if(uwList.contains("position")){
                int ind = uwList.indexOf("position");
                result = rwList.get(ind).toString()+"/"+hwList.get(ind).toString()+"/"+uwList.get(ind).toString();
            }
        }
        public void color_Sense(){

        }
        public void person_Sense(){

        }
        public void god_Sense(){

        }
        public void style_Sense(){

        }
        public void food_Sense(){

        }
        public void metal_Sense(){

        }
    }
    class VerbRules{
        public void offer_Sense(){
            
        }
        public void create_Sense(){

        }
    }
    public void getPrev(){
        if(prevuw.contains("number")){
            prevFlag = true;
          //  wsdNoun.river_army_Sense();
        }else if(prevuw.contains("person")){
            prevFlag = true;
         //   wsdNoun.person_Sense();
        }else if( (prevuw.contains("god")) || (prevuw.contains("goddess")) ){
            prevFlag = true;
        }else if( (prevuw.contains("document")) || (prevuw.contains("book")) || (prevuw.contains("language")) ){
            prevFlag = true;
        }else if ((prevuw.indexOf("food") != -1) || (prevuw.indexOf("fruit") != -1)|| (prevhw.indexOf("fruit") != -1) || (prevhw.indexOf("food") != -1) ){
            prevFlag = true;
        }else if ((prevuw.indexOf("metal") != -1) || (prevhw.indexOf("metal") != -1)) {
            prevFlag = true;
        }else if (prevuw.indexOf("date") != -1) {
            prevFlag = true;
        }else if ((prevuw.contains("color"))) {
            prevFlag = true;
        }
    }
    public void getNext(){
        r.getNextUW();
        if(nextuw.contains("person")){
            nextFlag = true;
        }else if( (nextuw.contains("god")) || (nextuw.contains("goddess")) ){
            nextFlag = true;
        }else if( (nextuw.contains("document")) || (nextuw.contains("book")) || (nextuw.contains("language")) ){
            nextFlag = true;
        }else if ((nextuw.indexOf("food") != -1) || (nextuw.indexOf("fruit") != -1)|| (nexthw.indexOf("fruit") != -1) || (nexthw.indexOf("food") != -1) ){
            nextFlag = true;
        }else if ((nextuw.indexOf("metal") != -1) || (nexthw.indexOf("metal") != -1)) {
            nextFlag = true;
        }else if (nextuw.indexOf("date") != -1) {
            nextFlag = true;
        }else if ((nextuw.contains("color"))) {
            nextFlag = true;
        }
    }
    public void wsd_Category1(String morph, int j){

    }
    public void wsd_Category2(int j){
        String prevconcept="";
        String nextconcept = "";
        if(prevconcept!=null)
            prevconcept = uwList.get(j-1).toString();
        if(nextconcept!=null)
            nextconcept = "";
        if(prevconcept.equals(nextconcept)){

        }
    }

}
