(defproject ducks "0.1.0-SNAPSHOT"
  :description "TODO app, Democracy Works coding homework."
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.0"]
                 [hiccup "1.0.5"]
                 [ring-server "0.4.0"]]
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler ducks.handler/app
         :init ducks.handler/init
         :destroy ducks.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring/ring-mock "0.3.0"] [ring/ring-devel "1.4.0"]]}})
