package fd.group.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fd.group.entites.AppRole;
import fd.group.entites.AppUser;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AppUserRepository useRepository;
    @Autowired
    private AppRoleRepository roleRepository;

    @Override
    public AppUser saveUser(AppUser user) {
        return useRepository.save(user);
    }

    @Override
    public AppRole saveRole(AppRole role) {
        return roleRepository.save(role);
    }

    @Override
    public void AddRoleToUser(String username, String rolename) {
        AppRole appRole = roleRepository.findByRolename(rolename);
        AppUser appUser = useRepository.findByUsername(username);
        appUser.getRoles().add(appRole);
    }

    @Override
    public AppUser findUserByUsername(String username) {
        return useRepository.findByUsername(username);
    }

}
