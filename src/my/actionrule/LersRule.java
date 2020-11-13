
package my.actionrule;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class LersRule {
    
    ValueSet leftSet;
    ValueSet rightSet; // decision set
    double confidence;
    int support;
    ArrayList<String> AttributesName;  //Ex.  [F,  A,  B]
    
    public LersRule(ValueSet a, ValueSet d, double confidence, int support, ArrayList<String> Names) {
        this.leftSet = a;
        this.rightSet = d;
        this.confidence = confidence;
        this.support = support;
        this.AttributesName = Names;
    }
    
    @Override
    public String toString() {
        // (a1, c5) -> d6 -- Support:2 -- Confidence 50%

        String s ="";
        s += "[ ";

        for (int i = 0; i < this.leftSet.values.size(); i++) {
            String val = this.leftSet.values.get(i);
            if (val != null) {
                String AttName = this.AttributesName.get(i);
                s += "(" + AttName + ") " ;
                s += val+", ";
            }
        }

        // Remove last comma and space
        int t = s.lastIndexOf(',');
        s = s.substring(0, t);
        s += " ] ---> "; 
        
        // Adding decision valueSet
        for (int i = 0; i < rightSet.values.size(); i++) {
            if (rightSet.values.get(i) != null) {
                s += "( "+ this.AttributesName.get(i)+ " ) ";
                s += this.rightSet.values.get(i);
                break;
            }
        }
        //new DecimalFormat("#.##").format(dblVar);
        //String.format(" 0.2f%",this.confidence)
        String tmp = String.format("%.2f",this.confidence*100);
        s += "{ Support==>"+ this.support + " Confidence==>"+tmp+"%}\n";
        return s; 
    }
    
    @Override
    public boolean equals(Object other) {

        if (!(other instanceof LersRule)) {
            return false;
        }

        LersRule r = (LersRule) other;

        return this.leftSet.equals(r.leftSet) && this.rightSet.equals(r.rightSet);

    }
    
}
