
-- name: db-version-find-sql
-- returns the first row, {:version <int>}
select *
from db_version
limit 1

-- name: db-version-update-sql!
-- updates the db version number, used by drift migrations
update db_version
set version = :version
