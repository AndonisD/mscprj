package uk.ac.kcl.mscPrj.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//@Entity
public interface Reportable {
    Long getId();
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
}
