package com.netcracker.domain;

import com.netcracker.domain.enumeration.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="category")
public class Category {

    private static final String SEQ_NAME = "category_seq";
    @Id
    @Column(name="id")
    private Long id;

    @Column(name="gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name="category_name")
    private String name;


}
