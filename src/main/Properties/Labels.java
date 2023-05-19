package main.Properties;
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;

public class Labels extends JLabel{
    
    private String text;
    private Icon icon;

    public Labels(String text, Icon icon){
        this.text = text;
        this.icon = icon;
        setProperties();
    }

    public Labels(String text){
        this.text = text;
        setProperties();
    }

    private void setProperties(){
        setText(text);
        setIcon(icon);

        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.PLAIN, 15));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}
