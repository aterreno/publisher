(ns publisher.core

  (:gen-class)
  (:require [langohr.core      :as rmq]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]
            [langohr.basic     :as lb]
            [cheshire.core :refer :all]))

(def ^{:const true}
  default-exchange-name "")

(defn -main
  [& args]
  (let [conn  (rmq/connect)
        ch    (lch/open conn)
        qname "hello-world"]
    (println (format "[main] Connected. Channel id: %d" (.getChannelNumber ch)))
    (lq/declare ch qname :exclusive false :auto-delete true)

    (println "[main] Publishing...")
    (lb/publish ch default-exchange-name qname (generate-string {:id 1}) :content-type "text/plain" :type "greetings.hi")

    (println "[main] Disconnecting...")
    (rmq/close ch)
    (rmq/close conn)))
