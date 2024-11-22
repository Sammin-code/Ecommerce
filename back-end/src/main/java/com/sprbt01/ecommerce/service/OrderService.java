package com.sprbt01.ecommerce.service;

import com.sprbt01.ecommerce.model.*;
import com.sprbt01.ecommerce.dto.OrderRequest;
import com.sprbt01.ecommerce.exception.ResourceNotFoundException;
import com.sprbt01.ecommerce.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Class Name: OrderService
 * Package: com.sprbt01.ecommerce.service
 * Description:提供訂單相關的服務方法，包括創建訂單、計算總金額、根據用戶ID查詢訂單。
 * author:
 * Create: 2024/9/4
 * Version: 1.0
 */
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    /**
     * 根據訂單ID查找訂單
     * @param orderId 訂單ID
     * @return 對應的訂單，若不存在則返回 null
     */
    public Order getOrderById(Long orderId){
        return orderRepository.findById(orderId).orElse(null);
    }

    /**
     * 根據用戶ID獲取該用戶的所有訂單
     * @param userId 用戶ID
     * @return 該用戶的訂單列表
     */
    public List<Order> getOrdersByUserId(Long userId) {
        User user = userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        return orderRepository.findByUser(user);
    }
    /**
     * 計算購物車商品的總金額
     * @param cartItems 購物車中的商品列表
     * @return 總金額
     */
    private BigDecimal calculateTotalAmount(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(item -> BigDecimal.valueOf(item.getProduct().getPrice())
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 創建訂單，包含檢查庫存、計算總金額、創建訂單項目和更新庫存
     * @param userId 用戶ID
     * @param productId 商品ID
     */
    public void createOrder(Long userId, Long productId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if (!optionalCart.isPresent()) {
            throw new RuntimeException("The cart isn't found.");
        }

        Cart cart = optionalCart.get();

        Optional<CartItem> optionalItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (!optionalItem.isPresent()) {
            throw new RuntimeException("Can't create order. Item isn't in cart.");
        }

        CartItem cartItem = optionalItem.get();

        Order order = new Order();
        User user = userService.getUserById(userId);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        BigDecimal quantity = BigDecimal.valueOf(cartItem.getQuantity());
        BigDecimal price = BigDecimal.valueOf(cartItem.getProduct().getPrice());


        order.setTotalAmount(quantity.multiply(price));

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(price);
        orderItem.setOrder(order);
        System.out.println("Order date: " + order.getOrderDate());
        orderRepository.save(order);
        orderItemRepository.save(orderItem);
        Product product = cartItem.getProduct();
        int availableQuantity = product.getQuantity();

        if (availableQuantity < cartItem.getQuantity()) {
            throw new RuntimeException("Not enough stock available.");
        }

        product.setQuantity(availableQuantity - cartItem.getQuantity());
        productRepository.save(product);

        cartService.removeProductFromCart(userId, productId);
    }
}


