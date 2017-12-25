package io.ermdev.ess.business.login;

import io.ermdev.ees.data.entity.User;

public class LoginEvent {

    public User user;

    public LoginEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
