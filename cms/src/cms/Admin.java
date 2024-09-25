package cms;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;


import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTextField;
import java.awt.SystemColor;
import javax.swing.JSplitPane;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.SpringLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;
import javax.swing.JEditorPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class Admin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable courses;
	private JTable tableT;
	private JTable stdsTable;
	private JTextField semesterField;

	private JTable crsTable;
	private JTabbedPane tabbedPane;

	private String username;
	private String role;
	@SuppressWarnings("unused")
	private User user;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					User user = new User("", "","","");
					Admin frame = new Admin(user);
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
	
	private JComboBox<String> courseSearch;
	private Set<String> uniqueCourses = new HashSet<>();
	private DefaultTableModel crsTableModel,stdsTableModel;
	
public Admin(User user) {
		
		this.username = user.getUsername();
	    this.role = user.getRole();
	    
		setTitle("Course Management System");
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800,600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel menu = new JPanel();
		menu.setBounds(0, 0, 170, 561);
		contentPane.add(menu);
		menu.setLayout(null);
		      

		
		
		JLabel clzlogo = new JLabel("");
		clzlogo.setIcon(new ImageIcon(Admin.class.getResource("/Images/aherald zz.png")));
		clzlogo.setBounds(38, 11, 100,100);
		menu.add(clzlogo);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 132, 150, 2);
		menu.add(separator_1);
		
		
			JPanel panel = new JPanel();
			panel.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			panel.setBounds(10, 145, 150, 405);
			menu.add(panel);
			panel.setLayout(null);
		
	
			
			JLabel logOutLogo = new JLabel("");
			logOutLogo.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int answer = JOptionPane.showConfirmDialog(null,"Do you want to Logout?","Logout",JOptionPane.YES_NO_OPTION);
					if(answer == 0) {
						contentPane.setVisible(false);
						dispose();
						Login naya = new Login();
						naya.setVisible(true);
						
					}
				}
			});
			logOutLogo.setIcon(new ImageIcon(Admin.class.getResource("/Images/power-switch.png")));
			logOutLogo.setBounds(59, 298, 40, 40);
			panel.add(logOutLogo);
			
			JButton homebtn = new JButton("Home");
			homebtn.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                // Switch to Panel
	                tabbedPane.setSelectedIndex(0);
	            }
	        });
			homebtn.setBounds(10, 46, 128, 40);
			panel.add(homebtn);
			
			JButton crsbtn = new JButton("Courses");
			crsbtn.setBounds(10, 97, 128, 40);
			crsbtn.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                // Switch to Panel
	                tabbedPane.setSelectedIndex(1);
	            }
	        });
			panel.add(crsbtn);
			
			JButton teachersbtn = new JButton("Teachers");
			teachersbtn.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                // Switch to Panel
	                tabbedPane.setSelectedIndex(3);
	            }
	        });
			teachersbtn.setBounds(10, 199, 128, 40);
			panel.add(teachersbtn);
			
			JButton stdsbtn = new JButton("Students");
			stdsbtn.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                // Switch to Panel
	                tabbedPane.setSelectedIndex(2);
	            }
	        });
			stdsbtn.setBounds(10, 148, 128, 40);
			panel.add(stdsbtn);
		
		
		
		
		 tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(169, 0, 615, 561);
		contentPane.add(tabbedPane);
		
		
		JPanel homepage = new JPanel();
		tabbedPane.addTab("Home", null, homepage, null);
		homepage.setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 164, 590, 2);
		homepage.add(separator);
		
		JLabel lblNewLabel = new JLabel("Welcome");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 85));
		lblNewLabel.setBounds(45, 11, 501, 81);
		homepage.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel(username);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(46, 103, 270, 32);
		homepage.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel(role);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_2.setBounds(326, 103, 149, 32);
		homepage.add(lblNewLabel_2);
		
		JPanel crs = new JPanel();
        tabbedPane.addTab("Courses", null, crs, null);
        crs.setLayout(null);
		
        courseSearch = new JComboBox<>();
        getCoursesFromDatabase(); // Populate the uniqueCourses set
        courseSearch.setModel(new DefaultComboBoxModel<>(uniqueCourses.toArray(new String[0])));
        courseSearch.setBounds(51, 102, 189, 20);
        crs.add(courseSearch);
        
     // Add ActionListener to the courseSearch JComboBox
        courseSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // When the selection changes, update the table based on the selected course
                updateCoursesTable();
            }
        });
		
		JLabel Searchc_1 = new JLabel("Search Course");
		Searchc_1.setForeground(new Color(178, 34, 34));
		Searchc_1.setBounds(51, 71, 189, 20);
		crs.add(Searchc_1);
		
		JButton addCourse_1 = new JButton("Add Courses");
		addCourse_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new AddCourse(courses).setVisible(true);
			}
		});
		addCourse_1.setBounds(50, 303, 501, 23);
		crs.add(addCourse_1);
		
		JButton removeC = new JButton("Remove Course");
		removeC.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // Get the selected row index
		        int selectedRowIndex = crsTable.getSelectedRow();

		        if (selectedRowIndex == -1) {
		            // No row selected, show a message
		            JOptionPane.showMessageDialog(null, "Please select a course to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
		        } else {
		            // Get the course name from the selected row
		            String courseNameToRemove = crsTable.getValueAt(selectedRowIndex, 2).toString();

		            // Confirm removal
		            int confirmation = JOptionPane.showConfirmDialog(null,
		                    "Are you sure you want to remove this course?\nCourse Name: " + courseNameToRemove,
		                    "Confirm Removal", JOptionPane.YES_NO_OPTION);

		            if (confirmation == JOptionPane.YES_OPTION) {
		                // Remove course from the database
		                removeCourseFromDatabase(courseNameToRemove);

		                // Update the table after removal
		                updateCoursesTable();
		            }
		        }
		    }
		});
		removeC.setBounds(50, 337, 501, 23);
		crs.add(removeC);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(51, 133, 500, 159);
		crs.add(scrollPane);
		
		 crsTableModel = new DefaultTableModel(
			    new Object[][] {
			        // Empty data for initialization
			    },
			    new String[] {
			        "Level", "Semester", "CourseName"
			    }
			);
			crsTable = new JTable(crsTableModel);
			scrollPane.setViewportView(crsTable);

			updateCoursesTable(); // Populate the crsTable with data from the database

	
			
		
		JPanel students = new JPanel();
		tabbedPane.addTab("Students", null, students, null);
		students.setLayout(null);
		
		JLabel Searchs = new JLabel("View Student");
		Searchs.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Searchs.setForeground(new Color(178, 34, 34));
		Searchs.setBounds(10, 50, 189, 20);
		students.add(Searchs);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(10, 81, 590, 186);
		students.add(scrollPane_3);
		
		
		stdsTableModel = new DefaultTableModel(
				new Object [][] {},
				new String[] {
						"Student ID", "Student Name", "Course","Password"
				}
		);
		stdsTable = new JTable(stdsTableModel);
		scrollPane_3.setViewportView(stdsTable);
		
		updateStudentsTable();
		
		JButton addStudent = new JButton("Add Students");
		addStudent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Signup("Student").setVisible(true);
			}
		});
		addStudent.setBounds(41, 290, 501, 23);
		students.add(addStudent);
		
		JButton removeStudents = new JButton("Remove Students");
		removeStudents.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // Get the selected row index
		        int selectedRowIndex = stdsTable.getSelectedRow();

		        if (selectedRowIndex == -1) {
		            // No row selected, show a message
		            JOptionPane.showMessageDialog(null, "Please select a student to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
		        } else {
		            // Get the student ID from the selected row
		            String studentIdToRemove = stdsTable.getValueAt(selectedRowIndex, 0).toString();

		            // Confirm removal
		            int confirmation = JOptionPane.showConfirmDialog(null,
		                    "Are you sure you want to remove this student?\nStudent ID: " + studentIdToRemove,
		                    "Confirm Removal", JOptionPane.YES_NO_OPTION);

		            if (confirmation == JOptionPane.YES_OPTION) {
		                // Remove student from the database
		                removeStudentFromDatabase(studentIdToRemove);

		                // Update the table after removal
		                updateStudentsTable();
		            }
		        }
		    }
		});
		removeStudents.setBounds(41, 324, 501, 23);
		students.add(removeStudents);
		
		JButton resultbtn = new JButton("Result");
		resultbtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new ResultPanel().setVisible(true);
			}
		});
		resultbtn.setBounds(453, 49, 147, 23);
		students.add(resultbtn);
		
		JPanel teachers = new JPanel();
		tabbedPane.addTab("Teachers", null, teachers, null);
		teachers.setLayout(null);
		
		JLabel SearchT = new JLabel("View Teacher");
		SearchT.setFont(new Font("Tahoma", Font.PLAIN, 14));
		SearchT.setForeground(new Color(178, 34, 34));
		SearchT.setBounds(10, 50, 189, 20);
		teachers.add(SearchT);
		
		tableT = new JTable();
		tableT.setModel(new DefaultTableModel(
		        new Object[][] {
		                // Empty data for initialization
		        },
		        new String[] {
		                "Teacher Name", "Teacher ID", "Course","Password"
		        }
		));
		updateTeachersTable();

		// Set up a scroll pane for the JTable
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 81, 590, 189);
		teachers.add(scrollPane_2);

		// Add the JTable to the scroll pane
		scrollPane_2.setViewportView(tableT);
		
		JButton modules = new JButton("Assign Module");
		modules.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Assign().setVisible(true);
			}
		});
		modules.setBounds(451, 51, 149, 23);
		teachers.add(modules);
		
		JButton addTeachers = new JButton("Add Teachers");
		addTeachers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Signup("Teacher").setVisible(true);
			}
		});
		addTeachers.setBounds(45, 298, 501, 23);
		teachers.add(addTeachers);
		
		JButton removeTeacehrs = new JButton("Remove Teachers");
		removeTeacehrs.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // Get the selected row index
		        int selectedRowIndex = tableT.getSelectedRow();

		        if (selectedRowIndex == -1) {
		            // No row selected, show a message
		            JOptionPane.showMessageDialog(null, "Please select a teacher to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
		        } else {
		            // Get the teacher ID from the selected row
		            String teacherIdToRemove = tableT.getValueAt(selectedRowIndex, 1).toString();

		            // Confirm removal
		            int confirmation = JOptionPane.showConfirmDialog(null,
		                    "Are you sure you want to remove this teacher?\nTeacher ID: " + teacherIdToRemove,
		                    "Confirm Removal", JOptionPane.YES_NO_OPTION);

		            if (confirmation == JOptionPane.YES_OPTION) {
		                // Remove teacher from the database
		                removeTeacherFromDatabase(teacherIdToRemove);

		                // Update the table after removal
		                updateTeachersTable();
		            }
		        }
		    }
		});
		removeTeacehrs.setBounds(45, 332, 501, 23);
		teachers.add(removeTeacehrs);
		
		
		tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex == 2) { // Students tab
                    updateStudentsTable();
                } else if (selectedIndex == 3) { // Teachers tab
                    updateTeachersTable();
                }
            }
        });
		
	}
		
		
	 

	public void updateTeachersTable() {
	    // Assuming 'tableT' is the name of the table model for your teachers JTable
	    DefaultTableModel model = (DefaultTableModel) tableT.getModel();

	    // Clear the existing data in the table
	    model.setRowCount(0);

	    // Fetch and add new data from the database
	    try {
	        Connection connection = Conn.getConnection();
	        Statement statement = connection.createStatement();
	        ResultSet resultSet = statement.executeQuery("SELECT * FROM teacher");

	        while (resultSet.next()) {
	            //Table has columns: "Teacher Name", "Teacher ID", "Course",Password
	            String teacherName = resultSet.getString("name");
	            String teacherId = resultSet.getString("ID");
	            String course = resultSet.getString("courses");
	            String pass = resultSet.getString("password");

	            // Add a new row to the table model
	            model.addRow(new Object[]{teacherName, teacherId, course,pass});
	        }

	        resultSet.close();
	        statement.close();
	        connection.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

		
		private void getCoursesFromDatabase() {
	        try {
	            Connection connection = Conn.getConnection();
	            Statement statement = connection.createStatement();
	            ResultSet resultSet = statement.executeQuery("SELECT courses FROM course");

	            while (resultSet.next()) {
	                String courseName = resultSet.getString("courses");
	                uniqueCourses.add(courseName);
	            }

	            resultSet.close();
	            statement.close();
	            connection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
		
		public void updateCoursesTable() {
		    // Assuming 'crsTable' is the name of the table model for your JTable
		    DefaultTableModel model = (DefaultTableModel) crsTable.getModel();

		    // Clear the existing data in the table
		    model.setRowCount(0);

		    // Fetch and add new data from the database based on the selected course
		    try {
		        Connection connection = Conn.getConnection();
		        Statement statement = connection.createStatement();

		        // Get the selected course from the JComboBox
		        String selectedCourse = courseSearch.getSelectedItem().toString();

		        // Construct the SQL query based on the selected course
		        String query = "SELECT level, semester, module FROM course WHERE courses = '" + selectedCourse + "'";
		        ResultSet resultSet = statement.executeQuery(query);

		        // Create a Vector to hold the rows
		        Vector<Vector<Object>> rows = new Vector<>();

		        while (resultSet.next()) {
		            String level = resultSet.getString("level");
		            String semester = resultSet.getString("semester");
		            String courseName = resultSet.getString("module");

		            // Create a Vector for the current row
		            Vector<Object> row = new Vector<>();
		            row.add(level);
		            row.add(semester);
		            row.add(courseName);

		            // Add the row to the Vector of rows
		            rows.add(row);
		        }

		        // Close resources
		        resultSet.close();
		        statement.close();
		        connection.close();

		        // Sort the rows using a custom Comparator
		        rows.sort(new CourseRowComparator());

		        // Add the sorted rows to the table model
		        for (Vector<Object> row : rows) {
		            model.addRow(row);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
		
		private static class CourseRowComparator implements Comparator<Vector<Object>> {
	        public int compare(Vector<Object> row1, Vector<Object> row2) {
	            // Compare by "Level" first
	            int level1 = Integer.parseInt(row1.get(0).toString());
	            int level2 = Integer.parseInt(row2.get(0).toString());
	            int levelComparison = Integer.compare(level1, level2);

	            if (levelComparison != 0) {
	                return levelComparison;
	            }

	            // If "Level" is the same, compare by "Semester"
	            int semester1 = Integer.parseInt(row1.get(1).toString());
	            int semester2 = Integer.parseInt(row2.get(1).toString());
	            return Integer.compare(semester1, semester2);
	        }
	    }
		
		public void updateStudentsTable() {
		    // Assuming 'stdsTable' is the name of the table model for your students JTable
		    DefaultTableModel model = (DefaultTableModel) stdsTable.getModel();

		    // Clear the existing data in the table
		    model.setRowCount(0);

		    // Fetch and add new data from the database
		    try {
		        Connection connection = Conn.getConnection();
		        Statement statement = connection.createStatement();
		        ResultSet resultSet = statement.executeQuery("SELECT id,name,password, courses FROM student");

		        while (resultSet.next()) {
		            // Assuming your table has columns: "StudentID", "Student Name", "Module"
		            String studentId = resultSet.getString("id");
		            String studentName = resultSet.getString("name");
		            String course = resultSet.getString("courses");
		            String pass = resultSet.getString("password");

		            // Add a new row to the table model
		            model.addRow(new Object[]{studentId, studentName, course,pass});
		        }

		        resultSet.close();
		        statement.close();
		        connection.close();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
		
		private void removeStudentFromDatabase(String studentId) {
		    try {
		        Connection connection = Conn.getConnection();
		        Statement statement = connection.createStatement();

		        // Execute SQL query to remove the student based on ID
		        String query = "DELETE FROM students WHERE id = '" + studentId + "'";
		        statement.executeUpdate(query);

		        // Close resources
		        statement.close();
		        connection.close();
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Error removing student from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
		    }
		}
		
		private void removeTeacherFromDatabase(String teacherId) {
		    try {
		        Connection connection = Conn.getConnection();
		        Statement statement = connection.createStatement();

		        // Execute SQL query to remove the teacher based on ID
		        String query = "DELETE FROM teacher WHERE ID = '" + teacherId + "'";
		        statement.executeUpdate(query);

		        // Close resources
		        statement.close();
		        connection.close();
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Error removing teacher from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
		    }
		}
		
		private void removeCourseFromDatabase(String courseName) {
		    try {
		        Connection connection = Conn.getConnection();
		        Statement statement = connection.createStatement();

		        // Execute SQL query to remove the course based on course name
		        String query = "DELETE FROM course WHERE module = '" + courseName + "'";
		        statement.executeUpdate(query);

		        // Close resources
		        statement.close();
		        connection.close();
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Error removing course from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
		    }
		}
}

