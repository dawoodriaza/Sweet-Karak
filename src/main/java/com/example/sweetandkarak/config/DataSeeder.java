package com.example.sweetandkarak.config;

import com.example.sweetandkarak.enums.CafeStatusEnum;
import com.example.sweetandkarak.enums.OrderStatusEnum;
import com.example.sweetandkarak.enums.RoleEnum;
import com.example.sweetandkarak.model.Cafe;
import com.example.sweetandkarak.model.CafeReview;
import com.example.sweetandkarak.model.Cart;
import com.example.sweetandkarak.model.Item;
import com.example.sweetandkarak.model.ItemReview;
import com.example.sweetandkarak.model.Order;
import com.example.sweetandkarak.model.User;
import com.example.sweetandkarak.repository.CafeRepository;
import com.example.sweetandkarak.repository.CafeReviewRepository;
import com.example.sweetandkarak.repository.CartRepository;
import com.example.sweetandkarak.repository.ItemRepository;
import com.example.sweetandkarak.repository.ItemReviewRepository;
import com.example.sweetandkarak.repository.OrderRepository;
import com.example.sweetandkarak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ItemReviewRepository itemReviewRepository;
    private final CafeReviewRepository cafeReviewRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Database already seeded. Skipping.");
            return;
        }

        log.info("Seeding database...");

        User admin = User.builder()
                .fullName("System Admin")
                .email("admin@sweetkarak.com")
                .password(passwordEncoder.encode("admin123"))
                .phoneNumber("39000000")
                .role(RoleEnum.SYSTEM_ADMIN)
                .build();
        userRepository.save(admin);

        User cafeAdmin1 = User.builder()
                .fullName("Mohammed Hassan")
                .email("mohammed@sweetkarak.com")
                .password(passwordEncoder.encode("123456"))
                .phoneNumber("39001111")
                .role(RoleEnum.CAFE_ADMIN)
                .build();
        userRepository.save(cafeAdmin1);

        User cafeAdmin2 = User.builder()
                .fullName("Sara Ahmed")
                .email("sara@sweetkarak.com")
                .password(passwordEncoder.encode("123456"))
                .phoneNumber("39002222")
                .role(RoleEnum.CAFE_ADMIN)
                .build();
        userRepository.save(cafeAdmin2);

        User cafeAdmin3 = User.builder()
                .fullName("Khalid Omar")
                .email("khalid@sweetkarak.com")
                .password(passwordEncoder.encode("123456"))
                .phoneNumber("39003333")
                .role(RoleEnum.CAFE_ADMIN)
                .build();
        userRepository.save(cafeAdmin3);

        User customer1 = User.builder()
                .fullName("Ahmad Ali")
                .email("ahmad@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .phoneNumber("39004444")
                .role(RoleEnum.CUSTOMER)
                .build();
        userRepository.save(customer1);

        User customer2 = User.builder()
                .fullName("Fatima Khalid")
                .email("fatima@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .phoneNumber("39005555")
                .role(RoleEnum.CUSTOMER)
                .build();
        userRepository.save(customer2);

        User customer3 = User.builder()
                .fullName("Yusuf Nasser")
                .email("yusuf@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .phoneNumber("39006666")
                .role(RoleEnum.CUSTOMER)
                .build();
        userRepository.save(customer3);

        User inactiveUser = User.builder()
                .fullName("Deleted Account")
                .email("inactive@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .phoneNumber("39007777")
                .role(RoleEnum.CUSTOMER)
                .build();
        inactiveUser.setIsActive(0);
        userRepository.save(inactiveUser);

        Cafe cafeApproved1 = Cafe.builder()
                .cafeName("Karak House")
                .location("Manama, Bahrain")
                .ratingOutOf5Star(4.5)
                .cafeStatus(CafeStatusEnum.APPROVED)
                .cafeAdmin(cafeAdmin1)
                .build();
        cafeRepository.save(cafeApproved1);

        Cafe cafeApproved2 = Cafe.builder()
                .cafeName("Bait Al Karak")
                .location("Muharraq, Bahrain")
                .ratingOutOf5Star(4.2)
                .cafeStatus(CafeStatusEnum.APPROVED)
                .cafeAdmin(cafeAdmin2)
                .build();
        cafeRepository.save(cafeApproved2);

        Cafe cafePending1 = Cafe.builder()
                .cafeName("Golden Karak")
                .location("Riffa, Bahrain")
                .ratingOutOf5Star(0.0)
                .cafeStatus(CafeStatusEnum.PENDING_APPROVAL)
                .cafeAdmin(cafeAdmin1)
                .build();
        cafeRepository.save(cafePending1);

        Cafe cafePending2 = Cafe.builder()
                .cafeName("Saffron Cup")
                .location("Isa Town, Bahrain")
                .ratingOutOf5Star(0.0)
                .cafeStatus(CafeStatusEnum.PENDING_APPROVAL)
                .cafeAdmin(cafeAdmin3)
                .build();
        cafeRepository.save(cafePending2);

        Cafe cafeRejected = Cafe.builder()
                .cafeName("Old Karak Corner")
                .location("Hamad Town, Bahrain")
                .ratingOutOf5Star(0.0)
                .cafeStatus(CafeStatusEnum.REJECTED)
                .cafeAdmin(cafeAdmin3)
                .build();
        cafeRepository.save(cafeRejected);

        Cafe cafeInactive = Cafe.builder()
                .cafeName("Closed Karak")
                .location("Sitra, Bahrain")
                .ratingOutOf5Star(3.0)
                .cafeStatus(CafeStatusEnum.INACTIVE)
                .cafeAdmin(cafeAdmin2)
                .build();
        cafeInactive.setIsActive(0);
        cafeRepository.save(cafeInactive);

        Item item1 = Item.builder()
                .itemName("Classic Karak Tea")
                .itemDescription("Traditional sweet spiced karak tea")
                .price(new BigDecimal("0.500"))
                .quantityAvailable(200)
                .cafe(cafeApproved1)
                .build();
        itemRepository.save(item1);

        Item item2 = Item.builder()
                .itemName("Saffron Karak")
                .itemDescription("Karak tea with premium saffron")
                .price(new BigDecimal("0.800"))
                .quantityAvailable(100)
                .cafe(cafeApproved1)
                .build();
        itemRepository.save(item2);

        Item item3 = Item.builder()
                .itemName("Karak Latte")
                .itemDescription("Creamy karak with milk foam")
                .price(new BigDecimal("1.000"))
                .quantityAvailable(150)
                .cafe(cafeApproved1)
                .build();
        itemRepository.save(item3);

        Item item4 = Item.builder()
                .itemName("Karak Cappuccino")
                .itemDescription("Espresso style karak")
                .price(new BigDecimal("1.200"))
                .quantityAvailable(0)
                .cafe(cafeApproved1)
                .build();
        item4.setIsActive(0);
        itemRepository.save(item4);

        Item item5 = Item.builder()
                .itemName("Cardamom Karak")
                .itemDescription("Karak with extra cardamom")
                .price(new BigDecimal("0.600"))
                .quantityAvailable(180)
                .cafe(cafeApproved2)
                .build();
        itemRepository.save(item5);

        Item item6 = Item.builder()
                .itemName("Rose Karak")
                .itemDescription("Karak with rose water")
                .price(new BigDecimal("0.700"))
                .quantityAvailable(120)
                .cafe(cafeApproved2)
                .build();
        itemRepository.save(item6);

        Item item7 = Item.builder()
                .itemName("Mint Karak")
                .itemDescription("Karak with fresh mint")
                .price(new BigDecimal("0.650"))
                .quantityAvailable(90)
                .cafe(cafeApproved2)
                .build();
        itemRepository.save(item7);

        Item item8 = Item.builder()
                .itemName("Ginger Karak")
                .itemDescription("Karak with fresh ginger")
                .price(new BigDecimal("0.750"))
                .quantityAvailable(0)
                .cafe(cafeApproved2)
                .build();
        item8.setIsActive(0);
        itemRepository.save(item8);

        Order orderDelivered1 = Order.builder()
                .user(customer1)
                .item(item1)
                .cafe(cafeApproved1)
                .orderQuantity(2)
                .totalOrderPrice(new BigDecimal("1.000"))
                .paymentReference("PAY-001")
                .orderStatus(OrderStatusEnum.DELIVERED)
                .build();
        orderRepository.save(orderDelivered1);

        Order orderDelivered2 = Order.builder()
                .user(customer2)
                .item(item3)
                .cafe(cafeApproved1)
                .orderQuantity(1)
                .totalOrderPrice(new BigDecimal("1.000"))
                .paymentReference("PAY-002")
                .orderStatus(OrderStatusEnum.DELIVERED)
                .build();
        orderRepository.save(orderDelivered2);

        Order orderPaid = Order.builder()
                .user(customer3)
                .item(item5)
                .cafe(cafeApproved2)
                .orderQuantity(3)
                .totalOrderPrice(new BigDecimal("1.800"))
                .paymentReference("PAY-003")
                .orderStatus(OrderStatusEnum.PAID)
                .build();
        orderRepository.save(orderPaid);

        Order orderPreparing = Order.builder()
                .user(customer1)
                .item(item2)
                .cafe(cafeApproved1)
                .orderQuantity(1)
                .totalOrderPrice(new BigDecimal("0.800"))
                .paymentReference("PAY-004")
                .orderStatus(OrderStatusEnum.PREPARING)
                .build();
        orderRepository.save(orderPreparing);

        Order orderPending = Order.builder()
                .user(customer2)
                .item(item6)
                .cafe(cafeApproved2)
                .orderQuantity(2)
                .totalOrderPrice(new BigDecimal("1.400"))
                .paymentReference("PAY-005")
                .orderStatus(OrderStatusEnum.PENDING)
                .build();
        orderRepository.save(orderPending);

        Order orderCancelled = Order.builder()
                .user(customer3)
                .item(item7)
                .cafe(cafeApproved2)
                .orderQuantity(1)
                .totalOrderPrice(new BigDecimal("0.650"))
                .paymentReference("PAY-006")
                .orderStatus(OrderStatusEnum.CANCELLED)
                .build();
        orderRepository.save(orderCancelled);

        Order orderFailedPayment = Order.builder()
                .user(customer1)
                .item(item1)
                .cafe(cafeApproved1)
                .orderQuantity(1)
                .totalOrderPrice(new BigDecimal("0.500"))
                .paymentReference(null)
                .orderStatus(OrderStatusEnum.FAILED_PAYMENT)
                .build();
        orderRepository.save(orderFailedPayment);

        Cart cart1 = Cart.builder()
                .user(customer1)
                .item(item3)
                .cafe(cafeApproved1)
                .quantity(2)
                .totalPrice(new BigDecimal("2.000"))
                .build();
        cartRepository.save(cart1);

        Cart cart2 = Cart.builder()
                .user(customer1)
                .item(item2)
                .cafe(cafeApproved1)
                .quantity(1)
                .totalPrice(new BigDecimal("0.800"))
                .build();
        cartRepository.save(cart2);

        Cart cart3 = Cart.builder()
                .user(customer2)
                .item(item5)
                .cafe(cafeApproved2)
                .quantity(3)
                .totalPrice(new BigDecimal("1.800"))
                .build();
        cartRepository.save(cart3);

        Cart cart4 = Cart.builder()
                .user(customer3)
                .item(item6)
                .cafe(cafeApproved2)
                .quantity(1)
                .totalPrice(new BigDecimal("0.700"))
                .build();
        cartRepository.save(cart4);

        ItemReview ir1 = ItemReview.builder()
                .user(customer1)
                .item(item1)
                .cafe(cafeApproved1)
                .rating(5)
                .reviewDescription("Best karak in Bahrain, highly recommend!")
                .build();
        itemReviewRepository.save(ir1);

        ItemReview ir2 = ItemReview.builder()
                .user(customer2)
                .item(item1)
                .cafe(cafeApproved1)
                .rating(4)
                .reviewDescription("Really good, will order again")
                .build();
        itemReviewRepository.save(ir2);

        ItemReview ir3 = ItemReview.builder()
                .user(customer3)
                .item(item1)
                .cafe(cafeApproved1)
                .rating(3)
                .reviewDescription("Good but could be sweeter")
                .build();
        itemReviewRepository.save(ir3);

        ItemReview ir4 = ItemReview.builder()
                .user(customer1)
                .item(item2)
                .cafe(cafeApproved1)
                .rating(5)
                .reviewDescription("Saffron makes it special")
                .build();
        itemReviewRepository.save(ir4);

        ItemReview ir5 = ItemReview.builder()
                .user(customer2)
                .item(item5)
                .cafe(cafeApproved2)
                .rating(4)
                .reviewDescription("Great cardamom flavor")
                .build();
        itemReviewRepository.save(ir5);

        ItemReview ir6 = ItemReview.builder()
                .user(customer3)
                .item(item6)
                .cafe(cafeApproved2)
                .rating(2)
                .reviewDescription("Rose flavor too strong for me")
                .build();
        itemReviewRepository.save(ir6);

        CafeReview cr1 = CafeReview.builder()
                .user(customer1)
                .cafe(cafeApproved1)
                .rating(5)
                .reviewDescription("Amazing place, best karak in Bahrain")
                .build();
        cafeReviewRepository.save(cr1);

        CafeReview cr2 = CafeReview.builder()
                .user(customer2)
                .cafe(cafeApproved1)
                .rating(4)
                .reviewDescription("Nice atmosphere and fast service")
                .build();
        cafeReviewRepository.save(cr2);

        CafeReview cr3 = CafeReview.builder()
                .user(customer3)
                .cafe(cafeApproved1)
                .rating(5)
                .reviewDescription("My go-to karak spot")
                .build();
        cafeReviewRepository.save(cr3);

        CafeReview cr4 = CafeReview.builder()
                .user(customer1)
                .cafe(cafeApproved2)
                .rating(4)
                .reviewDescription("Good variety of karak options")
                .build();
        cafeReviewRepository.save(cr4);

        CafeReview cr5 = CafeReview.builder()
                .user(customer2)
                .cafe(cafeApproved2)
                .rating(3)
                .reviewDescription("Decent but not the best")
                .build();
        cafeReviewRepository.save(cr5);

        CafeReview cr6 = CafeReview.builder()
                .user(customer3)
                .cafe(cafeApproved2)
                .rating(4)
                .reviewDescription("Fresh ingredients, good quality")
                .build();
        cafeReviewRepository.save(cr6);

        log.info("Database seeded successfully.");

        log.info("SYSTEM_ADMIN  -> admin@sweetkarak.com     / admin123");
        log.info("CAFE_ADMIN 1  -> mohammed@sweetkarak.com  / 123456");
        log.info("CAFE_ADMIN 2  -> sara@sweetkarak.com      / 123456");
        log.info("CAFE_ADMIN 3  -> khalid@sweetkarak.com    / 123456");
        log.info("CUSTOMER 1    -> ahmad@gmail.com           / 123456");
        log.info("CUSTOMER 2    -> fatima@gmail.com          / 123456");
        log.info("CUSTOMER 3    -> yusuf@gmail.com           / 123456");
        log.info("INACTIVE USER -> inactive@gmail.com        / 123456");

        log.info("CAFES: 2 APPROVED, 2 PENDING_APPROVAL, 1 REJECTED, 1 INACTIVE");
        log.info("ITEMS: 6 active, 2 inactive (out of stock)");
        log.info("ORDERS: DELIVERED x2, PAID, PREPARING, PENDING, CANCELLED, FAILED_PAYMENT");
        log.info("CART: 4 cart entries across 3 customers");
        log.info("REVIEWS: 6 item reviews, 6 cafe reviews");

    }
}