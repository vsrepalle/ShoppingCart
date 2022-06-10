package com.shoppingcart.repository;

import com.shoppingcart.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating,Integer> {

    @Query(value = "select r from Rating r order by r.rating desc")
    List<Rating> findByRatingOrderByRatingDesc();

}
