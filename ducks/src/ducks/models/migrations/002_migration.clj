(ns ducks.models.migrations.002-migration
  "Namespace for migration bringing db to version 2."
  (:require [config.db-config :refer [db]]
            [yesql.core :refer [defquery]]))

(defquery todo-lists-create!
"ducks/models/sql/migrations/up_002.sql" {:connection db})

(defquery todo-list-entries-create!
"ducks/models/sql/migrations/up_002.sql" {:connection db})

(defquery todo-lists-populate!
"ducks/models/sql/migrations/up_002.sql" {:connection db})

(defquery todo-list-entries-populate!
"ducks/models/sql/migrations/up_002.sql" {:connection db})


(defquery todo-list-entries-drop!
"ducks/models/sql/migrations/down_002.sql" {:connection db})

(defquery todo-lists-drop!
"ducks/models/sql/migrations/down_002.sql" {:connection db})


(defn up
  "Function for bringing db up to version 2."
  []
  (todo-lists-create!)
  (todo-list-entries-create!)
  (todo-lists-populate!)
  (todo-list-entries-populate!))

(defn down
  "Function for brining db down to version 1."
  []
  (todo-list-entries-drop!)
  (todo-lists-drop!))
