package cms;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
    private String name;
    private String email;
    private String id;
    private String role;
    private String username;
    private String course;
    private String password;
    private String phone;

    // Constructor for login
    public User(String name, String email, String id, String role, String username, String course, String password,String phone) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.role = role;
        this.username = username;
        this.course = course;
        this.password = password;
        this.phone=(phone);

    }

    // For all panels
    public User(String name, String role, String username, String password) {
        this.username = username;
        this.role = role;
        this.name = name;
        this.password = password;

    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

    private void setData(String role) {
        Connection c = null;
        Statement s = null;
        ResultSet rs = null;

        try {
            c = Conn.getConnection(); // Assuming you have a Connection class
            s = c.createStatement();

            // Execute query based on the specified role
            String query = "";
            if (role.equals("Student")) {
                query = "SELECT * FROM student WHERE username = '" + username + "'";
            } else if (role.equals("Teacher")) {
                query = "SELECT * FROM teacher WHERE username = '" + username + "'";
            } else if (role.equals("Admin")) {
                query = "SELECT * FROM admin WHERE username = '" + username + "'";
            } else {
                // Handle invalid role
                System.out.println("Invalid role specified");
                return;
            }

            rs = s.executeQuery(query);

            if (rs.next()) {
                // Set common properties for all roles
                this.role = rs.getString("role");
                this.password = rs.getString("password");

                // Set role-specific properties
                
                    this.name = rs.getString("name");
                    this.id = rs.getString("id");
                    this.email = rs.getString("email");
                    this.course = rs.getString("course");
                    this.phone = rs.getString("phone");

            } else {
                // Handle case where no user with specified username and role is found
                System.out.println("User not found for role: " + role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (s != null) s.close();
                if (c != null) c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
