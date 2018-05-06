package fd.group.dao;

import java.util.List;

import fd.group.entites.Categorie;
import fd.group.entites.Client;
import fd.group.entites.Commande;
import fd.group.entites.Produit;
import fd.group.service.shopping.Panier;

public interface WebRepository {
    List<Categorie> listCategories();

    Categorie getCategorie(Long idCat);

    List<Produit> listproduits();

    List<Produit> produitsParMotCle(String mc);

    List<Produit> produitsParCategorie(Long idCat);

    List<Produit> produitsDisponible();

    Produit getProduit(Long idP);

    Commande enregistrerCommande(Panier p, Client c);
}
