import java.io.*;
import java.sql.*;


class calGPA {
public static void main (String args[]) throws SQLException, IOException
{
    try
    {
        Class.forName("oracle.jdbc.driver.OrableDriver");
    }
    catch (ClassNotFoundException x)
    {
        System.out.println("Driver could not be loaded.");
    }
    String dbacct, passwrd, name;
    Char grade;
    int credit;
    dbacct = readEntry("Enter database account: ");
    passwrd = readEntry("Enter password: ");
    Connection conn =
    DriverManager.getConnection("jdbc:oracle:oci8:"+dbacct+"/"+passwrd);
    /*
     * Student attributes used: Student_number, Name
     * Grade_Report attributes used: Student_Number, Section_Identifier
     * Section attributes used: Section_Identifier, Course_NUmber
     * Course attributes used: Course_Number
     */
    String stmt1 = 
    "select G.Grade, C.Credit_hours
    from STUDENT S, GRADE_REPORT G, SECTION SEC, COURSE C
    where G.Student_number=S.Student_number AND
    G.Section_identifier=SEC.Section_identifier AND
    SEC.Course_number=C.Course_number AND S.Name=?";
    PreparedStatement p = conn.prepareStatement(stmt1);
    name = readEntry("Please enter your name: ");
    p.clearParameters();
    p.setString(1, name);
    ResultSet r = p.executeQuery();
    double count=0, sum=0, avg=0;
    while(r.next())
    {
        grade = r.getChar(1);
        credit = r.getInteger(2);
        switch (grade)
        {
            case A: sum=sum+(4*credit); count=count+1; break;
            case B: sum=sum+(3*credit); count=count+1; break;
            case C: sum=sum+(2*credit); count=count+1; break;
            case D: sum=sum+(1*credit); count=count+1; break;
            case F: sum=sum+(0*credit); count=count+1; break;
            default: println("This grade "+grade+" will not be calculated.")break;
        }
    };
        avg = sum/count;
        System.out.printline("Student named "+name+" has a grade point average "+avg+".");
        r.close();
    }
}