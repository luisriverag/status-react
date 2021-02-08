(ns status-im.ui.screens.wallet.buy-crypto.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [status-im.i18n :as i18n]
            [status-im.ui.components.webview :as components.webview]
            [status-im.ui.screens.wallet.buy-crypto.sheets :as sheets]
            [status-im.ui.components.icons.icons :as icons]
            [status-im.ui.screens.chat.photos :as photos]
            [quo.core :as quo]
            [status-im.ui.components.list.views :as list]
            [status-im.ui.components.react :as react]
            [status-im.ui.components.topbar :as topbar])
  (:require-macros [status-im.utils.views :as views]))

(def learn-more-url "https://our.status.im/")

(defn on-buy-crypto-pressed []
  (re-frame/dispatch [:navigate-to :buy-crypto]))

(defn render-on-ramp [{:keys [name fees region logo-url description] :as on-ramp}]
  [react/touchable-highlight {:on-press #(re-frame/dispatch [:navigate-to :buy-crypto-website on-ramp])
                              :style {:flex 1}}
   [quo/list-item
    {:title          [react/view {:style {:flex 1}}
                      [quo/text {:size :large
                                 :weight :bold}
                       name]
                      [quo/text {} description]]
     :subtitle       [react/view {:style {:flex 1}}
                      [quo/text {:size :small
                                 :color :secondary} fees]
                      [quo/text {:size :small
                                 :color :secondary} region]]
     :icon           [photos/photo logo-url {:size 40}]
     :left-side-alignment :flex-start
     :accessory      :text}]])

(defn buy-crypto-header []
  [react/view {:align-items :center}
   [react/view {:padding-vertical 16}
    [quo/text {:weight :bold
               :size :x-large}
     (i18n/label :t/buy-crypto)]]
   [quo/text {:color :secondary}
    (i18n/label :t/buy-crypto-choose-a-service)]
   [react/touchable-highlight {:on-press #(re-frame/dispatch [:browser.ui/open-url learn-more-url])}
    [react/view {:padding-vertical 11}
     [quo/text {:color :link} (i18n/label :learn-more)]]]])

(views/defview buy-crypto []
  (views/letsubs [on-ramps [:buy-crypto/on-ramps]]
    [react/view {:flex 1}
     [topbar/topbar {:modal? true}]
     [quo/separator]
     [list/flat-list {:data               on-ramps
                      :key-fn             :site-url
                      :header             [buy-crypto-header]
                      :render-fn          render-on-ramp}]]))

(defn website [route]
  (let [has-loaded? (reagent/atom false)
        {:keys [name
                hostname
                logo-url
                site-url]} (get-in route [:route :params])]
    (fn []
      [react/view {:flex 1}
       [topbar/topbar {:content [react/view {:flex 1
                                             :align-items :center
                                             :justify-content :center}
                                 [quo/text
                                  {:weight :semi-bold}
                                  (i18n/label :t/buy-crypto)]
                                 [quo/text {:color :secondary}
                                  hostname]]
                       :modal? true}]
       (when-not @has-loaded?
         [react/view {:style {:flex 1
                              :position :absolute
                              :top 0
                              :left 0
                              :right 0
                              :bottom 0
                              :align-items :center
                              :justify-content :center}}
          [photos/photo logo-url {:size 40}]
          [quo/text
           {:size :x-large}
           (i18n/label :t/opening-buy-crypto {:site name})]
          [react/view {:style {:padding-horizontal 32}}
           [quo/text {:align :center
                      :color :secondary}
            (i18n/label :t/buy-crypto-leaving)]]])
       [components.webview/webview
        {:onLoadEnd #(reset! has-loaded? true)
         ;; NOTE: without this it crashes on android 11
         :androidHardwareAccelerationDisabled true
         :containerStyle (when-not @has-loaded? {:opacity 0})
         :source {:uri site-url}}]])))

(defn container []
  (reagent/create-class
   {:component-did-mount #(re-frame/dispatch [:buy-crypto.ui/loaded])
    :reagent-render buy-crypto}))

(defn banner []
  [react/touchable-highlight {:on-press on-buy-crypto-pressed}
   [react/view {:style sheets/banner-container}
    [react/view {:flex-direction :row}
     [react/view {:style sheets/highlight-container}
      [quo/text {:weight :bold
                 :size :tiny
                 :style sheets/highlight-text}
       (i18n/label :t/new)]]
     [react/view {:style {:justify-content :center
                          :align-items :center
                          :padding-left 5}}
      [quo/text {:size :large
                 :weight :medium
                 :color :link} (i18n/label :t/buy-crypto)]]]
    [react/view {:style {:align-content :flex-end
                         :align-self :flex-end}}
     [react/image {:source (icons/icon-source :buy-crypto)
                   :style sheets/icon}]]]])
