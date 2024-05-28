import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class DeletesecDB {
    static final String DB_URL = "jdbc:mysql://localhost:3306/secdb"; 
    static final String USER = "root";
    static final String PASS = "";

    public void Delete() {
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
                    "Select a table to delete data from:", "Delete Data from Table",
                    JOptionPane.PLAIN_MESSAGE, null, tables.toString().split("\n"), null);

            if (selectedTable != null && !selectedTable.isEmpty()) {
                // Retrieve column names
                ResultSetMetaData rsmd = stmt.executeQuery("SELECT * FROM " + selectedTable).getMetaData();
                int columnCount = rsmd.getColumnCount();
                StringBuilder columnNames = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.append(rsmd.getColumnName(i)).append("\n");
                }

                // Prompt the user to input the primary key value for deletion
                String primaryKey = JOptionPane.showInputDialog(null, "Enter the code to delete:");

                // Construct the SQL DELETE statement
                String deleteStatement = "DELETE FROM " + selectedTable + " WHERE " + rsmd.getColumnName(1) + " = '" + primaryKey + "'";

                // Execute the DELETE statement
                int rowsAffected = stmt.executeUpdate(deleteStatement);
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Row deleted successfully from " + selectedTable);
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
