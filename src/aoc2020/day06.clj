(ns aoc2020.day06
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]))

(s/def ::line-state #{::content ::empty})
(s/def ::current-acc (s/coll-of string?))
(s/def ::result (s/coll-of ::current-acc))

(s/check-asserts true)

(def input
  (->> "day06.txt"
       io/resource
       slurp
       str/split-lines))

(def data
  (-> (loop [state ::content
             result []
             current-acc []
             lines input]
        (let [line (peek lines)]
          (cond
            ;; Final step
            (empty? lines) (->> (conj result current-acc)
                                (s/assert ::result))
            ;; End of a block
            (and (= ::content state) (str/blank? line))
            (recur ::empty (conj result current-acc) [] (pop lines))
            ;; In the middle of a block
            (= ::content state)
            (recur ::content result (conj current-acc line) (pop lines))
            ;; In case of double empty lines
            (and (= ::empty state) (str/blank? line))
            (recur ::empty result current-acc (pop lines))
            ;; Beginning of a block
            (= ::empty state)
            (recur ::content result (conj current-acc line) (pop lines))
            ;; ---
            :else (throw (Exception. "Illegal state")))))
      reverse
      vec))

(def result-part-1
  (->> data
       (transduce
         (map #(->> (apply str %) (into #{}) count))
         +)))

(def result-part-2
  (->> data
       (transduce
         (map #(->> (map set %)
                    (apply set/intersection)
                    count))
         +)))
