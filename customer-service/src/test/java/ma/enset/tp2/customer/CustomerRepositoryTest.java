package ma.enset.tp2.customer;
import ma.enset.tp2.customer.domain.Customer;
import ma.enset.tp2.customer.repo.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest(properties={"eureka.client.enabled=false","spring.cloud.config.enabled=false"})
class CustomerRepositoryTest {
  @Autowired CustomerRepository repository;
  @Test void persistsAndFindsCustomerByEmail(){
    Customer saved=repository.save(new Customer("Test User","test@example.test"));
    assertThat(saved.getId()).isNotNull();
    assertThat(repository.findByEmailIgnoreCase("TEST@EXAMPLE.TEST")).isPresent();
  }
}
