(ns ducks.routes.home
  (:require [compojure.core :refer :all]
            [hiccup.element :as hiccup-elements]
            [ducks.views.layout :as layout]))

(defn home []
  (layout/common [:h1 "Under Construction!"]
                 ;; Image shamelessly stolen from
                 ;; http://www.cheops-pyramide.ch/khufu-pyramid/great-pyramid/khufu-construction.gif
                 (hiccup-elements/image "img/khufu-construction.gif" "Image of Khufu Pyramid under construction")
                 ))

(defroutes home-routes
  (GET "/" [] (home)))
