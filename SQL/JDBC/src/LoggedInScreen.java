import java.util.*;
import javax.swing.JOptionPane;
//import java.sql.*;

public class LoggedInScreen {
    static Scanner JObject = new Scanner (System.in);
    static String input;
    final static String HEADING1 = "Logged In Screen";
    static boolean exitTime = false;

    public void logIn() 
    {
       int userOption;

        while(!exitTime)
        {
            input = JOptionPane.showInputDialog(null,
                    "1. Display. \n"
                +   "2. Create. \n"
                +   "3. Modify. \n"
                +   "4. Remove. \n"
                +   "5. Quit", HEADING1, JOptionPane.QUESTION_MESSAGE);
            userOption = Integer.parseInt(input);
            switch (userOption)
            {
                case 1: {ViewsecDB ViewsecDBObject = new ViewsecDB(); ViewsecDBObject.View(); break;}
                case 2: {CreatesecDB CreatesecDBObject = new CreatesecDB(); CreatesecDBObject.Create(); break;}
                case 3: {ModifysecDB2 ModifysecDBObject = new ModifysecDB2(); ModifysecDBObject.Modify(); break;}
                case 4: {DeletesecDB DeletesecDBObject = new DeletesecDB(); DeletesecDBObject.Delete(); break;}
                case 5: {exitTime = true; break;}
            }//end of switch case
        }//end of while loop     
    }//End of Main Method
}

