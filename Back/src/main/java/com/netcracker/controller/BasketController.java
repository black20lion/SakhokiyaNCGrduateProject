package com.netcracker.controller;

import com.netcracker.domain.BasketItem;
import com.netcracker.exception.ResourceNotFoundException;
import com.netcracker.service.BasketService;
import com.netcracker.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rest")
public class BasketController {

    @Autowired
    BasketService basketService;

    @Autowired
    TokenService tokenService;

    @RolesAllowed("user")
    @GetMapping("/baskets")
    public ResponseEntity<List<BasketItem>> getBasket()
            throws ResourceNotFoundException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("Authorization");
        Long id = tokenService.getIdFromToken(token);
        List<BasketItem> basketItems = basketService.getAllBasketItemsByCustomerId(id);
        return ResponseEntity.ok().body(basketItems);
    }

    @RolesAllowed("user")
    @DeleteMapping("/baskets")
    public ResponseEntity<String> cleanBasket()
            throws ResourceNotFoundException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("Authorization");
        Long id = tokenService.getIdFromToken(token);
        basketService.deleteAllByCustomerId(id);
        String message = "Cart is empty";
        return ResponseEntity.ok().body(message);
    }

    @RolesAllowed("user")
    @GetMapping("/baskets/short")
    public ResponseEntity<Map<BigDecimal, BigDecimal>> getBasketInformation()
            throws ResourceNotFoundException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("Authorization");
        Long id = tokenService.getIdFromToken(token);
        Map<BigDecimal, BigDecimal> basketInformation = basketService.getAllBasketItemsByCustomerIdShort(id);
        return ResponseEntity.ok().body(basketInformation);
    }

    @RolesAllowed("user")
    @PostMapping("/baskets/add")
    public ResponseEntity<String> addItem(@RequestParam(value = "offer_id") Long offer_id)
            throws ResourceNotFoundException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("Authorization");
        Long id = tokenService.getIdFromToken(token);
        basketService.addItemIntoBasket(id, offer_id);
        String message = "Item successfully added";
        return ResponseEntity.ok().body(message);
    }

    @RolesAllowed("user")
    @PostMapping("/baskets/merge")
    public ResponseEntity<String> mergeBaskets(@RequestParam(value = "offer_id") Long[] offer_id, @RequestParam(value = "quantity") Long[] quantity)
            throws ResourceNotFoundException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("Authorization");
        Long id = tokenService.getIdFromToken(token);
        basketService.mergeBasket(id, offer_id, quantity);
        String message = "Baskets Merged";
        return ResponseEntity.ok().body(message);
    }

    @RolesAllowed("user")
    @PostMapping("/baskets/remove")
    public ResponseEntity<String> removeItem(@RequestParam(value = "offer_id") Long offer_id)
            throws ResourceNotFoundException {

        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("Authorization");
        Long id = tokenService.getIdFromToken(token);
        basketService.removeItemFromBasket(id, offer_id);
        String message = "Item successfully removed";
        return ResponseEntity.ok().body(message);
    }
}
