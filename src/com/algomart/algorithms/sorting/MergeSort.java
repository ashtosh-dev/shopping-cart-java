package com.algomart.algorithms.sorting;

import com.algomart.Product;
import java.util.ArrayList;
import java.util.List;

public class MergeSort {

    public static int operationCount = 0;

    public static void sortByPrice(List<Product> products, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            sortByPrice(products, left, mid);
            sortByPrice(products, mid + 1, right);
            mergeByPrice(products, left, mid, right);
        }
    }

    private static void mergeByPrice(List<Product> products, int left, int mid, int right) {
        List<Product> leftList = new ArrayList<>(products.subList(left, mid + 1));
        List<Product> rightList = new ArrayList<>(products.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;

        while (i < leftList.size() && j < rightList.size()) {
            operationCount++;
            if (leftList.get(i).getPrice() <= rightList.get(j).getPrice()) {
                products.set(k++, leftList.get(i++));
            } else {
                products.set(k++, rightList.get(j++));
            }
        }

        while (i < leftList.size()) {
            products.set(k++, leftList.get(i++));
        }

        while (j < rightList.size()) {
            products.set(k++, rightList.get(j++));
        }
    }

    public static void sortByRating(List<Product> products, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            sortByRating(products, left, mid);
            sortByRating(products, mid + 1, right);
            mergeByRating(products, left, mid, right);
        }
    }

    private static void mergeByRating(List<Product> products, int left, int mid, int right) {
        List<Product> leftList = new ArrayList<>(products.subList(left, mid + 1));
        List<Product> rightList = new ArrayList<>(products.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;

        while (i < leftList.size() && j < rightList.size()) {
            operationCount++;
            if (leftList.get(i).getRating() >= rightList.get(j).getRating()) {
                products.set(k++, leftList.get(i++));
            } else {
                products.set(k++, rightList.get(j++));
            }
        }

        while (i < leftList.size()) {
            products.set(k++, leftList.get(i++));
        }

        while (j < rightList.size()) {
            products.set(k++, rightList.get(j++));
        }
    }

    public static void sortByName(List<Product> products, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            sortByName(products, left, mid);
            sortByName(products, mid + 1, right);
            mergeByName(products, left, mid, right);
        }
    }

    private static void mergeByName(List<Product> products, int left, int mid, int right) {
        List<Product> leftList = new ArrayList<>(products.subList(left, mid + 1));
        List<Product> rightList = new ArrayList<>(products.subList(mid + 1, right + 1));

        int i = 0, j = 0, k = left;

        while (i < leftList.size() && j < rightList.size()) {
            operationCount++;
            if (leftList.get(i).getName().compareTo(rightList.get(j).getName()) <= 0) {
                products.set(k++, leftList.get(i++));
            } else {
                products.set(k++, rightList.get(j++));
            }
        }

        while (i < leftList.size()) {
            products.set(k++, leftList.get(i++));
        }

        while (j < rightList.size()) {
            products.set(k++, rightList.get(j++));
        }
    }

    public static void resetCount() {
        operationCount = 0;
    }
}