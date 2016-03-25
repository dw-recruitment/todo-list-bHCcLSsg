(ns ducks.models.migrations.001-migration
  "Namespace for the first db migration."
  (:require [config.db-config :refer [db]]
            [yesql.core :refer [defquery]]))

(defquery db-version-create!
  "ducks/models/sql/migrations/up_001.sql" {:connection db})

(defquery db-version-populate!
  "ducks/models/sql/migrations/up_001.sql" {:connection db})

(defquery doneness-create!
  "ducks/models/sql/migrations/up_001.sql" {:connection db})

(defquery doneness-populate-pt1!
  "ducks/models/sql/migrations/up_001.sql" {:connection db})

(defquery doneness-populate-pt2!
  "ducks/models/sql/migrations/up_001.sql" {:connection db})

(defquery todos-create!
  "ducks/models/sql/migrations/up_001.sql" {:connection db})

(defquery db-version-drop!
  "ducks/models/sql/migrations/down_001.sql" {:connection db})

(defquery doneness-drop!
  "ducks/models/sql/migrations/down_001.sql" {:connection db})

(defquery todos-drop!
  "ducks/models/sql/migrations/down_001.sql" {:connection db})

(defn up
  "Function for bringing db up to version 1."
  []
  (db-version-create!)
  (db-version-populate!)
  (doneness-create!)
  (doneness-populate-pt1!)
  (doneness-populate-pt2!)
  (todos-create!))

(defn down
  "Function for bringing db down to level 0."
  []
  (doneness-drop!)
  (todos-drop!)
  (db-version-drop!))
