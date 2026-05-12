package com.example.sweetandkarak.service;




import com.example.sweetandkarak.enums.CafeStatusEnum;
import com.example.sweetandkarak.model.Cafe;
import com.example.sweetandkarak.repository.CafeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CafeService {

    private final CafeRepository cafeRepository;

    public CafeService(CafeRepository cafeRepository) {
        this.cafeRepository = cafeRepository;
    }

    public Cafe createCafe(Cafe cafe) {
        cafe.setCafeStatus(CafeStatusEnum.PENDING_APPROVAL);
        return cafeRepository.save(cafe);
    }

    public Cafe getCafeById(Long id) {
        return cafeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cafe not found"));
    }

    public Page<Cafe> getAll(Pageable pageable) {
        return cafeRepository.findAll(pageable);
    }

    public Page<Cafe> search(String name, Pageable pageable) {
        return cafeRepository.findByCafeName(name, pageable);
    }

    public Page<Cafe> getByAdmin(Long adminId, Pageable pageable) {
        return cafeRepository.findByCafeAdminId(adminId, pageable);
    }

    public Page<Cafe> getByStatus(String status, Pageable pageable) {
        return cafeRepository.findByCafeStatus(
                CafeStatusEnum.valueOf(status),
                pageable
        );
    }

    public Cafe uploadImage(Long id, MultipartFile file) {
        Cafe cafe = getCafeById(id);
        cafe.setImageUrl(file.getOriginalFilename());
        return cafeRepository.save(cafe);
    }

    public Cafe approve(Long id) {
        Cafe cafe = getCafeById(id);
        cafe.setCafeStatus(CafeStatusEnum.APPROVED);
        return cafeRepository.save(cafe);
    }

    public Cafe reject(Long id) {
        Cafe cafe = getCafeById(id);
        cafe.setCafeStatus(CafeStatusEnum.REJECTED);
        return cafeRepository.save(cafe);
    }

    public void activate(Long id) {
        Cafe cafe = getCafeById(id);
        cafe.setActive(true);
        cafeRepository.save(cafe);
    }

    public void deactivate(Long id) {
        Cafe cafe = getCafeById(id);
        cafe.setActive(false);
        cafeRepository.save(cafe);
    }

    public void delete(Long id) {
        cafeRepository.deleteById(id);
    }
}