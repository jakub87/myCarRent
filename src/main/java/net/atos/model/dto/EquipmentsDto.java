package net.atos.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentsDto {

    @Pattern(regexp = "^[a-zA-Z][a-zA-Z 0-9]{1,40}")
    private String equipmentDescription;
}
