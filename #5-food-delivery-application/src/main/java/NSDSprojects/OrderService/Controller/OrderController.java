package NSDSprojects.OrderService.Controller;

import NSDSprojects.Common.Item;
import NSDSprojects.Common.OrderEntity;
import NSDSprojects.OrderService.Model.AvailabilityRequest;
import NSDSprojects.OrderService.Model.OrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest order) {
        Marker marker = null;
        boolean areEnoughItemsInStorage = orderService.checkItemsAvailability(order); //TODO manage Error

        order.getItems().forEach((key, value) -> {
            logger.debug("key: " + key + " value: " + value);
        });
        if (areEnoughItemsInStorage) {
            orderService.createOrder(new OrderEntity(order.getName(), order.getItems()));
            logger.debug("Order created");
            return ResponseEntity.ok("Order created");
        } else {
            logger.debug("Not enough items in storage available to fulfill your order");
            return ResponseEntity.badRequest().body("Not enough items in storage available to fulfill your order");
        }

    }

    @PostMapping("/availability")
    public ResponseEntity<String> modifyAvailability(@RequestBody AvailabilityRequest availabilityRequest) {
        boolean itemAlreadyExisting = orderService.modifyAvailability(availabilityRequest);
        if(itemAlreadyExisting) {
            logger.debug("Item's availability updated");
            return ResponseEntity.ok("Item's availability updated");
        } else {
            logger.debug("New item with indicated availability inserted");
            return ResponseEntity.ok("New item with indicated availability inserted");
        }
    }

    @GetMapping("/availability")
    public ResponseEntity<ArrayList<Item>> getAvailability() {
        ArrayList<Item> availability = orderService.getAvailability();
        logger.debug("Availability retrieved");
        return ResponseEntity.ok(availability);
    }
}