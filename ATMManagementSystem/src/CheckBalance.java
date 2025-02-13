import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CheckBalance extends JFrame {
    private JTextField userIdField;
    private String userId;

    // Database URL, username, and password
    private static final String DB_URL = "jdbc:mysql://localhost:3306/atm_system";
    private static final String DB_USER = "root"; // Update with your MySQL username
    private static final String DB_PASSWORD = "Sanika@4026"; // Update with your MySQL password

    // Constructor
    public CheckBalance(String userId) {
        // If userId is not provided, default to "U12345"
        this.userId = (userId == null || userId.isEmpty()) ? "U12345" : userId;

        // Set up the frame properties
        setTitle("Check Balance");
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
                new PinChange(userId).setVisible(true);
            }
        });

        checkbalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CheckBalance(userId).setVisible(true);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        // Create the right panel for CheckBalance functionality
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);

        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setBounds(40, 80, 150, 40);
        userIdLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

        userIdField = new JTextField();
        userIdField.setBounds(170, 80, 280, 40);
        userIdField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        userIdField.setForeground(Color.BLACK);
        userIdField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // If userId is provided, auto-fill the field
        userIdField.setText(this.userId);

        JButton checkActionBalanceButton = new JButton("Check Balance");
        checkActionBalanceButton.setBounds(150, 150, 200, 50);
        checkActionBalanceButton.setBackground(new Color(34, 139, 34)); // Forest Green
        checkActionBalanceButton.setForeground(Color.WHITE);
        checkActionBalanceButton.setFont(new Font("Tahoma", Font.BOLD, 18));

        // Action for Check Balance Button
        checkActionBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the input userId
                String userId = userIdField.getText();

                if (userId.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter your User ID.");
                    return;
                }

                // Call the checkBalance method to retrieve the balance
                double balance = checkBalance(userId);
                if (balance != -1) {
                    JOptionPane.showMessageDialog(null, "Your balance is: " + balance);
                } else {
                    JOptionPane.showMessageDialog(null, "User does not exist. Please log in or create an account.");
                }
            }
        });

        // Add all components to the right panel
        rightPanel.add(userIdLabel);
        rightPanel.add(userIdField);
        rightPanel.add(checkActionBalanceButton);

        // Add the right panel to the frame
        add(rightPanel, BorderLayout.CENTER);

        // Make the frame visible
        setVisible(true);
    }

    // Method to check balance logic
    public double checkBalance(String userId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT balance FROM accounts WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, userId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return rs.getDouble("balance");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1; // Return -1 if the user is not found
    }

    // Method to create a navigation button
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CheckBalance("U12345"));
    }
}
