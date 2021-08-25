package com.devsuperior.movieflix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.UserRepository;
import com.devsuperior.movieflix.services.exceptions.ForbiddenException;
import com.devsuperior.movieflix.services.exceptions.UnAuthorizedException;

@Service
public class AuthService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional(readOnly = true)
	public User authenticated() {
		try {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepository.findByEmail(username);
		}
		catch (Exception e) {
			throw new UnAuthorizedException("Invalid User");
		}		
		
	}
	
	public void validateSelfOrMember(Long userId) {
		User user = authenticated();
		if (!user.getId().equals(userId) && !user.hasRole("ROLE_MEMBER")) {
			throw new ForbiddenException("Acess denied");
			
		}
		
	}
	
	public void validateSelfOrVisitor(Long userId) {
		User user = authenticated();
		if (!user.getId().equals(userId) && !user.hasRole("ROLE_VISITOR")) {
			throw new ForbiddenException("Acess denied");
			
		}
		
	}
	

}
