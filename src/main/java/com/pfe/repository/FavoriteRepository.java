package com.pfe.repository;

import com.pfe.entity.Customer;
import com.pfe.entity.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    Favorite findByCustomerAndRestaurantId(Customer customer, Long id);
    void deleteByCustomerAndRestaurantId(Customer customer, Long id);

    Page<Favorite> findAllByCustomer_User_Email(Pageable pageable, String email);
}
