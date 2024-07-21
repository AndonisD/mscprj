package uk.ac.kcl.mscPrj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetUserDTO {

    private String username;

    private String reputation;
}
