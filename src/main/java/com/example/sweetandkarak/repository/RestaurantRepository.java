package com.example.sweetandkarak.repository;

import com.example.sweetandkarak.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
}
