(ns ducks.models.todo
  "Namespace for functions relating to todos."
  (:require [config.db-config :refer [db]]
            [clojure.java.jdbc :as jdbc]
            [ducks.models.todo-list-entry :refer
             [todo-list-entries-delete-by-todo-sql!
              todo-list-entry-create!]]
            [yesql.core :refer [defquery]]))

(defquery todo-create-sql!
  "ducks/models/sql/todo.sql" {:connection db})

(defquery todo-find-sql
  "ducks/models/sql/todo.sql" {:connection db})

(defquery todo-update-sql!
  "ducks/models/sql/todo.sql" {:connection db})

(defquery todo-delete-sql!
  "ducks/models/sql/todo.sql" {:connection db})

(defquery todo-find-by-uuid-sql
  "ducks/models/sql/todo.sql" {:connection db})

;; This should be pulled from the db.
(def legal-todo-doneness "The current legal doneness states."
  #{"todo" "done"})

;; In practice, it would make more sense to to use strings
;; instead of a java UUID
(defn make-todo
  "Helper for making todos."
  ([text] (make-todo text "todo"))
  ([text doneness] {:text text
                    :doneness doneness
                    :uuid (java.util.UUID/randomUUID)
                    :create_datetime (str (java.time.LocalDateTime/now))})
  ([text doneness uuid-str] {:text text
                             :doneness doneness
                             :uuid (java.util.UUID/fromString uuid-str)
                             :create_datetime (str
                                               (java.time.LocalDateTime/now))}))

(defn validate-todo
  "Checks that text, doneness, and uuid values are present and of the
  right type.  Fails an assert or returns the todo."
  [{doneness :doneness uuid :uuid :as todo}]
  (assert (legal-todo-doneness doneness)
          (str doneness
               " is not in the set of valid donenesss: "
               legal-todo-doneness))
  (assert (not (nil? uuid)) (str "uuid must not be null"))
  (assert (= java.util.UUID (class uuid))
          (str "uuid ought to be a java.util.UUID but "
               uuid " is a "
               (class uuid)))
  todo)

(defn convert-uuid-to-string
  "Return a todo whose uuid has been replaced by the string
  representation."
  [todo]
  (update-in todo [:uuid] str))

(defn convert-uuid-from-string
  "Returns a todo whose uuid string has been replaced with a real UUID"
  [todo]
  (update-in todo [:uuid] #(java.util.UUID/fromString %)))

(defn save-todo!
  "Saves a todo to the database."
  [todo todo-list-uuid]
  (jdbc/with-db-transaction [tx db]
    (-> todo
        validate-todo
        convert-uuid-to-string
        todo-create-sql!)
    (todo-list-entry-create! todo todo-list-uuid)
  todo))


(defn fetch-todos
  "Retrieves the todos from the database and converts the UUID strings
  into real UUIDs"
  []
  (map convert-uuid-from-string (todo-find-sql)))

(defn fetch-todos-by-todo-list-uuid
  "Returns the todos for a given todo-list-uuid"
  [todo-list-uuid]
  (todo-find-by-uuid-sql {:todo_list_uuid todo-list-uuid}))

(defn todo-update!
  "Updates a todo in the database."
  [todo]
  (todo-update-sql! todo))

(defn todo-delete!
  "Deletes a todo from the database."
  [todo]
  (jdbc/with-db-transaction [tx db]
    (todo-list-entries-delete-by-todo-sql! todo)
    (todo-delete-sql! todo)))
