import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ModifysecDB2 {
    static final String DB_URL = "jdbc:mysql://localhost:3306/secdb";
    static final String USER = "root";
    static final String PASS = "";

    public void Modify() {
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
                    "Select a table to modify data in:", "Modify Data in Table",
                    JOptionPane.PLAIN_MESSAGE, null, tables.toString().split("\n"), null);

            if (selectedTable != null && !selectedTable.isEmpty()) {
                // Retrieve column names
                ResultSetMetaData rsmd = stmt.executeQuery("SELECT * FROM " + selectedTable).getMetaData();
                int columnCount = rsmd.getColumnCount();
                StringBuilder columnNames = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.append(rsmd.getColumnName(i)).append("\n");
                }

                // Prompt the user to input the primary key value for modification
                String primaryKey = JOptionPane.showInputDialog(null, "Enter the primary key value to modify:");

                // Prompt the user to input the new values for each column
                StringBuilder newValues = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    String newValue = JOptionPane.showInputDialog(null, "Enter new value for " + rsmd.getColumnName(i) + ":");
                    newValues.append(rsmd.getColumnName(i)).append(" = '").append(newValue).append("'");
                    if (i < columnCount) {
                        newValues.append(", ");
                    }
                }

                // Construct the SQL UPDATE statement
                String updateStatement = "UPDATE " + selectedTable + " SET " + newValues + " WHERE " + rsmd.getColumnName(1) + " = '" + primaryKey + "'";

                // Execute the UPDATE statement
                int rowsAffected = stmt.executeUpdate(updateStatement);
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Row updated successfully in " + selectedTable);
                } else {
                    JOptionPane.showMessageDialog(null, "No row with primary key '" + primaryKey + "' found in " + selectedTable);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No table selected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
