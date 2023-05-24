package main.Properties.Custom;
import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import main.Properties.Hover;

public class CustomButton extends JButton{

    private boolean highlighted;
    
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

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
        updateButtonVisualState();
    }

    public boolean isHighlighted(){
        return highlighted;
    }

    private void updateButtonVisualState() {
        if (highlighted) setBackground(this.getBackground().brighter());
        else setBackground(Color.decode("#08145c")); // Reset to default background color
        this.revalidate();
        this.repaint();
    }

    private void setProperties(){
        setBackground(Color.decode("#08145c"));
        setForeground(Color.WHITE);
        setBorderPainted(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addMouseListener(new Hover(this));
    }


}
