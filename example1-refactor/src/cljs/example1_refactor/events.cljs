(ns example1-refactor.events
  (:require
   [re-frame.core :as re-frame]
   [example1-refactor.db :as db]
   ))

(defn cnt-plus
  "计数器加"
  [{:keys [db] :as app-data} delta]
  {:db (update db :cnt + delta)})

(defn query-user-balance
  "查询玩家账户余额"
  [{:keys [db] :as app-data} username]
  {:db (assoc db :loading-info (str "正在加载 " username "的账户信息，请稍等..."))
   :request {:event :query-user-balance
             :params {:username username}
             :resp-event :events/user-balance-resp}})

(defn user-balance-resp
  "处理玩家账户余额查询结果"
  [{:keys [db] :as app-data} balance]
  {:db (assoc db :user-balance balance
                 :loading-info nil)})

(defn lucky-draw
  "抽奖事件处理"
  [{:keys [db random-num] :as app-data}]
  (cond
    (<= 0 random-num 3)
    {:db (assoc db :lucky-draw-result "恭喜您中了3等奖！")}
    (<= 4 random-num 6)
    {:db (assoc db :lucky-draw-result "恭喜您中了2等奖！!")}
    (<= 7 random-num 8)
    {:db (assoc db :lucky-draw-result "恭喜您中了1等奖！!!")}
    :default
    {:db (assoc db :lucky-draw-result "很遗憾，您没有中奖！")}))

(def event-handler-map
  "事件处理函数与事件id映射map"
  {:events/cnt-plus cnt-plus
   :events/query-user-balance query-user-balance
   :events/lucky-draw lucky-draw
   :events/user-balance-resp user-balance-resp})
