import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    public Main() {
        // Set up splash screen window
        setTitle("ATM System - Splash Screen");
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setLocationRelativeTo(null);  // Center the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set background color (matching Guess The Number Game)
        getContentPane().setBackground(new Color(12, 91, 160));

        // Logo label with centered text (matching Guess The Number Game)
        JLabel logoLabel = new JLabel("ATM Management System", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Tahoma", Font.BOLD, 50));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBounds(50, getHeight() / 3, getWidth() - 100, 60);

        // Subtext label
        JLabel subTextLabel = new JLabel("Secure and Easy Banking Experience", SwingConstants.CENTER);
        subTextLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
        subTextLabel.setForeground(Color.LIGHT_GRAY);
        subTextLabel.setBounds(50, getHeight() / 2, getWidth() - 100, 50);

        // Loading label
        JLabel loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Tahoma", Font.ITALIC, 35));
        loadingLabel.setForeground(Color.LIGHT_GRAY);
        loadingLabel.setBounds(50, getHeight() - 250, getWidth() - 100, 40);

        // Set layout and add components
        setLayout(null);
        add(logoLabel);
        add(subTextLabel);
        add(loadingLabel);

        setVisible(true);

        // Show splash screen for 3 seconds
        Timer timer = new Timer(3000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Dispose splash screen and show login page
                dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new LoginScreen();  // Open LoginScreen
                    }
                });
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Main method to run the splash screen
    public static void main(String[] args) {
        new Main(); // Show SplashScreen
    }
}