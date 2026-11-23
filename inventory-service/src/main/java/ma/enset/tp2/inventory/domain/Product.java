package ma.enset.tp2.inventory.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
@Entity
@Table(name="products")
public class Product {
  @Id private String id;
  @Column(nullable=false) private String name;
  @Column(nullable=false,precision=14,scale=2) private BigDecimal price;
  @Column(nullable=false) private int quantity;
  protected Product() {}
  public Product(String name,BigDecimal price,int quantity){this.id=UUID.randomUUID().toString();this.name=name;this.price=price;this.quantity=quantity;}
  public String getId(){return id;}
  public String getName(){return name;}
  public BigDecimal getPrice(){return price;}
  public int getQuantity(){return quantity;}
  public void update(String name,BigDecimal price,int quantity){this.name=name;this.price=price;this.quantity=quantity;}
}
