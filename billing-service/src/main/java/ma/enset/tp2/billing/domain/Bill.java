package ma.enset.tp2.billing.domain;
import jakarta.persistence.*;
import java.time.Instant;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name="bills")
public class Bill {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private Long customerId;
  @Column(nullable=false) private String customerName;
  @Column(nullable=false) private String customerEmail;
  @Column(nullable=false) private Instant issuedAt;
  @Column(nullable=false,precision=14,scale=2) private BigDecimal total;
  @Column(nullable=false) private String currency;
  @OneToMany(mappedBy="bill",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.EAGER)
  private List<BillItem> items=new ArrayList<>();
  protected Bill(){}
  public Bill(Long customerId,String customerName,String customerEmail,String currency){
    this.customerId=customerId;this.customerName=customerName;this.customerEmail=customerEmail;
    this.currency=currency;this.issuedAt=Instant.now();this.total=BigDecimal.ZERO;
  }
  public void addItem(BillItem item){item.attach(this);items.add(item);total=total.add(item.getLineTotal());}
  public Long getId(){return id;}
  public Long getCustomerId(){return customerId;}
  public String getCustomerName(){return customerName;}
  public String getCustomerEmail(){return customerEmail;}
  public Instant getIssuedAt(){return issuedAt;}
  public BigDecimal getTotal(){return total;}
  public String getCurrency(){return currency;}
  public List<BillItem> getItems(){return items;}
}
