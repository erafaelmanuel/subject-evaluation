package io.ermdev.projectx.data.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name="tbl_term")
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
    @JoinTable(name = "tbl_term_subject", joinColumns = @JoinColumn(name = "termId"),
            inverseJoinColumns = @JoinColumn(name="subjectId"))
    private List<Subject> subjects = new ArrayList<>();
}
