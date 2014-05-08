(ns clojure-snake.core-test
  (:require [clojure.test :refer :all]
            [clojure-snake.game :refer :all]))

(deftest core-functions
  (testing "Add points"
    (is (= (add-points [0 1] '(-5 3) [3 0]) [-2 4])))

  (testing "Move snake without grow"
    (let [snake (create-snake)
          moved-snake (assoc snake :body '([2 1]))]
      (is (= (move snake) moved-snake))))

  (testing "Move snake with grow"
    (let [snake (create-snake)
          moved-snake (assoc snake :body '([2 1] [1 1]))]
      (is (= (move snake :grow) moved-snake))))

  (testing "Win condition"
    (is (win? {:body '([0 1] [0 2] [0 3] [0 4] [0 5])}))
    (is (not (win? {:body '([0 1])}))))

  (testing "Lose condition"
    (is (lose? {:body '([1 1] [1 2] [2 2] [2 1] [1 1])}))
    (is (not (lose? {:body '([0 1])}))))

  (testing "Eats apple?"
    (let [snake (create-snake)
          eating-apple {:location (-> snake :body first)}
          not-eating-apple {:location (->> snake :body first (mapv inc))}]
      (is (eats? snake eating-apple))
      (is (not (eats? snake not-eating-apple)))))

  (testing "Turn"
    (let [snake (create-snake)
          newdir [0 -1]]
      (is (:dir (turn snake newdir) newdir)))))
