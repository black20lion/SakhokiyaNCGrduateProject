package com.netcracker.service;

import com.netcracker.domain.BasketItem;
import com.netcracker.repository.BasketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class BasketService {
    @Autowired
    BasketRepository repository;

    public List<BasketItem> getAllBasketItemsByCustomerId(Long id) {
        return repository.findAllByCustomerId(id);
    }

    public Map<BigDecimal, BigDecimal> getAllBasketItemsByCustomerIdShort(Long id) {
        return repository.findAllByCustomerIdShort(id);
    }

    public void addItemIntoBasket(Long customer_id, Long offer_id, Long quantity) {
        repository.addItemIntoBasket(customer_id, offer_id, quantity);
    }

    public void deleteAllByCustomerId(Long id) {
        repository.deleteAllByCustomerId(id);
    }

    public void deleteByItemId(Long id) {
        repository.deleteById(id);
    }
}
