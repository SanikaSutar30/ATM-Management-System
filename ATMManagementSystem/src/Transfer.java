import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Transfer extends JFrame {
    private JTextField amountField;
    private JLabel fromUserIdTextField;
    private JTextField toUserIdTextField;
    private JLabel fromUserIdLabelValue;
    // Database URL, username, and password
    private static final String DB_URL = "jdbc:mysql://localhost:3306/atm_system";
    private static final String DB_USER = "root"; // Update with your MySQL username
    private static final String DB_PASSWORD = "Sanika@4026"; // Update with your MySQL password

    // Constructor
    public Transfer(String userId) {
        // Set up the frame properties
        setTitle("Transfer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the frame size to the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setLocationRelativeTo(null);

        // Set the layout of the frame (BorderLayout)
        setLayout(new BorderLayout());

        // Create the left navigation panel (reusing the navigation setup from Deposit)
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

        // Action listeners for navigation buttons (as in Deposit.java)
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
                new LogOut().setVisible(true);
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

        // Create the right panel for Transfer functionality
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(null);

       // Display the user ID
        // From User ID label and text field (set the default value from userId)

        JLabel fromUserIdLabel = new JLabel("User ID:");
        fromUserIdLabel.setBounds(40, 80, 150, 40);
        fromUserIdLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

        fromUserIdTextField = new JLabel(userId); // Display user ID
        fromUserIdTextField.setBounds(170, 80, 280, 40);
        fromUserIdTextField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        fromUserIdTextField.setForeground(Color.BLACK);


        // To User ID label and text field
        JLabel toUserIdLabel = new JLabel("To User ID:");
        toUserIdLabel.setBounds(40, 150, 150, 40);
        toUserIdLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

        toUserIdTextField = new JTextField();
        toUserIdTextField.setBounds(170, 150, 280, 40);
        toUserIdTextField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        toUserIdTextField.setForeground(Color.BLACK);
        toUserIdTextField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // Amount label and text field (for transfer amount)
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(40, 220, 150, 40);
        amountLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

        amountField = new JTextField();
        amountField.setBounds(170, 220, 280, 40);
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        amountField.setForeground(Color.BLACK);
        amountField.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(12, 91, 160)));

        // Transfer button
        JButton transferActionButton = new JButton("Transfer");
        transferActionButton.setBounds(150, 300, 200, 50);
        transferActionButton.setBackground(new Color(34, 139, 34)); // Forest Green
        transferActionButton.setForeground(Color.WHITE); // White text
        transferActionButton.setFont(new Font("Tahoma", Font.BOLD, 20));

        // Action for Transfer Button
        transferActionButton.addActionListener(e -> {
            String amountText = amountField.getText();
            String toUserId = toUserIdTextField.getText();

            if (amountText.isEmpty() || toUserId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                return;
            }

            try {
                double amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(null, "Amount must be greater than zero.");
                    return;
                }

                // Perform transfer (implement transfer method)
                boolean success = transfer(userId, toUserId, amount);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Transfer Successful!");
                } else {
                    JOptionPane.showMessageDialog(null, "Transfer Failed!");
                }

                // Clear the amount and toUserId fields after transfer
                amountField.setText("");
                toUserIdTextField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid amount.");
            }
        });

        // Add all components to the right panel
        rightPanel.add(fromUserIdLabel);
        rightPanel.add(fromUserIdTextField);
        rightPanel.add(toUserIdLabel);
        rightPanel.add(toUserIdTextField);
        rightPanel.add(amountLabel);
        rightPanel.add(amountField);
        rightPanel.add(transferActionButton);

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

    // Method to handle transfer logic
    public boolean transfer(String fromUserId, String toUserId, double amount) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false); // Start transaction

            // Check if both users exist in the accounts table
            String checkUserQuery = "SELECT user_id FROM accounts WHERE user_id = ?";
            try (PreparedStatement stmtCheckUser = conn.prepareStatement(checkUserQuery)) {
                stmtCheckUser.setString(1, fromUserId);
                try (ResultSet rs = stmtCheckUser.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(null, "Sender does not exist.");
                        return false;
                    }
                }

                stmtCheckUser.setString(1, toUserId);
                try (ResultSet rs = stmtCheckUser.executeQuery()) {
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(null, "Receiver does not exist.");
                        return false;
                    }
                }
            }

            // Check if sender has sufficient balance
            String balanceCheckQuery = "SELECT balance FROM accounts WHERE user_id = ?";
            double senderBalance = 0;
            try (PreparedStatement stmtBalanceCheck = conn.prepareStatement(balanceCheckQuery)) {
                stmtBalanceCheck.setString(1, fromUserId);
                try (ResultSet rs = stmtBalanceCheck.executeQuery()) {
                    if (rs.next()) {
                        senderBalance = rs.getDouble("balance");
                    }
                }
            }

            if (senderBalance < amount) {
                JOptionPane.showMessageDialog(null, "Insufficient balance.");
                return false;
            }

            // Update the balances of both users
            String updateSenderBalanceQuery = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?";
            try (PreparedStatement stmtUpdateSender = conn.prepareStatement(updateSenderBalanceQuery)) {
                stmtUpdateSender.setDouble(1, amount);
                stmtUpdateSender.setString(2, fromUserId);
                stmtUpdateSender.executeUpdate();
            }

            // Get the updated balance for the sender
            double updatedSenderBalance = 0;
            String getUpdatedSenderBalanceQuery = "SELECT balance FROM accounts WHERE user_id = ?";
            try (PreparedStatement stmtGetUpdatedSenderBalance = conn.prepareStatement(getUpdatedSenderBalanceQuery)) {
                stmtGetUpdatedSenderBalance.setString(1, fromUserId);
                try (ResultSet rs = stmtGetUpdatedSenderBalance.executeQuery()) {
                    if (rs.next()) {
                        updatedSenderBalance = rs.getDouble("balance");
                    }
                }
            }

            // Update the receiver balance
            String updateReceiverBalanceQuery = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?";
            try (PreparedStatement stmtUpdateReceiver = conn.prepareStatement(updateReceiverBalanceQuery)) {
                stmtUpdateReceiver.setDouble(1, amount);
                stmtUpdateReceiver.setString(2, toUserId);
                stmtUpdateReceiver.executeUpdate();
            }

            // Get the updated balance for the receiver
            double updatedReceiverBalance = 0;
            try (PreparedStatement stmtGetUpdatedReceiverBalance = conn.prepareStatement(getUpdatedSenderBalanceQuery)) {
                stmtGetUpdatedReceiverBalance.setString(1, toUserId);
                try (ResultSet rs = stmtGetUpdatedReceiverBalance.executeQuery()) {
                    if (rs.next()) {
                        updatedReceiverBalance = rs.getDouble("balance");
                    }
                }
            }

            // Insert transaction records for sender and receiver with balance_after
            String insertTransactionQuery = "INSERT INTO transactions (account_id, transaction_type, amount, balance_after) " +
                    "VALUES ((SELECT account_id FROM accounts WHERE user_id = ?), 'TRANSFER', ?, ?)";
            try (PreparedStatement stmtInsert = conn.prepareStatement(insertTransactionQuery)) {
                stmtInsert.setString(1, fromUserId);
                stmtInsert.setDouble(2, amount);
                stmtInsert.setDouble(3, updatedSenderBalance);
                stmtInsert.executeUpdate();
            }

            String insertReceiverTransactionQuery = "INSERT INTO transactions (account_id, transaction_type, amount, balance_after) " +
                    "VALUES ((SELECT account_id FROM accounts WHERE user_id = ?), 'TRANSFER', ?, ?)";
            try (PreparedStatement stmtInsertReceiver = conn.prepareStatement(insertReceiverTransactionQuery)) {
                stmtInsertReceiver.setString(1, toUserId);
                stmtInsertReceiver.setDouble(2, amount);
                stmtInsertReceiver.setDouble(3, updatedReceiverBalance);
                stmtInsertReceiver.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Main method to run the Transfer JFrame
    public static void main(String[] args) {
        // Replace with the actual logged-in user ID
        new Transfer("user123").setVisible(true);
    }
}
