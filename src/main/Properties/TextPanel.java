package main.Properties;
import javax.swing.*;

import main.Miscellanous.Constants;

public class TextPanel extends JPanel{
    private TextLabel logo, text;

    public TextPanel(String text, ImageIcon icon){
        logo = new TextLabel(Constants.mainScaleImage(icon));
        add(logo);
        this.text = new TextLabel(text);
        add(this.text);
    }
}
