package com.revature.repositories;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.revature.models.Role;
import com.revature.models.User;


@Repository
@Transactional
public interface RoleRepository extends JpaRepository<Role, UUID> {
	
	@Query("SELECT r.toUUID FROM User u INNER JOIN u.hasRole r WHERE u.username = ?1")
	List<Role> findRoleByusername(User l1);

	Role getRoleByrole(String role);
}
