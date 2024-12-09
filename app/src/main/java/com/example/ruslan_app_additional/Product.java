package com.example.ruslan_app_additional;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private boolean hasDiscount;

    public Product(String name, boolean hasDiscount) {
        this.name = name;
        this.hasDiscount = hasDiscount;
    }

    public String getName() {
        return name;
    }

    public boolean hasDiscount() {
        return hasDiscount;
    }
}
