(defproject clojure-snake "0.1.0-SNAPSHOT"
  :description "Simple Swing snake game"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [sonian/carica "1.1.0" :exclusions [[cheshire]]]]
  :main ^:skip-aot clojure-snake.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :test {:resource-paths ["test/resources"]}})
