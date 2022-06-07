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

    public void addItemIntoBasket(Long customer_id, Long offer_id) {
        repository.addItemIntoBasket(customer_id, offer_id);
    }

    public void deleteAllByCustomerId(Long id) {
        repository.deleteAllByCustomerId(id);
    }


    public void removeItemFromBasket (Long customer_id, Long offer_id) {
        List<Boolean> myBool = repository.checkIfLessThanTwo(customer_id, offer_id);
        if (!(myBool.get(0) == null)) {
            if (myBool.get(0)) {
                repository.startTransaction();
                repository.removeItemFromBasket(customer_id, offer_id);
                repository.endTransaction();
            } else {
                repository.startTransaction();
                repository.decrementNumberOfItems(customer_id, offer_id);
                repository.endTransaction();
            }
        }
    }

    public void mergeBasket(Long customer_id, Long[] offer_id, Long[] quantity) {
        repository.startTransaction();
        for (int i = 0; i < offer_id.length; i++) {
            for (int j = 0; j < quantity[i]; j++) {
                repository.addItemIntoBasketMerginal(customer_id, offer_id[i]);
            }
        }
        repository.endTransaction();
    }
}
