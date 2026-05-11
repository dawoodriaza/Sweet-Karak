package com.example.sweetandkarak.service;


import com.example.sweetandkarak.exception.ResourceNotFoundException;
import com.example.sweetandkarak.model.Restaurant;
import com.example.sweetandkarak.repository.RestaurantRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getAllRestaurants(){
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(long Id){
        return restaurantRepository.findById(Id).orElseThrow(()->new ResourceNotFoundException("Restaurant not found"));

    }

    public Restaurant createRestaurant(Restaurant restaurant){
        return restaurantRepository.save(restaurant);
    }
    public Restaurant updateRestaurant(Long id, Restaurant restaurant) {

        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        existing.setName(restaurant.getName());
        existing.setCrNo(restaurant.getCrNo());

        return restaurantRepository.save(existing);
    }

    public void deleteRestaurant(Long id) {

        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        restaurantRepository.delete(existing);
    }


}
