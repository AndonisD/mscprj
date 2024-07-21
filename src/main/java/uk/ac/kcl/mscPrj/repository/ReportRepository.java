package uk.ac.kcl.mscPrj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.kcl.mscPrj.model.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
