package my.actionrule;

import java.util.Arrays;
import java.util.List;

// Represents an action rule. Transformations can be derived from the dectoFromPair field
public class ActionRuleClass {

    private List<String[]> tojpairs;
    private List<String> attributeNames;
    private String[] dectojpair;

    private int sup;
    private double confi;

    public ActionRuleClass(List<String[]> tojpairs, List<String> attributeNames, String[] dectojpair, int sup, double confi) {
        this.tojpairs = tojpairs;
        this.attributeNames = attributeNames;
        this.dectojpair = dectojpair;
        this.sup = sup;
        this.confi = confi;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ActionRuleClass)) {
            return false;
        }
        ActionRuleClass r = (ActionRuleClass) other;
        if (this.tojpairs.size() != r.tojpairs.size()) {
            return false;
        }
        for (int i = 0; i < this.tojpairs.size(); i++) {
            if (!Arrays.equals(this.tojpairs.get(i), r.tojpairs.get(i))) {
                return false;
            }
        }
        return true;
    }
    @Override
    public int hashCode() {
        int val = tojpairs.get(0)[0].hashCode();
        return val;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for (int i = 0; i < this.tojpairs.size(); i++) {
            String[] currTerm = this.tojpairs.get(i);
            if (currTerm[1].equals(currTerm[2])) {
                stringBuilder.append(attributeNames.get(Integer.parseInt(currTerm[0])));
                stringBuilder.append(" = ");
                stringBuilder.append(currTerm[1]);
            } else {
                stringBuilder.append(attributeNames.get(Integer.parseInt(currTerm[0])));
                stringBuilder.append(": ");
                stringBuilder.append(currTerm[1]);
                stringBuilder.append(" -> ");
                stringBuilder.append(currTerm[2]);
            }
            stringBuilder.append(", ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1);
        stringBuilder.append(")");
        stringBuilder.append(" -> ");
        stringBuilder.append("(");
        stringBuilder.append(attributeNames.get(Integer.parseInt(this.dectojpair[0])));
        stringBuilder.append(": ");
        stringBuilder.append(this.dectojpair[1]);
        stringBuilder.append(" -> ");
        stringBuilder.append(this.dectojpair[2]);
        stringBuilder.append(")");

        stringBuilder.append(String.format(" SUPPORT: "+this.sup+" CONFIDENCE: "+(this.confi *100)+ " percent"));

        return stringBuilder.toString();
    }




}
