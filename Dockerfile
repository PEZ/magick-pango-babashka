# Start from a place of civilized shell scripting: https://babashka.org/
FROM babashka/babashka

RUN apt-get update && apt-get install -y imagemagick libpango1.0-dev

ENV PATH="/usr/bin:$PATH"
