package com.revature.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.revature.jwt.JWTConfig;
import com.revature.jwt.JWTTokenVerifier;
import com.revature.jwt.JWTUsernamePasswordAuthFilter;
import com.revature.services.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppicationSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final PasswordEncoder passwordEncoder;
	private final UserService userService;
	private final SecretKey secretKey;
	private final JWTConfig jwtc;
	
	@Autowired
	public AppicationSecurityConfig(PasswordEncoder passwordEncoder, UserService userService, JWTConfig jwtc, SecretKey secretKey) {
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
		this.jwtc = jwtc;
		this.secretKey = secretKey;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and()
			.csrf().disable()
			.authorizeRequests()
			.antMatchers("/authenticate").permitAll().antMatchers("/login").permitAll()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilter(new JWTUsernamePasswordAuthFilter(authenticationManager(), jwtc, secretKey))
			.addFilterAfter(new JWTTokenVerifier(jwtc, secretKey), JWTUsernamePasswordAuthFilter.class)
			.authorizeRequests()
			.anyRequest()
			.authenticated();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder amb) {
		amb.authenticationProvider(daoAuthenticationProvider());
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(userService);
		return provider;
	}
	
	@Bean
	public CorsFilter corsFilter() {
	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    final CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowCredentials(false);
	    configuration.addAllowedOrigin("*");
	    configuration.addAllowedHeader("*");
	    configuration.addExposedHeader("Authorization");
	    configuration.addAllowedMethod("OPTIONS");
	    configuration.addAllowedMethod("HEAD");
	    configuration.addAllowedMethod("GET");
	    configuration.addAllowedMethod("PUT");
	    configuration.addAllowedMethod("POST");
	    configuration.addAllowedMethod("DELETE");
	    configuration.addAllowedMethod("PATCH");
	    source.registerCorsConfiguration("/**", configuration);
	    return new CorsFilter(source);
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() 
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
//	@Override
//	@Bean
//	protected UserDetailsService userDetailsService() {
//		UserDetails user1 = User.withUsername("josh").password(passwordEncoder.encode("asdf"))
//				// .roles(ApplicationUserRole.PATIENT.name())
//				.authorities(ApplicationUserRole.PATIENT.getGrantedAuthorities())
//				.build();
//		UserDetails user2 = User.withUsername("eggman").password(passwordEncoder.encode("projectegg"))
//				// .roles(ApplicationUserRole.ONCOLOGIST.name())
//				.authorities(ApplicationUserRole.ONCOLOGIST.getGrantedAuthorities())
//				.build();
//		List<UserDetails> list = new ArrayList<UserDetails>();
//		list.add(user1);
//		list.add(user2);
//		System.out.println(user1.getAuthorities().toString());
//		System.out.println(user2.getAuthorities().toString());
//		return new InMemoryUserDetailsManager(list);
//	}
}
