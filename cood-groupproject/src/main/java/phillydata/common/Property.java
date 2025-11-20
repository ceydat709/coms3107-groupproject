package phillydata.common;

public class Property {
    /*if any of these are -1 that means that there was an error in that field of the csv for
    that row
     */
    private double market_value;
    private double total_livable_area;
    private int zip_code;
    //constructor
    public Property(double market_value, double total_livable_area, int zip_code) {
        this.market_value = market_value;
        this.total_livable_area = total_livable_area;
        this.zip_code = zip_code;
    }
    public double getMarket_value() {
        return market_value;
    }
    public double getTotal_livable_area() {
        return total_livable_area;
    }
    public int getZip_code() {
        return zip_code;
    }

}
