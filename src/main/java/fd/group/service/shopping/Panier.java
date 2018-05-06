package fd.group.service.shopping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import fd.group.entites.LigneCommande;
import fd.group.entites.Produit;

@Component
public class Panier implements Shopping, Serializable {
    private List<LigneCommande> ligneCommandes = new ArrayList<>();

    @Override
    public void ajouterProduit(Produit produit) {
        boolean existe = false;

        for (LigneCommande lc : ligneCommandes) {
            if (lc.getProduit().equals(produit)) {
                lc.setQuantite(lc.getQuantite() + 1);
                existe = true;
            }
        }

        if (!existe) {
            ligneCommandes.add(new LigneCommande(null, produit, 1));
        }
    }

    @Override
    public void supprimerPanier(Produit produit) {
        for (LigneCommande lc : ligneCommandes) {
            if (lc.getProduit().equals(produit)) {
                ligneCommandes.remove(produit);
                return;
            }
        }
    }

    @Override
    public double getTotal() {
        double total = 0;

        for (LigneCommande lc : ligneCommandes) {
            total += lc.getQuantite() * lc.getProduit().getPrix();
        }

        return total;
    }

    @Override
    public void vider() {
        ligneCommandes.clear();
    }

    @Override
    public List<LigneCommande> getLigneCommandes() {
        return ligneCommandes;
    }

}
