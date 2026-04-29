package com.algomart.algorithms.searching;

import com.algomart.Product;
import java.util.ArrayList;
import java.util.List;

public class BinarySearch {

    public static int operationCount = 0;

    // Binary search by name - list must be sorted by name first
    public static Product searchByName(List<Product> sortedProducts, String targetName) {
        operationCount = 0;
        int low = 0;
        int high = sortedProducts.size() - 1;

        while (low <= high) {
            operationCount++;
            int mid = (low + high) / 2;
            String midName = sortedProducts.get(mid).getName().toLowerCase();
            String target = targetName.toLowerCase();

            int comparison = midName.compareTo(target);

            if (comparison == 0) {
                return sortedProducts.get(mid);
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    // Search by price range - list must be sorted by price first
    public static List<Product> searchByPriceRange(List<Product> sortedProducts,
                                                   double minPrice, double maxPrice) {
        operationCount = 0;
        List<Product> result = new ArrayList<>();

        int startIndex = findFirstInRange(sortedProducts, minPrice);

        for (int i = startIndex; i < sortedProducts.size(); i++) {
            operationCount++;
            if (sortedProducts.get(i).getPrice() > maxPrice) break;
            result.add(sortedProducts.get(i));
        }

        return result;
    }

    private static int findFirstInRange(List<Product> products, double minPrice) {
        int low = 0, high = products.size() - 1, result = products.size();

        while (low <= high) {
            operationCount++;
            int mid = (low + high) / 2;
            if (products.get(mid).getPrice() >= minPrice) {
                result = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return result;
    }

    // Partial name search - searches for products containing the keyword
    public static List<Product> searchByKeyword(List<Product> products, String keyword) {
        operationCount = 0;
        List<Product> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Product p : products) {
            operationCount++;
            if (p.getName().toLowerCase().contains(lowerKeyword) ||
                    p.getCategory().toLowerCase().contains(lowerKeyword) ||
                    p.getDescription().toLowerCase().contains(lowerKeyword)) {
                result.add(p);
            }
        }
        return result;
    }

    public static void resetCount() {
        operationCount = 0;
    }
}