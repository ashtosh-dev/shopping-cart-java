package com.algomart.visualizer;

import com.algomart.CartItem;
import com.algomart.Product;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class SortifyVisualizer {

    private BorderPane mainLayout;
    private Runnable onExit;
    private List<Product> products;
    private List<CartItem> cart;
    private String activeAlgorithm = "Binary Search";

    private List<int[]> steps = new ArrayList<>();
    private List<String> stepDescriptions = new ArrayList<>();
    private int currentStep = 0;
    private Timeline timeline;

    private HBox arrayBox;
    private Label currentActionLabel;
    private Label stepInfoLabel;
    private ProgressBar progressBar;
    private VBox stepsListBox;
    private Label statusLabel;
    private Label comparisonsLabel;
    private Label stepsCountLabel;
    private Slider speedSlider;

    private int[] prices;
    private int searchTarget;

    private static final String COLOR_DEFAULT = "#3b82f6";
    private static final String COLOR_TARGET = "#22c55e";
    private static final String COLOR_DISCARDED_SMALL = "#f59e0b";
    private static final String COLOR_DISCARDED_LARGE = "#ef4444";
    private static final String COLOR_PIVOT = "#a855f7";
    private static final String COLOR_SORTED = "#22c55e";
    private static final String COLOR_COMPARING = "#f59e0b";

    public SortifyVisualizer(List<Product> products, List<CartItem> cart, Runnable onExit) {
        this.products = products;
        this.cart = cart;
        this.onExit = onExit;

// Pick 6 products spread across categories for better price variety
// Use cart items if available, otherwise fall back to spread products
        if (!cart.isEmpty()) {
            int size = Math.min(6, cart.size());
            prices = new int[size];
            for (int i = 0; i < size; i++) {
                prices[i] = (int) cart.get(i).getProduct().getDiscountedPrice();
            }
        } else {
            // No cart items — pick spread products as demo
            int[] indices = {1, 3, 11, 31, 41, 9};
            prices = new int[indices.length];
            for (int i = 0; i < indices.length; i++) {
                prices[i] = (int) products.get(indices[i]).getDiscountedPrice();
            }
        }

        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f7f9fc;");

        mainLayout.setTop(buildTopBar());
        mainLayout.setLeft(buildLeftSidebar());
        mainLayout.setRight(buildRightSidebar());
        mainLayout.setCenter(buildCenterMain());
        mainLayout.setBottom(buildFooter());

        initAlgorithm();
    }

    public BorderPane getView() {
        return mainLayout;
    }

    private void initAlgorithm() {
        steps.clear();
        stepDescriptions.clear();
        currentStep = 0;
        if (timeline != null) timeline.stop();

        if (activeAlgorithm.equals("Binary Search")) {
            initBinarySearch();
        } else if (activeAlgorithm.equals("Quick Sort")) {
            initQuickSort();
        } else if (activeAlgorithm.equals("Merge Sort")) {
            initMergeSort();
        }

        updateVisualization();
    }

    private void initBinarySearch() {
        int[] sorted = prices.clone();
        java.util.Arrays.sort(sorted);
        searchTarget = sorted[sorted.length / 2];

        int low = 0, high = sorted.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            steps.add(new int[]{low, high, mid, sorted[mid], sorted.length});

            if (sorted[mid] == searchTarget) {
                stepDescriptions.add("Found ₹" + searchTarget + " at index " + mid + "! Search complete.");
                break;
            } else if (sorted[mid] < searchTarget) {
                stepDescriptions.add("₹" + sorted[mid] + " < ₹" + searchTarget + ". Target is in right half. Discard left.");
                low = mid + 1;
            } else {
                stepDescriptions.add("₹" + sorted[mid] + " > ₹" + searchTarget + ". Target is in left half. Discard right.");
                high = mid - 1;
            }
        }
        prices = sorted;
    }

    private void initQuickSort() {
        int[] arr = prices.clone();
        quickSortWithSteps(arr, 0, arr.length - 1);
        prices = arr;
    }

    private void quickSortWithSteps(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIdx = partitionWithSteps(arr, low, high);
            quickSortWithSteps(arr, low, pivotIdx - 1);
            quickSortWithSteps(arr, pivotIdx + 1, high);
        }
    }

    private int partitionWithSteps(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            steps.add(new int[]{j, high, i, pivot, low});
            stepDescriptions.add("Comparing ₹" + arr[j] + " with pivot ₹" + pivot +
                    (arr[j] <= pivot ? ". Swap!" : ". No swap."));

            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        steps.add(arr.clone());
        stepDescriptions.add("Pivot ₹" + pivot + " placed at correct position " + (i + 1));
        return i + 1;
    }

    private void initMergeSort() {
        int[] arr = prices.clone();
        mergeSortWithSteps(arr, 0, arr.length - 1);
        prices = arr;
    }

    private void mergeSortWithSteps(int[] arr, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            stepDescriptions.add("Dividing: left=" + left + " mid=" + mid + " right=" + right);
            steps.add(new int[]{left, mid, right, -1, -1});
            mergeSortWithSteps(arr, left, mid);
            mergeSortWithSteps(arr, mid + 1, right);
            mergeWithSteps(arr, left, mid, right);
        }
    }

    private void mergeWithSteps(int[] arr, int left, int mid, int right) {
        int[] leftArr = java.util.Arrays.copyOfRange(arr, left, mid + 1);
        int[] rightArr = java.util.Arrays.copyOfRange(arr, mid + 1, right + 1);

        int i = 0, j = 0, k = left;
        while (i < leftArr.length && j < rightArr.length) {
            stepDescriptions.add("Merging: comparing ₹" + leftArr[i] + " and ₹" + rightArr[j]);
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
            }
            steps.add(new int[]{left, mid, right, k - 1, arr[k - 1]});
        }
        while (i < leftArr.length) {
            arr[k++] = leftArr[i++];
            steps.add(new int[]{left, mid, right, k - 1, arr[k - 1]});
            stepDescriptions.add("Copying left element ₹" + arr[k - 1]);
        }
        while (j < rightArr.length) {
            arr[k++] = rightArr[j++];
            steps.add(new int[]{left, mid, right, k - 1, arr[k - 1]});
            stepDescriptions.add("Copying right element ₹" + arr[k - 1]);
        }
    }

    private HBox buildTopBar() {
        HBox topBar = new HBox(30);
        topBar.setPadding(new Insets(15, 30, 15, 30));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");

        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        Label logoIcon = new Label("📊");
        logoIcon.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        VBox logoText = new VBox();
        Label sortify = new Label("AlgoMart Visualizer");
        sortify.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label subTitle = new Label("Watch algorithms work on real product data");
        subTitle.setFont(Font.font("Arial", 10));
        subTitle.setTextFill(Color.GRAY);
        logoText.getChildren().addAll(sortify, subTitle);
        logoBox.getChildren().addAll(logoIcon, logoText);

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        HBox nav = new HBox(10);
        nav.setAlignment(Pos.CENTER);
        String[] algos = {"Binary Search", "Quick Sort", "Merge Sort"};
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
                mainLayout.setRight(buildRightSidebar());
                initAlgorithm();
            });
            nav.getChildren().add(btn);
        }

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        Button exitBtn = new Button("← Exit Visualizer");
        exitBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-border-radius: 20; -fx-background-radius: 20; -fx-padding: 8 16; -fx-cursor: hand;");
        exitBtn.setOnAction(e -> {
            if (timeline != null) timeline.stop();
            onExit.run();
        });

        topBar.getChildren().addAll(logoBox, spacer1, nav, spacer2, exitBtn);
        return topBar;
    }
    private VBox buildCartItemsBox() {
        VBox cartItems = new VBox(10);
        if (cart.isEmpty()) {
            Label empty = new Label("Cart is empty");
            empty.setTextFill(Color.GRAY);
            empty.setFont(Font.font(11));
            cartItems.getChildren().add(empty);
        } else {
            for (CartItem item : cart) {
                HBox row = new HBox(10);
                row.setAlignment(Pos.CENTER_LEFT);
                Label name = new Label(item.getProduct().getName());
                name.setFont(Font.font("Arial", FontWeight.BOLD, 11));
                name.setMaxWidth(140);
                Region sp = new Region();
                HBox.setHgrow(sp, Priority.ALWAYS);
                Label qty = new Label("x" + item.getQuantity());
                qty.setTextFill(Color.web("#f59e0b"));
                qty.setFont(Font.font("Arial", FontWeight.BOLD, 13));
                row.getChildren().addAll(name, sp, qty);
                cartItems.getChildren().add(row);
            }
        }
        return cartItems;
    }


    private VBox buildLeftSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPrefWidth(240);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 0 1 0 0;");

        Label cartTitle = new Label("🛒 CART ITEMS");
        cartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        VBox cartItems = buildCartItemsBox();

        Separator sep = new Separator();

        Label controlsTitle = new Label("CONTROLS");
        controlsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        Button playBtn = new Button("▶ Play / Pause");
        playBtn.setMaxWidth(Double.MAX_VALUE);
        playBtn.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10; -fx-background-radius: 8; -fx-cursor: hand;");

        Button nextBtn = new Button("⏭ Next Step");
        nextBtn.setMaxWidth(Double.MAX_VALUE);
        nextBtn.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-padding: 10; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;");

        Button prevBtn = new Button("⏮ Previous Step");
        prevBtn.setMaxWidth(Double.MAX_VALUE);
        prevBtn.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-padding: 10; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;");

        Button resetBtn = new Button("🔄 Reset");
        resetBtn.setMaxWidth(Double.MAX_VALUE);
        resetBtn.setStyle("-fx-background-color: #fef2f2; -fx-text-fill: #ef4444; -fx-border-color: #fee2e2; -fx-padding: 10; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;");

        Label speedTitle = new Label("SPEED");
        speedTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        speedSlider = new Slider(1, 10, 5);
        HBox speedBox = new HBox(8);
        Label slow = new Label("Slow");
        slow.setStyle("-fx-font-size: 11; -fx-text-fill: #64748b;");
        Label fast = new Label("Fast");
        fast.setStyle("-fx-font-size: 11; -fx-text-fill: #64748b;");
        HBox.setHgrow(speedSlider, Priority.ALWAYS);
        speedBox.getChildren().addAll(slow, speedSlider, fast);

        playBtn.setOnAction(e -> {
            if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
                timeline.pause();
                playBtn.setText("▶ Play");
            } else {
                playBtn.setText("⏸ Pause");
                startAnimation();
            }
        });

        nextBtn.setOnAction(e -> {
            if (timeline != null) timeline.pause();
            playBtn.setText("▶ Play");
            if (currentStep < steps.size() - 1) {
                currentStep++;
                updateVisualization();
            }
        });

        prevBtn.setOnAction(e -> {
            if (timeline != null) timeline.pause();
            playBtn.setText("▶ Play");
            if (currentStep > 0) {
                currentStep--;
                updateVisualization();
            }
        });

        resetBtn.setOnAction(e -> {
            if (timeline != null) timeline.stop();
            playBtn.setText("▶ Play");
            currentStep = 0;
            initAlgorithm();
        });

        sidebar.getChildren().addAll(
                cartTitle, cartItems, sep,
                controlsTitle, playBtn, nextBtn, prevBtn, resetBtn,
                speedTitle, speedBox
        );
        return sidebar;
    }

    private VBox buildRightSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPrefWidth(260);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 0 1;");

        Label stepsTitle = new Label("STEP BY STEP");
        stepsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        stepsListBox = new VBox(8);
        updateStepsList();

        Separator sep = new Separator();

        Label statsTitle = new Label("STATISTICS");
        statsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        comparisonsLabel = new Label("0");
        stepsCountLabel = new Label("0 / " + steps.size());

        VBox statsBox = new VBox(8);
        statsBox.getChildren().addAll(
                buildStatRow("Comparisons", comparisonsLabel),
                buildStatRow("Steps", stepsCountLabel),
                buildStatRow("Total Steps", new Label(String.valueOf(steps.size())))
        );

        Separator sep2 = new Separator();

        Label legendTitle = new Label("LEGEND");
        legendTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        VBox legendBox = new VBox(8);
        if (activeAlgorithm.equals("Binary Search")) {
            legendBox.getChildren().addAll(
                    buildLegend(COLOR_DEFAULT, "Current Middle"),
                    buildLegend(COLOR_TARGET, "Target Found"),
                    buildLegend(COLOR_DISCARDED_SMALL, "Discarded (Too Small)"),
                    buildLegend(COLOR_DISCARDED_LARGE, "Discarded (Too Large)")
            );
        } else if (activeAlgorithm.equals("Quick Sort")) {
            legendBox.getChildren().addAll(
                    buildLegend(COLOR_PIVOT, "Pivot Element"),
                    buildLegend(COLOR_COMPARING, "Comparing"),
                    buildLegend(COLOR_SORTED, "Sorted Position"),
                    buildLegend(COLOR_DEFAULT, "Unsorted")
            );
        } else {
            legendBox.getChildren().addAll(
                    buildLegend(COLOR_DEFAULT, "Left Subarray"),
                    buildLegend(COLOR_COMPARING, "Right Subarray"),
                    buildLegend(COLOR_SORTED, "Merged / Sorted"),
                    buildLegend("#94a3b8", "Unsorted")
            );
        }

        sidebar.getChildren().addAll(stepsTitle, stepsListBox, sep, statsTitle, statsBox, sep2, legendTitle, legendBox);
        return sidebar;
    }

    private VBox buildCenterMain() {
        VBox center = new VBox(20);
        center.setPadding(new Insets(25, 40, 25, 40));

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label algoLabel = new Label("ALGORITHM: " + activeAlgorithm.toUpperCase());
        algoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        Region s1 = new Region();
        HBox.setHgrow(s1, Priority.ALWAYS);

        stepInfoLabel = new Label("STEP 0 / " + steps.size());
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(120);
        progressBar.setStyle("-fx-accent: #3b82f6;");

        HBox progressBox = new HBox(10, stepInfoLabel, progressBar);
        progressBox.setAlignment(Pos.CENTER);

        Region s2 = new Region();
        HBox.setHgrow(s2, Priority.ALWAYS);

        statusLabel = new Label("STATUS: READY");
        statusLabel.setTextFill(Color.web("#3b82f6"));
        statusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        header.getChildren().addAll(algoLabel, s1, progressBox, s2, statusLabel);

        String algoInfo = activeAlgorithm.equals("Binary Search")
                ? "Searching for ₹" + searchTarget + " in " + prices.length + " sorted product prices"
                : "Sorting " + prices.length + " product prices using " + activeAlgorithm;

        Label desc = new Label(algoInfo);
        desc.setFont(Font.font("Arial", 14));
        desc.setTextFill(Color.web("#475569"));

        // Canvas area - plain VBox with CSS background, no Rectangle node
        VBox canvasArea = new VBox();
        canvasArea.setMinHeight(280);
        canvasArea.setAlignment(Pos.CENTER);
        canvasArea.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #e2e8f0;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-width: 1.5;"
        );

        arrayBox = new HBox(6);
        arrayBox.setAlignment(Pos.CENTER);
        arrayBox.setPadding(new Insets(30));

        canvasArea.getChildren().add(arrayBox);
        VBox.setVgrow(canvasArea, Priority.ALWAYS);

        HBox actionCard = new HBox(15);
        actionCard.setPadding(new Insets(15));
        actionCard.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-radius: 12; -fx-background-radius: 12;");

        Label actionIcon = new Label("⚡");
        actionIcon.setFont(Font.font(20));

        VBox actionTexts = new VBox(4);
        Label aTitle = new Label("CURRENT ACTION");
        aTitle.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        aTitle.setTextFill(Color.web("#334155"));

        currentActionLabel = new Label("Press Play or Next Step to begin");
        currentActionLabel.setWrapText(true);
        currentActionLabel.setTextFill(Color.web("#475569"));
        currentActionLabel.setFont(Font.font(13));
        actionTexts.getChildren().addAll(aTitle, currentActionLabel);

        HBox.setHgrow(actionTexts, Priority.ALWAYS);
        actionCard.getChildren().addAll(actionIcon, actionTexts);

        center.getChildren().addAll(header, desc, canvasArea, actionCard);
        return center;
    }

    private void startAnimation() {
        double speedValue = speedSlider != null ? speedSlider.getValue() : 5;
        double delay = 1100 - (speedValue * 100);

        timeline = new Timeline(new KeyFrame(Duration.millis(delay), e -> {
            if (currentStep < steps.size() - 1) {
                currentStep++;
                updateVisualization();
            } else {
                timeline.stop();
                statusLabel.setText("STATUS: COMPLETE ✓");
                statusLabel.setTextFill(Color.web("#22c55e"));
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateVisualization() {
        if (steps.isEmpty()) return;

        arrayBox.getChildren().clear();

        int[] step = steps.get(currentStep);

        if (activeAlgorithm.equals("Binary Search")) {
            renderBinarySearchStep(step);
        } else if (activeAlgorithm.equals("Quick Sort")) {
            renderQuickSortStep(step);
        } else {
            renderMergeSortStep(step);
        }

        stepInfoLabel.setText("STEP " + (currentStep + 1) + " / " + steps.size());
        progressBar.setProgress((double) (currentStep + 1) / steps.size());
        stepsCountLabel.setText((currentStep + 1) + " / " + steps.size());
        comparisonsLabel.setText(String.valueOf(currentStep + 1));

        if (currentStep < stepDescriptions.size()) {
            currentActionLabel.setText(stepDescriptions.get(currentStep));
        }

        statusLabel.setText(currentStep == steps.size() - 1 ? "STATUS: COMPLETE ✓" : "STATUS: RUNNING...");
        statusLabel.setTextFill(currentStep == steps.size() - 1 ?
                Color.web("#22c55e") : Color.web("#3b82f6"));

        updateStepsList();
    }

    private void renderBinarySearchStep(int[] step) {
        int low = step[0], high = step[1], mid = step[2];

        for (int i = 0; i < prices.length; i++) {
            String color;
            boolean filled = false;

            if (i == mid) {
                color = prices[i] == searchTarget ? COLOR_TARGET : COLOR_DEFAULT;
                filled = true;
            } else if (i < low || i > high) {
                color = i < low ? COLOR_DISCARDED_SMALL : COLOR_DISCARDED_LARGE;
            } else {
                color = "#94a3b8";
            }

            arrayBox.getChildren().add(createBox(String.valueOf(prices[i]), String.valueOf(i), color, filled));
        }

        VBox targetInfo = new VBox(4);
        targetInfo.setAlignment(Pos.CENTER);
        targetInfo.setPadding(new Insets(0, 0, 0, 8));
        Label tLabel = new Label("TARGET");
        tLabel.setTextFill(Color.web(COLOR_TARGET));
        tLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        Label tVal = new Label("₹" + searchTarget);
        tVal.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        tVal.setTextFill(Color.web(COLOR_TARGET));
        tVal.setStyle("-fx-border-color: " + COLOR_TARGET + "; -fx-border-style: dashed; -fx-border-radius: 6; -fx-padding: 6 12;");
        targetInfo.getChildren().addAll(tLabel, tVal);
        arrayBox.getChildren().add(targetInfo);
    }

    private void renderQuickSortStep(int[] step) {
        int comparing = step[0], pivotIdx = step[1];

        for (int i = 0; i < prices.length; i++) {
            String color;
            boolean filled = false;

            if (i == pivotIdx) {
                color = COLOR_PIVOT;
                filled = true;
            } else if (i == comparing) {
                color = COLOR_COMPARING;
                filled = true;
            } else {
                color = COLOR_DEFAULT;
            }

            arrayBox.getChildren().add(createBox("₹" + prices[i], String.valueOf(i), color, filled));
        }
    }

    private void renderMergeSortStep(int[] step) {
        int left = step[0], mid = step[1], right = step[2], lastPlaced = step[3];

        for (int i = 0; i < prices.length; i++) {
            String color;
            boolean filled = false;

            if (i == lastPlaced) {
                color = COLOR_SORTED;
                filled = true;
            } else if (i >= left && i <= mid) {
                color = COLOR_DEFAULT;
            } else if (i > mid && i <= right) {
                color = COLOR_COMPARING;
            } else {
                color = "#94a3b8";
            }

            arrayBox.getChildren().add(createBox("₹" + prices[i], String.valueOf(i), color, filled));
        }
    }

    private VBox createBox(String value, String index, String colorHex, boolean filled) {
        VBox container = new VBox(5);
        container.setAlignment(Pos.CENTER);

        Label valLabel = new Label(value);
        valLabel.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        valLabel.setMaxWidth(60);
        valLabel.setTextFill(filled ? Color.WHITE : Color.web(colorHex));

        StackPane square = new StackPane();
        square.setPrefSize(65, 65);
        square.setMinSize(55, 55);
        square.setMaxSize(70, 70);
        if (filled) {
            square.setStyle("-fx-background-color: " + colorHex + "; -fx-border-color: " + colorHex + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 2;");
        } else {
            square.setStyle("-fx-background-color: white; -fx-border-color: " + colorHex + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 2;");
        }
        square.getChildren().add(valLabel);

        Label idxLabel = new Label(index);
        idxLabel.setTextFill(Color.web("#94a3b8"));
        idxLabel.setFont(Font.font(11));

        container.getChildren().addAll(square, idxLabel);
        return container;
    }

    private void updateStepsList() {
        if (stepsListBox == null) return;
        stepsListBox.getChildren().clear();
        int show = Math.min(7, stepDescriptions.size());
        for (int i = 0; i < show; i++) {
            boolean isCurrent = (i == currentStep % show);
            Label l = new Label((isCurrent ? "▶ " : "   ") + (i + 1) + ". " +
                    (i < stepDescriptions.size() ? stepDescriptions.get(i) : ""));
            l.setFont(Font.font("Arial", isCurrent ? FontWeight.BOLD : FontWeight.NORMAL, 11));
            l.setTextFill(isCurrent ? Color.web("#3b82f6") : Color.web("#475569"));
            l.setWrapText(true);
            stepsListBox.getChildren().add(l);
        }
    }

    private HBox buildStatRow(String label, Label valueLabel) {
        HBox row = new HBox();
        Label l = new Label(label);
        l.setTextFill(Color.web("#475569"));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        row.getChildren().addAll(l, spacer, valueLabel);
        return row;
    }

    private HBox buildLegend(String colorHex, String text) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        Rectangle colorBox = new Rectangle(10, 10, Color.web(colorHex));
        colorBox.setArcWidth(4);
        colorBox.setArcHeight(4);
        Label l = new Label(text);
        l.setTextFill(Color.web("#475569"));
        l.setFont(Font.font(12));
        row.getChildren().addAll(colorBox, l);
        return row;
    }

    private HBox buildFooter() {
        HBox footer = new HBox(40);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(12));
        footer.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 1 0 0 0;");

        String[] tabs = {"🏠 Home", "📊 Algorithms", "🏆 Battle Mode", "🕒 History", "⚙ Settings"};
        for (String tab : tabs) {
            Label l = new Label(tab);
            l.setFont(Font.font("Arial", 13));
            boolean active = tab.contains("Algorithms");
            if (active) {
                l.setTextFill(Color.web("#3b82f6"));
                l.setStyle("-fx-background-color: #eff6ff; -fx-padding: 8 16; -fx-background-radius: 20;");
            } else {
                l.setTextFill(Color.web("#64748b"));
                l.setStyle("-fx-padding: 8 16;");
            }
            footer.getChildren().add(l);
        }
        return footer;
    }
}