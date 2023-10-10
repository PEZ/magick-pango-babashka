#!/usr/bin/env -S bb -cp ./bin
(ns compose
  (:require [babashka.fs :as fs]
            [babashka.process :as p]))

(defn compose! [output-path]
  (when-let [output-dir (-> output-path fs/file fs/parent)]
    (fs/create-dirs output-dir))
  (p/sh "montage"
        "assets/ImageMagick.png" "assets/pango-name.png" "assets/babashka.png"
        "-geometry" "+0+0" "-background" "white" "-tile" "x1"
        "-mode" "Concatenate" output-path)
  (p/sh "convert" output-path "-gravity" "Center" "-pointsize" "36" "-annotate" "+0-100"
        "pango:<span weight='bold'>ImageMagick + Pango + Babashka</span>\nA Docker image to power your pictures + text compositions."
        output-path))

(comment
  (compose! "output/composition.png")
  )

(if (not= *file* (System/getProperty "babashka.file"))
  (println "Use the REPL, Padawan")
  (if-let [[output-path] *command-line-args*]
    (compose! output-path)
    (println "Usage: ./examples/compose <output-file>")))
