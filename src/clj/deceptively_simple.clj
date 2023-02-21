(ns deceptively-simple)



;; Deceptively simple
;;




;; this env: a REPL ...REPL server ...jack-in ...interprets Clojure 'forms'




;; simple value, list (...LISP), homoiconic, vars,  nice literals

1

(+ 1 2)

'(+ 1 2)

(def xs [1 2 3 4 5])
(type xs)

(def m {:a 1
        :b 2})
(type m)

(def s #{1 2 3 4 5})
(type s)



;; a lib of useful, composable functions over a very small number of composable data types

(get xs 0)

(get m :a)




;; proper value equality

(= #{1 2 [3 4]} 
   #{2 [3 4] 1})





;; immutable data, data sharing impl
;; ++ for concurrency - easy to reason about state, not chopped up and wrapped in code

(def ys (conj xs 0))




;; values vs stateful entities/identities ...'a stable logical entity associated with a series of different values over time'

(def state (atom [1 1 2 3 1]))

(deref state)

(swap! state conj (rand-int 9))





;; rich-viz (& ctrl) tools like Clerk ...literate programming
;;

(require '[nextjournal.clerk :as clerk])

(clerk/serve! {:browse true})
(clerk/show! 'nextjournal.clerk.tap)

(tap> @state) ;; tap plumbing

(add-watch state :state-watcher 
           (fn [_key _ref _old-value new-value]
             (tap> (count new-value))))






;; example Vega-lite chart (like Plotly, D3 based but more declarative)

(defn histo
  [xs]
  {:schema   "https://vega.github.io/schema/vega-lite/v5.json"
   :data     {:values (map (fn [n] {:x n}) xs)}
   :height   300
   :mark     {:type "bar" :tooltip true}
   :encoding {:x     {:field "x" :bin {:minstep 1}}
              :y     {:aggregate "count"}}})

(require '[nextjournal.clerk.viewer :as v])

(add-watch state :state-watcher
           (fn [_key _ref _old-value new-value]
             (tap> (clerk/html [:div
                                [:p "count " (count new-value)]
                                (v/vl (histo new-value))]))))


(remove-watch state :state-watcher)




;; Clojure on Java - e.g. this code
;; Clojure on a JS engine - e.g. Clerk's output
;; (Clojure on... Node, CLR/.Net, Erlang, Erlang VM)




;; a programming notebook (also to static HTML) - alt Jupyter (with a Clojure kernel)

;(clerk/halt!)
(clerk/serve! {:watch-paths ["src/clj"]
               :port        7777
               :browse?     true})
(clerk/show! "src/clj/deceptively_simple.clj")



#_(v/plotly {:data [{:z [[1 2 3]
                       [3 2 1]]
                   :type "surface"}]})




;; proper example of an HTML+JS notebook: https://ash-mcc.github.io/jotter2/notebooks/tsp_using_smile_and_sko.html
;; common fns in coded in Clojure
;; used by 2 GA frameworks: SMILE (Java); scikit-opt (Python)
;; finishes by caling MoviePy to build an animation of SMILE searching the solution space 




