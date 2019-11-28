(ns entr.components
  (:require [ulmus.signal :as ulmus]))

(defrecord Transform [k position-$ rotation-$ scale-$])

(defn transform
  [position-$ rotation-$ scale-$]
  (Transform. :entr/transform position-$ rotation-$ scale-$))

(defrecord PerspectiveCamera [k fov-$ aspect-$ near-$ far-$])

(defn perspective-camera
  [fov-$ aspect-$ near-$ far-$]
  (PerspectiveCamera. :entr/camera
                      fov-$ aspect-$ near-$ far-$))

(defrecord OrthoCamera [k left-$ right-$ top-$ bottom-$ near-$ far-$])

(defn ortho-camera
  [left-$ right-$ top-$ bottom-$ near-$ far-$]
  (OrthoCamera. :entr/camera
                left-$ right-$ top-$ bottom-$ near-$ far-$))

(defrecord BoxMesh [k width-$ height-$ depth-$])

(defn box-mesh
  [width-$ height-$ depth-$]
  (BoxMesh. :entr/mesh width-$ height-$ depth-$))

(defrecord ObjMesh [k model-path-$ texture-path-$])

(defn obj-mesh
  [model-path-$ texture-path-$]
  (ObjMesh. :entr/mesh model-path-$ texture-path-$))

(defrecord Color [k color-$])

(defn color
  [color-$]
  (Color. :entr/color color-$))

(defn statically
  [comp-fn & args]
  (apply comp-fn (map ulmus/constant args)))

