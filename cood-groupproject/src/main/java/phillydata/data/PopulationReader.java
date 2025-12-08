package phillydata.data;
import phillydata.common.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
 //implements singleton

public class PopulationReader {
    private static PopulationReader instance;
    private PopulationReader(){}
    
    public static PopulationReader getInstance(){
        if (instance==null){
            instance = new PopulationReader();
        }
        return instance;
    }

    public List<Population> getPopulations(String filename) {
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            List<Population> populations = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(" ");
                int zipCode =  Integer.parseInt(values[0]);
                int population = Integer.parseInt(values[1]);
                Population p = new Population(zipCode, population);
                populations.add(p);
            }
            return populations;
        }
        catch (IOException e){
            System.err.println(e.getMessage());
            return null;
        }
    }
}
