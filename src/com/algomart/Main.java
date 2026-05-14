package com.algomart;

import com.algomart.algorithms.searching.BinarySearch;
import com.algomart.algorithms.sorting.QuickSort;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    // App state
    private List<Product> allProducts;
    private List<Product> displayedProducts;
    private List<CartItem> cart = new ArrayList<>();
    private BorderPane mainLayout;
    private Label cartCountLabel;
    private VBox contentArea;

    @Override
    public void start(Stage stage) {
        allProducts = ProductDatabase.getAllProducts();
        displayedProducts = new ArrayList<>(allProducts);

        // Sort by price by default
        QuickSort.sortByPrice(displayedProducts, 0, displayedProducts.size() - 1);

        // Root layout
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #1a1a2e;");

        // Build sections
        mainLayout.setTop(buildTopBar());
        mainLayout.setLeft(buildSidebar());
        mainLayout.setCenter(buildContentArea());

        Scene scene = new Scene(mainLayout, 1200, 750);
        stage.setTitle("AlgoMart");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(600);
        stage.show();

        // Load home screen
        showProductGrid(displayedProducts);
    }

    // ─── TOP BAR ───────────────────────────────────────────────
    private HBox buildTopBar() {
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: #16213e; -fx-border-color: #0f3460; -fx-border-width: 0 0 2 0;");
        topBar.setPadding(new Insets(12, 20, 12, 20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setSpacing(20);

        // Logo
        Label logo = new Label("🛒 AlgoMart");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        logo.setTextFill(Color.web("#e94560"));

        // Search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search products...");
        searchField.setPrefWidth(400);
        searchField.setStyle(
                "-fx-background-color: #0f3460;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #888;" +
                        "-fx-border-color: #e94560;" +
                        "-fx-border-radius: 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-padding: 8 15 8 15;"
        );

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.isEmpty()) {
                displayedProducts = new ArrayList<>(allProducts);
                QuickSort.sortByPrice(displayedProducts, 0, displayedProducts.size() - 1);
            } else {
                displayedProducts = BinarySearch.searchByKeyword(allProducts, newVal);
            }
            showProductGrid(displayedProducts);
        });

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Cart button
        cartCountLabel = new Label("0");
        cartCountLabel.setStyle(
                "-fx-background-color: #e94560;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 10;" +
                        "-fx-padding: 2 6 2 6;" +
                        "-fx-background-radius: 10;"
        );

        Button cartBtn = new Button("🛒 Cart");
        cartBtn.setStyle(
                "-fx-background-color: #e94560;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 20 8 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;"
        );
        cartBtn.setOnAction(e -> showCart());

        topBar.getChildren().addAll(logo, searchField, spacer, cartCountLabel, cartBtn);
        return topBar;
    }

    // ─── SIDEBAR ───────────────────────────────────────────────
    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-background-color: #16213e;");
        sidebar.setPrefWidth(200);
        sidebar.setPadding(new Insets(20, 0, 20, 0));
        sidebar.setSpacing(5);

        Label navLabel = new Label("NAVIGATION");
        navLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11; -fx-padding: 0 0 10 20;");

        sidebar.getChildren().addAll(
                navLabel,
                sidebarButton("🏠  Home", () -> {
                    displayedProducts = new ArrayList<>(allProducts);
                    QuickSort.sortByPrice(displayedProducts, 0, displayedProducts.size() - 1);
                    showProductGrid(displayedProducts);
                }),
                sidebarButton("📦  All Products", () -> {
                    displayedProducts = new ArrayList<>(allProducts);
                    showProductGrid(displayedProducts);
                }),
                categorySection(),
                sidebarButton("💰  Budget Planner", () -> showBudgetPlanner()),
                sidebarButton("❤️  Wishlist", () -> showWishlist()),
                sidebarButton("📊  Visualizer", () -> showVisualizer())
        );

        return sidebar;
    }

    private Button sidebarButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setPrefWidth(200);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #ccc;" +
                        "-fx-font-size: 13;" +
                        "-fx-padding: 12 20 12 20;" +
                        "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #0f3460;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13;" +
                        "-fx-padding: 12 20 12 20;" +
                        "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-text-fill: #ccc;" +
                        "-fx-font-size: 13;" +
                        "-fx-padding: 12 20 12 20;" +
                        "-fx-cursor: hand;"
        ));
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private VBox categorySection() {
        VBox section = new VBox();
        Label catLabel = new Label("CATEGORIES");
        catLabel.setStyle("-fx-text-fill: #888; -fx-font-size: 11; -fx-padding: 15 0 5 20;");

        section.getChildren().add(catLabel);
        for (String cat : ProductDatabase.getCategories()) {
            if (cat.equals("All")) continue;
            Button btn = sidebarButton("  · " + cat, () -> {
                displayedProducts = new ArrayList<>();
                for (Product p : allProducts) {
                    if (p.getCategory().equals(cat)) displayedProducts.add(p);
                }
                showProductGrid(displayedProducts);
            });
            section.getChildren().add(btn);
        }
        return section;
    }

    // ─── CONTENT AREA ──────────────────────────────────────────
    private ScrollPane buildContentArea() {
        contentArea = new VBox();
        contentArea.setStyle("-fx-background-color: #1a1a2e;");
        contentArea.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(contentArea);
        scrollPane.setStyle("-fx-background-color: #1a1a2e; -fx-background: #1a1a2e;");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }

    // ─── PRODUCT GRID ──────────────────────────────────────────
    public void showProductGrid(List<Product> products) {
        contentArea.getChildren().clear();

        // Sort controls
        HBox sortBar = new HBox(10);
        sortBar.setPadding(new Insets(0, 0, 15, 0));
        sortBar.setAlignment(Pos.CENTER_LEFT);

        Label sortLabel = new Label("Sort by:");
        sortLabel.setTextFill(Color.web("#ccc"));

        ComboBox<String> sortAlgo = new ComboBox<>();
        sortAlgo.getItems().addAll("QuickSort", "MergeSort", "HeapSort");
        sortAlgo.setValue("QuickSort");
        sortAlgo.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-mark-color: white;");
        sortAlgo.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
            }
        });
        sortAlgo.setButtonCell(new ListCell<>() {
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
            }
        });

        ComboBox<String> sortField = new ComboBox<>();
        sortField.getItems().addAll("Price ↑", "Rating ↓", "Name A-Z");
        sortField.setValue("Price ↑");
        sortField.setStyle("-fx-background-color: #0f3460; -fx-text-fill: white; -fx-mark-color: white;");
        sortField.setCellFactory(lv -> new ListCell<>() {
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
            }
        });
        sortField.setButtonCell(new ListCell<>() {
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-background-color: #0f3460; -fx-text-fill: white;");
            }
        });

        Button sortBtn = new Button("Apply Sort");
        sortBtn.setStyle(
                "-fx-background-color: #e94560; -fx-text-fill: white;" +
                        "-fx-padding: 6 15 6 15; -fx-background-radius: 5; -fx-cursor: hand;"
        );

        Label statsLabel = new Label("");
        statsLabel.setTextFill(Color.web("#888"));
        statsLabel.setFont(Font.font("Arial", 11));

        sortBtn.setOnAction(e -> {
            List<Product> toSort = new ArrayList<>(products);
            String algo = sortAlgo.getValue();
            String field = sortField.getValue();
            long start = System.nanoTime();

            if (algo.equals("QuickSort")) {
                QuickSort.resetCount();
                if (field.equals("Price ↑")) QuickSort.sortByPrice(toSort, 0, toSort.size() - 1);
                else if (field.equals("Rating ↓")) QuickSort.sortByRating(toSort, 0, toSort.size() - 1);
                else QuickSort.sortByName(toSort, 0, toSort.size() - 1);
                statsLabel.setText("QuickSort | " + QuickSort.operationCount + " ops | O(n log n)");
            } else if (algo.equals("MergeSort")) {
                com.algomart.algorithms.sorting.MergeSort.resetCount();
                if (field.equals("Price ↑")) com.algomart.algorithms.sorting.MergeSort.sortByPrice(toSort, 0, toSort.size() - 1);
                else if (field.equals("Rating ↓")) com.algomart.algorithms.sorting.MergeSort.sortByRating(toSort, 0, toSort.size() - 1);
                else com.algomart.algorithms.sorting.MergeSort.sortByName(toSort, 0, toSort.size() - 1);
                statsLabel.setText("MergeSort | " + com.algomart.algorithms.sorting.MergeSort.operationCount + " ops | O(n log n)");
            } else {
                com.algomart.algorithms.sorting.HeapSort.resetCount();
                if (field.equals("Price ↑")) com.algomart.algorithms.sorting.HeapSort.sortByPrice(toSort);
                else if (field.equals("Rating ↓")) com.algomart.algorithms.sorting.HeapSort.sortByRating(toSort);
                else com.algomart.algorithms.sorting.HeapSort.sortByName(toSort);
                statsLabel.setText("HeapSort | " + com.algomart.algorithms.sorting.HeapSort.operationCount + " ops | O(n log n)");
            }

            long end = System.nanoTime();
            statsLabel.setText(statsLabel.getText() + " | " + (end - start) / 1_000_000.0 + "ms");
            displayedProducts = toSort;
            renderProductCards(toSort);
        });

        sortBar.getChildren().addAll(sortLabel, sortAlgo, sortField, sortBtn, statsLabel);

        Label countLabel = new Label(products.size() + " products found");
        countLabel.setTextFill(Color.web("#888"));
        countLabel.setPadding(new Insets(0, 0, 10, 0));

        contentArea.getChildren().addAll(sortBar, countLabel);
        renderProductCards(products);
    }

    private void renderProductCards(List<Product> products) {
        // Remove old cards but keep sort bar
        while (contentArea.getChildren().size() > 2) {
            contentArea.getChildren().remove(contentArea.getChildren().size() - 1);
        }

        FlowPane grid = new FlowPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10, 0, 10, 0));

        for (Product p : products) {
            grid.getChildren().add(buildProductCard(p));
        }

        contentArea.getChildren().add(grid);
    }

    private VBox buildProductCard(Product product) {
        VBox card = new VBox(8);
        card.setPrefWidth(200);
        card.setPadding(new Insets(15));
        card.setStyle(
                "-fx-background-color: #16213e;" +
                        "-fx-border-color: #0f3460;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );

        // Category badge
        Label category = new Label(product.getCategory());
        category.setStyle(
                "-fx-background-color: #0f3460;" +
                        "-fx-text-fill: #e94560;" +
                        "-fx-font-size: 10;" +
                        "-fx-padding: 3 8 3 8;" +
                        "-fx-background-radius: 10;"
        );

        // Product name
        Label name = new Label(product.getName());
        name.setTextFill(Color.WHITE);
        name.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        name.setWrapText(true);

        // Rating
        Label rating = new Label("⭐ " + product.getRating());
        rating.setTextFill(Color.web("#ffd700"));
        rating.setFont(Font.font(12));

        // Price
        Label price = new Label("₹" + String.format("%.0f", product.getDiscountedPrice()));
        price.setTextFill(Color.web("#e94560"));
        price.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Original price if discount
        if (product.getDiscountPercent() > 0) {
            Label originalPrice = new Label("₹" + String.format("%.0f", product.getPrice()));
            originalPrice.setTextFill(Color.web("#888"));
            originalPrice.setStyle("-fx-strikethrough: true; -fx-font-size: 11;");

            Label discount = new Label(product.getDiscountPercent() + "% OFF");
            discount.setStyle(
                    "-fx-background-color: #2d6a2d;" +
                            "-fx-text-fill: #90ee90;" +
                            "-fx-font-size: 10;" +
                            "-fx-padding: 2 6 2 6;" +
                            "-fx-background-radius: 5;"
            );

            card.getChildren().addAll(category, name, rating, price, originalPrice, discount);
        } else {
            card.getChildren().addAll(category, name, rating, price);
        }

        // Stock status
        Label stock = new Label(product.getStock() > 0 ? "In Stock (" + product.getStock() + ")" : "Out of Stock");
        stock.setTextFill(product.getStock() > 0 ? Color.web("#90ee90") : Color.web("#e94560"));
        stock.setFont(Font.font(11));

        // Add to cart button
        Button addToCart = new Button("Add to Cart");
        addToCart.setPrefWidth(Double.MAX_VALUE);
        addToCart.setStyle(
                "-fx-background-color: #e94560;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 8 0 8 0;" +
                        "-fx-background-radius: 5;" +
                        "-fx-cursor: hand;"
        );
        addToCart.setDisable(product.getStock() == 0);
        addToCart.setOnAction(e -> addToCart(product));

        card.getChildren().addAll(stock, addToCart);

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: #0f3460;" +
                        "-fx-border-color: #e94560;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, #e94560, 10, 0, 0, 0);"
        ));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: #16213e;" +
                        "-fx-border-color: #0f3460;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        ));

        return card;
    }

    // ─── CART ──────────────────────────────────────────────────
    private void addToCart(Product product) {
        for (CartItem item : cart) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                updateCartCount();
                return;
            }
        }
        cart.add(new CartItem(product, 1));
        updateCartCount();
    }

    private void updateCartCount() {
        int total = cart.stream().mapToInt(CartItem::getQuantity).sum();
        cartCountLabel.setText(total + " items");
    }

    private void showCart() {
        contentArea.getChildren().clear();

        Label title = new Label("🛒 Your Cart");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);
        title.setPadding(new Insets(0, 0, 20, 0));

        contentArea.getChildren().add(title);

        if (cart.isEmpty()) {
            Label empty = new Label("Your cart is empty. Start shopping!");
            empty.setTextFill(Color.web("#888"));
            empty.setFont(Font.font(16));
            contentArea.getChildren().add(empty);
            return;
        }

        double subtotal = 0;
        for (CartItem item : cart) {
            HBox row = buildCartRow(item);
            contentArea.getChildren().add(row);
            subtotal += item.getTotalPrice();
        }

        // Greedy discount
        com.algomart.algorithms.greedy.DiscountOptimizer.OptimizationResult result =
                com.algomart.algorithms.greedy.DiscountOptimizer.getBestCoupon(subtotal);

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #0f3460;");

        VBox summary = new VBox(8);
        summary.setPadding(new Insets(15));
        summary.setStyle("-fx-background-color: #16213e; -fx-background-radius: 10;");

        Label subtotalLabel = new Label("Subtotal: ₹" + String.format("%.2f", subtotal));
        subtotalLabel.setTextFill(Color.web("#ccc"));

        Label discountLabel = new Label("Best Coupon: " +
                (result.getBestCoupon() != null ? result.getBestCoupon().getCode() : "None") +
                " (-₹" + String.format("%.2f", result.getDiscountAmount()) + ")");
        discountLabel.setTextFill(Color.web("#90ee90"));

        Label totalLabel = new Label("Total: ₹" + String.format("%.2f", result.getFinalTotal()));
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        totalLabel.setTextFill(Color.web("#e94560"));

        Label greedyNote = new Label("💡 Greedy Algorithm selected the best coupon from " +
                result.getAllApplicable().size() + " applicable offers");
        greedyNote.setTextFill(Color.web("#888"));
        greedyNote.setFont(Font.font(11));
        greedyNote.setWrapText(true);

        Button checkout = new Button("Proceed to Checkout");
        checkout.setStyle(
                "-fx-background-color: #e94560; -fx-text-fill: white;" +
                        "-fx-font-weight: bold; -fx-padding: 12 30 12 30;" +
                        "-fx-background-radius: 5; -fx-cursor: hand;"
        );
        checkout.setOnAction(e -> {
            cart.clear();
            updateCartCount();
            Label success = new Label("✅ Order placed successfully! Thank you for shopping at AlgoMart.");
            success.setTextFill(Color.web("#90ee90"));
            success.setFont(Font.font(16));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(success);
        });

        summary.getChildren().addAll(subtotalLabel, discountLabel, totalLabel, greedyNote, checkout);
        contentArea.getChildren().addAll(sep, summary);
    }

    private HBox buildCartRow(CartItem item) {
        HBox row = new HBox(15);
        row.setPadding(new Insets(10));
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: #16213e; -fx-background-radius: 8;");

        Label name = new Label(item.getProduct().getName());
        name.setTextFill(Color.WHITE);
        name.setPrefWidth(300);
        name.setWrapText(true);

        Label qty = new Label("Qty: " + item.getQuantity());
        qty.setTextFill(Color.web("#ccc"));

        Label price = new Label("₹" + String.format("%.2f", item.getTotalPrice()));
        price.setTextFill(Color.web("#e94560"));
        price.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Button remove = new Button("Remove");
        remove.setStyle(
                "-fx-background-color: #333; -fx-text-fill: #e94560;" +
                        "-fx-cursor: hand; -fx-padding: 5 10 5 10;"
        );
        remove.setOnAction(e -> {
            cart.remove(item);
            updateCartCount();
            showCart();
        });

        row.getChildren().addAll(name, qty, price, remove);
        return row;
    }

    // ─── PLACEHOLDERS ──────────────────────────────────────────
    private void showBudgetPlanner() {
        contentArea.getChildren().clear();
        BudgetPlannerScreen screen = new BudgetPlannerScreen(allProducts, cart, this::updateCartCount);
        contentArea.getChildren().add(screen.getView());
    }

    private void showWishlist() {
        contentArea.getChildren().clear();
        WishlistScreen screen = new WishlistScreen(allProducts, cart, this::updateCartCount);
        contentArea.getChildren().add(screen.getView());
    }

    private void showVisualizer() {
        com.algomart.visualizer.SortifyVisualizer sortify =
                new com.algomart.visualizer.SortifyVisualizer(allProducts, cart, () -> {
                    mainLayout.setTop(buildTopBar());
                    mainLayout.setLeft(buildSidebar());
                    mainLayout.setRight(null);
                    mainLayout.setBottom(null);
                    mainLayout.setCenter(buildContentArea());
                    showProductGrid(displayedProducts);
                });
        mainLayout.setTop(null);
        mainLayout.setLeft(null);
        mainLayout.setRight(null);
        mainLayout.setBottom(null);
        mainLayout.setCenter(sortify.getView());
    }

    public static void main(String[] args) {
        launch(args);
    }
}