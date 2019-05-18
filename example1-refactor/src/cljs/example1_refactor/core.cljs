(ns example1-refactor.core
  (:require
    [reagent.core :as reagent]

    [example1-refactor.db :as db]
    [example1-refactor.events :as events]
    [example1-refactor.views :as views]
    [example1-refactor.config :as config]
    [example1-refactor.db :as db]
    [example1-refactor.effects :as effects]
    [re-frame.core :as rf]))

;;===========================
;; 集成
(def ctx-map
  {:random-num (fn [] (rand-int 10))
   :now        (fn [] (js/Date.now))})

(defn main-page
  "显示层集成"
  []
  (views/main-panel
    {:name              @(rf/subscribe [:subs/name])
     :username          @(rf/subscribe [:subs/username])
     :click-cnt         @(rf/subscribe [:subs/click-cnt])
     :loading-info      @(rf/subscribe [:subs/loading-info])
     :user-balance      @(rf/subscribe [:subs/user-balance])
     :lucky-draw-result @(rf/subscribe [:subs/lucky-draw-result])}
    rf/dispatch))

(defn init-sys
  "构建系统"
  [init-db sub-data-map event-handler-map effect-handler-map ctx-map]
  ;; 初始化app-data
  (rf/reg-event-db :init-db (fn [_ _] init-db))
  (rf/dispatch-sync [:init-db])
  ;; 注册数据订阅函数
  (doseq [[sub-id sub-fn] sub-data-map]
    (rf/reg-sub sub-id sub-fn))
  ;; 注册其他上下文需要的数据，如now random-num
  (doseq [[ctx-id ctx-fn] ctx-map]
    (rf/reg-cofx
      ctx-id
      (fn [coeffects _]
        (assoc coeffects ctx-id (ctx-fn)))))
  ;; 注册事件处理函数
  (let [ctxs (map #(rf/inject-cofx (first %)) ctx-map)]
    (doseq [[event-id hander-fn] event-handler-map]
      (rf/reg-event-fx
        event-id
        ctxs
        (fn [app-data [_ & event-args]]
          (apply hander-fn app-data event-args)))))
  ;; 注册effect处理函数
  (doseq [[effect-id effect-handler-fn] effect-handler-map]
    (rf/reg-fx effect-id effect-handler-fn)))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (rf/clear-subscription-cache!)
  (reagent/render [main-page]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (init-sys db/default-db
            db/db-sub-map
            events/event-handler-map
            effects/effects-map
            ctx-map)
  (dev-setup)
  (mount-root))
