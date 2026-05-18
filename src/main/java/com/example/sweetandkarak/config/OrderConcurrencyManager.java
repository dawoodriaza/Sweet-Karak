package com.example.sweetandkarak.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class OrderConcurrencyManager {

    private final ConcurrentHashMap<Long, ReentrantLock> itemStockLocks = new ConcurrentHashMap<>();

    public ReentrantLock getItemStockLock(Long itemId) {
        return itemStockLocks.computeIfAbsent(itemId, id -> new ReentrantLock(true));
    }

    public void removeItemLock(Long itemId) {
        itemStockLocks.remove(itemId);
        log.info("Removed stock lock for item {}", itemId);
    }
}
