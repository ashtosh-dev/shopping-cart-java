package com.algomart;

import java.util.ArrayList;
import java.util.List;

public class ProductDatabase {

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        // Electronics
        products.add(new Product(1, "Samsung 4K Smart TV", "Electronics", 45999, 4.5, 20, "55 inch 4K UHD Smart TV with HDR", 10));
        products.add(new Product(2, "Apple iPhone 15", "Electronics", 79999, 4.8, 15, "iPhone 15 128GB Black", 5));
        products.add(new Product(3, "Sony WH-1000XM5 Headphones", "Electronics", 29999, 4.7, 30, "Industry leading noise cancelling headphones", 8));
        products.add(new Product(4, "Dell Inspiron Laptop", "Electronics", 55999, 4.3, 12, "Intel i5 16GB RAM 512GB SSD", 0));
        products.add(new Product(5, "Canon EOS Camera", "Electronics", 34999, 4.6, 8, "24.1MP DSLR Camera with 18-55mm lens", 12));
        products.add(new Product(6, "JBL Bluetooth Speaker", "Electronics", 4999, 4.4, 50, "Portable waterproof speaker 20hr battery", 5));
        products.add(new Product(7, "iPad Air", "Electronics", 59999, 4.7, 18, "10.9 inch iPad Air M1 chip 64GB", 7));
        products.add(new Product(8, "Samsung Galaxy Watch", "Electronics", 19999, 4.3, 25, "Galaxy Watch 5 Pro health tracking", 3));
        products.add(new Product(9, "Logitech Mechanical Keyboard", "Electronics", 7999, 4.5, 40, "TKL mechanical keyboard RGB backlit", 0));
        products.add(new Product(10, "LG Monitor 27inch", "Electronics", 22999, 4.4, 15, "27 inch IPS 4K monitor 60Hz", 6));

        // Clothing
        products.add(new Product(11, "Levi's 511 Slim Jeans", "Clothing", 3999, 4.3, 60, "Classic slim fit stretch jeans", 15));
        products.add(new Product(12, "Nike Air Max Sneakers", "Clothing", 8999, 4.6, 35, "Air Max 270 running shoes", 10));
        products.add(new Product(13, "Allen Solly Formal Shirt", "Clothing", 1499, 4.2, 80, "Regular fit cotton formal shirt", 20));
        products.add(new Product(14, "Adidas Track Jacket", "Clothing", 3499, 4.4, 45, "3-stripe polyester track jacket", 8));
        products.add(new Product(15, "H&M Winter Coat", "Clothing", 5999, 4.1, 20, "Warm padded winter coat", 25));
        products.add(new Product(16, "Zara Casual T-Shirt", "Clothing", 999, 4.0, 100, "100% cotton crew neck t-shirt", 0));
        products.add(new Product(17, "Puma Running Shorts", "Clothing", 1299, 4.3, 70, "Lightweight dry-fit running shorts", 12));
        products.add(new Product(18, "Woodland Boots", "Clothing", 4999, 4.5, 30, "Genuine leather waterproof boots", 5));
        products.add(new Product(19, "US Polo Polo Shirt", "Clothing", 1799, 4.2, 55, "Classic polo shirt pique cotton", 18));
        products.add(new Product(20, "Fastrack Sunglasses", "Clothing", 1299, 4.1, 40, "UV400 protection polarized sunglasses", 10));

        // Groceries
        products.add(new Product(21, "Tata Salt 1kg", "Groceries", 25, 4.5, 200, "Iodized vacuum evaporated salt", 0));
        products.add(new Product(22, "Aashirvaad Atta 5kg", "Groceries", 280, 4.6, 150, "Whole wheat flour premium quality", 5));
        products.add(new Product(23, "Amul Butter 500g", "Groceries", 275, 4.7, 100, "Pasteurized butter unsalted", 0));
        products.add(new Product(24, "Fortune Sunflower Oil 5L", "Groceries", 699, 4.3, 80, "Refined sunflower cooking oil", 8));
        products.add(new Product(25, "Nescafe Classic Coffee 200g", "Groceries", 499, 4.5, 120, "100% pure instant coffee", 10));
        products.add(new Product(26, "Maggi Noodles Pack of 12", "Groceries", 192, 4.4, 200, "2 minute masala noodles", 0));
        products.add(new Product(27, "Britannia Good Day Biscuits", "Groceries", 60, 4.3, 300, "Butter cookies family pack", 0));
        products.add(new Product(28, "Tropicana Orange Juice 1L", "Groceries", 140, 4.2, 90, "100% pure orange juice no added sugar", 5));
        products.add(new Product(29, "Basmati Rice 5kg", "Groceries", 550, 4.6, 100, "Premium aged basmati rice", 10));
        products.add(new Product(30, "Tata Tea Gold 500g", "Groceries", 285, 4.5, 150, "Premium blend tea leaves", 0));

        // Books
        products.add(new Product(31, "Introduction to Algorithms (CLRS)", "Books", 2999, 4.9, 25, "Classic algorithms textbook 4th edition", 0));
        products.add(new Product(32, "Clean Code - Robert Martin", "Books", 1299, 4.7, 40, "A handbook of agile software craftsmanship", 10));
        products.add(new Product(33, "Atomic Habits", "Books", 499, 4.8, 60, "Tiny changes remarkable results", 15));
        products.add(new Product(34, "The Alchemist", "Books", 299, 4.7, 80, "Paulo Coelho's masterpiece", 0));
        products.add(new Product(35, "Rich Dad Poor Dad", "Books", 399, 4.6, 70, "What the rich teach their kids about money", 5));
        products.add(new Product(36, "Harry Potter Complete Set", "Books", 2999, 4.9, 15, "All 7 books boxed set", 8));
        products.add(new Product(37, "Wings of Fire - APJ Kalam", "Books", 199, 4.8, 90, "Autobiography of APJ Abdul Kalam", 0));
        products.add(new Product(38, "Zero to One - Peter Thiel", "Books", 599, 4.5, 35, "Notes on startups or how to build the future", 12));
        products.add(new Product(39, "Deep Work - Cal Newport", "Books", 449, 4.6, 45, "Rules for focused success in a distracted world", 10));
        products.add(new Product(40, "The Psychology of Money", "Books", 349, 4.7, 55, "Timeless lessons on wealth greed and happiness", 5));

        // Home & Kitchen
        products.add(new Product(41, "Prestige Pressure Cooker 5L", "Home & Kitchen", 1799, 4.5, 40, "Aluminium pressure cooker with safety valve", 10));
        products.add(new Product(42, "Philips Air Fryer", "Home & Kitchen", 7999, 4.4, 25, "Digital air fryer 4.1L capacity", 8));
        products.add(new Product(43, "Milton Water Bottle 1L", "Home & Kitchen", 399, 4.3, 100, "Stainless steel insulated water bottle", 0));
        products.add(new Product(44, "Bajaj Room Heater", "Home & Kitchen", 2499, 4.2, 30, "1000W fan room heater with thermostat", 15));
        products.add(new Product(45, "Wonderchef Cookware Set", "Home & Kitchen", 3999, 4.4, 20, "5 piece non-stick cookware set", 20));
        products.add(new Product(46, "Dyson V8 Vacuum Cleaner", "Home & Kitchen", 34999, 4.6, 10, "Cordless stick vacuum powerful suction", 5));
        products.add(new Product(47, "Ikea Study Lamp", "Home & Kitchen", 1299, 4.3, 50, "LED desk lamp with USB charging port", 0));
        products.add(new Product(48, "Cello Plastic Storage Box", "Home & Kitchen", 599, 4.1, 80, "Airtight food storage container set of 6", 10));
        products.add(new Product(49, "Usha Stand Fan", "Home & Kitchen", 3299, 4.3, 35, "400mm 3 speed stand fan with timer", 8));
        products.add(new Product(50, "Borosil Glass Set", "Home & Kitchen", 799, 4.4, 60, "Set of 6 borosilicate drinking glasses", 12));

        return products;
    }

    public static List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("All");
        categories.add("Electronics");
        categories.add("Clothing");
        categories.add("Groceries");
        categories.add("Books");
        categories.add("Home & Kitchen");
        return categories;
    }
}