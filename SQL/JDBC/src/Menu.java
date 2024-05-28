import java.util.*;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Menu 
{
    static Scanner JObject = new Scanner (System.in);
    static String input;
    static String NewUserName = "Sam", NewPassword = "123";
    final static String HEADING1 = "Login Screen";
    static boolean exitTime = false;
    static final String DB_URL = "jdbc:mysql://localhost:3306/secdb"; // Include the name of the database
    static final String USER = "root";
    static final String PASS = "";

    public static void main(String[] args) 
    {
       
        int userOption;
        
        
        while(!exitTime)
        {
            input = JOptionPane.showInputDialog(null,
                    "1. Login \n"
                +   "2. Sign Up \n"
                +   "3. Quit", HEADING1, JOptionPane.QUESTION_MESSAGE);
            userOption = Integer.parseInt(input);
            switch (userOption)
            {
                case 1: {login(); break;}
                case 2: {signUp(); break;}
                case 3: {exitTime = true; break;}
            }//end of switch case
        }//end of while loop     
    }//End of Main Method

    public static void login()
    {
        String Username, Password;

        Username = JOptionPane.showInputDialog("Enter your Username");
        Password = JOptionPane.showInputDialog("Enter your Password");

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM sdb_user WHERE Usr_Name='" + Username + "' AND Usr_Pass='" + Password + "'");
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Login Successful!");
                exitTime = true;
                LoggedInScreen LoggedInScreenObject = new LoggedInScreen();
                LoggedInScreenObject.logIn();
            } else {
                JOptionPane.showMessageDialog(null, "USERNAME OR PASSWORD INCORRECT", "Login Screen", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void signUp()
    {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            DatabaseMetaData metaData = conn.getMetaData();

            // Get the list of tables/views in the secdb database
            ResultSet resultSet = metaData.getTables("secdb", null, "sdb_user", new String[]{"TABLE", "VIEW"});
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