package com.app.sneakers.cart.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    @JsonIgnore  // avoid infinite loop in JSON
    private CartEntity cart;
    @Column(nullable = false)
    private String productId;
    private String productSku;
    private String productName;
    private String color;
    private String size;
    private int quantity;

    @JdbcTypeCode(SqlTypes.JSON)
    private Money unitPrice;

    @JdbcTypeCode(SqlTypes.JSON)
    private Money totalPrice;

    private boolean isAvailable;
}