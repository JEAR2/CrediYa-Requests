package co.com.crediya.model.user;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    String id;
    String name;
    String lastName;
    Date birthDate;
    String address;
    String email;
    String identityDocument;
    String phoneNumber;
    Integer roleId;
    BigDecimal baseSalary;
}
