package org.badr.mcpserver.model;

import lombok.*;
import java.util.Date;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bill {
    private Long id;
    private Date billDate;
    private Long customerId;
    private List<ProductItem> productItems;
    private Customer customer;
}