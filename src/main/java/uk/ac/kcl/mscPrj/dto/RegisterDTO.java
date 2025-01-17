package uk.ac.kcl.mscPrj.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import uk.ac.kcl.mscPrj.validation.ValidEmailDomain;

@Data
public class RegisterDTO {
    public RegisterDTO(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @NotNull
    @Size(min = 3)
    private String username;

    @Email(message = "Invalid email")
    @ValidEmailDomain(message = "Please register with your student email")
    @NotNull
    private String email;

    @NotNull
    @Size(min = 1)
    private String password;

//   @Pattern(regexp = "BACHELOR|MASTER|DOCTORATE", message = "Degree should be BACHELOR, MASTER or DOCTORATE")
}
