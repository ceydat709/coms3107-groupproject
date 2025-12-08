package phillydata.data;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import phillydata.common.ParkingViolation;


public class CSVParkingViolationReader implements ParkingViolationReaderStrategy {
    @Override
    public List<ParkingViolation> getParkingViolations(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            List<ParkingViolation> violations = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);
                String timestamp = values[0];
                String description = values[2];
                String vehicleId = values[3];
                String state = values[4];
                String violationId = values[5];
                String zipcode = values[6];

                int fine;
                try {
                    fine = Integer.parseInt(values[1]);
                    if (fine <= 0) fine = 0;
                } catch (NumberFormatException e) {
                    fine = 0;
                }
                ParkingViolation pv = new ParkingViolation(timestamp, fine, description, vehicleId, state, violationId, zipcode);
                violations.add(pv);
            }
            return violations;

        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    
}