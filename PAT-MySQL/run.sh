#!/bin/bash
# Compila y ejecuta por terminal (alternativa al botón Run)
sudo service mysql start 2>/dev/null
mkdir -p bin
javac -encoding UTF-8 -cp "lib/*" -d bin src/*.java && java -cp "bin:lib/*" Main
