(ns status-im.ui.screens.wallet.buy-crypto.sheets
  (:require [status-im.ui.components.colors :as colors]))

(def banner-container {:margin-horizontal 16
                       :flex-direction :row
                       :justify-content :space-between
                       :align-items :center
                       :flex 1
                       :margin-top 16
                       :border-radius 16
                       :margin-bottom 8
                       :padding-horizontal 12
                       :padding-vertical 5
                       :background-color colors/blue-light})

(def highlight-container
  {:padding 4
   :justify-content :center
   :border-radius 4
   :background-color colors/blue})

(def highlight-text
  {:text-transform :uppercase
   :color "#FFFFFF"})

(def icon
  {:width 68
   :height 36})
