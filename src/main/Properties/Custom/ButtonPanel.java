package main.Properties.Custom;
import java.awt.Component;
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

    public void removeButton(String name){
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof CustomButton) {
                CustomButton customButton = (CustomButton) component;
                String buttonLabelText = customButton.getText();
                if (buttonLabelText != null && buttonLabelText.equals(name)) {
                    remove(component);
                    revalidate();
                    repaint();
                    break; // Exit the loop once the button is found and removed
                }
            }
        }
    }

    public CustomButton getCustomButton(String name, JPanel panel){
        for (Component component : panel.getComponents()) {
            if (component instanceof CustomButton) {
                CustomButton customButton = (CustomButton) component;
                if (customButton.getName() != null && customButton.getText().equals(name)) return customButton;
            }
        }
        return null;
    }
}
