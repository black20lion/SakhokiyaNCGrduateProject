package com.netcracker.controller;

import com.netcracker.domain.Order;
import com.netcracker.domain.enumeration.DeliveryStatus;
import com.netcracker.domain.enumeration.DeliveryType;
import com.netcracker.domain.enumeration.OrderStatus;
import com.netcracker.domain.enumeration.PayType;
import com.netcracker.exception.ResourceNotFoundException;
import com.netcracker.service.OrderService;
import com.netcracker.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rest")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    TokenService tokenService;

    @GetMapping("/orders/{id}")
    public ResponseEntity<List<Order>> getOrderById(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        List<Order> orders = orderService.getByOrderId(id);
        return ResponseEntity.ok().body(orders);
    }

    @RolesAllowed({"user"})
    @GetMapping("/orders/params")
    public ResponseEntity<List<Order>> getOrdersByParams(@Nullable @RequestParam(value = "order_status") OrderStatus orderStatus,
                                                         @Nullable @RequestParam(value = "not_completed") Boolean notCompleted)
            throws ResourceNotFoundException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("Authorization");
        Long id = tokenService.getIdFromToken(token);
        List<Order> orders = orderService.getOrdersByCustomerIdOrOrderStatus(id, orderStatus, notCompleted);
        return ResponseEntity.ok().body(orders);
    }

    @RolesAllowed("user")
    @PostMapping("/orders/current")
    public ResponseEntity<List<Long>> createOrder(@RequestParam(value = "commentary") String commentary,
                                                   @RequestParam(value = "delivery_address") String deliveryAddress,
                                                   @RequestParam(value = "delivery_type") DeliveryType deliveryType,
                                                   @RequestParam(value = "delivery_status") DeliveryStatus deliveryStatus,
                                                   @RequestParam(value = "phone_number") String phoneNumber,
                                                   @RequestParam(value = "email") String email,
                                                  @RequestParam(value = "name") String name,
                                                  @RequestParam(value = "pay_type") PayType payType)
            throws ResourceNotFoundException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("Authorization");
        Long id = tokenService.getIdFromToken(token);
        List<Long> orderId = orderService.createOrder(id, commentary, deliveryAddress, deliveryType.toString(), deliveryStatus.toString(), phoneNumber, email, name, payType.toString());
        return ResponseEntity.ok().body(orderId);
    }

    @RolesAllowed("user")
    @PostMapping("/orders/payment")
    public ResponseEntity<String> payForOrder()
            throws ResourceNotFoundException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("Authorization");
        Long id = tokenService.getIdFromToken(token);
        orderService.payOrder(id);
        String message = "order is paid";
        return ResponseEntity.ok().body(message);
    }

}
