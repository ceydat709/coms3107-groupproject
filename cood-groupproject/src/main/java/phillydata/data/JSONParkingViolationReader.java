package phillydata.data;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import phillydata.common.ParkingViolation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONParkingViolationReader implements ParkingViolationReaderStrategy {

    private String validZip(String zipcode) {
        if (zipcode == null) return null;
        if (zipcode.length() < 5) return zipcode;
        return zipcode.substring(0, 5);
    }

    @Override
    public List<ParkingViolation> getParkingViolations(String filename) {
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

    