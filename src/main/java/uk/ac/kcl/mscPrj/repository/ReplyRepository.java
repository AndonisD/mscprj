package uk.ac.kcl.mscPrj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.kcl.mscPrj.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

}