package com.netcracker.repository;

import com.netcracker.domain.CustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;

@Repository
public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Integer> {

    List<CustomerInfo> findAll();

    @Query(value = "select * from customer c left outer join customer_info ci\n" +
            "on c.id = ci.customer_id where c.email = :email\n;", nativeQuery = true)
    List<CustomerInfo> findAllByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "Begin;\n" +
            "insert into customer_info values (\n" +
            "\t:customerId, \n" +
            "\t:firstName,\n" +
            "\t:lastName,\n" +
            "\tnull,\n" +
            "\tnull,\n" +
            "\tnull,\n" +
            "\tnull\n" +
            ");\n" +
            "COMMIT;\n", nativeQuery = true)
    void registerCustomerInfo(@Param("customerId") Long customerId,
                              @Nullable @Param("firstName") String firstName,
                              @Nullable @Param("lastName") String lastName);

    @Modifying
    @Transactional
    @Query(value = "Begin;\n" +
            "update customer_info \n" +
            "set first_name = :firstName,\n" +
            "last_name = :lastName,\n" +
            "gender = :gender,\n" +
            "phone_number = :phoneNumber,\n" +
            "last_delivery_address = :lastDeliveryAddress,\n" +
            "birth_date = :birthDate\n" +
            "where customer_id = :customerId\n;" +
            "COMMIT;", nativeQuery = true)
    void updateCustomerInfo(@Param("customerId") Long customerId,
                            @Nullable @Param("firstName") String firstName,
                            @Nullable @Param("lastName") String lastName,
                            @Nullable @Param("gender") String gender,
                            @Nullable @Param("phoneNumber") String phoneNumber,
                            @Nullable @Param("lastDeliveryAddress") String lastDeliveryAddress,
                            @Param("birthDate") Date birthDate);

    @Modifying
    @Transactional
    @Query(value = "Begin;\n" +
            "update customer_info \n" +
            "set first_name = :firstName,\n" +
            "last_name = :lastName,\n" +
            "gender = :gender,\n" +
            "phone_number = :phoneNumber,\n" +
            "last_delivery_address = :lastDeliveryAddress,\n" +
            "birth_date = null\n" +
            "where customer_id = :customerId\n;" +
            "COMMIT;", nativeQuery = true)
    void updateCustomerInfoDateNull(@Param("customerId") Long customerId,
                            @Nullable @Param("firstName") String firstName,
                            @Nullable @Param("lastName") String lastName,
                            @Nullable @Param("gender") String gender,
                            @Nullable @Param("phoneNumber") String phoneNumber,
                            @Nullable @Param("lastDeliveryAddress") String lastDeliveryAddress);

}
