import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class CrosswordGUI {
    private JFrame frame;
    private JTable initialTable;
    private JTable solutionTable;
    private JList<String> wordList;

    public CrosswordGUI(String[][] taskTable, Map<String, List<int[]>> foundCoordinates, String[] keywords) {
        // Frame setup
        frame = new JFrame("Crossword Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Left panel - Word List
        wordList = new JList<>(keywords);
        JScrollPane wordScrollPane = new JScrollPane(wordList);
        wordScrollPane.setPreferredSize(new Dimension(150, 600));
        frame.add(wordScrollPane, BorderLayout.WEST);

        // Center panel - Initial Table
        initialTable = new JTable(createTableModel(taskTable));
        styleTable(initialTable, keywords);
        JScrollPane initialScrollPane = new JScrollPane(initialTable);
        frame.add(initialScrollPane, BorderLayout.CENTER);

        // Right panel - Solution Table
        String[][] solutionArray = createSolutionArray(taskTable, foundCoordinates);
        solutionTable = new JTable(createTableModel(solutionArray));
        styleTable(solutionTable, keywords);
        JScrollPane solutionScrollPane = new JScrollPane(solutionTable);
        solutionScrollPane.setPreferredSize(new Dimension(300, 600));
        frame.add(solutionScrollPane, BorderLayout.EAST);

        frame.setVisible(true);
    }

    private DefaultTableModel createTableModel(String[][] data) {
        String[] columnNames = new String[data[0].length];
        for (int i = 0; i < data[0].length; i++) {
            columnNames[i] = String.valueOf(i);
        }
        return new DefaultTableModel(data, columnNames);
    }

    private void styleTable(JTable table, String[] keywords) {
        table.setRowHeight(keywords.length);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private String[][] createSolutionArray(String[][] taskTable, Map<String, List<int[]>> foundCoordinates) {
        String[][] solutionArray = new String[taskTable.length][taskTable[0].length];
        for (int i = 0; i < taskTable.length; i++) {
            for (int j = 0; j < taskTable[i].length; j++) {
                solutionArray[i][j] = " ";
            }
        }

        for (List<int[]> coordinates : foundCoordinates.values()) {
            for (int[] coordinate : coordinates) {
                solutionArray[coordinate[0]][coordinate[1]] = taskTable[coordinate[0]][coordinate[1]];
            }
        }
        return solutionArray;
    }
}