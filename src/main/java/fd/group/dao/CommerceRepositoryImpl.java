package fd.group.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fd.group.entites.Categorie;
import fd.group.entites.Client;
import fd.group.entites.Commande;
import fd.group.entites.Produit;
import fd.group.service.shopping.Shopping;

@Repository
@Transactional
public class CommerceRepositoryImpl implements CommerceRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public void ajouterCategorie(Categorie c) {
        em.persist(c);
    }

    @Override
    public List<Categorie> listCategories() {
        return em.createQuery("select c from Categorie c", Categorie.class).getResultList();
    }

    @Override
    public Categorie getCategorie(Long id) {
        return em.find(Categorie.class, id);
    }

    @Override
    public void supprimerCategrorie(Long id) {
        em.remove(getCategorie(id));
    }

    @Override
    public void modifierCategorie(Categorie c) {
        em.merge(c);
    }

    @Override
    public void ajouterProduit(Produit p, Long idCat) {
        Categorie categorie = getCategorie(idCat);
        p.setCategorie(categorie);
        em.persist(p);
    }

    @Override
    public List<Produit> listproduits() {
        return em.createQuery("select p from Produit p", Produit.class).getResultList();
    }

    @Override
    public List<Produit> produitsParMotCle(String mc) {
        return em.createQuery("select p from Produit p where p.designation like :motcle", Produit.class)
                .setParameter("motcle", "%" + mc + "%").getResultList();
    }

    @Override
    public List<Produit> produitsParCategorie(Long idCat) {
        return em.createQuery("select p from Produit p where p.categorie.id = :id", Produit.class)
                .setParameter("id", idCat).getResultList();
    }

    @Override
    public List<Produit> produitsDisponible() {
        return em.createQuery("select p from Produit p where p.quantite > 2", Produit.class).getResultList();
    }

    @Override
    public Produit getProduit(Long id) {
        return em.find(Produit.class, id);
    }

    @Override
    public void supprimerProduit(Long id) {
        em.remove(getProduit(id));
    }

    @Override
    public void modifierProduit(Produit p) {
        em.merge(p);
    }

    @Override
    public Commande enregistrerCommande(Shopping shop, Client c) {
        em.persist(c);

        Commande commande = new Commande();
        commande.setClient(c);
        commande.setDateCommande(new Date());
        commande.setLigneCommandes(shop.getLigneCommandes());

        em.persist(commande);

        shop.getLigneCommandes().forEach(lc -> {
            Produit produit = getProduit(lc.getProduit().getId());
            produit.setQuantite(produit.getQuantite() - lc.getProduit().getQuantite());
        });

        return commande;
    }

}
