package com.netcracker.controller;

import com.netcracker.domain.BasketItem;
import com.netcracker.exception.ResourceNotFoundException;
import com.netcracker.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rest")
public class BasketController {

    @Autowired
    BasketService basketService;

    @GetMapping("/baskets/{id}")
    public ResponseEntity<List<BasketItem>> getBasket(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        List<BasketItem> basketItems = basketService.getAllBasketItemsByCustomerId(id);
        return ResponseEntity.ok().body(basketItems);
    }

    @DeleteMapping("/baskets/{id}")
    public ResponseEntity<String> cleanBasket(@PathVariable(value = "id") Long id)
        throws ResourceNotFoundException {
        basketService.deleteAllByCustomerId(id);
        String message = "Cart is empty";
        return ResponseEntity.ok().body(message);
    }


    @GetMapping("/baskets/short/{id}")
    public ResponseEntity<Map<BigDecimal, BigDecimal>> getBasketInformation(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Map<BigDecimal, BigDecimal> basketInformation = basketService.getAllBasketItemsByCustomerIdShort(id);
        return ResponseEntity.ok().body(basketInformation);
    }

    @RolesAllowed("user")
    @PostMapping("/baskets/add")
    public ResponseEntity<String> addItem(@RequestParam(value = "customer_id") Long customer_id,
                                                    @RequestParam(value = "offer_id") Long offer_id)
            throws ResourceNotFoundException {
        basketService.addItemIntoBasket(customer_id, offer_id);
        String message = "Item successfully added";
        return ResponseEntity.ok().body(message);
    }

    @RolesAllowed("user")
    @PostMapping("/baskets/remove")
    public ResponseEntity<String> removeItem(@RequestParam(value = "customer_id") Long customer_id,
                                          @RequestParam(value = "offer_id") Long offer_id)
            throws ResourceNotFoundException {
        basketService.removeItemFromBasket(customer_id, offer_id);
        String message = "Item successfully removed";
        return ResponseEntity.ok().body(message);
    }

}
