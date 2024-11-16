package chat.flirtbackend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "usr_purchase")
public class UserPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long typeId;
    private Long userId;
    private Long characterId;
    private Long cost;
    @Column(updatable = false)
    private Date dtCreate;


}
