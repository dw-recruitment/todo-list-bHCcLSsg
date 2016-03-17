
-- name: todo-create-sql!
-- Takes {:uuid <str>, :text <str>, :create_datetime <date>}
-- Inserts into the todos table.
insert into todos
(uuid, text, doneness, create_datetime)
values
(:uuid, :text, :doneness, :create_datetime)

-- name: todo-find-sql
-- Returns all elements in the todos table.
select * from todos

-- name: todo-delete-sql!
-- Takes {:uuid <str>}
-- Deletes from todos table elements with matching uuid.
delete from todos
where uuid = :uuid

-- name: todo-update-sql!
-- Takes {:uuid <str> :text <str> :doneness <str>}
-- Updates text and doneness of the element in todos
-- whose uuid matches :uuid.
update todos
set text = :text,
    doneness = :doneness
where uuid = :uuid

-- name: todo-find-by-uuid-sql
-- Returns the todos for a given todo-list uuid
select * from todos
where uuid in
  (select todo_uuid from todo_list_entries
    where todo_list_uuid = :todo_list_uuid)
order by create_datetime
