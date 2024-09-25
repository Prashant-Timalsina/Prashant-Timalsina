package cms;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Assign extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> courses, teacher,module;
    private JButton close;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Assign frame = new Assign();
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
    public Assign() {
        setTitle("Assign Modules");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 256, 224);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Teacher :");
        lblNewLabel.setBounds(23, 55, 53, 14);
        contentPane.add(lblNewLabel);

        teacher = new JComboBox<>();
        teacher.setBounds(86, 51, 125, 22);
        contentPane.add(teacher);

        courses = new JComboBox<>();
        courses.setBounds(86, 18, 125, 22);
        contentPane.add(courses);

        JLabel lblNewLabel_1 = new JLabel("Courses");
        lblNewLabel_1.setBounds(23, 22, 46, 14);
        contentPane.add(lblNewLabel_1);

        JButton select = new JButton("Select Course");
        select.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewTable();
            }
        });
        select.setBounds(23, 117, 188, 23);
        contentPane.add(select);
        
        module = new JComboBox<String>();
        module.setBounds(23, 84, 188, 22);
        contentPane.add(module);
        
        close = new JButton("Close");
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); 
            }
        });
        close.setBounds(23, 151, 188, 23);
        contentPane.add(close);

        // Fill the courses JComboBox with courses from the database
        fillComboBox();

        courses.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedCourse = (String) courses.getSelectedItem();
                    fillComboBox(teacher, "teacher", "name", selectedCourse); // Pass the selected course
                    fillComboBox(module, "course", "module", selectedCourse); // Pass the selected course
                    
                }
            }
        });
        
    }

    // Method to fill JComboBox with data from a specific table
    public void fillComboBox() {
        try {
            Connection connection = Conn.getConnection();
            // Execute query to retrieve courses
            String query = "SELECT Distinct courses FROM course";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Clear existing items
            courses.removeAllItems();

            // Add courses to the JComboBox
            while (resultSet.next()) {
                courses.addItem(resultSet.getString("courses"));
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fillComboBox(JComboBox<String> comboBox, String tableName, String columnName, String selectedCourse) {
        try {
            Connection connection = Conn.getConnection();
            String query = "SELECT DISTINCT " + columnName + " FROM " + tableName + " WHERE courses = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, selectedCourse); // Bind the selected course as a parameter
            ResultSet resultSet = statement.executeQuery();

            // Clear existing items
            comboBox.removeAllItems();

            comboBox.addItem(""); // Empty item at index 0

            while (resultSet.next()) {
                comboBox.addItem(resultSet.getString(columnName));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNewTable() {
        String selectedCourse = (String) courses.getSelectedItem();
        String selectedTeacher = (String) teacher.getSelectedItem();
        String selectedModule = (String) module.getSelectedItem();

        // Check if a module is selected
        if (selectedModule == null || selectedModule.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a module!", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method
        }

        try {
            Connection connection = Conn.getConnection();
            String query = "CREATE TABLE IF NOT EXISTS cms.assign ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY,"
                            + "teacher VARCHAR(255),"
                            + "course VARCHAR(255),"
                            + "module VARCHAR(50),"
                            + "UNIQUE KEY unique_assignment (teacher, module)"
                            + ")";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();

            // Check if the assignment already exists
            query = "SELECT COUNT(*) FROM assign WHERE module = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, selectedModule);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            if (count == 0) {
                // Check if the teacher has already been assigned 4 modules
                query = "SELECT COUNT(*) FROM assign WHERE teacher = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, selectedTeacher);
                resultSet = statement.executeQuery();
                resultSet.next();
                int teacherCount = resultSet.getInt(1);
                if (teacherCount >= 4) {
                    // Show error message
                    JOptionPane.showMessageDialog(this, "This teacher already has 4 assigned modules!", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit the method
                }

                // Insert the selected teacher into the new table
                query = "INSERT INTO assign (teacher, course, module) VALUES (?, ?, ?)";
                statement = connection.prepareStatement(query);
                statement.setString(1, selectedTeacher);
                statement.setString(2, selectedCourse);
                statement.setString(3, selectedModule);
                statement.executeUpdate();

                // Show success message
                JOptionPane.showMessageDialog(this, "Module assigned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Show error message
                JOptionPane.showMessageDialog(this, "This module is already assigned to another teacher!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
