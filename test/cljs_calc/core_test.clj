(ns cljs-calc.core-test
  (:use [midje.sweet])
  (:require [cljs-calc.core :as core]))


  (facts "Adder calculates the sum of two numbers"
       (fact "1+1 = 2"
             (core/adder 1 1) => 2)
       (fact "2+2 = 4"
             (core/adder 2 2) => 4)
       (fact "0+0 = 0"
             (core/adder 0 0) => 0))