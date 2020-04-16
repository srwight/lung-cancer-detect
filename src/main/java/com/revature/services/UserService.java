package com.revature.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.revature.dao.UserDAO;
import com.revature.dao.models.UserDaoModel;
import com.revature.models.HasRole;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repositories.RoleRepository;
import com.revature.repositories.UserRepository;
import com.revature.security.ApplicationUserRole;

@Service
public class UserService implements UserDetailsService {

	private UserDAO userDao;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	public UserService(@Qualifier("userRepo") UserDAO userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userDao.SelectUserByUsername(username)
			.orElseThrow(() -> 
				new UsernameNotFoundException("The User you are trying to log in as is not available"));
	}

	private List<HasRole> createHasRoleList(User user, String ... roles) {
		List<HasRole> hasRole = new ArrayList<HasRole>();
		for (String roleStr : roles)
		{
			hasRole.add(new HasRole(user, roleRepo.getRoleByrole(roleStr), new Date(), true, null, false));
		}
		return hasRole;
	}
	
	public User createUser(String username, String password, String firstName, String lastName, 
			long homePhone, long cellPhone, long workPhone, String email,
			List<? extends GrantedAuthority> grantedAuthorities, 
			boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled,
			String ... roles) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		User user = new User(username, passwordEncoder.encode(password), firstName, lastName, homePhone, cellPhone, workPhone, email,
				grantedAuthorities, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
		user.setHasRole(createHasRoleList(user, roles));
		return user;
	}
	
	public User createUser(String username, String password, 
			List<? extends GrantedAuthority> grantedAuthorities, 
			boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled,
			String ... roles) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		User user = new User(username, passwordEncoder.encode(password), 
				grantedAuthorities, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
		user.setHasRole(createHasRoleList(user, roles));
		return user;
	}
	
	public void addUser(UserDaoModel userDao) {
		ApplicationUserRole userRole = null;
		if ("ADMIN".equals(userDao.getRole()[0])) 
		{
			userRole = ApplicationUserRole.ADMIN;
		}
		else if ("ONCOLOGIST".equals(userDao.getRole()[0])) 
		{
			userRole = ApplicationUserRole.ONCOLOGIST;
		} 
		else if ("PRIMARY".equals(userDao.getRole()[0])) 
		{
			userRole = ApplicationUserRole.PRIMARY;
		}
		else if ("RECEPTIONIST".equals(userDao.getRole()[0])) 
		{
			userRole = ApplicationUserRole.RECEPTIONIST;
		}
		else if ("PATIENT".equals(userDao.getRole()[0])) 
		{
			userRole = ApplicationUserRole.PATIENT;
		}
		
		User user1 = createUser(userDao.getUsername(), 
				userDao.getPassword(),
				userDao.getFirstName(), userDao.getLastName(), userDao.getHomePhone(), userDao.getCellPhone(), userDao.getWorkPhone(), userDao.getEmail(),
				userRole.getGrantedAuthorities(), 
				true, 
				true, 
				true, 
				true, "ADMIN");
		userRepo.save(user1);
	}
	
	public void modifyUser(UserDaoModel userDao) {
		ApplicationUserRole userRole = null;
		if ("ADMIN".equals(userDao.getRole()[0])) 
		{
			userRole = ApplicationUserRole.ADMIN;
		}
		else if ("ONCOLOGIST".equals(userDao.getRole()[0])) 
		{
			userRole = ApplicationUserRole.ONCOLOGIST;
		} 
		else if ("PRIMARY".equals(userDao.getRole()[0])) 
		{
			userRole = ApplicationUserRole.PRIMARY;
		}
		else if ("RECEPTIONIST".equals(userDao.getRole()[0])) 
		{
			userRole = ApplicationUserRole.RECEPTIONIST;
		}
		else if ("PATIENT".equals(userDao.getRole()[0])) 
		{
			userRole = ApplicationUserRole.PATIENT;
		}
		
		roleRepo.getRoleByrole(userDao.getRole()[0]);
		
		User userToModify = userRepo.getUserByusername(userDao.getUsername());
		userToModify.setHomePhone(userToModify.getHomePhone());
		userToModify.setCellPhone(userToModify.getCellPhone());
		userToModify.setWorkPhone(userToModify.getWorkPhone());
		userToModify.setEmail(userToModify.getEmail());
		
		
		
		userRepo.save(userToModify);
	}
	
	public List<UserDaoModel> getAllUsers() {
		List<User> users = userRepo.findAll();
		List<UserDaoModel> userDao = new ArrayList<>();
		for (User user : users)
		{
			String[] roleList = new String[user.getHasRole().size()];
			int count = 0;
			for (HasRole role : user.getHasRole())
			{
				roleList[count++] = role.getToUUID().getRole();
			}
			
			userDao.add(new UserDaoModel(user.getUsername(), "", user.getFirstName(), user.getLastName(), 
					user.getHomePhone(), user.getCellPhone(), user.getWorkPhone(), user.getEmail(), roleList));
		}
		return userDao;
	}
}
