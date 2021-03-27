package com.borowski.models.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.borowski.controllers.rest.UserRestController;
import com.borowski.models.User;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
	@Override
	public EntityModel<User> toModel(User entity) {
		return EntityModel.of(entity,
				linkTo(methodOn(UserRestController.class).getUserById(entity.getId())).withSelfRel(),
				linkTo(methodOn(UserRestController.class).getUsers()).withRel("users"));
	}

	@Override
	public CollectionModel<EntityModel<User>> toCollectionModel(Iterable<? extends User> entities) {
		return StreamSupport.stream(entities.spliterator(), false) //
				.map(this::toModel) //
				.collect(Collectors.collectingAndThen(Collectors.toList(), entityModel -> CollectionModel
						.of(entityModel, linkTo(methodOn(UserRestController.class).getUsers()).withRel("users"))));
	}
}
