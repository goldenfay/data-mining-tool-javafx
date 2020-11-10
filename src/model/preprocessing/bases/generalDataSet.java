
package model.preprocessing.bases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Goldenfay
 */
public class generalDataSet  implements Serializable{
    
    
    
    ArrayList<generalRow> instances = new ArrayList<generalRow>();
    ArrayList<String> attributsName = new ArrayList<String>();
    ArrayList<String> attributsType = new ArrayList<String>();
    ArrayList<String> attributsCategory = new ArrayList<String>();

    StringBuilder description = null;
    
    HashMap<Integer, HashMap<String,String>> discritizeTable=null;

    public generalDataSet() {
        if (instances == null) {
            instances = new ArrayList<generalRow>();
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

    public ArrayList<Object> getValuesOf(int attIndex) {
        ArrayList<Object> column = new ArrayList<Object>();
        for (generalRow row : this.getInstances()) {
            column.add(row.getValues().get(attIndex));
        }

        return column;

    }

    public double calculateDistance(int attIndex, int index1, int index2) {

        if (isNominal(attIndex)) {
            return this.getInstances().get(index1).getValues().get(attIndex).equals(this.getInstances().get(index2).getValues().get(attIndex)) ? 0 : 1;
        } else {
            return Math.abs(this.getInstances().get(index1).getValues().get(attIndex).compareTo(this.getInstances().get(index2).getValues().get(attIndex)));
        }

    }

    /*
    public double calculateMode(int attIndex){
        HashMap<Object,Integer> frequencies=new HashMap<Object,Integer>();
        double maxKey=0;
        for(generalRow row:this.getInstances()){
            Object value = row.getValues().get(attIndex);
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

    

    
    
    
    
    
    
    
    
    
    
    
    
    
                // Getters and setters

    public ArrayList<generalRow> getInstances() {
        return instances;
    }

    public void setInstances(ArrayList<generalRow> instances) {
        this.instances = instances;
    }

    public ArrayList<String> getAttributsName() {
        return attributsName;
    }

    public void setAttributsName(ArrayList<String> attributsName) {
        this.attributsName = attributsName;
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

    public StringBuilder getDescription() {
        return description;
    }

    public void setDescription(StringBuilder description) {
        this.description = description;
    }

    public HashMap<Integer, HashMap<String, String>> getDiscritizeTable() {
        return discritizeTable;
    }

    public void setDiscritizeTable(HashMap<Integer, HashMap<String, String>> discritizeTable) {
        this.discritizeTable = discritizeTable;
    }
    
    
    
}
