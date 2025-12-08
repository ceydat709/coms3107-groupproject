package phillydata.processing;

import phillydata.common.ParkingViolation;
import phillydata.common.Population;
import java.util.*;

public class FinesPerCapitalProcessor {
    private List<ParkingViolation> violations;
    private List<Population> populations;

    public FinesPerCapitalProcessor (List<ParkingViolation> violations, List<Population> populations) {
        if (violations == null || populations == null) {
            throw new IllegalArgumentException();
        }
        this.violations = violations;
        this.populations = populations;
    }

    public Map<String, Double> finesPerCapita() {
        //map zip code to population
        Map<String, Integer> populationMap = new HashMap<>();
        for (Population p : populations) {
            String zipCode = "" + p.getZip_code();
            populationMap.put(zipCode, p.getPopulation());
        }

        // map zip code to total fines
        Map<String, Integer> totalFinesMap = new HashMap<>();
        violations.stream()
                .filter(v -> v.getZipcode() != null && !v.getZipcode().isEmpty())
                .filter(v -> "PA".equals(v.getState()))
                .forEach(v -> {
                    String zipCode = v.getZipcode();
                    int fine = v.getFine();
                    if (!totalFinesMap.containsKey(zipCode)) {
                        totalFinesMap.put(zipCode, fine);
                    } else {
                        totalFinesMap.put(zipCode, totalFinesMap.get(zipCode) + fine);
                    }
                });

        // map zip code to fines per capita
        Map<String, Double> finesPerCapita = new HashMap<>();
        for (String zipCode : totalFinesMap.keySet()) {
            int totalFines = totalFinesMap.get(zipCode);

            if (totalFines == 0) continue;
            if (!populationMap.containsKey(zipCode)) continue;

            int population = populationMap.get(zipCode);
            if (population == 0) continue;

            double value = totalFines / (double) population;
            finesPerCapita.put(zipCode, value);
        }
        return finesPerCapita;
    }
}