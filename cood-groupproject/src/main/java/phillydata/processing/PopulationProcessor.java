package phillydata.processing;
import java.util.*;
import phillydata.common.*;
import phillydata.data.PopulationReader;
public class PopulationProcessor{
    private PopulationReader populationReader;
    private List<Populations> populations;
    public PopulationProcessor(PopulationReader populationR, String filename){
        if (populationR==null || filename==null){
            throw new IllegalArgumentException();
        }
        this.populationReader = populationR;
        this.populations = populationReader.getPopulations(filename);
        if (populations==null){
            throw new IllegalStateException();
        }
    }
    
    public int totalPopulation(){
        int total = 0;
        for (Population p: populations){
            total+=p.getPopulation();
        }
        return total;
    }


}