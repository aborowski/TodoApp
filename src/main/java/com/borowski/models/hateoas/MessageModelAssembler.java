package com.borowski.models.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.borowski.controllers.MessageRestController;
import com.borowski.controllers.TaskRestController;
import com.borowski.models.Message;
import com.borowski.models.Task;

@Component
public class MessageModelAssembler implements RepresentationModelAssembler<Message, EntityModel<Message>> {
	@Override
	public EntityModel<Message> toModel(Message entity) {
		return EntityModel.of(entity,
				linkTo(methodOn(MessageRestController.class).getMessageById(entity.getId())).withSelfRel(),
				linkTo(methodOn(MessageRestController.class).getMessages()).withRel("messages"));
	}
	
	@Override
	public CollectionModel<EntityModel<Message>> toCollectionModel(Iterable<? extends Message> entities) {
		return StreamSupport.stream(entities.spliterator(), false) //
				.map(this::toModel) //
				.collect(Collectors.collectingAndThen(Collectors.toList(), 
						entityModel -> CollectionModel.of(entityModel, linkTo(methodOn(MessageRestController.class).getMessages()).withRel("messages"))));
	}
}
