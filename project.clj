(defproject cljs-calc "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]]
  :plugins [;; ring plugin
            [lein-ring "0.8.3"]]

            ;; ring tasks configuration
            :ring {:handler cljs-calc.core/handler}
  )
