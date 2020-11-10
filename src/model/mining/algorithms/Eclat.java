package model.mining.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import model.mining.bases.AssociationRule;
import model.mining.bases.FrequentSet;
import model.preprocessing.bases.DataSet;
import model.preprocessing.bases.generalRow;
import model.preprocessing.bases.generalDataSet;

/**
 * Eclat association rules extraction algorithm implementation class
 * @author Goldenfay
 */
public class Eclat {

    public final int MINIMUM_SUPPORT;
    public double minConfidence;
    private generalDataSet data;

    public Eclat(generalDataSet data, int MINIMUM_SUPPORT, double minConfidence) {
        this.MINIMUM_SUPPORT = MINIMUM_SUPPORT;
        this.minConfidence = minConfidence;
        this.data = data;

    }

    /**
     * Méthode qui construit la table verticale initiale des singletons associés
     * avec les lignes où quelle ils figurent.
     *
     * @return La table inversé construite depuis le dataSet
     */
    public HashMap<FrequentSet, HashSet<Integer>> constructReversedTable() {
        HashMap<FrequentSet, HashSet<Integer>> singletonsTable = new HashMap<FrequentSet, HashSet<Integer>>();
        ArrayList<generalRow> instances = data.getInstances();
        for (int i = 0; i < instances.size(); i++) {
            for (String val : instances.get(i).getValues()) {
                HashSet<String> sing = new HashSet<>();
                sing.add(val);
                FrequentSet key = new FrequentSet(sing);
                if (!singletonsTable.containsKey(key)) {
                    singletonsTable.put(key, new HashSet<Integer>());
                } else {
                    HashSet<Integer> value = singletonsTable.get(key);
                    value.add(i);
                    singletonsTable.put(key,value);

                }
            }
        }
         // Filtering
        ((HashMap<FrequentSet, HashSet<Integer>>)(singletonsTable.clone())).forEach((t, u) -> {
            if(u.size()<MINIMUM_SUPPORT){
                 singletonsTable.remove(t);
                
            }
                
        });
        singletonsTable.forEach((t, u) -> {
            t.setSupport(u.size());
        });

        singletonsTable.forEach((t, u) -> {
            System.out.println(t+"\t :"+t.getSupport());
        });
        return singletonsTable;
    }

    public HashMap<FrequentSet, HashSet<Integer>> forwardExtraction(HashMap<FrequentSet, HashSet<Integer>> baseTable) {
        HashMap<FrequentSet, HashSet<Integer>> globalTable = new HashMap<FrequentSet, HashSet<Integer>>();
        //globalTable.putAll(baseTable);
        HashMap<FrequentSet, HashSet<Integer>> effectiveTable = (HashMap<FrequentSet, HashSet<Integer>>) baseTable.clone();

        int level = 1;

        while (effectiveTable != null && !effectiveTable.isEmpty()) {
            System.out.println("***************************** LEVEL " + level + "***********************************");

            HashMap<FrequentSet, HashSet<Integer>> thisLevelEntries = new HashMap<FrequentSet, HashSet<Integer>>();
            for (Map.Entry<FrequentSet, HashSet<Integer>> entry : effectiveTable.entrySet()) {
                if (entry.getKey().size() < level) {
                    continue;
                }
                if (entry.getKey().size() == level) {
                    thisLevelEntries.put(entry.getKey(), (HashSet<Integer>) entry.getValue().clone());
                }
                if (entry.getKey().size() > level) {
                    break;
                }

            }
             System.out.println("Effectif    "+level);
            thisLevelEntries.forEach((t, u) -> {
                System.out.println(t.getItems()+"   : "+t.getSupport());
            });
            
            
            effectiveTable = generateSubSetsTable(thisLevelEntries);
            if(!effectiveTable.isEmpty())
                globalTable.putAll((Map<? extends FrequentSet, ? extends HashSet<Integer>>) effectiveTable.clone());
            
            level++;
            System.out.println("Level "+(level-1));
            effectiveTable.forEach((t, u) -> {
                System.out.println(t.getItems()+"   : "+t.getSupport());
            });

        }
        
        globalTable.forEach((t, u) -> {
                System.out.println(t.getItems()+"   : "+t.getSupport());
            });

        return globalTable;

    }

    public HashSet<AssociationRule> extractAssociationRules(double minConfidence) {

        HashSet<AssociationRule> rulesSet = new HashSet<AssociationRule>();

        return rulesSet;

    }

    

    private HashMap<FrequentSet, HashSet<Integer>> generateSubSetsTable(HashMap<FrequentSet, HashSet<Integer>> levelEntries) {
        
        
        HashMap<FrequentSet, HashSet<Integer>> resultTable = new HashMap<>();
        if(levelEntries.isEmpty())
            return resultTable;
        int levelSize = -1;
        ArrayList<FrequentSet> keyList = new ArrayList<>(levelEntries.keySet());
        levelSize = keyList.get(0).size();
        for (int i = 0; i < keyList.size(); i++) {

            for (int j = i+1; j < keyList.size(); j++) {
                    // Get the ligne where itemSet j occured
                HashSet<Integer> linesSet = levelEntries.get(keyList.get(j));
                    // Make the intersection
                linesSet.retainAll(levelEntries.get(keyList.get(i)));
                
                    // If resulting lines Set support is > min support, add it to the table asoociated with the suitable key
                if(linesSet.size()>=MINIMUM_SUPPORT){
                    System.out.println("Intersection : "+linesSet.toString());
                    HashSet<String> set = (HashSet<String>) keyList.get(i).getItems().clone();
                    set.addAll(keyList.get(j).getItems());
                    FrequentSet f = new FrequentSet((set));
                    f.setSupport(linesSet.size());
                    resultTable.put(f, (HashSet<Integer>) linesSet.clone());
                }
                
            }

        }

        return resultTable;
    }

}
