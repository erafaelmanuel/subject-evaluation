package io.ermdev.ees.business.login;

import io.ermdev.ees.data.entity.User;
import io.ermdev.ees.data.repository.UserRepository;
import io.ermdev.mapfierj.core.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private UserRepository userRepository;
    private ModelMapper mapper;
    private User user;

    @Autowired
    LoginService(UserRepository userRepository, ModelMapper mapper){
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    private LoginDto findByUsername(String username) throws LoginException{
        user = userRepository.findByUsername(username);
        if(user==null)
            throw new LoginException("No user found on username:" + username);
        else {
            LoginDto loginDto = new LoginDto();
            mapper.setAndGetTransaction(user).mapTo(loginDto);
            return loginDto;
        }
    }

    public boolean authenticateUser(String username, String password) {
        try {
            LoginDto user = findByUsername(username);
            if(!password.equals(user.getPassword())) {
                throw new LoginException("Bad credentials");
            }
            if(!user.getEnabled()) {
                throw new LoginException("User is not activated");
            }
            return true;
        } catch (LoginException e) {
            return false;
        }
    }

    public User getUser() {
        return user;
    }
}