package uk.ac.kcl.mscPrj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class GetPostDTO {
    private String poster;

    private String body;

    private List<GetReplyDTO> replies;
}
