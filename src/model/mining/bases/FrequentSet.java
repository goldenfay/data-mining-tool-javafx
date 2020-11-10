package model.mining.bases;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import model.preprocessing.bases.DataSet;
import model.preprocessing.bases.Row;
import model.preprocessing.bases.generalDataSet;
import model.preprocessing.bases.generalRow;

/**
 *
 * @author Goldenfay
 */
public class FrequentSet<T> extends HashSet {

    private int support;

    public FrequentSet(HashSet items) {
        super();
        
        this.addAll(items);
        
        
    }
    
    
    
    public void calculateSupport(DataSet data){
        int cpt=0;
        for(Row row:data.getInstances()){
            
            if(row.getValues().containsAll(this))
                cpt++;
        }
        
        this.support=cpt;
        
        
    }
    
    public void calculateSupport(generalDataSet data,int flag){
        int cpt=0;
        for(generalRow row:data.getInstances()){
            
            if(row.getValues().containsAll(this.getItems()))
                cpt++;
        }
        
        this.support=cpt;
        
        
    }

    
    public HashSet getItems(){
        HashSet items=new HashSet<T>();
        this.forEach((t) -> {
            items.add(t);
        });
        return items;
    }
    
            // Getters & Setters
    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    @Override
    public String toString() {
        String string="{";
        int i=0;
        for(Object el:this){
            string+=(T)el+(i!=this.size()-1?" , ":"");
            i++;
        }
        string+="}";
        return string;
    }

    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FrequentSet other = (FrequentSet) obj;
        for(Object el:other)
            if(!this.contains(el))
                return false;
        return true;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    

}
