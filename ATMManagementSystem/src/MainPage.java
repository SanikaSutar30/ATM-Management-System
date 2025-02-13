import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPage extends JFrame {
    private String userId; // Store the userId

    // Constructor that accepts a userId
    public MainPage(String userId) {
        this.userId = userId;  // Store userId for future use
        initialize();
    }

    private void initialize() {
        setTitle("Main Application - ATM System");
        setSize(Toolkit.getDefaultToolkit().getScreenSize());  // Full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        // Main page panel
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(900, 700));  // Set preferred size for centering
        mainPanel.setBackground(new Color(54, 69, 79)); // Charcoal Gray
        mainPanel.setLayout(null);

        // Title Label
        JLabel mainLabel = new JLabel("Start Your Banking Journey Here");
        mainLabel.setBounds(250, 40, 500, 50);
        mainLabel.setForeground(Color.WHITE);
        mainLabel.setFont(new Font("Tahoma", Font.ITALIC, 30));

        // Add a label to display the userId (optional, for verification)
        JLabel userLabel = new JLabel("User ID: " + userId);
        userLabel.setBounds(350, 80, 200, 30);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));

        mainPanel.add(mainLabel);
        mainPanel.add(userLabel);

        // Buttons with actions
        addButton(mainPanel, "Deposit", 350, 120, e -> new Deposit(userId).setVisible(true));
        addButton(mainPanel, "Transfer", 350, 190, e -> new Transfer(userId).setVisible(true));
        addButton(mainPanel, "Withdraw", 350, 260, e -> new Withdraw(userId).setVisible(true));
        addButton(mainPanel, "Pin Change", 350, 330, e -> new PinChange(userId).setVisible(true));
        addButton(mainPanel, "Check Balance", 350, 400, e -> new CheckBalance(userId).setVisible(true));
        addButton(mainPanel, "Transfer History", 350, 470, e -> new TransactionHistory(userId).setVisible(true));
        addButton(mainPanel, "Logout", 350, 540, e -> logout());

        // Add panel to frame
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        getContentPane().add(mainPanel, gbc);

        setVisible(true);
    }

    // Helper method to create buttons
    private void addButton(JPanel panel, String text, int x, int y, ActionListener action) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 200, 40);
        button.setFont(new Font("Tahoma", Font.BOLD, 20));
        button.setBackground(new Color(34, 139, 34)); // Forest Green
        button.setForeground(Color.WHITE);
        button.addActionListener(action);
        panel.add(button);
    }

    private void logout() {
        dispose(); // Close the current frame
        new LoginScreen(); // Open the login screen
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainPage("user123")); // Replace with actual user ID from login
    }
}
