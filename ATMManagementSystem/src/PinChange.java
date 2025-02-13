import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PinChange extends JFrame {

    private JPasswordField newPinField, confirmPinField;
    private JLabel currentPinLabelValue;
    private String userId;
    private String currentPin;

    // Database URL, username, and password
    private static final String DB_URL = "jdbc:mysql://localhost:3306/atm_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sanika@4026";

    // Constructor
    public PinChange(String userId) {
        this.userId = userId;

        // Initialize the GUI
        setupGUI();

        // Perform the database check after the GUI is visible
        SwingUtilities.invokeLater(() -> checkAndLoadCurrentPin());
    }

    private void setupGUI() {
        // Set up the frame properties
        setTitle("Pin Change");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the frame size to the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setLocationRelativeTo(null);

        // Set the layout of the frame (BorderLayout)
        setLayout(new BorderLayout());

        // Create the left navigation panel
        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(54, 69, 79));
        navPanel.setPreferredSize(new Dimension(250, getHeight()));
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));

        JButton depositButton = createNavButton("Deposit");
        JButton transferButton = createNavButton("Transfer");
        JButton withdrawButton = createNavButton("Withdraw");
        JButton historyButton = createNavButton("Transaction History");
        JButton pinchangetButton = createNavButton("Pin Change");
        JButton checkbalanceButton = createNavButton("Check Balance");
        JButton logoutButton = createNavButton("Logout");
        // Action listeners for navigation buttons
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Deposit(userId).setVisible(true);
            }
        });

        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Transfer(userId).setVisible(true);
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Withdraw(userId).setVisible(true);
            }
        });

        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TransactionHistory(userId).setVisible(true);
            }
        });
        pinchangetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Deposit window
                new PinChange(userId).setVisible(true);
            }
        });
        checkbalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Deposit window
                new CheckBalance(userId).setVisible(true);
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Deposit window
                new LogOut();
            }
        });

        // Add buttons to navigation panel
        navPanel.add(depositButton);
        navPanel.add(transferButton);
        navPanel.add(withdrawButton);
        navPanel.add(pinchangetButton);
        navPanel.add(checkbalanceButton);
        navPanel.add(historyButton);
        navPanel.add(logoutButton);

        // Add the left panel to the frame
        add(navPanel, BorderLayout.WEST);

        // Create the right panel for Pin Change functionality
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);

        // Display the user ID
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setBounds(40, 30, 150, 40);
        userIdLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

        JLabel userIdLabelValue = new JLabel(userId); // Display user ID
        userIdLabelValue.setBounds(170, 30, 280, 40);
        userIdLabelValue.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        userIdLabelValue.setForeground(Color.BLACK);

        // Display the current PIN
        JLabel currentPinLabel = new JLabel("Current PIN:");
        currentPinLabel.setBounds(40, 100, 150, 40);
        currentPinLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

        currentPinLabelValue = new JLabel("Loading...");
        currentPinLabelValue.setBounds(170, 100, 280, 40);
        currentPinLabelValue.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        currentPinLabelValue.setForeground(Color.BLACK);

        // New PIN label and text field
        JLabel newPinLabel = new JLabel("Enter New PIN:");
        newPinLabel.setBounds(40, 170, 200, 40);
        newPinLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

        newPinField = new JPasswordField();
        newPinField.setBounds(230, 170, 280, 40);
        newPinField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        newPinField.setForeground(Color.BLACK);
        newPinField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // Confirm PIN label and text field
        JLabel confirmPinLabel = new JLabel("Confirm New PIN:");
        confirmPinLabel.setBounds(40, 240, 200, 40);
        confirmPinLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

        confirmPinField = new JPasswordField();
        confirmPinField.setBounds(230, 240, 280, 40);
        confirmPinField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        confirmPinField.setForeground(Color.BLACK);
        confirmPinField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // Pin Change button
        JButton pinchangeActionButton = new JButton("Change PIN");
        pinchangeActionButton.setBounds(150, 320, 200, 50);
        pinchangeActionButton.setBackground(new Color(34, 139, 34)); // Forest Green
        pinchangeActionButton.setForeground(Color.WHITE); // White text
        pinchangeActionButton.setFont(new Font("Tahoma", Font.BOLD, 20));

        // Add action listener for the submit button
        pinchangeActionButton.addActionListener(e -> changePin());

        // Add all components to the right panel
        rightPanel.add(userIdLabel);
        rightPanel.add(userIdLabelValue);
        rightPanel.add(currentPinLabel);
        rightPanel.add(currentPinLabelValue);
        rightPanel.add(newPinLabel);
        rightPanel.add(newPinField);
        rightPanel.add(confirmPinLabel);
        rightPanel.add(confirmPinField);
        rightPanel.add(pinchangeActionButton);

        // Add the right panel to the frame
        add(rightPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(230, 50));
        button.setBackground(null);
        button.setOpaque(false);
        button.setBorderPainted(false);
        return button;
    }

    private void checkAndLoadCurrentPin() {
        System.out.println("Fetching PIN for User ID: [" + userId + "]");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT atm_pin FROM users WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, userId);
                System.out.println("Executing Query: " + pstmt.toString()); // Debugging

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        currentPin = rs.getString("atm_pin");
                        currentPinLabelValue.setText(currentPin);
                        System.out.println("Fetched PIN: " + currentPin);
                    } else {
                        currentPin = "Unavailable";
                        currentPinLabelValue.setText("User ID not found.");
                        System.out.println("User ID not found in database");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            currentPinLabelValue.setText("Error: Unable to fetch PIN.");
        }
    }


    private void handleDatabaseError() {
        // Show the database error message
        int option = JOptionPane.showOptionDialog(
                this,
                "There was an issue connecting to the database. Please try again later.",
                "Database Error",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                new String[]{"OK"},
                "OK"
        );

        // After clicking "OK", show another dialog asking if they want to go to the login screen
        if (option == JOptionPane.OK_OPTION) {
            int loginOption = JOptionPane.showOptionDialog(
                    this,
                    "Do you want to go to the login screen?",
                    "Navigate to Login",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Yes", "No"},
                    "Yes"
            );

            // If the user clicks "Yes", navigate to the login screen
            if (loginOption == JOptionPane.YES_OPTION) {
                new LoginScreen();  // Open the login screen
                this.dispose(); // Close the current PinChange screen
            }
        }
    }

    private void changePin() {
        String newPin = new String(newPinField.getPassword());
        String confirmPin = new String(confirmPinField.getPassword());

        if (newPin.isEmpty() || confirmPin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields.");
            return;
        }

        if (!newPin.matches("\\d{4}") || !confirmPin.matches("\\d{4}")) {
            JOptionPane.showMessageDialog(this, "New PINs must be 4-digit numbers.");
            return;
        }

        if (!newPin.equals(confirmPin)) {
            JOptionPane.showMessageDialog(this, "New PINs do not match.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE users SET atm_pin = ? WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, newPin);
                pstmt.setString(2, userId);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "PIN changed successfully!");
                    newPinField.setText("");
                    confirmPinField.setText("");
                    checkAndLoadCurrentPin();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to change PIN. Please try again.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error changing PIN: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new PinChange("User123"); // Replace with actual user ID
    }
}
