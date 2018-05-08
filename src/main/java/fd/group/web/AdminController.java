package fd.group.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import fd.group.dao.AdminRepository;
import fd.group.entites.Categorie;
import fd.group.entites.Client;
import fd.group.entites.Commande;
import fd.group.entites.Produit;
import fd.group.service.shopping.Panier;
import fd.group.service.shopping.Shopping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private Shopping        shopping;
    private double          total;

    @ModelAttribute("categories")
    public List<Categorie> allCategorie() {
        return adminRepository.listCategories();
    }

    @ModelAttribute("produits")
    public List<Produit> allProduit() {
        if (adminRepository.listproduits().size() <= 11)
            return adminRepository.listproduits();
        else
            return adminRepository.listproduits().subList(0, 10);
    }

    @GetMapping("/produitParCat/{id}")
    public String produitParCat(@PathVariable Long id, Model model) {
        model.addAttribute("produits", adminRepository.produitsParCategorie(id));
        return "admin";
    }

    @PostMapping("/produitParMC")
    public String produitParMC(@Param("motcle") String motcle, Model model) {
        model.addAttribute("produits", adminRepository.produitsParMotCle(motcle));
        return "admin";
    }

    @GetMapping("/produitDisponible")
    public String produitDisponible(Model model) {
        if (adminRepository.produitsDisponible().size() <= 10) {
            model.addAttribute("produits", adminRepository.produitsDisponible());
            return "admin";
        } else {
            model.addAttribute("produits", adminRepository.produitsDisponible().subList(0, 10));
            return "admin";
        }
    }

    @ModelAttribute("context")
    public String context() {
        return "/admin";
    }

    @GetMapping("/")
    public String acceuil(Model model) {
        model.addAttribute("cargaisons", adminRepository.listCategories());
        return "admin";
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("cargaisons", adminRepository.listCategories());
        return "admin";
    }

    @GetMapping("/monitoring")
    public String monitoring(Model model) {
        model.addAttribute("produit", new Produit());
        return "monitoring";
    }

    @PostMapping("/commandeParNom")
    public String commandeParNom(@Param("nom") String nom, Model model) {
        List<Commande> commandes = adminRepository.listCommandeParClient(nom);

        if (commandes.isEmpty()) {
            return "redirect:/admin/checking";
        }

        else {
            model.addAttribute("commandes", commandes);
        }
        return "checking";
    }

    @PostMapping("/commandeParDate")
    public String commandeParDate(@Param("date") String date, Model model) throws Exception {

        List<Commande> commandes = adminRepository.listCommandeParDate(date);

        if (commandes.isEmpty()) {
            return "redirect:/admin/checking";
        }

        else {
            model.addAttribute("commandes", commandes);
        }
        return "checking";
    }

    @PostMapping("/commandeEntreDate")
    public String commandeEntreDate(@Param("dateDebut") String dateDebut, @Param("dateFin") String dateFin, Model model)
            throws Exception {

        List<Commande> commandes = adminRepository.listCommandeEntreDate(dateDebut, dateFin);

        if (commandes.isEmpty()) {
            return "redirect:/admin/checking";
        }

        else {
            model.addAttribute("commandes", commandes);
        }
        return "checking";
    }

    @GetMapping("/checking")
    public String checking(Model model) {
        if (adminRepository.listCommande().size() <= 10) {
            model.addAttribute("commandes", adminRepository.listCommande());
        } else {
            model.addAttribute("commandes", adminRepository.listCommande().subList(0, 11));
        }
        return "checking";
    }

    @PostMapping("/addCategorie")
    public String addCategorie(@Valid Categorie categorie, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorie", categorie);
            return "monitoring";
        }

        adminRepository.ajouterCategorie(categorie);
        model.addAttribute("categorie", new Categorie());

        return "redirect:/admin/monitoring";
    }

    @PostMapping("/editCategorie")
    public String editCategorie(@Valid Categorie categorie, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorie", categorie);
            return "monitoring";
        }

        adminRepository.modifierCategorie(categorie);
        model.addAttribute("categorie", new Categorie());

        return "redirect:/admin/monitoring";
    }

    @GetMapping("/ajouterProduit/{id}")
    public String ajouterProduit(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("action", "ajout");
        model.addAttribute("libelle", adminRepository.getCategorie(id).getLibelle());
        model.addAttribute("produit", new Produit());
        return "monitoring";
    }

    @GetMapping("/modifierProduit")
    public String modifierProduit(@Param("idCat") Long idCat, @Param("idProd") Long idProd, Model model) {
        Produit produit = adminRepository.getProduit(idProd);

        if (produit == null) {
            return "monitoring";
        }

        model.addAttribute("action", "modifierProduit");
        model.addAttribute("produit", produit);
        model.addAttribute("idCategorie", idCat);
        return "monitoring";
    }

    @GetMapping("/modifierCategorie/{id}")
    public String modifierCategorie(@PathVariable Long id, Model model) {
        Categorie categorie = adminRepository.getCategorie(id);

        if (categorie == null) {
            return "monitoring";
        }

        model.addAttribute("action", "modifierCategorie");
        model.addAttribute("categorie", categorie);
        return "monitoring";
    }

    @PostMapping("/editProduit/{idCat}")
    public String editProduit(@Valid Produit produit, @PathVariable Long idCat, BindingResult result, Model model,
            MultipartFile file) {
        if (result.hasErrors()) {
            model.addAttribute("action", "modifierProduit");
            return "monitoring";
        }

        if (!file.isEmpty()) {
            try {
                produit.setPhoto(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Impossible de charger l'image !");
            }
        }

        adminRepository.modifierProduit(produit, idCat);
        return "redirect:/admin/monitoring";
    }

    @GetMapping("/deleteCategorie/{id}")
    public String deleteCategorie(@PathVariable Long id) {
        adminRepository.supprimerCategrorie(id);
        return "monitoring";
    }

    @GetMapping("/deleteProduit/{id}")
    public String deleteProduit(@PathVariable Long id) {
        adminRepository.supprimerProduit(id);
        return "monitoring";
    }

    @ModelAttribute("categorie")
    public Categorie getCategorie() {
        return new Categorie();
    }

    @ModelAttribute("total")
    public double totalPanier() {
        if (!shopping.getLigneCommandes().isEmpty()) {
            shopping.getLigneCommandes().forEach(lc -> {
                total += (lc.getProduit().getPrix() * lc.getQuantite());
            });
            return total;
        } else {
            total = 0;
            return total;
        }
    }

    @ModelAttribute("client")
    public Client getClient() {
        return new Client();
    }

    @GetMapping("/listePanier")
    public String listePanier(Model model) {
        model.addAttribute("ligneCommandes", shopping.getLigneCommandes());
        return "admin";
    }

    @GetMapping("/viderPanier")
    public String viderPanier(Model model) {
        shopping.vider();
        return "redirect:/admin/index";
    }

    @GetMapping("/addPanier/{id}")
    public String addPanier(@PathVariable Long id) {
        shopping.ajouterProduit(adminRepository.getProduit(id));
        return "admin";
    }

    @GetMapping("/produitEditionParCat/{id}")
    public String produitEditionParCat(@PathVariable Long id, Model model) {
        List<Produit> prods = adminRepository.produitsParCategorie(id);

        if (prods.isEmpty()) {
            return "monitoring";
        }

        model.addAttribute("action", "supprimerProduit");
        model.addAttribute("produits", prods);
        model.addAttribute("idCategorie", id);

        return "monitoring";
    }

    @PostMapping("/addProduit/{idCat}")
    public String addProduit(@Valid Produit produit, @PathVariable Long idCat, BindingResult result, Model model,
            MultipartFile file) {
        if (result.hasErrors()) {
            model.addAttribute("action", "ajout");
            return "monitoring";
        }

        if (!file.isEmpty()) {
            try {
                produit.setPhoto(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Impossible de charger l'image");
            }
        }

        adminRepository.ajouterProduit(produit, idCat);

        return "redirect:/admin/monitoring";
    }

    @GetMapping(value = "/imageProduit/{id}", produces = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
    @ResponseBody
    public byte[] imageProduit(@PathVariable Long id) throws IOException {
        Produit p = adminRepository.getProduit(id);

        if (p.getPhoto() == null) {
            return new byte[0];
        }

        return IOUtils.toByteArray(new ByteArrayInputStream(p.getPhoto()));
    }

    @PostMapping("/commander")
    public String commander(@Valid Client client, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("client", client);
            return "admin";
        }

        adminRepository.enregistrerCommande((Panier) shopping, client);

        shopping.vider();

        return "redirect:/admin/index";
    }

}
