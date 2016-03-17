(ns ducks.routes.home
  (:require [compojure.core :refer :all]
            [hiccup.page :as page]
            [hiccup.form :as form]
            [hiccup.element :as hiccup-elements]
            [ducks.views.layout :as layout]
            [ducks.models.todo :refer [fetch-todos save-todo! make-todo]]))

(defn home [todos]
  (layout/common
   [:p
    (form/form-to
     [:post "/"]
     "Text: " (form/text-field "text")
     "Doneness: " (form/text-field "doneness")
     (form/submit-button "Submit"))
    ]
   [:h2 "Stuff To Do"]
   [:ul
    (map (fn [{:keys [text doneness]}]
           [:li
            [:div {:class "text"} text]
            [:div {:class "doneness"} doneness]])
         todos)
    ]
   ))

(defroutes home-routes
  (GET "/" [] (home (fetch-todos)))
  (POST "/" {params :params} (do (save-todo! (make-todo (:text params)
                                                        (:doneness params)))
                                 (home (fetch-todos)))))
