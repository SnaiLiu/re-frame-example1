(ns example1-refactor.db)

(def default-db
  {:name "re-frame1"
   :first-name "liu"
   :middle-name "&"
   :last-name "zhen"
   :cnt 0
   ;; 正在加载信息...
   :loading-info nil
   :user-balance 0
   :lucky-draw-result nil
   })

(def db-sub-map
  "订阅数据时订阅的ID对应的数据处理函数
  数据处理函数的输入为db"
  {:subs/name              :name
   :subs/username          (fn [{:keys [first-name middle-name last-name] :as db}]
                             (str first-name middle-name last-name))
   :subs/click-cnt         :cnt
   :subs/loading-info      :loading-info
   :subs/user-balance      :user-balance
   :subs/lucky-draw-result :lucky-draw-result})
