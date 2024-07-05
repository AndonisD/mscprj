package uk.ac.kcl.mscPrj.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "votes")
@Data
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @ManyToOne
    @JoinColumn(name = "rater_id")
    private User rater;

    @Column(name = "up_vote")
    private Boolean upVote;

    public Rating(Reply reply, User rater, Boolean upVote) {
        this.reply = reply;
        this.rater = rater;
        this.upVote = upVote;
    }
}
