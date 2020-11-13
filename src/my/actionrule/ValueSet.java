
package my.actionrule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class ValueSet {
    // it is a possible set of values Ex. [ f1 , A1 ,  NUll , Null]
    // it will store value with its position hence, it also consist of null values
    
    public ArrayList<String> values;
    // it is set of rows/objects which satisfies/consist of combination of values
    public Set<Integer> objects;
    
    public ValueSet(ArrayList<String> values, Set<Integer> objects) {
        this.values = values;
        this.objects = objects;
    }
    
    // Combine two ValueSet Ex ==>[ f1 , A1 ,  NUll , Null] + [ NUll , Null , B1 , C1 ]  => [ f1 , A1 , B1 , C1 ]
    public static ValueSet combine(ValueSet a, ValueSet b, int Size_limit) {
    // combinnig two ValueSets with size_limit constraint, if the new set is not of size_limit it will return null

        int val_set_size = a.values.size();

        ArrayList<String> new_val_set = new ArrayList<>();

        int num_same_val = 0;
        int num_diff_val = 0;

        for (int i = 0; i < val_set_size; i++) {
            String val_in_a = a.values.get(i);
            String val_in_b = b.values.get(i);

            // check if both value postion are null
            if (val_in_a == null && val_in_b == null) {
                new_val_set.add(null);
                continue;
            }

            if (val_in_a == null) {
                new_val_set.add(val_in_b);
                num_diff_val++;

            } else if (val_in_b == null) {
                new_val_set.add(val_in_a);
                num_diff_val++;

            } else {
                // val_in_a and val_in_b both exist and  both should be same.
                if (val_in_a.equals(val_in_b)) {
                    new_val_set.add(val_in_a);
                    num_same_val++;
                } else {
                    return null;
                }
            }
        }

        // snum_same_val + num_diff_val should be equal to size_limit.
        int totalSize = num_same_val + num_diff_val;
        if (totalSize != Size_limit)
            return null;

        // new ValueSet will have rows/objects from intersection of two input ValueSet
        Set<Integer> newObjects = new HashSet<>();
        for (Integer e : a.objects) {
            if (b.objects.contains(e)) {
                newObjects.add(e);
            }
        }

        return new ValueSet(new_val_set, newObjects);
    }
    
    
    // True if all values (except null) of this ValueSet are present in given ValueSet
    public boolean isSubsetOf(ValueSet other) {

        for (int i = 0; i < this.values.size(); i++) {
            String val = this.values.get(i);
            if (val == null)
                continue;

            String otherItem = other.values.get(i);
            if (!val.equals(otherItem)) {
                return false;
            }
        }

        return true;
    }
    
    public int getObjectSize() {
        return this.objects.size();
    }
    
    @Override
    public boolean equals(Object other) {

        if (!(other instanceof ValueSet)) {
            return false;
        }

        ValueSet a = (ValueSet) other;

        return this.values.equals(a.values);

    }

    @Override
    public String toString() {
        return values+" objects count=>"+objects.size();
    }
    
    
}
