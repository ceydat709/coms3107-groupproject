package phillydata.processing;

import phillydata.common.Property;
import phillydata.common.Population;
import phillydata.data.PropertyReader;
import phillydata.data.PopulationReader;
import java.util.*;

public class MarketValuePerCapitalProcessor {
    private PropertyReader propertyReader;
    private PopulationReader populationReader;
    private List<Property> properties;
    private List<Population> populations;
    private Map<String, Integer> memo = new HashMap<>();

    public MarketValuePerCapitalProcessor(PropertyReader propertyR, String propertiesFile, PopulationReader populationR, String populationFile) {
        if (propertyR == null || propertiesFile == null || populationR == null || populationFile == null) {
            throw new IllegalArgumentException();
        }
        this.propertyReader = propertyR;
        this.populationReader = populationR;
        this.properties = propertyReader.getProperties(propertiesFile);
        this.populations = populationReader.getPopulations(populationFile);
        if (properties == null || populations == null) {
            throw new IllegalStateException();
        }
    }

    public int marketValuePerCapita(String zipCode) {
        if (zipCode == null) {
            throw new IllegalArgumentException();
        }

        if (memo.containsKey(zipCode)) {
            return memo.get(zipCode);
        }

        int value = calculateMarketValuePerCapita(zipCode);
        memo.put(zipCode, value);
        return value;
    }

    private int calculateMarketValuePerCapita(String zipCode) {
        int population = 0;
        for (Population p : populations) {
            String popZip = "" + p.getZip_code();
            if (popZip.equals(zipCode)) {
                population = p.getPopulation();
                break;
            }
        }
        if (population == 0) {
            return 0;
        }
        double sum = 0.0;

        for (Property prop : properties) {
            String propZip = "" + prop.getZip_code();
            if (!propZip.equals(zipCode)) continue;
            double marketVal = prop.getMarket_value();

            if (marketVal <= 0) continue;
            sum += marketVal;
        }

        if (sum == 0.0) {
            return 0;
        }
        return (int) Math.round(sum/population);
    }
}