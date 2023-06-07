package main.Objects;

import java.util.ArrayList;

public class ReceiptDetails {
    private Receipt receipt;
    private ArrayList<Item> items;

    public ReceiptDetails(Receipt receipt, ArrayList<Item> items) {
        this.receipt = receipt;
        this.items = items;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
