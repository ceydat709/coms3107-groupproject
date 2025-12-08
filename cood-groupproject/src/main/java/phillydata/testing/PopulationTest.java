package phillydata.testing;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.FileWriter;
import java.io.PrintWriter;
import phillydata.processing.PopulationProcessor;
import phillydata.data.PopulationReader;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;


public class PopulationTest {

    @Test
    public void testNullReader(){
        
        assertThrows(IllegalArgumentException.class, () -> {
            PopulationProcessor pp = new PopulationProcessor(null, "somefile.csv");
        });
    }

    @Test
    public void testNullFilename(){
        assertThrows(IllegalArgumentException.class, () -> {
            PopulationProcessor pp = new PopulationProcessor(PopulationReader.getInstance(), null);
        });
    }

    @Test
    public void testInvalidFilename(){
        assertThrows(IllegalStateException.class , () -> {
            PopulationProcessor pp = new PopulationProcessor(PopulationReader.getInstance(), "nonexistentfile123456asdf.csv");
        });
    }

    @Test
    public void testTotalPopulation(@TempDir Path tempDir) throws Exception { //uses a temporary file to test totalPopulation()
        Path file = tempDir.resolve("file.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(file.toFile()))) {
            pw.println("19104 100");
            pw.println("19103 200");
            pw.println("19102 300");
        }
        PopulationProcessor pp = new PopulationProcessor(PopulationReader.getInstance(), file.toString());
        int total = pp.totalPopulation();
        assertEquals(600, total);
    }
    
}
