package io.ermdev.ees.ui.login;

@FunctionalInterface
public interface LoginListener {

    void onLoginSuccess(LoginEvent loginEvent);
}
