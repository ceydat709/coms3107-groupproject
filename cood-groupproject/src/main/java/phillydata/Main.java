package phillydata;

import phillydata.ui.UI;
import phillydata.data.*;
import phillydata.common.*;
import phillydata.processing.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: phillydata.Main <csv or json> <parking file> <properties file> <population file>");
            return;
        }
        String fileFormat = args[0];
        String parkingFile = args[1];
        String propertiesFile = args[2];
        String populationFile = args[3];

        if (!fileFormat.equals("csv") && !fileFormat.equals("json")) {
            System.out.println("First argument must be \"csv\" or \"json\" (case-sensitive)");
            return;
        }

        //check files exist
        try {
            new FileReader(parkingFile);
        } catch (IOException e) {
            System.out.println("Could not open parking file");
            return;
        }

        try {
            new FileReader(propertiesFile);
        } catch (IOException e) {
            System.out.println("Could not open properties file");
            return;
        }

        try {
            new FileReader(populationFile);
        } catch (IOException e) {
            System.out.println("Could not open population file");
            return;
        }

        ParkingViolationReaderStrategy parkingViolationReader;
        if (fileFormat.equals("csv")) {
            parkingViolationReader = new CSVParkingViolationReader();
        } else {
            parkingViolationReader = new JSONParkingViolationReader();
        }

        List<Population> populationData = PopulationReader.getInstance().getPopulations(populationFile);
        List<Property> propertyData = PropertyReader.getInstance().getProperties(propertiesFile);
        List<ParkingViolation> parkingData = parkingViolationReader.getParkingViolations(parkingFile);

        PopulationProcessor populationProcessor = new PopulationProcessor(PopulationReader.getInstance(), populationFile);
        FinesPerCapitalProcessor finesPerCapitalProcessor = new FinesPerCapitalProcessor(parkingData, populationData);
        AvgMarketValueProcessor avgMarketValueProcessor = new AvgMarketValueProcessor(PropertyReader.getInstance(), propertiesFile);
        AvgLivableAreaProcessor avgLivableAreaProcessor = new AvgLivableAreaProcessor(propertyData);
        MarketValuePerCapitalProcessor marketValuePerCapitalProcessor = new MarketValuePerCapitalProcessor(propertyData, populationData);

        UI ui = new UI (populationProcessor, finesPerCapitalProcessor, avgMarketValueProcessor, avgLivableAreaProcessor, marketValuePerCapitalProcessor);
        ui.run();
    }
}