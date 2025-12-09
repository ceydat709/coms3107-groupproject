package phillydata.ui;

import java.util.*;
import phillydata.processing.*;

public class UI {
    private PopulationProcessor populationProcessor;
    private FinesPerCapitalProcessor finesPerCapitalProcessor;
    private AvgMarketValueProcessor avgMarketValueProcessor;
    private AvgLivableAreaProcessor avgLivableAreaProcessor;
    private MarketValuePerCapitalProcessor marketValuePerCapitalProcessor;

    public UI(PopulationProcessor populationProcessor, FinesPerCapitalProcessor finesPerCapitalProcessor, AvgMarketValueProcessor avgMarketValueProcessor, AvgLivableAreaProcessor avgLivableAreaProcessor, MarketValuePerCapitalProcessor marketValuePerCapitalProcessor) {
        if (populationProcessor == null || finesPerCapitalProcessor == null || avgMarketValueProcessor == null || avgLivableAreaProcessor == null || marketValuePerCapitalProcessor == null) {
            throw new IllegalArgumentException();
        }
        this.populationProcessor = populationProcessor;
        this.finesPerCapitalProcessor = finesPerCapitalProcessor;
        this.avgMarketValueProcessor = avgMarketValueProcessor;
        this.avgLivableAreaProcessor = avgLivableAreaProcessor;
        this.marketValuePerCapitalProcessor = marketValuePerCapitalProcessor;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            showMenu();
            String menuChoice = sc.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(menuChoice);
            } catch (NumberFormatException e) {
                continue;
            }
            if (choice == 0) return;

            switch (choice) {
                case 1:
                    int totalPopulation = populationProcessor.totalPopulation();
                    System.out.println(totalPopulation);
                    break;
                case 2:
                    Map<String, Double> finesPerCapitaMap = finesPerCapitalProcessor.finesPerCapita();
                    List<String> sortedZipCodes = new ArrayList<>(finesPerCapitaMap.keySet());

                    Collections.sort(sortedZipCodes, (a, b) ->
                            Integer.parseInt(a) - Integer.parseInt(b));
                    for (String zipCode : sortedZipCodes) {
                        double value = finesPerCapitaMap.get(zipCode);
                        System.out.printf("%s %.4f%n", zipCode, value);
                    }
                    break;
                case 3:
                    System.out.println("Enter ZIP Code: ");
                    String zipMarketValue = sc.nextLine().trim();
                    try {
                        int zipAsInt = Integer.parseInt(zipMarketValue);
                        System.out.println(avgMarketValueProcessor.averageMarketValue(zipAsInt));
                    } catch (NumberFormatException e) {
                        System.out.println(0);
                    }
                    break;
                case 4:
                    System.out.println("Enter ZIP Code: ");
                    String zipLivableArea = sc.nextLine().trim();
                    System.out.println(avgLivableAreaProcessor.avgLivableArea(zipLivableArea));
                    break;

                case 5:
                    System.out.println("Enter ZIP Code: ");
                    String zipPerCapita = sc.nextLine().trim();
                    System.out.println(marketValuePerCapitalProcessor.marketValuePerCapita(zipPerCapita));
                    break;
                default:
                    break;
            }
        }
    }

    public void showMenu() {
        System.out.println("Option #1: Total Population for all ZIP Codes");
        System.out.println("Option #2: Fines per capita for each ZIP Code");
        System.out.println("Option #3: Average Market value for a ZIP Code");
        System.out.println("Option #4: Average total liveable area for a ZIP Code");
        System.out.println("Option #5: Residential market value per capita");
        System.out.println("Option #0: Exit");
    }
}