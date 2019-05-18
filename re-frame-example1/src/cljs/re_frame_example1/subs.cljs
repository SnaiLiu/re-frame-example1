(ns re-frame-example1.subs
  "从db中订阅数据"
  (:require
   [re-frame.core :as re-frame]))


(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

;; reg-sub ：re-frame提供的订阅数据的接口
;; 为你订阅的数据起一个ID（别名）
;; 数据处理的逻辑，因为db中存储的数据不一定是上层需要的形式，可以通过处理函数来封装
(re-frame/reg-sub
  ::username
  (fn [{:keys [first-name middle-name last-name] :as db}]
    (str first-name middle-name last-name)))

(re-frame/reg-sub
  ::click-cnt
  (fn [db]
    (:cnt db)))

(re-frame/reg-sub
  ::loading
  (fn [db]
    (:loading-info db)))

(re-frame/reg-sub
  ::user-balance
  (fn [db]
    (:user-balance db)))

(re-frame/reg-sub
  ::lucky-draw-result
  (fn [db]
    (:lucky-draw-result db)))
