package com.pfe.repository;

import com.pfe.entity.Manager;
import com.pfe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ManagerRepository extends JpaRepository<Manager,Long> {
//    @Query(nativeQuery = true,value="select * from manager  where user = :userId")
    Manager findByUser(User user);
    Manager findByUser_Email(String email);
}
