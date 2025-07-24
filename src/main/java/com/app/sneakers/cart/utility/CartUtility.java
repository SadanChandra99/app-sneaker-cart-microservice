package com.app.sneakers.cart.utility;

import com.app.sneakers.cart.entities.CartItem;
import com.app.sneakers.cart.entities.Money;

import java.util.List;
import java.util.Optional;

public class CartUtility {

    public static Optional<CartItem> findMatchingItem(List<CartItem> items, CartItem targetItem) {
        return items.stream()
                .filter(existing ->
                        existing.getProductId().equals(targetItem.getProductId()) &&
                                existing.getSize().equals(targetItem.getSize()) &&
                                existing.getColor().equals(targetItem.getColor()))
                .findFirst();
    }

    public static double calculateUnitPrice(CartItem item) {
        if (item.getQuantity() == 0) return 0;
        return item.getTotalPrice().getValue() / item.getQuantity();
    }

    public static void updateItemTotalPrice(CartItem item, int quantity, String currency) {
        double unitPrice = calculateUnitPrice(item);
        double updatedTotal = unitPrice * quantity;
        item.setTotalPrice(new Money(currency, updatedTotal));
        item.setQuantity(quantity);
    }
}
