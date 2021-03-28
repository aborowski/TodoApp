package com.borowski.services;

import java.util.List;

import com.borowski.models.Message;

public interface MessageService {
	List<Message> getMessages();
	
	Message getMessageById(int id);
	
	Message createMessage(Message message);
	
	Message replaceMessage(Message message);
	
	Message updateMessage(Message message);
	
	void deleteMessageById(int id);
}
