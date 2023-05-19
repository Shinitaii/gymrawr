package main.Properties;
import java.awt.event.*;
import javax.swing.*;

public class ButtonPanel extends JPanel{

    public ButtonPanel(){
        setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
    }

    public void addButton(String name, Icon icon, ActionListener listener){
        CustomButton newButton = new CustomButton(name, icon, listener);
        add(newButton);
    }
}
