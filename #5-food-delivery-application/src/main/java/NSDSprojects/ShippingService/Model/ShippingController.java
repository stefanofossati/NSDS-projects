package NSDSprojects.ShippingService.Model;

import NSDSprojects.Common.Kafka.OrderState;
import NSDSprojects.Common.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/shipping")
public class ShippingController {
    private ShippingService shippingService;

    @Autowired
    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @GetMapping("/check-order")
    public ResponseEntity<String> checkOrderStatus(@RequestParam Long orderId) {
        OrderState orderState = shippingService.getOrderStatus(orderId);
        if(orderState==null) {
            return ResponseEntity.badRequest().body("The order doesn't exist");
        } else {
            return ResponseEntity.ok("The order you requested is in state: " + orderState);
        }
    }

    @GetMapping("/check-order-state")
    public ResponseEntity<ArrayList<Order>> checkOrdersStatus(@RequestParam String username) {
        ArrayList<Order> orders = shippingService.getOrdersStatus(username);
        if(orders==null) {
            return ResponseEntity.badRequest().header("Header - Massage","No orders found for the user").body(null);
        } else {
            return ResponseEntity.ok(orders);
        }
    }

    @GetMapping("/orders-to-deliver")
    public ResponseEntity<ArrayList<OrderDelivery>> checkOrders() {
        ArrayList<OrderDelivery> orders = shippingService.getOrderToDeliver();
        if(orders==null) {
            return ResponseEntity.badRequest().header("Header - Massage","No orders found").body(null);
        } else {
            return ResponseEntity.ok(orders);
        }
    }

    @PostMapping("/notify-delivered")
    public ResponseEntity<String> notifySuccessfulDelivery(@RequestParam Long orderId) {
        boolean correctlyUpdated = shippingService.updateOrderStatus(orderId, OrderState.DELIVERED);
        if(correctlyUpdated) {
            return ResponseEntity.ok("Delivery correctly registered");
        } else {
            return ResponseEntity.badRequest().body("The order doesn't exist");
        }
    }
}
