(ns clojure-snake.game
    (:import (java.awt Color))
    (:require [carica.core :refer [config]]))

(def win-length (config :win-length))
(def width (config :width))
(def height (config :height))

(defn add-points [& pts]
  "Adds points together. You can use add-points to calculate
   the new position of a moving game object."
  (apply mapv + pts))

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

(defn eats? [{[head] :body} {apple :location}]
  (= head apple))

(defn turn [snake newdir]
  (assoc snake :dir newdir))

(defn reset-game [snake apple]
  (dosync (ref-set apple (create-apple))
          (ref-set snake (create-snake)))
  nil)

(defn update-direction [snake newdir]
  (when newdir (dosync (alter snake turn newdir))))
