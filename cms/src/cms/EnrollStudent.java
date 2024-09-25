package cms;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class EnrollStudent extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private String id, course;
    public int level;
    private List<String> modules = new ArrayList<>();
    private Set<String> uniqueCourses = new HashSet<>();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    EnrollStudent frame = new EnrollStudent("", "", "");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public EnrollStudent(String id, String course, String name) {

        this.id = id;
        this.course = course;

        lvlup(course, name);

        setTitle("Student Enrollment Details");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 414, 461);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel idLabel = new JLabel("ID : " + id);
        idLabel.setHorizontalAlignment(SwingConstants.CENTER);
        idLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
        idLabel.setBounds(10, 94, 378, 72);
        contentPane.add(idLabel);

        JLabel crsLabel = new JLabel("Course : " + course);
        crsLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
        crsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        crsLabel.setBounds(10, 177, 378, 72);
        contentPane.add(crsLabel);

        JLabel lvlLabel = new JLabel("Level : " + level);
        lvlLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lvlLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
        lvlLabel.setBounds(10, 260, 378, 72);
        contentPane.add(lvlLabel);

        JButton enrollButton = new JButton("Enroll");
        enrollButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call method to create enrollment table
                createEnrollmentTable(name, level);
            }
        });
        enrollButton.setBounds(152, 368, 89, 23);
        contentPane.add(enrollButton);

        JLabel namaewa = new JLabel("Name : " + name);
        namaewa.setHorizontalAlignment(SwingConstants.CENTER);
        namaewa.setFont(new Font("Tahoma", Font.PLAIN, 24));
        namaewa.setBounds(10, 11, 378, 72);
        contentPane.add(namaewa);
    }

    private void createEnrollmentTable(String name, int level) {
        try {
            // Establish connection to the database
            Connection connection = Conn.getConnection();

            // Check if the enrollment already exists for the given student, course, level
            String checkEnrollmentSQL = "SELECT * FROM enrollment WHERE id = ? AND level = ? AND course = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkEnrollmentSQL);
            checkStatement.setString(1, id);
            checkStatement.setInt(2, level);
            checkStatement.setString(3, course);

            System.out.println("Level: " + level);

            // If no rows are returned, insert the enrollment
            ResultSet resultSet = checkStatement.executeQuery();
            if (!resultSet.next()) {
                // Query to select modules for the given course and level
                String selectModulesSQL = "SELECT module FROM course WHERE courses = ? AND level = ?";
                PreparedStatement selectModulesStatement = connection.prepareStatement(selectModulesSQL);
                selectModulesStatement.setString(1, course);
                selectModulesStatement.setInt(2, level);
                ResultSet moduleResultSet = selectModulesStatement.executeQuery();

                // Create the SQL statement to insert enrollment details
                String insertEnrollmentSQL = "INSERT INTO enrollment (id, name, course, level, module) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertEnrollmentSQL);

                // Insert values into the enrollment table for each module
                while (moduleResultSet.next()) {
                    String module = moduleResultSet.getString("module");
                    insertStatement.setString(1, id);
                    insertStatement.setString(2, name);
                    insertStatement.setString(3, course);
                    insertStatement.setInt(4, level);
                    insertStatement.setString(5, module);
                    insertStatement.executeUpdate(); // Execute insert for each module
                }

                // Close the result set and statements
                moduleResultSet.close();
                selectModulesStatement.close();
                insertStatement.close();
            }

            // Close the result set and check statement
            resultSet.close();
            checkStatement.close();
            connection.close();

            System.out.println("Enrollment details inserted successfully");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void lvlup(String course, String name) {
        try {
            // Establish connection to the database
            Connection connection = Conn.getConnection();

            // Query to fetch the current level of the student
            String getCurrentLevelSQL = "SELECT MAX(level) AS max FROM enrollment WHERE id = ? AND name = ? AND course = ?";
            PreparedStatement getCurrentLevelStatement = connection.prepareStatement(getCurrentLevelSQL);
            getCurrentLevelStatement.setString(1, id);
            getCurrentLevelStatement.setString(2, name);
            getCurrentLevelStatement.setString(3, course);
            ResultSet currentLevelResultSet = getCurrentLevelStatement.executeQuery();

            // Retrieve the current level from the result set
            int maxLevel = 4;

            if (currentLevelResultSet.next()) {
                maxLevel = currentLevelResultSet.getInt("max");
            }

            // Set the level to the maximum of the current level and 4
            level = Math.max(maxLevel, 4);

            // Close the result set and statement for current level
            currentLevelResultSet.close();
            getCurrentLevelStatement.close();

            // Query to check if all modules for the current level are present in the marking table
            String checkModulesSQL = "SELECT DISTINCT module FROM course WHERE level = ? AND courses = ?";
            PreparedStatement checkModulesStatement = connection.prepareStatement(checkModulesSQL);
            checkModulesStatement.setInt(1, level);
            checkModulesStatement.setString(2, course);
            ResultSet moduleResultSet = checkModulesStatement.executeQuery();

            // List to hold modules for the current level
            List<String> levelModules = new ArrayList<>();

            // Populate the list with modules for the current level
            while (moduleResultSet.next()) {
                String module = moduleResultSet.getString("module");
                levelModules.add(module);
            }

            // Close the result set and statement for level modules
            moduleResultSet.close();
            checkModulesStatement.close();

            // Query to check if all modules for the current level are present in the marking table
            String checkMarkingSQL = "SELECT DISTINCT module FROM marking WHERE module = ?";
            PreparedStatement checkMarkingStatement = connection.prepareStatement(checkMarkingSQL);

            // Check if all modules for the current level are present in the marking table
            boolean allModulesPresent = true;
            for (String module : levelModules) {
                checkMarkingStatement.setString(1, module);
                ResultSet markingResultSet = checkMarkingStatement.executeQuery();

                // If any module for the current level is missing from the marking table, set the flag to false
                if (!markingResultSet.next()) {
                    allModulesPresent = false;
                    break;
                }

                // Close the result set for the current module
                markingResultSet.close();
            }

            // If all modules for the current level are present in the marking table and marks for level >= 160, update the level
            if (allModulesPresent) {
                int marks = marksForLevel(level, name);
                if (marks >= 160) {
                    System.out.println("Marks" + marks);
                    level++;
                } else {
                    JOptionPane.showMessageDialog(null, "Your marks is not sufficient", "Fail",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Not all modules are marked", "Incomplete Result",
                        JOptionPane.ERROR_MESSAGE);
            }

            // Close the connection
            connection.close();

            System.out.println("Level updated successfully to level " + level);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to calculate marks for the current level
    private int marksForLevel(int level, String name) {
        int totalMarks = 0;
        try {
            // Establish connection to the database
            Connection connection = Conn.getConnection();

            // Query to fetch the marks achieved by the student for each module in the given level
            String getMarksSQL = "SELECT marks FROM marking WHERE module IN (SELECT module FROM course WHERE level = ? AND courses = ?) AND student_name = ?";
            PreparedStatement getMarksStatement = connection.prepareStatement(getMarksSQL);
            getMarksStatement.setInt(1, level);
            getMarksStatement.setString(2, course);
            getMarksStatement.setString(3, name);
            ResultSet marksResultSet = getMarksStatement.executeQuery();

            // Sum up the marks achieved for all modules in the level
            while (marksResultSet.next()) {
                int marks = marksResultSet.getInt("marks");
                totalMarks += marks;
            }

            // Close the result set and statement
            marksResultSet.close();
            getMarksStatement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return totalMarks;
    }
}
