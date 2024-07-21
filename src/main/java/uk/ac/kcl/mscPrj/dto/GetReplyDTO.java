package uk.ac.kcl.mscPrj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetReplyDTO {
    private String poster;

    private String body;

    private Integer rating;
}
