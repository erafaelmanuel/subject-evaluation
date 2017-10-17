package io.erm.ees.model.v2;

public enum SecName {

    A(1, "A"), B(2, "B"), C(3, "C"), D(4, "D"), E(5, "E"), F(6, "F");

    private int key;
    private String value;

    SecName(int key, String value) {
        this.key=key;
        this.value=value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
