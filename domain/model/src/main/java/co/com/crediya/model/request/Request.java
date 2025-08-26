package co.com.crediya.model.request;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Request {
    String id;
    Double amount;
    Integer period;
    String email;
    Long idState;
    Long idLoanType;
}
