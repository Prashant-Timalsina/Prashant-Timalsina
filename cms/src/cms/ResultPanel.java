package cms;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;

public class ResultPanel extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ResultPanel frame = new ResultPanel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @param table 
	 */
	public ResultPanel() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 489,522);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 452, 427);
        getContentPane().add(scrollPane);

        table = new JTable();
        table.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "Student ID", "Name", "Level", "Total Marks", "Remarks"
                }
        ));
        scrollPane.setViewportView(table);
        
        JButton save = new JButton("Print");
        save.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		saveToCSV();
        	}
        });
        save.setBounds(183, 449, 118, 23);
        getContentPane().add(save);

        // Fetch results from the database when the panel is created
        fetchResultFromDatabase();
    }

    /**
     * Fetches results from the database and populates the table.
     */


	private void fetchResultFromDatabase() {
	    DefaultTableModel model = (DefaultTableModel) table.getModel();
	    model.setRowCount(0); // Clear existing rows
	    
	    Set<String> processedIds = new HashSet<>(); // To track processed student IDs with their levels
	    
	    try {
	        // Establish connection to the database
	        Connection connection = Conn.getConnection();

	        // Query to fetch distinct id, name, and level from enrollment
	        String fetchEnrollmentSQL = "SELECT DISTINCT id, name, level FROM enrollment";
	        PreparedStatement preparedStatement = connection.prepareStatement(fetchEnrollmentSQL);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        // Iterate through the result set
	        while (resultSet.next()) {
	            String id = resultSet.getString("id");
	            String name = resultSet.getString("name");
	            int level = resultSet.getInt("level");
	            
	            // Check if the ID and level combination is already processed
	            String combinedIdLevel = id + "_" + level;
	            if (processedIds.contains(combinedIdLevel)) {
	                continue; // Skip if already processed
	            }
	            
	            // Add ID and level combination to the processed set
	            processedIds.add(combinedIdLevel);

	            // Query to calculate total marks for each student at the given level
	            String totalMarksSQL = "SELECT SUM(marks) AS total_marks FROM marking WHERE student_name = ? AND level = ?";
	            PreparedStatement totalMarksStatement = connection.prepareStatement(totalMarksSQL);
	            totalMarksStatement.setString(1, name);
	            totalMarksStatement.setInt(2, level);
	            ResultSet totalMarksResultSet = totalMarksStatement.executeQuery();

	            int totalMarks = 0;
	            if (totalMarksResultSet.next()) {
	                totalMarks = totalMarksResultSet.getInt("total_marks");
	            }

	            // Close the totalMarksResultSet and totalMarksStatement
	            totalMarksResultSet.close();
	            totalMarksStatement.close();

	            // If total marks are zero, skip adding the row to the table
	            if (totalMarks == 0) {
	                continue;
	            }

	            // Determine remarks based on total marks
	            String remarks = (totalMarks >= 160) ? "Pass" : "Fail";

	            // Add row to the table
	            model.addRow(new Object[]{id, name, level, totalMarks, remarks});
	        }

	        // Close resources
	        resultSet.close();
	        preparedStatement.close();
	        connection.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	
	private void saveToCSV() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        String filename = "result.csv";
        boolean overwrite = false;
        
        // Check if the file already exists
        if (Files.exists(Paths.get(filename))) {
            // File already exists, prompt the user to choose whether to overwrite or update it
            int option = JOptionPane.showConfirmDialog(this, "File already exists. Do you want to overwrite it?", "File Exists", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                overwrite = true;
            }
        } else {
            overwrite = true; // If file doesn't exist, proceed with saving
        }
        
        if (overwrite) {
            // Proceed with saving
            try (FileWriter writer = new FileWriter(filename)) {
                for (int i = 0; i < model.getColumnCount(); i++) {
                    writer.write(model.getColumnName(i) + ",");
                }
                writer.write("\n");
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        writer.write(model.getValueAt(i, j).toString() + ",");
                    }
                    writer.write("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        JOptionPane.showMessageDialog(contentPane, "File is Saved", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

}
