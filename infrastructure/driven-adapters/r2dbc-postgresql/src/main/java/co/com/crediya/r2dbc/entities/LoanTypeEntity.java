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
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "minimumAmount")
    private Double minimumAmount;
    @Column(name = "maximumAmount")
    private Double maximumAmount;
    @Column(name = "interestRate")
    private Double interestRate;
    @Column(name = "automaticValidation")
    private Boolean automaticValidation;
}
