(ns ducks.test.handler
  (:require [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [compojure.core :refer [routes]]
            [compojure.route :as route]
            [ducks.routes.todo-list :refer [todo-list-routes]]
            [ducks.routes.about :refer [about-routes]])
  (:use clojure.test
        ring.mock.request
        ducks.handler))

(deftest test-app
  (testing "main route"
    (let [req (request :get "/list-uuid/UUID")
          response (app req)]
      (is (= (:status response) 200))
      (is (.contains (:body response) "Stuff To Do"))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404))))

  (testing "about route"
    (let [response (app (request :get "/about"))]
      (is (= (:status response) 200))
      (is (.contains (:body response) "About Ducks!")))))


;; constructing the app out of custom routes, excluding anti-forgery
(deftest test-dodo-post
  (testing "testing that posting a todo to / calls database functions"
    (let [save-todo-called (atom [])
          fetch-todos-by-todo-list-uuid-called (atom [])
          rabbit-told (atom {})]
      (with-redefs [ducks.services.rabbit/tell-rabbit!
                    (fn [msg] (compare-and-set! rabbit-told {} msg))
                    ducks.models.todo/save-todo!
                    (fn [todo uuid]
                      (swap! save-todo-called conj [todo uuid])
                      todo)
                    ducks.models.todo/fetch-todos-by-todo-list-uuid
                    (fn [uuid] (swap!
                                fetch-todos-by-todo-list-uuid-called
                                conj
                                uuid)
                      [])]
        (let [;; constructing the request
              req
              (request :post
                       "/list-uuid/UUID"
                       {:text "prose"
                        :doneness "todo"})

              ;; building custom app, excluding anti-forgery
              custom-app
              (-> (routes about-routes todo-list-routes app-routes)
                  (wrap-keyword-params)
                  (wrap-params))

              ;; calling app on request to get response
              response (custom-app req)

              ;; deconstructing the arguments passed to save-todo!
              [{:keys [text doneness]} uuid]
              (first @save-todo-called)]

          (is (= (:status response) 200))
          (is (= text "prose"))
          (is (= doneness "todo"))
          (is (first @fetch-todos-by-todo-list-uuid-called))
          (is (= (:action @rabbit-told) :post))
          (is (= (:type @rabbit-told) :todo)))))))


;; constructing the app out of custom routes, excluding anti-forgery
(deftest test-todo-delete
  (testing "testing that posting a todo to / calls database functions"
    (let [todo-delete-called (atom [])
          rabbit-told (atom {})]
      (with-redefs [ducks.services.rabbit/tell-rabbit!
                    (fn [msg] (compare-and-set! rabbit-told {} msg))
                    ducks.models.todo/todo-delete!
                    (fn [arg] (swap! todo-delete-called conj arg))]
        (let [custom-app
              (-> (routes about-routes todo-list-routes app-routes)
                  (wrap-keyword-params)
                  (wrap-params))
              req  (request :delete "/list-uuid/UUID" {:uuid "uuid"})
              response (custom-app req)]
          (is (= (:status response) 200))
          (is (= (:action @rabbit-told) :delete))
          (is (= (:type @rabbit-told) :todo))
          (is (= {:uuid  "uuid"}  (first @todo-delete-called))))))))
