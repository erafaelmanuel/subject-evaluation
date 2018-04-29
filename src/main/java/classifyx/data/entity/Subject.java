package classifyx.data.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tbl_subject")
public class Subject {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @ManyToMany(mappedBy = "subjects", cascade = CascadeType.ALL)
    private List<Term> terms = new ArrayList<>();
}
