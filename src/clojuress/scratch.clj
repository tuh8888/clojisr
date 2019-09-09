(ns clojuress.scratch
  (:require [clojuress.packages.base :as base]
            [clojuress.packages.stats :as stats]
            [clojuress.session :as session]
            [clojuress.protocols :as prot]
            [clojuress.core :as r :refer [r]])
  (:import (org.rosuda.REngine REXP REXPSymbol REXPDouble REXPInteger)
           (org.rosuda.REngine.Rserve RConnection)))

(comment

  (-> "1+2"
      r/eval-r->java
      r/java->clj
      (= [3.0]))

  (-> "1+2"
      r)

  (-> [1 2 3]
      base/mean)

  (-> [1 2 3]
      stats/median)

  (-> [1 nil 3]
      stats/median)

  (-> 1000
      (stats/rnorm [:= :mean 9]))

  (def result1
    (-> "(0:10000000)^2"
        r
        time))

  (->> result1
       r/r->java
       r/java->clj
       (take 9)
       (= (map #(double (* % %)) (range 9)))
       time)

  (->> result1
       r/r->java
       r/java->r
       r/r->java
       r/java->clj
       (take 9)
       (= (map #(double (* % %)) (range 9)))
       time)

  (def result2
    (-> "data.frame(a=1:10000000, b=rnorm(10000000))"
        r
        time))

  (-> result2
      ((juxt r/r-class
             r/names
             r/shape))
      (= [["data.frame"]
          ["a" "b"]
          [10000000 2]]))

  (session/clean-all)

  (r "1+2" :session-args {:port 5555})

  (r "1+2"  :session-args {:port 6666})

)


