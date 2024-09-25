package cms;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Marking extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField marks;
    private JComboBox<String> student;
    private String teacherName,Module;
    private int level;
    

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Marking frame = new Marking("","");
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
    public Marking(String Module, String teacher) {
    	
    	this.Module = Module;
        teacherName = teacher;
        
    	
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 299, 179);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        student = new JComboBox<String>();
        student.setBounds(91, 31, 164, 22);
        contentPane.add(student);
        
        fillCombobox(Module);

        JLabel lblNewLabel = new JLabel("Student");
        lblNewLabel.setBounds(10, 31, 71, 22);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Marks");
        lblNewLabel_1.setBounds(10, 64, 71, 22);
        contentPane.add(lblNewLabel_1);

        marks = new JTextField();
        marks.setBounds(91, 65, 86, 20);
        contentPane.add(marks);
        marks.setColumns(10);

        marks.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // Check if the text in the marks field is not a number
                if (!marks.getText().matches("\\d+")) {
                    // Display an error message
                    JOptionPane.showMessageDialog(contentPane, "Please enter a valid number.", "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                    // Clear the text field
                    marks.setText("");
                }
            }
        });

        JButton grade = new JButton("Grade");
        grade.setBounds(49, 97, 86, 23);
        contentPane.add(grade);
        
        // ActionListener for the "grade" button
        grade.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                grading(Module);
            }
        });
    }


	// Function to handle grading logic
    private void grading(String Module) {
        // Get the selected student's name
        String selectedStudent = (String) student.getSelectedItem();
        // Get the marks entered
        String marksEntered = marks.getText();
        
        // Check if a student is selected and marks are entered
        if (selectedStudent != null && !selectedStudent.isEmpty() && marksEntered.matches("\\d+")) {
            try {
                // Get connection and create statement
                Connection conn = Conn.getConnection();
                Statement stmt = conn.createStatement();
                
                // Check if the student already has a grade
                String checkExistingGradeQuery = "SELECT * FROM marking WHERE student_name = ? AND module = ? ";
                PreparedStatement checkStmt = conn.prepareStatement(checkExistingGradeQuery);
                checkStmt.setString(1, selectedStudent);
                checkStmt.setString(2, Module);
                ResultSet rs = checkStmt.executeQuery();
                
                // If the student already has a grade, prompt the user to update
                if (rs.next()) {
                    int choice = JOptionPane.showConfirmDialog(contentPane, "This student already has a grade. Do you want to update it?", "Update Grade", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        // Update the existing grade
                        String updateQuery = "UPDATE marking SET marks = ? WHERE student_name = ? AND module =?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                        updateStmt.setInt(1, Integer.parseInt(marksEntered));
                        updateStmt.setString(2, selectedStudent);
                        updateStmt.setString(3, Module);
                        updateStmt.executeUpdate();
                        JOptionPane.showMessageDialog(contentPane, "Grade updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(contentPane, "Grade not updated.", "Not Updated", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    // Insert a new grade
                    String insertQuery = "INSERT INTO marking (level,module, student_name, marks) VALUES (?, ?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(insertQuery);
                    pstmt.setInt(1, level);
                    pstmt.setString(2, Module);
                    pstmt.setString(3, selectedStudent);
                    pstmt.setInt(4, Integer.parseInt(marksEntered)); // Assuming marks is an integer
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(contentPane, "Grade added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    pstmt.close();
                }
                
                // Close connections
                checkStmt.close();
                stmt.close();
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(contentPane, "Error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(contentPane, "Please select a student and enter valid marks.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void fillCombobox(String Module) {
        try {
            Connection c = Conn.getConnection();
            Statement s = c.createStatement();

            // Prepare SQL query to fetch student names and their levels from enrollment table
            PreparedStatement ps = c.prepareStatement(
                    "SELECT s.name, e.level FROM student s INNER JOIN enrollment e ON s.id = e.id WHERE e.module = ?");
            ps.setString(1, Module);
            ResultSet rs = ps.executeQuery();

            // Clear existing items in the combo box
            student.removeAllItems();

            // Add a default item
            student.addItem("--Select Student");

            // Add each student name to the JComboBox
            while (rs.next()) {
                String studentName = rs.getString("name");
                level = rs.getInt("level");
                
                student.addItem(studentName);
            }

            rs.close();
            s.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
