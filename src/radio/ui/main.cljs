(ns radio.ui.main
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [goog.dom :refer [getElement]]
            [radio.data :as data]
            [radio.util :refer [on-ipc get-url]]
            [cljs.pprint :refer [pprint]]))

(enable-console-print!)

(defonce *state
  (reagent/atom {:flipped? false
                 :data {}}))

(defn start []
  (swap! *state assoc :started? true)
  (pprint "Start"))

(defn stop []
  (swap! *state assoc :started? false)
  (pprint "Stop"))

(defn header []
  [:header.header
   [:h1.header__text "Radio Time Header!"]])

(defn button [{:keys [on-click]} & children]
  [:button.button
   {:type     "button"
    :on-click on-click}
   children])

;;; Actions
(defn toggle-screen
  "Changes the focused screen between the main and config screen"
  [e]
  (let [flipped? (:flipped? @*state)]
    (.preventDefault e)
    (if flipped?
      (swap! *state assoc :flipped? false)
      (swap! *state assoc :flipped? true))))

(defn main-screen [{:keys [toggle start stop time started?]}]
  [:div.screen__main.screen__state
   [header]
   [button
    {:on-click (if started? stop start)}
    (if started? "Stop Pomodoro" "Start Pomodoro")]
   [:button.link {:type     "button"
                  :on-click toggle} "settings"]])

;;; Application Component
(defn radio[]
  (let [state    @*state
        started? (:started? state)]
    [:div.container
     [:div {:class (str
                    "screen"
                    (when (:flipped? state) " screen--flipped"))}
      [main-screen
       {:toggle   toggle-screen
        :start    start
        :stop     stop
        :started? started?
        :time     (:time state)}]]]))

(def app (with-meta radio
           {:component-did-mount #(swap! *state assoc :data (data/read))}))

(defn -main []
  (pprint "Starting Application")
  (reagent/render
    [app]
    (getElement "radio"))
    [:div.screen__main.screen__state
      [:header.header
      [:h1.header__text "Radio!"]]])

(set! *main-cli-fn* -main)