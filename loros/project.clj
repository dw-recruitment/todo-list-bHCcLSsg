(defproject loros "0.1.0-SNAPSHOT"
  :description "Echos rabbitmq messages to console."
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
  		[com.novemberain/langohr "3.5.0"]]
  :main ^:skip-aot loros.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
