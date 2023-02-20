(ns deceptively-simple)



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

(def x-through-time (atom {:a "initial state"}))

(deref x-through-time)

(swap! x-through-time assoc :a "state 3")

@x-through-time