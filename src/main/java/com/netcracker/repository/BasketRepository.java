package com.netcracker.repository;

import com.netcracker.domain.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface BasketRepository extends JpaRepository<BasketItem, Integer> {
    List<BasketItem> findAll();

    @Transactional
    void deleteAllByCustomerId(Long id);

    @Transactional
    void deleteById(Long id);

    List<BasketItem> findById(Long id);
    List<BasketItem> findAllByCustomerId(Long id);

    @Query(value = "select count(*), sum (total_sum) from " +
            "(select *, o.price as actual_price, o.price * bi.quantity as total_sum from basket_item bi left outer join offer o on " +
            "bi.offer_id = o.id where customer_id = :id and o.price_override IS NULL " +
            "union all " +
            "select *, o.price_override as actual_price, o.price_override * quantity as total_sum from basket_item bi left outer join offer o on " +
            "bi.offer_id = o.id where customer_id = :id and o.price_override IS NOT NULL) AS union_table", nativeQuery = true)
    Map<BigDecimal, BigDecimal> findAllByCustomerIdShort(@Param("id")Long id);

    @Modifying
    @Transactional
    @Query(value = "BEGIN; " +
            "insert into basket_item (customer_id, offer_id, quantity) " +
            "values (" +
            ":customerId, :offerId, :quantity); " +
            "COMMIT; ", nativeQuery = true)
    void addItemIntoBasket(@Param("customerId") Long customerId, @Param("offerId") Long offerId, @Param("quantity") Long quantity);

    @Query(value = "select count (*) from basket_item where customer_id = :customerId", nativeQuery = true)
    List<BigDecimal> getCountOfBasketItems(@Param("customerId") Long customerId);
}
