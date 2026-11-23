package ma.enset.tp2.billing;
import ma.enset.tp2.billing.api.BillDto;
import ma.enset.tp2.billing.client.CustomerClient;
import ma.enset.tp2.billing.client.InventoryClient;
import ma.enset.tp2.billing.domain.Bill;
import ma.enset.tp2.billing.repo.BillRepository;
import ma.enset.tp2.billing.service.BillingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class BillingServiceTest {
  private final BillRepository repo=mock(BillRepository.class);
  private final CustomerClient customers=mock(CustomerClient.class);
  private final InventoryClient products=mock(InventoryClient.class);
  private final BillingService service=new BillingService(repo,customers,products,"MAD",20);
  @BeforeEach void init(){
    when(customers.byId(1L)).thenReturn(new CustomerClient.CustomerView(1L,"Amina","amina@example.test"));
    when(products.byId("p1")).thenReturn(new InventoryClient.ProductView("p1","Keyboard",new BigDecimal("200.00"),10));
    when(repo.saveAndFlush(any(Bill.class))).thenAnswer(inv->inv.getArgument(0));
  }
  @Test void computesInvoiceFromFeignProducts(){
    var bill=service.issue(new BillDto.CreateRequest(1L,List.of(new BillDto.ItemRequest("p1",3))));
    assertEquals(0,new BigDecimal("600.00").compareTo(bill.total()));
    assertEquals("Amina",bill.customerName());
    assertEquals(1,bill.items().size());
  }
  @Test void rejectsInsufficientStock(){
    assertThrows(ResponseStatusException.class,()->service.issue(new BillDto.CreateRequest(1L,List.of(new BillDto.ItemRequest("p1",11)))));
    verify(repo,never()).saveAndFlush(any());
  }
}
