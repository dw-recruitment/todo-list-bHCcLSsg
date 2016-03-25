
-- name: todo-lists-create!
-- Create the todo-lists table.
--  uuid - text
--  description - text
CREATE TABLE todo_lists(uuid TEXT PRIMARY KEY, description TEXT);

-- name: todo-list-entries-create!
-- Create the todo_list_entries table
--  todo_list_uuid - text (foreign key, todo_lists.uuid)
--  todo_uuid - text (foreign key, todo.uuid)
CREATE TABLE todo_list_entries(todo_list_uuid TEXT, todo_uuid TEXT, FOREIGN KEY(todo_list_uuid) REFERENCES todo_lists(uuid), FOREIGN KEY(todo_uuid) REFERENCES todos(uuid));

-- name: todo-lists-populate!
-- Populates '00000000-0000-0000-0000-000000000000', 'legacy unlisted todos' into todo_list_table.
INSERT INTO todo_lists(uuid, description) values ('00000000-0000-0000-0000-000000000000', 'legacy unlisted todos');

-- name: todo-list-entries-populate!
-- Associates pre-existing todos with the todo_list '00000000-0000-0000-0000-000000000000'.
INSERT INTO todo_list_entries(todo_list_uuid, todo_uuid) select '00000000-0000-0000-0000-000000000000', uuid from todos;
