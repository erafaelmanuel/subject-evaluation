package io.ermdev.ees.data.repository;

import io.ermdev.ees.data.entity.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepositoryImpl {

    @Autowired
    private SessionFactory sessionFactory;

    public Role findById(Long roleId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Role role = session.get(Role.class, roleId);
        role.getUsers().size();

        session.getTransaction().commit();
        session.close();

        return role;
    }
}
