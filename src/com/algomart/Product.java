package com.algomart;

public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private double rating;
    private int stock;
    private String description;
    private double discountPercent;

    public Product(int id, String name, String category, double price,
                   double rating, int stock, String description, double discountPercent) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.stock = stock;
        this.description = description;
        this.discountPercent = discountPercent;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public double getRating() { return rating; }
    public int getStock() { return stock; }
    public String getDescription() { return description; }
    public double getDiscountPercent() { return discountPercent; }

    // Setters
    public void setStock(int stock) { this.stock = stock; }
    public void setPrice(double price) { this.price = price; }

    // Utility
    public double getDiscountedPrice() {
        return price - (price * discountPercent / 100);
    }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s | ₹%.2f | Rating: %.1f | Stock: %d",
                id, name, category, price, rating, stock);
    }
}