package co.com.crediya.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("requests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RequestsEntity {
    @Id
    @Column("request_id")
    private Long id;
    private Double amount;
    private Integer period;
    private String email;
    private Long idState;
    private Long idLoanType;
}
