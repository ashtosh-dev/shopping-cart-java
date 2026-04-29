//package com.algomart;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Label;
//import javafx.scene.layout.StackPane;
//import javafx.stage.Stage;
//
//public class Main extends Application {
//
//    @Override
//    public void start(Stage stage) {
//        Label label = new Label("AlgoMart is starting...");
//        StackPane root = new StackPane(label);
//        Scene scene = new Scene(root, 800, 600);
//        stage.setTitle("AlgoMart");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

package com.algomart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Test ProductDatabase
        List<Product> products = ProductDatabase.getAllProducts();
        System.out.println("Total products loaded: " + products.size());
        System.out.println("-----------------------------");
        for (Product p : products) {
            System.out.println(p);
        }
        System.out.println("-----------------------------");
        System.out.println("Categories: " + ProductDatabase.getCategories());

        // Keep window open
        Label label = new Label("Check console for product output");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("AlgoMart - DB Test");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}