package phillydata.testing;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;

import phillydata.processing.AvgLivableAreaProcessor;
import phillydata.data.PropertyReader;

public class AvgLivableAreaTest {
    @Test
    public void testNullReader() {
        assertThrows(IllegalArgumentException.class, () -> {
            AvgLivableAreaProcessor processor = new AvgLivableAreaProcessor(null, "somefile.csv");
        });
    }

    @Test
    public void testNullFilename() {
        assertThrows(IllegalArgumentException.class, () -> { AvgLivableAreaProcessor processor = new AvgLivableAreaProcessor(PropertyReader.getInstance(), null);
        });
    }

    @Test
    public void testInvalidFilename() {
        assertThrows(IllegalStateException.class, () -> {
            AvgLivableAreaProcessor processor = new AvgLivableAreaProcessor(PropertyReader.getInstance(), "nonexistentfile123456asdf.csv");
        });
    }

    @Test
    public void testAverageLiveableArea(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("properties.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file.toFile()))) {
            pw.println("zip_code,market_value,total_livable_area");
            pw.println("19104,100000,1200");
            pw.println("19104,500000,2000");
        }
        AvgLivableAreaProcessor processor = new AvgLivableAreaProcessor(PropertyReader.getInstance(), file.toString());
        assertEquals(1600,processor.avgLivableArea("19104"));
    }

    @Test
    public void testAverageLiveableAreaInvalidValues(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("properties.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file.toFile()))) {
            pw.println("zip_code,market_value,total_livable_area");
            pw.println("19104,100000,1000");
            pw.println("19104,200000,-1");
            pw.println("19104,300000,0");
            pw.println("19104,400000,2000");
        }
        AvgLivableAreaProcessor processor = new AvgLivableAreaProcessor(PropertyReader.getInstance(), file.toString());
        assertEquals(1500,processor.avgLivableArea("19104"));
    }

    @Test
    public void testNullZipCode(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("properties.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file.toFile()))) {
            pw.println("zip_code,market_value,total_livable_area");
            pw.println("19104,100000,1500");
        }
        AvgLivableAreaProcessor processor = new AvgLivableAreaProcessor(PropertyReader.getInstance(), file.toString());
        assertThrows(IllegalArgumentException.class, () -> {
            processor.avgLivableArea(null);
        });
    }

    @Test
    public void testMemoization(@TempDir Path tempDir) throws Exception {
        Path file = tempDir.resolve("properties.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file.toFile()))) {
            pw.println("zip_code,market_value,total_livable_area");
            pw.println("19104,100000,1500");
        }
        AvgLivableAreaProcessor processor = new AvgLivableAreaProcessor(PropertyReader.getInstance(), file.toString());
        assertEquals(1500,processor.avgLivableArea("19104"));
        assertEquals(1500,processor.avgLivableArea("19104"));
    }
}