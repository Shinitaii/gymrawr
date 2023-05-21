package main.Properties;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.*;

public class Focus implements FocusListener {

    private JTextField textField;
    private String text;

    public Focus(JTextField textField, String text){
        this.textField = textField;
        this.text = text;
    }

    public void focusGained(FocusEvent e) {
        if(textField.getText().equals(text)) textField.setText("");
        textField.setForeground(Color.BLACK);
    }

    public void focusLost(FocusEvent e) {
        if(textField.getText().isEmpty()) setPlaceholder(textField, text);
    }

    public static void setPlaceholder(JTextField component, String text){
        component.setText(text);
        component.setForeground(Color.GRAY);
    }
    
}
