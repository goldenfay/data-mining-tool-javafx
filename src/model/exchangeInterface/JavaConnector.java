
package model.exchangeInterface;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import model.preprocessing.bases.DataSet;
import model.preprocessing.bases.Row;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Java actions handler class to perform with Webview interface.
 * @author Goldenfay
 */
public class JavaConnector {
    
    private JSONObject JObject=new JSONObject();
    private DataSet dataSet;
    private HashMap<String,Object> resultsBundle;
    
    
    
    public JSONObject toJSonObject(HashMap map) throws JSONException{
        String tempJobjString=stringifyArray((ArrayList<Double>)map.get("Values"));
        this.JObject=new JSONObject();
        this.JObject.put("FiveNumberSummary",map.get("FiveNumberSummary"));
        this.JObject.put("Values",tempJobjString);
        return JObject;
    }
    
    public String stringifyObject(){
        return JObject.toString();
    }
    
    
    private String stringifyArray(ArrayList<Double> listValues){
        String jsonFormat="[";
        Collections.sort(listValues);
        for(int i=0;i<listValues.size();i++)
            jsonFormat+=listValues.get(i).doubleValue()+(i!=listValues.size()-1?",":"]");
        return jsonFormat;
        
    }
    
    
    public String stringifyFiveNumberSummary() throws JSONException{
        
       // return stringifyArray(this.JObject.get("F"))
       return new Gson().toJson((HashMap)JObject.get("FiveNumberSummary"));
    }
    
    public String stringifyAllResults(){
        HashMap<String,Object> duplicate=(HashMap<String,Object>) resultsBundle.clone();
        try{
            duplicate.put("SucessFlag","Sucess");
            String jsonString= new Gson().toJson(duplicate);
            return jsonString;
        }catch(Exception Je){
            
           duplicate.clear();
           duplicate.put("SucessFlag","Fail");
           return new Gson().toJson(duplicate);
        }
    }
    
    
    public String exportDataset(){
        
        ArrayList<HashMap<String,Double>> dataSet=new ArrayList<>();
        for(int i=0;i<this.dataSet.getInstances().size();i++){
            HashMap<String,Double> row=new HashMap<>();
            for(int j=0;j<this.dataSet.getInstances().get(i).getValues().size();j++){
                row.put(this.dataSet.getAttributsName().get(j), this.dataSet.getInstances().get(i).getValues().get(j));
            }
            dataSet.add(row);
        }
        
        HashMap<String,Object> exportDataSet=new HashMap<>();
        exportDataSet.put("Relation",dataSet);
            // Inclure la liste des attributs niminaux
        ArrayList<String> nominalAttributs=new ArrayList<>();
        for(int j=0;j<this.dataSet.getAttributsCategory().size();j++)
            if(!this.dataSet.isNumeric(j)) nominalAttributs.add(this.dataSet.getAttributsName().get(j));
        exportDataSet.put("NominalAttributs", nominalAttributs);        
        
        return new Gson().toJson(exportDataSet);
    }
    
    
    
    
    
    
            // Getters and Setters
    public JSONObject getJObject() {
        return JObject;
    }

    public void setJObject(JSONObject JObject) {
        this.JObject = JObject;
    }

    public HashMap<String, Object> getResultsBundle() {
        return resultsBundle;
    }

    public void setResultsBundle(HashMap<String, Object> resultsBundle) {
        this.resultsBundle = resultsBundle;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }
    
    
    
    
    
    
    
    
    
}
