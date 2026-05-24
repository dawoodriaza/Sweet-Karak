package com.example.sweetandkarak.service;

import com.example.sweetandkarak.dto.request.ItemCreateRequest;
import com.example.sweetandkarak.dto.response.ItemResponse;
import com.example.sweetandkarak.enums.CafeStatusEnum;
import com.example.sweetandkarak.exception.CafeNotApprovedException;
import com.example.sweetandkarak.exception.ResourceNotFoundException;
import com.example.sweetandkarak.mapper.ItemMapper;
import com.example.sweetandkarak.model.Cafe;
import com.example.sweetandkarak.model.Item;
import com.example.sweetandkarak.repository.CafeRepository;
import com.example.sweetandkarak.repository.ItemRepository;
import com.example.sweetandkarak.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CafeRepository cafeRepository;
    private final ItemMapper itemMapper;
    private final FileUploadUtil fileUploadUtil;

    @Transactional
    public ItemResponse createItem(ItemCreateRequest request) {
        Cafe cafe = findCafeById(request.getCafeId());

        if (cafe.getCafeStatus() != CafeStatusEnum.APPROVED) {
            throw new CafeNotApprovedException("Cafe must be approved before adding items. Cafe ID: " + cafe.getId());
        }

        Item item = itemMapper.toEntity(request, cafe);
        Item savedItem = itemRepository.save(item);
        log.info("Item created: {} for cafe: {}", savedItem.getId(), cafe.getId());
        return itemMapper.toResponse(savedItem);
    }

    public ItemResponse getItemById(Long id) {
        return itemMapper.toResponse(findItemById(id));
    }

    public Page<ItemResponse> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable).map(itemMapper::toResponse);
    }

    public Page<ItemResponse> getActiveItemsByCafe(Long cafeId, Pageable pageable) {
        return itemRepository.findByCafeIdAndIsActive(cafeId, 1, pageable).map(itemMapper::toResponse);
    }

    public Page<ItemResponse> getAllItemsByCafe(Long cafeId, Pageable pageable) {
        return itemRepository.findByCafeId(cafeId, pageable).map(itemMapper::toResponse);
    }

    public Page<ItemResponse> searchActiveItems(String name, Pageable pageable) {
        return itemRepository.findByItemNameIsActive(name, 1, pageable).map(itemMapper::toResponse);
    }

    public Page<ItemResponse> searchActiveItemsByCafe(Long cafeId, String name, Pageable pageable) {
        return itemRepository.findByCafeIdAndItemName(cafeId, name, 1, pageable).map(itemMapper::toResponse);
    }

    @Transactional
    public ItemResponse updateItem(Long id, ItemCreateRequest request) {
        Item item = findItemById(id);
        item.setItemName(request.getItemName());
        item.setItemDescription(request.getItemDescription());
        item.setPrice(request.getPrice());
        item.setQuantityAvailable(request.getQuantityAvailable());
        Item updatedItem = itemRepository.save(item);
        log.info("Item updated: {}", id);
        return itemMapper.toResponse(updatedItem);
    }

    @Transactional
    public ItemResponse uploadItemImage(Long id, MultipartFile file) {
        Item item = findItemById(id);
        try {
            if (item.getItemImage() != null) {
                fileUploadUtil.deleteFile(item.getItemImage());
            }
            String filePath = fileUploadUtil.saveFile(file, "item");
            item.setItemImage(filePath);
            Item updatedItem = itemRepository.save(item);
            log.info("Item image updated for item: {}", id);
            return itemMapper.toResponse(updatedItem);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (IOException e) {
            log.error("Failed to upload item image for item {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to save item image to disk");
        }
    }

    @Transactional
    public void activateItem(Long id) {
        Item item = findItemById(id);
        item.setIsActive(1);
        itemRepository.save(item);
        log.info("Item activated: {}", id);
    }

    @Transactional
    public void deactivateItem(Long id) {
        Item item = findItemById(id);
        item.setIsActive(0);
        itemRepository.save(item);
        log.info("Item deactivated: {}", id);
    }

    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
        log.info("Item deleted: {}", id);
    }

    private Item findItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with ID: " + id));
    }

    private Cafe findCafeById(Long id) {
        return cafeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cafe not found with ID: " + id));
    }
}