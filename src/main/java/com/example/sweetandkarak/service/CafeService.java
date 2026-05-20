package com.example.sweetandkarak.service;

import com.example.sweetandkarak.dto.request.CafeCreateRequest;
import com.example.sweetandkarak.dto.response.CafeResponse;
import com.example.sweetandkarak.enums.CafeStatusEnum;
import com.example.sweetandkarak.enums.RoleEnum;
import com.example.sweetandkarak.exception.DuplicateResourceException;
import com.example.sweetandkarak.exception.ResourceNotFoundException;
import com.example.sweetandkarak.mapper.CafeMapper;
import com.example.sweetandkarak.model.Cafe;
import com.example.sweetandkarak.model.User;
import com.example.sweetandkarak.repository.CafeRepository;
import com.example.sweetandkarak.repository.UserRepository;
import com.example.sweetandkarak.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;
    private final CafeMapper cafeMapper;
    private final EmailService emailService;
    private final FileUploadUtil fileUploadUtil;

    @Value("${app.admin.email}")
    private String systemAdminEmail;

    @Transactional
    public CafeResponse createCafe(CafeCreateRequest request) {
        if (cafeRepository.existsByCafeName(request.getCafeName())) {
            throw new DuplicateResourceException("Cafe name already exists: " + request.getCafeName());
        }

        User cafeAdmin = userRepository.findById(request.getCafeAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getCafeAdminId()));

        if (cafeAdmin.getRole() == RoleEnum.CUSTOMER || cafeAdmin.getRole() == RoleEnum.NON_SIGNED_UP_CUSTOMER) {
            cafeAdmin.setRole(RoleEnum.CAFE_ADMIN);
            userRepository.save(cafeAdmin);
            log.info("User {} promoted to CAFE_ADMIN", cafeAdmin.getId());
        }

        Cafe cafe = cafeMapper.toEntity(request, cafeAdmin);
        Cafe savedCafe = cafeRepository.save(cafe);
        log.info("Cafe created: {}, status: PENDING_APPROVAL", savedCafe.getId());

        emailService.sendCafeRequestSubmittedEmail(cafeAdmin.getEmail(), savedCafe.getCafeName());
        return cafeMapper.toResponse(savedCafe);
    }

    public CafeResponse getCafeById(Long id) {
        return cafeMapper.toResponse(findById(id));
    }

    public Page<CafeResponse> getAllCafes(Pageable pageable) {
        return cafeRepository.findAll(pageable).map(cafeMapper::toResponse);
    }

    public Page<CafeResponse> searchCafesByName(String name, Pageable pageable) {
        return cafeRepository.findByCafeName(name, pageable).map(cafeMapper::toResponse);
    }

    public Page<CafeResponse> getCafesByAdminEmail(String email, Pageable pageable) {
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return cafeRepository.findByCafeAdminId(admin.getId(), pageable).map(cafeMapper::toResponse);
    }

    public Page<CafeResponse> getCafesByStatus(String status, Pageable pageable) {
        return cafeRepository.findByCafeStatus(CafeStatusEnum.valueOf(status.toUpperCase()), pageable)
                .map(cafeMapper::toResponse);
    }

    @Transactional
    public CafeResponse uploadCafeImage(Long id, MultipartFile file) {
        Cafe cafe = findById(id);
        try {
            if (cafe.getCafeImage() != null) {
                fileUploadUtil.deleteFile(cafe.getCafeImage());
            }
            cafe.setCafeImage(fileUploadUtil.saveFile(file, "cafe"));
            log.info("Cafe image updated: {}", id);
            return cafeMapper.toResponse(cafeRepository.save(cafe));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save cafe image to disk");
        }
    }

    @Transactional
    public CafeResponse approveCafe(Long id) {
        Cafe cafe = findById(id);
        cafe.setCafeStatus(CafeStatusEnum.APPROVED);
        Cafe updated = cafeRepository.save(cafe);
        emailService.sendCafeApprovedEmail(cafe.getCafeAdmin().getEmail(), cafe.getCafeName());
        log.info("Cafe approved: {}", id);
        return cafeMapper.toResponse(updated);
    }

    @Transactional
    public CafeResponse rejectCafe(Long id) {
        Cafe cafe = findById(id);
        cafe.setCafeStatus(CafeStatusEnum.REJECTED);
        Cafe updated = cafeRepository.save(cafe);
        emailService.sendCafeRejectedEmail(cafe.getCafeAdmin().getEmail(), cafe.getCafeName());
        log.info("Cafe rejected: {}", id);
        return cafeMapper.toResponse(updated);
    }

    @Transactional
    public void activateCafe(Long id) {
        Cafe cafe = findById(id);
        cafe.setIsActive(1);
        cafeRepository.save(cafe);
        log.info("Cafe activated: {}", id);
    }

    @Transactional
    public void deactivateCafe(Long id) {
        Cafe cafe = findById(id);
        cafe.setIsActive(0);
        cafeRepository.save(cafe);
        log.info("Cafe deactivated: {}", id);
    }

    @Transactional
    public void deleteCafe(Long id) {
        cafeRepository.delete(findById(id));
        log.info("Cafe deleted: {}", id);
    }

    private Cafe findById(Long id) {
        return cafeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cafe not found with ID: " + id));
    }
}
