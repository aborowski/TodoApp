package com.borowski.services;

import java.util.List;

import com.borowski.models.Task;

public interface TaskService {
	List<Task> getTasks();
	
	Task getTaskById(int id);
	
	Task createTask(Task task);
	
	Task replaceTask(Task task);
	
	Task updateTask(Task task);
	
	void deleteTaskById(int id);
	
	Task lowerPriority(int id);
	
	Task raisePriority(int id);
}
