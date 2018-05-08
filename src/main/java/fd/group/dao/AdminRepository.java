package fd.group.dao;

import java.util.List;

import fd.group.entites.Categorie;
import fd.group.entites.Client;
import fd.group.entites.Commande;
import fd.group.entites.Produit;
import fd.group.service.shopping.Panier;

public interface AdminRepository {
    List<Categorie> listCategories();

    Categorie getCategorie(Long idCat);

    List<Produit> listproduits();

    List<Produit> produitsParMotCle(String mc);

    List<Produit> produitsParCategorie(Long idCat);

    List<Produit> produitsDisponible();

    Produit getProduit(Long idP);

    Commande enregistrerCommande(Panier p, Client c);

    void ajouterProduit(Produit p, Long idCat);

    void supprimerProduit(Long idP);

    void modifierProduit(Produit p, Long idCat);

    void ajouterCategorie(Categorie c);

    void supprimerCategrorie(Long idcat);

    void modifierCategorie(Categorie c);

    List<Commande> listCommande();

    List<Commande> listCommandeParDate(String date);

    List<Commande> listCommandeEntreDate(String dateDebut, String dateFin);

    List<Commande> listCommandeParClient(String nomClient);
}
