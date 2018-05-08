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

import fd.group.dao.UserRepository;
import fd.group.entites.Categorie;
import fd.group.entites.Client;
import fd.group.entites.Produit;
import fd.group.service.shopping.Panier;
import fd.group.service.shopping.Shopping;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Shopping       shopping;
    private double         total;

    @ModelAttribute("categories")
    public List<Categorie> allCategorie() {
        return userRepository.listCategories();
    }

    @ModelAttribute("produits")
    public List<Produit> allProduit() {
        if (userRepository.listproduits().size() <= 11)
            return userRepository.listproduits();
        else
            return userRepository.listproduits().subList(0, 10);
    }

    @ModelAttribute("context")
    public String context() {
        return "/user";
    }

    @GetMapping("/")
    public String acceuil(Model model) {
        // model.addAttribute("cargaisons", userRepository.listCategories());
        return "user";
    }

    @GetMapping("/index")
    public String index(Model model) {
        // model.addAttribute("cargaisons", userRepository.listCategories());
        return "user";
    }

    @GetMapping("/monitoring")
    public String monitoring(Model model) {
        model.addAttribute("produit", new Produit());
        return "monitoring";
    }

    @GetMapping("/ajouterProduit/{id}")
    public String ajouterProduit(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("action", "ajout");
        model.addAttribute("libelle", userRepository.getCategorie(id).getLibelle());
        model.addAttribute("produit", new Produit());
        return "monitoring";
    }

    @GetMapping("/modifierProduit")
    public String modifierProduit(@Param("idCat") Long idCat, @Param("idProd") Long idProd, Model model) {
        Produit produit = userRepository.getProduit(idProd);

        if (produit == null) {
            return "monitoring";
        }

        model.addAttribute("action", "modifierProduit");
        model.addAttribute("produit", produit);
        model.addAttribute("idCategorie", idCat);
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

        userRepository.modifierProduit(produit, idCat);
        return "redirect:/user/monitoring";
    }

    @GetMapping("/deleteProduit/{id}")
    public String deleteProduit(@PathVariable Long id) {
        userRepository.supprimerProduit(id);
        return "monitoring";
    }

    @GetMapping("/produitDisponible")
    public String produitDisponible(Model model) {
        if (userRepository.produitsDisponible().size() <= 10) {
            model.addAttribute("produits", userRepository.produitsDisponible());
            return "user";
        } else {
            model.addAttribute("produits", userRepository.produitsDisponible().subList(0, 10));
            return "user";
        }
    }

    @GetMapping("/produitParCat/{id}")
    public String produitParCat(@PathVariable Long id, Model model) {
        model.addAttribute("produits", userRepository.produitsParCategorie(id));
        return "user";
    }

    @PostMapping("/produitParMC")
    public String produitParMC(@Param("motcle") String motcle, Model model) {
        model.addAttribute("produits", userRepository.produitsParMotCle(motcle));
        return "user";
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

    @ModelAttribute("categorie")
    public Categorie getCategorie() {
        return new Categorie();
    }

    @GetMapping("/listePanier")
    public String listePanier(Model model) {
        model.addAttribute("ligneCommandes", shopping.getLigneCommandes());
        return "user";
    }

    @GetMapping("/viderPanier")
    public String viderPanier(Model model) {
        shopping.vider();
        return "redirect:/user/index";
    }

    @GetMapping("/addPanier/{id}")
    public String addPanier(@PathVariable Long id) {
        shopping.ajouterProduit(userRepository.getProduit(id));
        return "user";
    }

    @GetMapping("/produitEditionParCat/{id}")
    public String produitEditionParCat(@PathVariable Long id, Model model) {
        List<Produit> prods = userRepository.produitsParCategorie(id);

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

        userRepository.ajouterProduit(produit, idCat);

        return "redirect:/user/monitoring";
    }

    @GetMapping(value = "/imageProduit/{id}", produces = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
    @ResponseBody
    public byte[] imageProduit(@PathVariable Long id) throws IOException {
        Produit p = userRepository.getProduit(id);

        if (p.getPhoto() == null) {
            return new byte[0];
        }

        return IOUtils.toByteArray(new ByteArrayInputStream(p.getPhoto()));
    }

    @PostMapping("/commander")
    public String commander(@Valid Client client, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("client", client);
            return "user";
        }

        userRepository.enregistrerCommande((Panier) shopping, client);

        shopping.vider();

        return "redirect:/user/index";
    }

}
