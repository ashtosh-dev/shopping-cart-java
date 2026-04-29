package com.algomart.algorithms.greedy;

import com.algomart.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiscountOptimizer {

    public static class Coupon {
        private String code;
        private String description;
        private double minOrderValue;
        private double discountPercent;
        private double maxDiscountAmount;

        public Coupon(String code, String description, double minOrderValue,
                      double discountPercent, double maxDiscountAmount) {
            this.code = code;
            this.description = description;
            this.minOrderValue = minOrderValue;
            this.discountPercent = discountPercent;
            this.maxDiscountAmount = maxDiscountAmount;
        }

        public String getCode() { return code; }
        public String getDescription() { return description; }
        public double getMinOrderValue() { return minOrderValue; }
        public double getDiscountPercent() { return discountPercent; }
        public double getMaxDiscountAmount() { return maxDiscountAmount; }

        public double calculateDiscount(double orderTotal) {
            if (orderTotal < minOrderValue) return 0;
            double discount = orderTotal * discountPercent / 100;
            return Math.min(discount, maxDiscountAmount);
        }

        @Override
        public String toString() {
            return String.format("%s - %.0f%% off (min order ₹%.0f, max discount ₹%.0f)",
                    code, discountPercent, minOrderValue, maxDiscountAmount);
        }
    }

    // All available coupons in the system
    public static List<Coupon> getAvailableCoupons() {
        List<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon("SAVE10", "10% off on orders above ₹500", 500, 10, 200));
        coupons.add(new Coupon("MEGA20", "20% off on orders above ₹1000", 1000, 20, 500));
        coupons.add(new Coupon("SUPER30", "30% off on orders above ₹2000", 2000, 30, 1000));
        coupons.add(new Coupon("FLASH15", "15% off on orders above ₹800", 800, 15, 300));
        coupons.add(new Coupon("ELITE25", "25% off on orders above ₹5000", 5000, 25, 2000));
        coupons.add(new Coupon("WELCOME5", "5% off on any order", 0, 5, 100));
        return coupons;
    }

    // Greedy algorithm - picks the best applicable coupon
    public static OptimizationResult getBestCoupon(double orderTotal) {
        List<Coupon> coupons = getAvailableCoupons();
        List<Coupon> applicableCoupons = new ArrayList<>();

        // Step 1 - Filter applicable coupons
        for (Coupon c : coupons) {
            if (orderTotal >= c.getMinOrderValue()) {
                applicableCoupons.add(c);
            }
        }

        if (applicableCoupons.isEmpty()) {
            return new OptimizationResult(null, 0, orderTotal, new ArrayList<>());
        }

        // Step 2 - Greedy: sort by discount amount descending, pick the best
        Collections.sort(applicableCoupons, (a, b) -> {
            double discountA = a.calculateDiscount(orderTotal);
            double discountB = b.calculateDiscount(orderTotal);
            return Double.compare(discountB, discountA);
        });

        Coupon bestCoupon = applicableCoupons.get(0);
        double bestDiscount = bestCoupon.calculateDiscount(orderTotal);
        double finalTotal = orderTotal - bestDiscount;

        return new OptimizationResult(bestCoupon, bestDiscount, finalTotal, applicableCoupons);
    }

    public static class OptimizationResult {
        private Coupon bestCoupon;
        private double discountAmount;
        private double finalTotal;
        private List<Coupon> allApplicable;

        public OptimizationResult(Coupon bestCoupon, double discountAmount,
                                  double finalTotal, List<Coupon> allApplicable) {
            this.bestCoupon = bestCoupon;
            this.discountAmount = discountAmount;
            this.finalTotal = finalTotal;
            this.allApplicable = allApplicable;
        }

        public Coupon getBestCoupon() { return bestCoupon; }
        public double getDiscountAmount() { return discountAmount; }
        public double getFinalTotal() { return finalTotal; }
        public List<Coupon> getAllApplicable() { return allApplicable; }
    }
}