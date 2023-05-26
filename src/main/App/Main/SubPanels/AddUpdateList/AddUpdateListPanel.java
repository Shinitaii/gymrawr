package main.App.Main.SubPanels.AddUpdateList;
import javax.swing.ImageIcon;
import main.Miscellanous.Constants;
import main.Properties.Custom.CustomPanel;

public class AddUpdateListPanel extends CustomPanel{

    private AddPanel addPanel;
    private UpdatePanel updatePanel;
    private ListPanel listPanel;

    public AddUpdateListPanel(String panelName, ImageIcon icon, int size) {
        super(panelName, icon, size);
        updatePanel = new UpdatePanel(this, panelName);
        add(updatePanel, "Update");
        listPanel = new ListPanel(this, panelName);
        add(listPanel, "List");
        addPanel = new AddPanel(this, panelName, updatePanel, listPanel);
        add(addPanel, "Add");

        addButton("Add " + panelName, Constants.ADD_ICON, e -> getCardLayout().show(this, "Add"));
        addButton("Update " + panelName, Constants.UPDATE_ICON, e -> getCardLayout().show(this, "Update"));
        addButton("List of " + panelName, Constants.LIST_ICON, e -> getCardLayout().show(this, "List"));
    }

    public void removeListButton(String panelName){
        removeButton("List of " + panelName);
    }
    
}
