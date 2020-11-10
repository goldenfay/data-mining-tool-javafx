package model.preprocessing.preprocessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import model.preprocessing.bases.DataSet;
import model.preprocessing.bases.Row;

/**
 * A class to regroup all measurements to perform on a Dataset.
 * @author Goldenfay
 */
public class Measurement {

    /**
     * Calcul de la moyenne des valeurs d'une colonne(attribut).
     *
     * @param Data : Le dataSet d'entrée.
     * @param attIndex : L'index de l'attribut(colonne) à calculer la moyenne
     * pour.
     * @return La moyenne des valeurs de l'attribut attIndex
     */
    public static double calculateAverage(DataSet Data, int attIndex) {
        if(! Data.isNumeric(attIndex)) return -1;
        double S = 0;
        for (Row r : Data.getInstances()) {
            S = S + r.getValues().get(attIndex);
        }

        return S / Data.getInstances().size();
    }

    /**
     * Calcul de la médiane d'un attribut d'index attIndex.
     *
     * @param Data Le DataSet d'entrée.
     * @param attIndex L'index de l'attribut en question.
     * @return La médiane de l'attribut d'index attIndex.
     */
    public static double calculateMedian(DataSet Data, int attIndex) {
        int middle;
        ArrayList<Double> ordered = new ArrayList<Double>();
        for (Row r : Data.getInstances()) {
            ordered.add(r.getValues().get(attIndex));
        }

        Collections.sort(ordered);

        if (Data.getInstances().size() % 2 != 0) {
            middle = (Data.getInstances().size() / 2) + 1;
            return ordered.get(middle-1);
        } else {
            middle = Data.getInstances().size() / 2;
            return (ordered.get(middle-1)+ordered.get(middle))/2;
        }

        

    }

    /**
     * Calcul du Mode des valeurs d'un attribut d'index attIndex. <br/>
     * RQ : Il s'agit ici de calcul de mode dans le cas discret, puisque les
     * données du DataSet peuvent être prises comme des valeurs discrètes.
     *
     * @param Data
     * @param attIndex
     * @return
     */
    public static double calculateMod(DataSet Data, int attIndex) {
        double mode = 0;
        double key;
        int cpt = 0;
        HashMap<Double, Integer> frequencies = new HashMap<Double, Integer>();
        for (Row r : Data.getInstances()) {
            key = r.getValues().get(attIndex);
            if (!frequencies.containsKey(key)) {
                frequencies.put(key, 1);
            } else {
                frequencies.put(key, frequencies.get(key) + 1);
            }

        }

        mode = (double) frequencies.keySet().toArray()[0];
        for (Double el : frequencies.keySet()) {
            if (frequencies.get(el) > frequencies.get(mode)) {
                mode = el;
            }

        }

        return mode;
    }

    
    /**
     *  Calcul de la variance d'un attribut d'index attIndex
     * @param Data
     * @param attIndex
     * @return 
     */
    public static double calculateVariance(DataSet Data, int attIndex) {
        double sum=0;
        double mean=calculateAverage(Data, attIndex);
        for(Row row:Data.getInstances()){
            sum+=Math.pow(row.getValues().get(attIndex)-mean,2);
            
        }
        return (1/(Data.getInstances().size()-1)*sum);
    
    }
    
    
    /**
     * Calcul l'écart type de l'attribut d'index attIndex.
     * @param Data
     * @param attIndex
     * @return 
     */
    public static double calculateStandardDeviation(DataSet Data, int attIndex) {
        
        return Math.sqrt(attIndex);
    }
    
    /**
     * Calcul de la règles des 5 nombres : Minimum,1er quartil, Médiane, 3eme quartil, Maximum.
     * @param Data
     * @param attIndex
     * @return 
     */
    public static HashMap<String, Double> fiveNumberSummary(DataSet Data, int attIndex) {

        HashMap<String, Double> results = new HashMap<String, Double>();

        int Q1, Q3;
        ArrayList<Double> ordered = new ArrayList<Double>();
        for (Row r : Data.getInstances()) {
            ordered.add(r.getValues().get(attIndex));
        }

        Collections.sort(ordered);

        if (Data.getInstances().size() % 4 != 0) {
            Q1 = (Data.getInstances().size() / 4) + 1;
        } else {
            Q1 = Data.getInstances().size() / 4;
        }

        if ((Data.getInstances().size() * 3) % 4 != 0) {
            Q3 = ((Data.getInstances().size() * 3) / 4) + 1;
        } else {
            Q3 = (Data.getInstances().size() * 3) / 4;
        }

        results.put("Min", ordered.get(0));
        results.put("Q1", ordered.get(Q1-1));
        results.put("Median", calculateMedian(Data, attIndex));
        results.put("Q3", ordered.get(Q3-1));
        results.put("Max", ordered.get(ordered.size()-1));
        

        return results;
    }
    
    
    

}
