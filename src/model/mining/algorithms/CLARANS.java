package model.mining.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import static model.mining.algorithms.KMedoids.calculateSSE;
import static model.mining.algorithms.KMedoids.getRandom_nonMedoid;
import model.mining.bases.Cluster;
import model.preprocessing.bases.DataSet;
import model.preprocessing.bases.generalDataSet;
import model.preprocessing.bases.generalRow;

/**
 * CLARANS clustering algorithm implementation class
 * @author Goldenfay
 */
public class CLARANS {

    private int maxNeighbors;
    private int numlocal;
    private generalDataSet Data;

    public CLARANS(generalDataSet Data, int maxNeighbors, int numlocal) {
        this.maxNeighbors = maxNeighbors;
        this.numlocal = numlocal;
        this.Data = Data;
    }

    public HashMap<String, Object> cluster(int K) {

        HashMap<String, Object> result = new HashMap<>();
        double minCost = Double.POSITIVE_INFINITY;
        int current = -1;
        int bestNode = -1;
        StringBuilder log_string=new StringBuilder("");

        // Selecting K random medoids and affect each node to its nearest cluster
        ArrayList<Cluster> clustersList = KMedoids.generateRendomClusters(Data, K);

        for (int i = 0; i < numlocal; i++) {

            int random = (int) ThreadLocalRandom.current().nextInt(clustersList.size());
            current = clustersList.get(random).getCenter();

            int J = 1;

            while (J < this.maxNeighbors) {

                //int S = KMedoids.getRandom_nonMedoid(clustersList);
                //generalRow nonMedoid = Data.getInstances().get(S);

                ArrayList<Cluster> duplicate = (ArrayList<Cluster>) clustersList.clone();
                int cindex = (int) ThreadLocalRandom.current().nextInt(K);
                Cluster c = clustersList.get(cindex).copy();

                int S = getRandom_nonMedoid(clustersList);
                int old_center = c.getCenter();
                c.setCenter(S);
                c.getElemnts().remove((Object) S);
                c.getElemnts().add(old_center);
                duplicate.set(cindex, c);
                

                double previousTotalCost = calculateSSE(Data, clustersList);
                double newTotalCost = calculateSSE(Data, duplicate);

                System.out.println(previousTotalCost+" VS "+newTotalCost);
                if(previousTotalCost<minCost){
                    minCost=previousTotalCost;
                    bestNode = current;
                        log_string.append("Replacing instance n° "
                                +old_center+" by instance n° "+S+" \t "+"No improvement \n");
                }
                    
                if (newTotalCost < previousTotalCost) {
                    current = S;
                    clustersList=duplicate;
                    if (newTotalCost < minCost) {
                        minCost = newTotalCost;
                        bestNode = current;
                        log_string.append("Replacing instance n° "
                                +old_center+" by instance n° "+S+" \t MinCost improved from "
                                +previousTotalCost+" to "+newTotalCost+" \n");
                        
                        
                    }
                } else {
                    J++;

                }

            }
        }

        if (bestNode == -1) {
            result.put("BestNode", null);
        } else {
            result.put("BestNode", Data.getInstances().get(bestNode));
        }

        result.put("BestIndex", bestNode);
        result.put("MinCost", minCost);
        result.put("ClusterList",clustersList);
        result.put("Log", log_string.toString());
        return result;
    }
    
    
    
    
    
    
    public HashMap<String, Object> cluster(DataSet Data,int K) {

        HashMap<String, Object> result = new HashMap<>();
        double minCost = Double.POSITIVE_INFINITY;
        int current = -1;
        int bestNode = -1;
        StringBuilder log_string=new StringBuilder("");

        // Selecting K random medoids and affect each node to its nearest cluster
        ArrayList<Cluster> clustersList = KMedoids.generateRendomClusters(Data, K);
        int iter=0;
        for (int i = 0; i < numlocal; i++) {

            int random = (int) ThreadLocalRandom.current().nextInt(clustersList.size());
            current = clustersList.get(random).getCenter();

            int J = 1;

            while (J < this.maxNeighbors) {
                iter++;

                //int S = KMedoids.getRandom_nonMedoid(clustersList);
                //generalRow nonMedoid = Data.getInstances().get(S);

                ArrayList<Cluster> duplicate = (ArrayList<Cluster>) clustersList.clone();
                int cindex = (int) ThreadLocalRandom.current().nextInt(K);
                Cluster c = clustersList.get(cindex).copy();

                int S = getRandom_nonMedoid(clustersList);
                int old_center = c.getCenter();
                c.setCenter(S);
                c.getElemnts().remove((Object) S);
                c.getElemnts().add(old_center);
                duplicate.set(cindex, c);
                

                double previousTotalCost = calculateSSE(Data, clustersList);
                double newTotalCost = calculateSSE(Data, duplicate);

                System.out.println(previousTotalCost+" VS "+newTotalCost);
                if(previousTotalCost<minCost){
                    minCost=previousTotalCost;
                    bestNode = current;
                        log_string.append("Replacing instance n° "
                                +old_center+" by instance n° "+S+" \t "+"No improvement \n");
                }
                    
                if (newTotalCost < previousTotalCost) {
                    current = S;
                    clustersList=duplicate;
                    if (newTotalCost < minCost) {
                        minCost = newTotalCost;
                        bestNode = current;
                        log_string.append("Replacing instance n° "
                                +old_center+" by instance n° "+S+" \t MinCost improved from "
                                +previousTotalCost+" to "+newTotalCost+" \n");
                        
                        
                    }
                } else {
                    J++;

                }

            }
        }

        if (bestNode == -1) {
            result.put("BestNode", null);
        } else {
            result.put("BestNode", Data.getInstances().get(bestNode));
        }

        result.put("BestIndex", bestNode);
        result.put("MinCost", minCost);
        result.put("ClusterList",clustersList);
        result.put("Iterations", iter);
        result.put("Log", log_string.toString());
        return result;
    }

}
