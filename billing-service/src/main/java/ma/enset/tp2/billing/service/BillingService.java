package ma.enset.tp2.billing.service;
import feign.FeignException;
import ma.enset.tp2.billing.client.CustomerClient;
import ma.enset.tp2.billing.client.InventoryClient;
import ma.enset.tp2.billing.api.BillDto;
import ma.enset.tp2.billing.domain.Bill;
import ma.enset.tp2.billing.domain.BillItem;
import ma.enset.tp2.billing.repo.BillRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;
@Service
public class BillingService {
  private final BillRepository repo;
  private final CustomerClient customers;
  private final InventoryClient inventory;
  private final String currency;
  private final int maxItems;
  public BillingService(BillRepository repo,CustomerClient customers,InventoryClient inventory,
                        @Value("${app.billing.currency:MAD}") String currency,
                        @Value("${app.billing.max-items:20}") int maxItems){
    this.repo=repo;this.customers=customers;this.inventory=inventory;this.currency=currency;this.maxItems=maxItems;
  }
  @Transactional
  public BillDto.Response issue(BillDto.CreateRequest req){
    if(req.items().size()>maxItems)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Too many line items");
    CustomerClient.CustomerView customer;
    try{customer=customers.byId(req.customerId());}
    catch(FeignException.NotFound ex){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unknown customer");}
    if(customer==null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unknown customer");
    Bill bill=new Bill(customer.id(),customer.name(),customer.email(),currency);
    for(BillDto.ItemRequest line:req.items()){
      InventoryClient.ProductView p;
      try{p=inventory.byId(line.productId());}
      catch(FeignException.NotFound ex){throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unknown product: "+line.productId());}
      if(p==null || p.price()==null)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Unknown product: "+line.productId());
      if(line.quantity()>p.quantity())throw new ResponseStatusException(HttpStatus.CONFLICT,"Insufficient demo stock for "+p.name());
      bill.addItem(new BillItem(p.id(),p.name(),line.quantity(),p.price()));
    }
    return BillDto.Response.of(repo.saveAndFlush(bill));
  }
  @Transactional(readOnly=true)
  public BillDto.Response one(Long id){return BillDto.Response.of(repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Bill not found")));}
  @Transactional(readOnly=true)
  public List<BillDto.Response> all(){return repo.findAll().stream().map(BillDto.Response::of).toList();}
  @Transactional(readOnly=true)
  public List<BillDto.Response> forCustomer(Long id){return repo.findByCustomerIdOrderByIssuedAtDesc(id).stream().map(BillDto.Response::of).toList();}
}
