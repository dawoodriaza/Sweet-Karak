package com.example.sweetandkarak.model;

import com.example.sweetandkarak.enums.CafeStatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cafes")
public class Cafe extends BaseEntity {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_admin_id", nullable = false)
    private User cafeAdmin;

    @OneToMany(mappedBy = "cafe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "cafe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "cafe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemReview> itemReviews = new ArrayList<>();

    @OneToMany(mappedBy = "cafe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CafeReview> cafeReviews = new ArrayList<>();
}
