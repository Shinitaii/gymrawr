package main.App.Main.SubPanels;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import main.Database.MySQL;
import main.Miscellanous.*;
import main.Objects.*;
import main.Properties.Custom.*;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class SalesReportPanel extends JPanel {
    private JPanel listPanel, receiptPanel;

    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;
    private static JComboBox<String> sortDays;
    private TextLabel salesLabelPrice;
    private CustomButton back;
    private JTextArea receiptTextArea;

    public SalesReportPanel(){
        listPanel = new JPanel(new GridBagLayout());
        add(listPanel);

        TextLabel instructionLabel = new TextLabel("Double click on a cell to view more details about the receipt", 12);
        CommonComponent.addComponent(listPanel, instructionLabel, 0, 0, 1, 1);
        table = new JTable();
        initializeTableModel();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                JTable target = (JTable) e.getSource();
                int selectedRow = target.getSelectedRow();
                createReceiptDetails(selectedRow);
            }
        });
        tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(250, 300));
        CommonComponent.addComponent(listPanel, tableScrollPane, 0, 2, 4, 1);
        TextLabel todayDateLabel = new TextLabel("Current date: " + LocalDate.now().toString(), 12);
        CommonComponent.addComponent(listPanel, todayDateLabel, 0, 1, 1, 1);
        TextLabel sortBoxLabel = new TextLabel("Select date range:", 12);
        CommonComponent.addComponent(listPanel, sortBoxLabel, 2, 1, 1, 1);
        sortDays = new JComboBox<>(new String[]{"Daily", "Weekly", "Monthly", "All"});
        sortDays.addItemListener(e -> retrieveReceipts(sortDays.getSelectedIndex()));
        CommonComponent.addComponent(listPanel, sortDays, 3, 1, 1, 1);
        TextLabel salesLabel = new TextLabel("Total Sales (based on selected date range): ", 12);
        CommonComponent.addComponent(listPanel, salesLabel, 0, 3, 1, 1);
        salesLabelPrice = new TextLabel("₱0.00", 12);
        CommonComponent.addComponent(listPanel, salesLabelPrice, 1, 3, 1, 1);

        receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.setVisible(false);
        add(receiptPanel);
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        back = new CustomButton("Back", null, e -> back());
        backPanel.add(back);
        receiptPanel.add(backPanel, BorderLayout.NORTH);
        receiptTextArea = new JTextArea();
        receiptPanel.add(receiptTextArea, BorderLayout.CENTER);

        retrieveReceipts(0);
    }

    private void back(){
        listPanel.setVisible(true);
        receiptPanel.setVisible(false);
        revalidate();
        repaint();
    }

    public JComboBox<String> getSortDays() {
        return sortDays;
    }

    public void initializeTableModel() {
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{"Receipt Number","Receipt Codes", "Receipt Date", "Cashier Name"});
        table.setModel(tableModel);
    }

    public void retrieveReceipts(int type){
        tableModel.setRowCount(0);
        LocalDate today = LocalDate.now();
        LocalDate start = today;
        LocalDate end = today.plusDays(1);
        if(type == 1){
            start = today.minusDays(today.getDayOfWeek().getValue());
            end = today.plusDays(6 - today.getDayOfWeek().getValue());
        } else if(type == 2){
            start = today.withDayOfMonth(1);
            end = today.withDayOfMonth(today.lengthOfMonth());
        }

        try (Connection conn = MySQL.getConnection()) {
            String sql = "SELECT * FROM receipts";
            if (type != 3) sql += " WHERE receipt_date between ? and ?";
            sql += " ORDER BY receipt_date";
            PreparedStatement statement = conn.prepareStatement(sql);
            if (type != 3) {
                statement.setDate(1, Date.valueOf(start));
                statement.setDate(2, Date.valueOf(end));
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Object[] rowData = new Object[]{
                    resultSet.getString("receipt_code").substring(0, 10),
                    resultSet.getString("receipt_code").substring(10),
                    resultSet.getDate("receipt_date"),
                    resultSet.getString("cashier_name")
                };
                tableModel.addRow(rowData);
            }
            revalidate();
            repaint();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Messages.databaseConnectionFailed();
        }
    }

    private void createReceiptDetails(int selectedRow) { 
        if (selectedRow != -1) {
            Object value1 = table.getValueAt(selectedRow, 0);
            Object value2 = table.getValueAt(selectedRow, 1);
            String receiptCode = value1.toString() + value2.toString();
    
            try (Connection conn = MySQL.getConnection()) {
                String sql = "SELECT * FROM receipts WHERE receipt_code = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, receiptCode);
                ResultSet resultSet = statement.executeQuery();
    
                if (resultSet.next()) {
                    Receipt receipt = new Receipt(
                        resultSet.getInt("id"),
                        resultSet.getString("receipt_code"),
                        resultSet.getString("cashier_name"),
                        resultSet.getTimestamp("receipt_date"),
                        resultSet.getDouble("receipt_customer_payment"),
                        resultSet.getDouble("receipt_total_price"),
                        resultSet.getDouble("receipt_change")
                    );

    
                    // Retrieve purchased items
                    String itemsSql = "SELECT * FROM items WHERE receipt_id = ?";
                    PreparedStatement itemsStatement = conn.prepareStatement(itemsSql);
                    itemsStatement.setInt(1, receipt.getId());
                    ResultSet itemsResultSet = itemsStatement.executeQuery();
    
                    ArrayList<Item> items = new ArrayList<>();
                    while (itemsResultSet.next()) {
                        Item item = new Item(
                            itemsResultSet.getInt("id"),
                            itemsResultSet.getInt("receipt_id"),
                            itemsResultSet.getInt("product_id"),
                            itemsResultSet.getInt("quantity")
                        );
                        items.add(item);
                    }
    
                    ReceiptDetails receiptDetails = new ReceiptDetails(receipt, items);
                    listPanel.setVisible(false);
                    receiptPanel.setVisible(true);
                    displayOnTextArea(receiptDetails);
                    
                    revalidate();
                    repaint();
                    conn.close();
                } else JOptionPane.showMessageDialog(null, "Cannot find receipt!", Constants.APP_TITLE, JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                e.printStackTrace();
                Messages.databaseConnectionFailed();
            }
        }
    }

    private void displayOnTextArea(ReceiptDetails receiptDetails){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(receiptDetails.getReceipt().getReceiptDate());
        
        String str = "";
        String seperator = "----------------------------------------------------\n";
        str += seperator;
        str += "Gym RAWR" + "\n";
        str += seperator;
        str += "Details:" + "\n";
        str += "Date: " + formattedDate + "\n";
        str += "Receipt Number: " + (receiptDetails.getReceipt().getReceiptCode()).substring(0, 10) + "\n";
        str += "Cashier: " + receiptDetails.getReceipt().getCashierName() + "\n";
        str += seperator;
        str += "Items:" + "\n";
        Map<Integer, Products> productMap = new HashMap<>();
        for (Products product : Products.getProductList()) {
            productMap.put(product.getProductID(), product);
        }
        for (Item item : receiptDetails.getItems()) {
            int productId = item.getProductId();
            if (productMap.containsKey(productId)) {
                Products product = productMap.get(productId);
                str += item.getQuantity() + "x - " + product.getProductName() + " - ₱" + (item.getQuantity() * product.getProductPrice()) + "\n";
            }
        }
        str += seperator;
        str += "Total Price: ₱" + receiptDetails.getReceipt().getReceiptTotalPrice() + "\n";
        str += "Customer Payment: ₱" + receiptDetails.getReceipt().getReceiptPayment() + "\n";
        str += "Change: ₱" + receiptDetails.getReceipt().getReceiptChange() + "\n";
        str += seperator;

        receiptTextArea.setText(str);
    }
}
