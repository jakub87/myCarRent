package net.atos.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Pattern(regexp = "^[a-zA-Z -]{3,25}$",message = "Invalid characters / size must be 3 - 25.")
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z -]{3,25}$",message = "Invalid characters / size must be 3 - 25.")
    private String lastName;

     @Pattern(regexp=".+\\@.+\\..+", message = "Not valid email format.") // prosty regex dla walidacji emaila .+\@.+\..+
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must contain: at least one lowercase letter and uppercase and digit and special character and size >= 8.")
    private String password;

    @Pattern(regexp = "[a-zA-Z]*", message = "Invalid characters.")
    private String city;

    private Integer mobileNumber;

}
