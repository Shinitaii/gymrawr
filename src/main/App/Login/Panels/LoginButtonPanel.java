package main.App.Login.Panels;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import main.App.Main.MainWindow;
import main.Database.MySQL;
import main.Miscellanous.Messages;
import main.Objects.Products;
import main.Objects.User;

import java.sql.*;

public class LoginButtonPanel extends JPanel{

    private JButton loginButton;
    private User user;

    public LoginButtonPanel(CredentialPanel credentialPanel){
        setLayout(new FlowLayout(FlowLayout.CENTER));

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //looks username and password in the database
                String username = credentialPanel.getUsernameField().getText(); 
                String password = String.valueOf(credentialPanel.getPasswordField().getPassword());

                checkCredentials(username, password);
            }
        });
        add(loginButton);
    }

    public User getUser() {
        return user;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    private void checkCredentials(String username, String password){
        try(Connection conn = MySQL.getConnection()){ 
            PreparedStatement query = conn.prepareStatement("select * from users where user_username = ? and user_password = ?");
            query.setString(1, username);
            query.setString(2, password);
            ResultSet result = query.executeQuery();
            if(result.next()){ //if results are found by checking the username and password from the database, it will go here
                int userID = result.getInt("user_id");
                int userLevel = result.getInt("user_level");
                Messages.loginSuccessful(username);
                switchToMainWindow(username, password, userID, userLevel);
            } else Messages.loginFailed();  
            conn.close();
        } catch (SQLException e){
            Messages.databaseConnectionFailed();
            e.printStackTrace();
        }
    }

    private void switchToMainWindow(String name, String pass, int userID, int userLevel){
        this.user = new User(name, pass, userID, userLevel);
        
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    Products.initializeProducts();
					MainWindow window = new MainWindow(user);
					window.getWindow().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

        SwingUtilities.getWindowAncestor(this).dispose();
    }

}
