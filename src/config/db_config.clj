(ns config.db-config
  "Namespace for storing database connection information.")

(def db
  "Connection information for db/ducks.db sqlite."
  {:classname   "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname     "db/ducks.db"})
