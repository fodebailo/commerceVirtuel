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
import org.springframework.web.bind.annotation.ResponseBody;

import fd.group.dao.WebRepository;
import fd.group.entites.Categorie;
import fd.group.entites.Client;
import fd.group.entites.Commande;
import fd.group.entites.Produit;
import fd.group.service.shopping.Panier;
import fd.group.service.shopping.Shopping;

@Controller
public class WebController {
    @Autowired
    private WebRepository webRepository;
    @Autowired
    private Shopping      shopping;
    private double        total;

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

    @ModelAttribute("categories")
    public List<Categorie> allCategorie() {
        return webRepository.listCategories();
    }

    @ModelAttribute("produits")
    public List<Produit> allProduit() {
        if (webRepository.listproduits().size() <= 11)
            return webRepository.listproduits();
        else
            return webRepository.listproduits().subList(0, 10);
    }

    @ModelAttribute("context")
    public String context() {
        return "";
    }

    @GetMapping("/")
    public String acceuil(Model model) {
        // model.addAttribute("cargaisons", webRepository.listCategories());
        return "index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        // model.addAttribute("cargaisons", webRepository.listCategories());
        return "index";
    }

    @GetMapping("/produitDisponible")
    public String produitDisponible(Model model) {
        if (webRepository.produitsDisponible().size() <= 10) {
            model.addAttribute("produits", webRepository.produitsDisponible());
            return "index";
        } else {
            model.addAttribute("produits", webRepository.produitsDisponible().subList(0, 10));
            return "index";
        }
    }

    @GetMapping("/produitParCat/{id}")
    public String produitParCat(@PathVariable Long id, Model model) {
        model.addAttribute("produits", webRepository.produitsParCategorie(id));
        return "index";
    }

    @GetMapping("/listePanier")
    public String listePanier(Model model) {
        model.addAttribute("ligneCommandes", shopping.getLigneCommandes());
        return "index";
    }

    @GetMapping("/viderPanier")
    public String viderPanier(Model model) {
        shopping.vider();
        return "redirect:/index";
    }

    @GetMapping("/addPanier/{id}")
    public String addPanier(@PathVariable Long id) {
        shopping.ajouterProduit(webRepository.getProduit(id));
        return "index";
    }

    @PostMapping("/produitParMC")
    public String produitParMC(@Param("motcle") String motcle, Model model) {
        model.addAttribute("produits", webRepository.produitsParMotCle(motcle));
        return "index";
    }

    @GetMapping(value = "/imageProduit/{id}", produces = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE })
    @ResponseBody
    public byte[] imageProduit(@PathVariable Long id) throws IOException {
        Produit p = webRepository.getProduit(id);

        if (p.getPhoto() == null) {
            return new byte[0];
        }

        return IOUtils.toByteArray(new ByteArrayInputStream(p.getPhoto()));
    }

    @PostMapping("/commander")
    public String commander(@Valid Client client, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("client", client);
            return "redirect:/index#commande";
        }

        Commande commande = webRepository.enregistrerCommande((Panier) shopping, client);

        if (commande != null) {
            model.addAttribute("message", "La commande a été bien enregistré !");
            model.addAttribute("client", new Client());
        } else {
            model.addAttribute("message", "Le serveur est temporaiement inaccessible !");
            model.addAttribute("client", new Client());
        }

        shopping.vider();

        return "redirect:/index";
    }

}