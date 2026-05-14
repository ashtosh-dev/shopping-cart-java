package com.algomart;

import com.algomart.algorithms.dp.KnapsackSolver;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class BudgetPlannerScreen {

    private List<Product> allProducts;
    private List<CartItem> cart;
    private Runnable updateCart;

    public BudgetPlannerScreen(List<Product> allProducts, List<CartItem> cart, Runnable updateCart) {
        this.allProducts = allProducts;
        this.cart = cart;
        this.updateCart = updateCart;
    }

    public VBox getView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Label title = new Label("💰 Budget Planner");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Enter your budget and we'll find the best combination of products using the 0/1 Knapsack algorithm.");
        subtitle.setTextFill(Color.web("#888"));
        subtitle.setWrapText(true);
        subtitle.setFont(Font.font(13));

        Label algoNote = new Label("🧠 Algorithm: Dynamic Programming — 0/1 Knapsack  |  Time Complexity: O(n × W)");
        algoNote.setStyle(
                "-fx-background-color: #0f3460;" +
                        "-fx-text-fill: #e94560;" +
                        "-fx-padding: 8 15 8 15;" +
                        "-fx-background-radius: 8;"
        );
        algoNote.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        HBox inputRow = new HBox(15);
        inputRow.setAlignment(Pos.CENTER_LEFT);

        Label budgetLabel = new Label("Your Budget (₹):");
        budgetLabel.setTextFill(Color.web("#ccc"));
        budgetLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextField budgetField = new TextField();
        budgetField.setPromptText("e.g. 5000");
        budgetField.setPrefWidth(200);
        budgetField.setStyle(
                "-fx-background-color: #0f3460;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #888;" +
                        "-fx-border-color: #e94560;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10 15 10 15;" +
                        "-fx-font-size: 14;"
        );

        Label catLabel = new Label("Category:");
        catLabel.setTextFill(Color.web("#ccc"));
        catLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(ProductDatabase.getCategories());
        categoryBox.setValue("All");
        categoryBox.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
        categoryBox.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
            }
        });
        categoryBox.setButtonCell(new ListCell<>() {
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
            }
        });

        Button planBtn = new Button("🚀 Find Best Items");
        planBtn.setStyle(
                "-fx-background-color: #e94560;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 25 10 25;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;" +
                        "-fx-font-size: 14;"
        );

        inputRow.getChildren().addAll(budgetLabel, budgetField, catLabel, categoryBox, planBtn);

        VBox resultsArea = new VBox(15);

        planBtn.setOnAction(e -> {
            resultsArea.getChildren().clear();

            String budgetText = budgetField.getText().trim();
            if (budgetText.isEmpty()) {
                showError(resultsArea, "Please enter a budget amount.");
                return;
            }

            double budget;
            try {
                budget = Double.parseDouble(budgetText);
            } catch (NumberFormatException ex) {
                showError(resultsArea, "Please enter a valid number.");
                return;
            }

            if (budget <= 0) {
                showError(resultsArea, "Budget must be greater than 0.");
                return;
            }

            List<Product> filtered = new java.util.ArrayList<>();
            String selectedCat = categoryBox.getValue();
            for (Product p : allProducts) {
                if (selectedCat.equals("All") || p.getCategory().equals(selectedCat)) {
                    filtered.add(p);
                }
            }

            KnapsackSolver.KnapsackResult result = KnapsackSolver.solve(filtered, budget);
            showResults(resultsArea, result, budget);
        });

        root.getChildren().addAll(title, subtitle, algoNote, inputRow, resultsArea);
        return root;
    }

    private void showResults(VBox area, KnapsackSolver.KnapsackResult result, double budget) {
        if (result.getSelectedProducts().isEmpty()) {
            showError(area, "No products fit within this budget. Try increasing your budget.");
            return;
        }

        HBox summaryRow = new HBox(20);
        summaryRow.getChildren().addAll(
                buildStatCard("Total Spent", "₹" + String.format("%.0f", result.getTotalCost()), "out of ₹" + String.format("%.0f", budget), "#e94560"),
                buildStatCard("Remaining Budget", "₹" + String.format("%.0f", budget - result.getTotalCost()), "saved for later", "#90ee90"),
                buildStatCard("Items Selected", String.valueOf(result.getSelectedProducts().size()), "optimal combination", "#ffd700"),
                buildStatCard("Total Rating Score", String.format("%.1f", result.getTotalValue()), "maximum value achieved", "#e94560")
        );

        Label dpLabel = new Label("📊 How Knapsack worked: Evaluated " +
                result.getSelectedProducts().size() + " items against ₹" +
                String.format("%.0f", budget) + " budget. Built a DP table to maximize rating value while staying within budget.");
        dpLabel.setTextFill(Color.web("#888"));
        dpLabel.setWrapText(true);
        dpLabel.setFont(Font.font(12));
        dpLabel.setStyle("-fx-background-color: #16213e; -fx-padding: 10 15 10 15; -fx-background-radius: 8;");

        Label productsTitle = new Label("✅ Recommended Items:");
        productsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        productsTitle.setTextFill(Color.WHITE);

        VBox productsList = new VBox(10);
        for (Product p : result.getSelectedProducts()) {
            productsList.getChildren().add(buildResultProductRow(p));
        }

        area.getChildren().addAll(summaryRow, dpLabel, productsTitle, productsList);
    }

    private HBox buildResultProductRow(Product product) {
        HBox row = new HBox(15);
        row.setPadding(new Insets(12));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
                "-fx-background-color: #16213e;" +
                        "-fx-border-color: #0f3460;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );

        Label category = new Label(product.getCategory());
        category.setStyle(
                "-fx-background-color: #0f3460;" +
                        "-fx-text-fill: #e94560;" +
                        "-fx-font-size: 10;" +
                        "-fx-padding: 3 8 3 8;" +
                        "-fx-background-radius: 10;"
        );

        Label name = new Label(product.getName());
        name.setTextFill(Color.WHITE);
        name.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        name.setPrefWidth(300);
        name.setWrapText(true);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label rating = new Label("⭐ " + product.getRating());
        rating.setTextFill(Color.web("#ffd700"));

        Label price = new Label("₹" + String.format("%.0f", product.getDiscountedPrice()));
        price.setTextFill(Color.web("#e94560"));
        price.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Button addBtn = new Button("+ Cart");
        addBtn.setStyle(
                "-fx-background-color: #e94560;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 6 12 6 12;" +
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
            addBtn.setText("✓ Added");
            addBtn.setStyle(
                    "-fx-background-color: #2d6a2d;" +
                            "-fx-text-fill: #90ee90;" +
                            "-fx-font-weight: bold;" +
                            "-fx-padding: 6 12 6 12;" +
                            "-fx-background-radius: 5;"
            );
            if (updateCart != null) updateCart.run();
        });

        row.getChildren().addAll(category, name, spacer, rating, price, addBtn);
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

    private void showError(VBox area, String message) {
        Label err = new Label("⚠️ " + message);
        err.setTextFill(Color.web("#e94560"));
        err.setFont(Font.font(14));
        area.getChildren().add(err);
    }
}