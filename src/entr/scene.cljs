(ns entr.scene
  (:require
    [ulmus.transaction :as ulmus]
    [entr.renderer :as r]))

(defrecord Scene [engine renderer entities-$])

(defn init!
  [scene]
  (println "init scene:" scene)

  ; subscribe to entities
  ; init or remove accordingly.
  (ulmus/subscribe! (:entities-$ scene)
                    (fn [entities] (println "Entities sub:" entities)))
  

  (r/init! (:renderer scene))
  (doseq [entity @(:entities-$ scene)]
    (r/init-entity! (:renderer scene) entity)))

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
