package model.mining.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import model.mining.bases.Cluster;
import model.preprocessing.bases.DataSet;
import model.preprocessing.bases.Row;
import model.preprocessing.bases.generalDataSet;
import model.preprocessing.bases.generalRow;
import model.preprocessing.bases.generalDataSet;

/**
 * K-Medoids clustering algorithm implementation class
 * @author Goldenfay
 */
public class KMedoids {

    private int K;
    private int maxItterations;
    private generalDataSet data;
    private LinkedHashMap<Integer, Double> SSEProgress = null;

    public KMedoids(int K, generalDataSet data) {
        this.K = K;
        this.data = data;
    }
    /*
    public ArrayList<Cluster> cluster() {

        ArrayList<Cluster> clustersList = new ArrayList<Cluster>();

        ArrayList<Integer> choosedMedoids = new ArrayList<>();
        boolean stopCondition = false;
        // Create K Clusters randomeley
        for (int i = 0; i < K; i++) {
            int randommedoid;
            do {
                randommedoid = (int) ThreadLocalRandom.current().nextInt(data.getInstances().size());
            } while (choosedMedoids.contains(randommedoid));

            choosedMedoids.add(randommedoid);
            clustersList.add(new Cluster(randommedoid));
        }

        while (!stopCondition(clustersList)) {

            // Calculate Distances between every row and every cluster center
            for (int i = 0; i < data.getInstances().size(); i++) {
                generalRow row = data.getInstances().get(i);
                double distance = 0, minDist = 0;
                Cluster choosedCluster = null;
                for (Cluster c : clustersList) {
                    if (i == c.getCenter()) {
                        continue;
                    }
                    distance = Math.abs(data.getInstances().get(c.getCenter()).compareTo(row));
                    if (distance < minDist) {
                        minDist = distance;
                        choosedCluster = c;
                    }

                }
                choosedCluster.getElemnts().add(i);
                Collections.shuffle(null);

            }

        }

        return clustersList;
    }

    public ArrayList<Cluster> startClustering() {

        ArrayList<Cluster> clustersList = new ArrayList<Cluster>();

        ArrayList<Integer> choosedMedoids = new ArrayList<>();
        boolean stopCondition = false;
        // Create K Clusters randomeley
        for (int i = 0; i < K; i++) {
            int randommedoid;
            do {
                randommedoid = (int) ThreadLocalRandom.current().nextInt(data.getInstances().size());
            } while (choosedMedoids.contains(randommedoid));

            choosedMedoids.add(randommedoid);
            clustersList.add(new Cluster(randommedoid));
        }

        while (!stopCondition) {
            ArrayList<Double> sseLists = new ArrayList<>();

            // Calculate Distances between every row and every cluster center
            for (int i = 0; i < data.getInstances().size(); i++) {
                generalRow row = data.getInstances().get(i);
                double distance = 0, minDist = 0;
                Cluster choosedCluster = null;
                for (Cluster c : clustersList) {
                    if (i == c.getCenter()) {
                        continue;
                    }
                    distance = Math.abs(data.getInstances().get(c.getCenter()).compareTo(row));
                    if (distance < minDist) {
                        minDist = distance;
                        choosedCluster = c;
                    }

                }
                choosedCluster.getElemnts().add(i);
                Collections.shuffle(null);

            }

            stopCondition = true;
            for (int cindex = 0; cindex < clustersList.size(); cindex++) {
                Cluster c = clustersList.get(cindex), centerDup = c.copy();
                ArrayList<Integer> clusterElements = new ArrayList<>(c.getElemnts());

                // Chosse a non-medoid row
                generalRow nonMedoid;
                for (int i = 0; i < clusterElements.size(); i++) {
                    if (clusterElements.get(i) == c.getCenter()) {
                        continue;
                    }

                    nonMedoid = data.getInstances().get(clusterElements.get(i));

                    double sseWithOther = 0.0, sseOthersWithCenter = 0.0;

                    for (int oindex : clusterElements) {
                        sseWithOther += Math.sqrt(Math.pow(nonMedoid.compareTo(data.getInstances().get(oindex)), 2));
                        sseOthersWithCenter += Math.sqrt(Math.pow(data.getInstances().get(c.getCenter()).compareTo(data.getInstances().get(oindex)), 2));
                    }

                    if (sseWithOther < sseOthersWithCenter) {
                        clustersList.get(cindex).setCenter(i);
                        sseLists.set(cindex, sseWithOther);
                        break;

                    } else {
                        sseLists.set(cindex, sseOthersWithCenter);
                    }

                }

                if (!data.getInstances().get(centerDup.getCenter()).equals(data.getInstances().get(clustersList.get(cindex).getCenter()))) {
                    stopCondition = false;
                }

            }
        }

        return clustersList;
    }


    */
    public ArrayList<Cluster> launchClustering() {

        ArrayList<Cluster> clustersList = new ArrayList<Cluster>();

        ArrayList<Integer> choosedMedoids = new ArrayList<>();
        boolean stopCondition = false;
        // Create K Clusters randomeley
        for (int i = 0; i < K; i++) {
            int randommedoid;
            do {
                randommedoid = (int) ThreadLocalRandom.current().nextInt(data.getInstances().size());
            } while (choosedMedoids.contains(randommedoid));

            choosedMedoids.add(randommedoid);
            clustersList.add(new Cluster(randommedoid));
        }

        while (!stopCondition) {
            System.out.println("*********New Iteration :");
            for(Cluster el:clustersList)
                System.out.println(el);
            
            SSEProgress = new LinkedHashMap<Integer, Double>();

            // Calculate Distances between every row and every cluster center
            for (int i = 0; i < data.getInstances().size(); i++) {
                generalRow row = data.getInstances().get(i);
                double distance = 0, minDist = Double.POSITIVE_INFINITY;
                Cluster choosedCluster = null;
                for (Cluster c : clustersList) {
                    if (i == c.getCenter()) {
                        continue;
                    }
                    distance = distance(data.getInstances().get(c.getCenter()), row, 1);//Math.abs(data.getInstances().get(c.getCenter()).compareTo(row));
                    if (distance < minDist) {
                        minDist = distance;
                        choosedCluster = c;
                    }

                }
                choosedCluster.getElemnts().add(i);
                if(choosedCluster==null)
                    choosedCluster=new Cluster((new Random().nextInt(data.getInstances().size())));
                choosedCluster.getElemnts().add(i);
                
                

            }

            //stopCondition = true;
            ArrayList<Cluster> duplicate = (ArrayList<Cluster>) clustersList.clone();
            int cindex = (int) ThreadLocalRandom.current().nextInt(K);
            Cluster c = clustersList.get(cindex).copy();

            int newMedoidIndex = getRandom_nonMedoid(clustersList);
            int old_center=c.getCenter();
            c.setCenter(newMedoidIndex);
            c.getElemnts().remove((Object)newMedoidIndex);
            c.getElemnts().add(old_center);
            duplicate.set(cindex, c);
            System.out.println(clustersList.indexOf(c));

            double previousTotalCost = calculateSSE(data, clustersList);
            double newTotalCost = calculateSSE(data, duplicate);

            System.out.println(previousTotalCost+"  "+newTotalCost);
            if (newTotalCost - previousTotalCost <= 0) {
                SSEProgress.put(newMedoidIndex, newTotalCost);
                clustersList = duplicate;
            } else {
                stopCondition = true;
            }

        }
        return clustersList;

    }

    
    public HashMap<String,Object> launchClustering(DataSet dataSet,int Flag) {

        HashMap<String,Object> results=new HashMap<>();
        ArrayList<Cluster> clustersList = new ArrayList<Cluster>();

        ArrayList<Integer> choosedMedoids = new ArrayList<>();
        boolean stopCondition = false;
        // Create K Clusters randomeley
        for (int i = 0; i < K; i++) {
            int randommedoid;
            do {
                randommedoid = (int) ThreadLocalRandom.current().nextInt(dataSet.getInstances().size());
            } while (choosedMedoids.contains(randommedoid));

            choosedMedoids.add(randommedoid);
            clustersList.add(new Cluster(randommedoid));
        }
        double distance = 0, minDist = Double.POSITIVE_INFINITY;
        int iteration=1;
        while (!stopCondition) {
            System.out.println("*********New Iteration :");
            for(Cluster el:clustersList)
                System.out.println(el);
            
            SSEProgress = new LinkedHashMap<Integer, Double>();

            // Calculate Distances between every row and every cluster center
            for (int i = 0; i < dataSet.getInstances().size(); i++) {
                Row row = dataSet.getInstances().get(i);
                
                Cluster choosedCluster = null;
                for (Cluster c : clustersList) {
                    if (i == c.getCenter()) {
                        continue;
                    }
                    distance = distance(dataSet.getInstances().get(c.getCenter()),row,1);//Math.abs(dataSet.getInstances().get(c.getCenter()).compareTo(row));
                    if (distance < minDist) {
                        minDist = distance;
                        choosedCluster = c;
                    }

                }
                if(choosedCluster==null)
                    choosedCluster=clustersList.get(0);
                choosedCluster.getElemnts().add(i);
                
                

            }

            //stopCondition = true;
            ArrayList<Cluster> duplicate = (ArrayList<Cluster>) clustersList.clone();
            int cindex = (int) ThreadLocalRandom.current().nextInt(K);
            Cluster c = clustersList.get(cindex).copy();

            int newMedoidIndex = getRandom_nonMedoid(clustersList);
            int old_center=c.getCenter();
            c.setCenter(newMedoidIndex);
            c.getElemnts().remove((Object)newMedoidIndex);
            c.getElemnts().add(old_center);
            duplicate.set(cindex, c);
            System.out.println(clustersList.indexOf(c));

            double previousTotalCost = calculateSSE(dataSet, clustersList);
            double newTotalCost = calculateSSE(dataSet, duplicate);

            System.out.println(previousTotalCost+"  "+newTotalCost);
            if (newTotalCost - previousTotalCost <= 0) {
                SSEProgress.put(newMedoidIndex, newTotalCost);
                clustersList = duplicate;
            } else {
                stopCondition = true;
            }
            iteration++;

        }
        double minCost = calculateSSE(dataSet, clustersList);
        results.put("Clusters",(ArrayList<Cluster>)clustersList.clone());
        results.put("Cost",minCost);
        results.put("Iterations",iteration);
        return results;

    }
    
    
    
    
    
    
    
    
    public static ArrayList<Cluster> generateRendomClusters(generalDataSet Data, int K) {

        ArrayList<Cluster> clustersList = new ArrayList<Cluster>();

        ArrayList<Integer> choosedMedoids = new ArrayList<>();
        // Create K Clusters randomeley
        for (int i = 0; i < K; i++) {
            int randommedoid;
            do {
                randommedoid = (int) ThreadLocalRandom.current().nextInt(Data.getInstances().size());
            } while (choosedMedoids.contains(randommedoid));

            choosedMedoids.add(randommedoid);
            clustersList.add(new Cluster(randommedoid));
        }

        // Calculate Distances between every row and every cluster center
        for (int i = 0; i < Data.getInstances().size(); i++) {
            generalRow row = Data.getInstances().get(i);
            double distance = 0, minDist = Double.POSITIVE_INFINITY;
            Cluster choosedCluster = null;
            for (Cluster c : clustersList) {
                if (i == c.getCenter()) {
                    continue;
                }
                distance = Math.abs(Data.getInstances().get(c.getCenter()).compareTo(row));
                if (distance < minDist) {
                    minDist = distance;
                    choosedCluster = c;
                }

            }
            choosedCluster.getElemnts().add(i);

        }

        return clustersList;

    }
    
    
    
    
    public static ArrayList<Cluster> generateRendomClusters(DataSet Data, int K) {

        ArrayList<Cluster> clustersList = new ArrayList<Cluster>();

        ArrayList<Integer> choosedMedoids = new ArrayList<>();
        // Create K Clusters randomeley
        for (int i = 0; i < K; i++) {
            int randommedoid;
            do {
                randommedoid = (int) ThreadLocalRandom.current().nextInt(Data.getInstances().size());
            } while (choosedMedoids.contains(randommedoid));

            choosedMedoids.add(randommedoid);
            clustersList.add(new Cluster(randommedoid));
        }

        // Calculate Distances between every row and every cluster center
        for (int i = 0; i < Data.getInstances().size(); i++) {
            Row row = Data.getInstances().get(i);
            double distance = 0, minDist = Double.POSITIVE_INFINITY;
            Cluster choosedCluster = null;
            for (Cluster c : clustersList) {
                
                distance = distance(Data.getInstances().get(c.getCenter()),row,1);
                if (distance < minDist) {
                    minDist = distance;
                    choosedCluster = c;
                }

            }
            choosedCluster.getElemnts().add(i);

        }

        return clustersList;

    }

    public static int getRandom_nonMedoid(ArrayList<Cluster> clustersList) {
        ArrayList<Integer> sample = new ArrayList<>();
        for (Cluster c : clustersList) {
            sample.addAll(c.getElemnts());
            //sample.remove(c.getCenter());
        }
        Collections.shuffle(sample);

        return sample.get(0);
    }

    public static double calculateSSE(generalDataSet data, ArrayList<Cluster> clusterList) {

        double SSE = 0;
        System.out.println("*********************\t\t Inter clusters Distances :************************************************");
        for (int i = 0; i < clusterList.size(); i++) {
            Cluster c = clusterList.get(i);
            double sumCluster = 0,intersum=0;
            for (int j = 0; j < c.getElemnts().size(); j++) {
                intersum+=Math.abs(distance(data.getInstances().get(c.getElemnts().get(j)), data.getInstances().get(c.getCenter()), 1));
                sumCluster += Math.abs(distance(data.getInstances().get(c.getElemnts().get(j)), data.getInstances().get(c.getCenter()), 1));
            }
            SSE += sumCluster;
            
            System.out.println("Cluster "+i+"  : "+intersum);
        
            
            
        }
        System.out.println("*********************************************************************");

        return SSE;

    }
    
    
    public static double calculateSSE(DataSet data, ArrayList<Cluster> clusterList) {

        double SSE = 0;
        System.out.println("*********************\t\t Inter clusters Distances :************************************************");
        for (int i = 0; i < clusterList.size(); i++) {
            Cluster c = clusterList.get(i);
            double sumCluster = 0,intersum=0;;
            for (int j = 0; j < c.getElemnts().size(); j++) {
                intersum+=Math.abs(distance(data.getInstances().get(c.getElemnts().get(j)), data.getInstances().get(c.getCenter()), 1));
                sumCluster += Math.abs(distance(data.getInstances().get(c.getElemnts().get(j)), data.getInstances().get(c.getCenter()), 1));
            }
            SSE += sumCluster;
                        System.out.println("Cluster "+i+"  : "+intersum);

        }
        System.out.println("*********************************************************************");

        return SSE;

    }

    public static double distance(generalRow instance1, generalRow instance2, int methode) {
        double dist = 0, dif;
        for (int i = 0; i < instance1.getValues().size(); i++) {
            try {
                dif = Math.pow(Float.parseFloat(instance1.getValues().get(i) + "") - Float.parseFloat(instance2.getValues().get(i) + ""), 2);
            } catch (NumberFormatException e) {
                dif = Math.pow(instance1.getValues().get(i).equalsIgnoreCase(instance2.getValues().get(i)) ? 0 : 1, 2);
            }
            dist += dif;
        }
        return Math.sqrt(dist);
    }
    
    
    
    public static double distance(Row instance1, Row instance2, int methode) {
        double dist = 0, dif;
        for (int i = 0; i < instance1.getValues().size(); i++) {
            try {
                dif = Math.pow(Float.parseFloat(instance1.getValues().get(i) + "") - Float.parseFloat(instance2.getValues().get(i) + ""), 2);
            } catch (NumberFormatException e) {
                dif = Math.pow(instance1.getValues().get(i).doubleValue()-instance2.getValues().get(i).doubleValue(), 2);
            }
            dist += dif;
        }
        return Math.sqrt(dist);
    }

    public boolean stopCondition(ArrayList<Cluster> clustersList) {
        return false;
    }

    //Getters and Setters
    public int getK() {
        return K;
    }

    public int getMaxItterations() {
        return maxItterations;
    }

    public void setMaxItterations(int maxItterations) {
        this.maxItterations = maxItterations;
    }

    public generalDataSet getData() {
        return data;
    }

    public void setData(generalDataSet data) {
        this.data = data;
    }

    public LinkedHashMap<Integer, Double> getSSEProgress() {
        return SSEProgress;
    }

    public void setSSEProgress(LinkedHashMap<Integer, Double> SSEProgress) {
        this.SSEProgress = SSEProgress;
    }

}
