package classifyx.data.repository;

import classifyx.data.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT a FROM Role AS a WHERE id=:roleId")
    Role findById(@Param("roleId") Long id);
}
