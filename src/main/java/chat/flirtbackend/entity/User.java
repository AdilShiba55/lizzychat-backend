package chat.flirtbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(name = "usr")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Column(updatable = false)
    private String email;
    private String password;
    private String avatarPath;
    @Column(updatable = false)
    private Date dtCreate;
    private Date dtVerified;
    private Date dtBlocked;
    private Long sourceId;
}
