-- updates the db version number, used by drift migrations
update db_version
set version = :version + 1
