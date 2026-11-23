package ma.enset.tp2.billing.api;
import jakarta.validation.Valid;
import ma.enset.tp2.billing.service.BillingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/bills")
public class BillController {
  private final BillingService service;
  public BillController(BillingService service){this.service=service;}
  @GetMapping public List<BillDto.Response> all(@RequestParam(required=false) Long customerId){
    return customerId==null?service.all():service.forCustomer(customerId);
  }
  @GetMapping("/{id}") public BillDto.Response one(@PathVariable Long id){return service.one(id);}
  @PostMapping @ResponseStatus(HttpStatus.CREATED)
  public BillDto.Response create(@Valid @RequestBody BillDto.CreateRequest request){return service.issue(request);}
}
