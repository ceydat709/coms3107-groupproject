package phillydata.common;
public class Population {
    private int zip_code;
    private int population;
    public Population(int zip_code, int population) {
        this.zip_code = zip_code;
        this.population = population;
    }
    public int getZip_code() {
        return zip_code;
    }
    public int getPopulation() {
        return population;
    }
}
