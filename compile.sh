#!/bin/bash

BASE_COMPILE_CODE="javac -d bin -cp src"
MAIN_PROGRAM="bin/main/Main"
mkdir bin
for package in `echo src/*`; do
  for module in $(ls $package); do
    $BASE_COMPILE_CODE $package'/'$module
  done
done  
