(ns aoc2020.day01
  (:require [clojure.java.io :as io]
            [clojure.math.combinatorics :as combo]
            [clojure.string :as str]
            [net.cgrand.xforms :as x]))

(def input
  (-> "day01.txt"
      io/resource
      slurp
      (str/split #"\n")
      (->> (into [] (map #(Integer/parseInt %))))))

;; Part 1

(defn result-part-1 [ints]
  (some->> (combo/cartesian-product ints ints)
           (some (fn [[x y :as pair]]
                   (when (-> (+ x y) (= 2020))
                     pair)))
           (apply *)))

(comment
  (result-part-1 input)
  )

;; Part 2

(defn result-part-2 [ints]
  (some->> (combo/cartesian-product ints ints ints)
           (some (fn [[x y z :as triplet]]
                   (when (-> (+ x y z) (= 2020))
                     triplet)))
           (apply *)))

(comment
  (result-part-2 input)
  )

;; ---

;; Playing with transducers

(comment

  (time
    (->> (combo/cartesian-product input input)
         count))

  (time
    (->> (combo/cartesian-product input input)
         (into [] x/count)
         first))

  (time
    (->> (combo/cartesian-product input input)
         (transduce (map (fn [_] 1)) + 0)))

  )
