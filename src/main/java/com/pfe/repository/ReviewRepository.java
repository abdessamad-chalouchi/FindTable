package com.pfe.repository;

import com.pfe.entity.Restaurant;
import com.pfe.entity.ReviewRestaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public interface ReviewRepository extends JpaRepository<ReviewRestaurant,Long> {
    @Query("SELECT COUNT(r.numberStar) as nbRating,AVG(r.numberStar) as rating FROM ReviewRestaurant r where r.idRestaurant = ?1")
    List<List<Float>> avgRestaurantRating(Restaurant restaurant);
    @Query(value = "SELECT r.idRestaurant as restaurant,AVG(r.numberStar) as rating " +
            "FROM ReviewRestaurant r group by r.idRestaurant  order by AVG(r.numberStar) DESC")
    List<Object[]> listAvgRestaurantRating();
    @Query(value = "select substring(r.numberStar,1,1) as rating,count(r) from ReviewRestaurant r where r.idRestaurant = ?1 group by rating")
    List<ArrayList<Integer>> restaurantRatingDetail(Restaurant restaurant);
    List<ReviewRestaurant> findByIdRestaurant(Restaurant restaurant);
    Page<ReviewRestaurant> findAllByIdRestaurant(Restaurant restaurant, Pageable pageable);
    Page<ReviewRestaurant> findAllByIdRestaurantAndCommentIsNotNull(Restaurant restaurant, Pageable pageable);
    Page<ReviewRestaurant> findAllByIdRestaurantAndCommentIsNull(Restaurant restaurant,Pageable pageable);
}
