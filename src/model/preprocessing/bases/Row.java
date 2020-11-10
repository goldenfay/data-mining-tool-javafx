
package model.preprocessing.bases;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Implémentation d'une instance/ligne de données.
 * @author Goldenfay
 */
public class Row implements Comparable,Serializable{
    
    private ArrayList<Double> values=new ArrayList<Double>();

    public Row() {
        if(values==null) 
            values=new ArrayList<Double>();
    }

        
    public Row copy(){
        Row newRow =new Row();
        newRow.getValues().addAll(this.values);
        return newRow;
    }
    
    
    public generalRow transform(){
        generalRow row=new generalRow();
        
        for(Double el:values)
            row.getValues().add(el+"");
        
        return row;
    }
    
    public ArrayList<Double> getValues() {
        return values;
    }

    public void setValues(ArrayList<Double> values) {
        this.values = values;
    }
    
    
    
    public ArrayList<String> getValuesAsStringArray(){
        
        ArrayList<String> listeValues=new ArrayList<String>();
        
        for(int i=0;i<values.size();i++) listeValues.add(values.get(i)+"");
        
        return listeValues;
    }

    

    @Override
    public int compareTo(Object o) {
        if(!(o instanceof Row)){
            throw new IllegalArgumentException("Invalid argument on CompareTo Method : Argument is not a 'Row' Object.");
        }
        Row r=(Row)o;
        
        
        
        return 0;
    }

    @Override
    public String toString() {
        return this.values.toString();
    }
    
    
    
    
    
    
    
}
