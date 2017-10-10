package io.erm.ees.model.v2;

public class SpecialCurriculum {

    private long id;
    private long courseId;
    private String name;
    private String type;

    public SpecialCurriculum() {

    }

    public SpecialCurriculum(long id, long courseId, String name, String type) {
        this.id = id;
        this.courseId = courseId;
        this.name = name;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
