package chat.flirtbackend.entity;

import chat.flirtbackend.dto.Archiveable;
import chat.flirtbackend.dto.Nameable;
import chat.flirtbackend.util.UtCharacterImageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "occupation")
public class Occupation implements Nameable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long routineClothesId;
    private Long routineLocationId;
    private Long underwearLocationId;
    private Long nakedLocationId;
    @Column(updatable = false)
    private Date dtCreate;
    private Date dtArchive;

    public Long getLocationIdByImageTypeId(Long imageTypeId) {
        if(imageTypeId.equals(UtCharacterImageType.ROUTINE)) {
            return routineLocationId;
        } else if(imageTypeId.equals(UtCharacterImageType.UNDERWEAR)) {
            return underwearLocationId;
        } else if(imageTypeId.equals(UtCharacterImageType.NAKED)) {
            return nakedLocationId;
        }
        return null;
    }
}
