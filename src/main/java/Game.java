
import java.io.Serializable;
import java.util.StringJoiner;

public class Game implements Serializable {
    private int year;
    private int sales;
    private int rating;
    private String name;

    public Game() {
    }

    public Game(String name, int year, int sales, int rating) {
        this.year = year;
        this.sales = sales;
        this.rating = rating;
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public int getSales() {
        return sales;
    }

    public int getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String convertToXml() {
        StringJoiner joiner = new StringJoiner("\n");
        return joiner.add("\t<game year=\"" + year +  "\">")
                .add("\t\t<name>" + name + "</name>")
                .add("\t\t<sales>" + sales + "</sales>")
                .add("\t\t<rating>" + rating + "</rating>")
                .add("\t</game>")
                .toString();
    }
}