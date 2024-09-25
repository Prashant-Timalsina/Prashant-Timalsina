package cms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conn {

    private static final String URL = "jdbc:mysql://localhost:3306/cms";
    private static final String DATABASE_NAME = "cms";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    static {
        try {
            // Register the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Get connection to the default database (e.g., 'mysql' database)
            Connection defaultConnection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement st = defaultConnection.createStatement();

            // Create the database if not exists
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            st.executeUpdate(createDatabaseQuery);
            System.out.println("Created Database: " + DATABASE_NAME);

            // Close the connection to the default database
            defaultConnection.close();

            // Connect to the 'cms' database
            Connection connection = getConnection();
            Statement st1 = connection.createStatement();

            // Create the admin table if not exists
            String createAdminTableQuery = "CREATE TABLE IF NOT EXISTS cms.admin ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "username VARCHAR(255) NOT NULL,"
                    + "name Varchar(50) NOT NULL, "
                    + "password VARCHAR(255) NOT NULL,"
                    + "roles VARCHAR(255) NOT NULL,"
                    + "email VARCHAR(50), "
                    + "phone VARCHAR(10), "
                    + "courses VARCHAR(50))";
            st1.executeUpdate(createAdminTableQuery);

            // Check if admin user already exists
            String checkAdminExistenceQuery = "SELECT COUNT(*) FROM admin WHERE roles IN ('Admin', 'Student', 'Teacher')";
            ResultSet adminExistenceResult = st1.executeQuery(checkAdminExistenceQuery);
            adminExistenceResult.next();
            int adminCount = adminExistenceResult.getInt(1);

            // If admin user does not exist, insert
            if (adminCount == 0) {
                // Insert Admin role
                String insertAdminUserQuery = "INSERT INTO admin (username, name, password, roles) VALUES ('admin', 'admin', 'admin', 'Admin')";
                st1.executeUpdate(insertAdminUserQuery);
            }

            adminExistenceResult.close();


            // Create the teacher table if not exists
            String createTeacher = "CREATE TABLE IF NOT EXISTS cms.student (" +
                    "name VARCHAR(50)," +
                    "username VARCHAR(50), " +
                    "password VARCHAR(50), " +
                    "ID VARCHAR(10) PRIMARY KEY, " +
                    "email VARCHAR(50), " +
                    "phone VARCHAR(10), " +
                    "roles VARCHAR(50), " +
                    "courses VARCHAR(50), " +
                    "modules VARCHAR(50))";
            st1.execute(createTeacher);

            // Create the students table if not exists
            String createStudent = "CREATE TABLE IF NOT EXISTS cms.teacher (" +
                    "name VARCHAR(50)," +
                    "username VARCHAR(50), " +
                    "password VARCHAR(50), " +
                    "ID VARCHAR(10) PRIMARY KEY, " +
                    "email VARCHAR(50), " +
                    "phone VARCHAR(10), " +
                    "roles VARCHAR(50), " +
                    "courses VARCHAR(50))";
            st1.execute(createStudent);

            // Create the courses table if not exists
	        String createCourses = "CREATE TABLE IF NOT EXISTS cms.course (" +
	        		"courses VARCHAR(50)," +
	                "level VARCHAR(5), " +
	                "semester VARCHAR(5), " +
	                "module VARCHAR(50))";
	        st1.execute(createCourses);
	        
            String sql = "CREATE TABLE IF NOT EXISTS enrollment (id VARCHAR(50), name VARCHAR(50), course VARCHAR(50), level int(5), module VARCHAR(50))";
            st1.execute(sql);
            
            String sql1 = "CREATE TABLE IF NOT EXISTS marking (level int, module VARCHAR(50), student_name VARCHAR(60), marks int(5))";
            st1.execute(sql1);
            
	        
            // Close the connection to the 'cms' database
            connection.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    public static Connection getConnection() throws SQLException {
    	  try {
              return DriverManager.getConnection(URL, USERNAME, PASSWORD);
          } catch (SQLException e) {
              System.err.println("Failed to establish a connection to the database:");
              e.printStackTrace();
              throw e; // Rethrow the exception to propagate it to the caller
          }
    }
}