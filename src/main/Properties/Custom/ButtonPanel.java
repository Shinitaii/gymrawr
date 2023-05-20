package main.Properties.Custom;
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

    public void addButton(String name, Icon icon, ActionListener listener, String actionCommand){
        CustomButton newButton = new CustomButton(name, icon, listener);
        newButton.setActionCommand(actionCommand);
        add(newButton);
    }
}
