package co.com.crediya.model.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AutoValidationPayload {
    private String requestId;          // id de la solicitud (puede ser string)
    private String applicantId;        // id del cliente (o email si usas email)
    private Double salary;             // salario base (para regla 5 salarios)
    private Double amount;             // monto del préstamo nuevo
    private Double annualRate;         // tasa anual (ej. 0.18)
    private Integer termMonths;        // periodo en meses
    private Double CurrentMonthlyDebt; // suma de cuotas mensuales aprobadas
    private String email;              // email del solicitante
    private String traceId;            // trace id para trazabilidad
}
