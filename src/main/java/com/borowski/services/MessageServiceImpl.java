package com.borowski.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.borowski.exceptions.NoMessageFoundException;
import com.borowski.models.Message;
import com.borowski.repositories.MessageRepository;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	MessageRepository repository;

	@Override
	public List<Message> getMessages() {
		return repository.findAll();
	}

	@Override
	public Message getMessageById(int id) {
		Message message = repository.findById(id)
			.orElseThrow(() -> new NoMessageFoundException(id));
		
		if(message.getSoftDeletable().isDeleted())
			throw new NoMessageFoundException(id);
		return message;
	}

	@Override
	public Message createMessage(Message message) {
		return repository.save(message);
	}

	@Override
	public Message replaceMessage(Message message) {
		return repository.findById(message.getId()).map((foundMessage) -> {
			foundMessage.updateFields(message, true);
			return repository.save(foundMessage);
		}).orElseGet(() -> {
			return repository.save(message);
		});
	}

	@Override
	public Message updateMessage(Message message) {
		Message foundMessage = repository.findById(message.getId())
				.orElseThrow(() -> new NoMessageFoundException(message.getId()));
		foundMessage.updateFields(message);
		return repository.save(foundMessage);
	}

	@Override
	public void deleteMessageById(int id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException ex) {
			throw new NoMessageFoundException(id);
		}
	}

}
