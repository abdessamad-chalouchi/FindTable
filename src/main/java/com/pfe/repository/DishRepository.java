package com.pfe.repository;

import com.pfe.entity.Dish;
import com.pfe.entity.Menu;
import com.pfe.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByMenu(Menu menu);
    Page<Dish> findAllByMenu_Restaurant(Restaurant restaurant, Pageable pageable);
}
