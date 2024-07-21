package uk.ac.kcl.mscPrj.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class AbstractReportable {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
}
