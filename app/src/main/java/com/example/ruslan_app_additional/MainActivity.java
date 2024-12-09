package com.example.ruslan_app_additional;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView productListRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        productListRecyclerView = findViewById(R.id.product_list);

        // Load the product list before initializing the adapter
        loadProductList();

        productAdapter = new ProductAdapter(products);
        productListRecyclerView.setAdapter(productAdapter);
        productListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button addLinkButton = findViewById(R.id.add_link_button);
        addLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddLinkActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String productName = data.getStringExtra("productName");
            boolean hasDiscount = data.getBooleanExtra("hasDiscount", false);
            Product newProduct = new Product(productName, hasDiscount);
            products.add(newProduct);
            productAdapter.notifyItemInserted(products.size() - 1);
            saveProductList(); // Save after adding a new product
        }
    }

    private void saveProductList() {
        try {
            FileOutputStream fos = openFileOutput("product_list.dat", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(products);
            oos.close();
            fos.close();
            Log.d("MainActivity", "Product list saved successfully");
        } catch (IOException e) {
            Log.e("MainActivity", "Error saving product list: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadProductList() {
        try {
            FileInputStream fis = openFileInput("product_list.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            products = (List<Product>) ois.readObject(); // Load the list into `products`
            ois.close();
            fis.close();
            Log.d("MainActivity", "Product list loaded successfully");
        } catch (IOException | ClassNotFoundException e) {
            Log.e("MainActivity", "Error loading product list: " + e.getMessage());
            e.printStackTrace();
            // Initialize an empty list if loading fails
            products = new ArrayList<>();
        }

        // Update the adapter
        if (productAdapter != null) {
            productAdapter.notifyDataSetChanged();
        }
    }

}