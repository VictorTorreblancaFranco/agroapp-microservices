package pe.agro.userservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    @NotNull(message = "El ID es obligatorio")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 150, message = "El nombre debe tener entre 2 y 150 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 150, message = "El apellido debe tener entre 2 y 150 caracteres")
    private String lastName;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser pasada")
    private LocalDate birthDate;

    @NotBlank(message = "El tipo de documento es obligatorio")
    @Pattern(regexp = "DNI|CED|PAS", message = "El tipo de documento debe ser DNI, CED o PAS")
    private String documentType;

    @NotBlank(message = "El número de documento es obligatorio")
    private String documentNumber;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe proporcionar un email válido")
    private String email;

    private String password;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^9[0-9]{8}$", message = "El teléfono debe tener 9 dígitos y comenzar con 9")
    private String phone;
}