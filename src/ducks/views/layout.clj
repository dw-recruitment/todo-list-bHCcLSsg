(ns ducks.views.layout
  (:require [hiccup.page :refer [html5 include-css]]))

(defn common [& body]
  (html5
    [:head
     [:title "Welcome to ducks"]
     (include-css "/css/screen.css")]
    [:body body]))
