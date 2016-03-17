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

  )

