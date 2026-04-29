package com.algomart.algorithms.dp;

import com.algomart.Product;
import java.util.ArrayList;
import java.util.List;

public class KnapsackSolver {

    public static class KnapsackResult {
        private List<Product> selectedProducts;
        private double totalCost;
        private double totalValue;
        private int[][] dpTable;

        public KnapsackResult(List<Product> selectedProducts, double totalCost,
                              double totalValue, int[][] dpTable) {
            this.selectedProducts = selectedProducts;
            this.totalCost = totalCost;
            this.totalValue = totalValue;
            this.dpTable = dpTable;
        }

        public List<Product> getSelectedProducts() { return selectedProducts; }
        public double getTotalCost() { return totalCost; }
        public double getTotalValue() { return totalValue; }
        public int[][] getDpTable() { return dpTable; }
    }

    // 0/1 Knapsack - given a budget, find the best combination of products
    // Value is based on rating * 10 (normalized)
    public static KnapsackResult solve(List<Product> products, double budget) {
        int n = products.size();
        int W = (int) budget; // budget in rupees

        // Build value and weight arrays
        int[] weights = new int[n];
        int[] values = new int[n];

        for (int i = 0; i < n; i++) {
            weights[i] = (int) products.get(i).getDiscountedPrice();
            values[i] = (int) (products.get(i).getRating() * 10);
        }

        // DP table
        int[][] dp = new int[n + 1][W + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                // Don't take item i
                dp[i][w] = dp[i - 1][w];

                // Take item i if it fits
                if (weights[i - 1] <= w) {
                    int valueWithItem = dp[i - 1][w - weights[i - 1]] + values[i - 1];
                    if (valueWithItem > dp[i][w]) {
                        dp[i][w] = valueWithItem;
                    }
                }
            }
        }

        // Traceback to find which products were selected
        List<Product> selected = new ArrayList<>();
        int w = W;
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selected.add(products.get(i - 1));
                w -= weights[i - 1];
            }
        }

        // Calculate actual totals
        double totalCost = 0;
        double totalValue = 0;
        for (Product p : selected) {
            totalCost += p.getDiscountedPrice();
            totalValue += p.getRating();
        }

        return new KnapsackResult(selected, totalCost, totalValue, dp);
    }
}