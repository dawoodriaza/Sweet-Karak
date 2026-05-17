package com.example.sweetandkarak.controller;

import com.example.sweetandkarak.dto.request.CafeCreateRequest;
import com.example.sweetandkarak.dto.response.ApiResponse;
import com.example.sweetandkarak.dto.response.CafeResponse;
import com.example.sweetandkarak.service.CafeService;
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

import java.security.Principal;

@RestController
@RequestMapping("/api/cafes")
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CafeResponse>> createCafe(@Valid @RequestBody CafeCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Cafe request submitted. Pending approval.", cafeService.createCafe(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CafeResponse>> getCafeById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Cafe fetched", cafeService.getCafeById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CafeResponse>>> getAllCafes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdOn") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(ApiResponse.success("Cafes fetched", cafeService.getAllCafes(pageable)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<CafeResponse>>> searchCafes(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success("Search results", cafeService.searchCafesByName(name, pageable)));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('CAFE_ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<Page<CafeResponse>>> getMyCafes(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success("My cafes", cafeService.getCafesByAdminEmail(principal.getName(), pageable)));
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<Page<CafeResponse>>> getCafesByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success("Cafes by status", cafeService.getCafesByStatus(status, pageable)));
    }

    @PostMapping("/{id}/image")
    @PreAuthorize("hasAnyRole('CAFE_ADMIN','SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<CafeResponse>> uploadCafeImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.success("Cafe image uploaded", cafeService.uploadCafeImage(id, file)));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<CafeResponse>> approveCafe(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Cafe approved", cafeService.approveCafe(id)));
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<CafeResponse>> rejectCafe(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Cafe rejected", cafeService.rejectCafe(id)));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<Object>> activateCafe(@PathVariable Long id) {
        cafeService.activateCafe(id);
        return ResponseEntity.ok(ApiResponse.success("Cafe activated"));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deactivateCafe(@PathVariable Long id) {
        cafeService.deactivateCafe(id);
        return ResponseEntity.ok(ApiResponse.success("Cafe deactivated"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<Object>> deleteCafe(@PathVariable Long id) {
        cafeService.deleteCafe(id);
        return ResponseEntity.ok(ApiResponse.success("Cafe deleted"));
    }
}
