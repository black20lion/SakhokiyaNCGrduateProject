package com.netcracker.repository;

import com.netcracker.domain.Order;
import com.netcracker.domain.enumeration.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAll();

    List<Order> findById(Long id);

    List<Order> findAllByCustomerId(Long id);

    List<Order> findAllByCustomerIdAndOrderStatus(Long id, OrderStatus orderStatus);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    @Query(value = "select * from order_entity where customer_id = :id AND order_status != 'COMPLETED'", nativeQuery = true)
    List<Order> findAllByCustomerIdNotCompleted(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "BEGIN;\n" +
            "insert into order_entity (customer_id, date_time, commentary, delivery_address, total_price, delivery_type, delivery_status, pay_status, order_status, phone_number, email)\n" +
            "values (\n" +
            "\t:customerId,\n" +
            "\tcurrent_date,\n" +
            "\t:commentary,\n" +
            "\t:deliveryAddress,\n" +
            "\t(select sum (total_sum) from \n" +
            "(select *, o.price as actual_price, o.price * bi.quantity as total_sum from basket_item bi left outer join offer o on\n" +
            "bi.offer_id = o.id where bi.customer_id = :customerId and o.price_override IS NULL\n" +
            "union all\n" +
            "select *, o.price_override as actual_price, o.price_override * quantity as total_sum from basket_item bi left outer join offer o on\n" +
            "bi.offer_id = o.id where bi.customer_id = :customerId and o.price_override IS NOT NULL) AS union_table),\n" +
            "\t:deliveryType,\n" +
            "\t:deliveryStatus,\n" +
            "\t'UNPAID',\n" +
            "\t'NEW',\n" +
            "\t:phoneNumber,\n" +
            "\t:email\n" +
            "); \n" +
            "COMMIT;", nativeQuery = true)
    void createOrder(@Param("customerId") Long customerId,
                     @Param("commentary") String commentary,
                     @Param("deliveryAddress") String deliveryAddress,
                     @Param("deliveryType") String deliveryType,
                     @Param("deliveryStatus") String deliveryStatus,
                     @Param("phoneNumber") String phoneNumber,
                     @Param("email") String email);


    @Query(value = "select * from lastval()", nativeQuery = true)
    List<Long> getOrderId();


    @Modifying
    @Query(value =
            "insert into order_item (order_id, offer_id, quantity)\n" +
                    "select distinct :orderId as order_id, bi.offer_id, bi.quantity from generate_series (1, :countOfRows), basket_item bi, order_entity oe " +
                    "where oe.id = :orderId and oe.customer_id = bi.customer_id", nativeQuery = true)
    void fillOrder(@Param("orderId") Long orderId, @Param("countOfRows") BigDecimal countOfRows);


    @Modifying
    @Transactional
    @Query(value = "Begin;\n" +
            "Update order_entity\n" +
            "set pay_status = 'PAID'\n" +
            "where id = :orderId\n;" +
            "commit;", nativeQuery = true)
    void payOrder(@Param("orderId") Long orderId);

}
