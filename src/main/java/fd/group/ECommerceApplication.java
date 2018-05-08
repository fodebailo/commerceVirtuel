package fd.group;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import fd.group.dao.AccountService;
import fd.group.dao.AdminRepository;
import fd.group.entites.AppRole;
import fd.group.entites.AppUser;
import fd.group.entites.Categorie;

@SpringBootApplication
public class ECommerceApplication implements CommandLineRunner {
    @Autowired
    private AdminRepository       adminRepository;
    @Autowired
    private AccountService        accountService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ECommerceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (adminRepository.listCategories().isEmpty()) {
            Stream.of("ordinateur", "televiseur", "portable mobile").forEach(l -> {
                adminRepository.ajouterCategorie(new Categorie(null, l));
            });
        }

        adminRepository.listCategories().forEach(c -> {
            System.out.println(c.getLibelle());
        });

        if (accountService.findUserByUsername("admin") == null) {
            accountService.saveUser(new AppUser(null, "admin", bCryptPasswordEncoder.encode("admin"), null));
            accountService.saveUser(new AppUser(null, "user", bCryptPasswordEncoder.encode("user"), null));

            accountService.saveRole(new AppRole(null, "ADMIN"));
            accountService.saveRole(new AppRole(null, "USER"));

            accountService.AddRoleToUser("admin", "ADMIN");
            accountService.AddRoleToUser("user", "USER");
        }

    }

}
