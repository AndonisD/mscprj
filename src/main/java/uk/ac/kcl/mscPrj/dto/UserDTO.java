package uk.ac.kcl.mscPrj.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    @NotNull
    @Size(min = 1)
    private String username;

//  @ValidEmail - custom validator can be used
//  @Email(message = "Email should be valid", regexp = "^[A-Za-z0-9+_.-]+@uni-sofia.com")
    @NotNull
    @Size(min = 1)
    private String email;

    @NotNull
    @Size(min = 1)
    private String password;

//   @Pattern(regexp = "BACHELOR|MASTER|DOCTORATE", message = "Degree should be BACHELOR, MASTER or DOCTORATE")
}
