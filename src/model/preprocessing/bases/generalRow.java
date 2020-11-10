
package model.preprocessing.bases;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Goldenfay
 */
public class generalRow implements Comparable, Serializable{
    
    private ArrayList<String> values=new ArrayList<String>();

    public generalRow() {
        if(values==null) 
            values=new ArrayList<String>();
    }

        
    public generalRow copy(){
        generalRow newRow =new generalRow();
        newRow.getValues().addAll(this.values);
        return newRow;
    }
    
    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
    
    
    
    public ArrayList<String> getValuesAsStringArray(){
        
        ArrayList<String> listeValues=new ArrayList<String>();
        
        for(int i=0;i<values.size();i++) listeValues.add(values.get(i)+"");
        
        return listeValues;
    }

    

    @Override
    public int compareTo(Object o) {
        if(!(o instanceof generalRow)){
            throw new IllegalArgumentException("Invalid argument on CompareTo Method : Argument is not a 'Row' String.");
        }
        generalRow r=(generalRow)o;
        
        int diff=0;
        
        for(int i=0;i<r.values.size();i++)
            diff+=values.get(i).compareTo(r.getValues().get(i));
        
        
        return diff;
    }
    
    
    
    @Override
    public String toString() {
        return this.values.toString();
    }
}
