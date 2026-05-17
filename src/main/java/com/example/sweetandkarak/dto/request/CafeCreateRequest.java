package com.example.sweetandkarak.dto.request;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CafeCreateRequest {

    @NotBlank(message = "Cafe name is required")
    private String cafeName;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Cafe admin ID is required")
    private Long cafeAdminId;
}
