package com.example.Orders.Microservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Orders.Microservice.entity.BookDTO;
import com.example.Orders.Microservice.entity.Order;
import com.example.Orders.Microservice.repo.OrderRepository;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${books.service.url}")
    private String booksServiceUrl;

    public Order placeOrder(Long bookId, int quantity) {
        // Fetch book details from Books Microservice
        BookDTO book = fetchBookDetails(bookId);
        if (book == null) {
            throw new RuntimeException("Book not found");
        }

        // Calculate total price
        double totalPrice = book.getPrice() * quantity;

        // Save order
        Order order = new Order();
        order.setBookId(bookId);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        return orderRepository.save(order);
    }

    private BookDTO fetchBookDetails(Long bookId) {
        String url = String.format("%s/%d", booksServiceUrl, bookId);
        try {
            ResponseEntity<BookDTO> response = restTemplate.getForEntity(url, BookDTO.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                logger.error("Failed to fetch book details. Status code: {}", response.getStatusCode());
                return null;
            }
        } catch (Exception e) {
            logger.error("Exception occurred while fetching book details", e);
            return null;
        }
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
