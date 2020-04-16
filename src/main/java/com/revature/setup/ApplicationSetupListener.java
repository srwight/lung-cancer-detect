package com.revature.setup;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repositories.RoleRepository;
import com.revature.repositories.UserRepository;
import com.revature.security.ApplicationUserRole;
import com.revature.services.UserService;

@Component
public class ApplicationSetupListener implements
ApplicationListener<ContextRefreshedEvent> {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	UserService uService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		User user;
		if ((user = userRepo.getUserByusername("admin")) != null)
		{
			System.out.println(user + " is already present in system.");
		}
		else
		{
			List<Role> roles = new ArrayList<Role>();
			roles.add(new Role("ADMIN"));
			roles.add(new Role("ONCOLOGIST"));
			roles.add(new Role("PRIMARY"));
			roles.add(new Role("RECEPTIONIST"));
			roles.add(new Role("PATIENT"));
			roleRepo.save(roles);
			userRepo.save(setup());			
		}
	}
	
	public List<User> setup() {
		List<User> users = new ArrayList<User>();
		User user1 = uService.createUser("admin", 
				"ApwfGSw$9000",
				ApplicationUserRole.ADMIN.getGrantedAuthorities(), 
				true, 
				true, 
				true, 
				true, "ADMIN");
		User user2 = uService.createUser("ssmith", 
				"#1fishFriend", "Sam", "Smith", 6458452101L, 0, 0, "ssmith@someemailservice.com",
				ApplicationUserRole.RECEPTIONIST.getGrantedAuthorities(),
				true, 
				true, 
				true, 
				true, "RECEPTIONIST");
		User user3 = uService.createUser("djackson", 
				"WfPhaSu$7M", "Darik", "Jackson", 6454151221L, 0, 8002468742L, "djackson@someemailservice.com",
				ApplicationUserRole.PRIMARY.getGrantedAuthorities(),
				true, 
				true, 
				true, 
				true, "PRIMARY");
		User user4 = uService.createUser("janderson", 
				"SPfC$1th", "John", "Anderson", 6454151721L, 0, 8002468742L, "janderson@someemailservice.com",
				ApplicationUserRole.ONCOLOGIST.getGrantedAuthorities(),
				true, 
				true, 
				true, 
				true, "ONCOLOGIST");
		users.add(user1);
		users.add(user2);
		users.add(user3);
		users.add(user4);
		return users;
	}
}
