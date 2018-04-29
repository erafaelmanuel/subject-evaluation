package classifyx.data.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tbl_curriculum")
public class Curriculum {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    @OneToMany(mappedBy = "curriculum", cascade = CascadeType.ALL)
    private List<Term> terms = new ArrayList<>();

}
