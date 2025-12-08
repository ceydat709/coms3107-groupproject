package phillydata.data;
import phillydata.common.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

//implements singleton
public class PropertyReader {
    private static PropertyReader instance;

    private PropertyReader(){}

    public static PropertyReader getInstance(){
        if (instance==null){
            instance = new PropertyReader();
        }
        return instance;
    }

    public List<Property> getProperties(String filename){
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){

            String[] headers = br.readLine().split(",");
            HashMap<String, Integer> indices = new HashMap<>();

            //find indices of headers in first row
            indices.put("total_livable_area",Arrays.asList(headers).indexOf("total_livable_area"));
            indices.put("market_value",Arrays.asList(headers).indexOf("market_value"));
            indices.put("zip_code",Arrays.asList(headers).indexOf("zip_code"));

            List<Property> properties = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null){
                String[] values = line.split(",",-1);
                double marketValue,totalLivableArea;
                int zipCode;
                if (validValue(values[indices.get("market_value")])){
                    marketValue = Double.parseDouble(values[indices.get("market_value")]);
                }
                else{ marketValue = -1; }

                if (validValue(values[indices.get("total_livable_area")])){
                    totalLivableArea = Double.parseDouble(values[indices.get("total_livable_area")]);
                }
                else{ totalLivableArea = -1; }
                
                if (validValue(values[indices.get("zip_code")].substring(0,5))){
                    zipCode = Integer.parseInt(values[indices.get("zip_code")].substring(0,5));
                }
                else{ zipCode = -1; }

                Property p = new Property(marketValue,totalLivableArea,zipCode);
                properties.add(p);
            }
            return properties;
        }
        catch(IOException e){
            System.err.println(e.getMessage());
            return null;
        }

    }
    private boolean validValue(String value){
        if(value==null || value.isEmpty()){
            return false;
        }
        try{
            double valueDouble = Double.parseDouble(value);
            return valueDouble > 0;
        } catch (NumberFormatException e){
            return false;
        }
    }
}
