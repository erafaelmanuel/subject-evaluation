package classifyx.ui.login;

import classifyx.data.entity.User;

public class LoginEvent {

    public User user;

    public LoginEvent(User source) {
        this.user = source;
    }

    public User getSource() {
        return user;
    }
}
