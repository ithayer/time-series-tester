(ns time-series-tester.core
  (:import [hyphotesisTest TimePoint]))

(defn run-fake-test
  "Generates some random timepoints and data"
  [n-points max-magnitude]
  (map (fn [i] [i (rand-int max-magnitude)]) (range n-points)));; TODO: When time point constructors are done, create timepoints.

(defn -main [& args]
  (let [t (TimePoint.)]
    (println "Clojure main: " (str t))))