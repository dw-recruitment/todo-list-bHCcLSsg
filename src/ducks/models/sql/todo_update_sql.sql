update todos
set text = :text,
    doneness = :doneness
where uuid = :uuid
