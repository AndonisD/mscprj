package uk.ac.kcl.mscPrj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.kcl.mscPrj.model.Reply;
import uk.ac.kcl.mscPrj.model.User;
import uk.ac.kcl.mscPrj.model.Rating;

import java.util.List;

public interface RateRepository extends JpaRepository<Rating, Long> {
    Rating findByReplyAndRater(Reply reply, User voter);

    public List<Rating> findAllByReply(Reply reply);
}
