package com.example.sweetandkarak.controller;

import com.example.sweetandkarak.dto.request.ItemCreateRequest;
import com.example.sweetandkarak.dto.response.ApiResponse;
import com.example.sweetandkarak.dto.response.ItemResponse;
import com.example.sweetandkarak.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CAFE_ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<ItemResponse>> createItem(@Valid @RequestBody ItemCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Item created", itemService.createItem(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemResponse>> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Item fetched", itemService.getItemById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ItemResponse>>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdOn") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ApiResponse.success("Items fetched", itemService.getAllItems(pageable)));
    }

    @GetMapping("/cafe/{cafeId}")
    public ResponseEntity<ApiResponse<Page<ItemResponse>>> getItemsByCafe(
            @PathVariable Long cafeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success("Items by cafe", itemService.getItemsByCafe(cafeId, pageable)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ItemResponse>>> searchItems(
            @RequestParam String name,
            @RequestParam(required = false) Long cafeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ItemResponse> items = (cafeId != null)
                ? itemService.searchItemsByCafeAndName(cafeId, name, pageable)
                : itemService.searchItemsByName(name, pageable);
        return ResponseEntity.ok(ApiResponse.success("Search results", items));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CAFE_ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<ItemResponse>> updateItem(@PathVariable Long id, @Valid @RequestBody ItemCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Item updated", itemService.updateItem(id, request)));
    }

    @PostMapping("/{id}/image")
    @PreAuthorize("hasAnyRole('CAFE_ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<ItemResponse>> uploadItemImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.success("Item image uploaded", itemService.uploadItemImage(id, file)));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('CAFE_ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<Object>> activateItem(@PathVariable Long id) {
        itemService.activateItem(id);
        return ResponseEntity.ok(ApiResponse.success("Item activated"));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('CAFE_ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deactivateItem(@PathVariable Long id) {
        itemService.deactivateItem(id);
        return ResponseEntity.ok(ApiResponse.success("Item deactivated"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CAFE_ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.success("Item deleted"));
    }
}
