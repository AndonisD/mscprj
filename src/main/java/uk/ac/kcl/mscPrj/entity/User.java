package uk.ac.kcl.mscPrj.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(min=3, max = 50)
    @Column(name = "username", unique = true)
    private String username;
    
    @Column(name = "email", unique = true)
    @Email
    private String email;

    @OneToMany(mappedBy = "poster")
    private List<Post> posts;
}
