package phillydata.testing;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import phillydata.processing.AvgMarketValueProcessor;
import phillydata.data.PropertyReader;

public class AvgMarketValueTest{
    @Test
    public void testNullReader(){
        assertThrows(IllegalArgumentException.class, () -> {
            AvgMarketValueProcessor amp = new AvgMarketValueProcessor(null, "somefile.csv");
        });
    }
    @Test
    public void testNullFilename(){
        assertThrows(IllegalArgumentException.class, () -> {
            AvgMarketValueProcessor amp = new AvgMarketValueProcessor(PropertyReader.getInstance(), null);
        });
    }
    @Test
    public void testInvalidFilename(){
        assertThrows(IllegalStateException.class , () -> {
            AvgMarketValueProcessor amp = new AvgMarketValueProcessor(PropertyReader.getInstance(), "nonexistentfile123456asdf.csv");
        });
    }
    @Test
    public void testAverageMarketValue(@TempDir Path tempDir) throws Exception { //uses a temporary file to test averageMarketValue()
        Path file = tempDir.resolve("file.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file.toFile()))) {
            pw.println("zip_code,market_value,total_livable_area");
            pw.println("19104,100000,1200");
            pw.println("19104,200000,1400");
            pw.println("19103,500000,2000");
        }
        AvgMarketValueProcessor amp = new AvgMarketValueProcessor(PropertyReader.getInstance(), file.toString());
        assertEquals(150000,amp.averageMarketValue(19104));
    }
    @Test
    public void testAverageMarketValueInvalidValues(@TempDir Path tempDir) throws Exception { //uses a temporary file to test averageMarketValue() with invalid values
        Path file = tempDir.resolve("file.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file.toFile()))) {
            pw.println("zip_code,market_value,total_livable_area");
            pw.println("19104,100000,1000");
            pw.println("19104,,900");
            pw.println("19104,dog,1500");
            pw.println("19104,0,2200");
            pw.println("19104,-50000,1800");
            pw.println("19104,200000,1600");
        }
        AvgMarketValueProcessor amp = new AvgMarketValueProcessor(PropertyReader.getInstance(), file.toString());
        assertEquals(150000,amp.averageMarketValue(19104));
    }
    @Test
    public void testAverageMarketValueNoValidValues(@TempDir Path tempDir) throws Exception { //uses a temporary file to test averageMarketValue() with no valid values
        Path file = tempDir.resolve("file.csv");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file.toFile()))) {
            pw.println("zip_code,market_value,total_livable_area");
            pw.println("19104,,1000");        
            pw.println("19104,dog,1500");     
            pw.println("19104,0,2000");       
            pw.println("19104,-20000,1800");  
            pw.println("19103,300000,1500");  
        }
        AvgMarketValueProcessor amp = new AvgMarketValueProcessor(PropertyReader.getInstance(), file.toString());
        assertEquals(0,amp.averageMarketValue(19104));
    }
}