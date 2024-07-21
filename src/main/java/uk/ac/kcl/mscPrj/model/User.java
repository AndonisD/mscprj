package uk.ac.kcl.mscPrj.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
public class User {
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isVerified=false;
        this.role="USER";
    }

    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isVerified=false;
        this.role=role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min=3, max = 50)
    @Column(name = "username", unique = true)
    private String username;
    
    @Column(name = "email", unique = true)
    @Email
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "verified")
    private boolean isVerified;

    @OneToMany(mappedBy = "poster")
    private List<Post> posts;

    @OneToMany
    @JoinColumn(name = "rater_id")
    private List<Rating> ratings;

    @Column(name = "roles")
    private String role;

}
