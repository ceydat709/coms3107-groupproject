package phillydata.processing;
import java.util.List;

import phillydata.common.Property;
import phillydata.data.PropertyReader;

public class AvgMarketValueProcessor {
    private PropertyReader propertyReader;
    private List<Property> properties;
    public AvgMarketValueProcessor(PropertyReader propertyR, String filename){
        if (propertyR==null || filename==null){
            throw new IllegalArgumentException();
        }
        this.propertyReader = propertyR;
        this.properties = propertyReader.getProperties(filename);
        if (properties==null){
            throw new IllegalStateException();
        }
    }

    //check in UI to see if the zipCode is valid (ie not 0)
    public int averageMarketValue(int zipCode){
        double total = 0;
        int count = 0;
        for (Property p: properties){
            if (p.getZip_code() == zipCode && p.getZip_code() != -1 && p.getMarket_value() != -1){
                total += p.getMarket_value();
                count++;
            }
        }
        if (count == 0){
            return 0;
        }
        int roundedTotal = (int) Math.round(total / count);
        return roundedTotal;
    }
}