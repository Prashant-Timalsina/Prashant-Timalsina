package cms;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.border.EtchedBorder;

public class StudentPanel extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField semesterField;
    private JPanel homePanel;
    private JPanel coursesPanel;
    private JLabel CourseHeader;
    private JTable table;
    private JPanel resultPanel;
    private JTable coursesTable;
    
    private String username;
    private String course;
    private String email;
    private String phone;
    private String id;
    private String name;
    private String role;
    
    private JComboBox<String> courseSearch;
	private Set<String> uniqueCourses = new HashSet<>();
	private JTable enrolledCourses;

	private User user;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	User user= new User("","","","");
                    StudentPanel frame = new StudentPanel(user);
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
    public StudentPanel(User user) {
    	this.role = user.getRole();
    	this.username = user.getUsername();
    	this.name = user.getName();
    	this.course = user.getCourse();
    	this.id = user.getId();
    	this.phone=user.getPhone();
    	this.email=user.getEmail();
    	
    	
    	
        setTitle("Student");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel menu = new JPanel();
        menu.setBounds(0, 0, 170, 561);
        contentPane.add(menu);
        menu.setLayout(null);

        JButton Home = new JButton("Home");
        Home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Show the Home panel
                CardLayout cardLayout = (CardLayout) (homePanel.getLayout());
                cardLayout.show(homePanel, "Home");
            }
        });
        Home.setBounds(21, 187, 128, 40);
        menu.add(Home);

        JButton crs = new JButton("Courses");
        crs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Show the Courses panel
                CardLayout cardLayout = (CardLayout) (homePanel.getLayout());
                cardLayout.show(homePanel, "CoursesPanel");
            }
        });
        crs.setBounds(21, 254, 128, 40);
        menu.add(crs);

        JLabel clzlogo = new JLabel("");
        clzlogo.setIcon(new ImageIcon(StudentPanel.class.getResource("/Images/aherald zz.png")));
        clzlogo.setBounds(38, 11, 100, 100);
        menu.add(clzlogo);

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(10, 132, 150, 2);
        menu.add(separator_1);

        homePanel = new JPanel();
        homePanel.setBounds(168, 0, 616, 561);
        contentPane.add(homePanel);
        homePanel.setLayout(new CardLayout());
        
        

        JPanel homepage = new JPanel();
        homePanel.add(homepage, "Home");
        homepage.setLayout(null);
        
		
        JLabel welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setFont(new Font("Tahoma", Font.PLAIN, 85));
        welcomeLabel.setBounds(45, 11, 501, 81);
        homepage.add(welcomeLabel);
        
        JPanel panel = new JPanel();
        panel.setBorder(new EtchedBorder(EtchedBorder.RAISED, new Color(0, 139, 139), null));
        panel.setBounds(48, 158, 310, 378);
        homepage.add(panel);
        panel.setLayout(null);
        
        try {
            Connection connection = Conn.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cms.student WHERE username = '" + username + "'");

            if (resultSet.next()) {
                // Fetch student information
            	course = resultSet.getString("courses");
                email = resultSet.getString("email");
                phone = resultSet.getString("phone");
                id = resultSet.getString("id");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        
        JLabel lblFirstName = new JLabel("Name : "+name);
        lblFirstName.setHorizontalAlignment(SwingConstants.LEFT);
        lblFirstName.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblFirstName.setBounds(10, 27, 290, 46);
        panel.add(lblFirstName);
        
  
        
        JLabel lblEmail = new JLabel("Email : "+email);
        lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
        lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblEmail.setBounds(10, 84, 290, 46);
        panel.add(lblEmail);
        
        JLabel lblPhone = new JLabel("Phone : "+phone);
        lblPhone.setHorizontalAlignment(SwingConstants.LEFT);
        lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblPhone.setBounds(10, 141, 290, 46);
        panel.add(lblPhone);
        
        JLabel lblStudentNumber = new JLabel("Student ID : "+id);
        lblStudentNumber.setHorizontalAlignment(SwingConstants.LEFT);
        lblStudentNumber.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblStudentNumber.setBounds(10, 198, 290, 46);
        panel.add(lblStudentNumber);
        
        JButton enroll = new JButton("Enroll");
        enroll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                EnrollStudent enrollStudentFrame = new EnrollStudent(id, course, name);
                enrollStudentFrame.setVisible(true);
            }
        });
        enroll.setBounds(109, 283, 98, 23);
        panel.add(enroll);
        
        JLabel nameLabel = new JLabel("username: "+username);
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        nameLabel.setBounds(45, 103, 260, 46);
        homepage.add(nameLabel);
        
        JLabel roleLabel = new JLabel("role"+"("+role+")");
        roleLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        roleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        roleLabel.setBounds(315, 103, 231, 46);
        homepage.add(roleLabel);

        JLabel logOutLogo_2 = new JLabel("");
        logOutLogo_2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int answer = JOptionPane.showConfirmDialog(null, "Do you want to Logout?", "Logout",
                        JOptionPane.YES_NO_OPTION);
                if (answer == 0) {
                    contentPane.setVisible(false);
                    dispose();
                    Login naya = new Login();
                    naya.setVisible(true);

                }
            }
        });
        logOutLogo_2.setIcon(new ImageIcon(StudentPanel.class.getResource("/Images/power-switch.png")));
        logOutLogo_2.setBounds(60, 381, 40, 40);
        menu.add(logOutLogo_2);
        
        JButton result = new JButton("Result");
        
        result.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		CardLayout cardLayout = (CardLayout) (homePanel.getLayout());
                cardLayout.show(homePanel, "result");
        	}
        });
        result.setBounds(21, 323, 128, 40);
        menu.add(result);

        coursesPanel = new JPanel();
        homePanel.add(coursesPanel, "CoursesPanel");
        coursesPanel.setLayout(null);
        

        JScrollPane scrollPane_2 = new JScrollPane();
        scrollPane_2.setBounds(39, 127, 501, 158);
        coursesPanel.add(scrollPane_2);

        coursesTable = new JTable();
        scrollPane_2.setViewportView(coursesTable);
        coursesTable.setModel(new DefaultTableModel(
        	    new Object[][]{},
        	    new String[]{"Teacher", "Module Name"} 
        	));
        
        
        

        JLabel Searchc_1 = new JLabel("Search Course");
        Searchc_1.setForeground(new Color(178, 34, 34));
        Searchc_1.setBounds(40, 64, 189, 20);
        coursesPanel.add(Searchc_1);
        
        JLabel CourseHeader = new JLabel("Courses");
        CourseHeader.setFont(new Font("Tahoma", Font.PLAIN, 18));
        CourseHeader.setHorizontalAlignment(SwingConstants.CENTER);
        CourseHeader.setBounds(195, 11, 115, 42);
        coursesPanel.add(CourseHeader);
        
        courseSearch = new JComboBox<>();
        getCoursesFromDatabase(); // Populate the uniqueCourses set
        courseSearch.setModel(new DefaultComboBoxModel<>(uniqueCourses.toArray(new String[0])));
        courseSearch.setBounds(39, 95, 271, 22);
        coursesPanel.add(courseSearch);
        
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(156, 339, 252, 165);
        coursesPanel.add(scrollPane_1);
        
        enrolledCourses = new JTable();
        scrollPane_1.setViewportView(enrolledCourses);
        enrolledCourses.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"level", "Enrolled modules"
        	}
        ));
        
        fetchEnrolledCourses();
        
        JLabel lblNewLabel_1 = new JLabel("Enrolled ");
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setFont(new Font("Segoe UI Historic", Font.PLAIN, 14));
        lblNewLabel_1.setBounds(156, 308, 252, 29);
        coursesPanel.add(lblNewLabel_1);
                
        // Add ActionListener to courseSearch JComboBox
        courseSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchCourseDataFromDatabase(); // Call method to fetch course data when selection changes
            }
        });
        
        resultPanel = new JPanel();
        homePanel.add(resultPanel, "result");
        resultPanel.setLayout(null);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(96, 152, 339, 161);
        resultPanel.add(scrollPane);
        
        table = new JTable();
        table.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"Course", "Result"
        	}
        ));
        scrollPane.setViewportView(table);
        
        marks(name);
        
        JLabel lblNewLabel = new JLabel("Result");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblNewLabel.setBounds(189, 80, 140, 61);
        resultPanel.add(lblNewLabel);
        
        
       
        
    }


	private void marks(String name) {
		// TODO Auto-generated method stub
		DefaultTableModel model = (DefaultTableModel) table.getModel();

        // Clear existing data in the table
        model.setRowCount(0);

        try {
            Connection connection = Conn.getConnection();
            String query = "SELECT module, marks FROM marking WHERE student_name = ?";
            
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String Module = resultSet.getString("module");
                int marking = resultSet.getInt("marks");
                
                // Add a row to the table model
                model.addRow(new Object[] { Module, marking });
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
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
    
    private void fetchCourseDataFromDatabase() {
    	DefaultTableModel model = (DefaultTableModel) coursesTable.getModel();
        model.setRowCount(0); // Clear existing data

        try {
            Connection connection = Conn.getConnection();
            Statement statement = connection.createStatement();
            
            // Get the selected course from the JComboBox
	        String selectedCourse = courseSearch.getSelectedItem().toString();
	        
	        // Construct the SQL query based on the selected course
	        String query = "SELECT teacher, module FROM assign WHERE course = '" + selectedCourse + "'";
	        ResultSet resultSet = statement.executeQuery(query);

	        while (resultSet.next()) {
	            String teacher = resultSet.getString("teacher");
	            String moduleName = resultSet.getString("module");

	         // Create a Vector for the current row
	            Vector<Object> row = new Vector<>();
	            row.add(teacher); // Add teacher
	            row.add(moduleName); // Add module name

	            // Add the row to the table model
	            model.addRow(row);
	        }

	        // Close resources
	        resultSet.close();
	        statement.close();
	        connection.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
    }
    
    private void fetchEnrolledCourses() {
        DefaultTableModel model = (DefaultTableModel) enrolledCourses.getModel();
        model.setRowCount(0); // Clear existing data

        try {
            Connection c = Conn.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM enrollment WHERE id = ?");
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String module = rs.getString("module");
                int level = rs.getInt("level");

                model.addRow(new Object[]{level, module});
            }

            rs.close();
            ps.close();
            c.close();
        } catch (Exception error) {
            System.out.println("Error:\n" + error);
        }
    }
}
