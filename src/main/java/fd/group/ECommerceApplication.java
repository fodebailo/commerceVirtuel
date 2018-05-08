package fd.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fd.group.dao.WebRepository;

@SpringBootApplication
public class ECommerceApplication implements CommandLineRunner {
    @Autowired
    private WebRepository webRepository;

    public static void main(String[] args) {
        SpringApplication.run(ECommerceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        webRepository.listCategories().forEach(c -> {
            System.out.println(c.getLibelle());
        });
    }

}
