package com.example.sweetandkarak.controller;


        import com.example.sweetandkarak.model.Cafe;
        import com.example.sweetandkarak.service.CafeService;
        import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api/cafes")
public class CafeController {

    private final CafeService cafeService;

    public CafeController(CafeService cafeService) {
        this.cafeService = cafeService;
    }

    @PostMapping
    public Cafe createCafe(@RequestBody Cafe cafe) {
        return cafeService.createCafe(cafe);
    }

    @GetMapping("/{id}")
    public Cafe getCafeById(@PathVariable Long id){
        return cafeService.getCafeById(id);
    }

    @GetMapping
    public Page<Cafe> getAll(Pageable pageable) {
        return cafeService.getAll(pageable);
    }

    @GetMapping("/search")
    public Page<Cafe> search(@RequestParam String name, Pageable pageable) {
        return cafeService.search(name, pageable);
    }

    @GetMapping("/admin/{adminId}")
    public Page<Cafe> getByAdmin(@PathVariable Long adminId, Pageable pageable) {
        return cafeService.getByAdmin(adminId, pageable);
    }

    @GetMapping("/status")
    public Page<Cafe> getByStatus(@RequestParam String status, Pageable pageable) {
        return cafeService.getByStatus(status, pageable);
    }

    @PostMapping("/{id}/image")
    public Cafe uploadImage(@PathVariable Long id, @RequestParam MultipartFile file) {
        return cafeService.uploadImage(id, file);
    }

    @PatchMapping("/{id}/approve")
    public Cafe approve(@PathVariable Long id) {
        return cafeService.approve(id);
    }

    @PatchMapping("/{id}/reject")
    public Cafe reject(@PathVariable Long id) {
        return cafeService.reject(id);
    }

    @PatchMapping("/{id}/activate")
    public void activate(@PathVariable Long id) {
        cafeService.activate(id);
    }

    @PatchMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        cafeService.deactivate(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        cafeService.delete(id);
    }
}