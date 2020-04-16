package com.revature.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.revature.models.User;
import com.revature.repositories.HasRoleRepository;
import com.revature.repositories.RoleRepository;
import com.revature.repositories.UserRepository;
import com.revature.security.ApplicationUserRole;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class GetRolesByUser extends TestCase {
	
	@Autowired(required=true)
	UserRepository repo;
	
	@Autowired(required=true)
	HasRoleRepository repo2;
	
	@Autowired(required=true)
	RoleRepository repo3;
	
	@Test
	public void testCreateAccount() {
		// repo.save(SetupDB.setup());
		User user = repo.getUserByusername("admin");
		System.out.println(user.toString());
	}
	
}
