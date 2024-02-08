import java.io.*;
import java.sql.*;
import java.util.Scanner;

class CalGPA {
    public static void main(String args[]) throws SQLException, IOException {
        
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException x) {
            System.out.println("Driver could not be loaded.");
        }

        String dbacct, passwrd, name;
        char grade;
        int credit;

        Scanner input  = new Scanner(System.in);
        System.out.print("Enter database account: ");
        dbacct = input.next();
        System.out.print("\nEnter password: ");
        passwrd = input.next();

        Connection conn = DriverManager.getConnection("jdbc:oracle:oci8:" + dbacct + "/" + passwrd);
        /*
         * Student attributes used: Student_number, Name
         * Grade_Report attributes used: Student_Number, Section_Identifier
         * Section attributes used: Section_Identifier, Course_NUmber
         * Course attributes used: Course_Number
         */
        String stmt1 = "select G.Grade, C.Credit_hours from STUDENT S, GRADE_REPORT G, SECTION SEC, COURSE C where G.Student_number=S.Student_number AND G.Section_identifier=SEC.Section_identifier AND SEC.Course_number=C.Course_number AND S.Name=?";

        System.out.print("\nPlease enter your name: ");
        name = input.next();

        PreparedStatement p = conn.prepareStatement(stmt1);
        p.clearParameters();
        p.setString(1, name);
        ResultSet r = p.executeQuery();
        
        double count = 0, sum = 0, avg = 0;
        while (r.next()) {
            grade = r.getString(1).charAt(0);
            credit = r.getInt(2);
            switch (grade) {
                case 'A':
                    sum = sum + (4 * credit);
                    count = count + 1;
                    break;
                case 'B':
                    sum = sum + (3 * credit);
                    count = count + 1;
                    break;
                case 'C':
                    sum = sum + (2 * credit);
                    count = count + 1;
                    break;
                case 'D':
                    sum = sum + (1 * credit);
                    count = count + 1;
                    break;
                case 'F':
                    sum = sum + (0 * credit);
                    count = count + 1;
                    break;
                default:
                    System.out.println("This grade " + grade + " will not be calculated.");
                    break;
            }
        }

        avg = sum / count;
        System.out.println("Student named " + name + " has a grade point average " + avg + ".");
        
        r.close();
        conn.close();
        p.close();
        input.close();
    }
}