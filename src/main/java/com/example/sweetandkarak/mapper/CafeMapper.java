package com.example.sweetandkarak.mapper;

import com.example.sweetandkarak.dto.request.CafeCreateRequest;
import com.example.sweetandkarak.dto.response.CafeResponse;
import com.example.sweetandkarak.enums.CafeStatusEnum;
import com.example.sweetandkarak.model.Cafe;
import com.example.sweetandkarak.model.User;
import org.springframework.stereotype.Component;

@Component
public class CafeMapper {

    public Cafe toEntity(CafeCreateRequest request, User cafeAdmin) {
        return Cafe.builder()
                .cafeName(request.getCafeName())
                .location(request.getLocation())
                .cafeStatus(CafeStatusEnum.PENDING_APPROVAL)
                .cafeAdmin(cafeAdmin)
                .build();
    }

    public CafeResponse toResponse(Cafe cafe) {
        return CafeResponse.builder()
                .id(cafe.getId())
                .cafeName(cafe.getCafeName())
                .cafeImage(cafe.getCafeImage())
                .location(cafe.getLocation())
                .ratingOutOf5Star(cafe.getRatingOutOf5Star())
                .cafeStatus(cafe.getCafeStatus())
                .cafeAdminId(cafe.getCafeAdmin().getId())
                .cafeAdminName(cafe.getCafeAdmin().getFullName())
                .isActive(cafe.getIsActive())
                .createdOn(cafe.getCreatedOn())
                .build();
    }
}
