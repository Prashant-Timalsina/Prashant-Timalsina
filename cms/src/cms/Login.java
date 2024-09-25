package cms;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField userfield;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
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
    public Login() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 408, 465);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // Create an error label to display messages
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        errorLabel.setBounds(95, 496, 300, 20);
        contentPane.add(errorLabel);
        
        JLabel LoginText = new JLabel("Welcome to Herald College");
        LoginText.setFont(new Font("Tahoma", Font.BOLD, 15));
        LoginText.setHorizontalAlignment(SwingConstants.CENTER);
        LoginText.setBounds(10, 21, 372, 24);
        contentPane.add(LoginText);
        
        JLabel userlabel = new JLabel("Enter Username");
        userlabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        userlabel.setBounds(95, 167, 123, 17);
        contentPane.add(userlabel);
        
        userfield = new JTextField("");
        userfield.setBounds(95, 195, 207, 20);
        contentPane.add(userfield);
        userfield.setColumns(10);
        
        JLabel passwordlabel = new JLabel("Enter Password");
        passwordlabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        passwordlabel.setBounds(95, 240, 123, 17);
        contentPane.add(passwordlabel);
        
        passwordField = new JPasswordField();
        passwordField.setBounds(95, 268, 207, 20);
        contentPane.add(passwordField);
        
        JComboBox<String> roles = new JComboBox<>();
        roles.setModel(new DefaultComboBoxModel<>(new String[] {"(choose your role)", "Student", "Teacher", "Admin"}));
        roles.setBounds(95, 322, 207, 22);
        contentPane.add(roles);
        
        JButton loginbtn = new JButton("Login");
        loginbtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String username = userfield.getText();
                String pass = new String(passwordField.getPassword());
                String role = (String) roles.getSelectedItem();
                
                if (!role.equals("(choose your role)") && !username.isEmpty() && !pass.isEmpty()) {
                    try {
                        Connection con = Conn.getConnection();
                        String query = "";
                        switch (role) {
                            case "Student":
                                query = "SELECT * FROM student WHERE username = ? AND password = ?";
                                break;
                            case "Teacher":
                                query = "SELECT * FROM teacher WHERE username = ? AND password = ?";
                                break;
                            case "Admin":
                                query = "SELECT * FROM admin WHERE username = ? AND password = ?";
                                break;
                        }

                        try (PreparedStatement statement = con.prepareStatement(query)) {
                            statement.setString(1, username);
                            statement.setString(2, pass);
                            ResultSet result = statement.executeQuery();

                            if (result.next()) {
                                // Login successful
                                String name = result.getString("name");
                                String email = result.getString("email");
                                String id = result.getString("ID");
                                String course = result.getString("courses");
                                String password = result.getString("password");
                                String phone = result.getString("phone");
                                
                                User  loggedInUser = new User(name, email, id , role, username, course , password, phone);

                                openFrameForRole(loggedInUser);
                                dispose();
                            } else {
                                // Login failed
                                errorLabel.setText("Invalid username or password for " + role);
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } finally {
                            con.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    errorLabel.setText("Please fill in all fields and select a role.");
                }
            }
        });
        loginbtn.setBounds(138, 380, 89, 23);
        contentPane.add(loginbtn);
        
        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.setIcon(new ImageIcon(Login.class.getResource("/Images/aherald zz.png")));
        lblNewLabel.setBounds(138, 56, 100, 100);
        contentPane.add(lblNewLabel);
    }
    
    private void openFrameForRole(User user) {
        switch (user.getRole()) {
            case "Student":
                StudentPanel studentFrame = new StudentPanel(user);
                studentFrame.setVisible(true);
                break;
            case "Teacher":
                TeacherPanel teacherFrame = new TeacherPanel(user);
                teacherFrame.setVisible(true);
                break;
            case "Admin":
                Admin adminFrame = new Admin(user);
                adminFrame.setVisible(true);
                break;
        }
    }
}
