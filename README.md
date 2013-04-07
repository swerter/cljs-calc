# cljs-calculator

This is a short version of the excellent tutorial 
[modern-cljs](https://github.com/magomimmo/modern-cljs)

To get things running:
```bash
git clone https://github.com/swerter/cljs-calc.git
cd cljs-calc
```

To run the web server:

```bash
lein ring server
```


The main differences are that it is a shorter version and that it contains also 
a testing tutorial. It shows how to setup the testing environment.

## Usage

If you simply want to see the final application running:
lein repl

There is also a [Consular](https://github.com/achiu/consular) file. If you
have consular installed you can also simply run 
```bash
consular start Termfile
``` 
and server, autotests in clojure and clojurescript, repl, and cljsrepl start up
each in its own tab.


## Introduction

As an example of how such a clojurescript application can be set up
we use develop here a calculator that calculates the multiplication of two
numbers. This is a simple example but has all the elements of a bigger 
application: there are input elements, there is an output, and the calculation
can be tested.

## Let's start with a ring server

First we need to setup the project
```bash
lein new cljs-calc
cd cljs-calc
```

Now let's start with clojure server, that simply serves a static html page. For
that we need to add the [ring](https://github.com/ring-clojure/ring) plugin. The 
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
setup phase I only add tests now. First we need to add the 
[midje](https://github.com/marick/Midje.git) dependency. Midje is a testing
framework for clojure. We also add the plugin
[lein-midje](https://github.com/marick/lein-midje.git) to simplify running
our tests. 

Our `project.clj` now looks like this:
```clojure
(defproject cljs-calc "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [midje "1.5.0"]]
  :plugins [;; ring plugin
            [lein-ring "0.8.3"]
            [lein-midje "3.0.1"]]

            ;; ring tasks configuration
            :ring {:handler cljs-calc.core/handler}
  )

```

Now that the testing setup is done, let's create a function that adds
two numbers together. Very simple, I know, nothing of much use. But the 
purpose of this project is to show how to setup clojure and clojurescript, 
not how to build astronomical calculations. 

Replace the automatically generated tests `test/cljs_calc/core_test.clj` with
the following (take care with the namespace section: the namespace needs to
have the same name as the file, but with the underscore replaced with a dash):

```clojure
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
```

Running the tests with `lein midje` should return an error like
`Exception in thread "main" java.lang.RuntimeException: No such var: core/adder...`
because we do yet have implemented the adder function. Add the `adder` function
to `src/cljs-calc/core.cljs`:

```clojure
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
```

Run again `lein midje` and now you should see 
in green `All claimed facts (3) have been confirmed.`. 

If you are like me you prefer to have the testing done automatically
whenever you change a source or test file. To do so open a new
window in the terminal and run:

```bash
lein midje --lazytest
```

If you still have your ring server running and reload the page you should now
see our beautiful calculation of 2 + 2 = 4. If not, start the ring server
again with:
```bash
lein run server
```

## Intermezzo: Use consular to manage all these terminals

In the meantime we already have at least two terminal instances open: one
for the ring server and the other with the midje autotest. If you are
like me you also have the repl open to try out things. That makes already
three terminal instances. To always open all these instances whenever I continue
working on the project is a pain. Lucky for us there is help: 
[Consular](https://github.com/achiu/consular). To install consular you run
```bash
gem install consular
```

Afterwards, you need to add the core for the terminal program you use. For
instance, for the mac os x terminal
```bash
gem install consular-osx
```

In case you use iTerm run
```bash
gem install consular-iterm
```

Finally, you also need to load the terminal driver by running
```bash
consular init
```
which should generate a configure file loading the correct driver for your
teminal. In case of difficulties check the homepage of
[Consular](https://github.com/achiu/consular).

Now that we have consular installed, we need to write a Termfile to configure
what to start. Generate a file `Termfile.term` with the following content:
```ruby
curdir = Dir.pwd

tab "Ring server" do
    run "cd #{curdir}"
    run "lein ring server"
  end
  tab "clj autotest" do
    run "cd #{curdir}"
    run "lein midje --lazytest"
  end
  tab "Repl" do
    run "cd #{curdir}"
    run "lein repl"
  end
end
```





## License

Copyright © 2013 Michael J. Bruderer

Distributed under the Eclipse Public License, the same as Clojure.
