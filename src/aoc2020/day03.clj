(ns aoc2020.day03
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [orchestra.core :refer [defn-spec]]))

(s/def ::x-move integer?)
(s/def ::y-move integer?)
(s/def ::forest-map (s/coll-of seq?))

(def input
  (->> "day03.txt"
       io/resource
       slurp
       str/split-lines
       (mapv cycle)))

(comment
  (count input)
  )

(defn-spec count-tree-enconters integer?
  [data (s/keys :req [::forest-map ::x-move ::y-move])]
  (let [last-y (-> data ::forest-map count dec)]
    (loop [x 0
           y 0
           tree-count 0]
      (let [next-x (+ x (::x-move data))
            next-y (+ y (::y-move data))]
        (cond
          (> next-y last-y) tree-count
          ;; ---
          (-> data
              ::forest-map
              (get next-y)
              (nth next-x)
              (= \#))
          (recur next-x next-y (inc tree-count))
          ;; ---
          :else (recur next-x next-y tree-count))))))

;; Part 1

(defn resul-part-1 [forest-map]
  (count-tree-enconters {::forest-map forest-map
                         ::x-move 3
                         ::y-move 1}))

(comment
  (resul-part-1 input)
  )

;; Part 2

(defn resul-part-2 [forest-map]
  (->> [{::forest-map forest-map ::x-move 1 ::y-move 1}
        {::forest-map forest-map ::x-move 3 ::y-move 1}
        {::forest-map forest-map ::x-move 5 ::y-move 1}
        {::forest-map forest-map ::x-move 7 ::y-move 1}
        {::forest-map forest-map ::x-move 1 ::y-move 2}]
       (map count-tree-enconters)
       (apply *)))

(comment
  (resul-part-2 input)
  )
