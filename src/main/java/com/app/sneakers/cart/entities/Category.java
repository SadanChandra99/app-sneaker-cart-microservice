package com.app.sneakers.cart.entities;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Category {
    CLOTHING,
    FOOTWEAR,
    TOPWEAR,
    BOTTOMWEAR;

    @JsonCreator
    public static Category fromValue(String value) {
        return Category.valueOf(value.toUpperCase());
    }
}
