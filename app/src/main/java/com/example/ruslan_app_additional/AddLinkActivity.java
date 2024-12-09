package com.example.ruslan_app_additional;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document; // Correct import


import java.io.IOException;

public class AddLinkActivity extends AppCompatActivity {

    private EditText linkInput;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_link);

        linkInput = findViewById(R.id.link_input);
        saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productUrl = linkInput.getText().toString();
                new FetchProductData().execute(productUrl);
            }
        });
    }

    private class FetchProductData extends AsyncTask<String, Void, Product> {

        @Override
        protected Product doInBackground(String... urls) {
            String productUrl = urls[0];
            try {
                Document doc = Jsoup.connect(productUrl).get();

                // Extract product name
                String productName = doc.select("h1.b-product-info--title").text();

                // Check for discount
                boolean hasDiscount = !doc.select("span.b-product-price-old-number").isEmpty();
                String promotionData = doc.select("div.b-product-info").attr("data-b-promotion");
                 hasDiscount = promotionData != null && !promotionData.isEmpty(); // Add more specific checks if needed
                return new Product(productName, hasDiscount);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Product product) {
            if (product != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("productName", product.getName());
                resultIntent.putExtra("hasDiscount", product.hasDiscount());
                setResult(RESULT_OK, resultIntent);
                finish(); // Close AddLinkActivity
            }
        }
    }
}