package co.com.crediya.r2dbc.entities;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("state")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StateEntity {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
}
