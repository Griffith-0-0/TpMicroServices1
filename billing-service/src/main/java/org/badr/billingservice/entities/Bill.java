package org.badr.billingservice.entities;


import jakarta.persistence.*;
import lombok.*;
import org.badr.billingservice.model.Customer;

import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bill {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date billDate;
    private long customerId;
    @OneToMany(mappedBy = "bill", fetch = FetchType.EAGER)
    private List<ProductItem> productItems;
    @Transient
    private Customer customer;
}
