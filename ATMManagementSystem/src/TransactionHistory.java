import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TransactionHistory extends JFrame {

    private String userId;
    private JTable transactionTable;
    private JLabel statusLabel;

    // Database details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/atm_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sanika@4026";

    public TransactionHistory(String userId) {
        this.userId = userId;
        // Set up the frame properties
        setTitle("Transaction History");
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
        depositButton.addActionListener(e -> new Deposit(userId).setVisible(true));
        transferButton.addActionListener(e -> new Transfer(userId).setVisible(true));
        withdrawButton.addActionListener(e -> new Withdraw(userId).setVisible(true));
        historyButton.addActionListener(e -> new TransactionHistory(userId).setVisible(true));
        pinchangetButton.addActionListener(e -> new PinChange(userId).setVisible(true));
        checkbalanceButton.addActionListener(e -> new CheckBalance(userId).setVisible(true));
        logoutButton.addActionListener(e -> new LogOut());

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

        // Create the right panel for Transaction History functionality
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Top panel for labels and button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        // Create user ID label and its value
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setFont(new Font("Tahoma", Font.BOLD, 18));

        JLabel userIdLabelValue = new JLabel(userId);
        userIdLabelValue.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        userIdLabelValue.setForeground(Color.BLACK);

        // Status Label for validation info
        statusLabel = new JLabel("Validating user...");
        statusLabel.setFont(new Font("Tahoma", Font.BOLD, 16));

        // Download Button
        JButton downloadActionButton = new JButton("Download");
        downloadActionButton.setBackground(new Color(34, 139, 34)); // Forest Green
        downloadActionButton.setForeground(Color.WHITE); // White text
        downloadActionButton.setFont(new Font("Tahoma", Font.BOLD, 20));

        downloadActionButton.addActionListener(e -> downloadTransactionHistoryAsPDF());

        // Add components to the top panel
        topPanel.add(userIdLabel);
        topPanel.add(userIdLabelValue);
        topPanel.add(statusLabel);
        topPanel.add(downloadActionButton);

        // Add the top panel to the right panel
        rightPanel.add(topPanel, BorderLayout.NORTH);

        // Create the table for transaction history
        transactionTable = new JTable();
        transactionTable.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        transactionTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(transactionTable);

        // Add the scroll pane (table) to the right panel
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Add the right panel to the frame
        add(rightPanel, BorderLayout.CENTER);

        // Make the frame visible
        setVisible(true);

        // Validate and load data for the user
        SwingUtilities.invokeLater(this::validateAndLoadData);
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

    // Validate user ID and load transaction history
    private void validateAndLoadData() {
        if (isUserIdValid(userId)) {
            statusLabel.setText("User ID: " + userId + " is valid. Loading transactions...");
            fetchAndDisplayTransactionHistory();
        } else {
            statusLabel.setText("Invalid User ID: " + userId);
            JOptionPane.showMessageDialog(this, "User does not exist. Please log in or create an account.");
            int option = JOptionPane.showOptionDialog(
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
            if (option == JOptionPane.YES_OPTION) {
                new LoginScreen();  // Open the login screen
                this.dispose(); // Close the current TransactionHistory screen
            }
        }
    }

    // Check if userId exists in the database
    private boolean isUserIdValid(String userId) {
        String query = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error validating user ID: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    // Fetch and display transaction history
    private void fetchAndDisplayTransactionHistory() {
        String query = "SELECT t.transaction_date, t.transaction_type, t.amount, t.balance_after " +
                "FROM transactions t " +
                "JOIN accounts a ON t.account_id = a.account_id " +
                "WHERE a.user_id = ? " +
                "ORDER BY t.transaction_date DESC";

        List<Object[]> data = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                            rs.getString("transaction_date"),
                            rs.getString("transaction_type"),
                            rs.getDouble("amount"),
                            rs.getDouble("balance_after")
                    };
                    data.add(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching transaction history: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        String[] columns = {"Date", "Transaction Type", "Amount", "Balance After"};
        transactionTable.setModel(new DefaultTableModel(data.toArray(new Object[0][]), columns));

        // Apply table styles for better visualization
        transactionTable.setGridColor(Color.BLACK);
        transactionTable.setShowGrid(true);
    }

    // Method to generate and download the transaction history as PDF
    private void downloadTransactionHistoryAsPDF() {
        try {
            // Specify the target directory
            String directoryPath = "C:\\Users\\sanik\\Downloads\\ATM_System_Transaction_History";

            // Ensure the directory exists, create it if not
            if (!Files.exists(Paths.get(directoryPath))) {
                Files.createDirectories(Paths.get(directoryPath));
            }

            // Get the current month and year
            LocalDate currentDate = LocalDate.now();
            String month = currentDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH); // e.g., "Jan"
            int year = currentDate.getYear(); // e.g., "2025"

            // Construct the file name
            String fileName = userId + "_" + month + year + ".pdf"; // e.g., "4ebb7fa1_Jan2025.pdf"
            String fullPath = Paths.get(directoryPath, fileName).toString();

            // Create PdfWriter and PdfDocument instances
            PdfWriter writer = new PdfWriter(fullPath);
            PdfDocument pdfDoc = new PdfDocument(writer);

            // Create Document with PdfDocument
            Document document = new Document(pdfDoc);

            // Add title and user ID
            document.add(new Paragraph("Transaction History for User ID: " + userId));
            document.add(new Paragraph("\n"));  // Adding a new line

            // Add the transaction table
            Table table = new Table(4); // 4 columns: Date, Type, Amount, Balance After
            table.addCell(new Cell().add(new Paragraph("Date")));
            table.addCell(new Cell().add(new Paragraph("Transaction Type")));
            table.addCell(new Cell().add(new Paragraph("Amount")));
            table.addCell(new Cell().add(new Paragraph("Balance After")));

            // Populate the table with data from the JTable
            for (int i = 0; i < transactionTable.getRowCount(); i++) {
                table.addCell(new Cell().add(new Paragraph(transactionTable.getValueAt(i, 0).toString())));  // Date
                table.addCell(new Cell().add(new Paragraph(transactionTable.getValueAt(i, 1).toString())));  // Type
                table.addCell(new Cell().add(new Paragraph(transactionTable.getValueAt(i, 2).toString())));  // Amount
                table.addCell(new Cell().add(new Paragraph(transactionTable.getValueAt(i, 3).toString())));  // Balance
            }

            document.add(table);
            document.close();

            JOptionPane.showMessageDialog(this, "Transaction history downloaded as PDF: " + fullPath);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating PDF: " + e.getMessage(),
                    "PDF Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new TransactionHistory("User123");
    }
}
