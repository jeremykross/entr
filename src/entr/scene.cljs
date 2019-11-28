(ns entr.scene
  (:require
    clojure.set
    [ulmus.signal :as ulmus]
    [ulmus.transaction :as ulmus-tr]
    [entr.renderer :as r]))

(defrecord Scene [engine renderer entities-$])

(defn init!
  [scene]
  (println "init scene:" scene)

  ; subscribe to entities
  ; init or remove accordingly.
  (ulmus-tr/subscribe!
    (ulmus/zip (:entities-$ scene)
               (ulmus/prev (:entities-$ scene)))
    (fn [[entities prev-entities]]
      (let [new-entities
            (clojure.set/difference (into #{} entities)
                                    (into #{} prev-entities))]

        (println "init:" new-entities)

        (doseq [entity new-entities]
          (r/init-entity! (:renderer scene) entity)))))

  (r/init! (:renderer scene)))

(defn tick!
  [scene]
  (r/update! (:renderer scene))
  (doseq [entity @(:entities-$ scene)]
    (r/update-entity! (:renderer scene) entity))
  (r/render! (:renderer scene)))

(defn start!
  [scene])

(defn stop!
  [scene])
