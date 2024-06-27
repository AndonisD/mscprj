package uk.ac.kcl.mscPrj.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "posts")
@AllArgsConstructor //FIXME: may be a better idea to make a custom constructor without responses
@NoArgsConstructor
@Data
public class Post {
    public Post(String body, User poster, Boolean isAnonymous){
        this.body = body;
        this.poster = poster;
        this.isAnonymous = isAnonymous;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "poster_id") //FIXME: add username... or not to allow for anonymous posts?
    private User poster;

    @Column(name = "anonymous")
    private Boolean isAnonymous;

    private String body;

    @OneToMany
    @JoinColumn(name = "post_id")
    private List<Reply> replies;
    
}
