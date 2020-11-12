
package my.actionrule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Lers_Helper {
    //  Attribute ===>  F
    //  Value  ======>  F1 , F2 , F3
    // Object  ======>  1  , 2  ,  3 (row no.) 
    
    ArrayList<ValueSet> valueSets = new ArrayList<>(); // possible value set
    ArrayList<ValueSet> decisionValueSet = new ArrayList<>();
    ArrayList<LersRule> certainRules;
    ArrayList<LersRule> possibleRules;
    
    ArrayList<LersRule> GetCertainRules(){
        return certainRules;
    }
    
    
    void GenerateLERSRules(ArrayList<ArrayList<String>> table,int decisionAttribueIndex, ArrayList<String> attrinutesName ,int  minSupport, double minConfidence){
        
        // step 1 preprocess the tables
        // fill valueSets and decisionValueSet
        ProcessTable(table,decisionAttribueIndex);
        System.out.println("after processing the table decision attributes are==>");
        for(ValueSet v : decisionValueSet){
            System.out.println(v.toString());
        }
        
        System.out.println("valueSets size =>"+valueSets.size());
        System.out.println("decisionValueSet size==>"+decisionValueSet.size());
        
        //step 2 start iterations
        int size_of_sets = 2;
        certainRules = new ArrayList<>();
        possibleRules = new ArrayList<>();
        boolean is_there_left_unmarked_sets = true;
        
        while (is_there_left_unmarked_sets) {
            //################################################# processing Rules
            System.out.println("Iteration "+(size_of_sets-1));
            
            ArrayList<LersRule> newCertainRules = new ArrayList<>();
            ArrayList<LersRule> newPossibleRules = new ArrayList<>();

            ArrayList<ValueSet> newUnmarked = new ArrayList<>();
            
            
            for (ValueSet val_set : valueSets) {

                // First, check to see if this ValueSet is a subset of alraedy generated certain rules.
                // If so, skip this.
                boolean isSubsetOfMarked = false;
                for (LersRule c : certainRules) {
                    if (c.leftSet.isSubsetOf(val_set)) {
                        isSubsetOfMarked = true;
                        break;
                    }
                }

                if (isSubsetOfMarked)
                    continue;

                for (ValueSet dec_val_set : decisionValueSet) {

                    // Find intersection of val_set and dec_val_set.

                    //val_set.marked = null;
                    // This is a possible rule.
                    int numOverLap = 0;
                    for (Integer e : val_set.objects) {
                        if (dec_val_set.objects.contains(e)) {
                            numOverLap++;
                        }
                    }
                    
                    // necessary for possible rules
                    // Only create rules for fully or partially overlapping groups.
                    // in other words it is calculating ==> Support
                    if (numOverLap == 0)
                        continue;

                    double newConf = (double) numOverLap / val_set.objects.size();
                    //System.out.println("support=>"+numOverLap);
                    //System.out.println("confidence=>"+newConf);
                    // Must have min support and min confidence 
                    if (numOverLap < minSupport || newConf < minConfidence)
                        continue;

                    LersRule newRule = new LersRule(
                            val_set,
                            dec_val_set,
                            newConf,
                            numOverLap,
                            attrinutesName);


                    if (numOverLap == val_set.objects.size()) {
                        newCertainRules.add(newRule);
                    } else {
                        newPossibleRules.add(newRule);
                        newUnmarked.add(val_set);
                    }

                }
            }
            //System.out.println("rules generated after first loop");
            System.out.println("newCertainRules size=>"+newCertainRules.size());
            System.out.println("newPossibleRules size==>"+newPossibleRules.size());

            
            //****************** iteraion is over preparing for next one *****************************
            
            // Rules have been created. From the current possible ones, attempt to combine them.
            ArrayList<ValueSet> newValueSet = new ArrayList<>();
            for (int i = 0; i < newPossibleRules.size(); i++) {

                for (int j = i + 1; j < newPossibleRules.size(); j++) {
                    // Attempts to combine sets. If fails, returns null
                    //System.out.println("combining==>"+newUnmarked.get(i) +" and "+newUnmarked.get(j));
                    ValueSet combinedSet = ValueSet.combine(newUnmarked.get(i), newUnmarked.get(j), size_of_sets);
                    //System.out.println("=>"+ combinedSet);
                    if (combinedSet != null) {
                        newValueSet.add(combinedSet);
                    }
                }
            }
            
            size_of_sets++;
            
            
            // New ValueSets have been created.
            // Data cleaning ==> Remove duplicates 
            ArrayList<ValueSet> uniqueValueSet = new ArrayList<>();
            for (ValueSet a : newValueSet) {
                if (!uniqueValueSet.contains(a))
                    uniqueValueSet.add(a);
                    //System.out.println("new set>"+a);
            }

            ArrayList<LersRule> uniqueCertainRules = new ArrayList<>();
            for (LersRule r : newCertainRules) {
                if (!uniqueCertainRules.contains(r))
                    uniqueCertainRules.add(r);
            }

            ArrayList<LersRule> uniquePossibleRules = new ArrayList<>();
            for (LersRule r : newPossibleRules) {
                if (!uniquePossibleRules.contains(r))
                    uniquePossibleRules.add(r);
            }
            
            
            
            valueSets = uniqueValueSet;


            // Print Certain rules.
            System.out.println("CERTAIN RULES -------->");
            for(LersRule r : newCertainRules) {
                //printMessage(c.toString());
                System.out.println(r.toString());
            }

            // Print Possible rules with support and confidence.
            System.out.println("POSSIBLE RULES -------->");
            for(LersRule r : newPossibleRules) {
                //printMessage(p.toString());
                System.out.println(r.toString());
            }

            certainRules.addAll(uniqueCertainRules);
            possibleRules.addAll(uniquePossibleRules);

            is_there_left_unmarked_sets = !uniqueValueSet.isEmpty();
            
        }
        

        System.out.println("No more rules can be generated");

        
    }
    // input table and decision index
    void ProcessTable(ArrayList<ArrayList<String>> table, int d){
        
        System.out.println("Proceess table called");
        
        int row_len = table.get(0).size();
        int col_len = table.size();
        
        // for every index in row
        for (int i = 0; i < row_len; i++){
            
            HashMap<String, Set<Integer>> tmp_hm = new HashMap<>();
            Set<String> vals = new HashSet<>();

            // going through each of the columns
            for (int j = 0; j <col_len; j++) {

                String val = table.get(j).get(i);//  [j][i];

//                // Skip unspecified attributes
                if (val.equals("?"))
                    continue;

                Set<Integer> objects = tmp_hm.get(val);

                if (objects != null) {
                    // Add entry #.
                    objects.add(j);
                } else {
                    // Create new value.
                    Set<Integer> new_objects_set = new TreeSet<>();
                    new_objects_set.add(j);
                    tmp_hm.put(val, new_objects_set);
                }
            }
            
            // Creating ValueSets of attribute values 
            Set<String> keys = tmp_hm.keySet();
            for (String k : keys) {
                ArrayList<String> valueSet = new ArrayList<>();

                for (int a = 1; a < row_len; a++) {
                    valueSet.add(null);
                }

                // Add current Value to the ValueSet. Leave the rest blank. These will be filled in as
                // merging happens later on.
                valueSet.add(i, k);

                ValueSet ag = new ValueSet(valueSet, tmp_hm.get(k));

                // Separate the decision ValueSets from others.
                if (i == d) {
                    decisionValueSet.add(ag);
                } else {
                    valueSets.add(ag);
                }
            }
            
        }
        
    }
    
    
    
}

