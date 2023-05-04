package com.pfe.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tables")
public class Tables {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_restauraut", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer"}) // to ignor Serializabl
    private Restaurant idRestauraut;

    public Tables(Restaurant idRestauraut) {
        this.idRestauraut = idRestauraut;
    }
}