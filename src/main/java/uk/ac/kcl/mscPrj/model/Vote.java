package uk.ac.kcl.mscPrj.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "votes")
@Data
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @ManyToOne
    @JoinColumn(name = "voter_id")
    private User voter;

    @Column(name = "up_vote")
    private Boolean upVote;

    public Vote(Reply reply, User voter, Boolean upVote) {
        this.reply = reply;
        this.voter = voter;
        this.upVote = upVote;
    }
}
