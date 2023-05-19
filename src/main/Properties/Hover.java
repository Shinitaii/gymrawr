package main.Properties;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class Hover implements MouseListener{

    private JButton button;
    private Color buttonColor, hoveredColor;

    public Hover(JButton button){
        this.button = button;
        buttonColor = button.getBackground();
        hoveredColor = buttonColor.brighter();
    }

    public void mouseEntered(MouseEvent e) {
        button.setBackground(hoveredColor);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void mouseExited(MouseEvent e) {
        button.setBackground(buttonColor);
    }

    public void mousePressed(MouseEvent e) {
        button.setBackground(buttonColor.darker());
    }

    public void mouseReleased(MouseEvent e) {
        button.setBackground(hoveredColor);
    }

    public void mouseClicked(MouseEvent e) {
        // not needed
    }
}