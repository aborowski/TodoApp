package com.borowski.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.borowski.exceptions.DuplicateUsernameException;
import com.borowski.exceptions.NoTaskFoundException;
import com.borowski.exceptions.NoUserFoundException;
import com.borowski.models.User;
import com.borowski.repositories.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository repository;

	@Override
	public List<User> getUsers() {
		return repository.findAll();
	}

	@Override
	public User getUserById(int id) {
		User user = repository.findById(id).orElseThrow(() -> new NoUserFoundException(id));
		
		if(user.getSoftDeletable().isDeleted())
			throw new NoTaskFoundException(id);
		return user;
	}

	@Override
	public User createUser(User user) {
		if(repository.findByUsername(user.getUsername()).isPresent())
			throw new DuplicateUsernameException(user.getUsername());
		
		return repository.save(user);
	}

	@Override
	public User replaceUser(User user) {
		return repository.findById(user.getId()).map((foundUser) -> {
			foundUser.updateFields(user, true);
			return repository.save(foundUser);
		}).orElseGet(() -> {
			return repository.save(user);
		});
	}

	@Override
	public User updateUser(User user) {
		User foundUser = repository.findById(user.getId())
				.orElseThrow(() -> new NoUserFoundException(user.getId()));
		foundUser.updateFields(user);
		return repository.save(foundUser);
	}

	@Override
	public void deleteUserById(int id) {
		try {
			repository.deleteById(id);
		} catch(EmptyResultDataAccessException ex) {
			throw new NoUserFoundException(id);
		}
	}

}
