package com.netcracker.service;

import com.netcracker.domain.Offer;
import com.netcracker.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OfferService {

    @Autowired
    OfferRepository repository;

    public List<Offer> getAllOffers() {
        return repository.findAll();
    }

    public List<Offer> getOfferById(Long id) {
        return repository.findById(id);
    }

    public List<Offer> getOfferByProductId(Long id) {
        return repository.findOfferByProductId(id);
    }

    public List<Offer> getOfferBySaleStatus(boolean sale) {
        return repository.findOfferBySale(sale);
    }

    public List<Offer> getOfferByNoveltyStatus(boolean novelty) {
        return repository.findOfferByNovelty(novelty);
    }

    public List<Offer> getOfferByProductCategoryId(Long id) {
        return repository.getOfferByProductCategoryId(id);
    }

    public List<Offer> getBestOffers(Long id) {
        return repository.getBestOffers(id);
    }
}
