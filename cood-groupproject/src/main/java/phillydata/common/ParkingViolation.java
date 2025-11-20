package phillydata.common;

public class ParkingViolation {
    private String timestamp;
    private int fine;
    private String description;
    private String vehicleId;
    private String state;
    private String violationId;
    private String zipcode;

    // constructor
    public ParkingViolation(String timestamp, int fine, String description, String vehicleId, String state, String violationId, String zipcode) {
        this.timestamp = timestamp;
        this.fine = fine;
        this.description = description;
        this.vehicleId = vehicleId;
        this.state = state;
        this.violationId = violationId;
        this.zipcode = zipcode;
    }

    // accessors
    public String getTimestamp() { return timestamp; }
    public int getFine() { return fine; }
    public String getDescription() { return description; }
    public String getVehicleId() { return vehicleId; }
    public String getState() { return state; }
    public String getViolationId() { return violationId; }
    public String getZipcode() { return zipcode; }
}