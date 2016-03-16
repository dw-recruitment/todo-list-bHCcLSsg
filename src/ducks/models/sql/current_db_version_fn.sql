-- returns the db version number, used by drift migrations
select *
from db_version
limit 1
