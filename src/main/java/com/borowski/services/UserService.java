package com.borowski.services;

import java.util.List;

import com.borowski.models.User;

public interface UserService {
	List<User> getUsers();
	
	User getUserById(int id);
	
	User createUser(User user);
	
	User replaceUser(User user);
	
	User updateUser(User user);
	
	void deleteUserById(int id);
}
