package com.algomart.visualizer;

import com.algomart.CartItem;
import com.algomart.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;

public class SortifyVisualizer {

    private BorderPane mainLayout;
    private Runnable onExit;
    private List<Product> products;
    private List<CartItem> cart;
    private String activeAlgorithm = "Binary Search";

    public SortifyVisualizer(List<Product> products, List<CartItem> cart, Runnable onExit) {
        this.products = products;
        this.cart = cart;
        this.onExit = onExit;
        
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f7f9fc;");

        // Build main areas
        mainLayout.setTop(buildTopBar());
        mainLayout.setLeft(buildLeftSidebar());
        mainLayout.setRight(buildRightSidebar());
        mainLayout.setCenter(buildCenterMain());
        mainLayout.setBottom(buildFooter());
    }

    public BorderPane getView() {
        return mainLayout;
    }

    private HBox buildTopBar() {
        HBox topBar = new HBox(30);
        topBar.setPadding(new Insets(15, 30, 15, 30));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");

        // Logo area
        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        Label logoIcon = new Label("🧊");
        logoIcon.setTextFill(Color.web("#3b82f6"));
        logoIcon.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        VBox logoText = new VBox();
        Label sortify = new Label("Sortify");
        sortify.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label subTitle = new Label("Visualize. Understand. Master.");
        subTitle.setFont(Font.font("Arial", 10));
        subTitle.setTextFill(Color.GRAY);
        logoText.getChildren().addAll(sortify, subTitle);
        logoBox.getChildren().addAll(logoIcon, logoText);

        // Spacer
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        // Algorithms nav
        HBox nav = new HBox(20);
        nav.setAlignment(Pos.CENTER);
        String[] algos = {"Merge Sort", "Quick Sort", "Binary Search"};
        for (String algo : algos) {
            Button btn = new Button(algo);
            if (algo.equals(activeAlgorithm)) {
                btn.setStyle("-fx-background-color: #eff6ff; -fx-text-fill: #2563eb; -fx-font-weight: bold; -fx-background-radius: 15; -fx-padding: 8 16;");
            } else {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #64748b; -fx-padding: 8 16; -fx-cursor: hand;");
            }
            btn.setOnAction(e -> {
                activeAlgorithm = algo;
                mainLayout.setTop(buildTopBar());
                mainLayout.setCenter(buildCenterMain());
            });
            nav.getChildren().add(btn);
        }

        // Spacer
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        Button exitBtn = new Button("Exit Visualizer");
        exitBtn.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 8 16;");
        exitBtn.setOnAction(e -> onExit.run());

        Button modeBtn = new Button("🌙");
        modeBtn.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-background-radius: 20; -fx-border-radius: 20;");
        
        topBar.getChildren().addAll(logoBox, spacer1, nav, spacer2, exitBtn, modeBtn);
        return topBar;
    }

    private VBox buildLeftSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPrefWidth(260);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 0 1 0 0;");

        // Shopping Cart Section
        HBox cartTitleBox = new HBox(10);
        cartTitleBox.setAlignment(Pos.CENTER_LEFT);
        Label cartIcon = new Label("🛒");
        Label cartTitle = new Label("SHOPPING CART");
        cartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Label cartCount = new Label(String.valueOf(cart.size()));
        cartCount.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-padding: 2 6; -fx-background-radius: 10; -fx-font-size: 10;");
        cartTitleBox.getChildren().addAll(cartIcon, cartTitle, cartCount);

        VBox cartItems = new VBox(15);
        if (cart.isEmpty()) {
            Label emptyLbl = new Label("Cart is empty. Add items from home.");
            emptyLbl.setTextFill(Color.GRAY);
            emptyLbl.setFont(Font.font("Arial", 11));
            cartItems.getChildren().add(emptyLbl);
        } else {
            for (CartItem item : cart) {
                addMockItem(cartItems, 
                    item.getProduct().getName(), 
                    String.format("$%.2f", item.getProduct().getPrice()), 
                    String.valueOf(item.getQuantity()));
            }
        }

        Button addItemBtn = new Button("+ Add Item");
        addItemBtn.setMaxWidth(Double.MAX_VALUE);
        addItemBtn.setStyle("-fx-background-color: #f8fafc; -fx-border-color: #e2e8f0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10; -fx-text-fill: #64748b;");

        // Controls Section
        Label controlsTitle = new Label("CONTROLS");
        controlsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        controlsTitle.setPadding(new Insets(10, 0, 0, 0));

        GridPane btnGrid = new GridPane();
        btnGrid.setHgap(10);
        btnGrid.setVgap(10);
        
        Button playBtn = new Button("▶ Play / Pause");
        playBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 15; -fx-background-radius: 8;");
        Button nextBtn = new Button("⏭ Next Step");
        nextBtn.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-padding: 10 15; -fx-background-radius: 8; -fx-border-radius: 8;");
        Button prevBtn = new Button("⏮ Previous Step");
        prevBtn.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-padding: 10 15; -fx-background-radius: 8; -fx-border-radius: 8;");
        Button resetBtn = new Button("🔄 Reset");
        resetBtn.setStyle("-fx-background-color: #fef2f2; -fx-text-fill: #ef4444; -fx-border-color: #fee2e2; -fx-padding: 10 15; -fx-background-radius: 8; -fx-border-radius: 8;");

        btnGrid.add(playBtn, 0, 0);
        btnGrid.add(nextBtn, 1, 0);
        btnGrid.add(prevBtn, 0, 1);
        btnGrid.add(resetBtn, 1, 1);

        // Speed Section
        Label speedTitle = new Label("SPEED");
        speedTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        speedTitle.setPadding(new Insets(10, 0, 0, 0));
        
        HBox speedBox = new HBox(10);
        speedBox.setAlignment(Pos.CENTER);
        Label slow = new Label("Slow");
        slow.setStyle("-fx-font-size: 11; -fx-text-fill: #64748b;");
        Slider speedSlider = new Slider(1, 10, 5);
        Label fast = new Label("Fast");
        fast.setStyle("-fx-font-size: 11; -fx-text-fill: #64748b;");
        speedBox.getChildren().addAll(slow, speedSlider, fast);

        sidebar.getChildren().addAll(cartTitleBox, cartItems, addItemBtn, controlsTitle, btnGrid, speedTitle, speedBox);
        return sidebar;
    }

    private void addMockItem(VBox container, String name, String price, String qty) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        
        VBox texts = new VBox(2);
        Label n = new Label(name);
        n.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        Label p = new Label(price);
        p.setFont(Font.font("Arial", 11));
        p.setTextFill(Color.GRAY);
        texts.getChildren().addAll(n, p);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label q = new Label(qty);
        q.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        if (activeAlgorithm.equals("Binary Search") && qty.equals("30")) {
            q.setTextFill(Color.web("#22c55e")); // Green target
        } else {
            q.setTextFill(Color.web("#f59e0b")); // Orange/yellow regular
        }

        item.getChildren().addAll(texts, spacer, q);
        container.getChildren().add(item);
        
        // Add subtle divider
        Line div = new Line(0, 0, 200, 0);
        div.setStroke(Color.web("#f1f5f9"));
        container.getChildren().add(div);
    }

    private VBox buildRightSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPrefWidth(280);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 0 1;");

        // Steps
        Label stepsTitle = new Label("STEP BY STEP");
        stepsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        VBox stepsBox = new VBox(10);
        if (activeAlgorithm.equals("Binary Search")) {
            addStep(stepsBox, "1. Start with middle element", false);
            addStep(stepsBox, "2. Compare with target", false);
            addStep(stepsBox, "3. Target is greater", true);
            addStep(stepsBox, "4. Discard left half", false);
            addStep(stepsBox, "5. Find middle of remaining", false);
            addStep(stepsBox, "6. Compare with target", false);
            addStep(stepsBox, "7. Target found!", false);
        } else {
            addStep(stepsBox, "1. Initialize algorithm", false);
            addStep(stepsBox, "2. Execute step", true);
            addStep(stepsBox, "3. Complete", false);
        }

        // Stats
        Label statsTitle = new Label("STATISTICS");
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        statsTitle.setPadding(new Insets(15, 0, 0, 0));
        
        VBox statsBox = new VBox(8);
        addStat(statsBox, "Comparisons", "2");
        addStat(statsBox, "Steps", "3");
        if (activeAlgorithm.equals("Binary Search")) {
            addStat(statsBox, "Search Range", "2 - 4");
        }
        addStat(statsBox, "Time", "00:06");

        // Legend
        Label legendTitle = new Label("LEGEND");
        legendTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        legendTitle.setPadding(new Insets(15, 0, 0, 0));
        
        VBox legendBox = new VBox(8);
        if (activeAlgorithm.equals("Binary Search")) {
            addLegend(legendBox, "#3b82f6", "Current Element");
            addLegend(legendBox, "#22c55e", "Target Value");
            addLegend(legendBox, "#f59e0b", "Discarded (Too Small)");
            addLegend(legendBox, "#ef4444", "Discarded (Too Large)");
        } else {
            addLegend(legendBox, "#3b82f6", "Current");
            addLegend(legendBox, "#f59e0b", "Comparing");
            addLegend(legendBox, "#22c55e", "Merged / Sorted");
        }

        sidebar.getChildren().addAll(stepsTitle, stepsBox, statsTitle, statsBox, legendTitle, legendBox);
        return sidebar;
    }

    private void addStep(VBox box, String text, boolean isCurrent) {
        Label l = new Label(isCurrent ? "▶ " + text : "   " + text);
        l.setFont(Font.font("Arial", isCurrent ? FontWeight.BOLD : FontWeight.NORMAL, 12));
        if (isCurrent) l.setTextFill(Color.web("#3b82f6"));
        else l.setTextFill(Color.web("#475569"));
        box.getChildren().add(l);
    }

    private void addStat(VBox box, String label, String value) {
        HBox row = new HBox();
        Label l = new Label(label);
        l.setTextFill(Color.web("#475569"));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label v = new Label(value);
        v.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        row.getChildren().addAll(l, spacer, v);
        box.getChildren().add(row);
    }

    private void addLegend(VBox box, String colorHex, String text) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        Rectangle colorBox = new Rectangle(10, 10, Color.web(colorHex));
        colorBox.setArcWidth(4);
        colorBox.setArcHeight(4);
        Label l = new Label(text);
        l.setTextFill(Color.web("#475569"));
        row.getChildren().addAll(colorBox, l);
        box.getChildren().add(row);
    }

    private VBox buildCenterMain() {
        VBox center = new VBox();
        center.setPadding(new Insets(20, 40, 20, 40));

        // Header info
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        Label algoLabel = new Label("ALGORITHM: " + activeAlgorithm.toUpperCase());
        algoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        Region s1 = new Region();
        HBox.setHgrow(s1, Priority.ALWAYS);
        
        Label stepInfo = new Label("STEP 3 / 7");
        ProgressBar progress = new ProgressBar(0.42);
        progress.setPrefWidth(100);
        progress.setStyle("-fx-accent: #3b82f6;");
        HBox progressBox = new HBox(10, stepInfo, progress);
        progressBox.setAlignment(Pos.CENTER);
        
        Region s2 = new Region();
        HBox.setHgrow(s2, Priority.ALWAYS);
        
        Label statusLabel = new Label(activeAlgorithm.equals("Binary Search") ? "STATUS: SEARCHING..." : "STATUS: PROCESSING...");
        statusLabel.setTextFill(Color.web("#3b82f6"));
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        header.getChildren().addAll(algoLabel, s1, progressBox, s2, statusLabel);

        // Descriptive text
        Label desc = new Label(activeAlgorithm.equals("Binary Search") ? "Searching for 30 in the sorted list." : "Dividing and sorting.");
        desc.setFont(Font.font("Arial", 14));
        desc.setPadding(new Insets(20, 0, 0, 0));
        HBox descBox = new HBox(desc);
        descBox.setAlignment(Pos.CENTER);

        // Visualizer Canvas (A large oval holding elements)
        StackPane canvasArea = new StackPane();
        canvasArea.setPadding(new Insets(30, 0, 30, 0));

        // Central white oval to mimic UI
        Rectangle ovalBg = new Rectangle(650, 400);
        ovalBg.setArcWidth(400);
        ovalBg.setArcHeight(400);
        ovalBg.setFill(Color.WHITE);
        ovalBg.setStroke(Color.web("#e2e8f0"));
        ovalBg.setStrokeWidth(2);
        
        // Inner visualization
        VBox vizInnerBox = new VBox(30);
        vizInnerBox.setAlignment(Pos.CENTER);
        
        if (activeAlgorithm.equals("Binary Search")) {
            // Target
            VBox targetBox = new VBox(5);
            targetBox.setAlignment(Pos.CENTER);
            Label tLabel = new Label("TARGET VALUE");
            tLabel.setTextFill(Color.web("#22c55e"));
            tLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            Label tVal = new Label("30");
            tVal.setFont(Font.font("Arial", FontWeight.BOLD, 24));
            tVal.setTextFill(Color.web("#22c55e"));
            tVal.setStyle("-fx-border-color: #22c55e; -fx-border-style: dashed; -fx-border-radius: 8; -fx-padding: 10 20;");
            targetBox.getChildren().addAll(tLabel, tVal);
            
            // Pointer arrow
            Label arrow = new Label("▼");
            arrow.setTextFill(Color.web("#22c55e"));
            arrow.setFont(Font.font("Arial", 20));
            HBox arrowBox = new HBox(arrow);
            arrowBox.setAlignment(Pos.CENTER);
            arrowBox.setPadding(new Insets(0, 0, 0, 110)); // Shift to point to 30

            // Array
            HBox arrayBox = new HBox(15);
            arrayBox.setAlignment(Pos.CENTER);
            
            arrayBox.getChildren().add(createArrayBox("8", "0", "#f59e0b", false));
            arrayBox.getChildren().add(createArrayBox("17", "1", "#f59e0b", false));
            arrayBox.getChildren().add(createArrayBox("23", "2", "#3b82f6", true));   // Current Middle
            arrayBox.getChildren().add(createArrayBox("30", "3", "#22c55e", false));
            arrayBox.getChildren().add(createArrayBox("42", "4", "#ef4444", false));
            
            // Labels below array
            HBox labelsBot = new HBox(20);
            labelsBot.setAlignment(Pos.CENTER);
            
            Label l1 = new Label("Discarded (Too Small)");
            l1.setTextFill(Color.web("#f59e0b"));
            VBox midBox = new VBox(LabelBuilder("Current Middle", "#3b82f6"), LabelBuilder("Index 3", "#3b82f6"));
            midBox.setAlignment(Pos.CENTER);
            Label l2 = new Label("Discarded (Too Large)");
            l2.setTextFill(Color.web("#ef4444"));
            
            labelsBot.getChildren().addAll(l1, midBox, l2);
            
            vizInnerBox.getChildren().addAll(targetBox, arrowBox, arrayBox, labelsBot);
            
        } else {
            // Generic sort structure
            Label comingSoon = new Label("Visualization Structure for " + activeAlgorithm + "\n(Implement Tree Nodes Here)");
            comingSoon.setFont(Font.font("Arial", 16));
            vizInnerBox.getChildren().add(comingSoon);
        }

        canvasArea.getChildren().addAll(ovalBg, vizInnerBox);
        
        Region expander = new Region();
        VBox.setVgrow(expander, Priority.ALWAYS);

        // Action cards
        HBox bottomCards = new HBox(20);
        
        // Current Action Card
        HBox actionCard = new HBox(15);
        actionCard.setPadding(new Insets(20));
        actionCard.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 12; -fx-background-radius: 12;");
        HBox.setHgrow(actionCard, Priority.ALWAYS);
        
        Circle iconBg = new Circle(25, Color.web("#3b82f6"));
        Label icon = new Label("🔍");
        icon.setTextFill(Color.WHITE);
        StackPane iconPane = new StackPane(iconBg, icon);
        
        VBox actionTexts = new VBox(5);
        Label aTitle = new Label("CURRENT ACTION");
        aTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Label aDesc = new Label("Compare 30 with middle value 23.\nSince 30 is greater, we discard the left half\nand continue searching in the right half.");
        aDesc.setWrapText(true);
        aDesc.setTextFill(Color.web("#475569"));
        actionTexts.getChildren().addAll(aTitle, aDesc);
        
        actionCard.getChildren().addAll(iconPane, actionTexts);
        
        // Overview Card
        VBox ovCard = new VBox(10);
        ovCard.setPrefWidth(250);
        ovCard.setPadding(new Insets(20));
        ovCard.setStyle("-fx-background-color: #f8fafc; -fx-border-radius: 12; -fx-background-radius: 12;");
        
        Label ovTitle = new Label(activeAlgorithm.toUpperCase() + " OVERVIEW");
        ovTitle.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        ovTitle.setTextFill(Color.web("#334155"));
        Label ovDesc = new Label("Efficiently finds a target by repeatedly dividing the search interval in half.");
        ovDesc.setWrapText(true);
        ovDesc.setTextFill(Color.web("#475569"));
        Label ovTime = new Label("Time Complexity: O(log n)");
        ovTime.setTextFill(Color.web("#3b82f6"));
        ovTime.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        
        ovCard.getChildren().addAll(ovTitle, ovDesc, ovTime);
        bottomCards.getChildren().addAll(actionCard, ovCard);

        center.getChildren().addAll(header, descBox, canvasArea, expander, bottomCards);
        return center;
    }
    
    private Label LabelBuilder(String txt, String color) {
        Label l = new Label(txt);
        l.setTextFill(Color.web(color));
        return l;
    }

    private VBox createArrayBox(String value, String index, String colorHex, boolean filledBg) {
        VBox container = new VBox(5);
        container.setAlignment(Pos.CENTER);
        
        Label valLabel = new Label(value);
        valLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        valLabel.setTextFill(filledBg ? Color.WHITE : Color.web(colorHex));
        
        StackPane square = new StackPane();
        square.setPrefSize(60, 60);
        if (filledBg) {
            square.setStyle("-fx-background-color: " + colorHex + "; -fx-border-color: " + colorHex + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 2;");
        } else {
            square.setStyle("-fx-background-color: white; -fx-border-color: " + colorHex + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 2;");
        }
        square.getChildren().add(valLabel);
        
        Label idxLabel = new Label(index);
        idxLabel.setTextFill(Color.web("#94a3b8"));
        
        container.getChildren().addAll(square, idxLabel);
        return container;
    }

    private HBox buildFooter() {
        HBox footer = new HBox(40);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(15));
        footer.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 1 0 0 0;");
        
        footer.getChildren().add(footerLink("🏠 Home", false));
        footer.getChildren().add(footerLink("📊 Algorithms", true));
        footer.getChildren().add(footerLink("🏆 Battle Mode", false));
        footer.getChildren().add(footerLink("🕒 History", false));
        footer.getChildren().add(footerLink("⚙ Settings", false));
        
        return footer;
    }
    
    private Label footerLink(String text, boolean active) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", 14));
        if (active) {
            l.setTextFill(Color.web("#3b82f6"));
            l.setStyle("-fx-background-color: #eff6ff; -fx-padding: 8 16; -fx-background-radius: 20;");
        } else {
            l.setTextFill(Color.web("#64748b"));
            l.setStyle("-fx-padding: 8 16;");
        }
        return l;
    }
}