package net.javaci.bank202101.api.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CustomerBaseDto extends UserBaseDto {

    @NotEmpty
    private String status;
}
