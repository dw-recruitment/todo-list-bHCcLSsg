
-- name: alter-todos-add-create-datetime-sql!
-- Adds create_datetime - DATETIME to todos table.
ALTER TABLE todos ADD COLUMN create_datetime DATETIME 

-- name: initialize-legacy-todo-dates-sql!
-- Populates prexisting todos with a create_datetime of 'now'.
update todos set create_datetime = (datetime('now', 'localtime'))

