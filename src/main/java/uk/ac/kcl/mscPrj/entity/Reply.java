package uk.ac.kcl.mscPrj.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
