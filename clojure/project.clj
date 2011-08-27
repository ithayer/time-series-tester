(defproject time-series-tester "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [log4j "1.2.15" :exclusions [javax.mail/mail
                                              javax.jms/jms
                                              com.sun.jdmk/jmxtools
                                              com.sun.jmx/jmxri]]
                 ]
  :dev-dependencies [[lein-javac "1.2.1-SNAPSHOT"]]
  :java-source-path [["src/java"]]
  :main time-series-tester.core)
