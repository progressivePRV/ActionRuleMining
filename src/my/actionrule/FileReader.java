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
    
    String dataFilePath="";
    String attributeFilePath = "";
    String delimeter="";
    Integer minSupport=0;
    Double minConfidence=0.0;
    List<String> stableAttributes = new ArrayList<>();
        
	
    public void setAttributeFile(String path) {
            attributeFilePath= path;
    }

    public void setDataFile(String path) {
            dataFilePath=path;
    }

    public void setDelimeter(String delimeter) {
            this.delimeter=delimeter;
    }

    @Override
    public String toString() {
        return "dataFilePath=" + dataFilePath + ",\n attributeFilePath=" + attributeFilePath + ",\n delimeter=" + delimeter + ",\n minSupport=" + minSupport + ",\n minConfidence=" + minConfidence + ",\n stableAttributes=" + stableAttributes;
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
            
    public HashMap<String,Set<String>> getAttributes(){
        
        //send files 
        //read files - filling missing values, disc.
        //getContinuousAttributes
        //load attributes from LERS

            Set<String> aSet = new HashSet<>();
            aSet.add("a1");
            aSet.add("a2");
            aSet.add("a3");
            aSet.add("a4");

            Set<String> bSet = new HashSet<>();
            bSet.add("b1");
            bSet.add("b2");
            bSet.add("b3");

            Set<String> cSet = new HashSet<>();
            cSet.add("c1");
            cSet.add("c2");
            cSet.add("c3");
            cSet.add("c4");

            Set<String> dSet = new HashSet<>();
            dSet.add("d1");
            dSet.add("d2");

            HashMap<String,Set<String>> attributes = new HashMap<>();
            attributes.put("A", aSet);
            attributes.put("B", bSet);
            attributes.put("C", cSet);
            attributes.put("D", dSet);
            return attributes;

    }
    
}
