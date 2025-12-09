package phillydata.testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;

import phillydata.processing.MarketValuePerCapitalProcessor;
import phillydata.data.PropertyReader;
import phillydata.data.PopulationReader;

public class MarketValuePerCapitalTest {
    @Test
    public void testNullReader() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MarketValuePerCapitalProcessor(null, "properties.csv", PopulationReader.getInstance(), "population.txt");
        });
    }

    @Test
    public void testNullFilename() {
        assertThrows(IllegalArgumentException.class, () -> {
            new MarketValuePerCapitalProcessor(PropertyReader.getInstance(), null, PopulationReader.getInstance(), "population.txt");
        });
    }

    @Test
    public void testInvalidFilename() {
        assertThrows(IllegalStateException.class, () -> {
            new MarketValuePerCapitalProcessor(PropertyReader.getInstance(), "notreal12388.csv",
                    PopulationReader.getInstance(), "notreal129038.txt");
        });
    }

    @Test
    public void testMarketValuePerCapita(@TempDir Path tempDir) throws Exception {
        Path propertiesFile = tempDir.resolve("properties.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(propertiesFile.toFile()))) {
            pw.println("zip_code,market_value,total_livable_area");
            pw.println("19107,275000,1350");
            pw.println("19107,425000,1850");
        }
        Path populationFile = tempDir.resolve("population.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(populationFile.toFile()))) {
            pw.println("19107 14000");
        }
        MarketValuePerCapitalProcessor processor = new MarketValuePerCapitalProcessor(PropertyReader.getInstance(), propertiesFile.toString(), PopulationReader.getInstance(), populationFile.toString());
        assertEquals(50, processor.marketValuePerCapita("19107"));
    }

    @Test
    public void testMarketValuePerCapitaInvalidValues(@TempDir Path tempDir) throws Exception {
        Path propertiesFile = tempDir.resolve("properties.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(propertiesFile.toFile()))) {
            pw.println("zip_code,market_value,total_livable_area");
            pw.println("19103,150000,1100");
            pw.println("19103,0,1400");
            pw.println("19103,-1,1900");
            pw.println("19103,250000,2200");
        }
        Path populationFile = tempDir.resolve("population.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(populationFile.toFile()))) {
            pw.println("19103 16000");
        }
        MarketValuePerCapitalProcessor processor = new MarketValuePerCapitalProcessor(PropertyReader.getInstance(), propertiesFile.toString(), PopulationReader.getInstance(), populationFile.toString());
        assertEquals(25, processor.marketValuePerCapita("19103"));
    }

    @Test
    public void testNoPopulation(@TempDir Path tempDir) throws Exception {
        Path propFile = tempDir.resolve("properties.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(propFile.toFile()))) {
            pw.println("zip_code,market_value,total_livable_area");
            pw.println("19122,180000,1600");
        }
        Path populationFile = tempDir.resolve("population.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(populationFile.toFile()))) {
            pw.println("19122 0");
        }
        MarketValuePerCapitalProcessor processor = new MarketValuePerCapitalProcessor(PropertyReader.getInstance(), propFile.toString(), PopulationReader.getInstance(), populationFile.toString());
        assertEquals(0, processor.marketValuePerCapita("19122"));
    }

    @Test
    public void testInvalidMarketValues(@TempDir Path tempDir) throws Exception {
        Path propFile = tempDir.resolve("properties.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(propFile.toFile()))) {
            pw.println("zip_code,market_value,total_livable_area");
            pw.println("19106,0,1300");
            pw.println("19106,-1,1700");
            pw.println("19106,-150,2100");
        }
        Path popFile = tempDir.resolve("population.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(popFile.toFile()))) {
            pw.println("19106 12500");
        }
        MarketValuePerCapitalProcessor processor = new MarketValuePerCapitalProcessor(PropertyReader.getInstance(), propFile.toString(), PopulationReader.getInstance(), popFile.toString());
        assertEquals(0, processor.marketValuePerCapita("19106"));
    }

    @Test
    public void testNullZipCode(@TempDir Path tempDir) throws Exception {
        Path propFile = tempDir.resolve("properties.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(propFile.toFile()))) {
            pw.println("zip_code,market_value,total_livable_area");
            pw.println("19123,320000,1450");
        }
        Path popFile = tempDir.resolve("population.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(popFile.toFile()))) {
            pw.println("19123 18000");
        }
        MarketValuePerCapitalProcessor processor = new MarketValuePerCapitalProcessor(PropertyReader.getInstance(), propFile.toString(), PopulationReader.getInstance(), popFile.toString());
        assertThrows(IllegalArgumentException.class, () -> {
            processor.marketValuePerCapita(null);
        });
    }

    @Test
    public void testMemoization(@TempDir Path tempDir) throws Exception {
        Path propFile = tempDir.resolve("properties.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(propFile.toFile()))) {
            pw.println("zip_code,market_value,total_livable_area");
            pw.println("19125,225000,1550");
        }
        Path popFile = tempDir.resolve("population.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(popFile.toFile()))) {
            pw.println("19125 15000");
        }
        MarketValuePerCapitalProcessor processor = new MarketValuePerCapitalProcessor(PropertyReader.getInstance(), propFile.toString(), PopulationReader.getInstance(), popFile.toString());
        assertEquals(15, processor.marketValuePerCapita("19125"));
        assertEquals(15, processor.marketValuePerCapita("19125"));
    }
}