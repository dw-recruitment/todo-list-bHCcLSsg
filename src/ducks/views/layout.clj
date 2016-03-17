(ns ducks.views.layout
  "Namespace for common layout functions."
  (:require [hiccup.page :refer [html5 include-css]]))

(defn common
  "The html and css common to all pages."
  [& body]
  (html5
    [:head
     [:title "Welcome to ducks"]
     (include-css "/css/screen.css")]
    [:body body]))
