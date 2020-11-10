
package model.mining.bases;

import java.util.ArrayList;
import model.preprocessing.bases.Row;

/**
 *
 * @author Goldenfay
 */
public class Cluster {
    
    private int center;
    private ArrayList<Integer> elemnts;

    public Cluster(int center) {
        this.center = center;
        this.elemnts=new ArrayList<>();
    }
    
    
    public Cluster copy(){
        Cluster newCl=new Cluster(this.center);
        newCl.setElemnts(this.elemnts);
        
        return newCl;
    }
    
    
    
    
            //Getters & Setters

    public int getCenter() {
        return center;
    }

    public void setCenter(int center) {
        this.center = center;
    }

    public ArrayList<Integer> getElemnts() {
        return elemnts;
    }

    public void setElemnts(ArrayList<Integer> elemnts) {
        this.elemnts = elemnts;
    }

    @Override
    public String toString() {
        return "center : "+this.center+" , Elements("+this.elemnts.size()+"):"+this.elemnts.toString();
    }
    
    
    
    
    

    
    
    
    
    
}
