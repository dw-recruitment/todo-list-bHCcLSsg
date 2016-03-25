(ns ducks.routes.todo-list
  "Namespace for 'todo-list' routes."
  (:require [compojure.core :refer :all]
            [hiccup.page :as page]
            [hiccup.form :as form]
            [hiccup.element :as hiccup-elements]
            [ducks.views.layout :as layout]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ducks.services.rabbit :refer [tell-rabbit!]]
            [ducks.models.todo-list :refer [todo-list-find-by-uuid]]
            [ducks.models.todo :refer [convert-uuid-from-string
                                       make-todo
                                       save-todo!
                                       todo-delete!
                                       todo-update!
                                       validate-todo
                                       fetch-todos-by-todo-list-uuid]]))


(defn next-doneness
  "The next possible doneness state for a given doneness."
  [doneness]
  (if (= doneness  "done")
    "todo"
    "done"))

(defn next-doneness-label
  "The label for the next possible doneness state for some doneness."
  [doneness]
  (if (= doneness "done")
    "undo"
    "complete"))


(defn enbutton-doneness
  "Creates a button for changing todo state."
  [uuid doneness text todo-list-uuid]
  (form/form-to
   [:put (str  "/list-uuid/" todo-list-uuid)]
   (anti-forgery-field)
   (form/hidden-field "uuid" uuid)
   (form/hidden-field "doneness" (next-doneness doneness))
   (form/hidden-field "text" text)
   (form/submit-button (next-doneness-label doneness))))

(defn delete-button
  "Creates a button for deleting a todo."
  [uuid todo-list-uuid]
  (form/form-to
   [:delete (str  "/list-uuid/" todo-list-uuid)]
   (anti-forgery-field)
   (form/hidden-field "uuid" uuid)
   (form/submit-button "delete")))

(defn format-todo
  "Returns the html formatting a task"
  [todo-list-uuid]
  (fn [{:keys [text doneness uuid]}]
    [:li
     [:div {:class (str "text " doneness)} text]
     [:div
      {:class "doneness"}
      doneness
      (enbutton-doneness uuid doneness text todo-list-uuid)
      (delete-button uuid todo-list-uuid)]]))

(defn todo-list
  "Takes the todos, the todo-list-uuid, and the todo-list-description
and generates the page html."
  [todos todo-list-uuid description]
  (layout/common
   (page/include-css "/css/todo.css")
   [:p (hiccup-elements/link-to "/" "Never mind, too many things to do.")]
   [:p
    (form/form-to
     [:post (str  "/list-uuid/" todo-list-uuid)]
     "Text: " (form/text-field "text")
     "Doneness: " (form/text-field "doneness")
     (anti-forgery-field)
     (form/submit-button "Submit"))]
   [:h2 "Stuff To Do: " description]
   [:ul (map (format-todo todo-list-uuid) todos)]))

(defn fetch-todos
  "Helper to extract the uuid from the params and get the todos from db."
  [params]
  (fetch-todos-by-todo-list-uuid (:todo-list-uuid params)))

(defn fetch-list-description
  "Helper to extract the uuid from the params and get the description."
  [params]
  (let [uuid (:todo-list-uuid params)]
      (:description (todo-list-find-by-uuid uuid))))

(defroutes todo-list-routes
  "Routes for the 'todo-list' page.  Supports GET, DELETE, PUT, and POST."
  (GET "/list-uuid/:todo-list-uuid" {params :params}
       (todo-list (fetch-todos params)
                  (:todo-list-uuid params)
                  (fetch-list-description params)))

  (DELETE "/list-uuid/:todo-list-uuid" {params :params}
          (do (todo-delete!
               (select-keys params [:uuid]))
              (tell-rabbit! {:action :delete
                             :type :todo
                             :uuid (:uuid params)})
              (todo-list (fetch-todos params)
                         (:todo-list-uuid params)
                         (fetch-list-description params))))

  (PUT "/list-uuid/:todo-list-uuid" {params :params}
       (let [todo
             (select-keys params [:uuid :text :doneness])]
         (validate-todo (convert-uuid-from-string todo))
         (todo-update! todo)
         (tell-rabbit! (merge {:action :update
                               :type :todo}
                              todo))
         (todo-list (fetch-todos params)
                    (:todo-list-uuid params)
                    (fetch-list-description params))))

  (POST "/list-uuid/:todo-list-uuid" {params :params}
        (let [todo  (save-todo! (validate-todo
                                 (make-todo (:text params)
                                            (:doneness params)))
                                (:todo-list-uuid params))]
          (tell-rabbit! (merge {:action :post
                                :type :todo}
                               todo))
          (todo-list (fetch-todos params)
                     (:todo-list-uuid params)
                     (fetch-list-description params)))))
