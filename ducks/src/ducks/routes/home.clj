(ns ducks.routes.home
  "Namespace for 'home' routes."
  (:require [compojure.core :refer :all]
            [hiccup.page :as page]
            [hiccup.form :as form]
            [hiccup.element :as hiccup-elements]
            [ducks.views.layout :as layout]
            [ducks.models.todo-list :refer [todo-list-find-all
                                            make-and-persist-todo-list!
                                            todo-list-purge!]]
            [ducks.services.rabbit :refer [tell-rabbit!]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))


(defn delete-button
  "Takes a uuid for the todo.  Returns html for button to delete todo."
  [uuid]
  (form/form-to
   [:delete "/"]
   (anti-forgery-field)
   (form/hidden-field "uuid" uuid)
   (form/submit-button "delete")))

(defn format-todo-list
  "Takes a todo-list and returns a link to the list-uuid page."
  [{:keys [description uuid]}]
  [:li {:class "todo-list"}
   (hiccup-elements/link-to (str "/list-uuid/" uuid) description)
   (delete-button uuid)])

(defn home
  "Takes a seq of todo-lists, generating the page."
  [todo-lists]
  (layout/common
   [:p
    [:h2 "New todo list:"]
    (form/form-to
     [:post (str  "/")]
     "Description: " (form/text-field "description")
     (anti-forgery-field)
     (form/submit-button "Submit"))]
   [:p
    [:h2 "TODO Lists"
     [:ul (map format-todo-list todo-lists)]]]))


(defroutes home-routes
  "Routes for the home (/) page.  Supports GET, DELETE, and POST."
  (GET "/" [] (home (todo-list-find-all)))
  (DELETE "/" {params :params}
          (do (todo-list-purge! (select-keys params [:uuid]))
              (tell-rabbit! {:action :delete
                             :type :todo-list
                             :uuid (:uuid params)})
              (home (todo-list-find-all))))
  (POST "/" {params :params}
        (let [todo-list  (make-and-persist-todo-list! (:description params))]
            (tell-rabbit! {:action :post
                           :type :todo-list
                           :uuid (:uuid todo-list)
                           :description (:description params)})
            (home (todo-list-find-all)))))
