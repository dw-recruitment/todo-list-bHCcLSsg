(ns ducks.routes.about
  (:require [compojure.core :refer :all]
            [ducks.views.layout :as layout]))

(defn about []
  (layout/common [:h1 "About Ducks!"]
                 [:p 
                  " This assignment is designed to gauge how well you know or can pick up "
                  " Clojure and some common libraries to build a simple web application. "
                  ]
                 [:p 
                  " You should spend no more than a week on it. In fact, it is due one "
                  " week after it is assigned. You should also focus on depth rather than "
                  " breadth. Do as much good work as you can on each step rather than "
                  " trying to do a little work on more steps. "
                  ]
                 [:p 
                  " Since we want to evaluate all of these projects anonymously, you "
                  " should not include any identifying data in the code (your name, city, "
                  " email address, phone number, school, etc.). We will also not be able "
                  " to give feedback on it until the recruitment process is over. "
                  ]
                 [:p
                  " However, you may ask questions of the Democracy Works employee that "
                  " you received the assignment from (and them only since the others will "
                  " be evaluating all of the exercises and should know as little as "
                  " possible about which project belongs to you). "
                  ]
                 ))

(defroutes about-routes
  (GET "/about" [] (about)))
