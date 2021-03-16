CREATE SCHEMA IF NOT EXISTS `demodb` ;

create table entity_metadata (id integer not null, created_at timestamp not null default current_timestamp, updated_at timestamp on update current_timestamp, primary key (id)) engine=InnoDB;

create table hibernate_sequences (sequence_name varchar(255) not null, next_val bigint, primary key (sequence_name)) engine=InnoDB;

insert into hibernate_sequences(sequence_name, next_val) values ('default',0);

create table todo (id integer not null, created_at timestamp not null default current_timestamp, updated_at timestamp on update current_timestamp, description varchar(255), priority integer, deleted tinyint default 0, deleted_at datetime(6), title varchar(255), user_id integer, primary key (id)) engine=InnoDB;

create table todo_user (todo_id integer not null, watchers_id integer not null) engine=InnoDB;

create table user (id integer not null, created_at timestamp not null default current_timestamp, updated_at timestamp on update current_timestamp, email varchar(255), deleted tinyint default 0, deleted_at datetime(6), username varchar(255), primary key (id)) engine=InnoDB;

alter table todo add constraint FK_USER_ID foreign key (user_id) references user (id);

alter table todo_user add constraint FK_WATCHER_ID foreign key (watchers_id) references user (id);

alter table todo_user add constraint FK_TODO_ID foreign key (todo_id) references todo (id);