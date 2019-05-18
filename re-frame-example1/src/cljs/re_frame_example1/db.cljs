(ns re-frame-example1.db)


;; db，保存当前app 状态 state
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
