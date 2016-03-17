
-- name: db-version-create!
-- Create the db_version table
--  version - integer
CREATE TABLE db_version(version INTEGER PRIMARY KEY);

-- name: db-version-populate!
-- Initialize the db_version table with a zero.
INSERT into db_version values(0);

-- name: doneness-create!
-- Create the doneness table.
--  state - text, primary key
CREATE TABLE doneness(state TEXT PRIMARY KEY);

-- name: doneness-populate-pt1!
-- Populate the doneness table with 'todo'
INSERT into doneness values('todo');

-- name: doneness-populate-pt2!
-- Populate the doneness table with 'done'
INSERT into doneness values('done');

-- name: todos-create!
-- Create the todos table
--  uuid - text
--  text - text
--  doneness - text (foreign key, doneness.state)
CREATE TABLE todos(uuid TEXT PRIMARY KEY, text TEXT, doneness TEXT, FOREIGN KEY(doneness) REFERENCES doneness(state));
