package fd.group.entites;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Client implements Serializable {
    @Id
    @GeneratedValue
    private Long   id;
    @NotNull(message = "Le nom est obligatoire")
    private String nom;
    @NotNull(message = "Le téléphone est obligatoire !")
    @Pattern(regexp = "[0-9]{9}", message = "Le téléphone est incorrect")
    private String telephone;
    @NotNull(message = "L'adresse est obligatoire !")
    private String adresse;
}
