#!/usr/bin/env -S bb -cp ./examples
(ns compose
  (:require [babashka.fs :as fs]
            [babashka.process :as p]))

(defonce compose-tmp (name (gensym "compose-")))

(defn compose! [output-path]
  (let [output-dir (-> output-path fs/file fs/parent)]
    (when output-dir
      (fs/create-dirs output-dir))
    (let [tmp-dir (fs/path (fs/temp-dir) compose-tmp)
          pango-path (str (fs/path tmp-dir "pango.png"))
          montage-1-path (str (fs/path tmp-dir "montage-1.png"))
          montage-2-path (str (fs/path tmp-dir "montage-2.png"))]
      (fs/create-dirs tmp-dir)
      (println "Writing pango output to: " pango-path)
      (p/sh "convert" "-background" "white" "-size" "1140x"
            (str "pango:<span font_size='36000'>"
                 "<b>"
                 "<span foreground='#9370DB'>cospaia</span>"
                 " / magick-pango-babashka<tt>:latest</tt></b></span>"
                 "\n"
                 "<span font_size='28000'>"
                 "A Docker image to power your pictures + text "
                 "compositions.</span>"
                 "\n\nExample:\n"
                 "<tt>docker run -v \"$(pwd)\":/work -w /work "
                 "cospaia/magick-pango-babashka examples/compose.clj "
                 "output/composition.png</tt>")
            "-bordercolor" "white" "-border" "30"
            pango-path)
      (println "Writing first montage output to: " montage-1-path)
      (p/sh "montage" "-resize" "350x"
            "examples/assets/babashka.png"
            "examples/assets/ImageMagick.png"
            "examples/assets/pango-name.png"
            "-geometry" "+0+0" "-gravity" "center"
            "-background" "white" "-tile" "x1"
            "-mode" "Concatenate"
            montage-1-path)
      (println "Writing second montage output to: " montage-2-path)
      (p/sh "convert" montage-1-path
            "-gravity" "north" "-extent" "1200x383+0-20"
            montage-2-path)
      (println "Writing result output to: " output-path)
      (p/sh "montage" montage-2-path pango-path
            "-tile" "x2" "-mode" "concatenate"
            output-path))))

(comment
  (compose! "output/composition.png")
  :rcf)

(if (not= *file* (System/getProperty "babashka.file"))
  (println "Use the REPL, Padawan")
  (if-let [[output-path] *command-line-args*]
    (compose! output-path)
    (println "Usage: ./examples/compose <output-file>")))
