
-- name: todo-list-create-sql!
-- Inserts a new todo-list into todo_lists
insert into todo_lists
(uuid, description)
values
(:uuid, :description)

-- name: todo-list-update-sql!
-- Updates a todo-list in todo_lists
update todo_lists
set description = :description
where uuid = :uuid

-- name: todo-list-delete-sql!
-- Takes {:uuid <str>}
-- Deletes todo-lists  from todo_lists, whose uuid = :uuid.
-- Does not remove todos or todo-list-entries
delete from todo_lists
where uuid = :uuid

-- name: todo-list-entries-delete-sql!
-- Takes {:uuid <str>}
-- Deletes todo-list-entries whose todo_list_uuid = :uuid.
delete from todo_list_entries
where todo_list_entries.todo_list_uuid = :uuid;

-- name: todo-delete-orphans-sql!
-- Deletes todos without todo_list_entries
delete from todos
where todos.uuid not in (select todo_list_entries.todo_uuid from todo_list_entries);


-- name: todo-list-find-sql
-- Returns all of the todo-lists in the database
select * from todo_lists

-- name: todo-list-find-by-uuid-sql
-- Takes {:uuid <str>}
-- Returns the todo_list items whose uuid matchs :uuid.
select * from todo_lists
where uuid = :uuid
