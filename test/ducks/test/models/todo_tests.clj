(ns ducks.test.models.todo-tests
  (:use clojure.test
        ducks.models.todo))


(deftest test-legal-todo-doneness
  (testing "todo is a valid doneness"
    (is (legal-todo-doneness "todo")))
  (testing "done is a valid state"
    (is (legal-todo-doneness "done")))
  (testing "there are only two valid states"
    (is (= 2 (count legal-todo-doneness)))))

(deftest test-todo-validity
  (testing "invalid state"
    (is (thrown? AssertionError  (validate-todo (make-todo "testA" "badstate")))))
  (testing "nil uuid"
    (is (thrown? AssertionError  (validate-todo {:text "testB" :doneness "done" :uuid nil}))))
  (testing "not a uuid"
    (is (thrown? AssertionError  (validate-todo {:text "testC" :doneness "todo" :uuid 42}))))
  (testing "just the text"
    (let [t (make-todo "some text")]
      (assert (= "some text" (:text t)))
      (assert (= "todo" (:doneness t)))
      (assert (= java.util.UUID (class (:uuid t)))))))

(deftest test-uuid-conversion
  (testing "round-tripping"
    (let [t (make-todo "testD")
          result (convert-uuid-from-string (convert-uuid-to-string t))]
      (assert (= t result)))))


