package uk.ac.kcl.mscPrj.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
public class VerificationToken { //POSSIBLE TO SET EXPIRATION DATE TOO
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="token_string")
    private String verificationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public VerificationToken(User user) {
        this.user = user;
        createdDate = new Date();
        verificationToken = UUID.randomUUID().toString();
    }

}
