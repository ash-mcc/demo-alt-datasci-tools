(ns R-interop)


;; bridging to/from R
;;

;; load the Clojure-R bridge library
(require '[clojisr.v1.r :as r :refer [require-r]]) 
            


;; ask R to interprete a string as code
;; and assign it to a Clojure var
(def x (r/r "1 + 5"))

;; an RObject is a Clojure reference to an R variable (value or function)
(type x)



;; convert Clojure form into R code
(r/clj->r [1 nil 3])



;; R code as a string
(def f (r/r "function(x) x*10")) 

;; Clojure -> R -> Clojure
(-> 5 
    f 
    r/r->clj)



;; load R's library of prepared datasets
(require-r '[datasets :as datasets]) 

;; get the Iris dataset
(def iris-df datasets/iris)
(def clazz (r/r "class"))
(clazz iris-df) ;; class in R
(type iris-df) ;; type in Clojure

;; call an R function on it
(def summary (r/r "summary"))
(summary iris-df)



;; element getters/setters
(r/bra iris-df 1 "Sepal.Length") ;; df[1,"Sepal.Length"]
(r/bra<- iris-df 1 "Sepal.Length" 1.2345) ;; df[1,"Sepal.Length"]<-4.2




;; load a couple more libs
(require-r '[GGally :as GGally]) ;; ggplot2 specialised 
(require '[clojisr.v1.applications.plotting :as plotting]) ;; Clojure-R bridge helper

;; pairwise comparison of the variables 
(->> iris-df
     GGally/ggpairs
     (plotting/plot->file "iris.svg"))

;; zero-copy data sharing between Clojure and R
(def iris-ds (r/r->clj iris-df))
(type iris-ds)




;; load Clojure dataset functions 
(require '[scicloj.ml.dataset :as ds])

;; a bit like R's summary() function
(ds/info iris-ds)






