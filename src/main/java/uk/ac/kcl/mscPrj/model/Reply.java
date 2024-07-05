package uk.ac.kcl.mscPrj.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "replies")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reply {

    public Reply(User poster, Post post, String body){
        this.poster = poster;
        this.post = post;
        this.body = body;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "poster_id")
    private User poster;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String body;

    @OneToMany
    @JoinColumn(name = "reply_id")
    private List<Vote> votes;

}
