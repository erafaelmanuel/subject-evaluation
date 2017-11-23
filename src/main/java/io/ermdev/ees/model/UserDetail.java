package io.ermdev.ees.model;

public class UserDetail {

    private long id;
    private String username;
    private String password;
    private UserType userType;
    private boolean isActivated;
    private String registrationDate;
    private String activated;

    public UserDetail() {
        super();
    }

    public UserDetail(String username, String password, UserType userType, boolean isActivated,
                      String registrationDate) {
        this();
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.isActivated = isActivated;
        this.registrationDate = registrationDate;
    }

    public UserDetail(long id, String username, String password, UserType userType, boolean isActivated,
                      String registrationDate) {
        this();
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.isActivated = isActivated;
        this.registrationDate = registrationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setUserType(String userType) {
        if (userType.equals(UserType.TEACHER.getType()))
            this.userType = UserType.TEACHER;
        else if (userType.equals(UserType.ADMIN.getType()))
            this.userType = UserType.ADMIN;
        else if(userType.equals(UserType.DEAN.getType()))
            this.userType = UserType.DEAN;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getActivated() {
        return activated;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userType=" + userType +
                ", isActivated=" + isActivated +
                ", registrationDate='" + registrationDate + '\'' +
                '}';
    }
}
