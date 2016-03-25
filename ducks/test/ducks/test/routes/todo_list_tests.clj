(ns ducks.test.routes.todo-list-tests
  (:use clojure.test
        ring.mock.request
        ducks.routes.todo-list))


(deftest test-next-doneness
  (testing "todo"
    (let [result (next-doneness "todo")]
      (is (= result "done"))))
  (testing "done"
    (let [result (next-doneness "done")]
      (is (= result "todo")))))


(deftest test-next-doneness-label
  (testing "todo"
    (let [result (next-doneness-label "todo")]
      (is (= result "complete"))))
  (testing "done"
    (let [result (next-doneness-label "done")]
      (is (= result "undo")))))

(deftest test-enbutton
  (testing "done"
    (let [uri (java.net.URI. "/list-uuid/UUID")
          expected [:form {:method "POST", :action uri}
                    [:input {:type "hidden",
                             :name "_method",
                             :id "_method",
                             :value "PUT"}]
                    [:input {:type "hidden",
                             :name "uuid",
                             :id "uuid",
                             :value "uuid"}]
                    [:input {:type "hidden",
                             :name "doneness",
                             :id "doneness",
                             :value "todo"}]
                    [:input {:type "hidden",
                             :name "text",
                             :id "text",
                             :value "prose"}]
                    [:input {:type "submit", :value "undo"}]]
          result (enbutton-doneness "uuid" "done" "prose" "UUID")]
      ;; check that every element in the expected exists in result
      ;; not checking for csrf
      (map (fn [e] (is (some #(= e %) result))) expected)))

  (testing "todo"
    (let [uri (java.net.URI. "/list-uuid/UUID")
          expected [:form {:method "POST", :action uri}
                    [:input {:type "hidden",
                             :name "_method",
                             :id "_method",
                             :value "PUT"}]
                    [:input {:type "hidden",
                             :name "uuid",
                             :id "uuid",
                             :value "uuid"}]
                    [:input {:type "hidden",
                             :name "doneness",
                             :id "doneness",
                             :value "done"}]
                    [:input {:type "hidden",
                             :name "text",
                             :id "text",
                             :value "prose"}]
                    [:input {:type "submit", :value "complete"}]]
          result (enbutton-doneness "uuid" "todo" "prose" "UUID")]
      ;; check that every element in the expected exists in result
      ;; not checking for csrf
      (map (fn [e] (is (some #(= e %) result))) expected))))

(deftest test-delete-button
  (testing "make a button"
    (let [uri (java.net.URI. "/list-uuid/UUID")
          expected [:form {:method "POST", :action uri}
                    [:input {:type "hidden",
                             :name "_method",
                             :id "_method",
                             :value "DELETE"}]
                    [:input {:type "hidden",
                             :name "uuid",
                             :id "uuid",
                             :value "uuid"}]
                    [:input {:type "submit", :value "delete"}]]
          result (delete-button "uuid" "UUID")]
      ;; check that every element in the expected exists in result
      ;; not checking for csrf
      (map (fn [e] (is (some #(= e %) result))) expected))))
