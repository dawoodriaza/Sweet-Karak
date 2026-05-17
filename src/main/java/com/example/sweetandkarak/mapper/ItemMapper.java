package com.example.sweetandkarak.mapper;

import com.example.sweetandkarak.dto.request.ItemCreateRequest;
import com.example.sweetandkarak.dto.response.ItemResponse;
import com.example.sweetandkarak.model.Cafe;
import com.example.sweetandkarak.model.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public Item toEntity(ItemCreateRequest request, Cafe cafe) {
        return Item.builder()
                .itemName(request.getItemName())
                .itemDescription(request.getItemDescription())
                .price(request.getPrice())
                .quantityAvailable(request.getQuantityAvailable())
                .cafe(cafe)
                .build();
    }

    public ItemResponse toResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .itemName(item.getItemName())
                .itemImage(item.getItemImage())
                .itemDescription(item.getItemDescription())
                .price(item.getPrice())
                .quantityAvailable(item.getQuantityAvailable())
                .cafeId(item.getCafe().getId())
                .cafeName(item.getCafe().getCafeName())
                .isActive(item.getIsActive())
                .createdOn(item.getCreatedOn())
                .build();
    }
}
