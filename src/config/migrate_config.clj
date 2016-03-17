(ns config.migrate-config
  "Namespace for db migration functions."
  (:require [config.db-config :refer [db]]
            [yesql.core :refer [defquery]]))

(defquery db-version-find-sql
  "ducks/models/sql/db_version.sql" {:connection db})

(defquery db-version-update-sql!
  "ducks/models/sql/db_version.sql" {:connection db})

(defn get-current-version
  "Checks the current version in the database.
   Returns and int, defaulting to 0 if there's an exception."
   []
  (try (:version (first (db-version-find-sql)))
       (catch Exception e
           (.printStackTrace e)
         0)))

(defn set-current-version!
  "Takes an int."
  [i]
  (try (db-version-update-sql! {:version i})
  (catch Exception e
    (println "caught: " e))))

(defn migrate-config
  "Function returning the drift migration config.  Required for drift."
  []
  {:directory "/src/ducks/models/migrations"
   :current-version get-current-version
   :update-version set-current-version! })
