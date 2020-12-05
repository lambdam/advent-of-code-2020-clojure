(ns aoc2020.day04
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def input
  (->> "day04.txt"
       io/resource
       slurp
       str/split-lines
       (into [] (comp (partition-by str/blank?)
                      (remove #(every? str/blank? %))
                      (map #(str/join " " %))
                      (map (fn [string]
                             (->> (str/split string #"\s+")
                                  (map (fn [key-value]
                                         (->> (str/split key-value #":")
                                              (into [])))))))
                      (map #(into {} %))))))

(defn- count-valid-passeports [passeports]
  (->> passeports
       (map keys)
       (filter #(let [current-keys-set (set %)]
                  (or (= #{"byr" "cid" "ecl" "eyr" "hcl" "hgt" "iyr" "pid"}
                         current-keys-set)
                      (= #{"byr" #_"cid" "ecl" "eyr" "hcl" "hgt" "iyr" "pid"}
                         current-keys-set))))
       count))

;; Part 1

(defn result-part-1 [passeports]
  (count-valid-passeports passeports))

(comment
  (result-part-1 input)
  )

;; Part 2

(defn- parse-int [string]
  (try
    (Integer/parseInt string)
    (catch Exception e
      nil)))

(defn result-part-2 [passeports]
  (->> passeports
       (map (fn [passeport]
              (reduce-kv (fn [acc k v]
                           (case k
                             "byr" (let [year (parse-int v)]
                                     (cond
                                       (not (integer? year)) acc
                                       ;; ---
                                       (and (>= year 1920) (<= year 2002))
                                       (assoc acc k v)
                                       ;; ---
                                       :else acc))
                             "cid" (assoc acc k v)
                             "ecl" (if (contains? #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} v)
                                     (assoc acc k v)
                                     acc)
                             "eyr" (let [year (parse-int v)]
                                     (cond
                                       (not (integer? year)) acc
                                       ;; ---
                                       (and (>= year 2020) (<= year 2030))
                                       (assoc acc k year)
                                       ;; ---
                                       :else acc))
                             "hcl" (if (re-matches #"#[0-9a-f]{6}" v)
                                     (assoc acc k v)
                                     acc)
                             "hgt" (let [unit (re-find #"cm|in" v)
                                         size (some-> (str/split v #"[cm|in]")
                                                      first
                                                      parse-int)]
                                     (cond
                                       (or (not unit)
                                           (not size))
                                       acc
                                       ;; ---
                                       (and (= "cm" unit)
                                            (>= size 150)
                                            (<= size 193))
                                       (assoc acc k v)
                                       ;; ---
                                       (and (= "in" unit)
                                            (>= size 59)
                                            (<= size 76))
                                       (assoc acc k v)
                                       ;; ---
                                       :else acc))
                             "iyr" (let [year (parse-int v)]
                                     (cond
                                       (not (integer? year)) acc
                                       ;; ---
                                       (and (>= year 2010) (<= year 2020))
                                       (assoc acc k year)
                                       ;; ---
                                       :else acc))
                             "pid" (if (re-matches #"\d{9}" v)
                                     (assoc acc k v)
                                     acc)
                             ))
                         {}
                         passeport)))
       count-valid-passeports))

(comment
  (result-part-2 input)
  )
