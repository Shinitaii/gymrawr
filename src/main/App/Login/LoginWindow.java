package main.App.Login;
import javax.swing.*;

import main.Miscellanous.Constants;

public class LoginWindow {
    private JFrame window;
    private LoginPanel loginPanel;
    
    public LoginWindow() {
        window = new JFrame("Gym RAWR");
        window.setIconImage(Constants.APP_ICON.getImage());
        
        loginPanel = new LoginPanel();
        window.add(loginPanel);
        
        window.pack();
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
    }
    
    public JFrame getWindow() {
        return window;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					LoginWindow window = new LoginWindow();
					window.getWindow().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
}
