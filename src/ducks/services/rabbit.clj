(ns ducks.services.rabbit
  "Namespace for rabbitmq related functions."
  (:require [clojure.core.async :as async]
            [langohr.core      :as rmq]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]
            [langohr.consumers :as lc]
            [langohr.basic     :as lb]))

(def rabbit-channel
  "core.async channel for sending messages to rabbbitmq"
  (async/chan 1))


(def ^{:const true}
  default-exchange-name "")

(defn tell-rabbit!
  "Add a message to the rabbit-channel, asynchronous."
  [msg]
  (async/go
    (println "rabbit told: " msg)
    (async/>! rabbit-channel msg)))

(def rabbit-conn (atom nil))
(def rabbit-ch (atom nil))

(defn start-rabbit-services
  "Starts the rabbit service."
  []
  (println "Starting rabbit service...")
  (let [conn  (rmq/connect)
        ch    (lch/open conn)
        qname "langohr.examples.hello-world"]
    (println (format "[main] Connected. Channel id: %d" (.getChannelNumber ch)))
    (compare-and-set! rabbit-conn nil conn)
    (compare-and-set! rabbit-ch nil ch)
    (lq/declare ch qname {:exclusive false :auto-delete true})
    (async/go-loop []
      (let [msg (async/<! rabbit-channel)]
        (println "Rabbit service sees a: " msg)
        (lb/publish ch default-exchange-name qname (str msg)
                    {:content-type "text/plain" :type "greetings.hi"})
        (recur)))))

(defn stop-rabbit-services
  "Stops the rabbit service and shuts down rabbitmq connection."
  []
  (async/close! rabbit-channel)
  (rmq/close @rabbit-ch)
  (rmq/close @rabbit-conn))
