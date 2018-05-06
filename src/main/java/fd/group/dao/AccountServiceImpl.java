package fd.group.dao;

import org.springframework.stereotype.Service;

import fd.group.entites.AppRole;
import fd.group.entites.AppUser;

@Service
public class AccountServiceImpl implements AccountService {

    @Override
    public AppUser saveUser(AppUser user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AppRole saveRole(AppRole role) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void AddRoleToUser(String username, String rolename) {
        // TODO Auto-generated method stub

    }

    @Override
    public AppUser findUserByUsername(String username) {
        // TODO Auto-generated method stub
        return null;
    }

}
