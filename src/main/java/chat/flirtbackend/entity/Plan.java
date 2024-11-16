package chat.flirtbackend.entity;

import chat.flirtbackend.dto.Nameable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "plan")
public class Plan implements Nameable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long textMessageLimit;
    private Long imageMessageLimit;
}
