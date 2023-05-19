package main.App.Login.Panels;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CredentialPanel extends JPanel{

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPassword;

    public CredentialPanel(){
        setLayout(new GridLayout(3,1,10,10));

        //username text field creation
        usernameField = new JTextField(25);
        setPlaceholder(usernameField, "Username");
        usernameField.addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent e) {
                if(usernameField.getText().equals("Username")) usernameField.setText("");
                usernameField.setForeground(Color.BLACK);
            }
            public void focusLost(FocusEvent e){
                if(usernameField.getText().isEmpty()) setPlaceholder(usernameField, "Username");
            }
        });
        add(usernameField);

        //password field creation
        passwordField = new JPasswordField(25);
        setPlaceholder(passwordField, "Password");
        passwordField.addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent e){
                String password = String.valueOf(passwordField.getPassword());
                if(!showPassword.isSelected())passwordField.setEchoChar('●');
                if(password.equals("Password")) passwordField.setText("");
                passwordField.setForeground(Color.BLACK);
            }
            public void focusLost(FocusEvent e){
                String password = String.valueOf(passwordField.getPassword());
                if(password.isEmpty()) setPlaceholder(passwordField, "Password");
            }
        });
        add(passwordField);

        showPassword = new JCheckBox("Show password");
        showPassword.setBackground(Color.WHITE);
        showPassword.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (showPassword.isSelected()) passwordField.setEchoChar((char)0);
                else 
                    if(!String.valueOf(passwordField.getPassword()).equals("Password"))passwordField.setEchoChar('●');
            }
        });
        add(showPassword);
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    private void setPlaceholder(JTextField component, String text){
        component.setText(text);
        component.setForeground(Color.GRAY);
        if(component instanceof JPasswordField) ((JPasswordField)component).setEchoChar((char)0);
    }

}
