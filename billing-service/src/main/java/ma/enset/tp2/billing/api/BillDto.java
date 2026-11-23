package ma.enset.tp2.billing.api;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import ma.enset.tp2.billing.domain.Bill;
import ma.enset.tp2.billing.domain.BillItem;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
public final class BillDto {
  private BillDto(){}
  public record ItemRequest(@NotBlank String productId,@Min(1) int quantity){}
  public record CreateRequest(@NotNull Long customerId,@NotEmpty @Valid List<ItemRequest> items){}
  public record ItemResponse(Long id,String productId,String productName,int quantity,BigDecimal unitPrice,BigDecimal lineTotal){
    public static ItemResponse of(BillItem i){return new ItemResponse(i.getId(),i.getProductId(),i.getProductName(),i.getQuantity(),i.getUnitPrice(),i.getLineTotal());}
  }
  public record Response(Long id,Long customerId,String customerName,String customerEmail,Instant issuedAt,
                         BigDecimal total,String currency,List<ItemResponse> items){
    public static Response of(Bill b){return new Response(b.getId(),b.getCustomerId(),b.getCustomerName(),b.getCustomerEmail(),
      b.getIssuedAt(),b.getTotal(),b.getCurrency(),b.getItems().stream().map(ItemResponse::of).toList());}
  }
}
