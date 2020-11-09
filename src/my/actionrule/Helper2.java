
package kdd_project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Helper2 {
    
    // variables
    //  Ex.===> attibute f ==> value [f1, f2, f3]
    HashMap<String, ArrayList<String>> attribute_and_values = new HashMap<>();
    // reverse mapping, requires for checking if in set two values are not of same attibute
    // Ex.===>  f1 ==> f
    HashMap<String, String> value_and_attribute = new HashMap<>();
    // object => row number
    // this will show that which value is in which row 
    // Ex. f1 ===> [1,4,5,7,9] ===== f1 is present in rows 1,4,5,7,9  
    HashMap<String, HashSet<Integer>> values_and_objects = new HashMap<>();
    
    HashMap<String, String> CR = new HashMap<>(); // certain rules
    //HashSet<String> MS = new HashSet<>(); // marked set
    //HashSet<String> UMS = new HashSet<>(); // Unmarked set
    ArrayList<String> MS = new ArrayList<>(); // marked ArrayList
    ArrayList<String> UMS = new ArrayList<>(); // Unmarked ArrayList
    
    HashMap<String, String> GetCeratainRules(){
        return CR;
    }
    
    
    //  DA =====> DecisionAttribute  
    void GenerateCertainRules(ArrayList<ArrayList<String>> rows, String DA, ArrayList<String> attributes ){
        
        // initialize the empty values for attibutes
        // f ==> []
        for (String s : attributes) {
            attribute_and_values.put(s, new ArrayList<>());
        }
        
        // parsing rows for set of rows
        System.out.println("parsing rows to create sets.........");
        for (int i = 0; i < rows.size(); i++) {
            ArrayList<String> row = rows.get(i);
            for (int j = 0; j < attributes.size(); j++) {
                String value = row.get(j);
                if (values_and_objects.containsKey(value)) {
                    HashSet<Integer> n = values_and_objects.get(value);
                    n.add(i + 1);
                    values_and_objects.replace(value, n);
                } else {
                    HashSet<Integer> temp = new HashSet<>();
                    temp.add(i + 1);
                    values_and_objects.put(value, temp);
                    ArrayList<String> s = attribute_and_values.get(attributes.get(j));
                    s.add(value);
                    attribute_and_values.put(attributes.get(j), s);
                    value_and_attribute.put(value, attributes.get(j));
                }
            }
        }
        
        System.out.println("printing the sets.......");
        for (String s : values_and_objects.keySet()) {
            System.out.print("value=>" + s + " ");
            System.out.println("object=>" + values_and_objects.get(s).toString());
        }
        // printing attribute and values
        System.out.println("printing attribute to value map.........");
        for (String s : attribute_and_values.keySet()) {
            System.out.print("attribute=>" + s + " ");
            System.out.println("values=>" + attribute_and_values.get(s).toString());
        }
        // printing values and attribute (reverse mapping)
        System.out.println("printing value to attribute map.........");
        for (String s : value_and_attribute.keySet()) {
            System.out.println(s + " attribute of " + value_and_attribute.get(s));
        }
        
        // DAV ==> Decision attribute value 
        ArrayList<String> DAV = attribute_and_values.get(DA);
        System.out.println("decision attribute values = " + DAV.toString());
        // remove decsion attribute from of attributes
        attribute_and_values.remove(DA);
        
        
        System.out.println("First Loop==========>");
        // for every attribute
            // for every value of that attribute
                // check with every attribute of decision fro subset
        for (String s : attributes) {
            //System.out.println("s=>"+s);
            if (s.equals(DA)) {
                continue;
            }
            for (String value : attribute_and_values.get(s)) {
                // check if current attribute value is subset of DAV
                boolean isNotMarked = true;
                for (String dav : DAV) {
                    // prospective_set_to_be_marked
                    HashSet<Integer> set1 = values_and_objects.get(value);
                    // decision set
                    HashSet<Integer> set2 = values_and_objects.get(dav);

                    // checking for certain rule
                    // if set1 is subset of set2
                    if (set2.containsAll(set1)) {
                        // set1 is subset of set2 ==> marked ===> include in certain rule
                        CR.put(value, dav);
                        // value => dav
                        MS.add(value);
                        isNotMarked = false;
                    }
                }

                if (isNotMarked) {
                    UMS.add(value);
                }
            }
        }
        
        /// printing certain rules
        System.out.println("Certain rules =====>");
        for (String s : CR.keySet()) {
            System.out.println(s + "  ----->  " + CR.get(s));
        }
        System.out.println("un marked sets===>");
        for (String s : UMS) {
            System.out.println(s);
        }
        
        // generate Arraylist<HashSet<String>> from first loop output of unmarked and marked set
        ArrayList<HashSet<String>> UMSarr = GALF(UMS);
        ArrayList<HashSet<String>> MSarr = GALF(MS);
        
        
        // Generate Combination of un marked sets
        System.out.println("Generating Combination of un marked sets.....");
        ArrayList<HashSet<String>> CUMS = GCUMS(UMSarr, MSarr);

        System.out.println("printing combination of unmarked set to be used for second loop");
        for (HashSet<String> hs : CUMS) {
            System.out.println(hs.toString());
        }
        
        // further loops
        int i = 2;
        while (true) {

            System.out.println("going for " + i + " loop");
            //  ArrayList<HashSet<String>> UMSarr <--------------------------using this again
            // ArrayList<HashSet<String>> MSarr 
            UMSarr.clear();
            MSarr.clear();

            for (HashSet<String> hs : CUMS) {
                // generate hashset of integer from given set of values ==>(give common rows for given values => GCRGV)
                HashSet<Integer> set1 = GCRGV(hs);
                // compare it wiht decions values
                boolean isNotMarked = true;
                for (String dav : DAV) {
                    HashSet<Integer> set2 = values_and_objects.get(dav);

                    if (set2.containsAll(set1)) {
                        // set1 is subset of set2 ==> marked ===> include in certain rule
                        CR.put(hs.toString(), dav);
                        // value => dav
                        MSarr.add(hs);
                        isNotMarked = false;
                    }
                }
                if (isNotMarked) {
                    UMSarr.add(hs);
                }
            }
            // if in this iteration less than 2 Unmarket set is generated ===> no further compination of unmarket set is possible
            if (UMSarr.size() < 2) {
                break;
            }
            i++;
            System.out.println("Generating Combination of un-marked sets.....");
            CUMS = GCUMS(UMSarr, MSarr);

            System.out.println("printing combination of unmarked set......");
            for (HashSet<String> hs : CUMS) {
                System.out.println(hs.toString());
            }
        }
        
        
    }
    
    private ArrayList<HashSet<String>> GALF(ArrayList<String> UMS) {
        System.out.println("Generate arraylist from called");

        ArrayList<HashSet<String>> output = new ArrayList<>();
        for (String s : UMS) {
            HashSet<String> h = new HashSet<>();
            h.add(s);
            output.add(h);
        }
        return output;
    }
    
    private ArrayList<HashSet<String>> GCUMS(ArrayList<HashSet<String>> UMS, ArrayList<HashSet<String>> MS) {

        System.out.println("Generating combination of UM set  called");

        // Combination of un marked sets
        //HashSet<HashSet<String>> CUMS = new HashSet<>();
        ArrayList<HashSet<String>> CUMS = new ArrayList<>();

        for (int i = 0; i < UMS.size() - 1; i++) {

            HashSet<String> c1 = new HashSet<>();
            c1.addAll(UMS.get(i));
            //System.out.println("outerloop un market set c1=>" + c1);

            for (int j = i + 1; j < UMS.size(); j++) {
                HashSet<String> c2 = new HashSet<>();
                c2.addAll(UMS.get(j));
                //System.out.println("innerloop un market set c2=>" + c2);
                c2.addAll(c1);
                System.out.println("after adding c1 and c2 = >" + c2);
                //check if  after combining both the set it does not have values from same attributes
                if (CIIHVFSA(c2)) {
                    // yes cannot combine
                    continue;
                }
                // check if any of the new generated set are superset of Marked set
                boolean is_super_set = false;
                for (HashSet<String> hs : MS) {
                    if (c2.containsAll(hs)) {
                        System.out.println(c2 + " is a superset of Marked set " + hs+", so can't use it");
                        is_super_set = true;
                        break;
                    }
                }
                if (is_super_set) {
                    continue;
                }
                // otherwise add into new genearted combination of Un markedt set
                //System.out.println(c2 + " is not a superset of any marked set so adding in genereted possible combinations");
                CUMS.add(c2);
            }
        }
        return CUMS;
    }
    
    private boolean CIIHVFSA(HashSet<String> c1) {
        System.out.println("Checking if set has value from same attributes called");

        HashSet<String> tmp = new HashSet<>();
        for (String s : c1) {
            if (tmp.contains(value_and_attribute.get(s))) {
                System.out.println(c1 + " contains values from same atribute returning true");
                return true;
            } else {
                tmp.add(value_and_attribute.get(s));
            }
        }
        return false;
    }
    
    private HashSet<Integer> GCRGV(HashSet<String> hs) {
        HashSet<Integer> tmp = new HashSet<>();
        for (String s : hs) {
            if (tmp.size() == 0) {
                tmp.addAll(values_and_objects.get(s));
            } else {
                tmp.retainAll(values_and_objects.get(s));
            }
        }
        return tmp;
    }
}
