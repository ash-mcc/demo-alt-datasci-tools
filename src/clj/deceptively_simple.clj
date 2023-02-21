(ns deceptively-simple)

;; # MARKUP TITLE



;; Preamble
;;


;; me: backgorund: dist sys arch > RPC, CORBA > Oak, Java > banks ... > data sci-y
;; interests: applied xyz, tech for enter-prise, inter-work, inter-op


;; these days... almost pick your platform and lang 
;; me: plat=JVM, lang=...
;; want: it simple! small syntax, ~0 boilerplate, good interop... >>> Clojure
;; demo some...



;; Deceptively simple
;;


;; this env: a REPL ...REPL server ...jack-in ...interpretes Clojure 'forms'


;; simple value, list (...LISP), homoiconic, vars,  nice literals

1

(+ 1 2)

'(+ 1 2)

(def xs [1 2 3 4 5])

(def m {:a 1
        :b 2})


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


;; rich-viz (& ctrl) tools like Clerk
;;

(require '[nextjournal.clerk :as clerk])

(clerk/serve! {:browse true})
(clerk/show! 'nextjournal.clerk.tap)

(tap> @state)

(add-watch state :state-watcher 
           (fn [_key _ref _old-value new-value]
             (tap> (count new-value))))


;; example Vega-lite chart

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
;;(clerk/halt!)


;; a programming notebook - alt Jupyter (with a Clojure kernel)

(clerk/serve! {:watch-paths ["src/clj"]
               :port        7777
               :browse?     false})
(clerk/show! "src/clj/deceptively_simple.clj")

