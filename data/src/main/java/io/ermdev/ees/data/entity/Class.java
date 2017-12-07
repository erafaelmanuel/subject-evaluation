package io.ermdev.ees.data.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name="tblclass")
@Entity
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable(name = "tblclass_student", joinColumns = @JoinColumn(name = "classId"),
            inverseJoinColumns = @JoinColumn(name = "studentId"))
    private List<Student> students = new ArrayList<>();

    public Class(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}