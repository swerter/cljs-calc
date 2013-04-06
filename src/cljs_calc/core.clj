(ns cljs-calc.core
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defn adder [a b]
  (+ a b))

;; defroutes macro defines a function that chains individual route
;; functions together. The request map is passed to each function in
;; turn, until a non-nil response is returned.
(defroutes app-routes
  ; to serve document root address
  (GET "/" [] (str "<p>Calculating: 2 + 2 = " (adder 2 2) "</p>"))
  ; to serve static pages saved in resources/public directory
  (route/resources "/")
  ; if page is not found
  (route/not-found "Page not found"))

;; site function creates a handler suitable for a standard website,
;; adding a bunch of standard ring middleware to app-route:
(def handler
  (handler/site app-routes))
