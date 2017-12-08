package io.ermdev.ees.data.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name="tblterm")
@Entity
public class Term {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private Integer semester;
    private Integer year;
    @ManyToOne
    private Curriculum curriculum;
    @ManyToMany
    @JoinTable(name = "tblterm_subject", joinColumns = @JoinColumn(name = "termId"),
            inverseJoinColumns = @JoinColumn(name="subjectId"))
    private List<Subject> subjects = new ArrayList<>();

}
