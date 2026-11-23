package ma.enset.tp2.inventory;
import ma.enset.tp2.inventory.domain.Product;
import ma.enset.tp2.inventory.repo.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest(properties={"eureka.client.enabled=false","spring.cloud.config.enabled=false"})
class ProductRepositoryTest {
  @Autowired ProductRepository repository;
  @Test void persistsProduct(){
    Product saved=repository.save(new Product("Monitor",new BigDecimal("1800"),5));
    assertThat(repository.findById(saved.getId())).isPresent();
  }
}
