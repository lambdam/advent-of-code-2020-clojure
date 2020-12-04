(ns aoc2020.day02
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [clojure.string :as str]
            [orchestra.core :refer [defn-spec]]))

(s/def ::lowest integer?)
(s/def ::highest integer?)
(s/def ::target-letter char?)
(s/def ::password string?)

(s/def ::db-entry (s/keys :req [::lowest ::highest ::target-letter ::password]))

(def input
  (-> "day02.txt"
      io/resource
      slurp
      (str/split #"\n")))

(defn-spec parse-lines (s/coll-of ::db-entry)
  [lines (s/coll-of string?)]
  (->> lines
       (map (fn [line]
              (let [[range letter' password] (-> line
                                                 str/trim
                                                 (str/split #"\s+"))
                    [lowest highest] (as-> range <>
                                         (str/split <> #"-")
                                         (map #(Integer/parseInt %) <>))
                    letter (first letter')]
                {::lowest lowest
                 ::highest highest
                 ::target-letter letter
                 ::password password})))))

;; Part 1

(defn result-part-1 [lines]
  (->> lines
       parse-lines
       (filter (fn [{::keys [lowest highest target-letter password]}]
                 (as-> password <>
                   (filter #(= % target-letter) <>)
                   (count <>)
                   (and (>= <> lowest)
                        (<= <> highest)))))
       count))

(comment
  (result-part-1 input)
  )

;; Part 2

(defn result-part-2 [lines]
  (->> lines
       parse-lines
       (filter (fn [{::keys [lowest highest target-letter password]}]
                 (let [first-letter (nth password (dec lowest) nil)
                       second-letter (nth password (dec highest) nil)]
                   (and (not= first-letter second-letter)
                        (or (= first-letter target-letter)
                            (= second-letter target-letter))))))
       count))

(comment
  (result-part-2 input)
  )
