package MainFolder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import javax.persistence.*;

@Entity
@Data
public class Students {

    @NotEmpty(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Name should contain only letters")
    private String name;

    @NotEmpty(message = "Surname is required")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Surname should contain only letters")
    private String surname;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Day is required")
    private String day;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    

    // public String toString() {
    //     return "Student{" +
    //             "name='" + name + '\'' +
    //             ", surname='" + surname + '\'' +
    //             ", email='" + email + '\'' +
    //             ", password=" + password +
    //             ", day='" + day + '\'' +
    //             ", role=" + role +
    //             '}';
    // }
}
