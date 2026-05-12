package com.example.sweetandkarak.controller;


import com.example.sweetandkarak.model.Item;
import com.example.sweetandkarak.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @PostMapping
    public Item createItem(@RequestBody Item item) {
        return itemService.createItem(item);
    }


    @GetMapping("/{id}")
    public Item getById(@PathVariable Long id) {
        return itemService.getItemById(id);
    }


    @GetMapping
    public Page<Item> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return itemService.getAllItems(pageable);
    }


    @GetMapping("/cafe/{cafeId}")
    public Page<Item> getByCafe(
            @PathVariable Long cafeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return itemService.getByCafe(cafeId, pageable);
    }

    @GetMapping("/search")
    public Page<Item> search(
            @RequestParam String name,
            @RequestParam(required = false) Long cafeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (cafeId != null) {
            return itemService.searchByCafeAndName(cafeId, name, pageable);
        }

        return itemService.searchByName(name, pageable);
    }


    @PutMapping("/{id}")
    public Item update(@PathVariable Long id, @RequestBody Item item) {
        return itemService.updateItem(id, item);
    }


    @PostMapping("/{id}/image")
    public Item uploadImage(@PathVariable Long id, @RequestParam MultipartFile file) {
        return itemService.uploadImage(id, file);
    }

    @PatchMapping("/{id}/activate")
    public void activate(@PathVariable Long id) {
        itemService.activate(id);
    }


    @PatchMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        itemService.deactivate(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }
}