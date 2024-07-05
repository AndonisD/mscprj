package uk.ac.kcl.mscPrj.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteDTO {
    @NotNull
    private Long replyId;

    @NotNull
    private Boolean upVote;
}
