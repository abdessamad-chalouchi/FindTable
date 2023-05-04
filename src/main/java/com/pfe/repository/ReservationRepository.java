package com.pfe.repository;

import com.pfe.entity.*;
import com.pfe.models.CustomReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
@Transactional
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Reservation findByDateAndIdTableAndTime(LocalDate date, Tables idTables, LocalTime time);
    Page<Reservation> findAllByIdCustomer_User_Email(Pageable pageable, String email);
    Page<Reservation> findAllByIdTable_IdRestauraut(Pageable pageable, Restaurant restaurant);
    void deleteByIdAndIdCustomer(Long id, Customer customer);

    @Query("SELECT r.time, COUNT(r.time) "
            + "FROM Reservation AS r where r.idTable.idRestauraut=:res and r.date=:date GROUP BY r.time ")
    List<Object[]> findAllByIdAndRestaurant(@Param("res") Restaurant restaurant, @Param("date") LocalDate date);


}
