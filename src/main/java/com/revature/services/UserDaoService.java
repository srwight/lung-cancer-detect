package com.revature.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.revature.dao.UserDAO;
import com.revature.models.HasRole;
import com.revature.models.User;
import com.revature.repositories.UserRepository;
import com.revature.security.ApplicationUserRole;

@Repository("userRepo")
public class UserDaoService implements UserDAO {

	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	public UserDaoService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Optional<User> SelectUserByUsername(String username) {
		return getUser(username);
//		return getUsers().stream().filter(user -> username.equals(user.getUsername())).findFirst();
	}

	private Optional<User> getUser(String username) {
		User user = userRepository.getUserByusername(username);
		User authUser = null;
		if (user != null)
		{
			if (user.getHasRole().size() != 0)
			{
				System.out.println(user.getUsername());
				List<GrantedAuthority> roleList = new ArrayList<>();
				for (HasRole role : user.getHasRole()) {
					System.out.println(role.getToUUID().getRole());
					for (ApplicationUserRole sysRole : ApplicationUserRole.values()) {
						if (role.getToUUID().getRole().equals(sysRole.name()))
						{
							roleList.addAll(sysRole.getGrantedAuthorities());
							break;
						}
					}
				}
				authUser = new User(user.getUsername(), user.getPassword(), roleList, user.isAccountNonExpired(), user.isAccountNonLocked(), user.isCredentialsNonExpired(), user.isEnabled());
				authUser.setHasRole(user.getHasRole());
			}
		}
		System.out.println(authUser.toString());
		return Optional.of(authUser);
	}
	
//	private List<User> getUsers() {
//		List<User> users = userRepository.findAll();
//		users.add(new User("josh", 
//				passwordEncoder.encode("asdf"), 
//				ApplicationUserRole.PATIENT.getGrantedAuthorities(), 
//				true, 
//				true, 
//				true, 
//				true));
//		users.add(new User("eggman", 
//				passwordEncoder.encode("projectegg"), 
//				ApplicationUserRole.ONCOLOGIST.getGrantedAuthorities(), 
//				true, 
//				true, 
//				true, 
//				true));
//		return users;
//	}
}
