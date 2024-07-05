package uk.ac.kcl.mscPrj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.kcl.mscPrj.model.Reply;
import uk.ac.kcl.mscPrj.model.User;
import uk.ac.kcl.mscPrj.model.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Vote findByReplyAndVoter(Reply reply, User voter);
}
