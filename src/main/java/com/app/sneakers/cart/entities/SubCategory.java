package com.app.sneakers.cart.entities;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SubCategory {

    SHIRT,
    TSHIRT,
    JEANS,
    PANT,
    SNEAKERS,
    SHOES;

    @JsonCreator
    public static Category fromValue(String value) {
        return Category.valueOf(value.toUpperCase());
    }
}
