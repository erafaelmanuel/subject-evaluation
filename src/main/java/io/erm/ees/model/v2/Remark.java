package io.erm.ees.model.v2;

public enum Remark {
    NOTSET(0, "NOTSET"), PASSED(1, "PASSED"), FAILED(2, "FAILED"), INCOMPLETE(3, "INCOMPLETE"), DROPPED(3, "DROPPED");

    private int key;
    private String code;

    Remark(int key, String code) {
        this.key=key;
        this.code=code;
    }

    public int getKey() {
        return key;
    }

    public String getCode() {
        return code;
    }
}
