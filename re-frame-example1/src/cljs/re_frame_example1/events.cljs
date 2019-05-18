(ns re-frame-example1.events
  (:require
   [re-frame.core :as re-frame]
   [re-frame-example1.db :as db]
   ))

;; reg-event-db：注册db处理事件
(re-frame/reg-event-db
 ::initialize-db
 (fn [db event]
   ;; 返回新的db
   db/default-db))

;; ----- 修改db状态，驱动页面变化 -----
;; coeffects：一个大map，里面包含了db(app state)，以及其他数据源，相当于给了你整个世界。
;; event：事件数据
;; 返回值：
;      返回一个或多个effect，事件的处理结果是要改变db，则返回{:db new-db}
(re-frame/reg-event-fx
  ::cnt-plus                                                ;; event-id
  (fn [{:keys [db] :as coeffects} [event-id delta]]
    {:db (update db :cnt + delta)}))

;; ------ 需要调用第三方（如http服务）-----
(re-frame/reg-event-fx
  ::query-user-balance
  (fn [{:keys [db] :as coeffects} [_ username]]
    {:db (assoc db :loading-info (str "正在加载 " username "的账户信息，请稍等..."))
     :request {:event :query-user-balance
               :params {:username username}
               :resp-event ::user-balance-resp}}))

(re-frame/reg-event-fx
  ::user-balance-resp
  (fn [{:keys [db] :as coeffects} [_ balance]]
    {:db (assoc db :loading-info nil
                   :user-balance balance)}))

(re-frame/reg-event-fx
  ::lucky-draw
  [(re-frame/inject-cofx :random-num)]
  (fn [{:keys [db random-num] :as coeffects} [_]]
    (cond
      (<= 0 random-num 3)
      {:db (assoc db :lucky-draw-result "恭喜您中了3等奖！")}
      (<= 4 random-num 6)
      {:db (assoc db :lucky-draw-result "恭喜您中了2等奖！!")}
      (<= 7 random-num 8)
      {:db (assoc db :lucky-draw-result "恭喜您中了1等奖！!!")}
      :default
      {:db (assoc db :lucky-draw-result "很遗憾，您没有中奖！")}
      )))


;;==========================================================
;; 注册效果处理器
;; :db 有re-frame默认效果处理器
;; :request 为自定义效果处理器，负责远程请求

(re-frame/reg-fx
  :request
  (fn [{:keys [event params resp-event] :as request-data}]
    (case event
      :query-user-balance
      (js/setTimeout #(re-frame/dispatch [resp-event 20000]) 5000))))


;;==========================================================
;; 注册其他数据源
;;  如：随机数
;;     当前时间

;; 测试的时候，可以只改这里，输入固定值
(re-frame/reg-cofx
  :random-num
  (fn [coeffects _]
    (assoc coeffects :random-num (rand-int 10))))


