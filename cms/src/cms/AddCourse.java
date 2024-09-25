package cms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCourse extends JFrame {

    private JPanel contentPane;
    private JTextField crsName;
    private JComboBox lvlbox;
    private JComboBox sembox;
    private JTextField mods;

    public AddCourse(JTable coursesTable) {
        setTitle("Add Course");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 400, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblCourseName = new JLabel("Course Name:");
        lblCourseName.setBounds(37, 30, 100, 14);
        contentPane.add(lblCourseName);

        crsName = new JTextField();
        crsName.setBounds(147, 27, 200, 20);
        contentPane.add(crsName);
        crsName.setColumns(10);

        JLabel lblLvl = new JLabel("Level");
        lblLvl.setBounds(37, 60, 100, 14);
        contentPane.add(lblLvl);

        lvlbox = new JComboBox();
        lvlbox.setModel(new DefaultComboBoxModel(new String[] {"4", "5", "6"}));
        lvlbox.setBounds(147, 57, 200, 20);
        contentPane.add(lvlbox);

        JLabel lblsem = new JLabel("Semester");
        lblsem.setBounds(37, 90, 100, 14);
        contentPane.add(lblsem);

        sembox = new JComboBox();
        sembox.setModel(new DefaultComboBoxModel(new String[] {"1", "2"}));
        sembox.setBounds(147, 87, 200, 20);
        contentPane.add(sembox);

        JLabel lblMods = new JLabel("Module");
        lblMods.setBounds(37, 120, 100, 14);
        contentPane.add(lblMods);

        mods = new JTextField();
        mods.setBounds(147, 117, 200, 20);
        contentPane.add(mods);
        mods.setColumns(10);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addCourseToDatabase(coursesTable);
            }
        });
        addButton.setBounds(147, 200, 89, 23);
        contentPane.add(addButton);
    }

    private void addCourseToDatabase(JTable coursesTable) {
        String courseName = crsName.getText();
        String Level = lvlbox.getSelectedItem().toString();
        String Semester = sembox.getSelectedItem().toString();
        String Module = mods.getText();

        // Validate if any of the fields are empty
        if (courseName.isEmpty() || Module.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try {
            Connection connection = Conn.getConnection();
            String query = "INSERT INTO course (courses, level, semester, module) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, courseName);
            preparedStatement.setString(2, Level);
            preparedStatement.setString(3, Semester);
            preparedStatement.setString(4, Module);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Course added successfully.");
                // Refresh the courses table after adding the course
//                ((DefaultTableModel) coursesTable.getModel()).fireTableDataChanged();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add course.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
