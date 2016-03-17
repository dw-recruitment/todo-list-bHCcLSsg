
-- name: todo-list-entries-create-sql!
-- Inserts a new todo-list-entry into db
insert into todo_list_entries
(todo_list_uuid, todo_uuid)
values
(:todo_list_uuid, :todo_uuid)

-- name: todo-list-entries-delete-by-todo-sql!
-- deletes a todo-list-entry, taking a todo
delete from todo_list_entries
where
todo_uuid = :uuid


-- name: todo-list-entries-delete-by-todo-list-sql!
-- Deletes todo-list-entries for a test-list
delete from todo_list_entries
where todo_list_entries.todo_list_uuid = :uuid;

