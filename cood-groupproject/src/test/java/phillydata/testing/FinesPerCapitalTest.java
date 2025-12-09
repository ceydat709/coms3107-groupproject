package phillydata.testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Map;

import phillydata.processing.FinesPerCapitalProcessor;
import phillydata.data.CSVParkingViolationReader;
import phillydata.data.PopulationReader;

public class FinesPerCapitalTest {
    @Test
    public void testNullReaders() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FinesPerCapitalProcessor(null, "parking.csv", PopulationReader.getInstance(), "population.txt");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new FinesPerCapitalProcessor(new CSVParkingViolationReader(), "parking.csv", null, "population.txt");
        });
    }

    @Test
    public void testNullFilenames() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FinesPerCapitalProcessor(new CSVParkingViolationReader(), null, PopulationReader.getInstance(), "population.txt");
        });
    }

    @Test
    public void testInvalidFilename() {
        assertThrows(IllegalStateException.class, () -> {
            new FinesPerCapitalProcessor(new CSVParkingViolationReader(), "notreal321123.csv", PopulationReader.getInstance(), "notreal234.txt");
        });
    }

    @Test
    public void testFinesPerCapital(@TempDir Path tempDir) throws Exception {
        Path parkingFile = tempDir.resolve("parking.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(parkingFile.toFile()))) {
            pw.println("date,fine,violation,plate_id,state,ticket_number,zipcode");
            pw.println("2013-04-03T15:15:00Z,36,METER EXPIRED CC,1322731,PA,2905938,19104");
            pw.println("2013-04-03T07:35:00Z,51,DOUBLE PARKED,1322731,PA,2905939,19104");
        }

        Path populationFile = tempDir.resolve("population.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(populationFile.toFile()))) {
            pw.println("19104 10000");
        }

        FinesPerCapitalProcessor processor = new FinesPerCapitalProcessor( new CSVParkingViolationReader(), parkingFile.toString(), PopulationReader.getInstance(), populationFile.toString());
        Map<String, Double> result = processor.finesPerCapita();
        assertEquals(0.0087, result.get("19104"), 0.0001);
    }

    @Test
    public void testNonPA(@TempDir Path tempDir) throws Exception {
        Path parkingFile = tempDir.resolve("parking.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(parkingFile.toFile()))) {
            pw.println("date,fine,violation,plate_id,state,ticket_number,zipcode");
            pw.println("2013-09-18T14:58:00Z,41,EXPIRED INSPECTION,1322731,PA,2905940,19104");
            pw.println("2013-09-23T13:58:00Z,41,EXPIRED INSPECTION,1322731,PA,2905941,19104");
            pw.println("2013-01-11T13:31:00Z,36,METER EXPIRED CC,1199878,NJ,2905942,19104");
        }
        Path populationFile = tempDir.resolve("population.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(populationFile.toFile()))) {
            pw.println("19104 20000");
        }
        FinesPerCapitalProcessor processor = new FinesPerCapitalProcessor( new CSVParkingViolationReader(), parkingFile.toString(), PopulationReader.getInstance(), populationFile.toString());
        Map<String, Double> result = processor.finesPerCapita();
        assertEquals(0.0041, result.get("19104"), 0.0001);
    }

    @Test
    public void testNoPopulation(@TempDir Path tempDir) throws Exception {
        Path parkingFile = tempDir.resolve("parking.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(parkingFile.toFile()))) {
            pw.println("date,fine,violation,plate_id,state,ticket_number,zipcode");
            pw.println("2013-03-11T14:51:00Z,36,METER EXPIRED CC,1199878,PA,2905943,19103");
        }

        Path populationFile = tempDir.resolve("population.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(populationFile.toFile()))) {
            pw.println("19103 0");
        }

        FinesPerCapitalProcessor processor = new FinesPerCapitalProcessor( new CSVParkingViolationReader(), parkingFile.toString(), PopulationReader.getInstance(), populationFile.toString());
        Map<String, Double> result = processor.finesPerCapita();
        assertFalse(result.containsKey("19103"));
    }
}