package com.pfe.repository;

import com.pfe.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r.city,r.country FROM Restaurant AS r order by r.country")
    List<Object[]> findAllCity();

}
