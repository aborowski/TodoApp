package com.borowski.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.borowski.exceptions.NoTaskFoundException;
import com.borowski.exceptions.OperationNotAllowedException;
import com.borowski.models.Priority;
import com.borowski.models.Task;
import com.borowski.repositories.TaskRepository;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {
	
	@Autowired
	TaskRepository repository;

	@Override
	public List<Task> getTasks() {
		return repository.findAll();
	}

	@Override
	public Task getTaskById(int id) {
		Task task = repository.findById(id).orElseThrow(() -> new NoTaskFoundException(id));
		
		if(task.getSoftDeletable().isDeleted())
			throw new NoTaskFoundException(id);
		return task;
	}

	@Override
	public Task createTask(Task task) {
		return repository.save(task);
	}

	@Override
	public Task replaceTask(Task task) {
		return repository.findById(task.getId()).map((foundTask) -> {
			return repository.save(foundTask);
		}).orElseGet(() -> {
			return repository.save(task);
		});
	}

	@Override
	public Task updateTask(Task task) {
		Task foundTask = repository.findById(task.getId())
				.orElseThrow(() -> new NoTaskFoundException(task.getId()));
		foundTask.updateFields(task);
		return repository.save(foundTask);
	}

	@Override
	public void deleteTaskById(int id) {
		try {
			repository.deleteById(id);
		} catch(EmptyResultDataAccessException ex) {
			throw new NoTaskFoundException(id);
		}
	}

	@Override
	public Task lowerPriority(int id) {
		Task task = repository.findById(id)
				.orElseThrow(() -> new NoTaskFoundException(id));

		if(task.getPriority() != Priority.LOW) {
			task.setPriority(task.getPriority().previous());
			return repository.save(task);
		}
		
		throw new OperationNotAllowedException("Cannot lower priority on Task with id " + id);
	}

	@Override
	public Task raisePriority(int id) {
		Task task = repository.findById(id)
				.orElseThrow(() -> new NoTaskFoundException(id));

		if(task.getPriority() != Priority.CRITICAL) {
			task.setPriority(task.getPriority().next());
			return repository.save(task);
		}
		
		throw new OperationNotAllowedException("Cannot raise priority on Task with id " + id);
	}

}
