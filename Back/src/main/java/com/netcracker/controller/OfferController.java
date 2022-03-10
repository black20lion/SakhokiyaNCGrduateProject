package com.netcracker.controller;

import com.netcracker.domain.Offer;
import com.netcracker.exception.ResourceNotFoundException;
import com.netcracker.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rest")
public class OfferController {

    @Autowired
    OfferService offerService;

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
    public ResponseEntity<List<Offer>> getOfferBySaleStatus(@PathVariable(value = "sale") boolean sale)
            throws ResourceNotFoundException {
        List<Offer> offers = offerService.getOfferBySaleStatus(sale);
        return ResponseEntity.ok().body(offers);
    }

    @GetMapping("/offers/novelty/{novelty}")
    public ResponseEntity<List<Offer>> getOfferByNoveltyStatus(@PathVariable(value = "novelty") boolean novelty)
            throws ResourceNotFoundException {
        List<Offer> offers = offerService.getOfferByNoveltyStatus(novelty);
        return ResponseEntity.ok().body(offers);
    }

    @GetMapping("/offers/product/{id}")
    public ResponseEntity<List<Offer>> getOfferByProductId(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        List<Offer> offers = offerService.getOfferByProductId(id);
        return ResponseEntity.ok().body(offers);
    }

    @GetMapping("/offers/category/{id}")
    public ResponseEntity<List<Offer>> getProductByCategoryId(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        List<Offer> products = offerService.getOfferByProductCategoryId(id);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/offers/best/{id}")
    public ResponseEntity<List<Offer>> getBestOffers(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        List<Offer> products = offerService.getBestOffers(id);
        return ResponseEntity.ok().body(products);
    }
}
