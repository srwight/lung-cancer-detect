package com.revature.repositories;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.revature.models.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, UUID> {
	User getUserByusername(String username);
}