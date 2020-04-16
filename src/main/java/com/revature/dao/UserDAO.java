package com.revature.dao;

import java.util.Optional;

import com.revature.models.User;

public interface UserDAO {

	Optional<User> SelectUserByUsername(String username);
	
}
