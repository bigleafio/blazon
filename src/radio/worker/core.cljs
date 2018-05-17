(ns radio.worker.core
(:require [cljs.pprint :refer [pprint]]))

(enable-console-print!)

(defn init []
  (pprint "Starting Worker"))
