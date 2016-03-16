(ns config.migrate-config
  (:require [config.db-config :refer [db]]
            [yesql.core :refer [defquery]]))

;; arguments: <none>
;; returns: sequence of hashmaps, ought to be a single entry
;; sample return: (:version 0)
(defquery current-db-version "ducks/models/sql/current_db_version_fn.sql" {:connection db})


(defquery update-db-version! "ducks/models/sql/update_db_version_fn.sql" {:connection db})


;; TODO; this needs more testing
(defn get-current-version []
  "Checks the current version in the database, defaulting to zero if there's an exception."
  (try (:version (first (current-db-version)))
       (catch Exception e
           (.printStackTrace e)
         0)))


;; TODO; completely untested.  :(
(defn set-current-version! [i]
  (update-db-version! i))


(defn migrate-config []
  {:directory "/src/ducks/models/migrations"
   :current-version get-current-version
   :update-version set-current-version! })
