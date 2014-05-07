(ns clojure-snake.core
  (:import (java.awt Color))
  (:gen-class))

(def width 75)
(def height 50)
(def point-size 10)
(def turn-millis 75)
(def win-length 5)

(defn add-points [& pts]
  "Adds points together. You can use add-points to calculate
   the new position of a moving game object."
  (apply mapv + pts))

(defn point-to-screen-rect [[x y]]
  "Converts a point in game space to a rectangle on the screen."
  (map (partial * point-size) [x y 1 1]))

(defn create-snake []
  {:body '([1 1])
   :dir [1 0]
   :type :snake
   :color (Color. 15 160 70)})

(defn create-apple []
  {:location [(rand-int width) (rand-int height)]
   :type :apple
   :color (Color. 210 50 90)})

(defn move [{:keys [body dir] :as snake} & [grow]]
  "Returns new snake after one step. Use optional param grow to eat an apple"
  (assoc snake :body (cons (add-points (first body) dir)
                           (if grow
                             body
                             (butlast body)))))

(defn win? [{body :body}]
  (>= (count body) win-length))

(defn- head-overlaps-body? [{[head & tail] :body}]
  (contains? (set tail) head))

(def lose? head-overlaps-body?)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
