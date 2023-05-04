package com.pfe.repository;

import com.pfe.entity.Gallery;
import com.pfe.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface GalleryRepository extends JpaRepository<Gallery, Long> {

    List<Gallery> findByRestaurant(Restaurant restaurant);
}
