import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewsecDB {
    static final String DB_URL = "jdbc:mysql://localhost:3306/secdb";
    static final String USER = "root";
    static final String PASS = "";

    public void View() {
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
                    "Select a table/view to view:", "Table/View Viewer",
                    JOptionPane.PLAIN_MESSAGE, null, tables.toString().split("\n"), null);

            if (selectedTable != null && !selectedTable.isEmpty()) {
                // Retrieve column names
                ResultSetMetaData rsmd = stmt.executeQuery("SELECT * FROM " + selectedTable).getMetaData();
                int columnCount = rsmd.getColumnCount();
                StringBuilder columnNames = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    columnNames.append(rsmd.getColumnName(i)).append("\t");
                }

                // Retrieve and format the data from the selected table
                ResultSet tableData = stmt.executeQuery("SELECT * FROM " + selectedTable);
                StringBuilder tableInfo = new StringBuilder(columnNames.toString() + "\n");
                while (tableData.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        String columnValue = tableData.getString(i);
                        int padding = Math.max(1, rsmd.getColumnDisplaySize(i) - columnValue.length());
                        tableInfo.append(columnValue);
                        // Add padding spaces
                        for (int j = 0; j < padding; j++) {
                            tableInfo.append(" ");
                        }
                        tableInfo.append("\t");
                    }
                    tableInfo.append("\n");
                }
                tableData.close();
                JOptionPane.showMessageDialog(null, "Selected Table/View: " + selectedTable + "\n\n" + tableInfo.toString());
            } else {
                JOptionPane.showMessageDialog(null, "No table/view selected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

