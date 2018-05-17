(ns radio.main.core
(:require [cljs.nodejs :as nodejs]
          [cljs.pprint :refer [pprint]]
          [electron :refer [app Menu shell]]
          [electron-default-menu]
          [radio.util :as u]))

(nodejs/enable-util-print!)

(goog-define dev? true)

(defonce *main-window (atom nil))

(defn default-menu []
  (let [menu (electron-default-menu app shell)]
    (->> menu
         (.buildFromTemplate Menu)
         (.setApplicationMenu Menu))))

(defn reload []
  (when-not (nil? *main-window)
    (u/relaunch)
    (u/quit)))

(defn create-window []
  (reset! *main-window (u/browser-window {:width     300
                                          :height    500
                                          :resizable false}))
  (u/load-url @*main-window "index.html")
  (u/on @*main-window "closed" #(reset! *main-window nil))
  (default-menu))

(defn activate []
  (when (nil? @*main-window)
    (create-window)))

(defn -main []
  (pprint "Starting Main")
  (.on app "window-all-closed" #(when-not (= js/process.platform "darwin") (.quit app)))
  (.on app "ready" create-window)
  (.on app "activate" activate))

(set! *main-cli-fn* -main)