package chat.flirtbackend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "payment")
public class MyPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String paymentId;
    private String payerId;
    private String token;
    private Double total;
    private Long sourceId;
    private Long typeId;
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Object data;
    @Column(updatable = false)
    private Date dtCreate;
    private Date dtConfirmed;
}
