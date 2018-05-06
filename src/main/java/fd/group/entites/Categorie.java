package fd.group.entites;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Categorie implements Serializable {
    @Id
    @GeneratedValue
    private Long   id;
    @NotNull(message = "Le libelle est obligatoire !")
    private String libelle;
}
