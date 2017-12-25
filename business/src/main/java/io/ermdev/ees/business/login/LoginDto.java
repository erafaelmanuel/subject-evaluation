package io.ermdev.ees.business.login;

public class LoginDto {

    private String username;
    private String password;
    private Boolean enabled=true;

    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginDto(String username, String password, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
