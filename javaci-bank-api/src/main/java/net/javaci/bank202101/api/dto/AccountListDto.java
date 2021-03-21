package net.javaci.bank202101.api.dto;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccountListDto extends AccountSaveDto {

    @NotEmpty
    private Long id;
    
    @Schema(required = true, example = "123456-00002", name = "account number", description = "Account number" )
    @NotEmpty
    private String accountNumber;
}
