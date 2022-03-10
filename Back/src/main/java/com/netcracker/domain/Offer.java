package com.netcracker.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="offer")
public class Offer {

    private static final String SEQ_NAME = "offer_seq";

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "price_override")
    private BigDecimal price_override;

    @Column(name = "start")
    private Date start;

    @Column(name = "expire")
    private Date expire;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "sale")
    private boolean sale;

    @Column(name = "novelty")
    private boolean novelty;

    @Column(name = "priority")
    private BigDecimal priority;
}
