package ma.enset.tp2.customer.api;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import ma.enset.tp2.customer.domain.Customer;
import ma.enset.tp2.customer.repo.CustomerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
  private final CustomerRepository repo;
  public CustomerController(CustomerRepository repo){this.repo=repo;}
  public record CustomerInput(@NotBlank String name,@NotBlank @Email String email){}
  @GetMapping public List<Customer> all(){return repo.findAll();}
  @GetMapping("/{id}") public Customer one(@PathVariable Long id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer not found"));}
  @PostMapping @ResponseStatus(HttpStatus.CREATED)
  public Customer create(@Valid @RequestBody CustomerInput input){
    if(repo.findByEmailIgnoreCase(input.email().trim()).isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");
    return repo.save(new Customer(input.name().trim(),input.email().trim()));
  }
  @PutMapping("/{id}") public Customer update(@PathVariable Long id,@Valid @RequestBody CustomerInput input){
    Customer c=one(id);
    repo.findByEmailIgnoreCase(input.email().trim()).filter(other->!other.getId().equals(id))
        .ifPresent(other->{throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");});
    c.setName(input.name().trim());c.setEmail(input.email().trim()); return repo.save(c);
  }
  @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id){if(!repo.existsById(id))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer not found");repo.deleteById(id);}
}
