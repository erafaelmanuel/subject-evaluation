package io.ermdev.ees.data.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name="tblrole")
@Entity
public class Role {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
