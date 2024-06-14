package uk.ac.kcl.mscPrj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.kcl.mscPrj.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
