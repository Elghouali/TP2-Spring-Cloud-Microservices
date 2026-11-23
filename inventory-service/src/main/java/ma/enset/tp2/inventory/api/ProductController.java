package ma.enset.tp2.inventory.api;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import ma.enset.tp2.inventory.domain.Product;
import ma.enset.tp2.inventory.repo.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;
@RestController
@RequestMapping("/api/products")
public class ProductController {
  private final ProductRepository repo;
  public ProductController(ProductRepository repo){this.repo=repo;}
  public record ProductInput(@NotBlank String name,@NotNull @DecimalMin(value="0",inclusive=false) BigDecimal price,@Min(0) int quantity){}
  @GetMapping public List<Product> all(){return repo.findAll();}
  @GetMapping("/{id}") public Product one(@PathVariable String id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found"));}
  @PostMapping @ResponseStatus(HttpStatus.CREATED)
  public Product create(@Valid @RequestBody ProductInput input){return repo.save(new Product(input.name().trim(),input.price(),input.quantity()));}
  @PutMapping("/{id}") public Product update(@PathVariable String id,@Valid @RequestBody ProductInput input){
    Product p=one(id);p.update(input.name().trim(),input.price(),input.quantity());return repo.save(p);
  }
  @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable String id){if(!repo.existsById(id))throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Product not found");repo.deleteById(id);}
}
