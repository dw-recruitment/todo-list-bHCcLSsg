(ns ducks.models.migrations.003-migration
  "Namespace for bringing db to version 3."
  (:require [config.db-config :refer [db]]
            [yesql.core :refer [defquery]]))

(defquery  alter-todos-add-create-datetime-sql!
  "ducks/models/sql/migrations/up_003.sql" {:connection db})

(defquery initialize-legacy-todo-dates-sql!
  "ducks/models/sql/migrations/up_003.sql" {:connection db})

(defn up
  "Function for bringing db up to version 3."
  []
  (initialize-legacy-todo-dates-sql!))

(defn down
  "Function for bringing db down to version 2.  Doesn't do anything."
  []
  ;; no going back.  sqlite doesn't support dropping columns.
  )
