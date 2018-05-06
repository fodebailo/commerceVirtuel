package fd.group.service.shopping;

import java.util.List;

import fd.group.entites.LigneCommande;
import fd.group.entites.Produit;

public interface Shopping {
    void ajouterProduit(Produit produit);

    void supprimerPanier(Produit produit);

    double getTotal();

    void vider();

    List<LigneCommande> getLigneCommandes();
}
