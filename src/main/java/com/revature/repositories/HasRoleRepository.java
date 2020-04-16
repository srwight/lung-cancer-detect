package com.revature.repositories;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.models.HasRole;

@Repository
@Transactional
public interface HasRoleRepository extends JpaRepository<HasRole, UUID> {

}
