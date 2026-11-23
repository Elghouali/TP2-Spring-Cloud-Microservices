package ma.enset.tp2.billing.repo;
import ma.enset.tp2.billing.domain.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BillRepository extends JpaRepository<Bill,Long>{List<Bill> findByCustomerIdOrderByIssuedAtDesc(Long customerId);}
