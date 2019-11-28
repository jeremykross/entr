(ns entr.entity
  (:require ulmus.transaction))

(defrecord Entity
  [id tags components])

(defn entity
  [& components]
  (let [ent
        (Entity.
          (keyword (gensym))
          []
          (into
            {}
            (map (fn [c]
                   [(:k c) c])
                 components)))]

    (ulmus.transaction/propogate!)

    ent))
           

