# An ImageMagick + Pango Docker image

A Docker image with:

* ImageMagick
* Pango
* [Babashka](https://babashka.org). (Wether you use it is up to you. It's wonderful.)

## Usage

Here's an example from https://imagemagick.org/Usage/text/

``` sh
docker run --rm -v $(pwd):/output cospaia/magick-pango-babashka convert -background lightblue pango:"Anthony Thyssen" /output/pango.gif
```

Note that we use the `convert` command here, where the original example uses `magick`. This is because ImageMagick < 7 didn't have the `magick` command. See about versions below.

## Versions

These are the versions reported on my machine:

``` sh
$ docker run cospaia/magick-pango-babashka convert -version
Version: ImageMagick 6.9.11-60 Q16 aarch64 2021-01-25 https://imagemagick.org
Copyright: (C) 1999-2021 ImageMagick Studio LLC
License: https://imagemagick.org/script/license.php
Features: Cipher DPC Modules OpenMP(4.5) 
Delegates (built-in): bzlib djvu fftw fontconfig freetype heic jbig jng jp2 jpeg lcms lqr ltdl lzma openexr pangocairo png tiff webp wmf x xml zlib

$ docker run cospaia/magick-pango-babashka pango-view --version
pango-view (pango) 1.50.6
```

Note. The image is based on Ubuntu, which as of this writing only has packages for ImageMagick version 6.9.

## License

MIT. See [license file](LICENSE).