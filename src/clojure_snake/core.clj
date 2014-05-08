(ns clojure-snake.core
  (:import (java.awt Color Dimension)
           (javax.swing JPanel JFrame Timer JOptionPane)
           (java.awt.event ActionListener KeyListener KeyEvent))
  (:require [clojure.contrib import-static])
  (:gen-class))
;; (import-static java.awt.event.KeyEvent VK_LEFT VK_RIGHT VK_UP VK_DOWN)

(def width 75)
(def height 50)
(def point-size 10)
(def turn-millis 75)
(def win-length 5)
(def dirs {KeyEvent/VK_LEFT  [-1 0]
           KeyEvent/VK_RIGHT [1 0]
           KeyEvent/VK_UP    [0 -1]
           KeyEvent/VK_DOWN  [0 1]})

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

(defn update-positions [snake apple]
  (dosync
   (if (eats? @snake @apple)
     (do (ref-set apple (create-apple))
       (alter snake move :grow))
     (alter snake move)))
  nil)

(defn fill-point [g pt color]
  (let [[x y width height] (point-to-screen-rect pt)]
    (.setColor g color)
    (.fillRect g x y width height)))

(defmulti paint (fn [g object & _] (:type object)))

(defmethod paint :apple [g {:keys [location color]}]
  (fill-point g location color))

(defmethod paint :snake [g {:keys [body color]}]
  (doseq [point body]
    (fill-point g point color)))

(defn game-panel [frame snake apple]
  (proxy [JPanel ActionListener KeyListener] []
    (paintComponent [g]
      (proxy-super paintComponent g)
      (paint g @snake)
      (paint g @apple))
    (actionPerformed [e]
      (update-positions snake apple)
      (when (lose? @snake)
        (reset-game snake apple)
        (JOptionPane/showMessageDialog frame "You lose!"))
      (when (win? @snake)
        (reset-game snake apple)
        (JOptionPane/showMessageDialog frame "You win!"))
      (.repaint this))
    (keyPressed [e]
      (update-direction snake (dirs (.getKeyCode e))))
    (getPreferredSize []
      (Dimension. (* (inc width) point-size)
                  (* (inc height) point-size)))
    (keyReleased [e])
    (keyTyped [e])))


(defn game []
  (let [snake (ref (create-snake))
        apple (ref (create-apple))
        frame (JFrame. "Snake")
        panel (game-panel frame snake apple)
        timer (Timer. turn-millis panel)]
    (doto panel
      (.setFocusable true)
      (.addKeyListener panel))
    (doto frame
      (.add panel)
      (.pack)
      (.setVisible true))
    (.start timer)
    [snake, apple, timer]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (game))
