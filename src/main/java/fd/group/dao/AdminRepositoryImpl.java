package fd.group.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fd.group.entites.Categorie;
import fd.group.entites.Client;
import fd.group.entites.Commande;
import fd.group.entites.Produit;
import fd.group.service.shopping.Panier;

@Component
public class AdminRepositoryImpl implements AdminRepository {
    @Autowired
    private CommerceRepository commerceRepository;

    @Override
    public List<Categorie> listCategories() {
        return commerceRepository.listCategories();
    }

    @Override
    public Categorie getCategorie(Long idCat) {
        return commerceRepository.getCategorie(idCat);
    }

    @Override
    public List<Produit> listproduits() {
        return commerceRepository.listproduits();
    }

    @Override
    public List<Produit> produitsParMotCle(String mc) {
        return commerceRepository.produitsParMotCle(mc);
    }

    @Override
    public List<Produit> produitsParCategorie(Long idCat) {
        return commerceRepository.produitsParCategorie(idCat);
    }

    @Override
    public List<Produit> produitsDisponible() {
        return commerceRepository.produitsDisponible();
    }

    @Override
    public Produit getProduit(Long idP) {
        return commerceRepository.getProduit(idP);
    }

    @Override
    public Commande enregistrerCommande(Panier p, Client c) {
        return commerceRepository.enregistrerCommande(p, c);
    }

    @Override
    public void ajouterProduit(Produit p, Long idCat) {
        commerceRepository.ajouterProduit(p, idCat);
    }

    @Override
    public void supprimerProduit(Long idP) {
        commerceRepository.supprimerProduit(idP);
    }

    @Override
    public void modifierProduit(Produit p, Long idCat) {
        commerceRepository.modifierProduit(p, idCat);
    }

    @Override
    public void ajouterCategorie(Categorie c) {
        commerceRepository.ajouterCategorie(c);
    }

    @Override
    public void supprimerCategrorie(Long idcat) {
        commerceRepository.supprimerCategrorie(idcat);
    }

    @Override
    public void modifierCategorie(Categorie c) {
        commerceRepository.modifierCategorie(c);
    }

    @Override
    public List<Commande> listCommande() {
        return commerceRepository.listCommande();
    }

    @Override
    public List<Commande> listCommandeParDate(String dateCommande) {
        return commerceRepository.listCommandeParDate(dateCommande);
    }

    @Override
    public List<Commande> listCommandeEntreDate(String dateDebut, String dateFin) {
        return commerceRepository.listCommandeEntreDate(dateDebut, dateFin);
    }

    @Override
    public List<Commande> listCommandeParClient(String nomClient) {
        return commerceRepository.listCommandeParClient(nomClient);
    }

}
