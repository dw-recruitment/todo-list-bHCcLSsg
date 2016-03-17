(ns ducks.models.todo
  (:require [config.db-config :refer [db]]
            [yesql.core :refer [defquery]]))

(def legal-todo-doneness #{"todo" "done"})

(defn make-todo
  "Helper for making todos."  ;; a really helpful helper would do them too.
  ([text] (make-todo text "todo"))
  ([text doneness] {:text text :doneness doneness :uuid (java.util.UUID/randomUUID)})
  ([text doneness uuid-str] {:text text :doneness doneness :uuid (java.util.UUID/fromString uuid-str)}))

(defn validate-todo [{doneness :doneness uuid :uuid :as todo}]
  "Checks that text, doneness, and uuid values are present and of the right type."
  (assert (legal-todo-doneness doneness) (str doneness " is not in the set of valid donenesss: " legal-todo-doneness))
  (assert (not (nil? uuid)) (str "uuid must not be null"))
  (assert (= java.util.UUID (class uuid)) (str "uuid ought to be a java.util.UUID but " uuid " is a " (class uuid)))
  todo)

(defn convert-uuid-to-string [todo]
  "Return a todo whose uuid has been replaced by the string representation"
  (update-in todo [:uuid] #(.toString %)))

(defn convert-uuid-from-string [todo]
  "Returns a todo whose uuid string has been replaced with a real UUID"
  (update-in todo [:uuid] #(java.util.UUID/fromString %)))

(defquery insert-todo! "ducks/models/sql/insert_todo.sql" {:connection db})

(defn save-todo! [todo]
  "Saves a todo to the database."
  (-> todo
      validate-todo
      convert-uuid-to-string
      insert-todo!
      ))

(defquery retrieve-todos "ducks/models/sql/retrieve_todos.sql" {:connection db})

(defn fetch-todos []
  "Retrieves the todos from the database and converts the UUID strings into real UUIDs"
  (map convert-uuid-from-string (retrieve-todos)))


(defquery todo-update-sql! "ducks/models/sql/todo_update_sql.sql" {:connection db})

(defn todo-update! [todo]
  (todo-update-sql! todo))

(defquery todo-delete-sql! "ducks/models/sql/todo_delete_sql.sql" {:connection db})

(defn todo-delete! [todo]
  (todo-delete-sql! todo))


