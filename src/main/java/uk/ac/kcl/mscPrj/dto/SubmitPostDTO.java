package uk.ac.kcl.mscPrj.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubmitPostDTO {
    @NotNull
    @Size(min = 1, max = 500)
    private String body;
    @NotNull
    private Boolean isAnonymous;
}
