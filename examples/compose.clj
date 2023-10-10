#!/usr/bin/env -S bb -cp ./bin
(ns compose
  (:require [babashka.fs :as fs]
            [babashka.process :as p]))

(defn compose! [output-path]
  (let [output-dir (-> output-path fs/file fs/parent)]
    (when output-dir
      (fs/create-dirs output-dir))
    (let [tmp-sub-dir (name (gensym "compose-"))
          pango-output (str (fs/path (fs/temp-dir) tmp-sub-dir "pango.png"))
          first-montage (str (fs/path (fs/temp-dir) tmp-sub-dir "montage1.png"))
          second-montage (str (fs/path (fs/temp-dir) tmp-sub-dir "montage2.png"))]
      (println "Writing pango output to: " pango-output)
      (p/sh "convert"
            "-background" "white"
            "-size" "1140x"
            (str "pango:<span weight='bold' font_size='36000'>"
                 "ImageMagick + Pango + Babashka</span>"
                 "\n"
                 "<span font_size='28000'>"
                 "A Docker image to power your pictures + text "
                 "compositions.</span>")
            "-bordercolor" "white" "-border" "30"
            pango-output)
      (println "Writing first montage output to: " first-montage)
      (p/sh "montage" "-resize" "380x"
            "assets/ImageMagick.png" "assets/pango-name.png" "assets/babashka.png"
            "-geometry" "+0+0" "-gravity" "center"
            "-background" "white" "-tile" "x1"
            "-mode" "Concatenate"
            first-montage)
      (println "Writing first montage output to: " second-montage)
      (p/sh "convert" first-montage
            "-gravity" "north" "-extent" "1200x440+0-20"
            second-montage)
      (println "Writing result output to: " output-path)
      (p/sh "montage" second-montage pango-output
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
