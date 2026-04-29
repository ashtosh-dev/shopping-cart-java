package com.algomart.algorithms.sorting;

import com.algomart.Product;
import java.util.List;

public class HeapSort {

    public static int operationCount = 0;

    // Sort by Price
    public static void sortByPrice(List<Product> products) {
        int n = products.size();

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyByPrice(products, n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            swap(products, 0, i);
            heapifyByPrice(products, i, 0);
        }
    }

    private static void heapifyByPrice(List<Product> products, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        operationCount++;
        if (left < n && products.get(left).getPrice() > products.get(largest).getPrice()) {
            largest = left;
        }

        operationCount++;
        if (right < n && products.get(right).getPrice() > products.get(largest).getPrice()) {
            largest = right;
        }

        if (largest != i) {
            swap(products, i, largest);
            heapifyByPrice(products, n, largest);
        }
    }

    // Sort by Rating
    public static void sortByRating(List<Product> products) {
        int n = products.size();

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyByRating(products, n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            swap(products, 0, i);
            heapifyByRating(products, i, 0);
        }
    }

    private static void heapifyByRating(List<Product> products, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        operationCount++;
        if (left < n && products.get(left).getRating() > products.get(largest).getRating()) {
            largest = left;
        }

        operationCount++;
        if (right < n && products.get(right).getRating() > products.get(largest).getRating()) {
            largest = right;
        }

        if (largest != i) {
            swap(products, i, largest);
            heapifyByRating(products, n, largest);
        }
    }

    // Sort by Name
    public static void sortByName(List<Product> products) {
        int n = products.size();

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyByName(products, n, i);
        }

        for (int i = n - 1; i > 0; i--) {
            swap(products, 0, i);
            heapifyByName(products, i, 0);
        }
    }

    private static void heapifyByName(List<Product> products, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        operationCount++;
        if (left < n && products.get(left).getName().compareTo(products.get(largest).getName()) > 0) {
            largest = left;
        }

        operationCount++;
        if (right < n && products.get(right).getName().compareTo(products.get(largest).getName()) > 0) {
            largest = right;
        }

        if (largest != i) {
            swap(products, i, largest);
            heapifyByName(products, n, largest);
        }
    }

    private static void swap(List<Product> products, int i, int j) {
        Product temp = products.get(i);
        products.set(i, products.get(j));
        products.set(j, temp);
    }

    public static void resetCount() {
        operationCount = 0;
    }
}