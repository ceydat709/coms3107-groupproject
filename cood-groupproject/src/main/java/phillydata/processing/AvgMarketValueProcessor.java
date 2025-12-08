package phillydata.processing;
import phillydata.data.PropertyReader;
import phillydata.common.Property;

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
            if (p.getZipCode() == zipCode && p.getZipCode() != -1 && p.getMarketValue() != -1){
                total += p.getMarketValue();
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