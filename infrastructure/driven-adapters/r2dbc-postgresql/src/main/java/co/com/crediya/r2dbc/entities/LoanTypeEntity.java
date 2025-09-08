package co.com.crediya.r2dbc.entities;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("loan_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanTypeEntity {
    @Id
    private Long id;
    private String name;
    private String code;
    @Column(name = "minimum_amount")
    private Double minimumAmount;
    @Column(name = "maximum_amount")
    private Double maximumAmount;
    @Column(name = "interest_rate")
    private Double interestRate;
    @Column(name = "automatic_validation")
    private Boolean automaticValidation;
}
