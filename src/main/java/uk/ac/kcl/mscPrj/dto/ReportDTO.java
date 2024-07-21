package uk.ac.kcl.mscPrj.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportDTO {
    @NotNull
    private Long reportableId;
}
