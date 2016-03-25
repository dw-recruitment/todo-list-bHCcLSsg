(ns ducks.models.todo-list-entry
  "Namespace for functions relating to todo-list-entries."
  (:require [config.db-config :refer [db]]
            [yesql.core :refer [defquery]]
            [clojure.java.jdbc :as jdbc]))



(defquery todo-list-entries-delete-by-todo-sql!
  "ducks/models/sql/todo_list_entries.sql" {:connection db})

(defquery todo-list-entries-delete-by-todo-list-sql!
  "ducks/models/sql/todo_list_entries.sql" {:connection db})

(defquery todo-list-entries-create-sql!
  "ducks/models/sql/todo_list_entries.sql" {:connection db})

(defn todo-list-entry-create!
  "Creates the todo in the database, assocating it with the
todo-list-uuid's todo-list."
  [todo todo-list-uuid]
  (todo-list-entries-create-sql! {:todo_list_uuid todo-list-uuid
                                  :todo_uuid (:uuid todo)}))
