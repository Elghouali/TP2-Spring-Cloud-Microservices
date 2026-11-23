package ma.enset.tp2.customer.repo;
import ma.enset.tp2.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface CustomerRepository extends JpaRepository<Customer,Long>{
  Optional<Customer> findByEmailIgnoreCase(String email);
}
