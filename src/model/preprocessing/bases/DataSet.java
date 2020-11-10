package model.preprocessing.bases;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.temporal.TemporalQueries;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implémentation du DataSet : un ensemble de
 * tuples(instances,lignes,enregistrements)
 *
 * @author Goldenfay
 */
public class DataSet implements Serializable{
    private String name;
    ArrayList<Row> instances = new ArrayList<Row>();
    ArrayList<String> attributsName = new ArrayList<String>();
    ArrayList<String> attributsType = new ArrayList<String>();
    ArrayList<String> attributsCategory = new ArrayList<String>();

    StringBuilder description = null;
    
    HashMap<Integer, ArrayList<HashMap<String,String>>> discritizeTable=null;

    public DataSet() {
        if (instances == null) {
            instances = new ArrayList<Row>();
        }

        if (attributsName == null) {
            attributsName = new ArrayList<String>();
        }

        if (attributsCategory == null) {
            attributsCategory = new ArrayList<String>();
        }

        if (attributsType == null) {
            attributsType = new ArrayList<String>();
        }

    }

    public void updateAttributsCategories() {
        for (int i = 0; i < attributsName.size(); i++) {
            attributsCategory.add("unknown");
        }

        String[] lines = description.toString().split("\n");
        for (int i = 0; i < lines.length; i++) {

            String line = lines[i];

            if (line.contains("Real:")) {

                Matcher m = Pattern.compile("[0-9]+")
                        .matcher(line);
                while (m.find()) {

                    attributsCategory.set(Integer.parseInt(m.group().trim()) - 1, "Real");
                }
            }

            if (line.contains("Ordered:")) {

                Matcher m = Pattern.compile("[0-9]+")
                        .matcher(line);
                while (m.find()) {
                    attributsCategory.set(Integer.parseInt(m.group()) - 1, "Ordered");
                }

            }

            if (line.contains("Binary:")) {

                Matcher m = Pattern.compile("[0-9]+")
                        .matcher(line);
                while (m.find()) {
                    attributsCategory.set(Integer.parseInt(m.group()) - 1, "Binary");
                }
            }
            if (line.contains("Nominal:")) {

                Matcher m = Pattern.compile("[0-9]+")
                        .matcher(line);
                while (m.find()) {
                    attributsCategory.set(Integer.parseInt(m.group()) - 1, "Nominal");
                }
            }

        }

    }

    public ArrayList<Double> getValuesOf(int attIndex) {
        ArrayList<Double> column = new ArrayList<Double>();
        for (Row row : this.getInstances()) {
            column.add(row.getValues().get(attIndex));
        }

        return column;

    }

    public double calculateDistance(int attIndex, int index1, int index2) {

        if (isNominal(attIndex)) {
            return this.getInstances().get(index1).getValues().get(attIndex) == this.getInstances().get(index2).getValues().get(attIndex) ? 0 : 1;
        } else {
            return Math.abs(this.getInstances().get(index1).getValues().get(attIndex) - this.getInstances().get(index2).getValues().get(attIndex));
        }

    }

    /*
    public double calculateMode(int attIndex){
        HashMap<Double,Integer> frequencies=new HashMap<Double,Integer>();
        double maxKey=0;
        for(Row row:this.getInstances()){
            Double value = row.getValues().get(attIndex);
            frequencies.put(value,frequencies.keySet().contains(value)?frequencies.get(value)+1:1);
            if(frequencies.get(maxKey)<frequencies.get(value))
                maxKey=value;
        }
        
        return maxKey;
    }
     */
    public boolean isNumeric(int attrIndex) {
        return this.attributsCategory.get(attrIndex).equalsIgnoreCase("Real");
    }

    public boolean isNominal(int attrIndex) {
        return this.attributsCategory.get(attrIndex).equalsIgnoreCase("Nominal") || this.attributsCategory.get(attrIndex).equalsIgnoreCase("Binary");
    }

    public boolean isOrdered(int attrIndex) {
        return this.attributsCategory.get(attrIndex).equalsIgnoreCase("Ordered");
    }

    public DataSet descriticize() {
        int N = 5;
        DataSet descritisized = new DataSet();
        descritisized.setDescription(description);
        descritisized.setAttributsName(attributsName);
        descritisized.setAttributsCategory(attributsCategory);
        descritisized.setAttributsType(attributsType);

        for (int i = 0; i < this.instances.size(); i++) {
            descritisized.getInstances().add(this.instances.get(i));

        }
        for (int j = 0; j < this.attributsName.size(); j++) {
            if (isNumeric(j)) {
                ArrayList<Double> values = new ArrayList<>();
                for (int i = 0; i < this.instances.size(); i++) {
                    values.add(this.instances.get(i).getValues().get(j));
                }

                double max = (double) Collections.max(values);
                double min = (double) Collections.min(values);
                int intervalWidth = (int) Math.floor((max - min) / N);
                intervalWidth = intervalWidth == 0 ? 1 : intervalWidth;
                System.out.println(min + "   " + max + "   " + intervalWidth);

                for (int i = 0; i < this.instances.size(); i++) {
                    int newValues = (int) (descritisized.getInstances().get(i).getValues().get(j) - min) / intervalWidth;
                    descritisized.getInstances().get(i).getValues().set(j, (double) newValues);
                }

            }

        }

        return descritisized;
    }

    public DataSet descriticize(int attrIndex, int N, int methode) {
        
        DataSet descritisized = new DataSet();
        descritisized.setDescription(description);
        descritisized.setAttributsName(attributsName);
        descritisized.setAttributsCategory(attributsCategory);
        descritisized.setAttributsType(attributsType);
        if(this.getDiscritizeTable()==null)
            descritisized.setDiscritizeTable(new HashMap<>());
        else
            descritisized.setDiscritizeTable(this.getDiscritizeTable());

        for (int i = 0; i < this.instances.size(); i++) {
            descritisized.getInstances().add(this.instances.get(i));

        }
        ArrayList<Double> values = new ArrayList<>();
        for (int i = 0; i < this.instances.size(); i++) {
            values.add(this.instances.get(i).getValues().get(attrIndex));
        }
        
        double max = (double) Collections.max(values);
        double min = (double) Collections.min(values);
        double intervalWidth = (double)((max - min) / N);
        intervalWidth = intervalWidth == 0 ? 1 : intervalWidth;
        System.out.println("Interval width : "+intervalWidth+"  Min : "+min+"  Max"+max);
        
        if (methode == 1) { // Equal Width
            if (isNumeric(attrIndex)) {
                
                ArrayList<HashMap<String, String>> list=new ArrayList<>();
                
                
                int j=0;
                for(double i=min;i<max && j<N;i+=intervalWidth){
                   
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Range","["+String.format("%.2f",i)+", "+String.format("%.2f",i+intervalWidth)+"]");
                    map.put("Name",this.attributsName.get(attrIndex)+"_range"+(j));
                    list.add(j, map);
                    j++;
                }
                descritisized.getDiscritizeTable().put(attrIndex, list);
               

                for (int i = 0; i < this.instances.size(); i++) {
                    double range = this.getInstances().get(i).getValues().get(attrIndex) - min;
                    int newValues = (int) ( range / intervalWidth);
                    if(newValues>=N) newValues=N-1;
                    descritisized.getInstances().get(i).getValues().set(attrIndex, (double) newValues);
                    System.out.print("range "+range+"min="+min+" "+( (this.getInstances().get(i).getValues().get(attrIndex) - min) / intervalWidth));
                }

            }
        }
        if (methode == 2) {  // Equal Depth

            if (isNumeric(attrIndex)) {

                ArrayList<HashMap<String, String>> list=new ArrayList<>();
                int intervalDepth = (int)( values.size() / N);
                intervalDepth = intervalDepth == 0 ? 1 : intervalDepth;
                
                Collections.sort(values);
                int[] markers = new int[this.instances.size()];
                double[] delimiters = new double[N+1];
                int[] cardinal = new int[N+1];
                for(int i=0;i<=N;i++){
                    delimiters[i]=i==N-1?max:min;
                    cardinal[i]=0;
                }
                int k=0;
                for (int i = 0; i < this.instances.size(); i++) {
                    double val = descritisized.getInstances().get(i).getValues().get(attrIndex);
                    int intervalIndex=(int)( val - min) / (int)intervalWidth;
                    while(cardinal[intervalIndex]>=intervalDepth && intervalIndex<N)
                        intervalIndex++;
                    
                    if(intervalIndex>=N)
                        intervalIndex=N-1;
                    
                    cardinal[intervalIndex]++;
                    markers[i]=intervalIndex;
                    if(val>delimiters[intervalIndex])
                        delimiters[intervalIndex]=val;
                }
                for(int i=0;i<N;i++)
                    System.out.println("Interval :"+i+delimiters[i]);
                
                int j=1;
                for(int i=0;i<delimiters.length;i++){
                   
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Range","["+String.format("%.2f",(i==0?min:delimiters[i-1]))+", "+String.format("%.2f",delimiters[i])+"]");
                    map.put("Name",this.attributsName.get(attrIndex)+"_range"+j);
                    list.add(j-1, map);
                    j++;
                }
                descritisized.getDiscritizeTable().put(attrIndex, list);
                System.out.println(min + "   " + max + "   " + intervalWidth);

                for (int i = 0; i < this.instances.size(); i++) {
                    int newValues = (int)( (descritisized.getInstances().get(i).getValues().get(attrIndex)) - min) / (int)intervalWidth;
                    descritisized.getInstances().get(i).getValues().set(attrIndex, (double) newValues);
                }

            }
        }
        
        if (methode == 3) {  // Mean

            if (isNumeric(attrIndex)) {

                double[] averages = new double[N];
                int intervalDepth = (int)( values.size() / N);
                intervalDepth = intervalDepth == 0 ? 1 : intervalDepth;
                

                for (int i = 0; i < this.instances.size(); i++) {
                    int newValues = (int)( (descritisized.getInstances().get(i).getValues().get(attrIndex)) - min) / (int)intervalWidth;
                    descritisized.getInstances().get(i).getValues().set(attrIndex, (double) newValues);
                }

            }
        }
        
        
        

        return descritisized;
    }
    
    
    public generalDataSet transform(){
        
        generalDataSet generated=new generalDataSet();
        generated.setDescription(description);
        generated.setAttributsName(attributsName);
        generated.setAttributsCategory(attributsCategory);
        generated.setAttributsType(attributsType);
        generated.setDiscritizeTable(new HashMap<>());

        for (int i = 0; i < this.instances.size(); i++) {
            generalRow row=new generalRow();
            for(int j=0;j<this.attributsName.size();j++){
                if(this.discritizeTable.containsKey(j)){
                    int rangeindex=(int)((double)this.instances.get(i).getValues().get(j));
                    row.getValues().add(this.discritizeTable.get(j).get(rangeindex).get("Name"));
                    
                }
                else{
                    row.getValues().add(this.instances.get(i).getValues().get(j)+"");
                }
            }
            generated.getInstances().add(row);

        }
        
        return generated;
    }

    //Getters & Setters
    public ArrayList<Row> getInstances() {
        return instances;
    }

    public void setInstances(ArrayList<Row> instances) {
        this.instances = instances;
    }

    public ArrayList<String> getAttributsName() {
        return attributsName;
    }

    public void setAttributsName(ArrayList<String> attributsName) {
        this.attributsName = attributsName;
    }

    public StringBuilder getDescription() {
        return description;
    }

    public void setDescription(StringBuilder description) {
        this.description = description;
    }

    public ArrayList<String> getAttributsType() {
        return attributsType;
    }

    public void setAttributsType(ArrayList<String> attributsType) {
        this.attributsType = attributsType;
    }

    public ArrayList<String> getAttributsCategory() {
        return attributsCategory;
    }

    public void setAttributsCategory(ArrayList<String> attributsCategory) {
        this.attributsCategory = attributsCategory;
    }

    public HashMap<Integer, ArrayList<HashMap<String, String>>> getDiscritizeTable() {
        return discritizeTable;
    }

    public void setDiscritizeTable(HashMap<Integer, ArrayList<HashMap<String, String>>> discritizeTable) {
        this.discritizeTable = discritizeTable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    
    
    
    
    

}
