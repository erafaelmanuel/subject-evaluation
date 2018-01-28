package io.ermdev.projectx.data.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name="tbl_subject")
@Entity
public class Subject {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @ManyToMany(mappedBy = "subjects", cascade = CascadeType.ALL)
    private List<Term> terms = new ArrayList<>();
}
