package classifyx.data.service;

import classifyx.data.entity.User;
import classifyx.data.repository.UserRepository;
import classifyx.ui.login.LoginDto;
import classifyx.exception.LoginException;
import mapfierj.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private UserRepository userRepository;

    private User user;

    @Autowired
    LoginService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private LoginDto findByUsername(String username) throws LoginException {
        final Mapper mapper = new Mapper();
        user = userRepository.findByUsername(username);
        if(user==null)
            throw new LoginException("No user found on username:" + username);
        else {
            return mapper.set(user).mapTo(LoginDto.class);
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
