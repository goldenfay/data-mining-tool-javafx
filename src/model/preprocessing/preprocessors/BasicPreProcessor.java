package model.preprocessing.preprocessors;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import jdk.nashorn.internal.runtime.regexp.RegExp;
import model.preprocessing.bases.DataSet;
import model.preprocessing.bases.Row;
import static model.preprocessing.preprocessors.Measurement.calculateMod;

/**
 *
 * @author Goldenfay
 */
public class BasicPreProcessor implements Preprocessor {

    public BasicPreProcessor() {

    }

    /**
     * Construction du Dataset depuis un fichier input(.arff).
     *
     * @param dataSetPath
     * @return un DataSet contenant les instances du fier .arff.
     */
    public static DataSet loadDataSetFrom(String dataSetPath) throws IOException {
        DataSet dataSet = new DataSet();
        try {
            InputStream inStream = new FileInputStream(dataSetPath);
            InputStreamReader inStreamReader = new InputStreamReader(inStream);
            BufferedReader br = new BufferedReader(inStreamReader);
            String line = br.readLine();
            StringBuilder desc = new StringBuilder();
            int i;
            Pattern pattern = Pattern.compile("(([0-9])+,)+");
            while (((line = br.readLine()) != null) && (!line.startsWith("@data")) && (!line.contains("Attribute Information"))) {//&& (!pattern.matcher(line).find())) {
            }

            if (line.contains("Attribute Information")) {

                while (((line = br.readLine()) != null) && (!line.contains("@relation"))) {
                    
                        
                    desc.append(line.replace("%", "").replace("-", "") + "\n");

                }

            }

            

            if (line.contains("@relation")) {
                dataSet.setName(line.replace("@relation ", ""));
                System.out.println(" name : "+dataSet.getName());
                dataSet.setDescription(desc);
                while (((line = br.readLine()) != null) && (!line.startsWith("@data"))) {
                    String[] entities = line.split(" ");
                    dataSet.getAttributsName().add(entities[1]);
                    dataSet.getAttributsType().add(entities[2]);
                }
                dataSet.updateAttributsCategories();

            }

            line = br.readLine();
            if (line != null) {
                while (line != null) {
                    String[] TabL = line.split(",");
                    Row tempRow = new Row();
                    for (String s : TabL) {
                        if (s.equals("absent")) {
                            tempRow.getValues().add((Double) 1.0);
                        } else {
                            if (s.equals("present")) {
                                tempRow.getValues().add((Double) 2.0);
                            } else {
                                tempRow.getValues().add(Double.parseDouble((s.trim())));
                            }
                        }
                    }
                    dataSet.getInstances().add(tempRow);
                    line = br.readLine();

                }
                
            }
            br.close();
        } catch (Exception e) {
            if (e instanceof PatternSyntaxException) {
                System.out.println("Erro on compiling regular expression, pattern may be invalide \n");
            }

            System.out.println(e.toString());
        }

        System.out.println("ICI " + dataSet.getInstances().size());
        return dataSet;
    }

    @Override
    public HashMap<String, Object> simpleDataAnalysis(DataSet Data, int attrIndex) {

        HashMap<String, Double> fiveNSummary = Measurement.fiveNumberSummary(Data, attrIndex);
        HashMap<String, Object> results = new HashMap<String, Object>();
        System.out.println("Hole : " + fiveNSummary.get("Q1"));

        results.put("FiveNumberSummary", fiveNSummary);
        results.put("Mode", calculateMod(Data, attrIndex));
        results.put("Average", Measurement.calculateAverage(Data, attrIndex));

        results.put("Values", Data.getValuesOf(attrIndex));

        return results;
    }

    @Override
    public DataSet cleanAttributs(DataSet Data, ArrayList<Integer> attrIndexes) {

        return Data;
    }
}
