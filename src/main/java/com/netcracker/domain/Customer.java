package com.netcracker.domain;

import com.netcracker.domain.enumeration.Gender;
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
    String eMail;

    @Column(name = "password")
    String password;
}
