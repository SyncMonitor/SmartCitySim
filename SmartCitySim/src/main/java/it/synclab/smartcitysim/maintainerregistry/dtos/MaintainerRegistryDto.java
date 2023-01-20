package it.synclab.smartcitysim.maintainerregistry.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaintainerRegistryDto {
    
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String company;

    @NotBlank
    private String phone;

    @NotBlank
    @Email
    private String email;
}
