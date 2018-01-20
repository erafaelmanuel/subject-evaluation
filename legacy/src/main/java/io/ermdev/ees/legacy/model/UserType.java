package io.ermdev.ees.legacy.model;

public enum UserType {

    TEACHER("user/teacher"), DEAN("user/dean"),  ADMIN("admin/admin");

    private String type;

    UserType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
