package com.netcracker.repository;

import com.netcracker.domain.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {
    List<Offer> findAll();
    List<Offer> findById(Long id);
    List<Offer> findOfferByProductId(Long id);
    List<Offer> findOfferBySale(Boolean sale);
    List<Offer> findOfferByNovelty(Boolean novelty);

    @Query("select o from Offer o left outer join Product p on o.productId = p.id where p.categoryId = :id")
    List<Offer> getOfferByProductCategoryId(@Param("id") Long id);

    @Query("select o from Offer o left outer join Product p on o.productId = p.id left outer join Category c on p.categoryId = c.id, Customer cust left outer" +
            " join CustomerInfo ci on cust.id = ci.id where cust.id = :id AND ci.gender = c.gender order by o.priority desc")
    List<Offer> getBestOffers(@Param("id") Long id);

}
