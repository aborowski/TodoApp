package com.borowski.models.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.borowski.controllers.TaskRestController;
import com.borowski.models.Priority;
import com.borowski.models.Task;

@Component
public class TaskModelAssembler implements RepresentationModelAssembler<Task, EntityModel<Task>> {

	@Override
	public EntityModel<Task> toModel(Task entity) {
		EntityModel<Task> entityModel = EntityModel.of(entity,
				linkTo(methodOn(TaskRestController.class).getTaskById(entity.getId())).withSelfRel(),
				linkTo(methodOn(TaskRestController.class).getTasks()).withRel("tasks"));
		
		if(entityModel.getContent().getPriority() != Priority.LOW) {
			entityModel.add(linkTo(methodOn(TaskRestController.class).lowerPriority(entityModel.getContent().getId())).withRel("lower-priority"));
		}
		if(entityModel.getContent().getPriority() != Priority.CRITICAL) {
			entityModel.add(linkTo(methodOn(TaskRestController.class).raisePriority(entityModel.getContent().getId())).withRel("raise-priority"));
		}
		
		return entityModel;
	}

	@Override
	public CollectionModel<EntityModel<Task>> toCollectionModel(Iterable<? extends Task> entities) {
		return StreamSupport.stream(entities.spliterator(), false) //
				.map(this::toModel) //
				.collect(Collectors.collectingAndThen(Collectors.toList(), entityModel -> CollectionModel
						.of(entityModel, linkTo(methodOn(TaskRestController.class).getTasks()).withRel("tasks"))));
	}

}
