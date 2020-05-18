
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class GameTableModel extends AbstractTableModel {
    private List<Game> games = new ArrayList<>();
    private final static String[] COLUMN_NAMES = {"name", "year", "sales", "rating"};

    public GameTableModel() {
    }

    public GameTableModel(File xmlFile) {
        games.clear();

        int year;
        int sales;
        int rating;
        String name;

        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);


            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("game");
            int size = nodeList.getLength();
            for (int i = 0; i < size; i++) {
                // Выводим информацию по каждому из найденных элементов
                Node node = nodeList.item(i);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    Element element = (Element) node;
                    year = Integer.valueOf(element.getAttribute("year"));
                    name = element.getElementsByTagName("name").item(0).getTextContent();
                    sales = Integer.valueOf(element.getElementsByTagName("sales").item(0).getTextContent());
                    rating = Integer.valueOf(element.getElementsByTagName("rating").item(0).getTextContent());
                    games.add(new Game(name, year, sales, rating));
                }
            }
        } catch (NumberFormatException | SAXException | ParserConfigurationException | IOException exc) {
            JOptionPane.showMessageDialog(null, exc.getMessage());
        }
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex < 2) {
            return String.class;
        } else
            return Integer.class;
    }

    @Override
    public int getRowCount() {
        return games.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int row, int column) {
        switch (column) {
            case 0:
                return games.get(row).getName();
            case 1:
                return games.get(row).getYear();
            case 2:
                return games.get(row).getSales();
            default:
                return games.get(row).getRating();
        }
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        switch (column) {
            case 0:
                games.get(row).setName((String) aValue);
                break;
            case 1:
                games.get(row).setYear(Integer.parseInt((String) aValue));
                break;
            case 2:
                games.get(row).setSales(Integer.parseInt((String) aValue));
                break;
            case 3:
                games.get(row).setRating(Integer.parseInt((String) aValue));
        }
    }

    public void deleteRows(int[] rows) {
        for (int i = rows.length - 1; i >= 0; --i)
            if (rows[i] < games.size())
                games.remove(rows[i]);
    }

    public String toXmlString() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("<games>");
        for (Game elem : games) {
            joiner.add(elem.convertToXml());
        }
        joiner.add("</games>");
        return joiner.toString();
    }

    public void saveXml(File selectedFile) throws IOException {
        FileWriter writer = new FileWriter(selectedFile);
        writer.write(toXmlString());
        writer.flush();
    }

    public void saveBinary(File selectedFile) throws IOException {
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(selectedFile));
        stream.writeObject(games);
        stream.close();
    }

    public GameTableModel getBinary(File selectedFile) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(selectedFile));
        games.clear();
        GameTableModel model = new GameTableModel();
        games = (List<Game>) stream.readObject();
        model.setGames(games);
        stream.close();
        return model;
    }
}

