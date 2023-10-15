import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class test {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String DB_USER = "your_username";
    private static final String DB_PASSWORD = "your_password";

    private JFrame frame;
    private JTextField keywordField;
    private JButton searchButton;
    private JTable resultTable;

    public test() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());

            UIDefaults defaults = UIManager.getLookAndFeelDefaults();
            defaults.put("nimbusBase", new Color(18, 30, 49));
            defaults.put("nimbusBlueGrey", new Color(255, 255, 255));
            defaults.put("control", new Color(18, 30, 49));
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Dark Theme Keyword Search GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new BorderLayout());

        keywordField = new JTextField(20);
        searchButton = new JButton("Search");
        resultTable = new JTable();

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = keywordField.getText();
                searchDatabase(keyword);
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Keyword: "));
        inputPanel.add(keywordField);
        inputPanel.add(searchButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void searchDatabase(String keyword) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String query = "SELECT * FROM your_table WHERE column = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, keyword);
            ResultSet resultSet = preparedStatement.executeQuery();

            DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                String column1Data = resultSet.getString("column1");
                String column2Data = resultSet.getString("column2");
                model.addRow(new Object[]{column1Data, column2Data});
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new test();
            }
        });
    }
}
