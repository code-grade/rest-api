#!/usr/bin/sh

./mvnw build

mkdir -p target/dependency || exit
cd target/dependency || exit
jar -xf ../*.jar

cd ../../
docker build -t cseweb/backend-rest-api .