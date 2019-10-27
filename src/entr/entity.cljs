(ns entr.entity)

(defrecord Entity
  [id tags components])

(defn entity
  [& components]
  (Entity.
    (keyword (gensym))
    []
    (into
      {}
      (map (fn [c]
             [(:k c) c])
           components))))
           

