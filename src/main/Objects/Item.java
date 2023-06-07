package main.Objects;

public class Item {
    private int id, receiptId, productId, quantity;

    public Item(int id, int receipt, int product, int quantity){
        this.id = id;
        receiptId = receipt;
        productId = product;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getReceiptId() {
        return receiptId;
    }
}
