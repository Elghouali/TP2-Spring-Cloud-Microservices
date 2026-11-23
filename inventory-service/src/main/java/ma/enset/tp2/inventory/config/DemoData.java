package ma.enset.tp2.inventory.config;
import ma.enset.tp2.inventory.domain.Product;
import ma.enset.tp2.inventory.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
@Configuration
public class DemoData {
  @Bean CommandLineRunner sampleProducts(ProductRepository repo,@Value("${app.demo.seed:true}") boolean seed){
    return args->{if(seed && repo.count()==0){
      repo.save(new Product("Laptop Pro 14",new BigDecimal("7900.00"),22));
      repo.save(new Product("USB-C Dock",new BigDecimal("650.00"),65));
      repo.save(new Product("Wireless Mouse",new BigDecimal("230.00"),120));
    }};
  }
}
