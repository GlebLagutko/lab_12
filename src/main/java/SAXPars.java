import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import static java.lang.Integer.max;
import static java.lang.Math.min;

public class SAXPars extends DefaultHandler {
    private int oldestGame = 3000;
    private int earliestGame = 2000;
    private int size = 0;
    private int maxSales = 0;
    private int minSales = Integer.MAX_VALUE ;
    private int averageSales = 0;

    private boolean isSales;

    public int getOldestGame() {
        return oldestGame;
    }

    public int getEarliestGame() {
        return earliestGame;
    }

    public int getSize() {
        return size;
    }

    public int getMaxSales() {
        return maxSales;
    }

    public int getMinSales() {
        return minSales;
    }

    public int getAverageSales() {
        return averageSales;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException, SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("games")) {
            size = 0;
            maxSales = 0;
            averageSales = 0;
            minSales = Integer.MAX_VALUE ;
        }
        if (qName.equals("game")) {
            size++;
            if (oldestGame == 3000) {
                oldestGame = Integer.valueOf(attributes.getValue("year"));
            }
            if (earliestGame == 2000) {
                earliestGame = Integer.valueOf(attributes.getValue("year"));
            }

            oldestGame = max(oldestGame, Integer.valueOf(attributes.getValue("year")));
            earliestGame = min(earliestGame, Integer.valueOf(attributes.getValue("year")));

        }
        if (qName.equals("sales")) {
            isSales = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equals("sales")) {
            isSales = false;
        }
        if (qName.equals("games")) {
            averageSales /= size;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (isSales) {
            int sales = Integer.valueOf(new String(ch, start, length));
            minSales = min(sales, minSales);
            maxSales = max(sales, maxSales);
            averageSales += sales;
        }
    }


}
