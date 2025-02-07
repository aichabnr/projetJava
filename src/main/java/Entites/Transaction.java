package Entites;

import java.util.Date;

public class Transaction {
    private int id;
    private Date date;
    private String description;
    private String category;
    private double amount;
    private String type;

    public Transaction() {}

    public Transaction(int id, Date date, String description, String category, double amount, String type) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.category = category;
        this.amount = type.equals("EXPENSE") ? -Math.abs(amount) : Math.abs(amount);
        this.type = type;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) {
        this.amount = type.equals("EXPENSE") ? -Math.abs(amount) : Math.abs(amount);
    }

    public String getType() { return type; }
    public void setType(String type) {
        this.type = type;
        this.amount = type.equals("EXPENSE") ? -Math.abs(amount) : Math.abs(amount);
    }

    public boolean isExpense() {
        return type.equals("EXPENSE");
    }

    public double getAbsoluteAmount() {
        return Math.abs(amount);
    }
}