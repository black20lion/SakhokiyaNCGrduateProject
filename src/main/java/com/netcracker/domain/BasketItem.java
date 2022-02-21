package com.netcracker.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="basket_item")
public class BasketItem {

    private static final String SEQ_NAME = "basket_item_seq";

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name="customer_id")
    private Long customerId;

    @Column(name = "offer_id")
    private Long offerId;

    @Column(name = "quantity")
    private Long quantity;
}
