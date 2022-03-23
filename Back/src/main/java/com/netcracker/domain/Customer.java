package com.netcracker.domain;


import lombok.*;

import javax.persistence.*;


@Data
@AllArgsConstructor
//@EqualsAndHashCode(callSuper=false)
@Builder
@Entity
@Table(name = "customer")
@NoArgsConstructor
public class Customer {

    @Id
    @Column(name = "id")
    Long id;

    @Column(name = "email")
    String email;

}
