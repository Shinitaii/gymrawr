package main.Properties.Custom;
import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import main.Properties.Hover;

public class CustomButton extends JButton{
    
    public CustomButton(String text, Icon icon, ActionListener actionListener){
        super(text, icon);
        setProperties();
        addActionListener(actionListener);
    }

    public CustomButton(String text, Icon icon){
        super(text, icon); 
        setProperties();     
    }

    public CustomButton(String text){
        super(text);
        setProperties();
    }

    private void setProperties(){
        setBackground(Color.decode("#08145c"));
        setForeground(Color.WHITE);
        setBorderPainted(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addMouseListener(new Hover(this));
    }
}
