(ns ducks.test.handler
  (:use clojure.test
        ring.mock.request
        ducks.handler))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (.contains (:body response) "Stuff To Do"))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404))))

  (testing "about route"
    (let [response (app (request :get "/about"))]
      (is (= (:status response) 200))
      (is (.contains (:body response) "About Ducks!"))))


  ;; Posting is inherently side-effecting but it would be neat testing this without
  ;; with-redefs or atoms
  (testing "testing that posting a todo to / calls database functions"
    (let [save-todo-called (atom [])
          fetch-todos-called (atom [])]
      (with-redefs [ducks.models.todo/save-todo! (fn [arg] (swap! save-todo-called conj arg))
                    ducks.models.todo/fetch-todos (fn [] (swap! fetch-todos-called conj true) [])]
        (let [response (app (request :post "/" {:text "prose" :doneness "todo"}))
              {:keys [text doneness]} (first @save-todo-called)]
          (is (= (:status response) 200))
          (is (= text "prose"))
          (is (= doneness "todo"))
          (is (first @fetch-todos-called))))))

  (testing "testing that posting a todo to / calls database functions"
    (let [todo-delete-called (atom [])]
      (with-redefs [ducks.models.todo/todo-delete! (fn [arg] (swap! todo-delete-called conj arg))]
        (let [response (app (request :delete "/" {:uuid "uuid"}))]
          (is (= (:status response) 200))
          (is (= {:uuid  "uuid"}  (first @todo-delete-called))))))))

