package com.netcracker.service;

import com.netcracker.domain.Order;
import com.netcracker.domain.enumeration.OrderStatus;
import com.netcracker.repository.BasketRepository;
import com.netcracker.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    OrderRepository repository;

    @Autowired
    BasketRepository basketRepository;

    public List<Order> getByOrderId(Long id) {
        return repository.findById(id);
    }

    public List<Order> getOrdersByCustomerIdOrOrderStatus(Long id, OrderStatus orderStatus, Boolean notCompleted) {
        if (notCompleted != null && notCompleted && id != null)
            return repository.findAllByCustomerIdNotCompleted(id);
        else if (id != null && orderStatus != null)
            return repository.findAllByCustomerIdAndOrderStatus(id, orderStatus);
        else if (id == null && orderStatus != null)
            return repository.findAllByOrderStatus(orderStatus);
        else if (id != null)
            return repository.findAllByCustomerId(id);
        else
            return repository.findAll();
    }

    @Transactional
    public List<Long> createOrder(Long customerId, String commentary, String deliveryAddress, String deliveryType,
                                        String deliveryStatus, String phoneNumber, String email) {
        repository.createOrder(customerId, commentary, deliveryAddress, deliveryType, deliveryStatus, phoneNumber, email);
        List<Long> orderId = repository.getOrderId();
        repository.fillOrder(orderId.get(0), basketRepository.getCountOfBasketItems(customerId).get(0));
        basketRepository.deleteAllByCustomerId(customerId);
        return orderId;
    }

    public void payOrder(Long id) {
        repository.payOrder(id);
    }

}
