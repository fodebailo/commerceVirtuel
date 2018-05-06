package fd.group.entites;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Commande implements Serializable {
    @Id
    @GeneratedValue
    private Long                id;
    @Temporal(TemporalType.DATE)
    private Date                dateCommande;
    @ManyToOne
    private Client              client;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<LigneCommande> ligneCommandes = new ArrayList<>();
}
