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

public class TeacherPanel extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField semesterField;
    private JPanel homePanel;
    private JPanel coursesPanel;
    private JLabel CourseHeader;
    private JPanel resultPanel;
    private JTable table;
    private JTable coursesTable;
    
    
    private JComboBox<String> courseSearch,markModules;
	private Set<String> uniqueCourses = new HashSet<>();

	
	private String username;
    private String course;
    private String email;
    private String phone;
    private String id;
    private String name;
    private String role;
    
    private User user;
     
    private JTable assign;
    private String teacherName;
    private JTable marking;



    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	User user= new User("","","","");
                    TeacherPanel frame = new TeacherPanel(user);
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
    public TeacherPanel(User user) {
    	
    	this.role = user.getRole();
    	this.username = user.getUsername();
    	this.name = user.getName();
    	this.course = user.getCourse();
    	this.id = user.getId();
    	this.phone=user.getPhone();
    	this.email=user.getEmail();

    	this.teacherName= name;
    	
        setTitle("Teacher");

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
        clzlogo.setIcon(new ImageIcon(TeacherPanel.class.getResource("/Images/aherald zz.png")));
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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cms.teacher WHERE username = '" + user + "'");

            if (resultSet.next()) {
                // Fetch student information
                name = resultSet.getString("name");
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
        lblFirstName.setBounds(10, 51, 290, 46);
        panel.add(lblFirstName);
 
        JLabel lblEmail = new JLabel("Email : "+email);
        lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
        lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblEmail.setBounds(10, 116, 290, 46);
        panel.add(lblEmail);
        
        JLabel lblPhone = new JLabel("Phone : "+phone);
        lblPhone.setHorizontalAlignment(SwingConstants.LEFT);
        lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblPhone.setBounds(10, 179, 290, 46);
        panel.add(lblPhone);
        
        JLabel lblStudentNumber = new JLabel("Teacher ID : "+id);
        lblStudentNumber.setHorizontalAlignment(SwingConstants.LEFT);
        lblStudentNumber.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblStudentNumber.setBounds(10, 236, 290, 46);
        panel.add(lblStudentNumber);
        
        JLabel nameLabel = new JLabel(username);
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        nameLabel.setBounds(55, 103, 160, 46);
        homepage.add(nameLabel);
        
        JLabel roleLabel = new JLabel("("+role+")");
        roleLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        roleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        roleLabel.setBounds(295, 103, 136, 46);
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
        logOutLogo_2.setIcon(new ImageIcon(TeacherPanel.class.getResource("/Images/power-switch.png")));
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

        coursesTable = new JTable(); // Renamed to coursesTable
        scrollPane_2.setViewportView(coursesTable);
        coursesTable.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "Level","Semester", "Module Name"
                }
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
        
        assign = new JTable();
        assign.setModel(new DefaultTableModel(
        	new Object[][] {},
        	new String[] {
        		"Modulas"
        	}
        ));
        assign.setBounds(165, 385, 265, 93);
        coursesPanel.add(assign);
        
     // Assuming table_2 is already initialized and added to coursesPanel

        try {
            Connection c = Conn.getConnection();
            Statement s = c.createStatement();
            String stac = "SELECT module FROM assign WHERE teacher = '" + name + "'";
            ResultSet rs = s.executeQuery(stac);

            DefaultTableModel model = (DefaultTableModel) assign.getModel();

            while (rs.next()) {
                String module = rs.getString("module");
                // Add each module to the table model
                model.addRow(new Object[]{module});
            }

            rs.close();
            s.close();
            c.close();
        } catch (Exception ea) {
            ea.printStackTrace();
        }

        
        JLabel lblNewLabel_3 = new JLabel("Assigned Modules");
        lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_3.setBounds(165, 345, 265, 29);
        coursesPanel.add(lblNewLabel_3);
        
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
        
        markModules = new JComboBox<String>();
        markModules.setBounds(91, 83, 381, 31);
        resultPanel.add(markModules);
        
        try {
            Connection connection = Conn.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT module FROM assign WHERE teacher = '" + name + "'");

            while (resultSet.next()) {
                String module = resultSet.getString("module");
                // Add each module to the markModules JComboBox
                markModules.addItem(module);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        JLabel lblNewLabel = new JLabel("Select Module");
        lblNewLabel.setBounds(91, 43, 104, 29);
        resultPanel.add(lblNewLabel);
        
        JButton mark = new JButton("Mark Modules");
        mark.setBounds(142, 161, 243, 31);
        mark.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected module from markModules JComboBox
                String selectedModule = markModules.getSelectedItem().toString();

                // Pass the selected module and teacher's name to Marking.java
                Marking markingWindow = new Marking(selectedModule, teacherName);
                markingWindow.setVisible(true);
            }
        });


        resultPanel.add(mark);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(91, 228, 381, 290);
        resultPanel.add(scrollPane);
        
        marking = new JTable();
        scrollPane.setViewportView(marking);
        marking.setModel(new DefaultTableModel(
        	new Object[][] {
        	},
        	new String[] {
        		"Student Name", "module", "Marks"
        	}
        ));

        // Call the marku function with the selected module
        
        marku(name);
        
    }
    
    /**
     * Fetch course data from the database.
     */
    private void fetchCourseDataFromDatabase() {
    	DefaultTableModel model = (DefaultTableModel) coursesTable.getModel();
        model.setRowCount(0); // Clear existing data

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
	            String moduleName = resultSet.getString("module");

	            // Create a Vector for the current row
	            Vector<Object> row = new Vector<>();
	            row.add(level);
	            row.add(semester);
	            row.add(moduleName);

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
    
    private void marku(String name) {
        DefaultTableModel model = (DefaultTableModel) marking.getModel();

        // Clear existing data in the table
        model.setRowCount(0);

        try {
            Connection connection = Conn.getConnection();
            String query = "SELECT student_name, module, marks FROM marking WHERE module IN (SELECT module FROM assign WHERE teacher = ?)";
            
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String studentName = resultSet.getString("student_name");
                String module = resultSet.getString("module");
                int marks = resultSet.getInt("marks");

                // Add a row to the table model
                model.addRow(new Object[]{studentName, module, marks});
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}