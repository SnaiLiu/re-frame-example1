(ns re-frame-example1.views
  (:require
   [re-frame.core :as re-frame]
   [re-frame-example1.subs :as subs]
   [re-frame-example1.events :as events]
   ))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        username (re-frame/subscribe [::subs/username])
        click-cnt (re-frame/subscribe [::subs/click-cnt])
        loading (re-frame/subscribe [::subs/loading])
        user-balance (re-frame/subscribe [::subs/user-balance])
        lucky-draw-result (re-frame/subscribe [::subs/lucky-draw-result])]
    (if @loading
      [:div @loading]
      [:div
       [:div "===============普通示例=============="]
       [:h1 "Hello from" @name]
       [:div "===============reg-sub 示例 ========="]
       [:h2 "welcome " @username]
       [:div "===============普通事件处理，改变db==========="]
       [:h3 "当前点击数：" @click-cnt]
       [:div.cnt
        {:on-click #(re-frame.core/dispatch [::events/cnt-plus 2])}
        "点我+2"]

       [:br]
       [:div "===============远程调用效果处理示例============="]
       [:div.balance
        [:h3 "当期账户余额:  " @user-balance]]
       [:div.http
        {:on-click #(re-frame.core/dispatch [::events/query-user-balance "snailiu"])}
        "点击查询snailiu的账户"]

       [:div "===============其他数据源注入的示例=============="]
       [:h3 "中奖结果：" @lucky-draw-result]
       [:div
        {:on-click #(re-frame.core/dispatch [::events/lucky-draw])}
        "点我抽奖"]])))
