
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogOut extends JFrame {
    public LogOut() {
        // Set up the frame properties
        setTitle("Logout - ATM System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the frame size to match the full screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setLayout(null); // Use null layout for absolute positioning

        // Create a label to display the logout message
        JLabel messageLabel = new JLabel("You have logged out successfully!", JLabel.CENTER);
        messageLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
        messageLabel.setBounds(screenSize.width / 4, screenSize.height / 3, screenSize.width / 2, 50);
        add(messageLabel);

        // Create a "Go to Login" button
        JButton loginButton = new JButton("Go to Login");
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 20));
        loginButton.setBackground(new Color(34, 139, 34));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBounds(screenSize.width / 2 - 150, screenSize.height - 200, 200, 50); // Positioned at the bottom center
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new LoginScreen(); // Open the login screen
            }
        });
        add(loginButton);

        // Make the frame visible
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LogOut());
    }
}
