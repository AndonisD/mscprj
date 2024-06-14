package uk.ac.kcl.mscPrj.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    @Column(name = "poster")
    private String username;

    @ManyToOne
    @JoinColumn(name = "poster_id") //FIXME: add username... or not to allow for anonymous posts?
    private User poster;

    @OneToMany
    @JoinColumn(name = "post_id")
    private List<Response> responses;
    
}
