#!/usr/bin/sh

./mvnw package

mkdir -p target/dependency || exit
cd target/dependency || exit
jar -xf ../*.jar

cd ../../
docker build -t code-grade/backend-rest-api .