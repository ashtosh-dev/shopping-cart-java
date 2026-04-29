package com.algomart.algorithms.sorting;

import com.algomart.Product;
import java.util.List;

public class QuickSort {

    public static int operationCount = 0;

    public static void sortByPrice(List<Product> products, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(products, low, high);
            sortByPrice(products, low, pivotIndex - 1);
            sortByPrice(products, pivotIndex + 1, high);
        }
    }

    private static int partition(List<Product> products, int low, int high) {
        double pivot = products.get(high).getPrice();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            operationCount++;
            if (products.get(j).getPrice() <= pivot) {
                i++;
                swap(products, i, j);
            }
        }
        swap(products, i + 1, high);
        return i + 1;
    }

    public static void sortByRating(List<Product> products, int low, int high) {
        if (low < high) {
            int pivotIndex = partitionByRating(products, low, high);
            sortByRating(products, low, pivotIndex - 1);
            sortByRating(products, pivotIndex + 1, high);
        }
    }

    private static int partitionByRating(List<Product> products, int low, int high) {
        double pivot = products.get(high).getRating();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            operationCount++;
            if (products.get(j).getRating() >= pivot) {
                i++;
                swap(products, i, j);
            }
        }
        swap(products, i + 1, high);
        return i + 1;
    }

    public static void sortByName(List<Product> products, int low, int high) {
        if (low < high) {
            int pivotIndex = partitionByName(products, low, high);
            sortByName(products, low, pivotIndex - 1);
            sortByName(products, pivotIndex + 1, high);
        }
    }

    private static int partitionByName(List<Product> products, int low, int high) {
        String pivot = products.get(high).getName();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            operationCount++;
            if (products.get(j).getName().compareTo(pivot) <= 0) {
                i++;
                swap(products, i, j);
            }
        }
        swap(products, i + 1, high);
        return i + 1;
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