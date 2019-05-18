(ns example1-refactor.effects
  "效果处理模块"
  (:require [re-frame.core :as re-frame]))

(defn request-handler
  "处理request"
  [{:keys [event params resp-event] :as request-data}]
  (case event
    :query-user-balance
    (js/setTimeout #(re-frame/dispatch [resp-event 20000]) 5000)))

(def effects-map
  "效果处理映射"
  {:request request-handler})
