package cms;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Canvas;
import java.awt.SystemColor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.sql.DriverManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import cms.Login;
import javax.swing.JPasswordField;

public class Signup extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField namefield;
	private JTextField userfield;
	private JTextField emailfield;
	private JTextField phonefield;
	private JLabel errorLabel;
	private JTextField idF;
	private JPasswordField passW;
 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Signup frame = new Signup("");
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
	
	private Set<String> uniqueCourses = new HashSet<>();
	
	public Signup(String role) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Initialize the error label
        errorLabel = new JLabel("");
        errorLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 11));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setBounds(65, 464, 400, 20);
        contentPane.add(errorLabel);
		
		JLabel lblNewLabel = new JLabel("New Account Create");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 464, 19);
		contentPane.add(lblNewLabel);
		
		JLabel heraldlogo = new JLabel("");
		heraldlogo.setIcon(new ImageIcon(Signup.class.getResource("/Images/aherald zz.png")));
		heraldlogo.setHorizontalAlignment(SwingConstants.CENTER);
		heraldlogo.setBackground(Color.WHITE);
		heraldlogo.setBounds(187, 41, 100, 100);
		contentPane.add(heraldlogo);
		
		JLabel namelabel = new JLabel("Name");
		namelabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		namelabel.setBounds(65, 213, 46, 14);
		contentPane.add(namelabel);
		
		JLabel userlabel = new JLabel("Username");
		userlabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		userlabel.setBounds(255, 213, 65, 14);
		contentPane.add(userlabel);
		
		
		
		JLabel emaillabel = new JLabel("Email");
		emaillabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		emaillabel.setBounds(65, 301, 46, 14);
		contentPane.add(emaillabel);
		
		JLabel phonelabel = new JLabel("Phone Number");
		phonelabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		phonelabel.setBounds(65, 346, 89, 14);
		contentPane.add(phonelabel);
		
		JTextField roles = new JTextField(role);
		roles.setEditable(false);
		roles.setBounds(65, 398, 161, 22);
		contentPane.add(roles);
		
		JComboBox courses = new JComboBox();
        getCoursesFromDatabase(); // Populate the uniqueCourses set
        courses.setModel(new DefaultComboBoxModel<>(uniqueCourses.toArray(new String[0])));
        courses.setBounds(288, 398, 177, 22);
        contentPane.add(courses);
		
		JButton signupbtn = new JButton("Register");
		signupbtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String name = namefield.getText();
				String user = userfield.getText();
				char[] passwordChars = passW.getPassword();
				String pass = new String(passwordChars);
				String id = idF.getText();
				String email = emailfield.getText();
				String phone = phonefield.getText() ;
				String role = roles.getText() ;
				String course = courses.getSelectedItem().toString() ;
				
		        // Validate email using regex
				String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		        if (!email.matches(emailRegex)) {
	                showError("Invalid email format");
		        	return;
		        }
		        
		        String passRegex = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";
		        if (!pass.matches(passRegex)) {
		            showError("Invalid password format. Password must contain at least one digit, one letter, and be at least 8 characters long.");
		            return;
		        }

		        
		        // Validate phone using regex
		        String phoneRegex = "^\\d{10}$";
		        if (!phone.matches(phoneRegex)) {
	                showError("Invalid phone number format");
		            return;
		        }
		        				
		        registerUser(name,user,pass,id, email, phone, role, course);
				
			}
		});
		
		
		signupbtn.setBounds(125, 490, 195, 23);
		contentPane.add(signupbtn);
		
		namefield = new JTextField();
		namefield.setColumns(10);
		namefield.setBounds(65, 226, 169, 20);
		contentPane.add(namefield);
		
		userfield = new JTextField();
		userfield.setColumns(10);
		userfield.setBounds(255, 226, 169, 20);
		contentPane.add(userfield);
		
		emailfield = new JTextField();
		emailfield.setColumns(10);
		emailfield.setBounds(65, 315, 222, 20);
		contentPane.add(emailfield);
		
		phonefield = new JTextField();
		phonefield.setColumns(10);
		phonefield.setBounds(65, 360, 222, 20);
		contentPane.add(phonefield);
		
		
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(Signup.class.getResource("/Images/user.png")));
		lblNewLabel_1.setBounds(10, 206, 40, 40);
		contentPane.add(lblNewLabel_1);
		
		
		
		JLabel lblNewLabel_1_2 = new JLabel("");
		lblNewLabel_1_2.setIcon(new ImageIcon(Signup.class.getResource("/Images/email.png")));
		lblNewLabel_1_2.setBounds(10, 295, 40, 40);
		contentPane.add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_3 = new JLabel("");
		lblNewLabel_1_3.setIcon(new ImageIcon(Signup.class.getResource("/Images/phone.png")));
		lblNewLabel_1_3.setBounds(10, 340, 40, 40);
		contentPane.add(lblNewLabel_1_3);
		
		JLabel lblNewLabel_1_4 = new JLabel("");
		lblNewLabel_1_4.setIcon(new ImageIcon(Signup.class.getResource("/Images/roles.png")));
		lblNewLabel_1_4.setBounds(10, 391, 40, 40);
		contentPane.add(lblNewLabel_1_4);
		
		JLabel lblNewLabel_1_5 = new JLabel("");
		lblNewLabel_1_5.setIcon(new ImageIcon(Signup.class.getResource("/Images/courses.png")));
		lblNewLabel_1_5.setBounds(236, 391, 40, 40);
		contentPane.add(lblNewLabel_1_5);
		
		JLabel ID = new JLabel("NewID");
		ID.setFont(new Font("Tahoma", Font.BOLD, 11));
		ID.setBounds(65, 257, 46, 14);
		contentPane.add(ID);
		
		idF = new JTextField();
		idF.setColumns(10);
		idF.setBounds(65, 270, 169, 20);
		contentPane.add(idF);
		
		Random random = new Random();
        int randomNumber = random.nextInt(100000); // Modify the range as needed
        idF.setText(String.valueOf(randomNumber));
        
        JLabel lblpass = new JLabel("Password");
        lblpass.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblpass.setBounds(255, 257, 65, 14);
        contentPane.add(lblpass);
        
        passW = new JPasswordField();
        passW.setColumns(10);
        passW.setBounds(255, 270, 169, 20);
        contentPane.add(passW);
	}
	
	private void registerUser(String name, String user, String pass, String id, String email, String phone, String role, String course) {
	    try {
	        // Get the database connection
	        try (Connection con = Conn.getConnection()) {
	            System.out.println("Connection Success");

	            // Check if the ID already exists
	            String checkQuery = "SELECT * FROM STUDENT WHERE ID = ? OR ID = ?";
	            try (PreparedStatement checkStatement = con.prepareStatement(checkQuery)) {
	                checkStatement.setString(1, id);
	                checkStatement.setString(2, id);

	                try (ResultSet resultSet = checkStatement.executeQuery()) {
	                    if (resultSet.next()) {
	                        // ID already exists, show an error message
	                        JOptionPane.showMessageDialog(contentPane, "ID already exists. Please try again with another ID.", "Error", JOptionPane.ERROR_MESSAGE);
	                        return;
	                    }
	                }
	            }

	            // Validate name and username
	            if (name.isEmpty() || user.isEmpty()) {
	                // Display an error message or handle the validation failure accordingly
	                JOptionPane.showMessageDialog(null, "Name and username cannot be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
	                return;
	            }

	            // Insert data into the appropriate table
	            String insertQuery;
	            if ("Student".equals(role)) {
	                insertQuery = "INSERT INTO student (`name`, `username`, `password`, `ID`, `email`, `phone`, `roles`, `courses`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	            } else if ("Teacher".equals(role)) {
	                insertQuery = "INSERT INTO teacher (`name`, `username`, `password`, `ID`, `email`, `phone`, `roles`, `courses`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	            } else {
	                // Handle the case for other roles if needed
	                JOptionPane.showMessageDialog(contentPane, "Invalid role specified", "Error", JOptionPane.ERROR_MESSAGE);
	                return;
	            }

	            try (PreparedStatement insertStatement = con.prepareStatement(insertQuery)) {
	                insertStatement.setString(1, name);
	                insertStatement.setString(2, user);
	                insertStatement.setString(3, pass);
	                insertStatement.setString(4, id);
	                insertStatement.setString(5, email);
	                insertStatement.setString(6, phone);
	                insertStatement.setString(7, role);
	                insertStatement.setString(8, course);

	                // Execute the insert query
	                insertStatement.executeUpdate();

	                // Registration successful message
	                showSuccess("Registration successful");

	                try {
	                    // Add a delay of 1500 milliseconds (1.5 second)
	                    Thread.sleep(1500);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	                
	                // Dispose of the current Create Account frame
	                dispose();

	                
	            }
	        }
	    } catch (SQLException er) {
	        er.printStackTrace();
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


	
	private void showError(String errorMessage) {
		errorLabel.setForeground(Color.red);
        errorLabel.setText(errorMessage);
    }

    private void showSuccess(String successMessage) {
        errorLabel.setForeground(Color.magenta);
        errorLabel.setText(successMessage);
    }
}
