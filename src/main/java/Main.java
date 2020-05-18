import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.StringJoiner;

public class Main extends JFrame {
    private JTable table;
    private GameTableModel tableModel;
    private JMenuBar bar;
    private JMenu menu;
    private JMenuItem openXml;
    private JMenuItem saveXml;
    private JMenuItem deleteRows;
    private JMenuItem addRow;
    private JMenuItem saveBinary;
    private JMenuItem getBinary;
    private JMenuItem saxCalculation;

    public Main() throws HeadlessException {
        super("Lab 12");

        setPreferredSize(new Dimension(700, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JWindow window = new JWindow();
        window.getContentPane().add(
                new JLabel("", new ImageIcon(ResourceLoader.getFileUrl("images/sea.png")), SwingConstants.CENTER));
        window.setBounds(0, 0, 840, 497);
        window.setVisible(true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        window.setVisible(false);
        bar = new JMenuBar();
        menu = new JMenu("file");
        openXml = new JMenuItem("openXML");
        saveXml = new JMenuItem("saveXML");
        deleteRows = new JMenuItem("deleteRows");
        addRow = new JMenuItem("addRow");
        saveBinary = new JMenuItem("saveBinary");
        getBinary = new JMenuItem("openBinary");
        saxCalculation = new JMenuItem("saxCalculation");
        menu.add(openXml);
        menu.add(saveXml);
        menu.addSeparator();
        menu.add(addRow);
        menu.add(deleteRows);
        menu.addSeparator();
        menu.add(saveBinary);
        menu.add(getBinary);
        menu.addSeparator();
        menu.add(saxCalculation);
        bar.add(menu);


        table = new JTable();
        tableModel = new GameTableModel();
        table.getTableHeader().setReorderingAllowed(false);
        table.setModel(tableModel);
        JScrollPane pane = new JScrollPane(table);
        pane.setVisible(true);
        add(pane);

        setJMenuBar(bar);

        openXml.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
            chooser.setFileFilter(filter);
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                tableModel = new GameTableModel(chooser.getSelectedFile());
                table.setModel(tableModel);
            }
        });

        saveXml.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int option = chooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    tableModel.saveXml(chooser.getSelectedFile());
                } catch (IOException exc) {
                    JOptionPane.showMessageDialog(this, exc.getMessage());
                }
            }
        });

        addRow.addActionListener(e -> {
            tableModel.getGames().add(new Game());
            tableModel.fireTableDataChanged();
            table.repaint();
            table.revalidate();
        });

        deleteRows.addActionListener(e -> {
            tableModel.deleteRows(table.getSelectedRows());
            table.repaint();
            table.revalidate();
        });

        saveBinary.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int option = chooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    tableModel.saveBinary(chooser.getSelectedFile());
                } catch (IOException exc) {
                    JOptionPane.showMessageDialog(this, exc.getMessage());
                }
            }
        });

        getBinary.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int option = chooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    table.setModel(tableModel.getBinary(chooser.getSelectedFile()));
                    table.updateUI();
                } catch (IOException | ClassNotFoundException exc) {
                    JOptionPane.showMessageDialog(this, exc.getMessage());
                }
            }
        });

        saxCalculation.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    SAXParserFactory factory = SAXParserFactory.newInstance();
                    SAXParser parser = factory.newSAXParser();
                    SAXPars handler = new SAXPars();
                    parser.parse(chooser.getSelectedFile(), handler);
                    StringJoiner joiner = new StringJoiner("\n");
                    joiner.add("The oldest game was shown in: " + handler.getOldestGame())
                            .add("The earliest:" + handler.getEarliestGame())
                            .add("Average sales: " + handler.getAverageSales())
                            .add("Max sales: " + handler.getMaxSales())
                            .add("Count : " + handler.getSize());
                    JOptionPane.showMessageDialog(null, joiner.toString());
                } catch (IOException | SAXException | ParserConfigurationException exc) {
                    JOptionPane.showMessageDialog(this, exc.getMessage());
                }
            }
        });


        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
