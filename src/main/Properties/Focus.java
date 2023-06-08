package main.Properties;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;

public class Focus implements FocusListener {

    private JTextField textField;
    private String text;
    private JPasswordField passwordField;
    private JCheckBox showPassword;

    public Focus(JTextField textField, String text){
        this.textField = textField;
        this.text = text;
    }

    public Focus(JPasswordField passwordField, JCheckBox showPassword, String text){
        this.passwordField = passwordField;
        this.showPassword = showPassword;
        this.text = text;
    }

    public void focusGained(FocusEvent e) {
        Object object = e.getSource();
        if(object instanceof JTextField){
            this.textField = (JTextField) object;
            if(textField.getText().equals(text)) textField.setText("");
            textField.setForeground(Color.BLACK);
        } else if(object instanceof JPasswordField) {
            this.passwordField = (JPasswordField) object;
            String password = String.valueOf(passwordField.getPassword());
            if(!showPassword.isSelected())passwordField.setEchoChar('●');
            if(password.equals(text)) passwordField.setText("");
            passwordField.setForeground(Color.BLACK);
        }
    }

    public void focusLost(FocusEvent e) {
        Object object = e.getSource();
        if(object instanceof JTextField){
            this.textField = (JTextField) object;
            if(textField.getText().isEmpty()) setPlaceholder(textField, text);
        } else if(object instanceof JPasswordField) {
            this.passwordField = (JPasswordField) object;
            String password = String.valueOf(passwordField.getPassword());
            if(password.isEmpty()) setPlaceholder(passwordField, text);
        }
    }

    public static void setPlaceholder(JTextField component, String text){
        component.setText(text);
        component.setForeground(Color.GRAY);
        if(component instanceof JPasswordField) ((JPasswordField)component).setEchoChar((char)0);
    }
    
}
