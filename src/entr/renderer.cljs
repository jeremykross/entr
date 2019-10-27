(ns entr.renderer)

(defprotocol IRenderer
  (init! [renderer])
  (update! [renderer])
  (render! [renderer])
  (init-entity! [renderer entity])
  (update-entity! [renderer entity]))

