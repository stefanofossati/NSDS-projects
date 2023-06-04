package NSDSprojects.OrderService.Model;

import NSDSprojects.Common.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        boolean areEnoughItemsInStorage = orderService.checkItemsAvailability(order);
        if (areEnoughItemsInStorage) {
            orderService.createOrder(new Order(order.getName(), order.getItems()));
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
}
