package model.mining.algorithms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import model.mining.bases.AssociationRule;
import model.mining.bases.FrequentSet;
import model.preprocessing.bases.DataSet;
import model.preprocessing.bases.Row;
import model.preprocessing.bases.generalDataSet;
import model.preprocessing.bases.generalRow;

/**
 *
 * @author Goldenfay
 */
public class Apriori {

    public final int MINIMUM_SUPPORT;
    public double minConfidence;
    private generalDataSet data;
    private generalDataSet generalData;

    public Apriori(generalDataSet data, int MINIMUM_SUPPORT, double minConfidence) {
        this.MINIMUM_SUPPORT = MINIMUM_SUPPORT;
        this.minConfidence = minConfidence;
        this.data = data;
        

    }
    
    
   

    public HashSet<FrequentSet> extractSingletonItemSets() {
        HashSet<FrequentSet> freqSet = new HashSet<FrequentSet>();
        HashMap<String, Integer> freqDist = new HashMap<String, Integer>();
        for (generalRow row : data.getInstances()) {
            for (String val : row.getValues()) {
                if (freqDist.containsKey(val)) {
                    freqDist.put(val, freqDist.get(val) + 1);
                } else {
                    freqDist.put(val, 1);
                }
            }
        }

        freqDist.forEach((t, u) -> {
            if (u >= MINIMUM_SUPPORT) {

                HashSet<String> singleton = new HashSet<String>();
                singleton.add(t);
                FrequentSet freqItem = new FrequentSet(singleton);
                freqItem.setSupport(u);

                freqSet.add(freqItem);
            }

        });
        return freqSet;
    }

    public HashMap<Integer, HashSet<FrequentSet>> forwardExtraction(HashSet<FrequentSet> baseSet) {
        HashMap<Integer, HashSet<FrequentSet>> all_I_itemSets = new HashMap<Integer, HashSet<FrequentSet>>();
        HashSet<String> allItems = new HashSet<String>();
        baseSet.forEach((t) -> {
            allItems.addAll(t.getItems());
        });

        all_I_itemSets.put(1, baseSet);
        int level = 2;

        while (baseSet != null && !baseSet.isEmpty()) {
            System.out.println("***************************** LEVEL "+level+"***********************************");
            all_I_itemSets.put(level, null);
            HashSet<FrequentSet> i_itemSet = new HashSet<FrequentSet>();

            baseSet.forEach((t) -> {
                i_itemSet.addAll(generateFrequenItemSets(t.getItems(), allItems));
            });
            
            baseSet=new HashSet<FrequentSet>(i_itemSet);
            for(FrequentSet f:i_itemSet) System.out.println(" : "+f.getSupport());
            
            all_I_itemSets.put(level, (HashSet<FrequentSet>) i_itemSet.clone());
            if(!baseSet.isEmpty())
                level++;

        }

        return all_I_itemSets;

    }

    public HashSet<AssociationRule> extractAssociationRules(double minConfidence) {

        HashSet<AssociationRule> rulesSet = new HashSet<AssociationRule>();

        return rulesSet;

    }

    

    public HashSet<FrequentSet> generateFrequenItemSets(HashSet<String> subset, HashSet<String> set) {
        HashSet<FrequentSet> generatedSubSets = new HashSet<FrequentSet>();

        HashSet<String> intersect = new HashSet<String>(set);
        intersect.retainAll(subset);
        HashSet<String> availables = new HashSet<String>(set);
        availables.removeAll(intersect);

        availables.forEach((t) -> {
            

            HashSet<String> tempSet = new HashSet<String>(subset);
            tempSet.add(t);
            FrequentSet frequent = new FrequentSet((HashSet) tempSet.clone());
            frequent.calculateSupport(data,1);
            if (frequent.getSupport() >= MINIMUM_SUPPORT) {
                generatedSubSets.add(frequent);
            }

            
        });

        return generatedSubSets;

    }
    
    
    
    
    /*****************************************************************************************************************/
    /*****************************************************************************************************************/
    /*****************************************************************************************************************/
    /*****************************************************************************************************************/
    
    
    
    public HashSet<FrequentSet<String>> extractSingletonItemSets_generalized() {
        HashSet<FrequentSet<String>> freqSet = new HashSet<FrequentSet<String>>();
        HashMap<String, Integer> freqDist = new HashMap<String, Integer>();
        for (generalRow row : data.getInstances()) {
            for (String val : row.getValues()) {
                if (freqDist.containsKey(val)) {
                    freqDist.put(val, freqDist.get(val) + 1);
                } else {
                    freqDist.put(val, 1);
                }
            }
        }

        freqDist.forEach((t, u) -> {
            if (u >= MINIMUM_SUPPORT) {

                HashSet<String> singleton = new HashSet<String>();
                singleton.add(t);
                FrequentSet freqItem = new FrequentSet(singleton);
                freqItem.setSupport(u);

                freqSet.add(freqItem);
            }

        });
        return freqSet;
    }

    public HashMap<Integer, HashSet<FrequentSet<String>>> forwardExtraction_generalized(HashSet<FrequentSet<String>> baseSet) {
        HashMap<Integer, HashSet<FrequentSet<String>>> all_I_itemSets = new HashMap<Integer, HashSet<FrequentSet<String>>>();
        HashSet<String> allItems = new HashSet<String>();
        baseSet.forEach((t) -> {
            allItems.addAll(t.getItems());
        });

        all_I_itemSets.put(1, baseSet);
        int level = 2;

        while (baseSet != null && !baseSet.isEmpty()) {
            //System.out.println("***************************** LEVEL "+level+"***********************************");
            all_I_itemSets.put(level, null);
            HashSet<FrequentSet<String>> i_itemSet = new HashSet<FrequentSet<String>>();

            baseSet.forEach((t) -> {
                i_itemSet.addAll(generateFrequenItemSets_generalized(t.getItems(), allItems));
            });
            
            baseSet=new HashSet<FrequentSet<String>>(i_itemSet);
            for(FrequentSet f:i_itemSet) System.out.println(f.getSupport());
            
            all_I_itemSets.put(level, i_itemSet);
            if(!baseSet.isEmpty())
                level++;

        }

        return all_I_itemSets;

    }

    public HashSet<AssociationRule> extractAssociationRules_generalized(double minConfidence) {

        HashSet<AssociationRule> rulesSet = new HashSet<AssociationRule>();

        return rulesSet;

    }

    

    public HashSet<FrequentSet<String>> generateFrequenItemSets_generalized(HashSet<String> subset, HashSet<String> set) {
        HashSet<FrequentSet<String>> generatedSubSets = new HashSet<FrequentSet<String>>();

        HashSet<String> intersect = new HashSet<String>(set);
        intersect.retainAll(subset);
        HashSet<String> availables = new HashSet<String>(set);
        availables.removeAll(intersect);

        availables.forEach((t) -> {
            

            HashSet<String> tempSet = new HashSet<String>(subset);
            tempSet.add(t);
            FrequentSet<String> frequent = new FrequentSet<String>(tempSet);
            frequent.calculateSupport(data,1);
            if (frequent.getSupport() >= MINIMUM_SUPPORT) {
                generatedSubSets.add(frequent);
            }

            
        });

        return generatedSubSets;

    }

}
