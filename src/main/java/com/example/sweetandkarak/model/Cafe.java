package com.example.sweetandkarak.model;


import com.example.sweetandkarak.enums.CafeStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name= "cafe")
public class Cafe {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cafe_name", nullable = false)
    private String cafeName;

    @Column(name = "cafe_image")
    private String cafeImage;

    @Column(name = "location")
    private String location;

    @Column(name = "rating_out_of_5_star")
    private Double ratingOutOf5Star = 0.0;
    @Enumerated(EnumType.STRING)
    @Column(name = "cafe_status", nullable = false)
    private CafeStatusEnum cafeStatus = CafeStatusEnum.PENDING_APPROVAL;

    @CreationTimestamp
    @Column(name="created_on", updatable = false)
    private LocalDateTime createdOn;


    @UpdateTimestamp
    @Column(name = "updated_on")
    private LocalDateTime updatedOn;


    @Column(name = "is_active")
    private  int  isActive = 1;


    public void setImageUrl(@Nullable String originalFilename) {
    }

    public void setActive(boolean b) {
    }
}
