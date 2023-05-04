package com.pfe.repository;

import com.pfe.entity.Customer;
import com.pfe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByUser(User user);
    Customer findByUser_Email(String email);
}
