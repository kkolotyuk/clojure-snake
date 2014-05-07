(ns clojure-snake.core-test
  (:require [clojure.test :refer :all]
            [clojure-snake.core :refer :all]))

(deftest core-functions
  (testing "Add points"
    (is (= (add-points [0 1] '(-5 3) [3 0]) [-2 4])))

  (testing "Point to screen rect"
    (is (= (point-to-screen-rect [4 -2]) [40 -20 10 10]))))
