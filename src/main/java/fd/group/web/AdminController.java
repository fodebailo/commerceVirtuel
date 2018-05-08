package fd.group.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fd.group.dao.AdminRepository;
import fd.group.entites.Categorie;
import fd.group.entites.Produit;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;

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

}
