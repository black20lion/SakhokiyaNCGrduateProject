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
        private Long id;

        @Column(name = "first_name")
        private String firstName;

        @Column(name = "second_name")
        private String secondName;

        @Column(name = "patronymic")
        private String patronymic;

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

