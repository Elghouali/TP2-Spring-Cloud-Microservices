package ma.enset.tp2.billing.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.math.BigDecimal;
@FeignClient(name="inventory-service",path="/api/products")
public interface InventoryClient {
  record ProductView(String id,String name,BigDecimal price,int quantity){}
  @GetMapping("/{id}") ProductView byId(@PathVariable("id") String id);
}
