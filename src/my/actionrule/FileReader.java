/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.actionrule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author rojatkaraditi
 */
public class FileReader {
    
    String dataFile="";
    String attributeFile = "";
    String delimeter="";
    Integer minSupport=0;
    Double minConfidence=0.0;
    List<String> stableAttributes = new ArrayList<>();
    String decisionAttribute="";
    String toDecisionAttribute="";
    String fromDecisionAttribute="";
    ArrayList<ArrayList<String>> data = new ArrayList<>();
    ArrayList<String> attributesList = new ArrayList<>();

    public ArrayList<String> getAttributesList() {
        return attributesList;
    }

    public void setAttributesList(ArrayList<String> attributesList) {
        this.attributesList = attributesList;
    }
        
	
    public void setAttributeFile(String attributeFile) {
            this.attributeFile= attributeFile;
    }

    public void setDataFile(String dataFile) {
            this.dataFile=dataFile;
    }

    public void setDelimeter(String delimeter) {
            if(delimeter!=null && delimeter.equals("tab")){
                delimeter = "\t";
            }
            this.delimeter=delimeter;
    }
    
    public void setMinSupport(Integer minSupport){
        this.minSupport = minSupport;
    }
    
    public void setMinConfidence(Double minConfidence){
        this.minConfidence = minConfidence;
    }
    
    public void setStableAttributes(List<String> stableAttributes){
        this.stableAttributes = stableAttributes;
    }

    public void setDecisionAttribute(String decisionAttribute) {
        this.decisionAttribute = decisionAttribute;
    }

    public void setToDecisionAttribute(String toDecisionAttribute) {
        this.toDecisionAttribute = toDecisionAttribute;
    }

    public void setFromDecisionAttribute(String fromDecisionAttribute) {
        this.fromDecisionAttribute = fromDecisionAttribute;
    }

    public String getDataFile() {
        return dataFile;
    }

    public String getAttributeFile() {
        return attributeFile;
    }

    public String getDelimeter() {
        return delimeter;
    }

    public Integer getMinSupport() {
        return minSupport;
    }

    public Double getMinConfidence() {
        return minConfidence;
    }

    public List<String> getStableAttributes() {
        return stableAttributes;
    }

    public String getDecisionAttribute() {
        return decisionAttribute;
    }

    public String getToDecisionAttribute() {
        return toDecisionAttribute;
    }

    public String getFromDecisionAttribute() {
        return fromDecisionAttribute;
    }

    public ArrayList<ArrayList<String>> getData() {
        return data;
    }
    
    
    
            
    public HashMap<String,Set<String>> getAttributes() throws Exception{
        
        //send files 
        //read files - filling missing values, disc.
        //getContinuousAttributes
        //load attributes from LERS
        DataProcess dp = new DataProcess();
        dp.processFiles(attributeFile, dataFile, delimeter);
        attributesList = (ArrayList<String>) dp.getAttributes();
        HashMap<String,Set<String>> attributeValues = new HashMap<>();
        data =	dp.getData();
        
        if(attributesList!=null && !attributesList.isEmpty()){
            for(String attribute:attributesList){
                attributeValues.put(attribute, dp.getDistinctAttributeValues(attribute));
            }
        }
        
        return attributeValues;
        

//            Set<String> aSet = new HashSet<>();
//            aSet.add("a1");
//            aSet.add("a2");
//            aSet.add("a3");
//            aSet.add("a4");
//
//            Set<String> bSet = new HashSet<>();
//            bSet.add("b1");
//            bSet.add("b2");
//            bSet.add("b3");
//
//            Set<String> cSet = new HashSet<>();
//            cSet.add("c1");
//            cSet.add("c2");
//            cSet.add("c3");
//            cSet.add("c4");
//
//            Set<String> dSet = new HashSet<>();
//            dSet.add("d1");
//            dSet.add("d2");
//
//            HashMap<String,Set<String>> attributes = new HashMap<>();
//            attributes.put("A", aSet);
//            attributes.put("B", bSet);
//            attributes.put("C", cSet);
//            attributes.put("D", dSet);
//            return attributes;

    }

    @Override
    public String toString() {
        return "dataFile=" + dataFile + ",\n attributeFile=" + attributeFile + ",\n delimeter=" + delimeter + ",\n minSupport=" + minSupport + ",\n minConfidence=" + minConfidence + ",\n stableAttributes=" + stableAttributes + ",\n decisionAttribute=" + decisionAttribute + ",\n toDecisionAttribute=" + toDecisionAttribute + ",\n fromDecisionAttribute=" + fromDecisionAttribute;
    }
    
    
    
}
