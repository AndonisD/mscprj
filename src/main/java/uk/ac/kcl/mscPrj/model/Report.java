package uk.ac.kcl.mscPrj.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reports")
@NoArgsConstructor
@Data
public class Report {

    public Report (User reporter, Reportable reportable, String reportableType){
        this.reporter = reporter;
        this.reportable = reportable;
        this.reportableType = reportableType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User reporter;

    @ManyToOne(targetEntity = AbstractReportable.class)
    @JoinColumn(name = "reportable_id")
    private Reportable reportable;

    private String reportableType;

}
