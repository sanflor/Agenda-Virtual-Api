package com.todotic.contactlistapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Email(message = "El email es invalido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
}
