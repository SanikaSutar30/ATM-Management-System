import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
//import javax.swing.border.LineBorder;
import java.util.HashSet;
import java.util.UUID;
import java.sql.*;
import java.math.BigDecimal;

public class SignUp extends JFrame{
    private static HashSet<String> usedPasswords = new HashSet<>();

    public SignUp() {
        final JFrame frame = new JFrame("Sign Up - ATM System");
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setBackground(Color.WHITE);

        // Sign-up panel
        JPanel signupPanel = new JPanel();
        signupPanel.setPreferredSize(new Dimension(550, 750));  // Increased height for ATM PIN
        signupPanel.setBackground(new Color(54, 69, 79)); // Charcoal Gray

        // Title Label
        JLabel titleLabel = new JLabel("Sign Up Here...");
        titleLabel.setBounds(160, 20, 250, 70);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 30));

        // Name label and text field
        JLabel nameLabel = new JLabel("Name :");
        nameLabel.setBounds(50, 120, 280, 40);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));

        final JTextField nameField = new JTextField();
        nameField.setBounds(210, 120, 280, 40);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        nameField.setForeground(Color.BLACK);
        nameField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // Age label and text field
        JLabel ageLabel = new JLabel("Age :");
        ageLabel.setBounds(50, 210, 280, 40);
        ageLabel.setForeground(Color.WHITE);
        ageLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));

        final JTextField ageField = new JTextField();
        ageField.setBounds(210, 210, 280, 40);
        ageField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        ageField.setForeground(Color.BLACK);
        ageField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // Phone label and text field
        JLabel phoneLabel = new JLabel("Phone No. :");
        phoneLabel.setBounds(50, 280, 280, 40);
        phoneLabel.setForeground(Color.WHITE);
        phoneLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));

        final JTextField phoneField = new JTextField();
        phoneField.setBounds(210, 280, 280, 40);
        phoneField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        phoneField.setForeground(Color.BLACK);
        phoneField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // Email label and text field
        JLabel emailLabel = new JLabel("Email Id:");
        emailLabel.setBounds(50, 360, 280, 40);
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));

        final JTextField emailField = new JTextField();
        emailField.setBounds(210, 360, 280, 40);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        emailField.setForeground(Color.BLACK);
        emailField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // Password label and password field
        JLabel passwordLabel = new JLabel("Password :");
        passwordLabel.setBounds(50, 440, 280, 40);
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));

        final JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(210, 440, 280, 40);
        passwordField.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // Confirm Password label and password field
        JLabel confirmPasswordLabel = new JLabel("Confirm Pass. :");
        confirmPasswordLabel.setBounds(50, 520, 280, 40);
        confirmPasswordLabel.setForeground(Color.WHITE);
        confirmPasswordLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));

        final JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(210, 520, 280, 40);
        confirmPasswordField.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        confirmPasswordField.setForeground(Color.BLACK);
        confirmPasswordField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // ATM PIN label and password field
        JLabel atmPinLabel = new JLabel("ATM PIN :");
        atmPinLabel.setBounds(50, 600, 280, 40);
        atmPinLabel.setForeground(Color.WHITE);
        atmPinLabel.setFont(new Font("Sans Serif", Font.BOLD, 20));

        final JPasswordField atmPinField = new JPasswordField();
        atmPinField.setBounds(210, 600, 280, 40);
        atmPinField.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        atmPinField.setForeground(Color.BLACK);
        atmPinField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // Sign Up button
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(185, 670, 180, 40);
        signUpButton.setBackground(new Color(34, 139, 34)); // Forest Green
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("Tahoma", Font.BOLD, 20));

        // Sign Up button action listener
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String enteredName = nameField.getText();
                String enteredAge = ageField.getText();
                String enteredPhone = phoneField.getText();
                String enteredEmail = emailField.getText();
                char[] enteredPassword = passwordField.getPassword();
                char[] enteredConfirmPassword = confirmPasswordField.getPassword();
                char[] enteredAtmPin = atmPinField.getPassword();

                // Validate inputs
                if (enteredName.isEmpty() || enteredAge.isEmpty() || enteredPhone.isEmpty() || enteredEmail.isEmpty()
                        || enteredPassword.length == 0 || enteredConfirmPassword.length == 0 || enteredAtmPin.length == 0) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.");
                    return;
                }

                // Validate age (must be a number)
                try {
                    int age = Integer.parseInt(enteredAge);
                    if (age <= 0) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid age.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Age must be a number.");
                    return;
                }

                // Validate phone number (basic check for length)
                if (enteredPhone.length() != 10) {
                    JOptionPane.showMessageDialog(frame, "Phone number must be 10 digits.");
                    return;
                }

                // Validate email format
                if (!enteredEmail.contains("@") || !enteredEmail.contains(".")) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid email address.");
                    return;
                }

                // Validate password match
                if (!new String(enteredPassword).equals(new String(enteredConfirmPassword))) {
                    JOptionPane.showMessageDialog(frame, "Passwords do not match.");
                    return;
                }

                // Validate ATM PIN (must be a 4-digit number)
                if (enteredAtmPin.length != 4) {
                    JOptionPane.showMessageDialog(frame, "ATM PIN must be 4 digits.");
                    return;
                }

                // Check if password is already used
                if (usedPasswords.contains(new String(enteredPassword))) {
                    JOptionPane.showMessageDialog(frame, "Password already in use. Please choose another password.");
                    return;
                }

                // Store the new password
                usedPasswords.add(new String(enteredPassword));

                // Generate a unique user ID
                String userId = UUID.randomUUID().toString().substring(0, 8); // Shorten UUID for simplicity

                // Insert user data into the database
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm_system", "root", "Sanika@4026")) {
                    connection.setAutoCommit(false); // Start transaction

                    // Insert into users table
                    String insertUserQuery = "INSERT INTO users (user_id, name, age, phone, email, password, atm_pin) VALUES (?, ?, ?, ?, ?, ?, ?)";

                    PreparedStatement userStmt = connection.prepareStatement(insertUserQuery);
                    userStmt.setString(1, userId);
                    userStmt.setString(2, enteredName);
                    userStmt.setInt(3, Integer.parseInt(enteredAge));
                    userStmt.setString(4, enteredPhone);
                    userStmt.setString(5, enteredEmail);
                    userStmt.setString(6, new String(enteredPassword));
                    userStmt.setString(7, new String(enteredAtmPin));
                    userStmt.executeUpdate();

                    // Generate account_id and insert into accounts table
                    String accountId = UUID.randomUUID().toString().substring(0, 12); // Unique 12-char account ID
                    String insertAccountQuery = "INSERT INTO accounts (account_id, user_id, balance) VALUES (?, ?, ?)";
                    PreparedStatement accountStmt = connection.prepareStatement(insertAccountQuery);
                    accountStmt.setString(1, accountId);
                    accountStmt.setString(2, userId);
                    accountStmt.setBigDecimal(3, BigDecimal.valueOf(0.00)); // Initial balance = 0.00
                    accountStmt.executeUpdate();

                    connection.commit(); // Commit transaction
                    JOptionPane.showMessageDialog(frame, "User registration successful!\nYour User ID: " + userId + "\nYour Account ID: " + accountId);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
                }

                // Ask user if they want to proceed to login
                int response = JOptionPane.showConfirmDialog(frame,
                        "Great! Your sign-up is complete.\nYour User ID: " + userId + "\nWould you like to proceed to the login page?",
                        "Registration Successful",
                        JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {
                    frame.dispose(); // Close the sign-up page
                    new LoginScreen(); // Open the login page
                } else {
                    JOptionPane.showMessageDialog(frame, "Thank you for signing up! Your User ID is: " + userId + "\nExiting now. Don't forget to log in next time!");
                    System.exit(0); // Exit the system
                }
            }
        });

        // Add components to sign-up panel
        signupPanel.setLayout(null);
        signupPanel.add(titleLabel);
        signupPanel.add(nameLabel);
        signupPanel.add(ageLabel);
        signupPanel.add(phoneLabel);
        signupPanel.add(emailLabel);
        signupPanel.add(passwordLabel);
        signupPanel.add(confirmPasswordLabel);
        signupPanel.add(atmPinLabel);
        signupPanel.add(nameField);
        signupPanel.add(ageField);
        signupPanel.add(phoneField);
        signupPanel.add(emailField);
        signupPanel.add(passwordField);
        signupPanel.add(confirmPasswordField);
        signupPanel.add(atmPinField);
        signupPanel.add(signUpButton);

        // Add components to the frame using GridBagLayout for centering
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();
        container.setLayout(new GridBagLayout());  // Use GridBagLayout for centering
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        container.add(signupPanel, gbc);  // Center sign-up panel

        // Make the frame visible
        frame.setVisible(true);
    }

    // Main method to launch SignUp
    public static void main(String[] args) {
        new SignUp();
    }
}
