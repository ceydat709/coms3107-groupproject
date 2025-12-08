package phillydata.data;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import phillydata.common.ParkingViolation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ParkingViolationReader {

    private String validZip(String zipcode) {
        if (zipcode == null) return null;
        if (zipcode.length() < 5) return zipcode;
        return zipcode.substring(0, 5);
    }

    public List<ParkingViolation> violationCSVReader(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            List<ParkingViolation> violations = new ArrayList<>();
            String line = br.readLine();
            if (line == null) {
                return violations;
            }

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

                zipcode = validZip(zipcode);
                ParkingViolation pv = new ParkingViolation(timestamp, fine, description, vehicleId, state, violationId, zipcode);
                violations.add(pv);
            }
            return violations;

        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public List<ParkingViolation> violationJSONReader(String filename) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(new FileReader(filename));
            List<ParkingViolation> violations = new ArrayList<>();

            for (Object obj : array) {
                JSONObject jsonObj = (JSONObject) obj;
                String timestamp = (String) jsonObj.get("date");
                String description = (String) jsonObj.get("violation");
                String vehicleId = (String) jsonObj.get("plate_id");
                String state = (String) jsonObj.get("state");
                String violationId = String.valueOf(jsonObj.get("ticket_number"));
                String zipcode = (String) jsonObj.get("zip_code");

                int fine;
                try {
                    fine = Integer.parseInt(String.valueOf(jsonObj.get("fine")));
                    if (fine <= 0) fine = 0;
                } catch (NumberFormatException e) {
                    fine = 0;
                }
                zipcode = validZip(zipcode);
                ParkingViolation pv = new ParkingViolation(timestamp, fine, description, vehicleId, state, violationId, zipcode);
                violations.add(pv);
            }
            return violations;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}