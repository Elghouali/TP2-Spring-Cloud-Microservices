package ma.enset.tp2.billing.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name="customer-service",path="/api/customers")
public interface CustomerClient {
  record CustomerView(Long id,String name,String email){}
  @GetMapping("/{id}") CustomerView byId(@PathVariable("id") Long id);
}
