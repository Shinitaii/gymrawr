package main.Objects;

import java.sql.*;


public class Receipt {
    private int id;
    private String receiptCode, cashierName;
    private Timestamp receiptDate;
    private double receiptTotalPrice, receiptPayment, receiptChange;

    public Receipt(int id, String receiptCode, String cashierName, Timestamp receiptDate, double receiptTotalPrice, double receiptPayment, double receiptChange){
        this.id = id;
        this.receiptCode = receiptCode;
        this.cashierName = cashierName;
        this.receiptDate = receiptDate;
        this.receiptTotalPrice = receiptTotalPrice;
        this.receiptPayment = receiptPayment;
        this.receiptChange = receiptChange;
    }

    public int getId() {
        return id;
    }

    public String getReceiptCode() {
        return receiptCode;
    }

    public String getCashierName() {
        return cashierName;
    }

    public Timestamp getReceiptDate() {
        return receiptDate;
    }

    public double getReceiptTotalPrice() {
        return receiptTotalPrice;
    }

    public double getReceiptPayment() {
        return receiptPayment;
    }

    public double getReceiptChange() {
        return receiptChange;
    }

}
