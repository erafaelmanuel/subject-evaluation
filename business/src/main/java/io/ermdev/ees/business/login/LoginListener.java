package io.ermdev.ees.business.login;

@FunctionalInterface
public interface LoginListener {

    void onLoginSuccess(LoginEvent loginEvent);
}
