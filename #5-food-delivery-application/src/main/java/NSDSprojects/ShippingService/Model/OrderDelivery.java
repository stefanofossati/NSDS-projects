package NSDSprojects.ShippingService.Model;

import java.util.Map;

public class OrderDelivery {

    private Long orderId;
    private String name;

    private String address;
    private Map<String, Integer> items;

    public OrderDelivery() {
    }

    public OrderDelivery(Long orderId, String name, String address, Map<String, Integer> items) {
        this.orderId = orderId;
        this.name = name;
        this.address = address;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Map<String, Integer> getItems() {
        return items;
    }
}
