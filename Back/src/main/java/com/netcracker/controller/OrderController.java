package com.netcracker.controller;

import com.netcracker.domain.Order;
import com.netcracker.domain.enumeration.DeliveryStatus;
import com.netcracker.domain.enumeration.DeliveryType;
import com.netcracker.domain.enumeration.OrderStatus;
import com.netcracker.domain.enumeration.PayType;
import com.netcracker.exception.ResourceNotFoundException;
import com.netcracker.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rest")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping("/orders/{id}")
    public ResponseEntity<List<Order>> getOrderById(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        List<Order> orders = orderService.getByOrderId(id);
        return ResponseEntity.ok().body(orders);
    }

    @RolesAllowed({"user", "admin"})
    @GetMapping("/orders/params")
    public ResponseEntity<List<Order>> getOrdersByParams(@Nullable @RequestParam(value = "customer_id") Long customerId,
                                                         @Nullable @RequestParam(value = "order_status") OrderStatus orderStatus,
                                                         @Nullable @RequestParam(value = "not_completed") Boolean notCompleted)
            throws ResourceNotFoundException {
        List<Order> orders = orderService.getOrdersByCustomerIdOrOrderStatus(customerId, orderStatus, notCompleted);
        return ResponseEntity.ok().body(orders);
    }

    @RolesAllowed("user")
    @PostMapping("/orders/current")
    public ResponseEntity<List<Long>> createOrder(@RequestParam(value = "customer_id") Long customerId,
                                                   @RequestParam(value = "commentary") String commentary,
                                                   @RequestParam(value = "delivery_address") String deliveryAddress,
                                                   @RequestParam(value = "delivery_type") DeliveryType deliveryType,
                                                   @RequestParam(value = "delivery_status") DeliveryStatus deliveryStatus,
                                                   @RequestParam(value = "phone_number") String phoneNumber,
                                                   @RequestParam(value = "email") String email,
                                                  @RequestParam(value = "name") String name,
                                                  @RequestParam(value = "pay_type") PayType payType)
            throws ResourceNotFoundException {
        List<Long> orderId = orderService.createOrder(customerId, commentary, deliveryAddress, deliveryType.toString(), deliveryStatus.toString(), phoneNumber, email, name, payType.toString());
        return ResponseEntity.ok().body(orderId);
    }

    @PostMapping("/orders/payment/{id}")
    public ResponseEntity<String> payForOrder(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        orderService.payOrder(id);
        String message = "order is paid";
        return ResponseEntity.ok().body(message);
    }

}
