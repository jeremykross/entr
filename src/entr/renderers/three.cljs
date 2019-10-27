(ns entr.renderers.three
  (:require 
    cljsjs.three
    cljsjs.three-examples.loaders.OBJLoader
    [entr.components :as component]
    entr.renderer))

(defn init-box-mesh!
  [obj-3d c] 
  (let [mesh
        (js/THREE.Mesh.
          (js/THREE.BoxGeometry.
            @(:width-$ c)
            @(:height-$ c)
            @(:depth-$ c))
          (js/THREE.MeshPhongMaterial.
            #js {:color 0xffffff }))]
    (.add obj-3d mesh)
    mesh))

(defn init-perspective-camera!
  [renderer obj-3d c]
  (let [camera
        (js/THREE.PerspectiveCamera.
          @(:fov-$ c)
          @(:aspect-$ c)
          @(:near-$ c)
          @(:far-$ c))]
    (.add obj-3d camera)
    (reset! (:camera-ref renderer) camera)
    camera))

(defn init-ortho-camera!
  [renderer obj-3d c]
  (let [camera (js/THREE.OrthographicCamera.
                 @(:left-$ c)
                 @(:right-$ c)
                 @(:top-$ c)
                 @(:bottom-$ c)
                 @(:near-$ c)
                 @(:far-$ c))]
    (.add obj-3d camera)
    (reset! (:camera-ref renderer) camera)
    camera))

(defn init-obj-mesh!
  [obj-3d c]
  (.load (js/THREE.OBJLoader.) @(:model-path-$ c)
         (fn [mesh]
           (.load (js/THREE.TextureLoader.)
                  @(:texture-path-$ c)
                  (fn [texture]
                    (.traverse mesh
                               (fn [n] (when (.-isMesh n)
                                         (set!
                                           (.-map (.-material n))
                                           texture))))
                    (.add obj-3d mesh))))
         #(println "Progress")
         #(println "Error")))


(defn update-transform!
  [obj-3d c]
  (let [[x y z] @(:position-$ c)
        [rx ry rz] @(:rotation-$ c)
        [sx sy sz] @(:scale-$ c)]
    (.set (.-scale obj-3d) sx sy sz)
    (.set (.-rotation obj-3d) rx ry rz)
    (.set (.-position obj-3d) x y z)))

(defn update-color!
  [obj-3d c]
  (.traverse obj-3d
             (fn [n] 
               (when (.-isMesh n)
                 (.set (.-color (.-material n)) @(:color-$ c))))))

(defn init-component!
  [renderer obj-3d component-type component]
  (condp instance? component
    component/BoxMesh
    (init-box-mesh! obj-3d component)
    component/PerspectiveCamera
    (init-perspective-camera! renderer obj-3d component)
    component/OrthoCamera
    (init-ortho-camera! renderer obj-3d component)
    component/ObjMesh
    (init-obj-mesh! obj-3d component)
    false))

(defn update-component!
  [renderer obj-3d component-type component]
  (condp instance? component
    component/Transform
    (update-transform! obj-3d component)
    component/Color
    (update-color! obj-3d component)
    false))

(defrecord ThreeRenderer
  [kind webgl-ref scene-ref camera-ref]
  entr.renderer/IRenderer

  (init! [renderer]
    (let [scene (js/THREE.Scene.)
          light (js/THREE.DirectionalLight. 0xffffff 1)]
      (.set (.-position light) 0 1 1)
      (.add scene light)
      (reset! (:scene-ref renderer) scene)))

  (init-entity! [renderer entity]
    (let [obj-3d (js/THREE.Object3D.)]
      (set! (.-name obj-3d) (str (:id entity)))
      (.add @(:scene-ref renderer) obj-3d)

      (doseq [[component-type component] (:components entity)]
        (init-component! renderer obj-3d component-type component))))

  (update! [renderer])

  (update-entity! [renderer entity]
    (let [obj-3d (.getObjectByName @(:scene-ref renderer) (str (:id entity)))]
      (doseq [[component-type component] (:components entity)]
        (update-component! renderer obj-3d component-type component))))

  (render! [renderer]
    (.render @(:webgl-ref renderer)
             @(:scene-ref renderer)
             @(:camera-ref renderer))))

(defn three-renderer!
  [w h opts]
  (let [webgl (js/THREE.WebGLRenderer. (clj->js opts))
        renderer (ThreeRenderer. :three (atom webgl) (atom nil) (atom nil))]
    (.setSize webgl w h)
    (.setClearColor webgl 0xffffff 0)
    renderer))

