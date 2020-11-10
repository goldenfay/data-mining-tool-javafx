package model.mining.bases;

import static java.lang.Double.NaN;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import model.preprocessing.bases.DataSet;
import model.preprocessing.bases.generalDataSet;

/**
 *
 * @author Goldenfay
 */
public class AssociationRule<T> {

    FrequentSet leftSubset;
    FrequentSet rightSubset;

    public AssociationRule(FrequentSet leftSubset, FrequentSet rightSubset) {
        this.leftSubset = leftSubset;
        this.rightSubset = rightSubset;
    }

    public double calculateConfidence(generalDataSet data) {
        FrequentSet union=new FrequentSet(leftSubset);
        union.addAll(rightSubset.getItems());
        union.calculateSupport(data,1);
        System.out.println("Union :"+union.getSupport()+"   Left : "+leftSubset.getSupport());
        double conf = (double)union.getSupport()/(double)leftSubset.getSupport();
        if(Double.isNaN(conf))
            conf=0;
        System.out.println("Confidence :"+conf);
        return conf;
    }

    public static ArrayList<AssociationRule> extractAssociationRules(FrequentSet frequentitemSet, generalDataSet data,double minConfidence) {
        ArrayList<AssociationRule> assocRuleList = new ArrayList<AssociationRule>();

        int cardinality = frequentitemSet.size();
        System.out.println("Cardinality : "+cardinality);

        
        int size = 1;

        HashSet<FrequentSet> levelsSubsets = new HashSet<FrequentSet>(), duplication = new HashSet<FrequentSet>();
        while (size < cardinality) {
            levelsSubsets = (HashSet<FrequentSet>) duplication.clone();
            if (size == 1) {
                for (Object item : frequentitemSet) {
                    HashSet<FrequentSet> subset = new HashSet<FrequentSet>(), rest;
                    HashSet x = new HashSet(); 
                    x.add(item);
                    subset.add(new FrequentSet(x));
                    FrequentSet f = new FrequentSet(x);
                    f.calculateSupport(data,1);
                    levelsSubsets.add(f);

                    rest = (HashSet) frequentitemSet.getItems().clone();
                    rest.remove(item);
                    FrequentSet right = new FrequentSet((HashSet) rest.clone());
                    right.calculateSupport(data,1);
                    AssociationRule rule = new AssociationRule(f,right );
                    System.out.println("LEFT Support :"+f.getSupport()+" Right support:  "+right.getSupport());
                    System.out.println(rule.calculateConfidence(data));
                    
                    if(rule.calculateConfidence(data)>=minConfidence)
                        assocRuleList.add(rule);
                }
                
                size++;
            } else {
                for ( FrequentSet itemSet : (HashSet<FrequentSet>)levelsSubsets.clone()) {
                    if(size<itemSet.size()) break; // Work on this level complete ! Go to next level
                    for(Object item:frequentitemSet)
                        if(!itemSet.contains(item)){
                            itemSet.add(item);
                            break;
                        }
                    itemSet.calculateSupport(data,1);
                            
                    
                    FrequentSet f = new FrequentSet(itemSet.getItems());
                    f.calculateSupport(data,1);
                    levelsSubsets.add(f);
                    
                    HashSet rest = (HashSet) frequentitemSet.getItems().clone();
                    rest.removeAll(itemSet.getItems());
                    FrequentSet right = new FrequentSet(rest);
                    right.calculateSupport(data,1);
                    AssociationRule rule = new AssociationRule(f,right );
                    System.out.println(rule.calculateConfidence(data));
                    if(rule.calculateConfidence(data)>=minConfidence)
                        assocRuleList.add(rule);
                }
                size++;
            }

            duplication = (HashSet<FrequentSet>) levelsSubsets.clone();
            levelsSubsets.clear();
        }

        return assocRuleList;
    }

    public FrequentSet getLeftSubset() {
        return leftSubset;
    }

    public void setLeftSubset(FrequentSet leftSubset) {
        this.leftSubset = leftSubset;
    }

    public FrequentSet getRightSubset() {
        return rightSubset;
    }

    // Getters & Setters
    public void setRightSubset(FrequentSet rightSubset) {
        this.rightSubset = rightSubset;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
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
        final AssociationRule other = (AssociationRule) obj;
        if (!Objects.equals(this.leftSubset, other.leftSubset)) {
            return false;
        }
        if (!Objects.equals(this.rightSubset, other.rightSubset)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AssociationRule:" + leftSubset.toString()+"\t ==>" + rightSubset.toString();
    }
    
    
    

}
