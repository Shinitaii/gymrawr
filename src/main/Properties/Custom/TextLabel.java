package main.Properties.Custom;
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;

public class TextLabel extends JLabel {
    public TextLabel(String text, int size){
        super(text);
        setProperties(size);
    }
    public TextLabel(Icon icon){
        super(icon);
    }

    private void setProperties(int size){
        setFont(new Font("Arial", Font.PLAIN, size));
        setForeground(Color.decode("#08145c"));
    }
}
