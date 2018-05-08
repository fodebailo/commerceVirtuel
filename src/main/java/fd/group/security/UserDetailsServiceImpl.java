package fd.group.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fd.group.dao.AccountService;
import fd.group.entites.AppUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = accountService.findUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("L'utilisateur est introuvable !");
        }

        List<GrantedAuthority> roles = new ArrayList<>();

        user.getRoles().forEach(r -> {
            roles.add(new SimpleGrantedAuthority(r.getRolename()));
        });

        return new User(user.getUsername(), user.getPassword(), roles);
    }

}
