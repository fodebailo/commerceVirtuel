package fd.group.entites;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LigneCommande implements Serializable {
    @Id
    @GeneratedValue
    private Long    id;
    @ManyToOne
    private Produit produit;
    private int     quantite;
}
