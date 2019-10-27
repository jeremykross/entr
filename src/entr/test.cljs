(ns entr.test
  (:require
    [entr.engines.basic :refer [basic-engine]]
    [entr.renderers.three :refer [three-renderer!]]
    [entr.renderer :as renderer]
    [entr.components :as component]
    [entr.entity :as entity]
    [entr.scene :as scene]
    [ulmus.core :as ulmus]))

(defn test!
  []
  (let [entities-$ (ulmus/of
                     [(entity/entity
                        (component/statically
                          component/transform [0 0 5] [0 0 0] [1 1 1])
                        (component/statically
                          component/perspective-camera
                          45.0 (/ 640 360) 1.0 1000.0))
                      (entity/entity
                        (component/statically
                          component/box-mesh 1 1 1))
                      (entity/entity
                        (component/statically
                          component/transform
                          [0 0 0] [0 1.57 0] [1 1 1])
                        (component/statically
                          component/obj-mesh
                          "meshes/D1.obj"
                          "textures/D1.png"))])

        scene (scene/Scene. 
                (basic-engine)
                (three-renderer! 640 360 {:canvas 
                                          (.getElementById js/document "canvas")})
                entities-$)]

    (scene/init! scene)
    ((fn tick! []
       (scene/tick! scene)
       (js/requestAnimationFrame tick!)))))

