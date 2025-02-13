import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Deposit extends JFrame {
    private JTextField amountField;
    private JLabel userIdLabelValue; // Label to display user ID

    // Database URL, username, and password
    private static final String DB_URL = "jdbc:mysql://localhost:3306/atm_system";
    private static final String DB_USER = "root"; // Update with your MySQL username
    private static final String DB_PASSWORD = "Sanika@4026"; // Update with your MySQL password

    // Constructor
    public Deposit(String userId) {
        // Set up the frame properties
        setTitle("Deposit");
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

        // Create the right panel for Deposit functionality
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);

        // Display the user ID
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setBounds(40, 80, 150, 40);
        userIdLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

        userIdLabelValue = new JLabel(userId); // Display user ID
        userIdLabelValue.setBounds(170, 80, 280, 40);
        userIdLabelValue.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        userIdLabelValue.setForeground(Color.BLACK);

        // Amount label and text field (for deposit amount)
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(40, 150, 150, 40);
        amountLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

        amountField = new JTextField();
        amountField.setBounds(170, 150, 280, 40);
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        amountField.setForeground(Color.BLACK);
        amountField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // Deposit button
        JButton depositActionButton = new JButton("Deposit");
        depositActionButton.setBounds(150, 220, 200, 50);
        depositActionButton.setBackground(new Color(34, 139, 34)); // Forest Green
        depositActionButton.setForeground(Color.WHITE); // White text
        depositActionButton.setFont(new Font("Tahoma", Font.BOLD, 20));

        // Action for Deposit Button
        depositActionButton.addActionListener(e -> {
            String amountText = amountField.getText();

            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter an amount.");
                return;
            }

            try {
                double amount = Double.parseDouble(amountText);

                // Call the deposit method
                boolean success = deposit(userId, amount);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Deposit Successful!");
                } else {
                    JOptionPane.showMessageDialog(null, "Deposit Failed!");
                }
                // Clear the amount field after the deposit operation
                amountField.setText("");  // Clears the amount text field

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid amount.");
            }
        });

        // Add all components to the right panel
        rightPanel.add(userIdLabel);
        rightPanel.add(userIdLabelValue);
        rightPanel.add(amountLabel);
        rightPanel.add(amountField);
        rightPanel.add(depositActionButton);

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

    // Method to handle deposit logic
    public boolean deposit(String userId, double amount) {
        if (amount <= 0) {
            JOptionPane.showMessageDialog(null, "Deposit amount must be greater than zero.");
            return false;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false); // Start transaction

            // Check if the user exists in the accounts table
            String checkUserQuery = "SELECT user_id FROM accounts WHERE user_id = ?";
            try (PreparedStatement stmtCheckUser = conn.prepareStatement(checkUserQuery)) {
                stmtCheckUser.setString(1, userId);
                try (ResultSet rs = stmtCheckUser.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(null, "User does not exist. Please log in or create an account.");
                        return false;
                    }
                }
            }

            // Update balance in the accounts table
            String updateBalanceQuery = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?";
            try (PreparedStatement stmtUpdate = conn.prepareStatement(updateBalanceQuery)) {
                stmtUpdate.setDouble(1, amount);
                stmtUpdate.setString(2, userId);
                int rowsAffected = stmtUpdate.executeUpdate();

                // Debugging line to check rowsAffected
                System.out.println("Rows affected by balance update: " + rowsAffected);

                if (rowsAffected > 0) {
                    // Retrieve the updated balance
                    String fetchBalanceQuery = "SELECT balance FROM accounts WHERE user_id = ?";
                    double updatedBalance = 0;
                    try (PreparedStatement stmtFetch = conn.prepareStatement(fetchBalanceQuery)) {
                        stmtFetch.setString(1, userId);
                        try (ResultSet rs = stmtFetch.executeQuery()) {
                            if (rs.next()) {
                                updatedBalance = rs.getDouble("balance");
                            }
                        }
                    }

                    // Insert the transaction into the transactions table
                    String insertTransactionQuery = "INSERT INTO transactions (account_id, transaction_type, amount, balance_after) " +
                            "VALUES ((SELECT account_id FROM accounts WHERE user_id = ?), 'DEPOSIT', ?, ?)";
                    try (PreparedStatement stmtInsert = conn.prepareStatement(insertTransactionQuery)) {
                        stmtInsert.setString(1, userId);
                        stmtInsert.setDouble(2, amount);
                        stmtInsert.setDouble(3, updatedBalance);
                        stmtInsert.executeUpdate();
                    }

                    conn.commit(); // Commit transaction
                    return true;
                } else {
                    conn.rollback(); // Rollback transaction if balance update fails
                    JOptionPane.showMessageDialog(null, "Deposit Failed! User's balance update was not successful.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while processing the deposit.");
        }
        return false;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Deposit("U12345")); // Example userId
    }
}