package uk.ac.kcl.mscPrj.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;
    
    @Column(name = "email", unique = true)
    private String email;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;
}
