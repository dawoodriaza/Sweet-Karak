package com.example.sweetandkarak.service;




import com.example.sweetandkarak.enums.CafeStatusEnum;
import com.example.sweetandkarak.exception.ResourceNotFoundException;
import com.example.sweetandkarak.model.Cafe;
import com.example.sweetandkarak.model.Item;
import com.example.sweetandkarak.repository.CafeRepository;
import com.example.sweetandkarak.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CafeRepository cafeRepository;

    public ItemService(ItemRepository itemRepository,
                       CafeRepository cafeRepository) {
        this.itemRepository = itemRepository;
        this.cafeRepository = cafeRepository;
    }


    public Item createItem(Item item) {

        Cafe cafe = cafeRepository.findById(item.getCafe().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cafe not found"));

        if (cafe.getCafeStatus() != CafeStatusEnum.APPROVED) {
            throw new RuntimeException("Cafe not approved");
        }

        item.setCafe(cafe);

        return itemRepository.save(item);
    }


    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found"));
    }


    public Page<Item> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }


    public Page<Item> getByCafe(Long cafeId, Pageable pageable) {
        return itemRepository.findByCafeId(cafeId, pageable);
    }


    public Page<Item> searchByName(String name, Pageable pageable) {
        return itemRepository.findByItemName(name, pageable);
    }




    public Item updateItem(Long id, Item newItem) {

        Item item = getItemById(id);

        item.setItemName(newItem.getItemName());
        item.setItemDescription(newItem.getItemDescription());
        item.setPrice(newItem.getPrice());
        item.setQuantityAvailable(newItem.getQuantityAvailable());

        return itemRepository.save(item);
    }


    public Item uploadImage(Long id, MultipartFile file) {

        Item item = getItemById(id);

        item.setItemImage(file.getOriginalFilename());

        return itemRepository.save(item);
    }

    public void activate(Long id) {
        Item item = getItemById(id);
        item.setIsActive(1);
        itemRepository.save(item);
    }

    public void deactivate(Long id) {
        Item item = getItemById(id);
        item.setIsActive(0);
        itemRepository.save(item);
    }

    public void delete(Long id) {
        itemRepository.deleteById(id);
    }
}