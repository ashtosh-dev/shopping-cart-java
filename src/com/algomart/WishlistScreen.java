package com.algomart;

import com.algomart.algorithms.backtracking.WishlistOptimizer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class WishlistScreen {

    private List<Product> allProducts;
    private List<CartItem> cart;
    private Runnable updateCart;

    public WishlistScreen(List<Product> allProducts, List<CartItem> cart, Runnable updateCart) {
        this.allProducts = allProducts;
        this.cart = cart;
        this.updateCart = updateCart;
    }

    public VBox getView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Title
        Label title = new Label("❤️ Wishlist Optimizer");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Set your constraints and we'll find all valid product combinations using Backtracking.");
        subtitle.setTextFill(Color.web("#888"));
        subtitle.setWrapText(true);
        subtitle.setFont(Font.font(13));

        Label algoNote = new Label("🧠 Algorithm: Backtracking with Pruning  |  Explores all combinations, prunes invalid branches early");
        algoNote.setStyle(
                "-fx-background-color: #0f3460;" +
                        "-fx-text-fill: #e94560;" +
                        "-fx-padding: 8 15 8 15;" +
                        "-fx-background-radius: 8;"
        );
        algoNote.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        // Constraints grid
        GridPane constraintsGrid = new GridPane();
        constraintsGrid.setHgap(20);
        constraintsGrid.setVgap(15);
        constraintsGrid.setPadding(new Insets(20));
        constraintsGrid.setStyle(
                "-fx-background-color: #16213e;" +
                        "-fx-background-radius: 10;"
        );

        Label constraintsTitle = new Label("Set Your Constraints:");
        constraintsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        constraintsTitle.setTextFill(Color.WHITE);

        // Max budget
        Label budgetLabel = new Label("Max Budget (₹):");
        budgetLabel.setTextFill(Color.web("#ccc"));
        TextField budgetField = new TextField("3000");
        styleTextField(budgetField);

        // Max items
        Label maxItemsLabel = new Label("Max Items:");
        maxItemsLabel.setTextFill(Color.web("#ccc"));
        TextField maxItemsField = new TextField("4");
        styleTextField(maxItemsField);

        // Min rating
        Label minRatingLabel = new Label("Min Rating:");
        minRatingLabel.setTextFill(Color.web("#ccc"));
        ComboBox<String> minRatingBox = new ComboBox<>();
        minRatingBox.getItems().addAll("4.0", "4.2", "4.4", "4.5", "4.6", "4.7", "4.8");
        minRatingBox.setValue("4.5");
        styleComboBox(minRatingBox);

        // Category
        Label categoryLabel = new Label("Category:");
        categoryLabel.setTextFill(Color.web("#ccc"));
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(ProductDatabase.getCategories());
        categoryBox.setValue("All");
        styleComboBox(categoryBox);

        constraintsGrid.add(budgetLabel, 0, 0);
        constraintsGrid.add(budgetField, 1, 0);
        constraintsGrid.add(maxItemsLabel, 2, 0);
        constraintsGrid.add(maxItemsField, 3, 0);
        constraintsGrid.add(minRatingLabel, 0, 1);
        constraintsGrid.add(minRatingBox, 1, 1);
        constraintsGrid.add(categoryLabel, 2, 1);
        constraintsGrid.add(categoryBox, 3, 1);

        Button findBtn = new Button("🔍 Find All Valid Combinations");
        findBtn.setStyle(
                "-fx-background-color: #e94560;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12 25 12 25;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 14;"
        );

        VBox resultsArea = new VBox(15);

        findBtn.setOnAction(e -> {
            resultsArea.getChildren().clear();

            double budget;
            int maxItems;
            double minRating;

            try {
                budget = Double.parseDouble(budgetField.getText().trim());
                maxItems = Integer.parseInt(maxItemsField.getText().trim());
                minRating = Double.parseDouble(minRatingBox.getValue());
            } catch (NumberFormatException ex) {
                showError(resultsArea, "Please enter valid numbers for budget and max items.");
                return;
            }

            String category = categoryBox.getValue();
            WishlistOptimizer.Constraints constraints =
                    new WishlistOptimizer.Constraints(budget, maxItems, minRating, category);

            // Show loading label
            Label loading = new Label("⏳ Running backtracking algorithm...");
            loading.setTextFill(Color.web("#888"));
            resultsArea.getChildren().add(loading);

            WishlistOptimizer.BacktrackingResult result =
                    WishlistOptimizer.findOptimalWishlist(allProducts, constraints);

            resultsArea.getChildren().clear();
            showResults(resultsArea, result, budget);
        });

        root.getChildren().addAll(title, subtitle, algoNote, constraintsTitle, constraintsGrid, findBtn, resultsArea);
        return root;
    }

    private void showResults(VBox area, WishlistOptimizer.BacktrackingResult result, double budget) {

        // Stats row
        HBox statsRow = new HBox(20);
        statsRow.getChildren().addAll(
                buildStatCard("Combinations Found", String.valueOf(result.getAllCombinations().size()), "valid product sets", "#e94560"),
                buildStatCard("Nodes Explored", String.valueOf(result.getNodesExplored()), "backtracking nodes", "#ffd700"),
                buildStatCard("Nodes Pruned", String.valueOf(result.getNodesPruned()), "branches cut early", "#90ee90"),
                buildStatCard("Best Combo Items", String.valueOf(result.getBestCombination().size()), "highest rated set", "#e94560")
        );

        // Backtracking explanation
        Label btLabel = new Label("🌳 Backtracking explored " + result.getNodesExplored() +
                " nodes and pruned " + result.getNodesPruned() +
                " branches that exceeded budget. Found " +
                result.getAllCombinations().size() + " valid combinations total.");
        btLabel.setTextFill(Color.web("#888"));
        btLabel.setWrapText(true);
        btLabel.setFont(Font.font(12));
        btLabel.setStyle("-fx-background-color: #16213e; -fx-padding: 10 15 10 15; -fx-background-radius: 8;");

        if (result.getAllCombinations().isEmpty()) {
            showError(area, "No valid combinations found. Try relaxing your constraints.");
            return;
        }

        // Best combination
        Label bestTitle = new Label("🏆 Best Combination (Highest Total Rating):");
        bestTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        bestTitle.setTextFill(Color.WHITE);

        VBox bestList = new VBox(10);
        double bestTotal = 0;
        for (Product p : result.getBestCombination()) {
            bestList.getChildren().add(buildProductRow(p, true));
            bestTotal += p.getDiscountedPrice();
        }

        Label bestTotalLabel = new Label("Total Cost: ₹" + String.format("%.0f", bestTotal));
        bestTotalLabel.setTextFill(Color.web("#e94560"));
        bestTotalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        bestTotalLabel.setPadding(new Insets(5, 0, 0, 0));

        // All combinations (show first 5)
        Label allTitle = new Label("📋 All Valid Combinations (showing first 5 of " +
                result.getAllCombinations().size() + "):");
        allTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        allTitle.setTextFill(Color.WHITE);
        allTitle.setPadding(new Insets(10, 0, 0, 0));

        VBox allCombos = new VBox(10);
        int count = Math.min(5, result.getAllCombinations().size());
        for (int i = 0; i < count; i++) {
            List<Product> combo = result.getAllCombinations().get(i);
            allCombos.getChildren().add(buildComboCard(combo, i + 1));
        }

        area.getChildren().addAll(statsRow, btLabel, bestTitle, bestList, bestTotalLabel, allTitle, allCombos);
    }

    private VBox buildComboCard(List<Product> combo, int index) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(12));
        card.setStyle(
                "-fx-background-color: #16213e;" +
                        "-fx-border-color: #0f3460;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );

        double total = 0;
        StringBuilder names = new StringBuilder();
        for (Product p : combo) {
            total += p.getDiscountedPrice();
            names.append(p.getName()).append(", ");
        }
        if (names.length() > 2) names.setLength(names.length() - 2);

        Label comboTitle = new Label("Combo #" + index + " — " + combo.size() + " items");
        comboTitle.setTextFill(Color.web("#ccc"));
        comboTitle.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        Label comboNames = new Label(names.toString());
        comboNames.setTextFill(Color.web("#888"));
        comboNames.setWrapText(true);
        comboNames.setFont(Font.font(12));

        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER_LEFT);
        Label totalLabel = new Label("₹" + String.format("%.0f", total));
        totalLabel.setTextFill(Color.web("#e94560"));
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addAllBtn = new Button("Add All to Cart");
        addAllBtn.setStyle(
                "-fx-background-color: #e94560;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 5 12 5 12;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );
        addAllBtn.setOnAction(e -> {
            for (Product p : combo) {
                boolean found = false;
                for (CartItem item : cart) {
                    if (item.getProduct().getId() == p.getId()) {
                        item.setQuantity(item.getQuantity() + 1);
                        found = true;
                        break;
                    }
                }
                if (!found) cart.add(new CartItem(p, 1));
            }
            addAllBtn.setText("✓ Added");
            addAllBtn.setStyle(
                    "-fx-background-color: #2d6a2d;" +
                            "-fx-text-fill: #90ee90;" +
                            "-fx-padding: 5 12 5 12;" +
                            "-fx-background-radius: 5;"
            );
            if (updateCart != null) updateCart.run();
        });

        footer.getChildren().addAll(totalLabel, spacer, addAllBtn);
        card.getChildren().addAll(comboTitle, comboNames, footer);
        return card;
    }

    private HBox buildProductRow(Product product, boolean isBest) {
        HBox row = new HBox(15);
        row.setPadding(new Insets(10));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
                "-fx-background-color: " + (isBest ? "#1a2e1a" : "#16213e") + ";" +
                        "-fx-border-color: " + (isBest ? "#2d6a2d" : "#0f3460") + ";" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );

        Label name = new Label(product.getName());
        name.setTextFill(Color.WHITE);
        name.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        name.setPrefWidth(300);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label rating = new Label("⭐ " + product.getRating());
        rating.setTextFill(Color.web("#ffd700"));

        Label price = new Label("₹" + String.format("%.0f", product.getDiscountedPrice()));
        price.setTextFill(Color.web("#e94560"));
        price.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        Button addBtn = new Button("+ Cart");
        addBtn.setStyle(
                "-fx-background-color: #e94560;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 5 10 5 10;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );
        addBtn.setOnAction(e -> {
            boolean found = false;
            for (CartItem item : cart) {
                if (item.getProduct().getId() == product.getId()) {
                    item.setQuantity(item.getQuantity() + 1);
                    found = true;
                    break;
                }
            }
            if (!found) cart.add(new CartItem(product, 1));
            addBtn.setText("✓");
            addBtn.setStyle(
                    "-fx-background-color: #2d6a2d;" +
                            "-fx-text-fill: #90ee90;" +
                            "-fx-padding: 5 10 5 10;" +
                            "-fx-background-radius: 5;"
            );
            if (updateCart != null) updateCart.run();
        });

        row.getChildren().addAll(name, spacer, rating, price, addBtn);
        return row;
    }

    private VBox buildStatCard(String title, String value, String sub, String color) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle(
                "-fx-background-color: #16213e;" +
                        "-fx-border-color: #0f3460;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;"
        );
        HBox.setHgrow(card, Priority.ALWAYS);

        Label titleLabel = new Label(title);
        titleLabel.setTextFill(Color.web("#888"));
        titleLabel.setFont(Font.font(12));

        Label valueLabel = new Label(value);
        valueLabel.setTextFill(Color.web(color));
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        Label subLabel = new Label(sub);
        subLabel.setTextFill(Color.web("#666"));
        subLabel.setFont(Font.font(11));

        card.getChildren().addAll(titleLabel, valueLabel, subLabel);
        return card;
    }

    private void styleTextField(TextField field) {
        field.setStyle(
                "-fx-background-color: #0f3460;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #888;" +
                        "-fx-border-color: #e94560;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 8 12 8 12;"
        );
        field.setPrefWidth(150);
    }

    private void styleComboBox(ComboBox<String> box) {
        box.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
        box.setPrefWidth(150);
        box.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
            }
        });
        box.setButtonCell(new ListCell<>() {
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
            }
        });
    }

    private void showError(VBox area, String message) {
        Label err = new Label("⚠️ " + message);
        err.setTextFill(Color.web("#e94560"));
        err.setFont(Font.font(14));
        area.getChildren().add(err);
    }
}