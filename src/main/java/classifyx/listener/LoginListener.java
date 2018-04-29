package classifyx.listener;

import classifyx.event.LoginEvent;

@FunctionalInterface
public interface LoginListener {

    void onLoginSuccess(LoginEvent loginEvent);
}
