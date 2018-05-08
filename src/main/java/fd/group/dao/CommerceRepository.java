package fd.group.dao;

import java.util.List;

import fd.group.entites.Categorie;
import fd.group.entites.Client;
import fd.group.entites.Commande;
import fd.group.entites.Produit;
import fd.group.service.shopping.Shopping;

public interface CommerceRepository {
    void ajouterCategorie(Categorie c);

    List<Categorie> listCategories();

    Categorie getCategorie(Long id);

    void supprimerCategrorie(Long id);

    void modifierCategorie(Categorie c);

    void ajouterProduit(Produit p, Long idCat);

    List<Produit> listproduits();

    List<Produit> produitsParMotCle(String mc);

    List<Produit> produitsParCategorie(Long idCat);

    List<Produit> produitsDisponible();

    Produit getProduit(Long id);

    void supprimerProduit(Long id);

    void modifierProduit(Produit p, Long idCat);

    Commande enregistrerCommande(Shopping shop, Client c);

    List<Commande> listCommande();

    List<Commande> listCommandeParDate(String date);

    List<Commande> listCommandeEntreDate(String dateDebut, String dateFin);

    List<Commande> listCommandeParClient(String nomClient);
}
