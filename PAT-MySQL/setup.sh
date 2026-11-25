#!/bin/bash
# 1) Instala MySQL, 2) lo inicia, 3) crea la base de datos y el usuario, 4) descarga el driver JDBC
set -e

echo ">> Instalando MySQL Server..."
sudo apt-get update -y
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y mysql-server

echo ">> Iniciando MySQL..."
sudo service mysql start
sleep 5

echo ">> Creando base de datos pat_db y usuario pat..."
sudo mysql -e "CREATE DATABASE IF NOT EXISTS pat_db CHARACTER SET utf8mb4;
CREATE USER IF NOT EXISTS 'pat'@'localhost' IDENTIFIED BY 'pat123';
GRANT ALL PRIVILEGES ON pat_db.* TO 'pat'@'localhost';
FLUSH PRIVILEGES;"

echo ">> Descargando driver JDBC de MySQL..."
mkdir -p lib
JAR="lib/mysql-connector-j-8.0.33.jar"
if [ ! -f "$JAR" ]; then
  wget -q -O "$JAR" https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
fi

echo ">> Listo."
ls -lh lib
