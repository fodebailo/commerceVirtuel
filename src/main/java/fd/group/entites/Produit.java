package fd.group.entites;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Produit implements Serializable {
    @Id
    @GeneratedValue
    private Long      id;
    @NotNull(message = "La désignation est obligatoire !")
    private String    designation;
    private int       quantite;
    private double    prix;
    @Lob
    private byte[]    photo;
    @ManyToOne
    private Categorie categorie;
}
