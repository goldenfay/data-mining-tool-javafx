package model.mining.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import model.preprocessing.bases.DataSet;
import model.preprocessing.bases.Row;

/**
 * DBScan clustering algorithm implementation class
 * @author Goldenfay
 */
public class DBScan<T extends Comparable> {

    private DataSet data;
    private double epsilon;
    private int minPoints;
    
    public DBScan(DataSet data, double epsilon, int minPoints) {
        this.data = data;
        this.epsilon = epsilon;
        this.minPoints = minPoints;

    }

    public ArrayList<Integer> scan(int attrIndex) {
        ArrayList<Boolean> visitedList = new ArrayList<Boolean>();
        ArrayList<Integer> clusterMarkers = new ArrayList<>();
        ArrayList<Double> values= new ArrayList<Double>();

        for (int i = 0; i < this.data.getInstances().size(); i++) {
            visitedList.add(false);
            clusterMarkers.add(0);
            values.add(this.data.getInstances().get(i).getValues().get(attrIndex));
        }
        double[][] disMatrix=constructDistanceTable((ArrayList<T>) values,attrIndex);
        
        ArrayList<Integer> notVisited = notVisited(visitedList);
        int clustersMarker = 0;
        while (!notVisited.isEmpty()) {
            int random = new Random().nextInt(notVisited.size());
            int point = notVisited.get(random);

            visitedList.set(point, true);

            ArrayList<Integer> epsNeigh = epsilonNeighborhood(disMatrix,point);

            if (epsNeigh.size() >= this.minPoints) {
                clustersMarker++;
                clusterMarkers.set(point, clustersMarker);

                for (int i = 0; i < epsNeigh.size(); i++) {
                    int pPoint = epsNeigh.get(i);

                    if (!visitedList.get(pPoint)) {
                        ArrayList<Integer> neighborhood = epsilonNeighborhood(disMatrix,pPoint);
                        if (!neighborhood.isEmpty()) {
                            for(Integer el:neighborhood) if(!epsNeigh.contains(el)) epsNeigh.add(el);
                        }
                    }

                    if (clusterMarkers.get(pPoint) == 0) {
                        clusterMarkers.set(pPoint, clustersMarker);
                    }
                }

            }
            notVisited = notVisited(visitedList);

        }
        
        
        return clusterMarkers;

    }

    public ArrayList<Integer> scan() {

        ArrayList<Boolean> visitedList = new ArrayList<Boolean>();
        ArrayList<Integer> clusterMarkers = new ArrayList<>();

        for (int i = 0; i < this.data.getInstances().size(); i++) {
            visitedList.add(false);
            clusterMarkers.add(0);
        }
        double[][] disMatrix=constructDistanceTable((ArrayList<T>) this.data.getInstances(), -1);
        ArrayList<Integer> notVisited = notVisited(visitedList);
        int clustersMarker = 0;
        while (!notVisited.isEmpty()) {
            int random = new Random().nextInt(notVisited.size());
            int point = notVisited.get(random);

            visitedList.set(point, true);

            ArrayList<Integer> epsNeigh = epsilonNeighborhood(disMatrix,point);

            if (epsNeigh.size() >= this.minPoints) {
                clustersMarker++;
                clusterMarkers.set(point, clustersMarker);

                for (int i = 0; i < epsNeigh.size(); i++) {
                    int pPoint = epsNeigh.get(i);

                    if (!visitedList.get(pPoint)) {
                        ArrayList<Integer> neighborhood = epsilonNeighborhood(disMatrix,pPoint);
                        if (!neighborhood.isEmpty()) {
                            for(Integer el:neighborhood) if(!epsNeigh.contains(el)) epsNeigh.add(el);
                        }
                    }

                    if (clusterMarkers.get(pPoint) == 0) {
                        clusterMarkers.set(pPoint, clustersMarker);
                    }
                }

            }
            notVisited = notVisited(visitedList);

        }

        return clusterMarkers;

    }

    public double[][] constructDistanceTable(ArrayList<T> points,int attrIndex) {

        double[][] distanceMatrix = new double[points.size()][points.size()];

        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < i; j++) {
                double distance=0;
                if (points.get(i) instanceof Row) { // instances difference
                    if (distanceMatrix[i][j] != 0) {
                        distance=Math.abs(points.get(i).compareTo(j));
                        
                    }
                }else { // Attributs difference
                    
                    distance=Math.abs(this.data.calculateDistance(attrIndex, i, j));
                }
                
                distanceMatrix[i][j]=distanceMatrix[j][i]=distance;
                distanceMatrix[i][i]=0;

            }
        }

        
        return distanceMatrix;
    }

    public ArrayList<Integer> notVisited(ArrayList<Boolean> visitedList) {
        ArrayList<Integer> notvisited = new ArrayList<>();

        for (int i = 0; i < visitedList.size(); i++) {
            if (!visitedList.get(i)) {
                notvisited.add(i);
            }
        }
        return notvisited;
    }

    public ArrayList<Integer> epsilonNeighborhood(double[][] distanceMatrix,int point) {

        ArrayList<Integer> neighborhood=new ArrayList<Integer>();
        
        
        for(int i=0;i<distanceMatrix[0].length;i++){
            double distance = distanceMatrix[point][i];
            if(distance<=this.epsilon)
                neighborhood.add(i);
                
        }
        
        return neighborhood;
    }

    // Getters & Setters
    public DataSet getData() {
        return data;
    }

    public void setData(DataSet data) {
        this.data = data;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public void setMinPoints(int minPoints) {
        this.minPoints = minPoints;
    }

}
