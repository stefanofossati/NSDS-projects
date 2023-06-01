package NSDSprojects.ShippingService.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingService {
    private final ShippingConsumer shippingConsumer;

    @Autowired
    public ShippingService(ShippingConsumer shippingConsumer){
        this.shippingConsumer = shippingConsumer;
    }

}
