package org.badr.chatbot.model;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductItem {
    private Long id;
    private Long productId;
    private Integer quantity;
    private Double price;
    private Product product;
}