(ns time-series-tester.core
  (:import [hyphotesisTest TimePoint]))

(defn -main [& args]
  (let [t (TimePoint.)]
    (println "Clojure main: " (str t))))
