package com.algomart.algorithms.backtracking;

import com.algomart.Product;
import java.util.ArrayList;
import java.util.List;

public class WishlistOptimizer {

    public static class Constraints {
        private double maxBudget;
        private int maxItems;
        private double minRating;
        private String category; // "All" for no category filter

        public Constraints(double maxBudget, int maxItems, double minRating, String category) {
            this.maxBudget = maxBudget;
            this.maxItems = maxItems;
            this.minRating = minRating;
            this.category = category;
        }

        public double getMaxBudget() { return maxBudget; }
        public int getMaxItems() { return maxItems; }
        public double getMinRating() { return minRating; }
        public String getCategory() { return category; }
    }

    public static class BacktrackingResult {
        private List<List<Product>> allCombinations;
        private List<Product> bestCombination;
        private int nodesExplored;
        private int nodesPruned;

        public BacktrackingResult(List<List<Product>> allCombinations,
                                  List<Product> bestCombination,
                                  int nodesExplored, int nodesPruned) {
            this.allCombinations = allCombinations;
            this.bestCombination = bestCombination;
            this.nodesExplored = nodesExplored;
            this.nodesPruned = nodesPruned;
        }

        public List<List<Product>> getAllCombinations() { return allCombinations; }
        public List<Product> getBestCombination() { return bestCombination; }
        public int getNodesExplored() { return nodesExplored; }
        public int getNodesPruned() { return nodesPruned; }
    }

    private static int nodesExplored;
    private static int nodesPruned;
    private static List<List<Product>> allCombinations;
    private static List<Product> bestCombination;
    private static double bestRatingSum;

    public static BacktrackingResult findOptimalWishlist(List<Product> products,
                                                         Constraints constraints) {
        nodesExplored = 0;
        nodesPruned = 0;
        allCombinations = new ArrayList<>();
        bestCombination = new ArrayList<>();
        bestRatingSum = 0;

        // Filter by category and min rating first
        List<Product> filtered = new ArrayList<>();
        for (Product p : products) {
            if ((constraints.getCategory().equals("All") ||
                    p.getCategory().equals(constraints.getCategory())) &&
                    p.getRating() >= constraints.getMinRating()) {
                filtered.add(p);
            }
        }

        List<Product> current = new ArrayList<>();
        backtrack(filtered, constraints, current, 0, 0, 0);

        return new BacktrackingResult(allCombinations, bestCombination,
                nodesExplored, nodesPruned);
    }

    private static void backtrack(List<Product> products, Constraints constraints,
                                  List<Product> current, int start,
                                  double currentCost, double currentRating) {
        nodesExplored++;

        // Valid combination found - save it
        if (!current.isEmpty()) {
            allCombinations.add(new ArrayList<>(current));

            // Track best combination by total rating
            if (currentRating > bestRatingSum) {
                bestRatingSum = currentRating;
                bestCombination = new ArrayList<>(current);
            }
        }

        // Stop if max items reached
        if (current.size() >= constraints.getMaxItems()) {
            return;
        }

        for (int i = start; i < products.size(); i++) {
            Product p = products.get(i);
            double itemCost = p.getDiscountedPrice();

            // Pruning - skip if adding this item exceeds budget
            if (currentCost + itemCost > constraints.getMaxBudget()) {
                nodesPruned++;
                continue;
            }

            // Add item and recurse
            current.add(p);
            backtrack(products, constraints, current, i + 1,
                    currentCost + itemCost, currentRating + p.getRating());
            current.remove(current.size() - 1);
        }
    }
}