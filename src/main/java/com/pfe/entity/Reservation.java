package com.pfe.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@javax.persistence.Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;



    @Column(name = "number_seats", nullable = false)
    private Integer numberSeats;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_customer", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer"}) // to ignor Serializabl
    private Customer idCustomer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_table", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer"}) // to ignor Serializabl
    private Tables idTable;

    @Column(name = "notes")
    private String notes;
    public Reservation(LocalDate date, Integer numberSeats, LocalTime time, Customer idCustomer, String notes) {
        this.date = date;
        this.numberSeats = numberSeats;
        this.time = time;
        this.idCustomer = idCustomer;
        this.notes = notes;
    }
    public Reservation(LocalDate date,
                       Integer numberSeats,
                       LocalTime time,
                       Customer idCustomer,
                       Tables idTable,
                       String notes) {
        this.date = date;
        this.numberSeats = numberSeats;
        this.time = time;
        this.idCustomer = idCustomer;
        this.idTable = idTable;
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getNumberSeats() {
        return numberSeats;
    }

    public void setNumberSeats(Integer numberSeats) {
        this.numberSeats = numberSeats;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Customer getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(Customer idCustomer) {
        this.idCustomer = idCustomer;
    }

    public Tables getIdTable() {
        return idTable;
    }

    public void setIdTable(Tables idTables) {
        this.idTable = idTables;
    }

}