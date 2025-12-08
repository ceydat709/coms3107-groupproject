package phillydata.processing;

import phillydata.common.Property;
import java.util.*;

public class AvgLivableAreaProcessor {
    private List<Property> properties;
    private Map<String, Integer> memo = new HashMap<>();

    public AvgLivableAreaProcessor(List<Property> properties) {
        if (properties == null) {
            throw new IllegalArgumentException();
        }
        this.properties = properties;
    }

    public int averageLivableArea(String zipCode) {
        if (zipCode == null) {
            throw new IllegalArgumentException();
        }
        //use cached result if we already computed this zip
        if (memo.containsKey(zipCode)) {
            return memo.get(zipCode);
        }
        int result = calculateAverageLivableArea(zipCode);
        memo.put(zipCode, result);
        return result;
    }

    private int calculateAverageLivableArea(String zipCode) {
        int numHomes = 0;
        double sum = 0.0;
        for (Property p : properties) {
            String propZip = "" + p.getZip_code();
            if(!propZip.equals(zipCode)) {
                continue;
            }
            double area = p.getTotal_livable_area();
            if(area <= 0) continue;

            sum += area;
            numHomes++;
        }
        int avgLivableArea = 0;
        if(numHomes != 0) {
            avgLivableArea = (int) Math.round(sum / numHomes);
        }
        return avgLivableArea;
    }
}