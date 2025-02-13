import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginScreen extends JFrame {

    public LoginScreen() {
        // Set the frame properties
        setTitle("Login Page - ATM System");
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.WHITE);

        // Login panel
        JPanel loginPanel = new JPanel();
        loginPanel.setPreferredSize(new Dimension(500, 600)); // Set preferred size for centering
        loginPanel.setBackground(new Color(54, 69, 79)); // Charcoal Gray

        // Title Label
        JLabel titleLabel = new JLabel("Login Here...");
        titleLabel.setBounds(160, 40, 250, 100);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 30));

        // Username label and text field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(40, 150, 150, 100);
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));

        final JTextField usernameField = new JTextField();
        usernameField.setBounds(170, 180, 280, 40);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        usernameField.setForeground(Color.BLACK);
        usernameField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // Password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(40, 250, 150, 100);
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));

        final JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(170, 280, 280, 40);
        passwordField.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(75, 400, 150, 40);
        loginButton.setBackground(new Color(34, 139, 34)); // Forest Green
        loginButton.setForeground(Color.WHITE); // White text
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 20));

        // Login button action listener
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = usernameField.getText().trim(); // Trim leading/trailing spaces
                char[] enteredPassword = passwordField.getPassword();

                // Check if fields are empty
                if (enteredUsername.isEmpty() || enteredPassword.length == 0) {
                    JOptionPane.showMessageDialog(null, "Please enter both username and password.");
                    return;
                }

                // Connect to the database and validate credentials
                try (Connection connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/atm_system", "root", "Sanika@4026")) {

                    // SQL query to fetch user details
                    String sql = "SELECT user_id, name, password FROM users WHERE email = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, enteredUsername); // Bind username to query
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        String userId = resultSet.getString("user_id");  // Get the user ID
                        String userName = resultSet.getString("name");
                        String storedPassword = resultSet.getString("password");

                        // Trim the entered password and stored password before comparing
                        if (storedPassword.trim().equals(new String(enteredPassword).trim())) {
                            JOptionPane.showMessageDialog(null, "Login Successful!");

                            // Ensure no unwanted frames remain open
                            closeAllWindows();

                            // Open MainPage with user ID
                            MainPage mainPage = new MainPage(userId);
                            mainPage.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database connection error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        // Register button
        JButton signupButton = new JButton("Sign Up");
        signupButton.setBounds(275, 400, 150, 40);
        signupButton.setBackground(new Color(34, 139, 34)); // Forest Green
        signupButton.setForeground(Color.WHITE); // White text
        signupButton.setFont(new Font("Tahoma", Font.BOLD, 20));

        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUp().setVisible(true); // Open the SignUp window
                dispose(); // Close the login window
            }
        });

        // Add components to login panel
        loginPanel.setLayout(null);
        loginPanel.add(titleLabel);
        loginPanel.add(usernameLabel);
        loginPanel.add(passwordLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(signupButton);

        // Add components to the frame using GridBagLayout for centering
        setLayout(new GridBagLayout()); // Use GridBagLayout for centering
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(loginPanel, gbc); // Center login panel

        // Make the frame visible
        setVisible(true);
    }

    /**
     * Closes all currently open windows to prevent unwanted frames.
     */
    private void closeAllWindows() {
        for (Window window : Window.getWindows()) {
            window.dispose();
        }
    }

    public static void main(String[] args) {
        // Ensuring the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new LoginScreen());
    }
}
