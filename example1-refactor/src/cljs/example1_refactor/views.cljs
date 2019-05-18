(ns example1-refactor.views)

(defn loading
  "加载信息显示模块"
  [{:keys [loading-info]}]
  [:div.loading
   loading-info])

(defn user-info
  "用户信息展示模块"
  [{:keys [username user-balance]} dispatch]
  [:div.user-info
   [:h3 "welcome " username]
   [:h3 "当前账户余额：" user-balance]
   [:div {:on-click #(dispatch [:events/query-user-balance username])}
    (str "点击查询" username "的账户")]])

(defn click-count
  "点击事件展示模块"
  [{:keys [click-cnt]} dispatch]
  [:div
   [:h3 "当前点击数：" click-cnt]
   [:div.cnt
    {:on-click #(dispatch [:events/cnt-plus 2])}
    "点我+2"]])

(defn lucky-draw
  "抽奖展示模块"
  [{:keys [lucky-draw-result]} dispatch]
  [:div.lucky-draw
   [:h3 "中奖结果：" lucky-draw-result]
   [:div
    {:on-click #(dispatch [:events/lucky-draw])}
    "点我抽奖"]])

(defn main-panel
  "页面显示主面板"
  [{:keys [loading-info] :as page-info} dispatch]
  (if loading-info
    (loading page-info)
    [:div
     (user-info page-info dispatch)
     (click-count page-info dispatch)
     (lucky-draw page-info dispatch)]))
