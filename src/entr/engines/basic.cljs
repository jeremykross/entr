(ns entr.engines.basic
  (:require [entr.engine :refer [Engine]]))

(defn basic-engine
  ([] (basic-engine {}))
  ([opts]
   (Engine. :basic opts)))
