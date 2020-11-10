
package model.preprocessing.preprocessors;

import java.util.ArrayList;
import java.util.HashMap;
import model.preprocessing.bases.DataSet;

/**
 * A class to implement Dataset preprocessoring manager
 * @author Goldenfay
 */
public interface Preprocessor {
    
    public HashMap<String,Object> simpleDataAnalysis(DataSet Data, int attrIndex);
    
    
    public DataSet cleanAttributs(DataSet Data,ArrayList<Integer> attrIndexes);
}
