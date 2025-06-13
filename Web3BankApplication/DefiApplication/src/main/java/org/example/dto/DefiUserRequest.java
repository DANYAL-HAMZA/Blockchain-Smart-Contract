package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefiUserRequest {
    @Schema(
            name = "First Name of user"
    )
    private String userName;
    @Schema(
            name = "Last name of user"
    )
    private String password;
    @Schema(
            name ="User email"
    )
    private String email;
    @Schema(
            name ="User blockchain wallet address"
    )
    private String walletAddress;

}
