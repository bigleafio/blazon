(ns radio.data
  (:require [radio.util :refer [get-path]]))

(def fs      (js/require "fs"))
(def writeFileSync (.-writeFileSync fs))
(def readFileSync (.-readFileSync fs))

(defn db-path []
  (get-path "userData" "radio.json"))

(defn json-parse
  [string]
  (.parse js/JSON string))

(defn json-stringify
  [data]
  (.stringify js/JSON data))

(defn read []
  (try
    (-> (db-path)
        readFileSync
        json-parse
        (js->clj :keywordize-keys true))
    (catch js/Object e
      {})))

(defn- write-file-sync
  [data path]
  (writeFileSync path data))

(defn write
  [data]
  (-> (read)
      (merge data)
      clj->js
      json-stringify
      (write-file-sync (db-path))))
