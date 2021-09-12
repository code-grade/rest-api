#!/usr/bin/sh

mkdir -p target/dependency || exit
cd target/dependency || exit
jar -xf ../*.jar

cd ../../