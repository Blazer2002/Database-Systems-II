import java.sql.Statement;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws Exception {
        
        //Load JDBC driver
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Driver Loaded!");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver could not load.");
        }

        //Connect to Database
        String dbURL = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "system";
        String password = "Butter10";
        try {
            //Try to connect to server
            Connection connection   = DriverManager.getConnection(dbURL, username, password);
            System.out.println("Connected to Oracle database server.");

            //Get List of Students
            String sql_student_list = "SELECT * FROM STUDENT";
            Statement statement     = connection.createStatement();
            ResultSet result        = statement.executeQuery(sql_student_list);

            System.out.println("-----Students-----");
            while(result.next())
            {
                String nameStudent = result.getString(2);
                System.out.println(nameStudent);
            }
            result.close();
            
            //Get User to pick a student
            System.out.print("\nType the name of a student: ");
            Scanner scanner     = new Scanner(System.in);
            String studentName  = scanner.nextLine();  // Read user input


            //GPA finding SQL
            String sql_GPA_finder = "SELECT G.Grade, C.Credit_hours "+
            "FROM STUDENT S, GRADE_REPORT G, SECTION SEC, COURSE C "+
            "WHERE G.Student_number=S.Student_number AND "+
            "G.Section_identifier=SEC.Section_identifier AND "+
            "SEC.Course_number=C.Course_number AND S.student_name=?";

            //Setup the SQL to take the students name
            PreparedStatement prepStatement = connection.prepareStatement(sql_GPA_finder);
            prepStatement.clearParameters();
            prepStatement.setString(1, studentName);

            //Get data from database server
            ResultSet result_GPA = prepStatement.executeQuery();

            double count = 0;
            double sum   = 0;
            double avg   = 0;

            //Go through data and sum up
            while(result_GPA.next())
            {
                String grade    = result_GPA.getString(1);
                int credit      = Integer.parseInt(result_GPA.getString(2));
                switch (grade)
                {
                    case "A": sum=sum+(4*credit); count=count+1; break;
                    case "B": sum=sum+(3*credit); count=count+1; break;
                    case "C": sum=sum+(2*credit); count=count+1; break;
                    case "D": sum=sum+(1*credit); count=count+1; break;
                    case "F": sum=sum+(0*credit); count=count+1; break;
                    default: System.out.println("This grade " + grade + " will not be calculated."); break;
                }
            };
            result_GPA.close();

            //Calculate Avg and spit out
            avg = sum/count;
            System.out.println("Student named "+ studentName +" has a grade point average "+ avg +".\n");

            //Close connection to server
            connection.close();
        } 
        catch (SQLException e) {
            System.out.println("Could not connect to database server.");
            e.printStackTrace();
        }
    }
}
