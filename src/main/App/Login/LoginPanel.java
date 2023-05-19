package main.App.Login;
import javax.swing.*;
import javax.swing.border.*;

import main.App.Login.Panels.*;

import java.awt.*;

public class LoginPanel extends JPanel{
    
    private LogoPanel logo;
    private CredentialPanel credential;
    private LoginButtonPanel loginButton;

    public LoginPanel(){
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10,10,10,10));

        //logo panel
        logo = new LogoPanel();
        add(logo, BorderLayout.NORTH);
        //credentials panel
        credential = new CredentialPanel();
        add(credential, BorderLayout.CENTER);
        //login panel
        loginButton = new LoginButtonPanel(credential);
        add(loginButton, BorderLayout.SOUTH);

        setBackgroundToWhite(this);
    }

    public LoginButtonPanel getLoginButtonPanel() {
        return loginButton;
    }

    private void setBackgroundToWhite(JPanel panel){
        for(Component component : panel.getComponents()){
            component.setBackground(Color.WHITE);
        }
    }
}