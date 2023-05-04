package com.pfe.repository;

import com.pfe.entity.Restaurant;
import com.pfe.entity.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface TablesRepository extends JpaRepository<Tables, Long> {
    List<Tables> findAllByIdRestauraut(Restaurant restaurant);
}
