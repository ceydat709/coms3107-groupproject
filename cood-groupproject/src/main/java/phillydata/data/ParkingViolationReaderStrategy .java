package phillydata.data;
import phillydata.common.ParkingViolation;
import java.util.List;

public interface ParkingViolationReaderStrategy {
    List<ParkingViolation> getParkingViolations(String filename);
}