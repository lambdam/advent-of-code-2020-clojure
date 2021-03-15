(ns aoc2020.core
  (:require [clojure.spec.alpha :as s]
            [orchestra.spec.test :as st]))

(s/check-asserts true)

;; Weirdly, (def instrument st/instrument) wouldn't work with cider-refresh
;; > aoc2020.core/instrument is not a function of no arguments
;; Works with this defn form
(defn instrument []
  (st/instrument))

(comment
  (instrument)
  )
