(set-env!
  :source-paths   #{"src/clj" "src/cljs"}
  :resource-paths #{"src/clj" "src/cljs"}
  :dependencies '[
    [org.clojure/clojure       "1.7.0-alpha5" :scope "provided"]
    [org.clojure/clojurescript "0.0-2985"     :scope "provided"]

    [adzerk/boot-cljs   "0.0-2814-3"     :scope "test"]
    [adzerk/boot-reload "0.2.4"          :scope "test"]
    [adzerk/boot-cljs-repl "0.1.10-SNAPSHOT" :scope "test"]
    [pandeiro/boot-http "0.6.3-SNAPSHOT" :scope "test"]

    [com.cognitect/transit-cljs "0.8.215"]
    [com.cognitect/transit-clj "0.8.229"]
    [bidi "1.18.10"]
    [ring/ring "1.2.1" :exclusions [org.clojure/java.classpath]]
])

(require
  '[adzerk.boot-cljs   :refer [cljs]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
  '[adzerk.boot-reload :refer [reload]]
  '[pandeiro.boot-http :refer [serve]]
)

(def compiler-opts
  {:warnings {:single-segment-namespace false}})

(deftask serving []
  ;;(merge-env! :source-paths #{"examples"})
  (comp (serve :handler 'transit-example.server/handler :dir ".")
        (watch)
        ;;    (anybar)
        (cljs-repl)
        (cljs)
        ))

(deftask none-opts []
  (task-options!
   cljs {:optimizations    :none
         :source-map       true
         :compiler-options compiler-opts})
  identity)

(deftask none []
  (comp (none-opts)
        (reload)
        (serving)
        ))

(deftask advanced-opts []
  (task-options!
   cljs {:optimizations    :advanced
         :compiler-options (merge compiler-opts
                                  {:closure-defines {:goog.DEBUG false}
                                   :elide-asserts   true})})
  identity)

(deftask advanced []
  (comp (advanced-opts)
        (serving)))
