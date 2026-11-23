package ma.enset.tp2.inventory.repo;
import ma.enset.tp2.inventory.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product,String>{}
