(ns ducks.routes.home
  (:require [compojure.core :refer :all]
            [hiccup.page :as page]
            [hiccup.form :as form]
            [hiccup.element :as hiccup-elements]
            [ducks.views.layout :as layout]
            [ducks.models.todo :refer
             [fetch-todos save-todo! make-todo todo-update! validate-todo convert-uuid-from-string todo-delete!]]))


(defn next-doneness [doneness]
  "The next possible doneness state for a given doneness."
  (if (= doneness  "done")
    "todo"
    "done"))

(defn next-doneness-label [doneness]
  "The label for the next possible doneness state for a given doneness."
  (if (= doneness "done")
    "undo"
    "complete"))


(defn enbutton-doneness [uuid doneness text]
  "Creates a button for changing todo state."
  (form/form-to
   [:put "/"]
   (form/hidden-field "uuid" uuid)
   (form/hidden-field "doneness" (next-doneness doneness))
   (form/hidden-field "text" text)
   (form/submit-button (next-doneness-label doneness))))

(defn delete-button [uuid]
  "Creates a button for deleting a todo."
  (form/form-to
   [:delete "/"]
   (form/hidden-field "uuid" uuid)
   (form/submit-button "delete")))

(defn format-todo [{:keys [text doneness uuid]}]
  "Returns the html formatting a task"
  [:li
   [:div {:class (str "text " doneness)} text]
   [:div {:class "doneness"} doneness (enbutton-doneness uuid doneness text) (delete-button uuid)]])


(defn home [todos]
  (layout/common
   (page/include-css "/css/todo.css")
   [:p
    (form/form-to
     [:post "/"]
     "Text: " (form/text-field "text")
     "Doneness: " (form/text-field "doneness")
     (form/submit-button "Submit"))
    ]
   [:h2 "Stuff To Do"]
   [:ul
    (map format-todo todos)
    ]
   ))

(defroutes home-routes
  (GET "/" [] (home (fetch-todos)))
  (DELETE "/" {params :params} (do (todo-delete! (select-keys params [:uuid]))
                                   (home (fetch-todos))))
  (PUT "/" {params :params} (let [todo (select-keys params [:uuid :text :doneness])]
                              (validate-todo (convert-uuid-from-string todo))
                              (todo-update! todo)
                              (home (fetch-todos))))
  (POST "/" {params :params} (do (save-todo! (validate-todo
                                              (make-todo (:text params)
                                                         (:doneness params))))
                                 (home (fetch-todos)))))
