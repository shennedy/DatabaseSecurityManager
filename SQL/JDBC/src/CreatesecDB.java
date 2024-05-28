import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class CreatesecDB {
    static final String DB_URL = "jdbc:mysql://localhost:3306/secdb"; 
    static final String USER = "root";
    static final String PASS = "";

    public void Create() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            DatabaseMetaData metaData = conn.getMetaData();

            // Get the list of tables/views in the secdb database
            ResultSet resultSet = metaData.getTables("secdb", null, null, new String[]{"TABLE", "VIEW"});
            StringBuilder tables = new StringBuilder();
            while (resultSet.next()) {
                tables.append(resultSet.getString("TABLE_NAME")).append("\n");
            }
            resultSet.close();

            // Display the list of tables/views and prompt the user to select one
            String selectedTable = (String) JOptionPane.showInputDialog(null,
                    "Select a table to add data to:", "Add Data to Table",
                    JOptionPane.PLAIN_MESSAGE, null, tables.toString().split("\n"), null);

            if (selectedTable != null && !selectedTable.isEmpty()) {
                // Retrieve column names
                ResultSetMetaData rsmd = stmt.executeQuery("SELECT * FROM " + selectedTable).getMetaData();
                int columnCount = rsmd.getColumnCount();
                StringBuilder columnNames = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.append(rsmd.getColumnName(i)).append("\n");
                }

                // Prompt the user to input data for each column
                String[] inputData = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    inputData[i - 1] = JOptionPane.showInputDialog(null, "Enter value for " + rsmd.getColumnName(i) + ":");
                }

                // Construct the SQL INSERT statement
                StringBuilder insertStatement = new StringBuilder("INSERT INTO " + selectedTable + " (");
                for (int i = 1; i <= columnCount; i++) {
                    insertStatement.append(rsmd.getColumnName(i));
                    if (i < columnCount) {
                        insertStatement.append(", ");
                    }
                }
                insertStatement.append(") VALUES (");
                for (int i = 0; i < columnCount; i++) {
                    insertStatement.append("'").append(inputData[i]).append("'");
                    if (i < columnCount - 1) {
                        insertStatement.append(", ");
                    }
                }
                insertStatement.append(")");

                // Execute the INSERT statement
                stmt.executeUpdate(insertStatement.toString());
                
                JOptionPane.showMessageDialog(null, "Data added successfully to " + selectedTable);
            } else {
                JOptionPane.showMessageDialog(null, "No table selected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

