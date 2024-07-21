package uk.ac.kcl.mscPrj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {
    private String username;

    private String reputation;
}
