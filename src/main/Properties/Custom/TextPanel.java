package main.Properties.Custom;
import javax.swing.*;

import main.Miscellanous.Constants;

public class TextPanel extends JPanel{
    private TextLabel logo, text;

    public TextPanel(String text, ImageIcon icon, int size){
        logo = new TextLabel(Constants.mainScaleImage(icon));
        add(logo);
        this.text = new TextLabel(text, size);
        add(this.text);
    }
}
