package io.ermdev.ess.business.login;

@FunctionalInterface
public interface LoginListener {

    void onLoginSuccess(LoginEvent loginEvent);
}
