(ns Python-interop)

;; bridging to/from Python
;;

;; load the Clojure-Python bridge library
(require '[libpython-clj2.python :as py] 
         '[libpython-clj2.require :refer [require-python]])

;; ask Python to interprete a string as code
;; and get a handle to the bridged env
(def bridge (py/run-simple-string "x = 1 + 5"))

;; the bridged env is Python objects with Java wrappers around them
;; - most C-backed things are zero-copy, e.g. Python's Numpy, Clojure's dataset

;; get the value assigned to 'x'
(-> bridge :globals (get "x"))


;; Clojure -> Python -> Clojure
;;

;; load Clojure dataset functions 
(require '[scicloj.ml.dataset :as ds]
         '[clojure.walk :as walk])

(def ds (-> [{:a 1 :b "x"}
             {:a 3 :b "y"}
             {:a 5 :b "z"}]
            ds/dataset))

(type ds)

;; load Python libs
(require-python '[builtins]
                '[pandas :as pd])

;; clj -> py
(def df (->> (ds/columns ds :as-map)
             (pd/DataFrame :data)))

;; df is a Python Panda DataFrame 
(builtins/type df)

;; py -> clj
(def ds' (-> df
             (py/py. "to_dict" "records")
             py/->jvm
             walk/keywordize-keys
             ds/dataset))

ds'




