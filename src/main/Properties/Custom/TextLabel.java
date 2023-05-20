package main.Properties.Custom;
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;

public class TextLabel extends JLabel {
    public TextLabel(String text){
        super(text);
        setProperties();
    }
    public TextLabel(Icon icon){
        super(icon);
    }

    private void setProperties(){
        setFont(new Font("Arial", Font.PLAIN, 25));
        setForeground(Color.decode("#08145c"));
    }
}
