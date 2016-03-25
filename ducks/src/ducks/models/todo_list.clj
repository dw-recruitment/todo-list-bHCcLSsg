(ns ducks.models.todo-list
  "Namespace for functions relating to todo-list."
  (:require [config.db-config :refer [db]]
            [ducks.models.todo-list-entry :refer
             [todo-list-entries-delete-by-todo-sql!
              todo-list-entry-create!]]
            [yesql.core :refer [defquery]]
            [clojure.java.jdbc :as jdbc]))

(defquery todo-list-create-sql!
  "ducks/models/sql/todo_list.sql" {:connection db})

(defquery todo-list-update-sql!
  "ducks/models/sql/todo_list.sql" {:connection db})

(defquery todo-list-delete-sql!
  "ducks/models/sql/todo_list.sql" {:connection db})

(defquery todo-delete-orphans-sql!
  "ducks/models/sql/todo_list.sql" {:connection db})

(defquery todo-list-find-sql
  "ducks/models/sql/todo_list.sql" {:connection db})

(defquery todo-list-find-by-uuid-sql
  "ducks/models/sql/todo_list.sql" {:connection db})

(defn todo-list-purge!
  "Deletes a todo-list and all associated todos from the database."
  [todo-list]
  (jdbc/with-db-transaction [tx db]
    (todo-list-entries-delete-sql! todo-list)
    (todo-list-delete-sql! todo-list)
    (todo-delete-orphans-sql!)))

(defn make-todo-list
  "Return a new todo-list."
  [desc]
  {:uuid (str (java.util.UUID/randomUUID)) :description desc})

(defn make-and-persist-todo-list!
  "Takes a description, saves a new todo-list to db, and returns it."
  [desc]
  (let [todo-list (make-todo-list desc)]
    (todo-list-create-sql! todo-list)
    todo-list))

(defn todo-list-find-all
  "Returns all of the todo-lists from the database."
  []
  (todo-list-find-sql))

(defn todo-list-find-by-uuid
  "Returns the todo-list from the database with matching uuid."
  [uuid]
  (first (todo-list-find-by-uuid-sql {:uuid uuid})))
