(ns ducks.models.migrations.000-migration)

(defn up []
  ["CREATE TABLE db_version(version INTEGER PRIMARY KEY)"]
  ["INSERT into db_version values(0)"]
  ["CREATE TABLE doneness(state TEXT PRIMARY KEY)"]
  ["INSERT into doneness values('todo')"]
  ["INSERT into doneness values('done')"]
  ["CREATE TABLE todos(uuid TEXT PRIMARY KEY, text TEXT, doneness TEXT, FOREIGN KEY(doneness) REFERENCES doneness(state))"]
  )

(defn down []
  ["DROP TABLE db_version"]
  )
