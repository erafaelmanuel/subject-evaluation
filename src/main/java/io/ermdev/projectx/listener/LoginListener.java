package io.ermdev.projectx.listener;

import io.ermdev.projectx.event.LoginEvent;

@FunctionalInterface
public interface LoginListener {

    void onLoginSuccess(LoginEvent loginEvent);
}
