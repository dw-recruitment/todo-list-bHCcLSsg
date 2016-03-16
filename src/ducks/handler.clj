(ns ducks.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ducks.routes.home :refer [home-routes]]
            [ducks.routes.about :refer [about-routes]]))

(defn init []
  (println "ducks is starting"))

(defn destroy []
  (println "ducks is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes about-routes home-routes app-routes)
      (handler/site)
      (wrap-base-url)))
