package main.Miscellanous;

import java.awt.*;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

import main.Objects.Products;
import main.Objects.Training;
import main.Properties.Focus;

public class CommonComponent {
    
    public static void addComponent(JPanel panel, Component component, int gridx, int gridy, int gridwidth, int gridheight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(component, gbc);
    }

    public static JTextField configureTextField(JPanel panel, JTextField textField, String placeholder, int x, int y, int w, int h) {
        textField = new JTextField(30);
        Focus.setPlaceholder(textField, placeholder);
        textField.addFocusListener(new Focus(textField, placeholder));
        addComponent(panel, textField, x, y, w, h);
        return textField;
    }

    public static JFormattedTextField configureDateField(JFormattedTextField textField){
        MaskFormatter formatter = null;
        try{
            formatter = new MaskFormatter("####-##-##");
            formatter.setPlaceholderCharacter('_');
        } catch (ParseException e){
            e.printStackTrace();
        }
        textField = new JFormattedTextField(formatter);
        textField.setColumns(10);
        return textField;
    }

    public static JFormattedTextField configureContactNumberField(JFormattedTextField textField){
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(false);
        NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMaximum(9999999999L); // 10 digits
        textField = new JFormattedTextField(numberFormatter);
        textField.setText("0");
        textField.setColumns(10);

        return textField;
    }

    public static JComboBox<String> configureProductComboBox(JComboBox<String> comboBox, int[] duration, int type){
        comboBox = new JComboBox<String>();
        int index = 0;
        for(Products products : Products.getProductList()){
            if(products.getProductType() == type) { //if product type is 0 (non-mem), 1 (member) & 2 (trainer)
                comboBox.addItem(products.getProductName());
                duration[index] = products.getProductDayDuration();
                index++;
             }
        }
        return comboBox;
    }

    public static JComboBox<String> configureTrainingComboBox(JComboBox<String> comboBox, int[] trainingID){
        comboBox = new JComboBox<String>();
        int index = 0;
        for(Training trainings : Training.getTrainingList()){
            comboBox.addItem(trainings.getTrainingName());
            trainingID[index] = trainings.getTrainingID();
            index++;
        }
        return comboBox;
    }


    public static JRadioButton createRadioButton(String label, String actionCommand, ButtonGroup group) {
        JRadioButton radioButton = new JRadioButton(label);
        radioButton.setForeground(Color.decode("#08145c"));
        radioButton.setActionCommand(actionCommand);
        group.add(radioButton);
        return radioButton;
    }
}
