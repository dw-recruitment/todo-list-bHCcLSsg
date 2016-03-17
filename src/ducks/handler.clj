(ns ducks.handler
  "Main handler, pulling together domain wrappers and defaults."
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ducks.routes.todo-list :refer [todo-list-routes]]
            [ducks.routes.about :refer [about-routes]]
            [ducks.routes.home :refer [home-routes]]
            [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn init
  "Initializes the app."
  []
  (println "ducks is starting"))

(defn destroy
  "Shuts down the app."
  []
  (println "ducks is shutting down"))

(defroutes app-routes
  "Defines routes for resources and not-found."
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  "The primary route, pulling together domain, defaults, and middleware."
  (-> (routes about-routes  todo-list-routes home-routes app-routes)
      (wrap-defaults site-defaults)
      (wrap-base-url)))
