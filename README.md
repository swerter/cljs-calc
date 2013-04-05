# cljs-calculator

This is a short version of the excellent tutorial 
[modern-cljs](https://github.com/magomimmo/modern-cljs)
```bash
git clone ...
```
The main differences are that it is a shorter version and that it contains also 
a testing tutorial. It shows how to setup the testing environment.

## Usage

If you simply want to see the final application running:
lein repl

There is also a consular file (brew install consular on the mac). Thus
you can also simply run 
```bash
consular start Termfile
``` 
if you have installed consular.


## Introduction

As an example of how such a clojurescript application can be set up
we use develop here a calculator that calculates the multiplication of two
numbers. This is a simple example but has all the elements of a bigger 
application: there are input elements, there is an output, and the calculation
can be tested.

## Disclaimer
I am in no means an expert in clojure or clojurescript. What I am describing
here is simply things that helped me.

## Let's start with a ring server

First we need to setup the project
```bash
lein new cljs-calc
cd cljs-calc
```

Now let's start with clojure server, that simply serves a static html page. For
that we need to add the [ring]https://github.com/ring-clojure/ring() plugin. The 
ring plugin is an API to the http level.

In addition we also add the [compojure](https://github.com/weavejester/compojure.git)
to the stack, which is a routing library for the ring server.

Finally the project.cljs file should look like this:
```clojure
  (defproject cljs-calc "0.1.0-SNAPSHOT"
    :description "FIXME: write description"
    :url "http://example.com/FIXME"
    :license {:name "Eclipse Public License"
              :url "http://www.eclipse.org/legal/epl-v10.html"}
    :dependencies [[org.clojure/clojure "1.4.0"]
                   [compojure "1.1.5"]]
    :plugins [;; ring plugin
              [lein-ring "0.8.3"]]

              ;; ring tasks configuration
              :ring {:handler cljs-calc.core/handler}
    )
```

We also need to setup the routes in the core.clj. Update the file
`src/cljs_calc/core.clj` and fill in the following:

```clojure
(ns cljs-calc.core
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

;; defroutes macro defines a function that chains individual route
;; functions together. The request map is passed to each function in
;; turn, until a non-nil response is returned.
(defroutes app-routes
  ; to serve document root address
  (GET "/" [] "<p>Hello from compojure</p>")
  ; to serve static pages saved in resources/public directory
  (route/resources "/")
  ; if page is not found
  (route/not-found "Page not found"))

;; site function creates a handler suitable for a standard website,
;; adding a bunch of standard ring middleware to app-route:
(def handler
  (handler/site app-routes))
```

When you now start the ring server with
```bash
lein run server
```
you will see a browser window opening and the text appearing 
"Hello from compojure".
(If the browser window did not open you can manually enter the url:
[http://localhost:3000](http://localhost:3000))

## Add tests for the clojure code

In best practice you should start with the tests. But as we are still the 
setup phase I only add tests now. 



## License

Copyright © 2013 Michael J. Bruderer

Distributed under the Eclipse Public License, the same as Clojure.
