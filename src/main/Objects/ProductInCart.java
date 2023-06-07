package main.Objects;

public class ProductInCart {
    private String name, code;
    private int id, quantity, duration;
    private double price;

    public ProductInCart(int id, String name, String code, int quantity, double price, int duration){
        this.id = id;
        this.name = name;
        this.code = code;
        this.quantity = quantity;
        this.price = price;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public void setQuantity(int newQuantity) {
    }
}
