package ma.enset.tp2.billing.domain;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity
@Table(name="bill_items")
public class BillItem {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="bill_id") private Bill bill;
  @Column(nullable=false) private String productId;
  @Column(nullable=false) private String productName;
  @Column(nullable=false) private int quantity;
  @Column(nullable=false,precision=14,scale=2) private BigDecimal unitPrice;
  @Column(nullable=false,precision=14,scale=2) private BigDecimal lineTotal;
  protected BillItem(){}
  public BillItem(String productId,String productName,int quantity,BigDecimal unitPrice){
    this.productId=productId;this.productName=productName;this.quantity=quantity;this.unitPrice=unitPrice;
    this.lineTotal=unitPrice.multiply(BigDecimal.valueOf(quantity));
  }
  void attach(Bill bill){this.bill=bill;}
  public Long getId(){return id;}
  public String getProductId(){return productId;}
  public String getProductName(){return productName;}
  public int getQuantity(){return quantity;}
  public BigDecimal getUnitPrice(){return unitPrice;}
  public BigDecimal getLineTotal(){return lineTotal;}
}
