package com.netcracker.controller;

import com.netcracker.domain.Offer;
import com.netcracker.exception.ResourceNotFoundException;
import com.netcracker.service.OfferService;
import com.netcracker.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rest")
public class OfferController {

    @Autowired
    OfferService offerService;

    @Autowired
    TokenService tokenService;

    @GetMapping("/offers")
    List<Offer> getAllOffers() {
        return offerService.getAllOffers();
    }

    @GetMapping("/offers/{id}")
    public ResponseEntity<List<Offer>> getOfferById(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        List<Offer> offers = offerService.getOfferById(id);
        return ResponseEntity.ok().body(offers);
    }

    @GetMapping("/offers/sale/{sale}")
    public ResponseEntity<List<Offer>> getOffersBySaleStatus(@PathVariable(value = "sale") boolean sale)
            throws ResourceNotFoundException {
        List<Offer> offers = offerService.getOfferBySaleStatus(sale);
        return ResponseEntity.ok().body(offers);
    }

    @GetMapping("/offers/novelty/{novelty}")
    public ResponseEntity<List<Offer>> getOffersByNoveltyStatus(@PathVariable(value = "novelty") boolean novelty)
            throws ResourceNotFoundException {
        List<Offer> offers = offerService.getOfferByNoveltyStatus(novelty);
        return ResponseEntity.ok().body(offers);
    }

    @GetMapping("/offers/product/{id}")
    public ResponseEntity<List<Offer>> getOffersByProductId(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        List<Offer> offers = offerService.getOfferByProductId(id);
        return ResponseEntity.ok().body(offers);
    }

    @GetMapping("/offers/category/{id}")
    public ResponseEntity<List<Offer>> getOffersByCategoryId(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        List<Offer> products = offerService.getOfferByProductCategoryId(id);
        return ResponseEntity.ok().body(products);
    }

    @RolesAllowed("user")
    @GetMapping("/offers/best")
    public ResponseEntity<List<Offer>> getBestOffers()
            throws ResourceNotFoundException {
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("Authorization");
        Long id = tokenService.getIdFromToken(token);
        List<Offer> products = offerService.getBestOffers(id);
        return ResponseEntity.ok().body(products);
    }
}
