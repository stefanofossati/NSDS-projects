package NSDSprojects.OrderService.Model;

import NSDSprojects.Common.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest order) {
        boolean areEnoughItemsInStorage = orderService.checkItemsAvailability(order);
        if (areEnoughItemsInStorage) {
            orderService.createOrder(new Order(order.getName(), order.getItems()));
            System.out.println("Order created");
            return ResponseEntity.ok("Order created");
        } else {
            return ResponseEntity.badRequest().body("Not enough items in storage available to fulfill your order");
        }

    }

    @PostMapping("/availability")
    public ResponseEntity<String> modifyAvailability(@RequestBody AvailabilityRequest availabilityRequest) {
        boolean itemAlreadyExisting = orderService.modifyAvailability(availabilityRequest);
        if(itemAlreadyExisting) {
            return ResponseEntity.ok("Item's availability updated");
        } else {
            return ResponseEntity.ok("New item with indicated availability inserted");
        }
    }
}
