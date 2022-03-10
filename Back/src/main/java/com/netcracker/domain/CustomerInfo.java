package com.netcracker.domain;

import com.netcracker.domain.enumeration.Gender;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "customer_info")
public class CustomerInfo {
        @Id
        @Column(name = "customer_id")
        private Long customerId;

        @Column(name = "first_name")
        private String firstName;

        @Column(name = "last_name")
        private String lastName;

        @Column(name = "gender")
        @Enumerated(EnumType.STRING)
        private Gender gender;

        @Column(name = "phone_number")
        private String phoneNumber;

        @Column(name = "last_delivery_address")
        private String lastDeliveryAddress;

        @Column(name = "birth_date")
        private Date birthDate;
}

