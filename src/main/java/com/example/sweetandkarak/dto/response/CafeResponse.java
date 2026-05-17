package com.example.sweetandkarak.dto.response;

import com.example.sweetandkarak.enums.CafeStatusEnum;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CafeResponse {
    private Long id;
    private String cafeName;
    private String cafeImage;
    private String location;
    private Double ratingOutOf5Star;
    private CafeStatusEnum cafeStatus;
    private Long cafeAdminId;
    private String cafeAdminName;
    private Integer isActive;
    private LocalDateTime createdOn;
}
