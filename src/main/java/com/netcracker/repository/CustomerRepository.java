package com.netcracker.repository;

import com.netcracker.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findAll();
    <S extends Customer> S save(S entity);

    @Modifying
    @Transactional
    @Query(value = "BEGIN;\n" +
            "insert into customer (email, passwordHash)\n " +
            "values ( " +
            " :email  " +
            " :passwordHash); " +
            "COMMIT;", nativeQuery = true)
    void createCustomer(@Param("email") String email,
                        @Param("passwordHash") String passwordHash);
}
