(ns ducks.routes.home
  (:require [compojure.core :refer :all]
            [hiccup.element :as hiccup-elements]
            [ducks.views.layout :as layout]
            [ducks.models.todo :refer [fetch-todos]]))

(defn home [todos]
  (layout/common [:h1 "Stuff To Do"]
                 [:ul
                  (map (fn [{:keys [text doneness]}]
                         [:li
                          [:div {:class "text"} text]
                          [:div {:class "doneness"} doneness]])
                       todos)
                  ]
                 ))

(defroutes home-routes
  (GET "/" [] (home (fetch-todos))))
