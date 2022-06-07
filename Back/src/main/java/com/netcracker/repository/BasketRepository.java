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
            ":customerId, :offerId, 1) " +
            "on conflict (offer_id) do update " +
            "set quantity = basket_item.quantity+1 ; " +
            "COMMIT; ", nativeQuery = true)
    void addItemIntoBasket(@Param("customerId") Long customerId, @Param("offerId") Long offerId);

    @Modifying
    @Transactional
    @Query(value =
            "insert into basket_item (customer_id, offer_id, quantity) " +
            "values (" +
            ":customerId, :offerId, 1) " +
            "on conflict (offer_id) do update " +
            "set quantity = basket_item.quantity+1 ; ", nativeQuery = true)
    void addItemIntoBasketMerginal(@Param("customerId") Long customerId, @Param("offerId") Long offerId);


    @Modifying
    @Transactional
    @Query(value =
            "delete from basket_item  " +
            "where customer_id = :customerId and offer_id = :offerId " +
                    " ", nativeQuery = true)
    void removeItemFromBasket(@Param("customerId") Long customerId, @Param("offerId") Long offerId);

    @Modifying
    @Transactional
    @Query(value =
            "UPDATE basket_item SET quantity = basket_item.quantity-1  " +
                    "where customer_id = :customerId and offer_id = :offerId ; " +
                    "  ", nativeQuery = true)
    void decrementNumberOfItems(@Param("customerId") Long customerId, @Param("offerId") Long offerId);


    @Transactional
    @Query(value = " \n" +
            " SELECT (( SELECT quantity FROM basket_item " +
            " where customer_id = :customerId and offer_id = :offerId) < 2) ", nativeQuery = true)
    List<Boolean> checkIfLessThanTwo(@Param("customerId") Long customerId, @Param("offerId") Long offerId);




    @Query(value = "select count (*) from basket_item where customer_id = :customerId", nativeQuery = true)
    List<BigDecimal> getCountOfBasketItems(@Param("customerId") Long customerId);

    @Modifying
    @Transactional
    @Query(value = "BEGIN ; ", nativeQuery = true)
    void startTransaction();

    @Modifying
    @Transactional
    @Query(value = "COMMIT ; ", nativeQuery = true)
    void endTransaction();

}
