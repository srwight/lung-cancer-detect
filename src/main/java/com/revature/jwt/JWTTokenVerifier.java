package com.revature.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.base.Strings;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JWTTokenVerifier extends OncePerRequestFilter {
	
	private final SecretKey secretKey;
	private final JWTConfig jwtc;
	
	public JWTTokenVerifier(JWTConfig jwtc, SecretKey secretKey) {
		this.jwtc = jwtc;
		this.secretKey = secretKey;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader(jwtc.getAuthorizationHeader());
		
		if (Strings.isNullOrEmpty(authHeader) || !authHeader.startsWith(jwtc.getTokenPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}
		try {
			String token = authHeader.replace(jwtc.getTokenPrefix(), "");
			Jws<Claims> claimsJws = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build().parseClaimsJws(token);
			Claims body = claimsJws.getBody();
			String username = body.getSubject();
			List<Map<String, String>> auths = (List<Map<String, String>>) body.get("authorities");
			
			List<SimpleGrantedAuthority> sga = auths.stream().map(m -> new SimpleGrantedAuthority(m.get("authority")))
			.collect(Collectors.toList());
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, sga);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (JwtException e) {
			System.out.println(e.getMessage());
			throw new IllegalStateException("Token cannot be trusted");
		}
		filterChain.doFilter(request, response);
	}
	
}
