# 🛒 AlgoMart — Algorithm-Powered Shopping App

A JavaFX-based online shopping application built as a Design and Analysis of Algorithms (DAA) lab open-ended project. Every core feature is powered by a real DAA algorithm — made visible and interactive.

---

## 👥 Team
- Ashutosh Shenoy
- Kanish K

**College:** St. Joseph Engineering College, Mangaluru  
**Course:** Design and Analysis of Algorithms (22AIM42)  
**Branch:** AI & ML

---

## 🧠 Algorithm Mapping

| Feature | Algorithm | DAA Module |
|---|---|---|
| Sort products by price/rating/name | QuickSort, MergeSort, HeapSort | Module 2 |
| Search for a product | Binary Search | Module 2 |
| Discount & coupon selection | Greedy Optimization | Module 3 |
| Budget-based cart suggestions | 0/1 Knapsack (DP) | Module 4 |
| Wishlist constraint explorer | Backtracking with Pruning | Module 5 |
| Algorithm comparison stats | Complexity Analysis | Module 1 |

---

## ✨ Features

- **Product Grid** — Browse 50 products across 5 categories
- **Smart Search** — Binary Search with keyword matching
- **Algorithm Sorter** — Sort by price, rating, or name using QuickSort, MergeSort, or HeapSort. Shows operation count and time taken
- **Cart with Greedy Discount** — Automatically selects the best coupon from available offers
- **Budget Planner** — Enter a budget, get the optimal product combination using 0/1 Knapsack DP
- **Wishlist Optimizer** — Set constraints, backtracking finds all valid combinations and prunes invalid branches
- **Algorithm Visualizer** — Watch Binary Search, QuickSort, and MergeSort animate step by step on real product price data

---

## 🗂️ Project Structure
```
src/com/algomart/
├── algorithms/
│   ├── sorting/        # QuickSort, MergeSort, HeapSort
│   ├── searching/      # BinarySearch
│   ├── greedy/         # DiscountOptimizer
│   ├── dp/             # KnapsackSolver
│   └── backtracking/   # WishlistOptimizer
├── visualizer/         # SortifyVisualizer (JavaFX animation)
├── Main.java           # Entry point + main UI
├── Product.java        # Product model
├── CartItem.java       # Cart item model
```

---

## 🛠️ Tech Stack

- **Language:** Java 21
- **UI Framework:** JavaFX 21
- **Build Tool:** Maven
- **IDE:** IntelliJ IDEA

---

## 🚀 How to Run

**Using Maven (recommended):**
```bash
mvn javafx:run
```

**Requirements:**
- Java 21+
- Maven 3.6+

---

## 📊 DAA Concepts Covered

**Module 1 — Analysis Framework**
Operation counting and time measurement for every sort operation. Live stats panel shows comparisons made and time taken in milliseconds.

**Module 2 — Divide and Conquer**
QuickSort, MergeSort, HeapSort for product sorting. Binary Search for product lookup.

**Module 3 — Greedy Method**
Discount optimizer greedily selects the highest-value applicable coupon from all available offers.

**Module 4 — Dynamic Programming**
0/1 Knapsack solves the budget planning problem — given a budget, find the combination of products that maximizes total rating value.

**Module 5 — Backtracking**
Wishlist optimizer explores all valid product combinations within user-defined constraints, pruning branches that exceed budget early.

---

## 🎬 Demo Flow

1. **Home** → Sort products, observe algorithm stats
2. **Search** → Type a product name, see Binary Search results
3. **Cart** → Add items, watch Greedy discount apply automatically
4. **Budget Planner** → Enter ₹5000, see Knapsack suggest optimal items
5. **Wishlist** → Set constraints, see backtracking find all valid combos
6. **Visualizer** → Watch Binary Search animate on cart prices
