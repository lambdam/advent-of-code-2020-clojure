(ns aoc2020.day05
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [orchestra.core :refer [defn-spec]]))

(s/def ::input
  (s/and string?
         #(-> % count (= 10))
         #(-> (take 7 %) set (set/subset? #{\F \B}))
         #(-> (nthrest % 7) set (set/subset? #{\L \R}))))

(def inputs
  (->> "day05.txt"
       io/resource
       slurp
       str/split-lines
       (s/assert (s/coll-of ::input))))

(s/def ::row integer?)
(s/def ::column integer?)
(s/def ::id integer?)
(s/def ::seat
  (s/keys :req [::row ::column ::id]))

(defn-spec input->seat ::seat
  [input ::input]
  (loop [letters (seq input)
         [row- row+ :as row] [0 127]
         [col- col+ :as col] [0 7]]
    (if (empty? letters)
      {::row row-
       ::column col-
       ::id (-> row- (* 8) (+ col-))}
      (let [tail (rest letters)]
        (case (first letters)
          \F (recur tail
                    (as-> (- row+ row-) <>
                      (quot <> 2)
                      (- row+ <>)
                      (dec <>)
                      [row- <>])
                    col)
          \B (recur tail
                    (as-> (- row+ row-) <>
                      (quot <> 2)
                      (+ row- <>)
                      (inc <>)
                      [<> row+])
                    col)
          \L (recur tail
                    row
                    (as-> (- col+ col-) <>
                      (quot <> 2)
                      (- col+ <>)
                      (dec <>)
                      [col- <>]))
          \R (recur tail
                    row
                    (as-> (- col+ col-) <>
                      (quot <> 2)
                      (+ col- <>)
                      (inc <>)
                      [<> col+])))))))

(comment
  (input->seat "FBFBBFFRLR")
  )

;; Part 1

(comment
  (->> inputs
       (map input->seat)
       (sort-by ::id)
       last
       ::id)
  )
