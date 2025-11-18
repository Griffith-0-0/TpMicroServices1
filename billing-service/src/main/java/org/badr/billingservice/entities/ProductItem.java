package org.badr.billingservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.badr.billingservice.model.Product;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductItem {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private long productId;
    private int quantity;
    private double price;
    @ManyToOne
    private Bill bill;
    @Transient
    private Product product;
}
